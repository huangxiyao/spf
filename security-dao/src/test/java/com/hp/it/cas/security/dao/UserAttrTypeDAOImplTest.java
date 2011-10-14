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
 * Test class for the USER_ATTR_TYPE table DAO
 * @author CAS Generator v1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

public class UserAttrTypeDAOImplTest {

    private static final Calendar CALENDAR_NOW = Calendar.getInstance();
	final static private String USERATTRTYPECD = "A";
	final static private String CRTUSERID = "A";
	final static private Date CRTTS = new Date();
	final static private String LASTMAINTUSERID = "A";
	final static private Date LASTMAINTTS = new Date();
	final static private String USERATTRTYPENM = "A";
	final static private String USERATTRTYPEDN = "A";
    
	@Autowired
	ISecurityDAOFactory daoFactory;
	
	/**
	 * Test key equals key is true
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingKeys() {
		UserAttrTypeKey key = createKey();
		UserAttrTypeKey key2 = createKey();
		assertTrue(key.equals(key2));
	}
	
	/**
	 * Test key equals a different key is false.
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingKey() {
		UserAttrTypeKey key = createKey();
		UserAttrTypeKey key2 = new UserAttrTypeKey();
		assertFalse(key.equals(key2));
	}
	
	
	/**
	 *  Test key equals null key is false
	 */
	@Test
	public void thatEqualsReturnsFalseForNull() {
		UserAttrTypeKey key = createKey();
		UserAttrTypeKey key2 = null;
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals another object is false
	 */
	@Test
	public void thatEqualsReturnsFalseForAnotherObject() {
		UserAttrTypeKey key = createKey();
		UserAttrType key2 = new UserAttrType();
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test null key equals null key is true
	 */
	@Test
	public void thatEqualsRetrunsTrueForTwoNullKeys() {
		UserAttrTypeKey key = new UserAttrTypeKey();
		UserAttrTypeKey key2 = new UserAttrTypeKey();
		assertTrue(key.equals(key2));
	}
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem1Null() {
	    UserAttrTypeKey key = new UserAttrTypeKey();
		UserAttrTypeKey key2 = new UserAttrTypeKey();
		key.setUserAttrTypeCd(USERATTRTYPECD);
		key2.setUserAttrTypeCd(null);    
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForLikeObjectsAreEqual() {
	     UserAttrTypeKey key = createKey();
		 UserAttrTypeKey key2 = createKey();
		 assertTrue(key.hashCode() == key2.hashCode());	 
	 }
	 
	 /**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForDifferentObjectsAreNotEqual() {
	     UserAttrTypeKey key = createKey();
		 UserAttrType record = createRecord();
		 assertTrue(key.hashCode() != record.hashCode());	 
	 }
	 
	/**
	 * Test bean equals method
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingBeans() {
		UserAttrTypeKey key = createKey();
		UserAttrType record = createRecord();
		record.setKey(key);
		UserAttrType record2 = createRecord();
		record2.setKey(key);
		assertTrue(record.equals(record2));
	}

	/**
	 * Test bean equals method expecting failure
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingBeans() {
		UserAttrTypeKey key = createKey();
		UserAttrType record = createRecord();
		record.setKey(key);
		UserAttrType record2 = createRecord();
		UserAttrTypeKey key2 = new UserAttrTypeKey();
		record2.setKey(key2);
		assertFalse(record.equals(record2));
	}
	
    /**
	 * Test insert method
	 */
	@Test
	public void thatInsertCanOccurNormally() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		UserAttrTypeKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserAttrType record2 = dao.selectByPrimaryKey(key);
		assertNotNull(record2);
	}
	
	/**
	 * Test that insert with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithRecordNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		dao.insert(null);
	}

	/**
	 * Test that insert with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithPrimaryKeyNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		record.setKey(null);
		dao.insert(record);
	}
	
    /**
	 * Test that insert with null key column(s) throws verifier exception
	 */
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void thatInsertWithKeyColumnNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		UserAttrTypeKey key = new UserAttrTypeKey();
		key.setUserAttrTypeCd(null);
		record.setKey(key);
		dao.insert(record);
    }
        
    /**
	 * Test that insert with null value in required column(s) with defaults works
	 */
	 	@Test
		public void thatInsertingRecordWithMissingFieldsInserts() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
        UserAttrType record = new UserAttrType();
		UserAttrTypeKey key = createKey();
		record.setKey(key);
		UserAttrTypeKey returnedKey = dao.insert(record);
		UserAttrType newRecord = dao.selectByPrimaryKey(returnedKey);
        assertNotNull(newRecord);
	}
       
	/**
	 * Test that duplicate keys throws exception
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void thatInsertDuplicateKeyThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		UserAttrTypeKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserAttrType record2 = createRecord();
		record2.setKey(key);
		dao.insert(record2);
	}
	
    /**
	 * Test select works as expected
	 */
	@Test
    public void thatSelectCanOccurNormally() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		UserAttrTypeKey key = createKey();
		record.setKey(key);
		
		dao.insert(record);
		UserAttrType selectedRecord = dao.selectByPrimaryKey(key);
		assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatSelectWithPrimaryKeyNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		dao.selectByPrimaryKey(null);
	}
	
	/**
	 * Test that select with null value in key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatSelectWithKeyColumnNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrTypeKey key = new UserAttrTypeKey();
		key.setUserAttrTypeCd(null);
		dao.selectByPrimaryKey(key);
	}

	/**
	 * Test that update works as expected
	 */
    @Test
	public void thatUpdateCanOccurNormally(){
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrTypeKey key = createKey();
		UserAttrType record1 = createRecord();
	    record1.setKey(key);
		dao.insert(record1);
		UserAttrType record2 = new UserAttrType();
	    record2.setKey(key);
		record2.setLastMaintUserId("B");
		dao.updateByPrimaryKeySelective(record2);
              
		UserAttrType returnedRecord = dao.selectByPrimaryKey(key);
		Assert.assertEquals(returnedRecord.getLastMaintUserId(), "B");
	}
	
	/**
	 * Test that update with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithRecordNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		dao.updateByPrimaryKeySelective(null);
	}
	   
	/**
	 * Test that update with null key throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithPrimaryKeyNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		record.setKey(null);
		dao.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * Test that update with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithKeyColumnNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		UserAttrTypeKey key = new UserAttrTypeKey();
		key.setUserAttrTypeCd(null);
		dao.updateByPrimaryKeySelective(record);
    }
	
	/**
	 * Test that delete works as expected
	 */
	@Test
	public void thatDeleteCanOccurNormally(){
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrType record = createRecord();
		UserAttrTypeKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		dao.deleteByPrimaryKey(key);
	}

	/**
	 * Test that delete with null key throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithPrimaryKeyNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		dao.deleteByPrimaryKey(null);
	}
	
	/**
	 * Test that delete with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithKeyColumnNulledThrowsException() {
		IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();
		UserAttrTypeKey key = new UserAttrTypeKey();
		key.setUserAttrTypeCd(null);
		dao.deleteByPrimaryKey(key);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        IUserAttrTypeDAO userAttrTypeDao = daoFactory.getUserAttrTypeDAO();
        
        List<UserAttrType> records = userAttrTypeDao.findRangeByUserAttrTypeCd(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        IUserAttrTypeDAO userAttrTypeDao = daoFactory.getUserAttrTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserAttrType> records = userAttrTypeDao.findRangeByUserAttrTypeCd(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        IUserAttrTypeDAO userAttrTypeDao = daoFactory.getUserAttrTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrType> records = userAttrTypeDao.findRangeByUserAttrTypeCd(String.valueOf(1), null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IUserAttrTypeDAO userAttrTypeDao = daoFactory.getUserAttrTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrType> records = userAttrTypeDao.findRangeByUserAttrTypeCd(String.valueOf(1), null, Integer.valueOf(21), Integer.valueOf(40)); 
        assertTrue(records.size() == 20);

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithSameMinMaxWorksAsExpected() {
        IUserAttrTypeDAO userAttrTypeDao = daoFactory.getUserAttrTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrType> records = userAttrTypeDao.findRangeByUserAttrTypeCd(String.valueOf(11), String.valueOf(11), Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 1);
        
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithoutRowLimitsWorksAsExpected() {
        IUserAttrTypeDAO userAttrTypeDao = daoFactory.getUserAttrTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttrType> records = userAttrTypeDao.findRangeByUserAttrTypeCd(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
    
    
   	/**
	 * Helper method for creating a record
	 * @return record  a DB record with all columns set
	 */
	public static UserAttrType createRecord() {
		UserAttrType record = new UserAttrType();
		record.setCrtUserId(CRTUSERID);
		record.setCrtTs(CRTTS);
		record.setLastMaintUserId(LASTMAINTUSERID);
		record.setLastMaintTs(LASTMAINTTS);
		record.setUserAttrTypeNm(USERATTRTYPENM);
		record.setUserAttrTypeDn(USERATTRTYPEDN);

		return record;
	}

	/**
	 * Helper method for creating a key
	 * @return key  a record key with all columns set
	 */
	public static UserAttrTypeKey createKey() {
		UserAttrTypeKey key = new UserAttrTypeKey();
		key.setUserAttrTypeCd(USERATTRTYPECD);

		return key;
	}
	
	/**
     * Helper method for creating a record when adding multiple records.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserAttrType createRecord(int g, int r) {
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, r);

        
        UserAttrType userAttrType = new UserAttrType();
        userAttrType.setCrtUserId(String.valueOf(r));
        userAttrType.setCrtTs(calendar.getTime());
        userAttrType.setLastMaintUserId(String.valueOf(r));
        userAttrType.setLastMaintTs(calendar.getTime());
        userAttrType.setUserAttrTypeNm(String.valueOf(r));
        userAttrType.setUserAttrTypeDn(String.valueOf(r));

        return userAttrType;
    }

    /**
     * Helper method for creating a key when adding multiple records.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserAttrTypeKey createKey(int g, int r) {
        UserAttrTypeKey userAttrTypeKey = new UserAttrTypeKey();
        userAttrTypeKey.setUserAttrTypeCd(String.valueOf(g * 10 + r));

        return userAttrTypeKey;
    }
    
    public void populateData() {

    IUserAttrTypeDAO dao = daoFactory.getUserAttrTypeDAO();

    UserAttrType userAttrType = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                userAttrType = createRecord(g, r);
                userAttrType.setKey(createKey(g, r));

                dao.insert(userAttrType);
            }
        }
    }
    
}