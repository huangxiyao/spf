package com.hp.it.cas.security.dao;

import java.util.List;
  

/**
 * An interface method for the DAO.
 * A Data Access Object manages the connection with the data source.
 *
 * @author CAS Generator v1.0.0
 */
public interface IUserAttrValuDAO {
   
    /**
     * Insert method that returns a key.  For standard inserts the returned key would be the 
     * same as the key contained in the record.  For tables with auto-generated columns, the 
     * key would contain the generated value for that column.
     
     * @param record  a UserAttrValu object containing the data to insert
     * @return  a key object containing the auto-generated column value
     */
    UserAttrValuKey insert(UserAttrValu record);

    /**
     * Update by primary key.
     * @param record  a UserAttrValu object containing the data to insert
     *        the primary key columns are required, all others optional.
     * @return rows number of rows updated
     */
    int updateByPrimaryKeySelective(UserAttrValu record);

    /**
     * Select by primary key.
     * @param key the table primary key object, all ordinals required
     * @return record a single database record
     */
     UserAttrValu selectByPrimaryKey(UserAttrValuKey key);
    
    /**
     * Delete by primary key.
     * @param key the table primary key object, all ordinals required
     * @return rows the number of rows deleted
     */
     int deleteByPrimaryKey(UserAttrValuKey key);
    
    /**
     * Select by primary key, selective.
     * @param key  the table primary key object, first ordinal required, all others optional.
     * @return records  a list of database records
     */
    List<UserAttrValu> selectByPrimaryKeyDiscretionary(UserAttrValuKey key);
 
    /**
     * Find a range of records by primary key.  The minimum value for the first primary key is required, all
     * the rest of the values are optional.  A range is not required, however if the end value is specified
     * the start value must also be specified.
     *
     * @param userIdMin  The USER_ID lower limit.
     * @param userIdMax  The USER_ID upper limit.
     * @param userIdTypeCdMin  The USER_ID_TYPE_CD lower limit.
     * @param userIdTypeCdMax  The USER_ID_TYPE_CD upper limit.
     * @param cmpndUserAttrIdMin  The CMPND_USER_ATTR_ID lower limit.
     * @param cmpndUserAttrIdMax  The CMPND_USER_ATTR_ID upper limit.
     * @param smplUserAttrIdMin  The SMPL_USER_ATTR_ID lower limit.
     * @param smplUserAttrIdMax  The SMPL_USER_ATTR_ID upper limit.
     * @param userAttrInstncIdMin  The USER_ATTR_INSTNC_ID lower limit.
     * @param userAttrInstncIdMax  The USER_ATTR_INSTNC_ID upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<UserAttrValu> findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String userIdMin, String userIdMax, String userIdTypeCdMin, String userIdTypeCdMax, String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, String userAttrInstncIdMin, String userAttrInstncIdMax, Integer rownumMin, Integer rownumMax);
    /**
     * Select by index IE1_USER_ATTR_VALU, selective.
     * @param cmpndUserAttrId the CMPND_USER_ATTR_ID column
     * @param smplUserAttrId the SMPL_USER_ATTR_ID column
     * @param userAttrValuTx the USER_ATTR_VALU_TX column
     * @return records  a list of database records
     */
     
    List<UserAttrValu> selectByIe1UserAttrValu( String cmpndUserAttrId , String smplUserAttrId , String userAttrValuTx );     
     
     /**
     * Find a range of records by the provided criteria.  The minimum value for the leading index column is 
     * required, the rest of the values are optional.  A range is not required, however if the end value is 
     * specified the start value must also be specified.
     *
     * @param cmpndUserAttrIdMin  The CMPND_USER_ATTR_ID lower limit.
     * @param cmpndUserAttrIdMax  The CMPND_USER_ATTR_ID upper limit.
     * @param smplUserAttrIdMin  The SMPL_USER_ATTR_ID lower limit.
     * @param smplUserAttrIdMax  The SMPL_USER_ATTR_ID upper limit.
     * @param userAttrValuTxMin  The USER_ATTR_VALU_TX lower limit.
     * @param userAttrValuTxMax  The USER_ATTR_VALU_TX upper limit.
     * @param rownumMin  The row number lower limit.
     * @param rownumMax  The row number upper limit.
     * 
     * @return records  a list of database records
     */
     
     List<UserAttrValu> findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, String userAttrValuTxMin, String userAttrValuTxMax, Integer rownumMin, Integer rownumMax);     
     
}