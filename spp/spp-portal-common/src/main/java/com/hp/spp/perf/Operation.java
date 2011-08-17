package com.hp.spp.perf;

/**
 * Enumeration type used to capture the name of the operation for which we record start, end and error
 * times.
 */
public enum Operation {
	REQUEST("Overall request"),
	UGS_CALL("Web service call to User Group Manager"),
	ESM_CALL("Web service call to E-service Manager"),
	COMPUTE_GROUPS_INC_UGS("Calculation of user groups including call to UGS web service"),
	UPS_CALL("Web service call to User Profile Service"),
	SESSION_CHECK("Verification if the session exists and is correct for the current request"),
	SIMPLE_SESSION("Calculation of a simple user session"),
	COMPLEX_SESSION("Calculation of a complex user session"),
	WEB_CONNECTOR("Web connector HTTP call to the remote web site"),
	STOP_SIMULATION("Simulation stop process"),
	START_SIMULATION("Simulation start process"),
	WSRP_PROFILE("Injection of user profile and context keys into WSRP SOAP message"),
	WSRP_CALL("Web service call to WSRP portlet");


	private final String mDescription;

	private Operation(String description) {
		mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}
}
