<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<h1><c:out value="${greetings}" /></h1>
<c:forEach items="${listOfItems}" var="item">
	<c:set var="itemId" value="${item.id}" />
	<% String id = String.valueOf(pageContext.findAttribute("itemId")); %>
	<portlet:renderURL var="itemUrl" windowState="maximized">
		<portlet:param name="id" value="<%=id%>" />
		<c:if test="${preview}">
			<portlet:param name="preview" value="true" />
		</c:if>
	</portlet:renderURL>
	<p>
		<div><a href="<c:out value="${itemUrl}" escapeXml="false"/>"><c:out value="${item.label}" /></a></div>
		<div><fmt:formatDate value="${item.date}" pattern="yyyy-MM-dd HH:mm:ss,SSS" /></div>
		<div><c:out value="${item.description}" /></div>
	</p>
</c:forEach>
