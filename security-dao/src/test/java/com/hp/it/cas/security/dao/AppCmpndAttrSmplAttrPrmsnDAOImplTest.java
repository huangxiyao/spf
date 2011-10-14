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
 * Test class for the APP_CMPND_ATTR_SMPL_ATTR_PRMSN table DAO
 * @author CAS Generator v1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

public class AppCmpndAttrSmplAttrPrmsnDAOImplTest {

    private static final Calendar CALENDAR_NOW = Calendar.getInstance();
	final static private BigDecimal APPPRTFLID = new BigDecimal(1);
	final static private String CMPNDUSERATTRID = "A";
	final static private String SMPLUSERATTRID = "A";
	final static private String CRTUSERID = "A";
	final static private Date CRTTS = new Date();
    
	@Autowired
	ISecurityDAOFactory daoFactory;
	
	/**
	 * Test key equals key is true
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingKeys() {
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		AppCmpndAttrSmplAttrPrmsnKey key2 = createKey();
		assertTrue(key.equals(key2));
	}
	
	/**
	 * Test key equals a different key is false.
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingKey() {
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		AppCmpndAttrSmplAttrPrmsnKey key2 = new AppCmpndAttrSmplAttrPrmsnKey();
		assertFalse(key.equals(key2));
	}
	
	
	/**
	 *  Test key equals null key is false
	 */
	@Test
	public void thatEqualsReturnsFalseForNull() {
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		AppCmpndAttrSmplAttrPrmsnKey key2 = null;
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals another object is false
	 */
	@Test
	public void thatEqualsReturnsFalseForAnotherObject() {
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		AppCmpndAttrSmplAttrPrmsn key2 = new AppCmpndAttrSmplAttrPrmsn();
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test null key equals null key is true
	 */
	@Test
	public void thatEqualsRetrunsTrueForTwoNullKeys() {
		AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		AppCmpndAttrSmplAttrPrmsnKey key2 = new AppCmpndAttrSmplAttrPrmsnKey();
		assertTrue(key.equals(key2));
	}
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem1Null() {
	    AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		AppCmpndAttrSmplAttrPrmsnKey key2 = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		key2.setAppPrtflId(null);    
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(SMPLUSERATTRID);
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem2Null() {
	    AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		AppCmpndAttrSmplAttrPrmsnKey key2 = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		key2.setAppPrtflId(APPPRTFLID);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(null);    
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(SMPLUSERATTRID);
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem3Null() {
	    AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		AppCmpndAttrSmplAttrPrmsnKey key2 = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		key2.setAppPrtflId(APPPRTFLID);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(null);    
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForLikeObjectsAreEqual() {
	     AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		 AppCmpndAttrSmplAttrPrmsnKey key2 = createKey();
		 assertTrue(key.hashCode() == key2.hashCode());	 
	 }
	 
	 /**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForDifferentObjectsAreNotEqual() {
	     AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		 AppCmpndAttrSmplAttrPrmsn record = createRecord();
		 assertTrue(key.hashCode() != record.hashCode());	 
	 }
	 
	/**
	 * Test bean equals method
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingBeans() {
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		record.setKey(key);
		AppCmpndAttrSmplAttrPrmsn record2 = createRecord();
		record2.setKey(key);
		assertTrue(record.equals(record2));
	}

	/**
	 * Test bean equals method expecting failure
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingBeans() {
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		record.setKey(key);
		AppCmpndAttrSmplAttrPrmsn record2 = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key2 = new AppCmpndAttrSmplAttrPrmsnKey();
		record2.setKey(key2);
		assertFalse(record.equals(record2));
	}
	
    /**
	 * Test insert method
	 */
	@Test
	public void thatInsertCanOccurNormally() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		AppCmpndAttrSmplAttrPrmsn record2 = dao.selectByPrimaryKey(key);
		assertNotNull(record2);
	}
	
	/**
	 * Test that insert with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithRecordNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		dao.insert(null);
	}

	/**
	 * Test that insert with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithPrimaryKeyNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		record.setKey(null);
		dao.insert(record);
	}
	
    /**
	 * Test that insert with null key column(s) throws verifier exception
	 */
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void thatInsertWithKeyColumnNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		record.setKey(key);
		dao.insert(record);
    }
        
    /**
	 * Test that insert with null value in required column(s) with defaults works
	 */
	 	@Test
		public void thatInsertingRecordWithMissingFieldsInserts() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        AppCmpndAttrSmplAttrPrmsn record = new AppCmpndAttrSmplAttrPrmsn();
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		record.setKey(key);
		AppCmpndAttrSmplAttrPrmsnKey returnedKey = dao.insert(record);
		AppCmpndAttrSmplAttrPrmsn newRecord = dao.selectByPrimaryKey(returnedKey);
        assertNotNull(newRecord);
	}
       
	/**
	 * Test that duplicate keys throws exception
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void thatInsertDuplicateKeyThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		AppCmpndAttrSmplAttrPrmsn record2 = createRecord();
		record2.setKey(key);
		dao.insert(record2);
	}
	
    /**
	 * Test select works as expected
	 */
	@Test
    public void thatSelectCanOccurNormally() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		record.setKey(key);
		
		dao.insert(record);
		AppCmpndAttrSmplAttrPrmsn selectedRecord = dao.selectByPrimaryKey(key);
		assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatSelectWithPrimaryKeyNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		dao.selectByPrimaryKey(null);
	}
	
	/**
	 * Test that select with null value in key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatSelectWithKeyColumnNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		dao.selectByPrimaryKey(key);
	}

    /**
	 * Test that discretionary select works as expected
	 */
    @Test
	public void thatDiscretionaryFindCanOccurNormally() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		AppCmpndAttrSmplAttrPrmsn record2 = new AppCmpndAttrSmplAttrPrmsn();
		AppCmpndAttrSmplAttrPrmsnKey key2 = createKey();
	    key2.setAppPrtflId(APPPRTFLID);
	    key2.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setSmplUserAttrId("B");
		record2.setKey(key2);
		record2.setCrtUserId("B");
		record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
		dao.insert(record2);
              
		AppCmpndAttrSmplAttrPrmsnKey key3 = new AppCmpndAttrSmplAttrPrmsnKey();
	    key3.setAppPrtflId(APPPRTFLID);
	    key3.setCmpndUserAttrId(CMPNDUSERATTRID);
		key3.setSmplUserAttrId(null);
		List<AppCmpndAttrSmplAttrPrmsn> selectedRecords = dao.selectByPrimaryKeyDiscretionary(key3);
		Assert.assertEquals(2, selectedRecords.size());
	}
	
    /**
	 * Test that discretionary select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithPrimaryKeyNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		dao.selectByPrimaryKeyDiscretionary(null);
	}
	
	/**
	 * Test that discretionary select with first ordinal null throws exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithFirstKeyColumnNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(null);
		dao.selectByPrimaryKeyDiscretionary(key);
	}
    /**
	 * Test that select by index works as expected
	 */
	@Test
	public void thatSelectByIe1AppCmpndAttrSmplAttrPCanOccurNormally() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		AppCmpndAttrSmplAttrPrmsn record2 = new AppCmpndAttrSmplAttrPrmsn();
		AppCmpndAttrSmplAttrPrmsnKey key2 = new AppCmpndAttrSmplAttrPrmsnKey();;
        key2.setAppPrtflId(new BigDecimal(2));
        key2.setCmpndUserAttrId(CMPNDUSERATTRID);
        key2.setSmplUserAttrId(SMPLUSERATTRID);
        record2.setCrtUserId("B");
        record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
          
        record2.setKey(key2);
		dao.insert(record2);
		List<AppCmpndAttrSmplAttrPrmsn> selectedRecords = dao.selectByIe1AppCmpndAttrSmplAttrP(CMPNDUSERATTRID , SMPLUSERATTRID );
		Assert.assertEquals(2, selectedRecords.size());
	}
	
	/**
	 * Test that select by index with null index first ordinal throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatIndexSelectByIe1AppCmpndAttrSmplAttrPWithIndexNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		dao.selectByIe1AppCmpndAttrSmplAttrP(null , null );
	}   
	
	/**
	 * Test that delete works as expected
	 */
	@Test
	public void thatDeleteCanOccurNormally(){
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsn record = createRecord();
		AppCmpndAttrSmplAttrPrmsnKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		dao.deleteByPrimaryKey(key);
	}

	/**
	 * Test that delete with null key throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithPrimaryKeyNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		dao.deleteByPrimaryKey(null);
	}
	
	/**
	 * Test that delete with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithKeyColumnNulledThrowsException() {
		IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
		AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		dao.deleteByPrimaryKey(key);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(null, null, null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), BigDecimal.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), null, null, null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), null, null, null, null, null, Integer.valueOf(21), Integer.valueOf(40)); 
        assertTrue(records.size() == 20);

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithSameMinMaxWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), BigDecimal.valueOf(1), null, null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 10);
        
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithoutRowLimitsWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), null, null, null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithMultiColumnsWithoutRowLimitsWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), null, String.valueOf(1), null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1AppCmpndAttrSmplAttrPWithNullValuesThrowsException() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
    
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1AppCmpndAttrSmplAttrPWithRowMinNullAndRowMaxSetThrowsException() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByIe1AppCmpndAttrSmplAttrPWithRowLimitsWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1AppCmpndAttrSmplAttrPWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, Integer.valueOf(21), Integer.valueOf(40));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1AppCmpndAttrSmplAttrPWithoutRowLimitsWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test
    public void thatFindByRangeByIe1AppCmpndAttrSmplAttrPWithMultiColumnsWithoutRowLimitsWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
        
    @Test(expected = IllegalArgumentException.class)
    public void thatFindDistinctIe1AppCmpndAttrSmplAttrPWithNullValuesThrowsException() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        
        List<Hashtable> records = appCmpndAttrSmplAttrPrmsnDao.findDistinctByCmpndUserAttrIdBySmplUserAttrId(null, null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void thatFindDistinctIe1AppCmpndAttrSmplAttrPOnTheHappyPathWorksAsExpected() {
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = createRecord(1, 1);
        
        appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, 1));
        appCmpndAttrSmplAttrPrmsn.getKey().setCmpndUserAttrId("1");
        appCmpndAttrSmplAttrPrmsnDao.insert(appCmpndAttrSmplAttrPrmsn);
        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn2 = createRecord(1, 1);
        appCmpndAttrSmplAttrPrmsn2.setKey(createKey(2, 1));
        appCmpndAttrSmplAttrPrmsn2.getKey().setCmpndUserAttrId("1");
        appCmpndAttrSmplAttrPrmsnDao.insert(appCmpndAttrSmplAttrPrmsn2);
        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn3 = createRecord(1, 1);
        appCmpndAttrSmplAttrPrmsn3.setKey(createKey(1, 1));
        appCmpndAttrSmplAttrPrmsn3.getKey().setCmpndUserAttrId("2");
        appCmpndAttrSmplAttrPrmsnDao.insert(appCmpndAttrSmplAttrPrmsn3);
        
        List<Hashtable> records = appCmpndAttrSmplAttrPrmsnDao.findDistinctByCmpndUserAttrIdBySmplUserAttrId("1", null);
        assertNotNull(records);
        assertTrue(records.size() == 2);
        assertTrue(!records.get(0).equals(records.get(1)));
        
    }   
    
   	/**
	 * Helper method for creating a record
	 * @return record  a DB record with all columns set
	 */
	public static AppCmpndAttrSmplAttrPrmsn createRecord() {
		AppCmpndAttrSmplAttrPrmsn record = new AppCmpndAttrSmplAttrPrmsn();
		record.setCrtUserId(CRTUSERID);
		record.setCrtTs(CRTTS);

		return record;
	}

	/**
	 * Helper method for creating a key
	 * @return key  a record key with all columns set
	 */
	public static AppCmpndAttrSmplAttrPrmsnKey createKey() {
		AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
		key.setAppPrtflId(APPPRTFLID);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);

		return key;
	}
	
	/**
     * Helper method for creating a record when adding multiple records.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static AppCmpndAttrSmplAttrPrmsn createRecord(int g, int r) {
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, r);

        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = new AppCmpndAttrSmplAttrPrmsn();
        appCmpndAttrSmplAttrPrmsn.setCrtUserId(String.valueOf(r));
        appCmpndAttrSmplAttrPrmsn.setCrtTs(calendar.getTime());

        return appCmpndAttrSmplAttrPrmsn;
    }

    /**
     * Helper method for creating a key when adding multiple records.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static AppCmpndAttrSmplAttrPrmsnKey createKey(int g, int r) {
        AppCmpndAttrSmplAttrPrmsnKey appCmpndAttrSmplAttrPrmsnKey = new AppCmpndAttrSmplAttrPrmsnKey();
        appCmpndAttrSmplAttrPrmsnKey.setAppPrtflId(BigDecimal.valueOf(g));
        appCmpndAttrSmplAttrPrmsnKey.setCmpndUserAttrId(String.valueOf(g));
        appCmpndAttrSmplAttrPrmsnKey.setSmplUserAttrId(String.valueOf(g * 10 + r));

        return appCmpndAttrSmplAttrPrmsnKey;
    }
    
    public void populateData() {

    IAppCmpndAttrSmplAttrPrmsnDAO dao = daoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();

    AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                appCmpndAttrSmplAttrPrmsn = createRecord(g, r);
                appCmpndAttrSmplAttrPrmsn.setKey(createKey(g, r));

                dao.insert(appCmpndAttrSmplAttrPrmsn);
            }
        }
    }
    
}