package com.hp.it.cas.persona.security;

import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.populator.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.providers.ldap.LdapAuthenticationProvider;
import org.springframework.security.providers.ldap.LdapAuthenticator;
import org.springframework.security.providers.ldap.authenticator.BindAuthenticator;

/**
 * An authentication provider that authenticates applications against the Enterprise Directory.
 *
 * @author Quintin May
 */
public class ApplicationAuthenticationProvider extends LdapAuthenticationProvider {

	private static final String SECURE_ENTERPRISE_DIRECTORY	= "ldaps://ldap.hp.com:636/o=hp.com";
	private static final String ENTERPRISE_DIRECTORY		= "ldap://ldap.hp.com:389/o=hp.com";
	
	/**
	 * Creates an application authentication provider.
	 * @throws Exception if there is a problem initializing the authentication provider.
	 */
	public ApplicationAuthenticationProvider() throws Exception {
		super(createAuthenticator(), createPopulator());
		setUserDetailsContextMapper(new ApplicationPrincipalMapper());
	}
	
	private static LdapAuthenticator createAuthenticator() throws Exception {
		DefaultSpringSecurityContextSource secureLdap = new DefaultSpringSecurityContextSource(SECURE_ENTERPRISE_DIRECTORY);
		secureLdap.afterPropertiesSet();
		
		BindAuthenticator authenticator = new BindAuthenticator(secureLdap);
		authenticator.setUserDnPatterns(new String[] {"cn={0},ou=Applications"});
		authenticator.afterPropertiesSet();
		
		return authenticator;
	}
	
	private static LdapAuthoritiesPopulator createPopulator() throws Exception {
		DefaultSpringSecurityContextSource ldap = new DefaultSpringSecurityContextSource(ENTERPRISE_DIRECTORY);
		ldap.afterPropertiesSet();
		
		LdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(ldap, "ou=Groups");
		return populator;
	}
}
