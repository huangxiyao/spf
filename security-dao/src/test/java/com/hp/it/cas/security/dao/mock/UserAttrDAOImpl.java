package com.hp.it.cas.security.dao.mock;

import com.hp.it.cas.xa.validate.Verifier;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;

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
 * A mock action for IUserAttrDAO interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
public class UserAttrDAOImpl implements Action {
    private static final Logger logger = LoggerFactory.getLogger(UserAttrDAOImpl.class);

    private enum ActionType {
        insert,
        selectByPrimaryKey,
        findRangeByUserAttrId,
        selectByAk1UserAttr,
        findRangeByUserAttrNm,
        selectByIe1UserAttr,
        findRangeByUserAttrTypeCd,
        updateByPrimaryKeySelective,
        deleteByPrimaryKey
    }

    private final ActionType actionType;
    private final Map<UserAttrKey, UserAttr> cache;

    /**
     * Creates a new DAO JMock Action.
     *
     * @param  actionType  the action type
     * @param  cache       the cache that is used to store records
     */
    private UserAttrDAOImpl(ActionType actionType, Map<UserAttrKey, UserAttr> cache) {
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
    public static IUserAttrDAO mockDAO(Mockery context) {
        final Map<UserAttrKey, UserAttr> cache = new HashMap<UserAttrKey, UserAttr>();
        final IUserAttrDAO userAttrDAO = context.mock(IUserAttrDAO.class);

        context.checking(new Expectations() {
                {
                    allowing(userAttrDAO).insert(with(aNonNull(UserAttr.class)));
                    will(insert(cache));

                    allowing(userAttrDAO).selectByPrimaryKey(with(any(UserAttrKey.class)));
                    will(selectByPrimaryKey(cache));

                    allowing(userAttrDAO).findRangeByUserAttrId(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByUserAttrId(cache));
                    
                    allowing(userAttrDAO).selectByAk1UserAttr(
                        with(any(String.class)));
                    will(selectByAk1UserAttr(cache));
                    
                    allowing(userAttrDAO).findRangeByUserAttrNm(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByUserAttrNm(cache));

                    allowing(userAttrDAO).selectByIe1UserAttr(
                        with(any(String.class)));
                    will(selectByIe1UserAttr(cache));
                    
                    allowing(userAttrDAO).findRangeByUserAttrTypeCd(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByUserAttrTypeCd(cache));

                    allowing(userAttrDAO).updateByPrimaryKeySelective(with(any(UserAttr.class)));
                    will(updateByPrimaryKeySelective(cache));

                    allowing(userAttrDAO).deleteByPrimaryKey(with(any(UserAttrKey.class)));
                    will(deleteByPrimaryKey(cache));
                }
            });

        return userAttrDAO;
    }

    /**
     * Creates an action for an insert.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action insert(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.insert, cache);
    }

    /**
     * Creates an action for a select by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKey(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.selectByPrimaryKey, cache);
    }


    /**
     * Creates an action for a find range by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByUserAttrId(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.findRangeByUserAttrId, cache);
    }
    
    /**
     * Creates an action for a select by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByAk1UserAttr(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.selectByAk1UserAttr, cache);
    }

    /**
     * Creates an action for a find range by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByUserAttrNm(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.findRangeByUserAttrNm, cache);
    }
    
    /**
     * Creates an action for a select by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByIe1UserAttr(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.selectByIe1UserAttr, cache);
    }

    /**
     * Creates an action for a find range by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByUserAttrTypeCd(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.findRangeByUserAttrTypeCd, cache);
    }
    
    /**
     * Creates an action for an update by primary key selective.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action updateByPrimaryKeySelective(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.updateByPrimaryKeySelective, cache);
    }


    /**
     * Creates an action for an delete by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action deleteByPrimaryKey(Map<UserAttrKey, UserAttr> cache) {
        return new UserAttrDAOImpl(ActionType.deleteByPrimaryKey, cache);
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

            case findRangeByUserAttrId: {
                result = invokeFindRangeByUserAttrId(invocation);

                break;
            }
            
            case selectByAk1UserAttr: {
                result = invokeSelectByAk1UserAttr(invocation);

                break;
            }

            case findRangeByUserAttrNm: {
                result = invokeFindRangeByUserAttrNm(invocation);

                break;
            }
            
            
            case selectByIe1UserAttr: {
                result = invokeSelectByIe1UserAttr(invocation);

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
        UserAttr record = (UserAttr) invocation.getParameter(0);

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
        if(record.getUserAttrTypeCd() == null) {
            record.setUserAttrTypeCd(" ");
        }
        if(record.getUserAttrNm() == null) {
            record.setUserAttrNm(" ");
        }
        if(record.getUserAttrDn() == null) {
            record.setUserAttrDn(" ");
        }
        if(record.getUserAttrDefnTx() == null) {
            record.setUserAttrDefnTx(" ");
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
        UserAttrKey key = (UserAttrKey) invocation.getParameter(0);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The UserAttr key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getUserAttrId(), "The userAttrId cannot be null.")
                .throwIfError("Unable to select.");
                
        UserAttr record = cache.get(key);
        
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
    private Object invokeFindRangeByUserAttrId(Invocation invocation) {
        List<UserAttr> records = new ArrayList<UserAttr>();
        
        String userAttrIdMin = (String) invocation.getParameter(0);
        String userAttrIdMax = (String) invocation.getParameter(1);
        Integer rownumMin = (Integer) invocation.getParameter(2);
        Integer rownumMax = (Integer) invocation.getParameter(3);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrIdMin, "The osspVndrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query");
        
        for (UserAttr record : cache.values()) {
            UserAttrKey cacheKey = record.getKey();
            boolean match = (userAttrIdMin.compareTo(cacheKey.getUserAttrId()) <= 0);
            match &= ((userAttrIdMax == null) || (userAttrIdMax.compareTo(cacheKey.getUserAttrId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, record {}",
                    new Object[] {
                        userAttrIdMin, 
                        userAttrIdMax,
                        record
                    });
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<UserAttr>() {
            public int compare(UserAttr o1, UserAttr o2) {
                UserAttrKey key1 = o1.getKey();
                UserAttrKey key2 = o2.getKey();

                int comparison = key1.getUserAttrId().compareTo(key2.getUserAttrId());
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<UserAttr> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }
    
    /**
     * Performs a select for a record in the cache matching the select unique key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected record
     */
    private Object invokeSelectByAk1UserAttr(Invocation invocation) {
        UserAttr record = null;
        Object key0 = invocation.getParameter(0);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The userAttrNm cannot be null.")
                .throwIfError("Unable to select.");
                
        for (UserAttr recordC : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(recordC.getUserAttrNm());

            if (match) {
                logger.trace("key {}, record {}", key0, record);
                record = recordC;

                break;
            }
        }

        return record;
    }
    /**
     * Performs a select for records in the cache matching a range of records bounded by the invocation parameters.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeFindRangeByUserAttrNm(Invocation invocation) {
        List<UserAttr> records = new ArrayList<UserAttr>();
        
        String userAttrNmMin = (String) invocation.getParameter(0);
        String userAttrNmMax = (String) invocation.getParameter(1);
        Integer rownumMin = (Integer) invocation.getParameter(2);
        Integer rownumMax = (Integer) invocation.getParameter(3);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrNmMin, "The userAttrNm min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        for (UserAttr recordC : cache.values()) {
            boolean match = (userAttrNmMin.compareTo(recordC.getUserAttrNm()) <= 0);
            match &= ((userAttrNmMax == null) || (userAttrNmMax.compareTo(recordC.getUserAttrNm()) >= 0));
            
            if (match) {
                logger.trace("keys {}, record {}",
                    new Object[] {
                        userAttrNmMin, 
                        userAttrNmMax,
                        recordC
                    });
                records.add(recordC);
            }
        }

        Collections.sort(records, new Comparator<UserAttr>() {
            public int compare(UserAttr o1, UserAttr o2) {
                UserAttrKey key1 = o1.getKey();
                UserAttrKey key2 = o2.getKey();

                int comparison = key1.getUserAttrId().compareTo(key2.getUserAttrId());
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<UserAttr> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }

    /**
     * Performs a select for records in the cache matching the select key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeSelectByIe1UserAttr(Invocation invocation) {
        List<UserAttr> records = new ArrayList<UserAttr>();
        Object key0 = invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The userAttrTypeCd cannot be null.").throwIfError("Unable to select.");
        
        for (UserAttr record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getUserAttrTypeCd());

            if (match) {
                logger.trace("key {}, record {}", key0, record);
                records.add(record);
            }
        }

        return records;
        
    }
    /**
     * Performs a select for records in the cache matching a range of records bounded by the invocation parameters.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeFindRangeByUserAttrTypeCd(Invocation invocation) {
        List<UserAttr> records = new ArrayList<UserAttr>();
        
        String userAttrTypeCdMin = (String) invocation.getParameter(0);
        String userAttrTypeCdMax = (String) invocation.getParameter(1);
        Integer rownumMin = (Integer) invocation.getParameter(2);
        Integer rownumMax = (Integer) invocation.getParameter(3);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrTypeCdMin, "The userAttrTypeCd min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        for (UserAttr recordC : cache.values()) {
            boolean match = (userAttrTypeCdMin.compareTo(recordC.getUserAttrTypeCd()) <= 0);
            match &= ((userAttrTypeCdMax == null) || (userAttrTypeCdMax.compareTo(recordC.getUserAttrTypeCd()) >= 0));
            
            if (match) {
                logger.trace("keys {}, record {}",
                    new Object[] {
                        userAttrTypeCdMin, 
                        userAttrTypeCdMax,
                        recordC
                    });
                records.add(recordC);
            }
        }

        Collections.sort(records, new Comparator<UserAttr>() {
            public int compare(UserAttr o1, UserAttr o2) {
                UserAttrKey key1 = o1.getKey();
                UserAttrKey key2 = o2.getKey();

                int comparison = key1.getUserAttrId().compareTo(key2.getUserAttrId());
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<UserAttr> recordsSlice = records.subList((start - 1), end);
        
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
        UserAttr record = (UserAttr) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to update.");
        verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to update.");
        verifier.isNotNull(record.getKey().getUserAttrId(), "The key column USER_ATTR_ID cannot be null.")
                .throwIfError("Unable to update.");

        boolean recordExists = (record == null) ? false : cache.containsKey(record.getKey());

        if (! recordExists) {
            throw new DataIntegrityViolationException(
                String.format("Record does not exist for key %s",
                    (record == null) ? null : record.getKey()));
        }

        logger.trace("record {}", record);

        UserAttr recordInCache = cache.get(record.getKey());

        recordInCache.setLastMaintUserId(record.getLastMaintUserId() == null ? recordInCache.getLastMaintUserId() : record.getLastMaintUserId());
        recordInCache.setLastMaintTs(record.getLastMaintTs() == null ? recordInCache.getLastMaintTs() : record.getLastMaintTs());
        recordInCache.setUserAttrTypeCd(record.getUserAttrTypeCd() == null ? recordInCache.getUserAttrTypeCd() : record.getUserAttrTypeCd());
        recordInCache.setUserAttrNm(record.getUserAttrNm() == null ? recordInCache.getUserAttrNm() : record.getUserAttrNm());
        recordInCache.setUserAttrDn(record.getUserAttrDn() == null ? recordInCache.getUserAttrDn() : record.getUserAttrDn());
        recordInCache.setUserAttrDefnTx(record.getUserAttrDefnTx() == null ? recordInCache.getUserAttrDefnTx() : record.getUserAttrDefnTx());

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
        UserAttrKey key = (UserAttrKey) invocation.getParameter(0);
        UserAttr record = cache.remove(key);

        logger.trace("key {}, record {}", key, record);

        return (record == null) ? 0 : 1;
    }
}