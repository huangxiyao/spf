<%@ page import="com.hp.spp.wsrp.shield.WsrpShield"%>
<%-- VAP 7.4 comes with JSTL 1.1 but SPP cannot upgrade to this version due to other deps; therefore
we continue to use JSTL 1.0 but have to update taglib URL, otherwise the runtime EL values are not allowed --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
	if ("POST".equals(request.getMethod())) {
		if (request.getParameter("enabled") != null && request.getParameter("url") != null) {
			WsrpShield.getInstance().setRemoteServerEnabled(
					request.getParameter("url"),
					Boolean.valueOf(request.getParameter("enabled")));
		}
		response.sendRedirect(request.getRequestURI());;
		return;
	}

	pageContext.setAttribute("remoteServers", WsrpShield.getInstance().getRemoteServerList());
	pageContext.setAttribute("rowColor", new String[] {"#E7E7E7", "#FFFFFF"});
%>
<html>
<head>
	<title>SPP Remote Server Availability Administration</title>
	<script type="text/javascript" language="JavaScript"><!--
	var theme = '#336633';
	//--></script>
	<script type="text/javascript" language="JavaScript" src="http://www.hp.com/country/us/en/js/hpweb_utilities.js"></script>
	<script type="text/javascript">
		function submitEnabled(url, enabled) {
			document.forms.ServerEnabledForm.elements['url'].value = url;
			document.forms.ServerEnabledForm.elements['enabled'].value = enabled;
			document.forms.ServerEnabledForm.submit();
		}
	</script>
	<style type="text/css">
		.enabled_false { font-weight:bold; color:red; }
	</style>
	<link rel="shortcut icon" href="http://welcome.hp-ww.com/img/favicon.ico">
</head>
<body>
	<h1>SPP Remote Server Availability Administration</h1>
	<form method="POST" name="ServerEnabledForm">
		<input type="hidden" name="enabled" />
		<input type="hidden" name="url" />
		<table border="0" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC">
			<tr class="theme">
				<th class="small" scope="col"><span class="themebody">URL</span></th>
				<th class="small" scope="col"><span class="themebody">Server Names</span></th>
				<th class="small" scope="col"><span class="themebody">Enabled</span></th>
				<th class="small" scope="col"><span class="themebody">Action</span></th>
			</tr>
			<c:forEach items="${remoteServers}" var="server" varStatus="status">
				<tr bgcolor="<c:out value="${rowColor[status.index % 2]}" />">
					<td><c:out value="${server.url}" /></td>
					<td>
						<c:forEach items="${server.names}" var="name" varStatus="nameStatus">
							<c:out value="${name}" /><c:if test="${!nameStatus.last}"><br /></c:if>
						</c:forEach>
					</td>
					<td align="center">
						<span class="enabled_<c:out value="${server.enabled}" />"><c:out value="${server.enabled}" /></span>
					</td>
					<td>
						<c:if test="${server.enabled}">
							<input type="button" value="Disable" onclick="submitEnabled('<c:out value="${server.url}" />', '<c:out value="${!server.enabled}" />');" class="primButton"/>
						</c:if>
						<c:if test="${!server.enabled}">
							<input type="button" value="Enable" onclick="submitEnabled('<c:out value="${server.url}" />', '<c:out value="${!server.enabled}" />');" class="primButton"/>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
</body>
</html>
