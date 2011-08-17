package com.hp.spp.webservice.ugs.manager;

import com.hp.spp.portal.common.site.Site;
import com.hp.spp.portal.common.site.SiteManager;
import org.apache.log4j.Logger;

/**
 * The factory creates the instance of {@link UserGroupService} class using the following rules in order:
 * <ol>
 * <li>If specific class name is available through {@link com.hp.spp.portal.common.site.Site#getUGSClientClassName()}
 * 	it will be instantiated.</li>
 * <li>If the given site is using mock profile it will return {@link MockUserGroupService}</li>
 * <li>Otherwise it will return a default implementation.</li>
 * </ol>
 */
public class UserGroupServiceFactory {

	private static final Logger mLog = Logger.getLogger(UserGroupServiceFactory.class);

	public UserGroupService newUserGroupService(String siteName) {
		Site site = SiteManager.getInstance().getSite(siteName);

		// If we have a class specified explicitely, then use it. This way it will be possible to
		// use mock profile for UPS and a web service for UGS
		String clientClassName = site.getUGSClientClassName();
		if (clientClassName != null) {
			try {
				if (mLog.isDebugEnabled()) {
					mLog.debug("Using UserGroupServiceClient class: " + clientClassName);
				}
				return (UserGroupService) Class.forName(clientClassName).newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException("Error creating UserGroupServiceClient instance: " + e, e);
			}
		}
		else {
			//If the client class name is not explicitely specified, let's see if the site is
			// using mock profile
			boolean isSiteUsingMockProfile = site.getUseMockProfile();
			if (isSiteUsingMockProfile) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("Using MockUserGroupServiceClient");
				}
				return new MockUserGroupService();
			}
			else {
				//No client class name specified and no mock profile used by site - let's use
				// the default client

				UserGroupService aDefault = new HPPUserGroupServiceClient();
				if (mLog.isDebugEnabled()) {
					mLog.debug("Using default UserGroupServiceClient: " + aDefault);
				}
				return aDefault;
			}
		}
	}
}
