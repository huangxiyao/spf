<%-----------------------------------------------------------------------------
	hpweb_header.jsp
	
	STYLE: HPWeb Header - Only stretch logo, enhanced layout is supported.
	STYLE TYPE: Header
	USES HEADER FILE: Yes

	The header displays itself in two parts.   This is done to allow  the 
	horizontal navigation to display between the upper portion 
	(hp-wide global buttons) and the lower portion (title and breadcrumbs).
	
	'hpweb_header_split' request attribute set here to indicate to the
	grid component and here that the lower portion remains to be displayed.
	
-----------------------------------------------------------------------------%>


<%----------------------------------------------------------------------------- 
	Imports
-----------------------------------------------------------------------------%>

<jsp:directive.page import="javax.servlet.http.HttpServletRequest" />
<jsp:directive.page import="javax.servlet.http.HttpSession" />

<jsp:directive.page import="com.vignette.portal.website.enduser.PortalContext" />
<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="java.util.ArrayList" />
<jsp:directive.page import="java.util.Iterator" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemNode" />
<jsp:directive.page import="com.hp.frameworks.wpa.hpweb.MenuItem" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.hpweb.Utils" />


<%----------------------------------------------------------------------------- 
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-core-enhanced" %>

<%----------------------------------------------------------------------------- 
	Style Arguments
-----------------------------------------------------------------------------%>

<%-- boolean widePage --%>
<c:set var="widePageArg" value="${widePage}" />


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		
<%----------------------------------------------------------------------------- 
	Upper header portion, hp global wide buttons and search form
-----------------------------------------------------------------------------%>
	
<c:choose>
<c:when test="${empty hpweb_header_split}">

	<c:set var="hpweb_header_split" value="true" scope="request" />

<jsp:scriptlet>

// Set variables from HPWebModel bean, with defaults from message catalog.

// hpweb:logo tag attributes

String sectionTitleDef = Utils.getI18nValue(i18nID, "hpweb.sectionTitle",
			portalContext);
String contactTextDef = Utils.getI18nValue(i18nID, "hpweb.contactText",
			portalContext);
String contactUrlDef = Utils.getI18nValue(i18nID, "hpweb.contactUrl",
			portalContext);
String liveChatTextDef = Utils.getI18nValue(i18nID, "hpweb.liveChatText",
			portalContext);
String liveChatUrlDef = Utils.getI18nValue(i18nID, "hpweb.liveChatUrl",
			portalContext);
String buyingOptTextDef = Utils.getI18nValue(i18nID, "hpweb.buyingOptText",
			portalContext);
String buyingOptUrlDef = Utils.getI18nValue(i18nID, "hpweb.buyingOptUrl",
			portalContext);
String orderStatusTextDef = Utils.getI18nValue(i18nID, "hpweb.orderStatusText",
			portalContext);
String orderStatusUrlDef = Utils.getI18nValue(i18nID, "hpweb.orderStatusUrl",
			portalContext);
String custSvcTextDef = Utils.getI18nValue(i18nID, "hpweb.custSvcText",
			portalContext);
String custSvcUrlDef = Utils.getI18nValue(i18nID, "hpweb.custSvcUrl",
			portalContext);
String myAccountUrlDef = Utils.getI18nValue(i18nID, "hpweb.myAccountUrl",
			portalContext);
String cartTextDef = Utils.getI18nValue(i18nID, "hpweb.cartText",
			portalContext);
String cartUrlDef = Utils.getI18nValue(i18nID, "hpweb.cartUrl",
			portalContext);
String cartItemTextDef = Utils.getI18nValue(i18nID, "hpweb.cartItemText",
			portalContext);

// hpweb:topNavSearch tag attributes

String exploreUrlDef = Utils.getI18nValue(i18nID, "hpweb.exploreUrl",
			portalContext);
String communitiesUrlDef = Utils.getI18nValue(i18nID, "hpweb.communitiesUrl",
			portalContext);
String searchWidgetDef = Utils.getI18nValue(i18nID, "hpweb.searchWidget",
			portalContext);
String searchUrlDef = Utils.getI18nValue(i18nID, "hpweb.searchUrl",
			portalContext);
String searchReturnTextDef = Utils.getI18nValue(i18nID, "hpweb.searchReturnText",
			portalContext);
String searchContactUrlDef = Utils.getI18nValue(i18nID, "hpweb.searchContactUrl",
			portalContext);
String searchReturnUrlDef = Utils.getI18nValue(i18nID, "hpweb.searchReturnUrl",
			portalContext);
String searchOmnitureTagDef = Utils.getI18nValue(i18nID, "hpweb.searchOmnitureTag",
			portalContext);
String searchAudienceDef = Utils.getI18nValue(i18nID, "hpweb.searchAudience",
			portalContext);

</jsp:scriptlet>

<%----------------------------------------------------------------------------- 
	Template (for upper header portion) 
-----------------------------------------------------------------------------%>

	<hpweb:logo>
		<jsp:attribute name="sectionTitle"><c:out value="${HPWebModel.sectionTitle}" default="<%= sectionTitleDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="contactText"><c:out value="${HPWebModel.contactText}" default="<%= contactTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="contactUrl"><c:out value="${HPWebModel.contactUrl}" default="<%= contactUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="liveChatText"><c:out value="${HPWebModel.liveChatText}" default="<%= liveChatTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="liveChatUrl"><c:out value="${HPWebModel.liveChatUrl}" default="<%= liveChatUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="buyingOptText"><c:out value="${HPWebModel.buyingOptText}" default="<%= buyingOptTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="buyingOptUrl"><c:out value="${HPWebModel.buyingOptUrl}" default="<%= buyingOptUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="orderStatusText"><c:out value="${HPWebModel.orderStatusText}" default="<%= orderStatusTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="orderStatusUrl"><c:out value="${HPWebModel.orderStatusUrl}" default="<%= orderStatusUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="custSvcText"><c:out value="${HPWebModel.custSvcText}" default="<%= custSvcTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="custSvcUrl"><c:out value="${HPWebModel.custSvcUrl}" default="<%= custSvcUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="username">${HPWebModel.username}</jsp:attribute>
		<jsp:attribute name="signInUrl">${HPWebModel.signInUrl}</jsp:attribute>
		<jsp:attribute name="signOutUrl">${HPWebModel.signOutUrl}</jsp:attribute>
		<jsp:attribute name="registerUrl">${HPWebModel.registerUrl}</jsp:attribute>
		<jsp:attribute name="profileUrl">${HPWebModel.profileUrl}</jsp:attribute>
		<jsp:attribute name="myAccountUrl"><c:out value="${HPWebModel.myAccountUrl}" default="<%= myAccountUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="cartText"><c:out value="${HPWebModel.cartText}" default="<%= cartTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="cartUrl"><c:out value="${HPWebModel.cartUrl}" default="<%= cartUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="cartItemText"><c:out value="${HPWebModel.cartItemText}" default="<%= cartItemTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="cartItemCount">${HPWebModel.cartItemCount}</jsp:attribute>
		<jsp:attribute name="wide">${widePageArg}</jsp:attribute>
	</hpweb:logo>
	
	<hpweb:topNavSearch>
		<jsp:attribute name="exploreUrl"><c:out value="${HPWebModel.exploreUrl}" default="<%= exploreUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="communitiesUrl"><c:out value="${HPWebModel.communitiesUrl}" default="<%= communitiesUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="searchWidget"><c:out value="${HPWebModel.searchWidget}" default="<%= searchWidgetDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="searchUrl"><c:out value="${HPWebModel.searchUrl}" default="<%= searchUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="searchReturnText"><c:out value="${HPWebModel.searchReturnText}" default="<%= searchReturnTextDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="searchContactUrl"><c:out value="${HPWebModel.searchContactUrl}" default="<%= searchContactUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="searchReturnUrl"><c:out value="${HPWebModel.searchReturnUrl}" default="<%= searchReturnUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="searchOmnitureTag"><c:out value="${HPWebModel.searchOmnitureTag}" default="<%= searchOmnitureTagDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="searchAudience"><c:out value="${HPWebModel.searchAudience}" default="<%= searchAudienceDef %>" escapeXml="false" /></jsp:attribute>
	</hpweb:topNavSearch>
	
	<div class="clear"></div>

</c:when>
<c:otherwise>
	
<%----------------------------------------------------------------------------- 
	Lower header portion, title and breadcrumbs
-----------------------------------------------------------------------------%>

<jsp:scriptlet>

// hpweb:title tag attributes

String titleDef = Utils.getI18nValue(i18nID, "hpweb.title",
			portalContext);

// Use Vignette menu item node title as default page title.

if (titleDef == null || titleDef.length() == 0) {
	MenuItemNode menuItemNode = MenuItemUtils.getSelectedMenuItemNode(portalContext);
	if (menuItemNode != MenuItemUtils.EMPTY_NODE) {
		titleDef = Utils.filterSpan(menuItemNode.getTitle());
	}
}

String taglineDef = Utils.getI18nValue(i18nID, "hpweb.tagline",
			portalContext);
String topPromotionDef = Utils.getI18nValue(i18nID, "hpweb.topPromotion",
			portalContext);
String generateBreadcrumbsDef = Utils.getI18nValue(i18nID, "hpweb.generateBreadcrumbs",
			portalContext);

// If the HPWebModel 'generateBreadcrumbs' boolean property is true,
// then traverse navigation tree and find the ancestor nodes 
// (MenuItemNode type) of the current selected node.   Then set the
// 'breadcrumbItems' array list to those nodes.

ArrayList breadcrumbItems = null;

if (HPWebModel.getBreadcrumbItems().size() == 0 &&
	(HPWebModel.isGenerateBreadcrumbs() || 
	(generateBreadcrumbsDef != null && generateBreadcrumbsDef.equals("true")))) {

	breadcrumbItems = new ArrayList();

	List allNodeList = MenuItemUtils.getAllNodes(portalContext);

	String spID = portalContext.getCurrentSecondaryPage().getTemplateFriendlyID();
	boolean regularNavPage = spID.equals("PAGE") || spID.equals("JSP_INCLUDE_PAGE");

	boolean selectedBranchFound = false;
	Iterator allNodes = allNodeList.iterator();
	MenuItemNode node;
	ArrayList breadcrumbNodes = new ArrayList();
	int selectedNodeLevel = -1;

	// Iterate over all the nodes and create breadcrumbs for menu items
	// leading to currently selected menu item.

	while (allNodes.hasNext())
	{
		node = (MenuItemNode) allNodes.next();

		if (node.getHref().indexOf("template.") == -1)
		{
			selectedBranchFound = node.isSelected() && regularNavPage;
		}
		else
		{
			selectedBranchFound = node.getHref().endsWith("." + spID);
		}

		// Break out of loop once selected menu item is found
		// and remember selected node level.

		if (selectedBranchFound) {
			selectedNodeLevel = node.getLevel();
			break;
		}

		breadcrumbNodes.add(node.getLevel(), node);
		
	}
	if (selectedBranchFound) {

		// Add breadcrumbs for ancester menu items up to, but not
		// including selected menu item.

		for (int i=0; i < selectedNodeLevel; i++) {
			node = (MenuItemNode) breadcrumbNodes.get(i);
			MenuItem menuItem = Utils.createMenuItem(portalContext, node, 
				regularNavPage, spID);
			breadcrumbItems.add(menuItem);	
		}
	}
	
	pageContext.setAttribute("breadcrumbItems", breadcrumbItems);
}

</jsp:scriptlet>

<%----------------------------------------------------------------------------- 
	Template  (for lower header portion)
-----------------------------------------------------------------------------%>

	<%-- Index page title, which includes breadcrumbs and tagline --%>
	<!--startindex-->
	
<c:choose>
<c:when test="${! empty HPWebModel.breadcrumbItems}" >
	<hpweb:title breadcrumbItems="${HPWebModel.breadcrumbItems}" >
		<jsp:attribute name="title"><c:out value="${HPWebModel.title}" default="<%= titleDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="tagline"><c:out value="${HPWebModel.tagline}" default="<%= taglineDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="topPromotion"><c:out value="${HPWebModel.topPromotion}" default="<%= topPromotionDef %>" escapeXml="false" /></jsp:attribute>
	</hpweb:title>
</c:when>
<c:when test="${! empty breadcrumbItems}" >
	<hpweb:title breadcrumbItems="${breadcrumbItems}" >
		<jsp:attribute name="title"><c:out value="${HPWebModel.title}" default="<%= titleDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="tagline"><c:out value="${HPWebModel.tagline}" default="<%= taglineDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="topPromotion"><c:out value="${HPWebModel.topPromotion}" default="<%= topPromotionDef %>" escapeXml="false" /></jsp:attribute>
	</hpweb:title>
</c:when>
<c:otherwise>
	<hpweb:title>
		<jsp:attribute name="title"><c:out value="${HPWebModel.title}" default="<%= titleDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="tagline"><c:out value="${HPWebModel.tagline}" default="<%= taglineDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="topPromotion"><c:out value="${HPWebModel.topPromotion}" default="<%= topPromotionDef %>" escapeXml="false" /></jsp:attribute>
	</hpweb:title>
</c:otherwise>
</c:choose>

	<!--stopindex-->

</c:otherwise>
</c:choose>