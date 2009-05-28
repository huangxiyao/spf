package com.hp.it.spf.xa.portalurl;

import junit.framework.TestCase;

import java.util.Map;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class AbstractPortalURLTest extends TestCase {
	public void testCreateBaseUrlSameSiteSameProtocol() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host:my_port/portal/site/my_site", null,
				"my_folder/my_page", false, -1, -1);
		assertEquals(
				"Render URL",
				"http://my_host:my_port/portal/site/my_site/my_folder/my_page/",
				url.createBaseUrl(false).toString());

		url = new TestURL("http://my_host:my_port/portal/site/my_site/", null,
				"/my_folder/my_page", false, -1, -1);
		assertEquals(
				"Action URL",
				"http://my_host:my_port/portal/site/my_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlSameSiteDifferentProtocol() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host:my_port/portal/site/my_site", null,
				"my_folder/my_page", true, -1, -1);
		assertEquals("Render URL",
				"https://my_host/portal/site/my_site/my_folder/my_page/", url
						.createBaseUrl(false).toString());

		url = new TestURL("https://my_host:my_port/portal/site/my_site/", null,
				"/my_folder/my_page", false, -1, -1);
		assertEquals(
				"Action URL",
				"http://my_host/portal/site/my_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlDifferentSiteSameProtocol() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("https://my_host:my_port/portal/site/my_site",
				"2nd_site", "my_folder/my_page", true, -1, -1);
		assertEquals(
				"Render URL",
				"https://my_host:my_port/portal/site/2nd_site/my_folder/my_page/",
				url.createBaseUrl(false).toString());

		url = new TestURL("http://my_host:my_port/portal/site/my_site/",
				"another_site", "/my_folder/my_page", false, -1, -1);
		assertEquals(
				"Action URL",
				"http://my_host:my_port/portal/site/another_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlDifferentSiteDifferentProtocol()
			throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host:my_port/portal/site/my_site",
				"another_site", "my_folder/my_page", true, -1, -1);
		assertEquals("Render URL",
				"https://my_host/portal/site/another_site/my_folder/my_page/",
				url.createBaseUrl(false).toString());

		url = new TestURL("https://my_host:my_port/portal/site/my_site/",
				"another_site", "/my_folder/my_page", false, -1, -1);
		assertEquals(
				"Action URL",
				"http://my_host/portal/site/another_site/template.PAGE/action.process/my_folder/my_page/",
				url.createBaseUrl(true).toString());
	}

	public void testCreateBaseUrlWithPortalParameters() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host:my_port/portal/site/my_site", null,
				"template.MY_PAGE/action.process/", false, -1, -1);
		String[] values = new String[] { "one", "two" };
		url.setParameter("greeting", "hello world");
		url.setParameter("farewell", "goodbye world");
		url.setParameter("numbers", values);
		assertEquals(
				"Portal URL",
				"http://my_host:my_port/portal/site/my_site/template.MY_PAGE/action.process/?greeting=hello+world&farewell=goodbye+world&numbers=one&numbers=two",
				url.createBaseUrl(false).toString());
	}

	public void testCreateBaseUrlFromRequestUrl() throws Exception {
		AbstractPortalURL url;

		url = new TestURL(
				"http://my_host:my_port/portal/site/my_site/some/extra/path?and=stuff",
				null, null, false, -1, -1);
		assertEquals("Portal URL with no friendly URI",
				"http://my_host:my_port/portal/site/my_site/", url
						.createBaseUrl(false).toString());
		url = new TestURL(
				"http://my_host:my_port/portal/site/my_site/some/extra/path?and=stuff",
				null, "/template.MY_PAGE/extra/path", false, -1, -1);
		assertEquals(
				"Portal URL with no friendly URI",
				"http://my_host:my_port/portal/site/my_site/template.MY_PAGE/extra/path/",
				url.createBaseUrl(false).toString());
		url = new TestURL("http://my_host/portal/site/my_site?a=b", null, null,
				false, -1, -1);
		assertEquals(
				"Portal URL with no friendly URI, based on site root URL ending with query string",
				"http://my_host/portal/site/my_site/", url.createBaseUrl(false)
						.toString());
		url = new TestURL("http://my_host/portal/site/my_site#anchor", null,
				null, false, -1, -1);
		assertEquals(
				"Portal URL with no friendly URI, based on site root URL ending with anchor",
				"http://my_host/portal/site/my_site/", url.createBaseUrl(false)
						.toString());
		url = new TestURL("http://my_host/portal/site/my_site#anchor?a=b/c", null,
				null, false, -1, -1);
		assertEquals(
				"Portal URL with no friendly URI, based on site root URL ending with anchor and query string",
				"http://my_host/portal/site/my_site/", url.createBaseUrl(false)
						.toString());

	}

	public void testCreateBaseUrlWithNonstandardPort() throws Exception {
		AbstractPortalURL url;

		url = new TestURL("http://my_host/portal/site/my_site/some/stuff",
				"switched_site", "other/stuff", true, 81, 444);
		assertEquals("HTTPS portal URL with port 444",
				"https://my_host:444/portal/site/switched_site/other/stuff/",
				url.createBaseUrl(false).toString());

		url = new TestURL("https://my_host/portal/site/my_site/some/stuff",
				"switched_site", "other/stuff", false, 81, 444);
		assertEquals("HTTP portal URL with port 81",
				"http://my_host:81/portal/site/switched_site/other/stuff/", url
						.createBaseUrl(false).toString());

		url = new TestURL("https://my_host/portal/site/my_site/some/stuff",
				"switched_site", "other/stuff", false, 81, 444);
		assertEquals("HTTP portal URL with port 81",
				"http://my_host:81/portal/site/switched_site/other/stuff/", url
						.createBaseUrl(false).toString());

		url = new TestURL("http://my_host:81/portal/site/my_site/some/stuff",
				"switched_site", "other/stuff", true, 81, 444);
		assertEquals("HTTPS portal URL with port 444 (previous port exists)",
				"https://my_host:444/portal/site/switched_site/other/stuff/",
				url.createBaseUrl(false).toString());

		url = new TestURL("http://my_host:82/portal/site/my_site/some/stuff",
				"switched_site", "other/stuff", false, 81, 444);
		assertEquals("HTTP portal URL - same scheme, no change",
				"http://my_host:82/portal/site/switched_site/other/stuff/", url
						.createBaseUrl(false).toString());

		url = new TestURL("https://my_host:445/portal/site/my_site/some/stuff",
				"switched_site", "other/stuff", true, 81, 444);
		assertEquals("HTTPS portal URL - same scheme, no change",
				"https://my_host:445/portal/site/switched_site/other/stuff/",
				url.createBaseUrl(false).toString());
	}

	public void testInvalidSiteRootUrl() throws Exception {
		AbstractPortalURL url;
		Exception e = null;

		try {
			url = new TestURL("bad_url", "my_site", "my_page", false, -1, -1);
		} catch (Exception x) {
			e = x;
		}
		assertEquals("Neither relative nor absolute URL", true,
				(e != null && e instanceof IllegalArgumentException));

		try {
			url = new TestURL("ftp://bad_url/portal/site/bad", null, null,
					false, -1, -1);
			e = null;
		} catch (Exception x) {
			e = x;
		}
		assertEquals("Absolute URL not HTTP or HTTPS", true,
				(e != null && e instanceof IllegalArgumentException));

		try {
			url = new TestURL("http://", null, null, false, -1, -1);
			e = null;
		} catch (Exception x) {
			e = x;
		}
		assertEquals("URL with no host or path", true,
				(e != null && e instanceof IllegalArgumentException));

		try {
			url = new TestURL("https:///bad_url", null, null, false, -1, -1);
			e = null;
		} catch (Exception x) {
			e = x;
		}
		assertEquals("URL with no host", true,
				(e != null && e instanceof IllegalArgumentException));

		try {
			url = new TestURL("http://bad_url", null, null, false, -1, -1);
			e = null;
		} catch (Exception x) {
			e = x;
		}
		assertEquals("URL with no path", true,
				(e != null && e instanceof IllegalArgumentException));

		try {
			url = new TestURL("http://host/bad_url/", null, null, false, -1, -1);
			e = null;
		} catch (Exception x) {
			e = x;
		}
		assertEquals("URL with no /site/", true,
				(e != null && e instanceof IllegalArgumentException));

		try {
			url = new TestURL("https://host/bad_url/site/", null, null, false,
					-1, -1);
			e = null;
		} catch (Exception x) {
			e = x;
		}
		assertEquals("URL with missing site name", true,
				(e != null && e instanceof IllegalArgumentException));

		try {
			url = new TestURL("https://host_bad_url/site//", null, null, false,
					-1, -1);
			e = null;
		} catch (Exception x) {
			e = x;
		}
		assertEquals("URL with blank site name", true,
				(e != null && e instanceof IllegalArgumentException));
	}

	private class TestURL extends AbstractPortalURL {

		protected TestURL(String siteRootUrl, String anotherSiteName,
				String pageFriendlyUri, boolean secure,
				int nonStandardHttpPort, int nonStandardHttpsPort) {
			super(siteRootUrl, anotherSiteName, pageFriendlyUri, secure,
					nonStandardHttpPort, nonStandardHttpsPort);
		}

		public String urlToString() {
			return null;
		}

		protected void addPrivateParameters(StringBuilder result,
				Map.Entry<String, PortletParameters> portletParameters,
				String portletFriendlyId) {
		}

		protected void addPublicParameters(StringBuilder result,
				Map.Entry<String, PortletParameters> portletParameters,
				String portletFriendlyId) {
		}
	}
}
