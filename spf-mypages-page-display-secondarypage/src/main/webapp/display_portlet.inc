<%
  if (portletWindow.isChromeDisplayed())
  {
%>
    <%-- Include Chrome Style To Display Portlet Window --%>
    <vgn-portal:includeStyle friendlyID="chrome" panelUID="<%= dividerPanel.getPanelUID() %>" />
<%
  }
  else
  {
%>
    <%@include file="chromeless_portlet.inc" %>
<%
  }
%>
