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

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.user.profile.manager.IUserProfileRetriever;
import com.hp.it.spf.user.profile.manager.UserProfileRetrieverFactory;

/**
 * This authenticator is used for AtHP users
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
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
     * @param request HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
     *      #AbstractAuthenticator(javax.servlet.http.HttpServletRequest)
     */
    public AtHPAuthenticator(HttpServletRequest request) {
        super(request);
    }

    /**
     * @see AbstractAuthenticator#mapHeaderToUserProfileMap()
     */
    @SuppressWarnings("unchecked")
    protected void mapHeaderToUserProfileMap() {
        super.mapHeaderToUserProfileMap();

        // if profileid is not specified, then use email to instead
        String profileId = (String)userProfile.get(AuthenticationConsts.KEY_PROFILE_ID);
        if (profileId == null || profileId.trim().equals("")) {
            userProfile.put(AuthenticationConsts.KEY_PROFILE_ID,
                            userProfile.get(AuthenticationConsts.KEY_EMAIL));
        }

        // retrieve atHP specific attributes. e.g. mailstop, street, etc.
        userProfile.put(AuthenticationConsts.KEY_MAIL_STOP,
                        userProfile.get(AuthenticationConsts.HEADER_MAIL_STOP_NAME));
        userProfile.put(AuthenticationConsts.KEY_CITY,
                        userProfile.get(AuthenticationConsts.HEADER_CITY_NAME));
        userProfile.put(AuthenticationConsts.KEY_STREET,
                        userProfile.get(AuthenticationConsts.HEADER_STREET_NAME));
        userProfile.put(AuthenticationConsts.KEY_STATE,
                        userProfile.get(AuthenticationConsts.HEADER_STATE_NAME));
        userProfile.put(AuthenticationConsts.KEY_ZIP,
                        userProfile.get(AuthenticationConsts.HEADER_ZIP_NAME));

        setPhone();
    }

    /**
     * This method is used to get the phone extension It will get the String
     * from ssouser's field phone, and splitting on "ext." if there are more
     * than one field after splitting, the first one will be stored in ssouser's
     * field phone, the second one will be stored in ssouser's field phone_ext
     */
    @SuppressWarnings("unchecked")
    private void setPhone() {
        String tmpPhone = (String)userProfile.get(AuthenticationConsts.KEY_PHONE_NUMBER);
        if (tmpPhone != null) {
            tmpPhone = tmpPhone.toLowerCase();
            String[] phones = tmpPhone.split(AuthenticationConsts.PHONE_EXT_SPLIT);
            if (phones.length > 1) {
                String ext = phones[1].trim();
                if (ext != null && ext.length() > 0) {
                    userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER_EXT,
                                    ext);
                }
            }
            userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER,
                            phones[0].trim());
        } else {
            userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER, "");
        }
    }

    /**
     * Retrieve user groups from atHP header and invoke related super method to
     * retrieve user groups from other sources
     * 
     * @return retrieved groups set or an empty set
     */
    @SuppressWarnings("unchecked")
    protected Set getUserGroup() {
        Set<String> groups = new HashSet<String>();
        // retrive groups from http header
        String groupstring = getValue(AuthenticationConsts.HEADER_GROUP_NAME);
        // groups are divided by ,
        if (groupstring != null) {
            StringTokenizer st = new StringTokenizer(groupstring, "^");
            while (st.hasMoreElements()) {
                String temp = (String)st.nextElement();
                if (temp.toLowerCase()
                        .startsWith(AuthenticationConsts.ATHP_GROUP_PREFIX)) {
                    String group = temp.substring(3, temp.indexOf(','));
                    LOG.info("Get UserGroup = " + group);
                    groups.add(group);
                }
            }
        }

        // loggin atHP
        groups.add(AuthenticationConsts.LOCAL_ATHP_NAME);

        // retrive groups with invoking super method and merge them
        groups.addAll(super.getUserGroup());
        return groups;
    }

    /**
     * Retrieve user profile according to user type from user profile retriever for 
     * atHP user.
     * 
     * @return user profile map or an empty map
     * @throws UserProfileException if retrieving user profiles errror
     */
    protected Map<Object, Object> getUserProfile() {
        String originalProfileId = getValue(AuthenticationConsts.HEADER_PROFILE_ID_PROPERTY_NAME);
        if (originalProfileId != null && !"".equals(originalProfileId.trim())) {
            request.setAttribute(AuthenticationConsts.USER_IDENTIFIER_TYPE, EUserIdentifierType.EMPLOYEE);
        } else {
            request.setAttribute(AuthenticationConsts.USER_IDENTIFIER_TYPE, EUserIdentifierType.EMPLOYEE_SIMPLIFIED_EMAIL_ADDRESS);
        }
        return super.getUserProfile();
    }
}
