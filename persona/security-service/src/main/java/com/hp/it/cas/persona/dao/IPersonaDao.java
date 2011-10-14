package com.hp.it.cas.persona.dao;

import java.util.Set;

import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.UserAttr;

/**
 * A DAO that provides Persona-specific queries.
 *
 * @author Quintin May
 */
public interface IPersonaDao {

	/**
	 * Returns the compound attributes that are defined to have the specified simple attribute as a member.
	 * @param simpleUserAttributeIdentifier the simple attribute key.
	 * @return the compound attributes or an empty set.
	 */
	Set<UserAttr> findCompoundAttributesForSimpleAttribute(String simpleUserAttributeIdentifier);

	/**
	 * Returns the simple attributes that are defined as members of the specified compound attribute.
	 * @param compoundUserAttributeIdentifier the compound attribute whose members are to be found.
	 * @return the simple attributes defined as members of the compound attribute or an empty set.
	 */
	Set<UserAttr> findSimpleAttributesForCompoundAttribute(String compoundUserAttributeIdentifier);

	/**
	 * Returns all defined simple attributes.
	 * @return all simple attributes or an empty set.
	 */
	Set<UserAttr> selectAllSimpleAttributes();

	/**
	 * Returns all defined compound attributes.
	 * @return all compound attributes or an empty set.
	 */
	Set<UserAttr> selectAllCompoundAttributes();
	
	/**
	 * Returns the number of defined compound/simple attributes having the specified compound or simple attribute. Used to enforce
	 * foreign key constraints during an attribute delete operation.
	 * @param userAttrId the key of the compound or simple component of the compound/simple attribute.
	 * @return the number of compound/simple attributes referencing the user attrbute as either the compound or simple component.
	 */
	int countCmpndAttrSmplAttrWithUserAttrId(String userAttrId);
	
	/**
	 * Returns the number of defined user/attribute/values referencing the specified simple attribute. Used to enforce foreign key
	 * constraints during an attribute delete operation.
	 * @param smplUserAttrId the simple user attribute key.
	 * @return the number of user/attribute/values referencing the simple attribute.
	 */
	int countUserAttrValuWithUserAttrId(String smplUserAttrId);
	
	/**
	 * Deletes the attribute permissions that reference the specified attribute (compound or simple).
	 * @param userAttrId the compound or simple attribute for which permissions are to be deleted.
	 * @return the number of permissions deleted.
	 */
	int deleteAppUserAttrPrmsnWithUserAttrId(String userAttrId);

	/**
	 * Deletes compound/simple attribute permissions that reference the specified attribute via the compound or simple component.
	 * @param userAttrId the attribute for which permissions are to be deleted.
	 * @return the number of persmissions deleted.
	 */
	int deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(String userAttrId);

	/**
	 * Returns a count of the user_attr_valu records having the compound/simple attribute. Used for enforcing foreign
	 * key constraints.
	 * @param key the compound/simple attribute key.
	 * @return the number of user_attr_value records having the compound/simple attribute.
	 */
	int countUserAttrValuWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key);

	/**
	 * Deletes the permissions referencing the compound/simple key.
	 * @param key the compound/simple attribute key.
	 * @return the number of records deleted.
	 */
	int deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key);
}
