package com.hp.it.cas.persona.mock.dao;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.security.dao.CmpndAttrSmplAttr;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.xa.validate.Verifier;

class MockCmpndAttrSmplAttrDao implements ICmpndAttrSmplAttrDAO {

	private final MockDatabase database;
	
	MockCmpndAttrSmplAttrDao(MockDatabase database) {
		this.database = database;
	}
	
	public CmpndAttrSmplAttrKey insert(CmpndAttrSmplAttr cmpndAttrSmplAttr) {
		if (database.cmpndAttrSmplAttrTable.containsKey(cmpndAttrSmplAttr.getKey())) {
			throw new DataIntegrityViolationException("Duplicate primary key.");
		}
		validateForInsert(cmpndAttrSmplAttr);
		database.cmpndAttrSmplAttrTable.put(cmpndAttrSmplAttr.getKey(), cmpndAttrSmplAttr);
		return cmpndAttrSmplAttr.getKey();
	}

	public int deleteByPrimaryKey(CmpndAttrSmplAttrKey key) {
		return database.cmpndAttrSmplAttrTable.remove(key) == null ? 0 : 1;
	}

	public CmpndAttrSmplAttr selectByPrimaryKey(CmpndAttrSmplAttrKey key) {
		return database.cmpndAttrSmplAttrTable.get(key);
	}

	public List<CmpndAttrSmplAttr> selectByPrimaryKeyDiscretionary(CmpndAttrSmplAttrKey key) {
		List<CmpndAttrSmplAttr> result = new ArrayList<CmpndAttrSmplAttr>();
		for (Entry<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> entry : database.cmpndAttrSmplAttrTable.entrySet()) {
			if (key.getCmpndUserAttrId() == null || key.getCmpndUserAttrId().equals(entry.getKey().getCmpndUserAttrId()) &&
					(key.getSmplUserAttrId() == null || key.getSmplUserAttrId().equals(entry.getKey().getSmplUserAttrId()))) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	public int updateByPrimaryKeySelective(CmpndAttrSmplAttr record) {
		if (! database.cmpndAttrSmplAttrTable.containsKey(record.getKey())) {
			throw new DataIntegrityViolationException("Can't update non-existent record.");
		}
		validateForUpdate(record);
		return database.cmpndAttrSmplAttrTable.put(record.getKey(), record) == null ? 0 : 1;
	}

	private void validateForInsert(CmpndAttrSmplAttr record) {
		new Verifier()
    		.isNotEmpty(record.getCrtUserId(), "Null CRT_USER_ID")
    		.throwIfError();
		
		validateRequiredFields(record);
	}

	private void validateForUpdate(CmpndAttrSmplAttr record) {
		validateRequiredFields(record);
	}
	
	private void validateRequiredFields(CmpndAttrSmplAttr record) {
		new Verifier()
    		.isNotNull(record.getKey().getCmpndUserAttrId(),	"Null CMPND_USER_ATTR_ID")
    		.isNotNull(record.getKey().getSmplUserAttrId(), 	"Null SMPL_USER_ATTR_ID")
    		.throwIfError();
	}

	public List<CmpndAttrSmplAttr> selectByIe1CmpndAttrSmplAttr(String arg0) {
        throw new UnsupportedOperationException();
	}

    public List<Hashtable> findDistinctBySmplUserAttrId(String smplUserAttrId) {
        throw new UnsupportedOperationException();
    }

    public List<CmpndAttrSmplAttr> findRangeByCmpndUserAttrIdBySmplUserAttrId(String cmpndUserAttrIdMin,
            String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin,
            Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

    public List<CmpndAttrSmplAttr> findRangeBySmplUserAttrId(String smplUserAttrIdMin, String smplUserAttrIdMax,
            Integer rownumMin, Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

}
