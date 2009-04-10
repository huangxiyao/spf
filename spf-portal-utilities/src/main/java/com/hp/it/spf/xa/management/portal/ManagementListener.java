package com.hp.it.spf.xa.management.portal;

import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogConfiguration;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.vignette.portal.log.LogWrapper;

/**
 * Servlet context listener which starts RMI Registry and binds to it JMXConnectorServer to 
 * allow access the management features using JMX with tools such as JConsole.
 * This class needs a port number used to run the registry. It can be specified either through
 * a system property <tt>-Dspf.management.port</tt> or in the property file available on the
 * classpath and named <tt>spfmanagement.properties</tt>. Other optional properties can be used
 * to further configure this class. See {@link com.hp.it.spf.xa.management.portal} description
 * for more information.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class ManagementListener implements ServletContextListener {

	private static final LogWrapper LOG = new LogWrapper(ManagementListener.class);

	/**
	 * Resource path (must be available classpath) of the optional property file which contains
	 * <tt>spf.management.port</tt> property containing the port number.
	 */
	static final String DEFAULT_PROPERTY_FILE_RESOURCE_PATH = "spfmanagement.properties";

	static final String PROP_NAME_PREFIX = "spf.management.";

	/**
	 * Port number property name.
	 */
	static final String MANAGEMENT_PORT_PROP_NAME = PROP_NAME_PREFIX + "port";

	/**
	 * Use platform MBean server property name.
	 */
	static final String USE_PLATFORM_MBREAN_SERVER_PROP_NAME = PROP_NAME_PREFIX + "usePlatformMBeanServer";

	/**
	 * Realm name property name.
	 */
	static final String REALM_PROP_NAME = PROP_NAME_PREFIX + "authentication.realmName";

	/**
	 * Group names property name.
	 */
	static final String GROUPS_PROP_NAME = PROP_NAME_PREFIX + "authentication.groupNames";
	
	/**
	 * JMX connector server which handles incoming management connections.
	 */
	private JMXConnectorServer mJMXServer;

	/**
	 * Resource path to the configuration file.
	 */
	private final String mPropertyFileResourcePath;


	public ManagementListener(String propertyFileResourcePath)
	{
		mPropertyFileResourcePath = propertyFileResourcePath;
	}

	public ManagementListener()
	{
		this(DEFAULT_PROPERTY_FILE_RESOURCE_PATH);
	}

	/**
	 * Application initialization logic.
	 * If the management port is properly defined this method will create RMI registry,
	 * will bind MBeans, create and start JMX connector server. If the port is not configured 
	 * this method does nothing simply logging an error message. 
	 */
	public void contextInitialized(ServletContextEvent event) {
		int managementPort = getManagementPort();
		if (managementPort <= 0) {
			LOG.error("Management port system property (" + MANAGEMENT_PORT_PROP_NAME + 
					") not defined. Management features will not be available.");
		}
		else {
			try {
				boolean usePlatformMBeanServer = getUsePlatformMBeanServerFlag();
				String realmName = getRealmName();
				Set<String> groupNames = getGroupNames();

				createRMIRegistry(managementPort);
				MBeanServer mbs = getMBeanServer(usePlatformMBeanServer);
				registerMBeans(mbs);
				startJMXConnectorServer(mbs, managementPort, realmName, groupNames);
			} catch (Exception e) {
				LOG.error("Management initialization failed", e);
			}
		}
	}

	/**
	 * Stops JMX connector server.
	 */
	public void contextDestroyed(ServletContextEvent event) {
		stopJMXConnectorServer();
	}
	
	/**
	 * @return <tt>true</tt> if the management system could be properly started.
	 */
	boolean managementFeaturesAavailable() {
		return mJMXServer != null;
	}
	
	/**
	 * Starts JMX connector server. It also writes to the log file the URL which can be used 
	 * to access the management features.
	 * @param mbs managed server this connector server forwards requests to
	 * @param managementPort port on the local host which will be used to connect to the server
	 * @param realmName realm name used for authentication or <tt>null</tt> if default realm should
	 * be used
	 * @param groupNames set of groups the user must be a member of to access the management features;
	 * if this set is emtpy all the authenticated user can access the features
	 * @throws IOException If an error occurs when starting the connector server
	 */
	void startJMXConnectorServer(MBeanServer mbs, int managementPort, String realmName, Set<String> groupNames) throws IOException {
		String hostName = InetAddress.getLocalHost().getHostName();
		JMXServiceURL url = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://" + hostName + ":" + managementPort + "/spfmanagement");

		Map<String, Object> environment = new HashMap<String, Object>();
		environment.put(JMXConnectorServer.AUTHENTICATOR, new Authenticator(realmName, groupNames));

		mJMXServer = JMXConnectorServerFactory.newJMXConnectorServer(url, environment, mbs);
		mJMXServer.start();
		LOG.info("JMX Connector Server available at " + url);
	}

	/**
	 * @return platform MBean server giving access not only SPF management features but also
	 * to JVM statistics
	 * @param usePlatformMBeanServer If <tt>true</tt> JVM platform MBean server will be used;
	 * otherwise a private server will be created.
	 */
	MBeanServer getMBeanServer(boolean usePlatformMBeanServer) {
		if (usePlatformMBeanServer) {
			return ManagementFactory.getPlatformMBeanServer();
		}
		else {
			return MBeanServerFactory.createMBeanServer();
		}
	}

	/**
	 * Creates RMI registry on the specified port.
	 * @param portNumber port number under which the registry will run
	 * @throws RemoteException If an error occurs when starting the registry
	 */
	void createRMIRegistry(int portNumber) throws RemoteException {
		LocateRegistry.createRegistry(portNumber);
	}

	/**
	 * @return the management port number based on the value of {@link #MANAGEMENT_PORT_PROP_NAME}
	 * system property, property file entry or <tt>-1</tt> if the number was incorrect or
	 * if the property was not specified.
	 */
	int getManagementPort() {
		String mgmtPort = getProperty(MANAGEMENT_PORT_PROP_NAME);

		if (mgmtPort == null || mgmtPort.trim().equals("")) {
			return -1;
		}
		try {
			return Integer.parseInt(mgmtPort);
		}
		catch (NumberFormatException e) {
			LOG.error("Management port value is incorrect: " + mgmtPort);
			return -1;
		}
	}

	/**
	 * @return <tt>true</tt> if the flag property {@link #USE_PLATFORM_MBREAN_SERVER_PROP_NAME}
	 * is specified and its value is <tt>true</tt>.
	 */
	boolean getUsePlatformMBeanServerFlag() {
		String usePlatformMBeanServer =
				getProperty(USE_PLATFORM_MBREAN_SERVER_PROP_NAME);
		return Boolean.valueOf(usePlatformMBeanServer);
	}

	/**
	 * @return the name of the realm to authenticate users against or <tt>null</tt> if default
	 * WebLogic Server realm should be used.
	 */
	String getRealmName() {
		String realmName = getProperty(REALM_PROP_NAME);
		if (realmName == null || realmName.trim().equals("")) {
			return null;
		}
		return realmName.trim(); 
	}

	/**
	 * @return a set of group names the authenticated user must be a member of in order to use
	 * the management features or <tt>null</tt> if all authenticated users can use them.
	 */
	Set<String> getGroupNames() {
		String groups = getProperty(GROUPS_PROP_NAME);
		if (groups == null || groups.trim().equals("")) {
			return null;
		}
		return new TreeSet<String>(Arrays.asList(groups.split("\\s*(,|;)\\s*")));
	}

	/**
	 * @param propertyName name of the system or configuration file property
	 * @return the property value first checking system property and then configuration file.
	 */
	String getProperty(String propertyName) {
		String propertyValue = System.getProperty(propertyName);
		if (propertyValue == null && mPropertyFileResourcePath != null) {
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("System property '" + propertyName + "' not specified. " +
						"Trying configuratin file '" + mPropertyFileResourcePath + "'");
			}
			propertyValue = PropertyResourceBundleManager.getString(
					mPropertyFileResourcePath, propertyName);
		}

		return propertyValue;
	}

	/**
	 * Stops JMX connector server.
	 */
	void stopJMXConnectorServer() {
		if (mJMXServer != null) {
			try {
				LOG.info("Stopping JMX Connector Server");
				mJMXServer.stop();
				mJMXServer = null;
			} catch (IOException e) {
				LOG.error("Error stopping JMXConnectorServer", e);
			}
		}
	}

	/**
	 * Registers SPF managed beans.
	 * Currently the following beans are available:
	 * <ul>
	 * <li>
	 * 	logging (com.hp.it.spf.portal:component=logging) - allows to view and configure Log4j loggers
	 * 	present in the portal.
	 * </li>
	 * </ul>
	 * @param mbs managed server in which the beans get registered
	 * @throws Exception If a registration error occurs
	 */
	void registerMBeans(MBeanServer mbs) throws Exception {
		mbs.registerMBean(
				new Log4jManagerDynamicMBean(), 
				new ObjectName("com.hp.it.spf.portal:component=logging"));
	}

}
