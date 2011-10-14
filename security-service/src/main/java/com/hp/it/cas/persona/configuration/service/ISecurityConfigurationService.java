package com.hp.it.cas.persona.configuration.service;

import java.util.Set;

import org.springframework.security.annotation.Secured;

/**
 * A service for managing the security configuration of Persona.
 *
 * @author Quintin May
 */
@Secured("IS_AUTHENTICATED_FULLY")
public interface ISecurityConfigurationService {

	/**
	 * Returns the application with the specified identifier.
	 * @param applicationPortfolioIdentifier the application identifier.
	 * @return the application or null.
	 */
	IApplication findApplication(int applicationPortfolioIdentifier);
	
	/**
	 * Adds authorization for the application to have access to the attribute.
	 * @param application the application for which to grant authorization.
	 * @param userAttribute the attribute to which authorization is granted. If the attribute is a simple attribute then the application
	 * can write values for the attribute. If the attribute is a compound attribute then access is granted to all attributes in the compound attribute.
	 * @return the created permission.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	IPermission addPermission(IApplication application, IUserAttribute userAttribute);

	/**
	 * Adds authorization for the application to have access to the attribute.
	 * @param application the application for which to grant authorization.
	 * @param compoundUserAttribute the compound attribute containing the simple attribute or zero (0) if the permission is for a purely simple (top-level) attribute.
	 * @param simpleUserAttribute the simple attribute to which authorization is granted.
	 * @return the created permission.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	IPermission addPermission(IApplication application, IUserAttribute compoundUserAttribute, IUserAttribute simpleUserAttribute);

	/**
	 * Returns all permissions granted to the application.
	 * @param application the application for which permissions are to be found.
	 * @return the permissions or an empty set.
	 */
	Set<IPermission> findPermissions(IApplication application);

	// TODO if time permits findPermission(IApplication, IUserAttribute)
	// TODO if time permits findPermission(IApplication, IUserAttribute, IUserAttribute)
	// TODO if time permits findPermissions(IApplication, IUserattribute compound)
	
	/**
	 * Removes the permission. No action if the permission does not exist.
	 * @param permission the permission to remove.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	void removePermission(IPermission permission);
}
