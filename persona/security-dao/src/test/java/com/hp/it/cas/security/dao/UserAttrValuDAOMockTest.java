package com.hp.it.cas.security.dao;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IUserAttrValuDAO;
import com.hp.it.cas.security.dao.UserAttrValu;
import com.hp.it.cas.security.dao.UserAttrValuKey;
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
 * Tests the mocked implementation of the {@link IUserAttrValuDAO} interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
@RunWith(JMock.class)
public class UserAttrValuDAOMockTest {
    private static final Calendar CALENDAR_NOW = Calendar.getInstance();

    private Mockery context = new JUnit4Mockery();
    private IUserAttrValuDAO userAttrValuDAO;

    /**
     * Test {@link IUserAttrValuDAO#deleteByPrimaryKey(UserAttrValuKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatDoesNotExist() {
        UserAttrValuKey userAttrValuKey = createKey(1, 1);

        int count = userAttrValuDAO.deleteByPrimaryKey(userAttrValuKey);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserAttrValuDAO#deleteByPrimaryKey(UserAttrValuKey)}.
     */
    @Test
    public void deleteByPrimaryKeyOfRecordThatExists() {
        UserAttrValu userAttrValu = createRecord(1);
        userAttrValu.setKey(createKey(1, 1));

        userAttrValuDAO.insert(userAttrValu);

        int count = userAttrValuDAO.deleteByPrimaryKey(userAttrValu.getKey());

        assertTrue(count == 1);
    }

    /**
     * Test {@link IUserAttrValuDAO#deleteByPrimaryKey(UserAttrValuKey)}.
     */
    @Test
    public void deleteByPrimaryKeyUsigNullKey() {
        int count = userAttrValuDAO.deleteByPrimaryKey(null);

        assertTrue(count == 0);
    }

    /**
     * Test {@link IUserAttrValuDAO#insert(UserAttrValu)}.
     */
    @Test
    public void insertOfAValidRecord() {
        UserAttrValu userAttrValu = createRecord(1);
        userAttrValu.setKey(createKey(1, 1));

        userAttrValuDAO.insert(userAttrValu);
    }

    /**
     * Test {@link IUserAttrValuDAO#insert(UserAttrValu)}.
     */
    @Test
    public void insertOfRecordWithNullFieldsInserts() {
        UserAttrValu userAttrValu = new UserAttrValu();
        userAttrValu.setKey(createKey(1, 1));

        userAttrValuDAO.insert(userAttrValu);
        UserAttrValu record =  userAttrValuDAO.selectByPrimaryKey(userAttrValu.getKey());
        assertNotNull(record);
    }

    /**
     * Test {@link IUserAttrValuDAO#insert(UserAttrValu)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void insertOfDuplicateRecordFailsWithException() {
        UserAttrValu userAttrValu = createRecord(1);
        userAttrValu.setKey(createKey(1, 1));

        userAttrValuDAO.insert(userAttrValu);
        userAttrValuDAO.insert(userAttrValu);
    }

    /**
     * Test {@link IUserAttrValuDAO#insert(UserAttrValu)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertOfRecordWithNullKeyFailsWithException() {
        UserAttrValu userAttrValu = createRecord(1);
        userAttrValu.setKey(null);

        userAttrValuDAO.insert(userAttrValu);
    }


    /**
     * Test {@link IUserAttrValuDAO#selectByPrimaryKeyDiscretionary(UserAttrValuKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithFullKey() {
        UserAttrValu userAttrValu = null;

        for (int r = 1; r <= 10; ++r) {
            userAttrValu = createRecord(r);
            userAttrValu.setKey(createKey(1, r));

            userAttrValuDAO.insert(userAttrValu);
        }

        List<UserAttrValu> userAttrValuList = userAttrValuDAO.selectByPrimaryKeyDiscretionary(userAttrValu.getKey());

        assertTrue(userAttrValuList.size() == 1);
    }

    /**
     * Test {@link IUserAttrValuDAO#selectByPrimaryKeyDiscretionary(UserAttrValuKey)}.
     */
    @SuppressWarnings("null")
    @Test
    public void selectByPrimaryKeyDiscretionaryWithPartialKey() {
        UserAttrValu userAttrValu = null;

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
                userAttrValu = createRecord(g);
                userAttrValu.setKey(createKey(g, r));

                userAttrValuDAO.insert(userAttrValu);
            }
        }

        UserAttrValuKey userAttrValuKey = createKey(10, 10);
        userAttrValuKey.setUserAttrInstncId(null);

        List<UserAttrValu> userAttrValuList = userAttrValuDAO.selectByPrimaryKeyDiscretionary(userAttrValuKey);

        assertTrue(userAttrValuList.size() == 10);
    }

    /**
     * Test {@link IUserAttrValuDAO#selectByPrimaryKey(UserAttrValuKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatDoesNotExist() {
        UserAttrValu userAttrValu = createRecord(1);
        userAttrValu.setKey(createKey(1, 1));

        userAttrValu = userAttrValuDAO.selectByPrimaryKey(userAttrValu.getKey());

        assertNull(userAttrValu);
    }

    /**
     * Test {@link IUserAttrValuDAO#selectByPrimaryKey(UserAttrValuKey)}.
     */
    @Test
    public void selectByPrimaryKeyOfRecordThatExists() {
        UserAttrValu userAttrValuIn = createRecord(1);
        userAttrValuIn.setKey(createKey(1, 1));

        userAttrValuDAO.insert(userAttrValuIn);

        UserAttrValu userAttrValuOut = userAttrValuDAO.selectByPrimaryKey(userAttrValuIn.getKey());

        assertEquals(userAttrValuIn, userAttrValuOut);
    }

    /**
     * Test {@link IUserAttrValuDAO#selectByPrimaryKey(UserAttrValuKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKey() {
        UserAttrValu userAttrValu = userAttrValuDAO.selectByPrimaryKey(null);

        assertNull(userAttrValu);
    }

    /**
     * Test {@link IUserAttrValuDAO#selectByPrimaryKey(UserAttrValuKey)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectByPrimaryKeyUsingNullKeyFields() {
        UserAttrValuKey userAttrValuKey = new UserAttrValuKey();
        UserAttrValu userAttrValu = userAttrValuDAO.selectByPrimaryKey(userAttrValuKey);

        assertNull(userAttrValu);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithNullValuesThrowsException() {
        
        List<UserAttrValu> records = userAttrValuDAO.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(null, null, null, null, null, null, null, null, null, null, null, null);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void thatFindByRangeByPrimaryKeyWithRowMinNullAndRowMaxSetThrowsException() {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
                
        List<UserAttrValu> records = userAttrValuDAO.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), String.valueOf(1), null, Integer.valueOf("1"));

    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithRowLimitsWorksAsExpected() {
        UserAttrValu userAttrValu = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttrValu = createRecord(g);
                userAttrValu.setKey(createKey(g, r));
    
                userAttrValuDAO.insert(userAttrValu);
            }
        }
        
        List<UserAttrValu> records = userAttrValuDAO.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), null, null, null, null, null, null, null, null, null, Integer.valueOf(0), Integer.valueOf(20));
        assertTrue(records.size() == 20);
    }
    
    @Test
    public void thatFindByRangeByPrimaryKeyWithOutRowLimitsWorksAsExpected() {
        UserAttrValu userAttrValu = null;
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        for (int g = 1; g <= 10; ++g) {
            for (int r = 1; r <= 10; ++r) {
        
                userAttrValu = createRecord(g);
                userAttrValu.setKey(createKey(g, r));
    
                userAttrValuDAO.insert(userAttrValu);
            }
        }
        
        List<UserAttrValu> records = userAttrValuDAO.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String.valueOf(1), null, null, null, null, null, null, null, null, null, null, null);
        assertTrue(records.size() == 100);
    }

    /**
     * Test {@link IUserAttrValuDAO#updateByPrimaryKeySelective(UserAttrValu)}.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void updateByPrimaryKeySelectiveOfRecordThatDoesNotExist() {
        UserAttrValu userAttrValu = createRecord(1);
        userAttrValu.setKey(createKey(1, 1));

        userAttrValuDAO.updateByPrimaryKeySelective(userAttrValu);
    }

    /**
     * Test {@link IUserAttrValuDAO#updateByPrimaryKeySelective(UserAttrValu)}.
     */
    @Test
    public void updateByPrimaryKeySelectiveOfRecordThatExists() {
        UserAttrValu userAttrValu = createRecord(1);
        userAttrValu.setKey(createKey(1, 1));

        UserAttrValuKey userAttrValuKey = userAttrValuDAO.insert(userAttrValu);

        UserAttrValu userAttrValuIn = createRecord(2);
        userAttrValuIn.setKey(userAttrValuKey);
        userAttrValuDAO.updateByPrimaryKeySelective(userAttrValuIn);

        UserAttrValu userAttrValuOut = userAttrValuDAO.selectByPrimaryKey(userAttrValuIn.getKey());

        assertEquals(userAttrValu.getCrtUserId(), userAttrValuOut.getCrtUserId());
        assertEquals(userAttrValu.getCrtTs(), userAttrValuOut.getCrtTs());
        assertEquals(userAttrValuIn.getLastMaintUserId(), userAttrValuOut.getLastMaintUserId());
        assertEquals(userAttrValuIn.getLastMaintTs(), userAttrValuOut.getLastMaintTs());
        assertEquals(userAttrValuIn.getUserAttrValuTx(), userAttrValuOut.getUserAttrValuTx());
    }

    /**
     * Test {@link IUserAttrValuDAO#updateByPrimaryKeySelective(UserAttrValu)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKey() {
        userAttrValuDAO.updateByPrimaryKeySelective(null);
    }

    /**
     * Test {@link IUserAttrValuDAO#updateByPrimaryKeySelective(UserAttrValu)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateByPrimaryKeySelectiveUsingNullKeyField() {
        UserAttrValu userAttrValu = createRecord(1);

        userAttrValuDAO.updateByPrimaryKeySelective(userAttrValu);
    }

        
    /**
     * Setup a mock DAO instance used for testing.
     */
    @Before
    public void setUp() {
        ISecurityDAOFactory securityDAOFactory = new SecurityDAOFactoryImpl(context);

        userAttrValuDAO = securityDAOFactory.getUserAttrValuDAO();
    }

    /**
     * Helper method for creating a record.
     * @param r the record index
     * @return record  a DB record with all columns set
     */
    public static UserAttrValu createRecord(int r) {
        Calendar calendar = CALENDAR_NOW;
        calendar.set(Calendar.DAY_OF_YEAR, r);

        UserAttrValu userAttrValu = new UserAttrValu();
        userAttrValu.setCrtUserId(String.valueOf(r));
        userAttrValu.setCrtTs(calendar.getTime());
        userAttrValu.setLastMaintUserId(String.valueOf(r));
        userAttrValu.setLastMaintTs(calendar.getTime());
        userAttrValu.setUserAttrValuTx(String.valueOf(r));

        return userAttrValu;
    }

    /**
     * Helper method for creating a key.
     * @param g the group index
     * @param r the record index
     * @return key  a record key with all columns set
     */
    public static UserAttrValuKey createKey(int g, int r) {
        UserAttrValuKey userAttrValuKey = new UserAttrValuKey();
        userAttrValuKey.setUserId(String.valueOf(g));
        userAttrValuKey.setUserIdTypeCd(String.valueOf(g));
        userAttrValuKey.setCmpndUserAttrId(String.valueOf(g));
        userAttrValuKey.setSmplUserAttrId(String.valueOf(g));
        userAttrValuKey.setUserAttrInstncId(String.valueOf(g * 10 + r));

        return userAttrValuKey;
    }
}