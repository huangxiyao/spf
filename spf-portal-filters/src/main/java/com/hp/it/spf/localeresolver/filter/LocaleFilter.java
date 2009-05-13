/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.it.spf.localeresolver.hpweb.LanguageNegotiator;
import com.hp.it.spf.localeresolver.hpweb.LocaleProviderFactory;
import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProviderFactory;
import com.hp.it.spf.localeresolver.http.NoAcceptableLanguageException;

/**
 * A servlet filter that resolves user and application locales per HP.com
 * standards. The filter sets the locale of the response to the resolved value.
 * Applications can retrieve the value via
 * {@link javax.servlet.ServletResponse#getLocale()}. See class documentation
 * for {@link com.hp.it.spf.localeresolver.hpweb.LanguageNegotiator} for a brief
 * description of the algorithm used to resolve locale.
 * 
 * <blockquote>
 * <p>
 * <b>Note:</b> This filter is identical to the
 * {@link com.hp.it.spf.localeresolver.portal.filter.SPFLocaleFilter} except for
 * the latter's <code>appSpecificLocaleSetter</code> functionality. In SPF, we
 * use the <code>LocaleFilter</code> prior to single-sign-on, and the
 * <code>SPFLocaleFilter</code> afterwards, because single-sign-on depends on
 * knowing the resolved locale, but the app-specific locale setter for SPF
 * cannot execute until after single-sign-on. Hence we needed to at least
 * resolve the locale and save it to the response <b>before</b> single-sign-on,
 * then set it into the app-specific API <b>after</b> single-sign-on. Hence we
 * have the 2 filters for that. Although the <code>SPFLocaleFilter</code> does
 * not really need to re-resolve the locale - it will not have changed since the
 * <code>LocaleFilter</code> resolved it - we do so anyway, so that
 * <code>SPFLocaleFilter</code> could immediately stand alone as a single
 * filter in future should this split-dependency ever not be needed anymore.
 * </p>
 * </blockquote>
 * 
 * <p>
 * The filter may be configured with the following parameters. The value of each
 * parameter is the name of a class that implements the
 * {@link com.hp.it.spf.localeresolver.hpweb.LocaleProviderFactory} interface
 * (or, in the case of the <code>targetLocaleProviderFactory</code>, the
 * {@link com.hp.it.spf.localeresolver.hpweb.TargetLocaleProviderFactory}
 * interface). The class will be instantiated using its null-argument
 * constructor.
 * </p>
 * 
 * <dl>
 * <dt><code>targetLocaleProviderFactory</code></dt>
 * <dd>
 * <p>
 * The allowed and known locales in which the target (application) is localized.
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
 * The locales the subject (user) has specified in their HP Passport profile.
 * </p>
 * </dd>
 * </dl>
 * 
 * @author Quintin May
 * @author Scott Jorgenson
 * @version $Revision: 2.0 $
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
					.setTargetLocaleProviderFactory((TargetLocaleProviderFactory) Class
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
