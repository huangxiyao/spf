package com.hp.it.cas.security.dao;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.AppUserAttrPrmsn;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;
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
 * Tests the mocked implementation of the {@link IAppUserAttrPrmsnDAO} interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
@RunWith(JMock.class)
public class AppUserAttrPrmsnDAOMockTest {
    private static final Calendar CALENDAR_NOW = Calendar.getInstance();

    private Mockery context = new JUnit4Mockery();
    private IAppUserAttrPrmsnDAO appUserAttrPrmsnDAO;

    /**
     * Test {@link IAppUserAttrPrmsnDAO#deleteByPrimaryKey(AppUserAttrPrmsnKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatDoesNotExist() {
        AppUserAttrPrmsnKey appUserAttrPrmsnKey = createKey(1, 1);

        int count = appUserAttrPrmsnDAO.deleteByPrimaryKey(appUserAttrPrmsnKey);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#deleteByPrimaryKey(AppUserAttrPrmsnKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatExists() {
        AppUserAttrPrmsn appUserAttrPrmsn = createRecord(1);
        appUserAttrPrmsn.setKey(createKey(1, 1));

        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);

        int count = appUserAttrPrmsnDAO.deleteByPrimaryKey(appUserAttrPrmsn.getKey());

        assertTrue(count == 1);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#deleteByPrimaryKey(AppUserAttrPrmsnKey)}.
     */
    @Test
    public void deleteByPrimaryKeyUsigNullKey() {
        int count = appUserAttrPrmsnDAO.deleteByPrimaryKey(null);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#insert(AppUserAttrPrmsn)}.
     */
    @Test
    public void insertOfAValidRecord() {
        AppUserAttrPrmsn appUserAttrPrmsn = createRecord(1);
        appUserAttrPrmsn.setKey(createKey(1, 1));

        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#insert(AppUserAttrPrmsn)}.
     */
    @Test
    public void insertOfRecordWithNullFieldsInserts() {
        AppUserAttrPrmsn appUserAttrPrmsn = new AppUserAttrPrmsn();
        appUserAttrPrmsn.setKey(createKey(1, 1));

        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
        AppUserAttrPrmsn record =  appUserAttrPrmsnDAO.selectByPrimaryKey(appUserAttrPrmsn.getKey());
        assertNotNull(record);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#insert(AppUserAttrPrmsn)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void insertOfDuplicateRecordFailsWithException() {
        AppUserAttrPrmsn appUserAttrPrmsn = createRecord(1);
        appUserAttrPrmsn.setKey(createKey(1, 1));

        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#insert(AppUserAttrPrmsn)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertOfRecordWithNullKeyFailsWithException() {
        AppUserAttrPrmsn appUserAttrPrmsn = createRecord(1);
        appUserAttrPrmsn.setKey(null);

        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
    }


    /**
     * Test {@link IAppUserAttrPrmsnDAO#selectByPrimaryKeyDiscretionary(AppUserAttrPrmsnKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithFullKey() {
        AppUserAttrPrmsn appUserAttrPrmsn = null;

        for (int r = 1; r <= 10; ++r) {
            appUserAttrPrmsn = createRecord(r);
            appUserAttrPrmsn.setKey(createKey(1, r));

            appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
        }

        List<AppUserAttrPrmsn> appUserAttrPrmsnList = appUserAttrPrmsnDAO.selectByPrimaryKeyDiscretionary(appUserAttrPrmsn.getKey());

        assertTrue(appUserAttrPrmsnList.size() == 1);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#selectByPrimaryKeyDiscretionary(AppUserAttrPrmsnKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithPartialKey() {
        AppUserAttrPrmsn appUserAttrPrmsn = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                appUserAttrPrmsn = createRecord(g);
                appUserAttrPrmsn.setKey(createKey(g, r));

                appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
            }
        }

        AppUserAttrPrmsnKey appUserAttrPrmsnKey = createKey(10, 10);
        appUserAttrPrmsnKey.setUserAttrId(null);

        List<AppUserAttrPrmsn> appUserAttrPrmsnList = appUserAttrPrmsnDAO.selectByPrimaryKeyDiscretionary(appUserAttrPrmsnKey);

        assertTrue(appUserAttrPrmsnList.size() == 10);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#selectByPrimaryKey(AppUserAttrPrmsnKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatDoesNotExist() {
        AppUserAttrPrmsn appUserAttrPrmsn = createRecord(1);
        appUserAttrPrmsn.setKey(createKey(1, 1));

        appUserAttrPrmsn = appUserAttrPrmsnDAO.selectByPrimaryKey(appUserAttrPrmsn.getKey());

        assertNull(appUserAttrPrmsn);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#selectByPrimaryKey(AppUserAttrPrmsnKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatExists() {
        AppUserAttrPrmsn appUserAttrPrmsnIn = createRecord(1);
        appUserAttrPrmsnIn.setKey(createKey(1, 1));

        appUserAttrPrmsnDAO.insert(appUserAttrPrmsnIn);

        AppUserAttrPrmsn appUserAttrPrmsnOut = appUserAttrPrmsnDAO.selectByPrimaryKey(appUserAttrPrmsnIn.getKey());

        assertEquals(appUserAttrPrmsnIn, appUserAttrPrmsnOut);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#selectByPrimaryKey(AppUserAttrPrmsnKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKey() {
        AppUserAttrPrmsn appUserAttrPrmsn = appUserAttrPrmsnDAO.selectByPrimaryKey(null);

        assertNull(appUserAttrPrmsn);
    }

    /**
     * Test {@link IAppUserAttrPrmsnDAO#selectByPrimaryKey(AppUserAttrPrmsnKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKeyFields() {
        AppUserAttrPrmsnKey appUserAttrPrmsnKey = new AppUserAttrPrmsnKey();
        AppUserAttrPrmsn appUserAttrPrmsn = appUserAttrPrmsnDAO.selectByPrimaryKey(appUserAttrPrmsnKey);

        assertNull(appUserAttrPrmsn);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDAO.findRangeByAppPrtflIdByUserAttrId(null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
                
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDAO.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), BigDecimal.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        AppUserAttrPrmsn appUserAttrPrmsn = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                appUserAttrPrmsn = createRecord(g);
                appUserAttrPrmsn.setKey(createKey(g, r));
    
                appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
            }
        }
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDAO.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), null, null, null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithOutRowLimitsWorksAsExpected() {
        AppUserAttrPrmsn appUserAttrPrmsn = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                appUserAttrPrmsn = createRecord(g);
                appUserAttrPrmsn.setKey(createKey(g, r));
    
                appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
            }
        }
        
        List<AppUserAttrPrmsn> records = appUserAttrPrmsnDAO.findRangeByAppPrtflIdByUserAttrId(BigDecimal.valueOf(1), null, null, null, null, null);
        assertTrue(records.size() == 100);
    }

        @Test(expected = IllegalArgumentException.class)
    public void thatFindDistinctIe1AppUserAttrPrmsnWithNullValuesThrowsException() {
        
        List<Hashtable> records = appUserAttrPrmsnDAO.findDistinctByUserAttrId(null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void thatFindDistinctIe1AppUserAttrPrmsnOnTheHappyPathWorksAsExpected() {
        
        AppUserAttrPrmsn appUserAttrPrmsn = createRecord(1);
        appUserAttrPrmsn.setKey(createKey(1, 1));
        appUserAttrPrmsn.getKey().setUserAttrId("1");
        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn);
        
        AppUserAttrPrmsn appUserAttrPrmsn2 = createRecord(1);
        appUserAttrPrmsn2.setKey(createKey(2, 1));
        appUserAttrPrmsn2.getKey().setUserAttrId("1");
        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn2);
        
        AppUserAttrPrmsn appUserAttrPrmsn3 = createRecord(1);
        appUserAttrPrmsn3.setKey(createKey(1, 1));
        appUserAttrPrmsn3.getKey().setUserAttrId("2");
        appUserAttrPrmsnDAO.insert(appUserAttrPrmsn3);
        
        List<Hashtable> records = appUserAttrPrmsnDAO.findDistinctByUserAttrId("1");
        assertNotNull(records);
        assertTrue(records.size() == 1);
        
    
    }
        
    /**
     * Setup a mock DAO instance used for testing.
     */
    @Before
    public void setUp() {
        ISecurityDAOFactory securityDAOFactory = new SecurityDAOFactoryImpl(context);

        appUserAttrPrmsnDAO = securityDAOFactory.getAppUserAttrPrmsnDAO();
    }

    /**
     * Helper method for creating a record.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static AppUserAttrPrmsn createRecord(int r) {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, r);

        AppUserAttrPrmsn appUserAttrPrmsn = new AppUserAttrPrmsn();
        appUserAttrPrmsn.setCrtUserId(String.valueOf(r));
        appUserAttrPrmsn.setCrtTs(calendar.getTime());

        return appUserAttrPrmsn;
    }

    /**
     * Helper method for creating a key.
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
}