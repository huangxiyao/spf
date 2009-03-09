/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.ResourceBundle;

import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * This is factory class to create impl class of <tt>IUserGroupRetriever</tt>.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UserGroupRetrieverFactory {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(UserGroupRetrieverFactory.class);
	/**
	 * create a concrete UserGroupsImpl class
	 * 
	 * @return a implimentation class of IUserGroupRetriever
	 */
	@SuppressWarnings("unchecked")
    public static IUserGroupRetriever createUserGroupImpl() {
	    ResourceBundle rb = PropertyResourceBundleManager.getBundle(AuthenticationConsts.SHARED_PORTAL_SSO_FILE_BASE);
        if (rb != null) { 
            try {
                String className = rb.getString(AuthenticationConsts.USER_GROUP_RETRIEVER);
                Class clazz = Class.forName(className);
                return (IUserGroupRetriever)clazz.newInstance();  
            } catch (Exception ex) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug("Error occurs when user groups retriever instantiating.", ex);
                }
            }
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Use default user groups retriever.");
        }
        return new DefaultUserGroupRetriever();
	}
}
