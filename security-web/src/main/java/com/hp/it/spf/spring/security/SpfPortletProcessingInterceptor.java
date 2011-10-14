package com.hp.it.spf.spring.security;

import java.security.Principal;

import javax.portlet.PortletRequest;

import org.springframework.security.authoritymapping.SimpleAttributes2GrantedAuthoritiesMapper;
import org.springframework.security.authoritymapping.SimpleMappableAttributesRetriever;
import org.springframework.security.ui.portlet.PortletPreAuthenticatedAuthenticationDetailsSource;
import org.springframework.security.ui.portlet.PortletProcessingInterceptor;

import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * A portlet processing interceptor that extracts the principal and authorities from an SPF based portlet. This is the only SPF custom class required
 * to perform an SPF portlet to Spring Security integration.
 *
 * @author Quintin May
 */
public class SpfPortletProcessingInterceptor extends PortletProcessingInterceptor {

	/**
	 * Creates a portlet processing interceptor.
	 * @throws Exception 
	 */
	public SpfPortletProcessingInterceptor() throws Exception {
		SpfAuthenticationDetailsSource authenticationDetailsSource = new SpfAuthenticationDetailsSource();
		setAuthenticationDetailsSource(authenticationDetailsSource);
	}
	
	/**
	 * SPF portlets do not have access to the user credentials so this implementation always returns a bogus value.
	 */
	@Override
	protected Object getCredentialsFromRequest(PortletRequest request) {
		return "passwordNotAvailable";
	}

	@Override
	protected Object getPrincipalFromRequest(PortletRequest request) {
		String profileId = (String) Utils.getUserProperty(request, Consts.KEY_PROFILE_ID);
		return new UserPrincipal(profileId);
	}

	/**
	 * A simple user principal.
	 *
	 * @author Quintin May
	 */
	private static class UserPrincipal implements Principal {
		private final String name;
		
		UserPrincipal(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}		

		public String toString() {
			return String.format("%s[%s]", getClass().getSimpleName(), name);
		}
	}
	
	/**
	 * A details source that gets user groups from an SPF portlet request and maps them to Spring Security GrantedAuthorities
	 * by prepending each group name with "ROLE_".
	 *
	 * @author Quintin May
	 */
	private static class SpfAuthenticationDetailsSource extends PortletPreAuthenticatedAuthenticationDetailsSource {

		private static final String[] NO_ROLES = new String[0];
		
		SpfAuthenticationDetailsSource() throws Exception {
			SimpleMappableAttributesRetriever attributesRetriever = new SimpleMappableAttributesRetriever();
			attributesRetriever.setMappableAttributes(NO_ROLES);
			
			setMappableRolesRetriever(attributesRetriever);			
			setUserRoles2GrantedAuthoritiesMapper(new SimpleAttributes2GrantedAuthoritiesMapper());
		}
		
		@Override
		protected String[] getUserRoles(Object context, String[] mappableRoles) {			
			return context instanceof PortletRequest ? Utils.getGroups((PortletRequest) context) : NO_ROLES;
		}
	}
}
