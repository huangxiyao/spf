/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.exception.portlet;

import com.hp.it.spf.xa.exception.portlet.SPFException;

/**
 * <p>
 * This class (or a subclass of it) is used to represent a system exception in
 * the Shared Portal Framework. A system exception is an internal system problem
 * outside of the user's responsibility and ability to control or fix. Contrast
 * this with a business exception, which is an error condition resulting from
 * circumstances under the user's control and generally the user's fault:
 * invalid user input as determined by business logic, for example.
 * </p>
 * <p>
 * An SPF BusinessException contains an error code, an error message, and
 * optionally some other Exception object.
 * </p>
 * 
 * @author sunnyee
 * @version TBD
 */
public class SystemException extends SPFException {

	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- Constructors

	public SystemException(String errorCode) {
		super(errorCode, null);
	}

	public SystemException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public SystemException(String errorCode, Exception exception) {
		super(errorCode, exception, null);
	}

	public SystemException(String errorCode, Exception exception,
			String errorMessage) {
		super(errorCode, exception, errorMessage);
	}

}
