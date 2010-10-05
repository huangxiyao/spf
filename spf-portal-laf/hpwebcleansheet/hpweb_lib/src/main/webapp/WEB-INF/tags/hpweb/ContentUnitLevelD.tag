<%@ tag pageEncoding="UTF-8" body-content="scriptless" %>

<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="url" type="java.lang.String" %>
<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="headerLevel" type="java.lang.Integer" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">
		<fmt:message var="spacerUrl" key="link.hpweb2003.secure_spacer" bundle="${urlResources}" />		
	</c:when>
	<c:otherwise>
		<fmt:message var="spacerUrl" key="link.hpweb2003.spacer" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${hpwebfn:isLocaleLTR(urlResources)}">	
		<c:set var="left" value="left" />
	</c:when>
	<c:otherwise>
		<c:set var="left" value="right" />
	</c:otherwise>
</c:choose>

<c:if test="${width == 0}">
	<c:set var="width" value="100%" />
</c:if>

<c:if test="${empty headerLevel || headerLevel < 2 || headerLevel > 6}">
	<c:set var="headerLevel" value="2" />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<table border="0" cellpadding="0" cellspacing="0" width="${width}">
<tr>
	<td align="${left}" valign="top">
		<h${headerLevel}><span class="leveld"><c:if test="${!empty url}">&raquo;&nbsp;<a href="${url}" class="bold"></c:if>${title}<c:if test="${!empty url}"></a></c:if></span></h${headerLevel}>
		<p><jsp:doBody /></p>		
	</td>
</tr>
<tr class="decoration">
	<td class="colorFFFFFFbg"><img src="${spacerUrl}" width="1" height="10" alt="" border="0"></td>
</tr>
</table>