package com.hp.it.cas.security.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.hp.it.cas.xa.validate.Verifier;

import java.util.List;
import java.util.Hashtable;
import java.math.BigDecimal;

/**
 * A Data Access Object manages the connection with the data source.
 * This DAO provides methods for the APP_CMPND_ATTR_SMPL_ATTR_PRMSN table.
 *
 * @author CAS Generator v1.0.0
 */

public class AppCmpndAttrSmplAttrPrmsnDAOImpl extends SqlMapClientDaoSupport implements IAppCmpndAttrSmplAttrPrmsnDAO {

    /**
     * Creates a new DAO
     * @param sqlMapClient the xml file describing the SQL maps
     */
    public AppCmpndAttrSmplAttrPrmsnDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.setSqlMapClient(sqlMapClient);
    }

    public AppCmpndAttrSmplAttrPrmsnKey insert(AppCmpndAttrSmplAttrPrmsn record) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
    	verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");
    
        getSqlMapClientTemplate().insert("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.insert", record);
        AppCmpndAttrSmplAttrPrmsnKey key = new AppCmpndAttrSmplAttrPrmsnKey();
        key.setAppPrtflId(record.getKey().getAppPrtflId());
        key.setCmpndUserAttrId(record.getKey().getCmpndUserAttrId());
        key.setSmplUserAttrId(record.getKey().getSmplUserAttrId());
        return key; 
    }

    public AppCmpndAttrSmplAttrPrmsn selectByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getAppPrtflId(), "The key column APP_PRTFL_ID cannot be null..")
    	        .isNotNull(key.getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to select.");
    	AppCmpndAttrSmplAttrPrmsn record = (AppCmpndAttrSmplAttrPrmsn) getSqlMapClientTemplate().queryForObject("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.selectByPrimaryKey", key);
        return record;
    }

    public int deleteByPrimaryKey(AppCmpndAttrSmplAttrPrmsnKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to delete.");
    	verifier.isNotNull(key.getAppPrtflId(), "The key column APP_PRTFL_ID cannot be null..")
    	        .isNotNull(key.getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to delete.");
    	int rows = getSqlMapClientTemplate().delete("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.deleteByPrimaryKey", key);
        return rows;
    }
    
    /**
     * Select by primary key, selective.
     * @param key the table primary key object, first ordinal required, all others optional.
     * @return records a list of database records
     */
    @SuppressWarnings("unchecked")
	public List<AppCmpndAttrSmplAttrPrmsn> selectByPrimaryKeyDiscretionary(AppCmpndAttrSmplAttrPrmsnKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getAppPrtflId(), "The key column APP_PRTFL_ID cannot be null..").throwIfError("Unable to select.");
        List<AppCmpndAttrSmplAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.selectByPrimaryKeyDiscretionary", key);
    	return records;
    }
    
        
    @SuppressWarnings("unchecked") 
    public List<AppCmpndAttrSmplAttrPrmsn> findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId(BigDecimal appPrtflIdMin, BigDecimal appPrtflIdMax, String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin, Integer rownumMax) {     
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(appPrtflIdMin, "The appPrtflIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindAppCmpndAttrSmplAttrPrmsnByRangeByPrimaryKeyWhere where = new FindAppCmpndAttrSmplAttrPrmsnByRangeByPrimaryKeyWhere();
        where.setAppPrtflIdMin(appPrtflIdMin);
        where.setAppPrtflIdMax(appPrtflIdMax);
        where.setCmpndUserAttrIdMin(cmpndUserAttrIdMin);
        where.setCmpndUserAttrIdMax(cmpndUserAttrIdMax);
        where.setSmplUserAttrIdMin(smplUserAttrIdMin);
        where.setSmplUserAttrIdMax(smplUserAttrIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<AppCmpndAttrSmplAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.findRangeByAppPrtflIdByCmpndUserAttrIdBySmplUserAttrId", where);
        return records;
    }
    
    /**
     * Select by index IE1_APP_CMPND_ATTR_SMPL_ATTR_P, selective.
     * @param cmpndUserAttrId  the CMPND_USER_ATTR_ID column(s).
     *        First ordinal required, all others optional
     * @param smplUserAttrId  the SMPL_USER_ATTR_ID column(s).
     *        First ordinal required, all others optional
     * @return records  a list of database records
     */
    @SuppressWarnings("unchecked")
    public List<AppCmpndAttrSmplAttrPrmsn> selectByIe1AppCmpndAttrSmplAttrP( String cmpndUserAttrId , String smplUserAttrId ) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(cmpndUserAttrId, "The column CMPND_USER_ATTR_ID cannot be null.").throwIfError("Unable to select.");          
        AppCmpndAttrSmplAttrPrmsn index = new AppCmpndAttrSmplAttrPrmsn();
        AppCmpndAttrSmplAttrPrmsnKey indexKey = new AppCmpndAttrSmplAttrPrmsnKey();
        indexKey.setCmpndUserAttrId(cmpndUserAttrId);
        indexKey.setSmplUserAttrId(smplUserAttrId);
        index.setKey(indexKey);
        List<AppCmpndAttrSmplAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.selectByIe1AppCmpndAttrSmplAttrP", index);
        return records;
    }
     
     @SuppressWarnings("unchecked")
     public List<AppCmpndAttrSmplAttrPrmsn> findRangeByCmpndUserAttrIdBySmplUserAttrId(String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin, Integer rownumMax){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(cmpndUserAttrIdMin, "The cmpndUserAttrIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindAppCmpndAttrSmplAttrPrmsnByRangeByIe1AppCmpndAttrSmplAttrPWhere where = new FindAppCmpndAttrSmplAttrPrmsnByRangeByIe1AppCmpndAttrSmplAttrPWhere();
        where.setCmpndUserAttrIdMin(cmpndUserAttrIdMin);
        where.setCmpndUserAttrIdMax(cmpndUserAttrIdMax);
        where.setSmplUserAttrIdMin(smplUserAttrIdMin);
        where.setSmplUserAttrIdMax(smplUserAttrIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<AppCmpndAttrSmplAttrPrmsn> records = getSqlMapClientTemplate().queryForList("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.findRangeByCmpndUserAttrIdBySmplUserAttrId", where);
        return records;
    }
    
    @SuppressWarnings("unchecked")
    public List<Hashtable> findDistinctByCmpndUserAttrIdBySmplUserAttrId(String cmpndUserAttrId, String smplUserAttrId){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(cmpndUserAttrId, "The index column CMPND_USER_ATTR_ID cannot be null.")
                .throwIfError("Unable to process query.");
        
        AppCmpndAttrSmplAttrPrmsnKey indexKey = new AppCmpndAttrSmplAttrPrmsnKey();
        indexKey.setCmpndUserAttrId(cmpndUserAttrId);
        indexKey.setSmplUserAttrId(smplUserAttrId);
        
        List<Hashtable> records = getSqlMapClientTemplate().queryForList("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.findDistinctByCmpndUserAttrIdBySmplUserAttrId", indexKey);
        return records;
        
    }
    
    static class FindAppCmpndAttrSmplAttrPrmsnByRangeByPrimaryKeyWhere {
    
        private BigDecimal appPrtflIdMin;
        private BigDecimal appPrtflIdMax;
        private String cmpndUserAttrIdMin;
        private String cmpndUserAttrIdMax;
        private String smplUserAttrIdMin;
        private String smplUserAttrIdMax;
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
    
     
    static class FindAppCmpndAttrSmplAttrPrmsnByRangeByIe1AppCmpndAttrSmplAttrPWhere {
    
        private String cmpndUserAttrIdMin;
        private String cmpndUserAttrIdMax;
        private String smplUserAttrIdMin;
        private String smplUserAttrIdMax;
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