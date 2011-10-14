package com.hp.it.cas.security.custom.dao;

import com.hp.it.cas.security.dao.CmpndAttrSmplAttrDAOImpl;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Roger Spotts
 *
 */
public class CmpndAttrSmplAttrCustomDAOImpl extends CmpndAttrSmplAttrDAOImpl implements
		ICmpndAttrSmplAttrCustomDAO{

	/**
	 * Instantiate the DAO
	 * @param sqlMapClient
	 */
	public CmpndAttrSmplAttrCustomDAOImpl(SqlMapClient sqlMapClient) {
		super(sqlMapClient);
        this.setSqlMapClient(sqlMapClient);
    }
	
	public int countCmpndAttrSmplAttrWithUserAttrId(String userAttrId) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("CMPND_ATTR_SMPL_ATTR.countCmpndAttrSmplAttrWithUserAttrId", userAttrId);
		return count.intValue();
	}

}
