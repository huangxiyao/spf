package com.hp.spp.portal.diagnosticcontext;

import com.hp.spp.common.util.DiagnosticContext;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Filter used to initialize the thread instance of DiagnosticContext  
 */

public class DiagnosticContextFilter implements Filter {
	public static final String DIAGNOSTIC_CONTEXT_REQUEST_KEY = DiagnosticContext.class.getName();
	private static Logger mLog = Logger.getLogger(DiagnosticContextFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) servletRequest).getSession();
		boolean isTopLevelRequest = (servletRequest.getAttribute(DIAGNOSTIC_CONTEXT_REQUEST_KEY) == null);

		if (mLog.isDebugEnabled()) {
    		mLog.debug("In DiagnosticContext filter ");
		}
		if (isTopLevelRequest) {
            DiagnosticContext diagnosticContext = DiagnosticContext.getThreadInstance();
			diagnosticContext.clear();
			servletRequest.setAttribute(DIAGNOSTIC_CONTEXT_REQUEST_KEY, diagnosticContext);

			// If we have have a diagnostic context object bound to session it means that the previous request
			// ended with a redirection and the diagnostic context was not empty.
			// IN this case take it from the session and use it to fill in the context for this request.
			if (session.getAttribute(DIAGNOSTIC_CONTEXT_REQUEST_KEY) != null) {
				DiagnosticContext savedDiagnosticContext = (DiagnosticContext) session.getAttribute(DIAGNOSTIC_CONTEXT_REQUEST_KEY);
				session.removeAttribute(DIAGNOSTIC_CONTEXT_REQUEST_KEY);
				diagnosticContext.copyFrom(savedDiagnosticContext);
			}
			
			filterChain.doFilter(
					servletRequest, 
					new ResponseWrapper(
							(HttpServletResponse) servletResponse, 
						session));
			
		}
		else {
			filterChain.doFilter(
					servletRequest, 
					new ResponseWrapper(
						(HttpServletResponse) servletResponse, 
						session));
			
		}
	}

	public void destroy() {
	}
}
