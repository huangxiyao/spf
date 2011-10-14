package com.hp.it.cas.security.dao.mock;

import com.hp.it.cas.xa.validate.Verifier;
import com.hp.it.cas.security.dao.IUserAttrValuDAO;
import com.hp.it.cas.security.dao.UserAttrValu;
import com.hp.it.cas.security.dao.UserAttrValuKey;

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
 * A mock action for IUserAttrValuDAO interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
public class UserAttrValuDAOImpl implements Action {
    private static final Logger logger = LoggerFactory.getLogger(UserAttrValuDAOImpl.class);

    private enum ActionType {
        insert,
        selectByPrimaryKey,
        selectByPrimaryKeyDiscretionary,
        findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId,
        selectByIe1UserAttrValu,
        findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx,
        updateByPrimaryKeySelective,
        deleteByPrimaryKey
    }

    private final ActionType actionType;
    private final Map<UserAttrValuKey, UserAttrValu> cache;

    /**
     * Creates a new DAO JMock Action.
     *
     * @param  actionType  the action type
     * @param  cache       the cache that is used to store records
     */
    private UserAttrValuDAOImpl(ActionType actionType, Map<UserAttrValuKey, UserAttrValu> cache) {
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
    public static IUserAttrValuDAO mockDAO(Mockery context) {
        final Map<UserAttrValuKey, UserAttrValu> cache = new HashMap<UserAttrValuKey, UserAttrValu>();
        final IUserAttrValuDAO userAttrValuDAO = context.mock(IUserAttrValuDAO.class);

        context.checking(new Expectations() {
                {
                    allowing(userAttrValuDAO).insert(with(aNonNull(UserAttrValu.class)));
                    will(insert(cache));

                    allowing(userAttrValuDAO).selectByPrimaryKey(with(any(UserAttrValuKey.class)));
                    will(selectByPrimaryKey(cache));

                    allowing(userAttrValuDAO).selectByPrimaryKeyDiscretionary(with(any(UserAttrValuKey.class)));
                    will(selectByPrimaryKeyDiscretionary(cache));

                    allowing(userAttrValuDAO).findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(cache));
                    
                    allowing(userAttrValuDAO).selectByIe1UserAttrValu(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)));
                    will(selectByIe1UserAttrValu(cache));
                    
                    allowing(userAttrValuDAO).findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(cache));

                    allowing(userAttrValuDAO).updateByPrimaryKeySelective(with(any(UserAttrValu.class)));
                    will(updateByPrimaryKeySelective(cache));

                    allowing(userAttrValuDAO).deleteByPrimaryKey(with(any(UserAttrValuKey.class)));
                    will(deleteByPrimaryKey(cache));
                }
            });

        return userAttrValuDAO;
    }

    /**
     * Creates an action for an insert.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action insert(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.insert, cache);
    }

    /**
     * Creates an action for a select by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKey(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.selectByPrimaryKey, cache);
    }

    /**
     * Creates an action for a select by primary key discretionary.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKeyDiscretionary(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.selectByPrimaryKeyDiscretionary, cache);
    }


    /**
     * Creates an action for a find range by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId, cache);
    }
    
    /**
     * Creates an action for a select by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByIe1UserAttrValu(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.selectByIe1UserAttrValu, cache);
    }

    /**
     * Creates an action for a find range by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx, cache);
    }
    
    /**
     * Creates an action for an update by primary key selective.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action updateByPrimaryKeySelective(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.updateByPrimaryKeySelective, cache);
    }


    /**
     * Creates an action for an delete by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action deleteByPrimaryKey(Map<UserAttrValuKey, UserAttrValu> cache) {
        return new UserAttrValuDAOImpl(ActionType.deleteByPrimaryKey, cache);
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

            case selectByPrimaryKeyDiscretionary: {
                result = invokeSelectByPrimaryKeyDiscretionary(invocation);

                break;
            }

            case findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId: {
                result = invokeFindRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(invocation);

                break;
            }
            
            case selectByIe1UserAttrValu: {
                result = invokeSelectByIe1UserAttrValu(invocation);

                break;
            }

            case findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx: {
                result = invokeFindRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(invocation);

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
        UserAttrValu record = (UserAttrValu) invocation.getParameter(0);

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
        if(record.getUserAttrValuTx() == null) {
            record.setUserAttrValuTx(" ");
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
        UserAttrValuKey key = (UserAttrValuKey) invocation.getParameter(0);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The UserAttrValu key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getUserId(), "The userId cannot be null.")
                .isNotNull(key.getUserIdTypeCd(), "The userIdTypeCd cannot be null.")
                .isNotNull(key.getCmpndUserAttrId(), "The cmpndUserAttrId cannot be null.")
                .isNotNull(key.getSmplUserAttrId(), "The smplUserAttrId cannot be null.")
                .isNotNull(key.getUserAttrInstncId(), "The userAttrInstncId cannot be null.")
                .throwIfError("Unable to select.");
                
        UserAttrValu record = cache.get(key);
        
        logger.trace("key {}, record {}", key, record);

        return record;
    }

    /**
     * Performs a select for records in the cache matching the select key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected record
     */
    private Object invokeSelectByPrimaryKeyDiscretionary(Invocation invocation) {
        List<UserAttrValu> records = new ArrayList<UserAttrValu>();
        UserAttrValuKey key = (UserAttrValuKey) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The UserAttrValu key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getUserId(), "The userId cannot be null.").throwIfError("Unable to select.");
        
        for (UserAttrValu record : cache.values()) {
            UserAttrValuKey cacheKey = record.getKey();
            boolean match = key.getUserId()
                               .equals(cacheKey.getUserId());
            match &= (key.getUserIdTypeCd() == null)
                || key.getUserIdTypeCd()
                      .equals(cacheKey.getUserIdTypeCd());
            match &= (key.getCmpndUserAttrId() == null)
                || key.getCmpndUserAttrId()
                      .equals(cacheKey.getCmpndUserAttrId());
            match &= (key.getSmplUserAttrId() == null)
                || key.getSmplUserAttrId()
                      .equals(cacheKey.getSmplUserAttrId());
            match &= (key.getUserAttrInstncId() == null)
                || key.getUserAttrInstncId()
                      .equals(cacheKey.getUserAttrInstncId());

            if (match) {
                logger.trace("key {}, record {}", key, record);
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<UserAttrValu>() {
            public int compare(UserAttrValu o1, UserAttrValu o2) {
                UserAttrValuKey key1 = o1.getKey();
                UserAttrValuKey key2 = o2.getKey();

                int comparison = key1.getUserId().compareTo(key2.getUserId());
                if (comparison == 0) {
                    comparison = key1.getUserIdTypeCd().compareTo(key2.getUserIdTypeCd());
                }
                if (comparison == 0) {
                    comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getUserAttrInstncId().compareTo(key2.getUserAttrInstncId());
                }
                return comparison;
            }
        });

        return records;
    }

    /**
     * Performs a select for records in the cache matching a range of records bounded by the invocation parameters.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeFindRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(Invocation invocation) {
        List<UserAttrValu> records = new ArrayList<UserAttrValu>();
        
        String userIdMin = (String) invocation.getParameter(0);
        String userIdMax = (String) invocation.getParameter(1);
        String userIdTypeCdMin = (String) invocation.getParameter(2);
        String userIdTypeCdMax = (String) invocation.getParameter(3);
        String cmpndUserAttrIdMin = (String) invocation.getParameter(4);
        String cmpndUserAttrIdMax = (String) invocation.getParameter(5);
        String smplUserAttrIdMin = (String) invocation.getParameter(6);
        String smplUserAttrIdMax = (String) invocation.getParameter(7);
        String userAttrInstncIdMin = (String) invocation.getParameter(8);
        String userAttrInstncIdMax = (String) invocation.getParameter(9);
        Integer rownumMin = (Integer) invocation.getParameter(10);
        Integer rownumMax = (Integer) invocation.getParameter(11);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userIdMin, "The osspVndrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query");
        
        for (UserAttrValu record : cache.values()) {
            UserAttrValuKey cacheKey = record.getKey();
            boolean match = (userIdMin.compareTo(cacheKey.getUserId()) <= 0);
            match &= ((userIdMax == null) || (userIdMax.compareTo(cacheKey.getUserId()) >= 0));
            match &= ((userIdTypeCdMin == null) || (userIdTypeCdMin.compareTo(cacheKey.getUserIdTypeCd()) <= 0));
            match &= ((userIdTypeCdMax == null) || (userIdTypeCdMax.compareTo(cacheKey.getUserIdTypeCd()) >= 0));
            match &= ((cmpndUserAttrIdMin == null) || (cmpndUserAttrIdMin.compareTo(cacheKey.getCmpndUserAttrId()) <= 0));
            match &= ((cmpndUserAttrIdMax == null) || (cmpndUserAttrIdMax.compareTo(cacheKey.getCmpndUserAttrId()) >= 0));
            match &= ((smplUserAttrIdMin == null) || (smplUserAttrIdMin.compareTo(cacheKey.getSmplUserAttrId()) <= 0));
            match &= ((smplUserAttrIdMax == null) || (smplUserAttrIdMax.compareTo(cacheKey.getSmplUserAttrId()) >= 0));
            match &= ((userAttrInstncIdMin == null) || (userAttrInstncIdMin.compareTo(cacheKey.getUserAttrInstncId()) <= 0));
            match &= ((userAttrInstncIdMax == null) || (userAttrInstncIdMax.compareTo(cacheKey.getUserAttrInstncId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, {}, {}, {}, {}, record {}",
                    new Object[] {
                        userIdMin, 
                        userIdMax,
                        userIdTypeCdMin, 
                        userIdTypeCdMax,
                        cmpndUserAttrIdMin, 
                        cmpndUserAttrIdMax,
                        smplUserAttrIdMin, 
                        smplUserAttrIdMax,
                        userAttrInstncIdMin, 
                        userAttrInstncIdMax,
                        record
                    });
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<UserAttrValu>() {
            public int compare(UserAttrValu o1, UserAttrValu o2) {
                UserAttrValuKey key1 = o1.getKey();
                UserAttrValuKey key2 = o2.getKey();

                int comparison = key1.getUserId().compareTo(key2.getUserId());
                if (comparison == 0) {
                    comparison = key1.getUserIdTypeCd().compareTo(key2.getUserIdTypeCd());
                }
                if (comparison == 0) {
                    comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getUserAttrInstncId().compareTo(key2.getUserAttrInstncId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<UserAttrValu> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }
    
    /**
     * Performs a select for records in the cache matching the select key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeSelectByIe1UserAttrValu(Invocation invocation) {
        List<UserAttrValu> records = new ArrayList<UserAttrValu>();
        Object key0 = invocation.getParameter(0);
        Object key1 = invocation.getParameter(1);
        Object key2 = invocation.getParameter(2);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The cmpndUserAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (UserAttrValu record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getKey().getCmpndUserAttrId());
            match &= (key1 == null) || key1.equals(record.getKey().getSmplUserAttrId());
            match &= (key2 == null) || key2.equals(record.getUserAttrValuTx());

            if (match) {
                logger.trace("keys {}, {}, {}, record {}",
                    new Object[] {
                        key0,
                        key1,
                        key2,
                        record
                    });
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
    private Object invokeFindRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(Invocation invocation) {
        List<UserAttrValu> records = new ArrayList<UserAttrValu>();
        
        String cmpndUserAttrIdMin = (String) invocation.getParameter(0);
        String cmpndUserAttrIdMax = (String) invocation.getParameter(1);
        String smplUserAttrIdMin = (String) invocation.getParameter(2);
        String smplUserAttrIdMax = (String) invocation.getParameter(3);
        String userAttrValuTxMin = (String) invocation.getParameter(4);
        String userAttrValuTxMax = (String) invocation.getParameter(5);
        Integer rownumMin = (Integer) invocation.getParameter(6);
        Integer rownumMax = (Integer) invocation.getParameter(7);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(cmpndUserAttrIdMin, "The cmpndUserAttrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        for (UserAttrValu recordC : cache.values()) {
            boolean match = (cmpndUserAttrIdMin.compareTo(recordC.getKey().getCmpndUserAttrId()) <= 0);
            match &= ((cmpndUserAttrIdMax == null) || (cmpndUserAttrIdMax.compareTo(recordC.getKey().getCmpndUserAttrId()) >= 0));
            match &= ((smplUserAttrIdMin == null) || (smplUserAttrIdMin.compareTo(recordC.getKey().getSmplUserAttrId()) <= 0));
            match &= ((smplUserAttrIdMax == null) || (smplUserAttrIdMax.compareTo(recordC.getKey().getSmplUserAttrId()) >= 0));
            match &= ((userAttrValuTxMin == null) || (userAttrValuTxMin.compareTo(recordC.getUserAttrValuTx()) <= 0));
            match &= ((userAttrValuTxMax == null) || (userAttrValuTxMax.compareTo(recordC.getUserAttrValuTx()) >= 0));
            
            if (match) {
                logger.trace("keys {}, {}, {}, record {}",
                    new Object[] {
                        cmpndUserAttrIdMin, 
                        cmpndUserAttrIdMax,
                        smplUserAttrIdMin, 
                        smplUserAttrIdMax,
                        userAttrValuTxMin, 
                        userAttrValuTxMax,
                        recordC
                    });
                records.add(recordC);
            }
        }

        Collections.sort(records, new Comparator<UserAttrValu>() {
            public int compare(UserAttrValu o1, UserAttrValu o2) {
                UserAttrValuKey key1 = o1.getKey();
                UserAttrValuKey key2 = o2.getKey();

                int comparison = key1.getUserId().compareTo(key2.getUserId());
                if (comparison == 0) {
                    comparison = key1.getUserIdTypeCd().compareTo(key2.getUserIdTypeCd());
                }
                if (comparison == 0) {
                    comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getUserAttrInstncId().compareTo(key2.getUserAttrInstncId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<UserAttrValu> recordsSlice = records.subList((start - 1), end);
        
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
        UserAttrValu record = (UserAttrValu) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to update.");
        verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to update.");
        verifier.isNotNull(record.getKey().getUserId(), "The key column USER_ID cannot be null.")
                .isNotNull(record.getKey().getUserIdTypeCd(), "The key column USER_ID_TYPE_CD cannot be null.")
                .isNotNull(record.getKey().getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null.")
                .isNotNull(record.getKey().getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null.")
                .isNotNull(record.getKey().getUserAttrInstncId(), "The key column USER_ATTR_INSTNC_ID cannot be null.")
                .throwIfError("Unable to update.");

        boolean recordExists = (record == null) ? false : cache.containsKey(record.getKey());

        if (! recordExists) {
            throw new DataIntegrityViolationException(
                String.format("Record does not exist for key %s",
                    (record == null) ? null : record.getKey()));
        }

        logger.trace("record {}", record);

        UserAttrValu recordInCache = cache.get(record.getKey());

        recordInCache.setLastMaintUserId(record.getLastMaintUserId() == null ? recordInCache.getLastMaintUserId() : record.getLastMaintUserId());
        recordInCache.setLastMaintTs(record.getLastMaintTs() == null ? recordInCache.getLastMaintTs() : record.getLastMaintTs());
        recordInCache.setUserAttrValuTx(record.getUserAttrValuTx() == null ? recordInCache.getUserAttrValuTx() : record.getUserAttrValuTx());

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
        UserAttrValuKey key = (UserAttrValuKey) invocation.getParameter(0);
        UserAttrValu record = cache.remove(key);

        logger.trace("key {}, record {}", key, record);

        return (record == null) ? 0 : 1;
    }
}