package com.hp.spp.wsrp.url;

/**
 * Class used to create base URL for the portal relying on Vignette navigation item UIDs. 
 */
public class PageIdBaseUrlComposer implements BaseUrlComposer {
	private String mSite;
	private String mPageId;

	public PageIdBaseUrlComposer(String site, String pageId) {
		mSite = site;
		mPageId = pageId;
	}

	public String getBaseUrl() {
		StringBuffer url = new StringBuffer();
		url.append(mSite);
		url.append("/template.PAGE/menuitem.");
		url.append(mPageId);
		url.append('/');

		return url.toString();
	}
}
