/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.wsrp.injector.context.portal.filter;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.hp.it.spf.localeresolver.mock.MockFilterChain;

/**
 * Test RequestBindingFilterTest
 * 
 * @author Kaijian Ding
 */
public class RequestBindingFilterTest extends TestCase {

	private RequestBindingFilter filter = new RequestBindingFilter();

	protected void setUp() {
	}

	public void testInit() {
	}

	public void testDoFilterWithoutUserAgent() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			filter.doFilter(request, response, new MockFilterChain());
			assertNotNull(request.getAttribute(RequestBindingFilter.class
					.getName()
					+ ".ThreadName"));
		} catch (IOException e) {
			assertFalse(true);
		} catch (ServletException e) {
			assertFalse(true);
		}
	}

	public void testDoFilterWithUserAgent() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("user-agent", "__SPF"
				+ RequestBindingFilter.class.getName() + ".ThreadName");
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			filter.doFilter(request, response, new MockFilterChain());
			assertNull(request.getAttribute(RequestBindingFilter.class
					.getName()
					+ ".ThreadName"));
		} catch (IOException e) {
			assertFalse(true);
		} catch (ServletException e) {
			assertFalse(true);
		}
	}
}
