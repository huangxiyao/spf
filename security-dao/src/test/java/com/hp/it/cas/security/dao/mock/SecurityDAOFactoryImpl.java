package com.hp.it.cas.security.dao.mock;

import com.hp.it.cas.security.dao.ISecurityDAOFactory;
import com.hp.it.cas.security.dao.IAppCmpndAttrSmplAttrPrmsnDAO;
import com.hp.it.cas.security.dao.IAppUserAttrPrmsnDAO;
import com.hp.it.cas.security.dao.ICmpndAttrSmplAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrDAO;
import com.hp.it.cas.security.dao.IUserAttrTypeDAO;
import com.hp.it.cas.security.dao.IUserAttrValuDAO;
import com.hp.it.cas.security.dao.IUserIdTypeDAO;

import org.jmock.Expectations;
import org.jmock.Mockery;

/**
 * A mock implementation of the ISecurityDAOFactory interface.
 *
 * @author CAS Generator
 * @version v1.0.0
 */
public class SecurityDAOFactoryImpl implements ISecurityDAOFactory {
    ISecurityDAOFactory securityDAOFactory;

    IAppCmpndAttrSmplAttrPrmsnDAO appCmpndAttrSmplAttrPrmsnDAO;
    IAppUserAttrPrmsnDAO appUserAttrPrmsnDAO;
    ICmpndAttrSmplAttrDAO cmpndAttrSmplAttrDAO;
    IUserAttrDAO userAttrDAO;
    IUserAttrTypeDAO userAttrTypeDAO;
    IUserAttrValuDAO userAttrValuDAO;
    IUserIdTypeDAO userIdTypeDAO;

    /**
     * Creates a new SecurityDAOFactoryImpl.
     *
     * @param  context  the JMock context
     */
    public SecurityDAOFactoryImpl(Mockery context) {
        securityDAOFactory = context.mock(ISecurityDAOFactory.class);

        appCmpndAttrSmplAttrPrmsnDAO = AppCmpndAttrSmplAttrPrmsnDAOImpl.mockDAO(context);
        appUserAttrPrmsnDAO = AppUserAttrPrmsnDAOImpl.mockDAO(context);
        cmpndAttrSmplAttrDAO = CmpndAttrSmplAttrDAOImpl.mockDAO(context);
        userAttrDAO = UserAttrDAOImpl.mockDAO(context);
        userAttrTypeDAO = UserAttrTypeDAOImpl.mockDAO(context);
        userAttrValuDAO = UserAttrValuDAOImpl.mockDAO(context);
        userIdTypeDAO = UserIdTypeDAOImpl.mockDAO(context);

        context.checking(new Expectations() {
                {
                    allowing(securityDAOFactory).getAppCmpndAttrSmplAttrPrmsnDAO();
                    will(returnValue(appCmpndAttrSmplAttrPrmsnDAO));

                    allowing(securityDAOFactory).getAppUserAttrPrmsnDAO();
                    will(returnValue(appUserAttrPrmsnDAO));

                    allowing(securityDAOFactory).getCmpndAttrSmplAttrDAO();
                    will(returnValue(cmpndAttrSmplAttrDAO));

                    allowing(securityDAOFactory).getUserAttrDAO();
                    will(returnValue(userAttrDAO));

                    allowing(securityDAOFactory).getUserAttrTypeDAO();
                    will(returnValue(userAttrTypeDAO));

                    allowing(securityDAOFactory).getUserAttrValuDAO();
                    will(returnValue(userAttrValuDAO));

                    allowing(securityDAOFactory).getUserIdTypeDAO();
                    will(returnValue(userIdTypeDAO));

                }
            });
    }

    public IAppCmpndAttrSmplAttrPrmsnDAO getAppCmpndAttrSmplAttrPrmsnDAO() {
        return securityDAOFactory.getAppCmpndAttrSmplAttrPrmsnDAO();
    }

    public IAppUserAttrPrmsnDAO getAppUserAttrPrmsnDAO() {
        return securityDAOFactory.getAppUserAttrPrmsnDAO();
    }

    public ICmpndAttrSmplAttrDAO getCmpndAttrSmplAttrDAO() {
        return securityDAOFactory.getCmpndAttrSmplAttrDAO();
    }

    public IUserAttrDAO getUserAttrDAO() {
        return securityDAOFactory.getUserAttrDAO();
    }

    public IUserAttrTypeDAO getUserAttrTypeDAO() {
        return securityDAOFactory.getUserAttrTypeDAO();
    }

    public IUserAttrValuDAO getUserAttrValuDAO() {
        return securityDAOFactory.getUserAttrValuDAO();
    }

    public IUserIdTypeDAO getUserIdTypeDAO() {
        return securityDAOFactory.getUserIdTypeDAO();
    }

}
