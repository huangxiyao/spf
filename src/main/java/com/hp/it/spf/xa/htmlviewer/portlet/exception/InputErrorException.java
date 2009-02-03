/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.htmlviewer.portlet.exception;

import com.hp.it.spf.xa.exception.portlet.BusinessException;

/**
 * A business exception used in <code>config</code> mode of the <code>html-viewer</code> portlet when there has been an
 * administrator data-entry error.
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.exception.portlet.BusinessException</code>
 */
public class InputErrorException extends BusinessException {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default error code
	 */
	private static final String DEFAULT_ERROR = "error.input";

	/**
	 * Default error message for debug
	 */
	private static final String DEFAULT_MSG = "The user input is in error. The user should correct the error and try again.";

	/**
	 * Construction method which provides default error code and message.
	 * 
	 */
	public InputErrorException() {
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
	public InputErrorException(String errorCode) {
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
	public InputErrorException(String errorCode, String errorMsg) {
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
	public InputErrorException(String errorCode, Exception exception,
			String errorMsg) {
		super(errorCode, exception, errorMsg);
	}
}