package com.hp.spp.wsrp.url;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * Class used to create base URL to the portal that relies on the navigation item names.
 */
public class FriendlyPageNameBaseUrlComposer implements BaseUrlComposer {
	private String mHomePageUrl;
	private String mSiteDNSName;
	private String mPageName;
	private boolean mSecure;

	public FriendlyPageNameBaseUrlComposer(String homePageUrl, String site, String pageName, boolean secure) {
		mHomePageUrl = homePageUrl;
		mSiteDNSName = site;
		mPageName = pageName;
		this.mSecure = secure;
	}

	public String getBaseUrl() {
		StringBuffer result = new StringBuffer();
		int protoEnd = mHomePageUrl.indexOf(':');
		// if the home URL contains protocol we will rework it to reflect the mSecure flag
		if (protoEnd > 0) {
			String proto = mHomePageUrl.substring(0, protoEnd);
			if (mSecure && !"https".equals(proto)) {
				result.append("https");
			}
			else if (!mSecure && !"http".equals(proto)) {
				result.append("http");
			}
			else {
				result.append(proto);
			}
		}
		else {
			protoEnd = 0;
		}
		if (mSiteDNSName == null) {
			result.append(mHomePageUrl.substring(protoEnd));
		}
		else {
			final String siteMarker = "/site/";
			int sitePos = mHomePageUrl.indexOf(siteMarker);
			if (sitePos == -1) {
				throw new IllegalArgumentException("Unable to find '/site/' in the URL: " + mHomePageUrl);
			}
			sitePos += siteMarker.length();
			result.append(mHomePageUrl.substring(protoEnd, sitePos));
			result.append(mSiteDNSName);
		}

		if (result.charAt(result.length()-1) != '/') {
			result.append('/');
		}
		try {
			result.append("?page=").append(URLEncoder.encode(mPageName, "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 is not supported?");
		}

		return result.toString();
	}
}
