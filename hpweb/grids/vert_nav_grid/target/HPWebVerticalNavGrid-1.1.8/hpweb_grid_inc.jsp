
<%-----------------------------------------------------------------------------
	hpweb_grid_inc.jsp
	
	This jsp contains all the common code for each of the HPWeb grids and is 
	included by each of the grid component jsp file, 
	e.g. hpweb_vert_nav_grid.jsp.
	
	The code will display HPWeb header, footer, horizontal nav (optional), 
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
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


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
		"hpweb_layout_config_head.jsp");

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

<c:if test="${!hpwebfn:isLocaleLTR(urlResources)}">	
	<%--  Variable isRTL is used by horz nav and portlet chrome.  --%>
	<c:set var="isRTL" value="true" scope="request" />
</c:if>

<%-- 
	Grab layout settings from HPWebModel bean, and if a setting value is not 
	found there, then look in the grid message catalog. 	
--%>
	
<jsp:scriptlet>

String themeDef = Utils.getI18nValue(i18nID, "hpweb.theme", "#003366", 
			portalContext);
String headerStyleDef = Utils.getI18nValue(i18nID, "hpweb.headerStyle", "stretch",
			portalContext);
String pageWidthDef = Utils.getI18nValue(i18nID, "hpweb.pageWidth", "wide", 
			portalContext);

</jsp:scriptlet>
		
<c:set var="themeColor" value="${HPWebModel.themeColor}" />
<c:if test="${empty themeColor}">
	<c:set var="themeColor" value="<%= themeDef %>" />
</c:if>

<c:set var="headerStyle" value="${HPWebModel.headerStyle}" />
<c:if test="${empty headerStyle}">
	<c:set var="headerStyle" value="<%= headerStyleDef %>" />
</c:if>

<c:set var="pageWidth" value="${HPWebModel.pageWidth}" />
<c:if test="${empty pageWidth}">
	<c:set var="pageWidth" value="<%= pageWidthDef %>" />
</c:if>

<c:if test="${headerStyle eq 'stretch'}">
	<c:set var="stretch" value="true" />
</c:if>

<c:set var="widePage" value="true" />

<c:if test="${pageWidth eq 'narrow'}">
	<c:set var="widePage" value="false" />
</c:if>

<jsp:scriptlet>

// Create HashMap to pass arguments to styles that need values sourced from
// grid components.

HashMap args = new HashMap();
args.put("widePage", pageContext.getAttribute("widePage"));
args.put("themeColor", pageContext.getAttribute("themeColor"));

</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Template
-----------------------------------------------------------------------------%>

<html lang="<%= languageTag %>">
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
	<meta name="hp_design_version" content="hpweb.1.2j">
	
<c:forEach var="metaInfo" items="${HPWebModel.metaInfos}" varStatus="status">	
	<meta name="${metaInfo.key}" content="${metaInfo.value}">
</c:forEach>

	<c:if test="${!empty themeColor}">
       	<script type="text/javascript"> 
       		var theme = '${themeColor}'; 
       	</script>
    </c:if>
    
    <!-- CSS positioning code -->
	<link rel="stylesheet" type="text/css" href="${styleDir}hpweb_eeeep_ov2.css">
	
    <script type="text/javascript" language="JavaScript" src="${javascriptDir}hpweb_utilities.js"></script>
        
	<!-- CSS Print file -->
	<link rel="stylesheet" type="text/css" media="print" href="${styleDir}hpweb_print.css">
	
	<%-- Our static CSS style extensions to the HPWeb standard --%>
	<link href="<c:out value="${stylePath}" />hpweb_extensions.css" rel="stylesheet" type="text/css">
	
	<%-- Our dynamic CSS style extensions to the HPWeb standard --%>
	<%@ include file="hpweb_extensions_css.jsp" %>
	
	<%-- Our CSS portlet styles --%>
	<%@ include file="hpweb_portlet_css.jsp" %>
	
	<%-- Include portal's JSP file which contains any js script, css, etc, 
			for inserting into the head element.  --%>

	<jsp:include page="${layoutConfigHeadJspPath}" />

	<c:if test="${stretch}">
		<script type="text/javascript">
			var sval = "${searchLabel}";
			function clearSearch(el) { if(el.value==sval) el.value=''; }
			function restoreSearch(el) { if(el.value=='') el.value=sval; }
		</script>
	</c:if>    

</head>

<body class="pageLayout<c:if test="${widePage}">Wide</c:if>" text="#000000" link="#003366" alink="#003366" vlink="#660066">
		
	<c:if test="${isRTL}">
		<div dir="rtl">
	</c:if>

	<!--stopindex-->

	<vgn-portal:includeStyle friendlyID="account_controls" args="<%= args %>"/>
	
	<vgn-portal:includeStyle friendlyID="header" args="<%= args %>"/>
	
	<c:if test="${showHorzNav eq 'true'}">
		<vgn-portal:includeNavigation friendlyID="navigation-horizontal" />
	</c:if>
	
	<%-- Invoke header style again if this is a hpweb header which displays
			itself in two parts.   This is done to allow the horizontal navigation
			to display between the upper portion (hp-wide global buttons)
			and the lower portion (title and breadcrumbs).
			'hpweb_header_split' is a request attribute set by the hpweb header
			component.
	 --%>
	
	<c:if test="${hpweb_header_split eq 'true'}">
		<vgn-portal:includeStyle friendlyID="header" args="<%= args %>"/>
	</c:if>
	
	<div id="enhancedPageBody">

		<c:choose>
			<c:when test="${showVertNav eq 'true'}">
				<vgn-portal:includeNavigation friendlyID="navigation-vertical" 
							isSecondary="true" />
			</c:when>
			<c:otherwise>
				<%-- Set the wide page width style value because hp.com assumes
						that the vertical menu will be present and sets it to a shorter
						width. --%>
						
				<style type="text/css">
					.pageLayoutWide #enhancedContentArea {
						width: 974px;
					}
				</style>
			</c:otherwise>
		</c:choose>
		
		<!--startindex-->
				
		<div id="enhancedContentArea">		
			<a name="jumptocontent"><span class="screenReading">${contentStart}</span></a>									
			<vgn-portal:includePageContent />
		</div>
				
		<!--stopindex-->
		
	</div>

	<vgn-portal:includeStyle friendlyID="footer" args="<%= args %>" />

	<c:if test="${isRTL}">
		</div>
	</c:if>

	</body>
</html>