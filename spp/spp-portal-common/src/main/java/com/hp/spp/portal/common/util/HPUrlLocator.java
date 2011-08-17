package com.hp.spp.portal.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hp.spp.portal.common.dao.CommonDAO;
import com.hp.spp.portal.common.dao.CommonDAOCacheImpl;
import com.hp.spp.profile.Constants;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Singleton based on existing class in sesame. Uses the SPP_HPURLS table in
 * order to get the localized URLs for certain HP pages as well as the base URL
 * for HP styles.
 * 
 * @author Sesame team
 * @author Adrien Geymond
 * @author Phil Bretherton
 * @author Mathieu Vidal
 * 
 */
public class HPUrlLocator {

	// -------------------------------------------------------- Public constant
	/**
	 * Welcome page URL key.
	 */
	public static final String WELCOME_PAGE = "WELCOME_PAGE";

	/**
	 * Privacy page URL key.
	 */
	public static final String PRIVACY_PAGE = "PRIVACY_PAGE";

	/**
	 * Legacy page URL key.
	 */
	public static final String LEGACY_PAGE = "LEGACY_PAGE";

	/**
	 * Welcome page URL key.
	 */
	public static final String CLASSIC_WELCOME = "CLASSIC_WELCOME";

	/**
	 * Secure welcome page URL key.
	 */
	public static final String SECURE_WELCOME = "SECURE_WELCOME";

	/**
	 * Default welcome page URL key.
	 */
	public static final String DEFAULT_WELCOME = "DEFAULT_WELCOME";

	/**
	 * Locale specific URL key, determined primary by country
	 */
	public static final String STR_LOC = "STR_LOC";

	/**
	 * Locale specific URL key, determined primary by language
	 */
	public static final String STR_LOC_BY_LANGUAGE = "STR_LOC_BY_LANGUAGE";

	/**
	 * Default locale specific URL key
	 */
	public static final String DEFAULT_STR_LOC = "DEFAULT_STR_LOC";

	/**
	 * Session key for the URL locator map.
	 */
	public static final String URL_LOCATOR_MAP = "URL_LOCATOR_MAP";

	/**
	 * English language code.
	 */
	public static final String EN_LANGUAGE = "en";

	/**
	 * Default country code (US).
	 */
	public static final String DEFAULT_COUNTRY_CODE = "US";

	/**
	 * Default string locator
	 */
	public static final String DEFAULT_STRING_LOCATOR = "/country/"
			+ DEFAULT_COUNTRY_CODE.toLowerCase() + "/" + EN_LANGUAGE;

	// ------------------------------------------------------- Private constant
	private static final String URLLOCATORFILENAME = "urlLocatorParameters";

	private static HPUrlLocator mUrlLocator = null;

	private HashMap mUrlMapFromBundle = null;

	/**
	 * constructor read urlLocator.properties
	 */
	private static Logger mLog = Logger.getLogger(HPUrlLocator.class);

	private HPUrlLocator() {
	}

	/**
	 * Implements lazy instanciation of the HPUrlLocator singleton.
	 * 
	 * @return Instance of the HPUrlLocator
	 */
	public static HPUrlLocator getInstance() {
		if (mUrlLocator == null) {
			mUrlLocator = new HPUrlLocator();
			mUrlLocator.init();
		}
		return mUrlLocator;
	}

	/**
	 * Initializes the HPUrlLocator by reading the values from the properties
	 * file.
	 * 
	 */
	private void init() {
		if (mLog.isDebugEnabled())
			mLog.debug("Initializing the HPUrlLocator");
		try {
			ResourceBundle parameters = ResourceBundle
					.getBundle(URLLOCATORFILENAME);
			mUrlMapFromBundle = new HashMap();

			Enumeration enumeration = parameters.getKeys();
			String key = null;
			String value = null;
			while (enumeration.hasMoreElements()) {
				key = (String) enumeration.nextElement();
				value = parameters.getString(key);
				if (mLog.isDebugEnabled())
					mLog.debug("key : " + key + " value :" + value);
				mUrlMapFromBundle.put(key, value);
			}
		} catch (Exception e) {
			mLog.error("Problem during url Locator initialization", e);
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("HPUrlLocator has been initialized");
		}
	}

	/**
	 * Static method that gets the welcome URL, possibly localized.
	 * 
	 * @param pc
	 *            PortalContext used for localization.
	 * @param isLocalized
	 *            should the page be localized or the default used (false)
	 * @return Full localized URL
	 */
	public static String getWelcomeUrl(PortalContext pc, boolean isLocalized) {
		StringBuffer finalUrl = new StringBuffer();
		// retrieve string locator from session
		HashMap urlMap = getUrlMapFromSession(pc);
		String strLoc = (String) urlMap.get(STR_LOC);

		// construct the welcome url for https or http
		if (pc.getPortalRequest().getRequest().getScheme().equals("https")) {
			finalUrl.append(urlMap.get(SECURE_WELCOME));
		} else {
			finalUrl.append(urlMap.get(CLASSIC_WELCOME));
		}

		// localize the url
		if (isLocalized) {
			finalUrl.append(strLoc);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("End of method getWelcomeUrl with finalUrl [" + finalUrl
					+ "]");
		}
		return finalUrl.toString();
	}

	/**
	 * Static method that constructs a localized url, with welcome as base path
	 * and a ressource.
	 * 
	 * @param pc
	 *            PortalContext used for localization.
	 * @param cst_page
	 *            name of the ressource
	 * @param byCountry
	 *            if the localization is primary by country (true) or by
	 *            language (false)
	 * @return Full localized URL
	 */
	public static String getLocalizedHPUrl(PortalContext pc, String cst_page,
			boolean byCountry) {
		StringBuffer finalUrl = new StringBuffer();
		// retrieve string locator from session
		HashMap urlMap = getUrlMapFromSession(pc);

		String strLoc = null;
		if (byCountry) {
			strLoc = (String) urlMap.get(STR_LOC);
		}
		else {
			strLoc = (String) urlMap.get(STR_LOC_BY_LANGUAGE);
		}

		// Localize the url
		finalUrl.append(((String) urlMap.get(CLASSIC_WELCOME)).replaceAll(
				"-ww", ""));
		finalUrl.append(strLoc);
		finalUrl.append("/").append(urlMap.get(cst_page));

		if (mLog.isDebugEnabled()) {
			mLog.debug("End of method getWelcomeUrl with finalUrl [" + finalUrl
					+ "]");
		}
		return finalUrl.toString();
	}

	/**
	 * Method that looks for the map in session. If it is not found then it is
	 * created and put in session.
	 * 
	 * @param pc
	 *            PortalContext used to get the user session and the localized
	 *            map
	 * @return The URL map
	 */
	private static HashMap getUrlMapFromSession(PortalContext pc) {
		// retrieve map in session
		HttpSession session = pc.getPortalRequest().getSession();
		HashMap urlMap = (HashMap) session.getAttribute(URL_LOCATOR_MAP);

		// if no map, construct it and put it in session
		if (urlMap == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("URL map not found in session");
			}
			Map userProfile = (HashMap) session
					.getAttribute(Constants.PROFILE_MAP);
			if (userProfile != null)	{
				String countryCode = (String) userProfile
						.get(Constants.MAP_COUNTRY);
				String languageCode = (String) userProfile
						.get(Constants.MAP_LANGUAGE);
				urlMap = getInstance().getUrlLocatorMap(countryCode,
						languageCode.substring(0, 2));
				session.setAttribute(HPUrlLocator.URL_LOCATOR_MAP, urlMap);
				if (mLog.isDebugEnabled()) {
					mLog.debug("URL map put in session: " + urlMap);
				}
			}
			else	{
				throw new IllegalStateException("No user profile in the HttpSession");
			}
		}
		return urlMap;
	}

	/**
	 * Creates the URL locator map.
	 * 
	 * @param userCountryCode
	 *            the country code of the user
	 * @param userLangCode
	 *            the language code of the user
	 * @return The URL locator map
	 */
	private HashMap getUrlLocatorMap(String userCountryCode, String userLangCode) {
		HashMap urlMap = new HashMap(this.mUrlMapFromBundle);
		urlMap.put(STR_LOC, getUserStrLoc(userCountryCode, userLangCode));
		urlMap.put(STR_LOC_BY_LANGUAGE, getUserStrLocByLanguage(
				userCountryCode, userLangCode));
		return urlMap;
	}

	/**
	 * Gets the localized UserStrLoc from the database by focus on country.
	 * <p>
	 * If no combination country + language is found, we search by order :
	 * <ul>
	 * <li> a) country / en
	 * <li> b) country / first available language
	 * </ul>
	 * If nothing is found in database, we return /country/us/en
	 * <p>
	 * 
	 * @param userCountryCode
	 *            the country code of the user
	 * @param userLangCode
	 *            the language code of the user
	 * @return the localized UserStrLoc.
	 */
	String getUserStrLoc(String userCountryCode, String userLangCode) {
		// return default if country code null
		if (userCountryCode == null || "".equals(userCountryCode)) {
			return DEFAULT_STRING_LOCATOR;
		}

		// preparation of the search
		CommonDAO commonDAO = CommonDAOCacheImpl.getInstance();
		if (userCountryCode != null) {
			userCountryCode = userCountryCode.toLowerCase();
		}
		if (userLangCode != null) {
			userLangCode = userLangCode.toLowerCase();
		}

		// Look for country + language
		String userUrlLocator = commonDAO.getUrlLocator(userCountryCode,
				userLangCode);
		if (userUrlLocator != null) {
			return userUrlLocator;
		}

		// Look for country + EN
		userUrlLocator = commonDAO.getUrlLocator(userCountryCode, EN_LANGUAGE);
		if (userUrlLocator != null) {
			return userUrlLocator;
		}

		// Look for country + first available language
		userUrlLocator = commonDAO.getUrlLocator(userCountryCode);
		if (userUrlLocator != null) {
			return userUrlLocator;
		}

		// default
		return DEFAULT_STRING_LOCATOR;
	}

	/**
	 * Gets the localized UserStrLoc from the database by focus on language.
	 * <p>
	 * If no combination country + language is found, we search the combination
	 * 'country of associated SsoGuestUser' / 'language'. If nothing is found in
	 * database, we return /country/us/en
	 * <p>
	 * 
	 * @param userCountryCode
	 *            the country code of the user
	 * @param userLangCode
	 *            the language code of the user
	 * @return the localized UserStrLoc.
	 */
	String getUserStrLocByLanguage(String userCountryCode, String userLangCode) {
		// return default if language code null
		if (userLangCode == null || "".equals(userLangCode)) {
			return DEFAULT_STRING_LOCATOR;
		}

		// preparation of the search
		CommonDAO commonDAO = CommonDAOCacheImpl.getInstance();
		if (userCountryCode != null) {
			userCountryCode = userCountryCode.toLowerCase();
		}
		if (userLangCode != null) {
			userLangCode = userLangCode.toLowerCase();
		}

		// Look for country + language
		String userUrlLocator = commonDAO.getUrlLocator(userCountryCode,
				userLangCode);
		if (userUrlLocator != null) {
			return userUrlLocator;
		}

		// Look for country of SSO Guest User + language
		userUrlLocator = commonDAO.getUrlLocatorByLanguage(userLangCode);
		if (userUrlLocator != null) {
			return userUrlLocator;
		}

		// default
		return DEFAULT_STRING_LOCATOR;
	}

}
