package com.hp.it.cas.security.custom.dao;

/**
 * A custom DAO that will find the number of records for a USER_ATTR_ID in the 
 * CMPND_ATTR_SMPL_ATTR table.
 * 
 * @author Roger Spotts
 */
public interface ICmpndAttrSmplAttrCustomDAO {
	
	/**
	 * @param userAttrId  the value of the USER_ATTR_ID
	 * @return int  the number of records found
	 */
	int countCmpndAttrSmplAttrWithUserAttrId(String userAttrId) ;

}
