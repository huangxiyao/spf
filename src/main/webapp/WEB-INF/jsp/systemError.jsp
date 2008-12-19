<%-----------------------------------------------------------------------------
	commonError.jsp

	Renders a system error for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page
	import="com.hp.it.spf.htmlviewer.portlet.util.Consts" />

<%---------------------------------------------------------- TAG LIBRARIES --%>

<jsp:directive.include file="include.jsp" />

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />
<jsp:scriptlet>
	String pathToImages = (String)renderRequest.getContextPath() + "/css/html_viewer.css";
</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP ---%>

<link
	href="<%= renderResponse.encodeURL(pathToImages) %>/css/html_viewer.css"
	rel="stylesheet" type="text/css">
<table>
	<tbody>
		<tr>
			<td><spf-i18n-portlet:message key="<%= Consts.ERROR_CODE_INTERNAL %>.message" /></td>
		</tr>
		<tr>
			<td>(<spf-i18n-portlet:message key="error.code.message" /> <c:out
				value="${exception.errorCode}"></c:out>)</td>
		</tr>
	</tbody>
</table>