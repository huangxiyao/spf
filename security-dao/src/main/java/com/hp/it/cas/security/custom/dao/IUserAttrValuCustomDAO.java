package com.hp.it.cas.security.custom.dao;

import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;

/**
 * A custom DAO class for the USER_ATTR_VALUE table
 * 
 * @author Roger Spotts
 */

public interface IUserAttrValuCustomDAO {
	
	/**
	 * Find the number of records in the USER_ATTR_VALU table
	 * where the CMPND_USER_ATTR_ID or the SMPL_USER_ATTR_ID equals
	 * the passed in value
	 * 
	 * @param userAttrId
	 * @return int the number of records found
	 */
	int countUserAttrValuWithUserAttrId(String userAttrId) ;

	/**
	 * Find the number of records in the USER_ATTR_VALU table
	 * where the CMPND_USER_ATTR_ID or the SMPL_USER_ATTR_ID equals
	 * the given key
	 * 
	 * @param key
	 * @return int  the number of records found
	 */
	int countUserAttrValuWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key);

}
