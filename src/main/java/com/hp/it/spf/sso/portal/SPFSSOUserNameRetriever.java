/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.authentication.SSOUsernameRetriever;
import com.vignette.portal.log.LogWrapper;

/**
 * This class implements SSOUsernameRetriever interface and returns a valid SSO user name
 * defined in session. Vignette will call getSSOUsername method in this class to
 * implement SSO
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 * @version TBD
 */
public class SPFSSOUserNameRetriever implements SSOUsernameRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(SPFSSOUserNameRetriever.class);

    /**
     * @param request
     *            HttpServletRequest object
     * @return userName
     * @see com.epicentric.authentication.SSOUsernameRetriever#getSSOUsername(javax.servlet.http.HttpServletRequest)
     */
    public String getSSOUsername(HttpServletRequest request) {
		String ssoUsername = null;
		HttpSession session = request.getSession(true);
		try {
		    ssoUsername = (String) session.getAttribute(AuthenticationConsts.SSO_USERNAME);
		} catch (Exception ex) {
		    LOG.debug("Can't find SSO username which must be already defined in http session, maybe this is a guest user");
		}
		return ssoUsername;
    }
}