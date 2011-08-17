package com.hp.spp.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * Stores a singleton version of OSCache GeneralCacheAdministor that by default uses HTTP cache
 * updates notifications. The cache size is unlimited.
 */
public class Cache {

	private static final Logger mLog = Logger.getLogger(Cache.class);
	private static GeneralCacheAdministrator mCache;
	private static Properties mCacheConfig;

	private Cache() {}

	public static synchronized GeneralCacheAdministrator getInstance() {
		if (mCache == null) {
			mCache = new GeneralCacheAdministrator(getConfig());
		}
		return mCache;
	}

	public static synchronized Properties getConfig() {
		if (mCacheConfig == null) {
			mCacheConfig = new Properties();
			InputStream is = Cache.class.getResourceAsStream("/spp_oscache.properties");
			if (is == null) {
				throw new IllegalStateException("Unable to find in classpath the cache configuration");
			}
			try {
				mCacheConfig.load(is);
			}
			catch (IOException e) {
				throw new RuntimeException("Error loading cache configuration", e);
			}
			finally {
				try {
					is.close();
				}
				catch (IOException e) {
					throw new RuntimeException("Error loading cache configuration", e);
				}
			}

			if (mLog.isDebugEnabled()) {
				mLog.debug("Cache configuration loaded: " + mCacheConfig);
			}
		}
		return mCacheConfig;
	}
}
