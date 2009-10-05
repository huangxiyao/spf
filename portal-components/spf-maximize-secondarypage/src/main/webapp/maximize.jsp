<%--
/*######################################################################################
Copyright 1999-2007 Vignette Corporation.  All rights reserved.  This software
is an unpublished work and trade secret of Vignette, and distributed only
under restriction.  This software (or any part of it) may not be used,
modified, reproduced, stored on a retrieval system, distributed, or
transmitted without the express written consent of Vignette.  Violation of
the provisions contained herein may result in severe civil and criminal
penalties, and any violators will be prosecuted to the maximum extent
possible under the law.  Further, by using this software you acknowledge and
agree that if this software is modified by anyone such as you, a third party
or Vignette, then Vignette will have no obligation to provide support or
maintenance for this software.
#####################################################################################*/ 
--%>

<%@ page import="com.epicentric.common.website.HtmlUtils" %>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<jsp:useBean id="portletWindow"
			 scope="request"
			 type="com.vignette.portal.portlet.website.PortletWindowBean"/>

<vgn-portal:defineObjects/>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td width="100%">
			<vgn-portal:includeStyle friendlyID="page_controls"/>
		</td>
	</tr>
	<tr>
		<%-- HPWeb change: change to 0px from 15px --%>
		<td width="100%" style="padding:0px">
			<%
				if (portletWindow.isChromeDisplayed()) {
			%>
			<%-- HPWeb change: add <div> --%>
			<%-- Not needed anymore - QC #60 - DSJ 2009/6/2
			<div class="lastColumnPortlet">
			--%>
				<%-- Include Chrome Style To Display Portlet Window --%>
				<vgn-portal:includeStyle friendlyID="chrome"/>
			<%-- HPWeb change: add </div> --%>
			<%-- Not needed anymore - QC #60 - DSJ 2009/6/2
			</div>
			--%>
			<%
			}
			else {
			%>
			<%-- Display the Content Only --%>
			<vgn-portal:renderPortlet>
				<vgn-portal:onRenderSuccess>
					<vgn-portal:insertPortletContent/>
				</vgn-portal:onRenderSuccess>
				<vgn-portal:onRenderFailure>
					<% String secondaryPageI18nID = portalContext.getCurrentSecondaryPage().getUID(); %>
					<table border="0" cellspacing="0" cellpadding="0" style="margin:1em 0em;">
						<tr>
							<td valign="top" style="padding-right:.5em;">
								<vgn-portal:i18nElement><img
										src="<%=HtmlUtils.getPath(HtmlUtils.END_USER_IMAGE_PATH) %>/icons/icon_status_info_dimmed_sm.gif"
										title="<vgn-portal:i18nValue stringID="<%=secondaryPageI18nID%>" key="render_failed_message_icon_title" defaultValue="Unavailable" />"
										alt="<vgn-portal:i18nValue stringID="<%=secondaryPageI18nID%>" key="render_failed_message_icon_title" defaultValue="Unavailable" />"
										width="20" height="18"
										align="absmiddle"></vgn-portal:i18nElement></td>
							<td class="epi-dim">
								<p>
									<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>"
														  key="<%= portletRenderException.getName() %>"
														  defaultValue="The service or information you requested is not available at this time. Please try again later.">
										<vgn-portal:i18nParams
												value="<%= portletRenderException.getErrorParameters() %>"/>
									</vgn-portal:i18nValue>
								</p>
								<p style="font-size:smaller;">
									<vgn-portal:i18nValue stringID="<%= secondaryPageI18nID %>" key="error_code"
														  defaultValue="">
										<vgn-portal:i18nParams
												value="<%= new Object[] { portletRenderException.getName() } %>"/>
									</vgn-portal:i18nValue>
								</p>
							</td>
						</tr>
					</table>
				</vgn-portal:onRenderFailure>
			</vgn-portal:renderPortlet>
			<%
				}
			%>
		</td>
	</tr>
</table>

