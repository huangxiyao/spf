/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.user.User;

/**
 * ANONAuthenticator is extended from AbstractAuthenticator.
 * This antenticator will called for guest users.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @version TBD
 */
public class ANONAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;

    private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper.getLog(ANONAuthenticator.class);

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
     * This method will check whether the user has logged in.
     * If so, it will clean up session for this user.
     * 
     * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #isVAPLoggedIn(javax.servlet.http.HttpServletRequest)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #cleanupSession(javax.servlet.http.HttpServletRequest)
     * @see com.epicentric.common.website.SessionUtils#getCurrentUser(javax.servlet.http.HttpSession)
     */
    public void execute() {
        // Check whether VAP is logged in
        boolean isVAPLoggedIn = AuthenticatorHelper.isVAPLoggedIn(request);
        // if VAP is logged in, need to do the cleanup
        if (isVAPLoggedIn) {
            LOG.info("In method syncUser, when VAP is logged in do the cleanup sessions");
            AuthenticatorHelper.cleanupSession(request);
            // return the guest user name
            userName = (String)((User)SessionUtils.getCurrentUser(request
                    .getSession())).getProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID);
        }
    }
    
    /**
     * retrieve anonymouse user's profile
     */
    protected Map<String, String> getUserProfile() {
    	return null;
    }
    
    /**
     * retrieve anonymouse user's group
     */
    @SuppressWarnings("unchecked")
	protected Set getUserGroup() {
    	return null;
    }    
}
