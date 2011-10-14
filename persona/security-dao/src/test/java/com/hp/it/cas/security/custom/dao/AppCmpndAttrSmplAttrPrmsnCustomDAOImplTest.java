package com.hp.it.cas.security.custom.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.it.cas.config.dao.IAppDAO;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;

/**
 * A test class for the AppCmpndAttrSmplAttrCustomDAO.
 * 
 * @author Roger Spotts
 */
public class AppCmpndAttrSmplAttrPrmsnCustomDAOImplTest extends AbstractTest {
    
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
        
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDAO = securityDaoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        appCmpndAttrSmplAttrPrmsnDAO.insert(permission(APPLICATION_1, NAME, GIVEN_NAME));
        appCmpndAttrSmplAttrPrmsnDAO.insert(permission(APPLICATION_1, NAME, FAMILY_NAME));
    }

    /**
	 * Test CountCmpndAttrSmplAttrWithUserAttrKy method
     *
	 */
	@Test
	public void thatDeleteAppCmpndAttrSmplAttrPrmsnWithUserAttrKyCanOccurNormallyCompound() {
		IAppCmpndAttrSmplAttrPrmsnCustomDAO dao = securityCustomDaoFactory.getAppCmpndAttrSmplAttrPrmsnCustomDAO();
		int count1 = dao.deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(NAME);
		Assert.assertEquals(2, count1);
	}
	
    /**
     * Test CountCmpndAttrSmplAttrWithUserAttrKy method
     *
     */
    @Test
    public void thatDeleteAppCmpndAttrSmplAttrPrmsnWithUserAttrKyCanOccurNormallySimple() {
        IAppCmpndAttrSmplAttrPrmsnCustomDAO dao = securityCustomDaoFactory.getAppCmpndAttrSmplAttrPrmsnCustomDAO();
        int count2 = dao.deleteAppCmpndAttrSmplAttrPrmsnWithUserAttrId(FAMILY_NAME);
        Assert.assertEquals(1, count2);
    }
    
    @After public void deleteApp() {
	IAppDAO appDao = configDaoFactory.getAppDAO();
	appDao.deleteByPrimaryKey(application(APPLICATION_1).getKey());
    }
}