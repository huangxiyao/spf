/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.authentication.SSOUsernameRetriever;
import com.vignette.portal.log.LogWrapper;

/**
 * This class implements SSOUsernameRetriever interface and returns a valid SSO
 * username defined in session. Vignette will call getSSOUsername method in
 * this class to implement SSO
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version TBD
 */
public class SPFSSOUserNameRetriever implements SSOUsernameRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(SPFSSOUserNameRetriever.class);

    /**
     * Retrieve a valid SSO username
     * 
     * @param request HttpServletRequest object
     * @return authenticated user name
     * @see com.epicentric.authentication.SSOUsernameRetriever#getSSOUsername(javax.servlet.http.HttpServletRequest)
     */
    public String getSSOUsername(HttpServletRequest request) {
        if (request.getAttribute(AuthenticationConsts.SSO_USERNAME) != null) {
            return (String)request.getAttribute(AuthenticationConsts.SSO_USERNAME);
        } else {
            LOG.debug("Retrieve null from request for SSO username.");
            return null;
        }
    }
}