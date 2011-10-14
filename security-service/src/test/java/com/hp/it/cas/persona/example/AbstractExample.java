package com.hp.it.cas.persona.example;

import java.security.Principal;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.providers.preauth.PreAuthenticatedAuthenticationToken;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService;
import com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.user.service.IUserService;

abstract class AbstractExample{

	private static final int CLIENT_APPLICATION_PORTFOLIO_IDENTIFIER = 999999;

	private static final Authentication ADMIN_USER =
		new PreAuthenticatedAuthenticationToken(new UserPrincipal("Administrator"), "secret", new GrantedAuthority[] {new GrantedAuthorityImpl("ROLE_ADMIN-PERSONA")});

	protected static final Authentication USER =
		new PreAuthenticatedAuthenticationToken(new UserPrincipal("User"), "secret", new GrantedAuthority[0]);

	public static final String TITLE = "TITLE";
	public static final String NAME = "NM";
	public static final String GIVEN_NAME = "GVN_NM";
	public static final String FAMILY_NAME = "FMLY_NM";
	public static final String PET_NAME = "PET_NM";
	public static final String SERVICE_AGREEMENT_IDENTIFIER = "SAID";

	private IMetadataConfigurationService metadataConfigurationService;
	private ISecurityConfigurationService securityConfigurationService;
	protected IUserService userService;

	public void setMetadataConfigurationService(IMetadataConfigurationService metadataConfigurationService) {
		this.metadataConfigurationService = metadataConfigurationService;
	}

	public void setSecurityConfigurationService(ISecurityConfigurationService securityConfigurationService) {
		this.securityConfigurationService = securityConfigurationService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	protected void establishSecurityContext(Authentication token) {
		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(token);
		SecurityContextHolder.setContext(context);
	}

	protected void configure() {

		establishSecurityContext(ADMIN_USER);

		// define attributes
		IUserAttribute title		= metadataConfigurationService.addSimpleUserAttribute(null, TITLE, "A formal salutation", "Title definition");
		IUserAttribute name			= metadataConfigurationService.addCompoundUserAttribute(null, NAME, "What a person is called", "Name definition");
		IUserAttribute givenName	= metadataConfigurationService.addSimpleUserAttribute(null, GIVEN_NAME, "A person's first name", "Given name definition");
		IUserAttribute familyName	= metadataConfigurationService.addSimpleUserAttribute(null, FAMILY_NAME, "A person's last name", "Family name definition");
		IUserAttribute petName		= metadataConfigurationService.addSimpleUserAttribute(null, PET_NAME, "An animal's name", "Pet name definition");
		IUserAttribute serviceAgreementIdentifier = metadataConfigurationService.addSimpleUserAttribute(null, SERVICE_AGREEMENT_IDENTIFIER, "Service agreement identifier", "SAID definition");

		// define members of compound attributes
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(name, givenName);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(name, familyName);

		// define security
		IApplication application = securityConfigurationService.findApplication(CLIENT_APPLICATION_PORTFOLIO_IDENTIFIER);
		securityConfigurationService.addPermission(application, title);
		securityConfigurationService.addPermission(application, name);
		securityConfigurationService.addPermission(application, petName);
		securityConfigurationService.addPermission(application, serviceAgreementIdentifier);
	}

	private static class UserPrincipal implements Principal {

		private final String name;

		UserPrincipal(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
