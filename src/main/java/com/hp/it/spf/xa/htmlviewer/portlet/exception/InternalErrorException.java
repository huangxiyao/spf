/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.htmlviewer.portlet.exception;

import com.hp.it.spf.xa.exception.portlet.SystemException;

/**
 * A system exception used by the <code>html-viewer</code> portlet code
 * whenever a system error has occurred. For example, when no view filename is
 * configured by the time that the
 * {@link com.hp.it.spf.xa.htmlviewer.portlet.web.ViewController} executes.
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.exception.portlet.SystemException</code>
 */
public class InternalErrorException extends SystemException {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default error code
	 */
	public static final String DEFAULT_ERROR = "error.internal";

	/**
	 * Default error message for debug
	 */
	public static final String DEFAULT_MSG = "A system problem has occurred. The user should try again later.";

	/**
	 * Construction method which provides default error code and message.
	 * 
	 */
	public InternalErrorException() {
		super(DEFAULT_ERROR, DEFAULT_MSG);
	}

	/**
	 * Construction method.
	 * 
	 * @param errorCode -
	 *            Error code
	 * @param errorMsg -
	 *            Error message
	 */
	public InternalErrorException(String errorCode) {
		super(errorCode);
	}

	/**
	 * Construction method.
	 * 
	 * @param errorCode -
	 *            Error code
	 * @param errorMsg -
	 *            Error message
	 */
	public InternalErrorException(String errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}

	/**
	 * Construction method.
	 * 
	 * @param errorCode -
	 *            Error code
	 * @param exception -
	 *            Cause exception
	 * @param errorMsg -
	 *            Error message
	 */
	public InternalErrorException(String errorCode, Exception exception,
			String errorMsg) {
		super(errorCode, exception, errorMsg);
	}
}