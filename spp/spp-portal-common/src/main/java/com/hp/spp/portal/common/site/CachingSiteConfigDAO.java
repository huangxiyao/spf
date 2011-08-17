package com.hp.spp.portal.common.site;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class CachingSiteConfigDAO implements SiteConfigDAO {
	private SiteConfigDAO mDelegate;
	private GeneralCacheAdministrator mCache;
	private static final String SITE_CONFIG_KEY = "site.config";

    public CachingSiteConfigDAO(SiteConfigDAO delegate, GeneralCacheAdministrator cache){
		mDelegate = delegate;
		mCache = cache;
	}

    /**
     * This method fetches all the site configuration details encapsulated in Site object. If
     * the site object is not found in cache a db call is made for the same.
     * @param siteName Name of the site for which configuration details should be fetched.
     * @return Site The site object containing all site configurations.
     */
    public Site getSite(String siteName) {
		String key = getCacheKey(siteName);

        Site site;
        try {
			site = (Site) mCache.getFromCache(key);
		}
		catch (NeedsRefreshException e) {
			boolean updated = false;
			try {
				site = mDelegate.getSite(siteName);
				mCache.putInCache(key, site);
				updated = true;
			}
			finally {
				if (!updated) {
					mCache.cancelUpdate(key);
				}
			}
		}
		return site;
	}

	/**
     * This method sets the site key value in SPP_SITE_SETTING table
     * @param updatedSite Name of the site
     */
	public void updateSite(Site updatedSite) {
		String key = getCacheKey(updatedSite.getSiteName());
        mCache.flushEntry(key);
    	mDelegate.updateSite(updatedSite);
	}

    private String getCacheKey(String siteName){
         return new StringBuilder().append(SITE_CONFIG_KEY).append("-").append(siteName).toString();
    }
}