package com.hp.it.cas.persona.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.RunAsManager;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.util.Assert;

import com.hp.it.cas.xa.logging.StopWatch;
import com.hp.it.cas.xa.security.IApplicationPrincipal;

/**
 * A RunAs manager that supports RUN_AS_APPLICATION configuration attributes. Substitutes an authenticated application
 * as the authentication principal and leaves the original authentication authorities in place.
 * 
 * @author Quintin May
 */
public class RunAsApplicationManager implements RunAsManager, InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(RunAsApplicationManager.class);
	
	private String key;
	private IApplicationPrincipal applicationPrincipal;
	private String applicationPassword;
	private AuthenticationManager authenticationManager;
	
	private Authentication applicationAuthentication;
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(key, "A key is required and should match that configured for the RunAsImplAuthenticationProvider.");
		Assert.notNull(applicationPrincipal, "An application principal is required.");
		Assert.notNull(applicationPassword, "An application password is required.");
		Assert.notNull(authenticationManager, "An authentication manager is required.");
	}

	public Authentication buildRunAs(Authentication authentication, Object securedObject, ConfigAttributeDefinition configAttributeDefinition) {
		Authentication runAsAuthentication = null;
		
		// if the authentication is already an application, there is nothing to do
		if (! (authentication.getPrincipal() instanceof IApplicationPrincipal)) {
			if (applicationAuthentication == null) {
				StopWatch sw = new StopWatch().start();
				
				applicationAuthentication = new UsernamePasswordAuthenticationToken(applicationPrincipal, applicationPassword);
				applicationAuthentication = authenticationManager.authenticate(applicationAuthentication);
				
				logger.debug("Authenticate {} {}", applicationPrincipal, sw);
			}
			
			if (applicationAuthentication.isAuthenticated()) {
				runAsAuthentication = new RunAsApplicationToken(
						key,
						applicationAuthentication.getPrincipal(),
						applicationAuthentication.getCredentials(),
						authentication.getAuthorities(),
						authentication.getClass(),
						authentication);					
			}
		}
		
		return runAsAuthentication;
	}

	public boolean supports(ConfigAttribute attribute) {
		return "RUN_AS_APPLICATION".equals(attribute.getAttribute());
	}

	/**
	 * This implementation supports any secured object type, so always returns true.
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return true;
	}

	/**
	 * Sets the shared-secret used to produce the returned RunAs authentication. The key must have the same value as that used by
	 * {@link org.springframework.security.runas.RunAsImplAuthenticationProvider}.
	 * @param key the shared secret.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Sets the authentication manager that will be used to authenticate the RunAs application authentication. The application authentication will be
	 * performed only once and cached for future use.
	 * @param authenticationManager the authentication manager that will authenticate the substituted application.
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	/**
	 * Sets the principal that will appear in the RunAs authentication.
	 * @param applicationPrincipal an application principal.
	 */
	public void setApplicationPrincipal(IApplicationPrincipal applicationPrincipal) {
		this.applicationPrincipal = applicationPrincipal;
	}
	
	/**
	 * Sets the credentials that will be used to authenticate the principal.
	 * @param applicationPassword the application password.
	 */
	public void setApplicationPassword(String applicationPassword) {
		this.applicationPassword = applicationPassword;
	}
}
