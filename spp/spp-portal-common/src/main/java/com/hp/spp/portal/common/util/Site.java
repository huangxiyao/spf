package com.hp.spp.portal.common.util;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hp.spp.portal.common.dao.CommonDAO;
import com.hp.spp.portal.common.dao.CommonDAOCacheImpl;
import com.hp.spp.portal.common.helper.SiteURLHelper;

/**
 * Class which represents a site.
 * 
 * @author Mathieu Vidal
 * 
 */
public class Site {
	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(Site.class);

	private int mId;

	private String mName;

	private String mLandingPage;

	private boolean mLocaleInUrl;

	private String mHomePage;

	private String mPortletId;

	private String mHpappid;

	private String mLogoutPage;

	private String mUPSQueryId;

	private String mProtocol;

	private boolean mPersistSimulation;

	private String mSiteIdentifier;

	private boolean mAccessSite;

	private String mAccessCode;

	private String mStopSimulationPage;

	public Site(int id, String name, String landingPage, int localeInUrl,
			String homePage, String portletId, String hpAppId,
			String logoutPage, String upsQueryId, String protocol,
			int persistSimulation, String siteIdentifier, int accessSite,
			String accessCode, String stopSimulationPage) {
		mId = id;
		mName = name;
		mLandingPage = landingPage;
		if (localeInUrl == 0)
			mLocaleInUrl = false;
		else
			mLocaleInUrl = true;
		mHomePage = homePage;
		mPortletId = portletId;
		mHpappid = hpAppId;
		mLogoutPage = logoutPage;
		mUPSQueryId = upsQueryId;
		mProtocol = protocol;
		if (persistSimulation == 0)
			mPersistSimulation = false;
		else
			mPersistSimulation = true;
		mSiteIdentifier = siteIdentifier;
		if (accessSite == 0)
			mAccessSite = false;
		else
			mAccessSite = true;
		mAccessCode = accessCode;
		mStopSimulationPage = stopSimulationPage;
	}

	public String getAccessCode() {
		return mAccessCode;
	}

	public void setAccessCode(String accessCode) {
		mAccessCode = accessCode;
	}

	public boolean getAccessSite() {
		return mAccessSite;
	}

	public void setAccessSite(boolean accessSite) {
		mAccessSite = accessSite;
	}

	public String getHomePage() {
		return mHomePage;
	}

	public void setHomePage(String homePage) {
		mHomePage = homePage;
	}

	public String getHpappid() {
		return mHpappid;
	}

	public void setHpappid(String hpappid) {
		mHpappid = hpappid;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getLandingPage() {
		return mLandingPage;
	}

	public void setLandingPage(String landingPage) {
		mLandingPage = landingPage;
	}

	public boolean getLocaleInUrl() {
		return mLocaleInUrl;
	}

	public void setLocaleInUrl(boolean localeInUrl) {
		mLocaleInUrl = localeInUrl;
	}

	public String getLogoutPage() {
		return mLogoutPage;
	}

	public void setLogoutPage(String logoutPage) {
		mLogoutPage = logoutPage;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public boolean getPersistSimulation() {
		return mPersistSimulation;
	}

	public void setPersistSimulation(boolean persistSimulation) {
		mPersistSimulation = persistSimulation;
	}

	public String getPortletId() {
		return mPortletId;
	}

	public void setPortletId(String portletId) {
		mPortletId = portletId;
	}

	public String getProtocol() {
		return mProtocol;
	}

	public void setProtocol(String protocol) {
		mProtocol = protocol;
	}

	public String getSiteIdentifier() {
		return mSiteIdentifier;
	}

	public void setSiteIdentifier(String siteIdentifier) {
		mSiteIdentifier = siteIdentifier;
	}

	public String getUPSQueryId() {
		return mUPSQueryId;
	}

	public void setUPSQueryId(String queryId) {
		mUPSQueryId = queryId;
	}

	public String getStopSimulationPage() {
		return mStopSimulationPage;
	}

	public void setStopSimulationPage(String stopSimulationPage) {
		mStopSimulationPage = stopSimulationPage;
	}
	
	public static boolean concernSite(String siteURI) {
		// Call to a site not in the list of supported sites
		CommonDAO commonDao = CommonDAOCacheImpl.getInstance();
		Set supportedSites = commonDao.getSupportedSites();
		if (!supportedSites.contains(siteURI)) {
			mLog.warn("attempt to connect to site [" + siteURI
					+ "] out of list of defined sites");
			return false;
		}
		return true;
	}

	
}
