<%@ tag pageEncoding="UTF-8" body-content="empty"%>

<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="tagline" type="java.lang.String" %>
<%@ attribute name="breadcrumbItems" type="java.util.List"%>
<%@ attribute name="topPromotion" type="java.lang.String"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>

<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.frameworks.wpa.hpweb.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<div id="breadcrumbsReserved">
	<c:if test="${!empty breadcrumbItems}">
		<div id="breadcrumbs">

			<c:choose><c:when test="${hpwebfn:isLocaleLTR(urlResources)}">
				<c:forEach var="breadcrumb" items="${breadcrumbItems}" varStatus="loopStatus">
					<c:if test="${!loopStatus.first}">&gt;&nbsp;</c:if>
					<a href="${breadcrumb.url}">${breadcrumb.title}</a>
				</c:forEach>			
			</c:when>
			<c:otherwise>
				<c:set var="breadcrumbLength" value="${fn:length(breadcrumbItems)}" />
				<c:forEach items="${breadcrumbItems}" varStatus="loopStatus">
					<c:if test="${!loopStatus.first}">&lt;&nbsp;</c:if>
					<a href="${breadcrumbItems[breadcrumbLength - loopStatus.count].url}">${breadcrumbItems[breadcrumbLength - loopStatus.count].title}</a>
				</c:forEach>							
			</c:otherwise></c:choose>

		</div>
	</c:if>
</div>

<div id="titleArea">
	<div id="title"><h1>${title}</h1>${tagline}</div>
	<c:if test="${!empty topPromotion}">
		<div id="moniker">${topPromotion}</div>
	</c:if>
</div>
