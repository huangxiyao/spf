package com.hp.spp.wsrp.url;

public class PageIdActionUrlComposer implements BaseUrlComposer {
	private String mSite;
	private String mPageId;

	public PageIdActionUrlComposer(String site, String pageId) {
		mSite = site;
		mPageId = pageId;
	}

	public String getBaseUrl() {
		StringBuffer url = new StringBuffer();
		url.append(mSite);
		url.append("/template.PAGE/action.process/menuitem.");
		url.append(mPageId);
		url.append("/?javax.portlet.action=true");

		return url.toString();
	}
}
