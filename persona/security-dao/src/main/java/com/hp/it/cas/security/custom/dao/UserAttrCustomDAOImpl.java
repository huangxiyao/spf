package com.hp.it.cas.security.custom.dao;

import java.util.List;

import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrDAOImpl;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Roger Spotts
 *
 */
public class UserAttrCustomDAOImpl extends UserAttrDAOImpl implements IUserAttrCustomDAO {

	/**
	 * Load the SqlMapClient
	 * @param sqlMapClient
	 */
	public UserAttrCustomDAOImpl(SqlMapClient sqlMapClient) {
		super(sqlMapClient);
        this.setSqlMapClient(sqlMapClient);
    }
	
	@SuppressWarnings("unchecked")
	public List<UserAttr> findCompoundAttributesForSimpleAttribute(String simpleUserAttributeIdentifier) {
		List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.findCompoundAttributesForSimpleAttribute", simpleUserAttributeIdentifier);
		return records;
	}

	@SuppressWarnings("unchecked")
	public List<UserAttr> findSimpleAttributesForCompoundAttribute(String compoundUserAttributeIdentifier) {
		List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.findSimpleAttributesForCompoundAttribute", compoundUserAttributeIdentifier);
		return records;
	}

	@SuppressWarnings("unchecked")
	public List<UserAttr> selectAllCompoundAttributes() {
		List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.selectAllCompoundAttributes");
		return records;
	}

	@SuppressWarnings("unchecked")
	public List<UserAttr> selectAllSimpleAttributes() {
		List<UserAttr> records = getSqlMapClientTemplate().queryForList("USER_ATTR.selectAllSimpleAttributes");
		return records;
	}

}
