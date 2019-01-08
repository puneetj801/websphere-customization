/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * (c) IBM Corp. 2014, 2016
 *******************************************************************************/
package com.commerce.calendar.oms.service.commands;

import com.ibm.commerce.command.TaskCommand;
import com.sc.jaxws.other.webservices.GetCalendarDetailsResponse;

public interface XGetCalendarDetailsTaskCommand extends TaskCommand {
	
	public static final String defaultCommandClassName = XGetCalendarDetailsTaskCommandImpl.class.getName();
	
	public static final String ACTION_GET_CALENDAR_DETAILS = "getCalendarDetails";
	
	public GetCalendarDetailsResponse getCalendarDetailsResponse();
	
	/**
	 * @param calendarId the product partnumber
	 */
	public void setCalendarId(String calendarId);
	
	/**
	 * @param deliveryMethod the shipping method code
	 */
	public void setDeliveryMethod(String deliveryMethod);
	
	/**
	 * @param orgCode the organization code setup by OMS, if not supplied, defaults to "DEFAULT"
	 */
	public void setOrganizationCode(String orgCode);

}
