package com.hp.it.cas.security.custom.dao;

import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;

/**
 * The interface to the custom DAO that will delete from the APP_CMPND_ATTR_SMPL_ATTR_PRMSN 
 * table by the USER_ATTR_ID
 * 
 * @author Roger Spotts
 */
public interface IAppCmpndAttrSmplAttrPrmsnCustomDAO {
	
	/**
	 * @param userAttrId the USER_ATTR_ID value
	 * @return int  the number of records deleted
	 */
	int deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(String userAttrId) ;

	/**
	 * @param key
	 * @return int  the numnber of records deleted
	 */
	int deleteAppCmpndAttrSmplAttrPrmsnWithCmpndAttrSmplAttrKey(CmpndAttrSmplAttrKey key);

}
