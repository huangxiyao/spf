/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.setter;

import java.util.Locale;
import javax.servlet.http.Cookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.hp.it.spf.localeresolver.portal.setter.HpDomainCookieLocaleSetter;

import junit.framework.TestCase;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class HpDomainCookieLocalSetterTest extends TestCase {

    public void testSetLocaleForJustLanguageLocale() {
        HpDomainCookieLocaleSetter setter = new HpDomainCookieLocaleSetter("test");
        MockHttpServletResponse resp = new MockHttpServletResponse();

        setter.setLocale(new MockHttpServletRequest(), resp, Locale.ENGLISH);
        assertEquals(1, resp.getCookies().length);
        Cookie[] cookies = resp.getCookies();
        assertEquals("test", cookies[0].getName());
        assertEquals("en", cookies[0].getValue());
    }
    
    public void testShouldRenderFullFormOfLocale() {
        HpDomainCookieLocaleSetter setter = new HpDomainCookieLocaleSetter("test");
        MockHttpServletResponse resp = new MockHttpServletResponse();

        setter.setLocale(new MockHttpServletRequest(), resp, Locale.TAIWAN);
        assertEquals(1, resp.getCookies().length);
        Cookie[] cookies = resp.getCookies();
        assertEquals("test", cookies[0].getName());
        assertEquals("zh-TW", cookies[0].getValue());
    }
}
