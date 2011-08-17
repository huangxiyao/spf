 <%
/*
* Copyright 1999-2002 by Epicentric, Inc.,
* All rights reserved.
*
* This software is the confidential and proprietary information
* of Epicentric, Inc. ("Confidential Information").  You
* shall not disclose such Confidential Information and shall use
* it only in accordance with the terms of the license agreement
* you entered into with Epicentric.
*/
 %>

<%@ page
	import="java.util.*,
		com.epicentric.portalbeans.*,
		com.epicentric.common.*,
		com.epicentric.common.website.*,
		com.epicentric.permission.*,
		com.epicentric.components.PortalServicesComponent,
		com.epicentric.portalbeans.beans.bookmarkbean.*"

	contentType="text/html; charset=UTF-8"
%>
<%@taglib uri="epi-tags" prefix="epi_html"%>
<%@taglib uri="module-tags" prefix="mod"%>

<mod:view class="com.epicentric.portalbeans.beans.bookmarkbean.BookmarkBeanCommonView">

<%
	// Variables

	BookmarkBean bean = (BookmarkBean) view.getBean();

	String pathToJSPDir = PortalServicesComponent.getInstance()._getPortalHttpRoot()+view.getJSPDirectory();
	String bookmark_graphic = "bookmark.gif";

	// Upgrade i18n keys, the data structure and data
	view.updateKeys();
	if(!SessionUtils.getCurrentUser(session).isGuestUser()){
		if (!view.isSessionEditViewUpgraded()) {
			if (!view.isSessionAdminViewUpgraded()) {
				BookmarkUpgrader.upgradeAdminFolder(view);
			}
			BookmarkUpgrader.upgradeUserFolder(view);
			view.setIsSessionEditViewUpgraded(true);
		}
	}
	
	String actionUrl = bean.getFullViewURL("MY_PORTAL_VIEW");
	actionUrl = actionUrl.replaceFirst("&javax.portlet.prp_","_ws_MX_pm_ED&javax.portlet.prp_");
	//String actionUrl = bean.getViewURL("USER_BEAN_EDIT_VIEW");

	if(!view.allowDuplicates()){
		view.upgradeOptionalBookmarks();
	}

	//
	// Organizer used in:
	// 1) getting the defaults for empty user bookmarks.
	// 2) retrieving the required bookmarks.
	//
	BookmarkOrganizer organizer = bean.getBookmarkOrganizer();

	UserBookmarkOrganizer userOrganizer = view.getUserOrganizer();

	com.epicentric.metastore.MetaStoreFolder folder = view.getFolder();
	com.epicentric.metastore.MetaStoreFolder adminFolder = bean.getAdminFolder();

	//
	// use the properties but do not save them to the metastore.
	//
	String popup = folder.getProperty("popup");
	String targetWindow = "";

	if (StringUtils.isEmpty(popup))	{
		popup = bean.getAdminProperty("popup");
		if (StringUtils.isEmpty(popup)) {
			popup = "off";
		}
	}
	if (popup.equalsIgnoreCase("on"))	{
		targetWindow = "My Bookmarks";
	}

	String beginStr="<a href = \"";
	String endStr="\" target=\"" + targetWindow +"\"\\>";

	Bookmark bookmark;
	Vector requireds = null;

	if (organizer != null)	{
		requireds = organizer.getRequiredBookmarks();
	}

	if (requireds != null && !requireds.isEmpty())		{

		for (Enumeration e = requireds.elements(); e.hasMoreElements();)	{
			bookmark = (Bookmark)e.nextElement();
			String strName = bookmark.getName();
			int nIndex = strName.indexOf("&quot;");
			if (nIndex!=-1)
			{
				String first = strName.substring(0, nIndex);
				String last = strName.substring(nIndex+6);
				strName = first + "\"" + last;
			}
			// updating bookmark key in the event that this is the old module w/o the "Bookmark_" pretext
			//view.updateKeys(bookmark);
%>
	<%= I18nUtils.getValue(bean.getUID(),"Bookmark_"+bookmark.getBookmarkID(),bookmark.getURLString(),beginStr,endStr,session,request)%>
	<img src="<%= pathToJSPDir %>/images/<%= bookmark_graphic %>" border="0" width="9" height="9" alt="<%=strName%>" title="<%=strName%>"></a>
	<%= I18nUtils.getValue(bean.getUID(),"Bookmark_"+bookmark.getBookmarkID(),bookmark.getURLString(),beginStr,endStr,session,request)%>
	<mod:i18nValue alias="instance" key='<%= "Bookmark_"+strName%>' defaultValue="<%= strName%>" /></a>
<%
		}

	}
	Vector optionals = (userOrganizer != null) ? userOrganizer.getBookmarks() : organizer.getOptionalBookmarks();
	//boolean editable = EndUserUtils.isEditable(context, bean);

	if (optionals != null && !optionals.isEmpty()) {

		for (Enumeration e = optionals.elements(); e.hasMoreElements();) {
			bookmark = (Bookmark)e.nextElement();
			String strName = bookmark.getName();
			int nIndex = strName.indexOf("&quot;");
			if (nIndex!=-1)
			{
				String first = strName.substring(0, nIndex);
				String last = strName.substring(nIndex+6);
				strName = first + "\"" + last;
			}
%>

				<%= "&raquo;" + beginStr + bookmark.getURLString()+endStr%>
				<%=strName%></a>
				<br />
<%
		}
	}  // if no optional bookmarks & editable
	else if (requireds == null || requireds.isEmpty())	{
		%><mod:i18nValue alias="type" key="MY_PORTAL_VIEW.no_sites_message_text" defaultValue="There are no saved sites"/><br />
<%
	}
%>
<%
String edit_Text = I18nUtils.getValue(bean.getUID(), "edit_text", "Edit my quick links", session, request);
%>
<br>&raquo; <a href="<%=actionUrl%>"><%=edit_Text%></a><br><br>

</mod:view>
