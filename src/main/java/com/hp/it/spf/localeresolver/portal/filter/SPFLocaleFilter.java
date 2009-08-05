/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.filter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Locale;
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
import com.hp.it.spf.localeresolver.portal.setter.ILocaleSetter;

/**
 * A servlet filter that resolves user and application locales per HP.com and
 * Shared Portal Framework standards. The filter sets the locale of the response
 * to the resolved value. Applications can retrieve the value via
 * {@link javax.servlet.ServletResponse#getLocale()}. It also sets the resolved
 * locale to the HP.com cookies when appropriate, and provides a hook (the
 * <code>appSpecificLocaleSetter</code>, see below) for persisting it to
 * other locations as well. See class documentation for
 * {@link com.hp.it.spf.localeresolver.hpweb.LanguageNegotiator} for a brief
 * description of the algorithm used to resolve locale.
 * 
 * <blockquote>
 * <p>
 * <b>Note:</b> This filter is identical to the
 * {@link com.hp.it.spf.localeresolver.filter.LocaleFilter} except for the
 * <code>appSpecificLocaleSetter</code> functionality. In SPF, we use the
 * <code>LocaleFilter</code> prior to single-sign-on, and the
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
 * 
 * <dt><code>appSpecificLocaleSetter</code></dt>
 * <dd>
 * <p>
 * The application-specific locale setter, for persisting the resolved locale to
 * additional locations as needed. For example, this can be used to persist the
 * resolved locale into a portal-specific API or a custom cookie or etc.
 * </p>
 * </dd>
 * </dl>
 * 
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class SPFLocaleFilter implements Filter {
	private final LanguageNegotiator languageNegotiator = new LanguageNegotiator();
	private ILocaleSetter appSpecificLocaleSetter;

	/**
	 * Resolves a locale.
	 * <p>
	 * The framework code set cookies. The local code set the vignette user
	 * locale
	 * </p>
	 * 
	 * @param request
	 *            the http sevlet request
	 * @param response
	 *            the http servlet response
	 * @param chain
	 *            the next link in the chain
	 * @throws IOException .
	 * @throws ServletException .
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			try {
				// if (this.appSpecificLocaleSetter.shouldResolveLocale(req)) {
				languageNegotiator.negotiate(req, resp);
				Locale locale = (Locale) languageNegotiator
						.negotiatedValue(req);
				appSpecificLocaleSetter.setLocale(req, resp, locale);
				// }
			} catch (Exception ex) {
				throw new ServletException(ex);
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * @param configuration
	 *            the configuration info set in web.xml
	 * @throws ServletException .
	 */
	public void init(FilterConfig configuration) throws ServletException {
		try {
			String className;

			// Initialize the target locales provider factory. This factory's
			// constructor takes a boolean parameter indicating whether target
			// locales should be expanded to include simple locales for each
			// full locale for the target. The boolean parameter comes from
			// another (optional) filter init param.
			String expandLocales = configuration
					.getInitParameter("expandTargetLocales");
			className = configuration
					.getInitParameter("targetLocaleProviderFactory");
			if (className == null) {
				throw new NullPointerException(
						"targetLocaleProviderFactory must be specified.");
			}
			Class factoryClass = Class.forName(className);
			Class[] factoryConstructorFormalParams = { java.lang.Boolean.class };
			// If the factory constructor supports a boolean parameter, map the
			// init param into it. If not, call the empty constructor.
			try {
				Constructor factoryConstructor = factoryClass
						.getConstructor(factoryConstructorFormalParams);
				Object[] factoryConstructorActualParams = { new Boolean(expandLocales) };
				languageNegotiator
						.setTargetLocaleProviderFactory((TargetLocaleProviderFactory) factoryConstructor
								.newInstance(factoryConstructorActualParams));
			} catch (NoSuchMethodException e) {
				languageNegotiator
						.setTargetLocaleProviderFactory((TargetLocaleProviderFactory) factoryClass
								.newInstance());
			}

			// Initialize the default locale provider factory.
			className = configuration
					.getInitParameter("defaultLocaleProviderFactory");
			if (className != null) {
				languageNegotiator
						.setDefaultLocaleProviderFactory((LocaleProviderFactory) Class
								.forName(className).newInstance());
			}

			// Initialize the HPP locale provider factory.
			className = configuration
					.getInitParameter("passportLocaleProviderFactory");
			if (className != null) {
				languageNegotiator
						.setPassportLocaleProviderFactory((LocaleProviderFactory) Class
								.forName(className).newInstance());
			}

			// Initialize the app-specific locale setter factory.
			className = configuration
					.getInitParameter("appSpecificLocaleSetter");
			if (className != null) {
				this.appSpecificLocaleSetter = (ILocaleSetter) Class.forName(
						className).newInstance();
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
