package com.hp.spp.portal.login.business.preprocess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import com.epicentric.common.website.I18nUtils;
import com.epicentric.user.User;
import com.epicentric.user.UserManager;
import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.common.sql.PortalCommonDAO;
import com.hp.spp.portal.common.sql.PortalCommonDAOCacheImpl;
import com.hp.spp.portal.login.dao.LoginDAO;
import com.hp.spp.portal.login.dao.LoginDAOCacheImpl;
import com.hp.spp.portal.login.model.SPPGuestUser;
import com.hp.spp.profile.Constants;

/**
 * Class which allows to determine the locale of the Guest User. Called by the filter
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class Localizer {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(Localizer.class);

	/**
	 * the list of supported language codes.
	 */
	private static Set mLanguageCodes;
	
	private final String mDefaultGuestUser = "SSO_GUEST_USER_en";
	
	private final String mDefaultLanguageCode = "en";
	
	private final String mDefaultCountryCode = "us";
	
	private static final Pattern URL_PATTERN_FOR_LANGCOUNTRY = Pattern.compile( "/../../");

	/**
	 * This method retrieve the identifier of a guest user by its language. Several guest users
	 * are registered with language preference.
	 * <p>
	 * 
	 * @param request the request of the user
	 * @return the identifier of the guest user
	 */
	//public String getSSOGuestUser(HttpServletRequest request) {
	//	//return Constants.SSO_GUEST_USER + "_" + getLanguageCode(request);
	//	return getGuestUser(request);
	//}

	/**
	 * Returns the locale from the locale code. The locale code must have the correct pattern
	 * ISOlanguagecode_ISOcountrycode or ISOlanguagecode-ISOcountrycode_ISOcountrycode
	 * <p>
	 * 
	 * @param languageCode the locale code
	 * @return the locale
	 */
	protected Locale getLocale(String languageCode) {
		// _ is the separator between language and country code
		String languageCode2 = languageCode.substring(0, 2);

		return new Locale(languageCode2);
	}
	
	/**
	 * This method retrieves the language code of the user profile from the session.
	 * @param request the request of the user
	 * @return the language code
	 */
	public String getSessionLanguageCode(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String codeFromGuestSession = null;
		if(session.getAttribute(Constants.PROFILE_MAP) !=null){
			Map userProfile = (HashMap) session.getAttribute(Constants.PROFILE_MAP);			
			codeFromGuestSession = (String)userProfile.get(Constants.MAP_LANGUAGE);			
		}
		return codeFromGuestSession;			
	}
	
	/**
	 * This method retrieves the country code of the user profile from the session.
	 * @param request the request of the user
	 * @return the country code
	 */
	public String getSessionCountryCode(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String codeFromGuestSession = null;
		if(session.getAttribute(Constants.PROFILE_MAP) !=null){
			Map userProfile = (HashMap) session.getAttribute(Constants.PROFILE_MAP);			
			codeFromGuestSession = (String)userProfile.get(Constants.MAP_COUNTRY);			
		}
		return codeFromGuestSession;			
	}


	/**
	 * Retrieve the language code by the path info. We suppose that the path info has the
	 * pattern /sitename/localization/ressource. Localization can start by "/country" but it is
	 * not forced. For instance, the path info could be
	 * "sppportal/country/FR/fr/template.LOGIN" or "sppportal/FR/fr/template.LOGIN"
	 * <p>
	 * 
	 * @param pathInfo
	 * @return the language code
	 */
	public String getLanguageCodeByPathInfo(String pathInfo) {
		String languageCode = null;
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("pathInfo : [" + pathInfo + "]");
		}
		if (pathInfo == null || "".equals(pathInfo)) {
			return null;
		}

		// Add / to end of path info
		if (pathInfo.lastIndexOf('/') != pathInfo.length()) {
			pathInfo = pathInfo + "/";
		}

	    Matcher matcher = URL_PATTERN_FOR_LANGCOUNTRY.matcher(pathInfo);
	    if(matcher.find()) {
	      languageCode = matcher.group().substring(4,6).toLowerCase();
	    }
	    return StringUtils.lowerCase(languageCode);
	}

	/**
	 * Retrieve the country code by the path info. We suppose that the path info has the
	 * pattern /sitename/localization/ressource. Localization can start by "/country" but it is
	 * not forced. For instance, the path info could be
	 * "sppportal/country/FR/fr/template.LOGIN" or "sppportal/FR/fr/template.LOGIN"
	 * <p>
	 * 
	 * @param pathInfo
	 * @return the country code
	 */
	public String getCountryCodeByPathInfo(String pathInfo) {
		String countryCode = null;
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("pathInfo : [" + pathInfo + "]");
		}
		if (pathInfo == null || "".equals(pathInfo)) {
			return null;
		}

		// Add / to end of path info
		if (pathInfo.lastIndexOf('/') != pathInfo.length()) {
			pathInfo = pathInfo + "/";
		}

		String regex = "/../../";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(pathInfo);
	    if(matcher.find()) {
	    	countryCode = matcher.group().substring(1,3);
	    }
	    return StringUtils.lowerCase(countryCode);
	}

	/**
	 * Retrieve the locale code by the URL parameters.
	 * <p>
	 * 
	 * @param parameters
	 * @return the locale code
	 */
	public String getLanguageCodeByURLParameters(Map parameters) {
		if (parameters == null || parameters.get("lang") == null) {
			return null;
		}

		// we take the first value for the parameter
		String languageCode = ((String[]) parameters.get("lang"))[0];

		return StringUtils.lowerCase(languageCode);
	}

	/**
	 * Retrieve the country code by the URL parameters.
	 * <p>
	 * 
	 * @param parameters
	 * @return the country code
	 */
	public String getCountryCodeByURLParameters(Map parameters) {
		if (parameters == null || parameters.get("cc") == null) {
			return null;
		}

		// we take the first value for the parameter
		String countryCode = ((String[]) parameters.get("cc"))[0];
		if (countryCode.length() != 2) {
			return null;
		}

		return StringUtils.lowerCase(countryCode);
	}

	/**
	 * Retrieve the language code by the cookies.
	 * <p>
	 * 
	 * @param cookieTab
	 * @return the language code
	 */
	public String getLanguageCodeByCookie(Cookie[] cookieTab) {
		if (cookieTab == null) {
			return null;
		}

		// retrieve the codes
		String languageCode = null;
		// String countryCode = null;
		for (int i = 0; i < cookieTab.length; i++) {
			Cookie cookie = cookieTab[i];
			if (cookie != null && cookie.getName().equals("lang")) {
				languageCode = cookie.getValue();
			}
		}
		
		return StringUtils.lowerCase(languageCode);
	}

	/**
	 * Retrieve the country code by the cookies.
	 * <p>
	 * 
	 * @param cookieTab
	 * @return the country code
	 */
	public String getCountryCodeByCookie(Cookie[] cookieTab) {
		if (cookieTab == null) {
			return null;
		}

		// retrieve the codes
		String coockieCode = null;
		// String countryCode = null;
		for (int i = 0; i < cookieTab.length; i++) {
			Cookie cookie = cookieTab[i];
			if (cookie != null && cookie.getName().equals("cc")) {
				coockieCode = cookie.getValue();
			}
		}

		return StringUtils.lowerCase(coockieCode);
	}

	/**
	 * Retrieve the language code by the browser preferences.
	 * <p>
	 * 
	 * @param requestLocale
	 * @return the language code
	 */
	public String getLanguageCodeBrowserPreference(Locale requestLocale) {
		if (requestLocale == null) {
			return null;
		}

		// method requestLocale.getLanguageCode returns only fr for Locale fr_CA 
		String languageCode = requestLocale.toString().replace('_', '-').toLowerCase();
		if (mLog.isDebugEnabled()){
			mLog.debug("language code from locale [" + requestLocale.getLanguage() + "]");
			mLog.debug("language code from locale after transformation [" + languageCode + "]");
		}
		//return getAllowedLanguageCode(languageCode);
		return languageCode;
	}


	/**
	 * Add localization parameters to URL. For instance, with language fr, country CA and URL
	 * http://site, we obtain http://site?lang=fr&cc=CA.
	 * <p>
	 * 
	 * @param target the target URL
	 * @param localeCode the code of the locale
	 * @param separator the separator ? or & to add parameter
	 */
	public void addLocaleParameters(StringBuffer target, String localeCode, char separator) {
		// localeCode.substring(0, 2)is the ISOLanguageCode with 2 characters
		target.append(separator).append("lang").append('=').append(localeCode.substring(0, 2));
		// localeCode.substring(3, 5)is the ISOCountryCode with 2 characters
		target.append('&').append("cc").append('=').append(localeCode.substring(3, 5));
	}
	

	/**
	 * This method encapsulates call to database for actual retrieval of guest users from
	 * SPP_LOCALE table.
	 * @param languageCode
	 * @param countryCode
	 * @param siteIdentifier
	 * @return guestUser
	 */
	
	public SPPGuestUser getGuestUser(String languageCode, String countryCode, String siteIdentifier){
		if(mLog.isDebugEnabled()){
			mLog.debug("Computation of guestUser starts here whith LanguageCode: "+languageCode+
					" CountryCode: "+countryCode+" siteIdentifier: "+ siteIdentifier);
		}
		//This lookup is based on language, country and site identifier. It is given first priority for lookup
		//Check for country code is to avoid call if it is not present.
		LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();
		if( countryCode != null){
			SPPGuestUser langCountrySiteGuestUser = (SPPGuestUser)loginDAO.getGuestUser(languageCode,countryCode,siteIdentifier);
			if(langCountrySiteGuestUser != null){
				return langCountrySiteGuestUser;
			}
		}
		//This lookup is based on language and site identifier.
		SPPGuestUser langSiteGuestUser = (SPPGuestUser)loginDAO.getGuestUserBySite(languageCode, siteIdentifier);
		if(langSiteGuestUser != null){
			return langSiteGuestUser;
		}
		//This lookup is based on language and country code. Check for country code is to avoid call if it is not present.
		if(countryCode != null){
			SPPGuestUser langCountryGuestUser = (SPPGuestUser)loginDAO.getGuestUserByCountry(languageCode,countryCode);
			if(langCountryGuestUser != null){
				return langCountryGuestUser;
			}
		}
		
		//This lookup is based on only language code.
		SPPGuestUser langGuestUser = (SPPGuestUser)loginDAO.getGuestUser(languageCode);
		if(langGuestUser != null){
			return langGuestUser;
		}
		//Default case, when all the above lookup fails. Should not happen generally.
		if (mLog.isDebugEnabled()) {
			mLog.debug("Returning default guest user");
		}
		return new SPPGuestUser(null,null,null,mDefaultGuestUser,mDefaultLanguageCode);
	}
	
	/**
	 * Retrieves ':' delimited value of language/country to be used to put in session.
	 * Use of delimeter is to prevent two calls. As both of them are available in single
	 * call
	 * @param guestUser
	 * @return 
	 */
	public String[] getGuestUserLangCountry(String guestUser){
		if (mLog.isDebugEnabled()) {
			mLog.debug("Inside getGuestUserDetails");
		}
		try{
			UserManager userManager = UserManager.getInstance();
			User user = userManager.getUser(Constants.VGN_USERNAME, guestUser);
			return new String[]{(String)user.getProperty("language"), (String)user.getProperty("country")};
		}catch(Exception e){
			throw new IllegalStateException("Guest User not available in USERS table", new Throwable(e));
		}
	}
	
	/**
	 * Retrieves guest user from SPP_LOCALE table for a given request.
	 * @param request
	 * @return guestUser value
	 */
	public SPPGuestUser getSSOGuestUser(HttpServletRequest request){
		String guestUser = mDefaultGuestUser;
		String languageCountryComboForLookup[] = getLangCountyComboForLookup(request);
		
		String languageForLookup = languageCountryComboForLookup[0];
		String countryCode = languageCountryComboForLookup[1];
		
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Language and country code used to retrieve guest user");
			mLog.debug("Country: "+countryCode);
			mLog.debug("Language: "+languageForLookup);
		}
				
		String siteIdentifier = getSiteIdentifier(SiteURLHelper.determineMasterSite(request));
		SPPGuestUser sppGuestLocal = getGuestUser(languageForLookup,countryCode,siteIdentifier);
		if(sppGuestLocal != null){
			guestUser = sppGuestLocal.getGuestUser();
		}
		if(mLog.isDebugEnabled()){
			mLog.debug("GuestUser computed: "+sppGuestLocal.getGuestUser());
		}
		return sppGuestLocal;
	}
	
	/**
	 * This method is required to prevent computation of guest user having 
	 * LANGUAGE value in USERS table different from what was computed as simple 
	 * session computation. This problem arises because Vignette computes its
	 * own locale based on guestuser. :( 
	 * @param request
	 * @return Guest User
	 */
	public String getGuestUserForSSOAuthenticator(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			return ((SPPGuestUser)getSSOGuestUser(request)).getGuestUser();
		}else{
			Map userProfile = (Map)session.getAttribute(Constants.PROFILE_MAP);
			String guestUser = (String)session.getAttribute("GuestUser");
			if(mLog.isDebugEnabled()){
				mLog.error("Guest User Used :"+guestUser);
			}
			if(userProfile == null || guestUser == null){
				return ((SPPGuestUser)getSSOGuestUser(request)).getGuestUser();
			}else{
				return guestUser;
			}
		}
	}
	
	/**
	 * This method retrieves siteIdentifier from SPP_SITE table
	 * @param siteName
	 * @return siteIdentifier
	 */
	public String getSiteIdentifier(String siteName){
		if (mLog.isDebugEnabled()) {
			mLog.debug("Inside getSiteIdentifier");
		}
		PortalCommonDAO portalCommonDAO = PortalCommonDAOCacheImpl.getInstance();
		String siteIdentifier =  portalCommonDAO.getSiteIdentifier(siteName);
		if (mLog.isDebugEnabled()) {
			mLog.debug("SiteIdentifier: "+siteIdentifier);
		}
		return siteIdentifier;
	}
	
	/**
	 * This method is used to set Vignette locale which helps it determine
	 * localization bundle to be used. We need to do substring of language
	 * as for specific case as traditional Chinese(zh_CN) we need to pass
	 * language as separate from country to distinguish it from Locale(zh) 
	 * and Locale(zh,TW).
	 * @param languageCode
	 * @param guestUser
	 * @return
	 */
	public Integer setVignetteLocale(String languageCode, String countryCode, String guestUser){
		if (mLog.isDebugEnabled()) {
			mLog.debug("Inside setVignetteLocale");
		}
		try{
			UserManager userManager = UserManager.getInstance();
			User user = userManager.getUser(Constants.VGN_USERNAME, guestUser);
			
			String countryForLocale = (languageCode.length() == 5) ? languageCode.substring(3, 5).toUpperCase() : countryCode;
			languageCode = languageCode.substring(0,2);
			if (mLog.isDebugEnabled()) {
				mLog.debug("Language for Locale: "+languageCode+" Country for Locale: "+countryForLocale);
			}	
			Locale locale = new Locale(languageCode,countryForLocale);

			Locale localeVignette = I18nUtils.getUserLocale(user);
			if (localeVignette == null || !localeVignette.equals(locale)) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("Update locale of user [" + guestUser + "] to [" + locale.getDisplayName()+"]");
				}
				I18nUtils.setUserLocale(user, locale);
			}
//			I18nUtils.setUserLocale(user, locale);
//			if(mLog.isDebugEnabled()) {
//				mLog.debug("Locale Used: "+ locale);
//			}
		}catch(Exception e){
			String error = "Error  during retrieval of guest user with name ["+ guestUser + "]. " + e.getMessage();
			mLog.error(error);
			return new Integer(0);
		}
		return null;
	}
	
	/**
	 * This method computes combined language string and country code that is used to 
	 * lookup for guest users from SPP_LOCALE table. The language and country codes
	 * are retrieved from URLPath/QueryParams/Cookies/Browser in that order preference. 
	 * For eg. If language retrieved is 'zh' and country retrieved is 'cn' then
	 * lookup language is 'zh-cn' and lookup country is 'cn' and combo computed
	 * is 'zh-cn:cn'.
	 * @param request HttpServlet request object
	 * @return Combination of lookup language and country
	 */
	protected String[] getLangCountyComboForLookup(HttpServletRequest request){
		String languageCode = null;
		String countryCode = null;
		String lookupLanguageCode = null;
		
		/****************************************************************************/
		String pathInfo = request.getPathInfo();
		languageCode = getLanguageCodeByPathInfo(pathInfo);
		if (mLog.isDebugEnabled()) {
			mLog.debug("languageCodeByPathInfo [" + languageCode + "]");
		}
		if(languageCode != null){
			countryCode = getCountryCodeByPathInfo(pathInfo);
			if (mLog.isDebugEnabled()) {
				mLog.debug("countryCodeByPathInfo [" + countryCode + "]");
			}
			lookupLanguageCode = computeLookupLanguage(languageCode,countryCode);
			return new String[]{lookupLanguageCode, countryCode};
		}
		/****************************************************************************/		
		/****************************************************************************/
		Map requestParameters = request.getParameterMap();
		languageCode = getLanguageCodeByURLParameters(requestParameters);
		if (mLog.isDebugEnabled()) {
			mLog.debug("languageCodeByURLParameters [" + languageCode + "]");
		}
		if(languageCode != null){
			countryCode = getCountryCodeByURLParameters(requestParameters);
			if (mLog.isDebugEnabled()) {
				mLog.debug("countryCodeByURLParameters [" + countryCode + "]");
			}
			lookupLanguageCode = computeLookupLanguage(languageCode,countryCode);
			return new String[]{lookupLanguageCode, countryCode};
		}
		/****************************************************************************/
		/****************************************************************************/
		Cookie[] cookieTab = request.getCookies();
		languageCode = getLanguageCodeByCookie(cookieTab);
		if (mLog.isDebugEnabled()) {
			mLog.debug("languageCodeByCookie [" + languageCode + "]");
		}
		if(languageCode != null){
			countryCode = getCountryCodeByCookie(cookieTab);
			if (mLog.isDebugEnabled()) {
				mLog.debug("countryCodeByCookie [" + countryCode + "]");
			}
			lookupLanguageCode = computeLookupLanguage(languageCode,countryCode);
			//return computeCombo(lookupLanguageCode,countryCode);
			return new String[]{lookupLanguageCode, countryCode};
		}
		/****************************************************************************/
		/****************************************************************************/
		Locale requestLocale = request.getLocale();
		lookupLanguageCode = getLanguageCodeBrowserPreference(requestLocale);
		if (mLog.isDebugEnabled()){
			mLog.debug("locale from request [" + requestLocale + "]");
			mLog.debug("lookup language ["+ lookupLanguageCode +"]");
		}
		/****************************************************************************/
		/****************************************************************************/
		return new String[]{lookupLanguageCode, countryCode};
	}
	
	/**
	 * This method computes language string that is used to lookup
	 * guest user from SPP_LOCALE table. For eg. language = 'zh' and
	 * country = 'cn' will result in lookup language as zh-cn.
	 * Also language = 'zh-tw' and country = 'tw' will result  in 
	 * lookup language as 'zh-tw'.
	 * 
	 * @param language Used to determine language portion of lookup
	 * 				   language.
	 * @param country Used to determine country portion of lookup 
	 * 				  language
	 * @return language string used to lookup SPP_LOCALE table
	 */
	String computeLookupLanguage(String language,String country){
		int langLength = language.length();
		if(langLength < 2) {
			return null;
		}
		
		language = language.replace('_', '-').toLowerCase();

		if(langLength == 2){
			if(StringUtils.stripToNull(country) == null) {
				return language;
			}
			else {
				return new StringBuffer(language).append("-").append(country).toString();
			}
		}
		
		if(langLength == 5){
			return language;
		}
		return null;
	}
	
	/**
	 * This method is used to retrieve LANGUAGE value from the 
	 * ':' delimited language/country string.
	 * For eg. zh:cn will return 'z'
	 */
	public String getLanguageFromCombo(String langCountryCombo){
		if(langCountryCombo.indexOf(":") == -1){
			return langCountryCombo;
		}else{
			return langCountryCombo.substring(0,langCountryCombo.indexOf(":"));
		}
	}
	
	/**
	 * This method is used to retrieve COUNTRY value from the 
	 * ':' delimited language/country string.
	 * For eg. zh:cn will return 'cn'
	 */
	public String getCountryFromCombo(String langCountryCombo){
		if(langCountryCombo.indexOf(":") == -1){
			return null;
		}else{
			return langCountryCombo.substring(langCountryCombo.indexOf(":")+1,langCountryCombo.length());
		}
		
	}
}
