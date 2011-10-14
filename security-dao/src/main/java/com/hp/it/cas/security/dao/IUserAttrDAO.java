package com.hp.it.cas.security.dao;

import java.util.List;
  

/**
 * An interface method for the DAO.
 * A Data Access Object manages the connection with the data source.
 *
 * @author CAS Generator v1.0.0
 */
public interface IUserAttrDAO {
   
    /**
     * Insert method that returns a key.  For standard inserts the returned key would be the 
     * same as the key contained in the record.  For tables with auto-generated columns, the 
     * key would contain the generated value for that column.
     
     * @param record  a UserAttr object containing the data to insert
     * @return  a key object containing the auto-generated column value
     */
    UserAttrKey insert(UserAttr record);

    /**
     * Update by primary key.
     * @param record  a UserAttr object containing the data to insert
     *        the primary key columns are required, all others optional.
     * @return rows number of rows updated
     */
    int updateByPrimaryKeySelective(UserAttr record);

    /**
     * Select by primary key.
     * @param key the table primary key object, all ordinals required
     * @return record a single database record
     */
     UserAttr selectByPrimaryKey(UserAttrKey key);
    
    /**
     * Delete by primary key.
     * @param key the table primary key object, all ordinals required
     * @return rows the number of rows deleted
     */
     int deleteByPrimaryKey(UserAttrKey key);
    
    /**
     * Find a range of records by primary key.  The minimum value for the first primary key is required, all
     * the rest of the values are optional.  A range is not required, however if the end value is specified
     * the start value must also be specified.
     *
     * @param userAttrIdMin  The USER_ATTR_ID lower limit.
     * @param userAttrIdMax  The USER_ATTR_ID upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<UserAttr> findRangeByUserAttrId(String userAttrIdMin, String userAttrIdMax, Integer rownumMin, Integer rownumMax);
    /**
     * Select by index AK1_USER_ATTR, selective.
     * @param userAttrNm the USER_ATTR_NM column
     * @return record  a database record
     */
     public UserAttr selectByAk1UserAttr(String userAttrNm);
     
     
     /**
     * Find a range of records by the provided criteria.  The minimum value for the leading index column is 
     * required, the rest of the values are optional.  A range is not required, however if the end value is 
     * specified the start value must also be specified.
     *
     * @param userAttrNmMin  The USER_ATTR_NM lower limit.
     * @param userAttrNmMax  The USER_ATTR_NM upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<UserAttr> findRangeByUserAttrNm(String userAttrNmMin, String userAttrNmMax, Integer rownumMin, Integer rownumMax);     
     
    /**
     * Select by index IE1_USER_ATTR, selective.
     * @param userAttrTypeCd the USER_ATTR_TYPE_CD column
     * @return records  a list of database records
     */
     
    List<UserAttr> selectByIe1UserAttr( String userAttrTypeCd );     
     
     /**
     * Find a range of records by the provided criteria.  The minimum value for the leading index column is 
     * required, the rest of the values are optional.  A range is not required, however if the end value is 
     * specified the start value must also be specified.
     *
     * @param userAttrTypeCdMin  The USER_ATTR_TYPE_CD lower limit.
     * @param userAttrTypeCdMax  The USER_ATTR_TYPE_CD upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<UserAttr> findRangeByUserAttrTypeCd(String userAttrTypeCdMin, String userAttrTypeCdMax, Integer rownumMin, Integer rownumMax);     
     
}