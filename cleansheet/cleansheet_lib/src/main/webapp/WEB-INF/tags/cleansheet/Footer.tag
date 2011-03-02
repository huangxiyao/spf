<%@ tag pageEncoding="UTF-8" body-content="empty"%>

<%@ attribute name="privacyUrl" type="java.lang.String" %>
<%@ attribute name="imprintUrl" type="java.lang.String" %>
<%@ attribute name="termsUrl" type="java.lang.String"  %>
<%@ attribute name="saleTermsUrl" type="java.lang.String" %>
<%@ attribute name="feedbackUrl" type="java.lang.String" %>
<%@ attribute name="feedbackText" type="java.lang.String" %>
<%@ attribute name="siteMapUrl" type="java.lang.String" %>
<%@ attribute name="metricsUrl" type="java.lang.String" %>
<%@ attribute name="wide" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.it.spf.portal.cleansheet.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.it.spf.portal.cleansheet.Urls" />

<fmt:message var="privacy" key="text.cleansheet.privacy" bundle="${msgResources}" />
<fmt:message var="imprint" key="text.cleansheet.imprint" bundle="${msgResources}" />
<fmt:message var="terms" key="text.cleansheet.terms" bundle="${msgResources}" />
<fmt:message var="saleTerms" key="text.cleansheet.saleTerms" bundle="${msgResources}" />
<fmt:message var="siteMap" key="text.cleansheet.siteMap" bundle="${msgResources}" />

<fmt:message var="copyright" key="text.cleansheet.copyright" bundle="${msgResources}">
	<fmt:param>	
		<jsp:useBean id="now" class="java.util.Date" />
		<fmt:formatDate value="${now}"  pattern="yyyy"/>	
	</fmt:param>
</fmt:message>

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">
		<fmt:message var="defaultMetricsUrl" key="link.cleansheet.secure_metrics" bundle="${urlResources}" />
	</c:when>
	<c:otherwise>
		<fmt:message var="defaultMetricsUrl" key="link.cleansheet.metrics" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>

<c:if test="${empty privacyUrl}">
	<fmt:message var="privacyUrl" key="link.cleansheet.privacy" bundle="${urlResources}" />
</c:if>

<c:if test="${empty imprintUrl}">
	<fmt:message var="imprintUrl" key="link.cleansheet.imprint" bundle="${urlResources}" />
</c:if>

<c:if test="${empty termsUrl}">
	<fmt:message var="termsUrl" key="link.cleansheet.terms" bundle="${urlResources}" />
</c:if>

<c:if test="${empty metricsUrl}">
	<c:set var="metricsUrl" value="${defaultMetricsUrl}" />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<div id="footer">
	<c:if test="${!wide}"><div class="container740"></c:if>
		<c:choose>
			<c:when test="${!wide && empty saleTermsUrl && empty siteMapUrl}">
				<div id="privacy"><a href="${privacyUrl}" class="udrlinesmall">${privacy}</a></div>		
				<div id="terms"><a href="${termsUrl}" class="udrlinesmall">${terms}</a></div>
				<div id="feedback"><a href="${feedbackUrl}" class="udrlinesmall">${feedbackText}</a></div>			
			</c:when>
			<c:otherwise>
				<a href="${privacyUrl}" class="udrlinesmall">${privacy}</a>
				&nbsp;|&nbsp;<a href="${termsUrl}" class="udrlinesmall">${terms}</a>
				<c:if test="${!empty saleTermsUrl}">&nbsp;|&nbsp;<a href="${saleTermsUrl}" class="udrlinesmall">${saleTerms}</a></c:if>
				<c:if test="${!empty feedbackUrl}">&nbsp;|&nbsp;<a href="${feedbackUrl}" class="udrlinesmall">${feedbackText}</a></c:if>
				<c:if test="${!empty siteMapUrl}">&nbsp;|&nbsp;<a href="${siteMapUrl}" class="udrlinesmall">${siteMap}</a></c:if>
			</c:otherwise>
		</c:choose>		
	<c:if test="${!wide}"></div></c:if>
	
	<c:if test="${!empty imprintUrl}"><div id="impressum"><a href="${imprintUrl}" class="udrlinesmall">${imprint}</a></div></c:if>
</div>

<div id="copyright" class="small">${copyright}</div>

<!-- Begin METRICS Javascript -->
<script type="text/javascript" language="JavaScript" src="${metricsUrl}"></script>
<!-- End METRICS Javascript --> 
      