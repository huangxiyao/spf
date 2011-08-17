package com.hp.spp.wsrp.shield;

public class PortalRemoteServerInfo {
	private String mTitle;
	private String mWsdlUrl;

	public PortalRemoteServerInfo(String title, String wsdlUrl) {
		mTitle = title;
		mWsdlUrl = wsdlUrl;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getWsdlUrl() {
		return mWsdlUrl;
	}

	public String toString() {
		return "PortalRemoteServerInfo:[title:" + mTitle + ",wsdlUrl:" + mWsdlUrl + "]";
	}
}
