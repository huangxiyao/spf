package com.hp.it.cas.security.custom.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.it.cas.config.dao.IAppDAO;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrValuDAO;

/**
 * A test class for the UserAttrValuCustomDAO.  The test assumes
 * a database with specific data.
 * 
 * @author Roger Spotts
 */
public class UserAttrValuCustomDAOImplTest extends AbstractTest {
    
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
        
        IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDao = securityDaoFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
        appCmpndAttrSmplAttrPrmsnDao.insert(permission(APPLICATION_1, NAME, GIVEN_NAME));
        appCmpndAttrSmplAttrPrmsnDao.insert(permission(APPLICATION_1, NAME, FAMILY_NAME));
        
        IUserAttrValuDAO userAttrValuDao = securityDaoFactory.getUserAttrValuDAO();
        userAttrValuDao.insert(value(NAME, GIVEN_NAME, "Linus"));
        userAttrValuDao.insert(value(NAME, FAMILY_NAME, "van Pelt"));
    }

    /**
	 * Test CountCmpndAttrSmplAttrWithUserAttrKy method
     *
	 */
	// Commented to prevent hudson build failures
	@Test
	public void thatCountUserAttrValuWithUserAttrKyCanOccurNormally() {
		
		// This test assumes data in the database to match the query.  Change the key to match the data.
		IUserAttrValuCustomDAO dao = securityCustomDaoFactory.getUserAttrValuCustomDAO();
		Integer count1 = dao.countUserAttrValuWithUserAttrId(NAME);
		Assert.assertEquals(2, count1.intValue());
		Integer count2 = dao.countUserAttrValuWithUserAttrId(FAMILY_NAME);
		Assert.assertEquals(1, count2.intValue());
	}
	
	@After public void deleteApp() {
	    IAppDAO appDao = configDaoFactory.getAppDAO();
	    appDao.deleteByPrimaryKey(application(APPLICATION_1).getKey());
	}
	
}