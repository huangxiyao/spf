package com.hp.spp.filters.access;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import java.util.List;
import java.util.Collections;

public class CachingUrlAccessRuleDAO implements UrlAccessRuleDAO {

	private UrlAccessRuleDAO mDelegate;
	private GeneralCacheAdministrator mCache;

	public CachingUrlAccessRuleDAO(UrlAccessRuleDAO delegate, GeneralCacheAdministrator cache) {
		mDelegate = delegate;
		mCache = cache;
	}

	public List findUrlAccessRules(String ruleSetName) {
		String key = "urlaccessrules." + ruleSetName;
		List result = Collections.EMPTY_LIST;
		try {
			result = (List) mCache.getFromCache(key);
		}
		catch (NeedsRefreshException e) {
			boolean updated = false;
			try {
				result = mDelegate.findUrlAccessRules(ruleSetName);
				mCache.putInCache(key, result);
				updated = true;
			}
			finally {
				if (!updated) {
					mCache.cancelUpdate(key);
				}
			}
		}
		return result;
	}

}
