package com.hp.it.cas.persona.mock.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsn;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.xa.validate.Verifier;

class MockAppCmpndAttrSmplAttrPrmsnDao implements IAppCmpndAttrSmplAttrPrmsnDAO {

	private final MockDatabase database;
	
	MockAppCmpndAttrSmplAttrPrmsnDao(MockDatabase database) {
		this.database = database;
	}
	
	public AppCmpndAttrSmplAttrPrmsn selectByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey key) {
		return database.appCmpndAttrSmplAttrPrmsnTable.get(key);
	}

	public AppCmpndAttrSmplAttrPrmsnKey insert(AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn) {
		if (database.appCmpndAttrSmplAttrPrmsnTable.containsKey(appCmpndAttrSmplAttrPrmsn.getKey())) {
			throw new DataIntegrityViolationException("Duplicate primary key.", null);
		}
		
		validateForInsert(appCmpndAttrSmplAttrPrmsn);
		database.appCmpndAttrSmplAttrPrmsnTable.put(appCmpndAttrSmplAttrPrmsn.getKey(), appCmpndAttrSmplAttrPrmsn);
		return appCmpndAttrSmplAttrPrmsn.getKey();
	}

	public List<AppCmpndAttrSmplAttrPrmsn> selectByPrimaryKeyDiscretionary(AppCmpndAttrSmplAttrPrmsnKey key) {
		List<AppCmpndAttrSmplAttrPrmsn> result = new ArrayList<AppCmpndAttrSmplAttrPrmsn>();
		
		for (Entry<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> entry : database.appCmpndAttrSmplAttrPrmsnTable.entrySet()) {
			if (entry.getKey().getAppPrtflId().equals(key.getAppPrtflId()) &&
					((key.getCmpndUserAttrId() == null || key.getCmpndUserAttrId().equals(entry.getKey().getCmpndUserAttrId()))
					|| (key.getSmplUserAttrId() == null || key.getSmplUserAttrId().equals(entry.getKey().getSmplUserAttrId())))) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	public int deleteByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey key) {
		return database.appCmpndAttrSmplAttrPrmsnTable.remove(key) == null ? 0 : 1;
	}

	public int updateByPrimaryKeySelective(AppCmpndAttrSmplAttrPrmsn record) {
		if (! database.appCmpndAttrSmplAttrPrmsnTable.containsKey(record.getKey())) {
			throw new DataIntegrityViolationException("Cannot update non-existant record.");
		}

		validateForUpdate(record);
		database.appCmpndAttrSmplAttrPrmsnTable.put(record.getKey(), record);
		return 1;
	}

	private void validateForInsert(AppCmpndAttrSmplAttrPrmsn record) {
		new Verifier()
    		.isNotEmpty(record.getCrtUserId(), "Null CRT_USER_ID")
    		.throwIfError();
		
		validateRequiredFields(record);
	}

	private void validateForUpdate(AppCmpndAttrSmplAttrPrmsn record) {
		validateRequiredFields(record);
	}
	
	private void validateRequiredFields(AppCmpndAttrSmplAttrPrmsn record) {
		new Verifier()
    		.isNotNull(record.getKey().getAppPrtflId(),			"Null APP_PRTFL_ID")
    		.isNotNull(record.getKey().getCmpndUserAttrId(),	"Null CMPND_USER_ATTR_ID")
    		.isNotNull(record.getKey().getSmplUserAttrId(),		"Null SMPL_USER_ATTR_ID")
    		.throwIfError();
	}

	public List<AppCmpndAttrSmplAttrPrmsn> selectByIe1AppCmpndAttrSmplAttrP(String arg0, String arg1) {
		throw new UnsupportedOperationException();
	}

    public List<Hashtable> findDistinctByCmpndUserAttrIdBySmplUserAttrId(String cmpndUserAttrId, String smplUserAttrId) {
        throw new UnsupportedOperationException();
    }

    public List<AppCmpndAttrSmplAttrPrmsn> findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(
            BigDecimal appPrtflIdMin, BigDecimal appPrtflIdMax, String cmpndUserAttrIdMin, String cmpndUserAttrIdMax,
            String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin, Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

    public List<AppCmpndAttrSmplAttrPrmsn> findRangeByCmpndUserAttrIdBySmplUserAttrId(String cmpndUserAttrIdMin,
            String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin,
            Integer rownumMax) {
        throw new UnsupportedOperationException();
    }
}
