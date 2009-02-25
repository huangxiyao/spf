<%-----------------------------------------------------------------------------
	defError.jsp

	Renders a default error for the HTMLViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<%@ include file="include.jsp" %>
<%@ page import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" %>

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= renderResponse.encodeURL("/css/html_viewer.css") %>" rel="stylesheet" type="text/css">
<table>
	<tbody>
		<tr>
			<td><p><span class="spf-htmlviewer-internal-error-message">
				<spf-i18n-portlet:message key="<%= Consts.ERROR_CODE_INTERNAL %>" />
			</span></p></td>
		</tr>
		<tr>
			<td><p><span class="spf-htmlviewer-internal-error-code">
				<spf-i18n-portlet:message key="error.code">
					<spf-i18n-portlet:param value="<%= Consts.ERROR_CODE_INTERNAL %>" />
				</spf-i18n-portlet:message>
			</span></p></td>
		</tr>
	</tbody>
</table>