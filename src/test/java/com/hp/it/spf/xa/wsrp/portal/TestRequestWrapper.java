package com.hp.it.spf.xa.wsrp.portal;

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.jmock.Expectations;
import static org.hamcrest.core.Is.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
@RunWith(JMock.class)
public class TestRequestWrapper  {

	Mockery mContext = new JUnit4Mockery();

	@Test
	public void testGetHeader() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getHeader("sample"); will(returnValue("sampleValue"));
			allowing(request).getHeader("user-agent"); will(returnValue("firefox"));
		}});

		RequestWrapper wrapper = new RequestWrapper(request, "__KEY");
		assertThat("getting sample header", wrapper.getHeader("sample"), is("sampleValue"));
		assertThat("getting user-agent header", wrapper.getHeader("user-agent"), is("firefox__KEY"));
	}

	@Test
	public void testGetHeaderNoUserAgentInRequest() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getHeader("user-agent"); will(returnValue(null));
		}});

		RequestWrapper wrapper = new RequestWrapper(request, "__KEY");
		assertThat("user-agent value from wrapper", wrapper.getHeader("user-agent"), is("__KEY"));
	}

	@Test
	public void testGetHeaderNamesNoUserAgent() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getHeader("user-agent"); will(returnValue(null));
			allowing(request).getHeaderNames(); will(returnEnumeration("header1", "header2"));
		}});

		RequestWrapper wrapper = new RequestWrapper(request, "__KEY");
		boolean userAgentHeaderFound = false;
		for (Enumeration en = wrapper.getHeaderNames(); !userAgentHeaderFound && en.hasMoreElements(); ) {
			userAgentHeaderFound = "user-agent".equals(en.nextElement());
		}
		assertTrue("user-agent header added", userAgentHeaderFound);
	}

	@Test
	public void testGetHeaders() {
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		mContext.checking(new Expectations() {{
			allowing(request).getHeader("user-agent"); will(returnValue("firefox"));
			allowing(request).getHeaders("user-agent"); will(returnEnumeration("firefox"));
		}});

		RequestWrapper wrapper = new RequestWrapper(request, "__KEY");
		Enumeration en = wrapper.getHeaders("user-agent");
		assertTrue("user-agent header found", en.hasMoreElements());
		assertThat("user-agent header value", (String) en.nextElement(), is("firefox__KEY"));
	}
}
