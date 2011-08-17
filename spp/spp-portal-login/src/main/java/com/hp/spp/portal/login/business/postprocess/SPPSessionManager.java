package com.hp.spp.portal.login.business.postprocess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.epicentric.common.website.CookieUtils;
import com.hp.spp.hpp.supporttools.Decoder;
import com.hp.spp.hpp.supporttools.HPPHeaderHelper;
import com.hp.spp.perf.Operation;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.common.util.CheckSession;
import com.hp.spp.portal.common.util.HPUrlLocator;
import com.hp.spp.portal.login.business.preprocess.Localizer;
import com.hp.spp.portal.login.business.rule.UPSRulesProcessor;
import com.hp.spp.portal.login.dao.LoginDAO;
import com.hp.spp.portal.login.dao.LoginDAOCacheImpl;
import com.hp.spp.portal.login.model.SPPGuestUser;
import com.hp.spp.portal.simulation.StartSimulationManager;
import com.hp.spp.profile.Constants;
import com.hp.spp.webservice.ups.manager.SPPUserManager;

/**
 * Class which updates the data in session.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class SPPSessionManager {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(SPPSessionManager.class);

	/*
		 * Logic :
		 * 1) Check if the url is pointing to public page.
		 * 2) IF public page access
		 * 		i)Get the language code
		 * 		ii) Get the language code from session.
		 * 		iii) If language code not in session or language code in session doesn't correspond
		 * 			 to that returned from localizer compute simple session
		 * 3) If protected page access
		 * 		i) Check if session exists and if it is complex session.
		 * 		ii) If session does not exist or if it is not complex session compute complex session.
		 */
	public Integer ensureUserProfileInSession(HttpServletRequest request, HttpServletResponse response){
		if (isAccessToPublicPage(request)) {
				if(isSimpleSessionComputationRequired(request)) {
					return computeSimpleSession(request);
				}
				else {
					return null;
				}
		}else{
			//Access to protected site.
			
			Object userProfile = request.getSession(true).getAttribute(Constants.PROFILE_MAP);
			
			//Check if session exists
			if (userProfile == null) {
				//Session does not exist compute complex session
				//should never happen
				return computeComplexSession(request, response);
			}
			
			else {
				// Session exists, check if it is complex session
				// user name is only in the complex session map				
				String userName = (String) ((Map) userProfile).get(Constants.MAP_USERNAME);
					
				if (userName == null || "".equals(userName)) {
					return computeComplexSession(request, response);	

				}
				//If userName is not null and not blank, it means a complex session exists
				
			}
		}
		return null;
	}
	
	// This method prevents computation of simple session for subsequent on public pages.
	private boolean isSimpleSessionComputationRequired(HttpServletRequest request){
		Localizer localizer = new Localizer();
		String languageCodeFromSession = localizer.getSessionLanguageCode(request);
		
		if(languageCodeFromSession == null) {
			return true;
		}
		
		String pathInfo = request.getPathInfo();
			
		String languageFromURLPath = localizer.getLanguageCodeByPathInfo(pathInfo);

		if(languageFromURLPath != null) {
			return true;
		}
		
		String languageFromQueryParam = localizer.getLanguageCodeByURLParameters(request.getParameterMap());

		if(languageFromQueryParam != null) {
			return true;
		}
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Computation of simple session not required");
		}
		return false;
	}

	/**
	 * Computation of the data in session. The profile will be a simple one for
	 * access to public pages or the complex one otherwise.
	 * 
	 * @param request
	 * @return an error code, null if no error
	 */
	public Integer computeSession(HttpServletRequest request, HttpServletResponse response) {
		if (isAccessToPublicPage(request)) {
			return computeSimpleSession(request);
		}
		return computeComplexSession(request, response);
	}

	/**
	 * Determine if the request is for a public page (no HPP protection).
	 * 
	 * @param request
	 * @return boolean
	 */
	public boolean isAccessToPublicPage(HttpServletRequest request) {
		// hppId is present only for acces to protected page
		String hppId = HPPHeaderHelper.getHPPId(request);
		if (mLog.isDebugEnabled()) {
			mLog.debug("isAccessToPublicPage");
			mLog.debug("request.getHeader(Constants.SM_UNIVERSALID) :"
					+ HPPHeaderHelper.getHPPId(request));
			mLog.debug("hppId :" + hppId);
		}
		if (hppId == null || "".equals(hppId)) {
			return true;
		}
		return false;
	}

	/**
	 * This method computes simple profile session attributes for pre-login
	 * phase. This involves looking up SPP_LOCALE table based on language code
	 * country code and site identifier for guest user and preferred language 
	 * code. This guest user is then used to pick language and country code 
	 * for guest user session.
	 * in turn used to lookup 
	 * @param request
	 * @return errorCode
	 */
	public Integer computeSimpleSession(HttpServletRequest request) {
		boolean errorOccured = false;
		TimeRecorder.getThreadInstance().recordStart(Operation.SIMPLE_SESSION);
		try {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Inside computeSimpleSession");
			}
			
			Localizer localizer = new Localizer();
			
			String siteIdentifier = localizer.getSiteIdentifier(SiteURLHelper.determineMasterSite(request));
			if (mLog.isDebugEnabled()) {
				mLog.debug("Site Identifier: "+siteIdentifier);
			}
			
			//Fetch appropriate guest user from SPP_LOCALE table
			SPPGuestUser sppGuestLocal = localizer.getSSOGuestUser(request);
			String guestUser = sppGuestLocal.getGuestUser();
			String preferredLanguageCode = sppGuestLocal.getPreferredLanguageCode();
			if (mLog.isDebugEnabled()){
				mLog.debug("GuestUser for Localizer: "+guestUser);
				mLog.debug("PreferredLanguageCode: "+preferredLanguageCode);
			}
			
			//Fetch language/country ':' separated values.xx-yy:zz
			String langCountryComboForSession[] = localizer.getGuestUserLangCountry(guestUser);
			if (mLog.isDebugEnabled()) {
				mLog.debug("Language/Country combination: "+langCountryComboForSession);
			}
			
			String sessionlanguage = langCountryComboForSession[0];
			String sessioncountry = langCountryComboForSession[1];
						
			if (mLog.isDebugEnabled()) {
				mLog.debug("PreferredLanguageCode: "+preferredLanguageCode);
			}
			//Set vignette Locale
			if(localizer.setVignetteLocale(sessionlanguage,sessioncountry,guestUser) != null){
				errorOccured = true;
				return new Integer(0);
			}
			//Set the values in session  1)HTTPRequest 2)Language code 3) Country Code 4) Preferred Language Code 5) Site Identifier
			//Note: Site name to be removed in future.
			setSessionAttributes(request, sessionlanguage,sessioncountry,preferredLanguageCode,siteIdentifier,guestUser);
		}finally {
			if (!errorOccured) {
				TimeRecorder.getThreadInstance().recordEnd(Operation.SIMPLE_SESSION);
			}
			else {
				TimeRecorder.getThreadInstance().recordError(Operation.SIMPLE_SESSION, null);
			}
		}
		
		//Value of null means not error in computation of guest user session
		return null;
}
		//Setting attributes for guest user session
		private void setSessionAttributes(HttpServletRequest request, String language,String country,
												String preferredLanguageCode,String siteIdentifier, String guestUser){
				if (mLog.isDebugEnabled()) {
					mLog.debug("Inside setSessionAttributes");
				}
				HttpSession session = request.getSession(true);
				Map userProfile = new HashMap();
				userProfile.put(Constants.MAP_LANGUAGE, language);
				userProfile.put(Constants.MAP_COUNTRY, country);
				userProfile.put(Constants.PREFERREDLANGUAGECODE,preferredLanguageCode);
				userProfile.put(Constants.MAP_SITE, SiteURLHelper.determineMasterSite(request));
				userProfile.put(Constants.MAP_SITE_ID, siteIdentifier);
				userProfile.put(Constants.MAP_HOMEPAGE, SiteURLHelper.getURLHomePage(request));
				session.setAttribute(Constants.PROFILE_MAP, userProfile);
				session.setAttribute("GuestUser",guestUser);
				if (mLog.isDebugEnabled()){
						mLog.debug("GuestUser Session Attributes:");
						mLog.debug("Language Code: "+language);
						mLog.debug("Country Code: "+country);
						mLog.debug("Preferred LanguageCode: "+preferredLanguageCode);
						mLog.debug("Site Identifier: "+siteIdentifier);
						mLog.debug("HomePage URL: "+SiteURLHelper.getURLHomePage(request));
						mLog.debug("Site Name: "+SiteURLHelper.determineMasterSite(request));
						mLog.debug("GuestUser: "+guestUser);
				}
				
				//remove attribute 'URL_LOCATOR_MAP' from session so that it is recomputed
				//for each simple session computation call. Looks like its an existing bug.
				request.getSession().removeAttribute(HPUrlLocator.URL_LOCATOR_MAP);
		}

	/**
	 * Computation of the data in session with complete profile.
	 * <p>
	 * 
	 * @param request
	 *            the incoming request
	 * @return an error code, null if no error
	 */
	public Integer computeComplexSession(HttpServletRequest request, HttpServletResponse response) {
		TimeRecorder.getThreadInstance().recordStart(Operation.COMPLEX_SESSION);
		Integer error = null;
		try {

			// retrieve values from request
			String userId = HPPHeaderHelper.getSMUser(request);
			String hppId = HPPHeaderHelper.getHPPId(request);

			String site = SiteURLHelper.determineMasterSite(request);
			String sessionToken = CookieUtils.getCookieValue(request,
					Constants.SMSESSION);
			if (mLog.isDebugEnabled()) {
				mLog.debug("computeComplexSession with");
				mLog.debug("userId : [" + userId + "]");
				mLog.debug("hppId : [" + hppId + "]");
				mLog.debug("site : [" + site + "]");
				mLog.debug("sessionToken : [" + sessionToken + "]");
			}
			Map userProfile = new HashMap();

			SPPUserManager sppUserManager = new SPPUserManager();

			// continue profile construction
			error = sppUserManager
					.constructQueryAndRetrieveProfileFromUPS(userId, hppId, site,
							sessionToken, userProfile);

			if (error != null) {
				return error;
			}


			error = sppUserManager.computePostUPSProfile(request, userId,
					hppId, site, userProfile);
		
			if (error != null) {
				return error;
			}
			
			sppUserManager.storeProfileInSession(request, userProfile);

			//Invokes all the rule classes designated for a given site in table.
			error = UPSRulesProcessor.execute(request, userProfile, false);
			
			if(error != null){
				mLog.warn("Error during execution of UPS rules");
				if (mLog.isDebugEnabled()){
					mLog.debug("Error value returned by UPS rules processor, errorCode [" + error.toString() + "]" +
							" for user " + userId);
				}
				return error;
			}
				
			// Store id of session in database
			CheckSession.addRowToCheckSession(hppId, request.getSession().getId());
			// Verify simulation
			checkSimulation(site, request, response);
		}
		finally {
			if (error != null) {
				TimeRecorder.getThreadInstance().recordError(Operation.COMPLEX_SESSION, null);
			}
			else {
				TimeRecorder.getThreadInstance().recordEnd(Operation.COMPLEX_SESSION);
			}
		}

		return error;
	}

	private void checkSimulation(String site, HttpServletRequest request, HttpServletResponse response) {
		LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();

		// The site is not configured to allow persistence of simulation
		if (!loginDAO.getPersistSimulation(site)) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("the site [" + site
						+ "] is not allowed to persist simulation");
			}
			return;
		}

		// proceed simulation, if there is an error then redirect the user to the simulation
		//error page
		String redirection = (new StartSimulationManager()).simulateFromLogin(site, request);
		
		if (redirection != null){
			try {
				
				if (mLog.isDebugEnabled()){
					mLog.debug("redirection to : [" + redirection + "]");
				}
				//Redirect user to redirection url
				response.sendRedirect(redirection);
			} catch (IOException e) {
				String error = "error during redirect : " + e.getMessage();
				mLog.error(error);
				throw new IllegalStateException(error, e);
			}
		}

		return;
	}

	public Integer resetComplexSession(HttpServletRequest request, HttpServletResponse response) {
		if(mLog.isDebugEnabled()){
			mLog.info("Invoking resetComplexSession");
		}
		
		Integer error = computeComplexSession(request,response);
		return error;
	}

}


