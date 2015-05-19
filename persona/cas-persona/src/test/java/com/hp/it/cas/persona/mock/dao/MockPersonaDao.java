package com.hp.it.cas.persona.mock.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hp.it.cas.persona.dao.IPersonaDao;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttr;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
import com.hp.it.cas.security.dao.UserAttrValuKey;

class MockPersonaDao implements IPersonaDao {

	private final MockDatabase database;
	
	MockPersonaDao(MockDatabase database) {
		this.database = database;
	}
	
	public Set<UserAttr> findCompoundAttributesForSimpleAttribute(String simpleUserAttributeIdentifier) {
		Set<UserAttr> result = new HashSet<UserAttr>();
		
		for (CmpndAttrSmplAttr cmpndAttrSmplAttr : database.cmpndAttrSmplAttrTable.values()) {
			if (cmpndAttrSmplAttr.getKey().getSmplUserAttrId().equals(simpleUserAttributeIdentifier)) {
				UserAttrKey key = new UserAttrKey();
				key.setUserAttrId(cmpndAttrSmplAttr.getKey().getCmpndUserAttrId());
				result.add(database.userAttrTable.get(key));
			}
		}
		
		return result;
	}

	public Set<UserAttr> findSimpleAttributesForCompoundAttribute(String compoundUserAttributeIdentifier) {
		Set<UserAttr> result = new HashSet<UserAttr>();
		
		for (CmpndAttrSmplAttr cmpndAttrSmplAttr : database.cmpndAttrSmplAttrTable.values()) {
			if (cmpndAttrSmplAttr.getKey().getCmpndUserAttrId().equals(compoundUserAttributeIdentifier)) {
				UserAttrKey key = new UserAttrKey();
				key.setUserAttrId(cmpndAttrSmplAttr.getKey().getSmplUserAttrId());
				result.add(database.userAttrTable.get(key));
			}
		}
		
		return result;
	}

	public Set<UserAttr> selectAllCompoundAttributes() {
		Set<UserAttr> result = new HashSet<UserAttr>();
		
		for (UserAttr userAttr : database.userAttrTable.values()) {
			if ("CMPND".equals(userAttr.getUserAttrTypeCd())) {
				result.add(userAttr);
			}
		}
		
		return result;
	}

	public Set<UserAttr> selectAllSimpleAttributes() {
		Set<UserAttr> result = new HashSet<UserAttr>();
		
		for (UserAttr userAttr : database.userAttrTable.values()) {
			if ("SMPL".equals(userAttr.getUserAttrTypeCd())) {
				result.add(userAttr);
			}
		}
		
		return result;
	}

	public int countCmpndAttrSmplAttrWithUserAttrId(String userAttrId) {
		int count = 0;
		for (CmpndAttrSmplAttrKey cmpndAttrSmplAttrKey : database.cmpndAttrSmplAttrTable.keySet()) {
			if (cmpndAttrSmplAttrKey.getCmpndUserAttrId().equals(userAttrId)
					|| cmpndAttrSmplAttrKey.getSmplUserAttrId().equals(userAttrId)) {
				++count;
			}
		}
		return count;
	}

	public int countUserAttrValuWithUserAttrId(String userAttrKy) {
		int count = 0;
		for (UserAttrValuKey userAttrValuKey : database.userAttrValuTable.keySet()) {
			if (userAttrValuKey.getSmplUserAttrId().equals(userAttrKy)
					|| userAttrValuKey.getCmpndUserAttrId().equals(userAttrKy)) {
				++count;
			}
		}
		return count;
	}

	public int deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(String userAttrId) {
		int count = 0;
		for (Iterator<AppCmpndAttrSmplAttrPrmsnKey> i = database.appCmpndAttrSmplAttrPrmsnTable.keySet().iterator(); i.hasNext();) {
			AppCmpndAttrSmplAttrPrmsnKey key = i.next();
			if (key.getCmpndUserAttrId().equals(userAttrId)
					|| key.getSmplUserAttrId().equals(userAttrId)) {
				i.remove();
				++count;
			}
		}
		return count;
	}

	public int deleteAppUserAttrPrmsnWithUserAttrId(String userAttrId) {
		int count = 0;
		for (Iterator<AppUserAttrPrmsnKey> i = database.appUserAttrPrmsnTable.keySet().iterator(); i.hasNext();) {
			AppUserAttrPrmsnKey key = i.next();
			if (key.getUserAttrId().equals(userAttrId)) {
				i.remove();
				++count;
			}
		}
		return count;
	}

	public int countUserAttrValuWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key) {
		int count = 0;
		for (UserAttrValuKey userAttrValuKey : database.userAttrValuTable.keySet()) {
			if (userAttrValuKey.getSmplUserAttrId().equals(key.getSmplUserAttrId())
					&& userAttrValuKey.getCmpndUserAttrId().equals(key.getCmpndUserAttrId())) {
				++count;
			}
		}
		return count;
	}

	public int deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key) {
		int count = 0;
		for (Iterator<AppCmpndAttrSmplAttrPrmsnKey> i = database.appCmpndAttrSmplAttrPrmsnTable.keySet().iterator(); i.hasNext();) {
			AppCmpndAttrSmplAttrPrmsnKey k = i.next();
			if (k.getCmpndUserAttrId().equals(key.getCmpndUserAttrId())
					&& k.getSmplUserAttrId().equals(key.getSmplUserAttrId())) {
				i.remove();
				++count;
			}
		}
		return count;
	}
}
