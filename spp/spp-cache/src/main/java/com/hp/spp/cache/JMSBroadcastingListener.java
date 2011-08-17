package com.hp.spp.cache;

import com.opensymphony.oscache.base.InitializationException;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.events.CacheEntryEvent;

/**
 * This class is similar to OSCache JMSBroadcastingListener except that it implicitely creates
 * <tt>cache.cluster.jms.node.name</tt> configuration entry setting it to a UID value. This enables
 * to deploy the same OSCache configuration to several servers in the cluster. Otherwise, the package
 * for each server must be customized with the server-specific node name.
 */
public class JMSBroadcastingListener extends com.opensymphony.oscache.plugins.clustersupport.JMSBroadcastingListener {

	private JMSListenerHelper mHelper;

	public JMSBroadcastingListener() {
		mHelper = new JMSListenerHelper(this) {
			protected void superInitialize(com.opensymphony.oscache.base.Cache cache, Config config) throws InitializationException {
				JMSBroadcastingListener.super.initialize(cache, config);
			}

			protected void superCacheEntryFlushed(CacheEntryEvent cacheEntryEvent) {
				JMSBroadcastingListener.super.cacheEntryFlushed(cacheEntryEvent);
			}
		};
	}

	public void initialize(com.opensymphony.oscache.base.Cache cache, Config config) throws InitializationException {
		mHelper.initialize(cache, config);
	}

	public void cacheEntryFlushed(CacheEntryEvent cacheEntryEvent) {
		mHelper.cacheEntryFlushed(cacheEntryEvent);
	}

}
