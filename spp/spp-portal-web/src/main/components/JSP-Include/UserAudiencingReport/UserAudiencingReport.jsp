<!-- UserAudiencingReport.jsp -->
<%/*--
	@(#)UserAudiencingReport.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            10-Oct-2006          Created

	Note :
	
--*/%>

<%@ page import="
	java.util.Iterator,
	java.util.List,
	java.util.ArrayList,
	java.util.Collection,
	java.util.Set,
	java.util.TreeMap,
	java.util.StringTokenizer,
	com.hp.spp.config.Config,
	javax.servlet.jsp.JspWriter,
	java.io.IOException,
	java.util.HashMap,
	com.epicentric.user.UserGroupQueryResults,	
	com.epicentric.user.UserGroupManager,
	com.epicentric.user.UserGroup,	
	com.epicentric.entity.EntityPersistenceException,
	com.epicentric.user.User,
	com.vignette.portal.website.enduser.PortalContext,
	com.epicentric.common.website.EndUserUtils,	
	java.util.Map,
	com.epicentric.site.Site,
	com.hp.spp.config.ConfigException,
	org.apache.log4j.Logger,
	com.epicentric.repository.Repository,
	com.epicentric.repository.RepositoryManager,
	com.epicentric.uid.UIDException,
	com.hp.spp.profile.Constants"	 		
        contentType="text/html; charset=UTF-8"
             
%>

<%!
	//Declare variable
	 private static final Logger mLog = Logger.getLogger("UserAudiencingReport.jsp");
	 
	/* This method retrives the user groups as a map.                 
         - It obtains all the groups of portal by calling getUserGroups() method          
	 - It iterates through the collections and constructs a new collection 
	   which contains groups belonging to a site. Here it is assumed that
	   a group belongs to a site if group title contains site name.	
         - Any group for which user has membership such groups titles are appeneded with 
           member tag.
        */
	private List getSiteGroups(PageContext pageContext,JspWriter out) throws IOException,
		EntityPersistenceException,UIDException{		
		List siteGroups =  new ArrayList();
		
		//Get the current user
		PortalContext aPortalContext = EndUserUtils.getPortalContext(pageContext);
		User currentUser = aPortalContext.getCurrentUser();
		// Get the site
		Site aSite = EndUserUtils.getSite(pageContext);
		String siteDNSName = aSite.getDNSName();
		
		//Each site maintains a main repository to store 
		//Components belongs to a site. 
		//Get the main Repository Id from the site.		
		Repository aRepository = aSite.getMainRepository();
		//Get the instance of the RepositoryManager
		RepositoryManager aRepositoryManager = RepositoryManager.getInstance();
		
		//Get the all the available groups 		
		UserGroupQueryResults allGroups = null;		
		UserGroupManager ugMgr = UserGroupManager.getInstance();		
		allGroups = ugMgr.getAllUserGroups();	
		
		//out.println("No of groups "+allGroups.size());
		
		//Iterate through the all groups		
		while (allGroups.hasNext()) {
			UserGroup aUserGroup = (UserGroup)allGroups.next();			
			String groupTitle= (String)aUserGroup.getProperty(UserGroup.TITLE_PROPERTY_ID);
			String groupUid = aUserGroup.getUID();					
			//check wheather user group belongs to  repository of the site.
			boolean isSiteGroup = aRepositoryManager.contains(aUserGroup.getUID(), aRepository);			
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
				String key =  groupTitle+ userGroupsAndSiteGroups.size()+"memberFlag";
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
       
	String configKey = null;
	String defaultFile = null;	
	String error = null;
	
	//Retrive user profile information  from the session as a map 	
	Map userProfile = (Map) session.getAttribute(Constants.PROFILE_MAP);	
	if(userProfile==null) {	
		//Log it as error and forward to error.jsp
		error = "User profile is not available ";
		mLog.error(error);
		request.setAttribute("Message",error);
		configKey = "SPP.report.ErrorPage";
		//defaultFile = "ErrorPage.jsp";
		defaultFile = "jsp/SPP/report/ErrorPage.jsp";
		include(configKey,defaultFile,application,request,response);
		return;
	}else{
		request.setAttribute("UserProfile",userProfile);
	}	
	
	
	try{	       
	        List userGroups = getUserGroups(userProfile);
	        List siteGroups = getSiteGroups(pageContext,out);
		Map  userGroupsAndSiteGroups = getUserGroupsAndSiteGroups(userGroups,siteGroups);		
		request.setAttribute("UserGroups",userGroupsAndSiteGroups);
	}catch(EntityPersistenceException aEntityPersistenceException){
		//log the error and forward to error.jsp		       
		error = "error during retrieval of user groups : " + aEntityPersistenceException.getMessage();
		mLog.error(error);
		request.setAttribute("Message",error);
		configKey = "SPP.report.ErrorPage";
		//defaultFile = "ErrorPage.jsp";	
		defaultFile = "jsp/SPP/report/ErrorPage.jsp";
		include(configKey,defaultFile,application,request,response);
		return;
	}catch(UIDException aUIDException){
		//log the error and forward to error.jsp		       
		error = "error during retrieval of user groups from the site repository : " + aUIDException.getMessage();
		mLog.error(error,aUIDException);
		request.setAttribute("Message",error);
		configKey = "SPP.report.ErrorPage";
		//defaultFile = "ErrorPage.jsp";	
		defaultFile = "jsp/SPP/report/ErrorPage.jsp";
		include(configKey,defaultFile,application,request,response);
		return;
	
	}
	
	//Include the AudiencingReportHelper.jsp
	configKey = "SPP.report.AudiencingReportHelper";
	//defaultFile = "AudiencingReportHelper.jsp";
	defaultFile = "jsp/SPP/report/AudiencingReportHelper.jsp";
	include(configKey,defaultFile,application,request,response);	
		
%>