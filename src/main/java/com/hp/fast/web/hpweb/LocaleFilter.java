/* $Header: /external/Repo3/SP_FW/portal_lib/locale_FAST/src/main/java/com/hp/fast/web/hpweb/LocaleFilter.java,v 1.1 2007/04/03 10:05:14 marcd Exp $ */

package com.hp.fast.web.hpweb;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.fast.web.http.NoAcceptableLanguageException;

/**
 * A servlet filter that resolves user and application locales per HP.com
 * standards. The filter sets the locale of the response to the resolved value.
 * Applications can retrieve the value via
 * {@link javax.servlet.ServletResponse#getLocale()}.
 * 
 * <p>
 * The filter may be configured with the following parameters. The value of each
 * parameter is the name of a class that implements the
 * {@link com.hp.fast.web.hpweb.LocaleProviderFactory} interface. The class will
 * be instantiated using its null-argument constructor.
 * </p>
 * 
 * <dl>
 * <dt><code>targetLocaleProviderFactory</code></dt>
 * <dd>
 * <p>
 * The locales in which the target (application) is localized, defaults to the
 * JVM default locale
 * </p>
 * </dd>
 * 
 * <dt><code>defaultLocaleProviderFactory</code></dt>
 * <dd>
 * <p>
 * The last-resort locale provider. Used if no locale could otherwise be
 * determined.
 * </p>
 * </dd>
 * 
 * <dt><code>passportLocaleProviderFactory</code></dt>
 * <dd>
 * <p>
 * The locales the subject (user) has specified in their HP Passport profile
 * </p>
 * </dd>
 * </dl>
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class LocaleFilter implements Filter {
    private final LanguageNegotiator languageNegotiator = new LanguageNegotiator();

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            try {
                languageNegotiator.negotiate((HttpServletRequest) request,
                        (HttpServletResponse) response);
            } catch (NoAcceptableLanguageException ex) {
                throw new ServletException(ex);
            }
        }

        chain.doFilter(request, response);
    }

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
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    public void destroy() {
    }

}
