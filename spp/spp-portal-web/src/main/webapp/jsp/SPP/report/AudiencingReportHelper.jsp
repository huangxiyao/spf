<!-- AudiencingReportHelper.jsp -->
<%/*--
	@(#)AudiencingReportHelper.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------


	v1        ShivaShanker B            10-Oct-2006          Created
        v2        ShivaShaker B             1-Dec-2006           Modified

	Note :
	
--*/%>

<%@ page import="
	java.util.Iterator,
	java.util.List,	
	java.util.Set,	
	java.util.TreeMap,
	java.util.HashMap,
	javax.servlet.jsp.JspWriter,
	java.io.IOException,
	java.util.Map,
	com.vignette.portal.website.enduser.PortalContext,
	com.epicentric.common.website.EndUserUtils,			
	com.epicentric.user.User,
	com.epicentric.site.Site,	
	com.epicentric.user.UserGroupQueryResults,	
	com.epicentric.user.UserGroupManager,
	com.epicentric.user.UserGroup,	
	com.epicentric.entity.EntityPersistenceException,
	com.hp.spp.config.Config,
	org.apache.log4j.Logger"	
        contentType="text/html; charset=UTF-8"
             
%>
<%!
         //Declare variable
         private static final Logger mLog = Logger.getLogger("AudiencingReportHelper.jsp");
         private static final String SIMULATINGUSER = "SimulatingUser";
         private static final String SIMULATOR= "Simulator.";

	/* 
	  This method displays the audmin audience report.
	*/		
	public void displayUserProfileAndGroups(HttpServletRequest request,JspWriter out,Map userProfile,Map userGroups) throws IOException {	        
		displayHyperLink(out);
		String tableTitleForUserAttributes = "USER ATTRIBUTES ";
		String tableTitleForGroups = "Group Membership";
		
		// Step 1 : Display the user profile information
	        if(userProfile!=null){			
			tableTitleForUserAttributes = "USER ATTRIBUTES "; 
			//Get the profile of the simulated user from 
			// the user profile map i.e userProfile						
			Map simulatingUserProfile = (Map)userProfile.get(SIMULATINGUSER);
			
			//Create a map to hold modified simulating user profile
			Map modifiedSimulatingUserProfile = new HashMap();			

			if(simulatingUserProfile!=null && simulatingUserProfile.size() >0 ){
				//Remove the simulating user profile from the user profile
				userProfile.remove(SIMULATINGUSER);

				Iterator simulatingUserProfileKeysItr = 
					simulatingUserProfile.keySet().iterator();
				while(simulatingUserProfileKeysItr.hasNext()){				   
					String key = (String)simulatingUserProfileKeysItr.next();					
					Object value = (Object)simulatingUserProfile.get(key);					
					
					//Add simulator word before each key in the simulator user profile
					modifiedSimulatingUserProfile.put(SIMULATOR+key,value);	
					
				}
					
				//The below table is added to display User Profile and Simulated User profile
				//in a single table and provide one empty row between each the profiles.
				 
				out.println("<table width='100%'>");
				out.println("<tr><td width='100%'>");				
				writeMapHtml(out, userProfile,tableTitleForUserAttributes,"DoubleColumnTable");				
				out.println("</td></tr>");
				
				out.println("<tr><td width='100%'>");				
				out.println("</td></tr>");				
				
				out.println("<tr><td width='100%'>");				
				writeMapHtml(out, modifiedSimulatingUserProfile,null,"DoubleColumnTable");				
				out.println("</td></tr>");
				out.println("</table>");
				
				//Reset the tableTitleForGroups
				tableTitleForGroups = tableTitleForGroups + "( These are the groups for the simulated user. )";

			}else{
				//display the user profile 			
				writeMapHtml(out, userProfile,tableTitleForUserAttributes,"DoubleColumnTable");	
			}
		}
		
		//Step 2: Display the group information
		if(userGroups!=null){			
			writeMapHtml(out, userGroups,tableTitleForGroups,"SingleColumnTable");
		}	
	}
	/*
	   This method displays local hyperlinks for User attributes and Group memberships	
	*/
	private void displayHyperLink(JspWriter out) throws IOException {		
		out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%'>");
			out.println("<tr>");
				out.println("<td align='left' valign='top' width='10'><font color='#003366'>&raquo;&nbsp;</font></td>");
				out.println("<td align=left><a href='#UserAttributes'>User attributes</a></td>");
			out.println("<tr>");
			out.println("<tr>");
				out.println("<td align='left' valign='top' width='10'><font color='#003366'>&raquo;&nbsp;</font></td>");
				out.println("<td align=left><a href='#GroupMembership'>Group membership</a></td>");
			out.println("<tr>");
		out.println("</table>");
	}
	
	
	/*
	   This method generates html table code fragment for the map.	
	*/
	private void writeMapHtml(JspWriter out, Object obj, String tableTitle,String tableFormat) throws IOException {
	        boolean alternateColor = true;
		out.println("<table width='100%'>");
		if(tableTitle!=null){
			String anchorName = null;
			if(tableTitle.indexOf("USER")!= -1){
				anchorName = "UserAttributes";		
			}else{
				anchorName = "GroupMembership";
			}
			out.println("<tr>");
			out.println("<td width='100%' colspan='2' style='background-color: #eb5f01;color: #ffffff;font-weight: bold;text-align: left;'>");			
			out.println("<a name="+anchorName+">");
			out.println(tableTitle);
			out.println("</a>");
			out.println("</td>");
			out.println("</tr>");
		}
		if (obj!=null && (obj instanceof Map)) {
		        Map map = new TreeMap((Map) obj);		        
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				if(alternateColor && "DoubleColumnTable".equals(tableFormat)){
					out.println("<tr class='colorDCDCDCbg'>");
					alternateColor = false;
				}else{
					out.println("<tr class='colorFFFFFFbg'>");
					alternateColor =true;
				}
				if("DoubleColumnTable".equals(tableFormat)){
					out.println("<td width='40%'>" + entry.getKey() + "</td>");
					out.println("<td width='60%'>");
					writeObjectHtml(out, entry.getValue());
					out.println("</td>");
				} else if("SingleColumnTable".equals(tableFormat)){
				        String key = (String)entry.getKey(); 
				        if(key.indexOf("memberFlag")!= -1){
						out.println("<tr bgcolor='#FFCCCC'>");				        
					}else{
						out.println("<tr>");	
					}	
					out.println("<td width='100%' colspan='2'>" + entry.getValue() + "</td>");															
					
				}	
				out.println("</tr>");
			}
		}
		out.println("</table>");
	}
	
	/*
	  This is a helper method to the writeMapHtml
	*/
	private void writeObjectHtml(JspWriter out, Object obj) throws IOException {
		if (obj instanceof Map) {
			writeMapHtml(out,obj,null,"DoubleColumnTable");
		}
		else if (obj instanceof List) {
			writeListHtml(out, (List) obj);
		}
		else {
			out.println(obj);
		}
	}
	
	/*	  
	  This method iterates through the list and prints the values in single line .
	*/ 
	private void writeListHtml(JspWriter out, List list) throws IOException{
		out.println("<div>");
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object obj = it.next();
			writeObjectHtml(out, obj);
		}
		out.println("</div>");
	}
	
		
%>

<%
        
        //The userProfile is already defined in calling jsp        
	Map userProfileInRequest = (Map) request.getAttribute("UserProfile");
	
	//Copy the user profile. This does a shallow copy i.e. it only copies the Map but the
	//key and values in the Map refer to the SAME objects as in the first Map.
	Map userProfileInRequestCopy = new TreeMap(userProfileInRequest);
	
	//The userGroups is already defined in calling jsp    
	Map userGroupsInRequest = (Map) request.getAttribute("UserGroups");
	
	//Display the user profile and groups information.
	displayUserProfileAndGroups(request,out,userProfileInRequestCopy,userGroupsInRequest); 
	
%> 

































