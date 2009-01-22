/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.hpweb;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author marc derosa
 *
 */
public class NullLocaleProvider implements LocaleProvider {

    public Collection getLocales() {
        return new ArrayList();
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return false;
    }

}
