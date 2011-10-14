package com.hp.it.cas.persona.configuration.service;

/**
 * An attribute is an identifier used to name information related to a user.
 *
 * @author Quintin May
 */
public interface IUserAttribute {
	
	/**
	 * Returns the unique, system-generated value used to identify the attribute.
	 * @return the key.
	 */
	String getUserAttributeIdentifier();
	
	/**
	 * Returns the name of the attribute. The name is appropriate for human consumption. Note that this value is subject to change and must not be used
	 * as an identifier.
	 * @return the name.
	 */
	String getUserAttributeName();
	
	/**
	 * Sets the name.
	 * @param userAttributeName the attribute name.
	 */
	void setUserAttributeName(String userAttributeName);
	
	/**
	 * Returns the attribute description.
	 * @return the description.
	 */
	String getUserAttributeDescription();
	
	/**
	 * Sets the attribute description.
	 * @param userAttributeDescription the description.
	 */
	void setUserAttributeDescription(String userAttributeDescription);
	
	/**
	 * Returns the attribute definition. This value can be extremely long and detailed and is meant for an in depth definition of the attribute and its use.
	 * @return the definition.
	 */
	String getUserAttributeDefinition();
	
	/**
	 * Sets the attribute definition.
	 * @param userAttributeDefinition the definition.
	 */
	void setUserAttributeDefinition(String userAttributeDefinition);

	/**
	 * Returns an indication as to whether the attribute is a simple attribute (the attribute is used to name a string value).
	 * @return true if the attribute is simple, false if the attribute is compound.
	 */
	boolean isSimpleUserAttribute();
	
	/**
	 * Returns an indication as to whether the attribute is a compound attribute (the attribute is used to name a compound value).
	 * @return true if the attribute is compound, false if the attribute is simple.
	 */
	boolean isCompoundUserAttribute();
}
