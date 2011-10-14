package com.hp.it.cas.persona.mock.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.security.dao.IUserAttrValuDAO;
import com.hp.it.cas.security.dao.UserAttrValu;
import com.hp.it.cas.security.dao.UserAttrValuKey;
import com.hp.it.cas.xa.validate.Verifier;

public class MockUserAttrValuDao implements IUserAttrValuDAO {

	private final MockDatabase database;
	
	MockUserAttrValuDao(MockDatabase database) {
		this.database = database;
	}
	
	public List<UserAttrValu> selectByPrimaryKeyDiscretionary(UserAttrValuKey key) {
		System.out.println("QUERY  " + key);

		List<UserAttrValu> result = new ArrayList<UserAttrValu>();
		
		for (UserAttrValu attribute : database.userAttrValuTable.values()) {
			UserAttrValuKey dataKey = attribute.getKey();
			boolean match = dataKey.getUserId().equals(key.getUserId()) && dataKey.getUserIdTypeCd().equals(key.getUserIdTypeCd());
			
			match &= key.getCmpndUserAttrId() == null || key.getCmpndUserAttrId().equals(dataKey.getCmpndUserAttrId());
			match &= key.getSmplUserAttrId() == null || key.getSmplUserAttrId().equals(dataKey.getSmplUserAttrId());
			match &= key.getUserAttrInstncId() == null || key.getUserAttrInstncId().equals(dataKey.getUserAttrInstncId());

			if (match) {
				result.add(attribute);
			}
		}
		
		return result;
	}

	public int deleteByPrimaryKey(UserAttrValuKey key) {
		System.out.println("DELETE " + key);

		return database.userAttrValuTable.remove(key) == null ? 0 : 1;
	}
	
	public List<UserAttrValu> selectByIe1UserAttrValu(String cmpndUserAttrId, String smplUserAttrId, String userAttrValuTx) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserAttrValu selectByPrimaryKey(UserAttrValuKey key) {
		System.out.println("QUERY  " + key);

		return database.userAttrValuTable.get(key);
	}

	public int updateByPrimaryKeySelective(UserAttrValu record) {
		System.out.println("UPDATE " + record + " " + record.getUserAttrValuTx());

		if (! database.userAttrValuTable.containsKey(record.getKey())) {
			throw new DataIntegrityViolationException("Cannot update non-existant record.");
		}
		
		validateForUpdate(record);
		return database.userAttrValuTable.put(record.getKey(), record) == null ? 0 : 1;
	}

	public UserAttrValuKey insert(UserAttrValu userAttrValu) {
		System.out.println("INSERT " + userAttrValu + " " + userAttrValu.getUserAttrValuTx());

		validateForInsert(userAttrValu);
		database.userAttrValuTable.put(userAttrValu.getKey(), userAttrValu);
		return userAttrValu.getKey();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (UserAttrValu value : database.userAttrValuTable.values()) {
			sb.append(value).append("\n");
		}
		return sb.toString();
	}
	
	private void validateForInsert(UserAttrValu record) {
		new Verifier()
    		.isNotEmpty(record.getCrtUserId(), "Null CRT_USER_ID")
    		.throwIfError();
		
		validateRequiredFields(record);
	}

	private void validateForUpdate(UserAttrValu record) {
		new Verifier()
    		.isNotEmpty(record.getLastMaintUserId(), "Null LAST_MAINT_USER_ID")
    		.throwIfError();
		
		validateRequiredFields(record);
	}
	
	private void validateRequiredFields(UserAttrValu record) {
		UserAttrValuKey userAttrKy = record.getKey();
		
		new Verifier()
    		.isNotNull(userAttrKy.getUserId(),				"Null USER_ID")
    		.isNotNull(userAttrKy.getUserIdTypeCd(), 		"Null USER_ID_TYPE_CD")
    		.isNotNull(userAttrKy.getCmpndUserAttrId(), 	"Null CMPND_USER_ATTR_ID")
    		.isNotNull(userAttrKy.getSmplUserAttrId(), 		"Null SMPL_USER_ATTR_ID")
    		.isNotNull(userAttrKy.getUserAttrInstncId(),	"Null USER_ATTR_INSTNC_ID")
    		.isNotNull(record.getUserAttrValuTx(),			"Null USER_ATTR_VALU_TX")
    		.throwIfError();
		
		String userIdTypeCd = userAttrKy.getUserIdTypeCd();
		new Verifier()
    		.isTrue(userIdTypeCd.equals("EXT") || userIdTypeCd.equals("EMP") || userIdTypeCd.equals("EMP_SEA"), "USER_ID_TYPE_CD must be 'EXT', 'EMP' or 'EMP_SEA'")
    		.throwIfError();
	}

    public List<UserAttrValu> findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String cmpndUserAttrIdMin,
            String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, String userAttrValuTxMin,
            String userAttrValuTxMax, Integer rownumMin, Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

    public List<UserAttrValu> findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(
            String userIdMin, String userIdMax, String userIdTypeCdMin, String userIdTypeCdMax,
            String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax,
            String userAttrInstncIdMin, String userAttrInstncIdMax, Integer rownumMin, Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

}
