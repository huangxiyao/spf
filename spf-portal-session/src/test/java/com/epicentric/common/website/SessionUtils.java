/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.epicentric.common.website;

import javax.servlet.http.HttpSession;

import com.epicentric.user.User;

/**
 * <p>
 * This is a surrogate class used by JUnit test to instead of 
 * Vignette's <tt>SessionUtils</tt>
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
public class SessionUtils {
    private static User mUser;

    /**
     * <p>
     * Return user which has been set by <tt>setCurrentUser</tt> 
     * at the init phase of the test.
     * </p>
     * <p>
     * This method is instand of the method which has the same signature 
     * in Vignette.
     * </p>
     * 
     * @param session http session
     * @return mocked user object
     */
    public static User getCurrentUser(HttpSession session) {
        return mUser;
    }

    /**
     * Maintain a mocked user object in SessionUtils, later the mocked
     * user can be retrieved by <tt>getCurrentUser</tt> method.
     * 
     * @param session http session
     * @param user mocked user
     */
    public static void setCurrentUser(HttpSession session, User user) {
        mUser = user;
    }
    
    /**
     * Clean up the resouces created at the beginning of the test.
     */
    public static void cleanup() {
        mUser = null;
    }
}
