<%@taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper" %>
<%@page import="com.epicentric.common.website.*,com.epicentric.page.*"%>

<vgn-portal:defineObjects/>

<%
ProfileHelper profileHelper = new ProfileHelper();
boolean isInternalUser=false;



if (!profileHelper.isSimulationMode(session)){
  try {
		String description        	= "";	
		String strISOCountryCode 	= (profileHelper.getOmnitureProfileValue(portalContext,"Country")).toUpperCase();
		String languageCode 		= profileHelper.getOmnitureProfileValue(portalContext,"Language");
		String strISOLanguageCode	= languageCode.substring(0, 2).toUpperCase();
		String partnerId 	  	= profileHelper.getOmnitureProfileValue(portalContext,"PartnerProId");
		String LoginId 		  	= profileHelper.getOmnitureProfileValue(portalContext,"LoginId");
		String partnerName 	  	= profileHelper.getOmnitureProfileValue(portalContext,"PartnerName");
		//String contact_HPRole 	  	= profileHelper.getOmnitureProfileValue(portalContext,"Contact_HPRole");
		String seniority 	  	= profileHelper.getOmnitureProfileValue(portalContext,"Seniority");
		String department 	  	= profileHelper.getOmnitureProfileValue(portalContext,"Department");
		//String jobFunctions 	  	= profileHelper.getOmnitureProfileValue(portalContext,"JobFunctions");
		String primaryChannelSegment 	= profileHelper.getOmnitureProfileValue(portalContext,"PrimaryChannelSegment");
		String tier 			= profileHelper.getOmnitureProfileValue(portalContext,"Tier");
		String hpOrg 			= profileHelper.getOmnitureProfileValue(portalContext,"HPOrg");
		String hierarchyType 		= profileHelper.getOmnitureProfileValue(portalContext,"HierarchyType");
		String programs 		= profileHelper.getOmnitureProfileValue(portalContext,"Programs");
		String partnerSubTypes 		= profileHelper.getOmnitureProfileValue(portalContext,"PartnerSubTypes");
		String IsPartnerAdmin 		= profileHelper.getOmnitureProfileValue(portalContext,"IsPartnerAdmin");
		String job_Functions 		= profileHelper.getOmnitureProfileValue(portalContext,"Job_Functions");
		String partnerNameHQ 		= profileHelper.getOmnitureProfileValue(portalContext,"PartnerNameHQ");
		String partnerNameHQEMEA 	= profileHelper.getOmnitureProfileValue(portalContext,"PartnerNameHQEMEA");
		String siteIdentifier	 	= profileHelper.getOmnitureProfileValue(portalContext,"SiteIdentifier");
		String hpInternalUser	 	= profileHelper.getOmnitureProfileValue(portalContext,"HPInternalUser");
		String doubleQuote 		="\"";
		String EU_Preferred 		= "EU-01pp-O142";
        	String EU_Registered		= "EU-01pp-O233";
        	String EU_CAN_PREF		= "EU-01pp-O141";
        	String EU_CAN_Registered	= "EU-01pp-O604";
        	String programsFlag		= "N/A";
        	
		//After login Country replaces with partner Country.
		if(!partnerId.equals("N/A")){
			strISOCountryCode 	= (profileHelper.getOmnitureProfileValue(portalContext,"PhysCountry")).toUpperCase();
		}
        	// For Programs
        	if(programs.lastIndexOf(";"+EU_Preferred+";")!=-1){           
            		programsFlag ="P";
            	}else if(programs.lastIndexOf(";"+EU_Registered+";")!=-1){
                	programsFlag ="R";
            	}else if(programs.lastIndexOf(";"+EU_CAN_PREF+";")!=-1){
                	programsFlag ="CP";
            	}else if(programs.lastIndexOf(";"+EU_CAN_Registered+";")!=-1){
                	programsFlag ="CR";
            	}else {
                	programsFlag ="N/A";
            	}	
            	
		if((partnerName.indexOf(doubleQuote))>-1){
		        partnerName= partnerName.replaceAll(doubleQuote,"\\\\\"");			        
    		}
    		if((partnerNameHQ.indexOf(doubleQuote))>-1){
			partnerNameHQ= partnerNameHQ.replaceAll(doubleQuote,"\\\\\"");			        
    		}
		
		if(hpInternalUser.equals("T")){
			isInternalUser=true;
		}
		
		String styleFriendID = portalContext.getCurrentSecondaryPage().getFriendlyID();
		if (!styleFriendID.equals("PAGE") && !styleFriendID.equals("JSP_INCLUDE_PAGE") && !styleFriendID.equals("spp_pagedisplay"))
		{
			String i18nID2ndPage = portalContext.getCurrentSecondaryPage().getUID();
			description = I18nUtils.getValue(i18nID2ndPage, "title_omniture", "Default Description", request);
		}
		else if (request.getAttribute("isEservicePage")!=null && (request.getAttribute("isEservicePage")).toString().equalsIgnoreCase("true"))
    		{
     		 	// we are in a proxy page so description = eservice name
      			description = (String)request.getAttribute("eServiceName");

    		}
		else
		{
			MenuItemNode node = MenuItemUtils.getSelectedMenuItemNode(portalContext);
			if (null!=node.getMenuItem().getDescription())
			{
				description = node.getMenuItem().getDescription();
			}
			else
			{
				description = "N/A";
			}	
		}
  %>
  
  <script type="text/javascript" language="JavaScript">  
  
  var s_account = "hphqemeasesame"; 
  
  		
	<%if (profileHelper.getProfile(session)!=null && partnerId!=null){%>
		
		var s_hp_Optout = <%=isInternalUser%>;		
		var s_prop1 = "<%=partnerId%>|<%=partnerName%>";
		var s_prop2 = "<%=IsPartnerAdmin%>|<%=seniority%>|<%=department%>|<%=job_Functions%>";
		var s_prop3 = "<%=primaryChannelSegment%>";
		var s_prop4 = "<%=tier%>";
		var s_prop5 = "<%=hpOrg%>";		
		var s_prop6 = "<%=hierarchyType%>";		
		var s_prop10 = "<%=programsFlag%>";		
		var s_prop14 = "<%=partnerSubTypes%>";
		var s_prop15 = "<%=IsPartnerAdmin%>|<%=seniority%>|<%=department%>|<%=job_Functions%>|<%=description%>";
		var s_prop16 = "<%=LoginId%>|<%=partnerId%>|<%=partnerName%>|<%=partnerSubTypes%>|<%=hpOrg%>|<%=strISOCountryCode%>|<%=programsFlag%>|<%=description%>";
		var s_prop17 = "<%=partnerId%>|<%=partnerName%>|<%=description%>";
		var s_prop18 = "<%=partnerId%>|<%=partnerName%>|<%=partnerNameHQ%>|<%=partnerNameHQEMEA%>|<%=description%>";
		var s_prop19 = "<%=programsFlag%>|<%=description%>";
		var s_prop20 = "<%=partnerSubTypes%>|<%=description%>";
		var s_prop21 = "<%=partnerId%>|<%=partnerName%>|<%=strISOCountryCode%>";
		var s_prop22 = "<%=partnerId%>|<%=partnerName%>|<%=strISOCountryCode%>|<%=partnerSubTypes%>";
		var s_prop23 = "<%=partnerId%>|<%=partnerName%>|<%=partnerNameHQ%>|<%=partnerSubTypes%>";
		var s_prop24 = "<%=partnerId%>|<%=partnerName%>|<%=partnerNameHQ%>|<%=partnerSubTypes%>|<%=hpOrg%>|<%=strISOCountryCode%>";
		var s_prop25 = "<%=partnerId%>|<%=partnerName%>|<%=partnerNameHQ%>|<%=partnerSubTypes%>|<%=hpOrg%>|<%=strISOCountryCode%>|<%=programsFlag%>";		
		var s_prop12 = "<%=siteIdentifier%>";
		var s_prop11 = "<%=hpInternalUser%>";
		
	<%}%>
		
	var s_prop7 = "<%=strISOCountryCode%>";
	var s_prop8 = "<%=strISOLanguageCode%>";
	var s_prop9 = "partners";
	var s_pageName = "sesame|<%=strISOCountryCode%>|<%=strISOLanguageCode%>|<%=description%>";
  
   	
  </script>
  
  <%} catch (Exception e) {	
  	out.print("Exception in SSP Omniture Header " + e);	
  }
}%>
