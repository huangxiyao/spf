package com.hp.spp.filters.access;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

public class AllowRule extends UrlAccessRule {
	private static final Logger mLog = Logger.getLogger(AllowRule.class);

	public AllowRule(String urlPattern) {
		super(urlPattern);
	}

	public boolean isAccessAllowed(HttpSession session) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("Access is allowed");
		}
		return true;
	}

	public boolean isLoginRequired() {
		return false;
	}
}
