package com.hp.spp.wsrp.export.exception;

public class ExportException extends CommonException {

	private static final long serialVersionUID = 2471001193893699180L;

	public ExportException() {
	}

	public ExportException(String message) {
		super(message);
	}

	public ExportException(Throwable cause) {
		super(cause);
	}

	public ExportException(String message, Throwable cause) {
		super(message, cause);
	}

}
