package com.hp.it.cas.persona.mock.dao;

import javax.naming.AuthenticationException;

import com.hp.it.cas.xa.security.ApplicationPrincipal;
import com.hp.it.cas.xa.security.IApplicationAuthenticator;
import com.hp.it.cas.xa.security.IApplicationPrincipal;

public class MockApplicationAuthenticator implements IApplicationAuthenticator {

	private final IApplicationPrincipal principal;
	
	public MockApplicationAuthenticator(int applicationPortfolioIdentifier, String password) {
		this.principal = new ApplicationPrincipal(applicationPortfolioIdentifier);
	}
	
	public void authenticate() throws AuthenticationException {
	}

	public int getApplicationPortfolioIdentifier() {
		return principal.getApplicationPortfolioIdentifier();
	}

	public IApplicationPrincipal getPrincipal() {
		return principal;
	}

}
