package com.hp.spp.common.healthcheck;

public class HealthcheckServerInfo {
	private String mServerName;		// web server name
	private String mApplicationName; // Portal or WSRP	
	private String mSiteName;		// Austin 1 or Austin 2
	private	String mServerType;		// primary or secondary
	private boolean mOutOfRotation;	// remove from LB rotation
	
	public HealthcheckServerInfo (String serverName, String applicationName, String serverType, String siteName, boolean outOfRotation) {

		mServerName = serverName;
		mApplicationName = applicationName;
		mServerType = serverType;
		mSiteName = siteName;
		mOutOfRotation = outOfRotation;	
	}

	public HealthcheckServerInfo (String serverName, String applicationName, String serverType, String siteName) {

		mServerName = serverName;
		mApplicationName = applicationName;
		mServerType = serverType;
		mSiteName = siteName;
	}

	public String getServerName() {
		return mServerName;
	}
	
	public String getApplicationName() {
		return mApplicationName;
	}
	
	public String getServerType() {
		return mServerType;
	}
	
	public String getSiteName() {
		return mSiteName;
	}
	
	public boolean getOutOfRotation() {
		return mOutOfRotation;
	}
	
	public void setOutOfRotation(boolean outOfRotation) {
		mOutOfRotation = outOfRotation;
	}
	
	public String toString() {
		return null;
	}
}