package com.hp.it.cas.persona.configuration.service;


/**
 * An application. Security in Persona is based on applications and predicates.
 *
 * @author Quintin May
 */
public interface IApplication {
	
	/**
	 * Returns the unique application identifier.
	 * @return the application identifier.
	 */
	int getApplicationPortfolioIdentifier();
	
	/**
	 * Returns the friendly display name of the application.
	 * @return the name.
	 */
	String getApplicationAliasName();
	
	/**
	 * Returns a description about the purpose of the application.
	 * @return the description.
	 */
	String getApplicationDescription();
}
