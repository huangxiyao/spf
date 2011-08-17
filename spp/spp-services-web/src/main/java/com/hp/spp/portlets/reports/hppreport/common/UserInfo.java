package com.hp.spp.portlets.reports.hppreport.common;

import com.hp.spp.portlets.reports.hppreport.command.SearchEntiyCommand;

/*
 * UserInfo is business object which holds user information.
 * @author girishsk
 * 
 */

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     girishsk           26-Oct-2006      Created
*
*/
public class UserInfo {
	private String mUserId ="";
	private String mHPPId="";
	private String mEmailId="";
	private String mEntityIdentifier="";
	private String mEntityType="";
	
	public UserInfo() {
		super();
	}		
	public UserInfo(String searchEntityIdentifier, String searchEntityType) {		
		if(SearchEntiyCommand.TYPE_USERID.equals(searchEntityType))
			setUserID(searchEntityIdentifier);
		else if (SearchEntiyCommand.TYPE_HPPID.equals(searchEntityType))
			setHPPID(searchEntityIdentifier);
		else
			setEmailID(searchEntityIdentifier);	
		//set the Search criteria 
		setEntityIdentifier(searchEntityIdentifier);
		//set the search type
		setEntityType(searchEntityType);
	}
	
	
	public String getUserID() {
		return mUserId;
	}
	
	public String getEmailID() {
		return mEmailId;
	}
	
	public String getHPPID() {
		return mHPPId;
	}
	

	
	public void setUserID(String userID) {
		mUserId = userID;
	}
	
	public void setEmailID(String emailID) {
		mEmailId = emailID;
	}
	
	
	public void setHPPID(String hppid) {
		mHPPId = hppid;
	}	
	
	
	/** Methods to retrive the currently stored entity type
	 *  in the UserInfo class
	 */
	
	public String getEntityIdentifier() {
		return mEntityIdentifier;
	}
	public void setEntityIdentifier(String entityIdentifier) {
		mEntityIdentifier = entityIdentifier;
	}
	public String getEntityType() {
		return mEntityType;
	}
	
	/**
	 * Gets the entity type that  HPP expects
	 * @return HPP type.
	 */
	
	public String getHPPEntityType() {
		String entityType = "";
		if(SearchEntiyCommand.TYPE_USERID.equals(this.mEntityType))
		     entityType = "userId";
		else if (SearchEntiyCommand.TYPE_HPPID.equals(this.mEntityType))
		  	entityType = "profileId";
		else //else email id.
		  	entityType = "email";

		return entityType;
	}
	
	public void setEntityType(String entityType) {
		mEntityType = entityType;
	}
	
}
