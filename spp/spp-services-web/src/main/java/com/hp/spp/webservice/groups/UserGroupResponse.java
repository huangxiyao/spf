package com.hp.spp.webservice.groups;

import java.util.List;

/**
 * @author bmollard
 */
public class UserGroupResponse {
  
	/**
	 * List of groups return for the given user.
	 */
    private List mGroupList;

	public List getGroupList() {
		return mGroupList;
	}

	public void setGroupList(List groupList) {
		mGroupList = groupList;
	}
   
}
