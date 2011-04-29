<%--
Copyright 1999-2003 Vignette Corporation.
All rights reserved.
 
THIS PROGRAM IS CONFIDENTIAL AND AN UNPUBLISHED WORK AND TRADE
SECRET OF THE COPYRIGHT HOLDER, AND DISTRIBUTED ONLY UNDER RESTRICTION. 

EXCEPT AS EXPLICITLY STATED IN A WRITTEN AGREEMENT BETWEEN THE PARTIES,
THE SOFTWARE IS PROVIDED AS-IS, WITHOUT WARRANTIES OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NONINFRINGEMENT,
PERFORMANCE, AND QUALITY.
--%>

<vgn-portal:includeStyle friendlyID="page_controls"/>

<!-- module layout - begins -->
<table border="0" cellspacing="15" cellpadding="0" width="100%" height="240">
  <tr>

<%
    Iterator pageDividerIterator = pageBean.getPageDividers().iterator();
	while (pageDividerIterator.hasNext())
	{
	  PageDividerBean pageDivider = (PageDividerBean)pageDividerIterator.next();
%>
      <td width="<%= pageDivider.getDividerWidth() %>%" valign="top" align="left">
<%--
  the nested table that follows this line is necessary for the case when
  an absolute-defined panel has at least two modules in it, and is set
  to a value smaller than either module's minimum width.  it is a bug in IE
  that the smaller of those two modules will display at its minimum chrome
  width, *NOT* at the 100% of the panel (which will be defined by the minimum
  chrome width of the wider module.  having this nested table causes both
  modules to display at 100% of that width, which looks much better.
--%>
        <table cellspacing="0" cellpadding="0" border="0" width="100%">
		  <tr>
		    <td>
<%
			  List dividerPanels = pageDivider.getPanels();
			  int numPanels = dividerPanels.size();
		      Iterator dividerPanelIterator = dividerPanels.iterator();
			  while (dividerPanelIterator.hasNext())
			  {
				PagePanelBean dividerPanel = (PagePanelBean)dividerPanelIterator.next();

				List portletPlaceholders = dividerPanel.getPortletPlaceholders();
				int numPortletplaceholders = portletPlaceholders.size();
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
				  if (portletPlaceholder.getPlaceholderIndex() != (numPortletplaceholders - 1))
				  {
%>
			        <br />
<%
				  }

				} // end of portlet place holder loop
%>

			    <%-- Add some vertical space between panels --%>
<%
				if (dividerPanel.getPanelIndex() != (numPanels - 1))
				{
%>
			      <br/>
<%
				}
			  }  // end of panel loop
%>

		    </td>
          </tr>
        </table>
      </td>
<%
	} // end of divider loop
%>
  </tr>
</table>


