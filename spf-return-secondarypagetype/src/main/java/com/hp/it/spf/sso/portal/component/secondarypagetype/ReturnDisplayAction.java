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

import javax.servlet.http.Cookie;
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

/**
 * Pre-display action for secondary page used by external applications to as return page
 * to the portal. Its goal is to redirect the user to the appropriate page based on data
 * available to it (session attribute, cookie).
 *
 * As part of HPP SSO Federation tasks, a cookie is created for each partner
 * accessing the partner portal site in the
 * <code> com.hp.it.spf.misc.portal.filter.RememberReturnURLFilter </code>
 *
 * This cookie contains the "last visited site" DNS name. ReturnDisplayAction
 * uses the cookie as one of the rule to get the redirect URL. The following
 * rules are applied to retrieve the redirect URL.
 *
 * <pre>
 * 		if(most-visited-site-URL present in SESSION)
 * 			redirect the user to the most-visited-site-URL
 * 		else if(most-visited-site-URL present in COOKIE)
 * 			redirect the user to the most-visited-site-URL
 * 		else if(most-visited-site-URL present in REQUEST)
 * 			redirect the user to the most-visited-site-URL
 * </pre>
 */
public class ReturnDisplayAction extends BaseAction {

	private static final String PROPERTY_FILE = "spf-return-config.properties";

	private static final String WAIT_SECONDS_PROPERTY = "wait.seconds";

	private static final LogWrapper LOG = new LogWrapper(ReturnDisplayAction.class);

	private static final String UNKNOWN_SITE_PAGE_URI = "public/unknownsite";

	private static final String SPF_SITE = "portal/site/spf";

	/**
	 * Method checks for the return URL in this order Session, Cookie, Request.
	 * Once the return URL is obtained, redirect the user to that URL.
	 * 
	 * @param context
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
			HttpServletRequest request = context.getPortalRequest().getRequest();
			HttpSession session = request.getSession();

			/*
			 * Get the return URL from SESSION if exists
			 */
			String url = (String) session.getAttribute(Consts.SESSION_ATTR_RETURN_URL);

			if (url == null) {

				/*
				 * Get the return URL from the COOKIE if exists
				 */

				// Get HP_SPF_SITE cookie
				Cookie siteCookie = Utils.getCookie(Consts.COOKIE_NAME_SITE, request.getCookies());

				if (siteCookie != null) {
					// Inside because Cookie exists

					// Get the cookie value
					String siteCookieValue = siteCookie.getValue();

					if (siteCookieValue != null && siteCookieValue.length() > 0) {

						// if the cookie value is not the default site (spf)
						if (!siteCookieValue.trim().equals(
								com.hp.it.spf.xa.misc.Consts.SPF_CORE_SITE)) {
							// Set the site URL
							url = context.getSiteURI(siteCookieValue.trim());
						} else {
							// Set the default site (spf)public page for
							// redirection
							url = appendUnknownSitePage(context
									.getSiteURI(com.hp.it.spf.xa.misc.Consts.SPF_CORE_SITE));
						}

					}

				} else {

					// Inside because cookie does not exists

					/*
					 * Get the return URL from the REQUEST finally
					 */

					url = Utils.getEffectiveSiteURL(request);

					// In case the url is null or the request url is SPF site,
					// do below
					if (url == null || (url != null && url.trim().indexOf(SPF_SITE) != -1)) {

						// Set the default site (spf)public page for redirection
						url = appendUnknownSitePage(context
								.getSiteURI(com.hp.it.spf.xa.misc.Consts.SPF_CORE_SITE));

					}

				}

			}

			// In debug mode to write to log file only if needed
			LOG.debug("SPFDEBUG (ReturnDisplayAction): Return URL before redirecting := " + url);

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
			LOG.error("ReturnDisplayAction error: " + e, e);
			return ExceptionUtil.redirectSystemErrorPage(context, null);
		}

		return null;

	}

	private int getWaitMillis(PortalContext context) {
		String filename = PROPERTY_FILE;
		Properties properties = new Properties();
		int seconds = 0;
		LOG.debug("ReturnDisplayAction: loading properties from secondary support file: "
				+ filename);
		InputStream file = I18nUtility.getLocalizedFileStream(context, filename, false);
		if (file != null) {
			try {
				properties.load(file);
				// Expect value in seconds.
				seconds = Integer.parseInt(properties.getProperty(WAIT_SECONDS_PROPERTY));
				if (seconds < 0) {
					seconds = 0;
				}
			} catch (Exception e) {
				LOG.error("ReturnDisplayAction: error loading secondary support file " + filename
						+ ": " + e);
			}
		} else {
			LOG.error("ReturnDisplayAction: missing secondary support file " + filename);
		}
		// Return value in milliseconds.
		return seconds * 1000;
	}

	/**
	 * Appends the "unknown site" page URI that is created in SPF to show it to
	 * the users when the SPF cookie containing the user's last visited site
	 * does not exist. This page can be customizable according to the business
	 * needs and hence the user is redirected to this page.
	 * 
	 * @param uri
	 *            encoded string representing the SPF site URI
	 * @return URL containing the public page of SPF appended to the uri
	 */

	private static String appendUnknownSitePage(String uri) {

		if (uri == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer(uri.trim());
		if (!uri.trim().endsWith("/")) {
			sb.append("/");
		}

		sb.append(UNKNOWN_SITE_PAGE_URI);

		return sb.toString();

	}

}
