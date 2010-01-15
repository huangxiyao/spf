<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="titleUrl" type="java.lang.String" %>
<%@ attribute name="promotion" type="java.lang.String" %>
<%@ attribute name="menuItems" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-core" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />

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


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<table border="0" cellpadding="0" cellspacing="0" width="170" background="${spacerUrl}">
<tr class="colorDCDCDCbg">
	<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td>
	<c:if test="${!empty titleUrl}"><td align="${left}" valign="top" width="10" class="color003366bld">&raquo;&nbsp;</td></c:if>
	<td align="${left}" valign="middle">
		<c:choose>
			<c:when test="${!empty titleUrl}">
				<h2><a href="${titleUrl}" class="bold"${dir}>${title}</a></h2>
			</c:when>
			<c:otherwise>
				<h2 class="bold">${title}</h2>
			</c:otherwise>
		</c:choose>
	</td>
</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="170" background="${spacerUrl}">
<tr>
	<td align="${left}" valign="top" width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td>
	<td align="${left}" valign="top" width="150">
		<table border="0" cellpadding="0" cellspacing="0" width="150" background="${spacerUrl}">
			<tr class="decoration">
				<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="10" alt="" border="0"></td>
				<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="10" alt="" border="0"></td>
				<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="10" alt="" border="0"></td>
				<td align="${left}" width="120"><img src="${spacerUrl}" width="120" height="10" alt="" border="0"></td>
			</tr>
			<c:choose>
				<c:when test="${!empty menuItems}">
					<c:forEach var="menuItem" items="${menuItems}">
						<c:if test="${menuItem.visible}">
							<hpweb:leftMenuItem subMenuItems="${menuItem.subMenuItems}">
								<jsp:attribute name="title">${menuItem.title}</jsp:attribute>
								<jsp:attribute name="url">${menuItem.url}</jsp:attribute>
								<jsp:attribute name="highlighted">${menuItem.highlighted}</jsp:attribute>
							</hpweb:leftMenuItem>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<jsp:doBody />
				</c:otherwise>
			</c:choose>			
			<tr class="decoration">
				<td align="${left}" width="150" colspan="4"><img src="${spacerUrl}" width="150" height="10" alt="" border="0"></td>
			</tr>			
		</table>
	</td>
	<td align="${left}" valign="top" width="10"><img src="${spacerUrl}" width="10" height="1" alt="" border="0"></td>
</tr>
<tr class="decoration">
	<td colspan="3" class="colorCCCCCCbg"><img src="${spacerUrl}" width="1" height="2" alt="" border="0"></td>
</tr>
<c:if test="${!empty promotion}">
	<tr class="decoration">
		<td colspan="3"><img src="${spacerUrl}" width="1" height="20" alt="" border="0"></td>
	</tr>
	<tr>
		<td align="center" valign="top" colspan="3">${promotion}</td>
	</tr>	
</c:if>
</table>