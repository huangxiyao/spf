<%@ tag pageEncoding="UTF-8" body-content="scriptless"%>

<%@ attribute name="zoneId" type="java.lang.String" %>
<%@ attribute name="zoneTitle" type="java.lang.String" %>
<%@ attribute name="countryLinks" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cscore" uri="http://frameworks.hp.com/spf/cleansheet-core" %>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<%--- divide to two columns later --%>
<div class="hidden" id="${zoneId}" title="${zoneTitle}">
    <div class="continentheader">${zoneTitle}</div>
    <div class="firstcolumn">
    	<c:forEach items="${countryLinks}" var="country">
         <a href="${country.url}" name="${country.name}" onclick="try{trackMetrics('linkClick',{type:'link', id:this.getAttribute('name')});} catch(err) {}">${country.displayName}</a>
         <br />
         </c:forEach>
	</div>
	<div class="secondcolumn">
	</div>
</div>
				