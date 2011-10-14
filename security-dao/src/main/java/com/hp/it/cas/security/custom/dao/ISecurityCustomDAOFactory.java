package com.hp.it.cas.security.custom.dao;

/**
 * A DAO Factory class that provides a single point of access for all the DAOs
 * in the security custom DAO environment.
 * 
 * @author Roger Spotts
 *
 */
public interface ISecurityCustomDAOFactory {

	/**
	 * @return userAttrCustomDAO
	 */
	IUserAttrCustomDAO getUserAttrCustomDAO();
	
	/**
	 * @return cmpndAttSmlpAttrCustomDAO
	 */
	ICmpndAttrSmplAttrCustomDAO getCmpndAttrSmplAttrCustomDAO();

	/**
	 * @return userAttrValuCustomDAO
	 */
	IUserAttrValuCustomDAO getUserAttrValuCustomDAO();
	
	/**
	 * @return appUserAttrPrmsnCustomDAO
	 */
	IAppUserAttrPrmsnCustomDAO getAppUserAttrPrmsnCustomDAO();
	
	/**
	 * @return appCmpndAttrSmplAttrCustomDAO
	 */
	IAppCmpndAttrSmplAttrPrmsnCustomDAO getAppCmpndAttrSmplAttrPrmsnCustomDAO();
	
}
