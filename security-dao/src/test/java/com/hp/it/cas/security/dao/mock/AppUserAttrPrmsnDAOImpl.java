package com.hp.it.cas.security.dao.mock;

import com.hp.it.cas.xa.validate.Verifier;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.AppUserAttrPrmsn;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;

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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;

/**
 * A mock action for IAppUserAttrPrmsnDAO interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
public class AppUserAttrPrmsnDAOImpl implements Action {
    private static final Logger logger = LoggerFactory.getLogger(AppUserAttrPrmsnDAOImpl.class);

    private enum ActionType {
        insert,
        selectByPrimaryKey,
        selectByPrimaryKeyDiscretionary,
        findRangeByAppPrtflIdByUserAttrId,
        selectByIe1AppUserAttrPrmsn,
        findRangeByUserAttrId,
        findDistinctByUserAttrId,
        deleteByPrimaryKey
    }

    private final ActionType actionType;
    private final Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache;

    /**
     * Creates a new DAO JMock Action.
     *
     * @param  actionType  the action type
     * @param  cache       the cache that is used to store records
     */
    private AppUserAttrPrmsnDAOImpl(ActionType actionType, Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
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
    public static IAppUserAttrPrmsnDAO mockDAO(Mockery context) {
        final Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache = new HashMap<AppUserAttrPrmsnKey, AppUserAttrPrmsn>();
        final IAppUserAttrPrmsnDAO appUserAttrPrmsnDAO = context.mock(IAppUserAttrPrmsnDAO.class);

        context.checking(new Expectations() {
                {
                    allowing(appUserAttrPrmsnDAO).insert(with(aNonNull(AppUserAttrPrmsn.class)));
                    will(insert(cache));

                    allowing(appUserAttrPrmsnDAO).selectByPrimaryKey(with(any(AppUserAttrPrmsnKey.class)));
                    will(selectByPrimaryKey(cache));

                    allowing(appUserAttrPrmsnDAO).selectByPrimaryKeyDiscretionary(with(any(AppUserAttrPrmsnKey.class)));
                    will(selectByPrimaryKeyDiscretionary(cache));

                    allowing(appUserAttrPrmsnDAO).findRangeByAppPrtflIdByUserAttrId(
                        with(any(BigDecimal.class)),
                        with(any(BigDecimal.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByAppPrtflIdByUserAttrId(cache));
                    
                    allowing(appUserAttrPrmsnDAO).selectByIe1AppUserAttrPrmsn(
                        with(any(String.class)));
                    will(selectByIe1AppUserAttrPrmsn(cache));
                    
                    allowing(appUserAttrPrmsnDAO).findRangeByUserAttrId(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByUserAttrId(cache));

                    allowing(appUserAttrPrmsnDAO).findDistinctByUserAttrId(
                        with(any(String.class)));
                    will(findDistinctByUserAttrId(cache));
                    
                    allowing(appUserAttrPrmsnDAO).deleteByPrimaryKey(with(any(AppUserAttrPrmsnKey.class)));
                    will(deleteByPrimaryKey(cache));
                }
            });

        return appUserAttrPrmsnDAO;
    }

    /**
     * Creates an action for an insert.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action insert(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.insert, cache);
    }

    /**
     * Creates an action for a select by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKey(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.selectByPrimaryKey, cache);
    }

    /**
     * Creates an action for a select by primary key discretionary.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKeyDiscretionary(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.selectByPrimaryKeyDiscretionary, cache);
    }


    /**
     * Creates an action for a find range by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByAppPrtflIdByUserAttrId(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.findRangeByAppPrtflIdByUserAttrId, cache);
    }
    
    /**
     * Creates an action for a select by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByIe1AppUserAttrPrmsn(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.selectByIe1AppUserAttrPrmsn, cache);
    }

    /**
     * Creates an action for a find range by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByUserAttrId(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.findRangeByUserAttrId, cache);
    }
    
    /**
     * Creates an action for find distinct by an index.
     * 
     * @param  cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findDistinctByUserAttrId(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.findDistinctByUserAttrId, cache);
    }
    

    /**
     * Creates an action for an delete by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action deleteByPrimaryKey(Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> cache) {
        return new AppUserAttrPrmsnDAOImpl(ActionType.deleteByPrimaryKey, cache);
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

            case findRangeByAppPrtflIdByUserAttrId: {
                result = invokeFindRangeByAppPrtflIdByUserAttrId(invocation);

                break;
            }
            
            case selectByIe1AppUserAttrPrmsn: {
                result = invokeSelectByIe1AppUserAttrPrmsn(invocation);

                break;
            }

            case findRangeByUserAttrId: {
                result = invokeFindRangeByUserAttrId(invocation);

                break;
            }
            
            case findDistinctByUserAttrId: {
                result = invokeFindDistinctByUserAttrId(invocation);

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
        AppUserAttrPrmsn record = (AppUserAttrPrmsn) invocation.getParameter(0);

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
        AppUserAttrPrmsnKey key = (AppUserAttrPrmsnKey) invocation.getParameter(0);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The AppUserAttrPrmsn key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getAppPrtflId(), "The appPrtflId cannot be null.")
                .isNotNull(key.getUserAttrId(), "The userAttrId cannot be null.")
                .throwIfError("Unable to select.");
                
        AppUserAttrPrmsn record = cache.get(key);
        
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
        List<AppUserAttrPrmsn> records = new ArrayList<AppUserAttrPrmsn>();
        AppUserAttrPrmsnKey key = (AppUserAttrPrmsnKey) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The AppUserAttrPrmsn key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getAppPrtflId(), "The appPrtflId cannot be null.").throwIfError("Unable to select.");
        
        for (AppUserAttrPrmsn record : cache.values()) {
            AppUserAttrPrmsnKey cacheKey = record.getKey();
            boolean match = key.getAppPrtflId()
                               .equals(cacheKey.getAppPrtflId());
            match &= (key.getUserAttrId() == null)
                || key.getUserAttrId()
                      .equals(cacheKey.getUserAttrId());

            if (match) {
                logger.trace("key {}, record {}", key, record);
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<AppUserAttrPrmsn>() {
            public int compare(AppUserAttrPrmsn o1, AppUserAttrPrmsn o2) {
                AppUserAttrPrmsnKey key1 = o1.getKey();
                AppUserAttrPrmsnKey key2 = o2.getKey();

                int comparison = key1.getAppPrtflId().compareTo(key2.getAppPrtflId());
                if (comparison == 0) {
                    comparison = key1.getUserAttrId().compareTo(key2.getUserAttrId());
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
    private Object invokeFindRangeByAppPrtflIdByUserAttrId(Invocation invocation) {
        List<AppUserAttrPrmsn> records = new ArrayList<AppUserAttrPrmsn>();
        
        BigDecimal appPrtflIdMin = (BigDecimal) invocation.getParameter(0);
        BigDecimal appPrtflIdMax = (BigDecimal) invocation.getParameter(1);
        String userAttrIdMin = (String) invocation.getParameter(2);
        String userAttrIdMax = (String) invocation.getParameter(3);
        Integer rownumMin = (Integer) invocation.getParameter(4);
        Integer rownumMax = (Integer) invocation.getParameter(5);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(appPrtflIdMin, "The osspVndrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query");
        
        for (AppUserAttrPrmsn record : cache.values()) {
            AppUserAttrPrmsnKey cacheKey = record.getKey();
            boolean match = (appPrtflIdMin.compareTo(cacheKey.getAppPrtflId()) <= 0);
            match &= ((appPrtflIdMax == null) || (appPrtflIdMax.compareTo(cacheKey.getAppPrtflId()) >= 0));
            match &= ((userAttrIdMin == null) || (userAttrIdMin.compareTo(cacheKey.getUserAttrId()) <= 0));
            match &= ((userAttrIdMax == null) || (userAttrIdMax.compareTo(cacheKey.getUserAttrId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, {}, record {}",
                    new Object[] {
                        appPrtflIdMin, 
                        appPrtflIdMax,
                        userAttrIdMin, 
                        userAttrIdMax,
                        record
                    });
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<AppUserAttrPrmsn>() {
            public int compare(AppUserAttrPrmsn o1, AppUserAttrPrmsn o2) {
                AppUserAttrPrmsnKey key1 = o1.getKey();
                AppUserAttrPrmsnKey key2 = o2.getKey();

                int comparison = key1.getAppPrtflId().compareTo(key2.getAppPrtflId());
                if (comparison == 0) {
                    comparison = key1.getUserAttrId().compareTo(key2.getUserAttrId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<AppUserAttrPrmsn> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }
    
    /**
     * Performs a select for records in the cache matching the select key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeSelectByIe1AppUserAttrPrmsn(Invocation invocation) {
        List<AppUserAttrPrmsn> records = new ArrayList<AppUserAttrPrmsn>();
        Object key0 = invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The userAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (AppUserAttrPrmsn record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getKey().getUserAttrId());

            if (match) {
                logger.trace("key {}, record {}", key0, record);
                records.add(record);
            }
        }

        return records;
        
    }
    /**
     * Performs a distinct select for records in the cache matching one or more of the invocation parameters.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    @SuppressWarnings("unchecked")
    private Object invokeFindDistinctByUserAttrId(Invocation invocation) {
        List<AppUserAttrPrmsn> records = new ArrayList<AppUserAttrPrmsn>();
        List<Hashtable> distinctRecords = new ArrayList<Hashtable>();
        Object key0 = invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The userAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (AppUserAttrPrmsn record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getKey().getUserAttrId());
            
            if (match) {
                logger.trace("key {}, record {}", key0, record);
                // Check this record against the records found previously
                boolean thisMatch = false;
                for(AppUserAttrPrmsn foundRecord : records) {
                    thisMatch = record.getKey().getUserAttrId().equals(foundRecord.getKey().getUserAttrId());
                    if(thisMatch) {
                        break;
                    }
                }
                if(!thisMatch) {
                    records.add(record);
                }
            }
        }
                         
        for(AppUserAttrPrmsn foundRecord : records) {
            Hashtable<String, String> hashedResult = new Hashtable<String, String>();
            hashedResult.put("userAttrId", foundRecord.getKey().getUserAttrId());
            distinctRecords.add(hashedResult);
        }
        return distinctRecords;
    }
    
    /**
     * Performs a select for records in the cache matching a range of records bounded by the invocation parameters.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeFindRangeByUserAttrId(Invocation invocation) {
        List<AppUserAttrPrmsn> records = new ArrayList<AppUserAttrPrmsn>();
        
        String userAttrIdMin = (String) invocation.getParameter(0);
        String userAttrIdMax = (String) invocation.getParameter(1);
        Integer rownumMin = (Integer) invocation.getParameter(2);
        Integer rownumMax = (Integer) invocation.getParameter(3);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrIdMin, "The userAttrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        for (AppUserAttrPrmsn recordC : cache.values()) {
            boolean match = (userAttrIdMin.compareTo(recordC.getKey().getUserAttrId()) <= 0);
            match &= ((userAttrIdMax == null) || (userAttrIdMax.compareTo(recordC.getKey().getUserAttrId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, record {}",
                    new Object[] {
                        userAttrIdMin, 
                        userAttrIdMax,
                        recordC
                    });
                records.add(recordC);
            }
        }

        Collections.sort(records, new Comparator<AppUserAttrPrmsn>() {
            public int compare(AppUserAttrPrmsn o1, AppUserAttrPrmsn o2) {
                AppUserAttrPrmsnKey key1 = o1.getKey();
                AppUserAttrPrmsnKey key2 = o2.getKey();

                int comparison = key1.getAppPrtflId().compareTo(key2.getAppPrtflId());
                if (comparison == 0) {
                    comparison = key1.getUserAttrId().compareTo(key2.getUserAttrId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<AppUserAttrPrmsn> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }

    /**
     * Removes a record from the cache if it exists.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  1 if the record matching the specified key was removed, otherwise 0
     */
    private Object invokeDeleteByPrimaryKey(Invocation invocation) {
        AppUserAttrPrmsnKey key = (AppUserAttrPrmsnKey) invocation.getParameter(0);
        AppUserAttrPrmsn record = cache.remove(key);

        logger.trace("key {}, record {}", key, record);

        return (record == null) ? 0 : 1;
    }
}