/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.epicentric.user;

import java.util.Map;

import com.epicentric.entity.EntityNotFoundException;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;

/**
 * <p>
 * This is a surrogate class used by JUnit test to instead of 
 * Vignette's <tt>UserManager</tt>
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
public class UserManager {
    private static UserManager userManager = null;
    private static User user = null;
    private static User guestUser = null;
    private static String type = "default";
    
    /**
     * Return a instance of UserManager
     * 
     * @return an instance of UserManager
     */
    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    /**
     * <p>
     * Return a mocked user set by <tt>createUser</tt>. This method will return the
     * mocked user by checking <tt>type</tt> value.
     * </p>
     * <p>
     * This method is instand of the method which has the same signature 
     * in Vignette.
     * </p>
     * 
     * @param propertyID user property id
     * @param propertyValue user property value
     * @return a mocked user
     * @throws EntityPersistenceException
     * @throws EntityNotFoundException
     */
    public User getUser(String propertyID, Object propertyValue) throws EntityPersistenceException,
                                                                EntityNotFoundException {
        boolean returnUserTag = true;
        String[] str = propertyValue.toString().split("[-]");
        int len = str.length;
        if (type.equalsIgnoreCase("both") && len > 1) {
            returnUserTag = true;
        } else if (type.equalsIgnoreCase("lang") && len == 1) {
            returnUserTag = true;
        } else if (type.equalsIgnoreCase("en") && len == 1 && str[0].endsWith("_en")) {
            returnUserTag = true;
        } else if (type.equalsIgnoreCase("default")) {
            returnUserTag = true;
        } else {
            returnUserTag = false;
        }
        return returnUserTag ? user : null;
    }

    /**
     * Set a mocked user and guest user to <tt>UserManager</tt>.
     * 
     * @param mUser mocked user
     * @param gUser mocked guest user
     */
    public static void createUser(User mUser, User gUser) {
        user = mUser;
        guestUser = gUser;
    }

    /**
     * <p>
     * Return a mocked user set by <tt>createUser</tt>. 
     * </p>
     * <p>
     * This method is instand of the method which has the same signature 
     * in Vignette.
     * </p>
     * 
     * @param initialPropertySettings
     * @return a mocked user
     * @throws UniquePropertyValueConflictException
     * @throws EntityPersistenceException
     */
    public User createUser(Map initialPropertySettings) throws UniquePropertyValueConflictException,
                                                       EntityPersistenceException {
        return user;

    }

    /**
     * Return a mocked user
     * 
     * @return a mocked user
     * @throws EntityPersistenceException
     */
    public User getGuestUser() throws EntityPersistenceException {
        return user;
    }

    /**
     * Set return type value
     * 
     * @param returnType string value of return type
     */
    public static void setReturnUserType(String returnType) {
        type = returnType;
    }
    
    /**
     * Clean up the resouces created at the beginning of the test.
     */
    public static void cleanup() {
        userManager = null;
        user = null;
        guestUser = null;
    }
}
