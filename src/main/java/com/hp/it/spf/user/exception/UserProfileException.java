package com.hp.it.spf.user.exception;

/**
 * UPS webservice exception wrapper
 * 
 * @author wuyingzh
 * @version 1.0
 */
public class UserProfileException extends Exception {

	private static final long serialVersionUID = 345532829912647547L;

	public UserProfileException(String message) {
		super(message);
	}

	public UserProfileException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public UserProfileException(Throwable throwable) {
		super(throwable);
	}
}
