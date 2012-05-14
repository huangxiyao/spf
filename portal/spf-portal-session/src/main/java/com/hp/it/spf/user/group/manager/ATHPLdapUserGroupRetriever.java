/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.user.exception.UserGroupsException;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;
import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * This is the implementation class of <tt>IUserGroupRetriever</tt>. <p> This class retrieves user
 * groups from @HP LDAP. </p>
 *
 * @author <link href="ye.liu@hp.com">Ye Liu</link>
 * @version 1.0
 */
public class ATHPLdapUserGroupRetriever implements IUserGroupRetriever {
	private static final LogWrapper LOG = AuthenticatorHelper.getLog(ATHPLdapUserGroupRetriever.class);
	private static final String LDAP_HOST = "ldap.hp.com";
	private static final int LDAP_PORT = 389;
	private static final String LDAP_BASE = "ou=Groups, o=hp.com";

	/**
	 * Retrieve user groups from @HP LDAP.
	 *
	 * @param userProfile user profiles
	 * @param request     HttpServletRequest
	 * @return user groups set, if user has no groups, an empty Set will be returned.
	 * @throws UserGroupsException if any exception occurs, an UserGroupsException will be thrown.
	 */
	public Set<String> getGroups(Map<String, Object> userProfile,
								 HttpServletRequest request) throws UserGroupsException {
		return getLDAPGroups((String)userProfile.get(AuthenticationConsts.KEY_USER_NAME));
	}

	/**
	 * Talk to LDAP and get all groups for a specific user.
	 *
	 * @param uid  @HP user name
	 * @return user groups set, if user has no groups, an empty Set will be returned.
	 */
	private Set<String> getLDAPGroups(String uid) {
		int scope = LDAPConnection.SCOPE_SUB;

		String user_filter = "(member=uid=" + uid + ", ou=People, o=hp.com)";
		String getAttrs[] = {"cn"};

		Set<String> groups = new TreeSet<String>();
		LDAPConnection ld = null;

		// Begin processing
		try {
			// Create a new connection
			ld = new LDAPConnection();

			// Connect and bind to the directory anonymously
			ld.connect(LDAP_HOST, LDAP_PORT);

			// Perform search - Get the groups for a specific customer  from the directory
			LDAPSearchResults res = ld.search(LDAP_BASE, scope, user_filter, getAttrs, false);

			if (res != null) {
				parseGroupsFromLDAPSearchResults(groups, res);
			}

			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Get UserGroups from LDAP for user " + uid + " : " + groups);
			}
		}
		catch (LDAPException e) {
			// If we've got an error we want to continue - the method will return empty groups but the login will not fail.
			// User won't get access to group-protected portal features.
			LOG.error("Communicate with LDAP failed for user " + uid + ", and got Exception: " + e, e);
		}
		finally {
			if (ld != null) {
				try {
					ld.disconnect();
				}
				catch (LDAPException e) {
					LOG.error("Disconnect from LDAP failed for user " + uid + ", and got Exception: " + e, e);
				}
			}
		}

		return groups;
	}


	/**
	 * Parse the groups from the LDAP search results.
	 *
	 * @param groups  extracted groups
	 * @param res LDAP search results.
	 */
	private void parseGroupsFromLDAPSearchResults(Set<String> groups, LDAPSearchResults res)
			throws LDAPException {
		while (res.hasMoreElements()) {
			LDAPEntry findEntry = (LDAPEntry) res.next();
			LDAPAttribute attr = findEntry.getAttribute("cn");
			if (attr != null) {
				String group = (String) attr.getStringValues().nextElement();
				groups.add(group);
			}
		}
	}
}
