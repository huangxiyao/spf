<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<c:forEach items="${listOfItems}" var="item">
	<c:set var="itemId" value="${item.id}" />
	<% String id = String.valueOf(pageContext.findAttribute("itemId")); %>
	<portlet:renderURL var="itemUrl" windowState="maximized">
		<portlet:param name="id" value="<%=id%>" />
	</portlet:renderURL>
	<div>&raquo; <a href="<c:out value="${itemUrl}" escapeXml="false"/>"><c:out value="${item.label}" /></a></div>
	<div><fmt:formatDate value="${item.date}" pattern="yyyy-MM-dd HH:mm:ss,SSS" /></div>
</c:forEach>
