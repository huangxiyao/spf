package com.hp.it.spf.openportal.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.openportal.log.SimpleLogFormatter;
import com.hp.it.spf.xa.misc.Utils;

/**
 * Filter which gets from the HTTP request header the diagnostic ID and sets it in
 * the {@link com.hp.it.spf.openportal.log.SimpleLogFormatter} so it's available in the log files.
 *
 * @author pranav
 */
public class LogDiagnosticIdFilter implements Filter {
	
	private static final Logger LOG = Logger.getLogger(LogDiagnosticIdFilter.class.getPackage().getName());
	private static final String FILTER_APPLIED = LogDiagnosticIdFilter.class.getName() + ".APPLIED";

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException
	{

		if (servletRequest.getAttribute(FILTER_APPLIED) == null) {
			servletRequest.setAttribute(FILTER_APPLIED, Boolean.TRUE);

			String uniqueID = Utils.getDiagnosticId((HttpServletRequest) servletRequest);
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("Diagnostic ID present in request header: " + uniqueID);
			}
			try {
				SimpleLogFormatter.setDiagnosticId(uniqueID);
				chain.doFilter(servletRequest, servletResponse);
			} finally {
				SimpleLogFormatter.setDiagnosticId(null);
			}
		}
		else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
