package com.hp.it.cas.security.dao.mock;

import com.hp.it.cas.xa.validate.Verifier;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsn;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;

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
 * A mock action for IAppCmpndAttrSmplAttrPrmsnDAO interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
public class AppCmpndAttrSmplAttrPrmsnDAOImpl implements Action {
    private static final Logger logger = LoggerFactory.getLogger(AppCmpndAttrSmplAttrPrmsnDAOImpl.class);

    private enum ActionType {
        insert,
        selectByPrimaryKey,
        selectByPrimaryKeyDiscretionary,
        findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId,
        selectByIe1AppCmpndAttrSmplAttrP,
        findRangeByCmpndUserAttrIdBySmplUserAttrId,
        findDistinctByCmpndUserAttrIdBySmplUserAttrId,
        deleteByPrimaryKey
    }

    private final ActionType actionType;
    private final Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache;

    /**
     * Creates a new DAO JMock Action.
     *
     * @param  actionType  the action type
     * @param  cache       the cache that is used to store records
     */
    private AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType actionType, Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
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
    public static IAppCmpndAttrSmplAttrPrmsnDAO mockDAO(Mockery context) {
        final Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache = new HashMap<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn>();
        final IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDAO = context.mock(IAppCmpndAttrSmplAttrPrmsnDAO.class);

        context.checking(new Expectations() {
                {
                    allowing(appCmpndAttrSmplAttrPrmsnDAO).insert(with(aNonNull(AppCmpndAttrSmplAttrPrmsn.class)));
                    will(insert(cache));

                    allowing(appCmpndAttrSmplAttrPrmsnDAO).selectByPrimaryKey(with(any(AppCmpndAttrSmplAttrPrmsnKey.class)));
                    will(selectByPrimaryKey(cache));

                    allowing(appCmpndAttrSmplAttrPrmsnDAO).selectByPrimaryKeyDiscretionary(with(any(AppCmpndAttrSmplAttrPrmsnKey.class)));
                    will(selectByPrimaryKeyDiscretionary(cache));

                    allowing(appCmpndAttrSmplAttrPrmsnDAO).findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(
                        with(any(BigDecimal.class)),
                        with(any(BigDecimal.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(cache));
                    
                    allowing(appCmpndAttrSmplAttrPrmsnDAO).selectByIe1AppCmpndAttrSmplAttrP(
                        with(any(String.class)),
                        with(any(String.class)));
                    will(selectByIe1AppCmpndAttrSmplAttrP(cache));
                    
                    allowing(appCmpndAttrSmplAttrPrmsnDAO).findRangeByCmpndUserAttrIdBySmplUserAttrId(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByCmpndUserAttrIdBySmplUserAttrId(cache));

                    allowing(appCmpndAttrSmplAttrPrmsnDAO).findDistinctByCmpndUserAttrIdBySmplUserAttrId(
                        with(any(String.class)),
                        with(any(String.class)));
                    will(findDistinctByCmpndUserAttrIdBySmplUserAttrId(cache));
                    
                    allowing(appCmpndAttrSmplAttrPrmsnDAO).deleteByPrimaryKey(with(any(AppCmpndAttrSmplAttrPrmsnKey.class)));
                    will(deleteByPrimaryKey(cache));
                }
            });

        return appCmpndAttrSmplAttrPrmsnDAO;
    }

    /**
     * Creates an action for an insert.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action insert(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.insert, cache);
    }

    /**
     * Creates an action for a select by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKey(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.selectByPrimaryKey, cache);
    }

    /**
     * Creates an action for a select by primary key discretionary.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKeyDiscretionary(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.selectByPrimaryKeyDiscretionary, cache);
    }


    /**
     * Creates an action for a find range by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId, cache);
    }
    
    /**
     * Creates an action for a select by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByIe1AppCmpndAttrSmplAttrP(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.selectByIe1AppCmpndAttrSmplAttrP, cache);
    }

    /**
     * Creates an action for a find range by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByCmpndUserAttrIdBySmplUserAttrId(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.findRangeByCmpndUserAttrIdBySmplUserAttrId, cache);
    }
    
    /**
     * Creates an action for find distinct by an index.
     * 
     * @param  cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findDistinctByCmpndUserAttrIdBySmplUserAttrId(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.findDistinctByCmpndUserAttrIdBySmplUserAttrId, cache);
    }
    

    /**
     * Creates an action for an delete by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action deleteByPrimaryKey(Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> cache) {
        return new AppCmpndAttrSmplAttrPrmsnDAOImpl(ActionType.deleteByPrimaryKey, cache);
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

            case findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId: {
                result = invokeFindRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(invocation);

                break;
            }
            
            case selectByIe1AppCmpndAttrSmplAttrP: {
                result = invokeSelectByIe1AppCmpndAttrSmplAttrP(invocation);

                break;
            }

            case findRangeByCmpndUserAttrIdBySmplUserAttrId: {
                result = invokeFindRangeByCmpndUserAttrIdBySmplUserAttrId(invocation);

                break;
            }
            
            case findDistinctByCmpndUserAttrIdBySmplUserAttrId: {
                result = invokeFindDistinctByCmpndUserAttrIdBySmplUserAttrId(invocation);

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
        AppCmpndAttrSmplAttrPrmsn record = (AppCmpndAttrSmplAttrPrmsn) invocation.getParameter(0);

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
        AppCmpndAttrSmplAttrPrmsnKey key = (AppCmpndAttrSmplAttrPrmsnKey) invocation.getParameter(0);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The AppCmpndAttrSmplAttrPrmsn key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getAppPrtflId(), "The appPrtflId cannot be null.")
                .isNotNull(key.getCmpndUserAttrId(), "The cmpndUserAttrId cannot be null.")
                .isNotNull(key.getSmplUserAttrId(), "The smplUserAttrId cannot be null.")
                .throwIfError("Unable to select.");
                
        AppCmpndAttrSmplAttrPrmsn record = cache.get(key);
        
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
        List<AppCmpndAttrSmplAttrPrmsn> records = new ArrayList<AppCmpndAttrSmplAttrPrmsn>();
        AppCmpndAttrSmplAttrPrmsnKey key = (AppCmpndAttrSmplAttrPrmsnKey) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The AppCmpndAttrSmplAttrPrmsn key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getAppPrtflId(), "The appPrtflId cannot be null.").throwIfError("Unable to select.");
        
        for (AppCmpndAttrSmplAttrPrmsn record : cache.values()) {
            AppCmpndAttrSmplAttrPrmsnKey cacheKey = record.getKey();
            boolean match = key.getAppPrtflId()
                               .equals(cacheKey.getAppPrtflId());
            match &= (key.getCmpndUserAttrId() == null)
                || key.getCmpndUserAttrId()
                      .equals(cacheKey.getCmpndUserAttrId());
            match &= (key.getSmplUserAttrId() == null)
                || key.getSmplUserAttrId()
                      .equals(cacheKey.getSmplUserAttrId());

            if (match) {
                logger.trace("key {}, record {}", key, record);
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<AppCmpndAttrSmplAttrPrmsn>() {
            public int compare(AppCmpndAttrSmplAttrPrmsn o1, AppCmpndAttrSmplAttrPrmsn o2) {
                AppCmpndAttrSmplAttrPrmsnKey key1 = o1.getKey();
                AppCmpndAttrSmplAttrPrmsnKey key2 = o2.getKey();

                int comparison = key1.getAppPrtflId().compareTo(key2.getAppPrtflId());
                if (comparison == 0) {
                    comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
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
    private Object invokeFindRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(Invocation invocation) {
        List<AppCmpndAttrSmplAttrPrmsn> records = new ArrayList<AppCmpndAttrSmplAttrPrmsn>();
        
        BigDecimal appPrtflIdMin = (BigDecimal) invocation.getParameter(0);
        BigDecimal appPrtflIdMax = (BigDecimal) invocation.getParameter(1);
        String cmpndUserAttrIdMin = (String) invocation.getParameter(2);
        String cmpndUserAttrIdMax = (String) invocation.getParameter(3);
        String smplUserAttrIdMin = (String) invocation.getParameter(4);
        String smplUserAttrIdMax = (String) invocation.getParameter(5);
        Integer rownumMin = (Integer) invocation.getParameter(6);
        Integer rownumMax = (Integer) invocation.getParameter(7);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(appPrtflIdMin, "The osspVndrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query");
        
        for (AppCmpndAttrSmplAttrPrmsn record : cache.values()) {
            AppCmpndAttrSmplAttrPrmsnKey cacheKey = record.getKey();
            boolean match = (appPrtflIdMin.compareTo(cacheKey.getAppPrtflId()) <= 0);
            match &= ((appPrtflIdMax == null) || (appPrtflIdMax.compareTo(cacheKey.getAppPrtflId()) >= 0));
            match &= ((cmpndUserAttrIdMin == null) || (cmpndUserAttrIdMin.compareTo(cacheKey.getCmpndUserAttrId()) <= 0));
            match &= ((cmpndUserAttrIdMax == null) || (cmpndUserAttrIdMax.compareTo(cacheKey.getCmpndUserAttrId()) >= 0));
            match &= ((smplUserAttrIdMin == null) || (smplUserAttrIdMin.compareTo(cacheKey.getSmplUserAttrId()) <= 0));
            match &= ((smplUserAttrIdMax == null) || (smplUserAttrIdMax.compareTo(cacheKey.getSmplUserAttrId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, {}, {}, record {}",
                    new Object[] {
                        appPrtflIdMin, 
                        appPrtflIdMax,
                        cmpndUserAttrIdMin, 
                        cmpndUserAttrIdMax,
                        smplUserAttrIdMin, 
                        smplUserAttrIdMax,
                        record
                    });
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<AppCmpndAttrSmplAttrPrmsn>() {
            public int compare(AppCmpndAttrSmplAttrPrmsn o1, AppCmpndAttrSmplAttrPrmsn o2) {
                AppCmpndAttrSmplAttrPrmsnKey key1 = o1.getKey();
                AppCmpndAttrSmplAttrPrmsnKey key2 = o2.getKey();

                int comparison = key1.getAppPrtflId().compareTo(key2.getAppPrtflId());
                if (comparison == 0) {
                    comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<AppCmpndAttrSmplAttrPrmsn> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }
    
    /**
     * Performs a select for records in the cache matching the select key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeSelectByIe1AppCmpndAttrSmplAttrP(Invocation invocation) {
        List<AppCmpndAttrSmplAttrPrmsn> records = new ArrayList<AppCmpndAttrSmplAttrPrmsn>();
        Object key0 = invocation.getParameter(0);
        Object key1 = invocation.getParameter(1);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The cmpndUserAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (AppCmpndAttrSmplAttrPrmsn record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getKey().getCmpndUserAttrId());
            match &= (key1 == null) || key1.equals(record.getKey().getSmplUserAttrId());

            if (match) {
                logger.trace("keys {}, {}, record {}",
                    new Object[] {
                        key0,
                        key1,
                        record
                    });
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
    private Object invokeFindDistinctByCmpndUserAttrIdBySmplUserAttrId(Invocation invocation) {
        List<AppCmpndAttrSmplAttrPrmsn> records = new ArrayList<AppCmpndAttrSmplAttrPrmsn>();
        List<Hashtable> distinctRecords = new ArrayList<Hashtable>();
        Object key0 = invocation.getParameter(0);
        Object key1 = invocation.getParameter(1);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The cmpndUserAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (AppCmpndAttrSmplAttrPrmsn record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getKey().getCmpndUserAttrId());
            match &= (key1 == null) || key1.equals(record.getKey().getSmplUserAttrId());
            
            if (match) {
                logger.trace("keys {}, {}, record {}",
                    new Object[] {
                        key0,
                        key1,
                        record
                    });
                // Check this record against the records found previously
                boolean thisMatch = false;
                for(AppCmpndAttrSmplAttrPrmsn foundRecord : records) {
                    thisMatch = record.getKey().getCmpndUserAttrId().equals(foundRecord.getKey().getCmpndUserAttrId());
                    thisMatch &= record.getKey().getSmplUserAttrId().equals(foundRecord.getKey().getSmplUserAttrId());
                    if(thisMatch) {
                        break;
                    }
                }
                if(!thisMatch) {
                    records.add(record);
                }
            }
        }
                         
        for(AppCmpndAttrSmplAttrPrmsn foundRecord : records) {
            Hashtable<String, String> hashedResult = new Hashtable<String, String>();
            hashedResult.put("cmpndUserAttrId", foundRecord.getKey().getCmpndUserAttrId());
            hashedResult.put("smplUserAttrId", foundRecord.getKey().getSmplUserAttrId());
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
    private Object invokeFindRangeByCmpndUserAttrIdBySmplUserAttrId(Invocation invocation) {
        List<AppCmpndAttrSmplAttrPrmsn> records = new ArrayList<AppCmpndAttrSmplAttrPrmsn>();
        
        String cmpndUserAttrIdMin = (String) invocation.getParameter(0);
        String cmpndUserAttrIdMax = (String) invocation.getParameter(1);
        String smplUserAttrIdMin = (String) invocation.getParameter(2);
        String smplUserAttrIdMax = (String) invocation.getParameter(3);
        Integer rownumMin = (Integer) invocation.getParameter(4);
        Integer rownumMax = (Integer) invocation.getParameter(5);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(cmpndUserAttrIdMin, "The cmpndUserAttrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        for (AppCmpndAttrSmplAttrPrmsn recordC : cache.values()) {
            boolean match = (cmpndUserAttrIdMin.compareTo(recordC.getKey().getCmpndUserAttrId()) <= 0);
            match &= ((cmpndUserAttrIdMax == null) || (cmpndUserAttrIdMax.compareTo(recordC.getKey().getCmpndUserAttrId()) >= 0));
            match &= ((smplUserAttrIdMin == null) || (smplUserAttrIdMin.compareTo(recordC.getKey().getSmplUserAttrId()) <= 0));
            match &= ((smplUserAttrIdMax == null) || (smplUserAttrIdMax.compareTo(recordC.getKey().getSmplUserAttrId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, {}, record {}",
                    new Object[] {
                        cmpndUserAttrIdMin, 
                        cmpndUserAttrIdMax,
                        smplUserAttrIdMin, 
                        smplUserAttrIdMax,
                        recordC
                    });
                records.add(recordC);
            }
        }

        Collections.sort(records, new Comparator<AppCmpndAttrSmplAttrPrmsn>() {
            public int compare(AppCmpndAttrSmplAttrPrmsn o1, AppCmpndAttrSmplAttrPrmsn o2) {
                AppCmpndAttrSmplAttrPrmsnKey key1 = o1.getKey();
                AppCmpndAttrSmplAttrPrmsnKey key2 = o2.getKey();

                int comparison = key1.getAppPrtflId().compareTo(key2.getAppPrtflId());
                if (comparison == 0) {
                    comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                }
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<AppCmpndAttrSmplAttrPrmsn> recordsSlice = records.subList((start - 1), end);
        
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
        AppCmpndAttrSmplAttrPrmsnKey key = (AppCmpndAttrSmplAttrPrmsnKey) invocation.getParameter(0);
        AppCmpndAttrSmplAttrPrmsn record = cache.remove(key);

        logger.trace("key {}, record {}", key, record);

        return (record == null) ? 0 : 1;
    }
}