package com.hp.it.spf.xa.portalurl;

import junit.framework.TestCase;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalURLFactoryTest extends TestCase {

	@Override
	protected void tearDown() throws Exception {
		System.getProperties().remove(PortalURLFactory.REMOTE_PROPERTY_NAME);
	}

	public void testCreatesRemoteUrlsDefault() {
		assertEquals("Default setting for mIsLocalPortlet", true,
				PortalURLFactory.createsRemoteUrls("NON_EXISTING_FILE"));
	}

	public void testCreatesRemoteUrlsSystemProperty() {
		System.setProperty(PortalURLFactory.REMOTE_PROPERTY_NAME, "false");
		assertEquals("Uses system property if defined", false, PortalURLFactory
				.createsRemoteUrls("NON_EXISTING_FILE"));
	}

	public void testCreatesRemoteUrlsPropertyFile() {
		assertEquals("Uses property file value if defined", false,
				PortalURLFactory.createsRemoteUrls("test_portalurl.properties"));

		System.setProperty(PortalURLFactory.REMOTE_PROPERTY_NAME, "true");
		assertEquals(
				"Uses system property if both property file and system property defined",
				true, PortalURLFactory
						.createsRemoteUrls("test_portalurl.properties"));
	}

	public void testCreatesWithNonstandardHttpPort() {
		assertEquals("Uses property file value if defined != 80", 82,
				PortalURLFactory
						.getNonstandardHttpPort("test_portalurl.properties"));

		System.setProperty(PortalURLFactory.HTTP_PORT_PROPERTY_NAME, "" + 83);
		assertEquals(
				"Uses system property over property file if defined != 80", 83,
				PortalURLFactory
						.getNonstandardHttpPort("test_portalurl.properties"));

		System.setProperty(PortalURLFactory.HTTP_PORT_PROPERTY_NAME, "" + 80);
		assertEquals(
				"Uses property file value when defined != 80 and system property defined == 80",
				82, PortalURLFactory
						.getNonstandardHttpPort("test_portalurl.properties"));
	}
}
