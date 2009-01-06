/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: HpPassportLocaleSetterTest.java
 * PACKAGE : com.hp.it.spf.localeresolver.filter
 * $Id: HpPassportLocaleSetterTest.java,v 1.2 2007/04/17 01:52:04 marcd Exp $
 * $Log: HpPassportLocaleSetterTest.java,v $
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

import com.hp.it.spf.localeresolver.mock.SurrogatePassportService;
import com.hp.it.spf.localeresolver.setter.HpPassportLocaleSetter;

import junit.framework.TestCase;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class HpPassportLocaleSetterTest extends TestCase {
    private HpPassportLocaleSetter setter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    protected void setUp() {
        setter = new HpPassportLocaleSetter(new SurrogatePassportService());
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    public void testSetLocaleForUserNotLoggedIn() {
        setter.setLocale(request, response, Locale.JAPAN);
        assertEquals(false, ((SurrogatePassportService) this.setter
                .getPassportService()).isModifiedUser());
    }

    public void testSetLocaleForUserLoggedInNotExpired() {
        Cookie[] cookies = new Cookie[] { new Cookie("SMSESSION",
                "00ddeidieje3wuu3334"), };
        request.setCookies(cookies);
        setter.setLocale(request, response, Locale.JAPAN);

        assertEquals(true, ((SurrogatePassportService) this.setter
                .getPassportService()).isModifiedUser());
    }
}
