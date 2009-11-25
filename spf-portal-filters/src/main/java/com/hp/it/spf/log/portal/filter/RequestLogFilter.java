package com.hp.it.spf.log.portal.filter;

import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.hp.it.spf.xa.misc.portal.RequestContext;
import com.hp.it.spf.xa.dc.portal.DiagnosticContext;
import com.hp.it.spf.xa.dc.portal.DiagnosticContextResponseWrapper;
import com.hp.it.spf.xa.dc.portal.DataCallback;
import com.hp.it.spf.xa.dc.portal.ErrorCode;
import com.hp.it.spf.xa.misc.Consts;
import com.hp.it.spf.xa.misc.Environment;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.common.website.MenuItemNode;
import com.epicentric.navigation.MenuItem;
import com.vignette.portal.website.enduser.PortalContext;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.MDC;

/**
 * This filter performs different activities around logging which are scoped to the lifetime of
 * a request.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class RequestLogFilter implements Filter {

	private static final String FILTER_APPLIED_KEY = RequestLogFilter.class.getName() + ".APPLIED";

	private static final String MDC_SITE_NAME = "SiteName";
	private static final String MDC_LOGIN_ID = "LoginId";
	private static final String MDC_PORTAL_SESSION_ID = "PortalSessionId";

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		boolean isTopLevelRequest = (request.getAttribute(FILTER_APPLIED_KEY) == null);

		if (isTopLevelRequest) {
			request.setAttribute(FILTER_APPLIED_KEY, Boolean.TRUE);
			
			initMDC((HttpServletRequest) request);
			RequestContext requestContext = initRequestContext(request);
			TimeRecorder timeRecorder = requestContext.getTimeRecorder();
			DiagnosticContext diagnosticContext = requestContext.getDiagnosticContext();
			initDiagnosticContext((HttpServletRequest) request, diagnosticContext);

			try {
				timeRecorder.recordStart(Operation.REQUEST, ((HttpServletRequest) request).getRequestURI());

				chain.doFilter(
						request,
						new DiagnosticContextResponseWrapper(
								(HttpServletRequest) request,
								(HttpServletResponse) response));

				timeRecorder.recordEnd(Operation.REQUEST);
			}
			catch (IOException e) {
				timeRecorder.recordError(Operation.REQUEST, e);
				diagnosticContext.setError(ErrorCode.REQUEST001, e.toString());
				throw e;
			}
			catch (ServletException e) {
				timeRecorder.recordError(Operation.REQUEST, e);
				diagnosticContext.setError(ErrorCode.REQUEST001, e.toString());
				throw e;
			}
			catch (RuntimeException e) {
				timeRecorder.recordError(Operation.REQUEST, e);
				diagnosticContext.setError(ErrorCode.REQUEST001, e.toString());
				throw e;
			}
			finally {
				timeRecorder.logRecordedData();

				// Thread instance must be reset to make sure that the next time (during next request) this thread
				// calls, the RequestContext gets properly initialized for this thread.
				RequestContext.resetThreadInstance();

				cleanMDC();
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}

	private void initDiagnosticContext(HttpServletRequest request, DiagnosticContext diagnosticContext) {
		diagnosticContext.set("Web Server Name", getWebServerName(request));
		diagnosticContext.set("Portal Server Name", Environment.singletonInstance.getManagedServerName());
		diagnosticContext.set("Nav Item Name", getMenuItemName(request));

		HttpSession session = request.getSession(false);
		if (session != null) {
			DiagnosticContext savedDiagnosticContext =
					(DiagnosticContext) session.getAttribute(DiagnosticContextResponseWrapper.DC_SESSION_KEY);
			if (savedDiagnosticContext != null) {
				session.removeAttribute(DiagnosticContextResponseWrapper.DC_SESSION_KEY);
				diagnosticContext.set("IMPORTANT", "Error from previous request");
				diagnosticContext.copyFrom(savedDiagnosticContext);
			}
		}
	}

	/**
	 * Retrieves the web server name based on the value of <code>SRPHostName</code> request header.
	 * If the header contains fully-qualified host name this method returns only the first part
	 * (up to '.'). If the header is not present the method returns empty string.
	 *
	 * @param request portal request
	 * @return web server host name or empty string if SRPHostName header is not present
	 */
	private String getWebServerName(HttpServletRequest request)
	{
		String srpName = request.getHeader("SRPHostName");
		if (srpName != null) {
			int pos = srpName.indexOf('.');
			if (pos > 0) {
				return srpName.substring(0, pos);
			}
			else {
				return srpName;
			}
		}

		return "";
	}

	/**
	 * Returns a callback which will be used to retrieve the menu item name.
	 * This approach is required as at the time this method is called portal request is not yet
	 * completely initialized - this happens in the filters and/or servlet which are invoked
	 * after this filter.
	 * @param request current request
	 * @return callback used to retrieve the menu item name
	 */
	private DataCallback getMenuItemName(final HttpServletRequest request) {
		return new DataCallback() {
			public String getData() {
				PortalContext portalContext = (PortalContext) request.getAttribute("portalContext");
				if (portalContext == null) {
					return "";
				}
				MenuItemNode menuItemNode = MenuItemUtils.getSelectedMenuItemNode(portalContext);
				if (menuItemNode == null) {
					return "";
				}
				MenuItem menuItem = menuItemNode.getMenuItem();
				if (menuItem == null) {
					return "";
				}
				return menuItem.getTitle();
			}
		};
	}

	/**
	 * Sets in the MDC site name, user's login ID (username) and portal session ID.
	 * @param request portal request
	 */
	private void initMDC(HttpServletRequest request) {
		String siteName = getSiteName(request);
		String loginId = getLoginId(request);
		String sessionId = getSessionId(request);

		if (siteName != null) {
			MDC.put(MDC_SITE_NAME, siteName);
		}
		if (loginId != null) {
			MDC.put(MDC_LOGIN_ID, loginId);
		}
		if (sessionId != null) {
			MDC.put(MDC_PORTAL_SESSION_ID, sessionId);
		}
	}

	private String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		else {
			return session.getId();
		}
	}

	private String getLoginId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		Map profile = (Map) session.getAttribute(Consts.USER_PROFILE_KEY);
		if (profile == null) {
			return null;
		}
		return String.valueOf(profile.get(Consts.KEY_USER_NAME));
	}

	private String getSiteName(HttpServletRequest request) {
		String siteName = Utils.getEffectiveSiteDNS(request);
		if (siteName == null && request.getRequestURI().indexOf("/console/") != -1) {
			return "console";
		}
		return siteName;
	}

	private void cleanMDC() {
		MDC.remove(MDC_SITE_NAME);
		MDC.remove(MDC_LOGIN_ID);
		MDC.remove(MDC_PORTAL_SESSION_ID);
	}

	/**
	 * Binds the thread-scoped {@link com.hp.it.spf.xa.misc.portal.RequestContext} to the given request.
	 * @param request portal request
	 * @return this request's thread-scoped context
	 */
	private RequestContext initRequestContext(ServletRequest request) {
		RequestContext requestContext = RequestContext.getThreadInstance();
		request.setAttribute(RequestContext.REQUEST_KEY, requestContext);
		return requestContext;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}
}
