package com.hp.it.spf.misc.portal.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Filter used to prevent concurrent access on request attributes (set/remove
 * methods) and attribute name enumeration. Concurrent access to request
 * attributes under portal local/remote portlets infrastructure may cause
 * {@link java.util.ConcurrentModificationException} during enumeration of
 * {@link HttpServletRequest#getAttributeNames()}.
 * <p>
 * Some web application container, like WebLogic 10.3.6.0, is using
 * {@link java.util.HashMap} as attributes store. This cause typical concurrent
 * access issue, like {@link java.util.ConcurrentModificationException}, or
 * typical infinite loop cause server hang.
 */
public class RequestConcurrentAccessFilter implements Filter {

	/**
	 * This filter wrap request to synchronize request attribute update and
	 * attribute names enumeration.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServletRequest newRequest = request;
		if (newRequest instanceof HttpServletRequest) {
			newRequest = wrapConcurrentAccessRequest((HttpServletRequest) newRequest);
		}
		chain.doFilter(newRequest, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	private HttpServletRequest wrapConcurrentAccessRequest(
			HttpServletRequest request) {
		// Make sure set/remove attribute and get attribute names are thread
		// synchronized
		return new HttpServletRequestWrapper(request) {

			@Override
			public synchronized void setAttribute(String name, Object o) {
				super.setAttribute(name, o);
			}

			@Override
			public synchronized void removeAttribute(String name) {
				super.removeAttribute(name);
			}

			/*
			 * To avoid the caller get ConcurrentModificationException during
			 * enumerate the attribute names, this method need to be
			 * synchronized, and return a clone of attribute names without link
			 * back to original attributes map.
			 */
			@Override
			public synchronized Enumeration<?> getAttributeNames() {
				Enumeration<?> enumer = super.getAttributeNames();
				List<Object> attributes = new ArrayList<Object>();
				while (enumer.hasMoreElements()) {
					attributes.add(enumer.nextElement());
				}
				return Collections.enumeration(attributes);
			}
		};
	}

}
