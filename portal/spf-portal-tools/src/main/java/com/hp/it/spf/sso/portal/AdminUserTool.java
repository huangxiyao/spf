package com.hp.it.spf.sso.portal;

import com.epicentric.authentication.AuthenticationManager;
import com.epicentric.authentication.Realm;
import com.epicentric.authentication.internal.AdminSetup;
import com.epicentric.entity.Entity;

import java.util.List;


/**
 * @author Ye Liu (ye.liu@hp.com)
 */
public class AdminUserTool
{
	public static void main(String[] args)
	{
		if (args.length < 2) {
			System.out.printf("java [options] %s %s%n",
					AdminUserTool.class.getName());
			System.exit(0);
		}

		try {
			AdminUserTool tool = new AdminUserTool();
			tool.createAdmin(args[0], args[1]);
		}
		catch (Exception e) {
			System.err.printf("Error occurred while registering administrator user: %s%n", e);
			e.printStackTrace();
			System.exit(3);
		}

	}


	public void createAdmin(String userName, String realmId) throws Exception
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
