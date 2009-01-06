/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.fast.web.spring;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.hp.fast.web.http.MediaTypeNegotiator;
import com.hp.fast.web.http.NotAcceptableException;
import com.hp.fast.web.spring.NegotiatedContentHandlerInterceptor;

public class NegotiatedContentHandlerInterceptorTest extends TestCase
{
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	public void setUp()
	{
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}
	
	public void test() throws NotAcceptableException
	{
		NegotiatedContentHandlerInterceptor hi = new NegotiatedContentHandlerInterceptor();
		MediaTypeNegotiator mn = new MediaTypeNegotiator(new Object[] {"text/xml", "application/xhtml+xml"});
		request.addHeader("Accept", "text/html;q=0.8,application/xhtml+xml;q=1.0");

		hi.setNegotiators(new Object[] {mn});
		
		try
		{
			hi.preHandle(request, response, null);
			fail("IllegalStateException expected");
		}
		catch (IllegalStateException ex) {}
		
		assertEquals("application/xhtml+xml", mn.negotiatedValue(request));
		assertEquals("application/xhtml+xml", response.getContentType());
	}

}
