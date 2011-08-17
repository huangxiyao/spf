package com.hp.spp.groups.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.hp.spp.groups.Group;
import com.hp.spp.groups.Site;
import com.hp.spp.groups.dao.SiteDAOCacheImpl;

/**
 * Represent an inGroup expression.
 * 
 * @author bmollard
 * 
 */
public class InGroup implements Expression {

	/**
	 * the name of the group.
	 */
	private String mGroupName;

	/**
	 * Return true if the user context is in group.
	 * 
	 * @return true if OK for the user context
	 * @param userContext user Context
	 * @param siteName name of the site
	 */
	public boolean isTrue(Map userContext, String siteName) {
		Group group = loadGroup(siteName, mGroupName);
		if (group != null) {
			return group.isMember(userContext, siteName);
		} else {
			return false;
		}
	}

	/**
	 * Loads group based on the site name and the group name. If the site
	 * does not correspond to a site in cache or in the DB then null is 
	 * returned. If the site is found, then the method iterates over
	 * the list of groups associated with this site and returns the group
	 * with the given name. If no such group is found, returns null.
	 * 
	 * @param siteName Name of the site
	 * @param groupName Name of the group
	 * @return The group based on the site and the group. If not found, returns null.
	 */
	protected Group loadGroup(String siteName, String groupName) {

		Group group = null;
		Site site = SiteDAOCacheImpl.getInstance().loadByName(siteName);
		if (site != null) {
			Iterator groupSetIt = site.getGroupList().iterator();
			while (groupSetIt.hasNext() && group == null) {
				Group currentGroup = (Group) groupSetIt.next();
				if (groupName.equals(currentGroup.getName())) {
					group = currentGroup;
				}
			}
		}
		return group;
	}

	public String getValue() {
		return mGroupName;
	}

	public void setValue(String nameGroup) {
		mGroupName = nameGroup;
	}

	public void getInGroupNames(ArrayList list) {
		list.add(mGroupName);
	}
}
