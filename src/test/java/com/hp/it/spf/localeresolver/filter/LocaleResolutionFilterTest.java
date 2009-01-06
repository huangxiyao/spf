/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: LocaleResolutionFilterTest.java
 * PACKAGE : com.hp.it.spf.localeresolver.filter
 * $Id: LocaleResolutionFilterTest.java,v 1.2 2007/04/17 01:52:08 marcd Exp $
 * $Log: LocaleResolutionFilterTest.java,v $
 * Revision 1.2  2007/04/17 01:52:08  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.spf.localeresolver.filter;

import javax.servlet.ServletException;
import org.springframework.mock.web.MockFilterConfig;

import com.hp.it.spf.localeresolver.filter.LocaleResolutionFilter;
import com.hp.it.spf.localeresolver.mock.MockLocaleProviderFactory;
import com.hp.it.spf.localeresolver.mock.MockLocaleSetter;
import com.hp.it.spf.localeresolver.provider.HppHeaderLocaleProviderFactory;

import junit.framework.TestCase;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class LocaleResolutionFilterTest extends TestCase {
    private MockFilterConfig config;

    protected void setUp() {
        config = new MockFilterConfig();
        config.addInitParameter("targetLocaleProviderFactory",
                MockLocaleProviderFactory.class.getName());
        config.addInitParameter("defaultLocaleProviderFactory",
                MockLocaleProviderFactory.class.getName());
        config.addInitParameter("passportLocaleProviderFactory",
                HppHeaderLocaleProviderFactory.class.getName());
        config.addInitParameter("userLocaleSetter", MockLocaleSetter.class.getName());
    }

    public void testInit() {
        try {
            LocaleResolutionFilter filter = new LocaleResolutionFilter();
            filter.init(config);
            assertTrue(true);
        } catch (ServletException e) {
            fail("should be no failures");
        }
    }
}
