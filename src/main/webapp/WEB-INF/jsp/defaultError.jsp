<%-----------------------------------------------------------------------------
	defError.jsp

	Renders a default error for the HTMLViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page
	import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" />

<%---------------------------------------------------------- TAG LIBRARIES --%>

<jsp:directive.include file="include.jsp" />

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />
<jsp:scriptlet>
	String pathToCSS = (String)renderRequest.getContextPath() + "/css/html_viewer.css";
</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP ---%>

<link
	href="<%= renderResponse.encodeURL(pathToCSS) %>"
	rel="stylesheet" type="text/css">
<table>
	<tbody>
		<tr>
			<td><p><span class="span.fs-htmlviewer-internal-error-message">
				<spf-i18n-portlet:message key="<%= Consts.ERROR_CODE_INTERNAL %>" />
			</span></p></td>
		</tr>
		<tr>
			<td><p><span class="span.fs-htmlviewer-internal-error-code">
				<spf-i18n-portlet:message key="error.code">
					<spf-i18n-portlet:param value="<%= Consts.ERROR_CODE_INTERNAL %>" />
				</spf-i18n-portlet:message>
			</span></p></td>
		</tr>
	</tbody>
</table>