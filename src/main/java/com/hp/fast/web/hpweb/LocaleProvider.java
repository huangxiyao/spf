/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/hpweb/LocaleProvider.java,v 1.1 2007/04/03 10:05:14 marcd Exp $ */

package com.hp.fast.web.hpweb;

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
