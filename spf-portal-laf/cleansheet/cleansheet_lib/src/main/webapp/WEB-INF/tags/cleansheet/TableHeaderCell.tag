<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="abbr" type="java.lang.String"  %>
<%@ attribute name="align" type="java.lang.String"  %>
<%@ attribute name="axis" type="java.lang.String"  %>
<%@ attribute name="bgcolor" type="java.lang.String"  %>
<%@ attribute name="cssClass" type="java.lang.String"  %>
<%@ attribute name="colspan" type="java.lang.String"  %>
<%@ attribute name="dir" type="java.lang.String"  %>
<%@ attribute name="headers" type="java.lang.String"  %>
<%@ attribute name="height" type="java.lang.String"  %>
<%@ attribute name="id" type="java.lang.String" %>
<%@ attribute name="nowrap" type="java.lang.String" %>
<%@ attribute name="rowspan" type="java.lang.String" %>
<%@ attribute name="scope" type="java.lang.String" %>
<%@ attribute name="style" type="java.lang.String" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="valign" type="java.lang.String" %>
<%@ attribute name="width" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${requestScope['com.hp.frameworks.wpa.hpweb.HEADER_ROW']}">
		<c:set var="cssClass" value="small themebody ${cssClass}" />
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="smallbold ${cssClass}" />
	</c:otherwise>
</c:choose>

<c:if test="${!empty abbr}">
	<c:set var="attrList" value='abbr="${abbr}"' />
</c:if>

<c:if test="${!empty align}">
	<c:set var="attrList" value='${attrList} align="${align}"' />
</c:if>

<c:if test="${!empty axis}">
	<c:set var="attrList" value='${attrList} axis="${axis}"' />
</c:if>

<c:if test="${!empty bgcolor}">
	<c:set var="attrList" value='${attrList} bgcolor="${bgcolor}"' />
</c:if>

<c:if test="${!empty cssClass}">
	<c:set var="attrList" value='${attrList} class="${cssClass}"' />
</c:if>

<c:if test="${!empty colspan}">
	<c:set var="attrList" value='${attrList} colspan="${colspan}"' />
</c:if>

<c:if test="${!empty dir}">
	<c:set var="attrList" value='${attrList} dir="${dir}"' />
</c:if>

<c:if test="${!empty headers}">
	<c:set var="attrList" value='${attrList} headers="${headers}"' />
</c:if>

<c:if test="${!empty height}">
	<c:set var="attrList" value='${attrList} height="${height}"' />
</c:if>

<c:if test="${!empty id}">
	<c:set var="attrList" value='${attrList} id="${id}"' />
</c:if>

<c:if test="${!empty nowrap}">
	<c:set var="attrList" value='${attrList} nowrap="${nowrap}"' />
</c:if>

<c:if test="${!empty rowspan}">
	<c:set var="attrList" value='${attrList} rowspan="${rowspan}"' />
</c:if>

<c:if test="${!empty scope}">
	<c:set var="attrList" value='${attrList} scope="${scope}"' />
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

<c:if test="${!empty width}">
	<c:set var="attrList" value='${attrList} width="${width}"' />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<th ${attrList}>
	<jsp:doBody />
</th>