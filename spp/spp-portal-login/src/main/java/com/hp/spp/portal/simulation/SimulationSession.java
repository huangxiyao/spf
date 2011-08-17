package com.hp.spp.portal.simulation;

public class SimulationSession {
	private String mSite;
	private String mHppIdSimulator;
	private String mHppIdSimulated;
	
	
	public SimulationSession(String site, String hppIdSimulator, String hppIdSimulated) {
		super();
		mSite = site;
		mHppIdSimulator = hppIdSimulator;
		mHppIdSimulated = hppIdSimulated;
	}
	
	public String getHppIdSimulated() {
		return mHppIdSimulated;
	}
	public void setHppIdSimulated(String hppIdSimulated) {
		mHppIdSimulated = hppIdSimulated;
	}
	public String getHppIdSimulator() {
		return mHppIdSimulator;
	}
	public void setHppIdSimulator(String hppIdSimulator) {
		mHppIdSimulator = hppIdSimulator;
	}
	public String getSite() {
		return mSite;
	}
	public void setSite(String site) {
		mSite = site;
	}
	
	
}
