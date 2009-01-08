package com.sun.portal.portletcontainer.admin.database.exception;

public class PortletRegistryDBException extends RuntimeException {
	/**
	 * DB exception when using jpa
	 */
	private static final long serialVersionUID = 1L;

	public PortletRegistryDBException(String message) {
		super(message);
	}
	
	public PortletRegistryDBException(String message, Throwable ta) {
		super(message, ta);
	}
}
