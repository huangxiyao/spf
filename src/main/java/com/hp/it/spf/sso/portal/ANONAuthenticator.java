/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.I18nUtils;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.user.User;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * ANONAuthenticator is extended from AbstractAuthenticator.
 * This antenticator will called for anonymous users.
 * <p>
 * Anonymous user is the user whose username start with sso_guest_user_
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 * @version TBD
 */
public class ANONAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;
    
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(ANONAuthenticator.class);

    private static final String SSO_GUEST_USER_PREFIX = "sso_guest_user_";
    /**
     * This is the constructor for ANON Authenticator,
     * It will set the user name as null which means the guest user for Vignette
     * @param request HttpServletRequest object
     */
    public ANONAuthenticator(HttpServletRequest request) {
        this.request = request;
        userName = null;
    }

    /**
     * This method will retrieve the correct username for anonymous user
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
        
        if (currentUser != null) {
            String currUserName = (String)currentUser.getProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID);
            if (currUserName.startsWith(SSO_GUEST_USER_PREFIX)) {
                Locale userLocale = I18nUtils.getUserLocale(currentUser);   
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug("Retrieve loacle from session user," + userLocale);
                }
                if (userLocale.equals(reqLocale)) {
                    userName = currUserName;
                    return;
                }
            }            
            AuthenticatorHelper.cleanupSession(request);
        } 
        
        String language = reqLocale.getLanguage();
        String country  = reqLocale.getCountry();
        
        String ssousername = null;
        
        // search sso_guest_user_<locale> user
        ssousername = SSO_GUEST_USER_PREFIX + language + "-" + country;
        User vapUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID,
                                                                  ssousername);
        if (vapUser != null) {
            userName = ssousername;
            return;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("User" + ssousername + "not found.");
        }
        
        // search sso_guest_user_<language_from_locale> user
        ssousername = SSO_GUEST_USER_PREFIX + language;
        vapUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID,
                                                             ssousername);
        if (vapUser != null) {
            userName = ssousername;
            return;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("User" + ssousername + "not found.");
        }
        
        // for default user
        userName = SSO_GUEST_USER_PREFIX + AuthenticationConsts.DEFAULT_LANGUAGE;
        return;
    }    
}
