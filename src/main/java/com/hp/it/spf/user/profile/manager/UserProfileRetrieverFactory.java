/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.ResourceBundle;

import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * This is a factory class to create a concrete implementation
 * of IUserProfileRetriever interface.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UserProfileRetrieverFactory {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(UserProfileRetrieverFactory.class);
	/**
	 * create a specified concrete IUserProfileRetriever class defined in the propertise file.
	 * 
	 * @return a implementation class of IUserProfileRetriever
	 */
	@SuppressWarnings("unchecked")
    public static IUserProfileRetriever createUserProfileImpl() {
	    ResourceBundle rb = PropertyResourceBundleManager.getBundle(AuthenticationConsts.SHARED_PORTAL_SSO_FILE_BASE);
	    if (rb != null) { 
	        try {
	            String className = rb.getString(AuthenticationConsts.USER_PROFILE_RETRIEVER);
	            Class clazz = Class.forName(className);
                return (IUserProfileRetriever)clazz.newInstance();  
	        } catch (Exception ex) {
	            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug("Error occurs when user profile retriever instantiating.", ex);
                }
	        }
	    }
	    if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Use default user profile retriever.");
        }
		return new DefaultUserProfileRetriever();
	}
}
