/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.htmlviewer.portlet.exception;

import javax.portlet.PortletRequest;

import com.hp.it.spf.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.exception.portlet.SystemException;

/**
 * A system exception used by the <code>html-viewer</code> portlet code
 * whenever a system error has occurred. For example, when no view filename is
 * configured by the time that the
 * {@link com.hp.it.spf.htmlviewer.portlet.web.ViewController} executes.
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
	public static final String DEFAULT_ERROR = Consts.ERROR_CODE_INTERNAL;

	/**
	 * Construction method which provides default error code.
	 * 
	 */
	public InternalErrorException(PortletRequest pRequest) {
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
	public InternalErrorException(PortletRequest pRequest, String errorCode) {
		super(errorCode);
	}
}