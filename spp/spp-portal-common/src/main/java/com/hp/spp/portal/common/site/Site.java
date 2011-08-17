package com.hp.spp.portal.common.site;

import java.lang.reflect.Method;

public class Site {

    private final String mSiteName;
    private int mUGSTimeoutInMilliseconds;
    private int mESMTimeoutInMilliseconds;
    private int mUPSTimeoutInMilliseconds;
    private String mSatisfactionSurveyUrl;
    private String mUPSMonitoringServletLogin;
    private String mUPSMonitoringServletPwd;
    private String mSearchPortletID;
    private String mSearchBrokerUrl;
    private String mSearchHpvcKey;
    private String mSearchIndexPage;
    private String mSiebelRegion;
    private boolean mUseMockProfile;
    private String mAdminUPSQueryId;
    private String mHPPAdminPassword;
    private String mHPPAdminLoginId;
    private int mCacheFileterPublicPagesCacheExpiryPeriodInMins;
    private String mSiteDownMessage;
    private String mWebSectionID;
    private String mUPSUrl;
	private String mUGSUrl;
	private String mUGSClientClassName;

    protected Site(String siteName){
        mSiteName = siteName;
    }
    public String getSiteName(){
        return mSiteName;
    }

    public String getUGSUrl() {
		return mUGSUrl;
	}

	public void setUGSUrl(String UGSUrl) {
		mUGSUrl = UGSUrl;
	}

	public String getUGSClientClassName() {
		return mUGSClientClassName;
	}

	public void setUGSClientClassName(String UGSClientClassName) {
		mUGSClientClassName = UGSClientClassName;
	}

	public String getSiteDownMessage() {
        return mSiteDownMessage;
    }

    public void setSiteDownMessage(String siteDownMessage) {
        mSiteDownMessage = siteDownMessage;
    }

    public String getUPSUrl() {
        return mUPSUrl;
    }

    public void setUPSUrl(String upsURL) {
       mUPSUrl = upsURL;
    }

    public String getWebSectionID() {
        return mWebSectionID;
    }

    public void setWebSectionID(String webSectionID) {
        mWebSectionID = webSectionID;
    }
    public String getAdminUPSQueryId() {
        return mAdminUPSQueryId;
    }

    public void setAdminUPSQueryId(String adminUPSQueryId) {
        mAdminUPSQueryId = adminUPSQueryId;
    }

    public int getCacheFileterPublicPagesCacheExpiryPeriodInMins() {
        return mCacheFileterPublicPagesCacheExpiryPeriodInMins;
    }

    public void setCacheFileterPublicPagesCacheExpiryPeriodInMins(int cacheFileterPublicPagesCacheExpiryPeriodInMins) {
        mCacheFileterPublicPagesCacheExpiryPeriodInMins = cacheFileterPublicPagesCacheExpiryPeriodInMins;
    }

    public int getESMTimeoutInMilliseconds() {
        return mESMTimeoutInMilliseconds;
    }

    public void setESMTimeoutInMilliseconds(int esmTimeoutInMilliseconds) {
        mESMTimeoutInMilliseconds= esmTimeoutInMilliseconds;
    }

    public String getHPPAdminLoginId() {
        return mHPPAdminLoginId;
    }

    public void setHPPAdminLoginId(String hppAdminLoginId) {
        mHPPAdminLoginId = hppAdminLoginId;
    }

    public String getHPPAdminPassword() {
        return mHPPAdminPassword;
    }

    public void setHPPAdminPassword(String hppAdminPassword) {
        mHPPAdminPassword = hppAdminPassword;
    }

    public String getSatisfactionSurveyUrl() {
        return mSatisfactionSurveyUrl;
    }

    public void setSatisfactionSurveyUrl(String satisfactionSurveyUrl) {
        mSatisfactionSurveyUrl = satisfactionSurveyUrl;
    }

    public String getSearchBrokerUrl() {
        return mSearchBrokerUrl;
    }

    public void setSearchBrokerUrl(String searchBrokerUrl) {
        mSearchBrokerUrl = searchBrokerUrl;
    }

    public String getSearchHpvcKey() {
        return mSearchHpvcKey;
    }

    public void setSearchHpvcKey(String searchHpvcKey) {
        mSearchHpvcKey = searchHpvcKey;
    }

    public String getSearchIndexPage() {
        return mSearchIndexPage;
    }

    public void setSearchIndexPage(String searchIndexPage) {
        mSearchIndexPage = searchIndexPage;
    }

    public String getSearchPortletID() {
        return mSearchPortletID;
    }

    public void setSearchPortletID(String searchPortletID) {
        mSearchPortletID = searchPortletID;
    }

    public String getSiebelRegion() {
        return mSiebelRegion;
    }

    public void setSiebelRegion(String siebelRegion) {
        mSiebelRegion = siebelRegion;
    }

    public int getUGSTimeoutInMilliseconds() {
        return mUGSTimeoutInMilliseconds;
    }

    public void setUGSTimeoutInMilliseconds(int ugsTimeoutInMilliseconds) {
        mUGSTimeoutInMilliseconds = ugsTimeoutInMilliseconds;
    }

    public String getUPSMonitoringServletLogin() {
        return mUPSMonitoringServletLogin;
    }

    public void setUPSMonitoringServletLogin(String upsMonitoringServletLogin) {
        mUPSMonitoringServletLogin = upsMonitoringServletLogin;
    }

    public String getUPSMonitoringServletPwd() {
        return mUPSMonitoringServletPwd;
    }

    public void setUPSMonitoringServletPwd(String upsMonitoringServletPwd) {
        mUPSMonitoringServletPwd = upsMonitoringServletPwd;
    }

    public int getUPSTimeoutInMilliseconds() {
        return mUPSTimeoutInMilliseconds;
    }

    public void setUPSTimeoutInMilliseconds(int upsTimeoutInMilliseconds) {
        mUPSTimeoutInMilliseconds = upsTimeoutInMilliseconds;
    }

    public boolean getUseMockProfile() {
        return mUseMockProfile;
    }

    public void setUseMockProfile(boolean useMockProfile) {
        mUseMockProfile = useMockProfile;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        Method[] methods = getClass().getDeclaredMethods();
        int i = 0;
        for(Method m: methods){
             if(m.getParameterTypes().length == 0 && m.getName().startsWith("get") &&
                     !(m.getName().equals("getHPPAdminPassword") || m.getName().equals("mUPSMonitoringServletPwd"))){
                try{
                    builder.append(m.getName().substring(3)).append("=").append(m.invoke(this));
                    if(++i < (methods.length - 1)){
                        builder.append(", ");
                    }
                }catch(Exception e){
                    throw new RuntimeException("Unable to fetch class attributes",e);
                }
             }
        }
        return builder.toString() ;
    }
}
