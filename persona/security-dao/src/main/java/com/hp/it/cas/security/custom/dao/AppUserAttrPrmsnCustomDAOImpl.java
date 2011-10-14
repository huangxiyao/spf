package com.hp.it.cas.security.custom.dao;

import com.hp.it.cas.security.dao.AppUserAttrPrmsnDAOImpl;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Roger Spotts
 */
public class AppUserAttrPrmsnCustomDAOImpl extends AppUserAttrPrmsnDAOImpl implements
		IAppUserAttrPrmsnCustomDAO{

	/**
	 * Instantiate the DAO
	 * @param sqlMapClient
	 */
	public AppUserAttrPrmsnCustomDAOImpl(SqlMapClient sqlMapClient) {
		super(sqlMapClient);
        this.setSqlMapClient(sqlMapClient);
    }
	
	public int deleteAppUserAttrPrmsnWithUserAttrId(String userAttrId) {
		int count = getSqlMapClientTemplate().delete("APP_USER_ATTR_PRMSN.deleteAppUserAttrPrmsnWithUserAttrId", userAttrId);
		return count;
	}

}
