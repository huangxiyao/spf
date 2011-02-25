
<%-----------------------------------------------------------------------------
	cleansheet_grid_inc.jsp
	
	This jsp contains all the common code for each of the cleansheet grids and is 
	included by each of the grid component jsp file, 
	e.g. cleansheet_vert_nav_grid.jsp.
	
	The code will display Cleansheet header, footer, horizontal nav (optional), 
	vertical navigation (optional), and the page content.
	
	- Horizontal navigation is displayed if the "showHorzNav" request scoped 
	attribute is set to "true" in the grid component jsp file.
	
	- Vertical navigation is displayed if the "showVertNav" request scoped 
	attribute is set to "true" in the grid component jsp file.
	
-----------------------------------------------------------------------------%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-----------------------------------------------------------------------------
	Directives
-----------------------------------------------------------------------------%>

<jsp:directive.page import="java.util.HashMap" />
<jsp:directive.page import="com.epicentric.template.Style" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.hpweb.Utils" />
<jsp:directive.page import="java.util.Locale" />

<%-----------------------------------------------------------------------------
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="vgn-portal" uri="vgn-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csfn" uri="http://frameworks.hp.com/wpa/cleansheet-fn" %>


<%----------------------------------------------------------------------------- 
	Locale initialization
-----------------------------------------------------------------------------%>

<vgn-portal:defineObjects/>

<jsp:scriptlet>

// Get current style, which is the grid, for use in generating the path.

Style currentStyle = portalContext.getCurrentStyle();
String i18nID = currentStyle.getUID();
	
pageContext.setAttribute("stylePath", portalContext.getPortalHttpRoot() + 
		currentStyle.getUrlSafeRelativePath());
	
pageContext.setAttribute("dnsName", portalContext.getCurrentSite().getDNSName());
	
</jsp:scriptlet>

<%-- Include the Layout Config style --%>

<vgn-portal:includeStyle friendlyID="hp_layout_config" />

<jsp:scriptlet>

// Get path to hp_layout_config head JSP file to include later in head.

pageContext.setAttribute("layoutConfigHeadJspPath",
		"/" + portalContext.getCurrentStyle().getUrlSafeRelativePath() +
		"cleansheet_layout_config_head.jsp");

</jsp:scriptlet>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />

<jsp:scriptlet>

// Get locale from logic to get it from what is set in Vignette.
// Use locale and ISO language tag for fmt tags, and <html lang=""> element.

Locale locale = Utils.getLocale(request);
String languageTag = Utils.localeToLanguageTag(locale);
String countryTag = locale.getCountry();
if (languageTag == null) {
    languageTag = "en-US";
    countryTag = "US";
}
pageContext.setAttribute("countryTag", countryTag);

</jsp:scriptlet>

<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>

<fmt:setLocale value="<%= locale %>" scope="request" />

<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />
<fmt:setBundle var="msgResources" basename="com.hp.frameworks.wpa.hpweb.Messages"  />

<fmt:message var="contentStart" key="text.hpweb2003.content" bundle="${msgResources}" />
<fmt:message var="htmlLang" key="text.hpweb2003.htmlLang" bundle="${msgResources}" />

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">
		<fmt:message var="javascriptDir" key="link.hpweb2003.secure_javascript_dir" bundle="${urlResources}" />
		<fmt:message var="styleDir" key="link.hpweb2003.secure_style_dir" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="javascriptDir" key="link.hpweb2003.javascript_dir" bundle="${urlResources}" />
		<fmt:message var="styleDir" key="link.hpweb2003.style_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>

<%-----------------------------------------------------------------------------
	Local variables
-----------------------------------------------------------------------------%>

<c:if test="${!csfn:isLocaleLTR(urlResources)}">	
	<%--  Variable isRTL is used by horz nav and portlet chrome.  --%>
	<c:set var="isRTL" value="true" scope="request" />
</c:if>

<jsp:scriptlet>

// Create HashMap to pass arguments to styles that need values sourced from
// grid components.

HashMap args = new HashMap();

</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Template
-----------------------------------------------------------------------------%>

<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="<%= languageTag %>" xml:lang="en-us">
<head>
	<vgn-portal:pageContentTitle />
	
	<%-- Override default window title with one from hpweb model, if it exists.
		 Also, Vignette may surround <span> element around the title if
		 it is retrieved from properties, so need to remove them. 
	--%>

	<c:choose>
	<c:when test="${! empty HPWebModel.windowTitle}">
    	<script type="text/javascript">
        	document.title="${HPWebModel.windowTitle}";
    	</script>
	</c:when>
	<c:otherwise>
    	<script type="text/javascript">
    		if (document.title.length > 13) {
        		document.title=document.title.replace(/<\/SPAN>/i, '');
        		document.title=document.title.replace(/<SPAN.*>/i, '');
			}
    	</script>
	</c:otherwise>
	</c:choose>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Content-Style-Type" content="text/css">
	<meta http-equiv="Cache-Control" content="no-cache">
	<c:if test="${fn:length(countryTag) != 0}">
		<meta name="target_country" content="${countryTag}">
	</c:if>
	<meta name="hp_design_version" content="hpweb.1.3">
	
<c:forEach var="metaInfo" items="${HPWebModel.metaInfos}" varStatus="status">	
	<meta name="${metaInfo.key}" content="${metaInfo.value}">
</c:forEach>

    <!--  link rel="stylesheet" type="text/css" href="${styleDir}hpweb_eeeep_ov2.css"-->
	<link href="http://www8.hp.com/us/en/images/i/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
	<link href="http://www8.hp.com/us/en/system/styles/header_footer_v2.css" media="screen" rel="stylesheet" type="text/css"/>
	<link href="http://www8.hp.com/us/en/system/styles/community_widget.css" media="screen" rel="stylesheet" type="text/css" />
	
	<!--[if (IE 7)|(IE 8)]>
         <link href="http://www8.hp.com/us/en/system/styles/header_footer_ie.css" rel="stylesheet" type="text/css" />
    <![endif]-->
    <!--[if IE 6]>
         <link href="http://www8.hp.com/us/en/system/styles/community_widget_ie6.css" media="screen" type="text/css" rel="stylesheet"/>
         <link href="http://www8.hp.com/us/en/system/styles/header_footer_ie6.css" rel="stylesheet" type="text/css" />
         <link href="http://www8.hp.com/us/en/system/styles/content_area_ie6_v2.css" media="screen" rel="stylesheet" type="text/css"/>
         <script src="http://www8.hp.com/us/en/scripts/homepage/png.js" type="text/javascript" charset="utf-8"></script>
         <script type="text/javascript">
              DD_belatedPNG.fix('.png');
         </script>
         <style type="text/css" media="screen">
              body {behavior: url("http://www8.hp.com/us/en/scripts/homepage/csshover3.htc"); }
         </style>
    <![endif]-->
	<script src="http://www8.hp.com/us/en/scripts/homepage/mootools_homepage.js" type="text/javascript">//</script>	
	
	<script type="text/javascript" language="JavaScript" src="${javascriptDir}hpweb_utilities.js"></script>

	<%-- Our static CSS style extensions to the HPWeb standard --%>
	<link href="<c:out value="${stylePath}" />cleansheet_extensions.css" rel="stylesheet" type="text/css">
	
	<%-- Our dynamic CSS style extensions to the HPWeb standard --%>
	<%@ include file="cleansheet_extensions_css.jsp" %>
	
	<%-- Our CSS portlet styles --%>
	<%@ include file="cleansheet_portlet_css.jsp" %>
	
	<%-- Include portal's JSP file which contains any js script, css, etc, 
			for inserting into the head element.  --%>

	<jsp:include page="${layoutConfigHeadJspPath}" />


</head>

<body>
		
	<c:if test="${isRTL}">
		<div dir="rtl">
	</c:if>

	<vgn-portal:includeStyle friendlyID="account_controls" args="<%= args %>"/>
	<div class="everything">
		 
		<vgn-portal:includeStyle friendlyID="header" args="<%= args %>"/>
		
		 
		<div style="height:50px; background-color:#2A2A2A" >
			<span style="position:relative;top:8px;left:30px; font-size:20pt; color:#C1C1C1">HP Support Center</span>
		</div>
		 
		<c:if test="${showHorzNav eq 'true'}">
		<vgn-portal:includeNavigation friendlyID="navigation-horizontal" />
		</c:if>
		<div style="padding: 0 12px 0 12px;background-color: #222;">
			<div class="body" id="body" style="background-color:#FFF">
					
				<a name="jumptocontent"><span class="screenReading">${contentStart}</span></a>									
				<vgn-portal:includePageContent />
			</div>
		</div>		
		<vgn-portal:includeStyle friendlyID="footer" args="<%= args %>" />

	</div>
	
	<c:if test="${isRTL}">
		</div>
	</c:if>
	
</body>
</html>