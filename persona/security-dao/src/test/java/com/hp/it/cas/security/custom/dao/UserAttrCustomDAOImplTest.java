package com.hp.it.cas.security.custom.dao;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.it.cas.config.dao.IAppDAO;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.UserAttr;

/**
 * A test class for the UserAttrCustomDAO.
 * 
 * @author Roger Spotts
 */
public class UserAttrCustomDAOImplTest extends AbstractTest {
    
    @Before
    public void populateData() {
        IAppDAO appDao = configDaoFactory.getAppDAO();
        appDao.insert(application(APPLICATION_1));
        
        IUserAttrDAO userAttrDao = securityDaoFactory.getUserAttrDAO();
        userAttrDao.insert(compoundAttribute(NAME));
        userAttrDao.insert(simpleAttribute(GIVEN_NAME));
        userAttrDao.insert(simpleAttribute(FAMILY_NAME));
        
        ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDao = securityDaoFactory.getCmpndAttrSmplAttrDAO();
        cmpndAttrSmplAttrDao.insert(compoundAttributeSimpleAttribute(NAME, GIVEN_NAME));
        cmpndAttrSmplAttrDao.insert(compoundAttributeSimpleAttribute(NAME, FAMILY_NAME));
    }
    
	/**
	 * Test FindCompoundAttributesForSimpleAttribute method
	 */
	@Test
	public void thatFindCompoundAttributesForSimpleAttributeCanOccurNormally() {
		IUserAttrCustomDAO dao = securityCustomDaoFactory.getUserAttrCustomDAO();
		List<UserAttr> records = dao.findCompoundAttributesForSimpleAttribute(GIVEN_NAME);
		Assert.assertEquals(1, records.size());
	}
	
	/**
	 * Test FindSimpleAttributesForCompoundAttribute method
	 */
	@Test
	public void thatFindSimpleAttributesForCompoundAttributeCanOccurNormally() {
		IUserAttrCustomDAO dao = securityCustomDaoFactory.getUserAttrCustomDAO();
		List<UserAttr> records = dao.findSimpleAttributesForCompoundAttribute(NAME);
		Assert.assertEquals(2, records.size());
	}
	
	/**
	 * Test SelectAllCompoundAttributes method
	 */
	@Test
	public void thatSelectAllCompoundAttributesCanOccurNormally() {
		IUserAttrCustomDAO dao = securityCustomDaoFactory.getUserAttrCustomDAO();
		List<UserAttr> records = dao.selectAllCompoundAttributes();
		Assert.assertEquals(1, records.size());
	}
	
	/**
	 * Test SelectAllSimpleAttributes method
	 */
	@Test
	public void thatSelectAllSimpleAttributesCanOccurNormally() {
		IUserAttrCustomDAO dao = securityCustomDaoFactory.getUserAttrCustomDAO();
		List<UserAttr> records = dao.selectAllSimpleAttributes();
		Assert.assertEquals(2, records.size());		
	}
	
	@After public void deleteApp() {
	    IAppDAO appDao = configDaoFactory.getAppDAO();
	    appDao.deleteByPrimaryKey(application(APPLICATION_1).getKey());
	}
}