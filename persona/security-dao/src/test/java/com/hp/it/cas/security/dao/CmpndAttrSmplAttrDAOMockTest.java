package com.hp.it.cas.security.dao;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttr;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.mock.SecurityDAOFactoryImpl;

import org.jmock.Mockery;

import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Calendar;
import java.util.Hashtable;

/**
 * Tests the mocked implementation of the {@link ICmpndAttrSmplAttrDAO} interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
@RunWith(JMock.class)
public class CmpndAttrSmplAttrDAOMockTest {
    private static final Calendar CALENDAR_NOW = Calendar.getInstance();

    private Mockery context = new JUnit4Mockery();
    private ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDAO;

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#deleteByPrimaryKey(CmpndAttrSmplAttrKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatDoesNotExist() {
        CmpndAttrSmplAttrKey cmpndAttrSmplAttrKey = createKey(1, 1);

        int count = cmpndAttrSmplAttrDAO.deleteByPrimaryKey(cmpndAttrSmplAttrKey);

        assertTrue(count == 0);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#deleteByPrimaryKey(CmpndAttrSmplAttrKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatExists() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = createRecord(1);
        cmpndAttrSmplAttr.setKey(createKey(1, 1));

        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);

        int count = cmpndAttrSmplAttrDAO.deleteByPrimaryKey(cmpndAttrSmplAttr.getKey());

        assertTrue(count == 1);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#deleteByPrimaryKey(CmpndAttrSmplAttrKey)}.
     */
    @Test
    public void deleteByPrimaryKeyUsigNullKey() {
        int count = cmpndAttrSmplAttrDAO.deleteByPrimaryKey(null);

        assertTrue(count == 0);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#insert(CmpndAttrSmplAttr)}.
     */
    @Test
    public void insertOfAValidRecord() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = createRecord(1);
        cmpndAttrSmplAttr.setKey(createKey(1, 1));

        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#insert(CmpndAttrSmplAttr)}.
     */
    @Test
    public void insertOfRecordWithNullFieldsInserts() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = new CmpndAttrSmplAttr();
        cmpndAttrSmplAttr.setKey(createKey(1, 1));

        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
        CmpndAttrSmplAttr record =  cmpndAttrSmplAttrDAO.selectByPrimaryKey(cmpndAttrSmplAttr.getKey());
        assertNotNull(record);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#insert(CmpndAttrSmplAttr)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void insertOfDuplicateRecordFailsWithException() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = createRecord(1);
        cmpndAttrSmplAttr.setKey(createKey(1, 1));

        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#insert(CmpndAttrSmplAttr)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertOfRecordWithNullKeyFailsWithException() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = createRecord(1);
        cmpndAttrSmplAttr.setKey(null);

        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
    }


    /**
     * Test {@link ICmpndAttrSmplAttrDAO#selectByPrimaryKeyDiscretionary(CmpndAttrSmplAttrKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithFullKey() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = null;

        for (int r = 1; r <= 10; ++r) {
            cmpndAttrSmplAttr = createRecord(r);
            cmpndAttrSmplAttr.setKey(createKey(1, r));

            cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
        }

        List<CmpndAttrSmplAttr> cmpndAttrSmplAttrList = cmpndAttrSmplAttrDAO.selectByPrimaryKeyDiscretionary(cmpndAttrSmplAttr.getKey());

        assertTrue(cmpndAttrSmplAttrList.size() == 1);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#selectByPrimaryKeyDiscretionary(CmpndAttrSmplAttrKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithPartialKey() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                cmpndAttrSmplAttr = createRecord(g);
                cmpndAttrSmplAttr.setKey(createKey(g, r));

                cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
            }
        }

        CmpndAttrSmplAttrKey cmpndAttrSmplAttrKey = createKey(10, 10);
        cmpndAttrSmplAttrKey.setSmplUserAttrId(null);

        List<CmpndAttrSmplAttr> cmpndAttrSmplAttrList = cmpndAttrSmplAttrDAO.selectByPrimaryKeyDiscretionary(cmpndAttrSmplAttrKey);

        assertTrue(cmpndAttrSmplAttrList.size() == 10);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#selectByPrimaryKey(CmpndAttrSmplAttrKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatDoesNotExist() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = createRecord(1);
        cmpndAttrSmplAttr.setKey(createKey(1, 1));

        cmpndAttrSmplAttr = cmpndAttrSmplAttrDAO.selectByPrimaryKey(cmpndAttrSmplAttr.getKey());

        assertNull(cmpndAttrSmplAttr);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#selectByPrimaryKey(CmpndAttrSmplAttrKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatExists() {
        CmpndAttrSmplAttr cmpndAttrSmplAttrIn = createRecord(1);
        cmpndAttrSmplAttrIn.setKey(createKey(1, 1));

        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttrIn);

        CmpndAttrSmplAttr cmpndAttrSmplAttrOut = cmpndAttrSmplAttrDAO.selectByPrimaryKey(cmpndAttrSmplAttrIn.getKey());

        assertEquals(cmpndAttrSmplAttrIn, cmpndAttrSmplAttrOut);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#selectByPrimaryKey(CmpndAttrSmplAttrKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKey() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = cmpndAttrSmplAttrDAO.selectByPrimaryKey(null);

        assertNull(cmpndAttrSmplAttr);
    }

    /**
     * Test {@link ICmpndAttrSmplAttrDAO#selectByPrimaryKey(CmpndAttrSmplAttrKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKeyFields() {
        CmpndAttrSmplAttrKey cmpndAttrSmplAttrKey = new CmpndAttrSmplAttrKey();
        CmpndAttrSmplAttr cmpndAttrSmplAttr = cmpndAttrSmplAttrDAO.selectByPrimaryKey(cmpndAttrSmplAttrKey);

        assertNull(cmpndAttrSmplAttr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDAO.findRangeByCmpndUserAttrIdBySmplUserAttrId(null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
                
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDAO.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                cmpndAttrSmplAttr = createRecord(g);
                cmpndAttrSmplAttr.setKey(createKey(g, r));
    
                cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
            }
        }
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDAO.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithOutRowLimitsWorksAsExpected() {
        CmpndAttrSmplAttr cmpndAttrSmplAttr = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                cmpndAttrSmplAttr = createRecord(g);
                cmpndAttrSmplAttr.setKey(createKey(g, r));
    
                cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
            }
        }
        
        List<CmpndAttrSmplAttr> records = cmpndAttrSmplAttrDAO.findRangeByCmpndUserAttrIdBySmplUserAttrId(String.valueOf(1), null, null, null, null, null);
        assertTrue(records.size() == 100);
    }

        @Test(expected = IllegalArgumentException.class)
    public void thatFindDistinctIe1CmpndAttrSmplAttrWithNullValuesThrowsException() {
        
        List<Hashtable> records = cmpndAttrSmplAttrDAO.findDistinctBySmplUserAttrId(null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void thatFindDistinctIe1CmpndAttrSmplAttrOnTheHappyPathWorksAsExpected() {
        
        CmpndAttrSmplAttr cmpndAttrSmplAttr = createRecord(1);
        cmpndAttrSmplAttr.setKey(createKey(1, 1));
        cmpndAttrSmplAttr.getKey().setSmplUserAttrId("1");
        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr);
        
        CmpndAttrSmplAttr cmpndAttrSmplAttr2 = createRecord(1);
        cmpndAttrSmplAttr2.setKey(createKey(2, 1));
        cmpndAttrSmplAttr2.getKey().setSmplUserAttrId("1");
        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr2);
        
        CmpndAttrSmplAttr cmpndAttrSmplAttr3 = createRecord(1);
        cmpndAttrSmplAttr3.setKey(createKey(1, 1));
        cmpndAttrSmplAttr3.getKey().setSmplUserAttrId("2");
        cmpndAttrSmplAttrDAO.insert(cmpndAttrSmplAttr3);
        
        List<Hashtable> records = cmpndAttrSmplAttrDAO.findDistinctBySmplUserAttrId("1");
        assertNotNull(records);
        assertTrue(records.size() == 1);
        
    
    }
        
    /**
     * Setup a mock DAO instance used for testing.
     */
    @Before
    public void setUp() {
        ISecurityDAOFactory securityDAOFactory = new SecurityDAOFactoryImpl(context);

        cmpndAttrSmplAttrDAO = securityDAOFactory.getCmpndAttrSmplAttrDAO();
    }

    /**
     * Helper method for creating a record.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static CmpndAttrSmplAttr createRecord(int r) {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, r);

        CmpndAttrSmplAttr cmpndAttrSmplAttr = new CmpndAttrSmplAttr();
        cmpndAttrSmplAttr.setCrtUserId(String.valueOf(r));
        cmpndAttrSmplAttr.setCrtTs(calendar.getTime());

        return cmpndAttrSmplAttr;
    }

    /**
     * Helper method for creating a key.
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
}