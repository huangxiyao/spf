package com.hp.spp.portlets.reports.hppreport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * It is an interface to a database and retrives 
 * - the user information who did not login for a period of time
 * - hpp groups belonging to perticular site.
 * 
 * @author girishsk
 * 
 */

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     girishsk          30-Oct-2006      Created
* 
*/

public class HPPReportModel implements Serializable {	
	
	private static final long serialVersionUID = 1L;	
	//Status flag to indicate wheather user is invalid/invalid. 
	boolean mIsInvalidSearch = false;
	Map mUserProfileMap = new HashMap(10);
	Map mHppGroupRoleMap = new HashMap(10);
	List mLocalHPPGroups= new ArrayList(10);
	   
	public HPPReportModel(){
		super();
	}
	
	public Map getHppGroupRoleMap() {
		return mHppGroupRoleMap;
	}
	public void setHppGroupRoleMap(Map hppGroupRoleMap) {
		this.mHppGroupRoleMap = hppGroupRoleMap;
	}
	public List getLocalHPPGroups() {
		return mLocalHPPGroups;
	}
	public void setLocalHPPGroups(List localHPPGroups) {
		this.mLocalHPPGroups = localHPPGroups;
	}
	
	public Map getUserProfileMap() {
		return mUserProfileMap;
	}
	
	public void setUserProfileMap(Map userProfileMap) {
		this.mUserProfileMap = userProfileMap;
	}

	public boolean isInvalidSearch() {
		return mIsInvalidSearch;
	}
	
	public void setInvalidSearch(boolean isInvalidSearch) {
		this.mIsInvalidSearch = isInvalidSearch;
	}		
	
	public String toString(){
		StringBuffer buf = new StringBuffer(30);
		buf.append("IsInvalidSearch : "+ (mIsInvalidSearch?"true":"false")+ "\n");
		buf.append("UserProfileMap size : "+ mUserProfileMap.size()+ "\n");
		buf.append("Local HPP groups : "+ mLocalHPPGroups.size()+ "\n");
		buf.append("Group Role Map : "+ mHppGroupRoleMap.size()+ "\n");
		return buf.toString();		
	}
	
	/*
	* Utility method to fetch null string.
	*/	
	private String getNullString(String value){
		return (value == null)?"":value;
	}
	
}

