package com.hp.it.cas.persona.mock.security;

import java.security.Principal;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.hp.it.cas.xa.security.ApplicationPrincipal;
import com.hp.it.cas.xa.security.IApplicationPrincipal;
import com.hp.it.cas.xa.security.SimplePrincipal;

public class MockAuthenticationProvider implements AuthenticationProvider {

	private static final String ADMIN_PRINCIPAL_NAME = "AdminUser";
	private static final String USER_PRINCIPAL_NAME = "User";
	private static final int APPLICATION_PORTFOLIO_IDENTIFIER = 999999;
	
	private static final GrantedAuthority[] ADMIN_AUTHORITIES = new GrantedAuthority[] {new GrantedAuthorityImpl("ROLE_ADMIN-PERSONA")};
	private static final GrantedAuthority[] NO_AUTHORITIES = new GrantedAuthority[0];
	
	public Authentication authenticate(Authentication request) throws AuthenticationException {
		Authentication authentication = null;
		String principalName = request.getName();
		
		if (ADMIN_PRINCIPAL_NAME.equals(principalName)) {
			authentication = new UsernamePasswordAuthenticationToken(request.getPrincipal(), request.getCredentials(), ADMIN_AUTHORITIES);
		} else if (USER_PRINCIPAL_NAME.equals(principalName)) {
			authentication = new UsernamePasswordAuthenticationToken(request.getPrincipal(), request.getCredentials(), NO_AUTHORITIES);
		} else if (request.getPrincipal() instanceof IApplicationPrincipal && ((IApplicationPrincipal) request.getPrincipal()).getApplicationPortfolioIdentifier() == APPLICATION_PORTFOLIO_IDENTIFIER) {
			authentication = new UsernamePasswordAuthenticationToken(request.getPrincipal(), request.getCredentials(), NO_AUTHORITIES);
		} else {
			throw new BadCredentialsException(request.getPrincipal() + " cannot be authenticated.");
		}

		return authentication;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public static Authentication getAdminAuthentication() {
		Principal principal = new SimplePrincipal(MockAuthenticationProvider.ADMIN_PRINCIPAL_NAME);
		return new UsernamePasswordAuthenticationToken(principal, "password");
	}

	public static Authentication getUserAuthentication() {
		Principal principal = new SimplePrincipal(MockAuthenticationProvider.USER_PRINCIPAL_NAME);
		return new UsernamePasswordAuthenticationToken(principal, "password");
	}
	
	public static Authentication getApplicationAuthentication() {
		Principal principal = new ApplicationPrincipal(APPLICATION_PORTFOLIO_IDENTIFIER);
		return new UsernamePasswordAuthenticationToken(principal, "password");
	}
	
	public static Authentication getAnonymousAuthentication() {
		Principal principal = new SimplePrincipal("AnonymousUser");
		return new UsernamePasswordAuthenticationToken(principal, "password");
	}

}
