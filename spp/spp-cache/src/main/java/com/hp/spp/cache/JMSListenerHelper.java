package com.hp.spp.cache;

import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.InitializationException;
import com.opensymphony.oscache.base.events.CacheEntryEvent;
import com.opensymphony.oscache.plugins.clustersupport.AbstractBroadcastingListener;
import org.apache.log4j.Logger;

import java.rmi.server.UID;


/**
 * This class allows SPP OSCache JMS listeners to be added even if the JMS server is not available
 * during cache initialization. It allows also 're-activate' the listener if it could not be initialized
 * properly when the cache was created and the listener was added to it.
 * <p>
 * The issues solved by this class don't happen in a single server deployments. In the WebLogic clusters
 * the JMS server is always pinned to a single server. When starting the servers of the cluster, the
 * server hosting the JMS server may start after the other servers of the cluster and therefore the
 * JMS listener would not be properly initialized and added to the cache. This class provides 3
 * OSCache configuration parameters that enable this:
 * <ul>
 * 	<li>cache.cluster.jms.on_failed_init_retries - is the number of attempts that the listener will
 * 	do when connecting to the JMS server</li>
 * 	<li>cache.cluster.jms.on_failed_init_wait_seconds - is the interval in seconds between connection
 * 	attempts. The default values are 3 attempts and 5 secondes</li>
 * 	<li>cache.cluster.jms.initialize_asynchronously - is the flag indicating that the initialization
 * 	of the listener must happen in its own thread. The default value of this parameter is <tt>false</tt>.
 *  This parameter is useful when you have a cache-related code deployed in WebLogic 9.1 cluster.
 * 	It turns out that in such a situation, the startup code such as <tt>ServletContextListener</tt>
 *  implemenatations are executed before the JNDI tree is retrieved from other cluster servers. This
 *  is problematic for the managed servers that do not hast the JMS server. In order to handle such
 *  a case you can set this flag to <tt>true</tt> and adjust appropriately on_failed_init_retries and
 * 	on_failed_init_wait_seconds parameters. This way the listener will be added in its own thread, in
 *  parallel to the server retrieving the JNDI tree.</li>
 * </ul>
 * <p>
 * Another service provided by this class is to recover from a lost connection to the JMS server, e.g.
 * if the WebLogic server hosting JMS server goes down. This connection can be reactivated by flushing
 * the cache entry called <tt>com.hp.spp.cache.jms.TopicConnectionRecovery</tt>.
 * <p>
 *
 * <b>WebLogic 9.1 Clustere WARNING<b>
 * Once again, make sure that you use initialize_asynchronously, on_failed_init_retries, on_failed_init_wait_seconds
 * parameters when you run in a WebLogic 9.1 clusters and you have a startup code that relies on the
 * cache.
 */
public abstract class JMSListenerHelper implements Runnable {

	private static final Logger mLog = Logger.getLogger(JMSListenerHelper.class);
	public static final String JMS_RECOVERY_CACHE_KEY = "com.hp.spp.cache.jms.TopicConnectionRecovery";

	private static final String CFG_INIT_ASYNC = "cache.cluster.jms.initialize_asynchronously";
	private static final String CFG_FAILED_INIT_WAIT_SEC = "cache.cluster.jms.on_failed_init_wait_seconds";
	private static final String CFG_FAILED_INIT_RETRIES = "cache.cluster.jms.on_failed_init_retries";
	private static final String CFG_TOPIC_FACTORY = "cache.cluster.jms.topic.factory";
	private static final String CFG_TOPIC_NAME = "cache.cluster.jms.topic.name";
	private static final String CFG_NODE_NAME = "cache.cluster.jms.node.name";

	private AbstractBroadcastingListener mListener;
	private Config mConfig;
	private com.opensymphony.oscache.base.Cache mCache;
	private boolean mInitialized = false;


	protected JMSListenerHelper(AbstractBroadcastingListener listener) {
		mListener = listener;
	}


	public void initialize(com.opensymphony.oscache.base.Cache cache, Config config) {
		mCache = cache;
		mConfig = config;

		String asyncInit = mConfig.getProperty(CFG_INIT_ASYNC);
		if (asyncInit != null && Boolean.valueOf(asyncInit).booleanValue()) {
			if (mLog.isInfoEnabled()) {
				mLog.info("Listener will be initialized in its own thread");
			}
			Thread thread = new Thread(this, "JMSListenerInitialization");
			thread.setDaemon(true);
			thread.start();
		}
		else {
			run();
		}
	}

	public void run() {
		int waitSeconds = 5;
		String waitStr = mConfig.getProperty(CFG_FAILED_INIT_WAIT_SEC);
		if (waitStr != null) {
			try {
				waitSeconds = Integer.parseInt(waitStr);
			}
			catch (NumberFormatException e) {
				mLog.warn("Value of '" + CFG_FAILED_INIT_WAIT_SEC + "' is not integer: " +
						waitStr + ". Will use default value: " + waitSeconds);
			}
		}

		int attempts = 3;
		String attemptsStr = mConfig.getProperty(CFG_FAILED_INIT_RETRIES);
		if (attemptsStr != null) {
			try {
				attempts = Integer.parseInt(attemptsStr);
			}
			catch (NumberFormatException e) {
				mLog.warn("Value of '" + CFG_FAILED_INIT_RETRIES + "' is not integer: " +
						attemptsStr + ". Will use default value: " + attempts);
			}
		}

		mConfig.set(CFG_NODE_NAME, new UID().toString());
		if (attempts <= 1) {
			attempts = 1;
		}
		mInitialized = false;
		for (int i = 0; !mInitialized && i < attempts; ++i) {
			try {
				superInitialize(mCache, mConfig);
				mInitialized = true;
				mLog.info("Listener initialization successful");
			}
			catch (InitializationException e) {
				if (i == attempts - 1) {
					// If I throw an exception here, the listener will not be added to the list.
					// Therefore only logging the error here. The failed listener can be reactivated
					// by flushing "com.hp.spp.cache.jms.TopicConnectionRecovery" entry.
					mLog.error(
							"Unable to initialize the listener but the listener will be added to the list. " +
							"This is probably due to topic factory (" + mConfig.getProperty(CFG_TOPIC_FACTORY) +
							") or topic (" + mConfig.getProperty(CFG_TOPIC_NAME) + ") missing in JNDI tree, " +
							"or a problem while connecting to JMS server. This listener will be marked as uninitialized. " +
							"You can reinitialize it through cacheAdmin.jsp page or by flushing cache entry '" + JMS_RECOVERY_CACHE_KEY + "'.");
				}
				else {
					mLog.warn("Intializing listener failed: " + e + ". Will try " + (attempts - i - 1) +
							" more time(s)" + (waitSeconds > 0 ? " in " + waitSeconds + " second(s)." : "."));
					if (waitSeconds > 0) {
						try {
							Thread.sleep(waitSeconds * 1000);
						}
						catch (InterruptedException e1) {
							// If exception is thrown from here the listener won't be added to the list.
							// Therefore only logging an error.
							mLog.error("Error occured when waiting for next initialization attempt", e1);
						}
					}
				}
			}
		}
	}

	public void cacheEntryFlushed(CacheEntryEvent cacheEntryEvent) {
		if (JMS_RECOVERY_CACHE_KEY.equals(cacheEntryEvent.getKey())) {
			try {
				mListener.finialize();
			}
			catch (Throwable e) {
				// I'll ignore any errors during the finalization to make sure to make sure I have
				// an opportunity to re-initialize it later.
				mLog.error("Error during JMS connection recovery", e);
			}
			initialize(mCache, mConfig);
		}
		else {
			if (!mInitialized) {
				mLog.error(
						"The cache flush notification cannot be sent as the listener was not initialized successfully. " +
						"You can reinitialize it through cacheAdmin.jsp page or by flushing cache entry '" + JMS_RECOVERY_CACHE_KEY + "'.");
			}
			else {
				try {
					superCacheEntryFlushed(cacheEntryEvent);
				}
				catch (Exception e) {
					mLog.error(
							"Error occured while sending cache flush notification. " +
							"This is probably due to topic factory (" + mConfig.getProperty(CFG_TOPIC_FACTORY) +
							") or topic (" + mConfig.getProperty(CFG_TOPIC_NAME) + ") missing in JNDI tree, " +
							"or a problem while connecting to JMS server. This listener will be marked as uninitialized. " +
							"You can reinitialize it through cacheAdmin.jsp page or by flushing cache entry '" + JMS_RECOVERY_CACHE_KEY + "'.", e);
					mInitialized = false;
				}
			}
		}
	}


	protected abstract void superInitialize(com.opensymphony.oscache.base.Cache cache, Config config) throws InitializationException;

	protected abstract void superCacheEntryFlushed(CacheEntryEvent cacheEntryEvent);

}
