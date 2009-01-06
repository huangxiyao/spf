/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/hpweb/DefaultLocaleProvider.java,v 1.1 2007/04/03 10:05:14 marcd Exp $ */

package com.hp.fast.web.hpweb;

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
