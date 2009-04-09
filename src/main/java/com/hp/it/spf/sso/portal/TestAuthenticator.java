/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.I18nUtils;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.site.Site;
import com.epicentric.user.User;
import com.hp.it.spf.xa.misc.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;

/**
 * This is the Authenticator for SandBox Mode. This authenticator can be run
 * without siteminder. All of the user info are read from property file.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @version TBD
 * @see others
 */
public class TestAuthenticator extends AbstractAuthenticator {

	private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper
			.getLog(TestAuthenticator.class);

	private ResourceBundle prop;

	private final String VIGNETTE_PREFIX = "com.vignette.portal.attribute.portlet.";

	private final String MESSAGEPANE = "messagePane";

	private final String GUESTUSER = "guestuser";

	private final String SANDBOX_USERNAME = "sandbox.username";

	private boolean actAsANON = false;

	/**
	 * Constructor for TestAuthenticator. It will get the resource bundle file
	 * for TestAuthenticator. Then it will read current user from the resource
	 * bundle file. The current user's info will be read from its property file
	 * as well.
	 * 
	 * @param request
	 *            HttpServletRequest object
	 */
	public TestAuthenticator(HttpServletRequest request) {
		this.request = request;
		// clean previous message pane
		request.getSession().removeAttribute(MESSAGEPANE);

		String profileFileName = retrieveProfileFile();
		try {
			LOG.info("Get Resource Bundle File = " + profileFileName);
			// Read current user's info.
			prop = ResourceBundle.getBundle(profileFileName);
			LOG.info("Refresh Resource Bundle File for user " + profileFileName
					+ ".properties");
		} catch (MissingResourceException e) {
			LOG.info("No Resource Bundle File = " + profileFileName);
			StringBuffer message = new StringBuffer("Cound not find "
					+ profileFileName + ".properties.");
			String currentUser = (String) request.getSession().getAttribute(
					SANDBOX_USERNAME);
			Site currentSite = AuthenticatorHelper.getCurrentSite(request);
			String profileFileName2 = "console_" + rb.getString("CurrentUser");
			// if not going to console, use anon user as logged in user
			if (currentSite != null) {
				actAsANON = true;
			}
			LOG.info("Get Resource Bundle File = " + profileFileName2);
			// Read current user's info.
			prop = ResourceBundle.getBundle(profileFileName2);
			LOG.info("Refresh Resource Bundle File for user "
					+ profileFileName2);
			message.append(" Please create " + profileFileName
					+ ".properties for user ");
			message.append(currentUser);
			String currentSiteName = currentSite != null ? Utils
					.getEffectiveSite(request).getDNSName() : "console";
			message.append(" on " + currentSiteName
					+ " site in %domain_home%/sandbox_resoureces/ directory.");
			request.getSession().setAttribute(MESSAGEPANE, message.toString());
		}
	}

	private String retrieveProfileFile() {
		String rbFile = retrieveRbFile();
		// Get current user.
		String currentUser = request.getParameter("username");
		if (currentUser == null || "".equals(currentUser.trim())) {
			currentUser = (String) request.getSession().getAttribute(
					SANDBOX_USERNAME);
		}
		try {
			refreshBundle();
			LOG.info("Get Resource Bundle File = " + rbFile);
			rb = ResourceBundle.getBundle(rbFile);
			if (currentUser == null || "".equals(currentUser.trim())
					|| GUESTUSER.equals(currentUser)) {
				// in this kind, act as anon user
				if (Utils.getEffectiveSite(request) != null) {
					actAsANON = true;
					return "console_" + rb.getString("CurrentUser");
				} else {
					currentUser = rb.getString("CurrentUser");
				}
			}
		} catch (MissingResourceException e) {
			LOG.info("No Resource Bundle File = " + rbFile);
		}
		if (Utils.getEffectiveSite(request) == null) {
			return "console_" + currentUser;
		}
		Site currentSite = AuthenticatorHelper.getCurrentSite(request);
		String currentSiteName = currentSite != null ? Utils
				.getEffectiveSiteDNS(request) : "console";
		return currentSiteName + "_" + currentUser;
	}

	/**
	 * Return current user's info from property file.
	 * 
	 * @param fieldName
	 *            field in request header
	 * @return corresponding field in request header
	 */
	protected String getValue(String fieldName) {
		if (fieldName == null || prop == null) {
			return null;
		}
		try {
			return prop.getString(fieldName);
		} catch (java.util.MissingResourceException e) {
			LOG.error(fieldName + " not found in properties file");
			return null;
		}
	}

	/**
	 * refresh ResourceBundle cache to reload properties file content Otherwise
	 * we have to restart the server to refresh the resource bundle file
	 */
	private void refreshBundle() {
		try {
			Class klass = java.util.ResourceBundle.class;
			Field field = null;
			try {
				field = klass.getDeclaredField("cacheList");
			} catch (NoSuchFieldException noSuchFieldEx) {
				LOG.error(noSuchFieldEx);
			}
			// allow ourselves to manipulate the value of the cacheList
			// (private) property in the ResourceBundle class
			field.setAccessible(true);
			sun.misc.SoftCache cache = null;
			try {
				cache = (sun.misc.SoftCache) field.get(null);
			} catch (IllegalAccessException illegalAccessEx) {
				LOG.error(illegalAccessEx);
			}
			cache.clear();
			// Put back the private status on the cacheList property
			field.setAccessible(false);
		} catch (Exception e) {
			LOG.error("Failed to refresh bundle due to " + e.getMessage());
		}
	}

	/**
	 * Get group info and return as a Set
	 * 
	 * @return groups
	 */
	protected Set getUserGroup() {
		Set groups = new HashSet();
		String groupstring = getValue(AuthenticationConsts.HEADER_GROUP_NAME);
		// groups are divided by ,
		if (groupstring != null) {
			StringTokenizer st = new StringTokenizer(groupstring, ",");
			while (st.hasMoreElements()) {
				String group = (String) st.nextElement();
				LOG.info("Get UserGroup = " + group);
				groups.add(group);
			}
		}
		// retrieve groups with invoking super method and merge them
		groups.addAll(super.getUserGroup());
		return groups;
	}

	/**
	 * Prepare infos for userProfile Map
	 * 
	 * @return customizedProfile map
	 */
	protected Map getUserProfile() {
		Map customizedProfile = new HashMap();
		Enumeration propertyKeys = prop.getKeys();
		while (propertyKeys.hasMoreElements()) {
			String customizedProfileKey = (String) propertyKeys.nextElement();
			customizedProfile.put(customizedProfileKey, prop
					.getString(customizedProfileKey));
		}
		return customizedProfile;
	}

	protected void mapHeaderToUserProfileMap() {
		// retrieve user profile
		userProfile = getUserProfile();
	}

	/**
	 * This method is used to perform all related tasks. 
	 * 1. If act as anon user
	 * 2. act as normal user
	 * 3. pass user info to portlet
	 * 4. pass context map to portlet
	 * 
	 * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
	 */
	public void execute() {
		if ("true".equalsIgnoreCase(request.getParameter("guestMode"))) {
			actAsANON = true;
		}
		// guestMode
		if (actAsANON == true) {
			ANONAuthenticator authenticator = new ANONAuthenticator(request);
			authenticator.execute();
			userName = authenticator.getUserName();
			request.getSession().setAttribute(SANDBOX_USERNAME, GUESTUSER);
		} else {
			// not guest
			super.execute();
			request.getSession().setAttribute(SANDBOX_USERNAME, userName);
		}
		boolean islocalMode = "true"
				.equalsIgnoreCase(getProperty("local_mode"));
		String portlets = getProperty("portlets");
		// portlets are divided by ,
		if (islocalMode && portlets != null) {
			StringTokenizer st = new StringTokenizer(portlets, ",");
			while (st.hasMoreElements()) {
				String portlet = (String) st.nextElement();
				// pass userProfile to portlets in vignette way
				request.setAttribute(VIGNETTE_PREFIX + portlet
						+ ".javax.portlet.userinfo", request.getSession()
						.getAttribute(AuthenticationConsts.USER_PROFILE_KEY));
				// pass userProfile to portlets in vignette way
				request.setAttribute(VIGNETTE_PREFIX + portlet + "."
						+ Consts.PORTAL_CONTEXT_KEY,
						retrieveUserContextKeys(request));
			}
		}
		User user = SessionUtils.getCurrentUser(request.getSession());
		if (user != null) {
			// make up locale for user
			String country = ssoUser.getCountry();
			String language = ssoUser.getLanguage();
			Locale locale = Locale.ENGLISH;
			if (language != null) {
				locale = country != null ? new Locale(language, country)
						: new Locale(language);
			}
			if (!user.isGuestUser()) {
				I18nUtils.setUserLocale(user, locale);
			}
		}
	}

	/**
	 * @param request
	 *            user original request
	 * @return user context key map whose values are {@link String} objects
	 */
	private Map<String, String> retrieveUserContextKeys(
			HttpServletRequest request) {
		Map<String, String> userContext = new HashMap<String, String>();
		userContext.put(Consts.KEY_PORTAL_SITE_URL, Utils
				.getPortalSiteURL(request));
		userContext.put(Consts.KEY_PORTAL_REQUEST_URL, Utils
				.getRequestURL(request));
		userContext.put(Consts.KEY_PORTAL_SITE_NAME, Utils
				.getEffectiveSiteDNS(request));
		userContext.put(Consts.KEY_PORTAL_SESSION_ID, request.getSession(true)
				.getId());
		userContext.put(Consts.KEY_SESSION_TOKEN, getHppSessionToken(request));
		return userContext;
	}

	/**
	 * @param request
	 *            incoming user request
	 * @return value of <code>SMSESSION</code> cookie set by HPP or empty string
	 *         if non could be found
	 */
	private String getHppSessionToken(HttpServletRequest request) {
		Cookie[] cookies = null;
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't
		// know the underlying request implementation
		synchronized (request) {
			cookies = request.getCookies();
		}
		if (cookies != null) {
			for (int i = 0, len = cookies.length; i < len; i++) {
				Cookie cookie = cookies[i];
				if ("SMSESSION".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return "";
	}
}
