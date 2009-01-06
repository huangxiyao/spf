/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/test/java/com/hp/fast/web/mock/MockPassportLocaleProviderFactory.java,v 1.1 2007/04/03 10:05:11 marcd Exp $ */

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
