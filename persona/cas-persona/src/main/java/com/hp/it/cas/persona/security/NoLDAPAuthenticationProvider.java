package com.hp.it.cas.persona.security;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.UserDetails;

import com.hp.it.cas.xa.security.IApplicationPrincipal;

public class NoLDAPAuthenticationProvider implements AuthenticationProvider {
	
	private static final GrantedAuthority[] APP_AUTHORITIES = new GrantedAuthority[] {new GrantedAuthorityImpl("ROLE_APP-PERSONA")};
	
	public Authentication authenticate(Authentication request) throws AuthenticationException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(new UserDetailsApplicationPrincipal(
                (String) request.getPrincipal(), (String) request.getCredentials(), APP_AUTHORITIES),
                request.getCredentials(), APP_AUTHORITIES);
        return authentication;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@SuppressWarnings("serial")
    private static class UserDetailsApplicationPrincipal implements UserDetails, IApplicationPrincipal {

        private static final String APPLICATION_ACCOUNT_PREFIX = "APP-";
        
        private String username;
        private String password;
        private GrantedAuthority[] authorities;
        private final int applicationPortfolioIdentifier;
        
        public UserDetailsApplicationPrincipal(String username, String password, GrantedAuthority[] authorities) {
            this.username = username;
            this.password = password;
            this.authorities=authorities;
            this.applicationPortfolioIdentifier = Integer.parseInt(username.substring(APPLICATION_ACCOUNT_PREFIX.length()));
        }
        
        public GrantedAuthority[] getAuthorities() {
            return this.authorities;
        }

        public String getPassword() {
            return this.password;
        }

        public String getUsername() {
            return this.username;
        }

        public boolean isAccountNonExpired() {
            return true;
        }

        public boolean isAccountNonLocked() {
            return true;
        }

        public boolean isCredentialsNonExpired() {
            return true;
        }

        public boolean isEnabled() {
            return true;
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
