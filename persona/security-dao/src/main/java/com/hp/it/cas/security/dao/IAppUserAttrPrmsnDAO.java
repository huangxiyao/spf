package com.hp.it.cas.security.dao;

import java.util.List;
  
import java.util.Hashtable;
import java.math.BigDecimal;

/**
 * An interface method for the DAO.
 * A Data Access Object manages the connection with the data source.
 *
 * @author CAS Generator v1.0.0
 */
public interface IAppUserAttrPrmsnDAO {
   
    /**
     * Insert method that returns a key.  For standard inserts the returned key would be the 
     * same as the key contained in the record.  For tables with auto-generated columns, the 
     * key would contain the generated value for that column.
     
     * @param record  a AppUserAttrPrmsn object containing the data to insert
     * @return  a key object containing the auto-generated column value
     */
    AppUserAttrPrmsnKey insert(AppUserAttrPrmsn record);

    /**
     * Select by primary key.
     * @param key the table primary key object, all ordinals required
     * @return record a single database record
     */
     AppUserAttrPrmsn selectByPrimaryKey(AppUserAttrPrmsnKey key);
    
    /**
     * Delete by primary key.
     * @param key the table primary key object, all ordinals required
     * @return rows the number of rows deleted
     */
     int deleteByPrimaryKey(AppUserAttrPrmsnKey key);
    
    /**
     * Select by primary key, selective.
     * @param key  the table primary key object, first ordinal required, all others optional.
     * @return records  a list of database records
     */
    List<AppUserAttrPrmsn> selectByPrimaryKeyDiscretionary(AppUserAttrPrmsnKey key);
 
    /**
     * Find a range of records by primary key.  The minimum value for the first primary key is required, all
     * the rest of the values are optional.  A range is not required, however if the end value is specified
     * the start value must also be specified.
     *
     * @param appPrtflIdMin  The APP_PRTFL_ID lower limit.
     * @param appPrtflIdMax  The APP_PRTFL_ID upper limit.
     * @param userAttrIdMin  The USER_ATTR_ID lower limit.
     * @param userAttrIdMax  The USER_ATTR_ID upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<AppUserAttrPrmsn> findRangeByAppPrtflIdByUserAttrId(BigDecimal appPrtflIdMin, BigDecimal appPrtflIdMax, String userAttrIdMin, String userAttrIdMax, Integer rownumMin, Integer rownumMax);
    /**
     * Select by index IE1_APP_USER_ATTR_PRMSN, selective.
     * @param userAttrId the USER_ATTR_ID column
     * @return records  a list of database records
     */
     
    List<AppUserAttrPrmsn> selectByIe1AppUserAttrPrmsn( String userAttrId );     
     
     /**
     * Find a range of records by the provided criteria.  The minimum value for the leading index column is 
     * required, the rest of the values are optional.  A range is not required, however if the end value is 
     * specified the start value must also be specified.
     *
     * @param userAttrIdMin  The USER_ATTR_ID lower limit.
     * @param userAttrIdMax  The USER_ATTR_ID upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<AppUserAttrPrmsn> findRangeByUserAttrId(String userAttrIdMin, String userAttrIdMax, Integer rownumMin, Integer rownumMax);     
     
    /**
     * Find a distinct set of records by the provided criteria.  All the columns are required.
     *
     * @param userAttrId  The USER_ATTR_ID value.
     * 
     * @return records  a list of database records
     */
     
     @SuppressWarnings("unchecked")
     List<Hashtable> findDistinctByUserAttrId(String userAttrId);     
}