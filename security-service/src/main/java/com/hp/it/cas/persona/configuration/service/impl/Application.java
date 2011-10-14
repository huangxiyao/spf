package com.hp.it.cas.persona.configuration.service.impl;

import com.hp.it.cas.config.dao.App;
import com.hp.it.cas.persona.configuration.service.IApplication;

class Application implements IApplication {

	private final int applicationPortfolioIdentifier;
	private final String applicationAliasName;
	private final String applicationDescription;
	
	Application(App app) {
		this.applicationPortfolioIdentifier = app.getKey().getAppPrtflId().intValue();
		this.applicationAliasName = app.getAppAliasNm();
		this.applicationDescription = app.getAppDn();
	}
	
	public String getApplicationAliasName() {
		return applicationAliasName;
	}

	public String getApplicationDescription() {
		return applicationDescription;
	}

	public int getApplicationPortfolioIdentifier() {
		return applicationPortfolioIdentifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + applicationPortfolioIdentifier;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Application other = (Application) obj;
		if (applicationPortfolioIdentifier != other.applicationPortfolioIdentifier)
			return false;
		return true;
	}

	public String toString() {
		return String.format("%s[%s: %s]", getClass().getSimpleName(), applicationPortfolioIdentifier, applicationAliasName);
	}
}
