package com.hp.spp.groups.dao;

import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.hp.spp.groups.Site;
import com.hp.spp.cache.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * DAO implementation that use the Cache system.
 * 
 * @author bmollard
 * 
 */
public class SiteDAOCacheImpl implements SiteDAO {

	private static Logger mLog = Logger.getLogger(SiteDAO.class);

	/**
	 * Instance for the singleton.
	 */
	private static SiteDAOCacheImpl mInstance;

	/**
	 * The Cache administrator. It's an OSCache component
	 */
	private static GeneralCacheAdministrator mCacheAdministrator;

	/**
	 * boolean to know if the cache is enabled
	 */
	private static boolean mIsCacheEnabled;

	/**
	 * Constructor.
	 * 
	 */
	public SiteDAOCacheImpl() {

	}

	static {
		init();
	}

	/**
	 * Initialize the cache.
	 * 
	 */
	private static void init() {
		InputStream is = SiteDAOCacheImpl.class.getResourceAsStream("/groupCore.properties");
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
			}
			catch (IOException e) {
				mLog.error("Unable to load cache configuration. Cache system is NOT enabled!", e);
			}
		}
		else {
			mLog.error("Unable to load cache configuration. Cache system is NOT enabled!");
		}
		mIsCacheEnabled = (mCacheAdministrator != null);
	}

	/**
	 * Return the singleton instance of this class.
	 * 
	 * @return the instance
	 */
	public static SiteDAOCacheImpl getInstance() {

		if (mInstance == null) {
			mInstance = new SiteDAOCacheImpl();
		}

		return mInstance;
	}

	/**
	 * Get the site from its key. Try to get it from the cache, if not exsits, load it from the
	 * DB
	 * 
	 * @param key : the key to get the Site
	 * @return Site the loaded site
	 */
	public Site load(long key) {

		if (mIsCacheEnabled) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Load site with id [" + key + "] from Cache");
			}

			Site myValue;
			try {
				// Try to get the Site from the cache
				myValue = (Site) mCacheAdministrator.getFromCache(String.valueOf(key));
			} catch (NeedsRefreshException nre) {
				// the Site is not found in the Cache, try to load it from the DB
				if (mLog.isDebugEnabled()) {
					mLog.debug("The site [" + key + "] is not found in the cache");
				}
				try {
					// Get the value form database
					myValue = SiteDAOHibernateImpl.getInstance().load(key);
					// Store in the cache
					mCacheAdministrator.putInCache(myValue.getName(), myValue);
				} catch (Exception ex) {
					mLog.error("An error has occured while getting a site "
							+ "from the database and putting it in cache", ex);
					// We have the current content if we want fail-over.
					myValue = (Site) nre.getCacheContent();
					// It is essential that cancelUpdate is called if the
					// cached content is not rebuilt
					mCacheAdministrator.cancelUpdate(myValue.getName());
				}
			}

			return myValue;
		} else {
			return SiteDAOHibernateImpl.getInstance().load(key);
		}
	}

	/**
	 * Get the site from its name. Try to get it from the cache. if not exsits, load it from
	 * the DB.
	 * 
	 * @param siteName the name of the site to get
	 * @return Site the loaded site
	 */
	public Site loadByName(String siteName) {

		if (mIsCacheEnabled) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Load site with name [" + siteName + "] from Cache");
			}

			Site myValue;
			try {
				// Try to get the Site from the cache
				myValue = (Site) mCacheAdministrator.getFromCache(siteName);
			} catch (NeedsRefreshException nre) {
				// the Site is not found in the Cache, try to load it from the DB
				if (mLog.isDebugEnabled()) {
					mLog.debug("The site [" + siteName + "] is not found in the cache");
				}
				try {
					// Get the value (probably by calling an EJB)
					myValue = SiteDAOHibernateImpl.getInstance().loadByName(siteName);
					// Store in the cache
					mCacheAdministrator.putInCache(siteName, myValue);
				} catch (Exception ex) {
					mLog.error("An error has occured while getting a site "
							+ "from the database and putting it in cache", ex);
					// We have the current content if we want fail-over.
					myValue = (Site) nre.getCacheContent();
					// It is essential that cancelUpdate is called if the
					// cached content is not rebuilt
					mCacheAdministrator.cancelUpdate(siteName);
				}
			}

			return myValue;
		} else {
			return SiteDAOHibernateImpl.getInstance().loadByName(siteName);
		}
	}

	/**
	 * Remove the Site that correpsnds tothe key from the cache.
	 * 
	 * @param key the key identifying the Site
	 */
	public void removeFromCache(String key) {
		if (mIsCacheEnabled) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Remove the entry [" + key + "] from the cache");
			}
			mCacheAdministrator.removeEntry(key);
			mCacheAdministrator.flushEntry(key);
		}
	}

	/**
	 * Add a site into the cache. The site is cached under its name.
	 * 
	 * @param siteToCache The site to be cached
	 */
	public void putInCache(Site siteToCache) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("Add the Site [" + siteToCache.getName() + "] into the cache");
		}
		mCacheAdministrator.putInCache(siteToCache.getName(), siteToCache);
	}

	/**
	 * Return true if the cache contain one site corresponding to the key.
	 * 
	 * @param key key to search in the cache
	 * @return true if the key is in cache
	 */
	public boolean isInCache(String key) {
		if (mIsCacheEnabled) {
			try {
				// Get from the cache
				mCacheAdministrator.getFromCache(key);
				return true;
			} catch (NeedsRefreshException nre) {
				return false;
			}
		} else {
			return false;
		}

	}

	public int getNbElementInCache() {
		if (mIsCacheEnabled) {

			return mCacheAdministrator.getCache().getNbEntries();
		} else {
			return 0;
		}
	}

	public static GeneralCacheAdministrator getAdmin() {
		return mCacheAdministrator;
	}

	public static void setAdmin(GeneralCacheAdministrator admin) {
		SiteDAOCacheImpl.mCacheAdministrator = admin;
	}

	/**
	 * Find Sites form a query not used in cache context, call the Hibernate one.
	 * 
	 * @param query HQL query to get a list of sites
	 * @return list of sites
	 */
	public List find(String query) {
		return SiteDAOHibernateImpl.getInstance().find(query);
	}

	/**
	 * Save the site in BD, call the hibernate implementation method.
	 * 
	 * @return status of save operation
	 * @param s the site to save
	 */
	public Long save(Site s) {
		return SiteDAOHibernateImpl.getInstance().save(s);
	}

	/**
	 * update the site in BD, call the hibernate implementation method.
	 * 
	 * @param s the site to update
	 */
	public void update(Site s) {
		SiteDAOHibernateImpl.getInstance().update(s);
	}

	/**
	 * delete the site in BD, call the hibernate implementation method.
	 * 
	 * @param s the site to delete
	 */
	public void delete(Site s) {
		SiteDAOHibernateImpl.getInstance().delete(s);
	}

	/**
	 * Reset completely the cache
	 * 
	 */
	public static synchronized void recreateCache() {
		//remove the commented code if this works correctly
//		mLog.debug("Completely reset the cache");
//		SiteDAOCacheImpl.mCacheAdministrator.destroy();
//		init();
		SiteDAOCacheImpl.mCacheAdministrator.flushAll();
	}

}
