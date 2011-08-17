package com.hp.spp.filters.access;

import junit.framework.TestCase;

import javax.servlet.http.HttpSession;

public class UrlAccessRuleTest extends TestCase {

	public void testMatchesSimplePatterns() throws Exception {
		TestRule r1 = new TestRule("/portal/console/*");
		assertTrue("Matching /portal/console/ctx_id.fd92f183f1c319fbc9d2cd8640108a0c/flg_clrctxst.1/",
				r1.matches("/portal/console/ctx_id.fd92f183f1c319fbc9d2cd8640108a0c/flg_clrctxst.1/"));
		assertFalse("Matching /portal/console", r1.matches("/portal/console"));
		assertFalse("Matching /portal/site/sppqa", r1.matches("/portal/site/sppqa"));

		TestRule r2 = new TestRule("/portal/console*");
		assertTrue("Matching /portal/console/abc", r2.matches("/portal/console/abc"));
		assertTrue("Matching /portal/console", r2.matches("/portal/console"));
		assertTrue("Matching /portal/consolexxx", r2.matches("/portal/consolexxx"));
	}

	public void testMatchesRegexPatterns() throws Exception {
		TestRule r1 = new TestRule("re:/portal/console/[^/]+$");
		assertTrue("Matching /portal/console/abc", r1.matches("/portal/console/abc"));
		assertFalse("Matching /portal/console/abc/def", r1.matches("/portal/console/abc/def"));
	}

	private class TestRule extends UrlAccessRule {
		public TestRule(String urlPattern) {
			super(urlPattern);
		}

		public boolean isAccessAllowed(HttpSession session) {
			return false;
		}

		public boolean isLoginRequired() {
			return false;
		}
	}
}
