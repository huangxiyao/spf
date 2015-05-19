package com.hp.it.cas.persona.uav.service;

import java.util.Set;

import org.springframework.security.annotation.Secured;

/**
 * A service that provides access to user attributes. This is the entry point to the Persona User Attribute Value API.
 * 
 * @author Quintin May
 */
public interface IUserAttributeValueService {

	/**
	 * Returns all of the profile data available for the user.
	 * 
	 * @param userIdentifier
	 *            the user identifier.
	 * @return all of the profile data or an empty set if no information is known about the specified user.
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	Set<IUserAttributeValue> findUserAttributeValues(IUserIdentifier userIdentifier);

	/**
	 * Adds a new simple attribute value to the user. Equivalent to {@link #putUserAttributeValue(IUserIdentifier, String, String, String, String)
	 * putUserAttributeValue(userIdentifier, null, null, simpleUserAttributeKey, value)}.
	 * 
	 * @param userIdentifier
	 *            the user.
	 * @param simpleUserAttributeIdentifier
	 *            the attribute (relationship name).
	 * @param value
	 *            the value to associate with the user.
	 * @return the newly created attribute value relationship.
	 */
	@Secured({"IS_AUTHENTICATED_FULLY", "RUN_AS_APPLICATION"})
	IUserAttributeValue addUserAttributeValue(IUserIdentifier userIdentifier, String simpleUserAttributeIdentifier, String value);

	/**
	 * Adds a new compound attribute value to the user. This method will always create a new compound attribute instance. Equivalent to
	 * {@link #putUserAttributeValue(IUserIdentifier, String, String, String, String) putUserAttributeValue(userIdentifier, compoundUserAttributeKey, null,
	 * simpleUserAttributeKey, value)}.
	 * 
	 * @param userIdentifier
	 *            the user.
	 * @param compoundUserAttributeIdentifier
	 *            the compound attribute (structure name) into which the data will be stored.
	 * @param simpleUserAttributeIdentifier
	 *            the simple attribute (map key within the structure) at which to store the data.
	 * @param value
	 *            the value to store.
	 * @return the newly created attribute value relationship.
	 */
	@Secured({"IS_AUTHENTICATED_FULLY", "RUN_AS_APPLICATION"})
	IUserAttributeValue addUserAttributeValue(IUserIdentifier userIdentifier, String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier, String value);

	/**
	 * Adds or updates an existing simple or compound attribute value.
	 * 
	 * @param userIdentifier
	 *            the user.
	 * @param compoundUserAttributeIdentifier
	 *            the compound attribute (structure name) into which the data will be stored. Null indicates that this is a simple attribute.
	 * @param instanceIdentifier
	 *            the structure instance to update. A null value will create a new instance.
	 * @param simpleUserAttributeIdentifier
	 *            the simple attribute (map key within a compound structure) at which to store the data.
	 * @param value
	 *            the value to store.
	 * @return the updated or newly created attribute value relationship.
	 */
	@Secured({"IS_AUTHENTICATED_FULLY", "RUN_AS_APPLICATION"})
	IUserAttributeValue putUserAttributeValue(IUserIdentifier userIdentifier, String compoundUserAttributeIdentifier, String instanceIdentifier,
			String simpleUserAttributeIdentifier, String value);

	/**
	 * Removes the relationship from the user.
	 * 
	 * @param userAttributeValue
	 *            the relationship.
	 */
	@Secured({"IS_AUTHENTICATED_FULLY", "RUN_AS_APPLICATION"})
	void remove(IUserAttributeValue userAttributeValue);
}
