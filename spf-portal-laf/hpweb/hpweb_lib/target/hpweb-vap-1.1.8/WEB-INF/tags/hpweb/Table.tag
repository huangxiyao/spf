<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="cssClass" type="java.lang.String"  %>
<%@ attribute name="dir" type="java.lang.String"  %>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="style" type="java.lang.String" %>
<%@ attribute name="summary" type="java.lang.String" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="width" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:if test="${!empty id}">
	<c:set var="attrList" value='id="${id}"' />
</c:if>

<c:if test="${!empty cssClass}">
	<c:set var="attrList" value='${attrList} class="${cssClass}"' />
</c:if>

<c:if test="${!empty dir}">
	<c:set var="attrList" value='${attrList} dir="${dir}"' />
</c:if>

<c:if test="${!empty style}">
	<c:set var="attrList" value='${attrList} style="${style}"' />
</c:if>

<c:if test="${!empty summary}">
	<c:set var="attrList" value='${attrList} summary="${summary}"' />
</c:if>

<c:if test="${!empty title}">
	<c:set var="attrList" value='${attrList} title="${title}"' />
</c:if>

<c:if test="${!empty width}">
	<c:set var="attrList" value='${attrList} width="${width}"' />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<table border="0" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC" ${attrList}>
	<jsp:doBody />
</table>


<%----------------------------------------------------------------------------- 
	Clean-up  
-----------------------------------------------------------------------------%>

<c:set var="com.hp.frameworks.wpa.hpweb.ALT_ROW" value="false" scope="request" />
