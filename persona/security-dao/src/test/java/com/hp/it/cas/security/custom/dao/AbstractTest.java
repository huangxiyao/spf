package com.hp.it.cas.security.custom.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.hp.it.cas.config.dao.App;
import com.hp.it.cas.config.dao.AppKey;
import com.hp.it.cas.config.dao.IConfigDAOFactory;
import com.hp.it.cas.config.dao.mock.ConfigDAOFactoryImpl;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsn;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;
import com.hp.it.cas.security.dao.AppUserAttrPrmsn;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttr;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
import com.hp.it.cas.security.dao.UserAttrValu;
import com.hp.it.cas.security.dao.UserAttrValuKey;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datasource-config.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractTest {
    
    protected static final int APPLICATION_1 = 999998;
    protected static final int APPLICATION_2 = 999999;
    
    protected static final String NAME = "TEST_NM";
    protected static final String GIVEN_NAME = "TEST_GVN_NM";
    protected static final String FAMILY_NAME = "TEST_FMLY_NM";
    protected static final String OCCUPATION = "TEST_OCCPTN";
    
    private Mockery context = new JUnit4Mockery();
    
    @Autowired
	protected ISecurityCustomDAOFactory securityCustomDaoFactory;
    
    @Autowired
    protected ISecurityDAOFactory securityDaoFactory;
    
    protected IConfigDAOFactory configDaoFactory;
    
    public AbstractTest(){
    	configDaoFactory = new ConfigDAOFactoryImpl(context);
    }
    
    protected App application(int applicationPortfolioIdentifier) {
        AppKey appKey = new AppKey();
        appKey.setAppPrtflId(new BigDecimal(applicationPortfolioIdentifier));
        
        App app = new App();
        app.setKey(appKey);
        app.setAppAliasNm("alias");
        app.setAppCiId("ci");
        app.setAppDn("dn");
        app.setCrtTs(new Date());
        app.setCrtUserId(getClass().getSimpleName());
        app.setLastMaintTs(new Date());
        app.setLastMaintUserId(getClass().getSimpleName());
        app.setSrcSysCrtTs(new Date());
        app.setSrcSysCrtUserId(getClass().getSimpleName());
        app.setSrcSysMaintTs(new Date());
        app.setSrcSysMaintUserId(getClass().getSimpleName());

        return app;
    }
    
    protected UserAttr simpleAttribute(String userAttrId) {
        return defineAttribute(userAttrId, "SMPL");
    }
    
    protected UserAttr compoundAttribute(String userAttrId) {
        return defineAttribute(userAttrId, "CMPND");
    }
    
    protected CmpndAttrSmplAttr compoundAttributeSimpleAttribute(String cmpndUserAttrId, String smplUserAttrId) {
        CmpndAttrSmplAttrKey cmpndAttrSmplAttrKey = new CmpndAttrSmplAttrKey();
        cmpndAttrSmplAttrKey.setCmpndUserAttrId(cmpndUserAttrId);
        cmpndAttrSmplAttrKey.setSmplUserAttrId(smplUserAttrId);
        
        CmpndAttrSmplAttr cmpndAttrSmplAttr = new CmpndAttrSmplAttr();
        cmpndAttrSmplAttr.setKey(cmpndAttrSmplAttrKey);
        cmpndAttrSmplAttr.setCrtTs(new Date());
        cmpndAttrSmplAttr.setCrtUserId(getClass().getSimpleName());
        
        return cmpndAttrSmplAttr;
    }
    
    private UserAttr defineAttribute(String userAttrId, String userAttrTypeCd) {
        UserAttrKey userAttrKey = new UserAttrKey();
        userAttrKey.setUserAttrId(userAttrId);

        UserAttr userAttr = new UserAttr();
        userAttr.setKey(userAttrKey);
        userAttr.setCrtTs(new Date());
        userAttr.setLastMaintTs(new Date());
        userAttr.setCrtUserId(getClass().getSimpleName());
        userAttr.setLastMaintUserId(getClass().getSimpleName());
        userAttr.setUserAttrNm(userAttrId);
        userAttr.setUserAttrDn(userAttrId);
        userAttr.setUserAttrDefnTx(userAttrId);
        userAttr.setUserAttrTypeCd(userAttrTypeCd);
        
        return userAttr;
    }
    
    protected AppUserAttrPrmsn permission(int appPrtflId, String userAttrId) {
        AppUserAttrPrmsnKey appUserAttrPrmsnKey = new AppUserAttrPrmsnKey();
        appUserAttrPrmsnKey.setAppPrtflId(new BigDecimal(appPrtflId));
        appUserAttrPrmsnKey.setUserAttrId(userAttrId);
        
        AppUserAttrPrmsn appUserAttrPrmsn = new AppUserAttrPrmsn();
        appUserAttrPrmsn.setKey(appUserAttrPrmsnKey);
        appUserAttrPrmsn.setCrtTs(new Date());
        appUserAttrPrmsn.setCrtUserId(getClass().getSimpleName());
        
        return appUserAttrPrmsn;
    }
    
    protected AppCmpndAttrSmplAttrPrmsn permission(int appPrtflId, String cmpndUserAttrId, String smplUserAttrId) {
        AppCmpndAttrSmplAttrPrmsnKey appCmpndAttrSmplAttrPrmsnKey = new AppCmpndAttrSmplAttrPrmsnKey();
        appCmpndAttrSmplAttrPrmsnKey.setAppPrtflId(new BigDecimal(appPrtflId));
        appCmpndAttrSmplAttrPrmsnKey.setCmpndUserAttrId(cmpndUserAttrId);
        appCmpndAttrSmplAttrPrmsnKey.setSmplUserAttrId(smplUserAttrId);
        
        AppCmpndAttrSmplAttrPrmsn appCmpndAttrSmplAttrPrmsn = new AppCmpndAttrSmplAttrPrmsn();
        appCmpndAttrSmplAttrPrmsn.setKey(appCmpndAttrSmplAttrPrmsnKey);
        appCmpndAttrSmplAttrPrmsn.setCrtTs(new Date());
        appCmpndAttrSmplAttrPrmsn.setCrtUserId(getClass().getSimpleName());
        
        return appCmpndAttrSmplAttrPrmsn;
    }
    
    protected UserAttrValu value(String cmpndUserAttrId, String smplUserAttrId, String value) {
        UserAttrValuKey userAttrValuKey = new UserAttrValuKey();
        userAttrValuKey.setCmpndUserAttrId(cmpndUserAttrId == null ? "" : cmpndUserAttrId);
        userAttrValuKey.setSmplUserAttrId(smplUserAttrId);
        userAttrValuKey.setUserAttrInstncId(UUID.randomUUID().toString());
        userAttrValuKey.setUserId("USER");
        userAttrValuKey.setUserIdTypeCd("TEST");
        
        UserAttrValu userAttrValu = new UserAttrValu();
        userAttrValu.setKey(userAttrValuKey);
        userAttrValu.setCrtTs(new Date());
        userAttrValu.setCrtUserId(getClass().getSimpleName());
        userAttrValu.setLastMaintTs(new Date());
        userAttrValu.setLastMaintUserId(getClass().getSimpleName());
        userAttrValu.setUserAttrValuTx(value);
        
        return userAttrValu;
    }
}