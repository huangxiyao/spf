package com.hp.spp.portal.mockhppwebagent;

import com.hp.spp.config.Config;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.portal.common.sql.PortalCommonDAOCacheImpl;
import com.hp.spp.profile.Constants;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


/**
 * MockHppWebAgentFilter mimics SiteMinder web agent that is installed on the web server.
 * This class should be used on in the development environment where SiteMinder is not installed.
 * It implements the basic security rules:
 * <ul>
 * <li>protect: /porta/console and redirect to /portal/site/console/template.PRELOGIN</li>
 * <li>protect: /portal/site/[sitename] and redirect to /portal/site/[sitename]/template.PRELOGIN</li>
 * </ul>
 * It also provides mock SMSESSION cookie through its {@link MockHppWebAgentLoginServlet}. The value
 * of username and hpp id taken from this cookie is then set by this filter as {@link Constants#SM_UNIVERSALID}
 * and {@link Constants#SM_USERDN} request headers.
 * Finally it also implements the logout by setting the value SMSESSION cookie to LOGGEDOFF.
 */
public class MockHppWebAgentFilter implements Filter {
	private static final String FILTER_APPLIED_REQUEST_KEY = MockHppWebAgentFilter.class.getName() + ".FILTER_APPLIED";
	private static final String SMSESSION_LOGGEDOFF = "LOGGEDOFF";
	private static final String TEMPLATE_PRELOGIN_URI = "/template.PRELOGIN/";

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (isFilterApplied(request)) {
			filterChain.doFilter(request, response);
		}
		else {
			setFilterApplied(request);

			RequestSiteInfo siteInfo = RequestSiteInfo.parse(request.getRequestURI());

			if (isLogoutRequest(request)) {
				logoutUser(request, response);
				filterChain.doFilter(request, response);
			}
			else if (isAccessToTemplatePrelogin(request)) {
				// For access to ../template.PRELOGIN/ we don't want to setup the SM_xxx headers.
				// Otherwise the SessionVerificationFilter will redirect indefinitely.
				filterChain.doFilter(request, response);
			}
			else if (isUserAutenticated(request)) {
				HppUserInfo hppUserInfo = getUserHppInfo(request);
				filterChain.doFilter(new MockHppWebAgentRequest(request, hppUserInfo.getUsername(), hppUserInfo.getProfileId()), response);
			}
			else if (isSiteRequest(siteInfo)) {
				// the use of this filter is only allowed for sites using mock profile
				if (!isSiteUsingMockProfile(siteInfo)) {
					throw new ServletException("For the security reasons " + getClass().getName() + " can only be used with mock profile!");
				}

				// if the request to a protected resource, we can redirect it directly to
				// login page as based on the first condition we know that the user is not
				// authenticated.
				if (isRequestToProtectedResource(request, siteInfo)) {
					redirectToLoginPage(request, response, siteInfo);
				}
				else {
					filterChain.doFilter(request, response);
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
	}

	private boolean isAccessToTemplatePrelogin(HttpServletRequest request) {
		return request.getRequestURI().indexOf(TEMPLATE_PRELOGIN_URI) != -1;
	}

	private boolean isSiteRequest(RequestSiteInfo siteInfo) {
		return siteInfo != null;
	}

	private void redirectToLoginPage(HttpServletRequest request, HttpServletResponse response, RequestSiteInfo siteInfo) throws IOException {
		StringBuffer requestUri = request.getRequestURL();
		if (request.getQueryString() != null) {
			requestUri.append('?').append(request.getQueryString());
		}

		StringBuilder redirectUri = getPreloginPageUri(request, siteInfo);
		redirectUri.append("?TARGET=");
		redirectUri.append(URLEncoder.encode(requestUri.toString(), "UTF-8"));
		redirectUri.append("&").append(Constants.SMAUTHREASON).append("=0");

		response.sendRedirect(redirectUri.toString());
	}

	static StringBuilder getPreloginPageUri(HttpServletRequest request, RequestSiteInfo siteInfo) {
		StringBuilder preloginPageUrl = new StringBuilder();
		String protocol = PortalCommonDAOCacheImpl.getInstance().getSiteProtocol(siteInfo.getProtectedSiteName());
		preloginPageUrl.append(protocol).append("://");
		preloginPageUrl.append(request.getServerName());
		String port = Config.getValue("SPP.port." + protocol, null);
		if (port != null && !"80".equals(port) && !"443".equals(port)) {
			preloginPageUrl.append(':').append(port);
		}
		preloginPageUrl.append(request.getContextPath());
		if ("console".equals(siteInfo.getProtectedSiteName())) {
			preloginPageUrl.append("/site/console");
		}
		else {
			preloginPageUrl.append("/site/").append(siteInfo.getProtectedSiteName());
		}

		preloginPageUrl.append(TEMPLATE_PRELOGIN_URI);
		return preloginPageUrl;
	}

	private boolean isRequestToProtectedResource(HttpServletRequest request, RequestSiteInfo siteInfo) {
		String requestUri = request.getRequestURI();
		boolean isTemplateRequest = requestUri.indexOf("template.") > 0;

		return siteInfo != null && !siteInfo.isPublic() && !isTemplateRequest;
	}

	private boolean isUserAutenticated(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0, len = cookies.length; i < len; ++i) {
				if (Constants.SMSESSION.equals(cookies[i].getName()) && !SMSESSION_LOGGEDOFF.equals(cookies[i].getValue())) {
					return true;
				}
			}
		}
		return false;
	}

	private void logoutUser(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie(Constants.SMSESSION, SMSESSION_LOGGEDOFF);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}

	private boolean isLogoutRequest(HttpServletRequest request) {
		String logoutUri = request.getContextPath() + "/jsp/SPP/logout.jsp";
		return logoutUri.equals(request.getRequestURI());
	}

	private void setFilterApplied(HttpServletRequest request) {
		request.setAttribute(FILTER_APPLIED_REQUEST_KEY, Boolean.TRUE);
	}

	private boolean isFilterApplied(HttpServletRequest request) {
		return request.getAttribute(FILTER_APPLIED_REQUEST_KEY) != null;
	}

	private HppUserInfo getUserHppInfo(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (int i = 0, len = cookies.length; i < len; ++i) {
			if (Constants.SMSESSION.equals(cookies[i].getName())) {
				String cookieValue = cookies[i].getValue();
				if (cookieValue == null || cookieValue.equals("") ||
						cookieValue.length() < MockHppWebAgentLoginServlet.FAKE_SMSESSION_PREFIX.length() ||
						SMSESSION_LOGGEDOFF.equals(cookieValue))
				{
					return null;
				}
				return HppUserInfo.parse(cookieValue.substring(MockHppWebAgentLoginServlet.FAKE_SMSESSION_PREFIX.length()));
			}
		}
		return null;
	}

	private boolean isSiteUsingMockProfile(RequestSiteInfo siteInfo) {
		//return siteInfo != null && Config.getBooleanValue(SPPUserManager.mUseMockProfile + siteInfo.getProtectedSiteName(), false);
        return siteInfo != null && SiteManager.getInstance().getSite(siteInfo.getProtectedSiteName()).getUseMockProfile();
    }

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}


}
