package com.hp.it.spf.xa.portalurl;

import junit.framework.TestCase;

public class VignetteRemotePortalURLTest extends TestCase {

	public void testCreateResourceUrl() throws Exception {
		VignetteRemotePortalURL url;

		url = new VignetteRemotePortalURL(
				"http://my_host:my_port/portal/site/my_site", null,
				"my_folder/my_page", false, -1, -1);
		url.setAsResourceURL("myPortlet",
				"http://producer.com:12345/servlets/my_servlet?some=parameter");
		System.out.println("Resource URL 1: " + url.toString());
		assertEquals(
				"Resource URL 1",
				"http://my_host:my_port/portal/site/my_site/template.BINARYPORTLET/my_folder/my_page/resource.process/?spf_p.tpst=myPortlet_ws_BI&spf_p.rst_myPortlet=wsrp-url%3Dhttp%253A%252F%252Fproducer.com%253A12345%252Fservlets%252Fmy_servlet%253Fsome%253Dparameter%26wsrp-requiresRewrite%3Dfalse&javax.portlet.begCacheTok=com.vignette.cachetoken&javax.portlet.endCacheTok=com.vignette.cachetoken",
				url.toString());

		url = new VignetteRemotePortalURL(
				"http://16.158.82.187:7001/portal/site/acme-athp/", null,
				"menuitem.47ec6b8a5377e45ff39bae300a25e901", false, -1, -1);
		url.setAsResourceURL("8f31298db68d493d32f55a10bb25e901",
				"http://16.158.82.187:7002/jsr168portlets/binaryDocDisplay");
		System.out.println("Resource URL 2: " + url.toString());
		assertEquals(
				"Resource URL 2",
				"http://16.158.82.187:7001/portal/site/acme-athp/template.BINARYPORTLET/menuitem.47ec6b8a5377e45ff39bae300a25e901/resource.process/?spf_p.tpst=8f31298db68d493d32f55a10bb25e901_ws_BI&spf_p.rst_8f31298db68d493d32f55a10bb25e901=wsrp-url%3Dhttp%253A%252F%252F16.158.82.187%253A7002%252Fjsr168portlets%252FbinaryDocDisplay%26wsrp-requiresRewrite%3Dfalse&javax.portlet.begCacheTok=com.vignette.cachetoken&javax.portlet.endCacheTok=com.vignette.cachetoken",
				url.toString());
	}
}
