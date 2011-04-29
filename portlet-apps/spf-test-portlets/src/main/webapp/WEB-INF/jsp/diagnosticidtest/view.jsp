<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<portlet:defineObjects />
<%
	String httpRequestDiagId = com.hp.it.spf.xa.misc.Utils.getDiagnosticId(request);
	String portletRequestDiagId = com.hp.it.spf.xa.misc.portlet.Utils.getDiagnosticId(renderRequest);
	String testResult = (httpRequestDiagId != null && httpRequestDiagId.equals(portletRequestDiagId) ? "ok" : "failed");
%>
<style type="text/css">
	#test th { text-align: left }
	#test .ok { color: white; background-color: green; }
	#test .failed { color: black; background-color: red; }
</style>
<p>
	This portlet tests the availability of Diagnostic ID using spf-common-utilities' <em>Utils.getDiagnosticId(HttpServletRequest)</em>
	and spf-portlet-utilities' <em>Utils.getDiagnosticId(PortletRequest)</em>.
</p>
<hr />
<table id="test">
	<tr>
		<th>Diagnostic ID in HttpServletRequest</th>
		<td><%=httpRequestDiagId%></td>
	</tr>
	<tr>
		<th>Diagnostic ID in PortletRequest</th>
		<td><%=portletRequestDiagId%></td>
	</tr>
	<tr>
		<th>Test Result</th>
		<td class="<%=testResult%>"><%=testResult%></td>
	</tr>
</table>
<% if (!"ok".equals(testResult)) { %>
<hr />
<p>If you are running this portlet as <em>local portlet</em> make sure that SPF secondary page
(page display or maximize) is used to render this portlet and that you have MapAttributeFilter
portlet filter declared for this portlet.</p>
<p>If you are running this portlet as <em>remote portlet</em> the consumer invoking
the portlet did not send SPF_DC_ID HTTP header.</p>
<% } %>
