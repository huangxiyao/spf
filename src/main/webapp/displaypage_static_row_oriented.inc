   
<%
  List pageDividers = pageBean.getPageDividers();
  int numDividers = pageDividers.size();
  Iterator pageDividerIterator = pageDividers.iterator();
  while (pageDividerIterator.hasNext())
  {
    PageDividerBean pageDivider = (PageDividerBean)pageDividerIterator.next();
%>

    <tr>
	  <td width="100%" valign="top">
	    <table border="0" width="100%" cellpadding="0" cellspacing="0">
		  <tr>
<%
		    int maxPanelWidth = 0;

		    List dividerPanels = pageDivider.getPanels();
			int numPanels = dividerPanels.size();
			Iterator dividerPanelIterator = dividerPanels.iterator();
			while (dividerPanelIterator.hasNext())
			{
			  PagePanelBean dividerPanel = (PagePanelBean)dividerPanelIterator.next();

			  if (maxPanelWidth < dividerPanel.getPanelWidth())
			  {
				  maxPanelWidth = dividerPanel.getPanelWidth();
			  }

  			  // HPWeb change: set flags to apply div to last portlet in row
  			  // only.
  			  boolean applyPortletDivsFlag = false;
  			  boolean applyAllPortletsFlag = false;
		      if (dividerPanel.getPanelIndex() == (numPanels - 1)) {
			    // Last portlet on row
  			  	applyPortletDivsFlag = true;
			  }
%>
			  <%-- HPWeb change:  remove left align attribute --%>
			  <td width=<%= dividerPanel.getPanelWidth() %><%= pageBean.usesPercentageWidths() ? "%" : "" %> valign="top">

			    <%@ include file="displaypage_static_common.inc" %>
				
			  </td>

	          <%-- Add some horizontal space between panels --%>
<%
		      if (dividerPanel.getPanelIndex() != (numPanels - 1))
			  {
%>
			    <td><img src="<%= imagePath %>/misc/spacer.gif" width=10 height=1 border="0" alt=""></td>
<%
			  }
			}  // end panel loop
%>
          </tr>

        <%-- loop to maintain width of absolute-width dividers --%>
<%
        if (pageBean.usesPercentageWidths())
        {
%>
          <tr>
            <td><img src="<%= imagePath %>/misc/spacer.gif" width="<%= maxPanelWidth %>" height="1" border="0" alt=""></td>
          </tr>
<%
        }
%>
      </table>
    </td>
  </tr>

<%
  if (pageDivider.getDividerIndex() != (numDividers - 1))
  {
%>
	<!-- vertical spacer between divs/rows - begins -->
	<!-- vertical spacer between divs/rows - ends -->
<%
	}
  } // end divider loop
%>

