package com.hp.spp.portal.cache;

import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.EntryRefreshPolicy;
import com.hp.spp.config.Config;
import com.hp.spp.portal.common.site.SiteManager;

public class CacheFilterRefreshPolicy implements EntryRefreshPolicy {

	/**
	 * Exteracts the site name from the entry key and returns true if the period since cache
	 * entry creation or its last update is greater than configured for the site cache expiration
	 * period.
	 */
	public boolean needsRefresh(CacheEntry entry) {
		String key = entry.getKey();
		int posStart = "site:".length();
		int posEnd = key.indexOf(',', posStart);
		if (posEnd == -1) {
			// This case should not happen but if it does, it's safer to return the value that will
			// force the cache refresh then the value that would result in cache read.
			return true;
		}
		String siteName = key.substring(posStart, posEnd);
        int cacheEntryExpiryPeriod = SiteManager.getInstance().getSite(siteName).getCacheFileterPublicPagesCacheExpiryPeriodInMins();
        if (cacheEntryExpiryPeriod <= 0) {
			return true;
		}
		long now = System.currentTimeMillis();
		if (entry.getLastUpdate() > 0) {
			return (now - entry.getLastUpdate()) > (1000 * 60 * cacheEntryExpiryPeriod);
		}
		else {
			return (now - entry.getCreated()) > (1000 * 60 * cacheEntryExpiryPeriod);
		}
	}

}
