package com.hp.spp.eservice;

import java.io.Serializable;


public class EServiceStatus implements Serializable {

	public EServiceStatus(EService eService) {
		mEService = eService;
	}

	public final static int NEW_ESERVICE = 0;

	public final static int EXISTING_ESERVICE = 1;

	private EService mEService;

	private int existingFlag;

	public int getExistingFlag() {
		return existingFlag;
	}

	public void setExistingFlag(int existingFlag) {
		this.existingFlag = existingFlag;
	}

	public EService getEService() {
		return mEService;
	}

	public void setEService(EService eService) {
		mEService = eService;
	}
}
