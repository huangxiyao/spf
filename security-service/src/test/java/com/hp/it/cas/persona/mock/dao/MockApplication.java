package com.hp.it.cas.persona.mock.dao;

import com.hp.it.cas.persona.configuration.service.IApplication;

class MockApplication implements IApplication {

	private final int applicationPortfolioIdentifier;
	
	MockApplication(int applicationPortfolioIdentifier) {
		this.applicationPortfolioIdentifier = applicationPortfolioIdentifier;
	}
	
	public int getApplicationPortfolioIdentifier() {
		return applicationPortfolioIdentifier;
	}

	public String getApplicationDescription() {
		return "Application description";
	}

	public String getApplicationAliasName() {
		return "Application alias name";
	}

}
