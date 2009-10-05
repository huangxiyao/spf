<%@ page import="com.hp.it.spf.navportlet.portal.component.portalbean.NavPortletBean" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="module-tags" prefix="mod" %>

<mod:view class="com.epicentric.portalbeans.beans.jspbean.JSPView">

<%
	try {
		NavPortletBean bean = (NavPortletBean) view.getBean();
		boolean underline = false;
		boolean thin = false;
		boolean bold = false;
		boolean linespacing = false;
		boolean displayDescription = false;
		boolean noHeader = false;
		boolean underlineLinks = false;
		int columns = 1;

		String menuItem = bean.getMenuItem();
		if (menuItem == null) menuItem = "";

		String uline = bean.getUnderline();
		if (uline == null) uline = "";
		if (!uline.equals("")) underline = true;

		String thinUline = bean.getThin();
		if (thinUline == null) thinUline = "";
		if (!thinUline.equals("")) thin = true;

		String boldHeader = bean.getBold();
		if (boldHeader == null) boldHeader = "";
		if (!boldHeader.equals("")) bold = true;

		String noHead = bean.getNoHeader();
		if (noHead != null && noHead.length() > 0) {
			noHeader = true;
		}

		String underlineLinksBeanValue = bean.getUnderlineLinks();
		if (underlineLinksBeanValue != null && underlineLinksBeanValue.length() > 0) {
			underlineLinks = true;
		}

		String lineSpace = bean.getLinespacing();
		if (lineSpace == null) lineSpace = "";
		if (!lineSpace.equals("")) linespacing = true;

		String displayDesc = bean.getDisplayDescription();
		if (displayDesc == null) displayDesc = "";
		if (!displayDesc.equals("")) displayDescription = true;

		String cols = bean.getColumns();
		if (cols != null && cols.length() > 0) {
			columns = Integer.parseInt(cols);
		}

		String textColor = bean.getTextColor();
		if (textColor == null)	{
			textColor = "";
		}

		String backgroundColor = bean.getBackgroundColor();
		if (backgroundColor == null)	{
			backgroundColor = "";
		}

		String descriptionBackgroundColor = bean.getDescriptionBackgroundColor();
		if (descriptionBackgroundColor == null) {
			descriptionBackgroundColor = "";
		}

		String fontSize = bean.getFontSize();
		if (fontSize == null)	{
			fontSize = "";
		}


		if(linespacing)
			out.print("<!-- linespace ok -->");
		if(!linespacing)
			out.print("<!-- linespace not ok -->");


		String actionUrl = bean.getFullViewURL("ADMIN_BEAN_EDIT_PROCESS_VIEW");
%>

<form name="MenuItem" method="POST" action="<%= actionUrl %>">

	<h1>Menu Item</h1>
		
	<hr size="1" noshade="noshade" />

	<table border="0" cellpadding="0" cellspacing="0" class="epi-datatable">		
		<tr>
			<td class="epi-formLabel">ID:</td>
			<td><input type="text" value="<%= menuItem %>" name="menuItem" /></td>
		</tr>
		<tr>
			<td class="epi-formLabel">Font Size:</td>
			<td><input type="text" value="<%= fontSize %>" name="fontSize" size="2" maxlength="2"/></td>
		</tr>
		<tr>
			<td class="epi-formLabel" colspan="2">
				<input type="radio" <%=(bold)?"checked":""%> name="header" value="bold" id="bold">
				<label for="bold">&nbsp;Bold Menu Header</label>
			</td>
		</tr>
		<tr>
			<td class="epi-formLabel" colspan="2">
				<input type="radio" <%=(underline)?"checked":""%> name="header" value="uline" id="uline">
				<label for="uline">&nbsp;Use Underline Menu Header</label>
			</td>
		</tr>
		<tr>
			<td class="epi-formLabel" colspan="2">
				<input type="radio" <%=(thin)?"checked":""%> name="header" value="thin" id="thin">
				<label for="thin">&nbsp;Use Thin Underline</label>
			</td>
		</tr>
		<tr>
			<td class="epi-formLabel" colspan="2">
				<input type="radio" <%=(noHeader)?"checked":""%> name="header" value="noheader" id="noheader">
				<label for="noheader">&nbsp;No Menu Header</label>
			</td>
		</tr>
	</table>
	<p/>

	<h1>Links Display Options</h1>
		
	<hr size="1" noshade="noshade" />
	
	<table border="0" class="epi-datatable">
		<tr>
			<td class="epi-formLabel" colspan="2">
				<input type="checkbox" <%=(linespacing)?"checked":""%> name="checkspace" value="checkspace" id="checkspace">
				<label for="checkspace">&nbsp;Use Huge Line Spacing</label>
			</td>
		</tr>
		<tr>
			<td class="epi-formLabel" colspan="2">
				<input type="checkbox" <%=(displayDescription)?"checked":""%> name="checkdisplaydesc" value="checkdisplaydesc" id="checkdisplaydesc">
				<label for="checkdisplaydesc">&nbsp;Display Link Description</label>
			</td>
		</tr>
		<tr>
			<td class="epi-formLabel" colspan="2">
				<input type="checkbox" <%=(underlineLinks)?"checked":""%> name="checkunderlinelinks" value="checkunderlinelinks" id="checkunderlinelinks">
				<label for="checkunderlinelinks">&nbsp;Underline Links</label>
			</td>
		</tr>
		<tr>
			<td class="epi-formLabel">Number of columns:</td>
			<td><select name="numbercolumns">
		<%
		for (int i =1;i < 10;i++)	{
			%><option value="<%= i %>" 
			<%
			if (i == columns)	{
			%>
				selected="selected"
			<%
			}
			%>><%= i %></option><%
		}
		%>
			</select>
			</td>
		</tr>
		<tr>
			<td class="epi-formLabel">Link Text Color:</td>
			<td><input type="text" value="<%= textColor %>" name="textColor" size="6" maxlength="6"/></td>
		</tr>
		<tr>
			<td class="epi-formLabel">Link Background Color:</td>
			<td><input type="text" value="<%= backgroundColor %>" name="backgroundColor" size="6" maxlength="6"/></td>
		</tr>
		<tr>
			<td class="epi-formLabel">Description Background Color:</td>
			<td><input type="text" value="<%= descriptionBackgroundColor %>" name="descriptionBackgroundColor" size="6" maxlength="6"/></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input class="epi-button" type="submit" value="Save Info" name="submit" /></td>
		</tr>
	</table>
</form>

<%
	} catch (Exception e) {
		out.print("There was a compile error. Contact SYSADMIN.");
	}
%>

</mod:view>