package com.hp.it.cas.security.custom.dao;

import java.util.List;

import com.hp.it.cas.security.dao.UserAttr;

/**
 * A custom DAO for finding records in the USER_ATTR table.
 * 
 * @author Roger Spotts
 *
 */
public interface IUserAttrCustomDAO {
	
	/**
	 * 
	 * @param simpleUserAttributeIdentifier  the value of the SMPL_USER_ATTR_ID
	 * @return list  a set of records
	 */
	List<UserAttr> findCompoundAttributesForSimpleAttribute(String simpleUserAttributeIdentifier);

	/**
	 * Find all records in the USER_ATTR table where the
	 * USER_ATTR.USER_ATTR_ID equals CMPND_ATTR_SMPL_ATTR.SMPL_USER_ATTR_ID
	 * for the given CMPND_USER_ATTR_ID
	 * 
	 * @param compoundUserAttributeIdentifier
	 * @return list  a set of records
	 */
	List<UserAttr> findSimpleAttributesForCompoundAttribute(String compoundUserAttributeIdentifier);

	/**
	 * Find all records in the USER_ATTR table where the 
	 * USER_ATTR_TYPE_CD = 'SMPL'
	 * 
	 * @return list  a set if records
	 */
	List<UserAttr> selectAllSimpleAttributes();
	
	/**
	 *Find all records in the USER_ATTR table where the 
	 * USER_ATTR_TYPE_CD = 'CMPND'
	 * 
	 * @return list  a set of records
	 */
	List<UserAttr> selectAllCompoundAttributes();
	
}
