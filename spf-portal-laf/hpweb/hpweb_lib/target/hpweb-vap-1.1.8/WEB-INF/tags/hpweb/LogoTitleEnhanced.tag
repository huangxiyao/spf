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

<fmt:message var="home" key="text.hpweb2003.home" bundle="${msgResources}" />
<fmt:message var="homeUrl" key="link.hpweb2003.home" bundle="${urlResources}" />

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">	
		<fmt:message var="globalImgDir" key="link.hpweb2003.secure_global_img_dir" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="globalImgDir" key="link.hpweb2003.global_img_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

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

<div id="logoTitleArea">
	<div id="logo"><a href="${homeUrl}"><img src="${globalImgDir}hpweb_1-2_topnav_hp_logo.gif" width="64" height="55" alt="${home}" border="0" /></a></div>
	<div id="title"><h1>${title}</h1>${tagline}</div>
	<c:if test="${!empty topPromotion}">
		<div id="moniker">${topPromotion}</div>
	</c:if>
</div>
