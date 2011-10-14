package com.hp.it.cas.persona.mock.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.security.dao.AppUserAttrPrmsn;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.xa.validate.Verifier;

class MockAppUserAttrPrmsnDao implements IAppUserAttrPrmsnDAO {

	private final MockDatabase database;
	
	MockAppUserAttrPrmsnDao(MockDatabase database) {
		this.database = database;
	}
	
	public AppUserAttrPrmsn selectByPrimaryKey(AppUserAttrPrmsnKey key) {
		return database.appUserAttrPrmsnTable.get(key);
	}

	public AppUserAttrPrmsnKey insert(AppUserAttrPrmsn appUserAttrPrmsn) {
		if (database.appUserAttrPrmsnTable.containsKey(appUserAttrPrmsn.getKey())) {
			throw new DataIntegrityViolationException("Duplicate primary key.", null);
		}
		
		validateForInsert(appUserAttrPrmsn);
		database.appUserAttrPrmsnTable.put(appUserAttrPrmsn.getKey(), appUserAttrPrmsn);
		return appUserAttrPrmsn.getKey();
	}

	public List<AppUserAttrPrmsn> selectByPrimaryKeyDiscretionary(AppUserAttrPrmsnKey key) {
		List<AppUserAttrPrmsn> result = new ArrayList<AppUserAttrPrmsn>();
		for (Entry<AppUserAttrPrmsnKey, AppUserAttrPrmsn> entry : database.appUserAttrPrmsnTable.entrySet()) {
			if (entry.getKey().getAppPrtflId().equals(key.getAppPrtflId()) &&
					(key.getUserAttrId() == null || key.getUserAttrId().equals(entry.getKey().getUserAttrId()))) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	public int deleteByPrimaryKey(AppUserAttrPrmsnKey key) {
		return database.appUserAttrPrmsnTable.remove(key) == null ? 0 : 1;
	}

	public int updateByPrimaryKeySelective(AppUserAttrPrmsn record) {
		if (! database.appUserAttrPrmsnTable.containsKey(record.getKey())) {
			throw new DataIntegrityViolationException("Cannot update non-existant record.");
		}
		
		validateForUpdate(record);
		database.appUserAttrPrmsnTable.put(record.getKey(), record);
		return 1;
	}

	private void validateForInsert(AppUserAttrPrmsn record) {
		new Verifier()
    		.isNotEmpty(record.getCrtUserId(), "Null CRT_USER_ID")
    		.throwIfError();
		
		validateRequiredFields(record);
	}

	private void validateForUpdate(AppUserAttrPrmsn record) {
		validateRequiredFields(record);
	}
	
	private void validateRequiredFields(AppUserAttrPrmsn record) {
		new Verifier()
    		.isNotNull(record.getKey().getAppPrtflId(),	"Null APP_PRTFL_ID")
    		.isNotNull(record.getKey().getUserAttrId(), "Null USER_ATTR_ID")
    		.throwIfError();
	}

	public List<AppUserAttrPrmsn> selectByIe1AppUserAttrPrmsn(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

    public List<Hashtable> findDistinctByUserAttrId(String userAttrId) {
        throw new UnsupportedOperationException();
    }

    public List<AppUserAttrPrmsn> findRangeByAppPrtflIdByUserAttrId(BigDecimal appPrtflIdMin, BigDecimal appPrtflIdMax,
            String userAttrIdMin, String userAttrIdMax, Integer rownumMin, Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

    public List<AppUserAttrPrmsn> findRangeByUserAttrId(String userAttrIdMin, String userAttrIdMax, Integer rownumMin,
            Integer rownumMax) {
        throw new UnsupportedOperationException();
    }
}
