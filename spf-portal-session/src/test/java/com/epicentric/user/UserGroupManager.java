/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.epicentric.user;

import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityType;

/**
 * <p>
 * This is a surrogate class used by JUnit test to instead of 
 * Vignette's <tt>UserGroupManager</tt>
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
public class UserGroupManager {
    /**
     * Return a instance of UserGroupManager
     * @return an instance of UserGroupManager
     */
    public static UserGroupManager getInstance() {
        return new UserGroupManager();
    }

    /**
     * <p>
     * Return null EntityType.
     * </p>
     * <p>
     * This method is instand of the method which has the same signature 
     * in Vignette.
     * </p>
     * 
     * @return an null EntityType
     */
    public EntityType getUserGroupEntityType() {
        return null;
    }

    /**
     * <p>
     * Return null UserGroupQueryResults.
     * </p>
     * <p>
     * This method is instand of the method which has the same signature 
     * in Vignette.
     * </p>
     * 
     * @return an null UserGroupQueryResults
     */
    public UserGroupQueryResults getUserGroups(String propertyID,
                                               Object propertyValue) throws EntityPersistenceException {
        return null;
    }
}
