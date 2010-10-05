<%@ tag pageEncoding="UTF-8" body-content="empty"%>

<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="tagline" type="java.lang.String" %>
<%@ attribute name="breadcrumbItems" type="java.util.List"%>
<%@ attribute name="topPromotion" type="java.lang.String"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
		<fmt:message var="spacerUrl" key="link.hpweb2003.secure_spacer" bundle="${urlResources}" />	
		<fmt:message var="globalImgDir" key="link.hpweb2003.secure_global_img_dir" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="spacerUrl" key="link.hpweb2003.spacer" bundle="${urlResources}" />
		<fmt:message var="globalImgDir" key="link.hpweb2003.global_img_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>



<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${hpwebfn:isLocaleLTR(urlResources)}">	
		<c:set var="left" value="left" />
		<c:set var="dir" value="" />	
	</c:when>
	<c:otherwise>
		<c:set var="left" value="right" />
		<c:set var="dir" value=" dir=\"rtl\"" />
	</c:otherwise>
</c:choose>

<c:set var="topPromotionPresent" value="${!empty topPromotion}" />
<c:set var="breadcrumbsPresent" value="${!empty breadcrumbItems}" />

<c:choose>
	<c:when test="${topPromotionPresent}">
		<c:set var="titleCellWidth" value="370" />
	</c:when>
	<c:otherwise>
		<c:set var="titleCellWidth" value="560" />	
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${breadcrumbsPresent}">
		<c:set var="titleSpacerHeight" value="6" />
	</c:when>
	<c:otherwise>
		<c:set var="titleSpacerHeight" value="20" />	
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>


<!-- Begin Page Title Area -->
<table border="0" cellpadding="0" cellspacing="0" width="740">
<tr>
	<td width="170" align="center" valign="middle"><a href="${homeUrl}"><img src="${globalImgDir}hpweb_1-2_topnav_hp_logo.gif" width="64" height="55" alt="${home}" border="0" /></a><br /></td>
	<td width="10"><img src="${spacerUrl}" width="10" height="93" alt="" border="0"></td>
	<td align="${left}" valign="top" width="${titleCellWidth}">
		<c:if test="${breadcrumbsPresent}">
			<c:forEach var="breadcrumb" items="${breadcrumbItems}" varStatus="loopStatus">
				<c:if test="${!loopStatus.first}">&nbsp;<span class="color666666">&gt;</span>&nbsp;</c:if>
				<a href="${breadcrumb.url}" class="udrlinesmall"${dir}>${breadcrumb.title}</a>
			</c:forEach>
			<br />
		</c:if> 
		<img src="${spacerUrl}" width="1" height="${titleSpacerHeight}" alt="" border="0"><h1${dir}>${title}</h1>
		<span${dir}>${tagline}</span>
	</td>
	<c:if test="${topPromotionPresent}">
		<td width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td>
		<td width="180"${dir}>${topPromotion}</td>
	</c:if>
	</tr>
</table>
<!-- End Page Title Area -->
