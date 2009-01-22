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
import com.epicentric.common.website.Localizer;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.site.Site;
import com.epicentric.user.User;

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

	private String profileFileName = null;

	private final String VIGNETTE_PREFIX = "com.vignette.portal.attribute.portlet.";


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
        
        profileFileName = retrieveProfileFile();
        try {
            LOG.info("Get Resource Bundle File = " + profileFileName);
            // Read current user's info.
            prop = ResourceBundle.getBundle(profileFileName);
            LOG
                    .info("Refresh Resource Bundle File for user "
                            + profileFileName);
        } catch (MissingResourceException e) {
            LOG.info("No Resource Bundle File = " + profileFileName);
            request.getSession().setAttribute(
                    AuthenticationConsts.SESSION_ATTR_SSO_ERROR, "1");
        }        
      
        // retrieve user profile
        userProfile = getUserProfile();       
        mapUserProfile2SSOUser();
    }

	private String retrieveProfileFile() {
		String rbFile = retrieveRbFile();
		// Get current user.
		String currentUser = (String) request.getSession().getAttribute(
				"sandbox.username");
		try {
			refreshBundle();
			LOG.info("Get Resource Bundle File = " + rbFile);
			rb = ResourceBundle.getBundle(rbFile);
			if (currentUser == null) {
				currentUser = rb.getString("CurrentUser");
			}
		} catch (MissingResourceException e) {
			LOG.info("No Resource Bundle File = " + rbFile);
		}
		Site currentSite = getCurrentSite();
		String currentSiteName = currentSite != null ? currentSite.getDNSName()
				: "console";
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
            while(st.hasMoreElements()) {
                String group = (String)st.nextElement();
                LOG.info("Get UserGroup = " + group);
                groups.add(group);
            }
        }
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
        while(propertyKeys.hasMoreElements()) {
            String customizedProfileKey = (String)propertyKeys.nextElement();
            customizedProfile.put(customizedProfileKey, prop
                    .getString(customizedProfileKey));
        }
        return customizedProfile;
    }

	/**
	 * This method is used to perform all related tasks. 1. It will invoke the
	 * mapRequest2User() method to map all the information from SSO product to
	 * SSOUser object. 2. Invoke the syncUser() method to perform the sync from
	 * SSOUser object to VAP. 3. Set the correct user name
	 * 
	 * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
	 */
	public void execute() {
		// first time login or initSession
		if (request.getSession().getAttribute("active") == null
				|| "true".equalsIgnoreCase((String) request
						.getParameter("initSession"))) {
			request.getSession().setAttribute("active", "true");
		}
		boolean active = "true".equals(request.getSession().getAttribute(
				"active"));
		// guestMode is for logout
		if ("true".equalsIgnoreCase(request.getParameter("guestMode"))) {
			// cleanupsession() will only clean session attributes starting from
			// sp_
			AuthenticatorHelper.cleanupSession(request);
			request.getSession().setAttribute("active", "false");
			userName = null;
			Localizer localizer = I18nUtils.getLocalizer(request
					.getSession(false), request);
			localizer.setLocale(Locale.ENGLISH);
		} else if (active) {
			// not guest
			super.execute();
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
	}
}
