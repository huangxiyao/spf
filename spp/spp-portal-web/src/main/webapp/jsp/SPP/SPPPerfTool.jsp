<%@page import="com.hp.spp.portal.common.perf.PerfToolLogManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.hp.spp.profile.Constants"%>
<%@page import="java.util.Map"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="com.hp.spp.webservice.ugs.manager.SPPUserGroupManager"%>
<%!
/** Usage
 * 1) Call the url http://host/portal/jsp/SPP/SPPPerfTool.jsp?testedTool=UGS
 * 2) Results are under /opt/spp/Vignette/Portal/logs/vignetteServer/SPP_PerfToolLog (FT) 
 *  and /var/spp/Vignette/Portal/logs/VignetteServer1/SPP_PerfToolLog 
 *   /var/spp/Vignette/Portal/logs/VignetteServer2/SPP_PerfToolLog on ITG 
 * Rmk : a fake profile is constructed. With addition of groups, some attributes will perhaps be missing
 **/
	private static final Logger mLog = Logger.getLogger("com.hp.spp.jsp.SPPPerfTool.jsp");

	// Creation of fake profile
  private static Map userProfile = new HashMap();
  static {
  userProfile.put("PartnerProIdHQ", "1-181RDO");
  userProfile.put("UserPreferedLanguageCode", "en");
  userProfile.put("PartnerHierarchyType", "Headquarters");
  userProfile.put("SiteName", "smartportal");
  userProfile.put("SiebelPreferredLanguage", "EN");
  userProfile.put("SecurityQuestion", "My first car");
  userProfile.put("PartnerProId", "1-181RDO");
  userProfile.put("DateOfBirth", "");
  userProfile.put("PhysCity", "Bangalore");
  userProfile.put("PrimaryContactId", "1-2A-2");
  userProfile.put("PartnerPhysicalAddressLine2", "bangalore");
  userProfile.put("PartnerPhysicalAddressLine3", "");
  userProfile.put("HPOrgs", "Default Organization");
  userProfile.put("PhysPostalCode", "55644");
  userProfile.put("PartnerPhysicalAddressCity", "Bangalore");
  userProfile.put("Contact_HPRole", "HP Champion");
  userProfile.put("ReceiveSMS", "F");
  userProfile.put("Status", "1");
  userProfile.put("SiteIdentifier", "smartportal");
  userProfile.put("HPRole", "Technical Solution Responsible");
  userProfile.put("PrimaryHPRoleId", "1-3LUT-1");
  userProfile.put("CountryCode", "DE");
  userProfile.put("Language", "en");
  userProfile.put("Programs", "EU-GPP-INT;EU-zz49-m213;EU-zz26-z107;EU-zz26-z108;87-E003-UPC;EU-01pp-O925;EU-01pp-P568");
  userProfile.put("IsPartnerAdmin", "T");
  userProfile.put("Tier", "");
  userProfile.put("Accreditations", "1-182YK3;1-182YK3;1-182YK3;EU-2004-L022");
  userProfile.put("PrimaryChannelSegment", "Distributor");
  userProfile.put("UserRights", "LOCAL_SECURITY_PartnerProfiler;LOCAL_SECURITY_Simulation");
  userProfile.put("ResponsibilityNames", "HP Partner Portal Admin - EMEA;HP Partner Portal LM User;HP Partner Portal User - EMEA");
  userProfile.put("TemplateNames", "Claus for testing WS;Claus for testing WS;Claus for testing WS;EU-SP Send In");
  userProfile.put("LastName", "Vidal");
  userProfile.put("FirstName", "Mat");
  userProfile.put("PartnerSubTypes", "Agent");
  userProfile.put("IsHybrid", "F");
  userProfile.put("LoginId", "MatV");
  }
%>
<%
try{
	// Retrieval of tested functionality
  String testedTool = request.getParameter("testedTool");

  if (testedTool.equalsIgnoreCase("UGS")){
 		mLog.info("Start UGS WS Call");
  		String siteName = (String)userProfile.get(Constants.MAP_SITE);
  		Map userProfileUGS = (new com.hp.spp.profile.ProfileHelper()).toLegacyProfile(userProfile);
  		
  		// Start Call
  		long startTime = System.currentTimeMillis();
  		String[] ugs_UserGroup = new SPPUserGroupManager().getGroupsFromUserGroupService(siteName,
					  (HashMap) userProfileUGS);
  		long endTime = System.currentTimeMillis() - startTime;
      PerfToolLogManager.traceUgsCall(endTime);
  		out.print("UGS call in ms : " + endTime +"<BR/>");
 		mLog.info("UGS call in ms : " + endTime);
  		if (ugs_UserGroup!=null){
  			for (int i=0;i<ugs_UserGroup.length;i++){
  				out.print(ugs_UserGroup[i]+";");
  			}
  		}
  }
} catch (Throwable t){
	mLog.error("Unexpected error occured : ", t);
}
%>

