/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeselector.portal.component.secondarypagetype;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.BaseAction;

import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * <p>
 * This action class redirects to the target URL for the locale selector. It is
 * expected to be the final action called in the action chain for the SPF locale
 * selector secondary page.
 * </p>
 * <p>
 * This class expects the locale selector form to have passed it the target URL
 * in the <code>spfLocaleSelectorTarget</code> form parameter. The value is
 * expected to be a string URL. This is the contract which this action has with
 * the form.
 * </p>
 * 
 * @author <link href="ying-zhi.wu@hp.com">Oliver</link>
 * @version TBD
 */
public class SelectLocaleRedirectProcessAction extends BaseAction {

	private static final LogWrapper LOG = new LogWrapper(
			SelectLocaleRedirectProcessAction.class);

	/**
	 * The execute method for this action class.
	 * 
	 * @param portalContext
	 *            portalContext object
	 * @return PortalURI
	 */
	public PortalURI execute(PortalContext portalContext) {

		HttpServletRequest request = portalContext.getPortalRequest()
				.getRequest();
		HttpServletResponse response = portalContext.getPortalResponse()
				.getResponse();

		String redirectUrl = (String) request
				.getParameter(Consts.LOCALE_SELECTOR_TARGET_NAME);

		// if the target url is absent,use the HTTP referer header
		if (redirectUrl == null || redirectUrl.trim().equals("")) {
			redirectUrl = request.getHeader("Referer");
		}
		// not sure why we need to do this - therefore leaving it in
		redirectUrl = this.filterUrlLocaleParams(redirectUrl);

		LOG.info("Locale selector: redirecting to target URL: " + redirectUrl);
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException ex) {
			LOG.error("Redirect failed!  More detail: " + ex);
		}
		return null;
	}

	/**
	 * Filter the HP.com standard locale parameters from the URL (in case they
	 * were there).
	 */
	protected String filterUrlLocaleParams(String redirectUrl) {
		String[] urlComponents = redirectUrl.split("(\\?|&)");
		StringBuffer baseUrl = new StringBuffer(urlComponents[0]);
		StringBuffer params = new StringBuffer();
		for (int i = 1; i < urlComponents.length; i++) {
			if (!isALangOrCcParameter(urlComponents[i])) {
				if (params.length() == 0) {
					params.append("?" + urlComponents[i]);
				} else {
					params.append("&" + urlComponents[i]);
				}
			}
		}
		return baseUrl.append(params).toString();
	}

	/**
	 * Check if the parameter is <code>lang</code> or <code>cc</code>.
	 * 
	 * @param param
	 *            url parameter
	 * @return yes or no
	 */
	private boolean isALangOrCcParameter(String param) {
		return (param.indexOf("lang=") == 0 || param.indexOf("cc=") == 0);
	}
}
