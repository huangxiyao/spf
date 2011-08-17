<%
  if (portletWindow.isChromeDisplayed())
  { 
 
%>
    <%-- Include Chrome Style To Display Portlet Window --%>
    <%--START - Added for Padding between the Vignette elements (GPP6.0 requirement) --%>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	   <tr>
		<td style="padding-left:<%=portletWindow.getPortletProperty("spacing.left")%>px;
			   padding-right:<%=portletWindow.getPortletProperty("spacing.right")%>px;
			   padding-top:<%=portletWindow.getPortletProperty("spacing.top")%>px;
			   padding-bottom:<%=portletWindow.getPortletProperty("spacing.bottom")%>px;">
			<vgn-portal:includeStyle friendlyID="chrome" panelUID="<%= dividerPanel.getPanelUID() %>" />
		 </td>
	   </tr>
	 </table>
	<%--END - Added for Padding between the Vignette elements (GPP6.0 requirement) --%>

<%
  }
  else
  {
%>
    <%@include file="chromeless_portlet.inc" %>
<%
  }
%>
