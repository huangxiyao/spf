/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.exception.portlet;

import com.hp.it.spf.xa.exception.portlet.SPFException;

/**
 * <p>
 * This class (or a subclass of it) is used to represent a business exception in
 * the Shared Portal Framework. A business exception is an error condition
 * resulting from circumstances under the user's control and generally the
 * user's fault: invalid user input as determined by business logic, for
 * example. Contrast this with system exceptions, which are internal system
 * problems outside of the user's responsibility and generally not reparable by
 * the user.
 * </p>
 * <p>
 * An SPF BusinessException contains an error code, an error message, and
 * optionally some other Exception object.
 * </p>
 * 
 * @author sunnyee
 * @version TBD
 */
public class BusinessException extends SPFException {

	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- Constructors

	public BusinessException(String errorCode) {
		super(errorCode, null);
	}

	public BusinessException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public BusinessException(String errorCode, Exception exception) {
		super(errorCode, exception, null);
	}

	public BusinessException(String errorCode, Exception exception,
			String errorMessage) {
		super(errorCode, exception, errorMessage);
	}

}
