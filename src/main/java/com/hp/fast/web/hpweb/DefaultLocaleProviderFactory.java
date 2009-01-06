
package com.hp.fast.web.hpweb;

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

    /**
     * @author Quintin May
     */
    static class DefaultLocaleProvider implements LocaleProvider {
        public Collection getLocales() {
            return Collections.singleton(Locale.getDefault());
        }
    }
}
