<%-----------------------------------------------------------------------------
	commonError.jsp

	Renders a system error for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<%@ include file="include.jsp" %>
<%@ page import="com.hp.it.spf.htmlviewer.portlet.util.Consts" %>
<%@ page import="com.hp.it.spf.xa.exception.portlet.ExceptionUtil" %>
<%@ page import="com.hp.it.spf.xa.i18n.portlet.I18nUtility" %>

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />
<%
	String errorCode = ExceptionUtil.getErrorCode(renderRequest, Consts.ERROR_CODE_INTERNAL);
	String errorMessage = ExceptionUtil.getLocalizedMessage(renderRequest, Consts.ERROR_CODE_INTERNAL);
%>

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= I18nUtility.getLocalizedFileURL(renderRequest, renderResponse, "/css/html_viewer.css", false) %>" rel="stylesheet" type="text/css">

<table>
	<tbody>
		<tr>
			<td>
				<p><span class="spf-htmlviewer-internal-error-message">
				<%= errorMessage %>
				</span></p>
				<p><span class="spf-htmlviewer-internal-error-code">
				<spf-i18n-portlet:message key="error.code">
					<spf-i18n-portlet:param value="<%= errorCode %>" />
				</spf-i18n-portlet:message>
				</span></p>
			</td>
		</tr>
	</tbody>
</table>