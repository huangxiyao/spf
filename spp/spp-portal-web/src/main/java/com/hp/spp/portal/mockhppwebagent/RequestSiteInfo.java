package com.hp.spp.portal.mockhppwebagent;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class RequestSiteInfo {
	private static final Pattern SITE_PATTERN = Pattern.compile("/.+?/((console|apc)|site/(public)?([^/?]+))");

	private String mProtectedSiteName;
	private boolean mPublic;

	public RequestSiteInfo(String protectedSiteName, boolean aPublic) {
		mProtectedSiteName = protectedSiteName;
		mPublic = aPublic;
	}

	public static RequestSiteInfo parse(String requestUri) {
		Matcher m = SITE_PATTERN.matcher(requestUri);
		if (m.find()) {
			if (m.group(2) != null) {
				return new RequestSiteInfo("console", false);
			}

			if ("console".equals(m.group(4))) {
				return new RequestSiteInfo("console", true);
			}

			boolean isRequestToPublicSite = m.group(3) != null;
			return new RequestSiteInfo(m.group(4), isRequestToPublicSite);
		}

		return null;
	}

	public String getProtectedSiteName() {
		return mProtectedSiteName;
	}

	public boolean isPublic() {
		return mPublic;
	}


}
