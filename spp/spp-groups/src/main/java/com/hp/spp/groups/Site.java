package com.hp.spp.groups;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * Entity Class to represent a Site.
 * 
 */
public class Site {

	/**
	 * Default constructor.
	 */
	public Site() {
	}

	// attributes

	/**
	 * id of the site.
	 */
	private long mId;

	/**
	 * name of the site.
	 */
	private String mName;

	/**
	 * List of groups for this site.
	 */
	private Set mGroupList;

	// getters

	/**
	 * Return the id attribute.
	 * 
	 * @return the id of the site
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Return the name attribute.
	 * 
	 * @return the name of the site
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Return the groupList attribute.
	 * 
	 * @return the list of groups of the site
	 */
	public Set getGroupList() {
		return mGroupList;
	}

	// setters

	/**
	 * Set the id attribute.
	 * 
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * Set the name attribute.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * Set the groupList attribute.
	 * 
	 * @param groupList the list of groups to set
	 */
	public void setGroupList(Set groupList) {
		this.mGroupList = groupList;
	}

	/**
	 * Method to add a group to this site.
	 * @param group the group to add
	 */
	public void addGroup(Group group) {

		if (this.mGroupList == null) {
			this.mGroupList = new TreeSet();
		}
		this.mGroupList.add(group);

	}

	/**
	 * Remove a group from this site.
	 * @param group the group to remove
	 */
	public void removeGroup(Group group) {

		this.mGroupList.remove(group);

	}
	
	/**
	 * Returns one particular group from the list of groups.
	 * The groups is searched by its id
	 */
	public Group getGroup(long groupId)
	{
		Iterator it = getGroupList().iterator();
		Group group = null;
		while (it.hasNext())
		{
			Group g = (Group) it.next();
			if (g.getId() == groupId)
			{
				group = g;
				break;
			}
		}
		return group;
	}

}
