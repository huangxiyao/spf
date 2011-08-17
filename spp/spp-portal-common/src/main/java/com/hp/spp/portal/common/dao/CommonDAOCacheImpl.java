package com.hp.spp.portal.common.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.spp.cache.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class CommonDAOCacheImpl implements CommonDAO {

	private static Logger mLog = Logger.getLogger(CommonDAOCacheImpl.class);

	// Instance for the singleton.
	private static CommonDAOCacheImpl mInstance;

	// force the use of cache
	private CommonDAOCacheImpl() {
	}

	// The Cache administrator. It's an OSCache component
	public static GeneralCacheAdministrator mCacheAdministrator;

	// boolean to know if the cache is enabled
	private static boolean mIsCacheEnabled;

	static {
		init();
	}

	/**
	 * Initialize the cache.
	 * 
	 */
	private static void init() {
		InputStream is = CommonDAOCacheImpl.class.getResourceAsStream("/commonCore.properties");
		if (is != null) {
			try {
				Properties prop = new Properties();
				prop.load(is);
				is.close();
				if ("1".equals(prop.getProperty("ENABLE_CACHE"))) {
					prop.remove("ENABLE_CACHE");
					mCacheAdministrator = Cache.getInstance();
				}
				if (mLog.isInfoEnabled()) {
					if (mCacheAdministrator == null) {
						mLog.info("Cache system is NOT enabled!");
					}
				}
			} catch (IOException e) {
				mLog.error("Unable to load cache configuration. Cache system is NOT enabled!",
						e);
			}
		} else {
			mLog.error("Unable to load cache configuration. Cache system is NOT enabled!");
		}
		mIsCacheEnabled = (mCacheAdministrator != null);
	}

	/**
	 * Return the singleton instance of this class or the class without cache
	 * 
	 * @return the instance
	 */
	public static CommonDAO getInstance() {
		if (mIsCacheEnabled) {
			if (mInstance == null) {
				mInstance = new CommonDAOCacheImpl();
			}

			return mInstance;
		} else
			return CommonDAOSQLManagerImpl.getInstance();
	}

	public String getUrlLocator(String userCountryCode, String userLangCode) {
		String urlLocator = "";
		String key = new StringBuffer("urlLocator").append('-').append(userCountryCode)
				.append('-').append(userLangCode).toString();
		try {
			urlLocator = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				urlLocator = CommonDAOSQLManagerImpl.getInstance().getUrlLocator(
						userCountryCode, userLangCode);
				// Store in the cache
				mCacheAdministrator.putInCache(key, urlLocator);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				urlLocator = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return urlLocator;
	}

	public String getUrlLocator(String userCountryCode) {
		String urlLocator = "";
		String key = new StringBuffer("urlLocator").append('-').append(userCountryCode)
				.append("-ByCountry").toString();
		try {
			urlLocator = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				urlLocator = CommonDAOSQLManagerImpl.getInstance().getUrlLocator(
						userCountryCode);
				// Store in the cache
				mCacheAdministrator.putInCache(key, urlLocator);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				urlLocator = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return urlLocator;
	}

	public String getUrlLocatorByLanguage(String userLanguageCode) {
		String urlLocator = "";
		String key = new StringBuffer("urlLocator").append('-').append(userLanguageCode)
				.append("-ByLanguage").toString();
		try {
			urlLocator = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				urlLocator = CommonDAOSQLManagerImpl.getInstance().getUrlLocatorByLanguage(
						userLanguageCode);
				// Store in the cache
				mCacheAdministrator.putInCache(key, urlLocator);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				urlLocator = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return urlLocator;
	}

	/* (non-Javadoc)
	 * @see com.hp.spp.portal.login.dao.CommonDAO#getSupportedSites()
	 */
	public Set getSupportedSites() {
		Set supportedSites = new HashSet();
		String key = new StringBuffer("supportedSites").toString();
		try {
			supportedSites = (Set) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				supportedSites = CommonDAOSQLManagerImpl.getInstance().getSupportedSites();
				// Store in the cache
				mCacheAdministrator.putInCache(key, supportedSites);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				supportedSites = (Set) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return supportedSites;
	}

	/* (non-Javadoc)
	 * @see com.hp.spp.portal.common.dao.CommonDAO#getUPSQueryId(java.lang.String)
	 */
	public String getUPSQueryId(String site) {
		String upsQueryId = "";
		String key = new StringBuffer("upsQueryId").append('-').append(site).toString();
		try {
			upsQueryId = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				upsQueryId = CommonDAOSQLManagerImpl.getInstance().getUPSQueryId(site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, upsQueryId);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				upsQueryId = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return upsQueryId;
	}

	/**
	 * Use directly CommonDAOSQLManagerImpl.getVignetteUserGroup as the data 
	 * are never put in the cache
	 * @deprecated
	 */
	public 	Set getVignetteUserGroup(String hppid) {
		return CommonDAOSQLManagerImpl.getInstance().getVignetteUserGroup(hppid);
	}

	/**
	 * Use directly CommonDAOSQLManagerImpl.getVignetteGroups as the data 
	 * are never put in the cache. 
	 * @deprecated
	 */
	public Set getVignetteGroups() {
		return CommonDAOSQLManagerImpl.getInstance().getVignetteGroups();
	}
	
	/**
	 * Use directly CommonDAOSQLManagerImpl.updateUserLastLoginDate as the data 
	 * are never put in the cache. 
	 * @deprecated
	 */
	public void updateUserLastLoginDate(String hppid, Date date) {
		CommonDAOSQLManagerImpl.getInstance().updateUserLastLoginDate(hppid, date);
	}
}
