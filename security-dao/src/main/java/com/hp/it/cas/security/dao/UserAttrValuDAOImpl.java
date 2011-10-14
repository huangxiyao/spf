package com.hp.it.cas.security.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.hp.it.cas.xa.validate.Verifier;

import java.util.List;

/**
 * A Data Access Object manages the connection with the data source.
 * This DAO provides methods for the USER_ATTR_VALU table.
 *
 * @author CAS Generator v1.0.0
 */

public class UserAttrValuDAOImpl extends SqlMapClientDaoSupport implements IUserAttrValuDAO {

    /**
     * Creates a new DAO
     * @param sqlMapClient the xml file describing the SQL maps
     */
    public UserAttrValuDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.setSqlMapClient(sqlMapClient);
    }

    public UserAttrValuKey insert(UserAttrValu record) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
    	verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");
    
        getSqlMapClientTemplate().insert("USER_ATTR_VALU.insert", record);
        UserAttrValuKey key = new UserAttrValuKey();
        key.setUserId(record.getKey().getUserId());
        key.setUserIdTypeCd(record.getKey().getUserIdTypeCd());
        key.setCmpndUserAttrId(record.getKey().getCmpndUserAttrId());
        key.setSmplUserAttrId(record.getKey().getSmplUserAttrId());
        key.setUserAttrInstncId(record.getKey().getUserAttrInstncId());
        return key; 
    }

    public int updateByPrimaryKeySelective(UserAttrValu record) {
        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey(), "The key cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey().getUserId(), "The key column USER_ID cannot be null..")
    	        .isNotNull(record.getKey().getUserIdTypeCd(), "The key column USER_ID_TYPE_CD cannot be null..")
    	        .isNotNull(record.getKey().getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..")
    	        .isNotNull(record.getKey().getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null..")
    	        .isNotNull(record.getKey().getUserAttrInstncId(), "The key column USER_ATTR_INSTNC_ID cannot be null..")
    	        .throwIfError("Unable to update.");
        int rows = getSqlMapClientTemplate().update("USER_ATTR_VALU.updateByPrimaryKeySelective", record);
        return rows;
    }

    public UserAttrValu selectByPrimaryKey(UserAttrValuKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getUserId(), "The key column USER_ID cannot be null..")
    	        .isNotNull(key.getUserIdTypeCd(), "The key column USER_ID_TYPE_CD cannot be null..")
    	        .isNotNull(key.getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getUserAttrInstncId(), "The key column USER_ATTR_INSTNC_ID cannot be null..")
    	        .throwIfError("Unable to select.");
    	UserAttrValu record = (UserAttrValu) getSqlMapClientTemplate().queryForObject("USER_ATTR_VALU.selectByPrimaryKey", key);
        return record;
    }

    public int deleteByPrimaryKey(UserAttrValuKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to delete.");
    	verifier.isNotNull(key.getUserId(), "The key column USER_ID cannot be null..")
    	        .isNotNull(key.getUserIdTypeCd(), "The key column USER_ID_TYPE_CD cannot be null..")
    	        .isNotNull(key.getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getUserAttrInstncId(), "The key column USER_ATTR_INSTNC_ID cannot be null..")
    	        .throwIfError("Unable to delete.");
    	int rows = getSqlMapClientTemplate().delete("USER_ATTR_VALU.deleteByPrimaryKey", key);
        return rows;
    }
    
    /**
     * Select by primary key, selective.
     * @param key the table primary key object, first ordinal required, all others optional.
     * @return records a list of database records
     */
    @SuppressWarnings("unchecked")
	public List<UserAttrValu> selectByPrimaryKeyDiscretionary(UserAttrValuKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getUserId(), "The key column USER_ID cannot be null..").throwIfError("Unable to select.");
        List<UserAttrValu> records = getSqlMapClientTemplate().queryForList("USER_ATTR_VALU.selectByPrimaryKeyDiscretionary", key);
    	return records;
    }
    
        
    @SuppressWarnings("unchecked") 
    public List<UserAttrValu> findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId(String userIdMin, String userIdMax, String userIdTypeCdMin, String userIdTypeCdMax, String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, String userAttrInstncIdMin, String userAttrInstncIdMax, Integer rownumMin, Integer rownumMax) {     
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userIdMin, "The userIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindUserAttrValuByRangeByPrimaryKeyWhere where = new FindUserAttrValuByRangeByPrimaryKeyWhere();
        where.setUserIdMin(userIdMin);
        where.setUserIdMax(userIdMax);
        where.setUserIdTypeCdMin(userIdTypeCdMin);
        where.setUserIdTypeCdMax(userIdTypeCdMax);
        where.setCmpndUserAttrIdMin(cmpndUserAttrIdMin);
        where.setCmpndUserAttrIdMax(cmpndUserAttrIdMax);
        where.setSmplUserAttrIdMin(smplUserAttrIdMin);
        where.setSmplUserAttrIdMax(smplUserAttrIdMax);
        where.setUserAttrInstncIdMin(userAttrInstncIdMin);
        where.setUserAttrInstncIdMax(userAttrInstncIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<UserAttrValu> records = getSqlMapClientTemplate().queryForList("USER_ATTR_VALU.findRangeByUserIdByUserIdTypeCdByCmpndUserAttrIdBySmplUserAttrIdByUserAttrInstncId", where);
        return records;
    }
    
    /**
     * Select by index IE1_USER_ATTR_VALU, selective.
     * @param cmpndUserAttrId  the CMPND_USER_ATTR_ID column(s).
     *        First ordinal required, all others optional
     * @param smplUserAttrId  the SMPL_USER_ATTR_ID column(s).
     *        First ordinal required, all others optional
     * @param userAttrValuTx  the USER_ATTR_VALU_TX column(s).
     *        First ordinal required, all others optional
     * @return records  a list of database records
     */
    @SuppressWarnings("unchecked")
    public List<UserAttrValu> selectByIe1UserAttrValu( String cmpndUserAttrId , String smplUserAttrId , String userAttrValuTx ) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(cmpndUserAttrId, "The column CMPND_USER_ATTR_ID cannot be null.").throwIfError("Unable to select.");          
        UserAttrValu index = new UserAttrValu();
        UserAttrValuKey indexKey = new UserAttrValuKey();
        indexKey.setCmpndUserAttrId(cmpndUserAttrId);
        indexKey.setSmplUserAttrId(smplUserAttrId);
        index.setUserAttrValuTx(userAttrValuTx);
        index.setKey(indexKey);
        List<UserAttrValu> records = getSqlMapClientTemplate().queryForList("USER_ATTR_VALU.selectByIe1UserAttrValu", index);
        return records;
    }
     
     @SuppressWarnings("unchecked")
     public List<UserAttrValu> findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx(String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, String userAttrValuTxMin, String userAttrValuTxMax, Integer rownumMin, Integer rownumMax){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(cmpndUserAttrIdMin, "The cmpndUserAttrIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindUserAttrValuByRangeByIe1UserAttrValuWhere where = new FindUserAttrValuByRangeByIe1UserAttrValuWhere();
        where.setCmpndUserAttrIdMin(cmpndUserAttrIdMin);
        where.setCmpndUserAttrIdMax(cmpndUserAttrIdMax);
        where.setSmplUserAttrIdMin(smplUserAttrIdMin);
        where.setSmplUserAttrIdMax(smplUserAttrIdMax);
        where.setUserAttrValuTxMin(userAttrValuTxMin);
        where.setUserAttrValuTxMax(userAttrValuTxMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<UserAttrValu> records = getSqlMapClientTemplate().queryForList("USER_ATTR_VALU.findRangeByCmpndUserAttrIdBySmplUserAttrIdByUserAttrValuTx", where);
        return records;
    }
    
    
    static class FindUserAttrValuByRangeByPrimaryKeyWhere {
    
        private String userIdMin;
        private String userIdMax;
        private String userIdTypeCdMin;
        private String userIdTypeCdMax;
        private String cmpndUserAttrIdMin;
        private String cmpndUserAttrIdMax;
        private String smplUserAttrIdMin;
        private String smplUserAttrIdMax;
        private String userAttrInstncIdMin;
        private String userAttrInstncIdMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
        public String getUserIdMin(){
            return this.userIdMin;
        }
    
        public String getUserIdMax(){
            return this.userIdMax;
        }
    
        public void setUserIdMin(String userIdMin){
            this.userIdMin = userIdMin;
        }
        
        public void setUserIdMax(String userIdMax){
            this.userIdMax = userIdMax;
        }
        
        public String getUserIdTypeCdMin(){
            return this.userIdTypeCdMin;
        }
    
        public String getUserIdTypeCdMax(){
            return this.userIdTypeCdMax;
        }
    
        public void setUserIdTypeCdMin(String userIdTypeCdMin){
            this.userIdTypeCdMin = userIdTypeCdMin;
        }
        
        public void setUserIdTypeCdMax(String userIdTypeCdMax){
            this.userIdTypeCdMax = userIdTypeCdMax;
        }
        
        public String getCmpndUserAttrIdMin(){
            return this.cmpndUserAttrIdMin;
        }
    
        public String getCmpndUserAttrIdMax(){
            return this.cmpndUserAttrIdMax;
        }
    
        public void setCmpndUserAttrIdMin(String cmpndUserAttrIdMin){
            this.cmpndUserAttrIdMin = cmpndUserAttrIdMin;
        }
        
        public void setCmpndUserAttrIdMax(String cmpndUserAttrIdMax){
            this.cmpndUserAttrIdMax = cmpndUserAttrIdMax;
        }
        
        public String getSmplUserAttrIdMin(){
            return this.smplUserAttrIdMin;
        }
    
        public String getSmplUserAttrIdMax(){
            return this.smplUserAttrIdMax;
        }
    
        public void setSmplUserAttrIdMin(String smplUserAttrIdMin){
            this.smplUserAttrIdMin = smplUserAttrIdMin;
        }
        
        public void setSmplUserAttrIdMax(String smplUserAttrIdMax){
            this.smplUserAttrIdMax = smplUserAttrIdMax;
        }
        
        public String getUserAttrInstncIdMin(){
            return this.userAttrInstncIdMin;
        }
    
        public String getUserAttrInstncIdMax(){
            return this.userAttrInstncIdMax;
        }
    
        public void setUserAttrInstncIdMin(String userAttrInstncIdMin){
            this.userAttrInstncIdMin = userAttrInstncIdMin;
        }
        
        public void setUserAttrInstncIdMax(String userAttrInstncIdMax){
            this.userAttrInstncIdMax = userAttrInstncIdMax;
        }
        
    
        public Integer getRownumMin() {
            return rownumMin;
        }

        public void setRownumMin(Integer rownumMin) {
            this.rownumMin = rownumMin;
        }

        public Integer getRownumMax() {
            return rownumMax;
        }
    
        public void setRownumMax(Integer rownumMax) {
            this.rownumMax = rownumMax;
        }
    }
    
     
    static class FindUserAttrValuByRangeByIe1UserAttrValuWhere {
    
        private String cmpndUserAttrIdMin;
        private String cmpndUserAttrIdMax;
        private String smplUserAttrIdMin;
        private String smplUserAttrIdMax;
        private String userAttrValuTxMin;
        private String userAttrValuTxMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
        public String getCmpndUserAttrIdMin(){
            return this.cmpndUserAttrIdMin;
        }
    
        public String getCmpndUserAttrIdMax(){
            return this.cmpndUserAttrIdMax;
        }
    
        public void setCmpndUserAttrIdMin(String cmpndUserAttrIdMin){
            this.cmpndUserAttrIdMin = cmpndUserAttrIdMin;
        }
        
        public void setCmpndUserAttrIdMax(String cmpndUserAttrIdMax){
            this.cmpndUserAttrIdMax = cmpndUserAttrIdMax;
        }
        
        public String getSmplUserAttrIdMin(){
            return this.smplUserAttrIdMin;
        }
    
        public String getSmplUserAttrIdMax(){
            return this.smplUserAttrIdMax;
        }
    
        public void setSmplUserAttrIdMin(String smplUserAttrIdMin){
            this.smplUserAttrIdMin = smplUserAttrIdMin;
        }
        
        public void setSmplUserAttrIdMax(String smplUserAttrIdMax){
            this.smplUserAttrIdMax = smplUserAttrIdMax;
        }
        
        public String getUserAttrValuTxMin(){
            return this.userAttrValuTxMin;
        }
    
        public String getUserAttrValuTxMax(){
            return this.userAttrValuTxMax;
        }
    
        public void setUserAttrValuTxMin(String userAttrValuTxMin){
            this.userAttrValuTxMin = userAttrValuTxMin;
        }
        
        public void setUserAttrValuTxMax(String userAttrValuTxMax){
            this.userAttrValuTxMax = userAttrValuTxMax;
        }
        
    
        public Integer getRownumMin() {
            return rownumMin;
        }

        public void setRownumMin(Integer rownumMin) {
            this.rownumMin = rownumMin;
        }

        public Integer getRownumMax() {
            return rownumMax;
        }
    
        public void setRownumMax(Integer rownumMax) {
            this.rownumMax = rownumMax;
        }
    }
    
}