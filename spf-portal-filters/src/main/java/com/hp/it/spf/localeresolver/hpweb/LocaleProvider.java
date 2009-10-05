/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import java.util.Collection;

/**
 * Provides a collection of locales.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public interface LocaleProvider {
    /**
     * Returns a collection of locales.
     * @return set of locales or empty set.
     */
    Collection getLocales();
}
