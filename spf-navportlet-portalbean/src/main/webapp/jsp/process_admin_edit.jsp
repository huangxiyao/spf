<%@ page import="com.hp.it.spf.navportlet.portal.component.portalbean.NavPortletBean" %>
<%@taglib uri="module-tags" prefix="mod" %>

<mod:view class="com.epicentric.portalbeans.beans.jspbean.JSPView">

<%
	NavPortletBean bean = (NavPortletBean) view.getBean();
	String uline = null;
	String thin = null;
	String bold = null;
	String noHeader = null;
	String linespace = null;
	String displayDesc = null;

	String header = view.request("header");
	String menuItem = view.request("menuItem");
	String checkline = view.request("checkspace");
	String checkDisplayDesc = view.request("checkdisplaydesc");
	String underlineLinks = view.request("checkunderlinelinks");
	String columns = view.request("numbercolumns");
	String textColor = view.request("textColor");
	String backgroundColor = view.request("backgroundColor");
	String fontSize = view.request("fontSize");
	String descriptionBackgroundColor = view.request("descriptionBackgroundColor");

	if (header.equals("uline")) uline = "x";
	if (header.equals("thin")) thin = "x";
	if (header.equals("bold")) bold = "x";
	if (header.equals("noheader")) noHeader = "x";
	if (checkline.equalsIgnoreCase("checkspace")) linespace = "x";
	if (checkDisplayDesc.equalsIgnoreCase("checkdisplaydesc")) displayDesc = "x";
	if (checkDisplayDesc.equalsIgnoreCase("checkunderlinelinks")) underlineLinks = "x";

	bean.setMenuItem(menuItem);
	bean.setUnderline(uline);
	bean.setThin(thin);
	bean.setBold(bold);
	bean.setNoHeader(noHeader);
	bean.setLinespacing(linespace);
	bean.setDisplayDescription(displayDesc);
	bean.setUnderlineLinks(underlineLinks);
	bean.setColumns(columns);
	bean.setTextColor(textColor);
	bean.setBackgroundColor(backgroundColor);
	bean.setFontSize(fontSize);
	bean.setDescriptionBackgroundColor(descriptionBackgroundColor);

	bean.save();
%>

</mod:view>
