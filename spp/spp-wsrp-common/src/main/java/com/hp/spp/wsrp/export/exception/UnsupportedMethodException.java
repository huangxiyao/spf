package com.hp.spp.wsrp.export.exception;

public class UnsupportedMethodException extends RuntimeException {

	private static final long serialVersionUID = 4612591171219548024L;

	public UnsupportedMethodException() {
	}

	public UnsupportedMethodException(String message) {
		super(message);
	}

	public UnsupportedMethodException(Throwable cause) {
		super(cause);
	}

	public UnsupportedMethodException(String message, Throwable cause) {
		super(message, cause);
	}

}
