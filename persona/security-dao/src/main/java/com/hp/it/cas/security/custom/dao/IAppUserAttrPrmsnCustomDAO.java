package com.hp.it.cas.security.custom.dao;

/**
 * This custom DAO will delete records form the APP_USER_ATTR_PRMSN table by the
 * USER_ATTR_ID
 * 
 * @author Roger Spotts
 */
public interface IAppUserAttrPrmsnCustomDAO {
	
	/**
	 * @param userAttrId  the USER_ATTR_ID value
	 * @return int  the number of records deleted
	 */
	int deleteAppUserAttrPrmsnWithUserAttrId(String userAttrId) ;

}
