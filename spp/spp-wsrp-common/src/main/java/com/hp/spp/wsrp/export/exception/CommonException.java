package com.hp.spp.wsrp.export.exception;

public class CommonException extends Exception {

	private static final long serialVersionUID = 2471001193893699180L;

	public CommonException() {
	}

	public CommonException(String message) {
		super(message);
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

}
