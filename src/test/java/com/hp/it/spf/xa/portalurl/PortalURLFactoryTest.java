package com.hp.it.spf.xa.portalurl;

import junit.framework.TestCase;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalURLFactoryTest extends TestCase {

	@Override
	protected void tearDown() throws Exception {
		System.getProperties().remove(PortalURLFactory.PROPERTY_NAME);
	}

	public void testCreatesRemoteUrlsDefault() {
		assertEquals("Default setting for mIsLocalPortlet",
				true, PortalURLFactory.createsRemoteUrls("NON_EXISTING_FILE"));
	}

	public void testCreatesRemoteUrlsSystemProperty() {
		System.setProperty(PortalURLFactory.PROPERTY_NAME, "false");
		assertEquals("Uses system property if defined",
				false, PortalURLFactory.createsRemoteUrls("NON_EXISTING_FILE"));
	}

	public void testCreatesRemoteUrlsPropertyFile() {
		assertEquals("Uses property file value if defined",
				false, PortalURLFactory.createsRemoteUrls("/test_portalurl.properties"));

		System.setProperty(PortalURLFactory.PROPERTY_NAME, "true");
		assertEquals("Uses system property if both property file and system property defined",
				true, PortalURLFactory.createsRemoteUrls("/test_portalurl.properties"));
	}
}
