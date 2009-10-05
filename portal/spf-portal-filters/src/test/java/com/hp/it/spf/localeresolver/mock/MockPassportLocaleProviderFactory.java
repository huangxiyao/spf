/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.hpweb.LocaleProvider;
import com.hp.it.spf.localeresolver.hpweb.LocaleProviderFactory;

/**
 * 
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class MockPassportLocaleProviderFactory implements LocaleProviderFactory {
    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return new LocaleProvider() {

            public Collection getLocales() {
                return Collections.singleton(new Locale("en", "US"));
            }

        };
    }

}
