<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="url" type="java.lang.String" %>
<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="headerLevel" type="java.lang.Integer" %>
<%@ attribute name="style" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-content" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${empty style}">
		<c:set var="style" value="intro" />
	</c:when>
	<c:otherwise>
		<c:set var="style" value="${fn:toLowerCase(style)}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${style eq 'levela'}">
		<hpweb:contentUnitLevelA title="${title}" url="${url}" width="${width}" headerLevel="${headerLevel}">
			<jsp:doBody />
		</hpweb:contentUnitLevelA>	
	</c:when>
	<c:when test="${style eq 'levelabig'}">
		<hpweb:contentUnitLevelA title="${title}" url="${url}" width="${width}" headerLevel="${headerLevel}" largeFont="true">
			<jsp:doBody />
		</hpweb:contentUnitLevelA>	
	</c:when>	
	<c:when test="${style eq 'levelb' || style eq 'levelbbeveled'}">
		<hpweb:contentUnitLevelB title="${title}" url="${url}" width="${width}" headerLevel="${headerLevel}">
			<jsp:doBody />
		</hpweb:contentUnitLevelB>	
	</c:when>		
	<c:when test="${style eq 'levelc'}">
		<hpweb:contentUnitLevelC title="${title}" url="${url}" width="${width}" headerLevel="${headerLevel}">
			<jsp:doBody />
		</hpweb:contentUnitLevelC>	
	</c:when>	
	<c:when test="${style eq 'levelcshaded'}">
		<hpweb:contentUnitLevelC title="${title}" url="${url}" width="${width}" headerLevel="${headerLevel}" shaded="true">
			<jsp:doBody />
		</hpweb:contentUnitLevelC>	
	</c:when>	
	<c:when test="${style eq 'leveld' || style eq 'levele'}">
		<hpweb:contentUnitLevelD title="${title}" url="${url}" width="${width}" headerLevel="${headerLevel}">
			<jsp:doBody />
		</hpweb:contentUnitLevelD>	
	</c:when>	
	<c:otherwise>
		<hpweb:contentUnitIntro title="${title}" width="${width}" headerLevel="${headerLevel}">
			<jsp:doBody />
		</hpweb:contentUnitIntro>
	</c:otherwise>
</c:choose>