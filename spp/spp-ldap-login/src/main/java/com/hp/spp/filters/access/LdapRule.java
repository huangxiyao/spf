package com.hp.spp.filters.access;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

import com.hp.spp.profile.Constants;

public class LdapRule extends UrlAccessRule {
	private Logger mLog = Logger.getLogger(LdapRule.class);

	public LdapRule(String urlPattern) {
		super(urlPattern);
	}

	public boolean isAccessAllowed(HttpSession session) {
		String email = getAuthenticatedEmail(session);
		boolean allowed = email != null;
		if (mLog.isDebugEnabled()) {
			if (allowed) {
				mLog.debug("Access allowed to user " + email);
			}
			else {
				mLog.debug("Access denied - user not authenticated");
			}
		}
		return allowed;
	}

	protected String getAuthenticatedEmail(HttpSession session) {
		if (session == null) {
			return null;
		}
		return (String) session.getAttribute(Constants.HP_LDAP_AUTH_EMAIL);
	}

	public boolean isLoginRequired() {
		return true;
	}
}
