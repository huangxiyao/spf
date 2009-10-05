/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hp.it.spf.xa.exception.portal.ExceptionUtil;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;

/**
 * This is a servlet filter will be invoked for every user's request. This
 * filter will do some actions for SSO and login
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version TBD
 */
public class SessionInitializationFilter implements Filter {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(SessionInitializationFilter.class);

    /**
     * Filter init operations
     * 
     * @param config FilterConfig
     * @throws ServletException javax.servlet.ServletException
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * This method will be called every time when this filter is invoked. It
     * will do: Redirect user to system error page if session error flag is
     * caught
     * 
     * @param request javax.servlet.ServletRequest Current request
     * @param response javax.servlet.ServletResponse Current response
     * @param chain javax.servlet.FilterChain Current Filter Chain
     * @throws IOException java.io.IOException
     * @throws ServletException javax.servlet.ServletException
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     * @see com.hp.serviceportal.framework.portal.exception.ExceptionUtil
     *      #redirectSystemErrorPage(com.vignette.portal.website.enduser.PortalContext,
     *      java.lang.String, java.lang.String, java.lang.String)
     * @see com.hp.serviceportal.framework.portal.authentication.AuthenticatorHelper
     *      #loggedIntoHPP(javax.servlet.http.HttpServletRequest)
     */
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException,
                                           ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse res = (HttpServletResponse)response;
            HttpSession session = req.getSession();

            if (session.getAttribute(AuthenticationConsts.SESSION_ATTR_SSO_ERROR) == null) {
                // retrieve sso username and set it into the session
                doSessionInitialize(req, res);

                // handle error if needed
                if (isRedirectAfterHandleError(req, res)) {
                    return;
                }
            } else {
            	// if there is a error tag in session, which means that current
                // request page is error handling page, cleanup session and error flag, 
            	// then return guest user.
            	
            	AuthenticatorHelper.cleanupSession(req);
                request.setAttribute(AuthenticationConsts.SSO_USERNAME, null);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Retrieve the SSO username according to the type of the Authenticator
     * defined in the request, and set SSO username into current session
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    private void doSessionInitialize(HttpServletRequest request,
                                     HttpServletResponse response) {
        String ssoUsername = null;
        request.setAttribute(AuthenticationConsts.SSO_USER_LOCALE,
                             response.getLocale());
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Retrieve loacle from response," + response.getLocale());
        }
        IAuthenticator authenticator = AuthenticatorFactory.createAuthenticator(request);
        if (authenticator != null) {
            authenticator.execute();
            ssoUsername = authenticator.getUserName();
        }
        request.setAttribute(AuthenticationConsts.SSO_USERNAME, ssoUsername);
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Retrieve SSO username," + ssoUsername);
        }
    }

    /**
     * Handle errors and check if the request is doing the sendRedirect
     * operation
     * 
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return is sendRedirect
     * @throws IOException
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    private boolean isRedirectAfterHandleError(HttpServletRequest req,
                                               HttpServletResponse res) throws IOException,
                                                                       ServletException {
        // If the session error flag is caught, redirect user to the system
        // error page.
        if (req.getSession()
               .getAttribute(AuthenticationConsts.SESSION_ATTR_SSO_ERROR) != null) {
            HashMap map = new HashMap(3);
            map.put(ExceptionUtil.ERROR_CODE_ATTR, "system-user_synch_failure");
            req.getSession().setAttribute(ExceptionUtil.SESSION_ATTR_SYSTEM_ERROR_DATA, map);

            if (req.getRequestURI().toUpperCase().indexOf("ERROR") == -1) {
                PortalURI uri = ExceptionUtil.redirectSystemErrorPage((PortalContext)req.getAttribute("portalContext"),
                                                                      null,
                                                                      "Error system-user_synch_failure",
                                                                      null);
                if (uri != null) {
                    LOG.error("caught by filter - redirecting to system error page");
                    res.sendRedirect(uri.toString());
                } else {
                    LOG.error("caught by filter - redirecting to system error page default");
                	String defaultUri = req.getContextPath();
                	defaultUri += "/site/";
                	defaultUri += Consts.SPF_CORE_SITE + "/";
                	defaultUri += Consts.PAGE_FRIENDLY_URI_SYSTEM_ERROR;
                	defaultUri = Utils.slashify(defaultUri);
                	res.sendRedirect(defaultUri);
                }
                return true;
            }
        }
        return false;
    }
}
