/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.wsrp.portal;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Wraps a request by appending <code>userAgentPostfix</code> to user-agent header value. The HTTP
 * header accessor methods are modified appropriately to return this updated value.
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version TBD
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	/**
	 * Name of the request attribute used to keep a reference to original session object
	 * associated with this request.
	 */
	public static final String ORIGINAL_SESSION = RequestWrapper.class.getName() + ".ORIGINAL_SESSION";

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
	 * Keeps the original session object associated with this request.
	 */
	private HttpSession mSession = null;

	/**
	 * Always returns non-null session ignoring <code>create</code> flag.
	 * @see #getSession()
	 */
	@Override
	public HttpSession getSession(boolean create)
	{
		return getSession();
	}

	/**
	 * Returns the original session associated with this request.
	 * @return non-null session
	 */
	@Override
	public HttpSession getSession()
	{
		// This method was implemented as a workaround to __CR3668__ and Vignette Support ticket __247976__.
		// The issue seems to be due to the fact that both local and remote portlets try to retrieve
		// the session object in the same request. Invoking getSession during local portlets
		// processing results in a new session being allocated. Unfortunately, as remote portlets
		// use non-WebLogic thread pool for rendering, WebLogic seems to get confused and associates
		// the session created during local portlet processing with the portal request.
		// As a result none of the information stored originally in the session can be found
		// and this results in errors.
		// To work around the issue we maintain a reference to original session (captured on the
		// first invocation of this method during the request) and return it any time this method
		// is called. This seems to prevent the issue.
		// In addition, to isolate SPF code from the session retrieval issue performed by Axis
		// handlers used during WSRP request processing (e.g. StickyHandler), we also store
		// a reference to the session as request attribute. Newly added com.hp.it.spf.wsrp.misc.Utils#retrieveSession
		// can then use the request attribute value instead of calling request.getSession()
		// which before this change resulted in new session being allocated and associated with
		// the portal request.

		if (mSession == null) {
			mSession = ((HttpServletRequest)getRequest()).getSession();

			// Store session in request attribute for com.hp.it.spf.wsrp.misc.Utils#retrieveSession
			getRequest().setAttribute(ORIGINAL_SESSION, mSession);
		}

		return mSession;
	}

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
