<%-- Display the Content Only --%>
<vgn-portal:renderPortlet>
	<vgn-portal:onRenderSuccess>
		<vgn-portal:insertPortletContent/>
	</vgn-portal:onRenderSuccess>
	<vgn-portal:onRenderFailure>
		<% String secondaryPageI18nID = portalContext.getCurrentSecondaryPage().getUID(); %>
		<table border="0" cellspacing="0" cellpadding="0" style="margin:1em 0em;">
			<tr>
				<td valign="top" style="padding-right:.5em;"><vgn-portal:i18nElement><img
						src="<%=HtmlUtils.getPath(HtmlUtils.END_USER_IMAGE_PATH) %>/icons/icon_status_info_dimmed_sm.gif"
						title="<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>" key="render_failed_message_icon_title" defaultValue="Unavailable" />"
						alt="<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>" key="render_failed_message_icon_title" defaultValue="Unavailable" />"
						width="20" height="18" align="absmiddle"></vgn-portal:i18nElement></td>
				<td class="epi-dim">
					<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>"
										  key="<%= portletRenderException.getName() %>"
										  defaultValue="An error has occurred.  This portlet is unavailable at this time.">
						<vgn-portal:i18nParams
								value="<%= portletRenderException.getErrorParameters() %>"/>
					</vgn-portal:i18nValue>
					<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>" key="error_code" defaultValue="">
						<vgn-portal:i18nParams
								value="<%= new Object[] { portletRenderException.getName() } %>"/>
					</vgn-portal:i18nValue>
				</td>
			</tr>
		</table>
	</vgn-portal:onRenderFailure>
</vgn-portal:renderPortlet>