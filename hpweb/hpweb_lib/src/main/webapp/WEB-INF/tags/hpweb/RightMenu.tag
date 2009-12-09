<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="promotion" type="java.lang.String" %>
<%@ attribute name="menuItems" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-content" %>
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


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<table border="0" cellpadding="0" cellspacing="0" width="180" bgcolor="#E7E7E7">
<tr class="decoration">
	<td class="colorCCCCCCbg" colspan="3"><img src="${spacerUrl}" width="1" height="1" alt="" border="0"></td>
</tr>
<tr>
	<td align="${left}" valign="top" width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td>
	<td align="${left}" valign="top" width="160">
		<table border="0" cellpadding="0" cellspacing="0" width="160" background="${spacerUrl}">
		<tr class="decoration">
			<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="10" alt="" border="0"></td>
			<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="10" alt="" border="0"></td>
			<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="10" alt="" border="0"></td>
			<td align="${left}" width="130"><img src="${spacerUrl}" width="130" height="10" alt="" border="0"></td>
		</tr>
		<c:choose>
			<c:when test="${!empty menuItems}">
				<c:forEach var="menuItem" items="${menuItems}">
					<c:if test="${menuItem.visible}">
						<hpweb:rightMenuItem subMenuItems="${menuItem.subMenuItems}">
							<jsp:attribute name="title">${menuItem.title}</jsp:attribute>
							<jsp:attribute name="url">${menuItem.url}</jsp:attribute>
							<jsp:attribute name="highlighted">${menuItem.highlighted}</jsp:attribute>
						</hpweb:rightMenuItem>
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<jsp:doBody />
			</c:otherwise>
		</c:choose>			
		<tr class="decoration">
			<td align="${left}" width="160" colspan="4"><img src="${spacerUrl}" width="160" height="20" alt="" border="0"></td>
		</tr>			
	</table>
</td>
<td align="${left}" valign="top" width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td>
</tr>
<c:if test="${!empty promotion}">
<tr class="decoration">
	<td colspan="3" class="colorCCCCCCbg"><img src="${spacerUrl}" width="1" height="1" alt="" border="0"></td>
</tr>
<tr class="decoration">
	<td colspan="3"><img src="${spacerUrl}" width="1" height="20" alt="" border="0"></td>
</tr>
<tr>
	<td align="center" valign="top" colspan="3">${promotion}</td>
</tr>	
<tr class="decoration">
	<td colspan="3"><img src="${spacerUrl}" width="1" height="20" alt="" border="0"></td>
</tr>
</c:if>
<tr class="decoration">
	<td colspan="3" class="colorCCCCCCbg"><img src="${spacerUrl}" width="1" height="2" alt="" border="0"></td>
</tr>
</table>