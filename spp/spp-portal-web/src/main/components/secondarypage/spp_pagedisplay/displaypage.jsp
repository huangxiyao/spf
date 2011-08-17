<%--
Copyright 1999-2004 Vignette Corporation.
All rights reserved.

THIS PROGRAM IS CONFIDENTIAL AND AN UNPUBLISHED WORK AND TRADE
SECRET OF THE COPYRIGHT HOLDER, AND DISTRIBUTED ONLY UNDER RESTRICTION.

EXCEPT AS EXPLICITLY STATED IN A WRITTEN AGREEMENT BETWEEN THE PARTIES,
THE SOFTWARE IS PROVIDED AS-IS, WITHOUT WARRANTIES OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NONINFRINGEMENT,
PERFORMANCE, AND QUALITY.
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
<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator"%>

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
	String hpSpacerPath = HPUrlLocator.getWelcomeUrl(portalContext, false) + "/img/s.gif";
%>

    <%@ include file="displaypage_static.inc" %>
<%
  }
%>
