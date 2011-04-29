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

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.I18nUtils;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.site.Site;
import com.epicentric.user.User;
import com.hp.it.spf.xa.misc.portal.Utils;

/**
 * This is the Authenticator for SandBox Mode. This authenticator can be run
 * without siteminder. All of the user info are read from property file.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
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
			LOG.debug("Get Resource Bundle File = " + profileFileName);
			// Read current user's info.
			prop = ResourceBundle.getBundle(profileFileName);
			LOG.debug("Refresh Resource Bundle File for user " + profileFileName
					+ ".properties");
		} catch (MissingResourceException e) {
			LOG.error("No Resource Bundle File = " + profileFileName);			

			// if not goes to console, act as anon user
			Site currentSite = Utils.getEffectiveSite(request);
			if (currentSite != null) {
				actAsANON = true;
			}
			
			// make up message pane
			StringBuffer message = new StringBuffer("Cound not find " + profileFileName + ".properties.");
			message.append(" Please create " + profileFileName + ".properties for user ");
			// Get current user
			String currentUser = request.getParameter("username");
			if (currentUser == null || "".equals(currentUser.trim())) {
				currentUser = (String) request.getSession().getAttribute(
						SANDBOX_USERNAME);
			}
			message.append(currentUser);
			String currentSiteName = currentSite != null ? Utils
					.getEffectiveSiteDNS(request) : "console";
			message.append(" on " + currentSiteName
					+ " site in %domain_home%/sandbox_resoureces/ directory.");
			request.getSession().setAttribute(MESSAGEPANE, message.toString());
			
			String profileFileName2 = "console_" + rb.getString("CurrentUser");
			LOG.debug("Get Resource Bundle File = " + profileFileName2);
			// Read current user's info.
			prop = ResourceBundle.getBundle(profileFileName2);
			LOG.debug("Refresh Resource Bundle File for user "
					+ profileFileName2);
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
			LOG.debug("Get Resource Bundle File = " + rbFile);
			rb = ResourceBundle.getBundle(rbFile);
			// if it is guest user or first time user, act as user console
			// vignette
			if (currentUser == null || "".equals(currentUser.trim())
					|| GUESTUSER.equals(currentUser)) {
				// in this kind, act as anon user
				if (Utils.getEffectiveSite(request) != null) {
					actAsANON = true;
				}
				return "console_" + rb.getString("CurrentUser");
			}
		} catch (MissingResourceException e) {
			LOG.error("No Resource Bundle File = " + rbFile);
		}
		// if some one goes to console
		if (Utils.getEffectiveSite(request) == null) {
			return "console_" + currentUser;
		}
		// some one goes to site
		return Utils.getEffectiveSiteDNS(request) + "_" + currentUser;
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
			LOG.error("Failed to refresh bundle due to " + e.getMessage(), e);
		}
	}

	/**
	 * Get group info and return as a Set
	 * 
	 * @return groups
	 */
	protected Set getUserGroups() {
		Set groups = new HashSet();
		String groupstring = getValue(AuthenticationConsts.HEADER_GROUP_NAME);
		// groups are divided by ,
		if (groupstring != null) {
			StringTokenizer st = new StringTokenizer(groupstring, ",");
			while (st.hasMoreElements()) {
				String group = (String) st.nextElement();
				LOG.debug("Get UserGroup = " + group);
				groups.add(group);
			}
		}
        // set authenticated user group
        groups.add(AuthenticationConsts.LOCAL_PORTAL_AUTHENTICATED_USERS);
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
	 * This method is used to perform all related tasks. 1. If act as anon user
	 * 2. act as normal user 
	 * 
	 * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
	 */
	public void execute() {
		if ("true".equalsIgnoreCase(request.getParameter("guestMode"))) {
			actAsANON = true;
		}
		// guestMode
		if (actAsANON) {
			ANONAuthenticator authenticator = new ANONAuthenticator(request);
			authenticator.execute();
			userName = authenticator.getUserName();
			request.getSession().setAttribute(SANDBOX_USERNAME, GUESTUSER);
		} else {
			// not guest
			super.execute();
			request.getSession().setAttribute(SANDBOX_USERNAME, userName);
		}

		User user = SessionUtils.getCurrentUser(request.getSession());
		if (user != null) {
			if (!user.isGuestUser()) {
				// use locale got from locale resolver
				I18nUtils.setUserLocale(user, (Locale)request.getAttribute(AuthenticationConsts.SSO_USER_LOCALE));
			}
		}
	}
}
