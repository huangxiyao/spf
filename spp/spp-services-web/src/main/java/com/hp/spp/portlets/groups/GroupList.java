package com.hp.spp.portlets.groups;

import java.io.Serializable;
import java.util.List;

/**
 * Class that allow to store a list of group Ids.
 * Thsi class will be used as a command for the delete and export operations
 * @author MJULIENS
 *
 */
public class GroupList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A list of Strings that are group ids
	 */
	private List groupIdList;
	
	/**
	 * A list of Strings that are group names
	 */
	private List groupNameList;
	
	GroupList()
	{
		
	}
	
	GroupList(List groupIdList, List groupNameList)
	{
		this.groupIdList = groupIdList;
		this.groupNameList = groupNameList;
	}

	public List getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List groupIdList) {
		this.groupIdList = groupIdList;
	}

	public List getGroupNameList() {
		return groupNameList;
	}

	public void setGroupNameList(List groupNameList) {
		this.groupNameList = groupNameList;
	}

	
}
