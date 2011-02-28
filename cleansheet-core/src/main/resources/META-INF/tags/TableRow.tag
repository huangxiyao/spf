<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="headerStyle" type="java.lang.Boolean" description="Flag indicating whether or not the table row should be rendered with a header style" %>
<%@ attribute name="cssClass" type="java.lang.String"  %>
<%@ attribute name="align" type="java.lang.String"  %>
<%@ attribute name="dir" type="java.lang.String"  %>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="style" type="java.lang.String" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="valign" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${headerStyle}">
		<c:set var="cssClass" value='theme ${cssClass}' />
		<c:set var="com.hp.frameworks.wpa.hpweb.HEADER_ROW" value="true" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="altRow" value="${requestScope['com.hp.frameworks.wpa.hpweb.ALT_ROW']}" />
		<c:set var="com.hp.frameworks.wpa.hpweb.ALT_ROW" value="${!altRow}" scope="request" />
		
		<c:choose>
			<c:when test="${!altRow}">
				<c:set var="attrList" value='bgcolor="#FFFFFF"' />
			</c:when>
			<c:otherwise>
				<c:set var="attrList" value='bgcolor="#E7E7E7"' />
			</c:otherwise>
		</c:choose>		
	</c:otherwise>
</c:choose>

<c:if test="${!empty align}">
	<c:set var="attrList" value='${attrList} align="${align}"' />
</c:if>

<c:if test="${!empty cssClass}">
	<c:set var="attrList" value='${attrList} class="${cssClass}"' />
</c:if>

<c:if test="${!empty dir}">
	<c:set var="attrList" value='${attrList} dir="${dir}"' />
</c:if>

<c:if test="${!empty id}">
	<c:set var="attrList" value='${attrList} id="${id}"' />
</c:if>

<c:if test="${!empty style}">
	<c:set var="attrList" value='${attrList} style="${style}"' />
</c:if>

<c:if test="${!empty title}">
	<c:set var="attrList" value='${attrList} title="${title}"' />
</c:if>

<c:if test="${!empty valign}">
	<c:set var="attrList" value='${attrList} valign="${valign}"' />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<tr ${attrList}">
	<jsp:doBody />
</tr>


<%----------------------------------------------------------------------------- 
	Clean-up  
-----------------------------------------------------------------------------%>

<c:set var="com.hp.frameworks.wpa.hpweb.HEADER_ROW" value="false" scope="request" />