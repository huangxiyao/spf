package com.hp.it.cas.persona.uav.service;

/**
 * A user identifier represents a unique identifier for a user. User identifiers are typically assigned by an authentication system. IUserIdentifier
 * encapsulates the authentication system issued identifier as well as an identifier type in order to allow user
 * identifiers from multiple authentication systems to be stored without concern for identifier collisions.
 * 
 * @author Quintin May
 */
public interface IUserIdentifier {

	/**
	 * Returns the type of identifer. For example, the HP Enterprise Directory stores two types of unique user identifiers: employee numbers and email
	 * addresses.
	 * 
	 * @return the identifier type.
	 */
	EUserIdentifierType getUserIdentifierType();

	/**
	 * Returns the unique (within the type domain) user identifier value.
	 * 
	 * @return the identifier value.
	 */
	String getUserIdentifier();
}
