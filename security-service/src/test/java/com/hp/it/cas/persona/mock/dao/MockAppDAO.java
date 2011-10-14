package com.hp.it.cas.persona.mock.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.config.dao.App;
import com.hp.it.cas.config.dao.AppKey;
import com.hp.it.cas.config.dao.IAppDAO;

class MockAppDAO implements IAppDAO {

	private final MockDatabase database;
	
	MockAppDAO(MockDatabase database) {
		this.database = database;
	}
	
	public int deleteByPrimaryKey(AppKey key) {
		return database.appTable.remove(key) == null ? 0 : 1;
	}

	public AppKey insert(App record) {
		AppKey key = record.getKey();
		if (database.appTable.containsKey(key)) {
			throw new DataIntegrityViolationException("Duplicate primary key.");
		}
		database.appTable.put(key, record);
		return key;
	}

	public App selectByPrimaryKey(AppKey key) {
		return database.appTable.get(key);
	}

	public int updateByPrimaryKeySelective(App record) {
		AppKey key = record.getKey();
		database.appTable.put(key, record);
		return 1;
	}

    public List<App> findRangeByAppPrtflId(BigDecimal appPrtflIdMin, BigDecimal appPrtflIdMax, Integer rownumMin,
            Integer rownumMax) {
        throw new UnsupportedOperationException();
    }

}
