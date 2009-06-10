  <tr>
<%
  List pageDividers = pageBean.getPageDividers();
  int numDividers = pageDividers.size();
  Iterator pageDividerIterator = pageDividers.iterator();
  while (pageDividerIterator.hasNext())
  { // begin column loop
    PageDividerBean pageDivider = (PageDividerBean)pageDividerIterator.next();
    
    // Add right padding to all but the rightmost column.
    int pageDividerPadding = 10;
    if (pageDivider.getDividerIndex() == (numDividers - 1))
    	pageDividerPadding = 0;
%>
    <td style="padding-right: <%= pageDividerPadding %>px;" width=<%= pageDivider.getDividerWidth() %><%= pageBean.usesPercentageWidths() ? "%" : "" %> valign="top">
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
	Iterator dividerPanelIterator = dividerPanels.iterator();
	while (dividerPanelIterator.hasNext())
	{ // begin panel loop
		PagePanelBean dividerPanel = (PagePanelBean)dividerPanelIterator.next();

		// Reset flag for indicating at least one portlet has displayed in this panel.
		boolean portletEnabledFlag = false;
%>   
            <%@ include file="displaypage_static_common.inc" %>
<%
		// Add vertical spacer between panels if there is another coming up and at least
		// one portlet was enabled in this one.
		if (dividerPanelIterator.hasNext() && portletEnabledFlag)
		{
%>
            <br>
<%
		}
	}  // end panel loop
%>
          </td>
        </tr>
      </table>
    </td>
<%
  } // end column loop
%>
  </tr>
<%
  // loop to maintain width of absolute-width dividers
  // Vignette OOB had this -- do we still need it?  DSJ 2009/Jun/9
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
    <td>
      <img src="<%= imagePath %>/misc/spacer.gif" width="<%= pageDivider.getDividerWidth() %>" height="1" border="0" alt="">
    </td>
<%
    }
%>
  </tr>
<%
  }
%>
