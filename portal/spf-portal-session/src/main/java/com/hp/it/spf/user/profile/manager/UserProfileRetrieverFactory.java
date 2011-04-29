/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.user.group.manager.CompoundUserGroupRetriever;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * Factory class used by {@link com.hp.it.spf.sso.portal.AbstractAuthenticator}
 * to create implementation classes of <tt>IUserProfileRetriever</tt>.
 * <p>
 * The fully qualified name of the class implementing {@link com.hp.it.spf.user.profile.manager.IUserProfileRetriever}
 * interface must be configured in the SPF configuration file called <tt>SharedPortalSSO.properties</tt>,
 * as the value for key <code>user_profile_retriever</code>. If this property is not specified
 * the default, no-op implementation {@link com.hp.it.spf.user.profile.manager.DefaultUserProfileRetriever}
 * will be used.
 * </p>
 * <p>
 * In case profile should be retrieved from several sources, this can be either implemented directly
 * in the retriever or {@link com.hp.it.spf.user.profile.manager.CompoundUserProfileRetriever} class
 * can be used. It allows additional configuration - the list of fully qualified names of the classes
 * implementing the profile retriever interface. The list is specified as the value for the key
 * <code>user_profile_retriever.delegates</code>. The class names should be separated by a comma.
 * <code>CompoundUserProfileRetriever</code> invokes the retriever implementations in the order
 * in which they were specified in the configuration file, merging profile maps returned by each of them. 
 * </p>
 * <p>
 * This class caches internally the loaded IUserProfileRetriever implementations. It also monitors
 * the configuration file for changes. If the value corresponding to the profile retriver class
 * name key changes, the implementation class will be reloaded. Otherwise, the cached implementation
 * will be used. 
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UserProfileRetrieverFactory {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(UserProfileRetrieverFactory.class);
    
    // use a map to contain all retrieved IUserProfileRetriever objects
    // each type of IUserProfileRetriever will be instantiated only once.
    private static Map<String, IUserProfileRetriever> retrieverMap;
    
    // save retrieved IUserProfileRetriever's properties information
    // hpp_user_profile_retriever=com.hp.it.spf.user.profile.manager.DefaultUserProfileRetriever
    // Map.put("user_profile_retriever", "com.hp.it.spf.user.profile.manager.DefaultUserProfileRetriever")
    private static Map<String, String> retrieverPorperties;

    private static String DEFAULT_RETRIEVER = DefaultUserProfileRetriever.class.getName();
    
    // put default retriever into the map
    static {
        retrieverMap = new ConcurrentHashMap<String, IUserProfileRetriever>();
        retrieverPorperties = new ConcurrentHashMap<String, String>();
        retrieverMap.put(DEFAULT_RETRIEVER, new DefaultUserProfileRetriever());
    }
    
	/**
	 * Create a specified concrete IUserProfileRetriever class defined in the propertise file.
	 * 
	 * @param key specified retriever key
	 * @return a implementation class of IUserProfileRetriever
	 */
	@SuppressWarnings("unchecked")
    public static IUserProfileRetriever createUserProfileImpl(String key) {
	    // if specified UserProfileRetriever value doesn't be changed, return the cached one directly
        if (isLoadedAndNoChange(key)) {
            return retrieverMap.get(key);
        }
        
        // specified retriever key doesn't exist
        try {
            String className = AuthenticatorHelper.getProperty(key);
            Class clazz = Class.forName(className);
            IUserProfileRetriever specifiedRetriever;
            if (clazz == CompoundUserProfileRetriever.class) {        
                String delegateClassKey = key.concat(".delegates");
                String delegateClassNames = AuthenticatorHelper.getProperty(delegateClassKey);
                
                specifiedRetriever = (IUserProfileRetriever)clazz.getConstructor(String.class)
                                                                 .newInstance((delegateClassNames));
                retrieverPorperties.put(key, className);
                retrieverPorperties.put(delegateClassKey, delegateClassNames);
            } else {
                specifiedRetriever = (IUserProfileRetriever)clazz.newInstance();
                retrieverPorperties.put(key, className);
            }   
            
            retrieverMap.put(key, specifiedRetriever);
        } catch (Exception ex) {
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("Error when instantiating user profile retriever.", ex);
            }
            // use default retriever when error occours
            key = DEFAULT_RETRIEVER;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Use user profile retriever: " + key);
        }
        return retrieverMap.get(key);	    
	}
	
	/**
     * If the specified key is in map and its value is not changed, then return
     * <code>true</code>, Otherwise, return <code>false</code>
     * 
     * @param key user profile retriever key
     * @return <code>true</code> if not changed, otherwise, <code>false</code>
     */
    private static boolean isLoadedAndNoChange(String key) {
        String value = AuthenticatorHelper.getProperty(key, true);

        if (retrieverPorperties.containsKey(key)) {
            if (retrieverPorperties.get(key).equals(value)) {                
                // CompoundUserGroupRetriever
                if (CompoundUserGroupRetriever.class.getName().equals(value)) {
                    String delegateKey = key.concat(".delegates");
                    String delegateValue = AuthenticatorHelper.getProperty(delegateKey);
                    // specified retriever key already exist in the
                    // retrieverPorperties map
                    if (retrieverPorperties.containsKey(delegateKey)) {
                        // delegated class names are not changed
                        if (retrieverPorperties.get(delegateKey).equals(delegateValue)) {
                            return true;
                        } 
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
