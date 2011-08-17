package com.hp.spp.config;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.base.NeedsRefreshException;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.ArrayList;

public class CachingConfigEntryDAO implements ConfigEntryDAO {
	private static final String CACHE_KEY = "config.entries";

	private ConfigEntryDAO mDelegate;
	private GeneralCacheAdministrator mCache;

	public CachingConfigEntryDAO(ConfigEntryDAO delegate, GeneralCacheAdministrator cache) {
		mDelegate = delegate;
		mCache = cache;
	}

	public ConfigEntry load(String name) {
		try {
			Map entries = (Map) mCache.getFromCache(CACHE_KEY);
			return (ConfigEntry) entries.get(name);
		}
		catch (NeedsRefreshException e) {
			Map entries = loadAllEntriesFromDelegate();
			udpateCache(entries);
			return (ConfigEntry) entries.get(name);
		}
	}

	private void udpateCache(Map entries) {
		boolean updated = false;
		try {
			mCache.putInCache(CACHE_KEY, entries);
			updated = true;
		}
		finally {
			if (!updated) {
				mCache.cancelUpdate(CACHE_KEY);
			}
		}
	}

	private Map loadAllEntriesFromDelegate() {
		Map entries = new TreeMap();
		List delegateEntries = mDelegate.getAllEntries();
		for (Iterator it = delegateEntries.iterator(); it.hasNext();) {
			ConfigEntry entry = (ConfigEntry) it.next();
			entries.put(entry.getName(), entry);
		}
		return entries;
	}

	public void save(ConfigEntry entry) {
		mDelegate.save(entry);
		mCache.flushEntry(CACHE_KEY);
	}

	public void delete(String name) {
		mDelegate.delete(name);
		mCache.flushEntry(CACHE_KEY);
	}

	public List getAllEntries() {
		try {
			Map entries = (Map) mCache.getFromCache(CACHE_KEY);
			return new ArrayList(entries.values());
		}
		catch (NeedsRefreshException e) {
			Map entries = loadAllEntriesFromDelegate();
			List allEntries = new ArrayList(entries.values());
			udpateCache(entries);
			return allEntries;
		}
	}
}
