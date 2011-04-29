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
<%@ page
	import="com.epicentric.page.website.PageBean,
			com.epicentric.page.website.PageDividerBean,
			com.epicentric.page.website.PagePanelBean,
			com.epicentric.page.website.PortletPlaceholderBean,
			com.vignette.portal.portlet.website.PortletWindowBean,
			com.epicentric.common.website.HtmlUtils,
			java.util.Iterator,
			java.util.List"
%>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<jsp:useBean id="pageBean" scope="request" type="com.epicentric.page.website.PageBean" />
<jsp:useBean id="portletWindowBeans" scope="request" type="java.util.Map" />

<vgn-portal:defineObjects/>

<%@ include file="my_pages_display_static.inc" %>    



