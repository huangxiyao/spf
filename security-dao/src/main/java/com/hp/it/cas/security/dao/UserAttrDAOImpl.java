package com.hp.it.cas.security.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.hp.it.cas.xa.validate.Verifier;

import java.util.List;

/**
 * A Data Access Object manages the connection with the data source.
 * This DAO provides methods for the USER_ATTR table.
 *
 * @author CAS Generator v1.0.0
 */

public class UserAttrDAOImpl extends SqlMapClientDaoSupport implements IUserAttrDAO {

    /**
     * Creates a new DAO
     * @param sqlMapClient the xml file describing the SQL maps
     */
    public UserAttrDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.setSqlMapClient(sqlMapClient);
    }

    public UserAttrKey insert(UserAttr record) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
    	verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");
    
        getSqlMapClientTemplate().insert("USER_ATTR.insert", record);
        UserAttrKey key = new UserAttrKey();
        key.setUserAttrId(record.getKey().getUserAttrId());
        return key; 
    }

    public int updateByPrimaryKeySelective(UserAttr record) {
        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey(), "The key cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey().getUserAttrId(), "The key column USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to update.");
        int rows = getSqlMapClientTemplate().update("USER_ATTR.updateByPrimaryKeySelective", record);
        return rows;
    }

    public UserAttr selectByPrimaryKey(UserAttrKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getUserAttrId(), "The key column USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to select.");
    	UserAttr record = (UserAttr) getSqlMapClientTemplate().queryForObject("USER_ATTR.selectByPrimaryKey", key);
        return record;
    }

    public int deleteByPrimaryKey(UserAttrKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to delete.");
    	verifier.isNotNull(key.getUserAttrId(), "The key column USER_ATTR_ID cannot be null..")
    	        .throwIfError("Unable to delete.");
    	int rows = getSqlMapClientTemplate().delete("USER_ATTR.deleteByPrimaryKey", key);
        return rows;
    }
    
        
    @SuppressWarnings("unchecked") 
    public List<UserAttr> findRangeByUserAttrId(String userAttrIdMin, String userAttrIdMax, Integer rownumMin, Integer rownumMax) {     
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrIdMin, "The userAttrIdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindUserAttrByRangeByPrimaryKeyWhere where = new FindUserAttrByRangeByPrimaryKeyWhere();
        where.setUserAttrIdMin(userAttrIdMin);
        where.setUserAttrIdMax(userAttrIdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.findRangeByUserAttrId", where);
        return records;
    }
    
    /**
     * Select by index AK1_USER_ATTR, selective.
     * @param userAttrNm  the USER_ATTR_NM column(s).
     *        First ordinal required, all others optional
     * @return record  a database records      
     */
    public UserAttr selectByAk1UserAttr( String userAttrNm ) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(userAttrNm, "The column USER_ATTR_NM cannot be null.").throwIfError("Unable to select.");          
        UserAttr index = new UserAttr();
        index.setUserAttrNm(userAttrNm);
        UserAttr record = (UserAttr) getSqlMapClientTemplate().queryForObject("USER_ATTR.selectByAk1UserAttr", index);
        return record;   
    }
     
     @SuppressWarnings("unchecked")
     public List<UserAttr> findRangeByUserAttrNm(String userAttrNmMin, String userAttrNmMax, Integer rownumMin, Integer rownumMax){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrNmMin, "The userAttrNmMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindUserAttrByRangeByAk1UserAttrWhere where = new FindUserAttrByRangeByAk1UserAttrWhere();
        where.setUserAttrNmMin(userAttrNmMin);
        where.setUserAttrNmMax(userAttrNmMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.findRangeByUserAttrNm", where);
        return records;
    }
    
    /**
     * Select by index IE1_USER_ATTR, selective.
     * @param userAttrTypeCd  the USER_ATTR_TYPE_CD column(s).
     *        First ordinal required, all others optional
     * @return records  a list of database records
     */
    @SuppressWarnings("unchecked")
    public List<UserAttr> selectByIe1UserAttr( String userAttrTypeCd ) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(userAttrTypeCd, "The column USER_ATTR_TYPE_CD cannot be null.").throwIfError("Unable to select.");          
        UserAttr index = new UserAttr();
        index.setUserAttrTypeCd(userAttrTypeCd);
        List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.selectByIe1UserAttr", index);
        return records;
    }
     
     @SuppressWarnings("unchecked")
     public List<UserAttr> findRangeByUserAttrTypeCd(String userAttrTypeCdMin, String userAttrTypeCdMax, Integer rownumMin, Integer rownumMax){         
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrTypeCdMin, "The userAttrTypeCdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindUserAttrByRangeByIe1UserAttrWhere where = new FindUserAttrByRangeByIe1UserAttrWhere();
        where.setUserAttrTypeCdMin(userAttrTypeCdMin);
        where.setUserAttrTypeCdMax(userAttrTypeCdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.findRangeByUserAttrTypeCd", where);
        return records;
    }
    
    
    static class FindUserAttrByRangeByPrimaryKeyWhere {
    
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
    
     
    static class FindUserAttrByRangeByAk1UserAttrWhere {
    
        private String userAttrNmMin;
        private String userAttrNmMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
        public String getUserAttrNmMin(){
            return this.userAttrNmMin;
        }
    
        public String getUserAttrNmMax(){
            return this.userAttrNmMax;
        }
    
        public void setUserAttrNmMin(String userAttrNmMin){
            this.userAttrNmMin = userAttrNmMin;
        }
        
        public void setUserAttrNmMax(String userAttrNmMax){
            this.userAttrNmMax = userAttrNmMax;
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
    
    static class FindUserAttrByRangeByIe1UserAttrWhere {
    
        private String userAttrTypeCdMin;
        private String userAttrTypeCdMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
        public String getUserAttrTypeCdMin(){
            return this.userAttrTypeCdMin;
        }
    
        public String getUserAttrTypeCdMax(){
            return this.userAttrTypeCdMax;
        }
    
        public void setUserAttrTypeCdMin(String userAttrTypeCdMin){
            this.userAttrTypeCdMin = userAttrTypeCdMin;
        }
        
        public void setUserAttrTypeCdMax(String userAttrTypeCdMax){
            this.userAttrTypeCdMax = userAttrTypeCdMax;
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