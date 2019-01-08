/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * (c) IBM Corp. 2014, 2016
 *******************************************************************************/
package com.commerce.calendar.oms.service.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.foundation.internal.client.services.invocation.DataObjectInvocationServiceObjectImpl;
import com.ibm.commerce.foundation.internal.client.services.invocation.InvocationService;
import com.ibm.commerce.foundation.internal.client.services.invocation.exception.InvocationServiceException;
import com.sc.jaxws.other.webservices.GetCalendarDetails;
import com.sc.jaxws.other.webservices.GetCalendarDetailsResponse;
import com.sc.jaxws.other.webservices.WebservicesFactory;
import com.sc.jaxws.other.webservices.DocumentRoot;

/**
 * @author P
 *
 */
public class XGetCalendarDetailsServiceClient {
	
	public static final String CLASSNAME = XGetCalendarDetailsServiceClient.class.getName();
	/** Used for logging purpose. **/
	public static final Logger LOGGER = LoggingHelper.getLogger(CLASSNAME);

	/** The component ID of this client facade implementation. **/
	public static final String COMPONENT_ID = "com.commerce.calendar.oms";

	private static XGetCalendarDetailsServiceClient instance = new XGetCalendarDetailsServiceClient();

	/**
	 * Returns the singleton instance of this client facade implementation.
	 * @return the singleton instance of this client facade implementation.
	 */
	public static XGetCalendarDetailsServiceClient getInstance() {
		return instance;
	}
	
	public GetCalendarDetailsResponse getCalendarDetails(String action, GetCalendarDetails request) throws InvocationServiceException {
		final String METHODNAME = "getCalendarDetails(String action, GetCalendarDetails request)";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASSNAME, METHODNAME); 
		}

		DocumentRoot docRoot = WebservicesFactory.eINSTANCE.createDocumentRoot();
		docRoot.setGetCalendarDetails(request);
		
		DataObjectInvocationServiceObjectImpl requestDataObject = new DataObjectInvocationServiceObjectImpl();
		requestDataObject.setDataObject(docRoot.getGetCalendarDetails());
		DataObjectInvocationServiceObjectImpl responseDataObject = new DataObjectInvocationServiceObjectImpl();

		if (LoggingHelper.isTraceEnabled(LOGGER) && null != requestDataObject) {
			LOGGER.logp(Level.FINE, CLASSNAME, METHODNAME, "request = " + new String(requestDataObject.getXML()));
			LOGGER.logp(Level.FINE, CLASSNAME, METHODNAME, "action = " + action);
		}

		InvocationService invocationService = new InvocationService(COMPONENT_ID, action, null);
		try {
		invocationService.invoke(requestDataObject, responseDataObject);
		} catch (InvocationServiceException ie){
			LOGGER.logp(Level.SEVERE, CLASSNAME, METHODNAME, "Error occurred during invoking calendar api service : " + ie.getMessage());
			throw ie;			
		}

		if (LoggingHelper.isTraceEnabled(LOGGER) && null != responseDataObject) {
			LOGGER.logp(LoggingHelper.DEFAULT_TRACE_LOG_LEVEL, CLASSNAME, METHODNAME, "response = " + new String(responseDataObject.getXML()));
		}
		 // TODO typecasting fails if date or time is null - cant do anything about it - need to tell oms date time should not be null
		GetCalendarDetailsResponse response = (GetCalendarDetailsResponse)responseDataObject.getDataObject();

		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASSNAME, METHODNAME); 
		}
		return response ;
	}
}
