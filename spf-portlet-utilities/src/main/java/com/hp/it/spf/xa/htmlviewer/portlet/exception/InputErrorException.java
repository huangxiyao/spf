/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.htmlviewer.portlet.exception;

import javax.portlet.PortletRequest;
import com.hp.it.spf.xa.exception.portlet.BusinessException;

/**
 * A business exception used in <code>config</code> mode of the
 * <code>html-viewer</code> portlet when there has been an administrator
 * data-entry error.
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
	 * Construction method which provides default error code.
	 * 
	 */
	public InputErrorException(PortletRequest pRequest) {
		super(pRequest, DEFAULT_ERROR);
	}

	/**
	 * Construction method which provides the given error code.
	 * 
	 * @param errorCode -
	 *            Error code
	 * @param errorMsg -
	 *            Error message
	 */
	public InputErrorException(PortletRequest pRequest, String errorCode) {
		super(errorCode);
	}
}