package com.hp.spp.wsrp.url;

import junit.framework.TestCase;

public class FriendlyPageNameBaseUrlComposerTest extends TestCase {

	public void testGetBaseUrl() throws Exception {
		FriendlyPageNameBaseUrlComposer composer;

		composer = new FriendlyPageNameBaseUrlComposer("http://site.com/portal/site/testsite", null, "MyPage", false);
		assertEquals("URL to the same site",
				"http://site.com/portal/site/testsite/?page=MyPage",
				composer.getBaseUrl());

		composer = new FriendlyPageNameBaseUrlComposer("http://site.com/portal/site/testsite", null, "MyPage2", true);
		assertEquals("Secure URL to the same site",
				"https://site.com/portal/site/testsite/?page=MyPage2",
				composer.getBaseUrl());

		composer = new FriendlyPageNameBaseUrlComposer("http://site.com/portal/site/testsite", "anothersite", "MyPage", false);
		assertEquals("URL to another site",
				"http://site.com/portal/site/anothersite/?page=MyPage",
				composer.getBaseUrl());

		composer = new FriendlyPageNameBaseUrlComposer("https://site.com/portal/site/testsite/", "anothersite", "MyPage2", true);
		assertEquals("Secure URL to another site",
				"https://site.com/portal/site/anothersite/?page=MyPage2",
				composer.getBaseUrl());
	}
}
