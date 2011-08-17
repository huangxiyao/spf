package com.hp.spp.portal.login.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.spp.db.DB;
import com.hp.spp.portal.common.sql.PortalCommonDAOSQLManagerImpl;
import com.hp.spp.portal.common.user.User;
import com.hp.spp.portal.common.user.UserMapper;
import com.hp.spp.portal.common.util.Site;
import com.hp.spp.portal.common.util.SiteMapper;
import com.hp.spp.portal.login.model.CustomError;
import com.hp.spp.portal.login.model.CustomErrorMapper;
import com.hp.spp.portal.login.model.LoginLabel;
import com.hp.spp.portal.login.model.LoginLabelMapper;
import com.hp.spp.portal.login.model.SPPGuestUser;
import com.hp.spp.portal.login.model.SPPGuestUserMapper;
import com.hp.spp.portal.login.model.Workflow;
import com.hp.spp.portal.login.model.WorkflowMapper;
import com.hp.spp.profile.Constants;

/**
 * Class to access the data with SQL orders.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public final class LoginDAOSQLManagerImpl implements LoginDAO {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger
			.getLogger(LoginDAOSQLManagerImpl.class);

	/**
	 * The instance for the singleton.
	 */
	private static LoginDAOSQLManagerImpl instance;

	// force the use of singleton
	private LoginDAOSQLManagerImpl() {
	}

	/**
	 * Return a singleton of the DAO.
	 * 
	 * @return the instance
	 */
	public static LoginDAOSQLManagerImpl getInstance() {
		if (null == instance) {
			instance = new LoginDAOSQLManagerImpl();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getURLLandingPage(java.lang.String)
	 */
	public String getPathMenuLandingPage(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getLandingPage();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getLocalizationInURL(java.lang.String)
	 */
	public boolean getLocalizationInURL(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getLocaleInUrl();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getTargetURL(java.lang.String,
	 *      java.lang.String)
	 */
	public String getPathMenuURL(String site, String errorCode) {
		Object[] args = { site, errorCode };
		String query = "SELECT PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE FROM SPP_WORKFLOW_ERROR WHERE PORTAL = ? AND ERROR_CODE = ?";
		Object result = DB.queryForObject(query, new WorkflowMapper(), args, new int[] { Types.VARCHAR, Types.INTEGER});	
		if (result != null && result instanceof Workflow)
			return ((Workflow) result).getTargetURL();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getDisplayErrorMessage(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean getDisplayErrorMessage(String site, String errorCode) {
		Object[] args = { site, errorCode };
		String query = "SELECT PORTAL, ERROR_CODE, TARGET_URL, DISPLAY_MESSAGE FROM SPP_WORKFLOW_ERROR WHERE PORTAL = ? AND ERROR_CODE = ? ";
		Object result = DB.queryForObject(query, new WorkflowMapper(), args, new int[] { Types.VARCHAR, Types.INTEGER});
		if (result != null && result instanceof Workflow)
			return ((Workflow) result).isDisplayErrorMessage();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getCustomErrorMessage(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public String getCustomErrorMessage(String site, String errorCode,
			String localeCode) {
		Object[] args = { site, errorCode, localeCode };
		String query = "SELECT PORTAL, ERROR_CODE, LOCALE, CUSTOM_ERROR_MESSAGE "
				+ "FROM SPP_CUSTOM_ERROR WHERE PORTAL = ? AND ERROR_CODE = ? AND LOCALE = ?";

		CustomError result = DB.queryForObject(query, new CustomErrorMapper(), args,
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR });
		if (result != null) {
			return result.getCustomErrorMessage();
		}

		if (mLog.isDebugEnabled()) {
			String error = "No CUSTOM_ERROR in database for portal [" + site
					+ "], errorCode [" + errorCode + "], localeCode ["
					+ localeCode + "]";
			mLog.debug(error);
		}
		return null;
	}

    public String getMessageFromLabel(String label, String localeCode, String siteName) {
        Object[] args = { label, localeCode, siteName };
		String query = "SELECT LOCALE, LABEL, MESSAGE, SITE_NAME FROM SPP_LOGIN_LABEL WHERE LABEL = ? AND LOCALE = ? AND SITE_NAME = ?";
		LoginLabel result = DB.queryForObject(query, new LoginLabelMapper(), args);
		if (result != null) {
			return result.getMessage();
		}

		if (localeCode.length() > 2){
			args[1] = localeCode.substring(0, 2);
			result = DB.queryForObject(query, new LoginLabelMapper(), args);
			if (result != null) {
				return result.getMessage();
			}
		}

		if (mLog.isDebugEnabled()) {
			String error = "No SPP_LOGIN_LABEL in database for label [" + label
					+ "], localeCode [" + localeCode + "], siteName [" + siteName + "]";
			mLog.debug(error);
		}
		return null;
    }

    public String getMessageFromLabel(String label, String localeCode) {
		Object[] args = { label, localeCode };
		String query = "SELECT LOCALE, LABEL, MESSAGE, SITE_NAME FROM SPP_LOGIN_LABEL WHERE SITE_NAME IS NULL AND LABEL = ? AND LOCALE = ?";
		LoginLabel result = DB.queryForObject(query, new LoginLabelMapper(), args);
		if (result != null) {
			return result.getMessage();
		}

		if (localeCode.length() > 2){
			args[1] = localeCode.substring(0, 2);
			result = DB.queryForObject(query, new LoginLabelMapper(), args);
			if (result != null) {
				return result.getMessage();
			}
		}

		if (mLog.isDebugEnabled()) {
			String error = "No SPP_LOGIN_LABEL in database for label [" + label
					+ "], localeCode [" + localeCode + "]";
			mLog.debug(error);
		}
		return null;
	}

	public String getMessageFromLabel(String label) {
		return getMessageFromLabel(label, Constants.DEFAULT_LANGUAGE.substring(
				0, 2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getPortletId(java.lang.String)
	 */
	public String getPortletId(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getPortletId();
		return null;
	}

	public String getHpappid(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getHpappid();
		return null;
	}

	public boolean getPersistSimulation(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getPersistSimulation();
		return false;
	}
	

	public String getLogoutMenuItemId(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getLogoutPage();
		return null;
	}

	public String getPathStopSimulationPage(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getStopSimulationPage();
		return null;
	}
	
	/*
	 * Retrieves guest user based on computed lookup language
	 * from SPP_LOCALE table
	 */
	public SPPGuestUser getGuestUser(String language){
		String query = "SELECT LANGUAGE_CODE, COUNTRY_CODE, SITE_IDENTIFIER, "
		+ "GUEST_USER, PREFERRED_LANGUAGE_CODE "
		+ "FROM SPP_LOCALE WHERE LANGUAGE_CODE = ?";
		
		Object[] args = { language };
		Object result = DB.queryForObject(query, new SPPGuestUserMapper(), args, new int[] { Types.VARCHAR });
		if (result != null && result instanceof SPPGuestUser){
			mLog.debug("Preferred Language Code : "+((SPPGuestUser)result).getPreferredLanguageCode());
			return (SPPGuestUser)result;
		}
		return null;
	}
	
	/*
	 * Retrieves guest user based on computed lookup language
	 * and site identifier from SPP_LOCALE table
	 */
	public SPPGuestUser getGuestUserBySite(String languageCode,String siteIdentifier){
		String query = "SELECT LANGUAGE_CODE, COUNTRY_CODE, SITE_IDENTIFIER, "
			+ "GUEST_USER, PREFERRED_LANGUAGE_CODE FROM SPP_LOCALE WHERE LANGUAGE_CODE = ? AND SITE_IDENTIFIER = ?";
		
		Object[] args = { languageCode , siteIdentifier };
		Object result = DB.queryForObject(query, new SPPGuestUserMapper(), args, new int[] { Types.VARCHAR, Types.VARCHAR});
		if (result != null && result instanceof SPPGuestUser){
			return (SPPGuestUser)result;
		}
		return null;
	}
	
	/*
	 * Retrieves guest user based on computed lookup language
	 * and country from SPP_LOCALE table
	 */
	public SPPGuestUser getGuestUserByCountry(String languageCode,String countryCode){
		String query = "SELECT LANGUAGE_CODE, COUNTRY_CODE, SITE_IDENTIFIER, "
			+ "GUEST_USER, PREFERRED_LANGUAGE_CODE FROM SPP_LOCALE WHERE LANGUAGE_CODE = ? AND COUNTRY_CODE = ?";
		
		Object[] args = { languageCode, countryCode };
		Object result = DB.queryForObject(query, new SPPGuestUserMapper(), args, new int[] { Types.VARCHAR, Types.VARCHAR});
		if (result != null && result instanceof SPPGuestUser){
			return (SPPGuestUser)result;
		}
		return null;
	}
	/*
	 * Retrieves guest user based on computed lookup language
	 * country code and site identifier from SPP_LOCALE table
	 */
	public SPPGuestUser getGuestUser(String languageCode,String countryCode,String siteIdentifier){
		String query = "SELECT LANGUAGE_CODE, COUNTRY_CODE, SITE_IDENTIFIER, "
			+ "GUEST_USER, PREFERRED_LANGUAGE_CODE FROM SPP_LOCALE WHERE LANGUAGE_CODE = ? AND COUNTRY_CODE =? AND SITE_IDENTIFIER = ?";
		
		Object[] args = { languageCode, countryCode, siteIdentifier };
		Object result = DB.queryForObject(query, new SPPGuestUserMapper(), args, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
		if (result != null && result instanceof SPPGuestUser){
			return (SPPGuestUser)result;
		}
		return null;
	}
		
	/**
	 * Reset the persist simulation flag in database to null
	 * in SPP_SITE table
	 */

	public boolean removePersistSimulation(String site){
		if (site == null || "".equals(site)){
			return false;
		}
		String query = "UPDATE SPP_SITE SET PERSIST_SIMULATION=null "
			+" WHERE SITE_IDENTIFIER = ?";
		int updatedRows = DB.update(query);
		//If number of rows updated is greater than zero
		if (updatedRows > 0){
			return true;
		}
		return false;
	}
}
