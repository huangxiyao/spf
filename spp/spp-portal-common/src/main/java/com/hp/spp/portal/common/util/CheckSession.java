package com.hp.spp.portal.common.util;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.hp.spp.portal.common.sql.SPPSQLManager;

public class CheckSession {
	public static final String EVAL_CHECKSESSION_TRUE = "VALID=TRUE";

	public static final String EVAL_CHECKSESSION_FALSE = "VALID=FALSE";

	private static Logger mLog = Logger.getLogger(CheckSession.class);

	public static String evaluateCheckSession(String HPPUserId, String PortalSessionId) {
		String eval = null;
		String query = "";
		query = query
				.concat("SELECT HPPUSERID, PORTAL_SESSIONID FROM SPP_USERSESSION WHERE HPPUSERID='");
		query = query.concat(HPPUserId);
		query = query.concat("' AND PORTAL_SESSIONID='");
		query = query.concat(PortalSessionId);
		query = query.concat("'");
		if (mLog.isDebugEnabled()) {
			mLog.debug("evaluateCheckSession Query " + query);
		}
		try {
			ArrayList result = SPPSQLManager.getInstance().executeSelectQuery(query);
			if (result.size() > 0 && !result.isEmpty() && (result.get(0) != null)) {
				eval = EVAL_CHECKSESSION_TRUE;
			} else {
				eval = EVAL_CHECKSESSION_FALSE;
			}
		} catch (Exception e) {
			mLog.error("Error in CheckSession", e);
			eval = EVAL_CHECKSESSION_FALSE;
		}

		return eval;
	}

	public static void addRowToCheckSession(String HPPUserId, String PortalSessionId) {
		if (HPPUserId != null && PortalSessionId != null) {
			String query = "";
			query = query
					.concat("INSERT INTO SPP_USERSESSION (HPPUSERID, PORTAL_SESSIONID,USERSESSION_CREATIONDATE ) VALUES  ('");
			query = query.concat(HPPUserId);
			query = query.concat("' ,'");
			query = query.concat(PortalSessionId);
			query = query.concat("',sysdate)");
			if (mLog.isDebugEnabled()) {
				mLog.debug("addRowToCheckSession Query " + query);
			}
			SPPSQLManager.getInstance().executeUpdateQuery(query);
		} else {
			mLog.error("Unable to Add row to check session HPPUserId or "
					+ "PortalSessionId is null ");
		}
	}

	public static void removeRowToCheckSession(String PortalSessionId) {
		String query = "";
		query = query.concat("DELETE FROM SPP_USERSESSION WHERE PORTAL_SESSIONID = '");
		query = query.concat(PortalSessionId);
		query = query.concat("'");
		if (mLog.isDebugEnabled()) {
			mLog.debug("removeRowToCheckSession Query " + query);
		}
		SPPSQLManager.getInstance().executeUpdateQuery(query);
	}
	
	/**
	 *  This function is invoked when user stops simulation. At this moment both simulators and simulated
	 *  entry is present in SPP_USERSESSION table. We intend to remove only the one corresponding to 
	 *  simulated user. 
	 */
	public static void removeSimulatedUserRowForCheckSession(String portalSessionId, String simulatedHPPID) {
		String query = "";
		query = query.concat("DELETE FROM SPP_USERSESSION WHERE PORTAL_SESSIONID = '");
		query = query.concat(portalSessionId);
		query = query.concat("'");
		query = query.concat(" AND HPPUSERID = '");
		query = query.concat(simulatedHPPID);
		query = query.concat("'");
		if (mLog.isDebugEnabled()) {
			mLog.debug("removeSimulatedUserRowForCheckSession Query " + query);
		}
		SPPSQLManager.getInstance().executeUpdateQuery(query);
	}

}