/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import javax.servlet.http.HttpServletRequest;

import com.vignette.portal.log.LogConfiguration;

/**
 * AuthenticatorFactory is the factory class to create concret Authenticator.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 * @version TBD
 */
public class AuthenticatorFactory {
    private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper.getLog(
            AuthenticatorFactory.class);

    private AuthenticatorFactory() {
    }

    /**
     * Create proper Authenticator based on certain conditions
     * 
     * @param request HttpServletRequest object
     * @return the created new concrete Authenticator
     */
    public static IAuthenticator createAuthenticator(HttpServletRequest request) {
        if (request == null) {
            LOG.error("system wrong: have a NULL request.");
            return null;
        }
        if (AuthenticatorHelper.isSandBox()) {
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("create TestAuthenticator");
            }            
            return new TestAuthenticator(request);
        } else if (AuthenticatorHelper.loggedIntoAtHP(request)) {
            // athp loged in
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("create AtHPAuthenticator");
            }
            return new AtHPAuthenticator(request);
        } else if (AuthenticatorHelper.loggedIntoHPP(request)
                || AuthenticatorHelper.loggedIntoFed(request)) {
            // hpp loged in
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("create HPPAuthenticator");
            }
            return new HPPAuthenticator(request);
        } else {
            // at anon state
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("create ANONAuthenticator");
            }
            return new ANONAuthenticator(request);
        }
    }

}
