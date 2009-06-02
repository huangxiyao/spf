
  <tr>
   
<%
  List pageDividers = pageBean.getPageDividers();
  int numDividers = pageDividers.size();
  Iterator pageDividerIterator = pageDividers.iterator();
  while (pageDividerIterator.hasNext())
  {
    PageDividerBean pageDivider = (PageDividerBean)pageDividerIterator.next();
%>
	<%-- HPWeb change: remove left align attribute --%>
    <td width=<%= pageDivider.getDividerWidth() %><%= pageBean.usesPercentageWidths() ? "%" : "" %> valign="top">
  
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
		  // HPWeb change: added following lines to set flags to
		  // apply div to all portlets in the last column only.

		  boolean applyPortletDivsFlag = false;
		  boolean applyAllPortletsFlag = false;
		  if (pageDivider.getDividerIndex() == (numDividers - 1))
		  {
		      applyPortletDivsFlag = true;
		      applyAllPortletsFlag = true;
		  }

		    List dividerPanels = pageDivider.getPanels();
			int numPanels = dividerPanels.size();
			Iterator dividerPanelIterator = dividerPanels.iterator();
			while (dividerPanelIterator.hasNext())
			{
			  PagePanelBean dividerPanel = (PagePanelBean)dividerPanelIterator.next();
%>   
			  <%@ include file="displaypage_static_common.inc" %>

			  <%-- Add some vertical space between panels --%>
<%
		      if (dividerPanel.getPanelIndex() != (numPanels - 1))
			  {
%>
			    <br/>
<%
			  }
			}  // end panel loop
%>

          </td>
        </tr>
      </table>
    </td>
<%
	if (pageDivider.getDividerIndex() != (numDividers - 1))
	{
%>
      <!-- horizontal spacer between div/column layouts - begins -->

	  <!-- horizontal spacer between div/column layouts - ends -->
<%
	}
  } // end divider loop
%>

  </tr>

  <%-- loop to maintain width of absolute-width dividers --%>
<%
  if (pageBean.usesPercentageWidths())
  {
%>
    <tr>
<%
    pageDividerIterator = pageBean.getPageDividers().iterator();
    while (pageDividerIterator.hasNext())
    {
	  PageDividerBean pageDivider = (PageDividerBean)pageDividerIterator.next();
%>
	  <%-- HPWeb change: set vertical divider to 10px height instead of 1px --%>
      <td><img src="<%= imagePath %>/misc/spacer.gif" width="<%= pageDivider.getDividerWidth() %>" height="10" border="0" alt="">
      </td>
<%
    }
%>
    </tr>
<%
  }
%>
