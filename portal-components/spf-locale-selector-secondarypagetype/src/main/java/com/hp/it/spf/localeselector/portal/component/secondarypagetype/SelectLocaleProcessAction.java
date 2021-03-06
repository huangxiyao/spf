/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeselector.portal.component.secondarypagetype;

import java.util.Collection;
import java.util.Locale;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epicentric.common.website.CookieUtils;
import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.it.spf.sso.portal.AuthenticationUtility;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.xa.exception.portal.ExceptionUtil;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.log.portal.LogHelper;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * <p>
 * This is the standard process action class for the locale selector secondary
 * page type. It validates the selected locale and then stores it into the
 * request, the HP.com standard locale cookies, and (if the user is logged-in)
 * HP Passport. In the secondary page, this action class may be chained with
 * other actions which persist the locale into other, site-specific locations,
 * finally terminating in the SelectLocaleRedirectProcessAction class.
 * </p>
 * <p>
 * This class expects the locale selector form to have passed the selected
 * locale in the form parameter named with the value of the portal
 * <code>Consts.PARAM_SELECT_LOCALE</code> constant, current
 * <code>spfSelectedLocale</code>. The value is expected to be an RFC 3066
 * language tag. This is the contract which this action has with the form.
 * </p>
 * 
 * @author <link href="ying-zhi.wu@hp.com">Oliver</link>
 * @version TBD
 */
public class SelectLocaleProcessAction extends BaseAction {

	private static final LogWrapper LOG = new LogWrapper(
			SelectLocaleProcessAction.class);

	// constants
	private static final int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;

	/**
	 * The execute method for this action class.
	 * 
	 * @param portalContext
	 *            The portal context object
	 * @return PortalURI
	 */
	public PortalURI execute(PortalContext portalContext) {

		try {
			boolean sFlag = true; // indicate whether the whole process is
			// success
			// or not
			HttpServletRequest request = portalContext.getPortalRequest()
					.getRequest();
			HttpServletResponse response = portalContext.getPortalResponse()
					.getResponse();

			// get locale selected by user from request object
			String localeStr = (String) request
					.getParameter(Consts.PARAM_SELECT_LOCALE);

			// convert to locale Object
			Locale plocale = I18nUtility.languageTagToLocale(localeStr);

			LOG.info("SelectLocaleProcessAction: invoked to set new locale: "
					+ plocale);
			if (isAvailableLocale(request, plocale)) {
			    	// CR #224 - do not set locale into Vignette because this could set it for
			    	// an SSO guest user - thus the current SSO guest user for locale A would 
			    	// get set with locale B, which would be a Bad Thing.  Anyway setting the
			    	// locale into Vignette at all here is unnecessary; the new locale does not
			    	// need to take effect in this request, but only in the next request after
			    	// the redirect process-action runs.  And setting the cookies (below) will
			    	// ensure that the new locale does take effect in that next request.
			    	// DSJ 2011/2/7
			    	/*
				LOG
						.info("SelectLocaleProcessAction: updating user's locale into request for Vignette.");
				boolean setLocaleFlag = I18nUtility.setLocale(request, plocale);
				if (!setLocaleFlag) {
					LOG
							.error("SelectLocaleProcessAction: update user's locale into request for Vignette failed.");
					sFlag = false;
				}
				*/

				// set locale into the HP.com cookies
				LOG
						.info("SelectLocaleProcessAction: updating user's locale into HP.com standard cookie(s).");
				addCookie(request, response, Consts.PARAM_HPCOM_LANGUAGE,
						I18nUtility.localeToLanguageTag(plocale).toLowerCase(),
						 1 * SECONDS_PER_YEAR);
				
				if (hasCountry(plocale)) {
					// country is not null, set country cookie
					addCookie(request, response, Consts.PARAM_HPCOM_COUNTRY, plocale
							.getCountry().toLowerCase(), 1 * SECONDS_PER_YEAR);
				} else {
					// country is null, delete country cookie
					delCookie(request, response, Consts.PARAM_HPCOM_COUNTRY);
				}

				// set locale into HPP
				if (isAuthenticatedByHPP(request)) {
					LOG
							.info("SelectLocaleProcessAction: updating user's locale into HPP. HPP language code: "
									+ I18nUtility.localeToHPPLanguage(plocale));
					try {
						updateLocaleInHPP(portalContext, request, plocale);
					} catch (PassportServiceException pse) {
						boolean gotFaults = false;
						List faults = pse.getFaults();
						if ((faults != null) && (faults.size() > 0)) {
							for (int i = 0; i < faults.size(); i++) {
								Object obj = faults.get(i);
								if (obj instanceof Fault) {
									LOG
											.error("SelectLocaleProcessAction: update user's locale into HPP failed. The failure has been ignored. More detail: "
													+ ((Fault) obj)
															.getDescription());
									gotFaults = true;
								}
							}
						}
						if (!gotFaults) {
							LOG
									.error(
											"SelectLocaleProcessAction: update user's locale into HPP failed. The failure has been ignored.",
											pse);
						}
						sFlag = false;
					} catch (Exception e) {
						LOG
								.error(
										"SelectLocaleProcessAction: update user's locale into HPP failed. The failure has been ignored.",
										e);
						sFlag = false;
					}
				} else {
					LOG
							.info("SelectLocaleProcessAction: not an HPP user, or not logged-in - will not update locale into HPP.");
				}
			} else {
				LOG
						.error("SelectLocaleProcessAction: locale is not in the site available locales - no updates performed.");
				sFlag = false;
			}

			// log the outcome
			if (!sFlag) {
				LOG.error("SelectLocaleProcessAction: process failed.");
			}

			// return null so process will continue normally
			return null;
		} catch (Exception ex) {
			// redirect to system error page if anything unusual happens
			LOG.error("SetLocaleProcessAction error: " + ex, ex);
			return ExceptionUtil.redirectSystemErrorPage(portalContext, null,
					null, null);
		}
	}

	/**
	 * Validate the selected locale against the site available locales.
	 */
	private boolean isAvailableLocale(HttpServletRequest request, Locale locale) {
		Collection alocales = I18nUtility.getAvailableLocales(request);
		return alocales.contains(locale);
	}

	/**
	 * Validate the selected locale contains a country code.
	 */
	private boolean hasCountry(Locale locale) {
		String country = locale.getCountry();
		return (country != null && !country.trim().equals(""));
	}

	/**
	 * Check if the user is authenticated against HPP.
	 */
	private boolean isAuthenticatedByHPP(HttpServletRequest request) {

		return AuthenticationUtility.loggedIntoHPP(request)
				|| AuthenticationUtility.loggedIntoFed(request);
	}

	/**
	 * Store the given locale into the user profile in HPP. This method calls
	 * HPP Web Services modify-user method.
	 */
	private void updateLocaleInHPP(PortalContext portalContext,
			HttpServletRequest request, Locale locale)
			throws PassportServiceException {
		String sessionToken = CookieUtils.getCookieValue(request,
				Consts.COOKIE_NAME_HPPSESSION);
		if (sessionToken == null) {
		    sessionToken = CookieUtils.getCookieValue(request,
				Consts.COOKIE_NAME_SMSESSION);
		}
		LOG.debug("retrieve SessionToken: " + sessionToken);

		String langCode = I18nUtility.localeToHPPLanguage(locale);

		String company = "";
		if (AuthenticatorHelper.isEnabledHPIAndHPE()) {
			if (AuthenticatorHelper.isFromHPE(request)) {
				company = Consts.COMPANY_HPE;
			} else if (AuthenticatorHelper.isFromHPI(request)) {
				company = Consts.COMPANY_HPI;
			}
		}

		PassportService ws = new PassportService();
		ws.setSystemLangCode(langCode);

		ProfileCore pc = new ProfileCore();
		pc.setLangCode(langCode);

		ws.modifyUser(sessionToken, pc, null, company);
	}

	/**
	 * Set a cookie (used to set the HP.com standard language and country
	 * cookies).
	 * 
	 * @param name
	 *            cookie name
	 * @param value
	 *            cookie value
	 * @param maxAge
	 *            the max age of the cookie (in seconds)
	 */
	private void addCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, int maxAge) {
        String cookieDomain = Utils.getCookieDomainName(request);
		Cookie cookie = Utils.newCookie(name, value, cookieDomain,
				Consts.HP_COOKIE_PATH, maxAge);
		response.addCookie(cookie);
	}

	/**
	 * Remove a cookie by cookie name (used to delete the HP.com standard
	 * country cookie when unneeded).
	 * 
	 * @param name
	 *            cookie name
	 */
	private void delCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		addCookie(request, response, name, null, 0);
	}
}
