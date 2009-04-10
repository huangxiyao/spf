<%-----------------------------------------------------------------------------
	config.jsp

	Renders the config mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<%@ include file="include.jsp" %>
<%@ page import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" %>
<%@ page import="com.hp.it.spf.xa.i18n.portlet.I18nUtility" %>

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects/>

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= I18nUtility.getLocalizedFileURL(renderRequest, renderResponse, "/css/html_viewer.css", false) %>" rel="stylesheet" type="text/css">

<table>
	<c:if test="${! empty errorMessages }">
		<c:forEach var="errorMessage" items="${errorMessages}">
			<tr>
				<td>
					<span class="spf-htmlviewer-config-error-label">
						<spf-i18n-portlet:message key="error.message"/>&nbsp;
					</span>
					<span class="spf-htmlviewer-config-error-message">
						<c:out value="${errorMessage}" escapeXml="false"/><br>
					</span>
				</td>
			</tr>
		</c:forEach>
	</c:if>
	<c:if test="${! empty warnMessages }">
		<c:forEach var="warnMessage" items="${warnMessages}">
			<tr>
				<td>
					<span class="spf-htmlviewer-config-warning-label">
						<spf-i18n-portlet:message key="warn.message"/>&nbsp;
					</span>
					<span class="spf-htmlviewer-config-warning-message">
						<c:out value="${warnMessage}" escapeXml="false"/><br>
					</span>
				</td>
			</tr>
		</c:forEach>
	</c:if>
	<c:if test="${! empty infoMessages }">
		<c:forEach var="infoMessage" items="${infoMessages}">
			<tr>
				<td>
					<span class="spf-htmlviewer-config-info-message">
						<c:out value="${infoMessage}" escapeXml="false"/>
					</span>
				</td>
			</tr>
		</c:forEach>
	</c:if>
</table>
<p></p>
<form name="htmlViewerConfig" action='<portlet:actionURL/>' method="post">
	<table>
		<tr>
			<td nowrap>
				<span class="spf-htmlviewer-config-label">
					<spf-help-portlet:classicContextualHelp anchorKey="config.viewfilename" titleKey="config.viewfilename.help.title" contentKey="config.viewfilename.help.content"/>&nbsp;&nbsp;
				</span>
			</td>					
			<td>
				<input type="text" name="<%= Consts.VIEW_FILENAME %>" value="<%= renderRequest.getAttribute(Consts.VIEW_FILENAME) %>">
			</td>
		</tr>
		<tr>
			<td nowrap>
				<span class="spf-htmlviewer-config-label">
					<spf-i18n-portlet:message key="config.subsfilename">
						<spf-i18n-portlet:classicContextualHelpParam titleKey="config.subsfilename.help.title" contentKey="config.subsfilename.help.content"/>
					</spf-i18n-portlet:message>&nbsp;&nbsp;
				</span>
			</td>					
			<td>
				<input type="text" name="<%= Consts.INCLUDES_FILENAME %>" value="<%= renderRequest.getAttribute(Consts.INCLUDES_FILENAME) %>">
			</td>
		</tr>
		<tr>	
			<td colspan=2>
				<span class="spf-htmlviewer-config-label">
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
				</span>
			</td>			
		</tr>
		<tr>	
			<td>
				&nbsp;
			</td>			
		</tr>		
		<tr>	
			<td>
				<span class="spf-htmlviewer-config-button">
					<input type="submit" value='<spf-i18n-portlet:message key="config.okbutton"/>'>
				</span>
			</td>			
		</tr>
	</table>
</form>
