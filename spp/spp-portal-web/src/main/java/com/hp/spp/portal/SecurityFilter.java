/**
 * 
 */
package com.hp.spp.portal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.navigation.MenuItem;
import com.epicentric.template.Style;
import com.hp.spp.portal.common.helper.PortalHelper;
import com.hp.spp.portal.common.helper.PortalURIHelper;
import com.vignette.portal.util.StringUtils;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * @author PBRETHER
 * 
 */
public class SecurityFilter implements Filter {

	private static final String URI_SEGMENT_SITE = "/portal/site/";

	private static final String URI_CONSOLE = "/portal/console";

	protected PortalHelper mPortalHelper = null;

	private static Logger mLog = Logger.getLogger(SecurityFilter.class);

	/**
	 * Initialize object variables and log the initalization of the filter
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		mLog.info("Initializing SecurityFilter");
		mPortalHelper = new PortalHelper();
	}

	/**
	 * Main filter method. Checks several security requirements. In case any of
	 * the requirements are not met then a redirect is performed. The
	 * requirements are:
	 * <li>
	 * <ol>
	 * That the user is accessing a site via a URL that can be protected by HPP.
	 * The protection is done by realm and the realm is only recognized if it is
	 * followed by a slash. For example, /portal/site/mysite is not protected,
	 * but /portal/site/mysite/ is protected.
	 * </ol>
	 * <ol>
	 * That the protocol (http/https) in the request is the same as that defined
	 * for the menu item or site
	 * </ol>
	 * </li>
	 * 
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (mLog.isDebugEnabled()) {
			mLog.debug("In doFilter of SecurityFilter");
		}

		String redirectUrl = performChecksForAllRequests(request);

		// if none of the checks for all requests require a redirection,
		// move on to the portal specific checks
		if (redirectUrl == null) {

			PortalContext portalContext = (PortalContext) request
					.getAttribute(PortalURIHelper.PORTAL_CONTEXT);

			// if the portal context is not null then we are in the portal
			// (/portal/site/*)
			if (portalContext != null) {
				redirectUrl = performChecksForPortalSpecificRequests(portalContext,
						request);
			}
		}

		if (redirectUrl != null) {
			((HttpServletResponse) response).sendRedirect(redirectUrl);
			if (mLog.isDebugEnabled()) {
				mLog.debug("Redirect from SecurityFilter: " + redirectUrl);
			}
		} else {
			if (mLog.isDebugEnabled()) {
				mLog
						.debug("No redirects as a result of the security filter checks");
			}
			chain.doFilter(request, response);
		}
	}

	/**
	 * Method that contains all the portal specific checks
	 * 
	 * @param portalContext
	 * @param request
	 * @return the URL to redirect to in case one of the checks requires 
	 * it
	 */
	private String performChecksForPortalSpecificRequests(PortalContext portalContext,
			ServletRequest request) {
		// for the moment we have only one check
		String redirectUrl = getProtocolRedirect(portalContext, request);
		// add further portal specific checks here:

		return redirectUrl;
	}

	/**
	 * Method contains all the checks that are made to all pages, whether portal
	 * pages or others (this includes admin JSP pages and console, depending on 
	 * the scope of the filter)
	 * 
	 * @param request
	 * @return the URL to redirect to in case one of the checks requires 
	 * it
	 */
	private String performChecksForAllRequests(ServletRequest request) {
		// for the moment we have only one check
		String redirectUrl = getHPPProtectedUrl(request);
		// add further checks for all requests here:

		return redirectUrl;
	}

	/**
	 * Gets the URL to which we need to redirect if the URL requested by the
	 * user is not a protected site.
	 * <p>
	 * HPP protects by 'realm'. For example, in the URL /portal/site/mysite/,
	 * HPP can protect the realm 'mysite'. However, if the user types
	 * /portal/site/mysite without the trailing slash, then HPP does not see
	 * 'mysite' as a realm and the URL is not protected, though this is a valid
	 * vignette URL that points to 'mysite'. This method checks whether the user
	 * is accessing a site, and if so it checks that the site name is followed
	 * by a slash. If not, the slash is added. The method also checks whether
	 * the user is trying to access the console without the trailing slash. If
	 * so, the slash is appended.
	 * <p>
	 * So, <code>/portal/site/mysite</code> =>
	 * <code>/portal/site/mysite/</code>
	 * 
	 * @param request
	 *            Used to get the requested URL
	 * @return null if there is no need to redirect, otherwise returns the URL
	 *         to redirect to
	 */
	private String getHPPProtectedUrl(ServletRequest request) {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestedUrl = httpRequest.getRequestURL().toString();
		if (mLog.isDebugEnabled()) {
			mLog.debug("Requested URI: " + requestedUrl);
		}

		// if the URI ends with '/' then we are sure it will be protected
		if (requestedUrl.endsWith("/")) {
			return null;
		}

		String protectedUrl = null;

		// check if the URL contains /portal/site/
		if (StringUtils.contains(requestedUrl, URI_SEGMENT_SITE)) {
			protectedUrl = getProtectedSiteUrl(requestedUrl);
		}
		// check if the URL ends in /portal/console
		else if (requestedUrl.endsWith(URI_CONSOLE)) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Console access with no trailing slash");
			}
			// simple: we just add the trailing slash
			protectedUrl = requestedUrl + "/";
		}

		// if the URL neither contains /portal/site/ nor ends with
		// /portal/console
		// then the method will return null
		return protectedUrl;
	}

	/**
	 * Converts a site URL such as /portal/site/smartportal to
	 * /portal/site/smartportal/
	 * 
	 * @param requestedUrl
	 *            Originally requested URL
	 * @return The protected site URL (with the trailing slash)
	 */
	private String getProtectedSiteUrl(String requestedUrl) {

		String protectedUrl = null;
		if (mLog.isDebugEnabled()) {
			mLog.debug("Site access with no trailing slash");
		}
		int indexOfSite = requestedUrl.indexOf(URI_SEGMENT_SITE)
				+ URI_SEGMENT_SITE.length();
		if (requestedUrl.indexOf('/', indexOfSite) == -1) {
			protectedUrl = requestedUrl + "/";
		}
		return protectedUrl;
	}

	/**
	 * If the protocol in the request is not the same as the protocol defined
	 * for the menu item or the site, then returns the full URL with the correct
	 * protocol. This URL should be used to redirect.
	 * 
	 * @param portalContext
	 * @param request
	 * @return
	 */
	private String getProtocolRedirect(PortalContext portalContext,
			ServletRequest request) {

		String redirectUrl = null;

		String specifiedProtocol = null;

		Style secondaryPage = portalContext.getCurrentSecondaryPage();

		String friendlyId = secondaryPage.getFriendlyID();

		if (mLog.isDebugEnabled()) {
			mLog.debug("Secondary page friendly ID: "
					+ secondaryPage.getFriendlyID());
		}

		if (isTemplate(friendlyId)) {
			String title = secondaryPage.getTitle();
			if (mLog.isDebugEnabled()) {
				mLog.debug("Template found");
			}

			specifiedProtocol = mPortalHelper.getProtocolFromTitle(title,
					portalContext);

		}
		// otherwise it is a menu item so check the menu item name
		else {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Menu item found");
			}

			MenuItemNode selectedNode = MenuItemUtils
					.getSelectedMenuItemNode(portalContext);

			MenuItem item = selectedNode.getMenuItem();

			// if the item is null then return the site setting
			if (item == null) {
				specifiedProtocol = mPortalHelper
						.getSiteProtocol(portalContext);
			}
			else {
				String title = item.getTitle();
				specifiedProtocol = mPortalHelper.getProtocolFromTitle(title,
						portalContext);
			}
		}

		String requestProtocol = request.getScheme();

		// if the specified protocol is not the same as the one that has been
		// requested then create the redirect URL
		if (!specifiedProtocol.equals(requestProtocol)) {

			String uri = mPortalHelper
					.getRequestedURI((HttpServletRequest) request);

			if (mLog.isDebugEnabled()) {
				mLog.debug("Current URI: " + uri);
			}
			redirectUrl = mPortalHelper.buildFullURI(uri, specifiedProtocol,
					portalContext);
			if (mLog.isDebugEnabled()) {
				mLog.debug("Protocol not correct. Request protocol was "
						+ requestProtocol + ". Specified protocol was "
						+ specifiedProtocol);
			}
		}

		return redirectUrl;
	}

	/**
	 * Returns true if the firendly id corresponds to a template. As the friendlyId of the template is 
	 * its name, the only way we can know that it is a template is by making sure that it is not 
	 * a standard or jsp-include
	 * page.
	 * @param friendlyId
	 * @return
	 */
	private boolean isTemplate(String friendlyId) {
		return 
			!"PAGE".equals(friendlyId) &&
			!"spp_pagedisplay".equals(friendlyId) &&
			!"JSP_INCLUDE_PAGE".equals(friendlyId) &&
			!"DHTMLPAGE".equals(friendlyId);
	}

	/**
	 * Nullifies object variables and logs destruction of the filter
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		mLog.info("Destroying SecurityFilter");
	}

}
