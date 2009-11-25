package com.hp.it.spf.misc.portal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import com.epicentric.site.Site;
import com.epicentric.template.Style;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Filter used to save user navigation state. The data saved by this filter is
 * used by <tt>PUBLIC_SPF_RETURN</tt> secondary page (implemented in
 * com.hp.it.spf.sso.portal.component.secondarypagetype.ReturnDisplayAction
 * class). Currently the filter saves 2 types of data:
 * <ul>
 * <li>current URL - is saved in a session attribute. If the users hit the
 * secondary page above during the session, they will be redirect to last
 * visited portal URL saved in session.</li>
 * <li>current site DNS name - is saved in a cookie. If the users hit the
 * secondary page above once their session expired and if they have this cookie
 * set, they will be redirected to that site-in-cookie's home page.</li>
 * </ul>
 */
public class RememberReturnURLFilter implements Filter {
	private static final String PAGE_FRIENDLY_ID_BINARYPORTLET = "BINARYPORTLET";

	private static final LogWrapper LOG = new LogWrapper(
			RememberReturnURLFilter.class);

	/**
	 * This filter performs the setting up of the Return URL in the Session and
	 * also the User's last visited site DNS name in the SPF site Cookie.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {

			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;

			saveCurrentUrlInSession(req);
			saveCurrentSiteInCookie(req, resp);

		}

		chain.doFilter(request, response);
	}

	/**
	 * Saves the current URL as session attribute so if external applications
	 * call <tt>PUBLIC_SPF_RETURN</tt> secondary page during the current
	 * session, the page will redirect the user to that URL.
	 * 
	 * @param request
	 *            portal request
	 */
	private void saveCurrentUrlInSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		// save the current URL to session
		PortalContext context = (PortalContext) request.getAttribute("portalContext");
		if (context != null) {
			Style thisPage = context.getCurrentSecondaryPage();
			if (thisPage != null) {
				if (!Consts.PAGE_FRIENDLY_ID_RETURN.equals(thisPage
						.getFriendlyID())) {
					if (!PAGE_FRIENDLY_ID_BINARYPORTLET.equals(thisPage
							.getFriendlyID())) {
						String currentURL = Utils.getRequestURL(request);
						if (session != null) {
							session.setAttribute(
									Consts.SESSION_ATTR_RETURN_URL, currentURL);
						}
					}
				}
			}
		}
	}

	/**
	 * Saves current site's DNS name in cookie os if external applications call
	 * <tt>PUBLIC_SPF_RETURN</tt> secondary page and the cookie exists, the user
	 * will be redirected to that site's home (root) page. The cookie is created
	 * only if it not exists or if its value is different from the current site.
	 * 
	 * @param request
	 *            current request
	 * @param response
	 *            response to which cookie will be added.
	 */
	private void saveCurrentSiteInCookie(HttpServletRequest request, HttpServletResponse response)
	{
		/*
		 * Below code is written as part of HPP SSO Federation Set the cookie
		 * HP_SPF_SITE with the "last visited site" DNS name as value
		 */

		// Get the user's current site name from the request
		Site currentSite = Utils.getEffectiveSite(request);

		if (currentSite != null) {

			// Get the user's current site DNS name
			String currentSiteName = currentSite.getDNSName();
			// In debug mode to write to log file only if needed
			LOG.debug("SPFDEBUG (RememberReturnURLFilter): Create a cookie for"
					+ " the site DNS := " + currentSiteName);

			/*
			 * Create a SPF cookie for the user only when the current site is
			 * not the default site "spf". Create the cookie only if it does not
			 * exists or in case exists does not have the currentSiteName value
			 */
			if (currentSiteName != null
					&& !currentSiteName.equals(com.hp.it.spf.xa.misc.Consts.SPF_CORE_SITE)
					&& shouldCreateOrUpdateSiteCookie(request, currentSiteName)) {

				HttpServletResponse res = (HttpServletResponse) response;

				// Create a new cookie “HP_SPF_SITE” with value as “Last
				// Visited Site DNS Name”.
				// Set the cookie domain, path and age. Age of the cookie is
				// 2 years.

				res.addCookie(AuthenticatorHelper.newCookie(
						Consts.COOKIE_NAME_SITE, currentSiteName,
						com.hp.it.spf.xa.misc.Consts.HP_COOKIE_DOMAIN,
						com.hp.it.spf.xa.misc.Consts.HP_COOKIE_PATH,
						2 * 365 * 24 * 60 * 60));

			}

		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	/**
	 * Boolean to decide whether SPF can create/update a cookie (HP_SPF_SITE) if it
	 * does not exist and in case exists should not contain the value of user's
	 * currentSiteName.
	 * 
	 * @param req
	 *            Request object
	 * @param currentSiteName
	 *            The user's current site DNS name for which cookie will be
	 *            created
	 * @return true if HP_SPF_SITE cookie need to be created/updated; false otherwise
	 */
	private boolean shouldCreateOrUpdateSiteCookie(HttpServletRequest req,
			String currentSiteName) {

		// Return false in case the request is null
		if (req == null) {
			return false;
		}

		Cookie[] cookies = req.getCookies();

		if (cookies != null) {

			for (int cnt = 0; cnt < cookies.length; cnt++) {
				Cookie cookie = cookies[cnt];

				if (cookie != null) {

					if (Consts.COOKIE_NAME_SITE.equals(cookie.getName())) {

						/*
						 * Inside when the cookie is HP_SPF_SITE.
						 * 
						 * If the value of the cookie is currentSiteName, then
						 * return false indicating the cookie already exists for
						 * current site.
						 * 
						 * Otherwise the cookie needs to be updated, therefore return true.
						 */
						if (cookie.getValue() != null
								&& cookie.getValue().trim().equals(currentSiteName.trim()))
						{
							return false;
						}
						else {
							return true;
						}
					}
				}
			}
		}

		/*
		 * Return true in case the control reaches this point as it means the
		 * cookie is not yet created
		 */
		return true;
	}
}
