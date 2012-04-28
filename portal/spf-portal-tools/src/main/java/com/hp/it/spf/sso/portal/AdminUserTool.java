package com.hp.it.spf.sso.portal;

import com.epicentric.authentication.AuthenticationManager;
import com.epicentric.authentication.Realm;
import com.epicentric.authentication.internal.AdminSetup;
import com.epicentric.entity.Entity;

import java.util.List;

/**
 * Tool to create Vignette server admin account.
 * <p>
 * In order to use this class its JAR file must be copied to Portal WAR directory and then the class
 * must be executed as follows:
 * <pre>
 * runs_with_classpath com.hp.it.spf.sso.portal.AdminUserTool {user name} {realm}
 * </pre>
 * runs_with_classpath(.bat or .sh) is Vignette tool present in VAP bin directory which allows to run a class
 * with the classpath containing all the libraries present in the portal WAR.
 *
 * @author Ye Liu (ye.liu@hp.com)
 */

public class AdminUserTool
{
	/**
		 * Performs the Vignette server admin creation process.
		 * @param args 2-element array with the first element is the user name and the 2nd element
		 * is the portal realm.
		 */
	public static void main(String[] args)
	{
		if (args.length < 2) {
			System.out.printf("java [options] %s %s%n",
					AdminUserTool.class.getName());
			System.exit(0);
		}

		try {
			AdminUserTool tool = new AdminUserTool();
			tool.createServerAdmin(args[0], args[1]);
		}
		catch (Exception e) {
			System.err.printf("Error occurred while registering administrator user: %s%n", e);
			e.printStackTrace();
			System.exit(3);
		}

	}


	/**
		 * Performs the actual server admin creation process.
		 * @param userName the user name of the server admin to be created
		 * @param realmId realm the users will be associated with
		 * @throws Exception If any exception is happened during Vignette API call
		 */
	public void createServerAdmin(String userName, String realmId) throws Exception
	{
		AdminSetup adminSetup = new AdminSetup(false, null);
		AuthenticationManager authMgr = AuthenticationManager.getDefaultAuthenticationManager();

		List<Realm> realms = authMgr.getSSORealms();

		Realm realm = null;
		for (Realm rlm : realms) {
			if (realmId.equals(rlm.getID())) {
				realm = rlm;
				break;
			}
		}

		Entity admin = adminSetup.createAdminUser(userName, "", realm);

		if (admin != null) {
			adminSetup.addEntityToAdminGroup(admin);
		}
	}
}
