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

<%@ page import="java.util.*,
                 javax.servlet.http.*,
                 com.epicentric.common.*,
                 com.epicentric.common.website.*,
                 com.epicentric.metastore.*,
                 com.epicentric.portalbeans.*,
                 com.epicentric.portalbeans.beans.bookmarkbean.*"

contentType="text/html; charset=UTF-8" %>

<%@taglib uri="epi-tags" prefix="epi_html" %>
<%@taglib uri="module-tags" prefix="mod" %>
<mod:view class="com.epicentric.portalbeans.beans.bookmarkbean.BookmarkBeanCommonView">

<%

	// updating bookmark key in the event that this is the old module w/o the "Bookmark_" pretext
	view.updateKeys();

	int numExtras = 3;

	// Upgrade the data structure and the data

	if (!view.isSessionAdminViewUpgraded()) {
		BookmarkUpgrader.upgradeAdminFolder(view);
		view.setIsSessionAdminViewUpgraded(true);
	}



	// Access metastore information
	BookmarkBean bean = (BookmarkBean)view.getBean();
	BookmarkOrganizer organizer = bean.getBookmarkOrganizer();
	//System.out.println("ORGANIZER"+organizer);
	String popup = bean.getAdminProperty("popup");

	// sets the checkbox property.
	String checkboxProperty = "";	// for the "checked" property of checkbox field
	if (popup.equalsIgnoreCase("on") == true)	{
		checkboxProperty = "checked=\"checked\"";
	}


%>

<form method="post" action="<%= bean.getBaseViewURL(PortalBeanView.ADMIN_BEAN_EDIT_PROCESS_VIEW) %>" style="display:inline; margin-bottom:0px;">
<table border="0" cellpadding="0" cellspacing="0">
<tr>
<td class="epi-trailOn">Details</td>
</tr>
<tr>
<td>
<hr size="1" noshade="noshade" />
</td>
</tr>
<tr>
<td colspan="3" style="padding-bottom: 10px"><mod:i18nValue alias="type" key="ADMIN_BEAN_EDIT_VIEW.description" defaultValue="Create default and/or required links by providing a friendly name that will appear on the main view of the module, and a URL for each default Web site."/><br /><br />
<mod:i18nValue alias="type" key="ADMIN_BEAN_EDIT_VIEW.note_message_text" defaultValue="Note" />:
<mod:i18nValue alias="type" key="ADMIN_BEAN_EDIT_VIEW.required_message_text" defaultValue="Non-required links will not be added to modules which are already in use by end users.  To force a link on to the existing module, mark the link as Required." />
</td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
<tr>
<td>
<table border="0" cellpadding="0" cellspacing="0" class="epi-dataTable" summary="This table contains a list of links.">
<tr>
<th scope="col" align="left"><b><mod:i18nValue alias='type' key='_global.name_textbox_label' defaultValue='Name'/></b></th>
<th scope="col" align="left"><b><mod:i18nValue alias="type" key="_global.URL_textbox_label" defaultValue="URL"/></b></th>
<th scope="col" align="center"><b><mod:i18nValue alias="type" key="ADMIN_BEAN_EDIT_VIEW.required_checkbox_label" defaultValue="Required"/></b></th>
</tr>
<%
	int rowIndex = 0;
	Vector bookmarks = organizer.getBookmarks();
	Bookmark bookmark;
	int i;

	for (i = 0; i < bookmarks.size(); i++)	{
		bookmark = (Bookmark)bookmarks.elementAt(i);
		if (rowIndex%2==0) {
%>
<tr  class="epi-rowOdd">
		<% } else { %>
		<tr  class="epi-rowEven">
		<%
		}
		String beginStr="<input size=\"16\" name=\"name"+ Integer.toString(i) +"\" id=\"name"+ Integer.toString(i) +"\" value=\"";
		String endStr="\" class=\"epi-input\" style=\"width:200px\"/>";
		String strName = bookmark.getName();
		int nIndex = strName.indexOf("&quot;");
		if (nIndex!=-1)
		{
			String first = strName.substring(0, nIndex);
			String last = strName.substring(nIndex+6);
			strName = first + "\"" + last;
		}


		%>
		<td align="right" scope="row"><%= I18nUtils.getValue(bean.getUID(),"Bookmark_"+strName,strName,beginStr,endStr, session,request)%></td>		<%beginStr="<input size=\"16\" name=\"url"+ Integer.toString(i) +"\" id=\"url"+ Integer.toString(i) +"\" value=\"";%>
		<td align="left"><%= I18nUtils.getValue(bean.getUID(),"Bookmark_"+bookmark.getBookmarkID(),bookmark.getURLString(),beginStr,endStr,session,request) %></td>
		<td align="center"><input type="checkbox" name="required<%= i %>" id="required<%= i %>"
					<% if (bookmark.getIsRequiredString().equalsIgnoreCase("on")) out.print("checked=\"checked\""); %> /></td>
					</tr>
		<%
		rowIndex++;
	}


			int j;
			for (j = 0; j < numExtras; j++)	{
				if (rowIndex%2==0) {
		%>
		<tr  class="epi-rowOdd">
		<% } else { %>
		<tr  class="epi-rowEven">
		<%}%>
		<td align="right" scope="row"><input size="16" name="name<%= i+j %>" id="name<%= i+j %>" class="epi-input" style="width:200px" /></td>
		<td align="left"><input size="16" name="url<%= i+j %>" id="url<%= i+j %>"  class="epi-input" style="width:200px" /></td>
		<td align="center"><input type="checkbox" name="required<%= i+j %>" id="required<%= i+j %>" /></td>
		</tr>
	<%
				rowIndex++;
			}
	%>
	</table>
	</td>
	</tr>
	<tr>
	<td colspan="3">
	<input name="popup" id="open_new_window" type="checkbox" <%= checkboxProperty %> />&nbsp;<label for="open_new_window"><b><mod:i18nValue alias="type" key="ADMIN_BEAN_EDIT_VIEW.new_browser_window_message_text" defaultValue="By default, open Web sites in new browser window"/></b></label></td>
	</tr>
	<tr>
	<td colspan="3"><hr size="1" noshade="noshade" /></td>
	</tr>
	<tr>
	<td colspan="3">
		<epi_html:i18nElement><input class="epi-button" type="submit" name="update" id="update" value="<mod:i18nValue alias="type" key="_global.update_button_label" defaultValue=" Save "/>" /></epi_html:i18nElement>
		<epi_html:i18nElement><input class="epi-button" type="submit" name="cancel" id="cancel" value="<mod:i18nValue alias="type" key="_global.reset_button_label" defaultValue="Reset"/>" /></epi_html:i18nElement>

		<!-- takes the total of two, counting the worse case scenario -->
		<input type="hidden" name="count" id="count" value="<%=i+j%>" />

		<input type="hidden" name="beanID" id="beanID" value="<%= bean.getID() %>" />
		<input type="hidden" name="viewID" id="viewID" value="<%= PortalBeanView.ADMIN_BEAN_EDIT_PROCESS_VIEW %>" />
</td>
</tr>
</table>
</form>
</mod:view>