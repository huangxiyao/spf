<%@page import="com.vignette.portal.website.enduser.PortalContext,
				com.epicentric.common.website.MenuItemUtils,
				com.epicentric.template.Branding,
				com.epicentric.template.BrandingUtils,
				com.hp.spp.portal.common.helper.LocalizationHelper"%>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<vgn-portal:defineObjects/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<html>
<head>

<%

String i18nID = new LocalizationHelper().getCommonI18nID(portalContext);

//Define the page background color...
Branding branding = BrandingUtils.getRequestBranding(portalContext);
String pageBackgroundColor = null ;

//Retrieve the background color value from the theme...
if(branding != null)
	pageBackgroundColor = branding.getColorValue("bgColor.page");

//if the color is not define...
if(pageBackgroundColor == null || "".equals(pageBackgroundColor ))
	// the default color is blank...
	pageBackgroundColor  = "#FFFFFF" ;

%>

<%-- #336633 --%>
<SCRIPT language=JavaScript type=text/javascript>
    theme = '#<%=MenuItemUtils.getSelectedMenuItemNode(portalContext).getColor()%>';
</SCRIPT>

<vgn-portal:i18nElement><vgn-portal:pageContentTitle /></vgn-portal:i18nElement>

<!-- Begin CSS Style -->
<vgn-portal:includeStyle friendlyID="SPP_cssStyle_TYPE"/>
<!-- End CSS Style -->


<!-- Begin CSS Style -->
<vgn-portal:includeStyle friendlyID="SPP_JSStyle_TYPE"/>
<!-- End CSS Style -->


</head>

<body bgcolor="<%=pageBackgroundColor%>">
<!-- Begin Content Area -->
<vgn-portal:includePageContent />
<!-- End Content Area -->

</body>
</html>