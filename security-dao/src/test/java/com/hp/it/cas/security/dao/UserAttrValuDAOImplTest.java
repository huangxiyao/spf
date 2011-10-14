package com.hp.it.cas.security.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;


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
 * Test class for the USER_ATTR_VALU table DAO
 * @author CAS Generator v1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

public class UserAttrValuDAOImplTest {

    private static final Calendar CALENDAR_NOW = Calendar.getInstance();
	final static private String USERID = "A";
	final static private String USERIDTYPECD = "A";
	final static private String CMPNDUSERATTRID = "A";
	final static private String SMPLUSERATTRID = "A";
	final static private String USERATTRINSTNCID = "A";
	final static private String CRTUSERID = "A";
	final static private Date CRTTS = new Date();
	final static private String LASTMAINTUSERID = "A";
	final static private Date LASTMAINTTS = new Date();
	final static private String USERATTRVALUTX = "A";
    
	@Autowired
	ISecurityDAOFactory daoFactory;
	
	/**
	 * Test key equals key is true
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingKeys() {
		UserAttrValuKey key = createKey();
		UserAttrValuKey key2 = createKey();
		assertTrue(key.equals(key2));
	}
	
	/**
	 * Test key equals a different key is false.
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingKey() {
		UserAttrValuKey key = createKey();
		UserAttrValuKey key2 = new UserAttrValuKey();
		assertFalse(key.equals(key2));
	}
	
	
	/**
	 *  Test key equals null key is false
	 */
	@Test
	public void thatEqualsReturnsFalseForNull() {
		UserAttrValuKey key = createKey();
		UserAttrValuKey key2 = null;
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals another object is false
	 */
	@Test
	public void thatEqualsReturnsFalseForAnotherObject() {
		UserAttrValuKey key = createKey();
		UserAttrValu key2 = new UserAttrValu();
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test null key equals null key is true
	 */
	@Test
	public void thatEqualsRetrunsTrueForTwoNullKeys() {
		UserAttrValuKey key = new UserAttrValuKey();
		UserAttrValuKey key2 = new UserAttrValuKey();
		assertTrue(key.equals(key2));
	}
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem1Null() {
	    UserAttrValuKey key = new UserAttrValuKey();
		UserAttrValuKey key2 = new UserAttrValuKey();
		key.setUserId(USERID);
		key2.setUserId(null);    
		key.setUserIdTypeCd(USERIDTYPECD);
		key2.setUserIdTypeCd(USERIDTYPECD);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(SMPLUSERATTRID);
		key.setUserAttrInstncId(USERATTRINSTNCID);
		key2.setUserAttrInstncId(USERATTRINSTNCID);
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem2Null() {
	    UserAttrValuKey key = new UserAttrValuKey();
		UserAttrValuKey key2 = new UserAttrValuKey();
		key.setUserId(USERID);
		key2.setUserId(USERID);
		key.setUserIdTypeCd(USERIDTYPECD);
		key2.setUserIdTypeCd(null);    
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(SMPLUSERATTRID);
		key.setUserAttrInstncId(USERATTRINSTNCID);
		key2.setUserAttrInstncId(USERATTRINSTNCID);
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem3Null() {
	    UserAttrValuKey key = new UserAttrValuKey();
		UserAttrValuKey key2 = new UserAttrValuKey();
		key.setUserId(USERID);
		key2.setUserId(USERID);
		key.setUserIdTypeCd(USERIDTYPECD);
		key2.setUserIdTypeCd(USERIDTYPECD);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(null);    
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(SMPLUSERATTRID);
		key.setUserAttrInstncId(USERATTRINSTNCID);
		key2.setUserAttrInstncId(USERATTRINSTNCID);
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem4Null() {
	    UserAttrValuKey key = new UserAttrValuKey();
		UserAttrValuKey key2 = new UserAttrValuKey();
		key.setUserId(USERID);
		key2.setUserId(USERID);
		key.setUserIdTypeCd(USERIDTYPECD);
		key2.setUserIdTypeCd(USERIDTYPECD);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(null);    
		key.setUserAttrInstncId(USERATTRINSTNCID);
		key2.setUserAttrInstncId(USERATTRINSTNCID);
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem5Null() {
	    UserAttrValuKey key = new UserAttrValuKey();
		UserAttrValuKey key2 = new UserAttrValuKey();
		key.setUserId(USERID);
		key2.setUserId(USERID);
		key.setUserIdTypeCd(USERIDTYPECD);
		key2.setUserIdTypeCd(USERIDTYPECD);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key2.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key2.setSmplUserAttrId(SMPLUSERATTRID);
		key.setUserAttrInstncId(USERATTRINSTNCID);
		key2.setUserAttrInstncId(null);    
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForLikeObjectsAreEqual() {
	     UserAttrValuKey key = createKey();
		 UserAttrValuKey key2 = createKey();
		 assertTrue(key.hashCode() == key2.hashCode());	 
	 }
	 
	 /**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForDifferentObjectsAreNotEqual() {
	     UserAttrValuKey key = createKey();
		 UserAttrValu record = createRecord();
		 assertTrue(key.hashCode() != record.hashCode());	 
	 }
	 
	/**
	 * Test bean equals method
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingBeans() {
		UserAttrValuKey key = createKey();
		UserAttrValu record = createRecord();
		record.setKey(key);
		UserAttrValu record2 = createRecord();
		record2.setKey(key);
		assertTrue(record.equals(record2));
	}

	/**
	 * Test bean equals method expecting failure
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingBeans() {
		UserAttrValuKey key = createKey();
		UserAttrValu record = createRecord();
		record.setKey(key);
		UserAttrValu record2 = createRecord();
		UserAttrValuKey key2 = new UserAttrValuKey();
		record2.setKey(key2);
		assertFalse(record.equals(record2));
	}
	
    /**
	 * Test insert method
	 */
	@Test
	public void thatInsertCanOccurNormally() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserAttrValu record2 = dao.selectByPrimaryKey(key);
		assertNotNull(record2);
	}
	
	/**
	 * Test that insert with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithRecordNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		dao.insert(null);
	}

	/**
	 * Test that insert with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithPrimaryKeyNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		record.setKey(null);
		dao.insert(record);
	}
	
    /**
	 * Test that insert with null key column(s) throws verifier exception
	 */
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void thatInsertWithKeyColumnNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = new UserAttrValuKey();
		key.setUserId(USERID);
		record.setKey(key);
		dao.insert(record);
    }
        
    /**
	 * Test that insert with null value in required column(s) with defaults works
	 */
	 	@Test
		public void thatInsertingRecordWithMissingFieldsInserts() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
        UserAttrValu record = new UserAttrValu();
		UserAttrValuKey key = createKey();
		record.setKey(key);
		UserAttrValuKey returnedKey = dao.insert(record);
		UserAttrValu newRecord = dao.selectByPrimaryKey(returnedKey);
        assertNotNull(newRecord);
	}
       
	/**
	 * Test that duplicate keys throws exception
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void thatInsertDuplicateKeyThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserAttrValu record2 = createRecord();
		record2.setKey(key);
		dao.insert(record2);
	}
	
    /**
	 * Test select works as expected
	 */
	@Test
    public void thatSelectCanOccurNormally() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = createKey();
		record.setKey(key);
		
		dao.insert(record);
		UserAttrValu selectedRecord = dao.selectByPrimaryKey(key);
		assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatSelectWithPrimaryKeyNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		dao.selectByPrimaryKey(null);
	}
	
	/**
	 * Test that select with null value in key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatSelectWithKeyColumnNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValuKey key = new UserAttrValuKey();
		key.setUserId(USERID);
		dao.selectByPrimaryKey(key);
	}

	/**
	 * Test that update works as expected
	 */
    @Test
	public void thatUpdateCanOccurNormally(){
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValuKey key = createKey();
		UserAttrValu record1 = createRecord();
	    record1.setKey(key);
		dao.insert(record1);
		UserAttrValu record2 = new UserAttrValu();
	    record2.setKey(key);
		record2.setLastMaintUserId("B");
		dao.updateByPrimaryKeySelective(record2);
              
		UserAttrValu returnedRecord = dao.selectByPrimaryKey(key);
		Assert.assertEquals(returnedRecord.getLastMaintUserId(), "B");
	}
	
	/**
	 * Test that update with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithRecordNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		dao.updateByPrimaryKeySelective(null);
	}
	   
	/**
	 * Test that update with null key throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithPrimaryKeyNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		record.setKey(null);
		dao.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * Test that update with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithKeyColumnNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = new UserAttrValuKey();
		key.setUserId(USERID);
		dao.updateByPrimaryKeySelective(record);
    }
    /**
	 * Test that discretionary select works as expected
	 */
    @Test
	public void thatDiscretionaryFindCanOccurNormally() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		UserAttrValu record2 = new UserAttrValu();
		UserAttrValuKey key2 = createKey();
	    key2.setUserId(USERID);
	    key2.setUserIdTypeCd(USERIDTYPECD);
		key2.setCmpndUserAttrId("B");
		key2.setSmplUserAttrId("B");
		key2.setUserAttrInstncId("B");
		record2.setKey(key2);
		record2.setCrtUserId("B");
		record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
		record2.setLastMaintUserId("B");
		record2.setLastMaintTs(new Date(System.currentTimeMillis()-864000000));
		record2.setUserAttrValuTx("B");
		dao.insert(record2);
              
		UserAttrValuKey key3 = new UserAttrValuKey();
	    key3.setUserId(USERID);
	    key3.setUserIdTypeCd(USERIDTYPECD);
		key3.setCmpndUserAttrId(null);
		key3.setSmplUserAttrId(null);
		key3.setUserAttrInstncId(null);
		List<UserAttrValu> selectedRecords = dao.selectByPrimaryKeyDiscretionary(key3);
		Assert.assertEquals(2, selectedRecords.size());
	}
	
    /**
	 * Test that discretionary select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithPrimaryKeyNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		dao.selectByPrimaryKeyDiscretionary(null);
	}
	
	/**
	 * Test that discretionary select with first ordinal null throws exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithFirstKeyColumnNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValuKey key = new UserAttrValuKey();
		key.setUserId(null);
		dao.selectByPrimaryKeyDiscretionary(key);
	}
    /**
	 * Test that select by index works as expected
	 */
	@Test
	public void thatSelectByIe1UserAttrValuCanOccurNormally() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		UserAttrValu record2 = new UserAttrValu();
		UserAttrValuKey key2 = new UserAttrValuKey();;
        key2.setUserId("B");
        key2.setUserIdTypeCd("B");
        key2.setCmpndUserAttrId(CMPNDUSERATTRID);
        key2.setSmplUserAttrId(SMPLUSERATTRID);
        key2.setUserAttrInstncId("B");
        record2.setCrtUserId("B");
        record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
        record2.setLastMaintUserId("B");
        record2.setLastMaintTs(new Date(System.currentTimeMillis()-864000000));
        record2.setUserAttrValuTx(USERATTRVALUTX);
          
        record2.setKey(key2);
		dao.insert(record2);
		List<UserAttrValu> selectedRecords = dao.selectByIe1UserAttrValu(CMPNDUSERATTRID , SMPLUSERATTRID , USERATTRVALUTX );
		Assert.assertEquals(2, selectedRecords.size());
	}
	
	/**
	 * Test that select by index with null index first ordinal throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatIndexSelectByIe1UserAttrValuWithIndexNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		dao.selectByIe1UserAttrValu(null , null , null );
	}   
	
	/**
	 * Test that delete works as expected
	 */
	@Test
	public void thatDeleteCanOccurNormally(){
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValu record = createRecord();
		UserAttrValuKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		dao.deleteByPrimaryKey(key);
	}

	/**
	 * Test that delete with null key throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithPrimaryKeyNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		dao.deleteByPrimaryKey(null);
	}
	
	/**
	 * Test that delete with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithKeyColumnNulledThrowsException() {
		IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();
		UserAttrValuKey key = new UserAttrValuKey();
		key.setUserId(USERID);
		dao.deleteByPrimaryKey(key);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(null, null, null, null, null, null, null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), null, null, null, null, null, null, null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), null, null, null, null, null, null, null, null, null, Integer.valueOf(21), Integer.valueOf(40)); 
        assertTrue(records.size() == 20);

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithSameMinMaxWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), String.valueOf(1), null, null, null, null, null, null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 10);
        
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithoutRowLimitsWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), null, null, null, null, null, null, null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithMultiColumnsWithoutRowLimitsWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), null, String.valueOf(1), null, null, null, null, null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1UserAttrValuWithNullValuesThrowsException() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
    
        List<UserAttrValu> records = userAttrValuDao.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(null, null, null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1UserAttrValuWithRowMinNullAndRowMaxSetThrowsException() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrValuWithRowLimitsWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String.valueOf(1), null, null, null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrValuWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String.valueOf(1), null, null, null, null, null, Integer.valueOf(21), Integer.valueOf(40));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrValuWithoutRowLimitsWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String.valueOf(1), null, null, null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrValuWithMultiColumnsWithoutRowLimitsWorksAsExpected() {
        IUserAttrValuDAO userAttrValuDao = daoFactory.getUserAttrValuDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrValu> records = userAttrValuDao.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String.valueOf(1), null, String.valueOf(1), null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
        
    
   	/**
	 * Helper method for creating a record
	 * @return record  a DB record with all columns set
	 */
	public static UserAttrValu createRecord() {
		UserAttrValu record = new UserAttrValu();
		record.setCrtUserId(CRTUSERID);
		record.setCrtTs(CRTTS);
		record.setLastMaintUserId(LASTMAINTUSERID);
		record.setLastMaintTs(LASTMAINTTS);
		record.setUserAttrValuTx(USERATTRVALUTX);

		return record;
	}

	/**
	 * Helper method for creating a key
	 * @return key  a record key with all columns set
	 */
	public static UserAttrValuKey createKey() {
		UserAttrValuKey key = new UserAttrValuKey();
		key.setUserId(USERID);
		key.setUserIdTypeCd(USERIDTYPECD);
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);
		key.setUserAttrInstncId(USERATTRINSTNCID);

		return key;
	}
	
	/**
     * Helper method for creating a record when adding multiple records.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserAttrValu createRecord(int g, int r) {
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, r);

        
        UserAttrValu userAttrValu = new UserAttrValu();
        userAttrValu.setCrtUserId(String.valueOf(r));
        userAttrValu.setCrtTs(calendar.getTime());
        userAttrValu.setLastMaintUserId(String.valueOf(r));
        userAttrValu.setLastMaintTs(calendar.getTime());
        userAttrValu.setUserAttrValuTx(String.valueOf(r));

        return userAttrValu;
    }

    /**
     * Helper method for creating a key when adding multiple records.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserAttrValuKey createKey(int g, int r) {
        UserAttrValuKey userAttrValuKey = new UserAttrValuKey();
        userAttrValuKey.setUserId(String.valueOf(g));
        userAttrValuKey.setUserIdTypeCd(String.valueOf(g));
        userAttrValuKey.setCmpndUserAttrId(String.valueOf(g));
        userAttrValuKey.setSmplUserAttrId(String.valueOf(g));
        userAttrValuKey.setUserAttrInstncId(String.valueOf(g * 10 + r));

        return userAttrValuKey;
    }
    
    public void populateData() {

    IUserAttrValuDAO dao = daoFactory.getUserAttrValuDAO();

    UserAttrValu userAttrValu = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                userAttrValu = createRecord(g, r);
                userAttrValu.setKey(createKey(g, r));

                dao.insert(userAttrValu);
            }
        }
    }
    
}