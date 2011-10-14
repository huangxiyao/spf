package com.hp.it.cas.persona.service;

import java.util.Set;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.web.UserAttribute;

/**
 * The service layer APIs provide the ability to invoke Persona APIs.
 * 
 * @author Kaijian Ding
 */
public interface IPersonaAdminService {

	/**
	 * find a user attribute by user attribute key
	 * 
	 * @param userAttributeId
	 * @return a user attribute
	 * @throws Exception
	 */
	public IUserAttribute findUserAttributeById(String userAttributeId)
			throws Exception;

	/**
	 * find a user attribute by user attribute name
	 * 
	 * @param userAttributeName
	 * @return a user attribute
	 * @throws Exception
	 */
	public IUserAttribute findUserAttributeByName(String userAttributeName)
			throws Exception;

	/**
	 * find a compound user attribute by user attribute key
	 * 
	 * @param userAttributeId
	 * @return a compound user attribute
	 * @throws Exception
	 */
	public IUserAttribute findCompoundUserAttributeById(String userAttributeId)
			throws Exception;

	/**
	 * find a compound user attribute by user attribute name
	 * 
	 * @param userAttributeName
	 * @return a compound user attribute
	 * @throws Exception
	 */
	public IUserAttribute findCompoundUserAttributeByName(
			String userAttributeName) throws Exception;

	/**
	 * find a simple user attribute by user attribute key
	 * 
	 * @param userAttributeId
	 * @return a simple user attribute
	 * @throws Exception
	 */
	public IUserAttribute findSimpleUserAttributeById(String userAttributeId)
			throws Exception;

	/**
	 * find a simple user attribute by user attribute name
	 * 
	 * @param userAttributeName
	 * @return a simple user attribute
	 * @throws Exception
	 */
	public IUserAttribute findSimpleUserAttributeByName(String userAttributeName)
			throws Exception;

	/**
	 * Updates the user attribute.
	 * 
	 * @param userAttributeId
	 *            attribute key value
	 * @param userAttributeName
	 *            attribute name value
	 * @param userAttributeDescription
	 *            attribute description value
	 * @param userAttributeDefinition
	 *            attribute definition value
	 * @return updated user attribute
	 * @throws NumberFormatException
	 *             if user attribute key is an invalid integer number.
	 * @throws IllegalStateException
	 *             if user attribute doesn't exist with the specified key.
	 * @throws Exception
	 *             if other errors raised
	 */
	public IUserAttribute updateUserAttribute(String userAttributeId,
			String userAttributeName, String userAttributeDescription,
			String userAttributeDefinition) throws Exception;

	/**
	 * Create a new compound user attribute.
	 * 
	 * @param userAttributeName
	 *            attribute name value
	 * @param userAttributeDescription
	 *            attribute description value
	 * @param userAttributeDefinition
	 *            attribute definition value
	 * @return a new compound user attribute
	 * @throws Exception
	 *             if errors raised
	 */
	public IUserAttribute createCompoundUserAttribute(String userAttributeId, String userAttributeName,
			String userAttributeDescription, String userAttributeDefinition)
			throws Exception;

	/**
	 * Create a new simple user attribute.
	 * 
	 * @param userAttributeName
	 *            attribute name value
	 * @param userAttributeDescription
	 *            attribute description value
	 * @param userAttributeDefinition
	 *            attribute definition value
	 * @return a new simple user attribute
	 * @throws Exception
	 *             if errors raised
	 */
	public IUserAttribute createSimpleUserAttribute(String userAttributeId, String userAttributeName,
			String userAttributeDescription, String userAttributeDefinition)
			throws Exception;

	/**
	 * Remove the user attribute.
	 * 
	 * @param userAttributeId
	 *            user attribute key value
	 * @throws IllegalStateException
	 *             if user attribute doesn't exist with the specified key.
	 * @throws Exception
	 *             if errors raised
	 */
	public void removeUserAttribute(String userAttributeId) throws Exception;

	/**
	 * list all compound user attibributes ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 */
	public Set<IUserAttribute> listAllCompoundUserAttributes() throws Exception;

	/**
	 * list all simple user attibributes ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 */
	public Set<IUserAttribute> listAllSimpleUserAttributes() throws Exception;

	/**
	 * list all simple user attibributes which are attached to a compound user
	 * attribute, ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 */
	public Set<IUserAttribute> listSimpleUserAttributesForCompound(
			String compoundUserAttributeId) throws Exception;

	/**
	 * list all simple user attibributes which are available to be added to
	 * compound user attribute, ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 */
	public Set<IUserAttribute> listAvailableSimpleUserAttributes(
			String compoundUserAttributeId) throws Exception;

	/**
	 * add simple user attribute to compound user attribute
	 * 
	 * @param simpleUserAttributeId
	 * @param compoundUserAttribute
	 * @throws Exception
	 */
	public void addSimpleUserAttributeToCompound(String simpleUserAttributeId,
			String compoundUserAttributeId) throws Exception;

	/**
	 * remova a simple user attribute from compound user attribute
	 * 
	 * @param simpleUserAttributeId
	 * @param compoundUserAttributeId
	 * @throws Exception
	 */
	public void removeSimpleUserAttributeFromCompound(
			String simpleUserAttributeId, String compoundUserAttributeId)
			throws Exception;

	/**
	 * list all simple user attributes in current user, ordered by attribute
	 * name
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @return list of simple userAttributes
	 * @throws Exception
	 */
	public Set<UserAttribute> listSimpleUserAttributesForUser(
			String userIdentifier, String userIdentifierTypeCode)
			throws Exception;

	/**
	 * add a simple user attribute to current user
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @param String
	 *            userAttributeKey
	 * @param String
	 *            value
	 * @throws Exception
	 */
	public void addSimpleUserAttributeToUser(String userIdentifier,
			String userIdentifierTypeCode, String userAttributeId, String value)
			throws Exception;

	/**
	 * update simple user attribute for current user
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @param String
	 *            userAttributeKey
	 * @param String
	 *            value
	 * @throws Exception
	 */
	public void updateSimpleUserAttributeForUser(String userIdentifier,
			String userIdentifierTypeCode, String instanceIdentifier,
			String userAttributeId, String value) throws Exception;

	/**
	 * remove simple user attribute for current user
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @param String
	 *            userAttributeKey
	 * @param String
	 *            value
	 * @throws Exception
	 */
	public void removeSimpleUserAttributeForUser(String userIdentifier,
			String userIdentifierTypeCode, String instanceIdentifier)
			throws Exception;

	/**
	 * list all simple user attribute in a compound user attribute for current
	 * user
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @param String
	 *            userAttributeKey
	 * @param String
	 *            value
	 * @return list of simple userAttributes
	 * @throws Exception
	 */
	public Set<UserAttribute> listSimpleUserAttributesInCompoundForUser(
			String userIdentifier, String userIdentifierTypeCode)
			throws Exception;

	/**
	 * add a compoud user attribute to current user
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @param String
	 *            compoundAttributeKey
	 * @param String
	 *            simpleAttributeKey
	 * @param String
	 *            value
	 * @return the instanceIdentifier of added CompoundUserAttribute
	 * @throws Exception
	 */
	public String addSimpleUserAttributeToCompoundForUser(String userIdentifier,
			String userIdentifierTypeCode, String compoundAttributeId,
			String simpleAttributeId, String value) throws Exception;

	/**
	 * update all simple user attributes in current compound to current user. if
	 * value is blank, remove this simple attribute
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @param String
	 *            instanceIdentifier
	 * @param String
	 *            compoundAttributeKey
	 * @param String
	 *            simpleAttributeKey
	 * @param String
	 *            value
	 * @throws Exception
	 */
	public void updateSimpleUserAttributeToCompoundForUser(
			String userIdentifier, String userIdentifierTypeCode,
			String instanceIdentifier, String compoundAttributeId,
			String simpleAttributeId, String value) throws Exception;

	/**
	 * remove simple user attributes from compound and user
	 * 
	 * @param String
	 *            userIdentifier
	 * @param String
	 *            userIdentifierTypeCode
	 * @param String
	 *            instanceIdentifier
	 * @param String
	 *            compoundAttributeKey
	 * @param String
	 *            simpleAttributeKey
	 * @throws Exception
	 */
	public void removeSimpleUserAttributeFromCompoundForUser(
			String userIdentifier, String userIdentifierTypeCode,
			String instanceIdentifier, String compoundAttributeId,
			String simpleAttributeId) throws Exception;

	/**
	 * Returns the application with the specified identifier.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @return the application or null.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#findApplication(int)
	 */
	public IApplication findApplication(String applicationPortfolioIdentifier)
			throws Exception;

	/**
	 * Returns all permissions granted to the application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @return the permissions or an empty set.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#findPermissions(IApplication)
	 */
	public Set<IPermission> listPermissionsForApplication(
			String applicationPortfolioIdentifier) throws Exception;

	/**
	 * Returns all simple user attribute permissions granted to the application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @return the permissions or an empty set.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#findPermissions(IApplication)
	 */
	public Set<IPermission> listSimplePermissionsForApplication(
			String applicationPortfolioIdentifier) throws Exception;

	/**
	 * Returns all compound user attribute permissions granted to the
	 * application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @return the permissions or an empty set.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#findPermissions(IApplication)
	 */
	public Set<IPermission> listCompoundPermissionsForApplication(
			String applicationPortfolioIdentifier) throws Exception;

	/**
	 * Returns all ungranted simple user attributes in the application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @return the permissions or an empty set.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 */
	public Set<IUserAttribute> listUnGrantedSimpleUserAttributesForApplication(
			String applicationPortfolioIdentifier) throws Exception;

	/**
	 * Returns all simple attribute which has no permission under compound
	 * attribute related to application. If compound level is not granted, add
	 * null to show a blank option in the select.
	 * 
	 * @param compoundKey
	 *            string value of the compound user attribute
	 * @return the simple user attribute set or an empty set.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 */
	public Set<IUserAttribute> listUnGrantedSimpleUserAttributesByCompoundForApplication(
			String applicationPortfolioIdentifier,
			String compoundUserAttributeId) throws Exception;

	/**
	 * Returns all granted simple attribute permissions of a specific compound
	 * attribute.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @param compoundAttributeKey
	 *            string value of a compound user attribute
	 * @return the compound attribute permissions or an empty set.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 */
	public Set<IPermission> listGrantedSimplePermissionsByCompoundForApplication(
			String applicationPortfolioIdentifier,
			String compoundUserAttributeId) throws Exception;

	/**
	 * Returns all ungranted compound user attributes in the application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @return the permissions or an empty set.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 */
	public Set<IUserAttribute> listUnGrantedCompoundUserAttributesForApplication(
			String applicationPortfolioIdentifier) throws Exception;

	/**
	 * Find a permission on a specific simple/compound attribute.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @param userAttributeId
	 *            string value of a simple/compound user attribute
	 * @return the attribute permission or null.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#findPermissions(IApplication)
	 * @see #getAllPermissions(String)
	 */
	public IPermission findPermission(String applicationPortfolioIdentifier,
			String userAttributeId) throws Exception;

	/**
	 * Find a permission on a specific compound[simple] attribute.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @param compoundAttributeId
	 *            string value of a compound user attribute
	 * @param simpleAttributeId
	 *            string value of a simple user attribute
	 * @return the attribute permission or null.
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#findPermissions(IApplication)
	 * @see #getAllPermissions(String)
	 */
	public IPermission findPermission(String applicationPortfolioIdentifier,
			String compoundAttributeId, String simpleAttributeId)
			throws Exception;

	/**
	 * Grant specified user attribute to application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @param userAttributeId
	 *            string value of simple user attribute
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see #queryApplication(String)
	 * @see com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService#findUserAttribute(String)
	 */
	public void addPermission(String applicationPortfolioIdentifier,
			String userAttributeId) throws Exception;

	/**
	 * Grant specified compound and simple user attribute to application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @param userAttributeKey
	 *            string value of simple user attribute
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see #queryApplication(String)
	 * @see com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService#findUserAttribute(String)
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#addPermission(IApplication,
	 *      IUserAttribute)
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#addPermission(IApplication,
	 *      IUserAttribute, IUserAttribute)
	 */
	public void addCompoundPermission(String applicationPortfolioIdentifier,
			String compoundId, String simpleId) throws Exception;

	/**
	 * Remove specified simple user attribute from application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @param userAttributeKey
	 *            string value of simple user attribute
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 */
	public void removePermission(String applicationPortfolioIdentifier,
			String simpleUserAttributeId) throws Exception;

	/**
	 * Remove specified compound and simple user attribute from application.
	 * 
	 * @param applicationPortfolioIdentifier
	 *            string value of the identifier of application
	 * @param userAttributeKey
	 *            string value of simple user attribute
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 */
	public void removePermission(String applicationPortfolioIdentifier,
			String compoundUserAttributeId, String simpleUserAttributeId)
			throws Exception;
}
