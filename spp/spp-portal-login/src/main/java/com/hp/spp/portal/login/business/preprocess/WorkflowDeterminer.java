package com.hp.spp.portal.login.business.preprocess;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.epicentric.common.website.CookieUtils;
import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.login.dao.LoginDAO;
import com.hp.spp.portal.login.dao.LoginDAOCacheImpl;
import com.hp.spp.portal.login.model.Workflow;
import com.hp.spp.profile.Constants;
import com.hp.spp.wsrp.url.PortalURL;
import com.hp.spp.wsrp.url.PortalURLFactory;

/**
 * Class which allows to determine the workflow during the preprocess login
 * (phase of the login before the HPP authentication).
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class WorkflowDeterminer {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(WorkflowDeterminer.class);
	
	private static final Pattern URL_PATTERN_FOR_LANGCOUNTRY = Pattern.compile( "/../../");

	/**
	 * Analyse the site, the error code and the language to determine the
	 * workflow.
	 * <p>
	 * 
	 * @param request
	 *            incoming request
	 * @return the target url of the error workflow
	 */
	public String processErrorWorkflow(HttpServletRequest request) {

		// site determination
		String site = SiteURLHelper.determineMasterSite(request);

		// error determination
		String errorCode = determineErrorCode(request);

		// workflow determination
		Map userProfile = (HashMap) request.getSession().getAttribute(
				Constants.PROFILE_MAP);
		String languageCode = (String) userProfile.get(Constants.MAP_LANGUAGE);
		Workflow workflow = determineWorkflow(site, errorCode, languageCode,
				request);

		// add parameters
		addParameters(workflow, site, languageCode, errorCode, request);

		return workflow.getTargetURL();
	}

	/*
	 * Retrieve error code from the paramater SMAUTHREASON
	 */
	String determineErrorCode(HttpServletRequest request) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("parameterMap : ");
			Object[] keys = request.getParameterMap().keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				mLog
						.debug(keys[i]
								+ " : "
								+ ((String[]) request.getParameterMap().get(
										keys[i]))[0]);
			}
		}

		if (request.getParameter(Constants.SMAUTHREASON) == null)
			return null;

		String errorCode = (String) request
				.getParameter(Constants.SMAUTHREASON);
		if (mLog.isDebugEnabled())
			mLog.debug("error code : " + errorCode);

		// if FROMLOGIN is false, firstredirection by SiteMinder (case of
		// deeplink). We don't consider it's an error.
		boolean fromLogin = false;
		if (request.getParameter(Constants.FROMLOGIN) != null
				&& "true".equals((String) request
						.getParameter(Constants.FROMLOGIN)))
			fromLogin = true;

		Object target = request.getParameter(Constants.TARGET);
		if (target != null
				&& ((String) target).indexOf(Constants.FROMLOGIN + "=true") > -1)
			fromLogin = true;

		if (!fromLogin) {
			if (mLog.isDebugEnabled())
				mLog.debug("the error code 0 is from first redirection. "
						+ "Error code pass to null");
			errorCode = null;
		}

		return errorCode;
	}

	/*
	 * Determine workflow depending site, error code, locale of the user and
	 * request
	 */
	Workflow determineWorkflow(String site, String errorCode,
			String languageCode, HttpServletRequest request) {
		Workflow workflow = new Workflow();

		LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();

		String pathMenuLandingPage = loginDAO.getPathMenuLandingPage(site);
		String urlHomePage = SiteURLHelper.getURLHomePage(request);
		workflow.setLandingPage(SiteURLHelper.getURLPage(site,
				pathMenuLandingPage, urlHomePage));
		if (mLog.isDebugEnabled())
			mLog
					.debug("url landing page : [" + workflow.getLandingPage()
							+ "]");

		workflow.setLocalizationInURL(loginDAO.getLocalizationInURL(site));

		// landing page as target URL
		if (errorCode == null)
			workflow.setTargetURL(workflow.getLandingPage());
		// Error code put by site Minder
		else {
			// target url
			String pathMenuErrorPage = loginDAO.getPathMenuURL(site, errorCode);
			if (Constants.LANDING_PAGE.equals(pathMenuErrorPage))
				workflow.setTargetURL(workflow.getLandingPage());
			else
				workflow.setTargetURL(SiteURLHelper.getURLPage(site,
						pathMenuErrorPage, urlHomePage));
			if (mLog.isDebugEnabled())
				mLog.debug("workflow.getTargetURL() : ["
						+ workflow.getTargetURL() + "]");

			// display error message
			workflow.setDisplayErrorMessage(loginDAO.getDisplayErrorMessage(
					site, errorCode));
		}

		return workflow;
	}

	/*
	 * Put parameters in request, rewrite URL with localization and forward
	 */
	void addParameters(Workflow workflow, String site, String languageCode,
			String errorCode, HttpServletRequest request) {
		if (mLog.isDebugEnabled()){
				mLog.debug("Error Code : "+ errorCode+" LanguageCode : "+languageCode+" Site : "+site);
		}
		String target = workflow.getTargetURL();

		// We use a PortalURL to abstract the redirection toward local or remote
		// portlet
		PortalURL portalURL = null;
		int end = target.indexOf("/menuitem.");
		if (end > -1) {
			String urlRootPage = target.substring(0, end);
			String menuItemId = target.replaceFirst(urlRootPage + "/menuitem.",
					"");
			menuItemId = menuItemId.replaceAll("/", "");
			portalURL = PortalURLFactory.createURL(urlRootPage, menuItemId);
		} else {
			int begin = target.indexOf("/?page=");
			String urlRootPage = target.substring(0, begin);
			String urlEnd = target.substring(begin);
			String pageName = urlEnd.replaceFirst("/\\?page=", "")
					.replaceFirst("/", "");
			portalURL = PortalURLFactory.createPageURL(urlRootPage, pageName);
		}

		// We retrieve the portlet id of the login portlet
		LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();
		String portletId = loginDAO.getPortletId(site);

		// deep link
		String hppTarget = request.getParameter(Constants.TARGET);
		portalURL.setParameter(portletId, Constants.TARGET, hppTarget);
	
		// error parameters
		if (errorCode != null) {
			// Add error code
			portalURL.setParameter(portletId, Constants.ERROR, errorCode);

			// Add displayErrorMessage
			portalURL.setParameter(portletId, Constants.DISPLAY_ERROR, String
					.valueOf(workflow.isDisplayErrorMessage()));
			
			// Add value of Cookie SMTRYNO
			Object smtryno = CookieUtils.getCookieValue(request,
					Constants.SMTRYNO);
			if (CookieUtils.getCookieValue(request, Constants.SMTRYNO) != null
					&& (smtryno instanceof String)) {
				portalURL.setParameter(portletId, Constants.SMTRYNO,
						(String) smtryno);
			}
		}

		// remove template.PAGE/ from the URL
		String url = portalURL.urlToString();
		
		//Fix for TD : 5516
		String localeFromTarget = getQueryParametersFromTarget(request);
		if(mLog.isDebugEnabled()){
			mLog.debug("Language from targer : "+localeFromTarget);
		}
		if(localeFromTarget != null){
			url = url.concat(localeFromTarget);
		}
		//End of Fix for TD : 5516
		
		url = url.replaceFirst("template.PAGE/", "");

		// need of localization of the URL
		// TODO (mat) change this to add localization in portal url
		// if (workflow.isLocalizationInURL())
		// (new Localizer()).addLocaleParameters(target, localeCode, separator);

		if (mLog.isDebugEnabled())
			mLog.debug("portalURL : [" + url + "]");
		workflow.setTargetURL(url);
	}
	/**
	 * @param request HttpServletRequest which comes from request
	 * @return language code as part of TARGET parameter which was
	 * 		   originally given by user in browser.
	 * Logic: 1) Retrieve TARGET parameter from the request
	 * 		  2) Retrieve lang/country code from the url
	 * 		  3) Create query string and return 
	 * 		  4) If info not found return null 
	 * Note : This flow should not affect the login flow. Therefore
	 * we have generic exception handling at the end.
	 */
	private String getQueryParametersFromTarget(HttpServletRequest request){
			String encodedURL = (String)request.getParameter(Constants.TARGET);
			try {
				if(encodedURL != null){
					String strippedEncodedURL = encodedURL.substring(encodedURL.toLowerCase().indexOf("http"));
					String strippedDecodedURL = URLDecoder.decode(strippedEncodedURL,"UTF-8");
					String queryFromPath = getLocaleFromURLPath(strippedDecodedURL);
					if(queryFromPath != null){
						return queryFromPath;
					}
					
					String queryFromParams = getLocaleFromQueryParams(strippedDecodedURL);
					if(queryFromParams != null){
						return queryFromParams;
					}
				}
			}catch(Exception e){
				return null;
			}
		return null;
	}
	
	private String getLocaleFromURLPath(String strippedDecodedURL ){
		String queryParams = null;
		if (strippedDecodedURL.lastIndexOf('/') != strippedDecodedURL.length())
			strippedDecodedURL = strippedDecodedURL + "/";

		/*String regex = "/../../";
	    Pattern pattern = Pattern.compile(regex);*/
	    Matcher matcher = URL_PATTERN_FOR_LANGCOUNTRY.matcher(strippedDecodedURL);
	    if(matcher.find()) {
	    	StringBuffer str = new StringBuffer("&lang=");
	    	str.append(matcher.group().substring(4,6).toLowerCase());
	    	str.append("&cc=");
	    	str.append(matcher.group().substring(1,3));
	    	return str.toString();
	    }
	    
	    return null;
	}
	
	private String getLocaleFromQueryParams(String strippedDecodedURL){
		StringBuffer str = new StringBuffer();
		try{
			URL url = new URL(strippedDecodedURL);
			String query = url.getQuery();
			if(query != null){
				String[] arrayForLang = query.split("&");
				String splitArrayForLang[] = new String[2];	
				for(int i = 0 ; i< arrayForLang.length ; ++i){
					splitArrayForLang = arrayForLang[i].split("=");
					if(splitArrayForLang[0].equalsIgnoreCase("lang")){
						str.append("&lang=").append(splitArrayForLang[1]);
						break;
					}
				}
				if(str.length() != 0){
					String[] arrayForCC = query.split("&");
					String splitArray2[] = new String[2];
					for(int i = 0 ; i< arrayForCC.length ; ++i){
						splitArray2 = arrayForCC[i].split("=");
						if(splitArray2[0].equalsIgnoreCase("cc")){
							str.append("&cc=").append(splitArray2[1]);
							break;
						}
					}
				}
			}
		}catch(Exception e){
			mLog.warn("Error retrieving locale information from TARGET");
			return null;
		}
		String returnStr = str.toString();
		if(returnStr.length() == 0){
			return null;
		}else{
			return returnStr;
		}
	}
}
