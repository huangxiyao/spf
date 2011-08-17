package com.hp.spp.webservice.ups.manager;

import com.hp.spp.portal.common.site.Site;
import com.hp.spp.portal.common.site.SiteManager;
import org.apache.log4j.Logger;

/**
 * Creates an instance of {@link UserProfileService}. By default it creates {@link HPPUserProfileServiceClient}
 * which connects to HPP UPS web service, unless a mock profile is to be used for the given site in which
 * case it creates {@link MockUserProfileService} instance.
 *
 * @see com.hp.spp.portal.common.site.Site#getUseMockProfile()
 */
public class UserProfileServiceFactory {
	private static final Logger mLog = Logger.getLogger(UserProfileServiceFactory.class);

	public UserProfileService newUserProfileService(String siteName) {
		Site site = SiteManager.getInstance().getSite(siteName);
		if (site == null) {
			throw new IllegalStateException("Site not found: " + siteName);
		}

		UserProfileService ups;
		if (site.getUseMockProfile()) {
			ups = new MockUserProfileService();
		}
		else {
			ups = new HPPUserProfileServiceClient();
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("Using the following UserProfileService implementation for the site '" +
					siteName + "': " + ups.getClass().getName());
		}

		return ups;
	}
}
