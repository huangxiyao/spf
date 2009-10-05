/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.epicentric.user;

import com.epicentric.entity.Entity;

/**
 * <p>
 * This is a utils class used by JUnit test to create a Vignette <tt>User</tt> object.
 * </p>
 * <p>
 * Vignette <tt>User</tt> is a final class, it can not be mocked by JMock. 
 * <tt>UserProvider</tt> is provided to instance <tt>User</tt> by invoking its
 * constructor with <tt>Entity</tt> parameter.
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UserProvider {

    /**
     * Create user with mocked Entity object.
     * 
     * @param entity a mocked entity object
     * @return vignette user
     */
    public static User getUser(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User Entity is invalid.");
        }
        return new User(entity);
    }

}
