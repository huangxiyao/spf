package com.hp.it.cas.security.custom.dao;

import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnDAOImpl;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * A custom DAO that will delete from the APP_CMPND_ATTR_SMPL_ATTR_PRMSN table by the USER_ATTR_ID
 * 
 * @author Roger Spotts
 */
public class AppCmpndAttrSmplAttrPrmsnCustomDAOImpl extends AppCmpndAttrSmplAttrPrmsnDAOImpl implements
		IAppCmpndAttrSmplAttrPrmsnCustomDAO{

	/**
	 * Instantiate the DAO
	 * @param sqlMapClient
	 */
	public AppCmpndAttrSmplAttrPrmsnCustomDAOImpl(SqlMapClient sqlMapClient) {
		super(sqlMapClient);
        this.setSqlMapClient(sqlMapClient);
    }
	
	public int deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(String userAttrId) {
		int count = getSqlMapClientTemplate().delete("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId", userAttrId);
		return count;
	}

	public int deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key) {
		int count = getSqlMapClientTemplate().delete("APP_CMPND_ATTR_SMPL_ATTR_PRMSN.deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey", key);
		return count;
	}

}
