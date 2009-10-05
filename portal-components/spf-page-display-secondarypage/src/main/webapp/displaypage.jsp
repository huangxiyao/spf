<%--
/*######################################################################################
Copyright 1999-2007 Vignette Corporation.  All rights reserved.  This software
is an unpublished work and trade secret of Vignette, and distributed only
under restriction.  This software (or any part of it) may not be used,
modified, reproduced, stored on a retrieval system, distributed, or
transmitted without the express written consent of Vignette.  Violation of
the provisions contained herein may result in severe civil and criminal
penalties, and any violators will be prosecuted to the maximum extent
possible under the law.  Further, by using this software you acknowledge and
agree that if this software is modified by anyone such as you, a third party
or Vignette, then Vignette will have no obligation to provide support or
maintenance for this software.
#####################################################################################*/ 
--%>
<%@ page import="com.epicentric.page.website.PageBean,
				 com.epicentric.page.website.PageDividerBean,
				 com.epicentric.page.website.PagePanelBean,
				 com.epicentric.page.website.PortletPlaceholderBean,
				 com.vignette.portal.portlet.website.PortletWindowBean,
				 com.epicentric.common.website.HtmlUtils,
				 com.epicentric.template.Style,
				 java.util.List,
				 java.util.Iterator"
%>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<%
  if (request.getAttribute("pageBean") != null)
  {
%>
    <jsp:useBean id="pageBean" scope="request" type="com.epicentric.page.website.PageBean" />
    <jsp:useBean id="portletWindowBeans" scope="request" type="java.util.Map" />

    <vgn-portal:defineObjects/>
<%
    String imagePath = HtmlUtils.getPath(HtmlUtils.END_USER_IMAGE_PATH);
    Style currentStyle = portalContext.getCurrentStyle();
    String i18nID = currentStyle.getUID();
%>

    <%@ include file="displaypage_static.inc" %>
<%
  }
%>
