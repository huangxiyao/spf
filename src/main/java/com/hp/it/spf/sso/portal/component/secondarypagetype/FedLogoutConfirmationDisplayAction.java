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
 * This display action is used to present a logout-confirmation page to the
 * federated HPP user. First the action ensures the user is actually federated.
 * Next the action forwards to the federated logout confirmation secondary page.
 * 
 * @author <link href="ysu@hp.com">Sun yu</link>
 * @version TBD
 */
public class FedLogoutConfirmationDisplayAction extends BaseAction {

	private static final LogWrapper LOG = new LogWrapper(
			FedLogoutConfirmationDisplayAction.class);

	/**
	 * Ensure the user is actually federated; redirect to the portal site home
	 * page if not; but if the user is federated, forward to the logout
	 * confirmation page. In the latter case, also set a request attribute
	 * containing the URL for resuming the user's session. The logout
	 * confirmation view can present that URL to the user.
	 * 
	 * @return The destination URI where user will be redirected after executing
	 * @param portalContext
	 *            The current portal context
	 */
	public PortalURI execute(PortalContext portalContext) {

		try {
			HttpServletRequest request = portalContext.getPortalRequest()
					.getRequest();

			LOG.info("FedLogoutConfirmationDisplayAction: invoked.");

			// Test that the user is actually federated. This test method is
			// assumed to send a redirect (eg to the portal site home page) if
			// the user is not federated. So if the user indicates not
			// federated, then just return.
			// TODO: Update the following line to use the new authentication
			// utility.
			if (/* !AuthenticationUtility.ensureFromFed(portalContext) */true) {
				LOG
						.info("FedLogoutConfirmationDisplayAction: user is not federated and has been redirected.");
				return null;
			}

			String url = portalContext.getSiteURI(portalContext
					.getCurrentSite().getDNSName());
			request
					.setAttribute(Consts.REQUEST_ATTR_FED_LOGOUT_RESUME_URL,
							url);
			return null;
		} catch (Exception ex) {
			// redirect to system error page if anything unusual happens
			LOG.error("FedLogoutConfirmationDisplayAction error: " + ex);
			return ExceptionUtil.redirectSystemErrorPage(portalContext, null,
					null, null);
		}
	}
}
