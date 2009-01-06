/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/hpweb/UserAgentLocaleProvider.java,v 1.1 2007/04/03 10:05:14 marcd Exp $ */

package com.hp.fast.web.hpweb;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * Returns all locales specified in the HTTP request
 * <code>Accept-Language</code> headers.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class UserAgentLocaleProvider implements LocaleProvider {
    private final Set locales;

    /**
     * Creates a locale provider that parses Accept-Language headers.
     * 
     * @param request
     *            the servlet request.
     */
    // @SuppressWarnings("unchecked")
    public UserAgentLocaleProvider(HttpServletRequest request) {
        Set locales = new LinkedHashSet();

        for (Enumeration l = request.getLocales(); l.hasMoreElements();) {
            locales.add(l.nextElement());
        }

        this.locales = Collections.unmodifiableSet(locales);
    }

    public Collection getLocales() {
        return locales;
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return false;
    }

}
