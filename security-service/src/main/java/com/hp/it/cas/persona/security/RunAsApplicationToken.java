package com.hp.it.cas.persona.security;

import java.security.Principal;
import java.util.Arrays;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.runas.RunAsUserToken;

/**
 * A RunAsUser token that provides access to the original principal.
 *
 * @author Quintin May
 */
@SuppressWarnings("serial")
public final class RunAsApplicationToken extends RunAsUserToken {

	private final Principal originalPrincipal;
	
	/**
	 * Creates a run-as application token.
	 * @param key the shared secret used to produce the token.
	 * @param principal the new principal.
	 * @param credentials the principal's credentials.
	 * @param authorities the authorities granted.
	 * @param originalAuthentication the class of the original authentication.
	 * @param originalPrincipal the principal for which this token is being substituted.
	 */
	@SuppressWarnings("unchecked")
	RunAsApplicationToken(String key, Object principal, Object credentials, GrantedAuthority[] authorities, Class originalAuthentication, Principal originalPrincipal) {
		super(key, principal, credentials, authorities, originalAuthentication);
		this.originalPrincipal = originalPrincipal;
	}

	/**
	 * Returns the principal of the original authentication.
	 * @return the principal.
	 */
	public Principal getOriginalPrincipal() {
		return originalPrincipal;
	}
	
	public String toString() {
		return String.format("%s[%s, %s]", getClass().getSimpleName(), getPrincipal(), Arrays.asList(getAuthorities()));
	}
}
