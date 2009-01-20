/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.help.portal.component.secondarypagetype;

import javax.servlet.http.HttpServletRequest;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;
import com.hp.it.spf.xa.exception.portal.ExceptionUtil;
import com.hp.it.spf.xa.interpolate.portal.FileInterpolator;
import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * <p>
 * This is the action class for the global help secondary page type. It
 * interpolates the <code>globalHelp.html</code> secondary support file
 * (expected to exist in the global help secondary page component) and then
 * passes the interpolated file contents to the JSP for rendering.
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 */
public class GlobalHelpDisplayAction extends BaseAction {

	/**
	 * Log
	 */
	private static final LogWrapper LOG = new LogWrapper(
			GlobalHelpDisplayAction.class);

	/**
	 * Base name of portal default global help HTML file (secondary support file
	 * in this component)
	 */
	private static final String GLOBAL_HELP_BASE_NAME = "globalHelp.html";

	/**
	 * Base name of site-specific global help HTML file (secondary support file
	 * in this component)
	 */
	private static final String SITE_GLOBAL_HELP_BASE_NAME_SUFFIX = "GlobalHelp.html";

	/**
	 * Get the <code>globalHelp.html</code> file content, localized and
	 * interpolated, and store it into the request for display by the primary
	 * JSP.
	 * 
	 * @param portalContext
	 *            The encapsulated PortalContext object of current secondary
	 *            page.
	 * @return PortalURI The address where user is redirected to after
	 *         executing.
	 * @throws ActionException
	 *             throw out
	 */
	public PortalURI execute(PortalContext portalContext)
			throws ActionException {

		try {
			// Get the HttpServletRequest from portalContext
			HttpServletRequest request = portalContext.getPortalRequest()
					.getRequest();

			// TODO: Get the user groups using the new SPF method.
			// String[] userGroups = AuthenticationUtility
			// .getGroupsFromCurrentUser(request);
			String[] userGroups = null;
			FileInterpolator f = null;
			String baseName = null;
			String helpContent = null;

			LOG.info("GlobalHelpDisplayAction: invoked.");

			// First look for site-specific global help file.
			baseName = portalContext.getCurrentSite().getDNSName()
					+ SITE_GLOBAL_HELP_BASE_NAME_SUFFIX;
			f = new FileInterpolator(portalContext, baseName, userGroups);
			helpContent = f.interpolate();

			// If the content is not null or blank, then store into request
			if (helpContent != null && helpContent.trim().length() > 0) {
				LOG
						.info("GlobalHelpDisplayAction: rendering content from proper localized version of "
								+ baseName + " secondary support file.");
				request.setAttribute(Consts.REQUEST_ATTR_GLOBAL_HELP_DATA,
						helpContent);
			} else {
				// If the content was null or blank, try default global help
				// file.
				f = new FileInterpolator(portalContext, GLOBAL_HELP_BASE_NAME,
						userGroups);
				helpContent = f.interpolate();

				// If the content is not null or blank, then store into request.
				if (helpContent != null && helpContent.trim().length() > 0) {
					LOG
							.info("GlobalHelpDisplayAction: rendering content from proper localized version of "
									+ GLOBAL_HELP_BASE_NAME
									+ " secondary support file.");
					request.setAttribute(Consts.REQUEST_ATTR_GLOBAL_HELP_DATA,
							helpContent);
				} else {
					throw new Exception(
							"GlobalHelpDisplayAction: both "
									+ baseName
									+ " and "
									+ GLOBAL_HELP_BASE_NAME
									+ " secondary support files are empty or not found in this component.");
				}
			}
			// Redirect null so action will forward normally to view
			return null;
		} catch (Exception ex) {
			// Redirect to system error page if anything unusual happens
			LOG.error("GlobalHelpDisplayAction error: " + ex);
			return ExceptionUtil.redirectSystemErrorPage(portalContext, null,
					null, null);
		}
	}
}
