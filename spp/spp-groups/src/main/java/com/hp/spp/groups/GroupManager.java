package com.hp.spp.groups;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.spp.groups.dao.SiteDAOCacheImpl;

/**
 * Class to manager the lists of groups for a user.
 * 
 * @author bmollard
 * 
 */
public class GroupManager {

	/**
	 * Return the list of groups for the user.
	 * 
	 * @param siteName name of the site
	 * @param userContext the map that represents the suer context
	 * @return list of available groups
	 */
	public static List getAvailableGroups(String siteName, Map userContext) {

		List groupList = new ArrayList();

		Site site = (Site) SiteDAOCacheImpl.getInstance().loadByName(siteName);

		if ((site != null) && (siteName.equals(site.getName()))) {

			Set s = site.getGroupList();
			Iterator it = s.iterator();
			while (it.hasNext()) {
				Group g = (Group) it.next();
				boolean isMember = g.isMember(userContext, siteName);
				if (isMember) {
					groupList.add(g.getName());
				}
			}
		}
		return groupList;
	}
	

}
