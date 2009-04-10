/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.filter;

import javax.servlet.ServletException;
import org.springframework.mock.web.MockFilterConfig;

import com.hp.it.spf.localeresolver.portal.filter.SPFLocaleFilter;
import com.hp.it.spf.localeresolver.portal.mock.MockLocaleProviderFactory;
import com.hp.it.spf.localeresolver.portal.mock.MockLocaleSetter;
import com.hp.it.spf.localeresolver.portal.provider.HppHeaderLocaleProviderFactory;

import junit.framework.TestCase;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class SPFLocaleFilterTest extends TestCase {
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
            SPFLocaleFilter filter = new SPFLocaleFilter();
            filter.init(config);
            assertTrue(true);
        } catch (ServletException e) {
            fail("should be no failures");
        }
    }
}
