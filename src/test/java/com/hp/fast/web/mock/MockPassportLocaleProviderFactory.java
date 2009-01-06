/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.fast.web.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.fast.web.hpweb.LocaleProvider;
import com.hp.fast.web.hpweb.LocaleProviderFactory;

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
