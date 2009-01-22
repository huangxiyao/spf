/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.core.hpweb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.springframework.mock.web.MockHttpServletRequest;

import com.hp.it.spf.localeresolver.core.hpweb.UserAgentLocaleProvider;

import junit.framework.TestCase;

/**
 * 
 * @author marc derosa
 *
 */
public class UserAgentLocaleProviderTest extends TestCase {
    private MockHttpServletRequest request;
    
    protected void setUp() {
        request = new MockHttpServletRequest();
        request.addPreferredLocale(new Locale("de"));
        request.addPreferredLocale(new Locale("ar", "ma"));
        request.addPreferredLocale(new Locale("ch", "tw"));
    }
    
    /**
     * Test that we maintain the priority order of locales in the
     * request.
     * <p>The proper order is ch, ar, de, en</p>
     */
    public void testUserAgentLocaleProviderLocalePriorityOrder() {
        UserAgentLocaleProvider provider = new UserAgentLocaleProvider(request);
        Collection locales = provider.getLocales();
        Iterator itr = locales.iterator();
        
        int counter = -1;
        while (itr.hasNext()) {
            counter++;
            Locale locale = (Locale) itr.next();
            String expected = null;
            if (counter == 0) {
                expected = "ch";
            } else if (counter == 1) {
                expected = "ar";
            } else if (counter == 2) {
                expected = "de";
            } else if (counter == 3) {
                expected = "en";
            } else {
                fail("unexpected counter value");
            }
            assertEquals(expected, locale.getLanguage());  
        }
    }
}
