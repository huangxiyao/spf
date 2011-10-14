package com.hp.it.cas.security.dao.mock;

import com.hp.it.cas.xa.validate.Verifier;
import com.hp.it.cas.security.dao.IUserAttrTypeDAO;
import com.hp.it.cas.security.dao.UserAttrType;
import com.hp.it.cas.security.dao.UserAttrTypeKey;

import org.hamcrest.Description;

import org.jmock.Expectations;
import org.jmock.Mockery;

import org.jmock.api.Action;
import org.jmock.api.Invocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * A mock action for IUserAttrTypeDAO interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
public class UserAttrTypeDAOImpl implements Action {
    private static final Logger logger = LoggerFactory.getLogger(UserAttrTypeDAOImpl.class);

    private enum ActionType {
        insert,
        selectByPrimaryKey,
        findRangeByUserAttrTypeCd,
        updateByPrimaryKeySelective,
        deleteByPrimaryKey
    }

    private final ActionType actionType;
    private final Map<UserAttrTypeKey, UserAttrType> cache;

    /**
     * Creates a new DAO JMock Action.
     *
     * @param  actionType  the action type
     * @param  cache       the cache that is used to store records
     */
    private UserAttrTypeDAOImpl(ActionType actionType, Map<UserAttrTypeKey, UserAttrType> cache) {
        this.actionType = actionType;

        this.cache = cache;
    }

    /**
     * Appends to the readable description included in a test failure message.
     *
     * @param  description  the description to be appended to
     */
    public void describeTo(Description description) {
        description.appendText("action type ")
                   .appendValue(actionType)
                   .appendText(" on cache");
    }

    /**
     * Initialize the mock DAO and create the required expectations for insert, select, update and
     * delete as needed.
     *
     * @param   context  the JMock context
     *
     * @return  the mocked DAO
     */
    public static IUserAttrTypeDAO mockDAO(Mockery context) {
        final Map<UserAttrTypeKey, UserAttrType> cache = new HashMap<UserAttrTypeKey, UserAttrType>();
        final IUserAttrTypeDAO userAttrTypeDAO = context.mock(IUserAttrTypeDAO.class);

        context.checking(new Expectations() {
                {
                    allowing(userAttrTypeDAO).insert(with(aNonNull(UserAttrType.class)));
                    will(insert(cache));

                    allowing(userAttrTypeDAO).selectByPrimaryKey(with(any(UserAttrTypeKey.class)));
                    will(selectByPrimaryKey(cache));

                    allowing(userAttrTypeDAO).findRangeByUserAttrTypeCd(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByUserAttrTypeCd(cache));
                    
                    allowing(userAttrTypeDAO).updateByPrimaryKeySelective(with(any(UserAttrType.class)));
                    will(updateByPrimaryKeySelective(cache));

                    allowing(userAttrTypeDAO).deleteByPrimaryKey(with(any(UserAttrTypeKey.class)));
                    will(deleteByPrimaryKey(cache));
                }
            });

        return userAttrTypeDAO;
    }

    /**
     * Creates an action for an insert.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action insert(Map<UserAttrTypeKey, UserAttrType> cache) {
        return new UserAttrTypeDAOImpl(ActionType.insert, cache);
    }

    /**
     * Creates an action for a select by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKey(Map<UserAttrTypeKey, UserAttrType> cache) {
        return new UserAttrTypeDAOImpl(ActionType.selectByPrimaryKey, cache);
    }


    /**
     * Creates an action for a find range by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByUserAttrTypeCd(Map<UserAttrTypeKey, UserAttrType> cache) {
        return new UserAttrTypeDAOImpl(ActionType.findRangeByUserAttrTypeCd, cache);
    }
    
    /**
     * Creates an action for an update by primary key selective.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action updateByPrimaryKeySelective(Map<UserAttrTypeKey, UserAttrType> cache) {
        return new UserAttrTypeDAOImpl(ActionType.updateByPrimaryKeySelective, cache);
    }


    /**
     * Creates an action for an delete by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action deleteByPrimaryKey(Map<UserAttrTypeKey, UserAttrType> cache) {
        return new UserAttrTypeDAOImpl(ActionType.deleteByPrimaryKey, cache);
    }

    /**
     * Perform the action required by the method that has been invoked.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  for a select action return the records that match the select key, for insert, update
     *          and delete actions return null
     *
     * @throws  Throwable  should not happen
     */
    public Object invoke(Invocation invocation) throws Throwable {
        Object result = null;

        switch (actionType) {
            case insert: {
                result = invokeInsert(invocation);

                break;
            }

            case selectByPrimaryKey: {
                result = invokeSelectByPrimaryKey(invocation);

                break;
            }

            case findRangeByUserAttrTypeCd: {
                result = invokeFindRangeByUserAttrTypeCd(invocation);

                break;
            }
            
            case updateByPrimaryKeySelective: {
                result = invokeUpdateByPrimaryKeySelective(invocation);

                break;
            }

            case deleteByPrimaryKey: {
                result = invokeDeleteByPrimaryKey(invocation);

                break;
            }

            default: {
                break;
            }
        }

        return result;
    }

    /**
     * Inserts a record into the cache.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the record key
     *
     * @throws  DataIntegrityViolationException  if the record already exists
     */
    private Object invokeInsert(Invocation invocation) {
        UserAttrType record = (UserAttrType) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
        verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");

        boolean recordExists = cache.containsKey(record.getKey());

        if (recordExists) {
            throw new DataIntegrityViolationException(String.format(
                    "Record already exists for key %s", record.getKey()));
        }

        logger.trace("record {}", record);
        if(record.getCrtUserId() == null) {
            record.setCrtUserId(" ");
        }
        if(record.getCrtTs() == null) {
            record.setCrtTs(new Date());
        }
        if(record.getLastMaintUserId() == null) {
            record.setLastMaintUserId(" ");
        }
        if(record.getLastMaintTs() == null) {
            record.setLastMaintTs(new Date());
        }
        if(record.getUserAttrTypeNm() == null) {
            record.setUserAttrTypeNm(" ");
        }
        if(record.getUserAttrTypeDn() == null) {
            record.setUserAttrTypeDn(" ");
        }
        cache.put(record.getKey(), record);

        return record.getKey();
    }

    /**
     * Performs a select for records in the cache matching the select key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeSelectByPrimaryKey(Invocation invocation) {
        UserAttrTypeKey key = (UserAttrTypeKey) invocation.getParameter(0);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The UserAttrType key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getUserAttrTypeCd(), "The userAttrTypeCd cannot be null.")
                .throwIfError("Unable to select.");
                
        UserAttrType record = cache.get(key);
        
        logger.trace("key {}, record {}", key, record);

        return record;
    }

    /**
     * Performs a select for records in the cache matching a range of records bounded by the invocation parameters.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeFindRangeByUserAttrTypeCd(Invocation invocation) {
        List<UserAttrType> records = new ArrayList<UserAttrType>();
        
        String userAttrTypeCdMin = (String) invocation.getParameter(0);
        String userAttrTypeCdMax = (String) invocation.getParameter(1);
        Integer rownumMin = (Integer) invocation.getParameter(2);
        Integer rownumMax = (Integer) invocation.getParameter(3);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrTypeCdMin, "The osspVndrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query");
        
        for (UserAttrType record : cache.values()) {
            UserAttrTypeKey cacheKey = record.getKey();
            boolean match = (userAttrTypeCdMin.compareTo(cacheKey.getUserAttrTypeCd()) <= 0);
            match &= ((userAttrTypeCdMax == null) || (userAttrTypeCdMax.compareTo(cacheKey.getUserAttrTypeCd()) >= 0));
            
            if (match) {
                logger.trace("keys {}, record {}",
                    new Object[] {
                        userAttrTypeCdMin, 
                        userAttrTypeCdMax,
                        record
                    });
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<UserAttrType>() {
            public int compare(UserAttrType o1, UserAttrType o2) {
                UserAttrTypeKey key1 = o1.getKey();
                UserAttrTypeKey key2 = o2.getKey();

                int comparison = key1.getUserAttrTypeCd().compareTo(key2.getUserAttrTypeCd());
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<UserAttrType> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }
    
    /**
     * Updates a record in the cache.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  number of rows updated
     *
     * @throws  DataIntegrityViolationException  if the record already exists
     */
    @SuppressWarnings("null")
    private Object invokeUpdateByPrimaryKeySelective(Invocation invocation) {
        UserAttrType record = (UserAttrType) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to update.");
        verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to update.");
        verifier.isNotNull(record.getKey().getUserAttrTypeCd(), "The key column USER_ATTR_TYPE_CD cannot be null.")
                .throwIfError("Unable to update.");

        boolean recordExists = (record == null) ? false : cache.containsKey(record.getKey());

        if (! recordExists) {
            throw new DataIntegrityViolationException(
                String.format("Record does not exist for key %s",
                    (record == null) ? null : record.getKey()));
        }

        logger.trace("record {}", record);

        UserAttrType recordInCache = cache.get(record.getKey());

        recordInCache.setLastMaintUserId(record.getLastMaintUserId() == null ? recordInCache.getLastMaintUserId() : record.getLastMaintUserId());
        recordInCache.setLastMaintTs(record.getLastMaintTs() == null ? recordInCache.getLastMaintTs() : record.getLastMaintTs());
        recordInCache.setUserAttrTypeNm(record.getUserAttrTypeNm() == null ? recordInCache.getUserAttrTypeNm() : record.getUserAttrTypeNm());
        recordInCache.setUserAttrTypeDn(record.getUserAttrTypeDn() == null ? recordInCache.getUserAttrTypeDn() : record.getUserAttrTypeDn());

        return 1;
    }

    /**
     * Removes a record from the cache if it exists.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  1 if the record matching the specified key was removed, otherwise 0
     */
    private Object invokeDeleteByPrimaryKey(Invocation invocation) {
        UserAttrTypeKey key = (UserAttrTypeKey) invocation.getParameter(0);
        UserAttrType record = cache.remove(key);

        logger.trace("key {}, record {}", key, record);

        return (record == null) ? 0 : 1;
    }
}