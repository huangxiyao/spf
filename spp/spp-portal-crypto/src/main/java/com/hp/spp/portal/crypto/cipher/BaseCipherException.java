package com.hp.spp.portal.crypto.cipher;

public class BaseCipherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2078871878195929239L;

	public BaseCipherException() {
	}

	public BaseCipherException(String message) {
		super(message);
	}

	public BaseCipherException(Throwable cause) {
		super(cause);
	}

	public BaseCipherException(String message, Throwable cause) {
		super(message, cause);
	}

}
