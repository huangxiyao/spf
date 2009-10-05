<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.Map"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<portlet:defineObjects />
<p>
    Portlet mode: <strong><%=renderRequest.getPortletMode()%></strong>
</p>
<p>
    Window state: <strong><%=renderRequest.getWindowState()%></strong>
</p>
<p>
	Currently set render parameters:
	<table>
		<tr>
			<th>Name</th>
			<th>Value</th>
		</tr>
		<% for (Map.Entry<String, String[]> param : renderRequest.getParameterMap().entrySet()) { %>
		<tr>
			<td><%=param.getKey()%></td>
			<td><%=Arrays.asList(param.getValue())%></td>
		</tr>
		<% } %>
	</table>
</p>

