/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * A locale provider factory that returns a locale provider that returns the
 * default locale for the current JVM.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
final class DefaultLocaleProviderFactory implements LocaleProviderFactory {
    private static final LocaleProvider INSTANCE = new DefaultLocaleProvider();

    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return INSTANCE;
    }
}
