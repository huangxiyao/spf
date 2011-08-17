package com.hp.spp.webservice.monitoring;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.hp.spp.config.ConfigException;


/**
 * Class to check that access to database is OK.
 * Needs the creation of the the datasource invoked.
 * 
 * @author Mathieu Vidal
 *
 */
public class DatabaseMonitoringServlet extends HttpServlet {

	private static Logger mLog = Logger.getLogger(DatabaseMonitoringServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String responseMsg = null;
		String dataSourceName = request.getParameter("datasource");
		if (dataSourceName != null){
			if (mLog.isDebugEnabled()) {
				mLog.debug("DatabaseMonitoringServlet called");
			}
	
			responseMsg = "DATASOURCE KO";
	
			try {
				if (eServiceResponseOK(dataSourceName)) {
					responseMsg = "DATASOURCE OK";
				}
			} catch (Exception e) {
				mLog.error("Error calling the Database", e);
			}
	
			if (mLog.isDebugEnabled()) {
				mLog.debug("DatabaseMonitoringServlet response message: "+responseMsg);
			}
		} else {
			responseMsg = "Missing parameter 'datasource' in the request";
		}

		response.getWriter().print(responseMsg);
	}

	private boolean eServiceResponseOK(String dataSourceName) throws RemoteException, ServiceException, ConfigException, MalformedURLException, NamingException, SQLException {
		Connection conn = null;
		try{
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(dataSourceName);
			conn = ds.getConnection();
			Statement statement = conn.createStatement();
			
			statement.execute("select 1 from dual");
			ResultSet result = statement.getResultSet();
			result.next();		
			if (1 == result.getInt(1))
				return true;
			return false;
		}
		finally{
			if(conn != null)
				conn.close();			
		}
	}
}


