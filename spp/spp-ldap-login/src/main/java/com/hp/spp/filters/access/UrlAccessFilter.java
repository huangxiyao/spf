package com.hp.spp.filters.access;

import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;

import com.hp.spp.config.Config;
import com.hp.spp.cache.Cache;
import com.hp.spp.hpp.supporttools.HPPHeaderHelper;
import com.hp.spp.profile.Constants;

public class UrlAccessFilter implements Filter {
	/**
	 * This configuration variable defined whether this filter should apply security. This variable is
	 * optional, if not specified, the fitler is active by default.
	 * This variable is OPTIONAL.
	 */
	public static final String CONFIG_KEY_ENABLED = "SPP.UrlAccessFilter.Enabled";

	/**
	 * The url to the login form. The url must start with '/' and is relative to the web application
	 * context path.
	 * This variable is OPTIONAL.
	 */
	public static final String CONFIG_KEY_LOGIN_PAGE = "SPP.UrlAccessFilter.LoginFormPage";

	/**
	 * The url to the page that is able to re-POST the data of the request that was suspended due
	 * to the security control. The url must start with '/' and is realtive to the web application
	 * context path.
	 * This variable is OPTIONAL.
	 */
	public static final String CONFIG_KEY_POST_REDIRECT_PAGE = "SPP.UrlAccessFitler.PostRedirectPage";

	public static final String ERROR_AUTH_FAILED = "AuthenticationFailed";
	public static final String ERROR_NOT_AUTHORIZED = "AuthorizationFailed";

	private static final String SAVED_REQUEST_KEY = UrlAccessFilter.class.getName() + ".SavedRequestData";
	private static final String URL_ACCESS_SECURITY_CHECK = "/url_access_security_check";
	private static final String FILTER_APPLIED = UrlAccessFilter.class.getName() + ".AlreadyApplied";

	private static final Logger mLog = Logger.getLogger(UrlAccessFilter.class);

	private String mRuleSetName;
	private UrlAccessRuleDAO mRuleDAO;
	private LdapLogin mLogin;

	public void init(FilterConfig filterConfig) throws ServletException {
		String param = filterConfig.getInitParameter("RuleSetName");
		if (param != null && !param.trim().equals("")) {
			mRuleSetName = param;
			if (mLog.isInfoEnabled()) {
				mLog.info("The following rule set will be used: " + mRuleSetName);
			}
		}
		else {
			mLog.warn("RuleSetName parameter not specified or is empty. Will used default 'portal'.");
			mRuleSetName = "portal";
		}

		setRuleDAO(new CachingUrlAccessRuleDAO(new JDBCUrlAccessRuleDAO(), Cache.getInstance()));
		setLdapLogin(new LdapLogin());
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (Config.getBooleanValue(CONFIG_KEY_ENABLED, true) &&
				servletRequest.getAttribute(FILTER_APPLIED) == null)
		{
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;

			boolean continueWithFilterChain;
			try {
				continueWithFilterChain = executeWorkflow(request, response);
			}
			catch (NamingException e) {
				// ServletException, being JDK 1.4 class doesn't wrap exceptions properly - need to do it manually
				ServletException se = new ServletException("Error authenticated user", e);
				se.initCause(e);
				throw se;
			}
			if (continueWithFilterChain) {
				try {
					servletRequest.setAttribute(FILTER_APPLIED, Boolean.TRUE);
					filterChain.doFilter(servletRequest, servletResponse);
				}
				finally {
					servletRequest.removeAttribute(FILTER_APPLIED);
				}
			}
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	protected boolean executeWorkflow(HttpServletRequest request, HttpServletResponse response) throws NamingException, IOException, ServletException {
		boolean continueWithFilterChain = false;

		if (isLoginRequest(request)) {
			boolean loginSuccessful = doLogin(request);
			if (loginSuccessful) {
				HttpSession session = request.getSession();
				RequestData data = findSavedRequest(session);
				if (data == null) {
					showDefaultPage(request, response);
				}
				else {
					removeSavedRequest(session);
					boolean accessAllowed = true;
					Iterator matchingRules = findMatchingRules(data.getRequestUri());
					while (accessAllowed && matchingRules.hasNext()) {
						accessAllowed = ((UrlAccessRule) matchingRules.next()).isAccessAllowed(session);
					}
					if (accessAllowed) {
						performSavedRequest(request, response, data);
					}
					else {
						showAccessDenied(request, response);
					}
				}
			}
			else {
				showLoginForm(request, response, ERROR_AUTH_FAILED);
			}
		}
		else {
			Iterator matchingRules = findMatchingRules(request.getRequestURI());
			if (!matchingRules.hasNext()) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("No rules found matching URI: " + request.getRequestURI());
				}
				continueWithFilterChain = true;
			}
			else {
				HttpSession session = request.getSession();
				boolean foundDenied = false;
				while (!foundDenied && matchingRules.hasNext()) {
					UrlAccessRule rule = (UrlAccessRule) matchingRules.next();
					if (!rule.isAccessAllowed(session)) {
						foundDenied = true;
						if (rule.isLoginRequired()) {
							saveRequest(session, request);
							showLoginForm(request, response, null);
						}
						else {
							showAccessDenied(request, response);
						}
					}
				}
				continueWithFilterChain = !foundDenied;
			}
		}

		return continueWithFilterChain;
	}


	protected void setRuleDAO(UrlAccessRuleDAO dao) {
		mRuleDAO = dao;
	}

	protected void setLdapLogin(LdapLogin ldapLogin) {
		mLogin = ldapLogin;
	}

	protected void showDefaultPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// let's try to redirect user to the root of the webapp - this is a hack but I don't know
		// where to go.
		response.sendRedirect(request.getContextPath() + "/");
	}

	protected void showAccessDenied(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		showLoginForm(request, response, ERROR_NOT_AUTHORIZED);
	}

	protected RequestData findSavedRequest(HttpSession session) {
		return (RequestData) session.getAttribute(SAVED_REQUEST_KEY);
	}

	protected RequestData saveRequest(HttpSession session, HttpServletRequest request) {
		RequestData data = new RequestData(request);
		session.setAttribute(SAVED_REQUEST_KEY, data);
		return data;
	}

	protected void performSavedRequest(HttpServletRequest request, HttpServletResponse response, RequestData data) throws IOException, ServletException {
		if (data.isGet()) {
			response.sendRedirect(data.getRequestUrl());
		}
		else {
			request.setAttribute("PostRedirectUrl", data.getRequestUrl());
			request.setAttribute("PostRedirectParams", data.getParameterMap());
			String postRedirectPage = Config.getValue(CONFIG_KEY_POST_REDIRECT_PAGE, "/jsp/SPP/hp_login_post_redirect.jsp");
			request.getRequestDispatcher(postRedirectPage).forward(request, response);
		}
	}

	protected void removeSavedRequest(HttpSession session) {
		session.removeAttribute(SAVED_REQUEST_KEY);
	}

	protected boolean doLogin(HttpServletRequest request) throws NamingException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if (mLog.isDebugEnabled()) {
			mLog.debug("Logging in user: " + email);
		}
		boolean result = mLogin.login(email, password);
		if (result) {
			request.getSession().setAttribute(Constants.HP_LDAP_AUTH_EMAIL, email);
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("Login of user '" + email + "': " + (result ? "successful" : "failed"));
		}
		return result;
	}

	protected boolean isLoginRequest(HttpServletRequest request) {
		return request.getRequestURI().indexOf(URL_ACCESS_SECURITY_CHECK) >= 0;
	}

	protected void showLoginForm(HttpServletRequest request, HttpServletResponse response, String errorCode) throws IOException, ServletException {
		String loginFormPage = Config.getValue(CONFIG_KEY_LOGIN_PAGE, "/jsp/SPP/hp_login.jsp");
		StringBuffer sb =
				new StringBuffer("https://").
				append(request.getServerName());
		int port = getHttpsPort(request)/*Config.getIntValue("SPP.port.https", 443)*/;
		if (port != 443) {
			sb.append(':').append(port);
		}
		sb.append(request.getContextPath());
		sb.append(loginFormPage);
		if (errorCode != null) {
			if (sb.indexOf("?") != -1) {
				sb.append('&');
			}
			else {
				sb.append('?');
			}
			sb.append("errorCode=").append(errorCode);
		}
		response.sendRedirect(sb.toString());
	}

	private int getHttpsPort(HttpServletRequest request) {
		int port = -1;

		if (isWebServerRequest(request)) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Web Server request: looking for HTTPS port numer in the config variable: SPP.port.https");
			}
			port = Config.getIntValue("SPP.port.https", -1);
		}
		else {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Direct Application Server request: looking for HTTPS port number in the config variables: xxxManagedServer.port.https");
			}
			// first check a managed server-specific port
			// This setting would be useful, if we wanted to stack several servers on the same machine,
			// e.g. 2 Vignette servers
			port = Config.getIntValue(System.getProperty("weblogic.Name") + ".ManagedServer.port.https", -1);
			if (port == -1) {
				// if not defined, check cluster-wide port; note that querying "ManagedServer.port.https" still
				// allows you define WSRP and VIgnette-specific variables, i.e. "wsrp.ManagedServer.port.https" and
				// "portal.ManagedServer.port.https"
				port = Config.getIntValue("ManagedServer.port.https", -1);
			}
		}

		if (port == -1) {
			// if no port defined, let's use the default https port
			if (mLog.isDebugEnabled()) {
				mLog.debug("No HTTPS port defined; using default");
			}
			port = 443;
		}
		return port;
	}

	private boolean isWebServerRequest(HttpServletRequest request) {
		return HPPHeaderHelper.getSMUser(request) != null;
	}


	protected Iterator findMatchingRules(String requestUri) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("Finding rule matching request URI: " + requestUri);
		}
		List rules = mRuleDAO.findUrlAccessRules(mRuleSetName);
		if (rules == null || rules.isEmpty()) {
			return Collections.EMPTY_LIST.iterator();
		}
		List result = new ArrayList();
		for (Iterator it = rules.iterator(); it.hasNext();) {
			UrlAccessRule rule = (UrlAccessRule) it.next();
			if (rule.matches(requestUri)) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("Found matching rule for '" + requestUri + "': " + rule);
				}
				result.add(rule);
			}
		}
		return result.iterator();
	}

	public void destroy() {
	}

}
