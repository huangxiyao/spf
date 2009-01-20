package com.hp.it.spf.user.exception;

/**
 * UGS webservice exception wrapper
 * 
 * @author  wuyingzh
 * @version 1.0
 */
public class UserGroupsException extends Exception {
	private static final long serialVersionUID = 345532829912627537L;

	public UserGroupsException(String message) {
		super(message);
	}

	public UserGroupsException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public UserGroupsException(Throwable throwable) {
		super(throwable);
	}
}
