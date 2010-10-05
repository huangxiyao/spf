<%@ tag pageEncoding="UTF-8" body-content="empty"%>

<%@ attribute name="printableDisabled" type="java.lang.Boolean" %>
<%@ attribute name="printableUrl" type="java.lang.String" %>
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

<fmt:message var="printable" key="text.hpweb2003.printable" bundle="${msgResources}" />
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
		<fmt:message var="globalImgDir" key="link.hpweb2003.secure_global_img_dir" bundle="${urlResources}" />
		<fmt:message var="defaultMetricsUrl" key="link.hpweb2003.secure_metrics" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="spacerUrl" key="link.hpweb2003.spacer" bundle="${urlResources}" />
		<fmt:message var="globalImgDir" key="link.hpweb2003.global_img_dir" bundle="${urlResources}" />
		<fmt:message var="defaultMetricsUrl" key="link.hpweb2003.metrics" bundle="${urlResources}" />
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
	<c:set var="metricsUrl" value="${defaultMetricsUrl}" />
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

<c:if test="${empty printableUrl}">
	<c:set var="printableUrl" value="?printable=true" />
</c:if>

<c:choose>
	<c:when test="${empty saleTermsUrl}">
		<c:set var="linkCellWidth1" value="33%" />
		<c:set var="linkCellWidth2" value="33%" />
	</c:when>
	<c:otherwise>
		<c:set var="linkCellWidth1" value="20%" />
		<c:set var="linkCellWidth2" value="30%" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<!-- Begin Printable Version -->
<table border="0" cellpadding="0" cellspacing="0" width="740">
<tr>
	<td align="center" valign="bottom" width="170" bgcolor="#F0F0F0"><c:if test="${!printableDisabled}"><img width="19" height="13" alt="" border="0" src="${globalImgDir}hpweb_1-2_prnt_icn.gif" /><a href="${printableUrl}" class="udrlinebold"${dir}>${printable}</a></c:if></td>

	<td width="10"></td>
	<td width="560"><img src="${spacerUrl}" width="1" height="60" alt="" border="0"></td>
</tr>
<tr>
	<td align="center" valign="bottom" width="170" bgcolor="#F0F0F0"><img src="${spacerUrl}" width="1" height="1" alt="" border="0"></td>
	<td width="10"></td>
	<td width="560"><img src="${spacerUrl}" width="1" height="20" alt="" border="0"></td>
</tr>
</table>
<!-- End Printable Version -->

<!-- Begin Footer Area -->
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr class="decoration">
	<td class="color666666bg"><img src="${spacerUrl}" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
	<td align="${left}" valign="top">
		<table border="0" cellpadding="0" cellspacing="0" width="740">
		<tr class="decoration">
			<td colspan="4"><img src="${spacerUrl}" width="1" height="4" alt="" border="0"></td>
		</tr>
		<tr>
			<td width="${linkCellWidth1}" align="center"><a href="${privacyUrl}" class="udrlinesmall">${privacy}</a><c:if test="${!empty imprintUrl}"><br/><a href="${imprintUrl}" class="udrlinesmall">${imprint}</a></c:if></td>
			<td width="${linkCellWidth2}" align="center"><a href="${termsUrl}" class="udrlinesmall">${terms}</a></td>
			
			<c:if test="${!empty saleTermsUrl}">
				<td width="${linkCellWidth2}" align="center"><a href="${saleTermsUrl}" class="udrlinesmall">${saleTerms}</a></td>					
			</c:if>
			
			<td width="${linkCellWidth1}" align="center"><a href="${feedbackUrl}" class="udrlinesmall">${feedbackText}</a></td>
		</tr>
		<tr class="decoration">
			<td colspan="4"><img src="${spacerUrl}" width="1" height="4" alt="" border="0"></td>
		</tr>
		<tr>
			<td align="center" colspan="4" class="small">${copyright}</td>
		</tr>
		</table>
	</td>
</tr>
</table>
<!-- End Footer Area -->  

<!-- Begin METRICS Javascript -->
<script type="text/javascript" language="JavaScript" src="${metricsUrl}"></script>
<!-- End METRICS Javascript -->      