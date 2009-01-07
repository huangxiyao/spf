/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.htmlviewer.portlet.exception;

import com.hp.it.spf.xa.exception.portlet.SystemException;

/**
 * Exception used in view mode of the HTMLViewer portlet when no view file
 * content exists to display.
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @version TBD
 * @see com.hp.it.spf.xa.exception.portlet.BusinessException
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