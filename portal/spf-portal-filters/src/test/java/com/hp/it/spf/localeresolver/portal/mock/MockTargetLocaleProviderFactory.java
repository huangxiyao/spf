/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.mock;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProvider;
import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProviderFactory;

/**
 * @author Scott Jorgenson
 */
public class MockTargetLocaleProviderFactory implements TargetLocaleProviderFactory {

    public TargetLocaleProvider getTargetLocaleProvider(HttpServletRequest request) {
        return new MockTargetLocaleProvider(request);
    }

}
