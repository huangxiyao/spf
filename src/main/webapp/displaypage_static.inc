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

<%-- HPWeb change: set cellspacing 0 instead of 10px --%>
<table border="0" cellspacing="0" cellpadding="0" <%= pageBean.usesPercentageWidths() ? " width=100%" : "" %> height="240">

<%
  if (pageBean.isColumnOriented())
  {
%>
    <%@ include file="displaypage_static_column_oriented.inc" %> 
<%
  }
  else
  {
%>
	<%@ include file="displaypage_static_row_oriented.inc" %> 
<%
  }
%>

</table>