package com.hp.spp.filters.access;

import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class LdapAclRule extends LdapRule {
	private static final Pattern mAclPattern = Pattern.compile("\\s*(,|;|\\|)\\s*");
	private static final Logger mLog = Logger.getLogger(LdapAclRule.class);

	private List mAuthorizedUsersEmails;

	public LdapAclRule(String urlPattern, String data) {
		super(urlPattern);
		if (data == null || data.trim().equals("")) {
			throw new IllegalArgumentException("Data containing access control list cannot be null nor empty for this rule");
		}
		String[] acl = mAclPattern.split(data.trim());
		mAuthorizedUsersEmails = Arrays.asList(acl);

		if (mLog.isInfoEnabled()) {
			mLog.info("List of authorized email addresses: " + mAuthorizedUsersEmails);
		}
	}

	public boolean isAccessAllowed(HttpSession session) {
		String authenticatedEmail = getAuthenticatedEmail(session);
		if (authenticatedEmail == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Access denied - user not authenticated");
			}
			return false;
		}
		boolean allowed = mAuthorizedUsersEmails.indexOf(authenticatedEmail) != -1;
		if (mLog.isDebugEnabled()) {
			if (allowed) {
				mLog.debug("Access allowed to user " + authenticatedEmail);
			}
			else {
				mLog.debug("Access denied - user '" + authenticatedEmail + "' not on the access control list");
			}
		}
		return allowed;
	}
}
