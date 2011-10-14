package com.hp.it.cas.security.dao;

import java.util.List;
  

/**
 * An interface method for the DAO.
 * A Data Access Object manages the connection with the data source.
 *
 * @author CAS Generator v1.0.0
 */
public interface IUserIdTypeDAO {
   
    /**
     * Insert method that returns a key.  For standard inserts the returned key would be the 
     * same as the key contained in the record.  For tables with auto-generated columns, the 
     * key would contain the generated value for that column.
     
     * @param record  a UserIdType object containing the data to insert
     * @return  a key object containing the auto-generated column value
     */
    UserIdTypeKey insert(UserIdType record);

    /**
     * Update by primary key.
     * @param record  a UserIdType object containing the data to insert
     *        the primary key columns are required, all others optional.
     * @return rows number of rows updated
     */
    int updateByPrimaryKeySelective(UserIdType record);

    /**
     * Select by primary key.
     * @param key the table primary key object, all ordinals required
     * @return record a single database record
     */
     UserIdType selectByPrimaryKey(UserIdTypeKey key);
    
    /**
     * Delete by primary key.
     * @param key the table primary key object, all ordinals required
     * @return rows the number of rows deleted
     */
     int deleteByPrimaryKey(UserIdTypeKey key);
    
    /**
     * Find a range of records by primary key.  The minimum value for the first primary key is required, all
     * the rest of the values are optional.  A range is not required, however if the end value is specified
     * the start value must also be specified.
     *
     * @param userIdTypeCdMin  The USER_ID_TYPE_CD lower limit.
     * @param userIdTypeCdMax  The USER_ID_TYPE_CD upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<UserIdType> findRangeByUserIdTypeCd(String userIdTypeCdMin, String userIdTypeCdMax, Integer rownumMin, Integer rownumMax);
}