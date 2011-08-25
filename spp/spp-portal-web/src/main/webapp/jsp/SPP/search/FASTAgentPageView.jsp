<!-- FASTAgentPageView.jsp -->
<%/*--
	@(#)FASTAgentPageView.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        sebestyj                 10-Jan-2011          Created

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
	String country = aNavigationItem.getLocale().getCountry();
	StringBuilder idString = new StringBuilder(aNavigationItem.getId());
	idString.append("#");
	if( null != language && language.length() != 0 ){
		idString.append(language);
	}
	
	if ( null != country && country.length() != 0 ) {
		if( language.length() > 0 ){
			idString.append("_");
		}
		idString.append(country);
	}

	
	//OVSD 604,876,212: This fix is done to differentiate between
	//traditional and simplified chinese in meta tag.
	if("zh".equalsIgnoreCase(language)){
		if("tw".equalsIgnoreCase(country)){
			language = "zh_tw";
		}else if("cn".equalsIgnoreCase(country)){
			language = "zh_cn";
		}
	}
	
	if(language==null ||language.length()==0){
		language = "IX";
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
		userGroup_formatted_string = "AllUserGroups";
	}

	//Constructing the framesetUrl
	String dnsName = (String)request.getParameter("SiteDNS");
	String framesetUrl =  request.getContextPath() +
				"/site/" + dnsName + "/menuitem."+ aNavigationItem.getId();
	
	// This hack was requested by gpp to force countries into the index based on which site the index represents.  This is being implemented brute-force because it is due NOW and there is no
	// sense trying to be elegant with such an ugly hack
	country = "WW";
	if( dnsName.equalsIgnoreCase("smartportal") ){
		country = "TN,MA,LY,GF,PF,TF,GP,MQ,NC,RE,PM,WF,KM,BJ,BF,BI,CM,CV,CF,TD,CG,DJ,GQ,GA,GM,GN,GW,CI,ML,MR,NE,ST,SN,TG,MG,CD,DZ,ER,ET,KE,RW,SO,TZ,UG,SC,AO,BW,LS,MW,MU,MZ,NA,SZ,ZM,ZW,GH,LR,NG,SL,BH,KW,OM,QA,SY,YE,JO,LB,EG,AE,IQ,IL,GR,CY,SA,ZA,TR,IE,GB,BE,LU,FI,LT,EE,LV,NL,SE,DK,GL,IS,FO,NO,AT,CH,LI,ES,PT,AD,DE,FR,IT,SM,AF,AM,AZ,GE,KZ,KG,MN,TJ,TM,UZ,BY,UA,AL,BA,MK,MT,MD,BG,HR,RO,SI,CS,CZ,HU,RU,SK,PL,RS,ME";
	} else if( dnsName.equalsIgnoreCase("partnerportal-ap") ){
		country = "CN,KR,JP,TW,HK,MO,ID,IN,MY,PH,SG,TH,VN,BD,BT,BN,KH,LA,MV,MM,NP,PK,LK,TL,AU,FJ,PG,WS,TO,VU";
	} else if( dnsName.equalsIgnoreCase("partnerportal-lar") ){
		country = "BR,MX,CL,CO,BZ,CR,DO,SV,GT,HN,NI,PA,AG,AW,BS,BB,BM,KY,GD,GY,HT,JM,AN,PR,LC,VC,KN,SR,TT,VG,DM,TC,BO,EC,PE,VE,AR,PY,UY";
	} else if( dnsName.equalsIgnoreCase("partner-portal") ){
		country = "US,CA";		
	}

	Date lastUpdate = new Date(0L);
	String formattedDate = "29/05/2006";
	try{
		lastUpdate = new Date(aNavigationItem.getLastUpdate());
    	Formatter formatter = new Formatter();
    	formattedDate = String.format("%1$td/%1$tm/%1$tY", lastUpdate);

	} catch(Exception e){
		//eating the exception and giving a default date
	}
			
%>		
<html lang="<%=language%>">
	<head>
		<meta HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8"/>		
		<meta NAME="cssearchkeyword" CONTENT="<%=searchKeywords%>"/>
		<meta NAME="title" CONTENT="<%= title %>"/>
		<meta NAME="csdescription" CONTENT="<%= description %>" />	
		<meta NAME="csisolanguage" CONTENT="<%=language%>"/>
		<meta NAME="csdocreleasedate" CONTENT="<%=formattedDate %>"/>
		<meta NAME="csimageurlhttp" CONTENT="<%= framesetUrl %>">			
		<meta NAME="csaudience" CONTENT="<%= userGroup_formatted_string%>"/>		
		<meta NAME="csidentification" CONTENT="<%= idString.toString() %>"/>
		<meta NAME="csfacetusage" CONTENT="<%= dnsName %>"/>
		<meta NAME="wildcatcountries" CONTENT="<%= country %>"/>
		<meta NAME="csdocumenttype" CONTENT="<%= aNavigationItem.getDocumentType() %>" />
		<meta NAME="cssearchcollection" CONTENT="spp" />
		<meta NAME="cspartner" CONTENT="AllPartners" />
		<meta NAME="csitemid" CONTENT="AllUsers" />
		<title><%= title %></title>
	</head>

	<body>
		<p><%= title %></p>
		<p><%= description %></p>		
	</body>
</html>
