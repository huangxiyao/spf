package com.hp.it.cas.security.dao;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsn;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;
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
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Hashtable;

/**
 * Tests the mocked implementation of the {@link IAppCmpndAttrSmplAttrPrmsnDAO} interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
@RunWith(JMock.class)
public class AppCmpndAttrSmplAttrPrmsnDAOMockTest {
    private static final Calendar CALENDAR_NOW = Calendar.getInstance();

    private Mockery context = new JUnit4Mockery();
    private IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDAO;

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#deleteByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatDoesNotExist() {
        AppCmpndAttrSmplAttrPrmsnKey appCmpndAttrSmplAttrPrmsnKey = createKey(1, 1);

        int count = appCmpndAttrSmplAttrPrmsnDAO.deleteByPrimaryKey(appCmpndAttrSmplAttrPrmsnKey);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#deleteByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatExists() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = createRecord(1);
        appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, 1));

        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);

        int count = appCmpndAttrSmplAttrPrmsnDAO.deleteByPrimaryKey(appCmpndAttrSmplAttrPrmsn.getKey());

        assertTrue(count == 1);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#deleteByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @Test
    public void deleteByPrimaryKeyUsigNullKey() {
        int count = appCmpndAttrSmplAttrPrmsnDAO.deleteByPrimaryKey(null);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#insert(AppCmpndAttrSmplAttrPrmsn)}.
     */
    @Test
    public void insertOfAValidRecord() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = createRecord(1);
        appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, 1));

        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#insert(AppCmpndAttrSmplAttrPrmsn)}.
     */
    @Test
    public void insertOfRecordWithNullFieldsInserts() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = new AppCmpndAttrSmplAttrPrmsn();
        appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, 1));

        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
        AppCmpndAttrSmplAttrPrmsn record =  appCmpndAttrSmplAttrPrmsnDAO.selectByPrimaryKey(appCmpndAttrSmplAttrPrmsn.getKey());
        assertNotNull(record);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#insert(AppCmpndAttrSmplAttrPrmsn)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void insertOfDuplicateRecordFailsWithException() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = createRecord(1);
        appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, 1));

        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#insert(AppCmpndAttrSmplAttrPrmsn)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertOfRecordWithNullKeyFailsWithException() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = createRecord(1);
        appCmpndAttrSmplAttrPrmsn.setKey(null);

        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
    }


    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#selectByPrimaryKeyDiscretionary(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithFullKey() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = null;

        for (int r = 1; r <= 10; ++r) {
            appCmpndAttrSmplAttrPrmsn = createRecord(r);
            appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, r));

            appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
        }

        List<AppCmpndAttrSmplAttrPrmsn> appCmpndAttrSmplAttrPrmsnList = appCmpndAttrSmplAttrPrmsnDAO.selectByPrimaryKeyDiscretionary(appCmpndAttrSmplAttrPrmsn.getKey());

        assertTrue(appCmpndAttrSmplAttrPrmsnList.size() == 1);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#selectByPrimaryKeyDiscretionary(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithPartialKey() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                appCmpndAttrSmplAttrPrmsn = createRecord(g);
                appCmpndAttrSmplAttrPrmsn.setKey(createKey(g, r));

                appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
            }
        }

        AppCmpndAttrSmplAttrPrmsnKey appCmpndAttrSmplAttrPrmsnKey = createKey(10, 10);
        appCmpndAttrSmplAttrPrmsnKey.setSmplUserAttrId(null);

        List<AppCmpndAttrSmplAttrPrmsn> appCmpndAttrSmplAttrPrmsnList = appCmpndAttrSmplAttrPrmsnDAO.selectByPrimaryKeyDiscretionary(appCmpndAttrSmplAttrPrmsnKey);

        assertTrue(appCmpndAttrSmplAttrPrmsnList.size() == 10);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#selectByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatDoesNotExist() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = createRecord(1);
        appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, 1));

        appCmpndAttrSmplAttrPrmsn = appCmpndAttrSmplAttrPrmsnDAO.selectByPrimaryKey(appCmpndAttrSmplAttrPrmsn.getKey());

        assertNull(appCmpndAttrSmplAttrPrmsn);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#selectByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatExists() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsnIn = createRecord(1);
        appCmpndAttrSmplAttrPrmsnIn.setKey(createKey(1, 1));

        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsnIn);

        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsnOut = appCmpndAttrSmplAttrPrmsnDAO.selectByPrimaryKey(appCmpndAttrSmplAttrPrmsnIn.getKey());

        assertEquals(appCmpndAttrSmplAttrPrmsnIn, appCmpndAttrSmplAttrPrmsnOut);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#selectByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKey() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = appCmpndAttrSmplAttrPrmsnDAO.selectByPrimaryKey(null);

        assertNull(appCmpndAttrSmplAttrPrmsn);
    }

    /**
     * Test {@link IAppCmpndAttrSmplAttrPrmsnDAO#selectByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKeyFields() {
        AppCmpndAttrSmplAttrPrmsnKey appCmpndAttrSmplAttrPrmsnKey = new AppCmpndAttrSmplAttrPrmsnKey();
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = appCmpndAttrSmplAttrPrmsnDAO.selectByPrimaryKey(appCmpndAttrSmplAttrPrmsnKey);

        assertNull(appCmpndAttrSmplAttrPrmsn);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDAO.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(null, null, null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
                
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDAO.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), BigDecimal.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                appCmpndAttrSmplAttrPrmsn = createRecord(g);
                appCmpndAttrSmplAttrPrmsn.setKey(createKey(g, r));
    
                appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
            }
        }
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDAO.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), null, null, null, null, null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithOutRowLimitsWorksAsExpected() {
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                appCmpndAttrSmplAttrPrmsn = createRecord(g);
                appCmpndAttrSmplAttrPrmsn.setKey(createKey(g, r));
    
                appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
            }
        }
        
        List<AppCmpndAttrSmplAttrPrmsn> records = appCmpndAttrSmplAttrPrmsnDAO.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal.valueOf(1), null, null, null, null, null, null, null);
        assertTrue(records.size() == 100);
    }

        @Test(expected = IllegalArgumentException.class)
    public void thatFindDistinctIe1AppCmpndAttrSmplAttrPWithNullValuesThrowsException() {
        
        List<Hashtable> records = appCmpndAttrSmplAttrPrmsnDAO.findDistinctByCmpndUserAttrIdBySmplUserAttrId(null, null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void thatFindDistinctIe1AppCmpndAttrSmplAttrPOnTheHappyPathWorksAsExpected() {
        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = createRecord(1);
        appCmpndAttrSmplAttrPrmsn.setKey(createKey(1, 1));
        appCmpndAttrSmplAttrPrmsn.getKey().setCmpndUserAttrId("1");
        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn);
        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn2 = createRecord(1);
        appCmpndAttrSmplAttrPrmsn2.setKey(createKey(2, 1));
        appCmpndAttrSmplAttrPrmsn2.getKey().setCmpndUserAttrId("1");
        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn2);
        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn3 = createRecord(1);
        appCmpndAttrSmplAttrPrmsn3.setKey(createKey(1, 1));
        appCmpndAttrSmplAttrPrmsn3.getKey().setCmpndUserAttrId("2");
        appCmpndAttrSmplAttrPrmsnDAO.insert(appCmpndAttrSmplAttrPrmsn3);
        
        List<Hashtable> records = appCmpndAttrSmplAttrPrmsnDAO.findDistinctByCmpndUserAttrIdBySmplUserAttrId("1", null);
        assertNotNull(records);
        assertTrue(records.size() == 2);
        assertTrue(!records.get(0).equals(records.get(1)));
        
    
    }
        
    /**
     * Setup a mock DAO instance used for testing.
     */
    @Before
    public void setUp() {
        ISecurityDAOFactory securityDAOFactory = new SecurityDAOFactoryImpl(context);

        appCmpndAttrSmplAttrPrmsnDAO = securityDAOFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
    }

    /**
     * Helper method for creating a record.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static AppCmpndAttrSmplAttrPrmsn createRecord(int r) {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, r);

        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = new AppCmpndAttrSmplAttrPrmsn();
        appCmpndAttrSmplAttrPrmsn.setCrtUserId(String.valueOf(r));
        appCmpndAttrSmplAttrPrmsn.setCrtTs(calendar.getTime());

        return appCmpndAttrSmplAttrPrmsn;
    }

    /**
     * Helper method for creating a key.
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
}