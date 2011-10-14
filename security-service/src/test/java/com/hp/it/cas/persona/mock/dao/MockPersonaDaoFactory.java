package com.hp.it.cas.persona.mock.dao;

import com.hp.it.cas.persona.dao.IPersonaDao;
import com.hp.it.cas.persona.dao.IPersonaDaoFactory;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrValuDAO;

public class MockPersonaDaoFactory implements IPersonaDaoFactory {

	private final IUserAttrValuDAO userAttrValuDao;
	private final IAppUserAttrPrmsnDAO appUserAttrPrmsnDao;
	private final IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao;
	private final IUserAttrDAO userAttrDao;
	private final ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao;
	private final IPersonaDao personaDao;
	
	public MockPersonaDaoFactory(MockDatabase database) {
		userAttrValuDao = new MockUserAttrValuDao(database);
		appUserAttrPrmsnDao = new MockAppUserAttrPrmsnDao(database);
		appCmpndAttrSmplAttrPrmsnDao = new MockAppCmpndAttrSmplAttrPrmsnDao(database);
		userAttrDao = new MockUserAttrDao(database);
		cmpndAttrSmplAttrDao = new MockCmpndAttrSmplAttrDao(database);
		personaDao = new MockPersonaDao(database);
	}
	
	public IAppUserAttrPrmsnDAO getAppUserAttrPrmsnDAO() {
		return appUserAttrPrmsnDao;
	}

	public IAppCmpndAttrSmplAttrPrmsnDAO getAppCmpndAttrSmplAttrPrmsnDAO() {
		return appCmpndAttrSmplAttrPrmsnDao;
	}

	public IUserAttrValuDAO getUserAttrValuDAO() {
		return userAttrValuDao;
	}

	public IUserAttrDAO getUserAttrDAO() {
		return userAttrDao;
	}

	public ICmpndAttrSmplAttrDAO getCmpndAttrSmplAttrDAO() {
		return cmpndAttrSmplAttrDao;
	}

	public IPersonaDao getPersonaDao() {
		return personaDao;
	}

}
