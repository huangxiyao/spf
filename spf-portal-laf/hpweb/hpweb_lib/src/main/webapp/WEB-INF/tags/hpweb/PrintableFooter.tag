<%@ tag pageEncoding="UTF-8" body-content="empty" %>

<%@ attribute name="privacyUrl" type="java.lang.String" %>
<%@ attribute name="imprintUrl" type="java.lang.String" %>
<%@ attribute name="termsUrl" type="java.lang.String"  %>
<%@ attribute name="saleTermsUrl" type="java.lang.String" %>
<%@ attribute name="feedbackUrl" type="java.lang.String" %>
<%@ attribute name="feedbackText" type="java.lang.String" %>
<%@ attribute name="metricsUrl" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.frameworks.wpa.hpweb.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />

<fmt:message var="return" key="text.hpweb2003.return" bundle="${msgResources}" />
<fmt:message var="privacy" key="text.hpweb2003.privacy" bundle="${msgResources}" />
<fmt:message var="imprint" key="text.hpweb2003.imprint" bundle="${msgResources}" />
<fmt:message var="terms" key="text.hpweb2003.terms" bundle="${msgResources}" />
<fmt:message var="saleTerms" key="text.hpweb2003.saleTerms" bundle="${msgResources}" />
<fmt:message var="copyright" key="text.hpweb2003.copyright" bundle="${msgResources}">
	<fmt:param>	
		<jsp:useBean id="now" class="java.util.Date" />
		<fmt:formatDate value="${now}"  pattern="yyyy"/>	
	</fmt:param>
</fmt:message>

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">
		<fmt:message var="spacerUrl" key="link.hpweb2003.secure_spacer" bundle="${urlResources}" />	
		<fmt:message var="javascriptDir" key="link.hpweb2003.secure_javascript_dir" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="spacerUrl" key="link.hpweb2003.spacer" bundle="${urlResources}" />
		<fmt:message var="javascriptDir" key="link.hpweb2003.javascript_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>

<c:if test="${empty privacyUrl}">
	<fmt:message var="privacyUrl" key="link.hpweb2003.privacy" bundle="${urlResources}" />
</c:if>

<c:if test="${empty imprintUrl}">
	<fmt:message var="imprintUrl" key="link.hpweb2003.imprint" bundle="${urlResources}" />
</c:if>

<c:if test="${empty termsUrl}">
	<fmt:message var="termsUrl" key="link.hpweb2003.terms" bundle="${urlResources}" />
</c:if>

<c:if test="${empty metricsUrl}">
	<c:set var="metricsUrl" value="${javascriptDir}metrics.js" />
</c:if>



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


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<table border="0" cellpadding="0" cellspacing="0" width="570">
<tr>
	<td align="center" valign="bottom" width="170"${dir}><span class="color003366">&raquo;&nbsp;</span><a href="javascript:history.back();">${return}</a></td>
	<td width="10"></td>
	<td width="390"><img src="${spacerUrl}" width="1" height="60" alt="" border="0"></td>
</tr>
<tr>
	<td align="center" valign="bottom" width="170">&nbsp;</td>
	<td width="10"></td>
	<td width="390"><img src="${spacerUrl}" width="1" height="20" alt="" border="0"></td>
</tr>
</table>


<!-- Begin Footer Area -->
<table border="0" cellpadding="0" cellspacing="0" width="570">
<tr class="decoration">
	<td class="color666666bg"><img src="${spacerUrl}" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
	<td align="${left}" valign="top">
		<table border="0" cellpadding="0" cellspacing="0" width="570">
		<tr class="decoration">
			<td colspan="7"><img src="${spacerUrl}" width="1" height="2" alt="" border="0"></td>
		</tr>
		<tr>
			<td align="center"><a href="${privacyUrl}" class="udrlinesmall">${privacy}</a><c:if test="${!empty imprintUrl}"><br/><a href="${imprintUrl}" class="udrlinesmall">${imprint}</a></c:if></td>
			<td width="2"></td>
			<td align="center"><a href="${termsUrl}" class="udrlinesmall">${terms}</a></td>
			<td width="2"></td>
			
			<c:if test="${!empty saleTermsUrl}">
				<td align="center"><a href="${saleTermsUrl}" class="udrlinesmall">${saleTerms}</a></td>
				<td width="2"></td>					
			</c:if>
			
			<td align="center"><a href="${feedbackUrl}" class="udrlinesmall">${feedbackText}</a></td>
		</tr>
		<tr class="decoration">
			<td colspan="4"><img src="${spacerUrl}" width="1" height="4" alt="" border="0"></td>
		</tr>
		<tr>
			<td align="center" colspan="7" class="small">${copyright}</td>
		</tr>
		</table>
	</td>
</tr>
</table>
<!-- End Footer Area -->  

<!-- Begin METRICS Javascript -->
<script type="text/javascript" language="JavaScript" src="${metricsUrl}"></script>
<!-- End METRICS Javascript -->      