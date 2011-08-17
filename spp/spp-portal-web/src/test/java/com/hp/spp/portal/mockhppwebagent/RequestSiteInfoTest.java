package com.hp.spp.portal.mockhppwebagent;

import junit.framework.TestCase;

public class RequestSiteInfoTest extends TestCase {

	public void testGetRequestSiteInfo() throws Exception {
		RequestSiteInfo siteInfo;

		siteInfo = RequestSiteInfo.parse("/portal/console");
		assertEquals("Site", "console", siteInfo.getProtectedSiteName());
		assertFalse("Is not public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("/portal/console/c_id.vgn_system_component/d_id.d_consolemenu_frame/");
		assertEquals("Site", "console", siteInfo.getProtectedSiteName());
		assertFalse("Is not public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("/portal/site/console/");
		assertEquals("Site", "console", siteInfo.getProtectedSiteName());
		assertTrue("Is public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("/portal/site/sppdev");
		assertEquals("Site", "sppdev", siteInfo.getProtectedSiteName());
		assertFalse("Is not public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("/portal/site/publicsppdev");
		assertEquals("Site", "sppdev", siteInfo.getProtectedSiteName());
		assertTrue("Is public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("/portal/site/console/template.PRELOGIN/");
		assertEquals("Site", "console", siteInfo.getProtectedSiteName());
		assertTrue("Is public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("/portal/images/misc/spacer.gif");
		assertNull("Site info", siteInfo);

		siteInfo = RequestSiteInfo.parse("http://localhost:7001/portal/console");
		assertNotNull("Site info for complete URL is not null", siteInfo);
		assertEquals("Site", "console", siteInfo.getProtectedSiteName());
		assertFalse("Is not public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("http://localhost:7001/portal/site/sppdev");
		assertNotNull("Site info for complete URL is not null", siteInfo);
		assertEquals("Site", "sppdev", siteInfo.getProtectedSiteName());
		assertFalse("Is not public", siteInfo.isPublic());

		siteInfo = RequestSiteInfo.parse("http://localhost:7001/portal/site/publicsppdev");
		assertNotNull("Site info for complete URL is not null", siteInfo);
		assertEquals("Site", "sppdev", siteInfo.getProtectedSiteName());
		assertTrue("Is public", siteInfo.isPublic());

	}
}
