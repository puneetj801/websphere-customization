/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * (c) IBM Corp. 2014, 2016
 *******************************************************************************/
package com.harrods.commerce.calendar.oms.scheduler.commands;

import com.ibm.commerce.command.ControllerCommand;

public interface XHrdsXProdCalendarCleanUpCommand extends ControllerCommand {
	
	public static final String defaultCommandClassName = XHrdsXProdCalendarCleanUpCommandImpl.class.getName();

}