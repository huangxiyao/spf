package com.hp.it.spf.xa.portalurl;

import junit.framework.TestCase;

public class VignetteLocalPortalURLTest extends TestCase {

	public void testCreateResourceUrl() throws Exception {
		VignetteLocalPortalURL url;

		url = new VignetteLocalPortalURL(
				"http://my_host:my_port/portal/site/my_site", null,
				"my_folder/my_page", false, -1, -1);
		url.setAsResourceURL("myPortlet",
				"/servlets/my_servlet?some=parameter");
		System.out.println("Resource URL 1: " + url.toString());
		assertEquals(
				"Resource URL 1",
				"/servlets/my_servlet?some=parameter",
				url.toString());

		url = new VignetteLocalPortalURL(
				"http://16.158.82.187:7001/portal/site/acme-athp/", null,
				"menuitem.47ec6b8a5377e45ff39bae300a25e901", false, -1, -1);
		url.setAsResourceURL("8f31298db68d493d32f55a10bb25e901",
				"http://16.158.82.187:7002/jsr168portlets/binaryDocDisplay");
		System.out.println("Resource URL 2: " + url.toString());
		assertEquals(
				"Resource URL 2",
				"http://16.158.82.187:7002/jsr168portlets/binaryDocDisplay",
				url.toString());
	}

}
