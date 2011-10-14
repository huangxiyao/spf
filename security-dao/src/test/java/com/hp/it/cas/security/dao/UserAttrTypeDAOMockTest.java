package com.hp.it.cas.security.dao;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IUserAttrTypeDAO;
import com.hp.it.cas.security.dao.UserAttrType;
import com.hp.it.cas.security.dao.UserAttrTypeKey;
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
 * Tests the mocked implementation of the {@link IUserAttrTypeDAO} interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
@RunWith(JMock.class)
public class UserAttrTypeDAOMockTest {
    private static final Calendar CALENDAR_NOW = Calendar.getInstance();

    private Mockery context = new JUnit4Mockery();
    private IUserAttrTypeDAO userAttrTypeDAO;

    /**
     * Test {@link IUserAttrTypeDAO#deleteByPrimaryKey(UserAttrTypeKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatDoesNotExist() {
        UserAttrTypeKey userAttrTypeKey = createKey(1, 1);

        int count = userAttrTypeDAO.deleteByPrimaryKey(userAttrTypeKey);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserAttrTypeDAO#deleteByPrimaryKey(UserAttrTypeKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatExists() {
        UserAttrType userAttrType = createRecord(1);
        userAttrType.setKey(createKey(1, 1));

        userAttrTypeDAO.insert(userAttrType);

        int count = userAttrTypeDAO.deleteByPrimaryKey(userAttrType.getKey());

        assertTrue(count == 1);
    }

    /**
     * Test {@link IUserAttrTypeDAO#deleteByPrimaryKey(UserAttrTypeKey)}.
     */
    @Test
    public void deleteByPrimaryKeyUsigNullKey() {
        int count = userAttrTypeDAO.deleteByPrimaryKey(null);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserAttrTypeDAO#insert(UserAttrType)}.
     */
    @Test
    public void insertOfAValidRecord() {
        UserAttrType userAttrType = createRecord(1);
        userAttrType.setKey(createKey(1, 1));

        userAttrTypeDAO.insert(userAttrType);
    }

    /**
     * Test {@link IUserAttrTypeDAO#insert(UserAttrType)}.
     */
    @Test
    public void insertOfRecordWithNullFieldsInserts() {
        UserAttrType userAttrType = new UserAttrType();
        userAttrType.setKey(createKey(1, 1));

        userAttrTypeDAO.insert(userAttrType);
        UserAttrType record =  userAttrTypeDAO.selectByPrimaryKey(userAttrType.getKey());
        assertNotNull(record);
    }

    /**
     * Test {@link IUserAttrTypeDAO#insert(UserAttrType)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void insertOfDuplicateRecordFailsWithException() {
        UserAttrType userAttrType = createRecord(1);
        userAttrType.setKey(createKey(1, 1));

        userAttrTypeDAO.insert(userAttrType);
        userAttrTypeDAO.insert(userAttrType);
    }

    /**
     * Test {@link IUserAttrTypeDAO#insert(UserAttrType)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertOfRecordWithNullKeyFailsWithException() {
        UserAttrType userAttrType = createRecord(1);
        userAttrType.setKey(null);

        userAttrTypeDAO.insert(userAttrType);
    }


    /**
     * Test {@link IUserAttrTypeDAO#selectByPrimaryKey(UserAttrTypeKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatDoesNotExist() {
        UserAttrType userAttrType = createRecord(1);
        userAttrType.setKey(createKey(1, 1));

        userAttrType = userAttrTypeDAO.selectByPrimaryKey(userAttrType.getKey());

        assertNull(userAttrType);
    }

    /**
     * Test {@link IUserAttrTypeDAO#selectByPrimaryKey(UserAttrTypeKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatExists() {
        UserAttrType userAttrTypeIn = createRecord(1);
        userAttrTypeIn.setKey(createKey(1, 1));

        userAttrTypeDAO.insert(userAttrTypeIn);

        UserAttrType userAttrTypeOut = userAttrTypeDAO.selectByPrimaryKey(userAttrTypeIn.getKey());

        assertEquals(userAttrTypeIn, userAttrTypeOut);
    }

    /**
     * Test {@link IUserAttrTypeDAO#selectByPrimaryKey(UserAttrTypeKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKey() {
        UserAttrType userAttrType = userAttrTypeDAO.selectByPrimaryKey(null);

        assertNull(userAttrType);
    }

    /**
     * Test {@link IUserAttrTypeDAO#selectByPrimaryKey(UserAttrTypeKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKeyFields() {
        UserAttrTypeKey userAttrTypeKey = new UserAttrTypeKey();
        UserAttrType userAttrType = userAttrTypeDAO.selectByPrimaryKey(userAttrTypeKey);

        assertNull(userAttrType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        
        List<UserAttrType> records = userAttrTypeDAO.findRangeByUserAttrTypeCd(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
                
        List<UserAttrType> records = userAttrTypeDAO.findRangeByUserAttrTypeCd(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        UserAttrType userAttrType = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttrType = createRecord(g);
                userAttrType.setKey(createKey(g, r));
    
                userAttrTypeDAO.insert(userAttrType);
            }
        }
        
        List<UserAttrType> records = userAttrTypeDAO.findRangeByUserAttrTypeCd(String.valueOf(1), null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithOutRowLimitsWorksAsExpected() {
        UserAttrType userAttrType = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttrType = createRecord(g);
                userAttrType.setKey(createKey(g, r));
    
                userAttrTypeDAO.insert(userAttrType);
            }
        }
        
        List<UserAttrType> records = userAttrTypeDAO.findRangeByUserAttrTypeCd(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }

    /**
     * Test {@link IUserAttrTypeDAO#updateByPrimaryKeySelective(UserAttrType)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void updateByPrimaryKeySelectiveOfRecordThatDoesNotExist() {
        UserAttrType userAttrType = createRecord(1);
        userAttrType.setKey(createKey(1, 1));

        userAttrTypeDAO.updateByPrimaryKeySelective(userAttrType);
    }

    /**
     * Test {@link IUserAttrTypeDAO#updateByPrimaryKeySelective(UserAttrType)}.
     */
    @Test
    public void updateByPrimaryKeySelectiveOfRecordThatExists() {
        UserAttrType userAttrType = createRecord(1);
        userAttrType.setKey(createKey(1, 1));

        UserAttrTypeKey userAttrTypeKey = userAttrTypeDAO.insert(userAttrType);

        UserAttrType userAttrTypeIn = createRecord(2);
        userAttrTypeIn.setKey(userAttrTypeKey);
        userAttrTypeDAO.updateByPrimaryKeySelective(userAttrTypeIn);

        UserAttrType userAttrTypeOut = userAttrTypeDAO.selectByPrimaryKey(userAttrTypeIn.getKey());

        assertEquals(userAttrType.getCrtUserId(), userAttrTypeOut.getCrtUserId());
        assertEquals(userAttrType.getCrtTs(), userAttrTypeOut.getCrtTs());
        assertEquals(userAttrTypeIn.getLastMaintUserId(), userAttrTypeOut.getLastMaintUserId());
        assertEquals(userAttrTypeIn.getLastMaintTs(), userAttrTypeOut.getLastMaintTs());
        assertEquals(userAttrTypeIn.getUserAttrTypeNm(), userAttrTypeOut.getUserAttrTypeNm());
        assertEquals(userAttrTypeIn.getUserAttrTypeDn(), userAttrTypeOut.getUserAttrTypeDn());
    }

    /**
     * Test {@link IUserAttrTypeDAO#updateByPrimaryKeySelective(UserAttrType)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKey() {
        userAttrTypeDAO.updateByPrimaryKeySelective(null);
    }

    /**
     * Test {@link IUserAttrTypeDAO#updateByPrimaryKeySelective(UserAttrType)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKeyField() {
        UserAttrType userAttrType = createRecord(1);

        userAttrTypeDAO.updateByPrimaryKeySelective(userAttrType);
    }

    /**
     * Setup a mock DAO instance used for testing.
     */
    @Before
    public void setUp() {
        ISecurityDAOFactory securityDAOFactory = new SecurityDAOFactoryImpl(context);

        userAttrTypeDAO = securityDAOFactory.getUserAttrTypeDAO();
    }

    /**
     * Helper method for creating a record.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserAttrType createRecord(int r) {
        Calendar calendar = CALENDAR_NOW;
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
     * Helper method for creating a key.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserAttrTypeKey createKey(int g, int r) {
        UserAttrTypeKey userAttrTypeKey = new UserAttrTypeKey();
        userAttrTypeKey.setUserAttrTypeCd(String.valueOf(g * 10 + r));

        return userAttrTypeKey;
    }
}