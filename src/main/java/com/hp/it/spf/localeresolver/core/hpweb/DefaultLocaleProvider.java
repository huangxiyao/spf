/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.hpweb;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * A locale provider that returns the default locale for the current JVM.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public final class DefaultLocaleProvider implements LocaleProvider {
    private static final LocaleProvider INSTANCE = new DefaultLocaleProvider();

    /**
     * Returns a static instance of this locale provider.
     * 
     * @return an instance of this locale provider.
     */
    public static LocaleProvider getInstance() {
        return INSTANCE;
    }

    public Collection getLocales() {
        return Collections.singleton(Locale.getDefault());
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return false;
    }

}
