package com.hp.it.cas.persona.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hp.it.cas.security.custom.dao.ISecurityCustomDAOFactory;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.UserAttr;

class PersonaDao implements IPersonaDao {
	
	private final ISecurityCustomDAOFactory userAttributesCustomDaoFactory;
	
	PersonaDao(ISecurityCustomDAOFactory userAttributesCustomDaoFactory) {
		this.userAttributesCustomDaoFactory = userAttributesCustomDaoFactory;
	}

	public Set<UserAttr> findCompoundAttributesForSimpleAttribute(String simpleUserAttributeIdentifier) {
		List<UserAttr> attributes = userAttributesCustomDaoFactory.getUserAttrCustomDAO().findCompoundAttributesForSimpleAttribute(simpleUserAttributeIdentifier);
		return new HashSet<UserAttr>(attributes);
	}

	public Set<UserAttr> findSimpleAttributesForCompoundAttribute(String compoundUserAttributeIdentifier) {
		List<UserAttr> attributes = userAttributesCustomDaoFactory.getUserAttrCustomDAO().findSimpleAttributesForCompoundAttribute(compoundUserAttributeIdentifier);
		return new HashSet<UserAttr>(attributes);
	}

	public Set<UserAttr> selectAllCompoundAttributes() {
		List<UserAttr> attributes = userAttributesCustomDaoFactory.getUserAttrCustomDAO().selectAllCompoundAttributes();
		return new HashSet<UserAttr>(attributes);
	}

	public Set<UserAttr> selectAllSimpleAttributes() {
		List<UserAttr> attributes = userAttributesCustomDaoFactory.getUserAttrCustomDAO().selectAllSimpleAttributes();
		return new HashSet<UserAttr>(attributes);
	}

	public int countCmpndAttrSmplAttrWithUserAttrId(String userAttrId) {
		return userAttributesCustomDaoFactory.getCmpndAttrSmplAttrCustomDAO().countCmpndAttrSmplAttrWithUserAttrId(userAttrId);
	}

	public int countUserAttrValuWithUserAttrId(String userAttrKy) {
		return userAttributesCustomDaoFactory.getUserAttrValuCustomDAO().countUserAttrValuWithUserAttrId(userAttrKy);
	}

	public int deleteAppUserAttrPrmsnWithUserAttrId(String userAttrId) {
		return userAttributesCustomDaoFactory.getAppUserAttrPrmsnCustomDAO().deleteAppUserAttrPrmsnWithUserAttrId(userAttrId);
	}

	public int deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(String userAttrId) {
		return userAttributesCustomDaoFactory.getAppCmpndAttrSmplAttrPrmsnCustomDAO().deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(userAttrId);
	}

	public int countUserAttrValuWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key) {
		return userAttributesCustomDaoFactory.getUserAttrValuCustomDAO().countUserAttrValuWithCmpndAttrSmplAttrKey(key);
	}

	public int deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key) {
		return userAttributesCustomDaoFactory.getAppCmpndAttrSmplAttrPrmsnCustomDAO().deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey(key);
	}
	
}
