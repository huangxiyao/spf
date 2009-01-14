/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.wsrp.injector.context;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Wraps a request by appending <code>userAgentPostfix</code> to user-agent header value. The HTTP
 * header accessor methods are modified appropriately to return this updated value.
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version TBD
 */
public class RequestWrapper extends HttpServletRequestWrapper {
	/**
	 * Stores modified user-agent value. It's a {@link java.util.Vector} in order to be able
	 * to easily generate {@link java.util.Enumeration} objects.
	 */
	private Vector mUserAgentValues = new Vector(1);

	/**
	 * <code>true</code> if user-agent header was present in the initial request.
	 */
	private boolean mUserAgentHeaderPresent = false;

	/**
	 * Creates the request wrapper that will return updated <code>user-agent</code> value.
	 * @param request original request
	 * @param userAgentPostfix postfix to add to the original request's <code>user-agent</code> value
	 */
	public RequestWrapper(HttpServletRequest request, String userAgentPostfix) {
		super(request);
		String userAgentValue = request.getHeader("user-agent");
		mUserAgentHeaderPresent = userAgentValue != null;
		if (mUserAgentHeaderPresent) {
			userAgentValue = userAgentValue + userAgentPostfix;
		}
		else {
			userAgentValue = userAgentPostfix;
		}
		mUserAgentValues.add(userAgentValue);
	}

	/**
	 * @param name name of the header
	 * @return updated user-agent value or original value if another header was requested
	 */
	public String getHeader(String name) {
		if ("user-agent".equalsIgnoreCase(name)) {
			return (String) mUserAgentValues.get(0);
		}
		else {
			return super.getHeader(name);
		}
	}

	/**
	 * @return enumeration of header names; in case <code>user-agent</code> header was not present
	 * in the original request this method will return it as if it were.
	 */
	public Enumeration getHeaderNames() {
		if (mUserAgentHeaderPresent) {
			return super.getHeaderNames();
		}
		else {
			// create anonymous enumeration that returns also 'user-agent' as it was not present
			// in the original request.
			return new Enumeration() {

				private Enumeration mSuperEnumeration = RequestWrapper.super.getHeaderNames();
				private boolean userAgentRequested = false;

				public boolean hasMoreElements() {
					return mSuperEnumeration.hasMoreElements() || !userAgentRequested;
				}

				public Object nextElement() {
					if (mSuperEnumeration.hasMoreElements()) {
						return mSuperEnumeration.nextElement();
					}
					else if (!userAgentRequested) {
						userAgentRequested = true;
						return "user-agent";
					}
					else {
						throw new NoSuchElementException();
					}
				}
			};
		}
	}

	/**
	 * @param name name of the header
	 * @return if <code>user-agent</code> is request it will return single-element enumaration with
	 * the updated value; otherwise it returns original request header values.
	 */
	public Enumeration getHeaders(String name) {
		if ("user-agent".equals(name)) {
			return mUserAgentValues.elements();
		}
		else {
			return super.getHeaders(name);
		}
	}
}
