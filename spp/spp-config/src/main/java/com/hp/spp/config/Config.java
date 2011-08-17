package com.hp.spp.config;

import com.hp.spp.cache.Cache;
import org.apache.log4j.Logger;


public class Config {
	private static final Logger mLog = Logger.getLogger(Config.class);

	private static ConfigImpl mConfig;

	/**
	 * Prefix used to specifiy a server-specific value.
	 * For Vignette server use the prefix 'portal' (must be set in an web app listener) and for
	 * WSRP use 'wsrp'. This prefix allow to define server-specific values, e.g. portal.SPP.https.port
	 * and wsrp.SPP.https.port and get their values by simply using non-profixed key. Doing
	 * Config.getValue("SPP.https.port") would return on Vignette the value for key prefixed with 'portal'
	 * and on WSRP server the value for key prefixed with 'wsrp'. If no prefixed value is found
	 * it will attempt to find non prefixed key - in our case it would return the value associated
	 * with "SPP.https.port" or null if the value is not defined.
	 */
	private static String mPrefix;

	private Config() {}

	/**
	 * @return Singleton instance of the configuration
	 */
	public static synchronized ConfigImpl getConfig() {
		if (mConfig == null) {
			mConfig = create(mPrefix);
		}
		return mConfig;
	}

	public static void setPrefix(String prefix) {
		if (mLog.isInfoEnabled()) {
			mLog.info("Setting config prefix to '" + prefix + "'");
		}
		mPrefix = prefix;
		// if mConfig is null we do nothing - it will be properly created during the first call
		// to getConfig method.
		// if mConfig is not null we have to re-create it to take into account the new prefix.
		if (mConfig != null) {
			// we can recreate it here - the cache will be kept as it's a singleton
			mConfig = create(mPrefix);
		}
	}

	private static ConfigImpl create(String prefix) {
		// this is here only for MGM so they can have their own Vignette instance without all SPP
		// code and database stuff deployed there
		if ("map".equals(System.getProperty("spp.config"))) {
			mLog.warn("spp.config system property set to 'map' - will use HashMapConfigEntryDAO implementation");
			return new ConfigImpl(new HashMapConfigEntryDAO());
		}
		else {
			return new ConfigImpl(
					new PrefixingConfigEntryDAO(
							new CachingConfigEntryDAO(
									new JDBCConfigEntryDAO(),
									Cache.getInstance()),
							prefix));
		}
	}

	/**
	 * @see ConfigImpl#getValue(String)
	 */
	public static String getValue(String name) throws ConfigException {
		return getConfig().getValue(name);
	}

	/**
	 * @see ConfigImpl#getValue(String, String)
	 */
	public static String getValue(String name, String defaultValue) {
		return getConfig().getValue(name, defaultValue);
	}

	/**
	 * @see ConfigImpl#getIntValue(String)
	 */
	public static int getIntValue(String name) throws ConfigException {
		return getConfig().getIntValue(name);
	}

	/**
	 * @see ConfigImpl#getIntValue(String, int)
	 */
	public static int getIntValue(String name, int defaultValue) {
		return getConfig().getIntValue(name, defaultValue);
	}

	/**
	 * @see ConfigImpl#getBooleanValue(String)
	 */
	public static boolean getBooleanValue(String name) throws ConfigException {
		return getConfig().getBooleanValue(name);
	}

	/**
	 * @see ConfigImpl#getBooleanValue(String, boolean)
	 */
	public static boolean getBooleanValue(String name, boolean defaultValue) {
		return getConfig().getBooleanValue(name, defaultValue);
	}

	/** 
	 * @see ConfigImpl#set(ConfigEntry entry)
	 */
	public static void set(ConfigEntry entry) throws ConfigException {
		getConfig().set(entry);
	}
}
