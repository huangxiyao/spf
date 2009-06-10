<%
  List pageDividers = pageBean.getPageDividers();
  Iterator pageDividerIterator = pageDividers.iterator();
  while (pageDividerIterator.hasNext())
  { // begin row loop
    PageDividerBean pageDivider = (PageDividerBean)pageDividerIterator.next();
%>
  <tr>
    <td width="100%" valign="top">
      <table border="0" width="100%" cellpadding="0" cellspacing="0">
        <tr>
<%
	int maxPanelWidth = 0;

	// Reset flag for indicating at least one portlet is enabled in this panel.
	boolean portletEnabledFlag = false;

    List dividerPanels = pageDivider.getPanels();
	Iterator dividerPanelIterator = dividerPanels.iterator();
	while (dividerPanelIterator.hasNext())
	{ // begin panel loop
		PagePanelBean dividerPanel = (PagePanelBean)dividerPanelIterator.next();
		if (maxPanelWidth < dividerPanel.getPanelWidth())
		{
			maxPanelWidth = dividerPanel.getPanelWidth();
		}

	    // Add right padding to all but the rightmost column.
		int dividerPanelPadding = 10;
		if (!dividerPanelIterator.hasNext())
			dividerPanelPadding = 0;
%>
          <td style="padding-right: <%= dividerPanelPadding %>px;" width=<%= dividerPanel.getPanelWidth() %><%= pageBean.usesPercentageWidths() ? "%" : "" %> valign="top">
            <%@ include file="displaypage_static_common.inc" %>
          </td>
<%
	} // end panel loop
%>
        </tr>
      </table>
    </td>
  </tr>
<%
	// Add vertical spacer between rows if there is another row coming up and at least
	// one portlet was enabled in this row.
	if (pageDividerIterator.hasNext() && portletEnabledFlag)
	{
%>
  <tr>
    <td>
      <br>
    </td>
  </tr>
<%
	}
  } // end row loop
%>
