package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


import com.epicentric.jdbc.ConnectionPool;
import com.epicentric.jdbc.ConnectionPoolManager;
import com.vignette.portal.log.LogWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * A class for monitoring the health of the Vignette database.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @author <link href="hao.zhang2@hp.com"> Zhang, Hao </link>
 * @version TBD
 * @see GeneralComponentCheckTask
 * @see LogWrapper
 * @see Connection
 * @see Statement
 */
public class DatabaseCheckTask extends GeneralComponentCheckTask {

	/**
	 * serialVersionUID
	 * long
	 */
	private static final long serialVersionUID = -7245998522253322126L;

	private static final String VIG_DB_NAME =
		"Vignette Database"; // the name of VIG DB

	private static final String QUERY_STRING =
		"select * from version"; //The SQL for testing the database

	/*
	 * Modified by xie xingxing(xxie@hp.com) for QXCR1000813689 at 5/29/2008.
	 * Collect more information for the error logs, then can provide more information for
	 * troubleshooting.
	 */
	// current class name
	private static String thisClassname = DatabaseCheckTask.class.getName();

	/**
	 * params read from the config file of healthcheck.xml
	 * added by ck for CR: 1000813522
	 */
	private Map params = new HashMap();
	/**
	 * the status of initialization
	 */
	private boolean initSuccess = false; //false serves as flag meaning init failed

	/**
	 * the log for vignette when throwing exception
	 */
	private static final LogWrapper LOG = new LogWrapper(
			DatabaseCheckTask.class);
	/**
	 * Set the attribute params, like url, pattern, trustStore,
	 * trustStorePassoword, and so on.
	 * added by ck for CR: 1000813522
	 * @param params
	 *            attribute params like url, pattern, trustStore,
	 *            trustStorePassoword, and so on
	 */
	public void setParams(Map params) {
		this.params = params;
	}
	/**
	 * constructor for DatabaseCheckTask
	 */
	public DatabaseCheckTask() {
		super(VIG_DB_NAME);
	}

	/**
	 * test the health of vignette database
	 *
	 * @see IComponentCheckTask#run()
	 */
	public void run() {
		/*
		 * Modified by xie xingxing(xxie@hp.com) for QXCR1000813689 at 6/10/2008.
		 * Collect more information for the error logs, then can provide more information for
		 * troubleshooting.
		 */
		String thisMethod = thisClassname + ".run(): ";
		String thisStep = thisMethod + "begin";
		LOG.debug(thisStep);

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		int tmpStatus = STATUS_FAIL; //STATUS_FAIL means failing of vig db
		long beginTime = System.currentTimeMillis(); //the current time

		/*
		 * Changed to use Vignette connection pool to setup the JDBC connection.
		 * Modified by Ye Liu (ye.liu@hp.com).
		 */
		ConnectionPool pool = ConnectionPoolManager.getDefaultPool();

		try {
			thisStep = thisMethod + "get connection";
			LOG.debug(thisStep);

			// Get the JDBC connection from Vignette connection pool
			con = pool.getConnection();

			thisStep = thisMethod + "create statement";
			LOG.debug(thisStep);
			stmt = con.prepareStatement(QUERY_STRING);

			thisStep = thisMethod + "execute query";
			LOG.debug(thisStep);
			// execute the SQL
			resultSet = stmt.executeQuery();

			if (resultSet != null) {
				resultSet.next();
			}

			tmpStatus = STATUS_PASS; //STATUS_FAIL means failing of vig db
		} catch (Exception e) {
			LOG.error(thisStep + "query: " + QUERY_STRING
					+ ", caught: " + e, e);
		} finally {
			try {
				// close the resultSet.
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (Exception e) {
				LOG.error(e.toString(), e);
			}
			try {
				// close the PreparedStatement.
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				LOG.error(e.toString(), e);
			}
			try {
				// close the connection and return it to the pool.
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				LOG.error(e.toString(), e);
			}
		}

		status = tmpStatus;
		responseTime = System.currentTimeMillis() - beginTime;

		thisStep = thisMethod + "end";
		LOG.debug(thisStep);
	}

}
