/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/hpweb/UrlLocaleProvider.java,v 1.1 2007/04/03 10:05:14 marcd Exp $ */

package com.hp.fast.web.hpweb;

import javax.servlet.http.HttpServletRequest;

/**
 * A locale provider that parses the language and country from HP.com standard
 * query parameters.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class UrlLocaleProvider extends AbstractLocaleProvider {
    private final String language;

    private final String country;

    /**
     * Creates an HP query parameter locale provider.
     * 
     * @param request
     *            the servlet request.
     */
    public UrlLocaleProvider(HttpServletRequest request) {
        language = request.getParameter(LANGUAGE);
        country = request.getParameter(COUNTRY);
    }

    protected String getLanguage() {
        return language;
    }

    protected String getCountry() {
        return country;
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return true;
    }

}
