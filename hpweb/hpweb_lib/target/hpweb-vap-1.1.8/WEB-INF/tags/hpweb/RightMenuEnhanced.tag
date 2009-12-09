<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="promotion" type="java.lang.String" %>
<%@ attribute name="menuItems" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-content-enhanced" %>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<div id="enhancedRightNav">
	<div id="enhancedRightNavLinks">
		<c:set var="com.hp.frameworks.wpa.hpweb.IN_LINKS_UNIT" value="true" scope="request" />
		<ul class="linksUnit">
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
		
		<c:if test="${requestScope['com.hp.frameworks.wpa.hpweb.IN_LINKS_UNIT']}">
			<c:remove var="com.hp.frameworks.wpa.hpweb.IN_LINKS_UNIT" scope="request" />
			</ul>
		</c:if>					
	</div>
	
	<c:if test="${!empty promotion}">	
		<div id="enhancedRightNavPromotions">${promotion}</div>
	</c:if>
</div>
