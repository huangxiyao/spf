package com.hp.it.spf.xa.management.portal;

import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogConfiguration;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
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
 * classpath and named <tt>spfmanagement.properties</tt>.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class ManagementListener implements ServletContextListener {

	static LogWrapper LOG = new LogWrapper(ManagementListener.class);

	/**
	 * Resource path (must be available classpath) of the optional property file which contains
	 * <tt>spf.management.port</tt> property containing the port number.
	 */
	static final String PROPERTY_FILE_RESOURCE_PATH = "spfmanagement.properties";

	/**
	 * Port number system property name.
	 */
	static final String MANAGEMENT_PORT_PROP_NAME = "spf.management.port";
	
	/**
	 * JMX connector server which handles incoming management connections.
	 */
	private JMXConnectorServer mJMXServer;

	/**
	 * Application initialization logic.
	 * If the management port is properly defined this method will create RMI registry,
	 * will bind MBeans, create and start JMX connector server. If the port is not configured 
	 * this method does nothing simply logging an error message. 
	 */
	public void contextInitialized(ServletContextEvent event) {
		int managementPort = getManagementPort(PROPERTY_FILE_RESOURCE_PATH);
		if (managementPort <= 0) {
			LOG.error("Management port system property (" + MANAGEMENT_PORT_PROP_NAME + 
					") not defined. Management features will not be available.");
		}
		else {
			try {
				createRMIRegistry(managementPort);
				MBeanServer mbs = getMBeanServer();
				registerMBeans(mbs);
				startJMXConnectorServer(mbs, managementPort);
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
	 * @throws IOException If an error occurs when starting the connector server
	 */
	void startJMXConnectorServer(MBeanServer mbs, int managementPort) throws IOException {
		String hostName = InetAddress.getLocalHost().getHostName();
		JMXServiceURL url = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://" + hostName + ":" + managementPort + "/spfmanagement");
		mJMXServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
		mJMXServer.start();
		LOG.info("JMX Connector Server available at " + url);
	}

	/**
	 * @return platform MBean server giving access not only SPF management features but also
	 * to JVM statistics
	 */
	MBeanServer getMBeanServer() {
		return ManagementFactory.getPlatformMBeanServer();
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
	 * @param propertyFileResourcePath resource path to the property file containing the property
	 * <tt>spf.management.port</tt> or null if the file should not be checked.
	 * @return the management port number based on the value of {@link #MANAGEMENT_PORT_PROP_NAME}
	 * system property, property file entry or <tt>-1</tt> if the number was incorrect or
	 * if the property was not specified.
	 */
	int getManagementPort(String propertyFileResourcePath) {
		String mgmtPort = System.getProperty(MANAGEMENT_PORT_PROP_NAME);
		if (mgmtPort == null && propertyFileResourcePath != null) {
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("System property '" + MANAGEMENT_PORT_PROP_NAME + "' not specified. " +
						"Trying configuration file '" + propertyFileResourcePath + "'");
			}
			mgmtPort = PropertyResourceBundleManager.getString(
					 propertyFileResourcePath, MANAGEMENT_PORT_PROP_NAME);
		}

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
