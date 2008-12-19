/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.exception.portlet;

/**
 * <p>
 * This abstract class is the base class for Shared Portal Framework portlet
 * exceptions. It has 2 subclasses: BusinessException for user-related errors
 * (ie errors triggered by invalid data submitted by the user and thus
 * correctable by the user) and SystemException for internal errors (ie system
 * problems not the user's fault). Feature services can extend or use this class
 * hierarchy for their own exception management, if they prefer. SPF exceptions
 * contain an error code, error message, and optionally another Exception
 * object.
 * </p>
 * 
 * @author sunnyee
 * @version TBD
 * @see com.hp.it.spf.xa.exception.portlet.BusinessException
 *      com.hp.it.spf.xa.exception.portlet.SystemException
 */
public abstract class SPFException extends Exception {

	// -------------------------------------------------------- Private members

	private String errorCode = null;
	private Exception exception = null;
	private String errorMessage = null;

	// ----------------------------------------------------------- Constructors

	protected SPFException(String errorCode, String errorMessage) {
		super("Error code: " + errorCode + "; Error message: " + errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

	}

	protected SPFException(String errorCode, Exception exception,
			String errorMessage) {
		super("Error code: " + errorCode + "; Error message: " + errorMessage
				+ "; Exception: " + exception, exception);
		this.errorCode = errorCode;
		this.exception = exception;
		this.errorMessage = errorMessage;

	}

	// --------------------------------------------------------- Public methods

	public String getErrorCode() {
		return this.errorCode;
	}

	public Exception getException() {
		return this.exception;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
