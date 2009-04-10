/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.portal.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.ac.portal.filter.AdmissionControlFilterConfig;
import com.hp.it.spf.xa.ac.HealthcheckStatus;
import com.hp.it.spf.xa.ac.ClosedStatus;

/**
 * The J2EE servlet filter for performing admission control on incoming client
 * requests.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class AdmissionControlFilter implements Filter {

	// ///////////////////////////////////////////////////////////
	// PRIVATE ATTRIBUTES
	// ///////////////////////////////////////////////////////////

	private static String HTTP_HOST_HEADER = "Host";

	private static String HTTP_USER_AGENT_HEADER = "User-Agent";

	private static String HEALTHCHECK_USER_AGENT_PREFIX = "healthchecker";

	private static int ADMITTED_CLIENT_QUERY_TIMEOUT = 60; // seconds

	private static String PRIV_CLIENT_QUERY_ON = "on";

	private static String PRIV_CLIENT_QUERY_OFF = "off";

	private static String PRIV_CLIENT_QUERY_YES = "yes";

	private static String PRIV_CLIENT_QUERY_NO = "no";

	private static Log LOG = LogFactory.getLog(AdmissionControlFilter.class);

	private ServletContext context;

	private AdmissionControlFilterConfig config;

	private String pendingAdmittedClientQuery;

	// ///////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// ///////////////////////////////////////////////////////////

	/**
	 * Finalizes the filter (at present, no action is taken).
	 */
	public void destroy() {

		// empty method

	} // end method destroy

	/**
	 * Executes the filter for a particular client request.
	 */
	public void doFilter(ServletRequest srequest, ServletResponse sresponse,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request;
		HttpServletResponse response;
		String poolHostname;
		String thisSiteHostname;
		boolean staffUser;
		boolean closed;
		boolean closing;
		boolean admitted;
		boolean usedPoolHostname;
		boolean usedThisSiteHostname;
		boolean usedOtherSiteHostname;
		this.pendingAdmittedClientQuery = null;

		LOG.debug("-- Enter AdmissionControlFilter.doFilter");

		// Preliminaries: Sanity-check the arguments - make sure they're not
		// null and correctly typed. If not, chain and return.
		if (chain == null)
			return;
		if ((srequest == null) || (sresponse == null)) {
			LOG.debug("-- Exit 1 AdmissionControlFilter.doFilter");
			chain.doFilter(srequest, sresponse);
			return;
		}
		if (!(srequest instanceof HttpServletRequest)
				|| !(sresponse instanceof HttpServletResponse)) {
			LOG.debug("-- Exit 2 AdmissionControlFilter.doFilter");
			chain.doFilter(srequest, sresponse);
			return;
		}
		request = (HttpServletRequest) srequest;
		response = (HttpServletResponse) sresponse;

		// Begin main case: First discover which pool this request maps into,
		// and what hostname the request used, if any. If none, chain and
		// return. Because admission control filter needs a hostname from the
		// client, and a pool into which it maps, to do its work.
		String hostname = this.getHostnameUsedByClient(request);
		String poolID = this.getPoolUsedByClient(request);
		LOG.debug("--- hostname used by client: [" + hostname + "]");
		LOG.debug("--- pool used by client: [" + poolID + "]");
		if ((hostname == null) || (poolID == null)) {
			LOG.debug("-- Exit 3 AdmissionControlFilter.doFilter");
			chain.doFilter(request, response);
			return;
		}

		// Next collect some facts about current state which will be needed in
		// subsequent steps. If no pool or site hostname for this site are
		// defined, chain and return (because these are needed by admission
		// control filter to do its job).
		poolHostname = this.getPoolHostname(poolID);
		thisSiteHostname = this.getThisSiteHostname(poolID);
		LOG.debug("--- pool hostname: [" + poolHostname + "]");
		LOG.debug("--- this site hostname: [" + thisSiteHostname + "]");
		if ((poolHostname == null) || (thisSiteHostname == null)) {
			LOG.debug("-- Exit 4 AdmissionControlFilter.doFilter");
			chain.doFilter(request, response);
			return;
		}
		closed = this.siteIsClosed(poolID);
		closing = this.siteIsClosing(poolID);
		staffUser = this.clientIsStaffUser(request, poolID);
		admitted = this.clientPreviouslyAdmitted(request, poolID,
				thisSiteHostname);
		usedPoolHostname = this.clientUsedPoolHostname(request, poolID);
		usedThisSiteHostname = this.clientUsedThisSiteHostname(request, poolID);
		usedOtherSiteHostname = this.clientUsedOtherSiteHostname(request,
				poolID);

		LOG.debug("--- closed: [" + closed + "]");
		LOG.debug("--- closing: [" + closing + "]");
		LOG.debug("--- staff user: [" + staffUser + "]");
		LOG.debug("--- previously admitted: [" + admitted + "]");
		LOG.debug("--- used pool hostname: [" + usedPoolHostname + "]");
		LOG
				.debug("--- used this site hostname: [" + usedThisSiteHostname
						+ "]");
		LOG.debug("--- used other site hostname: [" + usedOtherSiteHostname
				+ "]");

		// Ready to begin admission control.
		// Perform site closure control:
		if (!staffUser && (closed || (closing && !admitted))) {
			if (poolHostname != null) {
				LOG.debug("-- Exit 5 AdmissionControlFilter.doFilter");
				LOG
						.info("- AdmissionControlFilter.doFilter: outcome: [redirected]; reason: [site closed or closing]");
				this.redirect(request, response, poolHostname, poolID,
						thisSiteHostname);
				return;
			}
		}

		// Perform load redistribution ("unsticky site") control:
		if (!staffUser && !admitted && !usedPoolHostname
				&& !usedOtherSiteHostname) {
			if (poolHostname != null) {
				LOG.debug("-- Exit 6 AdmissionControlFilter.doFilter");
				LOG
						.info("- AdmissionControlFilter.doFilter: outcome: [redirected]; reason: [load redistribution]");
				this.redirect(request, response, poolHostname, poolID,
						thisSiteHostname);
				return;
			}
		}

		// Perform site affinity ("sticky site") control:
		if (usedPoolHostname || usedOtherSiteHostname
				|| (!staffUser && !usedThisSiteHostname)) {
			if (thisSiteHostname != null) {
				// Admit client, so load redistribution is bypassed next time -
				// otherwise endless redirection loop between sticky and
				// unsticky settings
				this.admitClient(request, poolID, thisSiteHostname);
				LOG.debug("-- Exit 7 AdmissionControlFilter.doFilter");
				LOG
						.info("- AdmissionControlFilter.doFilter: outcome: [redirected]; reason: [site affinity]");
				this.redirect(request, response, thisSiteHostname, poolID,
						thisSiteHostname);
				return;
			}
		}

		// If we made it this far, the request is admitted - pass it along up
		// the filter chain. Also set the site cookie (if any) and closing time
		// (if any).

		LOG.debug("-- Exit 8 AdmissionControlFilter.doFilter");
		LOG.info("- AdmissionControlFilter.doFilter: outcome: [admitted]");
		this.setSiteCookie(response, poolID);
		this.setClosingTime(request);
		this.admitClient(request, poolID, thisSiteHostname);
		chain.doFilter(request, response);

	} // end method doFilter

	/**
	 * Initializes the filter based on the given filter configuration.
	 */
	public void init(FilterConfig fconfig) throws ServletException {

		LOG.debug("-- Enter AdmissionControlFilter.init");

		// Save the ServletContext for later - it will be needed in the doFilter
		// method.

		this.context = fconfig.getServletContext();

		// Load the AdmissionControlFilterConfig for later - it will be needed
		// in the doFilter method.

		this.config = new AdmissionControlFilterConfig(fconfig);

		LOG.debug("-- Exit AdmissionControlFilter.init");

	} // end method init

	// ///////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//
	// Note: all private methods assume their given arguments are non-null, and
	// assume their string arguments have been normalized (trimmed and
	// lowercased).
	// ///////////////////////////////////////////////////////////

	private void redirect(HttpServletRequest request,
			HttpServletResponse response, String newHostname, String poolID,
			String thisSiteHostname) {

		String scheme, port, context, path, info, query, url;
		String name, value, flag, targetKey;
		String[] values;
		Enumeration params;
		boolean already = false;
		boolean copy;
		int i, p;

		// Make a URL equivalent to the original URL used by the browser for
		// this request - the only difference being insertion of the new
		// hostname.

		// First get the current scheme: http or https
		scheme = request.getScheme();
		if (scheme == null)
			scheme = "";

		// Next get the current port (and normalize it).
		p = this.getPortUsedByClient(request);
		if ((scheme.equalsIgnoreCase("http") && (p == 80))
				|| (scheme.equalsIgnoreCase("https") && (p == 443)))
			p = 0;
		if (p == 0)
			port = "";
		else
			port = ":" + p;

		// Next get the root path.
		context = request.getContextPath();
		if (context == null)
			context = "";

		// Next get the path.
		path = request.getServletPath();
		if (path == null)
			path = "";

		// Next get an additional path info.
		info = request.getPathInfo();
		if (info == null)
			info = "";

		// Lastly get the query string. In the case of a GET, this basically
		// just builds a duplicate query string. In the case of a POST, any
		// POST'ed request parameters are copied into the query string too
		// (since browsers always handle redirects with GET, such parameters
		// need to go into the query string in order for the target page to have
		// a chance of working). Don't forget to add the filter's own
		// query-string element, the admission-control flag, if any - unless
		// one was already present.
		//
		// Note: this also switches the hostname in any SSO target URL. First,
		// look for an SSO target query value - ie a target URL from an SSO
		// engine. If one is found, replace the hostname in that URL with this
		// one.
		query = "";
		flag = (String) this.config.getAdmittedClientQueryKey();
		targetKey = (String) this.config.getSsoTargetQueryKey();
		params = request.getParameterNames();
		if (params != null)
			while (params.hasMoreElements()) {
				name = (String) params.nextElement();
				values = request.getParameterValues(name);
				if (values != null)
					for (i = 0; i < values.length; i++) {
						value = values[i];
						copy = true;
						// Validate the admission control flag, if any.
						if (name.equals(flag)) {
							if (this.checkAdmittedClientQuery(value, poolID,
									thisSiteHostname))
								already = true;
							else
								copy = false;
						}
						// Adjust the SSO target URL hostname, if any, so that
						// when the SSO engine eventually redirects to that URL,
						// it redirects to the same hostname and port as this
						// redirect will go to.
						else if (name.equals(targetKey)) {
							value = this.replaceHostAndPort(value, newHostname
									+ port);
						}
						// Copy over the query param (except for invalid
						// admission control flag), adjusted as needed.
						if (copy)
							query = this.append(query, name, value);
					}
			}
		if ((this.pendingAdmittedClientQuery != null) && !already)
			query = this.append(query, flag, this.pendingAdmittedClientQuery);
		if (!"".equals(query))
			query = "?" + query;

		// Now put the redirect URL together.
		url = scheme + "://" + newHostname + port + context + path + info
				+ query;

		// Now set the HTTP 302 redirect URL into the response.
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			// should never happen
		} catch (IllegalStateException e) {
			// should never happen
		}

	} // end method redirect

	private void setSiteCookie(HttpServletResponse response, String poolID) {

		String name, value, domain, path;
		Cookie cookie;

		// An undefined site cookie name for this pool means no site cookie to
		// set.
		name = this.config.getSiteCookieName(poolID);
		if (name != null) {
			domain = this.config.getSiteCookieDomain(poolID);
			path = this.config.getSiteCookiePath(poolID);

			// An undefined site cookie value for this pool means to use this
			// site's primary hostname by default.
			value = this.config.getSiteCookieValue(poolID);
			if (value == null)
				value = this.getThisSiteHostname(poolID);

			cookie = new Cookie(name, value);
			if (domain != null)
				cookie.setDomain(domain);
			if (path != null)
				cookie.setPath(path);
			cookie.setMaxAge(-1); // Set as a session-only cookie

			response.addCookie(cookie);
		}

	} // end method setSiteCookie

	private String getPoolUsedByClient(HttpServletRequest request) {

		String poolID = null;
		String hostname = this.getHostnameUsedByClient(request);

		// First lookup in config the pool ID for the hostname used by the
		// client.
		if (hostname != null)
			poolID = this.config.getPool(hostname);

		// If that didn't get a pool ID, fallback to the default one.
		if (poolID == null)
			poolID = this.config.getDefaultPool();

		// Returns null if no mapped or default pool ID.
		return poolID;

	} // end method getPoolUsedByClient

	private String getHostAndPortUsedByClient(HttpServletRequest request) {

		int i;
		String targetKey = this.config.getSsoTargetQueryKey();
		String targetValue = null;
		String targetRemainder = null;
		String hostAndPort = null;

		if (request != null) {
			// First look for the info in the SSO target query parameter,
			// if any. Ie, if there is an SSO target query parameter, then the
			// host/port in its URL is taken as the info used by the end-user,
			// in preference over the hostname used in the current URL per-se.
			// This is because the host/port in the SSO target reflects the
			// one requested by the user; the one in the current URL is just
			// the one automatically invoked by the SSO engine, and it may not
			// be the same. We want the one used by the user.
			if (targetKey != null) {
				targetValue = request.getParameter(targetKey);
				if (targetValue != null) {
					i = targetValue.indexOf("://");
					if ((i >= 0) && (i < targetValue.length() - 3)) {
						targetRemainder = targetValue.substring(i + 3);
						i = targetRemainder.indexOf("/");
						if (i >= 0)
							hostAndPort = targetRemainder.substring(0, i);
						else
							hostAndPort = targetRemainder;
					}
				}
			}
			// Otherwise, the hostname used by the client is just in the HTTP
			// Host header.
			if (hostAndPort == null)
				hostAndPort = request.getHeader(HTTP_HOST_HEADER);
		}

		// Returns null if no target query parameter or Host header present.
		return hostAndPort;

	} // end method getHostAndPortUsedByClient

	private String getHostnameUsedByClient(HttpServletRequest request) {

		String hostAndPort = this.getHostAndPortUsedByClient(request);
		String hostname = "";
		int i;

		// The HTTP Host header / SSO target query parameter may contain a
		// port number - if so, ignore it.
		if (hostAndPort != null) {
			if ((i = hostAndPort.indexOf(":")) != -1)
				hostname = hostAndPort.substring(0, i);
			else
				hostname = hostAndPort;
		}

		// Normalize the hostname with config by trimming it and forcing it to
		// lowercase. Return null if no hostname in HTTP Host header or SSO
		// target query parameter.
		hostname = hostname.trim().toLowerCase();
		if ("".equals(hostname))
			hostname = null;
		return hostname;

	} // end method getHostnameUsedByClient

	private int getPortUsedByClient(HttpServletRequest request) {

		String hostAndPort = this.getHostAndPortUsedByClient(request);
		String port = "";
		int i, p = 0;

		// The HTTP host header / SSO target query parameter will also contain
		// a hostname - ignore it. If the port number is blank, that's OK.
		if (hostAndPort != null) {
			if (((i = hostAndPort.indexOf(":")) != -1)
					&& (i < hostAndPort.length() - 1))
				port = hostAndPort.substring(i + 1);
		}

		// Normalize the port number (should be numeric so lowercasing should
		// not be needed, but do it anyway).
		port = port.trim().toLowerCase();
		if ("".equals(port))
			port = null;

		// Convert the port number to int, validate it, and return it.
		if (port != null) {
			try {
				p = new Integer(port).intValue();
			} catch (NumberFormatException e) {
				p = 0;
			}
			if (p <= 0)
				p = 0;
		}
		return p;

	} // end method getPortUsedByClient

	private boolean siteIsClosed(String poolID) {

		boolean closed = false;
		Date when = this.getCloseDate();

		// If no close date, that means we are not closed - return false.
		// Otherwise compare the time of closure, adjusted outward by the sum of
		// the two grace periods, with the current time. If the adjusted time of
		// closure is already past, return true, else false. (Why both grace
		// periods? Because first the closing grace must expire, before the
		// closed grace applies.)
		if (when != null) {
			Integer closedGrace = (Integer) this.config.getClosedGrace(poolID);
			Integer closingGrace = (Integer) this.config
					.getClosingGrace(poolID);
			if ((closedGrace != null) && (closingGrace != null)) {
				int millis = (closedGrace.intValue() * 1000)
						+ (closingGrace.intValue() * 1000);
				Date then = new Date(when.getTime() + (long) millis);
				Date now = new Date();
				if (now.after(then))
					closed = true;
			}
		}
		return closed;

	} // end method isClosed

	private boolean siteIsClosing(String poolID) {

		boolean closing = false;
		Date when = this.getCloseDate();

		// If no close date, that means we are not closed - return false.
		// Otherwise compare the time of closure, adjusted outward by the sum of
		// the closing grace period, with the current time. If the adjusted time
		// of closure is already past, return true, else false.
		if (when != null) {
			Integer closingGrace = (Integer) this.config
					.getClosingGrace(poolID);
			if (closingGrace != null) {
				int millis = closingGrace.intValue() * 1000;
				Date then = new Date(when.getTime() + (long) millis);
				Date now = new Date();
				if (now.after(then))
					closing = true;
			}
		}
		return closing;

	} // end method isClosing

	private void setClosingTime(HttpServletRequest request) {

		Date when = this.getCloseDate();
		String closingTimeKey = this.config.getClosingTimeRequestKey();
		if (when != null)
			request.setAttribute(closingTimeKey, when);

	} // end method setClosingTime

	private Date getCloseDate() {

		Date when = null;
		String healthcheckRoot = this.config.getHealthcheckRoot();

		// Get the healthcheck Web app context from the current saved
		// context. Null if can't find it.
		ServletContext healthcheckContext = this.context
				.getContext(healthcheckRoot);
		if (healthcheckContext != null) {

			// Get the current healthcheck status from the application scope of
			// the healthcheck Web app context. Null if can't get it.
			HealthcheckStatus status = HealthcheckStatus
					.retrieve(healthcheckContext);
			if (status != null) {

				// Check for the status type that means "closed". Not closed if
				// not that status type - return null. Otherwise return the
				// date.
				if (status instanceof ClosedStatus)
					when = status.getDate();
			}
		}
		return when;

	} // end method getCloseDate

	private void admitClient(HttpServletRequest request, String poolID,
			String thisSiteHostname) {

		// First check session state for boolean flag, and set it if not
		// present.
		HttpSession session = request.getSession();
		String admittedSessionFlag = (String) this.config
				.getAdmittedClientSessionKey();
		// Limit applicability of this flag to this pool and site alone.
		admittedSessionFlag += "." + poolID + "." + thisSiteHostname;
		if (session != null) {
			if ((session.getAttribute(admittedSessionFlag)) == null)
				session.setAttribute(admittedSessionFlag, new Boolean(true));
		}

		// Second make the query flag, and buffer it where redirect() can find
		// it.
		this.pendingAdmittedClientQuery = this.makeAdmittedClientQuery(poolID,
				thisSiteHostname);

	} // end method admitClient

	private boolean clientPreviouslyAdmitted(HttpServletRequest request,
			String poolID, String thisSiteHostname) {

		// First check session state for a boolean flag there indicated the
		// client was previously admitted.
		HttpSession session = request.getSession();
		String admittedSessionFlag = (String) this.config
				.getAdmittedClientSessionKey();
		// Limit applicability of this flag to this pool and site alone.
		admittedSessionFlag += "." + poolID + "." + thisSiteHostname;
		if (session != null) {
			if ((session.getAttribute(admittedSessionFlag)) != null)
				return true;
		}

		// If the flag in session state was not found, check the query string.
		String admittedQueryFlag = (String) this.config
				.getAdmittedClientQueryKey();
		String queryValue = this.getQueryValue(request, admittedQueryFlag);
		if (queryValue != null) {
			if (this.checkAdmittedClientQuery(queryValue, poolID,
					thisSiteHostname))
				return true;
		}

		// Otherwise return false.
		return false;

	} // end method clientPreviouslyAdmitted

	private boolean clientIsStaffUser(HttpServletRequest request, String poolID) {

		HttpSession session = request.getSession();
		String ip = request.getRemoteAddr();
		String privClientQuery = this.config
				.getPrivilegedClientQueryKey(poolID);
		String privAttrName = this.config.getPrivilegedClientSessionKey();
		// Limit applicability of this flag to this pool alone.
		privAttrName += "." + poolID;

		// First check the privileged client query parameter from the config -
		// if it is found in the request parameters, with value "yes" or "on"
		// (case-insensitive), then consider the user to be a staff user. If it
		// is found in the request parameter, with value "no" or "off"
		// (case-insensitive), then consider the user to be an end user. Either
		// way, set the result into session for next time, in case the query
		// flag is missing then. Session should never be null.
		LOG.debug("--- isStaffUser 1");
		if (privClientQuery != null) {
			String value = this.getQueryValue(request, privClientQuery);
			if (value != null) {
				if (value.equalsIgnoreCase(PRIV_CLIENT_QUERY_ON)
						|| value.equalsIgnoreCase(PRIV_CLIENT_QUERY_YES)) {
					if (session != null)
						session.setAttribute(privAttrName, new Boolean(true));
					return true;
				} else if (value.equalsIgnoreCase(PRIV_CLIENT_QUERY_OFF)
						|| value.equalsIgnoreCase(PRIV_CLIENT_QUERY_NO)) {
					if (session != null)
						session.setAttribute(privAttrName, new Boolean(false));
					return false;
				}
			}
		}

		// If we made it to here, we did not have a query string to obey.
		// So now check the session. If we have previously recorded in the
		// session a determination for the user, then use that now. HttpSession
		// should never be null.
		LOG.debug("--- isStaffUser 2");
		if (session != null) {
			Boolean value = (Boolean) session.getAttribute(privAttrName);
			if (value != null)
				return value.booleanValue();
		}

		// If we made it to here, we did not have a valid-value privileged
		// client query parameter in the current or previous request. Check the
		// IP in the request against the privileged client IP addresses from the
		// config - if any match (leading substring), then consider the user to
		// be a staff user. IP from the request should never be null.
		LOG.debug("--- isStaffUser 3");
		if (ip != null) {
			ip = ip.trim().toLowerCase();
			List addresses = this.config.getPrivilegedClientAddresses(poolID);
			if (addresses != null) {
				for (int i = 0; i < addresses.size(); i++) {
					if (ip.startsWith((String) addresses.get(i)))
						return true;
				}
			}
		}

		// Note that the healthcheck application's healthcheckers are
		// always treated as staff users. They are identified by "healthchecker"
		// prefix in the User-Agent.
		LOG.debug("--- isStaffUser 4");
		String userAgent = request.getHeader(HTTP_USER_AGENT_HEADER);
		if (userAgent != null) {
			if (userAgent.startsWith(HEALTHCHECK_USER_AGENT_PREFIX))
				return true;
		}

		// If we made it this far, then there are no specific indications one
		// way or another whether this request is for a staff user. Default to
		// end user.
		LOG.debug("--- isStaffUser 5");
		return false;

	} // end method isStaffUser

	private boolean clientUsedPoolHostname(HttpServletRequest request,
			String poolID) {

		String hostname = this.getHostnameUsedByClient(request);
		HashSet poolHostnames = this.config.getPoolHostnames(poolID);
		if ((hostname != null) && (poolHostnames != null)
				&& poolHostnames.contains(hostname))
			return true && !clientUsedThisSiteHostname(request, poolID);
		else
			return false;

	} // end method clientUsedPoolHostname

	private boolean clientUsedThisSiteHostname(HttpServletRequest request,
			String poolID) {

		String hostname = this.getHostnameUsedByClient(request);
		HashSet siteHostnames = this.config.getThisSiteHostnames(poolID);
		if ((hostname != null) && (siteHostnames != null)
				&& siteHostnames.contains(hostname))
			return true;
		else
			return false;

	} // end method clientUsedThisSiteHostname

	private boolean clientUsedOtherSiteHostname(HttpServletRequest request,
			String poolID) {

		String hostname = this.getHostnameUsedByClient(request);
		HashSet siteHostnames = this.config.getOtherSiteHostnames(poolID);
		if ((hostname != null) && (siteHostnames != null)
				&& siteHostnames.contains(hostname))
			return true && !clientUsedPoolHostname(request, poolID)
					&& !clientUsedThisSiteHostname(request, poolID);
		else
			return false;

	} // end method cliendUsedOtherSiteHostname

	private String getPoolHostname(String poolID) {

		// Lookup the primary pool hostname for this pool ID.
		String poolHostname = (this.config.getPoolHostname(poolID));
		if (poolHostname != null) {

			// When the primary pool hostname is defined for this pool ID, apply
			// the following sanity check: make sure it is unique (ie not also
			// defined as a site hostname).
			HashSet thisSiteHostnames = this.config
					.getThisSiteHostnames(poolID);
			HashSet otherSiteHostnames = this.config
					.getOtherSiteHostnames(poolID);
			if (((thisSiteHostnames != null) && thisSiteHostnames
					.contains(poolHostname))
					|| ((otherSiteHostnames != null) && otherSiteHostnames
							.contains(poolHostname)))
				poolHostname = null;
		}

		// Hence we return the primary pool hostname, or null if not uniquely
		// defined.
		return poolHostname;

	} // end method getPoolHostname

	private String getThisSiteHostname(String poolID) {

		// Lookup the primary site hostname for this site for this pool ID.
		String siteHostname = this.config.getThisSiteHostname(poolID);
		if (siteHostname != null) {

			// When this site's primary site hostname is defined for this pool
			// ID, apply the following sanity check: make sure it is unique (ie
			// not also defined as a pool hostname or another site hostname).
			HashSet poolHostnames = this.config.getPoolHostnames(poolID);
			HashSet otherSiteHostnames = this.config
					.getOtherSiteHostnames(poolID);
			if (((poolHostnames != null) && poolHostnames
					.contains(siteHostname))
					|| ((otherSiteHostnames != null) && otherSiteHostnames
							.contains(siteHostname)))
				siteHostname = null;
		}

		// Hence we return the primary site hostname for this site, or null if
		// not uniquely defined.
		return siteHostname;

	} // end method getThisSiteHostname

	private String makeAdmittedClientQuery(String poolID,
			String thisSiteHostname) {

		String timestamp, poolIdHash, thisSiteHostnameHash, value;

		if ((poolID == null) || (thisSiteHostname == null))
			return null;

		// Generated format needs to be:
		// <timestamp>.<poolIDhash>.<thisSiteHostnameHash>
		timestamp = "" + new Date().getTime();
		poolIdHash = "" + Math.abs(poolID.hashCode());
		thisSiteHostnameHash = "" + Math.abs(thisSiteHostname.hashCode());
		value = timestamp + "." + poolIdHash + "." + thisSiteHostnameHash;
		return value;

	} // end method makeAdmittedClientQuery

	private boolean checkAdmittedClientQuery(String value, String poolID,
			String thisSiteHostname) {

		StringTokenizer fields;
		int poolIdHashcodeWas, hostnameHashcodeWas;
		long timeWas = 0, now;
		if (value == null)
			return false;

		// Expected format is: <timestamp>.<poolIDhash>.<thisSiteHostnameHash>
		fields = new StringTokenizer(value, ".");

		// First confirm timestamp. Allow it to be up to 1 minute old.
		if (!fields.hasMoreTokens())
			return false;
		try {
			timeWas = new Long(fields.nextToken()).longValue();
		} catch (NumberFormatException e) {
			return false;
		}
		now = new Date().getTime();
		if ((now > timeWas + (ADMITTED_CLIENT_QUERY_TIMEOUT * 1000))
				|| (now < timeWas - (ADMITTED_CLIENT_QUERY_TIMEOUT * 1000)))
			return false;

		// Second confirm pool ID. Must be the same as this site's pool ID.
		// Confirmed by using hashcodes instead of actual string values.
		if (!fields.hasMoreTokens())
			return false;
		try {
			poolIdHashcodeWas = new Integer(fields.nextToken()).intValue();
		} catch (NumberFormatException e) {
			return false;
		}
		if (Math.abs(poolIdHashcodeWas) != Math.abs(poolID.hashCode()))
			return false;

		// Lastly confirm site hostname. Must be the same as this site's
		// hostname. Confirmed by using hashcodes instead of actual string
		// values.
		if (!fields.hasMoreTokens())
			return false;
		try {
			hostnameHashcodeWas = new Integer(fields.nextToken()).intValue();
		} catch (NumberFormatException e) {
			return false;
		}
		if (Math.abs(hostnameHashcodeWas) != Math.abs(thisSiteHostname
				.hashCode()))
			return false;

		// If we made it this far, the query value is valid.
		return true;

	} // end method checkAdmittedClientQuery

	private String replaceHostAndPort(String targetValue, String hostAndPort) {

		int i, j;
		String newValue = targetValue;
		String targetRemainder;
		String before = "";
		String after = "";
		if (targetValue != null) {
			i = targetValue.indexOf("://");
			if ((i >= 0) && (i < targetValue.length() - 3)) {
				before = targetValue.substring(0, i + 3);
				targetRemainder = targetValue.substring(i + 3);
				j = targetRemainder.indexOf("/");
				if (j >= 0)
					after = targetRemainder.substring(j);
				newValue = before + hostAndPort + after;
			}
		}
		return newValue;

	} // end method replaceHostAndPort

	private String append(String query, String name, String value) {

		if (!"".equals(query))
			query += "&";
		try {
			query += URLEncoder.encode(name, "UTF-8");
			query += "=";
			query += URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// should never happen
		}
		return query;

	} // end method append

	private String getQueryValue(HttpServletRequest request, String key) {

		String value = null;
		String targetKey = this.config.getSsoTargetQueryKey();
		String targetValue, targetQuery, targetQueryPair;
		String targetQueryKey = null;
		String targetQueryValue = null;
		String[] targetQueryPairs;
		int i;

		if ((request != null) && (key != null)) {
			// First look for the given query key in the SSO target query
			// value of this request, and return its value if found.
			if (targetKey != null) {
				targetValue = request.getParameter(targetKey);
				if (targetValue != null) {
					i = targetValue.indexOf("?");
					if ((i >= 0) && (i < targetValue.length() - 1)) {
						targetQuery = targetValue.substring(i + 1);
						targetQueryPairs = targetQuery.split("&");
						for (i = 0; i < targetQueryPairs.length; i++) {
							targetQueryPair = targetQueryPairs[i];
							try {
								i = targetQueryPair.indexOf("=");
								if (i >= 1)
									targetQueryKey = URLDecoder.decode(
											targetQueryPair.substring(0, i),
											"UTF-8");
								if (i < targetQueryPair.length() - 1)
									targetQueryValue = URLDecoder.decode(
											targetQueryPair.substring(i + 1),
											"UTF-8");
							} catch (UnsupportedEncodingException e) {
								// should never happen
							}
							if (key.equals(targetQueryKey)) {
								value = targetQueryValue;
								break;
							}
						}
					}
				}
			}
			// Otherwise, if not found, look for the given query key in the
			// query string of this request, and return its value if found.
			if (value == null) {
				value = request.getParameter(key);
			}
		}
		return value;

	} // end method getQueryValue

}
