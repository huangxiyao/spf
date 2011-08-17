/**
 * 
 */
package com.hp.spp.portal.login.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.spp.cache.Cache;
import com.hp.spp.portal.login.model.SPPGuestUser;
import com.hp.spp.profile.Constants;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * DAO implementation that use the Cache system.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public final class LoginDAOCacheImpl implements LoginDAO {

	/**
	 * Logger.
	 */
	private static Logger mLog = Logger.getLogger(LoginDAOCacheImpl.class);

	/**
	 * Instance for the singleton.
	 */
	private static LoginDAOCacheImpl mInstance;

	// force the use of cache
	private LoginDAOCacheImpl() {
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
		InputStream is = LoginDAOCacheImpl.class.getResourceAsStream("/loginCore.properties");
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
	public static LoginDAO getInstance() {
		if (mIsCacheEnabled) {
			if (mInstance == null) {
				mInstance = new LoginDAOCacheImpl();
			}

			return mInstance;
		} else
			return LoginDAOSQLManagerImpl.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getCustomErrorMessage(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public String getCustomErrorMessage(String site, String errorCode, String localeCode) {
		String customErrorMessage = "";
		String key = new StringBuffer("customErrorMessage").append('-').append(site).append(
				'-').append(errorCode).append('-').append(localeCode).toString();
		try {
			customErrorMessage = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				customErrorMessage = LoginDAOSQLManagerImpl.getInstance()
						.getCustomErrorMessage(site, errorCode, localeCode);
				// Store in the cache
				mCacheAdministrator.putInCache(key, customErrorMessage);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				customErrorMessage = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return customErrorMessage;
	}

    /*
      * (non-Javadoc)
      *
      * @see com.hp.spp.portal.login.dao.LoginDAO#getDisplayErrorMessage(java.lang.String,
      *      java.lang.String)
      */
	public boolean getDisplayErrorMessage(String site, String errorCode) {
		boolean displayErrorMessage = true;
		String key = new StringBuffer("displayErrorMessage").append('-').append(site).append(
				'-').append(errorCode).toString();
		try {
			displayErrorMessage = "true".equals((String) mCacheAdministrator
					.getFromCache(key));
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				displayErrorMessage = LoginDAOSQLManagerImpl.getInstance()
						.getDisplayErrorMessage(site, errorCode);
				// Store in the cache
				mCacheAdministrator.putInCache(key, String.valueOf(displayErrorMessage));
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				displayErrorMessage = "true".equals((String) nre.getCacheContent());
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return displayErrorMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getHpappid(java.lang.String)
	 */
	public String getHpappid(String site) {
		String hpappid = "";
		String key = new StringBuffer("hpappid").append('-').append(site).toString();
		try {
			hpappid = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				hpappid = LoginDAOSQLManagerImpl.getInstance().getHpappid(site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, hpappid);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				hpappid = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return hpappid;
	}

    /*
	 * (non-Javadoc)
	 *
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getMessageFromLabel(java.lang.String, java.lang.String, java.lang.String)
	 */
    public String getMessageFromLabel(String label, String localeCode, String siteName) {
       String key = new StringBuilder().append(label).append("-").append(localeCode).append("-").append(siteName).toString();

        String labelValue;
        try {
			labelValue = (String) mCacheAdministrator.getFromCache(key);
		}
		catch (NeedsRefreshException e) {
			boolean updated = false;
			try {
				labelValue = LoginDAOSQLManagerImpl.getInstance().getMessageFromLabel(label, localeCode, siteName);
				mCacheAdministrator.putInCache(key, labelValue);
				updated = true;
			}
			finally {
				if (!updated) {
					mCacheAdministrator.cancelUpdate(key);
				}
			}
		}
		return labelValue;
	}

    /*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getLabel(java.lang.String, java.lang.String)
	 */
	public String getMessageFromLabel(String label, String localeCode) {
		String localizedLabel = "";
		String key = new StringBuffer("localizedLabel").append('-').append(label).append('-')
				.append(localeCode).toString();
		try {
			localizedLabel = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				localizedLabel = LoginDAOSQLManagerImpl.getInstance().getMessageFromLabel(label,
						localeCode);
				// Store in the cache
				mCacheAdministrator.putInCache(key, localizedLabel);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				localizedLabel = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return localizedLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getLabel(java.lang.String)
	 */
	public String getMessageFromLabel(String label) {
		return getMessageFromLabel(label, Constants.DEFAULT_LANGUAGE.substring(0, 2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getLocalizationInURL(java.lang.String)
	 */
	public boolean getLocalizationInURL(String site) {
		boolean localizationInURL = true;
		String key = new StringBuffer("localizationInURL").append('-').append(site).toString();
		try {
			localizationInURL = "true".equals((String) mCacheAdministrator
					.getFromCache(key));
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				localizationInURL = LoginDAOSQLManagerImpl.getInstance().getLocalizationInURL(
						site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, String.valueOf(localizationInURL));
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				localizationInURL = "true".equals((String) nre.getCacheContent());
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return localizationInURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getPathMenuLandingPage(java.lang.String)
	 */
	public String getPathMenuLandingPage(String site) {
		String pathMenuLandingPage = "";
		String key = new StringBuffer("pathMenuLandingPage").append('-').append(site)
				.toString();
		try {
			pathMenuLandingPage = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				pathMenuLandingPage = LoginDAOSQLManagerImpl.getInstance()
						.getPathMenuLandingPage(site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, pathMenuLandingPage);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				pathMenuLandingPage = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return pathMenuLandingPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getPathMenuURL(java.lang.String,
	 *      java.lang.String)
	 */
	public String getPathMenuURL(String site, String errorCode) {
		String pathMenuURL = "";
		String key = new StringBuffer("pathMenuURL").append('-').append(site).append('-')
				.append(errorCode).toString();
		try {
			pathMenuURL = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				pathMenuURL = LoginDAOSQLManagerImpl.getInstance().getPathMenuURL(site,
						errorCode);
				// Store in the cache
				mCacheAdministrator.putInCache(key, pathMenuURL);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				pathMenuURL = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return pathMenuURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getPortletId(java.lang.String)
	 */
	public String getPortletId(String site) {
		String portletId = "";
		String key = new StringBuffer("portletId").append('-').append(site).toString();
		try {
			portletId = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				portletId = LoginDAOSQLManagerImpl.getInstance().getPortletId(site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, portletId);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				portletId = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return portletId;
	}


	public static GeneralCacheAdministrator getCacheAdministrator() {
		return mCacheAdministrator;
	}

	public static void setCacheAdministrator(GeneralCacheAdministrator cacheAdministrator) {
		mCacheAdministrator = cacheAdministrator;
	}

	public boolean getPersistSimulation(String site) {
		boolean persistSimulation = true;
		String key = new StringBuffer("persistSimulation").append('-').append(site).toString();
		try {
			persistSimulation = "true".equals((String) mCacheAdministrator
					.getFromCache(key));
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				persistSimulation = LoginDAOSQLManagerImpl.getInstance().getPersistSimulation(
						site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, String.valueOf(persistSimulation));
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				persistSimulation = "true".equals((String) nre.getCacheContent());
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return persistSimulation;
	}

	public String getLogoutMenuItemId(String site) {
		String logoutMenuItemId = "";
		String key = new StringBuffer("hpappid").append('-').append(site).toString();
		try {
			logoutMenuItemId = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				logoutMenuItemId = LoginDAOSQLManagerImpl.getInstance().getLogoutMenuItemId(site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, logoutMenuItemId);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				logoutMenuItemId = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return logoutMenuItemId;
	}

	public String getPathStopSimulationPage(String site) {
		String stopSimulationMenuItemId = "";
		String key = new StringBuffer("stopSimulationMenuItem").append('-').append(site).toString();
		try {
			stopSimulationMenuItemId = (String) mCacheAdministrator.getFromCache(key);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + key + "] is not found in the cache");
			}
			try {
				// Get the value form database
				stopSimulationMenuItemId = LoginDAOSQLManagerImpl.getInstance().getPathStopSimulationPage(site);
				// Store in the cache
				mCacheAdministrator.putInCache(key, stopSimulationMenuItemId);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + key
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				stopSimulationMenuItemId = (String) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(key);
			}
		}
		return stopSimulationMenuItemId;
	}
	
	/**
	 * This method retrieves guest user and its preferred language code
	 * corresponding to only computed lookup language code from SPP_LOCALE table.
	 * To avoid multiple database calls we have put both the guestuser 
	 * and the corresponding preferred language code in the cache.  
	 */
	public SPPGuestUser getGuestUser(String languageCode){
		SPPGuestUser sppGuestLocal = null;
		// Key used to store/retrieve SPPGuestLocale object corresponding to guest user in cache
		String guestUserKey = new StringBuffer("spplocale").append('-').append(languageCode).toString();
		try {
			sppGuestLocal = (SPPGuestUser) mCacheAdministrator.getFromCache(guestUserKey);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + guestUserKey + "] is not found in the cache");
			}
			try {
				// Since the key was not found in cache we do a direct database call to fetch the guest user
				// and corresponding preferred langauge code as part of SPPLocale object
				sppGuestLocal = (SPPGuestUser)LoginDAOSQLManagerImpl.getInstance().getGuestUser(languageCode);
				mCacheAdministrator.putInCache(guestUserKey, sppGuestLocal);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + guestUserKey
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				sppGuestLocal = (SPPGuestUser) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(guestUserKey);
			}
		}
		return sppGuestLocal;
	}
	
	/**
	 * This method retrieves guest user and its preferred language code
	 * corresponding to computed lookup language code and site identifier
	 * from SPP_LOCALE table. To avoid multiple database calls we have put
	 * both the guestuser and the corresponding preferred language code in
	 * the cache.  
	 */
	public SPPGuestUser getGuestUserBySite(String languageCode,String siteIdentifier){
		SPPGuestUser sppGuestLocal = null;
		String guestUserKey = new StringBuffer("spplocale").append('-').append(languageCode)
					.append('-').append(siteIdentifier).toString();
		try {
			sppGuestLocal = (SPPGuestUser)mCacheAdministrator.getFromCache(guestUserKey);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + guestUserKey + "] is not found in the cache");
			}
			try {
				// Since the key was not found in cache we do a direct database call to fetch the guest user
				// and corresponding preferred langauge code
				sppGuestLocal = (SPPGuestUser)LoginDAOSQLManagerImpl.getInstance().getGuestUserBySite(languageCode,siteIdentifier);
				mCacheAdministrator.putInCache(guestUserKey, sppGuestLocal);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + guestUserKey
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				sppGuestLocal = (SPPGuestUser) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(guestUserKey);
			}
		}
		return sppGuestLocal;
	}

	/**
	 * This method retrieves guest user and its preferred language code
	 * corresponding to computed lookup language code and country code
	 * from SPP_LOCALE table. To avoid multiple database calls we have 
	 * put both the guestuser and the corresponding preferred language 
	 * code in the cache.  
	 */
	public SPPGuestUser getGuestUserByCountry(String languageCode,String countryCode){
		SPPGuestUser sppGuestLocal = null;
		String guestUserKey = new StringBuffer("spplocale").append('-').append(languageCode)
					.append('-').append(countryCode).toString();
		try {
			sppGuestLocal = (SPPGuestUser)mCacheAdministrator.getFromCache(guestUserKey);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + guestUserKey + "] is not found in the cache");
			}
			try {
				// Since the key was not found in cache we do a direct database call to fetch the guest user
				// and corresponding preferred langauge code
				sppGuestLocal = (SPPGuestUser)LoginDAOSQLManagerImpl.getInstance().getGuestUserByCountry(languageCode, countryCode);
				// Store in the cache. We store guestUser as null to avoid database call for every public page access
				mCacheAdministrator.putInCache(guestUserKey, sppGuestLocal);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + guestUserKey
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				sppGuestLocal = (SPPGuestUser) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(guestUserKey);
			}
		}
		return sppGuestLocal;
	}
	
	/**
	 * This method retrieves guest user and its preferred language code
	 * corresponding to computed lookup language code, country code and
	 * site identifier from SPP_LOCALE table. To avoid multiple database
	 * calls we have put both the guestuser and the corresponding preferred
	 * language code in the cache.  
	 */
	public SPPGuestUser getGuestUser(String languageCode, String countryCode, String siteIdentifier){
		SPPGuestUser sppGuestLocal = null;
		String guestUserKey = new StringBuffer("spplocale").append('-').append(languageCode).append('-')
					.append(countryCode).append('-').append(siteIdentifier).toString();
		try {
			sppGuestLocal = (SPPGuestUser)mCacheAdministrator.getFromCache(guestUserKey);
		} catch (NeedsRefreshException nre) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("The [" + guestUserKey + "] is not found in the cache");
			}
			try {
				// Since the key was not found in cache we do a direct database call to fetch the guest user
				// and corresponding preferred langauge code
				sppGuestLocal = (SPPGuestUser)LoginDAOSQLManagerImpl.getInstance().getGuestUser(languageCode, countryCode, siteIdentifier);
				mCacheAdministrator.putInCache(guestUserKey, sppGuestLocal);
			} catch (Exception ex) {
				mLog.error("An error has occured while getting the [" + guestUserKey
						+ "] from the database and putting it in cache", ex);
				// We have the current content if we want fail-over.
				sppGuestLocal = (SPPGuestUser) nre.getCacheContent();
				// cancelUpdate is called if the cached content is not rebuilt
				mCacheAdministrator.cancelUpdate(guestUserKey);
			}
		}
		return sppGuestLocal;
	}	
}
