package com.hp.spp.portal;

import junit.framework.TestCase;

public class PortalContextListenerTest extends TestCase {
	private String mNonProxyHosts;

	protected void setUp() throws Exception {
		mNonProxyHosts = System.getProperty(PortalContextListener.NON_PROXY_HOSTS_KEY);
	}

	protected void tearDown() throws Exception {
		if (mNonProxyHosts == null) {
			System.getProperties().remove(PortalContextListener.NON_PROXY_HOSTS_KEY);
		}
		else {
			System.setProperty(PortalContextListener.NON_PROXY_HOSTS_KEY, mNonProxyHosts);
		}
	}

	public void testSetNonProxyHosts() throws Exception {
		PortalContextListener listener = new PortalContextListener();

		// no property set
		System.getProperties().remove(PortalContextListener.NON_PROXY_HOSTS_KEY);
		listener.setNonProxyHosts();
		assertEquals("System property empty", "localhost|127.0.0.1", System.getProperty(PortalContextListener.NON_PROXY_HOSTS_KEY));

		// property set but no localhost or 127.0.0.1
		System.setProperty(PortalContextListener.NON_PROXY_HOSTS_KEY, "a|Bcd|e");
		listener.setNonProxyHosts();
		assertEquals("System property not empty but no localhost", "a|Bcd|e|localhost|127.0.0.1", System.getProperty(PortalContextListener.NON_PROXY_HOSTS_KEY));

		// only localhost set
		System.setProperty(PortalContextListener.NON_PROXY_HOSTS_KEY, "A|locaLHost|B");
		listener.setNonProxyHosts();
		assertEquals("Only localhost set", "A|locaLHost|B|127.0.0.1", System.getProperty(PortalContextListener.NON_PROXY_HOSTS_KEY));

		// only 127.0.0.1 set
		System.setProperty(PortalContextListener.NON_PROXY_HOSTS_KEY, "127.0.0.1|a|bCd");
		listener.setNonProxyHosts();
		assertEquals("Only 127.0.0.1 set", "127.0.0.1|a|bCd|localhost", System.getProperty(PortalContextListener.NON_PROXY_HOSTS_KEY));

		// both values already set
		System.setProperty(PortalContextListener.NON_PROXY_HOSTS_KEY, "a|localHosT|b|127.0.0.1");
		listener.setNonProxyHosts();
		assertEquals("Both values already set", "a|localHosT|b|127.0.0.1", System.getProperty(PortalContextListener.NON_PROXY_HOSTS_KEY));
	}

}
