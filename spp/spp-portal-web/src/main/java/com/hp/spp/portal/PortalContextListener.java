package com.hp.spp.portal;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.lang.reflect.Method;

import com.hp.spp.config.Config;

/**
 * Context listener used to initialize some of SPP objects in Vignette.
 * 
 * @see #initAxisWSTypeDescriptors(String) for details about why the listener is used to initialize
 * Axis web service type stubs.
 */
public class PortalContextListener implements ServletContextListener {
	public static final String NON_PROXY_HOSTS_KEY = "http.nonProxyHosts";

	private static Logger mLog = Logger.getLogger(PortalContextListener.class);

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		Config.setPrefix("portal");
		setNonProxyHosts();

		String doInit = servletContextEvent.getServletContext().getInitParameter("PortalContextListener.initAxisWSTypeDescriptors");
		if (doInit != null && Boolean.valueOf(doInit.trim()).booleanValue()) {
			initAxisWSTypeDescriptors();
		}
	}

	/**
	 * Appends <tt>localhost</tt> and <tt>127.0.0.1</tt> to the value of System's {@link #NON_PROXY_HOSTS_KEY}
	 * property.
	 */
	void setNonProxyHosts() {
		String nonProxyHosts = System.getProperty(NON_PROXY_HOSTS_KEY);
		String updatedNonProxyHosts = nonProxyHosts;
		if (updatedNonProxyHosts == null || "".equals(updatedNonProxyHosts)) {
			updatedNonProxyHosts = "localhost|127.0.0.1";
		}
		else {
			if (updatedNonProxyHosts.toLowerCase().indexOf("localhost") == -1) {
				updatedNonProxyHosts = updatedNonProxyHosts + "|localhost";
			}
			if (updatedNonProxyHosts.indexOf("127.0.0.1") == -1) {
				updatedNonProxyHosts = updatedNonProxyHosts + "|127.0.0.1";
			}
		}
		System.setProperty(NON_PROXY_HOSTS_KEY, updatedNonProxyHosts);
		if (mLog.isInfoEnabled()) {
			if (updatedNonProxyHosts.equals(nonProxyHosts)) {
				mLog.info("[SPP] System property " + NON_PROXY_HOSTS_KEY + " unchanged: '" + updatedNonProxyHosts + "'");
			}
			else {
				mLog.info(
					"[SPP] System property " + NON_PROXY_HOSTS_KEY + " updated from " +
					(nonProxyHosts == null ? "null" : "'" + nonProxyHosts + "'") +
					" to '" + updatedNonProxyHosts + "'");
			}
		}
	}

	/**
	 * Initializes Axis type descriptors for WSRP web service stubs.
	 * @see #initAxisWSTypeDescriptors(String)
	 * 
	 */
	void initAxisWSTypeDescriptors() {
		initAxisWSTypeDescriptors("oasis.names.tc.wsrp.v1.bind.WSRP_v1_Markup_Binding_SOAPStub");
		initAxisWSTypeDescriptors("oasis.names.tc.wsrp.v1.bind.WSRP_v1_PortletManagement_Binding_SOAPStub");
		initAxisWSTypeDescriptors("oasis.names.tc.wsrp.v1.bind.WSRP_v1_Registration_Binding_SOAPStub");
		initAxisWSTypeDescriptors("oasis.names.tc.wsrp.v1.bind.WSRP_v1_ServiceDescription_Binding_SOAPStub");
		initAxisWSTypeDescriptors("com.hp.runtime.ugs.asl.service.UGSRuntimeServiceXfireImplHttpBindingStub");
		initAxisWSTypeDescriptors("com.hp.spp.webservice.ups.client.UPSWebServicesSoapBindingStub");
		initAxisWSTypeDescriptors("com.hp.spp.webservice.eservice.client.EServiceManagerSoapBindingStub");
	}

	/**
	 * Initializes Axis type descriptors for web service stub defined in <tt>wsrpStubClassName</tt>
	 * class.
	 * The reason for the manual initialization is the following. With WSRP we noticed that sometimes
	 * SAXException is thrown during web service call, saying that the an element is invalid, even
	 * though the content of SOAP envelope was created using Axis stub. The problem was not fixed,
	 * however it was narrowed down to a synchronization issue during stub creation. The Axis stubs
	 * store type mappings in a static non synchronized map that is initialized during the first creation
	 * of a stub object. If type mapping initialization is performed by many stub objects created in parallel,
	 * it sometimes leads to incosistent state resulting in the SAXException. To prevent this multi-threading
	 * issue, the stubs are initalized in the application listener which is garanteed to be single-thread.
	 *
	 * @param wsrpStubClassName fully qualified name of the stub class.
	 */
	void initAxisWSTypeDescriptors(String wsrpStubClassName) {
		if (mLog.isInfoEnabled()) {
			mLog.info("Intializing WSRP type descriptors for " + wsrpStubClassName);
		}
		try {
			Class wsrpStubClass = Class.forName(wsrpStubClassName);
			Object wsrpStubInstance = wsrpStubClass.newInstance();
			Method createCallMethod = wsrpStubClass.getDeclaredMethod("createCall", null);
			createCallMethod.setAccessible(true);
			createCallMethod.invoke(wsrpStubInstance, null);
			createCallMethod.setAccessible(false);
		}
		catch (Exception e) {
			mLog.error("Error occured while initializing WSRP type descriptors", e);
		}
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
}
