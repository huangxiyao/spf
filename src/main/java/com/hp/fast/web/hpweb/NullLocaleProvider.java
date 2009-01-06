package com.hp.fast.web.hpweb;

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
