<%@page import="com.epicentric.common.website.MenuItemNode,
				com.epicentric.common.website.MenuItemUtils,
				com.epicentric.common.website.I18nUtils,
				com.hp.spp.portal.common.helper.LocalizationHelper,
				com.hp.spp.portal.common.helper.MenuItemHelper"%>

<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>
<vgn-portal:defineObjects />

<%
	MenuItemHelper menuItemHelper = new MenuItemHelper() ;
	LocalizationHelper localizationHelper = new LocalizationHelper() ;
	MenuItemNode menuItem = MenuItemUtils.getSelectedMenuItemNode(portalContext);
	
	String description = I18nUtils.getValue(menuItem.getID(),"description", "", session, request);
	String title = menuItem.getTitle();
	String page_title = I18nUtils.getValue(menuItem.getID(),"page_title", "", session, request);

	String description_bis = I18nUtils.getValue(menuItem.getID(), "description_bis", "", session, request);
	String title_bis = I18nUtils.getValue(menuItem.getID(), "title_bis", "", session, request);
	String href = I18nUtils.getValue(menuItem.getID(), "URLProd", "", session, request);
	href = localizationHelper.getValueNoSpan(href) ;

	String align = I18nUtils.getValue(menuItem.getID(), "align", "", session, request);
	align = localizationHelper.getValueNoSpan(align) ;
	String valign = I18nUtils.getValue(menuItem.getID(), "valign", "", session, request);
	valign = localizationHelper.getValueNoSpan(valign) ;
	String width = I18nUtils.getValue(menuItem.getID(), "width", "", session, request);
	width = localizationHelper.getValueNoSpan(width) ;
	String height = I18nUtils.getValue(menuItem.getID(), "height", "", session, request);
	height = localizationHelper.getValueNoSpan(height) ;

	String font_size = I18nUtils.getValue(menuItem.getID(), "font_size", "", session, request);
	font_size = localizationHelper.getValueNoSpan(font_size) ;

	//if (description_bis.equals("")){
	//	description_bis = description;
	//}

	if (page_title.equals("")) {
		page_title = title;
	}

	if (title_bis.equals("")) {
		title_bis = title;
	}

	boolean isHomePage = menuItemHelper.isHomePage(portalContext) ;

	if (height.equals("")) {
		if (isHomePage) {
			height = "185px";
		} else {
			height = "74px";
		}
	}

	if (align.equals("")) {
		if (isHomePage) {
			align = "center";
		} else {
			align = "left";
		}
	}

	if (valign.equals("")) {
		if (isHomePage) {
			valign = "middle";
		} else {
			valign = "top";
		}
	}

	if (font_size.equals("")) {
		if (isHomePage) {
			font_size = "20pt";
		} else {
			font_size = "18pt";
		}
	}

%>

<table style="table-layout: fixed; width: 100%" cellspacing=0 cellpadding=0 border=0>
	<%if (isHomePage){%><tbody><%}%>
		<tr style="height: <%=height%>">
			<!-- welcome box -->
			<td class="theme" align="<%=align%>" style="vertical-align: <%=valign%>"">
				<table cellspacing=0 cellpadding=0 border=0>
					<%if (!isHomePage){%>
					<tr style="height: 3px">
						<td>&nbsp;</td>
					</tr>
					<%}%>
					<tr>
						<%if (!isHomePage){%><td style="width: 10px;height: 6px">&nbsp;</td><%}%>
						<td align="<%=align%>">
							<h1 class=themeheader style="font-weight: normal; font-size: <%=font_size%>"><%=title_bis%></h1>
						</td>
					</tr>
					<tr>
						<%if (!isHomePage){%><td style="width: 10px;height: 9px">&nbsp;</td><%}%>
						<td style="vertical-align: bottom">
							<%if (!description_bis.equals("")) {%> 
							<%if (!href.equals("")){%><a href="<%=href%>" style="color:white;text-decoration:none"><%}else{%><font style="color:white;text-decoration:none"><%}%>&raquo;&nbsp;<%=description_bis%><%if (!href.equals("")){%></a><%}else{%></font><%}%>
							<%} else {%>
							&nbsp;
							<%}%>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	<%if (isHomePage){%></tbody><%}%>
</table>
