package com.hp.it.spf.xa.misc;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPortalSiteURL() throws Exception {
    	String url = Utils.getPortalSiteURL(null, null, null, 0, null);
    	Boolean t = new Boolean(true);
    	Boolean f = new Boolean (false);
    	System.out.println("testGetPortalSiteURL.1 got: " + url);
    	assertEquals(null, url);
    	url = Utils.getPortalSiteURL("", f, "", 8002, "");
    	System.out.println("testGetPortalSiteURL.2 got: " + url);
    	assertEquals("", url);
    	url = Utils.getPortalSiteURL("/portal/site/abc/template.PAGE", null, null, 0, null);
    	System.out.println("testGetPortalSiteURL.3 got: " + url);
    	assertEquals("/portal/site/abc/template.PAGE", url);
    	url = Utils.getPortalSiteURL("/portal/site/abc/template.PAGE", t, "host", 8002, null);
    	System.out.println("testGetPortalSiteURL.4 got: " + url);
    	assertEquals("/portal/site/abc/template.PAGE", url);
    	url = Utils.getPortalSiteURL("/portal/site/abc/template.PAGE", null, null, 0, "");
    	System.out.println("testGetPortalSiteURL.5 got: " + url);
    	assertEquals("/portal/site/abc/template.PAGE", url);
    	url = Utils.getPortalSiteURL("/portal/site/abc/template.PAGE", t, "host", 8002, "foo");
    	System.out.println("testGetPortalSiteURL.6 got: " + url);
    	assertEquals("/portal/site/foo/", url);
    	url = Utils.getPortalSiteURL("/portal/site/abc/template.PAGE", t, "host", 0, "foo/template.ANOTHER_PAGE/?a=b");
    	System.out.println("testGetPortalSiteURL.7 got: " + url);
    	assertEquals("/portal/site/foo/template.ANOTHER_PAGE/?a=b", url);

    	url = Utils.getPortalSiteURL("http://host", null, null, 0, "foo/unused");
    	System.out.println("testGetPortalSiteURL.8 got: " + url);
    	assertEquals("http://host", url);
    	url = Utils.getPortalSiteURL("http://host", t, "another", 8080, "foo/unused");
    	System.out.println("testGetPortalSiteURL.9 got: " + url);
    	assertEquals("https://another:8080", url);
    	url = Utils.getPortalSiteURL("https://host:7001", f, "", 8080, "foo/unused");
    	System.out.println("testGetPortalSiteURL.10 got: " + url);
    	assertEquals("http://host:8080", url);
    	url = Utils.getPortalSiteURL("https://host:7001", f, "another", 7001, "foo/unused");
    	System.out.println("testGetPortalSiteURL.11 got: " + url);
    	assertEquals("http://another:7001", url);
    	url = Utils.getPortalSiteURL("https://host:7001/", null, null, 443, "foo/unused");
    	System.out.println("testGetPortalSiteURL.12 got: " + url);
    	assertEquals("https://host/", url);
    	url = Utils.getPortalSiteURL("https://host:7001/", f, null, 80, "foo/unused");
    	System.out.println("testGetPortalSiteURL.13 got: " + url);
    	assertEquals("http://host/", url);
    	url = Utils.getPortalSiteURL("https://host:7001/something", f, "another", 81, "foo/unused");
    	System.out.println("testGetPortalSiteURL.14 got: " + url);
    	assertEquals("http://another:81/something", url);
    	url = Utils.getPortalSiteURL("https://host/something", f, "another", 81, "foo/unused");
    	System.out.println("testGetPortalSiteURL.15 got: " + url);
    	assertEquals("http://another:81/something", url);
    	url = Utils.getPortalSiteURL("http://host/", t, "another", 81, "foo/unused");
    	System.out.println("testGetPortalSiteURL.16 got: " + url);
    	assertEquals("https://another:81/", url);

    	url = Utils.getPortalSiteURL("http://host/portal/site/abc/template.PAGE?", t, "another", 443, null);
    	System.out.println("testGetPortalSiteURL.17 got: " + url);
    	assertEquals("https://another/portal/site/abc/template.PAGE?", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/abc", null, null, 443, "/foo/something");
    	System.out.println("testGetPortalSiteURL.18 got: " + url);
    	assertEquals("http://host:443/portal/site/abc/foo/something", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/abc/", null, null, 443, "/foo/something");
    	System.out.println("testGetPortalSiteURL.19 got: " + url);
    	assertEquals("http://host:443/portal/site/abc/foo/something", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/abc/remove", null, null, 0, "/foo/something");
    	System.out.println("testGetPortalSiteURL.20 got: " + url);
    	assertEquals("http://host/portal/site/abc/foo/something", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/", t, "another", 8080, "/foo/something");
    	System.out.println("testGetPortalSiteURL.21 got: " + url);
    	assertEquals("https://another:8080/portal/site/", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/", t, "another", 8080, "foo/something");
    	System.out.println("testGetPortalSiteURL.22 got: " + url);
    	assertEquals("https://another:8080/portal/site/foo/something", url);

    	url = Utils.getPortalSiteURL("http://host:12345/something", t, null, 0, null);
    	System.out.println("testGetPortalSiteURL.23 got: " + url);
    	assertEquals("https://host/something", url);
    	url = Utils.getPortalSiteURL("https://host:12345/something", f, null, 0, null);
    	System.out.println("testGetPortalSiteURL.24 got: " + url);
    	assertEquals("http://host/something", url);
    	
    	url = Utils.getPortalSiteURL("http://host:333/portal/site/sitename?a=b", null, null, 0, "/friendly/url");
    	System.out.println("testGetPortalSiteURL.25 got: " + url);
    	assertEquals("http://host:333/portal/site/sitename/friendly/url", url);
    	url = Utils.getPortalSiteURL("https://host/portal/site/sitename/some/friendly/url/?a=b", f, null, 81, "/other/friendly/url/?c=d");
    	System.out.println("testGetPortalSiteURL.26 got: " + url);
    	assertEquals("http://host:81/portal/site/sitename/other/friendly/url/?c=d", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/site1?a=b", f, null, -1, "site2?c=d");
    	System.out.println("testGetPortalSiteURL.27 got: " + url);
    	assertEquals("http://host/portal/site/site2/?c=d", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/site1?a=b", f, null, -1, "site2/?c=d");
    	System.out.println("testGetPortalSiteURL.28 got: " + url);
    	assertEquals("http://host/portal/site/site2/?c=d", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/site1/?a=b", f, null, -1, "site2?c=d");
    	System.out.println("testGetPortalSiteURL.29 got: " + url);
    	assertEquals("http://host/portal/site/site2/?c=d", url);
    	url = Utils.getPortalSiteURL("http://host/portal/site/site1/path1/?a=b", f, null, -1, "site2/path2?c=d");
    	System.out.println("testGetPortalSiteURL.30 got: " + url);
    	assertEquals("http://host/portal/site/site2/path2?c=d", url);
    }

}
