/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/hpweb/LocaleProviderFactory.java,v 1.1 2007/04/03 10:05:14 marcd Exp $ */

package com.hp.fast.web.hpweb;

import javax.servlet.http.HttpServletRequest;

/**
 * Factory that returns locale providers. Used by the {@link LocaleFilter} to
 * retrieve the application specific HP Passport user profile locale settings.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public interface LocaleProviderFactory {
    /**
     * Returns a locale provider based on the configuration of the current
     * request.
     * 
     * @param request
     *            the servlet request.
     * @return the locale provider.
     */
    LocaleProvider getLocaleProvider(HttpServletRequest request);
}
