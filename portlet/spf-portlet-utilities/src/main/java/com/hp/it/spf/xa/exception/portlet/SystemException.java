/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.exception.portlet;

import javax.portlet.PortletRequest;

import com.hp.it.spf.xa.exception.portlet.SPFException;

/**
 * <p>
 * This class (or a subclass of it) can be used by portlet developers to
 * represent some kind of <i>system exception</i>. A system exception is an
 * internal system problem outside of the user's responsibility and ability to
 * control or fix (for example, the Forums database being unavailable, or the
 * Forums thread referenced by a new note suddenly no longer being found in the
 * database). Contrast this with a <i>business exception</i>, which is an error
 * condition resulting from circumstances under the user's control and generally
 * the user's fault: invalid user input as determined by business logic, for
 * example (such as submitting blank text for a new posting to a forums thread).
 * </p>
 * <p>
 * An SPF <code>SystemException</code> contains an error code, an error
 * message, and optionally some other {@link java.lang.Throwable} object and/or
 * a localized error message for the user. Please see the class documentation
 * for the superclass, {@link SPFException}, for more information about these
 * attributes.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @author sunnyee
 * @version TBD
 * @see <code>com.hp.it.spf.xa.exception.portlet.SPFException</code>
 * <code>com.hp.it.spf.xa.exception.portlet.BusinessException</code>
 */
public class SystemException extends SPFException {

	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- Constructors

	/**
	 * <p>
	 * Construct an empty system exception. All class attributes are null or
	 * empty.
	 * </p>
	 */
	public SystemException() {
		super();
	}

	/**
	 * <p>
	 * Construct a system exception containing just an error code. See the
	 * {@link SPFException#SPFException(String)} documentation for more
	 * information about how the class attributes are set.
	 * </p>
	 * 
	 * @param pErrorCode
	 *            The error code (eg <code>forums.dbUnavailable</code>)
	 */
	public SystemException(String pErrorCode) {
		super(pErrorCode);
	}

	/**
	 * <p>
	 * Construct a system exception containing an error code and an error
	 * message. See the {@link SPFException#SPFException(String,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pErrorCode
	 *            The error code (eg <code>forums.dbUnavailable</code>)
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>The Forums database is down.</code>
	 */
	public SystemException(String pErrorCode, String pErrorMessage) {
		super(pErrorCode, pErrorMessage);
	}

	/**
	 * <p>
	 * Construct a system exception containing an error code, an error message,
	 * and a next-in-chain {@link java.lang.Throwable}. See the
	 * {@link SPFException#SPFException(String,Throwable,String)} documentation
	 * for more information about how the class attributes are set.
	 * </p>
	 * 
	 * @param pErrorCode
	 *            The error code (eg <code>forums.dbUnavailable</code>)
	 * @param pNext
	 *            Some throwable (eg exception) which is next in the chain (eg
	 *            the root cause, or a related exception)
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>The Forums database is down.</code>
	 */
	public SystemException(String pErrorCode, Throwable pNext,
			String pErrorMessage) {
		super(pErrorCode, pNext, pErrorMessage);
	}

	/**
	 * <p>
	 * Construct a system exception containing an error code and its localized
	 * message. See the {@link SPFException#SPFException(PortletRequest,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pErrorCode
	 *            The error code (eg <code>forums.dbUnavailable</code>) -
	 *            also the message key for the localized message
	 */
	public SystemException(PortletRequest pRequest, String pErrorCode) {
		super(pRequest, pErrorCode);
	}

	/**
	 * <p>
	 * Construct a system exception containing an error code, an error message,
	 * and their localized message. See the
	 * {@link SPFException#SPFException(PortletRequest,String,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pErrorCode
	 *            The error code (eg <code>forums.dbUnavailable</code>) -
	 *            also the message key for the localized message
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>The Forums database is down.</code>
	 */
	public SystemException(PortletRequest pRequest, String pErrorCode,
			String pErrorMessage) {
		super(pRequest, pErrorCode, pErrorMessage);
	}

	/**
	 * <p>
	 * Construct a system exception containing an error code, an error message,
	 * and a next-in-chain {@link java.lang.Throwable}. See the
	 * {@link SPFException#SPFException(PortletRequest,String,Throwable,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pErrorCode
	 *            The error code (eg <code>forums.dbUnavailable</code>) -
	 *            also the message key for the localized message
	 * @param pNext
	 *            Some throwable (exception) which is the root cause of this one
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>The Forums database is down.</code>
	 */
	public SystemException(PortletRequest pRequest, String pErrorCode,
			Throwable pNext, String pErrorMessage) {
		super(pRequest, pErrorCode, pNext, pErrorMessage);
	}
}
