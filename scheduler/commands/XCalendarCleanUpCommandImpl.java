/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * (c) IBM Corp. 2014, 2016
 *******************************************************************************/
package com.harrods.commerce.calendar.oms.scheduler.commands;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.foundation.server.services.dataaccess.JDBCQueryService;
import com.ibm.commerce.foundation.server.services.dataaccess.exception.QueryServiceApplicationException;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECMessageHelper;

public class XHrdsXProdCalendarCleanUpCommandImpl extends ControllerCommandImpl implements XHrdsXProdCalendarCleanUpCommand {
	
	private static final String CLASSNAME = XHrdsXProdCalendarCleanUpCommandImpl.class.getName();
	public static final Logger LOGGER = LoggingHelper.getLogger(CLASSNAME);

	@Override
	public void performExecute() throws ECException {
		super.performExecute();		
		JDBCQueryService service = new JDBCQueryService("com.ibm.commerce.catalog");
		try {
			LOGGER.logp(Level.FINEST, CLASSNAME, "performExecute()", "Starting cleaning up of XPRODCALENDAR table...");
			int count = service.executeUpdate("HARRODS_DELETE_EXPIRED_ROWS", new HashMap());
			LOGGER.logp(Level.FINEST, CLASSNAME, "performExecute()", "Finished doing Clean up of XPRODCALENDAR table. Deleted " + count + " rows");
		} catch (QueryServiceApplicationException e) {
			throw new ECSystemException(ECMessage._ERR_GENERIC, CLASSNAME, "performExecute()", ECMessageHelper.generateMsgParms(e.getMessage(), e));
		} catch (SQLException e) {
			throw new ECSystemException(ECMessage._ERR_SQL_EXCEPTION, CLASSNAME, "performExecute()", ECMessageHelper.generateMsgParms(e.getMessage(), e));
		}
	}
}
