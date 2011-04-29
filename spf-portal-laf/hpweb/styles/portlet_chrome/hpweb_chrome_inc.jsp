<%-----------------------------------------------------------------------------
	hpweb_chrome_inc.jsp
	
	This jsp contains all the common code for each of the HPWeb portlet chrome
	and is included by each of the chorme component jsp file, 
	e.g. hpweb_chrome_intro_box.jsp.

-----------------------------------------------------------------------------%>

<%----------------------------------------------------------------------------
	Imports
-----------------------------------------------------------------------------%>

<%@ page import="com.hp.frameworks.wpa.portal.hpweb.Utils" %>

<%----------------------------------------------------------------------------
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
					
<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<jsp:scriptlet>

String headerLevel = Utils.getI18nValue( i18nID, "hpweb.headerLevel", "2",
                        portalContext);
String boundingBox = Utils.getI18nValue( i18nID, "hpweb.boundingBox", "false",
                        portalContext);
String portletTitle = Utils.getI18nValue( portletWindow.getPortletUID(), 
						"title", portletWindow.getConfiguredTitle(), portalContext);

// Validate properties

if (boundingBox.equalsIgnoreCase("true"))
	boundingBox = "true";

int tmp = Integer.parseInt(headerLevel);
if (tmp < 2 || tmp > 6)
	headerLevel = "2";

pageContext.setAttribute("headerLevel", headerLevel);
pageContext.setAttribute("boundingBox", boundingBox);
pageContext.setAttribute("portletTitle", portletTitle);

</jsp:scriptlet>

<c:choose>
	<c:when test="${empty style}">
		<c:set var="style" value="intro" />
	</c:when>
	<c:otherwise>
		<c:set var="style" value="${fn:toLowerCase(style)}" />
	</c:otherwise>
</c:choose>

<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${style eq 'intro'}">
		<div id="introBlock">
			<h${headerLevel} class="headerAlpha">${portletTitle}</h${headerLevel}>
			<div class="content textLevelA">
	</c:when>
	<c:when test="${style eq 'callout'}">
		<%-- Use own calloutEnhancedExt style, not hp.com style because
				want callout that is the width of the page. --%>
		<div class="calloutEnhancedExt">
			<div class="contentBlock">
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${style eq 'levela' || style eq 'levelabig' }">
				<c:set var="headerClass" value="headerAlpha" />
				<c:set var="indicatorClass" value="" />	
			</c:when>
			<c:when test="${style eq 'levelb'}">
				<c:set var="headerClass" value="headerBeta" />
				<c:set var="indicatorClass" value="linkColor" />					
			</c:when>
			<c:when test="${style eq 'levelbbeveled'}">
				<c:set var="headerClass" value="headerBetaBevel" />
				<c:set var="indicatorClass" value="linkColor" />
			</c:when>			
			<c:when test="${style eq 'levelc' || fn:toLowerCase(style) eq 'levelcshaded'}">
				<c:set var="headerClass" value="headerGamma" />
				<c:set var="indicatorClass" value="linkColor" />
			</c:when>		
			<c:when test="${style eq 'leveld'}">
				<c:set var="headerClass" value="headerDelta" />
				<c:set var="indicatorClass" value="linkColor" />
			</c:when>	
			<c:otherwise>
				<c:set var="headerClass" value="headerEpsilon" />
				<c:set var="indicatorClass" value="linkColor" />
			</c:otherwise>		
		</c:choose>	
		
		<%-- If boundingBox is enabled, include bounding box styles,
			 applicable to level A, level B and beveled level B. --%>

		<c:choose>
			<c:when test="${boundingBox eq 'true' && 
				(headerClass eq 'headerAlpha' || 
			 	headerClass eq 'headerBeta' || 
			 	headerClass eq 'headerBetaBevel')}">
			 	
			 	<%-- validation check --%>

			</c:when>	
			<c:otherwise>
				<c:set var="boundingBox" value="false" />
			</c:otherwise>		
		</c:choose>	

		<%-- New div element id needed to set css styles above. --%>
		<div id="nonIntroBlock">
			<h${headerLevel} class="${headerClass}">${portletTitle}</h${headerLevel}>
			
			<%--
			<c:if test="${boundingBox eq 'true'}">
				<div id="${headerClass}BoundingBox">
			</c:if>
			<div class="contentBlock">
			--%>
			
			<%-- Override hp.com properties for contentBlock --%>
			<c:if test="${boundingBox eq 'true'}">
				<%-- New div element needed to make bounding box work. --%>
				<div id="${headerClass}BoundingBox">
					<div class="contentBlock" style="width:auto;">
			</c:if>

			<c:if test="${boundingBox ne 'true'}">
				<div class="contentBlock" style="width:auto;padding:10px;margin:0;">
			</c:if>
			
	</c:otherwise>
</c:choose>

		<vgn-portal:renderPortlet>
			<vgn-portal:onRenderSuccess>
				<vgn-portal:insertPortletContent />
			</vgn-portal:onRenderSuccess>
			<vgn-portal:onRenderFailure>
			
				<%@ include file="portlet_render_error_inc.jsp" %>
			
		  </vgn-portal:onRenderFailure>
		</vgn-portal:renderPortlet>	
		
			</div>
			<c:if test="${boundingBox eq 'true'}">
				</div>
			</c:if>
		</div>	

