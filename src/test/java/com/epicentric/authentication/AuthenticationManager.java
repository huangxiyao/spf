/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.epicentric.authentication;

import org.jmock.Mockery;

import com.epicentric.services.GenericService;
import com.hp.it.spf.sso.portal.MockeryUtils;

/**
 * <p>
 * This is a surrogate class used by JUnit test to instead of 
 * Vignette's <tt>AuthenticationManager</tt>
 * </p>
 * <p>
 * In the source code need to be test by JUnit, there are many static
 * methods invoked driectly from the classes of the third party projects,
 * such as Vigentte.
 * </p> 
 * <p>
 * To avoid invoking that static methods mentioned above, custom classes will be added to 
 * instand of that refered classes in the tested code at runtime.
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class AuthenticationManager extends GenericService {
    public static AuthenticationManager getDefaultAuthenticationManager()
    {
        return new AuthenticationManager();
    }
    
    public Realm getSSORealm()
    {
        Mockery context = MockeryUtils.createMockery();
        Realm realm = MockeryUtils.mockRealm(context, null);
        return realm;
    }
}
