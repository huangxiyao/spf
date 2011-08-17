package com.hp.spp.cache;


import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.CacheEntry;

import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.WeakHashMap;

/**
 * The singleton instance of this class is used to record the entries that are present in OSCache
 * caches. The events occuring on these caches are tracked by instances of {@link CacheMonitorNotifier}
 * listener class. This way, several independent OSCache cache instances can be monitored without
 * a need to create special purpose <tt>GeneralCacheAdministrator</tt> classes.
 */
public class CacheMonitor {

	/**
	 * Singleton instance.
	 */
	private static final CacheMonitor mInstance = new CacheMonitor();

	/**
	 * Maps cache entry group names to a set of caches in which these groups are present.
	 */
	private Map/*<String, Set<Cache>>*/ mGroups = new TreeMap();

	/**
	 * Maps cache entry keys to a set of caches in which these keys are present.
	 */
	private Map/*<String, Set<Cache>>*/ mKeys = new TreeMap();

	/**
	 * Used to maintain a list of caches monitored by this class.
	 * Using WeakHashMap to make sure that this monitor does not retain any references to Cache objects
	 * if those are not used anywhere else. This map is really used as a list rather than map -
	 * the elements are stored as map keys.
	 */
	private Map/*<Cache, Boolean>*/ mCaches = new WeakHashMap();

	private CacheMonitor() {}

	public static CacheMonitor getInstance() {
		return mInstance;
	}

	public Set getAllGroups() {
		return mGroups.keySet();
	}

	public Set getAllKeys() {
		return mKeys.keySet();
	}

	public void flushAll() {
		Set allCaches = new HashSet();
		for (Iterator it = mKeys.values().iterator(); it.hasNext();) {
			Set caches = (Set) it.next();
			allCaches.addAll(caches);
		}
		for (Iterator it = mGroups.values().iterator(); it.hasNext();) {
			Set caches = (Set) it.next();
			allCaches.addAll(caches);
		}

		for (Iterator it = allCaches.iterator(); it.hasNext();) {
			com.opensymphony.oscache.base.Cache cache = (com.opensymphony.oscache.base.Cache) it.next();
			cache.flushAll(new Date());
		}
	}

	public void flushGroup(String groupName) {
		Set caches = (Set) mGroups.get(groupName);
		if (caches != null) {
			// duplicate the cache set to avoid ConcurrentModificationException resulting from the
			// fact that the call to cache.flushGroup will trigger the CacheMonitorNotifier to be called
			// (remember - it's a cache listener) and then it will result in the call to monitor's
			// removeGroup method below.
			caches = new HashSet(caches);
			for (Iterator it = caches.iterator(); it.hasNext();) {
				com.opensymphony.oscache.base.Cache cache = (com.opensymphony.oscache.base.Cache) it.next();
				cache.flushGroup(groupName);
			}
		}
	}

	public void flushEntry(String entryKey) {
		Set caches = (Set) mKeys.get(entryKey);
		if (caches != null) {
			// duplicate the cache set to avoid ConcurrentModificationException resulting from the
			// fact that the call to cache.flushEntry will trigger the CacheMonitorNotifier to be called
			// (remember - it's a cache listener) and then it will result in the call to monitor's
			// removeEntry method below.
			caches = new HashSet(caches);
			for (Iterator it = caches.iterator(); it.hasNext();) {
				com.opensymphony.oscache.base.Cache cache = (com.opensymphony.oscache.base.Cache) it.next();
				cache.flushEntry(entryKey);
			}
		}
	}

	public List getFromCache(String key) {
		Set caches = (Set) mKeys.get(key);
		if (caches != null && !caches.isEmpty()) {
			List result = new ArrayList();
			for (Iterator it = caches.iterator(); it.hasNext();) {
				com.opensymphony.oscache.base.Cache cache = (com.opensymphony.oscache.base.Cache) it.next();
				try {
					Object content = cache.getFromCache(key);
					if (content != null) {
						result.add(content);
					}
				}
				catch (NeedsRefreshException e) {
					cache.cancelUpdate(key);
				}
			}
			return result;
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Force flush of the entry in all monitored caches - even those that don't contain the entry. This method
	 * is a workaround allowing the flush {@link JMSListenerHelper#JMS_RECOVERY_CACHE_KEY} and reestablish
	 * JMS connection - it permits to not tie this class to JMS listener classes.
	 * For normal flush use cases {@link #flushEntry(String)} should be used.
	 */
	public void forceFlushEntryInAllMonitoredCaches(String entryKey) {
		for (Iterator it = mCaches.keySet().iterator(); it.hasNext();) {
			com.opensymphony.oscache.base.Cache cache = (com.opensymphony.oscache.base.Cache) it.next();
			cache.flushEntry(entryKey);
		}
	}

	public void destroy() {
		mGroups.clear();
		mKeys.clear();
		mCaches.clear();
	}


	/**
	 * Callback from {@link CacheMonitorNotifier} when new entry is added.
	 */
	synchronized void addCacheEntryCallback(CacheEntry entry, com.opensymphony.oscache.base.Cache cache) {
		Set caches;
		caches = (Set) mKeys.get(entry.getKey());
		if (caches == null) {
			caches = new HashSet();
			mKeys.put(entry.getKey(), caches);
		}
		caches.add(cache);

		Set groups = entry.getGroups();
		if (groups != null && !groups.isEmpty()) {
			for (Iterator it = groups.iterator(); it.hasNext();) {
				String groupName = (String) it.next();
				caches = (Set) mGroups.get(groupName);
				if (caches == null) {
					caches = new HashSet();
					mGroups.put(groupName, caches);
				}
				caches.add(cache);
			}
		}
	}

	/**
	 * Callback from {@link CacheMonitorNotifier} when entry is removed.
	 */
	synchronized void removeCacheEntryCallback(CacheEntry entry, com.opensymphony.oscache.base.Cache cache) {
		if (entry == null) {
			return;
		}

		Set caches = (Set) mKeys.get(entry.getKey());
		if (caches != null) {
			caches.remove(cache);
			if (caches.isEmpty()) {
				mKeys.remove(entry.getKey());
			}
		}
	}

	/**
	 * Callback from {@link CacheMonitorNotifier} when cache group is removed.
	 */
	synchronized void removeCacheGroupCallback(String groupName, com.opensymphony.oscache.base.Cache cache) {
		Set caches = (Set) mGroups.get(groupName);
		if (caches != null) {
			caches.remove(cache);
			if (caches.isEmpty()) {
				mGroups.remove(groupName);
			}
		}
	}

	/**
	 * Callback from {@link CacheMonitorNotifier} when entire cache is flushed.
	 */
	synchronized void removeAllForCacheCallback(com.opensymphony.oscache.base.Cache cache) {
		for (Iterator it = mKeys.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Set caches = (Set) mKeys.get(key);
			caches.remove(cache);
			if (caches.isEmpty()) {
				it.remove();
			}
		}

		for (Iterator it = mGroups.keySet().iterator(); it.hasNext();) {
			String group = (String) it.next();
			Set caches = (Set) mGroups.get(group);
			caches.remove(cache);
			if (caches.isEmpty()) {
				it.remove();
			}
		}
	}

	/**
	 * Callback from {@link CacheMonitorNotifier} when it's added as a listener to a cache.
	 */
	synchronized void addCache(com.opensymphony.oscache.base.Cache cache) {
		mCaches.put(cache, Boolean.TRUE);
	}
}
