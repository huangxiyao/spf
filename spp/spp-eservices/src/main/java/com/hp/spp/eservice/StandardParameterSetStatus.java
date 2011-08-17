package com.hp.spp.eservice;


public class StandardParameterSetStatus {

	public StandardParameterSetStatus(StandardParameterSet paramSet) {
		mParamSet = paramSet;
	}

	public final static int NEW_PARAM_SET = 0;

	public final static int EXISTING_PARAM_SET = 1;

	private StandardParameterSet mParamSet;

	private int existingFlag;

	public int getExistingFlag() {
		return existingFlag;
	}

	public void setExistingFlag(int existingFlag) {
		this.existingFlag = existingFlag;
	}

	public StandardParameterSet getStandardParameterSet() {
		return mParamSet;
	}

	public void setStandardParameterSet(StandardParameterSet paramSet) {
		mParamSet = paramSet;
	}
}
