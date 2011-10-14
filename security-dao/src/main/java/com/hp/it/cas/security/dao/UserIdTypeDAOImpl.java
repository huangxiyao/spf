package com.hp.it.cas.security.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.hp.it.cas.xa.validate.Verifier;

import java.util.List;

/**
 * A Data Access Object manages the connection with the data source.
 * This DAO provides methods for the USER_ID_TYPE table.
 *
 * @author CAS Generator v1.0.0
 */

public class UserIdTypeDAOImpl extends SqlMapClientDaoSupport implements IUserIdTypeDAO {

    /**
     * Creates a new DAO
     * @param sqlMapClient the xml file describing the SQL maps
     */
    public UserIdTypeDAOImpl(SqlMapClient sqlMapClient) {
        super();
        this.setSqlMapClient(sqlMapClient);
    }

    public UserIdTypeKey insert(UserIdType record) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to insert.");
    	verifier.isNotNull(record.getKey(), "The record key cannot be null.").throwIfError("Unable to insert.");
    
        getSqlMapClientTemplate().insert("USER_ID_TYPE.insert", record);
        UserIdTypeKey key = new UserIdTypeKey();
        key.setUserIdTypeCd(record.getKey().getUserIdTypeCd());
        return key; 
    }

    public int updateByPrimaryKeySelective(UserIdType record) {
        Verifier verifier = new Verifier();
        verifier.isNotNull(record, "The record cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey(), "The key cannot be null.").throwIfError("Unable to update.");
    	verifier.isNotNull(record.getKey().getUserIdTypeCd(), "The key column USER_ID_TYPE_CD cannot be null..")
    	        .throwIfError("Unable to update.");
        int rows = getSqlMapClientTemplate().update("USER_ID_TYPE.updateByPrimaryKeySelective", record);
        return rows;
    }

    public UserIdType selectByPrimaryKey(UserIdTypeKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to select.");
    	verifier.isNotNull(key.getUserIdTypeCd(), "The key column USER_ID_TYPE_CD cannot be null..")
    	        .throwIfError("Unable to select.");
    	UserIdType record = (UserIdType) getSqlMapClientTemplate().queryForObject("USER_ID_TYPE.selectByPrimaryKey", key);
        return record;
    }

    public int deleteByPrimaryKey(UserIdTypeKey key) {
        Verifier verifier = new Verifier();
    	verifier.isNotNull(key, "The key cannot be null.").throwIfError("Unable to delete.");
    	verifier.isNotNull(key.getUserIdTypeCd(), "The key column USER_ID_TYPE_CD cannot be null..")
    	        .throwIfError("Unable to delete.");
    	int rows = getSqlMapClientTemplate().delete("USER_ID_TYPE.deleteByPrimaryKey", key);
        return rows;
    }
    
        
    @SuppressWarnings("unchecked") 
    public List<UserIdType> findRangeByUserIdTypeCd(String userIdTypeCdMin, String userIdTypeCdMax, Integer rownumMin, Integer rownumMax) {     
        
        Verifier verifier = new Verifier();
        verifier.isNotNull(userIdTypeCdMin, "The userIdTypeCdMin cannot be null")
                .isFalse((rownumMin == null && rownumMax != null), "You must include a starting row to use an ending row.")
                .throwIfError("Unable to process query.");
        
        FindUserIdTypeByRangeByPrimaryKeyWhere where = new FindUserIdTypeByRangeByPrimaryKeyWhere();
        where.setUserIdTypeCdMin(userIdTypeCdMin);
        where.setUserIdTypeCdMax(userIdTypeCdMax);
        where.setRownumMin(rownumMin);
        where.setRownumMax(rownumMax);
        
        List<UserIdType> records = getSqlMapClientTemplate().queryForList("USER_ID_TYPE.findRangeByUserIdTypeCd", where);
        return records;
    }
    
    
    static class FindUserIdTypeByRangeByPrimaryKeyWhere {
    
        private String userIdTypeCdMin;
        private String userIdTypeCdMax;
        private Integer rownumMin;
        private Integer rownumMax;
        
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