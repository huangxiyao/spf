package com.hp.it.cas.persona.uav.service.impl;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import com.hp.it.cas.persona.dao.IPersonaDaoFactory;
import com.hp.it.cas.persona.security.RunAsApplicationToken;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
import com.hp.it.cas.security.dao.UserAttrValu;
import com.hp.it.cas.security.dao.UserAttrValuKey;
import com.hp.it.cas.xa.logging.StopWatch;
import com.hp.it.cas.xa.validate.Verifier;

/**
 * A user attribute value service based on a DAO backing store. Features of the implementation:
 *
 * <ul>
 * 	<li>Read operations do not require authorization.</li>
 * 	<li>Write operations are authorized based on the invoking application and the attribute being written.</li>
 * </ul>
 * 
 * @author Quintin May
 */
public class UserAttributeValueService implements IUserAttributeValueService {

	private final Logger logger = LoggerFactory.getLogger(UserAttributeValueService.class.getName());
	
	private final IPersonaDaoFactory daoFactory;
	private final Authorizer authorizer;

	/**
	 * Creates a user attribute value service.
	 * @param daoFactory the DAO factory providing access to the backing store.
	 */
	public UserAttributeValueService(IPersonaDaoFactory daoFactory) {
		if (daoFactory == null) throw new NullPointerException("DAO factory cannot be null.");
		
		this.daoFactory = daoFactory;
		this.authorizer = new Authorizer(daoFactory);
	}
	
	public Set<IUserAttributeValue> findUserAttributeValues(IUserIdentifier userIdentifier) {
		logger.debug("ENTRY ({})", userIdentifier);

		new Verifier()
			.isNotNull(userIdentifier, "User identifier cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		Set<IUserAttributeValue> userAttributeValues = new HashSet<IUserAttributeValue>();
		
		UserAttrValuKey key = new UserAttrValuKey();
		key.setUserIdTypeCd(userIdentifier.getUserIdentifierType().getUserIdentifierTypeCode());
		key.setUserId(userIdentifier.getUserIdentifier());
		
		for (UserAttrValu userAttrValu : daoFactory.getUserAttrValuDAO().selectByPrimaryKeyDiscretionary(key)) {
			userAttributeValues.add(new UserAttributeValue(userAttrValu));
		}
		
		logger.debug("RETURN {} {} attribute values", sw, userAttributeValues.size());
		return userAttributeValues;
	}

	public IUserAttributeValue addUserAttributeValue(IUserIdentifier userIdentifier, String simpleUserAttributeIdentifier, String value) {
		return putUserAttributeValue(userIdentifier, null, null, simpleUserAttributeIdentifier, value);
	}

	public IUserAttributeValue addUserAttributeValue(IUserIdentifier userIdentifier, String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier, String value) {
		return putUserAttributeValue(userIdentifier, compoundUserAttributeIdentifier, null, simpleUserAttributeIdentifier, value);
	}

	public IUserAttributeValue putUserAttributeValue(IUserIdentifier userIdentifier, String compoundUserAttributeIdentifier, String instanceIdentifier, String simpleUserAttributeIdentifier, String value) {
		logger.debug("ENTRY ({}, {}, {}, {}, {})", new Object[] {userIdentifier, compoundUserAttributeIdentifier, instanceIdentifier, simpleUserAttributeIdentifier, value});

		new Verifier()
			.isNotNull(userIdentifier, "User identifier cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		IUserAttribute userAttribute = new UserAttribute(compoundUserAttributeIdentifier, simpleUserAttributeIdentifier);
		authorizer.authorize(userAttribute);

		if (instanceIdentifier == null) {
			instanceIdentifier = UUID.randomUUID().toString();
		} else {
			// this will throw an illegal argument exception if the instanceIdentifier is not a valid UUID.
			UUID.fromString(instanceIdentifier);
		}
		
		UserAttrValuKey userAttrValuKey = new UserAttrValuKey();
		userAttrValuKey.setUserIdTypeCd(userIdentifier.getUserIdentifierType().getUserIdentifierTypeCode());
		userAttrValuKey.setUserId(userIdentifier.getUserIdentifier());
		userAttrValuKey.setCmpndUserAttrId(userAttribute.getCompoundUserAttributeIdentifier());
		userAttrValuKey.setSmplUserAttrId(userAttribute.getSimpleUserAttributeIdentifier());
		userAttrValuKey.setUserAttrInstncId(instanceIdentifier);
		
		UserAttrValu userAttrValu = new UserAttrValu();
		userAttrValu.setKey(userAttrValuKey);
		userAttrValu.setUserAttrValuTx(value);
		
		Date now = new Date();
		String principalName = getAuditPrincipalName();
		userAttrValu.setCrtTs(now);
		userAttrValu.setCrtUserId(principalName);
		userAttrValu.setLastMaintTs(now);
		userAttrValu.setLastMaintUserId(principalName);

		validateForeignKeyConstraints(userAttrValu);
		
		try {
			daoFactory.getUserAttrValuDAO().insert(userAttrValu);
		} catch (DataIntegrityViolationException e) {
			daoFactory.getUserAttrValuDAO().updateByPrimaryKeySelective(userAttrValu);
		}
		
		IUserAttributeValue userAttributeValue = new UserAttributeValue(userAttrValu);
		logger.debug("RETURN {}", sw, userAttributeValue);
		return userAttributeValue;
	}

	public void remove(IUserAttributeValue userAttributeValue) {
		logger.debug("ENTRY ({})", userAttributeValue);
		
		new Verifier()
			.isNotNull(userAttributeValue, "User attribute value cannot be null.")
			.throwIfError();
		
		StopWatch sw = new StopWatch().start();
		
		authorizer.authorize(userAttributeValue.getUserAttribute());
		
		UserAttrValuKey userAttrValuKey = new UserAttrValuKey();
		userAttrValuKey.setUserIdTypeCd(userAttributeValue.getUserIdentifier().getUserIdentifierType().getUserIdentifierTypeCode());
		userAttrValuKey.setUserId(userAttributeValue.getUserIdentifier().getUserIdentifier());
		userAttrValuKey.setCmpndUserAttrId(userAttributeValue.getUserAttribute().getCompoundUserAttributeIdentifier());
		userAttrValuKey.setSmplUserAttrId(userAttributeValue.getUserAttribute().getSimpleUserAttributeIdentifier());
		userAttrValuKey.setUserAttrInstncId(userAttributeValue.getInstanceIdentifier());
		
		daoFactory.getUserAttrValuDAO().deleteByPrimaryKey(userAttrValuKey);
		logger.debug("RETURN {}", sw);
	}
	
	private String getAuditPrincipalName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication instanceof RunAsApplicationToken
			? ((RunAsApplicationToken) authentication).getOriginalPrincipal()
					: authentication.getPrincipal();
		
		return principal instanceof Principal ? ((Principal) principal).getName() : "";
	}
	
	private void validateForeignKeyConstraints(UserAttrValu userAttrValu) {
		logger.debug("ENTRY ({})", userAttrValu);
		StopWatch sw = new StopWatch().start();
		
		// USER_ID
		if (userAttrValu.getKey().getUserId() == null) {
			throw new DataIntegrityViolationException("USER_ID cannot be null.");
		}
		
		// USER_ID_TYPE_CD
		try {
			EUserIdentifierType.valueOfUserIdentifierTypeCode(userAttrValu.getKey().getUserIdTypeCd());
		} catch (IllegalArgumentException e) {
			throw new DataIntegrityViolationException(String.format("USER_ID_TYPE_CD '%s' is invalid.", userAttrValu.getKey().getUserIdTypeCd()), e);
		}
		
		// CMPND_USER_ATTR_KY
		if (userAttrValu.getKey().getCmpndUserAttrId() == null) {
			throw new DataIntegrityViolationException("CMPND_USER_ATTR_ID cannot be null.");
		}
		
		// SMPL_USER_ATTR_KY
		if (userAttrValu.getKey().getSmplUserAttrId() == null) {
			throw new DataIntegrityViolationException("SMPL_USER_ATTR_ID cannot be null.");
		}
		
		if ("".equals(userAttrValu.getKey().getCmpndUserAttrId())) {
			// simple
			UserAttrKey userAttrKey = new UserAttrKey();
			userAttrKey.setUserAttrId(userAttrValu.getKey().getSmplUserAttrId());
			UserAttr userAttr = daoFactory.getUserAttrDAO().selectByPrimaryKey(userAttrKey);
			if (userAttr == null || ! "SMPL".equals(userAttr.getUserAttrTypeCd())) {	// TODO this is an unfortunate constant
				throw new DataIntegrityViolationException(String.format("SMPL_USER_ATTR_ID '%s' is invalid.", userAttrValu.getKey().getSmplUserAttrId()));
			}
		} else {
			// compound/simple
			CmpndAttrSmplAttrKey cmpndAttrSmplAttrKey = new CmpndAttrSmplAttrKey();
			cmpndAttrSmplAttrKey.setCmpndUserAttrId(userAttrValu.getKey().getCmpndUserAttrId());
			cmpndAttrSmplAttrKey.setSmplUserAttrId(userAttrValu.getKey().getSmplUserAttrId());
			if (daoFactory.getCmpndAttrSmplAttrDAO().selectByPrimaryKey(cmpndAttrSmplAttrKey) == null) {
				throw new DataIntegrityViolationException(String.format("CMPND_USER_ATTR_ID.SMPL_USER_ATTR_ID '%s.%s' is invalid.", userAttrValu.getKey().getCmpndUserAttrId(), userAttrValu.getKey().getSmplUserAttrId()));
			}
		}
		
		logger.debug("RETURN {}", sw);
	}
}
