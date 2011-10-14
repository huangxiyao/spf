package com.hp.it.cas.security.custom.dao;

import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.UserAttrValuDAOImpl;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Roger Spotts
 *
 */
public class UserAttrValuCustomDAOImpl extends UserAttrValuDAOImpl implements
		IUserAttrValuCustomDAO{

	/**
	 * Load the SqlMapClient
	 * @param sqlMapClient
	 */
	public UserAttrValuCustomDAOImpl(SqlMapClient sqlMapClient) {
		super(sqlMapClient);
        this.setSqlMapClient(sqlMapClient);
    }
	
	public int countUserAttrValuWithUserAttrId(String userAttrId) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("USER_ATTR_VALU.countUserAttrValuWithUserAttrId", userAttrId);
		return count.intValue();
	}

	public int countUserAttrValuWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("USER_ATTR_VALU.countUserAttrValuWithCmpndAttrSmplAttrKey", key);
		return count.intValue();
	}

}
