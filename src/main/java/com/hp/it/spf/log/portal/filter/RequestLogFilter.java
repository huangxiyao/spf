package com.hp.it.spf.log.portal.filter;

import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.hp.it.spf.xa.misc.Consts;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.MDC;

/**
 * This filter performs different activities around logging which are scoped to the lifetime of
 * a request.
 * It initializes {@link TimeRecorder} and records the ovall request execution time. It also initializes
 * Log4J MDC.
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
			TimeRecorder timeRecorder = initTimeRecorder(request);

			try {
				timeRecorder.recordStart(Operation.REQUEST, ((HttpServletRequest) request).getRequestURI());
				chain.doFilter(request, response);
				timeRecorder.recordEnd(Operation.REQUEST);
			}
			catch (IOException e) {
				timeRecorder.recordError(Operation.REQUEST, e);
				throw e;
			}
			catch (ServletException e) {
				timeRecorder.recordError(Operation.REQUEST, e);
				throw e;
			}
			catch (RuntimeException e) {
				timeRecorder.recordError(Operation.REQUEST, e);
				throw e;
			}
			finally {
				timeRecorder.logRecordedData();

				// Thread instance must be reset to make sure that the next time (during next request) this thread
				// calls, the TimeRecorder gets properly initialized for this thread.
				TimeRecorder.resetThreadInstance();

				cleanMDC();
			}
		}
		else {
			chain.doFilter(request, response);
		}
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
	 * Binds the thread-scoped {@link com.hp.it.spf.xa.log.portal.TimeRecorder} to the given request.
	 * @param request portal request
	 * @return this request's thread-scoped time recorder
	 */
	private TimeRecorder initTimeRecorder(ServletRequest request) {
		TimeRecorder timeRecorder = TimeRecorder.getThreadInstance();
		request.setAttribute(TimeRecorder.REQUEST_KEY, timeRecorder);
		return timeRecorder;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}
}
