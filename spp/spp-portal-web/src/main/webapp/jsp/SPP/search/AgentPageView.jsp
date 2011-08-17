<!-- AgentPageView.jsp -->
<%/*--
	@(#)AgentPageView.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            11-Sep-2006          Created

	Note :This JSP page is responsible for rendering the agent page for a particular 
	Navigation Item. It expects the NavigationItem in the request.
		
--*/%>
<%@ page import="com.hp.spp.search.common.NavigationItem,
		 com.hp.spp.profile.Constants"
	 contentType="text/html; charset=UTF-8"
%>

<%	
	//Get the navigation item from the request.
	NavigationItem aNavigationItem = (NavigationItem) 
		request.getAttribute("NavigationItem");
		
	//Get search key words 
	String searchKeywords = aNavigationItem.getSearchKeywordsString();
	if(searchKeywords==null ||
		searchKeywords.length() == 0 ||
			Constants.SEARCHKEYWORDS_NOT_DEFINED.equals(searchKeywords)){
		       	searchKeywords = "";
	}
		
	
	// Retrieve the title
	String title = aNavigationItem.getTitle();
	if(title==null ||
		title.length()==0 ||
			Constants.TITLE_NOT_DEFINED.equals(title)){
			title="";
	} 

	// Retrieve the description
	String description = aNavigationItem.getDescription();
	
	if(description==null||
		description.length() == 0||
			Constants.DESCRIPTION_NOT_DEFINED.equals(description)){
			description = "";
	}

	//Retrive the Language
	String language = aNavigationItem.getLocale().getLanguage();
	
	//OVSD 604,876,212: This fix is done to differentiate between
	//traditional and simplified chinese in meta tag.
	if("zh".equalsIgnoreCase(aNavigationItem.getLocale().getLanguage())){
		if("tw".equalsIgnoreCase(aNavigationItem.getLocale().getCountry())){
			language = "zh-tw";
		}else if("cn".equalsIgnoreCase(aNavigationItem.getLocale().getCountry())){
			language = "zh-cn";
		}
	}
	
	if(language==null ||language.length()==0){
		language = "";
	}
	
	// Retrieve the groups and frame a string with comma seperated values
	Collection groupList = aNavigationItem.getGroupList();
	String userGroup_formatted_string = "";
	if(groupList!=null && groupList.size() > 0){
		Iterator groupListItr = groupList.iterator();
		StringBuffer userGroup_formatted_temp = new StringBuffer();		
		while(groupListItr.hasNext()){
			String aUserGroup = (String)groupListItr.next();		
			userGroup_formatted_temp.append(aUserGroup).append(",");

		}

		if(userGroup_formatted_temp!=null){
			userGroup_formatted_temp.deleteCharAt(userGroup_formatted_temp.length()-1);
			userGroup_formatted_string = userGroup_formatted_temp.toString();			
		}
	}
	
	if(userGroup_formatted_string==null ||
		userGroup_formatted_string.length()==0){
		userGroup_formatted_string = "";
	}

	//Constructing the framesetUrl
	String dnsName = (String)request.getParameter("SiteDNS");
	String framesetUrl =  request.getContextPath() +
				"/site/" + dnsName + "/menuitem."+ aNavigationItem.getId(); 
			
%>		
<html lang="<%=language%>">
	<head>
		<meta HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8"/>		
		<meta NAME="keywords" CONTENT="<%=searchKeywords%>"/>
		<!-- <meta NAME="Title" CONTENT="<%= title %>"/> -->
		<meta NAME="Description" CONTENT="<%= description %>" />	
		<!-- <meta NAME="Language" CONTENT="<%=language%>"/> -->
		<meta NAME="date" CONTENT="29/05/2006"/>
		<meta NAME="frameset_URL" CONTENT="<%= framesetUrl %>">			
		<meta NAME="spp_groups" CONTENT="<%= userGroup_formatted_string%>"/>		
		<meta NAME="spp_page_id" CONTENT="<%= aNavigationItem.getId() %>"/>
		<meta NAME="spp_site" CONTENT="<%= dnsName %>"/>
		<meta NAME="countries" CONTENT="WW"/>
		<title><%= title %></title>
	</head>

	<body>
		<p><%= title %></p>
		<p><%= description %></p>		
	</body>
</html>
