package com.hp.it.spf.xa.portalurl;

import junit.framework.TestCase;

import java.util.Map;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class AbstractPortalURLTest extends TestCase
{
	public void testCreateBaseUrlSameSiteSameProtocol() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host:my_port/portal/site/my_site", null, "my_folder/my_page", false);
		assertEquals("Render URL",
				"http://my_host:my_port/portal/site/my_site/my_folder/my_page/",
				url.createBaseUrl(false).toString());

		url = new TestURL("http://my_host:my_port/portal/site/my_site/", null, "/my_folder/my_page", false);
		assertEquals("Action URL",
				"http://my_host:my_port/portal/site/my_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlSameSiteDifferentProtocol() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host:my_port/portal/site/my_site", null, "my_folder/my_page", true);
		assertEquals("Render URL",
				"https://my_host:my_port/portal/site/my_site/my_folder/my_page/",
				url.createBaseUrl(false).toString());

		url = new TestURL("https://my_host:my_port/portal/site/my_site/", null, "/my_folder/my_page", false);
		assertEquals("Action URL",
				"http://my_host:my_port/portal/site/my_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlDifferentSiteSameProtocol() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("https://my_host:my_port/portal/site/my_site", "2nd_site", "my_folder/my_page", true);
		assertEquals("Render URL",
				"https://my_host:my_port/portal/site/2nd_site/my_folder/my_page/",
				url.createBaseUrl(false).toString());

		url = new TestURL("http://my_host:my_port/portal/site/my_site/", "another_site", "/my_folder/my_page", false);
		assertEquals("Action URL",
				"http://my_host:my_port/portal/site/another_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlDifferentSiteDifferentProtocol() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host:my_port/portal/site/my_site", "another_site", "my_folder/my_page", true);
		assertEquals("Render URL",
				"https://my_host:my_port/portal/site/another_site/my_folder/my_page/",
				url.createBaseUrl(false).toString());

		url = new TestURL("https://my_host:my_port/portal/site/my_site/", "another_site", "/my_folder/my_page", false);
		assertEquals("Action URL",
				"http://my_host:my_port/portal/site/another_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlWithPortalParameters() throws Exception {
		AbstractPortalURL url;
		
		url = new TestURL("http://my_host:my_port/portal/site/my_site", null, "template.MY_PAGE/action.process/", false);
		String[] values = new String[]{"one", "two"};
		url.setParameter("greeting", "hello world");
		url.setParameter("farewell", "goodbye world");
		url.setParameter("numbers", values);
		assertEquals("Portal URL",
				"http://my_host:my_port/portal/site/my_site/template.MY_PAGE/action.process/?greeting=hello+world&farewell=goodbye+world&numbers=one&numbers=two",
				url.createBaseUrl(false).toString());
	}
	
	private class TestURL extends AbstractPortalURL {

		protected TestURL(String siteRootUrl, String anotherSiteName, String pageFriendlyUri, boolean secure)
		{
			super(siteRootUrl, anotherSiteName, pageFriendlyUri, secure);
		}

		public String urlToString()
		{
			return null;
		}

		protected void addPrivateParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId) {
		}

		protected void addPublicParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId) {
		}
	}
}
