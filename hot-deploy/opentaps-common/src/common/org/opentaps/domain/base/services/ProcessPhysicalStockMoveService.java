package org.opentaps.domain.base.services;

/*
 * Copyright (c) 2009 - 2009 Open Source Strategies, Inc.
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

// DO NOT EDIT THIS FILE!  THIS IS AUTO GENERATED AND WILL GET WRITTEN OVER PERIODICALLY WHEN THE SERVICE MODEL CHANGES
// EXTEND THIS CLASS INSTEAD.

import org.opentaps.foundation.infrastructure.InfrastructureException;
import org.opentaps.foundation.service.ServiceWrapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.ofbiz.entity.GenericValue;
import org.opentaps.foundation.infrastructure.User;

/**
 * 
            Process a Physical Stock Move from one FacilityLocation to another, in the same Facility.
            This service will not only move quantities from one InventoryItem to another but it will
            also reassign any existing OrderItemShipGrpInvRes records to the new InventoryItem.
        .
 * Auto generated base service entity processPhysicalStockMove.
 *
 * Engine: simple
 * Location: component://product/script/org/ofbiz/product/inventory/StockMoveServices.xml
 * Invoke: processPhysicalStockMove
 * Defined in: file:/home/jeremy/programmation/opentaps-git/applications/product/servicedef/services_facility.xml
 */
public class ProcessPhysicalStockMoveService extends ServiceWrapper {

    /** The service name as used by the service engine. */
    public static final String NAME = "processPhysicalStockMove";
    /** If the service requires authentication. */
    public static final Boolean REQUIRES_AUTHENTICATION = Boolean.FALSE;
    /** If the service requires a new transaction. */
    public static final Boolean REQUIRES_NEW_TRANSACTION = Boolean.FALSE;
    /** If the service uses a transaction. */
    public static final Boolean USES_TRANSACTION = Boolean.TRUE;

    /** The enumeration of input parameters. */
    public static enum In {
        facilityId("facilityId"),
        locale("locale"),
        locationSeqId("locationSeqId"),
        productId("productId"),
        quantityMoved("quantityMoved"),
        targetLocationSeqId("targetLocationSeqId"),
        timeZone("timeZone"),
        userLogin("userLogin"),
        warningMessageList("warningMessageList");
        private final String _fieldName;
        private In(String name) { this._fieldName = name; }
        public String getName() { return this._fieldName; }
    }

    public static enum Out {
        errorMessage("errorMessage"),
        errorMessageList("errorMessageList"),
        locale("locale"),
        responseMessage("responseMessage"),
        successMessage("successMessage"),
        successMessageList("successMessageList"),
        timeZone("timeZone"),
        userLogin("userLogin"),
        warningMessageList("warningMessageList");
        private final String _fieldName;
        private Out(String name) { this._fieldName = name; }
        public String getName() { return this._fieldName; }
    }

    /**
     * Creates a new <code>ProcessPhysicalStockMoveService</code> instance.
     */
    public ProcessPhysicalStockMoveService() {
        super();
    }


    private String inFacilityId;
    private Locale inLocale;
    private String inLocationSeqId;
    private String inProductId;
    private BigDecimal inQuantityMoved;
    private String inTargetLocationSeqId;
    private TimeZone inTimeZone;
    private GenericValue inUserLogin;
    private List inWarningMessageList;
    private String outErrorMessage;
    private List outErrorMessageList;
    private Locale outLocale;
    private String outResponseMessage;
    private String outSuccessMessage;
    private List outSuccessMessageList;
    private TimeZone outTimeZone;
    private GenericValue outUserLogin;
    private List outWarningMessageList;

    private Set<String> inParameters = FastSet.newInstance();
    private Set<String> outParameters = FastSet.newInstance();

    /**
     * Auto generated value accessor.
     * This parameter is required.
     * @return <code>String</code>
     */
    public String getInFacilityId() {
        return this.inFacilityId;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>Locale</code>
     */
    public Locale getInLocale() {
        return this.inLocale;
    }
    /**
     * Auto generated value accessor.
     * This parameter is required.
     * @return <code>String</code>
     */
    public String getInLocationSeqId() {
        return this.inLocationSeqId;
    }
    /**
     * Auto generated value accessor.
     * This parameter is required.
     * @return <code>String</code>
     */
    public String getInProductId() {
        return this.inProductId;
    }
    /**
     * Auto generated value accessor.
     * This parameter is required.
     * @return <code>BigDecimal</code>
     */
    public BigDecimal getInQuantityMoved() {
        return this.inQuantityMoved;
    }
    /**
     * Auto generated value accessor.
     * This parameter is required.
     * @return <code>String</code>
     */
    public String getInTargetLocationSeqId() {
        return this.inTargetLocationSeqId;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>TimeZone</code>
     */
    public TimeZone getInTimeZone() {
        return this.inTimeZone;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>GenericValue</code>
     */
    public GenericValue getInUserLogin() {
        return this.inUserLogin;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>List</code>
     */
    public List getInWarningMessageList() {
        return this.inWarningMessageList;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>String</code>
     */
    public String getOutErrorMessage() {
        return this.outErrorMessage;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>List</code>
     */
    public List getOutErrorMessageList() {
        return this.outErrorMessageList;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>Locale</code>
     */
    public Locale getOutLocale() {
        return this.outLocale;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>String</code>
     */
    public String getOutResponseMessage() {
        return this.outResponseMessage;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>String</code>
     */
    public String getOutSuccessMessage() {
        return this.outSuccessMessage;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>List</code>
     */
    public List getOutSuccessMessageList() {
        return this.outSuccessMessageList;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>TimeZone</code>
     */
    public TimeZone getOutTimeZone() {
        return this.outTimeZone;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>GenericValue</code>
     */
    public GenericValue getOutUserLogin() {
        return this.outUserLogin;
    }
    /**
     * Auto generated value accessor.
     * This parameter is optional.
     * @return <code>List</code>
     */
    public List getOutWarningMessageList() {
        return this.outWarningMessageList;
    }

    /**
     * Auto generated value setter.
     * This parameter is required.
     * @param inFacilityId the inFacilityId to set
    */
    public void setInFacilityId(String inFacilityId) {
        this.inParameters.add("facilityId");
        this.inFacilityId = inFacilityId;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param inLocale the inLocale to set
    */
    public void setInLocale(Locale inLocale) {
        this.inParameters.add("locale");
        this.inLocale = inLocale;
    }
    /**
     * Auto generated value setter.
     * This parameter is required.
     * @param inLocationSeqId the inLocationSeqId to set
    */
    public void setInLocationSeqId(String inLocationSeqId) {
        this.inParameters.add("locationSeqId");
        this.inLocationSeqId = inLocationSeqId;
    }
    /**
     * Auto generated value setter.
     * This parameter is required.
     * @param inProductId the inProductId to set
    */
    public void setInProductId(String inProductId) {
        this.inParameters.add("productId");
        this.inProductId = inProductId;
    }
    /**
     * Auto generated value setter.
     * This parameter is required.
     * @param inQuantityMoved the inQuantityMoved to set
    */
    public void setInQuantityMoved(BigDecimal inQuantityMoved) {
        this.inParameters.add("quantityMoved");
        this.inQuantityMoved = inQuantityMoved;
    }
    /**
     * Auto generated value setter.
     * This parameter is required.
     * @param inTargetLocationSeqId the inTargetLocationSeqId to set
    */
    public void setInTargetLocationSeqId(String inTargetLocationSeqId) {
        this.inParameters.add("targetLocationSeqId");
        this.inTargetLocationSeqId = inTargetLocationSeqId;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param inTimeZone the inTimeZone to set
    */
    public void setInTimeZone(TimeZone inTimeZone) {
        this.inParameters.add("timeZone");
        this.inTimeZone = inTimeZone;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param inUserLogin the inUserLogin to set
    */
    public void setInUserLogin(GenericValue inUserLogin) {
        this.inParameters.add("userLogin");
        this.inUserLogin = inUserLogin;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param inWarningMessageList the inWarningMessageList to set
    */
    public void setInWarningMessageList(List inWarningMessageList) {
        this.inParameters.add("warningMessageList");
        this.inWarningMessageList = inWarningMessageList;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outErrorMessage the outErrorMessage to set
    */
    public void setOutErrorMessage(String outErrorMessage) {
        this.outParameters.add("errorMessage");
        this.outErrorMessage = outErrorMessage;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outErrorMessageList the outErrorMessageList to set
    */
    public void setOutErrorMessageList(List outErrorMessageList) {
        this.outParameters.add("errorMessageList");
        this.outErrorMessageList = outErrorMessageList;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outLocale the outLocale to set
    */
    public void setOutLocale(Locale outLocale) {
        this.outParameters.add("locale");
        this.outLocale = outLocale;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outResponseMessage the outResponseMessage to set
    */
    public void setOutResponseMessage(String outResponseMessage) {
        this.outParameters.add("responseMessage");
        this.outResponseMessage = outResponseMessage;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outSuccessMessage the outSuccessMessage to set
    */
    public void setOutSuccessMessage(String outSuccessMessage) {
        this.outParameters.add("successMessage");
        this.outSuccessMessage = outSuccessMessage;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outSuccessMessageList the outSuccessMessageList to set
    */
    public void setOutSuccessMessageList(List outSuccessMessageList) {
        this.outParameters.add("successMessageList");
        this.outSuccessMessageList = outSuccessMessageList;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outTimeZone the outTimeZone to set
    */
    public void setOutTimeZone(TimeZone outTimeZone) {
        this.outParameters.add("timeZone");
        this.outTimeZone = outTimeZone;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outUserLogin the outUserLogin to set
    */
    public void setOutUserLogin(GenericValue outUserLogin) {
        this.outParameters.add("userLogin");
        this.outUserLogin = outUserLogin;
    }
    /**
     * Auto generated value setter.
     * This parameter is optional.
     * @param outWarningMessageList the outWarningMessageList to set
    */
    public void setOutWarningMessageList(List outWarningMessageList) {
        this.outParameters.add("warningMessageList");
        this.outWarningMessageList = outWarningMessageList;
    }

    /** {@inheritDoc} */
    public String name() {
        return NAME;
    }

    /** {@inheritDoc} */
    public Boolean requiresAuthentication() {
        return REQUIRES_AUTHENTICATION;
    }

    /** {@inheritDoc} */
    public Boolean requiresNewTransaction() {
        return REQUIRES_NEW_TRANSACTION;
    }

    /** {@inheritDoc} */
    public Boolean usesTransaction() {
        return USES_TRANSACTION;
    }

    /** {@inheritDoc} */
    public Map<String, Object> inputMap() {
        Map<String, Object> mapValue = new FastMap<String, Object>();
        if (inParameters.contains("facilityId")) mapValue.put("facilityId", getInFacilityId());
        if (inParameters.contains("locale")) mapValue.put("locale", getInLocale());
        if (inParameters.contains("locationSeqId")) mapValue.put("locationSeqId", getInLocationSeqId());
        if (inParameters.contains("productId")) mapValue.put("productId", getInProductId());
        if (inParameters.contains("quantityMoved")) mapValue.put("quantityMoved", getInQuantityMoved());
        if (inParameters.contains("targetLocationSeqId")) mapValue.put("targetLocationSeqId", getInTargetLocationSeqId());
        if (inParameters.contains("timeZone")) mapValue.put("timeZone", getInTimeZone());
        if (inParameters.contains("userLogin")) mapValue.put("userLogin", getInUserLogin());
        if (inParameters.contains("warningMessageList")) mapValue.put("warningMessageList", getInWarningMessageList());
        // allow the User set to override the userLogin
        if (getUser() != null) mapValue.put("userLogin", getUser().getOfbizUserLogin());
        return mapValue;
    }

    /** {@inheritDoc} */
    public Map<String, Object> outputMap() {
        Map<String, Object> mapValue = new FastMap<String, Object>();
        if (outParameters.contains("errorMessage")) mapValue.put("errorMessage", getOutErrorMessage());
        if (outParameters.contains("errorMessageList")) mapValue.put("errorMessageList", getOutErrorMessageList());
        if (outParameters.contains("locale")) mapValue.put("locale", getOutLocale());
        if (outParameters.contains("responseMessage")) mapValue.put("responseMessage", getOutResponseMessage());
        if (outParameters.contains("successMessage")) mapValue.put("successMessage", getOutSuccessMessage());
        if (outParameters.contains("successMessageList")) mapValue.put("successMessageList", getOutSuccessMessageList());
        if (outParameters.contains("timeZone")) mapValue.put("timeZone", getOutTimeZone());
        if (outParameters.contains("userLogin")) mapValue.put("userLogin", getOutUserLogin());
        if (outParameters.contains("warningMessageList")) mapValue.put("warningMessageList", getOutWarningMessageList());
        return mapValue;
    }

    /** {@inheritDoc} */
    public void putAllInput(Map<String, Object> mapValue) {
        if (mapValue.containsKey("facilityId")) setInFacilityId((String) mapValue.get("facilityId"));
        if (mapValue.containsKey("locale")) setInLocale((Locale) mapValue.get("locale"));
        if (mapValue.containsKey("locationSeqId")) setInLocationSeqId((String) mapValue.get("locationSeqId"));
        if (mapValue.containsKey("productId")) setInProductId((String) mapValue.get("productId"));
        if (mapValue.containsKey("quantityMoved")) setInQuantityMoved((BigDecimal) mapValue.get("quantityMoved"));
        if (mapValue.containsKey("targetLocationSeqId")) setInTargetLocationSeqId((String) mapValue.get("targetLocationSeqId"));
        if (mapValue.containsKey("timeZone")) setInTimeZone((TimeZone) mapValue.get("timeZone"));
        if (mapValue.containsKey("userLogin")) setInUserLogin((GenericValue) mapValue.get("userLogin"));
        if (mapValue.containsKey("warningMessageList")) setInWarningMessageList((List) mapValue.get("warningMessageList"));
    }

    /** {@inheritDoc} */
    public void putAllOutput(Map<String, Object> mapValue) {
        if (mapValue.containsKey("errorMessage")) setOutErrorMessage((String) mapValue.get("errorMessage"));
        if (mapValue.containsKey("errorMessageList")) setOutErrorMessageList((List) mapValue.get("errorMessageList"));
        if (mapValue.containsKey("locale")) setOutLocale((Locale) mapValue.get("locale"));
        if (mapValue.containsKey("responseMessage")) setOutResponseMessage((String) mapValue.get("responseMessage"));
        if (mapValue.containsKey("successMessage")) setOutSuccessMessage((String) mapValue.get("successMessage"));
        if (mapValue.containsKey("successMessageList")) setOutSuccessMessageList((List) mapValue.get("successMessageList"));
        if (mapValue.containsKey("timeZone")) setOutTimeZone((TimeZone) mapValue.get("timeZone"));
        if (mapValue.containsKey("userLogin")) setOutUserLogin((GenericValue) mapValue.get("userLogin"));
        if (mapValue.containsKey("warningMessageList")) setOutWarningMessageList((List) mapValue.get("warningMessageList"));
    }

    /**
     * Construct a <code>ProcessPhysicalStockMoveService</code> from the input values of the given <code>ProcessPhysicalStockMoveService</code>.
     * @param input a <code>ProcessPhysicalStockMoveService</code>
     */
    public static ProcessPhysicalStockMoveService fromInput(ProcessPhysicalStockMoveService input) {
        ProcessPhysicalStockMoveService service = new ProcessPhysicalStockMoveService();
        return fromInput(input.inputMap());
    }

    /**
     * Construct a <code>ProcessPhysicalStockMoveService</code> from the output values of the given <code>ProcessPhysicalStockMoveService</code>.
     * @param output a <code>ProcessPhysicalStockMoveService</code>
     */
    public static ProcessPhysicalStockMoveService fromOutput(ProcessPhysicalStockMoveService output) {
        ProcessPhysicalStockMoveService service = new ProcessPhysicalStockMoveService();
        service.putAllOutput(output.outputMap());
        return service;
    }

    /**
     * Construct a <code>ProcessPhysicalStockMoveService</code> from the given input <code>Map</code>.
     * @param mapValue the service input <code>Map</code>
     */
    public static ProcessPhysicalStockMoveService fromInput(Map<String, Object> mapValue) {
        ProcessPhysicalStockMoveService service = new ProcessPhysicalStockMoveService();
        service.putAllInput(mapValue);
        if (mapValue.containsKey("userLogin")) {
            GenericValue userGv = (GenericValue) mapValue.get("userLogin");
            if (userGv != null) {
                try {
                    service.setUser(new User(userGv, userGv.getDelegator()));
                } catch (InfrastructureException e) { }
            }
        }
        return service;
    }

    /**
     * Construct a <code>ProcessPhysicalStockMoveService</code> from the given output <code>Map</code>.
     * @param mapValue the service output <code>Map</code>
     */
    public static ProcessPhysicalStockMoveService fromOutput(Map<String, Object> mapValue) {
        ProcessPhysicalStockMoveService service = new ProcessPhysicalStockMoveService();
        service.putAllOutput(mapValue);
        return service;
    }
}