package com.hp.spp.wsrp.export.exception;

public class ImportException extends CommonException {

	private static final long serialVersionUID = -3639172254963846763L;

	public ImportException() {
	}

	public ImportException(String message) {
		super(message);
	}

	public ImportException(Throwable cause) {
		super(cause);
	}

	public ImportException(String message, Throwable cause) {
		super(message, cause);
	}
}
