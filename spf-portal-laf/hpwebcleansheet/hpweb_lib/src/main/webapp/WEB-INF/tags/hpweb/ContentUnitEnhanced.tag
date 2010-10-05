<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="url" type="java.lang.String" %>
<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="headerLevel" type="java.lang.Integer" %>
<%@ attribute name="style" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-content-enhanced" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${empty style}">
		<c:set var="style" value="intro" />
	</c:when>
	<c:otherwise>
		<c:set var="style" value="${fn:toLowerCase(style)}" />
	</c:otherwise>
</c:choose>

<c:if test="${empty headerLevel || headerLevel < 2 || headerLevel > 6}">
	<c:set var="headerLevel" value="2" />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${style eq 'intro'}">
		<div id="introBlock" <c:if test="${width > 0}">style="width: ${width}px;"</c:if>>
			<h${headerLevel} class="headerAlpha">${title}</h${headerLevel}>
			<div class="content textLevelA">
				<jsp:doBody />
			</div>
		</div>	
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
		
		<div <c:if test="${width > 0}">style="width: ${width}px;"</c:if>>
			<h${headerLevel} class="${headerClass}"><c:if test="${!empty url}"><span class="${indicatorClass}">&raquo;</span>&nbsp;<a href="${url}"></c:if>${title}<c:if test="${!empty url}"></a></c:if></h${headerLevel}>
			<div class="contentBlock">
				<jsp:doBody />
			</div>
		</div>	
	</c:otherwise>
</c:choose>