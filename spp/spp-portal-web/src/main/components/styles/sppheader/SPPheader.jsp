<%@ page import="com.epicentric.common.website.I18nUtils" %>
<%@ page import="com.epicentric.site.Site" %>
<%@ page import="com.epicentric.common.website.MenuItemUtils" %>
<%@ page import="com.epicentric.common.website.MenuItemNode" %>
<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator" %>
<%@ page import="com.hp.spp.common.util.Environment" %>
<%@ page import="com.hp.spp.profile.Constants" %>
<%@ page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@ page import="com.hp.spp.portal.common.helper.PortalURIHelper" %>
<%@ page import="com.hp.spp.portal.common.helper.ProfileHelper" %>
<%@ page import="com.hp.spp.hpp.supporttools.Decoder" %>
<%@ page import="com.hp.spp.portal.common.helper.MenuItemHelper" %>
<%@ page import="com.vignette.portal.website.enduser.PortalURI"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hp.spp.hpp.supporttools.HPPHeaderHelper" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.epicentric.common.website.I18nUtils" %>
<%@ page import="com.epicentric.user.User" %>
<%@ page import="com.epicentric.user.UserManager" %>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<vgn-portal:defineObjects/>
<%

//Get web server name from a header
String webServerName = request.getHeader("SPP-Web-Server");
if (webServerName == null) {
	webServerName = "UNKNOWN";
}
out.println("<!-- Web Server Name: "+webServerName +"-->");

//Used to display the Server Name
String serverName = Environment.singletonInstance.getManagedServerName();
out.println("<!-- Portal Server Name: "+serverName +"-->");

ProfileHelper profileHelper = new ProfileHelper();

String hppId = HPPHeaderHelper.getHPPId(request);

if(hppId == null || "".equals(hppId)){
	HashMap userProfile = (HashMap) session.getAttribute(Constants.PROFILE_MAP) ;
	if(userProfile != null){
		Iterator iterator = userProfile.keySet().iterator() ;
		while(iterator.hasNext()) {
			Object key = iterator.next() ;
			Object value = userProfile.get(key) ;
			out.println("<!-- key: "+key+", value: "+value+" -->") ;
		}
	}
}

MenuItemNode menuItemNode = MenuItemUtils.getSelectedMenuItemNode(portalContext);
if (menuItemNode != null) {
	out.println();
	out.println("<!-- Nav-Item-Name: "+menuItemNode.getMenuItem().getTitle()+"-->");
	out.println();
}


/******************************************************************************
try {
	
	HashMap userProfile = (HashMap) session.getAttribute(Constants.PROFILE_MAP) ;
	User user = portalContext.getCurrentUser() ;
	
	//Not in simulated mode
	if (!profileHelper.isSimulationMode(portalContext)) {
		if(userProfile != null) {
			Iterator iterator = userProfile.keySet().iterator() ;
			while(iterator.hasNext()) {
				Object key = iterator.next() ;
				Object value = userProfile.get(key) ;
				out.println("<!-- key: "+key+", value: "+value+" -->") ;
			}
		}
		
		if(user != null) {
			out.println("") ;
			out.println("<!-- key: username, value: "+user.getProperty("username")+" -->") ;
			out.println("<!-- key: locale, value: "+I18nUtils.getUserLocale(user)+" -->") ;
		}
	} 
	else {
		// In simulated mode
		if(userProfile != null) {
			// Get the current site because the information displayed in PRM Portals
			// is different from SPP QA Portal
			Site currentSite = portalContext.getCurrentSite();

			Iterator iterator = userProfile.keySet().iterator() ;
			while(iterator.hasNext()) {
				Object key = iterator.next() ;
				// If the key is SimulatingUser then display First Name, Last Name
				// also display Company Name, Company Number if the site is not sppqa
				if (key.equals(Constants.MAP_SIMULATOR)){
					HashMap simulatedUserMap = (HashMap)userProfile.get(Constants.MAP_SIMULATOR);
					if(currentSite.getDNSName().equals(Constants.SPP_QA_SITE)) {
						out.print("<!-- SIMULATOR: "+simulatedUserMap.get(Constants.MAP_FIRSTNAME)+ " ");
						out.print(simulatedUserMap.get(Constants.MAP_LASTNAME)+" -->");
						out.println("");
					}
					else {
						out.print("<!-- SIMULATOR: Role: "+simulatedUserMap.get(Constants.MAP_HP_ROLE));
						out.print(", Language: "+simulatedUserMap.get(Constants.MAP_LANGUAGE));
						out.print(", FirstName: "+simulatedUserMap.get(Constants.MAP_FIRSTNAME)+ " ");
						out.print(", LastName: "+simulatedUserMap.get(Constants.MAP_LASTNAME));
						out.print(", Company Name: "+simulatedUserMap.get(Constants.MAP_COMPANY_NAME));
						out.print(", Company Number: "+simulatedUserMap.get(Constants.MAP_COMPANY_NUMBER)+" -->"); 
						out.println("");
					}
				}
				else {
					Object value = userProfile.get(key) ;
					out.println("<!-- key: "+key+", value: "+value+" -->") ;
				}
			}
		}
		
		if(user != null) {
			out.println("") ;
			out.println("<!-- key: username, value: "+user.getProperty("username")+" -->") ;
			out.println("<!-- key: locale, value: "+I18nUtils.getUserLocale(user)+" -->") ;
		}
	}
} catch(Exception e) {
	
}
******************************************************************************/

String hpImageSpacerURL = HPUrlLocator.getWelcomeUrl(portalContext,false) + "/";

LocalizationHelper localizationHelper = new LocalizationHelper() ;
String i18nID = localizationHelper.getCommonI18nID(portalContext);

String securityMessage = I18nUtils.getValue("e52f774b23843eeee13c7ef0b528efa0", "securitymessage", "", session, request);
securityMessage = localizationHelper.getValueNoSpan(securityMessage) ;

if(securityMessage == null || securityMessage.equalsIgnoreCase(""))	{
    %><!--security message not ok--><%
    securityMessage = "" ;
}

%>
<script type="text/javascript" language="javascript">
	var securityMessage = htmlFormat("<%=securityMessage%>");
</script>
<%

PortalURIHelper portalURIHelper = new PortalURIHelper();
MenuItemHelper menuItemHelper = new MenuItemHelper();

//	Find out if the user session is in simulation mode.
boolean sessionInSimulation = new ProfileHelper().isSimulationMode(session);

// if the user name is in the profile then we are authenticated...
// ... table with welcome message, sign out, update profile, country/language
if (profileHelper.isUserAuthenticated(portalContext))	{
	
	String editProfileURI = null ;
	MenuItemNode editProfileNode = menuItemHelper.findMenuItemByName(portalContext, "SPP_EditProfile") ;
	if(editProfileNode != null) {
		editProfileURI = menuItemHelper.getNodeHref(portalContext, editProfileNode) ;
		if(menuItemHelper.isEService(editProfileNode)) {
			editProfileURI = "#\" onclick=\"return prepareLink(this, '"+editProfileURI+"','"+editProfileNode.getMenuItem().getTitle()+"',securityMessage,"+sessionInSimulation+");";
		}
	}
//	editProfileURI = portalURIHelper.getFullPortalURL(portalContext, "SPP_EditProfile") ;
	
	// Retrieve the language name of the end-user
	String langCode = profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE);
	if(langCode == null || "".equals(langCode))
		langCode = "en" ;
	else
		langCode = langCode.toLowerCase() ;
	String language = I18nUtils.getValue(i18nID, "language_".concat(langCode), "English", request) ;
	
	// Retrieve the country name of the end-user
	String countryCode = profileHelper.getProfileValue(portalContext, Constants.MAP_COUNTRY) ;
	if(countryCode == null || "".equals(countryCode))
		countryCode = "us";
	else
		countryCode = countryCode.toLowerCase() ;
	
	String country = I18nUtils.getValue(i18nID, "country_".concat(countryCode), "United States", request) ;
	
	if("GB".equalsIgnoreCase(countryCode))
		countryCode = "uk";

%>
<table border="0" cellpadding="0" cellspacing="0" width="740">
	<tr class="decoration">
		<td width="10">
			<img src="<%=hpImageSpacerURL%>img/s.gif" width="10" height="1" alt="" border="0" />
			<noscript>
				<a href="http://welcome.hp.com/country/us/en/noscript.html"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="javascript_error" defaultValue="summary of site-wide JavaScript functionality" /></a>
			</noscript>
		</td>		
		<td align="left" width="260">
				<span style="font-size: 11px;"><%
				Site currentSite = portalContext.getCurrentSite();
				if (!profileHelper.isSimulationMode(portalContext)) {
					if (menuItemHelper.isHomePage(portalContext))	{
				%><vgn-portal:i18nValue stringID="<%=i18nID%>" key="welcome" defaultValue="Welcome" />&nbsp;<%
					}
				%><% if ( langCode.equals("ko") || langCode.equals("ja") || langCode.equals("zh_tw") || langCode.equals("zh_cn") ) { 
					%><%= profileHelper.getProfileValue(portalContext, Constants.MAP_LASTNAME) %>&nbsp;<%= profileHelper.getProfileValue(portalContext, Constants.MAP_FIRSTNAME) %><% if ( langCode.equals("ko") || langCode.equals("ja") ) {
						%>&nbsp;<vgn-portal:i18nValue stringID="<%=i18nID%>" key="welcome_san" defaultValue="" /><%
					}
					
					%><% 
					} else { 
						%><%= profileHelper.getProfileValue(portalContext, Constants.MAP_FIRSTNAME) %>&nbsp;<%= profileHelper.getProfileValue(portalContext, Constants.MAP_LASTNAME) %><%
					}
					
				} 
				else {
					
					String role = null;
					if ("T".equals(profileHelper.getProfileValue(portalContext, "IsPartnerAdmin")))	{
						role = "DPA";
					}
					else	{
						role = "User";
					}
					String simulatedLanguage = profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE);
					
					// PUT SIMULATING INDICATION 
					out.print("<!-- SIMULATING: ");
					if(!currentSite.getDNSName().equals(Constants.SPP_QA_SITE)) {
						out.print(role+" - ");
						out.print(simulatedLanguage+" ");
					}
					out.print(profileHelper.getProfileValue(portalContext, Constants.MAP_FIRSTNAME)+" ");
					out.print(profileHelper.getProfileValue(portalContext, Constants.MAP_LASTNAME)+" ");
					if(!currentSite.getDNSName().equals(Constants.SPP_QA_SITE)) {
						out.print("("+profileHelper.getProfileValue(portalContext, "PartnerLegalName")+" - ");
						out.print(profileHelper.getProfileValue(portalContext, Constants.MAP_COMPANY_NUMBER)+")");
					}
					out.print("-->");
		        %>
					<a href="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_HOMEPAGE)%>template.STOPSIMULATION"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="stopSimulating" defaultValue="Stop simulating" /></a>
					<br>
					<vgn-portal:i18nValue stringID="<%=i18nID%>" key="simulating" defaultValue="Simulating"/>&nbsp;<%= role %>&nbsp;<%= simulatedLanguage %>
					<%					

				}
				String companyName = profileHelper.getProfileValue(portalContext, "PartnerLegalName");
				String companyNumber = profileHelper.getProfileValue(portalContext, Constants.MAP_COMPANY_NUMBER);

				if(!"".equals(companyName) && !"".equals(companyNumber)) {
					 %> &nbsp;(<%= companyName %>&nbsp;-&nbsp;<%= companyNumber %>)<%
				}
				%>
		</td>
		<td>
			<img src="<%=hpImageSpacerURL%>img/s.gif" width="10" height="24" alt="" border="0" />
		</td>
		<td align="left" width="195" class="color003366">
			&raquo;&nbsp;
			<a href="javascript:logout();" class="small">
				<vgn-portal:i18nValue stringID="<%=i18nID%>" key="sign_out" defaultValue="Sign-out" />
			</a><% if(editProfileURI != null) { %>
			&nbsp;<span class="color666666">|</span>&nbsp;&raquo;&nbsp;
			<a href="<%=editProfileURI%>" class="small">
				<vgn-portal:i18nValue stringID="<%=i18nID%>" key="edit_profile" defaultValue="Edit your profile" />
			</a><% } %>
		</td>
		<td align="right" width="245" class="countryInd">
			<%=country%>-<%=language%>
		</td>
		<td>
			<img src="<%=hpImageSpacerURL%>img/s.gif" width="20" height="1" alt="" border="0" />
		</td>
	</tr>

</table>

<%
}
// not authenticated... table with register link , country-language indicator
else	{

	// Retrieve the language name of the end-user
	String langCode = profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE);
	if(langCode == null || "".equals(langCode))
		langCode = "en" ;
	else
		langCode = langCode.toLowerCase() ;
	String language = I18nUtils.getValue(i18nID, "language_".concat(langCode), "English", request) ;
	
	// Retrieve the country name of the end-user
	String countryCode = profileHelper.getProfileValue(portalContext, Constants.MAP_COUNTRY) ;
	if(countryCode == null || "".equals(countryCode))
		countryCode = "us";
	else
		countryCode = countryCode.toLowerCase() ;
	
	String country = I18nUtils.getValue(i18nID, "country_".concat(countryCode), "United States", request) ;
	
	String registerURI = null ;
	MenuItemNode registerNode = menuItemHelper.findMenuItemByName(portalContext, "SPP_Register") ;
	if(registerNode != null) {
		registerURI = menuItemHelper.getNodeHref(portalContext, registerNode) ;
		if(menuItemHelper.isEService(registerNode)) {
			registerURI = "#\" onclick=\"return prepareLink(this, '"+registerURI+"','"+registerNode.getMenuItem().getTitle()+"',securityMessage,"+sessionInSimulation+");";
		}
	}
//	String registerURI = portalURIHelper.getFullPortalURL(portalContext, "SPP_Register") ;
%>
<table border="0" cellpadding="0" cellspacing="0" width="740">
	<tr class="decoration">
		<td width="10">
			<img src="<%=hpImageSpacerURL%>img/s.gif" width="10" height="1" alt="" border="0" />
			<noscript>
				<a href="http://welcome.hp.com/country/us/en/noscript.html"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="javascript_error" defaultValue="summary of site-wide JavaScript functionality" /></a>
			</noscript>
		</td>		
		<td align="left" width="260">
			<nobr>
				<span style="font-size: 11px;">
				</span>
			</nobr>
		</td>
		<td>
			<img src="<%=hpImageSpacerURL%>img/s.gif" width="10" height="24" alt="" border="0" />
		</td>
		<td align="left" width="195" class="color003366"><% if(registerURI != null) { %>
			&raquo;&nbsp;
			<a href="<%=registerURI%>" class="small">
				<vgn-portal:i18nValue stringID="<%=i18nID%>" key="register" defaultValue="Register" />
			</a><% } else { %>&nbsp;<% } %>
		</td>
		<td align="right" width="245" class="countryInd">
			&nbsp;
		</td>
		<td align="right" width="245" class="countryInd">
			<%=country%>-<%=language%>
		</td>
		<td>
			<img src="<%=hpImageSpacerURL%>img/s.gif" width="20" height="1" alt="" border="0" />
		</td>
	</tr>

</table>

<%	
}
%>
