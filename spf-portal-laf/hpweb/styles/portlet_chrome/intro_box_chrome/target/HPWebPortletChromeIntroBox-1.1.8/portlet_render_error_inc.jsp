<%-----------------------------------------------------------------------------
	portlet_render_error_inc.jsp
	
	This jsp contains the default Vignette portlet render failure display code,
	and can by replaced by portals with custom error display code.  
	
	It is included by 'hpweb_chrome_inc.jsp' within the
	'<vgn-portal:onRenderFailure>' element.

-----------------------------------------------------------------------------%>

<%----------------------------------------------------------------------------
	Imports
-----------------------------------------------------------------------------%>

<%@ page import="com.vignette.portal.portlet.website.PortletRenderException" %>
<%@ page import="com.epicentric.common.website.HtmlUtils" %>

<%----------------------------------------------------------------------------
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<%-- Retrieve error messages from secondary page properties --%>
<% String secondaryPageI18nID = portalContext.getCurrentSecondaryPage().getUID(); %>

<div class="portlet-msg-error">

	<table border="0" cellspacing="0" cellpadding="0" style="margin:1em 0em;">
		<tr>
			<td valign="top" style="padding-right:.5em;">
				<vgn-portal:i18nElement>
					<img src="<%=HtmlUtils.getPath(HtmlUtils.END_USER_IMAGE_PATH) %>/icons/icon_status_info_dimmed_sm.gif"
						title="<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>" key="render_failed_message_icon_title" defaultValue="Unavailable" />"
						alt="<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>" key="render_failed_message_icon_title" defaultValue="Unavailable" />"
						width="20" height="18" align="absmiddle">
				</vgn-portal:i18nElement>
			</td>
			<td class="portlet-font-dim">
				<p>
					<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>"
							key="<%= portletRenderException.getName() %>"
							defaultValue="The service or information you requested is not available at this time. Please try again later.">
						<vgn-portal:i18nParams
								value="<%= portletRenderException.getErrorParameters() %>"/>
					</vgn-portal:i18nValue>
				</p>
				<p style="font-size:smaller;">
					<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>"
							key="error_code" defaultValue="">
						<vgn-portal:i18nParams
								value="<%= new Object[] { portletRenderException.getName() } %>"/>
					</vgn-portal:i18nValue>
				</p>
			</td>
		</tr>
	</table>

</div>
