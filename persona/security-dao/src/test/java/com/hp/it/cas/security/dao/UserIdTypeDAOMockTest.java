package com.hp.it.cas.security.dao;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IUserIdTypeDAO;
import com.hp.it.cas.security.dao.UserIdType;
import com.hp.it.cas.security.dao.UserIdTypeKey;
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
 * Tests the mocked implementation of the {@link IUserIdTypeDAO} interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
@RunWith(JMock.class)
public class UserIdTypeDAOMockTest {
    private static final Calendar CALENDAR_NOW = Calendar.getInstance();

    private Mockery context = new JUnit4Mockery();
    private IUserIdTypeDAO userIdTypeDAO;

    /**
     * Test {@link IUserIdTypeDAO#deleteByPrimaryKey(UserIdTypeKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatDoesNotExist() {
        UserIdTypeKey userIdTypeKey = createKey(1, 1);

        int count = userIdTypeDAO.deleteByPrimaryKey(userIdTypeKey);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserIdTypeDAO#deleteByPrimaryKey(UserIdTypeKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatExists() {
        UserIdType userIdType = createRecord(1);
        userIdType.setKey(createKey(1, 1));

        userIdTypeDAO.insert(userIdType);

        int count = userIdTypeDAO.deleteByPrimaryKey(userIdType.getKey());

        assertTrue(count == 1);
    }

    /**
     * Test {@link IUserIdTypeDAO#deleteByPrimaryKey(UserIdTypeKey)}.
     */
    @Test
    public void deleteByPrimaryKeyUsigNullKey() {
        int count = userIdTypeDAO.deleteByPrimaryKey(null);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserIdTypeDAO#insert(UserIdType)}.
     */
    @Test
    public void insertOfAValidRecord() {
        UserIdType userIdType = createRecord(1);
        userIdType.setKey(createKey(1, 1));

        userIdTypeDAO.insert(userIdType);
    }

    /**
     * Test {@link IUserIdTypeDAO#insert(UserIdType)}.
     */
    @Test
    public void insertOfRecordWithNullFieldsInserts() {
        UserIdType userIdType = new UserIdType();
        userIdType.setKey(createKey(1, 1));

        userIdTypeDAO.insert(userIdType);
        UserIdType record =  userIdTypeDAO.selectByPrimaryKey(userIdType.getKey());
        assertNotNull(record);
    }

    /**
     * Test {@link IUserIdTypeDAO#insert(UserIdType)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void insertOfDuplicateRecordFailsWithException() {
        UserIdType userIdType = createRecord(1);
        userIdType.setKey(createKey(1, 1));

        userIdTypeDAO.insert(userIdType);
        userIdTypeDAO.insert(userIdType);
    }

    /**
     * Test {@link IUserIdTypeDAO#insert(UserIdType)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertOfRecordWithNullKeyFailsWithException() {
        UserIdType userIdType = createRecord(1);
        userIdType.setKey(null);

        userIdTypeDAO.insert(userIdType);
    }


    /**
     * Test {@link IUserIdTypeDAO#selectByPrimaryKey(UserIdTypeKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatDoesNotExist() {
        UserIdType userIdType = createRecord(1);
        userIdType.setKey(createKey(1, 1));

        userIdType = userIdTypeDAO.selectByPrimaryKey(userIdType.getKey());

        assertNull(userIdType);
    }

    /**
     * Test {@link IUserIdTypeDAO#selectByPrimaryKey(UserIdTypeKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatExists() {
        UserIdType userIdTypeIn = createRecord(1);
        userIdTypeIn.setKey(createKey(1, 1));

        userIdTypeDAO.insert(userIdTypeIn);

        UserIdType userIdTypeOut = userIdTypeDAO.selectByPrimaryKey(userIdTypeIn.getKey());

        assertEquals(userIdTypeIn, userIdTypeOut);
    }

    /**
     * Test {@link IUserIdTypeDAO#selectByPrimaryKey(UserIdTypeKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKey() {
        UserIdType userIdType = userIdTypeDAO.selectByPrimaryKey(null);

        assertNull(userIdType);
    }

    /**
     * Test {@link IUserIdTypeDAO#selectByPrimaryKey(UserIdTypeKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKeyFields() {
        UserIdTypeKey userIdTypeKey = new UserIdTypeKey();
        UserIdType userIdType = userIdTypeDAO.selectByPrimaryKey(userIdTypeKey);

        assertNull(userIdType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        
        List<UserIdType> records = userIdTypeDAO.findRangeByUserIdTypeCd(null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
                
        List<UserIdType> records = userIdTypeDAO.findRangeByUserIdTypeCd(String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        UserIdType userIdType = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userIdType = createRecord(g);
                userIdType.setKey(createKey(g, r));
    
                userIdTypeDAO.insert(userIdType);
            }
        }
        
        List<UserIdType> records = userIdTypeDAO.findRangeByUserIdTypeCd(String.valueOf(1), null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithOutRowLimitsWorksAsExpected() {
        UserIdType userIdType = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userIdType = createRecord(g);
                userIdType.setKey(createKey(g, r));
    
                userIdTypeDAO.insert(userIdType);
            }
        }
        
        List<UserIdType> records = userIdTypeDAO.findRangeByUserIdTypeCd(String.valueOf(1), null, null, null);
        assertTrue(records.size() == 100);
    }

    /**
     * Test {@link IUserIdTypeDAO#updateByPrimaryKeySelective(UserIdType)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void updateByPrimaryKeySelectiveOfRecordThatDoesNotExist() {
        UserIdType userIdType = createRecord(1);
        userIdType.setKey(createKey(1, 1));

        userIdTypeDAO.updateByPrimaryKeySelective(userIdType);
    }

    /**
     * Test {@link IUserIdTypeDAO#updateByPrimaryKeySelective(UserIdType)}.
     */
    @Test
    public void updateByPrimaryKeySelectiveOfRecordThatExists() {
        UserIdType userIdType = createRecord(1);
        userIdType.setKey(createKey(1, 1));

        UserIdTypeKey userIdTypeKey = userIdTypeDAO.insert(userIdType);

        UserIdType userIdTypeIn = createRecord(2);
        userIdTypeIn.setKey(userIdTypeKey);
        userIdTypeDAO.updateByPrimaryKeySelective(userIdTypeIn);

        UserIdType userIdTypeOut = userIdTypeDAO.selectByPrimaryKey(userIdTypeIn.getKey());

        assertEquals(userIdType.getCrtUserId(), userIdTypeOut.getCrtUserId());
        assertEquals(userIdType.getCrtTs(), userIdTypeOut.getCrtTs());
        assertEquals(userIdTypeIn.getLastMaintUserId(), userIdTypeOut.getLastMaintUserId());
        assertEquals(userIdTypeIn.getLastMaintTs(), userIdTypeOut.getLastMaintTs());
        assertEquals(userIdTypeIn.getUserIdTypeNm(), userIdTypeOut.getUserIdTypeNm());
        assertEquals(userIdTypeIn.getUserIdTypeDn(), userIdTypeOut.getUserIdTypeDn());
    }

    /**
     * Test {@link IUserIdTypeDAO#updateByPrimaryKeySelective(UserIdType)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKey() {
        userIdTypeDAO.updateByPrimaryKeySelective(null);
    }

    /**
     * Test {@link IUserIdTypeDAO#updateByPrimaryKeySelective(UserIdType)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKeyField() {
        UserIdType userIdType = createRecord(1);

        userIdTypeDAO.updateByPrimaryKeySelective(userIdType);
    }

    /**
     * Setup a mock DAO instance used for testing.
     */
    @Before
    public void setUp() {
        ISecurityDAOFactory securityDAOFactory = new SecurityDAOFactoryImpl(context);

        userIdTypeDAO = securityDAOFactory.getUserIdTypeDAO();
    }

    /**
     * Helper method for creating a record.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserIdType createRecord(int r) {
        Calendar calendar = CALENDAR_NOW;
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
     * Helper method for creating a key.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserIdTypeKey createKey(int g, int r) {
        UserIdTypeKey userIdTypeKey = new UserIdTypeKey();
        userIdTypeKey.setUserIdTypeCd(String.valueOf(g * 10 + r));

        return userIdTypeKey;
    }
}