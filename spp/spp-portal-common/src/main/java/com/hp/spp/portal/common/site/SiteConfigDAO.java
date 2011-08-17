package com.hp.spp.portal.common.site;

public interface SiteConfigDAO {
	Site getSite(String siteName);
	void updateSite(Site updatedSite);
}
