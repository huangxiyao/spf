/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import com.hp.it.spf.localeresolver.http.ContentNegotiator;
import com.hp.it.spf.localeresolver.http.Negotiators;
import com.hp.it.spf.localeresolver.http.NoAcceptableLanguageException;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * A language negotiator implementation that incorporates all HP.com language
 * selection criteria, including URL, cookie, HP Passport (optional),
 * user-agent, and default language settings. Also persists language settings
 * into HP.com specified cookies when appropriate (ie when set from URL or HP
 * Passport). Each language selection source is validated against the allowed
 * locales for the target environment; only allowed languages are set. (As of
 * version 2.0, any locale known in the target environment can be forcibly set
 * via the URL, even if not otherwise allowed, by using the
 * {@link UrlLocaleProvider.ALLOW_LOCALE_PARAM} query parameter.)
 * 
 * @author Quintin May
 * @author Scott Jorgenson
 * @version $Revision: 2.0 $
 */
public class LanguageNegotiator implements ContentNegotiator {
	private static final String ACCEPTABLE = LanguageNegotiator.class.getName()
			+ ".ACCEPTABLE";
	private static final String NEGOTIATED_VALUE = LanguageNegotiator.class
			.getName()
			+ ".NEGOTIATED_VALUE";
	private static final String PERSIST_VALUE = LanguageNegotiator.class
			.getName()
			+ ".PERSIST_VALUE";
	private static final String ALLOWED_LOCALES = LanguageNegotiator.class
			.getName()
			+ ".ALLOWED_LOCALES";

	private TargetLocaleProviderFactory targetLocaleProviderFactory = null;
	private LocaleProviderFactory passportLocaleProviderFactory = null;
	private LocaleProviderFactory defaultLocaleProviderFactory = null;
	private Map defaultCountriesForLanguages = new Hashtable();

	private final CookieLocaleSaver cookieLocaleSaver = new CookieLocaleSaver();

	/**
	 * Sets the locale provider factory that returns locale providers that
	 * return the languages in which the application is localized.
	 * 
	 * @param targetLocaleProviderFactory
	 *            application locale provider factory.
	 */
	public void setTargetLocaleProviderFactory(
			TargetLocaleProviderFactory targetLocaleProviderFactory) {
		this.targetLocaleProviderFactory = targetLocaleProviderFactory;
	}

	/**
	 * Sets the locale provider factory that returns locale providers that in
	 * turn return the user's HP Passport locale settings.
	 * 
	 * @param passportLocaleProviderFactory
	 *            the HP Passport locale provider factory.
	 */
	public void setPassportLocaleProviderFactory(
			LocaleProviderFactory passportLocaleProviderFactory) {
		this.passportLocaleProviderFactory = passportLocaleProviderFactory;
	}

	/**
	 * Sets the last-resort locale provider factory. The first element returned
	 * by {@link LocaleProvider#getLocales()} will be used as the negotiated
	 * locale if no other source was able to provide a locale.
	 * 
	 * @param defaultLocaleProviderFactory
	 *            the default locale provider factory.
	 */
	public void setDefaultLocaleProviderFactory(
			LocaleProviderFactory defaultLocaleProviderFactory) {
		this.defaultLocaleProviderFactory = defaultLocaleProviderFactory;
	}

	/**
	 * Sets the mapping of languages to their default full locales, based on an
	 * input string of one or more, comma-delimited RFC 3066 language tags. The
	 * language in each tag is set to map to the full locale represented by each
	 * tag. For example, the input parameter <code>zh-CN,ja-JP</code> causes
	 * language <code>zh</code> (Chinese) to map to the full locale
	 * <code>zh-CN</code> (China Chinese), and language <code>ja</code>
	 * (Japanese) to map to the full locale <code>ja-JP</code> (Japan
	 * Japanese). Any invalid tags or language-only tags in the input string are
	 * ignored.
	 * 
	 * @param languageDefaultLocales
	 *            An input string of RFC 3066 language tags, as described above.
	 */
	public void setDefaultCountriesForLanguages(String languageDefaultLocales) {

		Collection locales = new HashSet();
		if (languageDefaultLocales != null) {
			Locale locale;
			String[] strLocales = languageDefaultLocales.split(",");
			for (int i = 0; i < strLocales.length; i++) {
				locale = I18nUtility.languageTagToLocale(strLocales[i].trim());
				if (locale != null) {
					locales.add(locale);
				}
			}
		}
		setDefaultCountriesForLanguages(locales);
	}

	/**
	 * Sets the mapping of languages to their default countries, based on an
	 * input collection of full locales. The language in the input full locale
	 * is set to map to the country of that full locale. For example, the input
	 * collection of China Chinese (<code>zh-CN</code>) and Japan Japanese (<code>ja-JP</code>)
	 * causes language <code>zh</code> (Chinese) to map to country
	 * <code>CN</code> (China), and language <code>ja</code> (Japanese) to
	 * map to country <code>JP</code> (Japan). Any language-only locales in
	 * the input collection are ignored.
	 * 
	 * @param languageDefaultLocales
	 *            An input collection of full locales, as described above.
	 */
	public void setDefaultCountriesForLanguages(
			Collection languageDefaultLocales) {

		if (languageDefaultLocales != null) {
			Locale locale;
			String language, country;
			Iterator i = languageDefaultLocales.iterator();
			while (i.hasNext()) {
				try {
					locale = (Locale) i.next();
				} catch (ClassCastException e) {
					locale = null;
				}
				if (locale != null) {
					language = locale.getLanguage();
					country = locale.getCountry();
					if (StringUtils.hasText(language)
							&& StringUtils.hasText(country)) {
						this.defaultCountriesForLanguages.put(language
								.toLowerCase(), country.toUpperCase());
					}
				}
			}
		}
	}

	/**
	 * Resolves the locale from the request and returns true if an acceptable
	 * locale was resolved (false otherwise). The resolved locale is set into a
	 * request attribute as a side-effect. See the class documentation above for
	 * a brief description of the algorithm used.
	 * 
	 * @param request
	 *            the request for which to resolve a locale
	 * @return true if the locale was resolved acceptably
	 */
	public boolean acceptable(HttpServletRequest request) {

		Boolean acceptable = (Boolean) request.getAttribute(ACCEPTABLE);

		// if we haven't determined this before for this request
		if (acceptable == null) {
			Locale negotiatedLocale = null;

			// if a target locale provider factory has been specified (required)
			if (targetLocaleProviderFactory != null) {
				// setup - get target locales - by default, this gets the
				// allowed locales for the target, but if special query
				// parameter is present (or was present in previous request in
				// this session), then that can override that and get all
				// locales known to the target instead.
				TargetLocaleProvider targetLocaleProvider = targetLocaleProviderFactory
						.getTargetLocaleProvider(request);
				Collection targetLocales = targetLocaleProvider.getLocales();
				request.setAttribute(ALLOWED_LOCALES, targetLocales);

				// check URL
				UrlLocaleProvider urlLocaleProvider = new UrlLocaleProvider(
						request);
				Collection urlLocales = urlLocaleProvider.getLocales();
				negotiatedLocale = Negotiators.resolveLocale(urlLocales,
						targetLocales, defaultCountriesForLanguages);
				if (negotiatedLocale == null) {
					// check cookies
					negotiatedLocale = Negotiators.resolveLocale(
							new CookieLocaleProvider(request).getLocales(),
							targetLocales, defaultCountriesForLanguages);

					if (negotiatedLocale == null) {
						if (passportLocaleProviderFactory != null) {
							// check HP Passport
							negotiatedLocale = Negotiators.resolveLocale(
									passportLocaleProviderFactory
											.getLocaleProvider(request)
											.getLocales(), targetLocales,
									defaultCountriesForLanguages);
						}

						if (negotiatedLocale == null) {
							// check user agent
							negotiatedLocale = Negotiators.resolveLocale(
									new UserAgentLocaleProvider(request)
											.getLocales(), targetLocales,
									defaultCountriesForLanguages);

							if (negotiatedLocale == null
									&& defaultLocaleProviderFactory != null) {
								// check for a default
								Collection defaultLocales = defaultLocaleProviderFactory
										.getLocaleProvider(request)
										.getLocales();
								if (!defaultLocales.isEmpty()) {
									negotiatedLocale = (Locale) defaultLocales
											.iterator().next();
								}
							}
						} else {
							// write cookies if locale specified in HP Passport
							request.setAttribute(PERSIST_VALUE, Boolean.TRUE);
						}
					}
				} else {
					// write cookies if locale specified in URL
					request.setAttribute(PERSIST_VALUE, Boolean.TRUE);
				}
			}

			acceptable = new Boolean(negotiatedLocale != null);
			request.setAttribute(ACCEPTABLE, acceptable);

			if (acceptable.booleanValue()) {
				request.setAttribute(NEGOTIATED_VALUE, negotiatedLocale);
			}
		}

		return acceptable.booleanValue();
	}

	public Object negotiatedValue(HttpServletRequest request) {
		return acceptable(request) ? (Locale) request
				.getAttribute(NEGOTIATED_VALUE) : null;
	}

	/**
	 * The main method for this class - resolves the locale for the request and
	 * sets it in the response, also persisting it when appropriate into HP.com
	 * cookies. A {@link NoAcceptableLanguageException} is thrown if a locale
	 * could not be resolved. See the class documentation abovce for a brief
	 * description of the algorithm used to resolve the locale.
	 */
	// @SuppressWarnings("unchecked")
	public void negotiate(HttpServletRequest request,
			HttpServletResponse response) throws NoAcceptableLanguageException {
		Locale negotiatedLocale = null;

		response.addHeader("Vary", "Cookie Accept-Language");

		if (acceptable(request)) {
			negotiatedLocale = (Locale) request.getAttribute(NEGOTIATED_VALUE);
			Boolean persist = (Boolean) request.getAttribute(PERSIST_VALUE);

			if (persist != null) {
				cookieLocaleSaver.saveLocale(request, response,
						negotiatedLocale);
			}

			response.setLocale(negotiatedLocale);
		} else {
			throw new NoAcceptableLanguageException(
					"No acceptable language could be negotiated.",
					(Collection) request.getAttribute(ALLOWED_LOCALES));
		}
	}

	/**
	 * @author Quintin May
	 */
	static class CookieLocaleSaver {

		void saveLocale(HttpServletRequest request,
				HttpServletResponse response, Locale locale) {
			if (locale != null) {
				addCookie(response, Consts.COOKIE_NAME_HPCOM_LANGUAGE, 
						I18nUtility.localeToLanguageTag(locale).toLowerCase());

				if (locale.getCountry() == null) {
					deleteCookie(response, Consts.COOKIE_NAME_HPCOM_COUNTRY);
				} else {
					addCookie(response, Consts.COOKIE_NAME_HPCOM_COUNTRY,
							locale.getCountry());
				}
			}
		}

		private void addCookie(HttpServletResponse response, String name,
				String value) {
			response.addCookie(createCookie(name, value, -1)); // -1 = session
		}

		private void deleteCookie(HttpServletResponse response, String name) {
			response.addCookie(createCookie(name, "", 0));
		}

		private Cookie createCookie(String name, String value, int maxAge) {
			Cookie cookie = new Cookie(name, value);

			cookie.setDomain(Consts.HP_COOKIE_DOMAIN);
			cookie.setPath(Consts.HP_COOKIE_PATH);
			cookie.setMaxAge(maxAge);

			return cookie;
		}
	}

}
