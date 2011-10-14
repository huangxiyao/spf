package com.hp.it.cas.persona.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.service.IPersonaAdminService;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.uav.service.UserIdentifier;
import com.hp.it.cas.persona.user.service.impl.UserService;
import com.hp.it.cas.persona.web.UserAttribute;
import com.hp.it.cas.persona.web.Utils;

/**
 * An implementation of the Persona UI service layer APIs.
 * 
 * @author Kaijian Ding
 */
public class PersonaAdminServiceImpl implements IPersonaAdminService {

	private IMetadataConfigurationService metaDataService;

	private IUserAttributeValueService userAttributeValueService;

	private ISecurityConfigurationService securityService;

	private UserService userService;

	/**
	 * get metaDataService
	 * 
	 * @return IMetadataConfigurationService
	 */
	public IMetadataConfigurationService getMetaDataService() {
		return metaDataService;
	}

	/**
	 * set metaDataService
	 * 
	 * @param IMetadataConfigurationService
	 *            metaDataService
	 */
	public void setMetaDataService(IMetadataConfigurationService metaDataService) {
		this.metaDataService = metaDataService;
	}

	/**
	 * get userAttributeValueService
	 * 
	 * @return IUserAttributeValueService
	 */
	public IUserAttributeValueService getUserAttributeValueService() {
		return userAttributeValueService;
	}

	/**
	 * set userAttributeValueService
	 * 
	 * @param IUserAttributeValueService
	 *            userAttributeValueService
	 */
	public void setUserAttributeValueService(
			IUserAttributeValueService userAttributeValueService) {
		this.userAttributeValueService = userAttributeValueService;
	}

	/**
	 * get securityService
	 * 
	 * @return ISecurityConfigurationService
	 */
	public ISecurityConfigurationService getSecurityService() {
		return securityService;
	}

	/**
	 * set securityService
	 * 
	 * @param ISecurityConfigurationService
	 *            securityService
	 */
	public void setSecurityService(ISecurityConfigurationService securityService) {
		this.securityService = securityService;
	}

	/**
	 * get userService
	 * 
	 * @return UserService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * set userService
	 * 
	 * @param UserService
	 *            userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * find a user attribute by user attribute key
	 * 
	 * @param userAttributeId
	 * @return a user attribute
	 * @throws Exception
	 * @see com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService#findUserAttribute(String)
	 */
	public IUserAttribute findUserAttributeById(String userAttributeId)
			throws Exception {
		return metaDataService.findUserAttribute(userAttributeId);
	}

	/**
	 * find a user attribute by user attribute name
	 * 
	 * @param userAttributeName
	 * @return a user attribute
	 * @throws Exception
	 * @see com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService#findUserAttributeByName(String)
	 */
	public IUserAttribute findUserAttributeByName(String userAttributeName)
			throws Exception {
		return metaDataService.findUserAttributeByName(userAttributeName);
	}

	/**
	 * find a compound user attribute by user attribute key
	 * 
	 * @param userAttributeId
	 * @return a compound user attribute
	 * @throws Exception
	 */
	public IUserAttribute findCompoundUserAttributeById(String userAttributeId)
			throws Exception {
		IUserAttribute iUserAttribute = findUserAttributeById(userAttributeId);
		return (iUserAttribute != null && iUserAttribute
				.isCompoundUserAttribute()) ? iUserAttribute : null;
	}

	/**
	 * find a compound user attribute by user attribute name
	 * 
	 * @param userAttributeName
	 * @return a compound user attribute
	 * @throws Exception
	 */
	public IUserAttribute findCompoundUserAttributeByName(
			String userAttributeName) throws Exception {
		IUserAttribute iUserAttribute = findUserAttributeByName(userAttributeName);
		return (iUserAttribute != null && iUserAttribute
				.isCompoundUserAttribute()) ? iUserAttribute : null;
	}

	/**
	 * find a simple user attribute by user attribute key
	 * 
	 * @param userAttributeId
	 * @return a simple user attribute
	 * @throws Exception
	 */
	public IUserAttribute findSimpleUserAttributeById(String userAttributeId)
			throws Exception {
		IUserAttribute iUserAttribute = findUserAttributeById(userAttributeId);
		return (iUserAttribute != null && iUserAttribute
				.isSimpleUserAttribute()) ? iUserAttribute : null;
	}

	/**
	 * find a simple user attribute by user attribute name
	 * 
	 * @param userAttributeName
	 * @return a simple user attribute
	 * @throws Exception
	 */
	public IUserAttribute findSimpleUserAttributeByName(String userAttributeId)
			throws Exception {
		IUserAttribute iUserAttribute = findUserAttributeByName(userAttributeId);
		return (iUserAttribute != null && iUserAttribute
				.isSimpleUserAttribute()) ? iUserAttribute : null;
	}

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
			String userAttributeDefinition) throws Exception {
		IUserAttribute iUserAttribute = findUserAttributeById(userAttributeId);
		if (iUserAttribute == null) {
			throw new IllegalStateException(
					"Want to update a non-exist user attribute.");
		}
		iUserAttribute.setUserAttributeName(userAttributeName);
		iUserAttribute.setUserAttributeDescription(userAttributeDescription);
		iUserAttribute.setUserAttributeDefinition(userAttributeDefinition);
		return metaDataService.putUserAttribute(iUserAttribute);
	}

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
			throws Exception {
		IUserAttribute iUserAttribute = metaDataService
				.addCompoundUserAttribute(userAttributeId,
						userAttributeName, userAttributeDescription, userAttributeDefinition);
		return iUserAttribute;
	}

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
			throws Exception {
		IUserAttribute iUserAttribute = metaDataService.addSimpleUserAttribute(
		        userAttributeId, userAttributeName,
				userAttributeDescription, userAttributeDefinition);
		return iUserAttribute;
	}

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
	public void removeUserAttribute(String userAttributeId) throws Exception {
		IUserAttribute iUserAttribute = findUserAttributeById(userAttributeId);
		if (iUserAttribute == null) {
			throw new IllegalStateException(
					"Want to remove a non-exist user attribute.");
		}
		metaDataService.removeUserAttribute(iUserAttribute);
	}

	/**
	 * list all compound user attibributes ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 * @see com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService#getCompoundUserAttributes()
	 */
	public Set<IUserAttribute> listAllCompoundUserAttributes() throws Exception {
		// get all compound attributes
		Set<IUserAttribute> attributeSet = Utils
				.getAttributeSetOrderByName(metaDataService
						.getCompoundUserAttributes());
		return attributeSet;
	}

	/**
	 * list all simple user attibributes ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 * @see com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService#getSimpleUserAttributes()
	 */
	public Set<IUserAttribute> listAllSimpleUserAttributes() throws Exception {
		// get all simple attributes
		Set<IUserAttribute> attributeSet = Utils
				.getAttributeSetOrderByName(metaDataService
						.getSimpleUserAttributes());
		return attributeSet;
	}

	/**
	 * list all simple user attibributes which are available to be added to
	 * compound user attribute, ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 */
	public Set<IUserAttribute> listAvailableSimpleUserAttributes(
			String compoundUserAttributeId) throws Exception {
		Set<IUserAttribute> simpleAttributeList = metaDataService
				.findSimpleAttributes(findCompoundUserAttributeById(compoundUserAttributeId));

		Set<IUserAttribute> allSimpleUserAttributes = metaDataService
				.getSimpleUserAttributes();
		Set<IUserAttribute> availableSimpleAttributeList = new HashSet<IUserAttribute>();
		for (IUserAttribute userAttribute : allSimpleUserAttributes) {
			if (!simpleAttributeList.contains(userAttribute)) {
				availableSimpleAttributeList.add(userAttribute);
			}
		}
		return Utils.getAttributeSetOrderByName(availableSimpleAttributeList);
	}

	/**
	 * list all simple user attibributes which are attached to a compound user
	 * attribute, ordered by attribute name
	 * 
	 * @return set of IUserAttribute
	 * @throws Exception
	 */
	public Set<IUserAttribute> listSimpleUserAttributesForCompound(
			String compoundUserAttributeId) throws Exception {
		return metaDataService
				.findSimpleAttributes(findCompoundUserAttributeById(compoundUserAttributeId));
	}

	/**
	 * add simple user attribute to compound user attribute
	 * 
	 * @param simpleUserAttributeId
	 * @param compoundUserAttribute
	 * @throws Exception
	 */
	public void addSimpleUserAttributeToCompound(String simpleUserAttributeId,
			String compoundUserAttributeId) throws Exception {
		if (simpleUserAttributeId == null || "".equals(simpleUserAttributeId)) {
			return;
		}
		if (compoundUserAttributeId == null
				|| "".equals(compoundUserAttributeId)) {
			return;
		}
		metaDataService.addCompoundAttributeSimpleAttribute(
				findCompoundUserAttributeById(compoundUserAttributeId),
				findSimpleUserAttributeById(simpleUserAttributeId));
	}

	/**
	 * remova a simple user attribute from compound user attribute
	 * 
	 * @param simpleUserAttributeId
	 * @param compoundUserAttributeId
	 * @throws Exception
	 */
	public void removeSimpleUserAttributeFromCompound(
			String simpleUserAttributeId, String compoundUserAttributeId)
			throws Exception {
		metaDataService.removeCompoundAttributeSimpleAttribute(
				findCompoundUserAttributeById(compoundUserAttributeId),
				findSimpleUserAttributeById(simpleUserAttributeId));
	}

	/**
	 * list all simple user attributes in current user, ordered by attribute
	 * name
	 * 
	 * @return list of simple userAttributes
	 * @throws Exception
	 */
	public Set<UserAttribute> listSimpleUserAttributesForUser(
			String userIdentifier, String userIdentifierTypeCode)
			throws Exception {
		Set<IUserAttributeValue> attributeList = userAttributeValueService
				.findUserAttributeValues(new UserIdentifier(EUserIdentifierType
						.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
						userIdentifier));

		Set<UserAttribute> resultList = new TreeSet<UserAttribute>();
		for (IUserAttributeValue userAttributeValue : attributeList) {
			if (userAttributeValue.getUserAttribute().isSimpleUserAttribute()) {
				String userAttributeName = metaDataService.findUserAttribute(
						userAttributeValue.getUserAttribute()
								.getSimpleUserAttributeIdentifier())
						.getUserAttributeName();
				resultList.add(new UserAttribute(null, userAttributeName,
						userAttributeValue));
			}
		}
		return resultList;
	}

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
			String userIdentifierTypeCode, String userAttributeIdentifier, String value)
			throws Exception {
		userAttributeValueService.addUserAttributeValue(new UserIdentifier(
				EUserIdentifierType
						.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
				userIdentifier), userAttributeIdentifier, value);
	}

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
			String userAttributeId, String value) throws Exception {
		userAttributeValueService.putUserAttributeValue(new UserIdentifier(
				EUserIdentifierType
						.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
				userIdentifier), null, instanceIdentifier, userAttributeId, value);
	}

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
			throws Exception {
		if (userIdentifier != null && !"".equals(userIdentifier.trim())
				&& instanceIdentifier != null
				&& !"".equals(instanceIdentifier.trim())) {
			Set<IUserAttributeValue> attributeList = userAttributeValueService
					.findUserAttributeValues(new UserIdentifier(
							EUserIdentifierType
									.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
							userIdentifier));

			for (IUserAttributeValue userAttributeValue : attributeList) {
				if (userAttributeValue.getUserAttribute()
						.isSimpleUserAttribute()) {
					if (instanceIdentifier.equals(userAttributeValue
							.getInstanceIdentifier())) {
						userAttributeValueService.remove(userAttributeValue);
						break;
					}
				}
			}
		}
	}

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
			throws Exception {
		Set<IUserAttributeValue> attributeList = userAttributeValueService
				.findUserAttributeValues(new UserIdentifier(EUserIdentifierType
						.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
						userIdentifier));
		// only for temporary record purpose, to record which compound
		// attribute has been dealed
		Map tmp = new HashMap();
		Set tmp2 = new HashSet();
		Set<UserAttribute> resultList = new TreeSet<UserAttribute>();
		// find out all compound attribute attached in current user
		for (IUserAttributeValue userAttributeValue : attributeList) {
			// only deal with compound attribute
			if (userAttributeValue.getUserAttribute().isCompoundUserAttribute()) {
				// add compound attribute instanceIdentifier and key to
				// tmp map
				tmp.put(userAttributeValue.getInstanceIdentifier(), String
						.valueOf(userAttributeValue.getUserAttribute()
								.getCompoundUserAttributeIdentifier()));
				tmp2.add(userAttributeValue.getInstanceIdentifier()
						+ String.valueOf(userAttributeValue.getUserAttribute()
								.getSimpleUserAttributeIdentifier()));
				resultList.add(new UserAttribute(metaDataService
						.findUserAttribute(
								userAttributeValue.getUserAttribute()
										.getCompoundUserAttributeIdentifier())
						.getUserAttributeName(), metaDataService
						.findUserAttribute(
								userAttributeValue.getUserAttribute()
										.getSimpleUserAttributeIdentifier())
						.getUserAttributeName(), userAttributeValue));
			}
		}
		for (Object instanceIdentifier : tmp.keySet()) {
			IUserAttribute compoundUserAttribute = metaDataService
					.findUserAttribute((String) tmp.get(instanceIdentifier));
			Set<IUserAttribute> simpleUserAttributes = metaDataService
					.findSimpleAttributes(compoundUserAttribute);
			for (IUserAttribute simpleUserAttribute : simpleUserAttributes) {
				if (!tmp2.contains((String) instanceIdentifier
						+ String.valueOf(simpleUserAttribute
								.getUserAttributeIdentifier()))) {
					UserAttribute userAttribute = new UserAttribute(
							compoundUserAttribute.getUserAttributeName(),
							simpleUserAttribute.getUserAttributeName(), null);
					userAttribute
							.setCompoundUserAttributeIdentifier(compoundUserAttribute
									.getUserAttributeIdentifier());
					userAttribute.setSimpleUserAttributeIdentifier(simpleUserAttribute
							.getUserAttributeIdentifier());
					userAttribute
							.setInstanceIdentifier((String) instanceIdentifier);
					resultList.add(userAttribute);
				}
			}
		}
		return resultList;
	}

	/**
	 * add a compoud&simple user attribute to current user
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
			String simpleAttributeId, String value) throws Exception {
		return (userAttributeValueService.addUserAttributeValue(
				new UserIdentifier(EUserIdentifierType
						.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
						userIdentifier),
				compoundAttributeId, simpleAttributeId, value))
				.getInstanceIdentifier();
	}

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
			String simpleAttributeId, String value) throws Exception {
		userAttributeValueService
				.putUserAttributeValue(new UserIdentifier(EUserIdentifierType
						.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
						userIdentifier),
						compoundAttributeId,
						instanceIdentifier, simpleAttributeId, value);
	}

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
			String simpleAttributeId) throws Exception {
		Set<IUserAttributeValue> attributeList = userAttributeValueService
				.findUserAttributeValues(new UserIdentifier(EUserIdentifierType
						.valueOfUserIdentifierTypeCode(userIdentifierTypeCode),
						userIdentifier));
		for (IUserAttributeValue userAttributeValue : attributeList) {
			if (userAttributeValue.getUserAttribute().isCompoundUserAttribute()) {
				if (instanceIdentifier.equals(userAttributeValue
						.getInstanceIdentifier())
						&& compoundAttributeId.equals(String
								.valueOf(userAttributeValue.getUserAttribute()
										.getCompoundUserAttributeIdentifier()))
						&& simpleAttributeId.equals(String
								.valueOf(userAttributeValue.getUserAttribute()
										.getSimpleUserAttributeIdentifier()))) {
					userAttributeValueService.remove(userAttributeValue);
					break;
				}
			}
		}
	}

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
			throws Exception {
		int identifier = Integer.valueOf(applicationPortfolioIdentifier)
				.intValue();
		IApplication iApplication = securityService.findApplication(identifier);
		return iApplication;
	}

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
			String applicationPortfolioIdentifier) throws Exception {
		IApplication iApplication = findApplication(applicationPortfolioIdentifier);
		Set<IPermission> set = securityService.findPermissions(iApplication);
		return set;
	}

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
			String applicationPortfolioIdentifier) throws Exception {
		Set<IPermission> set = listPermissionsForApplication(applicationPortfolioIdentifier);
		Set<IPermission> simpleAttr = new HashSet<IPermission>();
		for (IPermission permission : set) {
			if (permission.getCompoundUserAttribute() == null) {
				simpleAttr.add(permission);
			}
		}
		return simpleAttr;
	}

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
			String applicationPortfolioIdentifier) throws Exception {
		Set<IPermission> set = listPermissionsForApplication(applicationPortfolioIdentifier);
		Set<IPermission> compoundAttr = new HashSet<IPermission>();
		for (IPermission permission : set) {
			if (permission.getCompoundUserAttribute() != null) {
				compoundAttr.add(permission);
			}
		}
		return compoundAttr;
	}

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
			String applicationPortfolioIdentifier) throws Exception {
		Set<IPermission> set = listSimplePermissionsForApplication(applicationPortfolioIdentifier);
		// retrieve all simple user attributes, including granted and
		// not granted ones
		Set<IUserAttribute> simpleAttrSet = new HashSet<IUserAttribute>();
		simpleAttrSet.addAll(metaDataService.getSimpleUserAttributes());
		// remove granted simple attributes
		for (IPermission permission : set) {
			IUserAttribute attr = permission.getSimpleUserAttribute();
			if (simpleAttrSet.contains(attr)) {
				simpleAttrSet.remove(attr);
			}
		}
		return Utils.getAttributeSetOrderByName(simpleAttrSet);
	}

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
			String compoundUserAttributeId) throws Exception {
		boolean isGrantedAtCompoundLvl = false;
		Set<IUserAttribute> set = listSimpleUserAttributesForCompound(compoundUserAttributeId);
		Set<IPermission> permissions = listGrantedSimplePermissionsByCompoundForApplication(
				applicationPortfolioIdentifier, compoundUserAttributeId);
		for (IPermission permission : permissions) {
			IUserAttribute simpleUserAttr = permission.getSimpleUserAttribute();
			if (simpleUserAttr == null) {
				isGrantedAtCompoundLvl = true;
			} else if (set.contains(simpleUserAttr)) {
				set.remove(simpleUserAttr);
			}
		}

		if (!isGrantedAtCompoundLvl) {
			set.add(null);
		}

		return Utils.getAttributeSetOrderByName(set);
	}

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
			String applicationPortfolioIdentifier) throws Exception {
		// retrieve all compound attribute permissions
		Set<IPermission> set = listCompoundPermissionsForApplication(applicationPortfolioIdentifier);

		// retrieve all compound user attributes
		Set<IUserAttribute> compoundAttrSet = new HashSet<IUserAttribute>();
		compoundAttrSet.addAll(metaDataService.getCompoundUserAttributes());

		// retrieve all distinct compound user attributes which has
		// already granted permissions
		Set<IUserAttribute> compoundWithPermission = new HashSet<IUserAttribute>();
		for (IPermission permission : set) {
			IUserAttribute compoundAttr = permission.getCompoundUserAttribute();
			compoundWithPermission.add(compoundAttr);
		}

		// check if the compound user attributes has all permissions,
		// including each individual simple user attributes and 'ALL'
		// value(simple attributes= null)
		for (IUserAttribute userAttribute : compoundWithPermission) {
			if (isCompoundAllGranted(applicationPortfolioIdentifier,
					userAttribute)) {
				if (compoundAttrSet.contains(userAttribute)) {
					compoundAttrSet.remove(userAttribute);
				}
			}
		}

		return Utils.getAttributeSetOrderByName(compoundAttrSet);
	}

	/**
	 * Check if the specified compound user attribute granted to 'ALL' and all
	 * its simple user attributes are granted under the specified application.
	 * 
	 * @param appIdenitfier
	 *            string value of the application idenitfier
	 * @param userAttribute
	 *            compound user attribute
	 * @return <code>true</code> if all granted, don't need to do any grant
	 *         operation. Otherwise <code>false</code>
	 * @throws NumberFormatException
	 *             if identifier is an invalid integer number.
	 * @throws Exception
	 *             if other errors raised
	 * @see com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService#findPermissions(IApplication)
	 * @see #getGrantedSimplePermissionsByCompoundAttribute(String, String)
	 */
	private boolean isCompoundAllGranted(String appIdenitfier,
			IUserAttribute userAttribute) throws Exception {
		Set<IUserAttribute> set = metaDataService
				.findSimpleAttributes(userAttribute);
		Set<IPermission> permissions = listGrantedSimplePermissionsByCompoundForApplication(
				appIdenitfier, String.valueOf(userAttribute
						.getUserAttributeIdentifier()));
		// permissions has a more value of 'ALL'(simple user attribute = null)
		return (set.size() + 1) == permissions.size();

	}

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
			String compoundUserAttributeId) throws Exception {
		Set<IPermission> set = listPermissionsForApplication(applicationPortfolioIdentifier);
		Set<IPermission> grantedSet = new HashSet<IPermission>();

		for (IPermission permission : set) {
			if ((null != permission.getCompoundUserAttribute())
					&& (compoundUserAttributeId == permission.getCompoundUserAttribute()
							.getUserAttributeIdentifier())) {
				grantedSet.add(permission);
			}
		}
		return grantedSet;
	}

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
			String userAttributeId) throws Exception {
		Set<IPermission> set = listPermissionsForApplication(applicationPortfolioIdentifier);

		for (IPermission permission : set) {
			if ((null != permission.getCompoundUserAttribute())
					&& (userAttributeId == permission.getCompoundUserAttribute()
							.getUserAttributeIdentifier())
					&& (null == permission.getSimpleUserAttribute())) {
				return permission;
			}
			if ((null != permission.getSimpleUserAttribute())
					&& (userAttributeId == permission.getSimpleUserAttribute()
							.getUserAttributeIdentifier())
					&& (null == permission.getCompoundUserAttribute())) {
				return permission;
			}
		}

		return null;
	}

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
			throws Exception {
		Set<IPermission> set = listPermissionsForApplication(applicationPortfolioIdentifier);

		for (IPermission permission : set) {
			if ((null != permission.getCompoundUserAttribute())
					&& (compoundAttributeId == permission.getCompoundUserAttribute()
							.getUserAttributeIdentifier())
					&& (null != permission.getSimpleUserAttribute())
					&& (simpleAttributeId == permission.getSimpleUserAttribute()
							.getUserAttributeIdentifier())) {
				return permission;
			}
		}

		return null;
	}

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
			String userAttributeId) throws Exception {
		// retrieve application
		IApplication iApplication = findApplication(applicationPortfolioIdentifier);

		// retrieve user attribute
		IUserAttribute attribute = findUserAttributeById(userAttributeId);
		if (attribute == null) {
			throw new IllegalStateException(
					"Specified user attribute doesn't exist. "
							+ userAttributeId);
		}

		securityService.addPermission(iApplication, attribute);
	}

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
			String compoundId, String simpleId) throws Exception {
		if (simpleId == null || "".equals(simpleId.trim())) {
			addPermission(applicationPortfolioIdentifier, compoundId);
		} else {
			// retrieve simple user attribute
			// retrieve compound user attribute
			IUserAttribute compoundUserAttr = findCompoundUserAttributeById(compoundId);
			IUserAttribute simpleUserAttr = findSimpleUserAttributeById(simpleId);
			// retrieve application
			IApplication iApplication = findApplication(applicationPortfolioIdentifier);
			securityService.addPermission(iApplication, compoundUserAttr,
					simpleUserAttr);
		}
	}

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
			String simpleUserAttributeId) throws Exception {
		IPermission permission = findPermission(applicationPortfolioIdentifier,
				simpleUserAttributeId);
		securityService.removePermission(permission);
	}

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
			throws Exception {
		if (simpleUserAttributeId != null
				&& !"".equals(simpleUserAttributeId)) {
			IPermission permission = findPermission(
					applicationPortfolioIdentifier, compoundUserAttributeId,
					simpleUserAttributeId);
			securityService.removePermission(permission);
		} else {
			removePermission(applicationPortfolioIdentifier,
					compoundUserAttributeId);
		}
	}

}
