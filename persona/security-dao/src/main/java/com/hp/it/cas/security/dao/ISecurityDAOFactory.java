package com.hp.it.cas.security.dao;

/**
 * An interface for the DAO factory.
 *
 * @author CAS Generator v1.0.0
 */
public interface ISecurityDAOFactory {
	
	/**
	 * @return appCmpndAttrSmplAttrPrmsnDAO
	 */
	IAppCmpndAttrSmplAttrPrmsnDAO getAppCmpndAttrSmplAttrPrmsnDAO();
	
	/**
	 * @return appUserAttrPrmsnDAO
	 */
	IAppUserAttrPrmsnDAO getAppUserAttrPrmsnDAO();
	
	/**
	 * @return cmpndAttrSmplAttrDAO
	 */
	ICmpndAttrSmplAttrDAO getCmpndAttrSmplAttrDAO();
	
	/**
	 * @return userAttrDAO
	 */
	IUserAttrDAO getUserAttrDAO();
	
	/**
	 * @return userAttrTypeDAO
	 */
	IUserAttrTypeDAO getUserAttrTypeDAO();
	
	/**
	 * @return userAttrValuDAO
	 */
	IUserAttrValuDAO getUserAttrValuDAO();
	
	/**
	 * @return userIdTypeDAO
	 */
	IUserIdTypeDAO getUserIdTypeDAO();
	
}
