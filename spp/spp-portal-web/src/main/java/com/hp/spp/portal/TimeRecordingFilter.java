package com.hp.spp.portal;

import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;
import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.hpp.supporttools.HPPHeaderHelper;
import com.hp.spp.profile.Constants;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import org.apache.log4j.MDC;

/**
 * Filter used to initialize the thread instance of {@link TimeRecorder} and to log the gathered
 * time information.
 */
public class TimeRecordingFilter implements Filter {

	public static final String TIME_RECORDER_REQUEST_KEY = TimeRecorder.class.getName();

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		boolean isTopLevelRequest = (servletRequest.getAttribute(TIME_RECORDER_REQUEST_KEY) == null);

		if (isTopLevelRequest) {
			TimeRecorder timeRecorder = TimeRecorder.getThreadInstance();
			servletRequest.setAttribute(TIME_RECORDER_REQUEST_KEY, timeRecorder);

			try {
				// This filter is normally dedicated to TimeRecorder init and cleanup and it's not the ideal
				// place to handle Log4j MDC. However, this is the most outer SPP filter and putting
				// this code here we don't have to add yet another filter.
				initLog4jMDC((HttpServletRequest) servletRequest);

				timeRecorder.recordStart(Operation.REQUEST, ((HttpServletRequest) servletRequest).getRequestURI());
				filterChain.doFilter(servletRequest, servletResponse);
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

				clearLog4jMDC();
			}
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	public void destroy() {
	}

	/**
	 * Initializes Log4j Mapped Diagnostic Context with <tt>SiteName</tt>, <tt>PortalSessionId</tt>
	 * and <tt>LoginId</tt> values specific for the current request.
	 */
	private void initLog4jMDC(HttpServletRequest request) {
		String siteName = SiteURLHelper.determineMasterSite(request);
		String userId = HPPHeaderHelper.getSMUser(request);
		String sessionId = (request.getSession(false) == null ? null : request.getSession(true).getId());

		if (siteName != null) {
			MDC.put(Constants.MAP_SITE, siteName);
		}
		if (userId != null) {
			MDC.put(Constants.MAP_USERNAME, userId);
		}
		if (sessionId != null) {
			MDC.put(Constants.MAP_PORTALSESSIONID, sessionId);
		}
	}

	/**
	 * Removes the values from Log4j Mapped Diagnostic Context that were put there in method {@link #initLog4jMDC(javax.servlet.http.HttpServletRequest)}.
	 */
	private void clearLog4jMDC() {
		MDC.remove(Constants.MAP_SITE);
		MDC.remove(Constants.MAP_USERNAME);
		MDC.remove(Constants.MAP_PORTALSESSIONID);
	}
}
