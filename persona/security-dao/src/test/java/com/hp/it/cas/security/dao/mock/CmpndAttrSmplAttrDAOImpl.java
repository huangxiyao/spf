package com.hp.it.cas.security.dao.mock;

import com.hp.it.cas.xa.validate.Verifier;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttr;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;

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
import java.util.Hashtable;

/**
 * A mock action for ICmpndAttrSmplAttrDAO interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
public class CmpndAttrSmplAttrDAOImpl implements Action {
    private static final Logger logger = LoggerFactory.getLogger(CmpndAttrSmplAttrDAOImpl.class);

    private enum ActionType {
        insert,
        selectByPrimaryKey,
        selectByPrimaryKeyDiscretionary,
        findRangeByCmpndUserAttrIdBySmplUserAttrId,
        selectByIe1CmpndAttrSmplAttr,
        findRangeBySmplUserAttrId,
        findDistinctBySmplUserAttrId,
        deleteByPrimaryKey
    }

    private final ActionType actionType;
    private final Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache;

    /**
     * Creates a new DAO JMock Action.
     *
     * @param  actionType  the action type
     * @param  cache       the cache that is used to store records
     */
    private CmpndAttrSmplAttrDAOImpl(ActionType actionType, Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
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
    public static ICmpndAttrSmplAttrDAO mockDAO(Mockery context) {
        final Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache = new HashMap<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr>();
        final ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDAO = context.mock(ICmpndAttrSmplAttrDAO.class);

        context.checking(new Expectations() {
                {
                    allowing(cmpndAttrSmplAttrDAO).insert(with(aNonNull(CmpndAttrSmplAttr.class)));
                    will(insert(cache));

                    allowing(cmpndAttrSmplAttrDAO).selectByPrimaryKey(with(any(CmpndAttrSmplAttrKey.class)));
                    will(selectByPrimaryKey(cache));

                    allowing(cmpndAttrSmplAttrDAO).selectByPrimaryKeyDiscretionary(with(any(CmpndAttrSmplAttrKey.class)));
                    will(selectByPrimaryKeyDiscretionary(cache));

                    allowing(cmpndAttrSmplAttrDAO).findRangeByCmpndUserAttrIdBySmplUserAttrId(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeByCmpndUserAttrIdBySmplUserAttrId(cache));
                    
                    allowing(cmpndAttrSmplAttrDAO).selectByIe1CmpndAttrSmplAttr(
                        with(any(String.class)));
                    will(selectByIe1CmpndAttrSmplAttr(cache));
                    
                    allowing(cmpndAttrSmplAttrDAO).findRangeBySmplUserAttrId(
                        with(any(String.class)),
                        with(any(String.class)),
                        with(any(Integer.class)),
                        with(any(Integer.class)));
                    will(findRangeBySmplUserAttrId(cache));

                    allowing(cmpndAttrSmplAttrDAO).findDistinctBySmplUserAttrId(
                        with(any(String.class)));
                    will(findDistinctBySmplUserAttrId(cache));
                    
                    allowing(cmpndAttrSmplAttrDAO).deleteByPrimaryKey(with(any(CmpndAttrSmplAttrKey.class)));
                    will(deleteByPrimaryKey(cache));
                }
            });

        return cmpndAttrSmplAttrDAO;
    }

    /**
     * Creates an action for an insert.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action insert(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.insert, cache);
    }

    /**
     * Creates an action for a select by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKey(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.selectByPrimaryKey, cache);
    }

    /**
     * Creates an action for a select by primary key discretionary.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByPrimaryKeyDiscretionary(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.selectByPrimaryKeyDiscretionary, cache);
    }


    /**
     * Creates an action for a find range by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeByCmpndUserAttrIdBySmplUserAttrId(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.findRangeByCmpndUserAttrIdBySmplUserAttrId, cache);
    }
    
    /**
     * Creates an action for a select by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action selectByIe1CmpndAttrSmplAttr(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.selectByIe1CmpndAttrSmplAttr, cache);
    }

    /**
     * Creates an action for a find range by an index.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findRangeBySmplUserAttrId(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.findRangeBySmplUserAttrId, cache);
    }
    
    /**
     * Creates an action for find distinct by an index.
     * 
     * @param  cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action findDistinctBySmplUserAttrId(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.findDistinctBySmplUserAttrId, cache);
    }
    

    /**
     * Creates an action for an delete by primary key.
     *
     * @param   cache  the cache that is used to store records
     *
     * @return  this action
     */
    static Action deleteByPrimaryKey(Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cache) {
        return new CmpndAttrSmplAttrDAOImpl(ActionType.deleteByPrimaryKey, cache);
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

            case findRangeByCmpndUserAttrIdBySmplUserAttrId: {
                result = invokeFindRangeByCmpndUserAttrIdBySmplUserAttrId(invocation);

                break;
            }
            
            case selectByIe1CmpndAttrSmplAttr: {
                result = invokeSelectByIe1CmpndAttrSmplAttr(invocation);

                break;
            }

            case findRangeBySmplUserAttrId: {
                result = invokeFindRangeBySmplUserAttrId(invocation);

                break;
            }
            
            case findDistinctBySmplUserAttrId: {
                result = invokeFindDistinctBySmplUserAttrId(invocation);

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
        CmpndAttrSmplAttr record = (CmpndAttrSmplAttr) invocation.getParameter(0);

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
        CmpndAttrSmplAttrKey key = (CmpndAttrSmplAttrKey) invocation.getParameter(0);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The CmpndAttrSmplAttr key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getCmpndUserAttrId(), "The cmpndUserAttrId cannot be null.")
                .isNotNull(key.getSmplUserAttrId(), "The smplUserAttrId cannot be null.")
                .throwIfError("Unable to select.");
                
        CmpndAttrSmplAttr record = cache.get(key);
        
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
        List<CmpndAttrSmplAttr> records = new ArrayList<CmpndAttrSmplAttr>();
        CmpndAttrSmplAttrKey key = (CmpndAttrSmplAttrKey) invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key, "The CmpndAttrSmplAttr key cannot be null").throwIfError("Unable to select.");
        verifier.isNotNull(key.getCmpndUserAttrId(), "The cmpndUserAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (CmpndAttrSmplAttr record : cache.values()) {
            CmpndAttrSmplAttrKey cacheKey = record.getKey();
            boolean match = key.getCmpndUserAttrId()
                               .equals(cacheKey.getCmpndUserAttrId());
            match &= (key.getSmplUserAttrId() == null)
                || key.getSmplUserAttrId()
                      .equals(cacheKey.getSmplUserAttrId());

            if (match) {
                logger.trace("key {}, record {}", key, record);
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<CmpndAttrSmplAttr>() {
            public int compare(CmpndAttrSmplAttr o1, CmpndAttrSmplAttr o2) {
                CmpndAttrSmplAttrKey key1 = o1.getKey();
                CmpndAttrSmplAttrKey key2 = o2.getKey();

                int comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
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
    private Object invokeFindRangeByCmpndUserAttrIdBySmplUserAttrId(Invocation invocation) {
        List<CmpndAttrSmplAttr> records = new ArrayList<CmpndAttrSmplAttr>();
        
        String cmpndUserAttrIdMin = (String) invocation.getParameter(0);
        String cmpndUserAttrIdMax = (String) invocation.getParameter(1);
        String smplUserAttrIdMin = (String) invocation.getParameter(2);
        String smplUserAttrIdMax = (String) invocation.getParameter(3);
        Integer rownumMin = (Integer) invocation.getParameter(4);
        Integer rownumMax = (Integer) invocation.getParameter(5);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(cmpndUserAttrIdMin, "The osspVndrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query");
        
        for (CmpndAttrSmplAttr record : cache.values()) {
            CmpndAttrSmplAttrKey cacheKey = record.getKey();
            boolean match = (cmpndUserAttrIdMin.compareTo(cacheKey.getCmpndUserAttrId()) <= 0);
            match &= ((cmpndUserAttrIdMax == null) || (cmpndUserAttrIdMax.compareTo(cacheKey.getCmpndUserAttrId()) >= 0));
            match &= ((smplUserAttrIdMin == null) || (smplUserAttrIdMin.compareTo(cacheKey.getSmplUserAttrId()) <= 0));
            match &= ((smplUserAttrIdMax == null) || (smplUserAttrIdMax.compareTo(cacheKey.getSmplUserAttrId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, {}, record {}",
                    new Object[] {
                        cmpndUserAttrIdMin, 
                        cmpndUserAttrIdMax,
                        smplUserAttrIdMin, 
                        smplUserAttrIdMax,
                        record
                    });
                records.add(record);
            }
        }

        Collections.sort(records, new Comparator<CmpndAttrSmplAttr>() {
            public int compare(CmpndAttrSmplAttr o1, CmpndAttrSmplAttr o2) {
                CmpndAttrSmplAttrKey key1 = o1.getKey();
                CmpndAttrSmplAttrKey key2 = o2.getKey();

                int comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<CmpndAttrSmplAttr> recordsSlice = records.subList((start - 1), end);
        
        return recordsSlice;
    }
    
    /**
     * Performs a select for records in the cache matching the select key.
     *
     * @param   invocation  provides details about the method invoked
     *
     * @return  the selected records
     */
    private Object invokeSelectByIe1CmpndAttrSmplAttr(Invocation invocation) {
        List<CmpndAttrSmplAttr> records = new ArrayList<CmpndAttrSmplAttr>();
        Object key0 = invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The smplUserAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (CmpndAttrSmplAttr record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getKey().getSmplUserAttrId());

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
    private Object invokeFindDistinctBySmplUserAttrId(Invocation invocation) {
        List<CmpndAttrSmplAttr> records = new ArrayList<CmpndAttrSmplAttr>();
        List<Hashtable> distinctRecords = new ArrayList<Hashtable>();
        Object key0 = invocation.getParameter(0);

        Verifier verifier = new Verifier();
        verifier.isNotNull(key0, "The smplUserAttrId cannot be null.").throwIfError("Unable to select.");
        
        for (CmpndAttrSmplAttr record : cache.values()) {
            boolean match = (key0 == null) ? false : key0.equals(record.getKey().getSmplUserAttrId());
            
            if (match) {
                logger.trace("key {}, record {}", key0, record);
                // Check this record against the records found previously
                boolean thisMatch = false;
                for(CmpndAttrSmplAttr foundRecord : records) {
                    thisMatch = record.getKey().getSmplUserAttrId().equals(foundRecord.getKey().getSmplUserAttrId());
                    if(thisMatch) {
                        break;
                    }
                }
                if(!thisMatch) {
                    records.add(record);
                }
            }
        }
                         
        for(CmpndAttrSmplAttr foundRecord : records) {
            Hashtable<String, String> hashedResult = new Hashtable<String, String>();
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
    private Object invokeFindRangeBySmplUserAttrId(Invocation invocation) {
        List<CmpndAttrSmplAttr> records = new ArrayList<CmpndAttrSmplAttr>();
        
        String smplUserAttrIdMin = (String) invocation.getParameter(0);
        String smplUserAttrIdMax = (String) invocation.getParameter(1);
        Integer rownumMin = (Integer) invocation.getParameter(2);
        Integer rownumMax = (Integer) invocation.getParameter(3);
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(smplUserAttrIdMin, "The smplUserAttrId min value cannot be null.")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        for (CmpndAttrSmplAttr recordC : cache.values()) {
            boolean match = (smplUserAttrIdMin.compareTo(recordC.getKey().getSmplUserAttrId()) <= 0);
            match &= ((smplUserAttrIdMax == null) || (smplUserAttrIdMax.compareTo(recordC.getKey().getSmplUserAttrId()) >= 0));
            
            if (match) {
                logger.trace("keys {}, record {}",
                    new Object[] {
                        smplUserAttrIdMin, 
                        smplUserAttrIdMax,
                        recordC
                    });
                records.add(recordC);
            }
        }

        Collections.sort(records, new Comparator<CmpndAttrSmplAttr>() {
            public int compare(CmpndAttrSmplAttr o1, CmpndAttrSmplAttr o2) {
                CmpndAttrSmplAttrKey key1 = o1.getKey();
                CmpndAttrSmplAttrKey key2 = o2.getKey();

                int comparison = key1.getCmpndUserAttrId().compareTo(key2.getCmpndUserAttrId());
                if (comparison == 0) {
                    comparison = key1.getSmplUserAttrId().compareTo(key2.getSmplUserAttrId());
                }
                return comparison;
            }
        });
        
        int start = rownumMin == null || rownumMin == 0 ? 1 : rownumMin.intValue();
        int end = rownumMax == null  ? records.size() : rownumMax.intValue();
        end = end > records.size() ? records.size() : end;
        List<CmpndAttrSmplAttr> recordsSlice = records.subList((start - 1), end);
        
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
        CmpndAttrSmplAttrKey key = (CmpndAttrSmplAttrKey) invocation.getParameter(0);
        CmpndAttrSmplAttr record = cache.remove(key);

        logger.trace("key {}, record {}", key, record);

        return (record == null) ? 0 : 1;
    }
}