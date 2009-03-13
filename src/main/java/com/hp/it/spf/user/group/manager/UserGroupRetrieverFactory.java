/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.HashMap;
import java.util.Map;

import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * This is factory class to create implementation class of
 * <tt>IUserGroupRetriever</tt>.
 * <p>
 * This class use a map to maintain all loaded UserGroupRetrievers with the
 * specified key. If the key's value stored in properties file is not changed,
 * then the cached one will be retrieved.
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UserGroupRetrieverFactory {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(UserGroupRetrieverFactory.class);

    // use a map to contain all retrieved IUserGroupRetriever objects
    // each type of IUserGroupRetriever will be instantiated only once.
    private static Map<String, IUserGroupRetriever> retrieverMap;

    // save retrieved IUserGroupRetriever's properties information
    // hpp_user_group_retriever=com.hp.it.spf.user.group.manager.DefaultUserGroupRetriever
    // Map.put("hpp_user_group_retriever",
    // "com.hp.it.spf.user.group.manager.DefaultUserGroupRetriever")
    private static Map<String, String> retrieverPorperties;

    private static String DEFAULT_RETRIEVER = DefaultUserGroupRetriever.class.getName();

    // put default retriever into the map
    static {
        retrieverMap = new HashMap<String, IUserGroupRetriever>();
        retrieverPorperties = new HashMap<String, String>();
        retrieverMap.put(DEFAULT_RETRIEVER, new DefaultUserGroupRetriever());
    }

    /**
     * Create a concrete UserGroupsImpl class with the specified retriever key
     * 
     * @param key specified retriever key
     * @return a implimentation class of IUserGroupRetriever
     */
    @SuppressWarnings("unchecked")
    public static IUserGroupRetriever createUserGroupImpl(String key) {
        // if specified UserGroupRetriever value doesn't be changed, return the
        // cached one directly
        if (isLoadedAndNoChange(key)) {
            return retrieverMap.get(key);
        }

        // specified retriever key doesn't exist
        try {
            String className = AuthenticatorHelper.getProperty(key);
            Class clazz = Class.forName(className);
            IUserGroupRetriever specifiedRetriever;
            if (clazz == CompoundUserGroupRetriever.class) {
                String delegateClassKey = key.concat(".delegates");
                String delegateClassNames = AuthenticatorHelper.getProperty(delegateClassKey);

                specifiedRetriever = (IUserGroupRetriever)clazz.getConstructor(String.class)
                                                               .newInstance((delegateClassNames));
                retrieverPorperties.put(key, className);
                retrieverPorperties.put(delegateClassKey, delegateClassNames);
            } else {
                specifiedRetriever = (IUserGroupRetriever)clazz.newInstance();
                retrieverPorperties.put(key, className);
            }

            retrieverMap.put(key, specifiedRetriever);
        } catch (Exception ex) {
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("Error when instantiating user groups retriever.", ex);
            }
            // use default retriever when error occours
            key = DEFAULT_RETRIEVER;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Use user groups retriever: " + key);
        }
        return retrieverMap.get(key);
    }

    /**
     * If the specified key is in map and its value is not changed, then return
     * <code>true</code>, Otherwise, return <code>false</code>
     * 
     * @param key user group retriever key
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
