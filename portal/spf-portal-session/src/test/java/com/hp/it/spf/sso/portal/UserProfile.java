/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is the util class to maintain the user profiles for test.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "deprecation"})
public class UserProfile {
    protected static Map userProfile = new HashMap();
    
    // init userProfile map
    static {
        userProfile.put(AuthenticationConsts.KEY_PROFILE_ID, "aca249720c9b91b4d0c0d26c1c751f21");
        userProfile.put(AuthenticationConsts.KEY_USER_NAME, "vignetteAdmin");
        userProfile.put(AuthenticationConsts.KEY_EMAIL, "SP-VIGNETTE-ADMIN@groups.hp.com");
        userProfile.put(AuthenticationConsts.KEY_FIRST_NAME, "Vignette");
        userProfile.put(AuthenticationConsts.KEY_LAST_NAME, "Admin");                        
        userProfile.put(AuthenticationConsts.KEY_COUNTRY, "US");
        userProfile.put(AuthenticationConsts.KEY_LANGUAGE, "en");
        userProfile.put(AuthenticationConsts.KEY_LAST_CHANGE_DATE, new Date(2008, 9, 29, 17, 53, 25));
        userProfile.put(AuthenticationConsts.KEY_LAST_LOGIN_DATE, new Date(2008, 9, 29, 17, 53, 25));
        userProfile.put(AuthenticationConsts.KEY_TIMEZONE, AuthenticationConsts.DEFAULT_TIMEZONE);
        Set group = new HashSet();
        group.add("LOCAL_TEST_GROUP");
        userProfile.put(AuthenticationConsts.KEY_USER_GROUPS, group); 
        userProfile.put(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID, "primarysiteid");  
    }
    
    /**
     * Retrieve property from userProfile map with key.
     * 
     * @param property user profile map key
     * @return user profile value defined in the map
     */
    public static Object get(String property) {
        return userProfile.get(property);
    }
}
