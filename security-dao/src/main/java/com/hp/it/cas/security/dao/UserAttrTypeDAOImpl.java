package com.hp.it.cas.security.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.hp.it.cas.xa.validate.Verifier;

import java.util.List;

/**
 * A Data Access Object manages the connection with the data source.
 * This DAO provides methods for the USER_ATTR_TYPE table.
 *
 * @author CAS Generator v1.0.0
 */

public class UserAttrTypeDAOImpl extends SqlMapClientDaoSupport implements IUserAttrTypeDAO {

    /**
     * Creates a new DAO
     * @param sqlMapClient the xml file describing the SQL maps
     */
    public UserAttrTypeDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.setSqlMapClient(sqlMapClient);
    }

    public UserAttrTypeKey insert(UserAttrType record) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
    	verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");
    
        getSqlMapClientTemplate().insert("USER_ATTR_TYPE.insert", record);
        UserAttrTypeKey key = new UserAttrTypeKey();
        key.setUserAttrTypeCd(record.getKey().getUserAttrTypeCd());
        return key; 
    }

    public int updateByPrimaryKeySelective(UserAttrType record) {
        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey(), "The key cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey().getUserAttrTypeCd(), "The key column USER_ATTR_TYPE_CD cannot be null..")
    	        .throwIfError("Unable to update.");
        int rows = getSqlMapClientTemplate().update("USER_ATTR_TYPE.updateByPrimaryKeySelective", record);
        return rows;
    }

    public UserAttrType selectByPrimaryKey(UserAttrTypeKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getUserAttrTypeCd(), "The key column USER_ATTR_TYPE_CD cannot be null..")
    	        .throwIfError("Unable to select.");
    	UserAttrType record = (UserAttrType) getSqlMapClientTemplate().queryForObject("USER_ATTR_TYPE.selectByPrimaryKey", key);
        return record;
    }

    public int deleteByPrimaryKey(UserAttrTypeKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to delete.");
    	verifier.isNotNull(key.getUserAttrTypeCd(), "The key column USER_ATTR_TYPE_CD cannot be null..")
    	        .throwIfError("Unable to delete.");
    	int rows = getSqlMapClientTemplate().delete("USER_ATTR_TYPE.deleteByPrimaryKey", key);
        return rows;
    }
    
        
    @SuppressWarnings("unchecked") 
    public List<UserAttrType> findRangeByUserAttrTypeCd(String userAttrTypeCdMin, String userAttrTypeCdMax, Integer rownumMin, Integer rownumMax) {     
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userAttrTypeCdMin, "The userAttrTypeCdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindUserAttrTypeByRangeByPrimaryKeyWhere where = new FindUserAttrTypeByRangeByPrimaryKeyWhere();
        where.setUserAttrTypeCdMin(userAttrTypeCdMin);
        where.setUserAttrTypeCdMax(userAttrTypeCdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<UserAttrType> records = getSqlMapClientTemplate().queryForList("USER_ATTR_TYPE.findRangeByUserAttrTypeCd", where);
        return records;
    }
    
    
    static class FindUserAttrTypeByRangeByPrimaryKeyWhere {
    
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