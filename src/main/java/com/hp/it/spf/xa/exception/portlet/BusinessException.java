/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.exception.portlet;

import javax.portlet.PortletRequest;

import com.hp.it.spf.xa.exception.portlet.SPFException;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;

/**
 * <p>
 * This class (or a subclass of it) can be used by portlet developers to
 * represent some kind of <i>business exception</i>. A business exception is an
 * error condition resulting from circumstances under the user's control and
 * generally the user's fault: invalid user input as determined by business
 * logic, for example (such as submitting blank text for a new posting to a
 * forums thread). Contrast this with <i>system exceptions</i>, which are
 * internal system problems outside of the user's responsibility and generally
 * not reparable by the user (such as the Forums database being unavailable, or
 * the Forums thread referenced by a new note suddenly no longer being found in
 * the database).
 * </p>
 * <p>
 * An SPF <code>BusinessException</code> contains an error code, an error
 * message, and optionally some other {@link java.lang.Throwable} object which
 * is the root cause, and/or a localized error message for the user. Please see
 * the class documentation for the superclass, {@link SPFException}, for more
 * information about these attributes.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @author sunnyee
 * @version TBD
 * @see <code>com.hp.it.spf.xa.exception.portlet.SPFException</code>
 * <code>com.hp.it.spf.xa.exception.portlet.SystemException</code>
 */
public class BusinessException extends SPFException {

	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- Constructors

	/**
	 * <p>
	 * Construct an empty business exception. All class attributes are null or
	 * empty.
	 * </p>
	 */
	public BusinessException() {
		super();
	}

	/**
	 * <p>
	 * Construct business exception containing just an error code. See the
	 * {@link SPFException#SPFException(String)} documentation for more
	 * information about how the class attributes are set.
	 * </p>
	 * 
	 * @param pErrorCode
	 *            The error code (eg <code>forums.addNote.blank</code>)
	 */
	public BusinessException(String pErrorCode) {
		super(pErrorCode);
	}

	/**
	 * <p>
	 * Construct a business exception containing an error code and an error message.
	 * See the {@link SPFException#SPFException(String,String)} documentation
	 * for more information about how the class attributes are set.
	 * </p>
	 * 
	 * @param pErrorCode
	 *            The error code (eg <code>forums.addNote.blank</code>)
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>New note text for forums thread cannot be blank.</code>
	 */
	public BusinessException(String pErrorCode, String pErrorMessage) {
		super(pErrorCode, pErrorMessage);
	}

	/**
	 * <p>
	 * Construct a business exception containing an error code and an error message.
	 * See the {@link SPFException#SPFException(String,Throwable,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pErrorCode
	 *            The error code (eg <code>forums.addNote.blank</code>)
	 * @param pCause
	 *            Some throwable (eg exception) which is the root cause of this
	 *            one
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>New note text for forums thread cannot be blank.</code>
	 */
	public BusinessException(String pErrorCode, Throwable pCause,
			String pErrorMessage) {
		super(pErrorCode, pCause, pErrorMessage);
	}

	/**
	 * <p>
	 * Construct a business exception containing an error code and an error message.
	 * See the {@link SPFException#SPFException(PortletRequest,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pErrorCode
	 *            The error code (eg <code>forums.addNote.blank</code>)
	 */
	public BusinessException(PortletRequest pRequest, String pErrorCode) {
		super(pRequest, pErrorCode);
	}

	/**
	 * <p>
	 * Construct a business exception containing an error code and an error message.
	 * See the {@link SPFException#SPFException(PortletRequest,String,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pErrorCode
	 *            The error code (eg <code>forums.addNote.blank</code>)
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>New note text for forums thread cannot be blank.</code>
	 */
	public BusinessException(PortletRequest pRequest, String pErrorCode,
			String pErrorMessage) {
		super(pRequest, pErrorCode, pErrorMessage);
	}

	/**
	 * <p>
	 * Construct a business exception containing an error code and an error message.
	 * See the
	 * {@link SPFException#SPFException(PortletRequest,String,Throwable,String)}
	 * documentation for more information about how the class attributes are
	 * set.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pErrorCode
	 *            The error code (eg <code>forums.addNote.blank</code>)
	 * @param pCause
	 *            Some throwable (exception) which is the root cause of this one
	 * @param pErrorMessage
	 *            The error message - eg:
	 *            <code>New note text for forums thread cannot be blank.</code>
	 */
	public BusinessException(PortletRequest pRequest, String pErrorCode,
			Throwable pCause, String pErrorMessage) {
		super(pRequest, pErrorCode, pCause, pErrorMessage);
	}
}
