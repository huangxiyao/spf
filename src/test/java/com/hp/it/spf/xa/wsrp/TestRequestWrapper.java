package com.hp.it.spf.xa.wsrp;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class TestRequestWrapper extends MockObjectTestCase {

	public void testGetHeader() {
		Mock mockRequest = mock(HttpServletRequest.class);
		mockRequest.stubs().method("getHeader").
				with(eq("sample")).will(returnValue("samplevalue"));
		mockRequest.stubs().method("getHeader").
				with(eq("user-agent")).will(returnValue("firefox"));

		RequestWrapper wrapper = new RequestWrapper((HttpServletRequest) mockRequest.proxy(), "__KEY");
		assertEquals("getting sample header", "samplevalue", wrapper.getHeader("sample"));
		assertEquals("getting user-agent header", "firefox__KEY", wrapper.getHeader("user-agent"));
	}

	public void testGetHeaderNoUserAgentInRequest() {
		Mock mockRequest = mock(HttpServletRequest.class);
		mockRequest.stubs().method("getHeader").
				with(eq("user-agent")).will(returnValue(null));

		RequestWrapper wrapper = new RequestWrapper((HttpServletRequest) mockRequest.proxy(), "__KEY");
		assertEquals("user-agent value from wrapper", "__KEY", wrapper.getHeader("user-agent"));
	}

	public void testGetHeaderNamesNoUserAgent() {
		Mock mockRequest = mock(HttpServletRequest.class);
		mockRequest.stubs().method("getHeader");
		mockRequest.stubs().method("getHeaderNames").will(
				returnValue(new Vector(Arrays.asList(new String[] {"header1", "header2"})).elements()));

		RequestWrapper wrapper = new RequestWrapper((HttpServletRequest) mockRequest.proxy(), "__KEY");
		boolean userAgentHeaderFound = false;
		for (Enumeration en = wrapper.getHeaderNames(); !userAgentHeaderFound && en.hasMoreElements(); ) {
			userAgentHeaderFound = "user-agent".equals(en.nextElement());
		}
		assertTrue("user-agent header added", userAgentHeaderFound);
	}

	public void testGetHeaders() {
		Mock mockRequest = mock(HttpServletRequest.class);
		mockRequest.stubs().method("getHeader").with(eq("user-agent")).will(returnValue("firefox"));
		mockRequest.stubs().method("getHeaders").
				with(eq("user-agent")).
				will(returnValue(new Vector(Arrays.asList(new String[] {"firefox"})).elements()));

		RequestWrapper wrapper = new RequestWrapper((HttpServletRequest) mockRequest.proxy(), "__KEY");
		Enumeration en = wrapper.getHeaders("user-agent");
		assertTrue("user-agent header found", en.hasMoreElements());
		assertEquals("user-agent header value", "firefox__KEY", en.nextElement());
	}
}
