/*
 * Copyright (c) 2006 - 2009 Open Source Strategies, Inc.
 *
 * Opentaps is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Opentaps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Opentaps.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.opentaps.common.event;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.opentaps.common.agreement.UtilAgreement;
import org.opentaps.common.party.PartyHelper;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.common.util.UtilMessage;
import org.opentaps.domain.DomainsLoader;
import org.opentaps.domain.base.entities.GoodIdentification;
import org.opentaps.domain.base.entities.OrderItemShipGrpInvRes;
import org.opentaps.domain.base.entities.Product;
import org.opentaps.domain.base.entities.ProductFacilityLocation;
import org.opentaps.domain.base.entities.SupplierProduct;
import org.opentaps.domain.purchasing.PurchasingRepositoryInterface;
import org.opentaps.foundation.entity.hibernate.Query;
import org.opentaps.foundation.entity.hibernate.Session;
import org.opentaps.foundation.exception.FoundationException;
import org.opentaps.foundation.infrastructure.Infrastructure;
import org.opentaps.foundation.infrastructure.InfrastructureException;
import org.opentaps.foundation.repository.RepositoryException;

/**
 * Utility class for making Ajax JSON responses.
 * @author Chris Liberty (cliberty@opensourcestrategies.com)
 * @version $Rev$
 */
public final class AjaxEvents {

    private AjaxEvents() { }

    private static final String MODULE = AjaxEvents.class.getName();

    public static String doJSONResponse(HttpServletResponse response, JSONObject jsonObject) {
        return doJSONResponse(response, jsonObject.toString());
    }

    public static String doJSONResponse(HttpServletResponse response, Collection<?> collection) {
        return doJSONResponse(response, JSONArray.fromObject(collection).toString());
    }

    @SuppressWarnings("unchecked")
    public static String doJSONResponse(HttpServletResponse response, Map map) {
        return doJSONResponse(response, JSONObject.fromObject(map));
    }

    public static String doJSONResponse(HttpServletResponse response, String jsonString) {
        String result = "success";

        response.setContentType("application/x-json");
        try {
            response.setContentLength(jsonString.getBytes("UTF-8").length);
        } catch (UnsupportedEncodingException e) {
            Debug.logWarning("Could not get the UTF-8 json string due to UnsupportedEncodingException: " + e.getMessage(), MODULE);
            response.setContentLength(jsonString.length());
        }

        Writer out;
        try {
            out = response.getWriter();
            out.write(jsonString);
            out.flush();
        } catch (IOException e) {
            Debug.logError(e, "Failed to get response writer", MODULE);
            result = "error";
        }
        return result;
    }


    /*************************************************************************/
    /**                                                                     **/
    /**                      Common JSON Requests                           **/
    /**                                                                     **/
    /*************************************************************************/


    /** Gets a list of states (provinces) that are associated with a given countryGeoId. */
    public static String getStateDataJSON(HttpServletRequest request, HttpServletResponse response) {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        String countryGeoId = request.getParameter("countryGeoId");

        try {
            Collection<GenericValue> states = UtilCommon.getStates(delegator, countryGeoId);
            return doJSONResponse(response, states);
        } catch (GenericEntityException e) {
            return doJSONResponse(response, FastList.newInstance());
        }
    }

    /** Return agreement term list specific for given term type.
     * @throws GenericEntityException */
    @SuppressWarnings("unchecked")
    public static String getAgreementTermValidFieldsJSON(HttpServletRequest request, HttpServletResponse response) throws GenericEntityException {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        Locale locale = UtilHttp.getLocale(request.getSession());
        String termType = UtilCommon.getParameter(request, "termType");
        String itemId = UtilCommon.getParameter(request, "item");

        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put("item", itemId);

        List<String> validFields = UtilAgreement.getValidFields(termType, delegator);
        if (UtilValidate.isNotEmpty(validFields)) {

            resp.put("fields", validFields);

            // checks fields that require special handling
            for (String field : validFields) {
                if ("valueEnumId".equals(field)) {
                    // Enumeration. We should send enumeration name and list of values in response.
                    String enumTitle = (String) resp.get("enumTitle");
                    if (enumTitle != null) {
                        // Error. Only enumeration may be for given term type. Ignore.
                        Debug.logWarning("More than one enumeration value for term type " + termType + ". Ignore.", MODULE);
                        continue;
                    }

                    // Gets enumeration mapped to the term type
                    List<GenericValue> termTypeToEnumMaps = delegator.findByAnd("TermTypeToEnumTypeMap", UtilMisc.toMap("termTypeId", termType));
                    if (UtilValidate.isEmpty(termTypeToEnumMaps)) {
                        // Error. No mapping between term type and enum type.
                        Debug.logError("No mapping between term type " + termType + " and enumeration type.", MODULE);
                        continue;
                    }

                    // EnumerationType.description as field title
                    GenericValue termTypeToEnumMap = EntityUtil.getFirst(termTypeToEnumMaps);
                    enumTitle = (String) (termTypeToEnumMap.getRelatedOne("EnumerationType").get("description", "OpentapsEntityLabels", locale));
                    resp.put("enumTitle", UtilValidate.isNotEmpty(enumTitle) ? enumTitle : "Enumeration Value");

                    // Enumeration values
                    List<GenericValue> values = delegator.findByCondition("Enumeration", EntityCondition.makeCondition("enumTypeId", termTypeToEnumMap.getString("enumTypeId")), Arrays.asList("enumId", "description"), Arrays.asList("sequenceId"));
                    List<Map<String, String>> enumValues = new ArrayList<Map<String, String>>();
                    for (GenericValue value : values) {
                        Map<String, String> enumValue = new HashMap<String, String>();
                        enumValue.put("enumId", value.getString("enumId"));
                        enumValue.put("description", (String) value.get("description", "OpentapsEntityLabels", locale));
                        enumValues.add(enumValue);
                    }
                    resp.put("enumValues", enumValues);

                } else if ("currencyUomId".equals(field)) {

                    // Currency drop-down. Returns list of currencies.
                    List<GenericValue> currencies = UtilCommon.getCurrencies(delegator);
                    List<Map<String, String>> currencyValues = new ArrayList<Map<String, String>>();
                    for (GenericValue currency : currencies) {
                        Map<String, String> currencyValue = new HashMap<String, String>();
                        currencyValue.put("uomId", currency.getString("uomId"));
                        currencyValue.put("abbreviation", currency.getString("abbreviation"));
                        currencyValues.add(currencyValue);
                    }
                    resp.put("currencies", currencyValues);
                    resp.put("defaultCurrencyId", UtilConfig.getPropertyValue("opentaps", "defaultCurrencyUomId"));
                }
            }
        }

        return doJSONResponse(response, resp);
    }

    public static String getPartyCarrierAccountsJSON(HttpServletRequest request, HttpServletResponse response) {
        String partyId = request.getParameter("partyId");
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        return org.opentaps.common.event.AjaxEvents.doJSONResponse(response, PartyHelper.getPartyCarrierAccounts(partyId, delegator));
    }

    @SuppressWarnings("unchecked")
    public static String getNewInternalMessagesJSON(HttpServletRequest request, HttpServletResponse response) throws GenericServiceException {

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Locale locale = UtilHttp.getLocale(request);

        String partyIdTo = UtilCommon.getParameter(request, "partyIdTo");
        String returnNumberOnly = UtilCommon.getParameter(request, "returnNumberOnly");

        Map<String, Object> callCtxt = UtilMisc.<String, Object>toMap("partyIdTo", partyIdTo, "isRead", Boolean.FALSE, "locale", locale);
        Map<String, Object> callResult = dispatcher.runSync("opentaps.receiveInternalMessage", callCtxt);

        List<?> newMessages = FastList.newInstance();

        if (ServiceUtil.isError(callResult) || ServiceUtil.isFailure(callResult)) {
            Debug.logError("Unexpected error. Service opentaps.receiveInternalMessage returned error.", MODULE);
            return org.opentaps.common.event.AjaxEvents.doJSONResponse(response, UtilMisc.toMap("newMessages", newMessages, "numberOfNewMessages", 0));
        }

        List<?> messages = (List<?>) callResult.get("messages");

        if ("Y".equals(returnNumberOnly)) {
            return org.opentaps.common.event.AjaxEvents.doJSONResponse(response, UtilMisc.toMap("newMessages", null, "numberOfNewMessages", UtilValidate.isNotEmpty(messages) ? messages.size() : 0));
        }

        return org.opentaps.common.event.AjaxEvents.doJSONResponse(response, UtilMisc.toMap("newMessages", messages));
    }

    public static String checkExistOrderContentJSON(HttpServletRequest request, HttpServletResponse response) throws GenericServiceException {

        String orderId = UtilCommon.getParameter(request, "orderId");
        String fileName = UtilCommon.getParameter(request, "fileName");
        String initialPath = UtilProperties.getPropertyValue("content.properties", "content.upload.path.prefix");
        String ofbizHome = System.getProperty("ofbiz.home");
        if (!initialPath.startsWith("/")) {
            initialPath = "/" + initialPath;
        }
        String filePath = ofbizHome + initialPath + "/"  + org.opentaps.common.content.ContentServices.ORDERCONTENT_PREV + orderId + "/" + fileName;
        File file = new File(filePath);
        Debug.logInfo(filePath + " exist " + file.exists(), MODULE);
        return org.opentaps.common.event.AjaxEvents.doJSONResponse(response, UtilMisc.toMap("existSameFile", file.exists()));
    }

    /**
     * Finds and returns list of agreements to given supplier.
     */
    @SuppressWarnings("unchecked")
    public static String findSupplierAgreementsJSON(HttpServletRequest request, HttpServletResponse response) {

        Locale locale = UtilHttp.getLocale(request);
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");

        String organizationPartyId = (String) request.getSession().getAttribute("organizationPartyId");
        if (UtilValidate.isEmpty(organizationPartyId)) {
            organizationPartyId = UtilCommon.getParameter(request, "organizationPartyId");
            if (UtilValidate.isEmpty(organizationPartyId)) {
                organizationPartyId = UtilConfig.getPropertyValue("opentaps", "organizationPartyId");
                if (UtilValidate.isEmpty(organizationPartyId)) {
                    UtilMessage.createAndLogEventError(request, "OpentapsError_OrganizationNotSet", locale, MODULE);
                }
            }
        }
        String partyId = UtilCommon.getParameter(request, "partyId");

        List<GenericValue> agreements = null;
        try {

            agreements = delegator.findByAnd("Agreement", UtilMisc.toList(
                    EntityCondition.makeCondition("agreementTypeId", "PURCHASE_AGREEMENT"),
                    EntityCondition.makeCondition("statusId", "AGR_ACTIVE"),
                    EntityCondition.makeCondition("partyIdFrom", organizationPartyId),
                    EntityCondition.makeCondition("partyIdTo", partyId),
                    EntityUtil.getFilterByDateExpr())
            );

        } catch (GenericEntityException e) {
            Debug.logError(e.getMessage(), MODULE);
            return doJSONResponse(response, FastList.newInstance());
        }

        return doJSONResponse(response, UtilValidate.isNotEmpty(agreements) ? agreements : FastList.newInstance());
    }

    /**
     * Checks if a <code>SupplierProduct</code> exists.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @throws FoundationException if an error raise
     * @return a <code>String</code> value
     */
    public static String checkExistSupplierProductJSON(HttpServletRequest request, HttpServletResponse response) throws FoundationException {
        String productId = UtilCommon.getParameter(request, "productId");
        String partyId = UtilCommon.getParameter(request, "partyId");
        String currencyUomId = UtilCommon.getParameter(request, "currencyUomId");
        String quantity = UtilCommon.getParameter(request, "quantity");
        DomainsLoader domainLoader = new DomainsLoader(request);
        PurchasingRepositoryInterface purchasingRepository = domainLoader.loadDomainsDirectory().getPurchasingDomain().getPurchasingRepository();
        SupplierProduct supplierProduct = purchasingRepository.getSupplierProduct(partyId, productId, new BigDecimal(quantity), currencyUomId);
        return doJSONResponse(response, UtilMisc.toMap("existSupplierProduct", (supplierProduct != null)));
    }

    /** Return the objects which related with product in receive inventory form.
     * @throws GenericEntityException 
     */
    @SuppressWarnings("unchecked")
    public static String getReceiveInventoryProductRelatedsJSON(HttpServletRequest request, HttpServletResponse response) throws GenericEntityException {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession(true).getAttribute("userLogin");
        String productId = UtilCommon.getParameter(request, "productId");
        String facilityId = UtilCommon.getParameter(request, "facilityId");
        TimeZone timeZone = UtilCommon.getTimeZone(request);
        Locale locale = UtilHttp.getLocale(request);
        
        GenericValue facility = delegator.findByPrimaryKeyCache("Facility", UtilMisc.toMap("facilityId", facilityId));
        Map<String, Object> resp = new HashMap<String, Object>();
        try {
            Infrastructure infrastructure = new Infrastructure(dispatcher);
            Session session = infrastructure.getSession();
            Map svcResult = dispatcher.runSync("getProductByComprehensiveSearch", UtilMisc.toMap("productId", productId));
            if (!(ServiceUtil.isError(svcResult) || ServiceUtil.isFailure(svcResult))) {
                GenericValue productGv = (GenericValue) svcResult.get("product");
                if (productGv != null) {
                    resp.put("internalName", productGv.getString("internalName"));
                    resp.put("productId", productGv.getString("productId"));
                    // fill the productFacilityLocations selections
                    Product product = (Product) session.get(Product.class, productGv.getString("productId"));
                    List<ProductFacilityLocation> productFacilityLocations = (List<ProductFacilityLocation>) product.getProductFacilityLocations();
                    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
                    for (ProductFacilityLocation productFacilityLocation: productFacilityLocations) {
                        if (facilityId.equals(productFacilityLocation.getFacilityId())) {
                            Map<String, Object> value = new HashMap<String, Object>();
                            value.put("locationSeqId", productFacilityLocation.getLocationSeqId());
                            if (productFacilityLocation.getFacilityLocation() != null) {
                                value.put("facilityLocationAreaId", productFacilityLocation.getFacilityLocation().getAreaId());
                                value.put("facilityLocationAisleId", productFacilityLocation.getFacilityLocation().getAisleId());
                                value.put("facilityLocationSectionId", productFacilityLocation.getFacilityLocation().getSectionId());
                                value.put("facilityLocationLevelId", productFacilityLocation.getFacilityLocation().getLevelId());
                                value.put("facilityLocationPositionId", productFacilityLocation.getFacilityLocation().getPositionId());
                                if (productFacilityLocation.getFacilityLocation().getTypeEnumeration() != null) {
                                    value.put("facilityLocationTypeEnumDescription", productFacilityLocation.getFacilityLocation().getTypeEnumeration().get("description", locale));
                                }
                            }
                            values.add(value);
                        }
                    }
                    resp.put("productFacilityLocations", values);
                    String hql = "from GoodIdentification eo where eo.id.productId = :productId";
                    Query query = session.createQuery(hql);
                    query.setParameter("productId", productGv.get("productId"));
                    List<GoodIdentification> goodIdentifications = query.list();
                    values = new ArrayList<Map<String, Object>>();
                    for (GoodIdentification goodIdentification : goodIdentifications) {
                        Map<String, Object> value = new HashMap<String, Object>();
                        value.put("goodIdentificationTypeId", goodIdentification.getGoodIdentificationTypeId());
                        value.put("idValue", goodIdentification.getIdValue());
                        values.add(value);
                    }
                    resp.put("goodIdentifications", values);
                    
                    GenericValue facilityOwnerAcctgPref = delegator.findByPrimaryKeyCache("PartyAcctgPreference", UtilMisc.toMap("partyId", facility.getString("ownerPartyId")));
                    
                    svcResult = dispatcher.runSync("getProductCost", UtilMisc.toMap("productId", productGv.getString("productId"),
                                    "costComponentTypePrefix", "EST_STD", "currencyUomId", facilityOwnerAcctgPref.get("baseCurrencyUomId"),
                                    "userLogin", userLogin));
                    if (!ServiceUtil.isError(svcResult)) {
                        BigDecimal unitCost = (BigDecimal) svcResult.get("productCost");
                        resp.put("unitCost", unitCost);
                    }
                    
                    // find back ordered items
                    // use product.productId in case the productId passed in parameters was a goodId which was used to look up the product
                    hql = "from OrderItemShipGrpInvRes eo where eo.inventoryItem.productId = :productId and eo.inventoryItem.facilityId = :facilityId and eo.quantityNotAvailable is not null order by eo.reservedDatetime, eo.sequenceId";
                    query = session.createQuery(hql);
                    query.setParameter("productId", productGv.get("productId"));
                    query.setParameter("facilityId", facilityId);
                    List<OrderItemShipGrpInvRes> backOrderedItems = query.list();
                    values = new ArrayList<Map<String, Object>>();
                    for (OrderItemShipGrpInvRes backOrderedItem : backOrderedItems) {
                        Map<String, Object> value = new HashMap<String, Object>();
                        value.put("reservedDatetime", UtilDateTime.timeStampToString(backOrderedItem.getReservedDatetime(), timeZone, locale));
                        value.put("sequenceId", backOrderedItem.getSequenceId());
                        value.put("orderId", backOrderedItem.getOrderId());
                        value.put("orderItemSeqId", backOrderedItem.getOrderItemSeqId());
                        value.put("quantity", backOrderedItem.getQuantity());
                        value.put("quantityNotAvailable", backOrderedItem.getQuantityNotAvailable());
                        values.add(value);
                    }
                    resp.put("backOrderedItems", values);
                }
            }
        } catch (GenericEntityException e) {
            Debug.logError(e.getMessage(), MODULE);
            return doJSONResponse(response, FastList.newInstance());
        } catch (GenericServiceException e) {
            Debug.logError(e.getMessage(), MODULE);
            return doJSONResponse(response, FastList.newInstance());
        } catch (InfrastructureException e) {
            Debug.logError(e.getMessage(), MODULE);
            return doJSONResponse(response, FastList.newInstance());
        } catch (RepositoryException e) {
            Debug.logError(e.getMessage(), MODULE);
            return doJSONResponse(response, FastList.newInstance());
        }
        return doJSONResponse(response, resp);
    }
}
