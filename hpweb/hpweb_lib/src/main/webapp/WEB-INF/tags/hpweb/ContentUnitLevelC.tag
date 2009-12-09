<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="url" type="java.lang.String" %>
<%@ attribute name="headerLevel" type="java.lang.Integer" %>
<%@ attribute name="shaded" type="java.lang.Boolean" %>

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

<c:if test="${empty headerLevel || headerLevel < 2 || headerLevel > 6}">
	<c:set var="headerLevel" value="2" />
</c:if>

<c:choose>
	<c:when test="${width == 0}">
		<c:set var="width" value="100%" />
		<c:set var="bodyWidth" value="100%" />
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${shaded}">
				<c:set var="bodyWidth" value="${width - 20}" />
			</c:when>
			<c:otherwise>
				<c:set var="bodyWidth" value="${width}" />			
			</c:otherwise>
		</c:choose>	
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${shaded}">
		<c:set var="colspan" value="3" />
		<c:set var="background" value="colorE7E7E7bg" />		
	</c:when>
	<c:otherwise>
		<c:set var="colspan" value="1" />
		<c:set var="background" value="colorFFFFFFbg" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${!empty url}">
		<c:set var="headerTheme" value="color003366bld" />
	</c:when>
	<c:otherwise>
		<c:set var="headerTheme" value="bold" />
	</c:otherwise>	
</c:choose>

<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<table border="0" cellpadding="0" cellspacing="0" width="${width}">
<tr>
	<td align="${left}" valign="top" colspan="${colspan}"><h${headerLevel} class="${headerTheme}"><c:if test="${!empty url}">&raquo;&nbsp;<a href="${url}" class="bold"></c:if>${title}<c:if test="${!empty url}"></a></c:if></h${headerLevel}></td>
</tr>
<tr class="decoration">
	<td class="theme" colspan="${colspan}"><img src="${spacerUrl}" width="1" height="4" alt="" border="0"></td>
</tr>
<tr class="decoration">
	<td class="${background}" colspan="${colspan}"><img src="${spacerUrl}" width="1" height="10" alt="" border="0"></td>
</tr>
<tr class="${background}">
	<c:if test="${shaded}"><td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td></c:if>
	<td align="${left}" valign="top" width="${bodyWidth}"><jsp:doBody /></td>
	<c:if test="${shaded}"><td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td></c:if>
</tr>

<c:if test="${shaded}">
	<tr class="decoration">
		<td colspan="3" class="colorE7E7E7bg"><img src="${spacerUrl}" width="1" height="10" alt="" border="0"></td>
	</tr>
</c:if>

<tr class="decoration">
	<td colspan="${colspan}" class="colorFFFFFFbg"><img src="${spacerUrl}" width="1" height="10" alt="" border="0"></td>
</tr>
</table>