package com.hp.it.cas.persona.dao;

import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrValuDAO;

/**
 * A DAO factory that provides Persona specific DAOs.
 *
 * @author Quintin May
 */
public interface IPersonaDaoFactory {

	/**
	 * Returns a Persona DAO.
	 * @return a Persona DAO.
	 */
	IPersonaDao getPersonaDao();

	/**
	 * Returns a user attribute DAO.
	 * @return a user attribute DAO.
	 */
	IUserAttrDAO getUserAttrDAO();

	/**
	 * Returns a compound/simple attribute DAO.
	 * @return a compound/simple attribute DAO.
	 */
	ICmpndAttrSmplAttrDAO getCmpndAttrSmplAttrDAO();

	/**
	 * Returns a user attribute permission DAO.
	 * @return a user attribute permission DAO.
	 */
	IAppUserAttrPrmsnDAO getAppUserAttrPrmsnDAO();

	/**
	 * Returns a compound/simple attribute permission DAO.
	 * @return a compound/simple attribute permission DAO.
	 */
	IAppCmpndAttrSmplAttrPrmsnDAO getAppCmpndAttrSmplAttrPrmsnDAO();

	/**
	 * Returns a user/attribute/value DAO.
	 * @return a user/attribute/value DAO.
	 */
	IUserAttrValuDAO getUserAttrValuDAO();
}
