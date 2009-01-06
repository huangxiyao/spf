/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: HpCookieLocalSetterTest.java
 * PACKAGE : com.hp.it.spf.localeresolver.filter
 * $Id: HpDomainCookieLocalSetterTest.java,v 1.1 2007/09/30 03:09:08 marcd Exp $
 * $Log: HpDomainCookieLocalSetterTest.java,v $
 * Revision 1.1  2007/09/30 03:09:08  marcd
 * add cookie for clarion interop
 *
 * Revision 1.3  2007/09/30 02:35:00  marcd
 * add cookie for clarion interop
 *
 * Revision 1.2  2007/04/17 01:52:04  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.spf.localeresolver.setter;

import java.util.Locale;
import javax.servlet.http.Cookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.hp.it.spf.localeresolver.setter.HpDomainCookieLocaleSetter;

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
