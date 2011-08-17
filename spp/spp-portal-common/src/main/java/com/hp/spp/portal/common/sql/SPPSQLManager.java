package com.hp.spp.portal.common.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * This class is the main SQLManager for all the reports done in the portal This class is a
 * multiple singleton instance It can connect to the default Vignette Schema or to any other
 * external schema (with another datasource) defined in the properties file
 * 
 * @author MJULIENS
 * 
 */
public class SPPSQLManager {
	
	private static Logger mLog = Logger.getLogger(SPPSQLManager.class);

	private DataSource dataSource = null;
	
	// Default constructor which initialize a manager with a default data source
	private SPPSQLManager(){
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource) ic.lookup("jdbc/Vignette_PORTALDS");
		} catch (NamingException e) {
			mLog.error("error retrieving default datasource jdbc/Vignette_PORTALDS", e);
		}
	}
	
	// Constructor which creates a manager with a specific datasource
	private SPPSQLManager(String jndiNameDataSource){
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource) ic.lookup(jndiNameDataSource);
		} catch (NamingException e) {
			mLog.error("error retrieving datasource " + jndiNameDataSource, e);
		}
	}

	/**
	 * Singleton for the default Vignette database access
	 */
	private static SPPSQLManager theReportsVignetteSQLManager = new SPPSQLManager();

	/**
	 * Singleton for all the other schema defined in the properties file
	 */
	private static HashMap theReportsExternalDatasourceSQLManager = new HashMap();

	/**
	 * Initialize the list of external datasoures singletons from the properties file
	 * 
	 */
	static {
		ResourceBundle prop = ResourceBundle.getBundle("reportsSQLManager");
		Enumeration keys = prop.getKeys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith("DataSource_")) {
				key = key.replaceFirst("DataSource_", "");
				SPPSQLManager sqlm = new SPPSQLManager(key);
				theReportsExternalDatasourceSQLManager.put("DataSource_" + key, sqlm);
			}
		}
	}

	/**
	 * Provide the unique instance of the ReportsSQLManager for the default database acess :
	 * vignette
	 * 
	 * @return ReportsSQLManager
	 */
	public static SPPSQLManager getInstance() {
		return theReportsVignetteSQLManager;
	}

	/**
	 * Provide the unique instance of the ReportsSQLManager for any of the external datasource
	 * If the datasource is not referenced in the properties file, return null
	 * 
	 * @return ReportsSQLManager
	 */
	public static SPPSQLManager getInstance(String dataSourceName) {
		SPPSQLManager sqlm = null;
		if (theReportsExternalDatasourceSQLManager.containsKey("DataSource_" + dataSourceName)) {
			sqlm = (SPPSQLManager) theReportsExternalDatasourceSQLManager
					.get("DataSource_" + dataSourceName);
		}
		return sqlm;
	}

	/**
	 * Excute a SQL qery into portal database
	 * 
	 * @param statement
	 * @return ArrayList contains ArrayList for a SQL result row
	 */
	public ArrayList executeSelectQuery(String statement) {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList result = new ArrayList();
		ArrayList row = null;

		try {
			con = this.dataSource.getConnection();
			stmt = con.createStatement();
			mLog.debug("Execute query : "+statement);
			stmt.execute(statement);

			rs = stmt.getResultSet();
			int nbAtt = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				row = new ArrayList(nbAtt);
				for (int i = 0; i < nbAtt; i++)
					row.add((rs.getObject(i + 1) != null) ? rs.getObject(i + 1) : "");

				result.add(row);
			}

		} catch (Exception e) {
			mLog.error(" Error in executeSelectQuery " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// Log.error(e);
			}
		}
		return result;
	}

	/**
	 * Excute a update delete or insert SQL qery into portal database
	 * 
	 * @param statement
	 * @return ArrayList contains ArrayList for a SQL result row
	 */
	public int executeUpdateQuery(String statement) {
		Connection con = null;
		Statement stmt = null;
		int i = Integer.MAX_VALUE;
		try {
			con = this.dataSource.getConnection();
			stmt = con.createStatement();

			i = stmt.executeUpdate(statement);

		} catch (Exception e) {
			mLog.error(" Error in executeUpdateQuery " + e.getMessage(), e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// Log.error(e);
			}
		}
		return i;
	}
}
