package com.hp.it.spf.xa.dc.portal;

/**
 * Error codes which can be reported using {@link com.hp.it.spf.xa.dc.portal.DiagnosticContext}.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public enum ErrorCode {
	/**
	 * Request processing generic error.
	 */
	REQUEST001("Request processing generic error"),
	/**
	 * Session initialization generic error.
	 */
	AUTH001("Session initialization generic error"),
	/**
	 * User Profile Service generic invocation error.
	 */
	PROFILE001("User Profile Service generic error"),
	/**
	 * User Group Service generic invocation error.
	 */
	GROUPS001("User Group Service (UGS) generic error"),
	/**
	 * User Group Service error - site not found.
	 */
	GROUPS002("User Group Service - site not found"),
	/**
	 * WSRP invocation generic error.
	 */
	WSRP001("WSRP generic error")
	;

	/**
	 * This description is here only to ensure that the developers extending this class will provide
	 * some explanation about the error. While the JavaDoc could be enough, there is no guarantee
	 * that it will be provided.
	 */
	private String mDescription;

	ErrorCode(String description) {
		mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}
}
