package com.hp.spp.cache;

import com.opensymphony.oscache.base.events.CacheEntryEventListener;
import com.opensymphony.oscache.base.events.CacheEntryEvent;
import com.opensymphony.oscache.base.events.CacheGroupEvent;
import com.opensymphony.oscache.base.events.CachePatternEvent;
import com.opensymphony.oscache.base.events.CachewideEvent;
import com.opensymphony.oscache.base.*;

/**
 * This is a cache listener that records, through {@link CacheMonitor}, the entries that are present
 * in the cache. The listener is only used as a link between OSCache and <tt>CacheMonitor</tt>.
 * The actual tracking happens in <tt>CacheMonitor</tt>. The listener should be configured on the
 * listener list of oscache configuration property file.
 */
public class CacheMonitorNotifier implements CacheEntryEventListener, LifecycleAware {

	public void cacheEntryAdded(CacheEntryEvent event) {
		CacheMonitor.getInstance().addCacheEntryCallback(event.getEntry(), event.getMap());
	}

	public void cacheEntryFlushed(CacheEntryEvent event) {
		CacheMonitor.getInstance().removeCacheEntryCallback(event.getEntry(), event.getMap());
	}

	public void cacheEntryRemoved(CacheEntryEvent event) {
		CacheMonitor.getInstance().removeCacheEntryCallback(event.getEntry(), event.getMap());
	}

	public void cacheEntryUpdated(CacheEntryEvent event) {
		CacheMonitor.getInstance().addCacheEntryCallback(event.getEntry(), event.getMap());
	}

	public void cacheGroupFlushed(CacheGroupEvent event) {
		CacheMonitor.getInstance().removeCacheGroupCallback(event.getGroup(), event.getMap());
	}

	public void cachePatternFlushed(CachePatternEvent event) {
	}

	public void cacheFlushed(CachewideEvent event) {
		CacheMonitor.getInstance().removeAllForCacheCallback(event.getCache());
	}

	public void initialize(com.opensymphony.oscache.base.Cache cache, Config config) throws InitializationException {
		CacheMonitor.getInstance().addCache(cache);
	}

	public void finialize() throws FinalizationException {
	}
}
