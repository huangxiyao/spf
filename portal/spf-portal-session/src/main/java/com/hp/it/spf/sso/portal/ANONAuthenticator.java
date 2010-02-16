/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.user.User;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * ANONAuthenticator is extended from AbstractAuthenticator. This authenticator
 * will called for anonymous users.
 * <p>
 * Anonymous user is the user whose username start with sso_guest_user_
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version TBD
 */
public class ANONAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;

    private static final LogWrapper LOG = AuthenticatorHelper.getLog(ANONAuthenticator.class);

    /**
     * This is the constructor for ANON Authenticator, It will set the user name
     * as null which means the guest user for Vignette
     * 
     * @param request HttpServletRequest object
     */
    public ANONAuthenticator(HttpServletRequest request) {
        this.request = request;
        userName = null;
    }

    /**
     * This method will retrieve the correct username for anonymous user
     * This method can be called either when a user comes in as an anonymous user initially or switches from
	 * an authenticated user to an anonymous user (by logging out). In either case, a new anonysmous
	 * profile is created for the user. However, only in the second case, will the user session be cleaned up
	 * before anonymous user profile is created.
	 *
     * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #isVAPLoggedIn(javax.servlet.http.HttpServletRequest)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #cleanupSession(javax.servlet.http.HttpServletRequest)
     * @see com.epicentric.common.website.SessionUtils#getCurrentUser(javax.servlet.http.HttpSession)
     */
    public void execute() {
        User currentUser = SessionUtils.getCurrentUser(request.getSession());
        Locale reqLocale = (Locale)request.getAttribute(AuthenticationConsts.SSO_USER_LOCALE);
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Retrieve loacle from request defined by LocaleFilter,"
                      + reqLocale);
        }

        if (currentUser != null) {
            String currUserName = (String)currentUser.getProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID);
            if (currUserName.startsWith(AuthenticationConsts.ANON_USER_NAME_PREFIX)) {
            	String langTag = currUserName.substring(AuthenticationConsts.ANON_USER_NAME_PREFIX.length());
                Locale userLocale = I18nUtility.languageTagToLocale(langTag);
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug("Retrieve loacle from session user," + userLocale);
                }
                if (userLocale.equals(reqLocale)) {
                    userName = currUserName;
                    return;
                }
            }
            else { // Cleanup session only when user switching from authenticated to anonymous per CR 86
            	AuthenticatorHelper.cleanupSession(request);
            }
        }

        String language = reqLocale.getLanguage();
        String country = reqLocale.getCountry();
        language = (language != null) ? language.trim().toLowerCase() : "";
        country = (country != null) ? country.trim().toLowerCase() : "";

        String ssousername = null;

        // search sso_guest_user_<locale> user
        ssousername = AuthenticationConsts.ANON_USER_NAME_PREFIX
                      + language
                      + "-"
                      + country;
        User vapUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID,
                                                                  ssousername);
        if (vapUser != null) {
            userName = ssousername;
            saveUserProfile2Session(vapUser);
            return;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("User" + ssousername + "not found.");
        }

        // search sso_guest_user_<language_from_locale> user
        ssousername = AuthenticationConsts.ANON_USER_NAME_PREFIX + language;
        vapUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID,
                                                             ssousername);
        if (vapUser != null) {
            userName = ssousername;
            saveUserProfile2Session(vapUser);
            return;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("User" + ssousername + "not found.");
        }

        // for default user
        ssousername = AuthenticationConsts.ANON_USER_NAME_PREFIX
                      + AuthenticationConsts.DEFAULT_LANGUAGE;
        vapUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID,
                                                             ssousername);
        if (vapUser != null) {
            userName = ssousername;
            saveUserProfile2Session(vapUser);
            return;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("User" + ssousername + "not found.");
        }

        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("No anonymous user according to loacle is found, Vignette Guest User will be used.");
        }
    }

    /**
     * Map user profile information and group information retrieved from vap
     * user into session as a map
     * 
     * @param vapUser vignette user
     */
    @SuppressWarnings("unchecked")
    protected void saveUserProfile2Session(User vapUser) {
        if (vapUser != null) {
            userProfile.put(AuthenticationConsts.KEY_LANGUAGE, 
                            vapUser.getProperty(AuthenticationConsts.PROPERTY_LANGUAGE_ID));
            userProfile.put(AuthenticationConsts.KEY_COUNTRY, 
                            vapUser.getProperty(AuthenticationConsts.PROPERTY_COUNTRY_ID));
            userProfile.put(AuthenticationConsts.KEY_TIMEZONE,
                            vapUser.getProperty(AuthenticationConsts.PROPERTY_SPF_TIMEZONE_ID));
            super.saveUserProfile2Session(vapUser);
        }
    }

	@Override
	protected Set getUserGroups() {
		return new HashSet();
	}
}
