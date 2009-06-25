/*
 * Project: Service Portal
 * Copyright (c) 2008 HP. All Rights Reserved
 * 
 * The comments
 *
 * FILENAME: ReturnDisplayAction.java
 * PACKAGE : com.hp.it.spf.sso.portal.component.secondarypagetype
 * $Id: $
 * $Log: $
 */
package com.hp.it.spf.sso.portal.component.secondarypagetype;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hp.it.spf.xa.exception.portal.ExceptionUtil;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;

public class ReturnDisplayAction extends BaseAction {

	private static final String PROPERTY_FILE = "spf-return-config.properties";

	private static final String WAIT_SECONDS_PROPERTY = "wait.seconds";

	private static final LogWrapper LOG = new LogWrapper(
			ReturnDisplayAction.class);

	/**
	 * Get the return URL from session and redirect the user to it. If it is not
	 * found in session, then redirect the user to the site home page URL.
	 * 
	 * @param portalContext
	 *            The encapsulated PortalContext object of current secondary
	 *            page.
	 * @return PortalURI The address where user is redirected to after
	 *         executing.
	 * @throws ActionException
	 *             throw out
	 */
	public PortalURI execute(PortalContext context) throws ActionException {

		try {
			// Get the HttpServletRequest from portalContext
			HttpServletRequest request = context.getPortalRequest()
					.getRequest();
			HttpSession session = request.getSession();

			// Get the return URL form session
			String url = (String) session
					.getAttribute(Consts.SESSION_ATTR_RETURN_URL);
			if (url == null) {
				LOG
						.info("ReturnDisplayAction: no return URL found - redirecting to site home page.");
				// if the return URL is null, set it to the site home page URL
				url = Utils.getEffectiveSiteURL(request);
			}

			// Must sleep for a while to allow HPP time to recalculate any
			// headers.
			int millis = getWaitMillis(context);
			LOG.info("ReturnDisplayAction: redirecting to: " + url + " after "
					+ millis + " millisecond wait period.");
			if (millis > 0) {
				Thread.sleep(millis);
			}
			// redirect user to the return URL
			context.getPortalResponse().getResponse().sendRedirect(url);
		} catch (Exception e) {
			LOG.error("ReturnDisplayAction error: " + e);
			return ExceptionUtil.redirectSystemErrorPage(context, null);
		}
		return null;
	}

	// //////////////////

	private int getWaitMillis(PortalContext context) {
		String filename = PROPERTY_FILE;
		Properties properties = new Properties();
		int seconds = 0;
		LOG
				.debug("ReturnDisplayAction: loading properties from secondary support file: "
						+ filename);
		InputStream file = I18nUtility.getLocalizedFileStream(context,
				filename, false);
		if (file != null) {
			try {
				properties.load(file);
				// Expect value in seconds.
				seconds = Integer.parseInt(properties
						.getProperty(WAIT_SECONDS_PROPERTY));
				if (seconds < 0) {
					seconds = 0;
				}
			} catch (Exception e) {
				LOG
						.error("ReturnDisplayAction: error loading secondary support file "
								+ filename + ": " + e);
			}
		} else {
			LOG.error("ReturnDisplayAction: missing secondary support file "
					+ filename);
		}
		// Return value in milliseconds.
		return seconds * 1000;
	}

}
