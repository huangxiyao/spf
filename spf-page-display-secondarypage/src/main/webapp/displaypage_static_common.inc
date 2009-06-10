<%
  List portletPlaceholders = dividerPanel.getPortletPlaceholders();
  Iterator portletPlaceholderIterator = portletPlaceholders.iterator();
  while (portletPlaceholderIterator.hasNext())
  { // begin portlet loop
    PortletPlaceholderBean portletPlaceholder = (PortletPlaceholderBean)portletPlaceholderIterator.next();
    PortletWindowBean portletWindow = (PortletWindowBean)portletWindowBeans.get(portletPlaceholder.getPlaceholderUID());

    request.setAttribute("portletWindow", portletWindow);
%>
    <%@include file="display_portlet.inc" %>
<%
	// Add some vertical space between portlets
    if (portletPlaceholderIterator.hasNext())
    {
%>
	<br/>
<%
    }

    // Set flag indicating we have printed at least one portlet in this panel.
    portletEnabledFlag = true;
  } // end portlet loop
%>
