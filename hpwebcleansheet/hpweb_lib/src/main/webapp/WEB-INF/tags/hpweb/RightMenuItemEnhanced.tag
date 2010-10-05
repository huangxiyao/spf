<%@ tag pageEncoding="UTF-8" body-content="scriptless" %>

<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="url" type="java.lang.String" %>
<%@ attribute name="highlighted" type="java.lang.Boolean" %>
<%@ attribute name="subMenuItems" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-content-enhanced" %>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${empty requestScope['com.hp.frameworks.wpa.hpweb.HAS_PARENT']}">
		<c:set var="com.hp.frameworks.wpa.hpweb.HAS_PARENT" value="true" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="hasParent" value="${requestScope['com.hp.frameworks.wpa.hpweb.HAS_PARENT']}" />
	</c:otherwise>
</c:choose>

<c:set var="inLinksUnit" value="${requestScope['com.hp.frameworks.wpa.hpweb.IN_LINKS_UNIT']}" />


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${empty title && !hasParent}">
		<c:if test="${inLinksUnit}">
			<c:remove var="com.hp.frameworks.wpa.hpweb.IN_LINKS_UNIT" scope="request" />
			</ul>
		</c:if>
		<div class="blockDivider"></div>
	</c:when>
	<c:when test="${!empty title}">
		<c:if test="${!inLinksUnit}">
			<c:set var="com.hp.frameworks.wpa.hpweb.IN_LINKS_UNIT" value="true" scope="request" />
			<ul class="linksUnit">
		</c:if>
		
		<c:choose>
			<c:when test="${!hasParent && empty url}">
				<li><h2>${title}</h2>
			</c:when>
			<c:otherwise>
				<li><c:if test="${!hasParent}"><h2></c:if>&raquo;<a href="${url}">${title}</a><c:if test="${!hasParent}"></h2></c:if>
			</c:otherwise>
		</c:choose>		
			
		<c:choose>
			<c:when test="${empty subMenuItems}">		
				<jsp:doBody var="body" />
				<c:if test="${!empty body}">
					<ul class="linksUnit">
						${body}
					</ul>		
				</c:if>		
			</c:when>
			<c:otherwise>	
				<ul class="linksUnit">
					<c:forEach var="menuItem" items="${subMenuItems}">
						<c:if test="${menuItem.visible}">			
							<hpweb:leftMenuItem subMenuItems="${menuItem.subMenuItems}">
								<jsp:attribute name="title">${menuItem.title}</jsp:attribute>
								<jsp:attribute name="url">${menuItem.url}</jsp:attribute>
								<jsp:attribute name="highlighted">${menuItem.highlighted}</jsp:attribute>
							</hpweb:leftMenuItem>		
						</c:if>
					</c:forEach>
				</ul>
			</c:otherwise>
		</c:choose>
		
		</li>
			
	</c:when>
</c:choose>			


<%----------------------------------------------------------------------------- 
	Clean-up  
-----------------------------------------------------------------------------%>

<c:if test="${empty hasParent}">
	<c:remove var="com.hp.frameworks.wpa.hpweb.HAS_PARENT" scope="request" />
</c:if>