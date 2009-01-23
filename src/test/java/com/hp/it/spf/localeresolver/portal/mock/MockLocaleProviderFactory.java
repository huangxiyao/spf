/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.mock;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.core.hpweb.LocaleProvider;
import com.hp.it.spf.localeresolver.core.hpweb.LocaleProviderFactory;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class MockLocaleProviderFactory implements LocaleProviderFactory {

    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return new MockLocaleProvider(request);
    }

}
