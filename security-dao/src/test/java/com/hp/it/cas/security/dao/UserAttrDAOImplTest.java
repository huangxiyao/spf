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
 * Test class for the USER_ATTR table DAO
 * @author CAS Generator v1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

public class UserAttrDAOImplTest {

    private static final Calendar CALENDAR_NOW = Calendar.getInstance();
	final static private String USERATTRID = "A";
	final static private String CRTUSERID = "A";
	final static private Date CRTTS = new Date();
	final static private String LASTMAINTUSERID = "A";
	final static private Date LASTMAINTTS = new Date();
	final static private String USERATTRTYPECD = "A";
	final static private String USERATTRNM = "A";
	final static private String USERATTRDN = "A";
	final static private String USERATTRDEFNTX = "A long string of data";
    
	@Autowired
	ISecurityDAOFactory daoFactory;
	
	/**
	 * Test key equals key is true
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingKeys() {
		UserAttrKey key = createKey();
		UserAttrKey key2 = createKey();
		assertTrue(key.equals(key2));
	}
	
	/**
	 * Test key equals a different key is false.
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingKey() {
		UserAttrKey key = createKey();
		UserAttrKey key2 = new UserAttrKey();
		assertFalse(key.equals(key2));
	}
	
	
	/**
	 *  Test key equals null key is false
	 */
	@Test
	public void thatEqualsReturnsFalseForNull() {
		UserAttrKey key = createKey();
		UserAttrKey key2 = null;
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals another object is false
	 */
	@Test
	public void thatEqualsReturnsFalseForAnotherObject() {
		UserAttrKey key = createKey();
		UserAttr key2 = new UserAttr();
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test null key equals null key is true
	 */
	@Test
	public void thatEqualsRetrunsTrueForTwoNullKeys() {
		UserAttrKey key = new UserAttrKey();
		UserAttrKey key2 = new UserAttrKey();
		assertTrue(key.equals(key2));
	}
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem1Null() {
	    UserAttrKey key = new UserAttrKey();
		UserAttrKey key2 = new UserAttrKey();
		key.setUserAttrId(USERATTRID);
		key2.setUserAttrId(null);    
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForLikeObjectsAreEqual() {
	     UserAttrKey key = createKey();
		 UserAttrKey key2 = createKey();
		 assertTrue(key.hashCode() == key2.hashCode());	 
	 }
	 
	 /**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForDifferentObjectsAreNotEqual() {
	     UserAttrKey key = createKey();
		 UserAttr record = createRecord();
		 assertTrue(key.hashCode() != record.hashCode());	 
	 }
	 
	/**
	 * Test bean equals method
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingBeans() {
		UserAttrKey key = createKey();
		UserAttr record = createRecord();
		record.setKey(key);
		UserAttr record2 = createRecord();
		record2.setKey(key);
		assertTrue(record.equals(record2));
	}

	/**
	 * Test bean equals method expecting failure
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingBeans() {
		UserAttrKey key = createKey();
		UserAttr record = createRecord();
		record.setKey(key);
		UserAttr record2 = createRecord();
		UserAttrKey key2 = new UserAttrKey();
		record2.setKey(key2);
		assertFalse(record.equals(record2));
	}
	
    /**
	 * Test insert method
	 */
	@Test
	public void thatInsertCanOccurNormally() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserAttr record2 = dao.selectByPrimaryKey(key);
		assertNotNull(record2);
	}
	
	/**
	 * Test that insert with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithRecordNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		dao.insert(null);
	}

	/**
	 * Test that insert with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithPrimaryKeyNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		record.setKey(null);
		dao.insert(record);
	}
	
    /**
	 * Test that insert with null key column(s) throws verifier exception
	 */
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void thatInsertWithKeyColumnNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(null);
		record.setKey(key);
		dao.insert(record);
    }
        
    /**
	 * Test that insert with null value in required column(s) with defaults works
	 */
	 	@Test
		public void thatInsertingRecordWithMissingFieldsInserts() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
        UserAttr record = new UserAttr();
		UserAttrKey key = createKey();
		record.setKey(key);
		UserAttrKey returnedKey = dao.insert(record);
		UserAttr newRecord = dao.selectByPrimaryKey(returnedKey);
        assertNotNull(newRecord);
	}
       
	/**
	 * Test that duplicate keys throws exception
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void thatInsertDuplicateKeyThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		UserAttr record2 = createRecord();
		record2.setKey(key);
		dao.insert(record2);
	}
	
    /**
	 * Test select works as expected
	 */
	@Test
    public void thatSelectCanOccurNormally() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = createKey();
		record.setKey(key);
		
		dao.insert(record);
		UserAttr selectedRecord = dao.selectByPrimaryKey(key);
		assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatSelectWithPrimaryKeyNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		dao.selectByPrimaryKey(null);
	}
	
	/**
	 * Test that select with null value in key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatSelectWithKeyColumnNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(null);
		dao.selectByPrimaryKey(key);
	}

	/**
	 * Test that update works as expected
	 */
    @Test
	public void thatUpdateCanOccurNormally(){
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttrKey key = createKey();
		UserAttr record1 = createRecord();
	    record1.setKey(key);
		dao.insert(record1);
		UserAttr record2 = new UserAttr();
	    record2.setKey(key);
		record2.setLastMaintUserId("B");
		dao.updateByPrimaryKeySelective(record2);
              
		UserAttr returnedRecord = dao.selectByPrimaryKey(key);
		Assert.assertEquals(returnedRecord.getLastMaintUserId(), "B");
	}
	
	/**
	 * Test that update with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithRecordNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		dao.updateByPrimaryKeySelective(null);
	}
	   
	/**
	 * Test that update with null key throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithPrimaryKeyNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		record.setKey(null);
		dao.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * Test that update with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatUpdateWithKeyColumnNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(null);
		dao.updateByPrimaryKeySelective(record);
    }
    /**
	 * Test that select by index works as expected
	 */
	@Test
	public void thatSelectByAk1UserAttrCanOccurNormally() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		UserAttr record2 = new UserAttr();
		UserAttrKey key2 = new UserAttrKey();;
        key2.setUserAttrId("B");
        record2.setCrtUserId("B");
        record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
        record2.setLastMaintUserId("B");
        record2.setLastMaintTs(new Date(System.currentTimeMillis()-864000000));
        record2.setUserAttrTypeCd("B");
        record2.setUserAttrNm("B");
        record2.setUserAttrDn("B");
        record2.setUserAttrDefnTx("A long string of data");
          
        record2.setKey(key2);
		dao.insert(record2);
		UserAttr selectedRecord = dao.selectByAk1UserAttr(USERATTRNM );
		Assert.assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select by index with null index first ordinal throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatIndexSelectByAk1UserAttrWithIndexNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		dao.selectByAk1UserAttr(null );
	}   
    /**
	 * Test that select by index works as expected
	 */
	@Test
	public void thatSelectByIe1UserAttrCanOccurNormally() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		UserAttr record2 = new UserAttr();
		UserAttrKey key2 = new UserAttrKey();;
        key2.setUserAttrId("B");
        record2.setCrtUserId("B");
        record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
        record2.setLastMaintUserId("B");
        record2.setLastMaintTs(new Date(System.currentTimeMillis()-864000000));
        record2.setUserAttrTypeCd(USERATTRTYPECD);
        record2.setUserAttrNm("B");
        record2.setUserAttrDn("B");
        record2.setUserAttrDefnTx("A long string of data");
          
        record2.setKey(key2);
		dao.insert(record2);
		List<UserAttr> selectedRecords = dao.selectByIe1UserAttr(USERATTRTYPECD );
		Assert.assertEquals(2, selectedRecords.size());
	}
	
	/**
	 * Test that select by index with null index first ordinal throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatIndexSelectByIe1UserAttrWithIndexNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		dao.selectByIe1UserAttr(null );
	}   
	
	/**
	 * Test that delete works as expected
	 */
	@Test
	public void thatDeleteCanOccurNormally(){
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttr record = createRecord();
		UserAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		dao.deleteByPrimaryKey(key);
	}

	/**
	 * Test that delete with null key throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithPrimaryKeyNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		dao.deleteByPrimaryKey(null);
	}
	
	/**
	 * Test that delete with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithKeyColumnNulledThrowsException() {
		IUserAttrDAO dao = daoFactory.getUserAttrDAO();
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(null);
		dao.deleteByPrimaryKey(key);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrId(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrId(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrId(String.valueOf(1), null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrId(String.valueOf(1), null, Integer.valueOf(21), Integer.valueOf(40)); 
        assertTrue(records.size() == 20);

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithSameMinMaxWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrId(String.valueOf(11), String.valueOf(11), Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 1);
        
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithoutRowLimitsWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrId(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByAk1UserAttrWithNullValuesThrowsException() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
    
        List<UserAttr> records = userAttrDao.findRangeByUserAttrNm(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByAk1UserAttrWithRowMinNullAndRowMaxSetThrowsException() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrNm(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByAk1UserAttrWithRowLimitsWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrNm(String.valueOf(1), null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByAk1UserAttrWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrNm(String.valueOf(1), null, Integer.valueOf(21), Integer.valueOf(40));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByAk1UserAttrWithoutRowLimitsWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrNm(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
    
        
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1UserAttrWithNullValuesThrowsException() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
    
        List<UserAttr> records = userAttrDao.findRangeByUserAttrTypeCd(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1UserAttrWithRowMinNullAndRowMaxSetThrowsException() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrTypeCd(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrWithRowLimitsWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrTypeCd(String.valueOf(1), null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrTypeCd(String.valueOf(1), null, Integer.valueOf(21), Integer.valueOf(40));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrWithoutRowLimitsWorksAsExpected() {
        IUserAttrDAO userAttrDao = daoFactory.getUserAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<UserAttr> records = userAttrDao.findRangeByUserAttrTypeCd(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
    
        
    
   	/**
	 * Helper method for creating a record
	 * @return record  a DB record with all columns set
	 */
	public static UserAttr createRecord() {
		UserAttr record = new UserAttr();
		record.setCrtUserId(CRTUSERID);
		record.setCrtTs(CRTTS);
		record.setLastMaintUserId(LASTMAINTUSERID);
		record.setLastMaintTs(LASTMAINTTS);
		record.setUserAttrTypeCd(USERATTRTYPECD);
		record.setUserAttrNm(USERATTRNM);
		record.setUserAttrDn(USERATTRDN);
		record.setUserAttrDefnTx(USERATTRDEFNTX);

		return record;
	}

	/**
	 * Helper method for creating a key
	 * @return key  a record key with all columns set
	 */
	public static UserAttrKey createKey() {
		UserAttrKey key = new UserAttrKey();
		key.setUserAttrId(USERATTRID);

		return key;
	}
	
	/**
     * Helper method for creating a record when adding multiple records.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserAttr createRecord(int g, int r) {
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, r);

        
        UserAttr userAttr = new UserAttr();
        userAttr.setCrtUserId(String.valueOf(r));
        userAttr.setCrtTs(calendar.getTime());
        userAttr.setLastMaintUserId(String.valueOf(r));
        userAttr.setLastMaintTs(calendar.getTime());
        userAttr.setUserAttrTypeCd(String.valueOf(r));
        userAttr.setUserAttrNm(String.valueOf(g * 10 + r));            
        userAttr.setUserAttrDn(String.valueOf(r));
        userAttr.setUserAttrDefnTx(String.valueOf(r));

        return userAttr;
    }

    /**
     * Helper method for creating a key when adding multiple records.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserAttrKey createKey(int g, int r) {
        UserAttrKey userAttrKey = new UserAttrKey();
        userAttrKey.setUserAttrId(String.valueOf(g * 10 + r));

        return userAttrKey;
    }
    
    public void populateData() {

    IUserAttrDAO dao = daoFactory.getUserAttrDAO();

    UserAttr userAttr = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                userAttr = createRecord(g, r);
                userAttr.setKey(createKey(g, r));

                dao.insert(userAttr);
            }
        }
    }
    
}