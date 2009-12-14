/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.user.exception.UserProfileException;

/**
 * Interface allowing pluggabilty of external user profile information providers into
 * Shared Portal Framework (SPF).
 * The profile is retrieved by SPF if any of the following is true:
 * <ul>
 * <li>User is authenticated but the session does not contain profile data yet.</li>
 * <li>Identify of the user in the session does not correspond to the identity of the authenticated user.</li>
 * <li>Site accessed when the session was initialized is different from the site user is accessing in this request.</li>
 * <li>User profile has been updated, based on the last update date header sent by HP Passport.</li>
 * <li>Forced session initialization was requested, e.g. if user profile was updated outside of HP Passport.</li>
 * </ul>
 * <p>
 * The instances of the classes implementing this interface are created only once, and are reused on
 * each request requiring profile retrieval. As such, they must be thread-safe as they can be accessed
 * by several threads at the same time.
 * </p>
 * <p>
 * Note that as this class does not define any lifecycle methods such as init or destroy
 * the allocation of expensive resources which may be needed by this class should be managed outside
 * of it, in classes which are notified about application, session or request lifecycle events.
 * </p>
 * <p>
 * The profile map returned by {@link #getUserProfile(String, javax.servlet.http.HttpServletRequest)}
 * has the following structure:
 * <ul>
 * <li>Profile: Map</li>
 * <li>Map: (AttributeName : AttributeValue)*</li>
 * <li>AttributeName: String | Integer</li>
 * <li>AttributeValue: String | Map | List</li>
 * <li>List: (AttributeValue)*</li>
 * </ul>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public interface IUserProfileRetriever {

    /**
     * Retrieves user profile from a data source outside of the portal.
	 * <p>
	 * The user is uniqely identified by the given <code>userIdentifier</code> parameter which
	 * usually is provided by a web agent such as SiteMinder. If this identifier is not enough
	 * additional information such as site name can be retrieved from the provided <code>request</code>.
	 * {@link com.hp.it.spf.xa.misc.Utils} and {@link com.hp.it.spf.xa.misc.portal.Utils} can be used
	 * to retrieve this data.
	 * </p>
     * 
     * @param userIdentifier unique user identifier such as HP Passport GUID as supplied by SiteMinder
	 * in SM_UNIVERSALID HTTP request header
     * @param request portal request, providing access to additional data which may be required to
	 * determine the profile to be retrieved for the user (e.g. site name).
     * @return user profile map or an empty map
     * @throws UserProfileException if retrieving user profile error occurs
     */
    Map<String, Object> getUserProfile(String userIdentifier, HttpServletRequest request) throws UserProfileException;
}
