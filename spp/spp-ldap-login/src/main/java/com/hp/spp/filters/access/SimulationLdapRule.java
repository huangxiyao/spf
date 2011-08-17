package com.hp.spp.filters.access;

import com.hp.spp.profile.Constants;

import javax.servlet.http.HttpSession;
import java.util.Map;

import org.apache.log4j.Logger;

public class SimulationLdapRule extends LdapRule {
	private static final Logger mLog = Logger.getLogger(SimulationLdapRule.class);

	public SimulationLdapRule(String urlPattern) {
		super(urlPattern);
	}

	public boolean isAccessAllowed(HttpSession session) {
		Map userProfile = (Map) session.getAttribute(Constants.PROFILE_MAP);
		if (userProfile == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Access allowed - no user profile found");
			}
			return true;
		}
		String isSimulating = (String) userProfile.get(Constants.MAP_IS_SIMULATING);
		if (isSimulating == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Access allowed - no simulation info available");
			}
			return true;
		}
		// user is allowed either if she's not simulating or, if simulating, she's ldap authenticated
		boolean allowed =
				!Boolean.valueOf(isSimulating).booleanValue() ||
				getAuthenticatedEmail(session) != null;

		if (mLog.isDebugEnabled()) {
			boolean simulating = Boolean.valueOf(isSimulating).booleanValue();
			String email = getAuthenticatedEmail(session);
			if (allowed) {
				if (simulating || email == null) {
					mLog.debug("Access allowed - user not simulating");
				}
				else {
					mLog.debug("Access allowed to user " + email);
				}
			}
			else {
				mLog.debug("Access denied - user simulating but not authenticated");
			}
		}

		return allowed;

	}

}
