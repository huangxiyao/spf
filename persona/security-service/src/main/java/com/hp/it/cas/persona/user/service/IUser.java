package com.hp.it.cas.persona.user.service;

import java.util.Collection;
import java.util.Map;

import com.hp.it.cas.persona.uav.service.IUserIdentifier;

/**
 * A user is the central object for which the Persona User API manages information.
 * 
 * @author Quintin May
 */
public interface IUser {

	/**
	 * Returns the unique identifier of the user.
	 * 
	 * @return the identifier specified during creation of this user.
	 */
	IUserIdentifier getIdentifier();

	/**
	 * Returns the user's simple string attribute values.
	 * @return a map keyed by simpleUserAttributeIdentifiers. The value of each map entry is the list of values stored with the simpleUserAttributeIdentifier.
	 */
	Map<String, Collection<String>> getSimpleAttributeValues();
	
	/**
	 * Returns the user's compound user attribute values.
	 * @return a map keyed by compoundUserAttributeIdentifiers. The value of each map entry is the list of values stored with the compoundUserAttributeIdentifier.
	 */
	Map<String, Collection<ICompoundUserAttributeValue>> getCompoundAttributeValues();
	
	/**
	 * Adds a new simple attribute value.
	 * @param simpleUserAttributeIdentifier the key at which to store the value.
	 * @param value the attribute value.
	 */
	void addSimpleAttributeValue(String simpleUserAttributeIdentifier, String value);
	
	/**
	 * Adds a new compound attribute value.
	 * @param compoundUserAttributeIdentifier the key at which to store the value.
	 * @return the newly created compound attribute value.
	 */
	ICompoundUserAttributeValue addCompoundAttributeValue(String compoundUserAttributeIdentifier);
	
	/**
	 * Removes the user and all associated attribute values from the data store.
	 */
	void remove();
}
