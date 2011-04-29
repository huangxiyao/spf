<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<portlet:defineObjects/>
<%
pageContext.setAttribute("userProfile", renderRequest.getAttribute(renderRequest.USER_INFO));
pageContext.setAttribute("userProfileKey", renderRequest.getAttribute("ContextMap"));
pageContext.setAttribute("locale", renderRequest.getLocale());
%>
<style type="text/css">
.profileTable {
	width:600px;
	table-layout:fixed;
}
.profileTable th {
	background-color: #666;
	color: #fff;
	font-weight: bold;
	padding-left: 10px;
}

TD {
  word-break:break-all;
}
</style>
<table cellspacing="2" cellpadding="3" class="profileTable">
	<tr>
		<th width="200">
			Profile Key in userProfile Map
		</th>
		<th>
			Profile Value in userProfile Map
		</th>
	</tr>
	<c:forEach items="${userProfile}" var="entry">
		<tr>
			<td>
				<c:out value="${entry.key}" />
			</td>
			<td>
				<c:out value="${entry.value}" />
			</td>
		</tr>
	</c:forEach>	
	<tr>
		<td>
			Locale
		</td>
		<td>
			<c:out value="${locale}" />
		</td>
	</tr>
	<tr>
		<th width="200">
			Profile Key in Context Map
		</th>
		<th>
			Profile Value in Context Map
		</th>
	</tr>
	<c:forEach items="${userProfileKey}" var="entry">
		<tr>
			<td>
				<c:out value="${entry.key}" />
			</td>
			<td>
				<c:out value="${entry.value}" />
			</td>
		</tr>
	</c:forEach>
</table>
