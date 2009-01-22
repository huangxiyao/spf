/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

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
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;

/**
 * This is a servlet filter will be called for every user's request. This filter
 * will do some actions for SSO and login
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 * @version TBD
 */
public class SessionInitializationFilter implements Filter {
	private static final LogWrapper LOG = AuthenticatorHelper
			.getLog(SessionInitializationFilter.class);

	private static final ResourceBundle RES_BNDL = PropertyResourceBundleManager
			.getBundle(AuthenticationConsts.HEADER_CONSTS_FILE_BASE);

	/**
	 * @param config
	 *            FilterConfig
	 * @throws ServletException
	 *             javax.servlet.ServletException
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
	 * caught AND remove target from session if HPP user is loggedin AND deal
	 * with remember me cookie for HPP login page
	 * 
	 * @param request
	 *            javax.servlet.ServletRequest Current request
	 * @param response
	 *            javax.servlet.ServletResponse Current response
	 * @param chain
	 *            javax.servlet.FilterChain Current Filter Chain
	 * @throws IOException
	 *             java.io.IOException
	 * @throws ServletException
	 *             javax.servlet.ServletException
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 * @see com.hp.serviceportal.framework.portal.exception.ExceptionUtil
	 *      #redirectSystemErrorPage(com.vignette.portal.website.enduser.PortalContext,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 * @see com.hp.serviceportal.framework.portal.authentication.AuthenticatorHelper
	 *      #loggedIntoHPP(javax.servlet.http.HttpServletRequest)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			// retrieve sso username and set it into the session
			doSessionInitialize((HttpServletRequest) request);

			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;

			// handleHPP(req, res);

			if (isRedirectAfterHandleError(req, res)) {
				return;
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * retrieve the ssousername according to the type of the Authenticator
	 * defined in the request, and set ssousername into current session
	 * 
	 * @param request
	 *            HttpServletRequest object
	 */
	private void doSessionInitialize(HttpServletRequest request) {
		String ssoUsername = null;
		IAuthenticator authenticator = AuthenticatorFactory.createAuthenticator(request);
		if (authenticator != null) {
			authenticator.execute();
			ssoUsername = authenticator.getUserName();
		}
		request.setAttribute(AuthenticationConsts.SSO_USERNAME, ssoUsername);		
	}

	/**
	 * handle errors and check if the request is doing the sendRedirect
	 * operation
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @return is sendRedirect
	 * @throws IOException
	 * @throws ServletException
	 */
	private boolean isRedirectAfterHandleError(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		// If the session error flag is caught, redirect user to the system
		// error page.
		if (req.getSession().getAttribute(
				AuthenticationConsts.SESSION_ATTR_SSO_ERROR) != null) {
			HashMap map = new HashMap(3);
			map.put("errorCode", "Error system-user_synch_failure");
			req.getSession().setAttribute("systemErrorPage", map);

			if (req.getRequestURI().toUpperCase().indexOf("ERROR") == -1) {
				PortalURI uri = ExceptionUtil.redirectSystemErrorPage(
						(PortalContext) req.getAttribute("portalContext"),
						null, "Error system-user_synch_failure", null);

				LOG.error("catched by filter");
				res.sendRedirect(uri.toString());
				return true;
			}
		}
		return false;
	}

	/**
	 * handle HPP user
	 * 
	 * @param req
	 * @param res
	 */
	private void handleHPP(HttpServletRequest req, HttpServletResponse res) {
		// Just for hpp user
		if (AuthenticatorHelper.loggedIntoHPP(req)) {
			// remove the target session value
			HttpSession session = req.getSession();
			if (session
					.getAttribute(AuthenticationConsts.SESSSION_ATTR_SP_TARGET) != null) {
				session
						.removeAttribute(AuthenticationConsts.SESSSION_ATTR_SP_TARGET);
			}

			// Add for cleaning sptryno cookie
			String spTryNO = AuthenticatorHelper.getCookieValue(req,
					AuthenticationConsts.SPTRYNO_COOKIE);
			if (null != spTryNO) {
				res.addCookie(AuthenticatorHelper.newCookie(
						AuthenticationConsts.SPTRYNO_COOKIE, "",
						AuthenticationConsts.COOKIE_DOMAIN,
						AuthenticationConsts.COOKIE_PATH, 0));
			}

			String rememberMeCookie = AuthenticatorHelper.getCookieValue(req,
					AuthenticationConsts.COOKIE_REMEMBER_USER_FLAG);
			LOG.debug("remember me cookie value:" + rememberMeCookie);

			String userIdCookie = AuthenticatorHelper.getCookieValue(req,
					AuthenticationConsts.COOKIE_USER_ID);
			LOG.debug("user id cookie value:" + userIdCookie);

			String reqUserId = AuthenticatorHelper
					.getRequestHeader(
							req,
							getProperty(AuthenticationConsts.HEADER_USER_NAME_PROPERTY_NAME),
							true);
			LOG.debug("request user id:" + reqUserId);

			/**
			 * Below section deal with remember me cookie
			 */
			if (rememberMeCookie != null) {
				if ("1".equals(rememberMeCookie)) {
					if (userIdCookie == null || !reqUserId.equals(userIdCookie)) {
						res.addCookie(AuthenticatorHelper.newCookie(
								AuthenticationConsts.COOKIE_USER_ID, reqUserId,
								AuthenticationConsts.COOKIE_DOMAIN,
								AuthenticationConsts.COOKIE_PATH,
								365 * 24 * 60 * 60));
					}
				} else if ("0".equals(rememberMeCookie)) {
					if (userIdCookie != null) {
						res.addCookie(AuthenticatorHelper.newCookie(
								AuthenticationConsts.COOKIE_USER_ID, "",
								AuthenticationConsts.COOKIE_DOMAIN,
								AuthenticationConsts.COOKIE_PATH, 0));
					}
				}
				res.addCookie(AuthenticatorHelper.newCookie(
						AuthenticationConsts.COOKIE_REMEMBER_USER_FLAG, "",
						AuthenticationConsts.COOKIE_DOMAIN,
						AuthenticationConsts.COOKIE_PATH, 0));
			}
		}
	}

	private static String getProperty(String key) {
		try {
			return RES_BNDL.getString(key);
		} catch (Exception ex) {
			LOG.error("Can't find key " + key + "in resource bundle file");
			return null;
		}
	}
}
