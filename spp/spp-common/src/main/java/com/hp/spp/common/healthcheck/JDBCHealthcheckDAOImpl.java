package com.hp.spp.common.healthcheck;

import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import com.hp.spp.db.DB;
import com.hp.spp.db.DB;
import com.hp.spp.db.DatabaseTransaction;
import com.hp.spp.db.DatabaseException;

public class JDBCHealthcheckDAOImpl implements HealthcheckDAO {
	
	private static final Logger mLog = Logger.getLogger(JDBCHealthcheckDAOImpl.class);

	private static final String updateOutOfRotationFlag = "UPDATE SPP_HEALTHCHECK_SERVER_INFO SET OUT_OF_ROTATION_FLAG = ? WHERE SERVER_NAME = ?";
	private static final String queryAllServers = "SELECT * FROM SPP_HEALTHCHECK_SERVER_INFO";
	private static final String queryServerByExample = "SELECT SERVER_NAME, APPLICATION_NAME, SERVER_TYPE, SITE_NAME, OUT_OF_ROTATION_FLAG FROM SPP_HEALTHCHECK_SERVER_INFO WHERE SERVER_NAME = ?";
		
	// Save a new/updated shutdown server listing		
	public void updateOutOfRotationFlag(final List<HealthcheckServerInfo> listServerInfo) {

		if (mLog.isDebugEnabled()) {
			mLog.debug("JDBCHealthcheckInfoImpl updateOutOfRotationFlag calls");
		}
		
		DatabaseTransaction<Object> tx = new DatabaseTransaction<Object>() {
			protected Object doInTransaction() throws Exception {
				for (HealthcheckServerInfo serverInfo : listServerInfo) {
					String serverName = serverInfo.getServerName();
					String outOfRotationFlag;
					if (serverInfo.getOutOfRotation()) {
						outOfRotationFlag = "Y";
					} else {
						outOfRotationFlag = "N";					
					}
					Object[] args = {outOfRotationFlag,serverName};
					int result = update(updateOutOfRotationFlag, args, null);
					if (result == 0) {
						throw new DatabaseException("Unable to find healthcheck server info for server: " + serverName);
					}
				}
				return null;
			}
		};
		try {
			tx.execute();
		}
		catch (DatabaseException e) {
			throw e;
		}
		catch (Exception e) {
			throw new RuntimeException("Error updating outOfRationFlag in layer 7 health check code", e);
		}
	}

	// Method to return a list of HealthcheckInfo objects for all servers	
	public List<HealthcheckServerInfo> getAllServers() {

		if (mLog.isDebugEnabled()) {
			mLog.debug("ConfigBackedHealthcheckInfo getAllServers calls");
		}	
		
		return DB.query(queryAllServers, new HealthcheckMapper());		
	}	

	// Method to return a list of servers filtered by a input HealthcheckInfo example object		
	public List<HealthcheckServerInfo> findServersByExample(HealthcheckServerInfo infoExample) {
		List<String> args = new ArrayList<String>();
		StringBuilder whereClause = new StringBuilder();
		
		if (infoExample.getServerName() != null) {
			whereClause.append(" AND SERVER_NAME = ?");
			args.add(infoExample.getServerName());
		};
		if (infoExample.getApplicationName() != null) {
			whereClause.append(" AND APPLICATION_NAME = ?");
			args.add(infoExample.getApplicationName());
		}
		if (infoExample.getServerType() != null) {
			whereClause.append(" AND SERVER_TYPE = ?");
			args.add(infoExample.getServerType());
		}
		if (infoExample.getSiteName() != null) {
			whereClause.append(" AND SITE_NAME = ?");
			args.add(infoExample.getSiteName());
		}
		
		StringBuilder query = new StringBuilder("SELECT SERVER_NAME, APPLICATION_NAME, SERVER_TYPE, SITE_NAME, OUT_OF_ROTATION_FLAG FROM SPP_HEALTHCHECK_SERVER_INFO");
		if (whereClause.length() > 0) {
			query.append(" WHERE 1=1").append(whereClause);
		}
		
		return DB.query(query.toString(), new HealthcheckMapper(), args.toArray());
		 
	}
	
	// A method to compare two HealthcheckInfo objects	
	private boolean compareHealthcheckInfo(HealthcheckServerInfo infoExample, HealthcheckServerInfo infoSource) {
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("ConfigBackedHealthcheckInfo compareHealthcheckInfo(" + infoExample + ", " + infoSource + ")");
		}

		if ((infoExample.getServerName() != null) && (!infoExample.getServerName().equals(infoSource.getServerName()))) {
			return false;
		}
		if ((infoExample.getApplicationName() != null) && (!infoExample.getApplicationName().equals(infoSource.getApplicationName()))) {
			return false;
		}
		if ((infoExample.getServerType() != null) && (!infoExample.getServerType().equals(infoSource.getServerType()))) {
			return false;
		}
		if ((infoExample.getSiteName() != null) && (!infoExample.getSiteName().equals(infoSource.getSiteName()))) {
			return false;
		}

		return true;		
	}
}
