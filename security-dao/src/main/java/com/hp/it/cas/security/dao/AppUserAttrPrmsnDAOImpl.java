package com.hp.it.cas.security.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.hp.it.cas.xa.validate.Verifier;

import java.util.List;
import java.util.Hashtable;
import java.math.BigDecimal;

/**
 * A Data Access Object manages the connection with the data source.
 * This DAO provides methods for the APP_USER_ATTR_PRMSN table.
 *
 * @author CAS Generator v1.0.0
 */

public class AppUserAttrPrmsnDAOImpl extends SqlMapClientDaoSupport implements IAppUserAttrPrmsnDAO {

    /**
     * Creates a new DAO
     * @param sqlMapClient the xml file describing the SQL maps
     */
    public AppUserAttrPrmsnDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.setSqlMapClient(sqlMapClient);
    }

    public AppUserAttrPrmsnKey insert(AppUserAttrPrmsn record) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
    	verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");
    
        getSqlMapClientTemplate().insert("APP_USER_ATTR_PRMSN.insert", record);
        AppUserAttrPrmsnKey key = new AppUserAttrPrmsnKey();
        key.setAppPrtflId(record.getKey().getAppPrtflId());
        key.setUserAttrId(record.getKey().getUserAttrId());
        return key; 
    }

    public AppUserAttrPrmsn selectByPrimaryKey(AppUserAttrPrmsnKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getAppPrtflId(), "The key column APP_PRTFL_ID cannot be null..")
    	        .isNotNull(key.getUserAttrId(), "The key column USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to select.");
    	AppUserAttrPrmsn record = (AppUserAttrPrmsn) getSqlMapClientTemplate().queryForObject("APP_USER_ATTR_PRMSN.selectByPrimaryKey", key);
        return record;
    }

    public int deleteByPrimaryKey(AppUserAttrPrmsnKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to delete.");
    	verifier.isNotNull(key.getAppPrtflId(), "The key column APP_PRTFL_ID cannot be null..")
    	        .isNotNull(key.getUserAttrId(), "The key column USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to delete.");
    	int rows = getSqlMapClientTemplate().delete("APP_USER_ATTR_PRMSN.deleteByPrimaryKey", key);
        return rows;
    }
    
    /**
     * Select by primary key, selective.
     * @param key the table primary key object, first ordinal required, all others optional.
     * @return records a list of database records
     */
    @SuppressWarnings("unchecked")
	public List<AppUserAttrPrmsn> selectByPrimaryKeyDiscretionary(AppUserAttrPrmsnKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getAppPrtflId(), "The key column APP_PRTFL_ID cannot be null..").throwIfError("Unable to select.");
        List<AppUserAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_USER_ATTR_PRMSN.selectByPrimaryKeyDiscretionary", key);
    	return records;
    }
    
        
    @SuppressWarnings("unchecked") 
    public List<AppUserAttrPrmsn> findRangeByAppPrtflIdByUserAttrId(BigDecimal appPrtflIdMin, BigDecimal appPrtflIdMax, String userAttrIdMin, String userAttrIdMax, Integer rownumMin, Integer rownumMax) {     
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(appPrtflIdMin, "The appPrtflIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindAppUserAttrPrmsnByRangeByPrimaryKeyWhere where = new FindAppUserAttrPrmsnByRangeByPrimaryKeyWhere();
        where.setAppPrtflIdMin(appPrtflIdMin);
        where.setAppPrtflIdMax(appPrtflIdMax);
        where.setUserAttrIdMin(userAttrIdMin);
        where.setUserAttrIdMax(userAttrIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<AppUserAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_USER_ATTR_PRMSN.findRangeByAppPrtflIdByUserAttrId", where);
        return records;
    }
    
    /**
     * Select by index IE1_APP_USER_ATTR_PRMSN, selective.
     * @param userAttrId  the USER_ATTR_ID column(s).
     *        First ordinal required, all others optional
     * @return records  a list of database records
     */
    @SuppressWarnings("unchecked")
    public List<AppUserAttrPrmsn> selectByIe1AppUserAttrPrmsn( String userAttrId ) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(userAttrId, "The column USER_ATTR_ID cannot be null.").throwIfError("Unable to select.");          
        AppUserAttrPrmsn index = new AppUserAttrPrmsn();
        AppUserAttrPrmsnKey indexKey = new AppUserAttrPrmsnKey();
        indexKey.setUserAttrId(userAttrId);
        index.setKey(indexKey);
        List<AppUserAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_USER_ATTR_PRMSN.selectByIe1AppUserAttrPrmsn", index);
        return records;
    }
     
     @SuppressWarnings("unchecked")
     public List<AppUserAttrPrmsn> findRangeByUserAttrId(String userAttrIdMin, String userAttrIdMax, Integer rownumMin, Integer rownumMax){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrIdMin, "The userAttrIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindAppUserAttrPrmsnByRangeByIe1AppUserAttrPrmsnWhere where = new FindAppUserAttrPrmsnByRangeByIe1AppUserAttrPrmsnWhere();
        where.setUserAttrIdMin(userAttrIdMin);
        where.setUserAttrIdMax(userAttrIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<AppUserAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_USER_ATTR_PRMSN.findRangeByUserAttrId", where);
        return records;
    }
    
    @SuppressWarnings("unchecked")
    public List<Hashtable> findDistinctByUserAttrId(String userAttrId){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrId, "The index column USER_ATTR_ID cannot be null.")
                .throwIfError("Unable to process query.");
        
        AppUserAttrPrmsnKey indexKey = new AppUserAttrPrmsnKey();
        indexKey.setUserAttrId(userAttrId);
        
        List<Hashtable> records = getSqlMapClientTemplate().queryForList("APP_USER_ATTR_PRMSN.findDistinctByUserAttrId", indexKey);
        return records;
        
    }
    
    static class FindAppUserAttrPrmsnByRangeByPrimaryKeyWhere {
    
        private BigDecimal appPrtflIdMin;
        private BigDecimal appPrtflIdMax;
        private String userAttrIdMin;
        private String userAttrIdMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
        public BigDecimal getAppPrtflIdMin(){
            return this.appPrtflIdMin;
        }
    
        public BigDecimal getAppPrtflIdMax(){
            return this.appPrtflIdMax;
        }
    
        public void setAppPrtflIdMin(BigDecimal appPrtflIdMin){
            this.appPrtflIdMin = appPrtflIdMin;
        }
        
        public void setAppPrtflIdMax(BigDecimal appPrtflIdMax){
            this.appPrtflIdMax = appPrtflIdMax;
        }
        
        public String getUserAttrIdMin(){
            return this.userAttrIdMin;
        }
    
        public String getUserAttrIdMax(){
            return this.userAttrIdMax;
        }
    
        public void setUserAttrIdMin(String userAttrIdMin){
            this.userAttrIdMin = userAttrIdMin;
        }
        
        public void setUserAttrIdMax(String userAttrIdMax){
            this.userAttrIdMax = userAttrIdMax;
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
    
     
    static class FindAppUserAttrPrmsnByRangeByIe1AppUserAttrPrmsnWhere {
    
        private String userAttrIdMin;
        private String userAttrIdMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
        public String getUserAttrIdMin(){
            return this.userAttrIdMin;
        }
    
        public String getUserAttrIdMax(){
            return this.userAttrIdMax;
        }
    
        public void setUserAttrIdMin(String userAttrIdMin){
            this.userAttrIdMin = userAttrIdMin;
        }
        
        public void setUserAttrIdMax(String userAttrIdMax){
            this.userAttrIdMax = userAttrIdMax;
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