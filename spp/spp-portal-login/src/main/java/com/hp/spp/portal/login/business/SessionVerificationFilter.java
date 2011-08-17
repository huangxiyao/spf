package com.hp.spp.portal.login.business;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.hp.spp.perf.Operation;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.common.util.Site;
import com.hp.spp.portal.login.business.postprocess.SPPSessionManager;
import com.hp.spp.profile.Constants;
import com.hp.spp.common.util.DiagnosticContext;

/**
 * Class which check if the data in session concerning the user are correct. It
 * not, update the data in session
 * <p>
 * 
 * @author mvidal@capgemini.fr
 */
public class SessionVerificationFilter implements Filter {
	
	

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(SessionVerificationFilter.class);

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response,
				chain);
	}

	/**
	 * Implementation of the doFilter for Http protocol.
	 * <p>
	 * 
	 * @param request
	 *            incoming request
	 * @param response
	 *            incoming response
	 * @param chain
	 *            fitler chain
	 * @throws IOException
	 *             If an exeption occurs duing the fitler chain processing; this
	 *             class doesn't throw any IOException
	 * @throws ServletException
	 *             If an exception occurs during the filter chain processing;
	 *             this class doesn't throw any ServletException
	 */
	public void doFilter(HttpServletRequest request,
						 HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		TimeRecorder.getThreadInstance().recordStart(Operation.SESSION_CHECK);
        try {

			// check if request concerns a site
			String siteURI = SiteURLHelper.determineMasterSite(request);
			if (Site.concernSite(siteURI)) {

				// check session
				Integer error = checkSession(request, response);


                // Compute the url of the current site public or private
				computeCurrentSite(request);

				// put session info for locale portlet
				passDataToLocalPortlets(request);

				// Resolve error
				if (error != null) {
						if (mLog.isDebugEnabled()){
							mLog.debug("Error value returned, errorCode [" + error.toString() + "]");
						}


                        String urlPreLogin = SiteURLHelper.getURLHomePage(request)
								+ "template.PRELOGIN/";

                        String siteName = SiteURLHelper.determineMasterSite(request);
                        if("console".equals(siteName)){
                            urlPreLogin = urlPreLogin.replaceFirst(request.getContextPath() + "/", request.getContextPath() + "/site/");
                        }
                        String errorParameter = "?" + Constants.SMAUTHREASON + "="
								+ error.toString();
						String fromLoginParameter = "&" + Constants.FROMLOGIN + "=true";
						//This fix enables preserving user's locale infomation when redirected to landing page.
						String localeParameter = (String)request.getAttribute(Constants.LOCALE_PARAM);
						if(mLog.isDebugEnabled()){
							mLog.debug("LocaleParameter as received from rule class: "+localeParameter);
						}
						if(localeParameter != null){
							response.sendRedirect(urlPreLogin + errorParameter + fromLoginParameter + localeParameter);
						}else{
							response.sendRedirect(urlPreLogin + errorParameter + fromLoginParameter);
						}
					}
				}
			
			TimeRecorder.getThreadInstance().recordEnd(Operation.SESSION_CHECK);
		}
		catch (IOException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.SESSION_CHECK, e);
			throw e;
		}
		catch (RuntimeException e) {
			TimeRecorder.getThreadInstance().recordError(Operation.SESSION_CHECK, e);
			throw e;
		}

		chain.doFilter(request, response);
	}

	private void computeCurrentSite(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Map userProfile = (HashMap) session.getAttribute(Constants.PROFILE_MAP);
		if (userProfile == null){
			userProfile = new HashMap();
		}
		String urlCurrentSite = SiteURLHelper.getUrlCurrentSite(request);
		userProfile.put(Constants.MAP_CURRENT_SITE_URL, urlCurrentSite);
	}


	/*
	 * Check the session and construct it if needed.
	 */
	private Integer checkSession(HttpServletRequest request, HttpServletResponse response) {
		String siteURI = SiteURLHelper.determineMasterSite(request);
		SPPSessionManager sppSessionManager = new SPPSessionManager();
		
		//Check if session needs to get refreshed
		Integer error = this.checkSessionRefresh(request, response);
		
		if(error != null){
			return error;
		}
			
		// This call ensures that either simple or complex session exists.
		Integer sessionComputationErrorCode = sppSessionManager.ensureUserProfileInSession(request, response);
		
		//if error code returned from session computation, return immediately.
		if(sessionComputationErrorCode != null){
			return sessionComputationErrorCode;
		}
			
		
		// Retrieving profile from session.
		Object userProfile = request.getSession(true).getAttribute(Constants.PROFILE_MAP);
	
		// profile map not corresponding to the site
		String siteProfile = (String) ((Map) userProfile)
				.get(Constants.MAP_SITE);
		if (siteProfile == null || !siteProfile.equals(siteURI)) {
			if (mLog.isDebugEnabled())
				mLog.debug("session KO : "
						+ " profile map in session corresponding to site ["
						+ siteProfile + "]." + " site accessed : [" + siteURI
						+ "]");
			return sppSessionManager.computeSession(request, response);
		}

		// simple profile map for protected page
		// user name is only in the complex map
		String userName = (String) ((Map) userProfile).get(Constants.MAP_USERNAME);

		if ("".equals(userName))
			userName = null;
		
		if (!sppSessionManager.isAccessToPublicPage(request)
				&& userName == null) {
			if (mLog.isDebugEnabled())
				mLog.debug("session KO : profile map in session corresponding "
						+ "to pre login phase");
			return sppSessionManager.computeComplexSession(request, response);
		}

		// map OK
		if (mLog.isDebugEnabled())
			mLog.debug("map OK. site accessed : [" + siteURI + "]");
		return null;
	}

	/*
	 * Pass data to locale JSR-168 portlets
	 */
	private void passDataToLocalPortlets(HttpServletRequest request) {
		// Only the login portlet is setup actually
		String attributeLanguage = Constants.PREFIX_VGN_LOGINPORTLET
				+ Constants.MAP_LANGUAGE;
		String attributeSite = Constants.PREFIX_VGN_LOGINPORTLET
				+ Constants.MAP_SITE;
		HttpSession session = request.getSession(true);
		Map userProfile = (HashMap) session.getAttribute(Constants.PROFILE_MAP);

		// if no profile, try to compute simple profile
		Integer error = null;
		if (userProfile == null || userProfile.size() == 0) {
			SPPSessionManager sppSessionManager = new SPPSessionManager();
			error = sppSessionManager.computeSimpleSession(request);
		}

		// if error computing simple profile, try default profile
		if (error != null || userProfile == null
				|| userProfile.get(Constants.MAP_LANGUAGE) == null
				|| userProfile.get(Constants.MAP_SITE) == null) {
			userProfile = new HashMap();
			userProfile.put(Constants.MAP_LANGUAGE, Constants.DEFAULT_LANGUAGE);
			String site = SiteURLHelper.determineMasterSite(request);
			userProfile.put(Constants.MAP_SITE, site);
			String urlHomePage = SiteURLHelper.getURLHomePage(request);
			userProfile.put(Constants.MAP_HOMEPAGE, urlHomePage);
			userProfile.put(Constants.MAP_COUNTRY, "US");
			session.setAttribute(Constants.PROFILE_MAP, userProfile);
		}

		// pass language and site to local portlet
		String language = (String) userProfile.get(Constants.MAP_LANGUAGE);
		request.setAttribute(attributeLanguage, language);
		if (mLog.isDebugEnabled())
			mLog.debug("LANGUAGE for login portlet : [" + language + "]");

		String site = (String) userProfile.get(Constants.MAP_SITE);
		request.setAttribute(attributeSite, site);
		if (mLog.isDebugEnabled())
			mLog.debug("SITE for login portlet : [" + site + "]");
	}
	
	
	
	/**
	 * 
	 * Checks if forced refresh of session is required, by checking 
	 * flag SPP_REFRESH_SESSION in query string
	 * <p>
	 */
	private Integer checkSessionRefresh(HttpServletRequest request, HttpServletResponse response){
		SPPSessionManager sppSessionManager = new SPPSessionManager();
		String siteURI = SiteURLHelper.determineMasterSite(request);
		Integer error = null;
		if(mLog.isDebugEnabled()){
			mLog.debug("Checking if refresh flag is set ");
		}
		//Get the parameter for refresh
		String recomputeSession = (String)request.getParameter(Constants.SPP_REFRESH_SESSION);
	 
	
		if (recomputeSession != null && "true".equals(recomputeSession)){
			if (mLog.isDebugEnabled()){
				mLog.debug("session KO : "
						+ " refresh of session required due to recompute session flag : ["
						+ recomputeSession + "]" + ", site accessed : [" + siteURI
						+ "]");
			}
			error = sppSessionManager.resetComplexSession(request, response);
		}
		
		return error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}
