/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal.component.secondarypagetype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hp.it.spf.sso.portal.AuthenticationUtility;
import com.hp.it.spf.xa.exception.portal.ExceptionUtil;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.BaseAction;

/**
 * <p>
 * This display action is called when the user clicks the logout link. It just
 * redirects to the appropriate logout target page, which varies based on
 * authentication type (Standard HPP, AtHP, or Federated HPP).
 * </p>
 * <dl>
 * <p>
 * <dt>Standard HPP</dt>
 * <dd>Redirects to the portal site home page. Because SiteMinder can support
 * only one logout page per Web server, this action will typically run under the
 * central SPF-default site, not the actual site the user was using. Thus the
 * site name to redirect cannot be taken from the current request as usual.
 * Instead, it is expected to have been put into a parameter in the URL (named
 * with the value of the portal <code>Consts.PARAM_LOGOUT_SITE</code>,
 * currently <code>spfSite</code>) by the code which constructed the logout
 * hyperlink which the user clicked to invoke this action. <blockquote> <b>Note:</b>
 * In the standard HPP case, a localized logout-confirmation message string is
 * put into the HTTP session. The portal site home page can choose to read (and
 * erase) and display this message, if desired. The session attribute name is
 * the value of the portal <code>Consts.SESSION_ATTR_STATUS_MSG</code>
 * attribute. </blockquote> </dd>
 * </p>
 * <p>
 * <dt>AtHP</dt>
 * <dd>Redirects to the standard logout confirmation page hosted on the AtHP
 * portal: <code>http://athp.hp.com/portal/site/athp/template.LOGOUT</code>.</dd>
 * </p>
 * <p>
 * <dt>Federated HPP</dt>
 * <dd>Redirects to the federated logout confirmation page, if it exists on
 * this site; otherwise redirects to the portal site home page. The note above
 * applies, regarding how the site to redirect is determined.</dd>
 * </p>
 * </dl>
 * <p>
 * <b>Note:</b> This action does not have to cleanup portal session itself. It
 * is assumed the SSO module already did that. So there is no session-cleanup
 * logic in this action.
 * </p>
 * 
 * @author <link href="kaijian.ding@hp.com">Dingk</link>
 * @version TBD
 */
public class LogoutDisplayAction extends BaseAction {

	/**
	 * The URL to which to redirect upon logout in the athp case.
	 */
	private static final String ATHP_LOGOUT_URL = "http://athp.hp.com/portal/site/athp/template.LOGOUT";

	private static final LogWrapper LOG = new LogWrapper(
			LogoutDisplayAction.class);

	/**
	 * Cleanup the session and, based on the authentication type, redirect to
	 * the appropriate logout target.
	 * 
	 * @return The destination URI where user will be redirected after executing
	 * @param portalContext
	 *            The current portal context
	 */
	public PortalURI execute(PortalContext portalContext) {

		try {
			HttpServletRequest request = portalContext.getPortalRequest()
					.getRequest();
			HttpSession session = request.getSession(true);
			String url = null;

			LOG.info("LogoutDisplayAction: invoked.");

			// determine which site to redirect to
			String site = Utils.getEffectiveSiteDNS(request);

			// redirect to AtHP landing page in AtHP case
			if (AuthenticationUtility.isFromAtHP(request)) {
				LOG
						.info("LogoutDisplayAction: user was accessing from @HP - redirecting to @HP logout confirmation page.");
				url = ATHP_LOGOUT_URL;
			} else if (Utils.isFederatedSite(site)) {
				// otherwise, if not AtHP, assume we are using HPP, and check
				// for federation.
				// if federated, redirect to federated landing page at the
				// proper portal site.
				LOG
						.info("LogoutDisplayAction: user was accessing from federated HPP - redirecting to federated logout confirmation page.");
				// use TARGET parameter (typically not present) just in case
				url = request.getParameter(Consts.PARAM_SM_TARGET);
				if (url == null) {
					url = Utils.getEffectiveSiteURL(request, Consts.PAGE_FRIENDLY_URI_FED_LOGOUT);
				}
			} else {
				// finally, assume standard HPP case and redirect to portal site
				// home page. Put a localized logout confirmation message into
				// session, in case the target page chooses to display it. If no
				// such message resource, then put blank into session.
				LOG
						.info("LogoutDisplayAction: user was standard HPP - redirecting to site home page.");
				String msg = I18nUtility.getValue(portalContext
						.getCurrentSecondaryPage().getUID(),
						"logout.confirmation.text", "", portalContext);
				session.setAttribute(Consts.SESSION_ATTR_STATUS_MSG, msg);
				url = Utils.getEffectiveSiteURL(request);
			}

			// send the redirect and return null to allow processing to continue
			// normally.
			LOG.info("LogoutDisplayAction: redirecting to: " + url);
			portalContext.getPortalResponse().getResponse().sendRedirect(url);
			return null;
		} catch (Exception ex) {
			// redirect to system error page if anything unusual happens
			LOG.error("LogoutDisplayAction error: " + ex);
			return ExceptionUtil.redirectSystemErrorPage(portalContext, null,
					null, null);
		}
	}
}
