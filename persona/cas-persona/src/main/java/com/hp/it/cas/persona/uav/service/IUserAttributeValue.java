package com.hp.it.cas.persona.uav.service;

/**
 * A triple that stores the user, attribute, and value of the attribute.
 *
 * @author Quintin May
 */
public interface IUserAttributeValue {
	
	/**
	 * Returns the identifier of the user to whom the value is assigned.
	 * @return the user identifier.
	 */
	IUserIdentifier getUserIdentifier();
	
	/**
	 * Returns the attribute that defines the relationship between the user and the value.
	 * @return the attribute.
	 */
	IUserAttribute getUserAttribute();
	
	/**
	 * Returns the instance identifier for a value. The instance identifier is relevant only if the value is
	 * part of a compound structure. It uniquely identifies the instance of the structure. All UserAttributeValues having the
	 * same instance identifier are part of the same structure. For example, in an <em>address</em> structure the <em>city</em> and
	 * <em>state</em> will have the same instance identifier. If the user has multiple addresses, then each address structure will have
	 * a unique instance identifier.
	 * @return the instance identifier.
	 */
	String getInstanceIdentifier();
	
	/**
	 * Returns the value stored by this user/attribute relationship.
	 * @return the value.
	 */
	String getValue();
	
}
