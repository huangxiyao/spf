package com.hp.it.spf.xa.log.portal;

/**
 * Enumeration type used to capture the name of the operations for which we record start, end and error
 * times.
 */
public enum Operation {
	REQUEST("Overall request"),
	PROFILE_CALL("User profile retrieval"),
	GROUPS_CALL("User groups retrieval from User Group Service"),
	WSRP_CALL("Longest running WSRP portlet");


	private final String mDescription;

	private Operation(String description) {
		mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}
}
