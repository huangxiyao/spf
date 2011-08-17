<!-- AdminAudiencingReport.jsp -->
<%/*--
	@(#)AdminAudiencingReport.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            10-Oct-2006          Created

	Note :This is a jsp include page which will extract user information and groups of
	a site and constructs audiencing report.

--*/%>

<%@ page import="
	java.util.*,
	com.hp.spp.config.Config,
	javax.servlet.jsp.JspWriter,
	java.io.IOException,
	java.util.HashMap,
	com.epicentric.user.UserGroup,	
	com.epicentric.user.User,
	com.hp.spp.profile.Constants,	 
	com.epicentric.common.website.CookieUtils,	 
	com.hp.spp.webservice.ugs.manager.SPPUserGroupManager,	 
	com.epicentric.user.UserManager,
	com.hp.spp.webservice.ups.manager.SPPUserManager,
	com.epicentric.common.website.EndUserUtils,
	com.epicentric.site.Site,
	com.epicentric.entity.EntityPersistenceException,
	com.epicentric.entity.EntityNotFoundException,
	org.apache.log4j.Logger,
	com.epicentric.user.UserGroupQueryResults,	
	com.epicentric.user.UserGroupManager,
	com.epicentric.repository.Repository,
	com.epicentric.repository.RepositoryManager,
	com.epicentric.uid.UIDException

	"	
	contentType="text/html; charset=UTF-8"
%>

<%!      
        //Declare variable
         private static final Logger mLog = Logger.getLogger("AdminAudiencingReport.jsp");
  

        /* This method retrives the user profile and ugs user groups as a map.          
	  - It calls the UGS to get the user profile data using SPPUserManager.getUserProfile() method          
	  - It calls the UPS to get the ugs user groups using SPPUserGroupWSManager.getVignetteGroupsAsSet() method        
	  - It re-computes the ugs user groups using SPPUserGroupManager.computeUserGroup() method	  - 
	  - It updates the computed_userGroups into userProfile 
	  - Any group for which user has membership such groups titles are appeneded with 
	    member tag.
	*/
	private Map getUserProfileAndUgsUserGroups(String emailID,PageContext pageContext,
		HttpServletRequest request) 
			throws EntityPersistenceException,EntityNotFoundException{
		
		// Create a map which contains both user profile 
		// and ugs user group infomration
		Map userProfileAndGroups = new HashMap();
		
		//Get the user based on email id from Vignette database
		UserManager userManager = null;
		User user = null;
		//Get the user based on email id
		userManager = UserManager.getInstance();
		user = userManager.getUser(Constants.VGN_EMAIL, emailID);
		
		//Get the user id
		String userId = user.getUID();
		mLog.debug("UserID" +userId);
		
		//Get the hpp_id of the user
		String hppId = (String) user.getProperty(Constants.VGN_HPPID);
		mLog.debug(" hppID" +hppId);
		
		//Get the site
		Site aSite = EndUserUtils.getSite(pageContext);
		String site = aSite.getDNSName();
		mLog.debug("site" + site);		
		
		//Get the session token
		String sessionToken = CookieUtils.getCookieValue(request, Constants.SMSESSION);		
		mLog.debug("sessionToken" + sessionToken);

		Map userProfile = null;

		// Call UPS to get the user profile data on error log exception
		try{
			SPPUserManager aSPPUserManager = new SPPUserManager();
			userProfile = aSPPUserManager.getUserProfile(hppId,site,true); 
			userProfileAndGroups.put("UserProfile", userProfile);
		}catch(Exception ex){
			mLog.error("UPS exception occured ", ex);
		}		
		// Call UGS to get the user groups on exception log and continue
		// errors will be gracefully handled in jsp code
		try{
			SPPUserGroupManager aSPPUserGroupManager = new SPPUserGroupManager();
			Set<UserGroup> ugs_UserGroup = aSPPUserGroupManager.getVignetteGroups(site, (HashMap) userProfile);
				
			// Calculate the user_groups by doing a differential
			// of existing user groups
			Set computed_userGroups = aSPPUserGroupManager.computeUserGroup(userProfile, ugs_UserGroup);
			
			// Add the computed_userGroups into the userProfile as semicolon seperated	
			Map ugsUserGroups = new HashMap();
			StringBuffer userGroupList = new StringBuffer();
			for (Iterator iter = computed_userGroups.iterator(); iter.hasNext();) {
				UserGroup aUserGroup = (UserGroup) iter.next();
				if (userGroupList.length() > 0){
					userGroupList.append(';');
				}
				String groupTitle = aUserGroup.getProperty("title").toString();
				userGroupList.append(groupTitle);
				String groupId = aUserGroup.getUID();	
				//Add memberFlagto groupId and - member tag to groupTitle
				//ugsUserGroups.put(groupId+"memberFlag", groupTitle + " - member ");
				ugsUserGroups.put(groupId, groupTitle);
			}
			if (userGroupList.length() > 0){
				userProfile.put(Constants.MAP_USERGROUPS, userGroupList.toString());
			}
			userProfileAndGroups.put("UgsUserGroups",ugsUserGroups);
		}catch(Exception e){
			mLog.error("UGS exception occured " + e);		
		}
		
		return userProfileAndGroups;			
	}	
	
		
	/* This method retrives the  groups belonging to a perticular site.          	          
	  - It obtains all the groups of portal by calling getUserGroups() method          
	  - It iterates through the collections and constructs a new collection 
	    which contains groups belonging to a site. Here it is assumed that
	    a group belongs to a site if group title contains site name.	 
	*/
	private List getSiteGroups(String emailID,Map ugsUserGroups,PageContext pageContext,JspWriter out) 
		throws EntityPersistenceException,IOException,UIDException,
			EntityNotFoundException{		
		List siteGroups =  new ArrayList();
		
		// Get the site
		Site aSite = EndUserUtils.getSite(pageContext);
		String siteDNSName = aSite.getDNSName();
		
		//Each site maintains a main repository to store 
		//Components belongs to a site. 
		//Get the main Repository Id from the site.		
		Repository aRepository = aSite.getMainRepository();
		//Get the instance of the RepositoryManager
		RepositoryManager aRepositoryManager = RepositoryManager.getInstance();
		
		
		//Get the user based on email id from Vignette database
		UserManager userManager = null;
		User user = null;		
		userManager = UserManager.getInstance();
		user = userManager.getUser(Constants.VGN_EMAIL, emailID);
		

		//Get the all the available groups 		
		UserGroupQueryResults allGroups = null;		
		UserGroupManager ugMgr = UserGroupManager.getInstance();
		allGroups = ugMgr.getAllUserGroups();
		
		//Iterate through the all groups
		
		while (allGroups.hasNext()) {
			UserGroup group = (UserGroup)allGroups.next();			
			String groupTitle= (String)group.getProperty(UserGroup.TITLE_PROPERTY_ID);
			String groupUid = group.getUID();
			//check wheather user group belongs to  repository of the site.
			boolean isSiteGroup = aRepositoryManager.contains(group.getUID(), aRepository);						
			boolean isMember = user.hasParent(group, false);
			
			if(isSiteGroup){							
				siteGroups.add(groupTitle);	
			}			
			
		}//while		
		return siteGroups;
	} 
	
	
	/*
	  This method includes the another jsp page.
	*/
	private void include(String configKey,String defaultFile,
		ServletContext application,HttpServletRequest request,
			HttpServletResponse response) throws ServletException,IOException{
		String filePath = null;		
		try{
			filePath = (String)Config.getValue(configKey);       			
		}catch(Exception ex){
			//Log the exception but don't forward to error.jsp  		       
			String warn = "error occured while retrieving " + configKey +
			" value from persistence store : " + ex.getMessage(); 
			mLog.warn(warn,ex);

		}		
		if(filePath == null || filePath.length() == 0){
			filePath = defaultFile;
		}
		filePath = "/"+filePath;		
		RequestDispatcher rd = application.getRequestDispatcher(filePath);
		rd.include(request,response);		
	}
	
	
	
	/*
	  This method gives a Map which contains both user groups and site groups.
	*/
	private Map getUserGroupsAndSiteGroups(List userGroups,List siteGroups){
		Map userGroupsAndSiteGroups = new TreeMap();		
		if(userGroups!=null && userGroups.size()>0){
			ListIterator userGroupsItr = userGroups.listIterator();
			while(userGroupsItr.hasNext()){
				String groupTitle = (String)userGroupsItr.next();
				// The keys are created just for internal use, nothing to do with functionality.
				// These keys will be used in AudiencingReportHelper.jsp to determine
				// wheather the user is member of the group or not. If key contains memberFlag
				// then user is member of that group.
				String key = groupTitle+userGroupsAndSiteGroups.size()+"memberFlag";
				String value =groupTitle+" - member ";
				userGroupsAndSiteGroups.put(key,value);
				if(siteGroups!=null && siteGroups.size()>0){
					siteGroups.remove(groupTitle);
				}//if
			}//while
		}//if
		if(siteGroups!=null && siteGroups.size()>0){
			ListIterator siteGroupsItr = siteGroups.listIterator();
			while(siteGroupsItr.hasNext()){
				String groupTitle = (String)siteGroupsItr.next();
				// The keys are created just for internal use, nothing to do with functionality.
				// These keys will be used in AudiencingReportHelper.jsp to determine
				// wheather the user is member of the group or not. If key contains memberFlag
				// then user is member of that group.
				String key = groupTitle+userGroupsAndSiteGroups.size()+"";
				String value =groupTitle;
				userGroupsAndSiteGroups.put(key,value);
			}//while

		}//if		
		return userGroupsAndSiteGroups;
	}//getUserGroupsAndSiteGroups
	
	
	/*
	  This method gives the User Groups from the user profile.
	*/
	private  List getUserGroups(Map userProfile){
		List userGroups = new ArrayList();
		String delims = ";";
		String groupsString = (String)userProfile.get(Constants.MAP_USERGROUPS);
		if(groupsString != null && groupsString.trim().length() > 0  ){
			StringTokenizer st = new StringTokenizer(groupsString, delims);
			while (st.hasMoreTokens()) {
				String group = st.nextToken();
				userGroups.add(group);
			}//while
		}//if
		return userGroups;
	}//getUserGroups
	
	
%>




<%      
	String hppEmail = request.getParameter("emailInput");
	String message = null;	
	if (hppEmail != null) {
	        hppEmail = hppEmail.trim();
	        String configKey = null;
		String defaultFile = null;
		try {			
			Map userProfileAndUgsUserGroups = getUserProfileAndUgsUserGroups(
				hppEmail,pageContext,request);	
			//Get the userProfile	
			Map userProfile = (Map)userProfileAndUgsUserGroups.get("UserProfile");				
			if(userProfile==null){
			        // Log it as error and forward to error.jsp
				String error = "User profile is not available";
				mLog.error(error);
				configKey = "SPP.report.ErrorPage";
				//defaultFile = "ErrorPage.jsp";
				defaultFile = "jsp/SPP/report/ErrorPage.jsp";
				request.setAttribute("Message",error);
				include(configKey,defaultFile,application,request,response);
				return;
			}else{
				request.setAttribute("UserProfile",userProfile);
			}
			
			//Get the UgsUserGroups
			Map ugsUserGroups = (Map)userProfileAndUgsUserGroups.get("UgsUserGroups");			
			//request.setAttribute("UserGroups",ugsUserGroups);
			
			try{	
				//ugsUserGroups = null;
				List userGroups = getUserGroups(userProfile); 
				List siteGroups = getSiteGroups(hppEmail,ugsUserGroups,pageContext,out);
				Map  userGroupsAndSiteGroups = getUserGroupsAndSiteGroups(userGroups,siteGroups);				
				request.setAttribute("UserGroups",userGroupsAndSiteGroups);
				
			}catch(EntityPersistenceException aEntityPersistenceException){
				//log the error and forward to error.jsp		       
				String error = "error during retrieval of user groups : " + aEntityPersistenceException.getMessage();
				mLog.error(error);
				request.setAttribute("Message",error);
				configKey = "SPP.report.ErrorPage";
				//defaultFile = "ErrorPage.jsp";	
				defaultFile = "jsp/SPP/report/ErrorPage.jsp";
				include(configKey,defaultFile,application,request,response);
				return;
			}catch(UIDException aUIDException){
				//log the error and forward to error.jsp		       
				String error = "error during retrieval of user groups from the site repository : " + aUIDException.getMessage();
				mLog.error(error,aUIDException);
				request.setAttribute("Message",error);
				configKey = "SPP.report.ErrorPage";
				//defaultFile = "ErrorPage.jsp";	
				defaultFile = "jsp/SPP/report/ErrorPage.jsp";
				include(configKey,defaultFile,application,request,response);
				return;
			}	
			
			// Include the AudiencingReportHelper.jsp 			
			configKey = "SPP.report.AudiencingReportHelper";
			//defaultFile = "AudiencingReportHelper.jsp";
			defaultFile = "jsp/SPP/report/AudiencingReportHelper.jsp";
			include(configKey,defaultFile,application,request,response);
			
		}catch (EntityPersistenceException e) {					
			message = "No user exist in the database for this email [ "+ hppEmail + " ]";
		}catch (EntityNotFoundException e) {					
			message = "No user exist in the database for this email [ "+ hppEmail + " ]";
		}	
		
	}
	if (message != null) {
		out.println("<p><font style='color:#FF0000;'>" + message + "</font></p>");
	}
	
%>        

<% if(hppEmail==null || message != null ) { %>
<form name="adminAudienceRpt" method="get" action="">
  <table width="75%" border="0" cellspacing="0" cellpadding="0" align="left">
      <tr>
        <td width="28%" class="bold" >Select a User : </td>
        <td width="37%">&nbsp;</td>
        <td width="35%">&nbsp;</td>
      </tr>
      <tr>
        <td width="28%">&nbsp;</td>
        <td width="37%">&nbsp;</td>
        <td width="35%">&nbsp;</td>
      </tr>
      <tr>
        <td width="28%" class="bold">email address</td>
        <td width="45%"> 
          <input type="text" name="emailInput">
        </td>
        <td width="35%"> 
          <input class="primButton"  type="submit" name="Submit" value="Submit &raquo;">
        </td>
      </tr>
    </table>
</form>
<% } %>
     


