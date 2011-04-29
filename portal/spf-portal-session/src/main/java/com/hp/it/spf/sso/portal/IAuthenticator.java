/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

/**
 * IAuthenticator is an interface. This interface should be implemented by all
 * Anthenticators.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @version TBD
 */
public interface IAuthenticator {

    /**
     * This method is implemented to execute all authentication tasks. 
     * This method must be called before calling getUserName() to get the user name.
     */
    public void execute();

    /**
     * This method is implemented to get user name.
     * 
     * @return userName
     */
    public String getUserName();
}
