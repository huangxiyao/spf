package com.hp.spp.wsrp.shield;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.base.NeedsRefreshException;

import java.util.Set;
import java.util.List;

public class CachingRemoteServerDAO implements RemoteServerDAO {
	private static final String CACHE_KEY = "RemoteServerDAO.DisabledServerUrls";

	private RemoteServerDAO mDelegate;
	private GeneralCacheAdministrator mCache;

	public CachingRemoteServerDAO(RemoteServerDAO delegate, GeneralCacheAdministrator cache) {
		mDelegate = delegate;
		mCache = cache;
	}

	public Set<String> getDisabledServerUrls() {
		Set<String> result = null;
		try {
			result = (Set<String>) mCache.getFromCache(CACHE_KEY);
		}
		catch (NeedsRefreshException e) {
			boolean updated = false;
			try {
				result = mDelegate.getDisabledServerUrls();
				mCache.putInCache(CACHE_KEY, result);
				updated = true;
			}
			finally {
				if (!updated) {
					mCache.cancelUpdate(CACHE_KEY);
				}
			}
		}
		return result;
	}

	public List<PortalRemoteServerInfo> getPortalDeclaredRemoteServers() {
		// no caching here - let's just read it directly from the source
		return mDelegate.getPortalDeclaredRemoteServers();
	}

	public void setUrlEnabled(String url, boolean enabled) {
		mCache.flushEntry(CACHE_KEY);
		mDelegate.setUrlEnabled(url, enabled);
	}
}
