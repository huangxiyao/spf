<%-----------------------------------------------------------------------------
	config.jsp

	Renders the config mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<%@ include file="include.jsp" %>
<%@ page import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" %>

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects/>

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= renderResponse.encodeURL("/css/html_viewer.css") %>" rel="stylesheet" type="text/css">
<c:if test="${! empty errorMessage }">
	<p>
		<span class="spf-htmlviewer-config-error-label">
			<spf-i18n-portlet:message key="error.message"/>
		</span>&nbsp;
		<span class="spf-htmlviewer-config-error-message">
			<c:out value="${errorMessage}"/>
		</span>
	</p>
</c:if>
<c:if test="${! empty infoMessage }">
	<p>
		<span class="spf-htmlviewer-config-info-message">
			<c:out value="${infoMessage}"/>
		</span>
	</p>
</c:if>
<form name="htmlViewerConfig" action='<portlet:actionURL/>' method="post">
	<table>
		<tr>
			<td nowrap>
				<spf-help-portlet:classicContextualHelp anchorKey="config.viewfilename" titleKey="config.viewfilename.help.title" contentKey="config.viewfilename.help.content" width="450"/>&nbsp;&nbsp;
			</td>					
			<td>
				<input type="text" name="<%= Consts.VIEW_FILENAME %>" value="<%= renderRequest.getAttribute(Consts.VIEW_FILENAME) %>">
			</td>
		</tr>
		<tr>	
			<td colspan=2>
				<spf-i18n-portlet:message key="config.launchbuttonless">
					<spf-i18n-portlet:classicContextualHelpParam titleKey="config.launchbuttonless.help.title" contentKey="config.launchbuttonless.help.content"/>
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
