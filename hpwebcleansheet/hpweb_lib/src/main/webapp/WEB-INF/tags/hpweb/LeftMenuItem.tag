<%@ tag pageEncoding="UTF-8" body-content="scriptless" %>

<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="url" type="java.lang.String" %>
<%@ attribute name="highlighted" type="java.lang.Boolean" %>
<%@ attribute name="subMenuItems" type="java.util.List" %>

<%-- Deprecated Attributes --%>
<%@ attribute name="indentLevel" type="java.lang.Integer" %>

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
	</c:when>
	<c:otherwise>
		<fmt:message var="spacerUrl" key="link.hpweb2003.spacer" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${empty requestScope['com.hp.frameworks.wpa.hpweb.INDENT_LEVEL']}">
		<c:set var="indentLevel" value="0" />
	</c:when>
	<c:otherwise>
		<c:set var="indentLevel" value="${requestScope['com.hp.frameworks.wpa.hpweb.INDENT_LEVEL']}" />
	</c:otherwise>
</c:choose>

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

<c:set var="width" value="${150 - (indentLevel * 10)}" />

<c:choose>
	<c:when test="${empty url}">
		<c:set var="colspan" value="${4 - indentLevel}" />
	</c:when>
	<c:otherwise>
		<c:set var="colspan" value="${3 - indentLevel}" />
	</c:otherwise>
</c:choose>

<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>


<c:choose>
	<c:when test="${empty title}">
		<tr class="decoration">
			<td align="${left}" width="150" colspan="4"><img src="${spacerUrl}" width="150" height="10" alt=""></td>
		</tr>
		<tr class="decoration">
			<td align="${left}" width="10"><img src="${spacerUrl}" width="10" height="1" alt=""></td>
			<td align="${left}" valign="top" width="140" class="colorCCCCCCbg" colspan="3"><img src="${spacerUrl}" width="140" height="1" alt=""></td>
		</tr>
		<tr class="decoration">
			<td align="${left}" width="150" colspan="4"><img src="${spacerUrl}" width="150" height="10" alt=""></td>
		</tr>	
	</c:when>
	<c:otherwise>
		<tr>
			<c:if test="${indentLevel gt 0}">
				<td align="left" width="10"></td>
			</c:if>
			<c:if test="${indentLevel gt 1}">
				<td align="left" width="10"></td>
			</c:if>						
			<c:if test="${!empty url}">
				<td align="${left}" valign="top" width="10" class="color003366">&raquo;</td>
			</c:if>
			
			<td align="${left}" width="${width}" colspan="${colspan}">				
				<c:if test="${indentLevel eq 0}"><h3></c:if><c:if test="${!empty url}"><a href="${url}" <c:if test="${highlighted}">class="bold"</c:if>></c:if>${title}<c:if test="${!empty url}"></a></c:if><c:if test="${indentLevel eq 0}"></h3></c:if>
			</td>
		</tr>
	</c:otherwise>
</c:choose>

<c:set var="com.hp.frameworks.wpa.hpweb.INDENT_LEVEL" value="${indentLevel + 1}" scope="request" />

<c:choose>
	<c:when test="${!empty subMenuItems}">
		<c:forEach var="menuItem" items="${subMenuItems}">
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

<c:set var="com.hp.frameworks.wpa.hpweb.INDENT_LEVEL" value="${indentLevel}" scope="request" />

