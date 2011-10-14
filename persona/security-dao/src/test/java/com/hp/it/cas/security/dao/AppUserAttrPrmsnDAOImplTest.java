package com.hp.it.cas.security.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the APP_USER_ATTR_PRMSN table DAO
 * @author CAS Generator v1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

public class AppUserAttrPrmsnDAOImplTest {

    private static final Calendar CALENDAR_NOW = Calendar.getInstance();
	final static private BigDecimal APPPRTFLID = new BigDecimal(1);
	final static private String USERATTRID = "A";
	final static private String CRTUSERID = "A";
	final static private Date CRTTS = new Date();
    
	@Autowired
	ISecurityDAOFactory daoFactory;
	
	/**
	 * Test key equals key is true
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingKeys() {
		AppUserAttrPrmsnKey key = createKey();
		AppUserAttrPrmsnKey key2 = createKey();
		assertTrue(key.equals(key2));
	}
	
	/**
	 * Test key equals a different key is false.
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingKey() {
		AppUserAttrPrmsnKey key = createKey();
		AppUserAttrPrmsnKey key2 = new AppUserAttrPrmsnKey();
		assertFalse(key.equals(key2));
	}
	
	
	/**
	 *  Test key equals null key is false
	 */
	@Test
	public void thatEqualsReturnsFalseForNull() {
		AppUserAttrPrmsnKey key = createKey();
		AppUserAttrPrmsnKey key2 = null;
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals another object is false
	 */
	@Test
	public void thatEqualsReturnsFalseForAnotherObject() {
		AppUserAttrPrmsnKey key = createKey();
		AppUserAttrPrmsn key2 = new AppUserAttrPrmsn();
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test null key equals null key is true
	 */
	@Test
	public void thatEqualsRetrunsTrueForTwoNullKeys() {
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		AppUserAttrPrmsnKey key2 = new AppUserAttrPrmsnKey();
		assertTrue(key.equals(key2));
	}
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem1Null() {
	    AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		AppUserAttrPrmsnKey key2 = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		key2.setAppPrtflId(null);    
		key.setUserAttrId(USERATTRID);
		key2.setUserAttrId(USERATTRID);
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem2Null() {
	    AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		AppUserAttrPrmsnKey key2 = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		key2.setAppPrtflId(APPPRTFLID);
		key.setUserAttrId(USERATTRID);
		key2.setUserAttrId(null);    
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForLikeObjectsAreEqual() {
	     AppUserAttrPrmsnKey key = createKey();
		 AppUserAttrPrmsnKey key2 = createKey();
		 assertTrue(key.hashCode() == key2.hashCode());	 
	 }
	 
	 /**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForDifferentObjectsAreNotEqual() {
	     AppUserAttrPrmsnKey key = createKey();
		 AppUserAttrPrmsn record = createRecord();
		 assertTrue(key.hashCode() != record.hashCode());	 
	 }
	 
	/**
	 * Test bean equals method
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingBeans() {
		AppUserAttrPrmsnKey key = createKey();
		AppUserAttrPrmsn record = createRecord();
		record.setKey(key);
		AppUserAttrPrmsn record2 = createRecord();
		record2.setKey(key);
		assertTrue(record.equals(record2));
	}

	/**
	 * Test bean equals method expecting failure
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingBeans() {
		AppUserAttrPrmsnKey key = createKey();
		AppUserAttrPrmsn record = createRecord();
		record.setKey(key);
		AppUserAttrPrmsn record2 = createRecord();
		AppUserAttrPrmsnKey key2 = new AppUserAttrPrmsnKey();
		record2.setKey(key2);
		assertFalse(record.equals(record2));
	}
	
    /**
	 * Test insert method
	 */
	@Test
	public void thatInsertCanOccurNormally() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		AppUserAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		AppUserAttrPrmsn record2 = dao.selectByPrimaryKey(key);
		assertNotNull(record2);
	}
	
	/**
	 * Test that insert with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithRecordNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		dao.insert(null);
	}

	/**
	 * Test that insert with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithPrimaryKeyNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		record.setKey(null);
		dao.insert(record);
	}
	
    /**
	 * Test that insert with null key column(s) throws verifier exception
	 */
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void thatInsertWithKeyColumnNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		record.setKey(key);
		dao.insert(record);
    }
        
    /**
	 * Test that insert with null value in required column(s) with defaults works
	 */
	 	@Test
		public void thatInsertingRecordWithMissingFieldsInserts() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
        AppUserAttrPrmsn record = new AppUserAttrPrmsn();
		AppUserAttrPrmsnKey key = createKey();
		record.setKey(key);
		AppUserAttrPrmsnKey returnedKey = dao.insert(record);
		AppUserAttrPrmsn newRecord = dao.selectByPrimaryKey(returnedKey);
        assertNotNull(newRecord);
	}
       
	/**
	 * Test that duplicate keys throws exception
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void thatInsertDuplicateKeyThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		AppUserAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		AppUserAttrPrmsn record2 = createRecord();
		record2.setKey(key);
		dao.insert(record2);
	}
	
    /**
	 * Test select works as expected
	 */
	@Test
    public void thatSelectCanOccurNormally() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		AppUserAttrPrmsnKey key = createKey();
		record.setKey(key);
		
		dao.insert(record);
		AppUserAttrPrmsn selectedRecord = dao.selectByPrimaryKey(key);
		assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatSelectWithPrimaryKeyNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		dao.selectByPrimaryKey(null);
	}
	
	/**
	 * Test that select with null value in key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatSelectWithKeyColumnNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		dao.selectByPrimaryKey(key);
	}

    
    /**
	 * Test that discretionary select works as expected
	 */
    @Test
	public void thatDiscretionaryFindCanOccurNormally() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		AppUserAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		AppUserAttrPrmsn record2 = new AppUserAttrPrmsn();
        AppUserAttrPrmsnKey key2 = new AppUserAttrPrmsnKey();
        
        key2.setAppPrtflId(APPPRTFLID);        
        key2.setUserAttrId("B");   
        record2.setKey(key2);
		record2.setCrtUserId("B");
		record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
		dao.insert(record2);
		
              
		AppUserAttrPrmsnKey key3 = new AppUserAttrPrmsnKey();
        key3.setAppPrtflId(APPPRTFLID);
        key3.setUserAttrId(null);   
        List<AppUserAttrPrmsn> selectedRecords = dao.selectByPrimaryKeyDiscretionary(key3);
		Assert.assertEquals(2, selectedRecords.size());
	}
	
    /**
	 * Test that discretionary select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithPrimaryKeyNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		dao.selectByPrimaryKeyDiscretionary(null);
	}
	
	/**
	 * Test that discretionary select with first ordinal null throws exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithFirstKeyColumnNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(null);
		dao.selectByPrimaryKeyDiscretionary(key);
	}
    /**
	 * Test that select by index works as expected
	 */
	@Test
	public void thatSelectByIe1AppUserAttrPrmsnCanOccurNormally() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		AppUserAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		AppUserAttrPrmsn record2 = new AppUserAttrPrmsn();
		AppUserAttrPrmsnKey key2 = new AppUserAttrPrmsnKey();;
        key2.setAppPrtflId(new BigDecimal(2));
        key2.setUserAttrId(USERATTRID);
        record2.setCrtUserId("B");
        record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
          
        record2.setKey(key2);
		dao.insert(record2);
		List<AppUserAttrPrmsn> selectedRecords = dao.selectByIe1AppUserAttrPrmsn(USERATTRID );
		Assert.assertEquals(2, selectedRecords.size());
	}
	
	/**
	 * Test that select by index with null index first ordinal throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatIndexSelectByIe1AppUserAttrPrmsnWithIndexNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		dao.selectByIe1AppUserAttrPrmsn(null );
	}   
	
	/**
	 * Test that delete works as expected
	 */
	@Test
	public void thatDeleteCanOccurNormally(){
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsn record = createRecord();
		AppUserAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		dao.deleteByPrimaryKey(key);
	}

	/**
	 * Test that delete with null key throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithPrimaryKeyNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		dao.deleteByPrimaryKey(null);
	}
	
	/**
	 * Test that delete with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithKeyColumnNulledThrowsException() {
		IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		dao.deleteByPrimaryKey(key);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByAppPrtflIdByUserAttrId(null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), BigDecimal.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), null, null, null, Integer.valueOf(21), Integer.valueOf(40)); 
        assertTrue(records.size() == 20);

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithSameMinMaxWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), BigDecimal.valueOf(1), null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 10);
        
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithoutRowLimitsWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1AppUserAttrPrmsnWithNullValuesThrowsException() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
    
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByUserAttrId(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1AppUserAttrPrmsnWithRowMinNullAndRowMaxSetThrowsException() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByUserAttrId(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByIe1AppUserAttrPrmsnWithRowLimitsWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByUserAttrId(String.valueOf(1), null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1AppUserAttrPrmsnWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByUserAttrId(String.valueOf(1), null, Integer.valueOf(21), Integer.valueOf(40));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1AppUserAttrPrmsnWithoutRowLimitsWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDao.findRangeByUserAttrId(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
    
        
    @Test(expected = IllegalArgumentException.class)
    public void thatFindDistinctIe1AppUserAttrPrmsnWithNullValuesThrowsException() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        
        List<Hashtable> records = appUserAttrPrmsnDao.findDistinctByUserAttrId(null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void thatFindDistinctIe1AppUserAttrPrmsnOnTheHappyPathWorksAsExpected() {
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = daoFactory.getAppUserAttrPrmsnDAO();
        
        AppUserAttrPrmsn appUserAttrPrmsn = createRecord(1, 1);
        
        appUserAttrPrmsn.setKey(createKey(1, 1));
        appUserAttrPrmsn.getKey().setUserAttrId("1");
        appUserAttrPrmsnDao.insert(appUserAttrPrmsn);
        
        AppUserAttrPrmsn appUserAttrPrmsn2 = createRecord(1, 1);
        appUserAttrPrmsn2.setKey(createKey(2, 1));
        appUserAttrPrmsn2.getKey().setUserAttrId("1");
        appUserAttrPrmsnDao.insert(appUserAttrPrmsn2);
        
        AppUserAttrPrmsn appUserAttrPrmsn3 = createRecord(1, 1);
        appUserAttrPrmsn3.setKey(createKey(1, 1));
        appUserAttrPrmsn3.getKey().setUserAttrId("2");
        appUserAttrPrmsnDao.insert(appUserAttrPrmsn3);
        
        List<Hashtable> records = appUserAttrPrmsnDao.findDistinctByUserAttrId("1");
        assertNotNull(records);
        assertTrue(records.size() == 1);
        
    }   
    
   	/**
	 * Helper method for creating a record
	 * @return record  a DB record with all columns set
	 */
	public static AppUserAttrPrmsn createRecord() {
		AppUserAttrPrmsn record = new AppUserAttrPrmsn();
		record.setCrtUserId(CRTUSERID);
		record.setCrtTs(CRTTS);

		return record;
	}

	/**
	 * Helper method for creating a key
	 * @return key  a record key with all columns set
	 */
	public static AppUserAttrPrmsnKey createKey() {
		AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		key.setUserAttrId(USERATTRID);

		return key;
	}
	
	/**
     * Helper method for creating a record when adding multiple records.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static AppUserAttrPrmsn createRecord(int g, int r) {
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, r);

        
        AppUserAttrPrmsn appUserAttrPrmsn = new AppUserAttrPrmsn();
        appUserAttrPrmsn.setCrtUserId(String.valueOf(r));
        appUserAttrPrmsn.setCrtTs(calendar.getTime());

        return appUserAttrPrmsn;
    }

    /**
     * Helper method for creating a key when adding multiple records.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static AppUserAttrPrmsnKey createKey(int g, int r) {
        AppUserAttrPrmsnKey appUserAttrPrmsnKey = new AppUserAttrPrmsnKey();
        appUserAttrPrmsnKey.setAppPrtflId(BigDecimal.valueOf(g));
        appUserAttrPrmsnKey.setUserAttrId(String.valueOf(g * 10 + r));

        return appUserAttrPrmsnKey;
    }
    
    public void populateData() {

    IAppUserAttrPrmsnDAO dao = daoFactory.getAppUserAttrPrmsnDAO();

    AppUserAttrPrmsn appUserAttrPrmsn = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                appUserAttrPrmsn = createRecord(g, r);
                appUserAttrPrmsn.setKey(createKey(g, r));

                dao.insert(appUserAttrPrmsn);
            }
        }
    }
    
}