package com.hp.it.spf.user.group.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class UGSParametersManagerTest {
    private static Properties ps = new Properties();
    private static UGSParametersManager manager = null;

    /**
     * Init parameters for test
     * 
     * @throws Exception if operation failed
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String filepath = "./UGSParameters.properties";
        try {
            InputStream is = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResourceAsStream(filepath);
            ps.load(is);
            
            manager = UGSParametersManager.getInstance();
        } catch (IOException ex) {
            fail(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Test getInstance() method.
     * 
     * @see UGSParametersManager#getInstance()
     */
    @Test
    public void testGetInstance() {       
        assertNotNull("UGSParametersManager should not be null.", manager);
    }
    
    /**
     * Test getEndPoint() method.
     * 
     * @see UGSParametersManager#getEndPoint()
     */
    @Test
    public void testGetEndPoint() {
        assertEquals(manager.getEndPoint(),
                     ps.get(IUGSConstant.ENDPOINTS_PREFIX + ps.get(IUGSConstant.MODE)));
    }

    /**
     * Test getMode() method.
     * 
     * @see UGSParametersManager#getMode()
     */
    @Test
    public void testGetMode() {
        assertEquals(manager.getMode(),
                     ps.get(IUGSConstant.MODE));
    }

    /**
     * Test getPassword() method.
     * 
     * @see UGSParametersManager#getPassword()
     */
    @Test
    public void testGetPassword() {
        assertEquals(manager.getPassword(),
                     ps.get(IUGSConstant.PASSWORD_PREFIX + ps.get(IUGSConstant.MODE)));
    }

    /**
     * Test getUserName() method.
     * 
     * @see UGSParametersManager#getUserName()
     */
    @Test
    public void testGetUserName() {
        assertEquals(manager.getUserName(),
                     ps.get(IUGSConstant.USERNAME_PREFIX + ps.get(IUGSConstant.MODE)));
    }

    /**
     * Test getTimeout() method.
     * 
     * @see UGSParametersManager#getTimeout()
     */
    @Test
    public void testGetTimeout() {
        assertEquals(manager.getTimeout(),
                     ps.get(IUGSConstant.TIMEOUT + ps.get(IUGSConstant.MODE)));
    }
}
