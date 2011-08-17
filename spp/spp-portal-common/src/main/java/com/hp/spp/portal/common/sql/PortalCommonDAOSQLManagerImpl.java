package com.hp.spp.portal.common.sql;

import com.hp.spp.db.DB;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.portal.common.util.Site;
import com.hp.spp.portal.common.util.SiteMapper;

/**
 * Class to access the data with SQL orders.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public final class PortalCommonDAOSQLManagerImpl implements PortalCommonDAO {

	/**
	 * The instance for the singleton.
	 */
	private static PortalCommonDAOSQLManagerImpl instance;

	/**
	 * Query to obtain site data by name.
	 */
	public static final String querySiteByName = "SELECT ID, NAME, LANDING_PAGE, LOCALE_IN_URL, "
			+ "HOME_PAGE, PORTLET_ID, HPAPPID, LOGOUT_PAGE, UPS_QUERY_ID, PROTOCOL, "
			+ "PERSIST_SIMULATION, SITE_IDENTIFIER, ACCESS_SITE, ACCESS_CODE, STOP_SIMULATION_PAGE "
			+ "FROM SPP_SITE WHERE NAME = ?";
	
	private static final String mUpdateAccessSiteByName = "UPDATE SPP_SITE SET ACCESS_SITE = ? WHERE NAME = ?";
    
	private static final String mUpdateAccessCodeByName = "UPDATE SPP_SITE SET ACCESS_CODE = ? WHERE NAME = ?";
	   
	// force the use of singleton
	private PortalCommonDAOSQLManagerImpl() {
	}

	/**
	 * Return a singleton of the DAO.
	 * 
	 * @return the instance
	 */
	public static PortalCommonDAOSQLManagerImpl getInstance() {
		if (null == instance) {
			instance = new PortalCommonDAOSQLManagerImpl();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getSiteIdentifier(java.lang.String)
	 */
	public String getSiteIdentifier(String siteName) {
		Object[] args = { siteName };
		Object result = DB.queryForObject(querySiteByName, new SiteMapper(),
				args);
		if (result != null && result instanceof Site)
			return ((Site) result).getSiteIdentifier();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#isSiteAccessible(java.lang.String)
	 */
	public boolean isSiteAccessible(String siteURI) {
		Object[] args = { siteURI };
		Object result = DB.queryForObject(querySiteByName, new SiteMapper(),
				args);
		if (result != null && result instanceof Site)
			return ((Site) result).getAccessSite();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getAccessCode(java.lang.String)
	 */
	public String getAccessCode(String siteURI) {
		Object[] args = { siteURI };
		Object result = DB.queryForObject(querySiteByName, new SiteMapper(),
				args);
		if (result != null && result instanceof Site)
			return ((Site) result).getAccessCode();
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getPortalHomePageUrl(java.lang.String)
	 */
	public String getPortalHomePageUrl(String siteName) {
		Object[] args = { siteName };
		Object result = DB.queryForObject(querySiteByName, new SiteMapper(),
				args);
		if (result != null && result instanceof Site)
			return ((Site) result).getHomePage();
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.spp.portal.common.sql.PortalCommonDAO#getSiteProtocol(java.lang.String)
	 */
	public String getSiteProtocol(String siteName)	{
		Object[] args = { siteName };
		Object result = DB.queryForObject(querySiteByName, new SiteMapper(),
				args);
		if (result != null && result instanceof Site)
			return ((Site) result).getProtocol();
		return null;
	}

	public void setAccessCode(String siteName, String accessCode) {
		Object[] args = {accessCode,siteName };
		Object[] args2 = {siteName};
		int result = DB.update(mUpdateAccessCodeByName, args);
		if (result > 0 ){
			
			Object resultSite = DB.queryForObject(querySiteByName, new SiteMapper(),
					args2);
			if (resultSite != null && resultSite instanceof Site){
				((Site) resultSite).setAccessCode(accessCode);
			}
			
		}
	}

	public void setSiteAccessible(String siteName, int accessSite) {
		Object[] args = { accessSite,siteName };
		Object[] args2 = {siteName};
		
		boolean mAccessSite=(accessSite != 0);
		int result = DB.update(mUpdateAccessSiteByName, args);
		if (result > 0 ){
			Object resultSite = DB.queryForObject(querySiteByName, new SiteMapper(),
					args2);
			if (resultSite != null && resultSite instanceof Site){
				((Site) resultSite).setAccessSite(mAccessSite);
			}			
		}
	}

}
