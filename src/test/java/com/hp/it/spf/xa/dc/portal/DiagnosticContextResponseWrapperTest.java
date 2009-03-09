package com.hp.it.spf.xa.dc.portal;

import com.hp.it.spf.xa.misc.portal.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class DiagnosticContextResponseWrapperTest extends MockObjectTestCase
{

	protected void setUp() throws Exception
	{
		RequestContext.resetThreadInstance();
	}

	public void testSendRedirectDoesNothingIfNoErrors() throws Exception {
		Mock sessionMock = mock(HttpSession.class);
		sessionMock.
				expects(never()).
				method("setAttribute").
				with(eq(DiagnosticContextResponseWrapper.DC_SESSION_KEY));

		Mock requestMock = mock(HttpServletRequest.class);
		requestMock.stubs().method("getSession").will(returnValue(sessionMock.proxy()));

		Mock responseMock = mock(HttpServletResponse.class);
		responseMock.expects(once()).method("sendRedirect").with(eq("somewhere"));

		DiagnosticContextResponseWrapper wrapper =
				new DiagnosticContextResponseWrapper(
						(HttpServletRequest) requestMock.proxy(),
						(HttpServletResponse) responseMock.proxy());
		wrapper.sendRedirect("somewhere");
	}

	public void testSendRedirectSavesContextInSessionIfErrors() throws Exception {
		RequestContext.getThreadInstance().getDiagnosticContext().setError(ErrorCode.AUTH001, "xxx");
		Mock sessionMock = mock(HttpSession.class);
		sessionMock.expects(once()).method("setAttribute").with(
				eq(DiagnosticContextResponseWrapper.DC_SESSION_KEY), isA(DiagnosticContext.class));

		Mock requestMock = mock(HttpServletRequest.class);
		requestMock.stubs().method("getSession").will(returnValue(sessionMock.proxy()));

		Mock responseMock = mock(HttpServletResponse.class);
		responseMock.expects(once()).method("sendRedirect").with(eq("somewhereelse"));

		DiagnosticContextResponseWrapper wrapper =
				new DiagnosticContextResponseWrapper(
						(HttpServletRequest) requestMock.proxy(),
						(HttpServletResponse) responseMock.proxy());
		wrapper.sendRedirect("somewhereelse");
	}

}
