package com.hp.it.cas.security.dao;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
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

/**
 * Tests the mocked implementation of the {@link IUserAttrDAO} interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
@RunWith(JMock.class)
public class UserAttrDAOMockTest {
    private static final Calendar CALENDAR_NOW = Calendar.getInstance();

    private Mockery context = new JUnit4Mockery();
    private IUserAttrDAO userAttrDAO;

    /**
     * Test {@link IUserAttrDAO#deleteByPrimaryKey(UserAttrKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatDoesNotExist() {
        UserAttrKey userAttrKey = createKey(1, 1);

        int count = userAttrDAO.deleteByPrimaryKey(userAttrKey);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserAttrDAO#deleteByPrimaryKey(UserAttrKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatExists() {
        UserAttr userAttr = createRecord(1);
        userAttr.setKey(createKey(1, 1));

        userAttrDAO.insert(userAttr);

        int count = userAttrDAO.deleteByPrimaryKey(userAttr.getKey());

        assertTrue(count == 1);
    }

    /**
     * Test {@link IUserAttrDAO#deleteByPrimaryKey(UserAttrKey)}.
     */
    @Test
    public void deleteByPrimaryKeyUsigNullKey() {
        int count = userAttrDAO.deleteByPrimaryKey(null);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserAttrDAO#insert(UserAttr)}.
     */
    @Test
    public void insertOfAValidRecord() {
        UserAttr userAttr = createRecord(1);
        userAttr.setKey(createKey(1, 1));

        userAttrDAO.insert(userAttr);
    }

    /**
     * Test {@link IUserAttrDAO#insert(UserAttr)}.
     */
    @Test
    public void insertOfRecordWithNullFieldsInserts() {
        UserAttr userAttr = new UserAttr();
        userAttr.setKey(createKey(1, 1));

        userAttrDAO.insert(userAttr);
        UserAttr record =  userAttrDAO.selectByPrimaryKey(userAttr.getKey());
        assertNotNull(record);
    }

    /**
     * Test {@link IUserAttrDAO#insert(UserAttr)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void insertOfDuplicateRecordFailsWithException() {
        UserAttr userAttr = createRecord(1);
        userAttr.setKey(createKey(1, 1));

        userAttrDAO.insert(userAttr);
        userAttrDAO.insert(userAttr);
    }

    /**
     * Test {@link IUserAttrDAO#insert(UserAttr)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertOfRecordWithNullKeyFailsWithException() {
        UserAttr userAttr = createRecord(1);
        userAttr.setKey(null);

        userAttrDAO.insert(userAttr);
    }


    /**
     * Test {@link IUserAttrDAO#selectByPrimaryKey(UserAttrKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatDoesNotExist() {
        UserAttr userAttr = createRecord(1);
        userAttr.setKey(createKey(1, 1));

        userAttr = userAttrDAO.selectByPrimaryKey(userAttr.getKey());

        assertNull(userAttr);
    }

    /**
     * Test {@link IUserAttrDAO#selectByPrimaryKey(UserAttrKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatExists() {
        UserAttr userAttrIn = createRecord(1);
        userAttrIn.setKey(createKey(1, 1));

        userAttrDAO.insert(userAttrIn);

        UserAttr userAttrOut = userAttrDAO.selectByPrimaryKey(userAttrIn.getKey());

        assertEquals(userAttrIn, userAttrOut);
    }

    /**
     * Test {@link IUserAttrDAO#selectByPrimaryKey(UserAttrKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKey() {
        UserAttr userAttr = userAttrDAO.selectByPrimaryKey(null);

        assertNull(userAttr);
    }

    /**
     * Test {@link IUserAttrDAO#selectByPrimaryKey(UserAttrKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKeyFields() {
        UserAttrKey userAttrKey = new UserAttrKey();
        UserAttr userAttr = userAttrDAO.selectByPrimaryKey(userAttrKey);

        assertNull(userAttr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrId(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
                
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrId(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        UserAttr userAttr = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttr = createRecord(g);
                userAttr.setKey(createKey(g, r));
    
                userAttrDAO.insert(userAttr);
            }
        }
        
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrId(String.valueOf(1), null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithOutRowLimitsWorksAsExpected() {
        UserAttr userAttr = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttr = createRecord(g);
                userAttr.setKey(createKey(g, r));
    
                userAttrDAO.insert(userAttr);
            }
        }
        
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrId(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }

    /**
     * Test {@link IUserAttrDAO#updateByPrimaryKeySelective(UserAttr)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void updateByPrimaryKeySelectiveOfRecordThatDoesNotExist() {
        UserAttr userAttr = createRecord(1);
        userAttr.setKey(createKey(1, 1));

        userAttrDAO.updateByPrimaryKeySelective(userAttr);
    }

    /**
     * Test {@link IUserAttrDAO#updateByPrimaryKeySelective(UserAttr)}.
     */
    @Test
    public void updateByPrimaryKeySelectiveOfRecordThatExists() {
        UserAttr userAttr = createRecord(1);
        userAttr.setKey(createKey(1, 1));

        UserAttrKey userAttrKey = userAttrDAO.insert(userAttr);

        UserAttr userAttrIn = createRecord(2);
        userAttrIn.setKey(userAttrKey);
        userAttrDAO.updateByPrimaryKeySelective(userAttrIn);

        UserAttr userAttrOut = userAttrDAO.selectByPrimaryKey(userAttrIn.getKey());

        assertEquals(userAttr.getCrtUserId(), userAttrOut.getCrtUserId());
        assertEquals(userAttr.getCrtTs(), userAttrOut.getCrtTs());
        assertEquals(userAttrIn.getLastMaintUserId(), userAttrOut.getLastMaintUserId());
        assertEquals(userAttrIn.getLastMaintTs(), userAttrOut.getLastMaintTs());
        assertEquals(userAttrIn.getUserAttrTypeCd(), userAttrOut.getUserAttrTypeCd());
        assertEquals(userAttrIn.getUserAttrNm(), userAttrOut.getUserAttrNm());
        assertEquals(userAttrIn.getUserAttrDn(), userAttrOut.getUserAttrDn());
        assertEquals(userAttrIn.getUserAttrDefnTx(), userAttrOut.getUserAttrDefnTx());
    }

    /**
     * Test {@link IUserAttrDAO#updateByPrimaryKeySelective(UserAttr)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKey() {
        userAttrDAO.updateByPrimaryKeySelective(null);
    }

    /**
     * Test {@link IUserAttrDAO#updateByPrimaryKeySelective(UserAttr)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKeyField() {
        UserAttr userAttr = createRecord(1);

        userAttrDAO.updateByPrimaryKeySelective(userAttr);
    }

    /**
     * Test {@link IUserAttrDAO#selectByAk1UserAttr(String)}.
     */
    @Test
    public void selectByAk1UserAttrOfRecordsThatExist() {
        UserAttr userAttrIn = createRecord(1);
        userAttrIn.setKey(createKey(1, 1));

        userAttrDAO.insert(userAttrIn);

        UserAttr userAttrOut = userAttrDAO.selectByAk1UserAttr(
                userAttrIn.getUserAttrNm()
                );

        assertEquals(userAttrIn, userAttrOut);
    }

        
    /**
     * Test {@link IUserAttrDAO#selectByIe1UserAttr(String)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByIe1UserAttrOfRecordsThatExist() {
        UserAttr userAttr = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                userAttr = createRecord(g);
                userAttr.setKey(createKey(g, r));

                userAttrDAO.insert(userAttr);
            }
        }

        List<UserAttr> userAttrList = userAttrDAO.selectByIe1UserAttr(
                userAttr.getUserAttrTypeCd()
                );

        assertTrue(userAttrList.size() == 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1UserAttrWithNullValuesThrowsException() {
    
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrTypeCd(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByIe1UserAttrWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrTypeCd(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrWithRowLimitsWorksAsExpected() {
        UserAttr userAttr = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttr = createRecord(g);
                userAttr.setKey(createKey(g, r));
    
                userAttrDAO.insert(userAttr);
            }
        }
        
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrTypeCd(String.valueOf(1), null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByIe1UserAttrWithOutRowLimitsWorksAsExpected() {
        UserAttr userAttr = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttr = createRecord(g);
                userAttr.setKey(createKey(g, r));
    
                userAttrDAO.insert(userAttr);
            }
        }
        
        List<UserAttr> records = userAttrDAO.findRangeByUserAttrTypeCd(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }
        
    /**
     * Setup a mock DAO instance used for testing.
     */
    @Before
    public void setUp() {
        ISecurityDAOFactory securityDAOFactory = new SecurityDAOFactoryImpl(context);

        userAttrDAO = securityDAOFactory.getUserAttrDAO();
    }

    /**
     * Helper method for creating a record.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserAttr createRecord(int r) {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, r);

        UserAttr userAttr = new UserAttr();
        userAttr.setCrtUserId(String.valueOf(r));
        userAttr.setCrtTs(calendar.getTime());
        userAttr.setLastMaintUserId(String.valueOf(r));
        userAttr.setLastMaintTs(calendar.getTime());
        userAttr.setUserAttrTypeCd(String.valueOf(r));
        userAttr.setUserAttrNm(String.valueOf(r));
        userAttr.setUserAttrDn(String.valueOf(r));
        userAttr.setUserAttrDefnTx(String.valueOf(r));

        return userAttr;
    }

    /**
     * Helper method for creating a key.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserAttrKey createKey(int g, int r) {
        UserAttrKey userAttrKey = new UserAttrKey();
        userAttrKey.setUserAttrId(String.valueOf(g * 10 + r));

        return userAttrKey;
    }
}