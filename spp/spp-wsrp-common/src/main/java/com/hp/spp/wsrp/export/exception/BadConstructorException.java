package com.hp.spp.wsrp.export.exception;

public class BadConstructorException extends Exception {

	private static final long serialVersionUID = -1616066494374394705L;

	public BadConstructorException() {
	}

	public BadConstructorException(String message) {
		super(message);
	}

	public BadConstructorException(Throwable cause) {
		super(cause);
	}

	public BadConstructorException(String message, Throwable cause) {
		super(message, cause);
	}

}
