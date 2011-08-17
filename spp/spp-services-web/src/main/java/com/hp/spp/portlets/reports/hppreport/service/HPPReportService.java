package com.hp.spp.portlets.reports.hppreport.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.webservice.AdminViewUserResponseElement;
import com.hp.globalops.hppcbl.webservice.GetUserGroupsResponseElement;
import com.hp.globalops.hppcbl.webservice.GroupRole;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;
import com.hp.spp.portlets.reports.hppreport.common.UserInfo;
import com.hp.spp.reports.hppreport.dao.ReportDAO;
import com.hp.spp.reports.hppreport.exception.HPPReportException;
import com.hp.spp.portlets.reports.hppreport.model.HPPReportModel;

/**
* Service interface used by controller to retrive HPP data such as 
* HPP admin name and password.
* @author girishsk
* @version 2.0 intial
*/

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     girishsk          30-Oct-2006      Created
* v2 	 girishsk		   09-Dec-2006		Changed method getHPPUserProfileData
* 
*/
public class HPPReportService {	
	private Logger mLog = Logger.getLogger(HPPReportService.class);
	private HPPReportModel mHPPReportModel = null;
	//Toolkit class instance, has utilities for HPPReport interface
	private HPPReportToolkit mHPPReportToolkit = null;	
	private String mProfileID="";
	private UserInfo mUserInfo;
	
	public String getProfileID() {
		return mProfileID;
	}

	public void setProfileID(String profileID) {
		mProfileID = profileID;
	}

	public HPPReportService(){
		mHPPReportToolkit = new HPPReportToolkit();
	}	
	
	/*
	 * Loads the user and group information based on
	 * the ID entered in search which can either be HPPID, USERID or
	 * EMAIL ID
	 * @param UserInfo user information dispatched during search
	 * @param site Users site retrived from session
	 */
	public boolean load(UserInfo info, String site) throws PassportServiceException, HPPReportException{
		    //Load the admin credentials
		    mLog.debug("The logged in site : " + site);
			
		    if(info == null || site == null){
				mLog.error("Empty UserInfo or Site name" );
				throw new IllegalArgumentException("Empty UserInfo or Site name");
			}
			mHPPReportToolkit.loadHPPAdminCredentials(site);
			mHPPReportModel = new HPPReportModel();
			
			mLog.debug(" UserInfo Object : "+info);
			//Prime the userInfo instance.
			setUserInfo(info);			
			
			mLog.debug("EntityIdentifier :"+ info.getEntityIdentifier());
			mLog.debug("EntityType :"+ info.getEntityType());
			
		
			mHPPReportModel = init(site);
			return true;
		}

	/*
	* @return HPPReport report summary
	*/
	public HPPReportModel getReportSummary(){
		mLog.debug("Report details=" + mHPPReportModel);
		return mHPPReportModel;		
	}
	
	/*
	* Adds the user to the HPP group based on groupName passed to it 
	* @param groupName the name of group to add
	* @throws PassportServiceException 
	*/	
	public void addUserToGroup(String groupName) throws PassportServiceException{		
		mLog.debug("Group Name : " + groupName);
		PassportService ws = mHPPReportToolkit.getPassportService();
		String adminSessionToken = null;
		try{
			adminSessionToken = mHPPReportToolkit.getAdminSessionToken();
			mLog.debug("AdminSessionToken in addUserToGroup() : "+adminSessionToken);
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" admin session token for group : [ "+ groupName +" ]";
					
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));				
			throw psex;
		}		
		ProfileIdentity profileIdentity = new ProfileIdentity() ;
		profileIdentity.setProfileId(this.mProfileID) ;
		//Find the roleName from groupName
		/* Map map = mHPPReportModel.getHppGroupRoleMap();
		  String roleName = (String)map.get(groupName);
		  mLog.debug("Role Name : "+roleName);
		*/
		
		//It is assumed that we can add user to a group by assigning roleName as user
		String roleName = "user";		
		try{
			ws.addUserToGroup(adminSessionToken, profileIdentity, groupName, roleName, null);
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while adding user to " +
					" group : "  + groupName;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}			
		//Update model with change in group
		mHPPReportModel.setHppGroupRoleMap(getHPPGroupRoleMap(this.mProfileID));
	}
	 
	/*
	 * Removes the user from the  HPP group membership based on groupName 
	 * @param groupName the name of group to remove
	 * @throws PassportServiceException 
	 */

	public void removeUserFromGroup(String groupName) throws PassportServiceException{		
		mLog.debug("Group Name in removeUserFromGroup() :" + groupName);
		PassportService ws = mHPPReportToolkit.getPassportService();
		String adminSessionToken = null;
		try{
			adminSessionToken = mHPPReportToolkit.getAdminSessionToken();		
			mLog.debug("AdminSessionToken in removeUserFromGroup() : "+ adminSessionToken);
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" admin session token for group : "  + groupName;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}			
		
		ProfileIdentity profileIdentity = new ProfileIdentity() ;
		profileIdentity.setProfileId(this.mProfileID);
		
		//Find the roleName from groupName
		Map map = mHPPReportModel.getHppGroupRoleMap();
		String roleName = (String)map.get(groupName);		
		mLog.debug("Role Name in removeUserFromGroup(): "+roleName);  
		try{
			ws.removeUserFromGroup(adminSessionToken, profileIdentity, groupName, roleName, null) ;
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while removing user from" +
					" the group : "  + groupName;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}			
		//Update model with change in group
		mHPPReportModel.setHppGroupRoleMap(getHPPGroupRoleMap(this.mProfileID));
		
		//Remove the group from the collection			
		//mHPPReportModel.getLocalHPPGroups().remove(groupName);
		
	}
	
	/** 
	 * Create HPPReport given hPPID and site.
	 * @param hppID The hppID of the user
	 * @throws PassportServiceException 
	 */
	private HPPReportModel init(String site) 
	    throws PassportServiceException,HPPReportException{
		
	
	   //Set the user profile data by retriving profile ID from HPP.
		try{
			mHPPReportModel.setUserProfileMap(getHPPUserProfileData(mUserInfo));
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" user profile data ";			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}
		
		
		//Set the group role		
		try{
			mHPPReportModel.setHppGroupRoleMap(getHPPGroupRoleMap(mProfileID));
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" user group role  id "  + mProfileID;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}				
		mHPPReportModel.setLocalHPPGroups(getHPPGroupsForSite(site));		
		return mHPPReportModel;
	}
	
	/*
	* Fetches user profile data from HPP given an HPP profile ID.
	* @param profileId the HPPID of user
	* @throws PassportServiceException 
	*/	
	private Map getHPPUserProfileData(UserInfo info) throws PassportServiceException{
        PassportService ws = mHPPReportToolkit.getPassportService();
        String adminSessionToken = null;
        
        try{
        	adminSessionToken = mHPPReportToolkit.getAdminSessionToken();	
        	mLog.debug("AdminSessionToken in getHPPUserProfileData() "+adminSessionToken);
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" admin session token for userInfo : "  + info;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}	
		
		//Get the user information
		HashMap profileMap = null;
		String profileId = null;
		try{
			String entityType = info.getHPPEntityType();
			String entity = info.getEntityIdentifier();
			
			if(mLog.isDebugEnabled()){
				mLog.debug("Entity type"+ entityType);
				mLog.debug("Entity value"+ entity);
			}
			mLog.info("Invoking HPP for user profile information");
			AdminViewUserResponseElement adminview = ws.adminViewUser(adminSessionToken, entity, entityType) ;					
			profileMap = new HashMap(20);
			profileMap.put("UserID", getNullString(adminview.getProfileIdentity().getUserId()));
			profileId = getNullString(adminview.getProfileIdentity().getProfileId());
			profileMap.put("ProfileId", profileId);
			
			profileMap.put("FirstName", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getFirstName())) ;
			profileMap.put("LastName", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getLastName())) ;
			profileMap.put("CountryCode", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getResidentCountryCode())) ;
			profileMap.put("SecurityQuestion", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityQuestion())) ;
			profileMap.put("SecurityAnswer", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityAnswer())) ;
			profileMap.put("Email", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getEmail())) ;
			profileMap.put("Securitylevel", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityLevel())) ;
			profileMap.put("LastSuccessfulLoginDate", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getLastSuccessfulLoginDate())) ;
			profileMap.put("LastAttemptedLoginDate", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getLastAttemptedLoginDate())) ;
			profileMap.put("PasswordCreationDate", getNullString(adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getPasswordCreationDate())) ;
			
			if(mLog.isDebugEnabled()){
				mLog.debug("Data retrived from HPP" + profileMap);
			}
			mLog.info("Fetched user profile information from HPP");
			
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" admin view for the id : "  + profileId;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}	
		
		mLog.info("Setting profile id");
		//This step will set the profileID for all future calls to HPP.
		info.setHPPID(profileId);
		setUserInfo(info);
		//Store the profile ID for future use in add/remove cases.
		setProfileID(profileId);
		
		return profileMap;		
	}
	
	/*
	* Fetches user group - role map from HPP given an profileId
	*/	
	private Map getHPPGroupRoleMap(String profileId) throws PassportServiceException{		
		//Get the member groups of the user
		PassportService ws = mHPPReportToolkit.getPassportService();
		mLog.debug("ProfileId in getHPPGroupRoleMap() : "+profileId);
		String adminSessionToken = null;
		try{
			 adminSessionToken = mHPPReportToolkit.getAdminSessionToken();
			 mLog.debug("AdminSessionToken in getHPPGroupRoleMap() : "+ adminSessionToken );
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" admin session token for the id :"  + profileId;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}		
		HashMap userAllGroupRoleMap = new HashMap(20);
		ProfileIdentity profileIdentity = new ProfileIdentity();
		profileIdentity.setProfileId(profileId);
		//Get the user groups
		try{
			GetUserGroupsResponseElement grp_response = ws.getUserGroups(adminSessionToken, profileIdentity);
			for (int i = 0; i < grp_response.getGroupRoleCount(); i++) 
			{
				GroupRole groupRole = grp_response.getGroupRole(i) ;
				userAllGroupRoleMap.put(groupRole.getGroupName(), groupRole.getRoleName());				
			}
		}catch(PassportServiceException psex){			
			String error = "Error in passport service while getting " +
					" user groups for the id : "  + profileId;			
			mLog.error(error + HPPReportToolkit.getFaultsString(psex) + 
					HPPReportToolkit.getStackTrace(psex));		
			throw psex;
		}		

		return userAllGroupRoleMap;
	}
	
	/*
	 * Fetches the all SPP groups in HPP  for this user site from database
	 */
	private List getHPPGroupsForSite(String site) throws HPPReportException{		
		if(site == null || "".equals(site)){
			mLog.error("Site is empty");
		}
		List list = new java.util.ArrayList();
		ReportDAO aReportDAO = new ReportDAO();
		try{
			list = aReportDAO.findAllHppGroups(site);
		}catch(Exception ex){
			mLog.error(ex.getMessage() + HPPReportToolkit.getStackTrace(ex));
			throw new HPPReportException(ex); 
		}	
		return list;
	}
	
	
	/*
	* Utility method to fetch null string.
	*/	
	private String getNullString(String value){
		return (value == null)?"":value;
	}
	/**
	* @return Returns the mUserInfo.
	*/
	public UserInfo getUserInfo() {
		return mUserInfo;
	}

	/**
	* @param userInfo The mUserInfo to set.
	*/
	public void setUserInfo(UserInfo userInfo) {
		mUserInfo = userInfo;
	}
	
}

