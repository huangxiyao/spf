package com.hp.spp.portal.diagnosticcontext;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.hp.spp.common.util.DiagnosticContext;

public class ResponseWrapper extends HttpServletResponseWrapper {

	private HttpSession mSession;
	
	public ResponseWrapper(HttpServletResponse response, HttpSession session) {
		super(response);
		mSession = session;
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		super.sendRedirect(location);
		bindDiagnosticContextToSession();
	}

	private void bindDiagnosticContextToSession() {
		DiagnosticContext context = DiagnosticContext.getThreadInstance();
		if (!context.isEmpty()){
			mSession.setAttribute(DiagnosticContextFilter.DIAGNOSTIC_CONTEXT_REQUEST_KEY, 
					context.clone());
		}
	}

}
