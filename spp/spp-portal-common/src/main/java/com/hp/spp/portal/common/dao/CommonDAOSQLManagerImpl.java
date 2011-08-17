package com.hp.spp.portal.common.dao;

import java.sql.Types;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.spp.db.DB;
import com.hp.spp.db.UniqueObjectMapper;
import com.hp.spp.portal.common.sql.PortalCommonDAOSQLManagerImpl;
import com.hp.spp.portal.common.user.User;
import com.hp.spp.portal.common.user.UserMapper;
import com.hp.spp.portal.common.util.Site;
import com.hp.spp.portal.common.util.SiteMapper;
import com.hp.spp.portal.common.util.StringLocator;
import com.hp.spp.portal.common.util.StringLocatorMapper;

/**
 * Class to retrieve common data.
 * 
 * @author Mathieu Vidal
 * 
 */
public class CommonDAOSQLManagerImpl implements CommonDAO {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger
			.getLogger(CommonDAOSQLManagerImpl.class);

	// query to retrieve an user
	private static final String queryUserByUserName = "SELECT USER_NAME, LAST_NAME, COUNTRY FROM USERS WHERE USER_NAME = ?";

	// query to retrieve the groups defined in Vignette
	private static final String queryGroupsDefined = "SELECT NAME FROM USERGROUPS";

	// query to retrieve the groups for a user
	private static final String queryGroupsByUser = " SELECT USERGROUPS.NAME FROM USERGROUPS, USERGROUPMEMBERSHIP, USERS"
			+ " WHERE USERS.HPP_ID = ?"
			+ " AND USERS.UNIQUE_ID = USERGROUPMEMBERSHIP.USER_UID"
			+ " AND USERGROUPMEMBERSHIP.GROUP_UID = USERGROUPS.UNIQUE_ID";

	// query to update the last login date of a user
	private static final String queryUpdateLastLoginDate = "UPDATE USERS SET LAST_LOGIN_DATE = ? WHERE HPP_ID = ?";

	// The instance for the singleton.
	private static CommonDAOSQLManagerImpl instance;

	// force the use of singleton
	private CommonDAOSQLManagerImpl() {
	}

	/**
	 * Return a singleton of the DAO.
	 * 
	 * @return the instance
	 */
	public static CommonDAOSQLManagerImpl getInstance() {
		if (null == instance) {
			instance = new CommonDAOSQLManagerImpl();
		}
		return instance;
	}

	public String getUrlLocator(String userCountryCode, String userLangCode) {
		Object[] args = { userCountryCode, userLangCode };
		String query = "SELECT COUNTRYCODE, LANGUAGECODE, STR_LOC FROM SPP_HPURLS WHERE COUNTRYCODE =  ? AND LANGUAGECODE = ?";
		Object result = DB.queryForObject(query, new StringLocatorMapper(),
				args);
		if (result != null && result instanceof StringLocator)
			return ((StringLocator) result).getStringLocator();
		return null;
	}

	public String getUrlLocator(String userCountryCode) {
		Object[] args = { userCountryCode };
		String query = "SELECT COUNTRYCODE, LANGUAGECODE, STR_LOC FROM SPP_HPURLS WHERE COUNTRYCODE =  ?";
		Object result = DB.queryForObject(query, new StringLocatorMapper(),
				args);
		if (result != null && result instanceof StringLocator)
			return ((StringLocator) result).getStringLocator();
		return null;
	}

	public String getUrlLocatorByLanguage(String userLanguageCode) {
		String guestUser = "sso_guest_user_" + userLanguageCode;
		Object[] args = { guestUser };
		Object result = DB.queryForObject(queryUserByUserName,
				new UserMapper(), args);
		if (result != null && result instanceof User)
			return ((User) result).getCountry();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.login.dao.LoginDAO#getSupportedSites()
	 */
	public Set getSupportedSites() {
		String query = "SELECT ID, NAME, LANDING_PAGE, LOCALE_IN_URL, "
				+ "HOME_PAGE, PORTLET_ID, HPAPPID, LOGOUT_PAGE, UPS_QUERY_ID, PROTOCOL, "
				+ "PERSIST_SIMULATION, SITE_IDENTIFIER, ACCESS_SITE, ACCESS_CODE, STOP_SIMULATION_PAGE "
				+ "FROM SPP_SITE";

		List sqlResult = DB.query(query, new SiteMapper());

		Set supportedSites = new HashSet();
		if (sqlResult.size() == 0) {
			String error = "No Site defined in database";
			mLog.error(error);
			throw new IllegalStateException(error);
		}

		for (Iterator iter = sqlResult.iterator(); iter.hasNext();) {
			String site = ((Site) iter.next()).getName();
			supportedSites.add(site);
			if (mLog.isDebugEnabled())
				mLog.debug("Add to supported sites [" + site + "]");
		}
		return supportedSites;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.dao.CommonDAO#getUPSQueryId(java.lang.String)
	 */
	public String getUPSQueryId(String site) {
		Object[] args = { site };
		Object result = DB.queryForObject(
				PortalCommonDAOSQLManagerImpl.querySiteByName,
				new SiteMapper(), args);
		if (result != null && result instanceof Site)
			return ((Site) result).getUPSQueryId();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.dao.CommonDAO#
	 *      getVignetteUserGroup(java.lang.String, java.lang.String)
	 */
	public Set getVignetteUserGroup(String hppid) {
		Object[] args = { hppid };
		return new HashSet(DB.query(queryGroupsByUser,
				new UniqueObjectMapper(), args));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.dao.CommonDAO#getVignetteGroups()
	 */
	public Set getVignetteGroups() {
		return new HashSet(DB.query(queryGroupsDefined,
				new UniqueObjectMapper()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.dao.CommonDAO#updateUserLastLoginDate(java.lang.String,
	 *      java.lang.Date)
	 */
	public void updateUserLastLoginDate(String hppid, Date date) {
		java.sql.Date dateSQL = new java.sql.Date(date.getTime());
		DB.update(queryUpdateLastLoginDate, new Object[] { dateSQL, hppid },
				new int[] { Types.DATE, Types.VARCHAR });
	}
}
