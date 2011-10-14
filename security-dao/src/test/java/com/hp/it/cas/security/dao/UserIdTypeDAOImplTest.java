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
 * Test class for the USER_ID_TYPE table DAO
 * @author CAS Generator v1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

public class UserIdTypeDAOImplTest {

    private static final Calendar CALENDAR_NOW = Calendar.getInstance();
	final static private String USERIDTYPECD = "A";
	final static private String CRTUSERID = "A";
	final static private Date CRTTS = new Date();
	final static private String LASTMAINTUSERID = "A";
	final static private Date LASTMAINTTS = new Date();
	final static private String USERIDTYPENM = "A";
	final static private String USERIDTYPEDN = "A";
    
	@Autowired
	ISecurityDAOFactory daoFactory;
	
	/**
	 * Test key equals key is true
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingKeys() {
		UserIdTypeKey key = createKey();
		UserIdTypeKey key2 = createKey();
		assertTrue(key.equals(key2));
	}
	
	/**
	 * Test key equals a different key is false.
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingKey() {
		UserIdTypeKey key = createKey();
		UserIdTypeKey key2 = new UserIdTypeKey();
		assertFalse(key.equals(key2));
	}
	
	
	/**
	 *  Test key equals null key is false
	 */
	@Test
	public void thatEqualsReturnsFalseForNull() {
		UserIdTypeKey key = createKey();
		UserIdTypeKey key2 = null;
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals another object is false
	 */
	@Test
	public void thatEqualsReturnsFalseForAnotherObject() {
		UserIdTypeKey key = createKey();
		UserIdType key2 = new UserIdType();
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test null key equals null key is true
	 */
	@Test
	public void thatEqualsRetrunsTrueForTwoNullKeys() {
		UserIdTypeKey key = new UserIdTypeKey();
		UserIdTypeKey key2 = new UserIdTypeKey();
		assertTrue(key.equals(key2));
	}
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem1Null() {
	    UserIdTypeKey key = new UserIdTypeKey();
		UserIdTypeKey key2 = new UserIdTypeKey();
		key.setUserIdTypeCd(USERIDTYPECD);
		key2.setUserIdTypeCd(null);    
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForLikeObjectsAreEqual() {
	     UserIdTypeKey key = createKey();
		 UserIdTypeKey key2 = createKey();
		 assertTrue(key.hashCode() == key2.hashCode());	 
	 }
	 
	 /**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForDifferentObjectsAreNotEqual() {
	     UserIdTypeKey key = createKey();
		 UserIdType record = createRecord();
		 assertTrue(key.hashCode() != record.hashCode());	 
	 }
	 
	/**
	 * Test bean equals method
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingBeans() {
		UserIdTypeKey key = createKey();
		UserIdType record = createRecord();
		record.setKey(key);
		UserIdType record2 = createRecord();
		record2.setKey(key);
		assertTrue(record.equals(record2));
	}

	/**
	 * Test bean equals method expecting failure
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingBeans() {
		UserIdTypeKey key = createKey();
		UserIdType record = createRecord();
		record.setKey(key);
		UserIdType record2 = createRecord();
		UserIdTypeKey key2 = new UserIdTypeKey();
		record2.setKey(key2);
		assertFalse(record.equals(record2));
	}
	
    /**
	 * Test insert method
	 */
	@Test
	public void thatInsertCanOccurNormally() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		UserIdTypeKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserIdType record2 = dao.selectByPrimaryKey(key);
		assertNotNull(record2);
	}
	
	/**
	 * Test that insert with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithRecordNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		dao.insert(null);
	}

	/**
	 * Test that insert with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithPrimaryKeyNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		record.setKey(null);
		dao.insert(record);
	}
	
    /**
	 * Test that insert with null key column(s) throws verifier exception
	 */
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void thatInsertWithKeyColumnNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		UserIdTypeKey key = new UserIdTypeKey();
		key.setUserIdTypeCd(null);
		record.setKey(key);
		dao.insert(record);
    }
        
    /**
	 * Test that insert with null value in required column(s) with defaults works
	 */
	 	@Test
		public void thatInsertingRecordWithMissingFieldsInserts() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
        UserIdType record = new UserIdType();
		UserIdTypeKey key = createKey();
		record.setKey(key);
		UserIdTypeKey returnedKey = dao.insert(record);
		UserIdType newRecord = dao.selectByPrimaryKey(returnedKey);
        assertNotNull(newRecord);
	}
       
	/**
	 * Test that duplicate keys throws exception
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void thatInsertDuplicateKeyThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		UserIdTypeKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserIdType record2 = createRecord();
		record2.setKey(key);
		dao.insert(record2);
	}
	
    /**
	 * Test select works as expected
	 */
	@Test
    public void thatSelectCanOccurNormally() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		UserIdTypeKey key = createKey();
		record.setKey(key);
		
		dao.insert(record);
		UserIdType selectedRecord = dao.selectByPrimaryKey(key);
		assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatSelectWithPrimaryKeyNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		dao.selectByPrimaryKey(null);
	}
	
	/**
	 * Test that select with null value in key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatSelectWithKeyColumnNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdTypeKey key = new UserIdTypeKey();
		key.setUserIdTypeCd(null);
		dao.selectByPrimaryKey(key);
	}

	/**
	 * Test that update works as expected
	 */
    @Test
	public void thatUpdateCanOccurNormally(){
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdTypeKey key = createKey();
		UserIdType record1 = createRecord();
	    record1.setKey(key);
		dao.insert(record1);
		UserIdType record2 = new UserIdType();
	    record2.setKey(key);
		record2.setLastMaintUserId("B");
		dao.updateByPrimaryKeySelective(record2);
              
		UserIdType returnedRecord = dao.selectByPrimaryKey(key);
		Assert.assertEquals(returnedRecord.getLastMaintUserId(), "B");
	}
	
	/**
	 * Test that update with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithRecordNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		dao.updateByPrimaryKeySelective(null);
	}
	   
	/**
	 * Test that update with null key throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithPrimaryKeyNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		record.setKey(null);
		dao.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * Test that update with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithKeyColumnNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		UserIdTypeKey key = new UserIdTypeKey();
		key.setUserIdTypeCd(null);
		dao.updateByPrimaryKeySelective(record);
    }
	
	/**
	 * Test that delete works as expected
	 */
	@Test
	public void thatDeleteCanOccurNormally(){
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdType record = createRecord();
		UserIdTypeKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		dao.deleteByPrimaryKey(key);
	}

	/**
	 * Test that delete with null key throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithPrimaryKeyNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		dao.deleteByPrimaryKey(null);
	}
	
	/**
	 * Test that delete with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithKeyColumnNulledThrowsException() {
		IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();
		UserIdTypeKey key = new UserIdTypeKey();
		key.setUserIdTypeCd(null);
		dao.deleteByPrimaryKey(key);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        IUserIdTypeDAO userIdTypeDao = daoFactory.getUserIdTypeDAO();
        
        List<UserIdType> records = userIdTypeDao.findRangeByUserIdTypeCd(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        IUserIdTypeDAO userIdTypeDao = daoFactory.getUserIdTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserIdType> records = userIdTypeDao.findRangeByUserIdTypeCd(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        IUserIdTypeDAO userIdTypeDao = daoFactory.getUserIdTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserIdType> records = userIdTypeDao.findRangeByUserIdTypeCd(String.valueOf(1), null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IUserIdTypeDAO userIdTypeDao = daoFactory.getUserIdTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserIdType> records = userIdTypeDao.findRangeByUserIdTypeCd(String.valueOf(1), null, Integer.valueOf(21), Integer.valueOf(40)); 
        assertTrue(records.size() == 20);

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithSameMinMaxWorksAsExpected() {
        IUserIdTypeDAO userIdTypeDao = daoFactory.getUserIdTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserIdType> records = userIdTypeDao.findRangeByUserIdTypeCd(String.valueOf(11), String.valueOf(11), Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 1);
        
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithoutRowLimitsWorksAsExpected() {
        IUserIdTypeDAO userIdTypeDao = daoFactory.getUserIdTypeDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserIdType> records = userIdTypeDao.findRangeByUserIdTypeCd(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
    
    
   	/**
	 * Helper method for creating a record
	 * @return record  a DB record with all columns set
	 */
	public static UserIdType createRecord() {
		UserIdType record = new UserIdType();
		record.setCrtUserId(CRTUSERID);
		record.setCrtTs(CRTTS);
		record.setLastMaintUserId(LASTMAINTUSERID);
		record.setLastMaintTs(LASTMAINTTS);
		record.setUserIdTypeNm(USERIDTYPENM);
		record.setUserIdTypeDn(USERIDTYPEDN);

		return record;
	}

	/**
	 * Helper method for creating a key
	 * @return key  a record key with all columns set
	 */
	public static UserIdTypeKey createKey() {
		UserIdTypeKey key = new UserIdTypeKey();
		key.setUserIdTypeCd(USERIDTYPECD);

		return key;
	}
	
	/**
     * Helper method for creating a record when adding multiple records.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserIdType createRecord(int g, int r) {
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, r);

        
        UserIdType userIdType = new UserIdType();
        userIdType.setCrtUserId(String.valueOf(r));
        userIdType.setCrtTs(calendar.getTime());
        userIdType.setLastMaintUserId(String.valueOf(r));
        userIdType.setLastMaintTs(calendar.getTime());
        userIdType.setUserIdTypeNm(String.valueOf(r));
        userIdType.setUserIdTypeDn(String.valueOf(r));

        return userIdType;
    }

    /**
     * Helper method for creating a key when adding multiple records.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserIdTypeKey createKey(int g, int r) {
        UserIdTypeKey userIdTypeKey = new UserIdTypeKey();
        userIdTypeKey.setUserIdTypeCd(String.valueOf(g * 10 + r));

        return userIdTypeKey;
    }
    
    public void populateData() {

    IUserIdTypeDAO dao = daoFactory.getUserIdTypeDAO();

    UserIdType userIdType = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                userIdType = createRecord(g, r);
                userIdType.setKey(createKey(g, r));

                dao.insert(userIdType);
            }
        }
    }
    
}