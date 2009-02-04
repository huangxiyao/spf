/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This authenticator is used for AtHP users
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 * @version TBD
 * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
 */
public class AtHPAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;

    private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper.getLog(AtHPAuthenticator.class);

    /**
     * This is the constructor for AtHPAuthenticator. It will call the
     * constructor of AbstractAuthenticator which is its father class To do the
     * initialization task.
     * 
     * @param request
     *            HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
     *      #AbstractAuthenticator(javax.servlet.http.HttpServletRequest)
     */
    public AtHPAuthenticator(HttpServletRequest request) {
        super(request);
    }
    
    /**
     * @see AbstractAuthenticator#mapHeaderToUserProfileMap()
     */
    @SuppressWarnings("unchecked")
    protected void mapHeaderToUserProfileMap() {
        super.mapHeaderToUserProfileMap();

        // if profileid is not specified, then use email to instead
        String profileId = (String)userProfile.get(AuthenticationConsts.KEY_PROFILE_ID);
        if (profileId == null || profileId.trim().equals("")) {
            userProfile.put(AuthenticationConsts.KEY_PROFILE_ID,
                            userProfile.get(AuthenticationConsts.KEY_EMAIL));
        }
    }

    /**
     * retrieve user profile for atHP
     */
    protected Map<String, String> getUserProfile() {
        return null;
    }
}
