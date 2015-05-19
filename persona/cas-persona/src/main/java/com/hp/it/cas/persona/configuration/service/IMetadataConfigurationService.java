package com.hp.it.cas.persona.configuration.service;

import java.util.Set;

import org.springframework.security.annotation.Secured;

/**
 * A service for managing the metadata configuration of Persona.
 *
 * @author Quintin May
 */
@Secured("IS_AUTHENTICATED_FULLY")
public interface IMetadataConfigurationService {
	
	/**
	 * Returns all of the defined simple user attributes.
	 * @return the set of all simple user attributes or an empty set.
	 */
	Set<IUserAttribute> getSimpleUserAttributes();

	/**
	 * Returns all of the defined compound user attributes.
	 * @return the set of all compound user attributes or an empty set.
	 */
	Set<IUserAttribute> getCompoundUserAttributes();
	
	/**
	 * Finds the user attribute having the specified key.
	 * @param userAttributeIdentifier the attribute key.
	 * @return the simple or compound attribute with the specified key or null.
	 */
	IUserAttribute findUserAttribute(String userAttributeIdentifier);
	
	/**
	 * Returns all user attributes having the specified name.
	 * @param userAttributeName the attribute name.
	 * @return a set of matching attributes or an empty set. In general, the set will have only a single value, but it could have more.
	 */
	IUserAttribute findUserAttributeByName(String userAttributeName);
	
	/**
	 * Creates a new simple user attribute.
	 * @param userAttributeIdentifier TODO
	 * @param userAttributeName the attribute name. The name is a label for humans, not a key.
	 * @param userAttributeDescription the attribute description.
	 * @param userAttributeDefinition the verbose attribute definition.
	 * @return the new user attribute.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	IUserAttribute addSimpleUserAttribute(String userAttributeIdentifier, String userAttributeName, String userAttributeDescription, String userAttributeDefinition);
	
	/**
	 * Creates a new compound user attribute.
	 * @param userAttributeIdentifier TODO
	 * @param userAttributeName the attribute name. The name is a label for humans, not a key.
	 * @param userAttributeDescription the attribute description.
	 * @param userAttributeDefinition the verbose attribute definition.
	 * @return the new user attribute.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	IUserAttribute addCompoundUserAttribute(String userAttributeIdentifier, String userAttributeName, String userAttributeDescription, String userAttributeDefinition);
	
	/**
	 * Updates the user attribute.
	 * @param userAttribute the user attribute.
	 * @return the user attribute. This may not be the same object passed as the argument. The returned value should be used for further operations.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	IUserAttribute putUserAttribute(IUserAttribute userAttribute);
	
	/**
	 * Removes the user attribute.
	 * @param userAttribute the attribute to remove.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	void removeUserAttribute(IUserAttribute userAttribute);
	
	/**
	 * Returns the simple attributes that are allowed to appear within a structure defined by the compound attribute.
	 * @param compoundUserAttribute the compound attribute whose members are to be found.
	 * @return the simple attributes defined as members of the compound attribute or an empty set.
	 */
	Set<IUserAttribute> findSimpleAttributes(IUserAttribute compoundUserAttribute);

	/**
	 * Returns all of the compound attributes for which the simple attribute has been defined to be a member.
	 * @param simpleUserAttribute the simple attribute whose containing compound attributes are to be found.
	 * @return the compound attributes that can contain the simple attribute or an empty set.
	 */
	Set<IUserAttribute> findCompoundAttributes(IUserAttribute simpleUserAttribute);
	
	/**
	 * Defines a simple attribute as being permitted to appear as a member of a compound attribute.
	 * @param compoundUserAttribute the compound attribute for which the simple attribute will be a member.
	 * @param simpleUserAttribute the simple attribute to add as a member of the compound attribute.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	void addCompoundAttributeSimpleAttribute(IUserAttribute compoundUserAttribute, IUserAttribute simpleUserAttribute);
	
	/**
	 * Disassociates a simple attribte from a compound attribute. The simple attribute will no longer be permitted to appear as a member of the compound attribute.
	 * @param compoundUserAttribute the compound attribute from which to remove the simple attribute.
	 * @param simpleUserAttribute the simple attribute to remove from the compound attribute.
	 */
	@Secured("ROLE_ADMIN-PERSONA")
	void removeCompoundAttributeSimpleAttribute(IUserAttribute compoundUserAttribute, IUserAttribute simpleUserAttribute);
}
