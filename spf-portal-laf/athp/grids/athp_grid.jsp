
<%-----------------------------------------------------------------------------
	athp_grid.jsp
	
	This jsp contains all the common code for each of the @hp grids and is 
	included by each of the grid component jsp file, 
	e.g. athp_vert_nav_grid.jsp.
	
	The code will display HPWeb header, footer, horizontal nav (optional), 
	vertical navigation (optional), and the page content.
	
	- Horizontal navigation is displayed if the "showHorzNav" request scoped 
	attribute is set to "true" in the grid component jsp file.
	
	- Vertical navigation is displayed if the "showVertNav" request scoped 
	attribute is set to "true" in the grid component jsp file.

-----------------------------------------------------------------------------%>


<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page contentType="text/html;charset=UTF-8" />
<jsp:directive.page import="com.epicentric.template.Style" />
<jsp:directive.page import="com.epicentric.template.StyleUtils" />
<jsp:directive.page import="java.util.HashMap" />

<%---------------------------------------------------------- TAG LIBRARIES --%>


<%@ taglib prefix="vgn-portal" uri="vgn-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----------------------------------------------------------------- SCRIPT --%>

<vgn-portal:defineObjects/>

<%-- Conditional include the athp_model.jsp file customized by portal --%>

<vgn-portal:includeStyle friendlyID="hp_layout_config"/>

<jsp:scriptlet>

// Get path to hp_layout_config head JSP file to include later in head.

pageContext.setAttribute("layoutConfigHeadJspPath",
		"/" + portalContext.getCurrentStyle().getUrlSafeRelativePath() +
		"athp_layout_config_head.jsp");

</jsp:scriptlet>

<jsp:useBean id="AtHPModel" scope="request" 
        class="com.hp.frameworks.wpa.portal.athp.AtHPModel" />
        
<jsp:scriptlet>

	String headerJS;
	String homePageCSS;
  	if ( request != null && request.isSecure() ) {
  	    headerJS = "https://portal.hp.com/lib/navigation/header.js";
		homePageCSS = 
			"https://portal.hp.com/lib/navigation/css/homepages-v5-ssl.css";
  	}
	else {
  	    headerJS = "http://portal.hp.com/lib/navigation/header.js";
		homePageCSS = 
			"http://portal.hp.com/lib/navigation/css/homepages-v5.css";		
	}

</jsp:scriptlet>

<%----------------------------------------------------------------- MARKUP --%>


<html>
<head>
	<vgn-portal:pageContentTitle />

	<%-- Override default window title with one from athp model, if it exists.
		 Also, Vignette may surround <span> element around the title if
		 it is retrieved from properties, so need to remove them. 
	--%>

	<c:choose>
	<c:when test="${! empty AtHPModel.windowTitle}">
    	<script type="text/javascript">
        	document.title="${AtHPModel.windowTitle}";
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

<c:forEach var="metaInfo" items="${AtHPModel.metaInfos}" varStatus="status">	
	<meta name="<c:out value="${metaInfo.key}" />" content="<c:out value="${metaInfo.value}" />">
</c:forEach>
	
	<link rel="stylesheet" type="text/css" href="<%= homePageCSS %>">
	
	<script language="javascript" type="text/javascript" 
			src="<%= headerJS %>"></script>
	
	<%-- Include portal's JSP file which contains any js script, css, etc, 
			for inserting into the head element.  --%>

	<jsp:include page="${layoutConfigHeadJspPath}" />

</head>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" 
	bottommargin="0" marginwidth="0" marginheight="0">

<div id="header" style="clear:left;" >
	<vgn-portal:includeStyle friendlyID="header"/>
</div> 

<c:if test="${showHorzNav eq 'true'}">
<div id="horzNav">
	<vgn-portal:includeNavigation friendlyID="navigation-horizontal" />
</div>
</c:if>

<script type="text/javascript">
	drawHeader();
</script>

<div id="contentContainer">
<c:choose>
<c:when test="${showVertNav eq 'true'}">
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="140" valign="top">

			<div id="vertNav">
				<vgn-portal:includeNavigation friendlyID="navigation-vertical" 
						isSecondary="true" />
			</div>
		</td>

		<td id="bodytext" valign="top">

			<!--startindex-->
			<div id="content">
				<vgn-portal:includePageContent />
			</div>		
			<!--stopindex-->

		</td>
	
	</tr>
	</table>
</c:when>
<c:otherwise>
	<table width="100%" border="0" cellpadding="0" cellspacing="5">
	<tr>
		<td id="bodytext">
			<!--startindex-->
			<div id="content">
				<vgn-portal:includePageContent />
			</div>		
			<!--stopindex-->
		</td>
	</tr>
	</table>
</c:otherwise>
</c:choose>
</div>

<div id="footer">
	<vgn-portal:includeStyle friendlyID="footer" />
</div>

</body>
</html>
