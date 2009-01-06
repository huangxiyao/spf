/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * Filter for setting locale
 * 
 * FILENAME: LocaleResolutionFilter.java
 * PACKAGE : com.hp.it.spf.localeresolver.filter
 * $Id: LocaleResolutionFilter.java,v 1.9 2007/07/30 02:45:45 marcd Exp $
 * $Log: LocaleResolutionFilter.java,v $
 * Revision 1.9  2007/07/30 02:45:45  marcd
 * remove the resolution test
 *
 * Revision 1.8  2007/05/18 07:32:12  marcd
 * Interface
 *
 * Revision 1.7  2007/05/17 07:25:41  marcd
 * add javadoc
 *
 * Revision 1.6  2007/04/17 01:41:36  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.spf.localeresolver.filter;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hp.fast.web.hpweb.LanguageNegotiator;
import com.hp.fast.web.hpweb.LocaleProviderFactory;
import com.hp.it.spf.localeresolver.setter.ILocaleSetter;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class LocaleResolutionFilter implements Filter {
    private final LanguageNegotiator languageNegotiator = new LanguageNegotiator();
    private ILocaleSetter appSpecificLocaleSetter;
    
    /**
     * Resolves a locale.
     * <p>The framework code set cookies.  The local code set the vignette user locale</p>
     * @param request the http sevlet request
     * @param response the http servlet response
     * @param chain the next link in the chain
     * @throws IOException .
     * @throws ServletException .
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            try {
                //if (this.appSpecificLocaleSetter.shouldResolveLocale(req)) {
                    languageNegotiator.negotiate(req, resp);
                    Locale locale = (Locale) languageNegotiator.negotiatedValue(req);
                    appSpecificLocaleSetter.setLocale(req, resp, locale);
                //}
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * @param configuration the configuration info set in web.xml
     * @throws ServletException .
     */
    public void init(FilterConfig configuration) throws ServletException {
        try {
            String className;

            className = configuration
                    .getInitParameter("targetLocaleProviderFactory");
            if (className == null) {
                throw new NullPointerException(
                        "targetLocaleProviderFactory must be specified.");
            }
            languageNegotiator
                    .setTargetLocaleProviderFactory((LocaleProviderFactory) Class
                            .forName(className).newInstance());

            className = configuration
                    .getInitParameter("defaultLocaleProviderFactory");
            if (className != null) {
                languageNegotiator
                        .setDefaultLocaleProviderFactory((LocaleProviderFactory) Class
                                .forName(className).newInstance());
            }

            className = configuration
                    .getInitParameter("passportLocaleProviderFactory");
            if (className != null) {
                languageNegotiator
                        .setPassportLocaleProviderFactory((LocaleProviderFactory) Class
                                .forName(className).newInstance());
            }
            
            className = configuration.getInitParameter("appSpecificLocaleSetter");
            if (className != null) {
                this.appSpecificLocaleSetter = (ILocaleSetter) Class
                    .forName(className).newInstance();
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Nothing to destroy.
     */
    public void destroy() {
    }
}
