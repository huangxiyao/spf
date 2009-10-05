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
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.log.portal.LogHelper;

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
	 * Base name of portal default global help include properties file
	 * (secondary support file in this component)
	 */
	private static final String GLOBAL_HELP_INCLUDE_BASE_NAME = "globalHelpInclude.properties";

	/**
	 * Base name of site-specific global help HTML file (secondary support file
	 * in this component)
	 */
	private static final String SITE_GLOBAL_HELP_BASE_NAME_SUFFIX = "GlobalHelp.html";

	/**
	 * Base name of site-specific global help include properties file (secondary
	 * support file in this component)
	 */
	private static final String SITE_GLOBAL_HELP_INCLUDE_BASE_NAME_SUFFIX = "GlobalHelpInclude.properties";

	/**
	 * Get the global help HTML file content, localized and interpolated, and
	 * store it into the request for display by the primary JSP. The file
	 * content comes from this site's
	 * <code>&lt;site-name&gt;GlobalHelp.html</code> or the default
	 * <code>globalHelp.html</code>. Any token substitutions come from this
	 * site's <code>&lt;site-name&gt;GlobalHelpInclude.properties</code> or
	 * the default <code>globalHelpInclude.properties</code>.
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

			FileInterpolator f = null;
			String baseName = null;
			String propName = null;
			String helpContent = null;

			// First determine the proper global help file to use.
			baseName = portalContext.getCurrentSite().getDNSName()
					+ SITE_GLOBAL_HELP_BASE_NAME_SUFFIX;
			if (I18nUtility.getLocalizedFileName(portalContext, baseName) == null) {
				baseName = GLOBAL_HELP_BASE_NAME;
			}
			LOG.info("GlobalHelpDisplayAction: rendering content from "
					+ baseName + " resource bundle.");

			// Next determine the proper token substitutions property file to
			// use.
			propName = portalContext.getCurrentSite().getDNSName()
					+ SITE_GLOBAL_HELP_INCLUDE_BASE_NAME_SUFFIX;
			if (I18nUtility.getLocalizedFileName(portalContext, propName) == null) {
				propName = GLOBAL_HELP_INCLUDE_BASE_NAME;
			}
			LOG
					.info("GlobalHelpDisplayAction: include-tokens will be substituted from "
							+ propName + " resource bundle.");

			f = new FileInterpolator(portalContext, baseName, propName);
			helpContent = f.interpolate();

			// If the content is not null or blank, then store into request
			if (helpContent != null) {
				helpContent = helpContent.trim();
				if (helpContent.length() > 0) {
					request.setAttribute(Consts.REQUEST_ATTR_GLOBAL_HELP_DATA,
							helpContent);
				} else {
					throw new Exception(
							"GlobalHelpDisplayAction: interpolated content is empty.");
				}
			} else {
				throw new Exception(
						"GlobalHelpDisplayAction: interpolated content is null.");
			}
			// Return null so action will forward normally to view
			return null;
		} catch (Exception ex) {
			// Redirect to system error page if anything unusual happens
			LOG.error("GlobalHelpDisplayAction error: " + ex, ex);
			return ExceptionUtil.redirectSystemErrorPage(portalContext, null,
					null, null);
		}
	}
}
