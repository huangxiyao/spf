/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.core.http;

import java.util.Locale;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.hp.it.spf.localeresolver.core.http.LanguageNegotiator;
import com.hp.it.spf.localeresolver.core.http.NoAcceptableLanguageException;

public class LanguageNegotiatorTest extends TestCase {
	private MockHttpServletRequest request;

	private MockHttpServletResponse response;

	public void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	public void testAcceptLanguageMatchesProvidedLanguage()
			throws NoAcceptableLanguageException {
		LanguageNegotiator ln = new LanguageNegotiator(new Object[] {
				Locale.GERMAN, Locale.CANADA_FRENCH, });
		request.addPreferredLocale(Locale.GERMAN);
		ln.negotiate(request, response);

		assertEquals(Locale.GERMAN, ln.negotiatedValue(request));
		assertEquals(Locale.GERMAN, response.getLocale());
	}

	public void testAcceptLanguageCountryMatchesProvidedLanguageCountry()
			throws NoAcceptableLanguageException {
		LanguageNegotiator ln = new LanguageNegotiator(new Object[] {
				Locale.GERMAN, Locale.FRENCH, Locale.CANADA_FRENCH, });
		request.addPreferredLocale(Locale.CANADA_FRENCH);
		ln.negotiate(request, response);

		assertEquals(Locale.CANADA_FRENCH, ln.negotiatedValue(request));
		assertEquals(Locale.CANADA_FRENCH, response.getLocale());
	}

	public void testAcceptLanguageMatchesProvidedLanguageCountry()
			throws NoAcceptableLanguageException {
		try {
			LanguageNegotiator ln = new LanguageNegotiator(new Object[] {
					Locale.GERMAN, Locale.CANADA_FRENCH });
			request.addPreferredLocale(Locale.FRENCH);
			ln.negotiate(request, response);
			fail ("Canada French doesn't match French");
		} catch  (NoAcceptableLanguageException e) {
			assertTrue(true);
		}
	}

	public void testAcceptLanguageCountryMatchesProvidedLanguage()
			throws NoAcceptableLanguageException {
		LanguageNegotiator ln = new LanguageNegotiator(new Object[] {
				Locale.GERMAN, Locale.FRENCH });
		request.addPreferredLocale(Locale.CANADA_FRENCH);
		ln.negotiate(request, response);

		assertEquals(Locale.FRENCH, ln.negotiatedValue(request));
		assertEquals(Locale.FRENCH, response.getLocale());
	}

	public void testNoMatch() {
		LanguageNegotiator ln = new LanguageNegotiator(
				new Object[] { Locale.FRENCH });
		request.addPreferredLocale(Locale.GERMAN);
		try {
			ln.negotiate(request, response);
			fail("NoAcceptableLanguageException expected.");
		} catch (NoAcceptableLanguageException ex) {
		}

		assertNull(ln.negotiatedValue(request));
		assertEquals(new MockHttpServletResponse().getLocale(), response
				.getLocale());
	}

}
