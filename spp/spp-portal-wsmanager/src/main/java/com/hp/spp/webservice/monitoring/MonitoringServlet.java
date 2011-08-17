/**
 * Monitoring Servlet Monitors the following aspect of all the application servers under AdminServer
 * 	- ServerHealth State
 * 	- JVM Status
 * 	- Thread Status
 * 	- Datasource Status
 * @author pranav
 *
 */

package com.hp.spp.webservice.monitoring;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import sun.misc.BASE64Encoder;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.hpp.exception.HPPAdminException;
import com.hp.spp.portal.crypto.CryptoTools;
import com.hp.spp.portal.crypto.CryptoToolsException;
import com.hp.spp.portal.crypto.CryptoToolsImpl;

public class MonitoringServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private ObjectName mRuntimeService;
	private ObjectName mDomainRuntimeService;
	private String mAdminServerName;
	private int mAdminServerListenPort;
	private MBeanServerConnection mMBeanRemoteConnection;
	private JMXConnector connector;
	
	private String mAdminUsername;
	private String mAdminPassword;
	private static final String mTripleDesSecretkey = "C173gL/+Z+8ZPtl/v8t5Sgte94C//mfv";
	private static Logger mLog = Logger.getLogger(MonitoringServlet.class);

	private StringBuilder sbServer;
	private StringBuilder sbThread;
	private StringBuilder sbJVM;
	private StringBuilder sbDataSource;
	
	/*
	 * Initializes the runtimeMBean Server 
	 * Fetches the AdminServer Details from the Domain Runtime Bean
	 */
	@Override
	public void init() throws ServletException {
		
		super.init();
		try {
			mRuntimeService = new ObjectName("com.bea:Name=RuntimeService," +
			"Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
			
			mDomainRuntimeService = new ObjectName("com.bea:Name=DomainRuntimeService," +
	          "Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
			
			InitialContext ctx;
			ctx = new InitialContext();
			MBeanServer mBeanRuntimeServer = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");
			ObjectName mServerRuntime = (ObjectName)mBeanRuntimeServer.getAttribute(mRuntimeService,"ServerRuntime");
		
			mAdminServerName = (String)mBeanRuntimeServer.getAttribute(mServerRuntime,"AdminServerHost");
			mAdminServerListenPort = ((Integer)mBeanRuntimeServer.getAttribute(mServerRuntime,"AdminServerListenPort")).intValue();
			
			mAdminUsername = Config.getValue("SPP.Monitoring.AdminUsername","weblogic");
			mAdminPassword = getDecryptedPassword();
				
		} catch (Exception e) {
			mLog.error("Error while Initializing the Runtime MBeanServer Object");
			throw new IllegalStateException("Error while Initializing the Remote Connection for DomainRuntime MBeanServer Object", e);
		}
		
		if(mMBeanRemoteConnection == null){
			try {
				initRemoteConnection(mAdminServerName,mAdminServerListenPort,mAdminUsername,mAdminPassword);
			} catch (Exception e) {
				mLog.error("Error while Initializing the Remote Connection for DomainRuntime MBeanServer Object");
				throw new IllegalStateException("Error while Initializing the Remote Connection for DomainRuntime MBeanServer Object", e);
			}
		}
				
	}

	/*
	 * Decrypts the weblogic console admin password. 
	 * And returns the cleartext password in the string.
	 */
private String getDecryptedPassword() {
	
	CryptoTools cryptoTools = new CryptoToolsImpl(mTripleDesSecretkey);
	
	String clearPassword = null;
	try {
		String encryptedPassword = Config.getValue("SPP.Monitoring.AdminServerPassword","PrPmIJYl5zj2HC+//hmnLw==");
		mLog.debug("Ecrypted Password : "+encryptedPassword);
		
		//	encrypted admin password "FNBWk19/+Q48iozANkgCIw=="
		//  encrypted admin password "weblogic" is PrPmIJYl5zj2HC+//hmnLw==
		
		clearPassword = cryptoTools.decrypt(encryptedPassword);
		mLog.debug("cleartext Password : "+clearPassword);		
	} catch (CryptoToolsException e) {
		//Error while decrypting the key using cryptoTools.decrypt
		throw new IllegalStateException("Error occured when decrypting the encrypted Admin Password", e);		
	} 
	return clearPassword;
}

/*
 * 
 * Initializes the DomainRuntimeBean Remote Connection.
 * We would be using the connection in keep alive state. 
 * The connection would be alive till the application closes.
 */
private void initRemoteConnection(String adminServerName, int adminServerListenPort, String adminUsername, String adminPassword) throws IOException {
		
		String protocol = "t3";		
		String jndiRoot = "/jndi/";     
		String mServer = "weblogic.management.mbeanservers.domainruntime";
		JMXServiceURL serviceURL;
		serviceURL = new JMXServiceURL(protocol, adminServerName, adminServerListenPort, jndiRoot + mServer);
		
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.SECURITY_PRINCIPAL, adminUsername);
		h.put(Context.SECURITY_CREDENTIALS, adminPassword);
		h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,"weblogic.management.remote");
     	
		connector = JMXConnectorFactory.connect(serviceURL, h);
     	mMBeanRemoteConnection = connector.getMBeanServerConnection();
		
	}	

	/*
	 * 
	 * This servlet would print "MONITORINGSTATUS OK" under normal condition
	 * If any abnormality is observed during the monitoring the response would be "MONITORINGSTATUS KO".
	 * The monitoring tool pings this servlet and based on the response it alerts the Support Team.
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String responseMsg = null;
		sbServer = new StringBuilder("");
		sbThread = new StringBuilder("");
		sbJVM = new StringBuilder("");
		sbDataSource = new StringBuilder("");
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("MonitoringServlet called");
		}

		responseMsg = "MONITORINGSTATUS KO";

		try {
			if (isMonitoringStatusOK()) {
				responseMsg = "MONITORINGSTATUS OK";
			}
		} catch (Exception e) {
			mLog.error("Managed server condition not healthy : ", e);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("MonitoringServlet response message: "+responseMsg);
		}
		
		if ("true".equalsIgnoreCase(Config.getValue("SPP.Monitoring.DetailsEnabled", "true")) ){
			response.getWriter().print(responseMsg + sbServer + sbDataSource + sbThread +  sbJVM);
		}else{
			response.getWriter().print(responseMsg);			
		}
		
	}
	
	/*
	 * Checks all the monitoring conditions and returns false if any of the condition is failing.
	 */
	
	private boolean isMonitoringStatusOK() {
		
		sbServer = new StringBuilder("<br><br><h2>Monitoring Status in detail : </h2>");
		
		ObjectName[] mAllServerRuntime;
		try {
			mAllServerRuntime = (ObjectName[])mMBeanRemoteConnection.getAttribute(mDomainRuntimeService,"ServerRuntimes");
		} catch (Exception e) {
			mLog.error("AllServerRuntime details retrival failed");
			throw new IllegalStateException("AllServerRuntime details retrival failed : ", e);
		}
		
		return ( 
				isServerHealthOK() &&
				isThreadStatusOK(mAllServerRuntime) &&
				isJVMStatusOK(mAllServerRuntime) &&
				isDataSourceStatusOK(mAllServerRuntime)
				);		
	}

/*
 * Monitors all the datasources under the managed servers 
 * If datasource has failed due to any reason , the method returns false.
 */
	private boolean isDataSourceStatusOK(ObjectName[] allServerRuntime) {
		
		sbDataSource = new StringBuilder("<br><h3> Data-Source Status : </h3>");
		
		for(int i=0; i<allServerRuntime.length; i++){
			String mManagedServerName;
		
			try {
				mManagedServerName = (String)mMBeanRemoteConnection.getAttribute(allServerRuntime[i],"Name");
			
			if(!"AdminServer".equalsIgnoreCase(mManagedServerName)){
				
				sbDataSource.append("<b>").append(mManagedServerName).append(" : </b>");
				
				ObjectName mJDBCServiceRuntime = (ObjectName)mMBeanRemoteConnection.getAttribute(allServerRuntime[i],"JDBCServiceRuntime");
				ObjectName[] mJDBCDataSourceRuntime = (ObjectName[])mMBeanRemoteConnection.getAttribute(mJDBCServiceRuntime,"JDBCDataSourceRuntimeMBeans");
				for(int j=0;j<mJDBCDataSourceRuntime.length;j++){
					String mDatasourceName = (String)mMBeanRemoteConnection.getAttribute(mJDBCDataSourceRuntime[j],"Name");
					String mDatasourceState = (String)mMBeanRemoteConnection.getAttribute(mJDBCDataSourceRuntime[j],"State");
					
		
					sbDataSource.append("Data-source ").append(mDatasourceName)
					.append(" is : <b>").append(mDatasourceState).append("</b><br>");
					
					if(!"Running".equalsIgnoreCase(mDatasourceState)){
						mLog.error("Datasource"+mDatasourceName+" is not RUNNING in Managed Server : " + mManagedServerName);
						return false;
					}	
				}
			}
		} catch (Exception e) {
			mLog.error("Error while fetching DataSource Status details");		
			throw new IllegalStateException("Error while fetching DataSource Status details : ", e);			
		}
		
	}
		return true;
	}

	/*
	 * Monitors the JVM Usage of the managed servers.
	 * In case JVM Freepercentage has reached the lower threshold level , the method returns false.
	 */
	
	private boolean isJVMStatusOK(ObjectName[] allServerRuntime) {
		
		sbJVM = new StringBuilder("<br><h3> JVM Status : </h3>");
		
		for(int i=0; i<allServerRuntime.length; i++){
			
			String mManagedServerName;
			try {
				mManagedServerName = (String)mMBeanRemoteConnection.getAttribute(allServerRuntime[i],"Name");
				if(!"AdminServer".equalsIgnoreCase(mManagedServerName)){
					ObjectName mJVMRuntime = (ObjectName)mMBeanRemoteConnection.getAttribute(allServerRuntime[i],"JVMRuntime");
			
					int mHeapFreePercent = ((Integer) mMBeanRemoteConnection.getAttribute(mJVMRuntime,"HeapFreePercent")).intValue();
					long mHeapFreeCurrent = (Long) mMBeanRemoteConnection.getAttribute(mJVMRuntime,"HeapFreeCurrent");
					long mHeapSizeCurrent = (Long) mMBeanRemoteConnection.getAttribute(mJVMRuntime,"HeapSizeCurrent");
			
					sbJVM.append("<b>").append(mManagedServerName).append(" : </b>")
					.append("  HeapFreePercent : ").append(mHeapFreePercent)
					.append("<br>");
					
					mLog.debug("HeapFree Percent for managed server "+mManagedServerName+" is : " + mHeapFreePercent);
					mLog.debug("HeapFree Percent Threshold Set : " + Config.getIntValue("SPP.Monitoring.HeapFreePercent", 3));
					
					MonitoringStatistics.logJVMDetails(mManagedServerName + " : " +
							"HeapFreePercent [" + mHeapFreePercent + "]" + 
							"HeapFreeCurrent [" + mHeapFreeCurrent + "]" + 
							"HeapSizeCurrent [" + mHeapSizeCurrent + "]" );
					
					if(mHeapFreePercent <= Config.getIntValue("SPP.Monitoring.HeapFreePercent", 3) ){
						mLog.error("HeapFreePercentage is lower then the threshold value for Managed Server : " + mManagedServerName);
						return false;
					}
				}
			} catch (Exception e) {
				mLog.error("Error while fetching JVM Status details");		
				throw new IllegalStateException("Error while fetching JVM Status details : ", e);			
			}
		}
		return true;
	}
	/*
	 * Monitors the Thread Usage of the servers.
	 * Incase thread usage has increased till the threshold value , This method returns false.
	 */

	private boolean isThreadStatusOK(ObjectName[] allServerRuntime) {
		
		sbThread = new StringBuilder("<br><h3> Thread Status : </h3>");
		
		for(int i=0; i<allServerRuntime.length; i++){
			String mManagedServerName;
			try {
				mManagedServerName = (String)mMBeanRemoteConnection.getAttribute(allServerRuntime[i],"Name");
				if(!"AdminServer".equalsIgnoreCase(mManagedServerName)){
					
					sbThread.append("<b>").append(mManagedServerName).append(" : </b>");
			
					ObjectName mThreadRuntime = (ObjectName)mMBeanRemoteConnection.getAttribute(allServerRuntime[i],"ThreadPoolRuntime");
					int mExecuteThreadTotalCount = ((Integer) mMBeanRemoteConnection.getAttribute(mThreadRuntime,"ExecuteThreadTotalCount")).intValue();
					int mHoggingThreadCount = ((Integer) mMBeanRemoteConnection.getAttribute(mThreadRuntime,"HoggingThreadCount")).intValue();
					mLog.debug("ExecuteThreadTotalCount Count found : " + mExecuteThreadTotalCount);
					mLog.debug("HoggingThreadCount Count Found : " + mHoggingThreadCount);
					mLog.debug("ExecuteThread Set Threshold : "+ Config.getIntValue("SPP.Monitoring.ExecuteThreadTotalCount", 400));
					mLog.debug("HoggingThreadCount Set Threshold : "+ Config.getIntValue("SPP.Monitoring.HoggingThreadCount", 100));
					
					sbThread.append("<br>ExecuteThreadCount : ").append(mExecuteThreadTotalCount)
					.append("<br>HoggingThreadCount : ").append(mHoggingThreadCount).append("<br>");
					
					MonitoringStatistics.logThreadDetails(mManagedServerName + " : " +
							"ExecuteThreadCount [" + mExecuteThreadTotalCount + "]" ); 
					
					if( (mExecuteThreadTotalCount >= Config.getIntValue("SPP.Monitoring.ExecuteThreadTotalCount", 400)) 
							&& ( mHoggingThreadCount >= Config.getIntValue("SPP.Monitoring.HoggingThreadCount", 100 ) ) ){
						mLog.error("Thread Count status is not healthy for managed-server name : " + mManagedServerName);					
						return false;
					}
				}
			} catch (Exception e) {
				mLog.error("Error while fetching Thread Status details");		
				throw new IllegalStateException("Error while fetching Thread Status details : ", e);			
			}
		}
				return true;
				
	}
	
	/*
	 * Monitors the ServerHealth Status.
	 * Incase the ServerState is anything else then "RUNNING" , this method returns false.
	 */

	private boolean isServerHealthOK() {
		
		ObjectName mServersRuntime;
		try {
			mServersRuntime = (ObjectName)mMBeanRemoteConnection.getAttribute(mDomainRuntimeService,"DomainRuntime");
			ObjectName[] mServers = (ObjectName[])mMBeanRemoteConnection.getAttribute(mServersRuntime,"ServerLifeCycleRuntimes");
			
			for(int i=0; i<mServers.length; i++){
				
				String mManagedServerName = (String)mMBeanRemoteConnection.getAttribute(mServers[i],"Name");
				String mManagedServerState = (String)mMBeanRemoteConnection.getAttribute(mServers[i],"State");
				
				if(!"AdminServer".equalsIgnoreCase(mManagedServerName)){
				sbServer.append("<br><b>").append(mManagedServerName).append("</b> is ").append(mManagedServerState);
				}
				
				if(!"RUNNING".equalsIgnoreCase(mManagedServerState)){
					return false;
				}
			}
		} catch (Exception e) {
			mLog.error("Error while fetching Server Health Status details");		
			throw new IllegalStateException("Error while fetching Server Health Status details : ", e);			
		}
		return true;
	}
		
}
