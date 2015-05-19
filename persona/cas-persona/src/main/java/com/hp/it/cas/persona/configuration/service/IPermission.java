package com.hp.it.cas.persona.configuration.service;

/**
 * A permission is a mapping between applications and the attributes the applications are permitted to manipulate.
 * Permissions can be granted for simple attributes, specific attributes in a compound attribute, or all attributes in a compound attribute.
 * 
 * @author Quintin May
 */
public interface IPermission {
	
	/**
	 * Returns the application for which this permission grants authorization.
	 * @return the application.
	 */
	IApplication getApplication();
	
	/**
	 * Returns the compound attribute for which this permission grants authorization.
	 * @return the compound attribute or null if the permission is for a simple attribute.
	 */
	IUserAttribute getCompoundUserAttribute();
	
	/**
	 * Returns the simple attribute for which this permission grants authorization.
	 * @return the simple attribute or null if the permission is for all attributes in a compound attribute.
	 */
	IUserAttribute getSimpleUserAttribute();
}
