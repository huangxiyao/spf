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
                 com.epicentric.metastore.*,
                 com.epicentric.portalbeans.*,
                 com.epicentric.portalbeans.beans.bookmarkbean.*,
				 com.epicentric.common.website.*,
                 com.epicentric.common.*"

contentType="text/html; charset=UTF-8" %>


<%@taglib uri="epi-tags" prefix="epi_html" %>
<%@taglib uri="module-tags" prefix="mod" %>
<mod:view class="com.epicentric.portalbeans.beans.bookmarkbean.BookmarkBeanCommonView">




<%
	BookmarkBean bean = (BookmarkBean) view.getBean();

	// updating bookmark key in the event that this is the old module w/o the "Bookmark_" pretext
    view.updateKeys();

	int numExtras=3;

	MetaStoreFolder folder=view.getFolder();

	// sets the checkbox property.
	String popup=folder.getProperty("popup");
	if (StringUtils.isEmpty(popup)) {
		popup = bean.getAdminProperty("popup");
	}

	String checkboxProperty = "";	// for the "checked" property of checkbox field
	if (popup.equalsIgnoreCase("on") == true) {
		checkboxProperty = "checked=\"checked\"";
	}

	BookmarkOrganizer organizer = bean.getBookmarkOrganizer();

	if (organizer == null) {
		organizer = new BookmarkOrganizer();
	}

	UserBookmarkOrganizer userOrganizer = view.getUserOrganizer();


	String actionURL = bean.getBaseViewURL(PortalBeanView.USER_BEAN_EDIT_PROCESS_VIEW);

	%>

<form method="post" action="<%=actionURL%>" style="dispaly:inline; margin-bottom:0px">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>
		<table cellpadding="0" cellspacing="0" border="0" class="epi-dataTable" summary="This table contains a list of links.">
			<tr>
				<th align="left" scope="col"><mod:i18nValue alias="type" key="_global.name_textbox_label" defaultValue="Name"/></th>
				<th align="left" scope="col"><mod:i18nValue alias="type" key="_global.URL_textbox_label" defaultValue="URL"/></th>
				<th align="center" scope="col"><mod:i18nValue alias="type" key="USER_BEAN_EDIT_VIEW.delete_checkbox_label" defaultValue="Delete"/></th>
			</tr>
	<%
	int rowIndex = 0;
	Bookmark bookmark = null;
	Vector bookmarks = organizer.getRequiredBookmarks();
	if(!bookmarks.isEmpty()){
	for (int i=0;i<bookmarks.size();i++) {
		bookmark = (Bookmark) bookmarks.elementAt(i);
        String strName = bookmark.getName();
		int nIndex = strName.indexOf("&quot;");
		if (nIndex!=-1)
		{
			String first = strName.substring(0, nIndex);
			String last = strName.substring(nIndex+6);
			strName = first + "\"" + last;
		}

		if (rowIndex%2==0) {
		%>
		  <tr class="epi-rowOdd">
		<% } else { %>
		  <tr class="epi-rowEven">
		 <% } %>
			<td align="left" scope="row"><mod:i18nValue alias="instance" key='<%= "Bookmark_"+strName%>' defaultValue="<%= strName%>"/></td>
			<td align="left"><mod:i18nValue alias="instance" key='<%= "Bookmark_"+bookmark.getBookmarkID()%>' defaultValue="<%= bookmark.getURLString()%>"/></td>
			<td align="center" class="epi-dim" nowrap="nowrap">&nbsp;<mod:i18nValue alias="type" key="USER_BEAN_EDIT_VIEW.required_message_text" defaultValue="Required"/>&nbsp;</td>
		  </tr>
		<%
		rowIndex++;
	}
}
	Vector userBookmarks = (userOrganizer != null) ? userOrganizer.getBookmarks() : organizer.getOptionalBookmarks();
	int i;
	for (i = 0; i < userBookmarks.size(); i++) {
		bookmark = (Bookmark) userBookmarks.elementAt(i);
		String beginStr="<input size=\"16\" name=\"name"+ Integer.toString(i) +"\" id=\"name"+ Integer.toString(i) +"\" value=\"";
		String endStr="\" class=\"epi-input\" style=\"width:80px\" />";
		String strName = bookmark.getName();
		int nIndex = strName.indexOf("&quot;");
		if (nIndex!=-1)
		{
			String first = strName.substring(0, nIndex);
			String last = strName.substring(nIndex+6);
			strName = first + "\"" + last;
		}
		if (rowIndex%2==0) { %>
		  <tr class="epi-rowOdd">
		<% } else { %>
		  <tr class="epi-rowEven">
		<% } %>
			<td align="left" scope="row"><%= beginStr+strName+endStr%></td>
			<%beginStr="<input size=\"16\" name=\"url"+ Integer.toString(i) +"\" id=\"url"+ Integer.toString(i) +"\" value=\"";%>
			<td align="left"><%= beginStr+bookmark.getURLString()+endStr%></td>
			<td align="center"><input type="checkbox" name="delete<%= i %>" id="delete<%= i %>" /></td>
		  </tr>
		<%
		rowIndex++;
	}

	int j;
	for (j = 0; j < numExtras; j++)	{
		if (rowIndex%2==0) { %>
		  <tr class="epi-rowOdd">
		<% } else { %>
		  <tr class="epi-rowEven">
		<% } %>
			<td scope="row"><input size="16" name="name<%= i+j %>" id="name<%= i+j %>" class="epi-input" style="width:80px" /></td>
			<td><input size="16" name="url<%= i+j %>" id="url<%= i+j %>" class="epi-input" style="width:80px" /></td>
			<td>&nbsp;</td>
		  </tr>
		<%
		rowIndex++;
	}
	%>

	</table>
	<input type="hidden" name="count" id="count" value="<%= i+j %>" />
</td>
	</tr>
	<tr>
		<td colspan="3"><input name="popup" id="open_window" type="checkbox" <%= checkboxProperty %> /><label for="open_window"><b>&nbsp;<mod:i18nValue alias="type" key="USER_BEAN_EDIT_VIEW.new_window_message_text" defaultValue="Open Web sites in new window"/></b></label>
		</td>
	</tr>

	<tr>
		<td colspan="3"><hr size="1" noshade="noshade" /></td>
	</tr>
	<tr>
	<td colspan="3">

	<epi_html:i18nElement><input type="submit" name="update" id="update" class="epi-button" value='<mod:i18nValue alias="type" key="_global.update_button_label" defaultValue=" Save " />' /></epi_html:i18nElement>
	<epi_html:i18nElement><input type="submit" name="cancel" id="cancel" class="epi-button" value='<mod:i18nValue alias="type" key="_global.cancel_button_label" defaultValue="Cancel" />' /></epi_html:i18nElement>

	<input type="hidden" name="beanID" id="beanID" value="<%= bean.getID() %>" />
	<input type="hidden" name="viewID" id="viewID" value="<%= PortalBeanView.USER_BEAN_EDIT_PROCESS_VIEW %>" />
</td>
</tr>
</table>
</form>
</mod:view>
