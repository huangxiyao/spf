package com.hp.spp.portal.common.site;

import com.hp.spp.cache.Cache;

public class SiteManager {
    private static final SiteManager mSiteManager = new SiteManager();
    private SiteConfigDAO mSiteConfigDAO;
    

    public static SiteManager getInstance() {
        return mSiteManager;
    }

    private SiteManager() {
        mSiteConfigDAO  = new CachingSiteConfigDAO(new JDBCSiteConfigDAO(), Cache.getInstance());
    }

    public Site getSite(String siteName){
        return  mSiteConfigDAO.getSite(siteName);
    }

    public void updateSite(Site updatedSite){
    	mSiteConfigDAO.updateSite(updatedSite);    	
    }
}
