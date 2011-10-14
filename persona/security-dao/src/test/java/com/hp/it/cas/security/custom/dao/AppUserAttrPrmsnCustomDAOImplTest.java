package com.hp.it.cas.security.custom.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.it.cas.config.dao.IAppDAO;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;

/**
 * A test class for the AppUserAttrPrmsnCustomDAO.
 * 
 * @author Roger Spotts
 */
public class AppUserAttrPrmsnCustomDAOImplTest extends AbstractTest {
    
    @Before
    public void populateData() {
        IAppDAO appDao = configDaoFactory.getAppDAO();
        appDao.insert(application(APPLICATION_1));
        appDao.insert(application(APPLICATION_2));
        
        IUserAttrDAO userAttrDao = securityDaoFactory.getUserAttrDAO();
        userAttrDao.insert(compoundAttribute(NAME));
        userAttrDao.insert(simpleAttribute(FAMILY_NAME));
        
        IAppUserAttrPrmsnDAO appUserAttrPrmsnDao = securityDaoFactory.getAppUserAttrPrmsnDAO();
        appUserAttrPrmsnDao.insert(permission(APPLICATION_1, NAME));
        appUserAttrPrmsnDao.insert(permission(APPLICATION_2, NAME));
        appUserAttrPrmsnDao.insert(permission(APPLICATION_2, FAMILY_NAME));
    }
	
    /**
	 * Test CountCmpndAttrSmplAttrWithUserAttrKy method
     *
	 */
	@Test
	public void thatDeleteAppUserAttrPrmsnWithUserAttrIdCanOccurNormally() {
		IAppUserAttrPrmsnCustomDAO dao = securityCustomDaoFactory.getAppUserAttrPrmsnCustomDAO();
		int count1 = dao.deleteAppUserAttrPrmsnWithUserAttrId(NAME);
		Assert.assertEquals(2, count1);
		int count2 = dao.deleteAppUserAttrPrmsnWithUserAttrId(FAMILY_NAME);
		Assert.assertEquals(1, count2);
	}
	
	@After public void deleteApp() {
	    IAppDAO appDao = configDaoFactory.getAppDAO();
	    appDao.deleteByPrimaryKey(application(APPLICATION_1).getKey());
	    appDao.deleteByPrimaryKey(application(APPLICATION_2).getKey());
	}
	
}