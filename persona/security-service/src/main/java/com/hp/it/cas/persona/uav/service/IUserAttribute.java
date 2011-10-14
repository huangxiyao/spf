package com.hp.it.cas.persona.uav.service;

/**
 * A user attribute is the name used to identify a user profile data element. Attributes can be simple or compound.
 * 
 * <p>
 * A simple attribute identifies data that is not part of a structure. For example <em>email</em> or <em>company</em>.
 * </p>
 * 
 * <p>
 * A compound attribute identifies data that is part of a structure. For example: <em>address.city</em> or <em>name.givenName</em>. For these examples
 * <em>address</em> and <em>name</em> are the compound keys while <em>city</em> and <em>givenName</em> are the simple keys.
 * 
 * @author Quintin May
 */
public interface IUserAttribute {

	/**
	 * Returns the compound user attribute key if this attribute is part of a compound structure.
	 * 
	 * @return the compound user attribute key or null if this is not a compound user attribute.
	 */
	String getCompoundUserAttributeIdentifier();

	/**
	 * Returns the simple user attribute key.
	 * 
	 * @return the simple user attribute key.
	 */
	String getSimpleUserAttributeIdentifier();

	/**
	 * Returns an indication as to whether this attribute is part of a compound structure.
	 * 
	 * @return true if this is a compound user attribute, false otherwise.
	 */
	boolean isCompoundUserAttribute();

	/**
	 * Returns an indication as to whether this attribute is part of a compound structure.
	 * @return false if this is a compound user attribute, true otherwise.
	 */
	boolean isSimpleUserAttribute();
}
