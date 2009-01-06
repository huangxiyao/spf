/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
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
