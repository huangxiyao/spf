/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.epicentric.user;

import java.util.Set;

import com.epicentric.entity.EntityType;

/**
 * <p>
 * This is a surrogate class used by JUnit test to instead of 
 * Vignette's <tt>EntitySetWrapperFactory</tt>
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
public class EntitySetWrapperFactory {    
    /**
     * Return a instance of EntitySetWrapperFactory
     * @return an instance of EntitySetWrapperFactory
     */
    public static EntitySetWrapperFactory getInstance() {
        return new EntitySetWrapperFactory();
    }
    
    /**
     * <p>
     * Return the set passed into directly.
     * </p>
     * <p>
     * This method is instand of the method which has the same signature 
     * in Vignette.
     * </p>
     * 
     * @param elementType
     * @param setToWrap
     * @return
     */
    @SuppressWarnings("unchecked")
    Set getWrappedSet(EntityType elementType, Set setToWrap) {
        return setToWrap;
    }
}
