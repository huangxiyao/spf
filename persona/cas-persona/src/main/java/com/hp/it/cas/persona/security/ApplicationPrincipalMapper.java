package com.hp.it.cas.persona.security;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.ldap.LdapUserDetailsMapper;

import com.hp.it.cas.xa.security.IApplicationPrincipal;

/**
 * An LDAP user details mapper that produces a UserDetails representing an application (an instance of {@link IApplicationPrincipal}).
 *
 * @author Quintin May
 */
class ApplicationPrincipalMapper extends LdapUserDetailsMapper {

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String userName, GrantedAuthority[] authorities) {
		UserDetails userDetails = super.mapUserFromContext(ctx, userName, authorities);
		return new UserDetailsApplicationPrincipal(userDetails);
	}

	@SuppressWarnings("serial")
	private static class UserDetailsApplicationPrincipal implements UserDetails, IApplicationPrincipal {

		private static final String APPLICATION_ACCOUNT_PREFIX = "APP-";
		
		private final UserDetails userDetails;
		private final int applicationPortfolioIdentifier;

		UserDetailsApplicationPrincipal(UserDetails userDetails) {
			this.userDetails = userDetails;
			this.applicationPortfolioIdentifier = Integer.parseInt(userDetails.getUsername().substring(APPLICATION_ACCOUNT_PREFIX.length()));
		}
		
		public GrantedAuthority[] getAuthorities() {
			return userDetails.getAuthorities();
		}

		public String getPassword() {
			return userDetails.getPassword();
		}

		public String getUsername() {
			return userDetails.getUsername();
		}

		public boolean isAccountNonExpired() {
			return userDetails.isAccountNonExpired();
		}

		public boolean isAccountNonLocked() {
			return userDetails.isAccountNonLocked();
		}

		public boolean isCredentialsNonExpired() {
			return userDetails.isCredentialsNonExpired();
		}

		public boolean isEnabled() {
			return userDetails.isEnabled();
		}

		public int getApplicationPortfolioIdentifier() {
			return applicationPortfolioIdentifier;
		}

		public String getName() {
			return getUsername();
		}
		
		public String toString() {
			return String.format("%s[%s]", getClass().getSimpleName(), getName());
		}
	}
}
