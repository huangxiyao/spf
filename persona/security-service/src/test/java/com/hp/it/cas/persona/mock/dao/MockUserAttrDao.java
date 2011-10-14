package com.hp.it.cas.persona.mock.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
import com.hp.it.cas.xa.validate.Verifier;

public class MockUserAttrDao implements IUserAttrDAO {

	private final MockDatabase database;
	private int userAttributeKeySequence = 0;
	
	MockUserAttrDao(MockDatabase database) {
		this.database = database;
	}
	
	public UserAttr selectByPrimaryKey(UserAttrKey key) {
		return database.userAttrTable.get(key);
	}

	public UserAttrKey insert(UserAttr userAttr) {
		if (database.userAttrTable.containsKey(userAttr.getKey())) {
			throw new DataIntegrityViolationException("Duplicate primary key.");
		}
		if (selectByAk1UserAttr(userAttr.getUserAttrNm()) != null) {
			throw new DataIntegrityViolationException("Duplicate unique key.");
		}
		validateForInsert(userAttr);
		database.userAttrTable.put(userAttr.getKey(), userAttr);
		return userAttr.getKey();
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

	public int deleteByPrimaryKey(UserAttrKey key) {
		return database.userAttrTable.remove(key) == null ? 0 : 1;
	}

	public UserAttr selectByAk1UserAttr(String userAttrNm) {
		UserAttr result = null;
		for (Iterator<UserAttr> i = database.userAttrTable.values().iterator(); i.hasNext() && result == null;) {
			UserAttr userAttr = i.next();
			result = userAttrNm.equals(userAttr.getUserAttrNm()) ? userAttr : null;
		}
		return result;
	}

	public int updateByPrimaryKeySelective(UserAttr record) {
		if (! database.userAttrTable.containsKey(record.getKey())) {
			throw new DataIntegrityViolationException("Can't update non-existent record.");
		}
		validateForUpdate(record);
		return database.userAttrTable.put(record.getKey(), record) == null ? 0 : 1;
	}

	private void validateForInsert(UserAttr record) {
		new Verifier()
    		.isNotEmpty(record.getCrtUserId(), "Null CRT_USER_ID")
    		.throwIfError();
		
		validateRequiredFields(record);
	}

	private void validateForUpdate(UserAttr record) {
		new Verifier()
    		.isNotEmpty(record.getLastMaintUserId(), "Null LAST_MAINT_USER_ID")
    		.throwIfError();
		
		validateRequiredFields(record);
	}
	
	private void validateRequiredFields(UserAttr record) {
		new Verifier()
    		.isNotNull(record.getKey().getUserAttrId(),	"Null USER_ATTR_ID")
    		.isNotNull(record.getUserAttrTypeCd(), 		"Null USER_ATTR_TYPE_CD")
    		.isNotNull(record.getUserAttrNm(), 			"Null USER_ATTR_NM")
    		.isNotNull(record.getUserAttrDn(),			"Null USER_ATTR_DN")
    		.isNotNull(record.getUserAttrDefnTx(),		"Null USER_ATTR_DEFN_TX")
    		.throwIfError();
		
		String userAttrTypeCd = record.getUserAttrTypeCd();
		new Verifier()
    		.isTrue(userAttrTypeCd.equals("SMPL") || userAttrTypeCd.equals("CMPND"), "USER_ATTRY_TYPE_CD must be 'SMPL' or 'CMPND'")
    		.throwIfError();
	}

	public List<UserAttr> selectByIe1UserAttr(String arg0) {
        throw new UnsupportedOperationException();
	}

    public List<UserAttr> findRangeByUserAttrId(String userAttrIdMin, String userAttrIdMax, Integer rownumMin,
            Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

    public List<UserAttr> findRangeByUserAttrNm(String userAttrNmMin, String userAttrNmMax, Integer rownumMin,
            Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

    public List<UserAttr> findRangeByUserAttrTypeCd(String userAttrTypeCdMin, String userAttrTypeCdMax,
            Integer rownumMin, Integer rownumMax) {
        throw new UnsupportedOperationException();
    }
}
