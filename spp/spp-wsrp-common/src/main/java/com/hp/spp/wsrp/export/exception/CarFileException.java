package com.hp.spp.wsrp.export.exception;

public class CarFileException extends Exception {

	private static final long serialVersionUID = -3666200630547730693L;

	public CarFileException() {
	}

	public CarFileException(String message) {
		super(message);
	}

	public CarFileException(Throwable cause) {
		super(cause);
	}

	public CarFileException(String message, Throwable cause) {
		super(message, cause);
	}

}
