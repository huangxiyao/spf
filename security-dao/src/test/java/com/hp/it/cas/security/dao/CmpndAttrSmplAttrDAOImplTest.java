package com.hp.it.cas.security.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
 * Test class for the CMPND_ATTR_SMPL_ATTR table DAO
 * @author CAS Generator v1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional

public class CmpndAttrSmplAttrDAOImplTest {

    private static final Calendar CALENDAR_NOW = Calendar.getInstance();
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
		CmpndAttrSmplAttrKey key = createKey();
		CmpndAttrSmplAttrKey key2 = createKey();
		assertTrue(key.equals(key2));
	}
	
	/**
	 * Test key equals a different key is false.
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingKey() {
		CmpndAttrSmplAttrKey key = createKey();
		CmpndAttrSmplAttrKey key2 = new CmpndAttrSmplAttrKey();
		assertFalse(key.equals(key2));
	}
	
	
	/**
	 *  Test key equals null key is false
	 */
	@Test
	public void thatEqualsReturnsFalseForNull() {
		CmpndAttrSmplAttrKey key = createKey();
		CmpndAttrSmplAttrKey key2 = null;
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test key equals another object is false
	 */
	@Test
	public void thatEqualsReturnsFalseForAnotherObject() {
		CmpndAttrSmplAttrKey key = createKey();
		CmpndAttrSmplAttr key2 = new CmpndAttrSmplAttr();
		assertFalse(key.equals(key2));
	}
	
	/**
	 * Test null key equals null key is true
	 */
	@Test
	public void thatEqualsRetrunsTrueForTwoNullKeys() {
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		CmpndAttrSmplAttrKey key2 = new CmpndAttrSmplAttrKey();
		assertTrue(key.equals(key2));
	}
	/**
	 * Test key equals method expecting failure.
	 */
	@Test
	public void thatEqualsReturnsFalseForKeyItem1Null() {
	    CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		CmpndAttrSmplAttrKey key2 = new CmpndAttrSmplAttrKey();
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
	public void thatEqualsReturnsFalseForKeyItem2Null() {
	    CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		CmpndAttrSmplAttrKey key2 = new CmpndAttrSmplAttrKey();
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
	     CmpndAttrSmplAttrKey key = createKey();
		 CmpndAttrSmplAttrKey key2 = createKey();
		 assertTrue(key.hashCode() == key2.hashCode());	 
	 }
	 
	 /**
	 * Hashcode for the same object is the same
	 */
	 @Test
	 public void thatHashCodesForDifferentObjectsAreNotEqual() {
	     CmpndAttrSmplAttrKey key = createKey();
		 CmpndAttrSmplAttr record = createRecord();
		 assertTrue(key.hashCode() != record.hashCode());	 
	 }
	 
	/**
	 * Test bean equals method
	 */
	@Test
	public void thatEqualsReturnsTrueForMatchingBeans() {
		CmpndAttrSmplAttrKey key = createKey();
		CmpndAttrSmplAttr record = createRecord();
		record.setKey(key);
		CmpndAttrSmplAttr record2 = createRecord();
		record2.setKey(key);
		assertTrue(record.equals(record2));
	}

	/**
	 * Test bean equals method expecting failure
	 */
	@Test
	public void thatEqualsReturnsFalseForNonMatchingBeans() {
		CmpndAttrSmplAttrKey key = createKey();
		CmpndAttrSmplAttr record = createRecord();
		record.setKey(key);
		CmpndAttrSmplAttr record2 = createRecord();
		CmpndAttrSmplAttrKey key2 = new CmpndAttrSmplAttrKey();
		record2.setKey(key2);
		assertFalse(record.equals(record2));
	}
	
    /**
	 * Test insert method
	 */
	@Test
	public void thatInsertCanOccurNormally() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		CmpndAttrSmplAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		CmpndAttrSmplAttr record2 = dao.selectByPrimaryKey(key);
		assertNotNull(record2);
	}
	
	/**
	 * Test that insert with null record throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithRecordNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		dao.insert(null);
	}

	/**
	 * Test that insert with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatInsertWithPrimaryKeyNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		record.setKey(null);
		dao.insert(record);
	}
	
    /**
	 * Test that insert with null key column(s) throws verifier exception
	 */
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void thatInsertWithKeyColumnNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		record.setKey(key);
		dao.insert(record);
    }
        
    /**
	 * Test that insert with null value in required column(s) with defaults works
	 */
	 	@Test
		public void thatInsertingRecordWithMissingFieldsInserts() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
        CmpndAttrSmplAttr record = new CmpndAttrSmplAttr();
		CmpndAttrSmplAttrKey key = createKey();
		record.setKey(key);
		CmpndAttrSmplAttrKey returnedKey = dao.insert(record);
		CmpndAttrSmplAttr newRecord = dao.selectByPrimaryKey(returnedKey);
        assertNotNull(newRecord);
	}
       
	/**
	 * Test that duplicate keys throws exception
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void thatInsertDuplicateKeyThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		CmpndAttrSmplAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		CmpndAttrSmplAttr record2 = createRecord();
		record2.setKey(key);
		dao.insert(record2);
	}
	
    /**
	 * Test select works as expected
	 */
	@Test
    public void thatSelectCanOccurNormally() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		CmpndAttrSmplAttrKey key = createKey();
		record.setKey(key);
		
		dao.insert(record);
		CmpndAttrSmplAttr selectedRecord = dao.selectByPrimaryKey(key);
		assertNotNull(selectedRecord);
	}
	
	/**
	 * Test that select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatSelectWithPrimaryKeyNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		dao.selectByPrimaryKey(null);
	}
	
	/**
	 * Test that select with null value in key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatSelectWithKeyColumnNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		dao.selectByPrimaryKey(key);
	}

    
    /**
	 * Test that discretionary select works as expected
	 */
    @Test
	public void thatDiscretionaryFindCanOccurNormally() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		CmpndAttrSmplAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		CmpndAttrSmplAttr record2 = new CmpndAttrSmplAttr();
        CmpndAttrSmplAttrKey key2 = new CmpndAttrSmplAttrKey();
        
        key2.setCmpndUserAttrId(CMPNDUSERATTRID);        
        key2.setSmplUserAttrId("B");   
        record2.setKey(key2);
		record2.setCrtUserId("B");
		record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
		dao.insert(record2);
		
              
		CmpndAttrSmplAttrKey key3 = new CmpndAttrSmplAttrKey();
        key3.setCmpndUserAttrId(CMPNDUSERATTRID);
        key3.setSmplUserAttrId(null);   
        List<CmpndAttrSmplAttr> selectedRecords = dao.selectByPrimaryKeyDiscretionary(key3);
		Assert.assertEquals(2, selectedRecords.size());
	}
	
    /**
	 * Test that discretionary select with null key throws verifier exeception
	 */
    @Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithPrimaryKeyNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		dao.selectByPrimaryKeyDiscretionary(null);
	}
	
	/**
	 * Test that discretionary select with first ordinal null throws exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDiscretionarySelectWithFirstKeyColumnNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		key.setCmpndUserAttrId(null);
		dao.selectByPrimaryKeyDiscretionary(key);
	}
    /**
	 * Test that select by index works as expected
	 */
	@Test
	public void thatSelectByIe1CmpndAttrSmplAttrCanOccurNormally() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		CmpndAttrSmplAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
              
		CmpndAttrSmplAttr record2 = new CmpndAttrSmplAttr();
		CmpndAttrSmplAttrKey key2 = new CmpndAttrSmplAttrKey();;
        key2.setCmpndUserAttrId("B");
        key2.setSmplUserAttrId(SMPLUSERATTRID);
        record2.setCrtUserId("B");
        record2.setCrtTs(new Date(System.currentTimeMillis()-864000000));
          
        record2.setKey(key2);
		dao.insert(record2);
		List<CmpndAttrSmplAttr> selectedRecords = dao.selectByIe1CmpndAttrSmplAttr(SMPLUSERATTRID );
		Assert.assertEquals(2, selectedRecords.size());
	}
	
	/**
	 * Test that select by index with null index first ordinal throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatIndexSelectByIe1CmpndAttrSmplAttrWithIndexNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		dao.selectByIe1CmpndAttrSmplAttr(null );
	}   
	
	/**
	 * Test that delete works as expected
	 */
	@Test
	public void thatDeleteCanOccurNormally(){
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttr record = createRecord();
		CmpndAttrSmplAttrKey key = createKey();
		record.setKey(key);
		dao.insert(record);
		dao.deleteByPrimaryKey(key);
	}

	/**
	 * Test that delete with null key throws verifier exeception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithPrimaryKeyNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		dao.deleteByPrimaryKey(null);
	}
	
	/**
	 * Test that delete with null key column(s) throws verifier exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void thatDeleteWithKeyColumnNulledThrowsException() {
		ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		dao.deleteByPrimaryKey(key);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, Integer.valueOf(21), Integer.valueOf(40)); 
        assertTrue(records.size() == 20);

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithSameMinMaxWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), String.valueOf(1), null, null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 10);
        
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithoutRowLimitsWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, null, null);
        assertTrue(records.size() == 100);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1CmpndAttrSmplAttrWithNullValuesThrowsException() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
    
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeBySmplUserAttrId(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1CmpndAttrSmplAttrWithRowMinNullAndRowMaxSetThrowsException() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeBySmplUserAttrId(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByIe1CmpndAttrSmplAttrWithRowLimitsWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeBySmplUserAttrId(String.valueOf(1), null, Integer.valueOf(1), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1CmpndAttrSmplAttrWithRowLimitsSelectingTheSecondPageWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeBySmplUserAttrId(String.valueOf(1), null, Integer.valueOf(21), Integer.valueOf(40));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1CmpndAttrSmplAttrWithoutRowLimitsWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        populateData();
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDao.findRangeBySmplUserAttrId(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
    
        
    @Test(expected = IllegalArgumentException.class)
    public void thatFindDistinctIe1CmpndAttrSmplAttrWithNullValuesThrowsException() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        
        List<Hashtable> records = cmpndAttrSmplAttrDao.findDistinctBySmplUserAttrId(null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void thatFindDistinctIe1CmpndAttrSmplAttrOnTheHappyPathWorksAsExpected() {
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = daoFactory.getCmpndAttrSmplAttrDAO();
        
        CmpndAttrSmplAttr cmpndAttrSmplAttr = createRecord(1, 1);
        
        cmpndAttrSmplAttr.setKey(createKey(1, 1));
        cmpndAttrSmplAttr.getKey().setSmplUserAttrId("1");
        cmpndAttrSmplAttrDao.insert(cmpndAttrSmplAttr);
        
        CmpndAttrSmplAttr cmpndAttrSmplAttr2 = createRecord(1, 1);
        cmpndAttrSmplAttr2.setKey(createKey(2, 1));
        cmpndAttrSmplAttr2.getKey().setSmplUserAttrId("1");
        cmpndAttrSmplAttrDao.insert(cmpndAttrSmplAttr2);
        
        CmpndAttrSmplAttr cmpndAttrSmplAttr3 = createRecord(1, 1);
        cmpndAttrSmplAttr3.setKey(createKey(1, 1));
        cmpndAttrSmplAttr3.getKey().setSmplUserAttrId("2");
        cmpndAttrSmplAttrDao.insert(cmpndAttrSmplAttr3);
        
        List<Hashtable> records = cmpndAttrSmplAttrDao.findDistinctBySmplUserAttrId("1");
        assertNotNull(records);
        assertTrue(records.size() == 1);
        
    }   
    
   	/**
	 * Helper method for creating a record
	 * @return record  a DB record with all columns set
	 */
	public static CmpndAttrSmplAttr createRecord() {
		CmpndAttrSmplAttr record = new CmpndAttrSmplAttr();
		record.setCrtUserId(CRTUSERID);
		record.setCrtTs(CRTTS);

		return record;
	}

	/**
	 * Helper method for creating a key
	 * @return key  a record key with all columns set
	 */
	public static CmpndAttrSmplAttrKey createKey() {
		CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
		key.setCmpndUserAttrId(CMPNDUSERATTRID);
		key.setSmplUserAttrId(SMPLUSERATTRID);

		return key;
	}
	
	/**
     * Helper method for creating a record when adding multiple records.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static CmpndAttrSmplAttr createRecord(int g, int r) {
        Calendar calendar = (Calendar) CALENDAR_NOW.clone();
        calendar.set(Calendar.DAY_OF_YEAR, r);

        
        CmpndAttrSmplAttr cmpndAttrSmplAttr = new CmpndAttrSmplAttr();
        cmpndAttrSmplAttr.setCrtUserId(String.valueOf(r));
        cmpndAttrSmplAttr.setCrtTs(calendar.getTime());

        return cmpndAttrSmplAttr;
    }

    /**
     * Helper method for creating a key when adding multiple records.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static CmpndAttrSmplAttrKey createKey(int g, int r) {
        CmpndAttrSmplAttrKey cmpndAttrSmplAttrKey = new CmpndAttrSmplAttrKey();
        cmpndAttrSmplAttrKey.setCmpndUserAttrId(String.valueOf(g));
        cmpndAttrSmplAttrKey.setSmplUserAttrId(String.valueOf(g * 10 + r));

        return cmpndAttrSmplAttrKey;
    }
    
    public void populateData() {

    ICmpndAttrSmplAttrDAO dao = daoFactory.getCmpndAttrSmplAttrDAO();

    CmpndAttrSmplAttr cmpndAttrSmplAttr = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                cmpndAttrSmplAttr = createRecord(g, r);
                cmpndAttrSmplAttr.setKey(createKey(g, r));

                dao.insert(cmpndAttrSmplAttr);
            }
        }
    }
    
}