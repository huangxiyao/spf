package com.hp.spp.filters.access;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

public class DenyRule extends UrlAccessRule {
	private static final Logger mLog = Logger.getLogger(DenyRule.class);

	public DenyRule(String urlPattern) {
		super(urlPattern);
	}

	public boolean isAccessAllowed(HttpSession session) {
		if (mLog.isDebugEnabled()) {
			mLog.debug("Access is denied");
		}
		return false;
	}

	public boolean isLoginRequired() {
		return false;
	}
}
