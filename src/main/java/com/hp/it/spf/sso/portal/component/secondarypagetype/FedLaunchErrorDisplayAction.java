/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal.component.secondarypagetype;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.exception.portal.ExceptionUtil;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * <p>
 * This display action is used to present a federation error page to the
 * federated HPP user. When the launch of a federated session occurs, there is
 * the possibility of an error, and the purpose of this page is to present that
 * error to the end-user. First the action ensures the user is actually
 * federated. Next the action forwards to the federation error secondary page.
 * </p>
 * <p>
 * <b>Note:</b> Currently there is no protocol for identifying the particular
 * error and displaying it to the user. So this is a generic error-handling page
 * for all manner of possible federation error conditions.
 * </p>
 * 
 * @author <link href="yus@hp.com">Sunyu</link>
 * @version TBD
 */
public class FedLaunchErrorDisplayAction extends BaseAction {

	private static final LogWrapper LOG = new LogWrapper(
			FedLaunchErrorDisplayAction.class);

	/**
	 * Ensure the user is actually federated; redirect to the portal site home
	 * page if not; but if the user is federated, forward to the generic
	 * federation error page. In the latter case, also set a request attribute
	 * containing the URL for trying again. The error view can present that URL
	 * to the user.
	 * 
	 * @return The destination URI where user will be redirected to after
	 *         executing
	 * @param portalContext
	 *            Current portalContext
	 * 
	 * @see com.vignette.portal.website.enduser.components.BaseAction
	 *      #execute(com.vignette.portal.website.enduser.PortalContext)
	 */
	public PortalURI execute(PortalContext portalContext) {

		try {
			HttpServletRequest request = portalContext.getPortalRequest()
					.getRequest();

			LOG.info("FedLaunchErrorDisplayAction: invoked.");

			// Test that the user is actually federated. This test method is
			// assumed to send a redirect (eg to the portal site home page) if
			// the user is not federated. So if the user indicates not
			// federated, then just return.
			// TODO: Update the following line to use the new authentication
			// utility.
			if (/* !AuthenticationUtility.ensureFromFed(portalContext) */true) {
				LOG
						.info("FedLaunchErrorDisplayAction: user is not federated and has been redirected.");
				return null;
			}

			// Get the launch URL so the user can try again if desired. The
			// launch URL may be in the "TARGET" query string parameter; if not,
			// just use the current portal site home page.
			String launchUrl = null;
			launchUrl = request.getParameter("TARGET");
			if (launchUrl == null) {
				launchUrl = portalContext.getSiteURI(portalContext
						.getCurrentSite().getDNSName());
			}
			request.setAttribute(Consts.REQUEST_ATTR_FED_ERROR_TRY_AGAIN_URL, launchUrl);
			return null;
		} catch (Exception ex) {
			// redirect to system error page if anything unusual happens
			LOG.error("FedLaunchErrorDisplayAction error: " + ex);
			return ExceptionUtil.redirectSystemErrorPage(portalContext, null,
					null, null);
		}

	}
}
