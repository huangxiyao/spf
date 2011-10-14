package com.hp.it.cas.security.dao;

import java.util.List;
  
import java.util.Hashtable;

/**
 * An interface method for the DAO.
 * A Data Access Object manages the connection with the data source.
 *
 * @author CAS Generator v1.0.0
 */
public interface ICmpndAttrSmplAttrDAO {
   
    /**
     * Insert method that returns a key.  For standard inserts the returned key would be the 
     * same as the key contained in the record.  For tables with auto-generated columns, the 
     * key would contain the generated value for that column.
     
     * @param record  a CmpndAttrSmplAttr object containing the data to insert
     * @return  a key object containing the auto-generated column value
     */
    CmpndAttrSmplAttrKey insert(CmpndAttrSmplAttr record);

    /**
     * Select by primary key.
     * @param key the table primary key object, all ordinals required
     * @return record a single database record
     */
     CmpndAttrSmplAttr selectByPrimaryKey(CmpndAttrSmplAttrKey key);
    
    /**
     * Delete by primary key.
     * @param key the table primary key object, all ordinals required
     * @return rows the number of rows deleted
     */
     int deleteByPrimaryKey(CmpndAttrSmplAttrKey key);
    
    /**
     * Select by primary key, selective.
     * @param key  the table primary key object, first ordinal required, all others optional.
     * @return records  a list of database records
     */
    List<CmpndAttrSmplAttr> selectByPrimaryKeyDiscretionary(CmpndAttrSmplAttrKey key);
 
    /**
     * Find a range of records by primary key.  The minimum value for the first primary key is required, all
     * the rest of the values are optional.  A range is not required, however if the end value is specified
     * the start value must also be specified.
     *
     * @param cmpndUserAttrIdMin  The CMPND_USER_ATTR_ID lower limit.
     * @param cmpndUserAttrIdMax  The CMPND_USER_ATTR_ID upper limit.
     * @param smplUserAttrIdMin  The SMPL_USER_ATTR_ID lower limit.
     * @param smplUserAttrIdMax  The SMPL_USER_ATTR_ID upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<CmpndAttrSmplAttr> findRangeByCmpndUserAttrIdBySmplUserAttrId(String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin, Integer rownumMax);
    /**
     * Select by index IE1_CMPND_ATTR_SMPL_ATTR, selective.
     * @param smplUserAttrId the SMPL_USER_ATTR_ID column
     * @return records  a list of database records
     */
     
    List<CmpndAttrSmplAttr> selectByIe1CmpndAttrSmplAttr( String smplUserAttrId );     
     
     /**
     * Find a range of records by the provided criteria.  The minimum value for the leading index column is 
     * required, the rest of the values are optional.  A range is not required, however if the end value is 
     * specified the start value must also be specified.
     *
     * @param smplUserAttrIdMin  The SMPL_USER_ATTR_ID lower limit.
     * @param smplUserAttrIdMax  The SMPL_USER_ATTR_ID upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<CmpndAttrSmplAttr> findRangeBySmplUserAttrId(String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin, Integer rownumMax);     
     
    /**
     * Find a distinct set of records by the provided criteria.  All the columns are required.
     *
     * @param smplUserAttrId  The SMPL_USER_ATTR_ID value.
     * 
     * @return records  a list of database records
     */
     
     @SuppressWarnings("unchecked")
     List<Hashtable> findDistinctBySmplUserAttrId(String smplUserAttrId);     
}