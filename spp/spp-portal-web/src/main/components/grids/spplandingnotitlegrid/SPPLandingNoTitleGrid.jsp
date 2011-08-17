<%@page import="com.vignette.portal.website.enduser.PortalContext,
				com.epicentric.common.website.MenuItemUtils,
				com.epicentric.template.Branding,
				com.epicentric.template.BrandingUtils,
				com.hp.spp.portal.common.helper.LocalizationHelper"%>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<vgn-portal:defineObjects/>

<%

String i18nID = new LocalizationHelper().getCommonI18nID(portalContext);

//Define the page background color...
Branding branding = BrandingUtils.getRequestBranding(portalContext);
String pageBackgroundColor = null ;

//Retrieve the background color value from the theme...
if(branding != null)
	pageBackgroundColor = branding.getColorValue("bgColor.page");

//if the color is not define...
if(pageBackgroundColor == null || "".equals(pageBackgroundColor))
	// the default color is blank...
	pageBackgroundColor  = "#FFFFFF" ;

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<html>
<head>

<!-- Begin Metatag-->
<vgn-portal:includeStyle friendlyID="SPP_Metatag_Style"/>
<!-- End Metatag -->

<!-- Begin Set Guest User Locale Style -->
<vgn-portal:includeStyle friendlyID="SPP_Locale_Type"/>
<!-- End Set Guest User Locale Style -->

<%-- #336633 --%>
<SCRIPT language=JavaScript type=text/javascript>
    theme = '#<%=MenuItemUtils.getSelectedMenuItemNode(portalContext).getColor()%>';
</SCRIPT>

<!-- Begin CSS Style -->
<vgn-portal:includeStyle friendlyID="SPP_cssStyle_TYPE"/>
<!-- End CSS Style -->


<!-- Begin CSS Style -->
<vgn-portal:includeStyle friendlyID="SPP_JSStyle_TYPE"/>
<!-- End CSS Style -->


<!-- Begin Omniture Header -->
<vgn-portal:includeStyle friendlyID="SPP_OmnitureHeader_Type"/>
<!-- End Omniture Header -->

</head>

<body bgcolor="<%=pageBackgroundColor%>" text="#000000" link="#003366" alink="#003366" vlink="#336699" marginheight="0" marginwidth="0" leftmargin="0" topmargin="0">

<% if (request.getParameter("printable") == null) {%>

	<!-- Begin Header Area -->
	<vgn-portal:includeStyle friendlyID="SPP_Header_Type"/>
	<!-- End Header Area -->
	
	<!-- Begin Top Navigation -->
	<vgn-portal:includeStyle friendlyID="SPP_TopNavigation_Type"/>
	<!-- End Top Navigation -->
	
	<!-- Begin Search Area -->
	<vgn-portal:includeStyle friendlyID="SPP_Search_Type"/>
	<!-- End Search Area -->
	
	
<table width="740" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="170" valign="top" style="background-color:#F0F0F0;">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="170" valign="top" style="background-color:#FFFFFF;">
						<!-- Begin Welcome message Phil -->
						<vgn-portal:includeStyle friendlyID="sppwelcomenotitle"/>
						<!-- End Welcome message -->
					</td>
				</tr>
			    <tr>
					<!-- Begin Left Navigation -->
					<td width="170" valign="top" style="background-color:#F0F0F0;">
						<vgn-portal:includeStyle friendlyID="SPP_Login_Type"/>
					</td>
					<!-- End Left Navigation -->
				</tr>
			</table>
		</td>		
		<td style="padding-left:10px;" width="0"></td>
		<!-- Begin Content Area -->
		<td width="560" bgcolor="#FFFFFF" valign="top" colspan="4">
			<vgn-portal:includePageContent />
		</td>
		<!-- End Content Area -->
	
	</tr>
</table>

<table width="740" border="0" cellspacing="0" cellpadding="0">
	    <tr height="100">
	
		
		<td width="170" valign="top" style="background-color:#F0F0F0;">
		&nbsp;
		</td>
		
		
		<td style="padding-left:0px;" width="10"></td>
		<td width="560" bgcolor="#FFFFFF" valign="top" colspan="4">
		&nbsp;
		</td>
		</tr>
	</table>

<!-- Begin Printable Area -->
<vgn-portal:includeStyle friendlyID="SPP_Printable_Type"/>
<!-- End Printable Area -->

<%} else {%>

<!-- Begin Welcome message Phil -->
<vgn-portal:includeStyle friendlyID="sppwelcomenotitle"/>
<!-- End Welcome message -->

<table width="740" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="170" valign="top" style="background-color:#FFFFFF;">
		</td>		
		<td style="padding-left:10px;" width="0"></td>
		<!-- Begin Content Area -->
		<td width="560" bgcolor="#FFFFFF" valign="top" colspan="4">
			<vgn-portal:includePageContent />
		</td>
		<!-- End Content Area -->
	</tr>
</table>
	
<%}%>

<!-- Begin Footer Area -->
<vgn-portal:includeStyle friendlyID="SPP_Footer_Type"/>
<!-- End Footer Area -->

<!-- Begin Omniture Footer -->
<vgn-portal:includeStyle friendlyID="SPP_OmnitureFooter_Type"/>
<!-- End Omniture Footer -->

</body>
</html>
