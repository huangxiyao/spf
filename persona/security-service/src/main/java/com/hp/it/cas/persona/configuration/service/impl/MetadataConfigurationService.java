package com.hp.it.cas.persona.configuration.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.dao.IPersonaDao;
import com.hp.it.cas.persona.dao.IPersonaDaoFactory;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttr;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
import com.hp.it.cas.xa.logging.StopWatch;
import com.hp.it.cas.xa.validate.Verifier;

/**
 * A configuration service based on a DAO backing store.
 *
 * @author Quintin May
 */
public class MetadataConfigurationService extends AbstractConfigurationService implements IMetadataConfigurationService {

	private final Logger logger = LoggerFactory.getLogger(MetadataConfigurationService.class.getName());

	private final IUserAttrDAO userAttrDao;
	private final ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao;
	private final IPersonaDao personaDao;
	
	/**
	 * Creates a metadata configuration service.
	 * @param daoFactory the DAO factory providing access to the backing store.
	 */
	public MetadataConfigurationService(IPersonaDaoFactory daoFactory) {
		new Verifier().isNotNull(daoFactory, "DAO factory cannot be null").throwIfError();
		
		this.userAttrDao = daoFactory.getUserAttrDAO();
		this.cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
		this.personaDao = daoFactory.getPersonaDao();
	}

	public Set<IUserAttribute> getSimpleUserAttributes() {
		logger.debug("ENTRY");
		StopWatch sw = new StopWatch().start();
		
		Set<IUserAttribute> result = new HashSet<IUserAttribute>();
		for (UserAttr userAttr : personaDao.selectAllSimpleAttributes()) {
			result.add(from(userAttr));
		}
		
		logger.debug("RETURN {} {}", sw, result);
		return Collections.unmodifiableSet(result);
	}

	public Set<IUserAttribute> getCompoundUserAttributes() {
		logger.debug("ENTRY ({})", new Object[] {});
		StopWatch sw = new StopWatch().start();
		
		Set<IUserAttribute> result = new HashSet<IUserAttribute>();
		for (UserAttr userAttr : personaDao.selectAllCompoundAttributes()) {
			result.add(from(userAttr));
		}
		
		logger.debug("RETURN {} {}", sw, result);
		return Collections.unmodifiableSet(result);
	}

	public IUserAttribute findUserAttribute(String userAttributeIdentifier) {
		logger.debug("ENTRY ({})", userAttributeIdentifier);
		StopWatch sw = new StopWatch().start();
		
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(userAttributeIdentifier);		
		UserAttr userAttr = userAttrDao.selectByPrimaryKey(key);
		
		IUserAttribute attribute = userAttr == null ? null : from(userAttr);
		logger.debug("RETURN {} {}", sw, attribute);
		return attribute;
	}

	public IUserAttribute findUserAttributeByName(String userAttributeName) {
		logger.debug("ENTRY ({})", userAttributeName);
		StopWatch sw = new StopWatch().start();
		
		UserAttr userAttr = userAttrDao.selectByAk1UserAttr(userAttributeName);
		
		IUserAttribute attribute = userAttr == null ? null : from(userAttr);
		logger.debug("RETURN {} {}", sw, attribute);
		return attribute;
	}
	
	public IUserAttribute addSimpleUserAttribute(String userAttributeIdentifier, String userAttributeName, String userAttributeDescription, String userAttributeDefinition) {
		return addUserAttribute(userAttributeIdentifier, userAttributeName, userAttributeDescription, userAttributeDefinition, EUserAttributeType.SIMPLE);
	}

	public IUserAttribute addCompoundUserAttribute(String userAttributeIdentifier, String userAttributeName, String userAttributeDescription, String userAttributeDefinition) {		
		return addUserAttribute(userAttributeIdentifier, userAttributeName, userAttributeDescription, userAttributeDefinition, EUserAttributeType.COMPOUND);
	}

	private IUserAttribute addUserAttribute(String userAttributeIdentifier, String userAttributeName, String userAttributeDescription, String userAttributeDefinition, EUserAttributeType userAttributeType) {
		logger.debug("ENTRY ({}, {})", userAttributeName, userAttributeType);
		
		new Verifier()
		    .isNotEmpty(userAttributeIdentifier, "User attribute identifier must be specified.")
    		.isNotEmpty(userAttributeName, "User attribute name must be specified.")
    		.isNotEmpty(userAttributeDescription, "User attribute description must be specified.")
    		.isNotEmpty(userAttributeDefinition, "User attribute definition must be specified.")
    		.throwIfError();

		StopWatch sw = new StopWatch().start();
		
		UserAttr userAttr = new UserAttr();
		UserAttrKey userAttrKey = new UserAttrKey();
        userAttr.setKey(userAttrKey);
		userAttrKey.setUserAttrId(userAttributeIdentifier);
		
		userAttr.setUserAttrNm(userAttributeName);
		userAttr.setUserAttrDn(userAttributeDescription);
		userAttr.setUserAttrDefnTx(userAttributeDefinition);
		userAttr.setUserAttrTypeCd(userAttributeType.getUserAttributeTypeCode());
		
		Date now = new Date();
		String principalName = getAuditPrincipalName();
		userAttr.setCrtTs(now);
		userAttr.setCrtUserId(principalName);
		userAttr.setLastMaintTs(now);
		userAttr.setLastMaintUserId(principalName);

		validateForeignKeyConstraints(userAttr);
		userAttrDao.insert(userAttr);
		
		IUserAttribute attribute = from(userAttr);
		logger.debug("RETURN {} {}", sw, attribute);
		return attribute;
	}
	
	public IUserAttribute putUserAttribute(IUserAttribute userAttribute) {
		logger.debug("ENTRY ({})", userAttribute);
		
		new Verifier()
			.isNotNull(userAttribute, "User attribute cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(userAttribute.getUserAttributeIdentifier());
		
		UserAttr userAttr = new UserAttr();
		userAttr.setKey(key);
		userAttr.setUserAttrTypeCd(userAttribute.isSimpleUserAttribute()
				? EUserAttributeType.SIMPLE.getUserAttributeTypeCode() : EUserAttributeType.COMPOUND.getUserAttributeTypeCode());
		userAttr.setUserAttrNm(userAttribute.getUserAttributeName());
		userAttr.setUserAttrDn(userAttribute.getUserAttributeDescription());
		userAttr.setUserAttrDefnTx(userAttribute.getUserAttributeDefinition());

		Date now = new Date();
		String principalName = getAuditPrincipalName();
		userAttr.setCrtTs(now);
		userAttr.setCrtUserId(principalName);
		userAttr.setLastMaintTs(now);
		userAttr.setLastMaintUserId(principalName);

		validateForeignKeyConstraints(userAttr);
		userAttrDao.updateByPrimaryKeySelective(userAttr);

		logger.debug("RETURN {} {}", sw, userAttribute);
		return userAttribute;
	}

	public void addCompoundAttributeSimpleAttribute(IUserAttribute compoundUserAttribute, IUserAttribute simpleUserAttribute) {
		logger.debug("ENTRY ({}, {})", compoundUserAttribute, simpleUserAttribute);
		
		new Verifier()
			.isNotNull(compoundUserAttribute, "Compound user attribute cannot be null.")
			.isNotNull(simpleUserAttribute, "Simple user attribute cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		key.setCmpndUserAttrId(compoundUserAttribute.getUserAttributeIdentifier());
		key.setSmplUserAttrId(simpleUserAttribute.getUserAttributeIdentifier());
		
		CmpndAttrSmplAttr cmpndAttrSmplAttr = new CmpndAttrSmplAttr();
		cmpndAttrSmplAttr.setKey(key);

		Date now = new Date();
		String principalName = getAuditPrincipalName();
		cmpndAttrSmplAttr.setCrtTs(now);
		cmpndAttrSmplAttr.setCrtUserId(principalName);

		validateConstraints(cmpndAttrSmplAttr);
		cmpndAttrSmplAttrDao.insert(cmpndAttrSmplAttr);
		
		logger.debug("RETURN {}", sw);
	}

	public Set<IUserAttribute> findCompoundAttributes(IUserAttribute simpleUserAttribute) {
		logger.debug("ENTRY ({})", simpleUserAttribute);
		
		new Verifier()
			.isNotNull(simpleUserAttribute, "Simple user attribute cannot be null.")
			.throwIfError();

		StopWatch sw = new StopWatch().start();
		
		Set<UserAttr> userAttrs = personaDao.findCompoundAttributesForSimpleAttribute(simpleUserAttribute.getUserAttributeIdentifier());
		Set<IUserAttribute> attributes = new HashSet<IUserAttribute>();
		for (UserAttr userAttr : userAttrs) {
			attributes.add(from(userAttr));
		}
		
		logger.debug("RETURN {} {}", sw, attributes);
		return attributes;
	}

	public Set<IUserAttribute> findSimpleAttributes(IUserAttribute compoundUserAttribute) {
		logger.debug("ENTRY ({})", compoundUserAttribute);
		
		new Verifier()
			.isNotNull(compoundUserAttribute, "Compound user attribute cannot be null.")
			.throwIfError();

		StopWatch sw = new StopWatch().start();
		
		Set<UserAttr> userAttrs = personaDao.findSimpleAttributesForCompoundAttribute(compoundUserAttribute.getUserAttributeIdentifier());
		Set<IUserAttribute> attributes = new HashSet<IUserAttribute>();
		for (UserAttr userAttr : userAttrs) {
			attributes.add(from(userAttr));
		}
		
		logger.debug("RETURN {} {}", sw, attributes);
		return attributes;
	}

	public void removeUserAttribute(IUserAttribute simpleUserAttribute) {
		logger.debug("ENTRY ({})", simpleUserAttribute);
		
		new Verifier()
			.isNotNull(simpleUserAttribute, "Simple user attribute cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(simpleUserAttribute.getUserAttributeIdentifier());
		
		validateForeignKeyConstraintsForDelete(key);
		userAttrDao.deleteByPrimaryKey(key);
		
		logger.debug("RETURN {}", sw);
	}

	public void removeCompoundAttributeSimpleAttribute(IUserAttribute compoundUserAttribute, IUserAttribute simpleUserAttribute) {
		logger.debug("ENTRY ({}, {})", compoundUserAttribute, simpleUserAttribute);
		
		new Verifier()
    		.isNotNull(compoundUserAttribute, "Compound user attribute cannot be null.")
    		.isNotNull(simpleUserAttribute, "Simple user attribute cannot be null.")
    		.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		key.setCmpndUserAttrId(compoundUserAttribute.getUserAttributeIdentifier());
		key.setSmplUserAttrId(simpleUserAttribute.getUserAttributeIdentifier());
		
		validateForeignKeyConstraintsForDelete(key);
		cmpndAttrSmplAttrDao.deleteByPrimaryKey(key);
		
		logger.debug("RETURN {}", sw);
	}

	private void validateForeignKeyConstraints(UserAttr userAttr) {
		// USER_ATTR_TYPE_CD
		try {
			EUserAttributeType.valueOfUserAttributeTypeCode(userAttr.getUserAttrTypeCd());
		} catch (IllegalArgumentException e) {
			throw new DataIntegrityViolationException(String.format("USER_ATTR_TYPE_CD '%s' is invalid.", userAttr.getUserAttrTypeCd()), e);
		}
	}

	private void validateConstraints(CmpndAttrSmplAttr cmpndAttrSmplAttr) {
		logger.debug("ENTRY ({})", cmpndAttrSmplAttr);
		StopWatch sw = new StopWatch().start();
		
		// CMPND_USER_ATTR_KY
		if (cmpndAttrSmplAttr.getKey().getCmpndUserAttrId() == null) {
			throw new DataIntegrityViolationException("CMPND_USER_ATTR_ID cannot be null.");
		}
		
		UserAttrKey userAttrKey = new UserAttrKey();
		userAttrKey.setUserAttrId(cmpndAttrSmplAttr.getKey().getCmpndUserAttrId());

		UserAttr userAttribute = userAttrDao.selectByPrimaryKey(userAttrKey);
		if (userAttribute == null || ! EUserAttributeType.COMPOUND.getUserAttributeTypeCode().equals(userAttribute.getUserAttrTypeCd())) {
			throw new DataIntegrityViolationException(String.format("CMPND_USER_ATTR_ID '%s' is invalid.", cmpndAttrSmplAttr.getKey().getCmpndUserAttrId()));
		}
		
		// SMPL_USER_ATTR_KY
		userAttrKey.setUserAttrId(cmpndAttrSmplAttr.getKey().getSmplUserAttrId());
		userAttribute = userAttrDao.selectByPrimaryKey(userAttrKey);
		
		if (cmpndAttrSmplAttr.getKey().getSmplUserAttrId() == null
				|| userAttribute == null
				|| ! EUserAttributeType.SIMPLE.getUserAttributeTypeCode().equals(userAttribute.getUserAttrTypeCd())) {
			throw new DataIntegrityViolationException(String.format("SMPL_USER_ATTR_ID '%s' is invalid.", cmpndAttrSmplAttr.getKey().getSmplUserAttrId()));
		}
		
		logger.debug("RETURN {}", sw);
	}

	private void validateForeignKeyConstraintsForDelete(UserAttrKey key) {
		logger.debug("ENTRY ({})", key);
		StopWatch sw = new StopWatch().start();
		
		String id = key.getUserAttrId();

		// ON DELETE NO ACTION
		int count = personaDao.countCmpndAttrSmplAttrWithUserAttrId(id);
		if (count > 0) {
			throw new DataIntegrityViolationException(
					String.format("Cannot delete USER_ATTR %s because %d CMPND_ATTR_SMPL_ATTR reference(s) it.", id, count));
		}
		
		count = personaDao.countUserAttrValuWithUserAttrId(id);
		if (count > 0) {
			throw new DataIntegrityViolationException(
					String.format("Cannot delete USER_ATTR %s because %d USER_ATTR_VALU reference(s) it.", id, count));
		}
		
		// ON DELETE CASCADE		
		personaDao.deleteAppUserAttrPrmsnWithUserAttrId(id);
		personaDao.deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(id);
		
		logger.debug("RETURN {}", sw);
	}

	private void validateForeignKeyConstraintsForDelete(CmpndAttrSmplAttrKey key) {
		logger.debug("ENTRY ({})", key);
		StopWatch sw = new StopWatch().start();
		
		// ON DELETE NO ACTION
		int count = personaDao.countUserAttrValuWithCmpndAttrSmplAttrKey(key);
		if (count > 0) {
			throw new DataIntegrityViolationException(
					String.format("Cannot delete CMPND_ATTR_SMPL_ATTR %s.%s because %d USER_ATTR_VALU reference(s) it.",
							key.getCmpndUserAttrId(), key.getSmplUserAttrId(), count));
		}

		// ON DELETE CASCADE		
		personaDao.deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey(key);

		logger.debug("RETURN {}", sw);
	}

	private IUserAttribute from(UserAttr from) {
		UserAttribute userAttribute = new UserAttribute(from.getKey().getUserAttrId(),
				EUserAttributeType.COMPOUND.getUserAttributeTypeCode().equals(from.getUserAttrTypeCd()));
		userAttribute.setUserAttributeDefinition(from.getUserAttrDefnTx());
		userAttribute.setUserAttributeDescription(from.getUserAttrDn());
		userAttribute.setUserAttributeName(from.getUserAttrNm());
		return userAttribute;
	}
}
