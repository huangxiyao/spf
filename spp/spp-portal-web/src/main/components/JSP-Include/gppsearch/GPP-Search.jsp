<!-- GPP-Search.jsp -->
<%/*--
	@(#)GPP-Search.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            11-Sep-2006          Created

	Note :This is a dummy search page
--*/%>

<%@ page import="java.util.Iterator,
	java.util.List,
	java.util.ArrayList,
	java.util.Collection,
	java.util.Set,
	com.epicentric.site.Site,	
	com.epicentric.template.Style,
	com.epicentric.page.Page,
	java.util.Locale,
	com.epicentric.i18n.EditableLocalizedBundle,
	com.epicentric.common.website.I18nUtils,
	com.epicentric.user.UserGroup,
	com.epicentric.user.UserGroupManager,
	com.epicentric.user.UserGroupQueryResults,
	com.epicentric.authorization.PrincipalSet,
	com.epicentric.common.website.EndUserUtils,
	com.epicentric.common.website.MenuItemNode,
	com.epicentric.common.website.MenuItemUtils,
	com.epicentric.entity.EntityPersistenceException,
	com.vignette.portal.website.enduser.PortalContext,
	com.epicentric.common.website.AuthorizationUtils,
	com.epicentric.user.*,
	com.epicentric.permission.PermissionUtils,
	com.epicentric.entity.EntityType,
	java.net.URLEncoder"
	
	contentType="text/html; charset=UTF-8"
%>


<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>GPP - Search Dummy Page</title>

</head>

<body bgcolor="#008080">

<div align="center">
&nbsp;<p>&nbsp;</p>
	<table border="0" width="63%" id="table1">
		<tr>
			<td bgcolor="#808080">
			<p align="center"><b><font size="6">GPP - Search Dummy Page </font>
			</b></td>
		</tr>
		<tr>
			<td bgcolor="#C0C0C0">&nbsp;</td>
		</tr>
		<tr>
			<td bordercolor="#808080">
			<form method="POST" name="MainForm" method="post" action="/portal/jsp/SPP/search/testGPPSearch.jsp">
				<table border="0" width="100%" id="table2">
				
					<tr>
						<td width="302" bgcolor="#C0C0C0">
							<p align="right">
							<b>
								<font color="#008080">Audience
									<input type="radio" value="yes" checked name="Audience">
								</font>
							</b>
						</td>
						<td bgcolor="#C0C0C0">
							<p align="left">
							<b>
								<font color="#008080">Don't Audience
									<input type="radio" name="Audience" value="no">
								</font>
							</b>
						</td>
					</tr>
					<tr>
						<td width="302" bgcolor="#C0C0C0">
						<p align="right"><b><font color="#008080">Query Text</font></b></td>
						<td bgcolor="#C0C0C0">
						<input type="text" name="qt" size="22"></td>
					</tr>
					
					
					
					
					<tr>
					        						<td width="302" bgcolor="#C0C0C0">
						<p align="right"><b><font color="#008080">Language</font></b></td>
						<td bgcolor="#C0C0C0"><select size="1" name="lang">
						<% 
							Collection supportedLocalesList =  I18nUtils.getRegisteredLocales();
							if(supportedLocalesList!=null && supportedLocalesList.size()>0){				
								//Create a NavigationItem Object for all supported locales					  
								Iterator supportedLocalesListItr = supportedLocalesList.iterator();
								while (supportedLocalesListItr.hasNext()) {
									Locale supportedLocale = (Locale)supportedLocalesListItr.next();
									String languageCode = supportedLocale.getLanguage();
									if(languageCode.equals("en")){
										out.println("<option selected value="+languageCode+">");
										out.println(languageCode);
										out.println("</option>");
									}else{
										out.println("<option value="+languageCode+">");
										out.println(languageCode);
										out.println("</option>");
									}	
								}
							 }					
						%>						
						</select></td>
					</tr>
					<tr>
						<td width="302" bgcolor="#C0C0C0">
						<p align="right">
						<input type="submit" value="Submit" name="Submit"></td>
						<td bgcolor="#C0C0C0">
						<input type="reset" value="Reset" name="Reset"></td>
					</tr>
				</table>
				<%      
				        //Get All Menu Items
				        List menuItemNodesList = MenuItemUtils.getAllNodes(pageContext);
				        
				        //Get the site
				        Site aSite = EndUserUtils.getSite(pageContext);
				        
				        //Get the user
				        PortalContext aPortalContext = EndUserUtils.getPortalContext(pageContext);
				        User user = aPortalContext.getCurrentUser();
				                          
				        String hpp_id = (String)user.getProperty("hpp_id");				        
					// Get the groups 	
					StringBuffer groupList_StringBuffer = new StringBuffer();
					String groupList_String = null;	
					
						
					EntityType  userGroupType = UserGroupManager.getInstance().getUserGroupEntityType();					
					try{
						Set userGroups = user.getParents(userGroupType,true);							
						Iterator userGroupsItr = userGroups.iterator();
						while (userGroupsItr.hasNext()){		
							UserGroup aUserGroup = (UserGroup)userGroupsItr.next();			
							String title = (String)aUserGroup.getProperty("title");
							groupList_StringBuffer.append(title).append(",");							
						} 	

					}catch (Exception exception){
						exception.printStackTrace();
					// Handle exception.
					}					
					
					
					if(groupList_StringBuffer!=null){
						groupList_StringBuffer.deleteCharAt(groupList_StringBuffer.length()-1);
						groupList_String = groupList_StringBuffer.toString();						
					}
					
					
					
					
					
				
				%>					
								
				<input type="hidden" name="groups" value="<%=groupList_StringBuffer%>">
				<input type="hidden" name="spp_site" value="<%=aSite.getDNSName()%>">
				<input type="hidden" name="hpp_id" value="<%=hpp_id%>">
				
				
			</form>
			<p>&nbsp;</td>
		</tr>
	</table>
</div>

</body>

</html>
