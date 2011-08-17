/**
 * 
 */
package com.hp.spp.portal.common.sql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hp.spp.cache.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * DAO implementation that use the Cache system.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public final class PortalCommonDAOCacheImpl implements PortalCommonDAO {

	/** 
	 * Logger.
	 */
	private static Logger mLog = Logger.getLogger(PortalCommonDAOCacheImpl.class);

	/**
	 * Instance for the singleton.
	 */
	private static PortalCommonDAOCacheImpl mInstance;

	// force the use of cache
	private PortalCommonDAOCacheImpl() {
	}

	/**
	 * The Cache administrator. It's an OSCache component.
	 */
	private static GeneralCacheAdministrator mCacheAdministrator;

	/**
	 * boolean to know if the cache is enabled.
	 */
	private static boolean mIsCacheEnabled;

	static {
		init();
	}

	/**
	 * Initialize the cache.
	 * 
	 */
	private static void init() {
		InputStream is = PortalCommonDAOCacheImpl.class.getResourceAsStream("/commonCore.properties");
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
	 * Return the singleton instance of this class or the class without cache.
	 * 
	 * @return the instance
	 */
	public static PortalCommonDAO getInstance() {
		if (mIsCacheEnabled) {
			if (mInstance == null) {
				mInstance = new PortalCommonDAOCacheImpl();
			}

			return mInstance;
		} else
			return PortalCommonDAOSQLManagerImpl.getInstance();
	}


	/* (non-Javadoc)
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getSiteIdentifier(java.lang.String)
	 */
	public String getSiteIdentifier(String siteName) {
		String siteIdentifier = "";
		String key = new StringBuffer("siteIdentifier").append('-').append(siteName).toString();
		try {
			siteIdentifier = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				siteIdentifier = PortalCommonDAOSQLManagerImpl.getInstance().getSiteIdentifier(siteName);
				// Store in the cache
				mCacheAdministrator.putInCache(key, siteIdentifier);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				siteIdentifier = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return siteIdentifier;
	}

	/* (non-Javadoc)
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#isSiteAccessible(java.lang.String)
	 */
	public boolean isSiteAccessible(String siteURI) {
		boolean accessibleSite = true;
		String key = new StringBuffer("accessibleSite").append('-').append(siteURI).toString();
		try {
			accessibleSite = "true".equals((String) mCacheAdministrator
					.getFromCache(key));
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				accessibleSite = PortalCommonDAOSQLManagerImpl.getInstance().isSiteAccessible(
						siteURI);
				// Store in the cache
				mCacheAdministrator.putInCache(key, String.valueOf(accessibleSite));
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				accessibleSite = "true".equals((String) nre.getCacheContent());
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return accessibleSite;
	}

	/* (non-Javadoc)
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getAccessCode(java.lang.String)
	 */
	public String getAccessCode(String siteURI) {
		String accessCode = null;
		String key = new StringBuffer("accessCode").append('-').append(siteURI).toString();
		try {
			accessCode = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				accessCode = PortalCommonDAOSQLManagerImpl.getInstance().getAccessCode(siteURI);
				// Store in the cache
				mCacheAdministrator.putInCache(key, accessCode);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				accessCode = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return accessCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getPortalHomePageUrl(java.lang.String)
	 */
	public String getPortalHomePageUrl(String siteName) {
		String homePageUrl = null;
		String key = new StringBuffer("homePageUrl").append('-').append(siteName).toString();
		try {
			homePageUrl = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				homePageUrl = PortalCommonDAOSQLManagerImpl.getInstance().getPortalHomePageUrl(siteName);
				// Store in the cache
				mCacheAdministrator.putInCache(key, homePageUrl);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				homePageUrl = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return homePageUrl;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getSiteProtocol(java.lang.String)
	 */
	public String getSiteProtocol(String siteName)	{
		String protocol = null;
		String key = new StringBuffer("protocol").append('-').append(siteName).toString();
		try {
			protocol = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				protocol = PortalCommonDAOSQLManagerImpl.getInstance().getSiteProtocol(siteName);
				// Store in the cache
				mCacheAdministrator.putInCache(key, protocol);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				protocol = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return protocol;
	}

	public void setAccessCode(String siteName, String accessCode) {
		String key = new StringBuffer("accessCode").append('-').append(siteName).toString();
		try {
			PortalCommonDAOSQLManagerImpl.getInstance().setAccessCode(siteName, accessCode);
			mCacheAdministrator.flushEntry(key);
			if(mLog.isDebugEnabled()){
				mLog.debug("The ["+ key +"] is flushed from the cache");
			}
		} catch (Exception ex) {
				mLog.error("An error has occured while flushing and updating the [" + key
						+ "] from the database and putting it in cache", ex);
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
		}
	}

	public void setSiteAccessible(String siteName, int accessSite) {
		String key = new StringBuffer("accessibleSite").append('-').append(siteName).toString();
		try {
			PortalCommonDAOSQLManagerImpl.getInstance().setSiteAccessible(siteName, accessSite);
			mCacheAdministrator.flushEntry(key);
			if(mLog.isDebugEnabled()){
				mLog.debug("The ["+ key +"] is flushed from the cache");
			}
		} catch (Exception ex) {
				mLog.error("An error has occured while flushing and updating the [" + key
						+ "] from the database and putting it in cache", ex);
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
		}
	}
}
