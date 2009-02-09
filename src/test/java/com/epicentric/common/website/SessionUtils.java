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
 * Vignette <tt>SessionUtils</tt>
 * </p>
 * <p>
 * In the source code need to be test by JUnit, there are many static
 * methods invoked driectly from the classes of the third party projects. 
 * </p> 
 * <p>
 * 
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class SessionUtils {
    private static User mUser;

    public static User getCurrentUser(HttpSession session) {
        return mUser;
    }

    public static void setCurrentUser(HttpSession session, User user) {
        mUser = user;
    }
    
    public static void cleanup() {
        mUser = null;
    }
}
