/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.exception;

/**
 * Thrown when an error occurs while retrieving user profile. 
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UserProfileException extends RuntimeException {

	private static final long serialVersionUID = 345532829912647547L;

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message.
     * @see Exception#Exception()
     */
	public UserProfileException(String message) {
		super(message);
	}

    /**
     * Constructs a new exception with the specified detail message and cause. 
     * 
     * @param message the detail message.
     * @param throwable the cause.
     * @see Exception#Exception(String, Throwable)
     */
	public UserProfileException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param throwable the cause.
     * @see Exception#Exception()
     */
	public UserProfileException(Throwable throwable) {
		super(throwable);
	}
}
