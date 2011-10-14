package com.hp.it.cas.security.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.hp.it.cas.xa.validate.Verifier;

import java.util.List;
import java.util.Hashtable;

/**
 * A Data Access Object manages the connection with the data source.
 * This DAO provides methods for the CMPND_ATTR_SMPL_ATTR table.
 *
 * @author CAS Generator v1.0.0
 */

public class CmpndAttrSmplAttrDAOImpl extends SqlMapClientDaoSupport implements ICmpndAttrSmplAttrDAO {

    /**
     * Creates a new DAO
     * @param sqlMapClient the xml file describing the SQL maps
     */
    public CmpndAttrSmplAttrDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.setSqlMapClient(sqlMapClient);
    }

    public CmpndAttrSmplAttrKey insert(CmpndAttrSmplAttr record) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
    	verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");
    
        getSqlMapClientTemplate().insert("CMPND_ATTR_SMPL_ATTR.insert", record);
        CmpndAttrSmplAttrKey key = new CmpndAttrSmplAttrKey();
        key.setCmpndUserAttrId(record.getKey().getCmpndUserAttrId());
        key.setSmplUserAttrId(record.getKey().getSmplUserAttrId());
        return key; 
    }

    public CmpndAttrSmplAttr selectByPrimaryKey(CmpndAttrSmplAttrKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to select.");
    	CmpndAttrSmplAttr record = (CmpndAttrSmplAttr) getSqlMapClientTemplate().queryForObject("CMPND_ATTR_SMPL_ATTR.selectByPrimaryKey", key);
        return record;
    }

    public int deleteByPrimaryKey(CmpndAttrSmplAttrKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to delete.");
    	verifier.isNotNull(key.getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..")
    	        .isNotNull(key.getSmplUserAttrId(), "The key column SMPL_USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to delete.");
    	int rows = getSqlMapClientTemplate().delete("CMPND_ATTR_SMPL_ATTR.deleteByPrimaryKey", key);
        return rows;
    }
    
    /**
     * Select by primary key, selective.
     * @param key the table primary key object, first ordinal required, all others optional.
     * @return records a list of database records
     */
    @SuppressWarnings("unchecked")
	public List<CmpndAttrSmplAttr> selectByPrimaryKeyDiscretionary(CmpndAttrSmplAttrKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getCmpndUserAttrId(), "The key column CMPND_USER_ATTR_ID cannot be null..").throwIfError("Unable to select.");
        List<CmpndAttrSmplAttr> records = getSqlMapClientTemplate().queryForList("CMPND_ATTR_SMPL_ATTR.selectByPrimaryKeyDiscretionary", key);
    	return records;
    }
    
        
    @SuppressWarnings("unchecked") 
    public List<CmpndAttrSmplAttr> findRangeByCmpndUserAttrIdBySmplUserAttrId(String cmpndUserAttrIdMin, String cmpndUserAttrIdMax, String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin, Integer rownumMax) {     
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(cmpndUserAttrIdMin, "The cmpndUserAttrIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindCmpndAttrSmplAttrByRangeByPrimaryKeyWhere where = new FindCmpndAttrSmplAttrByRangeByPrimaryKeyWhere();
        where.setCmpndUserAttrIdMin(cmpndUserAttrIdMin);
        where.setCmpndUserAttrIdMax(cmpndUserAttrIdMax);
        where.setSmplUserAttrIdMin(smplUserAttrIdMin);
        where.setSmplUserAttrIdMax(smplUserAttrIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<CmpndAttrSmplAttr> records = getSqlMapClientTemplate().queryForList("CMPND_ATTR_SMPL_ATTR.findRangeByCmpndUserAttrIdBySmplUserAttrId", where);
        return records;
    }
    
    /**
     * Select by index IE1_CMPND_ATTR_SMPL_ATTR, selective.
     * @param smplUserAttrId  the SMPL_USER_ATTR_ID column(s).
     *        First ordinal required, all others optional
     * @return records  a list of database records
     */
    @SuppressWarnings("unchecked")
    public List<CmpndAttrSmplAttr> selectByIe1CmpndAttrSmplAttr( String smplUserAttrId ) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(smplUserAttrId, "The column SMPL_USER_ATTR_ID cannot be null.").throwIfError("Unable to select.");          
        CmpndAttrSmplAttr index = new CmpndAttrSmplAttr();
        CmpndAttrSmplAttrKey indexKey = new CmpndAttrSmplAttrKey();
        indexKey.setSmplUserAttrId(smplUserAttrId);
        index.setKey(indexKey);
        List<CmpndAttrSmplAttr> records = getSqlMapClientTemplate().queryForList("CMPND_ATTR_SMPL_ATTR.selectByIe1CmpndAttrSmplAttr", index);
        return records;
    }
     
     @SuppressWarnings("unchecked")
     public List<CmpndAttrSmplAttr> findRangeBySmplUserAttrId(String smplUserAttrIdMin, String smplUserAttrIdMax, Integer rownumMin, Integer rownumMax){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(smplUserAttrIdMin, "The smplUserAttrIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindCmpndAttrSmplAttrByRangeByIe1CmpndAttrSmplAttrWhere where = new FindCmpndAttrSmplAttrByRangeByIe1CmpndAttrSmplAttrWhere();
        where.setSmplUserAttrIdMin(smplUserAttrIdMin);
        where.setSmplUserAttrIdMax(smplUserAttrIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<CmpndAttrSmplAttr> records = getSqlMapClientTemplate().queryForList("CMPND_ATTR_SMPL_ATTR.findRangeBySmplUserAttrId", where);
        return records;
    }
    
    @SuppressWarnings("unchecked")
    public List<Hashtable> findDistinctBySmplUserAttrId(String smplUserAttrId){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(smplUserAttrId, "The index column SMPL_USER_ATTR_ID cannot be null.")
                .throwIfError("Unable to process query.");
        
        CmpndAttrSmplAttrKey indexKey = new CmpndAttrSmplAttrKey();
        indexKey.setSmplUserAttrId(smplUserAttrId);
        
        List<Hashtable> records = getSqlMapClientTemplate().queryForList("CMPND_ATTR_SMPL_ATTR.findDistinctBySmplUserAttrId", indexKey);
        return records;
        
    }
    
    static class FindCmpndAttrSmplAttrByRangeByPrimaryKeyWhere {
    
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
    
     
    static class FindCmpndAttrSmplAttrByRangeByIe1CmpndAttrSmplAttrWhere {
    
        private String smplUserAttrIdMin;
        private String smplUserAttrIdMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
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