<%@ tag pageEncoding="UTF-8" body-content="empty" %>

<%@ attribute name="exploreUrl" type="java.lang.String" %>
<%@ attribute name="communitiesUrl" type="java.lang.String" %>
<%@ attribute name="searchWidget" type="java.lang.String" %>
<%@ attribute name="searchUrl" type="java.lang.String" %>
<%@ attribute name="searchSectionName" type="java.lang.String" %>
<%@ attribute name="searchQueryPrefix" type="java.lang.String" %>
<%@ attribute name="searchReturnText" type="java.lang.String" %>
<%@ attribute name="searchContactUrl" type="java.lang.String" %>
<%@ attribute name="searchReturnUrl" type="java.lang.String" %>
<%@ attribute name="searchOmnitureTag" type="java.lang.String" %>
<%@ attribute name="searchAudience" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.it.spf.portal.cleansheet.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.it.spf.portal.cleansheet.Urls" />

<fmt:message var="prodServ" key="text.cleansheet.prodserv" bundle="${msgResources}" />
<fmt:message var="support" key="text.cleansheet.support" bundle="${msgResources}" />
<fmt:message var="solutions" key="text.cleansheet.solutions" bundle="${msgResources}" />
<fmt:message var="explore" key="text.cleansheet.explore" bundle="${msgResources}" />
<fmt:message var="communities" key="text.cleansheet.communities" bundle="${msgResources}" />

<fmt:message var="prodServUrl" key="link.cleansheet.prodserv" bundle="${urlResources}" />
<fmt:message var="supportUrl" key="link.cleansheet.support" bundle="${urlResources}" />
<fmt:message var="solutionsUrl" key="link.cleansheet.solutions" bundle="${urlResources}" />

<fmt:message var="searchCountryCode" key="text.cleansheet.searchCountryCode" bundle="${msgResources}" />
<fmt:message var="searchLanguageCode" key="text.cleansheet.searchLanguageCode" bundle="${msgResources}" />
<fmt:message var="searchCriteria" key="text.cleansheet.searchCriteria" bundle="${msgResources}" />
<fmt:message var="searchLabel" key="text.cleansheet.searchLabel" bundle="${msgResources}" />
<fmt:message var="searchBegin" key="text.cleansheet.beginSearch" bundle="${msgResources}" />

<c:if test="${empty searchUrl}">
	<fmt:message var="searchUrl" key="link.cleansheet.search" bundle="${urlResources}" />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<div id="tabSearchArea">
	<div id="tabArea">
		<div>
			<a href="${prodServUrl}">${prodServ}</a>
		</div>
		<div>
			<a href="${supportUrl}">${support}</a>
		</div>
		<c:choose>
			<c:when test="${!empty exploreUrl}">
				<div>
					<a href="${exploreUrl}">${explore}</a>
				</div>
			</c:when>
			<c:otherwise>
				<div>
					<a href="${solutionsUrl}">${solutions}</a>
				</div>
			</c:otherwise>
		</c:choose>
		<c:if test="${!empty communitiesUrl}">
			<div>
				<a href="${communitiesUrl}">${communities}</a>
			</div>
		</c:if>
	</div>

	<div id="searchAreaWide">
		<c:choose>
			<c:when test="${!empty searchWidget}">
				${searchWidget}
			</c:when>
			<c:otherwise>
				<form id="searchForm" class="zeroMargin" method="get" action="${searchUrl}">
					<input type="hidden" name="charset" value="UTF-8" />
					<input type="hidden" name="cc" value="${searchCountryCode}" />
					<input type="hidden" name="lang" value="${searchLanguageCode}" />						
					<input type="hidden" name="qp" value="${searchQueryPrefix}" />
					<input type="hidden" name="hps" value="${searchSectionName}" />
					<input type="hidden" name="hpn" value="${searchReturnText}" />
					
					<c:if test="${!empty searchContactUrl}">
						<input type="hidden" name="hpa" value="${searchContactUrl}" />
					</c:if>
					
					<c:if test="${!empty searchReturnUrl}">
						<input type="hidden" name="hpr" value="${searchReturnUrl}" />
					</c:if>
					
					<c:if test="${!empty searchOmnitureTag}">
						<input type="hidden" name="hpo" value="${searchOmnitureTag}" />
					</c:if>
					
					<c:if test="${!empty searchAudience}">
						<input type="hidden" name="h_audience" value="${searchAudience}" />
					</c:if>
			
					<div id="searchBoxW" class="bold">
						<label class="screenReading" for="textboxW">${searchCriteria}</label>
						<input id="textboxW" type="text" onblur="restoreSearch(this);" onfocus="clearSearch(this);" value="${searchLabel}" maxlength="100" size="26" name="qt">
						<input class="arrowButtonBlackBrd" type="submit" title="${searchBegin}" value="">
					</div>
				</form>
			</c:otherwise>
		</c:choose>
	</div>
</div>