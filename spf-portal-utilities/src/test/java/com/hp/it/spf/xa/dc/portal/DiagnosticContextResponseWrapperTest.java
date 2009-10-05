package com.hp.it.spf.xa.dc.portal;

import com.hp.it.spf.xa.misc.portal.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.jmock.Expectations;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
@RunWith(JMock.class)
public class DiagnosticContextResponseWrapperTest
{
	Mockery mContext = new JUnit4Mockery();

	@Before
	public void setUp() throws Exception
	{
		RequestContext.resetThreadInstance();
	}

	@Test
	public void testSendRedirectDoesNothingIfNoErrors() throws Exception {
		final HttpSession session = mContext.mock(HttpSession.class);
		mContext.checking(new Expectations() {{
			never(session).setAttribute(
					with(equal(DiagnosticContextResponseWrapper.DC_SESSION_KEY)),
					with(any(DiagnosticContext.class)));
		}});

		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getSession(); will(returnValue(session));
		}});

		final HttpServletResponse response = mContext.mock(HttpServletResponse.class);
		mContext.checking(new Expectations() {{
			oneOf(response).sendRedirect("somewhere");
		}});

		DiagnosticContextResponseWrapper wrapper = new DiagnosticContextResponseWrapper(request, response);
		wrapper.sendRedirect("somewhere");
	}

	@Test
	public void testSendRedirectSavesContextInSessionIfErrors() throws Exception {
		RequestContext.getThreadInstance().getDiagnosticContext().setError(ErrorCode.AUTH001, "xxx");

		final HttpSession session = mContext.mock(HttpSession.class);
		mContext.checking(new Expectations() {{
			oneOf(session).setAttribute(
					with(equal(DiagnosticContextResponseWrapper.DC_SESSION_KEY)),
					with(any(DiagnosticContext.class)));
		}});

		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getSession(); will(returnValue(session));
		}});

		final HttpServletResponse response = mContext.mock(HttpServletResponse.class);
		mContext.checking(new Expectations() {{
			oneOf(response).sendRedirect("somewhereelse");
		}});

		DiagnosticContextResponseWrapper wrapper =
				new DiagnosticContextResponseWrapper(request, response);
		wrapper.sendRedirect("somewhereelse");
	}

}
