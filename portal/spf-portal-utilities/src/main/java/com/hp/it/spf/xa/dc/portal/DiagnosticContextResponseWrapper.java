package com.hp.it.spf.xa.dc.portal;

import com.hp.it.spf.xa.misc.portal.RequestContext;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The response wrapper used to ensure that the errors captured in the {@link DiagnosticContext}
 * survive redirections.
 *  
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class DiagnosticContextResponseWrapper extends HttpServletResponseWrapper {

	public static final String DC_SESSION_KEY = DiagnosticContext.class.getName();

	private HttpServletRequest mRequest;

	public DiagnosticContextResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
		super(response);
		mRequest = request;
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		DiagnosticContext diagnosticContext = RequestContext.getThreadInstance().getDiagnosticContext();
		if (diagnosticContext.hasErrors()) {
			HttpSession session = mRequest.getSession();
			DiagnosticContext clonedContext = diagnosticContext.cloneErrorsOnly();
			session.setAttribute(DC_SESSION_KEY, clonedContext);
		}
		super.sendRedirect(location);
	}
}
