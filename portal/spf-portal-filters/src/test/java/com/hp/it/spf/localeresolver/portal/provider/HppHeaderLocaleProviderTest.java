/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.provider;

import java.util.Collection;
import java.util.Locale;

import com.hp.it.spf.localeresolver.portal.provider.HppHeaderLocaleProvider;
import com.hp.it.spf.xa.misc.portal.Consts;

import javax.servlet.http.Cookie;
import org.springframework.mock.web.MockHttpServletRequest;
import junit.framework.TestCase;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class HppHeaderLocaleProviderTest extends TestCase {
    private MockHttpServletRequest request;
    private static final String JAPANESE = "ja";
    private static final String HPP_TAIWAN_CODE = "12";

    protected void setUp() {
        this.request = new MockHttpServletRequest();
        Cookie[] cookies = {new Cookie(Consts.COOKIE_NAME_SMSESSION, "value")};
        request.setCookies(cookies);
    }

    public void testResolveLocaleWhenInputLocaleHasCountryDesignated() {
        HppHeaderLocaleProvider resolver = new HppHeaderLocaleProvider(request);
        resolver.setPreferredLanguageExtractor(JAPANESE);

        Collection locales = resolver.getLocales();
        assertNotNull(locales);
        assertEquals(1, locales.size());
        Locale locale = (Locale) locales.iterator().next();
        assertEquals(JAPANESE, locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    public void testGetLanguageChinese() {
    	HppHeaderLocaleProvider provider = new HppHeaderLocaleProvider(request);
    	provider.setPreferredLanguageExtractor(HPP_TAIWAN_CODE);
    	String actual = provider.getLanguage();
    	assertEquals("zh", actual);
    }
    
    public void testGetCountryChinese() {
    	HppHeaderLocaleProvider provider = new HppHeaderLocaleProvider(request);
    	provider.setPreferredLanguageExtractor(HPP_TAIWAN_CODE);
    	String actual = provider.getCountry();
    	assertEquals("tw", actual);
    }
    
    /* We test for the user being logged in by seeing if the smsession cookie
     * is present.  We cant just test to see if the sm headers are present 
     * because site minder is currently adding headers whether we are logged
     * in or not.  For instance this is the header at the login page
     * preferredlanguage=en
     *|hpclname=ServicePortalPublicPassport
     *|hpclidnumber=fa2144ff60f6413d1c03c4905203a9b8
     *|hpresidentcountrycode=US
     *|sn=PublicPassport
     *|givenname=SP
     *|email=ServicePortalPublicPassport@hp.com
     *|createtimestamp=2007-05-24 10:57:21
     *|modifytimestamp=2007-05-24 10:57:21
     *|clang=US-en
     */
    public void testUserNotLoggedIn() {
        request.setCookies(null);
        HppHeaderLocaleProvider provider = new HppHeaderLocaleProvider(request);
        String actual = provider.getCountry();
        assertNull(actual);
        actual = provider.getLanguage();
        assertNull(actual);
    }
}
