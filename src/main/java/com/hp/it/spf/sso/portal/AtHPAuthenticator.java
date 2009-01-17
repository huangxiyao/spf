/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

/**
 * This authenticator is used for AtHP users
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @version TBD
 * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
 */
public class AtHPAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;

    private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper.getLog(AtHPAuthenticator.class);

    /**
     * This is the constructor for AtHPAuthenticator. It will call the
     * constructor of AbstractAuthenticator which is its father class To do the
     * initialization task.
     * 
     * @param request
     *            HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
     *      #AbstractAuthenticator(javax.servlet.http.HttpServletRequest)
     */
    public AtHPAuthenticator(HttpServletRequest request) {
        super(request);
    }

    protected void mapHeaderToUserProfileMap() {
    	super.mapHeaderToUserProfileMap();
    	
    	// if profileid is not specified, then use email to instead
    	String profileId = userProfile.get(AuthenticationConsts.PROPERTY_PROFILE_ID);
    	if (profileId == null || profileId.trim().equals("")) {
	    	userProfile.put(AuthenticationConsts.PROPERTY_PROFILE_ID, 
	    					userProfile.get(AuthenticationConsts.PROPERTY_EMAIL_ID));
    	} 
    }
    
    /**
     * This method is used to retrieve group info for AtHP users It will get the
     * group info from the request header and analyse. Only the group starting
     * with "cn=sp_" will be retrieved
     * 
     * @return retrived groups
     */
    @SuppressWarnings("unchecked")
	protected Set getUserGroup() {
        Set groups = new HashSet();
        String groupstring = getValue(AuthenticationConsts.HEADER_GROUP_NAME);
        // groups are divided by ,
        if (groupstring != null) {
            StringTokenizer st = new StringTokenizer(groupstring, "^");
            while (st.hasMoreElements()) {
                String temp = (String)st.nextElement();
                if (temp.toLowerCase().startsWith(AuthenticationConsts.ATHP_GROUP_PREFIX)) {
                    String group = temp.substring(3, temp.indexOf(','));
                    LOG.info("Get UserGroup = " + group);
                    groups.add(group);
                }
            }
        }
        return groups;
    }
    
    /**
     * retrieve user profile for atHP
     */
    protected Map<String, String> getUserProfile() {
    	return null;
    }
}
