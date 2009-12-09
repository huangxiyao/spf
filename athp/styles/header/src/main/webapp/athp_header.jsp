<%-----------------------------------------------------------------------------
	athp_header.jsp
	
	STYLE: @HP Header
	STYLE TYPE: Header
	USES HEADER FILE: Yes

-----------------------------------------------------------------------------%>


<%-----------------------------------------------------------------------------
	Imports
-----------------------------------------------------------------------------%>

<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="java.util.ArrayList" />
<jsp:directive.page import="java.util.Iterator" />
<jsp:directive.page import="com.epicentric.common.website.I18nUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemNode" />

<%------------------------------------------------------------- DIRECTIVES --%>


<%----------------------------------------------------------- TAG LIBRARIES--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----------------------------------------------------------------- SCRIPT --%>

<jsp:useBean id="AtHPModel" scope="request" 
        class="com.hp.frameworks.wpa.portal.athp.AtHPModel" />

<jsp:scriptlet>

// Use layout properties from AtHPModel bean, with defaults from message properties.

pageContext.setAttribute("bodyStyleDisabledDef", I18nUtils.getValue(i18nID, "athp.bodyStyleDisabled", "",
			false, request));
pageContext.setAttribute("loginUrlDef", I18nUtils.getValue(i18nID, "athp.loginUrl", "",
			false, request));
pageContext.setAttribute("logoutUrlDef", I18nUtils.getValue(i18nID, "athp.logoutUrl", "",
			false, request));
pageContext.setAttribute("siteSearchIDDef", I18nUtils.getValue(i18nID, "athp.siteSearchID", "",
			false, request));
pageContext.setAttribute("siteSearchLabelDef", I18nUtils.getValue(i18nID, "athp.siteSearchLabel", "",
			false, request));
pageContext.setAttribute("myLinksDisabledDef", I18nUtils.getValue(i18nID, "athp.myLinksDisabled", "",
			false, request));
String generateBreadcrumbsDef = I18nUtils.getValue(i18nID, "athp.generateBreadcrumbs", "",
			false, request);
pageContext.setAttribute("generateBreadcrumbsDef", generateBreadcrumbsDef);

List breadcrumbItems = AtHPModel.getBreadcrumbItems();

if (breadcrumbItems.size() == 0 &&
	(AtHPModel.isGenerateBreadcrumbs() || 
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
	// including currently selected menu item.

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

		breadcrumbNodes.add(node.getLevel(), node);
		
		// Break out of loop once selected menu item is found
		// and remember selected node level.

		if (selectedBranchFound) {
			selectedNodeLevel = node.getLevel();
			break;
		}
		
	}
	if (selectedBranchFound) {

		// Add breadcrumbs for menu items, including current node.

		for (int i=0; i <= selectedNodeLevel; i++) {
			node = (MenuItemNode) breadcrumbNodes.get(i);
			breadcrumbItems.add(node.getTitle());	
		}
	}
}
pageContext.setAttribute("breadcrumbItems", breadcrumbItems);

</jsp:scriptlet>

<%----------------------------------------------------------------- MARKUP --%>

<script type="text/javascript">

	<c:choose>
		<c:when test="${empty AtHPModel.title}">
			<%-- Use navigation item name as title --%>
			<c:choose>
				<c:when test="${!empty AtHPModel.secondaryTitle}">
					setMultiLineBanner(document.title, "<c:out value="${AtHPModel.secondaryTitle}" />" <c:if test="${!empty AtHPModel.tertiaryTitle}">, "<c:out value="${AtHPModel.tertiaryTitle}" />"</c:if>);
				</c:when>
				<c:otherwise>
					setBanner(document.title);
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<%-- Use title attribute name as title --%>
			<c:choose>
				<c:when test="${!empty AtHPModel.secondaryTitle}">
					setMultiLineBanner("<c:out value="${AtHPModel.title}" />", "<c:out value="${AtHPModel.secondaryTitle}" />" <c:if test="${!empty AtHPModel.tertiaryTitle}">, "<c:out value="${AtHPModel.tertiaryTitle}" />"</c:if>);
				</c:when>
				<c:otherwise>
					setBanner("<c:out value="${AtHPModel.title}" />");
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	
	<c:if test="${!empty AtHPModel.greetingMessage}">
		setGreetingMessage("<c:out value="${AtHPModel.greetingMessage}" />");
	</c:if>	
	
	<c:if test="${!empty AtHPModel.warningMessage}">
		setWarningMessage("<c:out value="${AtHPModel.warningMessage}" />");
	</c:if>
	
	<c:if test="${!empty AtHPModel.loginUrl || ! empty loginUrlDef}">
		displayLogin("<c:out value="${AtHPModel.loginUrl}" default="${loginUrlDef}" />" <c:if test="${!empty AtHPModel.logoutUrl || ! empty logoutUrlDef}">, "<c:out value="${AtHPModel.logoutUrl}" default="${logoutUrlDef}" />"</c:if>);
	</c:if>		

	<c:if test="${!empty AtHPModel.siteSearchID || ! empty siteSearchIDDef}">
		setSiteSearch("<c:out value="${AtHPModel.siteSearchID}" default="${siteSearchIDDef}" />" <c:if test="${!empty AtHPModel.siteSearchLabel || ! empty siteSearchLabelDef}">, "<c:out value="${AtHPModel.siteSearchLabel}" default="${siteSearchLabelDef}" />"</c:if>);
	</c:if>	
		
	<c:if test="${AtHPModel.myLinksDisabled || myLinksDisabledDef eq 'true'}">
		disableMyLinks();	
	</c:if>

	<c:if test="${AtHPModel.bodyStyleDisabled || bodyStyleDisabledDef eq 'true'}">
		setBodyStyleDisabled();	
	</c:if>

	<c:if test="${!empty breadcrumbItems}">
		setBreadCrumbs(<c:forEach var="breadcrumb" items="${breadcrumbItems}" varStatus="loopStatus">"<c:out value="${breadcrumb}" />"<c:if test="${!loopStatus.last}">, </c:if></c:forEach>);
	</c:if>

</script>


