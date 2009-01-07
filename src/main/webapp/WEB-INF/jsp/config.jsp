<%-----------------------------------------------------------------------------
	config.jsp

	Renders the config mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" />

<%---------------------------------------------------------- TAG LIBRARIES --%>

<jsp:directive.include file="include.jsp" />

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects/>
<jsp:scriptlet>
	String pathToImages = (String) renderRequest.getContextPath() + "/css/html_viewer.css";
</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= renderResponse.encodeURL(pathToImages) %>" rel="stylesheet"
	type="text/css">
<c:if test="${! empty errorMessage }">
	<span class="fs-htmlviewer-error-message">
		<%= renderRequest.getAttribute(Consts.ERROR_MESSAGE) %>
	</span>
	<br>
</c:if>
<form name="htmlViewerConfig" action='<portlet:actionURL/>' method="post">
	<table>
		<tr>
			<td nowrap>
				<spf-help-portlet:contextualHelp anchorKey="config.viewfilename" titleKey="config.viewfilename.help.title" contentKey="config.viewfilename.help.content"/>&nbsp;&nbsp;
			</td>					
			<td>
				<input type="text" name="<%= Consts.VIEW_FILENAME %>" value="<%= renderRequest.getAttribute(Consts.VIEW_FILENAME) %>">
			</td>
		</tr>
		<tr>	
			<td colspan=2>
				<spf-i18n-portlet:message key="config.launchbuttonless">
					<spf-i18n-portlet:contextualHelpParam titleKey="config.launchbuttonless.help.title" contentKey="config.launchbuttonless.help.content"/>
				</spf-i18n-portlet:message>&nbsp;&nbsp;
				<c:choose>
					<c:when test="${! empty launchButtonless && launchButtonless == 'true' }">
				    	<input type="checkbox" name="<%= Consts.LAUNCH_BUTTONLESS %>" value="<%= Consts.LAUNCH_BUTTONLESS %>" checked>
					</c:when>
					<c:otherwise>
						<input type="checkbox" name="<%= Consts.LAUNCH_BUTTONLESS %>" value="<%= Consts.LAUNCH_BUTTONLESS %>">
					</c:otherwise>
				</c:choose>
			</td>			
		</tr>
		<tr>	
			<td>
				&nbsp;
			</td>			
		</tr>		
		<tr>	
			<td>
				<input type="submit" value='<spf-i18n-portlet:message key="config.okbutton"/>'>
			</td>			
		</tr>
	</table>
</form>
