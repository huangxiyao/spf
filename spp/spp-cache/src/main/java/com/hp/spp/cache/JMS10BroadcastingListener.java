package com.hp.spp.cache;

import com.opensymphony.oscache.base.events.CacheEntryEvent;
import com.opensymphony.oscache.base.InitializationException;
import com.opensymphony.oscache.base.Config;

/**
 * This class is similar to OSCache JMS10BroadcastingListener except that it implicitely creates
 * <tt>cache.cluster.jms.node.name</tt> configuration entry setting it to a UID value. This enables
 * to deploy the same OSCache configuration to several servers in the cluster. Otherwise, the package
 * for each server must be customized with the server-specific node name.
 */
public class JMS10BroadcastingListener extends com.opensymphony.oscache.plugins.clustersupport.JMS10BroadcastingListener {

	private JMSListenerHelper mHelper;

	public JMS10BroadcastingListener() {
		mHelper = new JMSListenerHelper(this) {
			protected void superInitialize(com.opensymphony.oscache.base.Cache cache, Config config) throws InitializationException {
				JMS10BroadcastingListener.super.initialize(cache, config);
			}

			protected void superCacheEntryFlushed(CacheEntryEvent cacheEntryEvent) {
				JMS10BroadcastingListener.super.cacheEntryFlushed(cacheEntryEvent);
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
