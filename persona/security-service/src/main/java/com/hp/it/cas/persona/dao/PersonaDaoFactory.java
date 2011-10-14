package com.hp.it.cas.persona.dao;

import com.hp.it.cas.security.custom.dao.ISecurityCustomDAOFactory;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrValuDAO;

/**
 * A DAO factory that hides a number of other DAO factories in order to isolate code from DAO refactorings.
 *
 * @author Quintin May
 */
public class PersonaDaoFactory implements IPersonaDaoFactory {

	private final ISecurityDAOFactory securityDaoFactory;
	private final IPersonaDao personaDao;
	
	/**
	 * Creats the DAO factory.
	 * @param securityDaoFactory the DAO factory for accessing user attributes.
	 * @param securityCustomDaoFactory the DAO factory for custom user attribute access.
	 */
	public PersonaDaoFactory(ISecurityDAOFactory securityDaoFactory, ISecurityCustomDAOFactory securityCustomDaoFactory) {
		this.securityDaoFactory = securityDaoFactory;
		this.personaDao = new PersonaDao(securityCustomDaoFactory);
	}

	public IPersonaDao getPersonaDao() {
		return personaDao;
	}

	public IUserAttrDAO getUserAttrDAO() {
		return securityDaoFactory.getUserAttrDAO();
	}

	public ICmpndAttrSmplAttrDAO getCmpndAttrSmplAttrDAO() {
		return securityDaoFactory.getCmpndAttrSmplAttrDAO();
	}

	public IAppUserAttrPrmsnDAO getAppUserAttrPrmsnDAO() {
		return securityDaoFactory.getAppUserAttrPrmsnDAO();
	}

	public IAppCmpndAttrSmplAttrPrmsnDAO getAppCmpndAttrSmplAttrPrmsnDAO() {
		return securityDaoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
	}

	public IUserAttrValuDAO getUserAttrValuDAO() {
		return securityDaoFactory.getUserAttrValuDAO();
	}
}
