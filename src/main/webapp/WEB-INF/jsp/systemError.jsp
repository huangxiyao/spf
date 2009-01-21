<%-----------------------------------------------------------------------------
	commonError.jsp

	Renders a system error for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page
	import="com.hp.it.spf.xa.i18n.portlet.I18nUtility" />
<jsp:directive.page
	import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" />
<jsp:directive.page
	import="com.hp.it.spf.xa.interpolate.portlet.web.FileInterpolatorController" />

<%---------------------------------------------------------- TAG LIBRARIES --%>

<jsp:directive.include file="include.jsp" />

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />
<jsp:scriptlet>
	String errorCode = (String)renderRequest.getAttribute(FileInterpolatorController.REQUEST_ATTR_ERROR_CODE);
	if (errorCode == null) 
		errorCode = Consts.ERROR_CODE_INTERNAL;
	String errorMessage = (String)renderRequest.getAttribute(FileInterpolatorController.REQUEST_ATTR_ERROR_MESSAGE);
	if (errorMessage == null)
		errorMessage = I18nUtility.getMessage(renderRequest, Consts.ERROR_CODE_INTERNAL);	
</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= renderResponse.encodeURL("/css/html_viewer.css") %>" rel="stylesheet" type="text/css">
<table>
	<tbody>
		<tr>
			<td><p><span class="spf-htmlviewer-internal-error-message">
				<%= errorMessage %>
			</span></p></td>
		</tr>
		<tr>
			<td><p><span class="spf-htmlviewer-internal-error-code">
				<spf-i18n-portlet:message key="error.code">
					<spf-i18n-portlet:param value="<%= errorCode %>" />
				</spf-i18n-portlet:message>
			</span></p></td>
		</tr>
	</tbody>
</table>