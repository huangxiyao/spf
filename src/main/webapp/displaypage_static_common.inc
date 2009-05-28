<%
  List portletPlaceholders = dividerPanel.getPortletPlaceholders();
  int numPortlets = portletPlaceholders.size();
  Iterator portletPlaceholderIterator = portletPlaceholders.iterator();
  while (portletPlaceholderIterator.hasNext())
  {
    PortletPlaceholderBean portletPlaceholder = (PortletPlaceholderBean)portletPlaceholderIterator.next();
    PortletWindowBean portletWindow = (PortletWindowBean)portletWindowBeans.get(portletPlaceholder.getPlaceholderUID());

    request.setAttribute("portletWindow", portletWindow);

	// HPWeb change: add <div> element if the applyPortletDivsFlag is true
	// and if the applyAllPortletsFlag is true, or it is the last portlet.
    if (applyPortletDivsFlag &&
		(applyAllPortletsFlag ||
    	(portletPlaceholder.getPlaceholderIndex() == (numPortlets - 1))))
	{
%>
	<div class="lastColumnPortlet">
<%
	}
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

	// HPWeb change: add closing <div>
    if (applyPortletDivsFlag &&
		(applyAllPortletsFlag ||
    	(portletPlaceholder.getPlaceholderIndex() == (numPortlets - 1))))
	{

%>
	</div>
<%
	}
  }
%>
