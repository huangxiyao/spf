/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.hpweb;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author marc derosa
 *
 */
public class NullLocaleProviderFactory implements LocaleProviderFactory {

    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return new NullLocaleProvider();
    }

}
