<%
  List portletPlaceholders = dividerPanel.getPortletPlaceholders();
  int numPortlets = portletPlaceholders.size();
  Iterator portletPlaceholderIterator = portletPlaceholders.iterator();
  while (portletPlaceholderIterator.hasNext())
  {
    PortletPlaceholderBean portletPlaceholder = (PortletPlaceholderBean)portletPlaceholderIterator.next();
    PortletWindowBean portletWindow = (PortletWindowBean)portletWindowBeans.get(portletPlaceholder.getPlaceholderUID());

    request.setAttribute("portletWindow", portletWindow);
%>
    <%@include file="display_portlet.inc" %>
	
    <%-- Add some vertical space between portlets --%>
<%
    if (portletPlaceholder.getPlaceholderIndex() != (numPortlets - 1))
    {
%>
	  <br />
<%
    }
  }
%>
