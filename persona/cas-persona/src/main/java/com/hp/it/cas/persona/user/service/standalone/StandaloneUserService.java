package com.hp.it.cas.persona.user.service.standalone;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.IUser;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.cas.xa.security.ApplicationPrincipal;

/**
 * A user service implementation that returns read-only user profiles that can be used independently of a SecurityContext.
 * 
 * @author Quintin May
 */
public class StandaloneUserService implements IUserService {

	private final IUserService userService;
	private final Authentication applicationAuthentication;

	/**
	 * Creates a user service. The service pre-authenticates the application during construction to provide fail-fast behavior in the event that the application
	 * cannot be authenticated.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            the identifier that is used to authentcate the application. This value is prepended with "APP-" prior to authenticating with Enterprise
	 *            Directory.
	 * @param applicationPassword
	 *            the application password.
	 * @param userService
	 *            the base user service used to retrieve user profiles.
	 * @param authenticationManager
	 *            the authentication manager used to authenticate the application.
	 */
	public StandaloneUserService(int applicationPortfolioIdentifier, String applicationPassword, IUserService userService,
			AuthenticationManager authenticationManager) {
		this.userService = userService;

		// Pre-authenticate the application and cache the authentication.
		ApplicationPrincipal applicationPrincipal = new ApplicationPrincipal(applicationPortfolioIdentifier);
		Authentication applicationAuthentication = new UsernamePasswordAuthenticationToken(applicationPrincipal, applicationPassword);
		this.applicationAuthentication = authenticationManager.authenticate(applicationAuthentication);
	}

	public IUser createUser(EUserIdentifierType userIdentifierType, String userIdentifier) {
		IUser user = null;
		Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();

		try {
			SecurityContextHolder.getContext().setAuthentication(applicationAuthentication);
			user = userService.createUser(userIdentifierType, userIdentifier);

			/*
			 * The user returned by UserService is highly optimized. Attribute values are retrieved from the backing datastore at an implementation determined
			 * time and cached. The collections returned by the attribute accessors are lazy and live. Any accesses that require service interaction must be
			 * done with a SecurityContext in place. In order to operate standalone, do a deep copy of the returned user to enable the user object to operate
			 * outside of a SecurityContext.
			 */
			user = new StandaloneUser(user);
		} finally {
			SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
		}

		return user;
	}
}
