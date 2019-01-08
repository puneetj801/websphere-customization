/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * (c) IBM Corp. 2014, 2016
 *******************************************************************************/
package com.commerce.calendar.oms.service.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.commerce.calendar.oms.service.client.XGetCalendarDetailsServiceClient;
import com.commerce.common.exception.XErrorMessages;
import com.commerce.order.constants.XECOrderConstants;
import com.ibm.commerce.command.TaskCommandImpl;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.foundation.internal.client.services.invocation.exception.InvocationServiceException;
import com.ibm.commerce.foundation.internal.server.services.registry.StoreConfigurationRegistry;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECMessageHelper;
import com.sc.doc.yfs.create.env.input.YFSEnvironmentXSDType;
import com.sc.jaxws.other.webservices.GetCalendarDetails;
import com.sc.jaxws.other.webservices.GetCalendarDetailsResponse;
import com.sc.jaxws.other.webservices.WebservicesFactory;
import com.sc.jaxws.other.ws.doc..get.calendar.details.input.CalendarXSDType;
import com.sc.jaxws.other.ws.doc..get.calendar.details.input.InputFactory;

/**
 * @author P
 * 
 * This task command makes the call to OMS getCalendar API service and returns the response
 *
 */
public class XGetCalendarDetailsTaskCommandImpl extends TaskCommandImpl implements XGetCalendarDetailsTaskCommand {
	
	/** LOGGER **/
	public static final String CLASSNAME = XGetCalendarDetailsTaskCommandImpl.class.getName();
	public static final Logger LOGGER = LoggingHelper.getLogger(CLASSNAME);
	
	/** STORECONF keys for default values for some of the parameters in the service request. **/
	
	/**
	 * the product partnumber
	 */
	private String calendarId;
	/**
	 * the shippingCode
	 */
	private String deliveryMethod;
	/**
	 * the organizationCode setup by OMS
	 */
	private String orgCode;
	private GetCalendarDetailsResponse calendarDetailsResponse;
	
	
	@Override
	public void performExecute() throws ECException {
		String methodName = "performExecute()";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASSNAME, methodName);
		}
		super.performExecute();		
		// compose the request object for getCalendarDetails service
		GetCalendarDetails request = composeRequest();		
		GetCalendarDetailsResponse response = null;
		try {
			// Invoke the web-service using the client facade.
			response = XGetCalendarDetailsServiceClient.getInstance().getCalendarDetails(getAction(), request);
		} catch (InvocationServiceException e) {
			throw new ECApplicationException(XErrorMessages._ERR_OMS_GETCALENDAR_SERVICE_UNAVAILABLE, CLASSNAME,
					methodName, ECMessageHelper.generateMsgParms(e.getMessage()));
		}
		// Set the service response.
		calendarDetailsResponse = response;
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASSNAME, methodName);
		}
	}
	
	/**
	 * @return GetCalendarDetails, composes the request for calendar API call to OMS
	 */
	protected GetCalendarDetails composeRequest(){
		String methodName = "composeRequest";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASSNAME, methodName);
		}
		GetCalendarDetails calRequest = WebservicesFactory.eINSTANCE.createGetCalendarDetails();		
		calRequest.setEnv(composeEnv());
		calRequest.setInput(composeCalendar());
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASSNAME, methodName);
		}
		return calRequest;
	}
	
	/**
	 * @return the CalendarXSDType
	 */
	protected CalendarXSDType composeCalendar(){
		String methodName = "composeCalendar";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASSNAME, methodName);
		}
		CalendarXSDType calendar = InputFactory.eINSTANCE.createCalendarXSDType();
		// calendarId = calendarId_deliveryMethod, according to new OMS specifications
		calendar.setCalendarId(calendarId + "_" + deliveryMethod);
		StoreConfigurationRegistry storeConfigurationRegistry = StoreConfigurationRegistry.getSingleton();
		String orgCode = storeConfigurationRegistry.getValue(getCommandContext().getStoreId(), "DEFAULT_ORGANIZATION_CODE_CALENDAR_API");
		if (orgCode == null || orgCode.length() == 0)
			orgCode = "DEFAULT";
		calendar.setOrganizationCode(orgCode);
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASSNAME, methodName);
		}
		return calendar;		
	}
	
	/**
	 * @return returns the YFSEnvironmentXSDType
	 */
	protected YFSEnvironmentXSDType composeEnv() {
		final String methodName = "composeEnv()";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASSNAME, methodName);
		}
		// Get the username and password for the OMS web-service from STORECONF table.
		StoreConfigurationRegistry storeConfigurationRegistry = StoreConfigurationRegistry.getSingleton();
		String serviceUserName = storeConfigurationRegistry.getValue(getCommandContext().getStoreId(), XECOrderConstants.OMS_ORDER_SERVICE_USERNAME);
		String servicePassword = storeConfigurationRegistry.getValue(getCommandContext().getStoreId(), XECOrderConstants.OMS_ORDER_SERVICE_PASSWORD);
		
		YFSEnvironmentXSDType yfsEnv = com.sc.doc.yfs.create.env.input.InputFactory.eINSTANCE.createYFSEnvironmentXSDType();
		yfsEnv.setUserId(serviceUserName);
		yfsEnv.setPassword(servicePassword);
		
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASSNAME, methodName);
		}
		return yfsEnv;
	}
	
	@Override
	public void validateParameters() throws ECException {
		String methodName = "validateParameters";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASSNAME, methodName);
		}
		super.validateParameters();
		if (calendarId == null || calendarId.length() == 0){
			throw new ECApplicationException(ECMessage._ERR_BAD_MISSING_CMD_PARAMETER, CLASSNAME, 
					"validateParameters()", ECMessageHelper.generateMsgParms("calendarId"));
		}
		LOGGER.logp(Level.FINE, CLASSNAME, methodName, "calendarId" + calendarId);
		
		if (deliveryMethod == null || deliveryMethod.length() == 0){
			throw new ECApplicationException(ECMessage._ERR_BAD_MISSING_CMD_PARAMETER, CLASSNAME, 
					"validateParameters()", ECMessageHelper.generateMsgParms("deliveryMethod"));
		}
		LOGGER.logp(Level.FINE, CLASSNAME, methodName, "deliveryMethod = " + deliveryMethod);
		
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASSNAME, methodName);
		}
	}

	/* (non-Javadoc)
	 * @see com.commerce.calendar.oms.service.commands.XGetCalendarDetailsTaskCommand#getCalendarDetailsResponse()
	 */
	@Override
	public GetCalendarDetailsResponse getCalendarDetailsResponse() {
		return calendarDetailsResponse;
	}

	/* (non-Javadoc)
	 * @see com.commerce.calendar.oms.service.commands.XGetCalendarDetailsTaskCommand#setCalendarId(java.lang.String)
	 */
	@Override
	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
		
	}

	/* (non-Javadoc)
	 * @see com.commerce.calendar.oms.service.commands.XGetCalendarDetailsTaskCommand#setDeliveryMethod(java.lang.String)
	 */
	@Override
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
		
	}

	/* (non-Javadoc)
	 * @see com.commerce.calendar.oms.service.commands.XGetCalendarDetailsTaskCommand#setOrganizationCode(java.lang.String)
	 */
	@Override
	public void setOrganizationCode(String orgCode) {
		this.orgCode = orgCode;
		
	}
	
	/**
	 * @return the action
	 */
	protected String getAction() {
		return XGetCalendarDetailsTaskCommand.ACTION_GET_CALENDAR_DETAILS;
	}
}
