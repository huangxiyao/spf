package com.hp.it.spf.xa.misc;

//FIXME (slawek) - use the common logger

//import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Collections;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Environment Class is a singleton, which provides Environment specific information.
 * It uses the Weblogic MBeanServers to retrieve the information.
 *
 * Use:
 * Environment.singletonInstance.getManagedServerName();
 *
 * Environment.singletonInstance.getType();
 *
 */
public enum Environment {

	/**
	 * The signleton instance of this class
	 */
	singletonInstance;
	
//	private final Logger mLog = Logger.getLogger(Environment.class);

	private String mType = "DEV";
	private String mServerId = null;
	private String mManagedServerName;
	private List<String> mManagedServerList;
	private int mManagedServerPort;

	private Environment() {
		determineEnvironmentType();
		gatherDataFromMBeans();
	}

	private void determineEnvironmentType() {
		try {
			InputStream is = Environment.class.getResourceAsStream("/spf_environment.properties");
			if (is == null) {
				mType = "DEV";
//				if (mLog.isInfoEnabled()) {
//					mLog.info("Unable to load environment.properties. Will use DEV!");
//				}
			}
			else {
				Properties prop = new Properties();
				prop.load(is);
				is.close();

				String envType = prop.getProperty("ENVIRONMENT");
				if (envType == null || envType.trim().equals("")) {
					mType = "DEV";
//					if (mLog.isInfoEnabled()) {
//						mLog.info("ENVIRONMENT entry not present in environment.properties. Will use DEV!");
//					}
				}
				else {
					mType = envType.trim();
//					if (mLog.isInfoEnabled()) {
//						mLog.info("Current environment is: " + mType);
//					}
				}
			}
		}
		catch (Exception e) {
			mType = "DEV";
//			mLog.error("Error occured while determining environment type! Will use DEV.", e);
		}
	}

	private void gatherDataFromMBeans() {
		try {
			 ObjectName runtimeService = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
			 InitialContext ctx;
			 ctx = new InitialContext();
			 MBeanServer mbeanServer = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");
	 		 ObjectName mServerRuntime = (ObjectName)mbeanServer.getAttribute(runtimeService,"ServerRuntime");
	 		 mManagedServerName = (String)mbeanServer.getAttribute(mServerRuntime,"Name");
	 		 mManagedServerPort = ((Integer) mbeanServer.getAttribute(mServerRuntime,"ListenPort")).intValue();

	 		 mServerId = mManagedServerName;

//	 		 if(mLog.isDebugEnabled()){
//	 			 mLog.debug("Managed Server Name : "+ mManagedServerName);
//	 			 mLog.debug("Managed Server Port : "+ mManagedServerPort);
//
//	 		 }

	 		ObjectName mDomainConfig = (ObjectName)mbeanServer.getAttribute(runtimeService,"DomainConfiguration");
	    	ObjectName[] mServers = (ObjectName[]) mbeanServer.getAttribute(mDomainConfig, "Servers");

	    	 List<String> serverList = new ArrayList<String>();

	    	 for (int i = 0;i < mServers.length;i++) {
	        	 String managedServerName = (String) mbeanServer.getAttribute(mServers[i],"Name");

//	        	 if (mLog.isDebugEnabled()) {
//	        		 mLog.debug("Managed Server Name : "+managedServerName);
//	        		 mLog.debug("Managed Server Connection String : "+
//	        				 mbeanServer.getAttribute(mServers[i],"ListenAddress")+
//	        				 ":"+
//	        				 mbeanServer.getAttribute(mServers[i],"ListenPort"));
//	        	 }

				 if (!"AdminServer".equalsIgnoreCase(managedServerName)) {
					 serverList.add((String) mbeanServer.getAttribute(mServers[i],"ListenAddress")+":"+ mbeanServer.getAttribute(mServers[i],"ListenPort"));
				 }
	    	 }

	    	 mManagedServerList = Collections.unmodifiableList(serverList);

		} catch (MalformedObjectNameException e) {
			throw new IllegalStateException("Error while creating the RuntimeService Object", e);
		} catch (NamingException e) {
			throw new IllegalStateException("Error while Initializing the context for MBeanServer", e);
		} catch (Exception e) {
			throw new IllegalStateException("Error while Retrieving the information from MBeanServer", e);
		}
	}

	/**
	    * This method will return current Environment Type
	    * @return Current Environment Type
	    *		  DEV
	    *		 		
	    */
	public String getType() {
		return mType;
	}

	/**
	 * 
	 * @ FixMe -Need to remove this method and it's related entries from the code as part of refractoring
	*/
	public String getServerId() {
		return mServerId;
	}
	
	/**
	    * This method will return current managed server name
	    * This information is retrieved from the ServerRuntime
	    * @return Current Managed Server Name
	    *		  sppvgna1
	    *		 		
	    */
	public String getManagedServerName(){
		return mManagedServerName;
	}
	
	/**
	    * This method will return current managed server port
	    * This information is retrieved from the ServerRuntime
	    * @return Current Managed Server Port
	    *		  30002 
	    * 		
	    */
	public int getManagedServerPort(){
		return mManagedServerPort;
	}
	
	/**
	    * This method will return a List of managed server connection String in a cluster
	    * This information is retrieved from the DomainConfiguration MBean
	    * @return A List of managed server connection string
	    *		  g2u0154.austin.hp.com:30002
	    *		  g2u0154.austin.hp.com:30004
	    *		  g1u1569.austin.hp.com:30002 
	    * 		  g1u1569.austin.hp.com:30004
	    */
	public List<String> getManagedServersList(){
		return mManagedServerList;
	}
}
