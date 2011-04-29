<%-----------------------------------------------------------------------------
	athp_vert_nav.jsp

	Displays site navigation elements in a vertically-oriented left menu.  
	Capable of rendering two levels of navigation elements.
-----------------------------------------------------------------------------%>


<%---------------------------------------------------------------- IMPORTS --%>

<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="java.util.ArrayList" />
<jsp:directive.page import="java.util.Iterator" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemNode" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.athp.MenuItem" />

<%------------------------------------------------------------- DIRECTIVES --%>


<%---------------------------------------------------------- TAG LIBRARIES --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Template --%>

<jsp:declaration>

/*
 * Method to create MenuItem object from MenuItemNode object.
 */
 
MenuItem createMenuItem(PortalContext portalContext, MenuItemNode node, 
		boolean regularNavPage, String spID) {

	MenuItem menuItem = new MenuItem();
	if (node.isSpacer()) {
		menuItem.setSpacer(true);
		return menuItem;
	}

	menuItem.setTitle(node.getTitle());
	menuItem.setVisible(node.isVisible());

	// If this is a regular nav item, we can set the values 
	// directly; otherwise, we'll need to calculate the secondary 
	// page URL and look at the menuID querstring parameter to 
	// determine whether or not this was the link that was clicked.
	if (node.getHref().indexOf("template.") == -1)
	{
		menuItem.setHref(node.getHref());
		menuItem.setSelected(node.isSelected() && regularNavPage);
	}
	else
	{
		String templateID = node.getHref().substring(node.getHref().lastIndexOf(".") + 1);
		menuItem.setHref(portalContext.createDisplayURI(templateID).toString());
		menuItem.setSelected(templateID.equals(spID));
	}
	return menuItem;
}

/*
 * Method to add MenuItem object to LeftMenuItems list.
 */
void addLeftMenuItem(List leftMenuItems, MenuItem menuItem, 
					MenuItemNode node, int offsetLevel, int[] nodeIndexes) {

	int currentLevel = node.getLevel();
	int index;

	if (currentLevel == offsetLevel) {
		// level 0 node, so just add menu item to left menu

		// generate menu item id, needed by @hp JS functions.
		menuItem.setId("id" + leftMenuItems.size());

		if (node.isVisible()) {
			leftMenuItems.add(menuItem);

			// save level 0 index
			nodeIndexes[0] = leftMenuItems.size() - 1;
		}
		else {
			// Flag 'not visible' parent node with -1 value
			// in nodeIndexes[0], so that child nodes (below)
			// can be ignored.
			nodeIndexes[0] = -1;
		}
	}
	// Ignore child nodes of not visible parent node
	else if (nodeIndexes[0] != -1) {
		// level 1 node
		// get current node's level 0 node in left menu, 
		// then add current node as child to level 0 node.

		index = nodeIndexes[0];
		MenuItem level0 = (MenuItem)leftMenuItems.get(index);

		// generate submenu item id
		menuItem.setId("id" + index + "_" + level0.getSubMenuItems().size());

		if (node.isVisible())
			level0.getSubMenuItems().add(menuItem);
	}
}
</jsp:declaration>

<%----------------------------------------------------------------- SCRIPT --%>

<jsp:useBean id="AtHPModel" scope="request" 
        class="com.hp.frameworks.wpa.portal.athp.AtHPModel" />


<jsp:scriptlet>

// navSplit is either 1 or 2 (default).  if the selected node's level is 0,
// then it will be changed to 1 (later).
int navSplit = 2;

List leftMenuItems = AtHPModel.getLeftMenuItems();

	// if the AtHPModel bean has left menu items, use it for athp layout
	// left nav, i.e. don't need to generate it from vap configured nav menu.

if (leftMenuItems.size() == 0) {

	leftMenuItems = new ArrayList();
	List allNodeList = MenuItemUtils.getAllNodes(portalContext);

	// Retrieve the ID of the current secondary page and determine whether or
	// not it is a "regular" nav page (meaning it is of type PAGE or 
	// JSP_INCLUDE_PAGE.
	String spID = portalContext.getCurrentSecondaryPage().getTemplateFriendlyID();
	boolean regularNavPage = spID.equals("PAGE") || spID.equals("JSP_INCLUDE_PAGE");

	// If navSplit is 0, then we display all items; otherwise we have to find
	// the list of children to display

	// Retrieve an iterator for all menu nodes
	Iterator allNodes = allNodeList.iterator();
	MenuItemNode node;
	MenuItem menuItem;
	String templateID;
	
	int currentLevel;
	int nodeIndexes[] = new int[1];

	if (navSplit == 0)
	{
		// Copy each MenuItemNode object node to MenuItem object in
		// vertical node list.

		while (allNodes.hasNext())
		{
			node = (MenuItemNode) allNodes.next();
			currentLevel = node.getLevel();

			// ignore nodes that are level 2 or higher
			if (currentLevel >= 2)
				continue;

			menuItem = createMenuItem(portalContext, node, regularNavPage, spID);
			addLeftMenuItem(leftMenuItems, menuItem, node, 0, nodeIndexes);

		}
	}
	else
	{

		// We want to show all level 2 and level 3 nodes in the branch 
		// of the current selected node.
		
		// The algorithm is to find the branch where the selected node
		// is (could be at any level), and put those level 2 and level 3 
		// nodes belonging to that branch into the left menu item list.

		boolean selectedBranchFound = false;
		
		while (allNodes.hasNext())
		{
			node = (MenuItemNode) allNodes.next();

			// Break loop when new branch with the same level or higher
			// is found, after we already have the selected branch.
			
			if (selectedBranchFound && node.getLevel() < navSplit) 
				break;
				
			if (node.getLevel() <= navSplit - 1 && selectedBranchFound == false)
			{
				// Check for selected branch.
				// If we find a pre-split nav item that is "selected" then 
				// we know that we have the correct branch
				if (node.getHref().indexOf("template.") == -1)
				{
					selectedBranchFound =  node.isSelected() && regularNavPage;
				}
				else
				{
					selectedBranchFound =  node.getHref().endsWith("." + spID);
				}
				if (selectedBranchFound) {
					navSplit = node.getLevel() + 1;
					
					// Clear left menu list of nodes that are not on
					// the selected branch.
					leftMenuItems.clear();
				}
			}
			
			// Put level 'navSplit' and level 'navSplit+1' nodes on left menu item 
			// lists.  Remove them from list (above) if it turned out that they're
			// not on the selected branch.  
			
			else if (node.getLevel() >= navSplit && node.getLevel() < navSplit+2)
			{

				// Create new node
				menuItem = createMenuItem(portalContext, node, regularNavPage, spID);
				if (menuItem.isSelected()) {
					selectedBranchFound = true;
				}
				
				// Add new node to menu items
				addLeftMenuItem(leftMenuItems, menuItem, node, navSplit, 
					nodeIndexes);
			}
		}

	}
}

pageContext.setAttribute("leftMenuItems", leftMenuItems);

</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP --%>

<script type="text/javascript">

<c:forEach var="node" items="${leftMenuItems}" varStatus="status">		

	<c:choose>
		<c:when test="${fn:length(node.subMenuItems) > 0}" >

	createNavExpandingList("<c:out value="${node.id}" />", "<c:out value="${node.title}" escapeXml="false" />", "<c:out value="${node.href}" />"<c:if test="${node.open}">, "true"</c:if>);

			<c:forEach var="subMenuNode" items="${node.subMenuItems}" varStatus="status2">
				<c:choose>
					<c:when test="${subMenuNode.separator}">
	addNavSeparator();
					</c:when>
					<c:when test="${subMenuNode.spacer}">
	addNavSpacer(10);
					</c:when>
					<c:when test="${empty subMenuNode.href}">
	addNavLabel("<c:out value="${subMenuNode.title}" escapeXml="false" />");
					</c:when>
					<c:otherwise>
	addNavExpandingListItem("<c:out value="${node.id}" />", "<c:out value="${subMenuNode.title}" escapeXml="false" />", "<c:out value="${subMenuNode.href}" />"<c:if test="${!empty subMenuNode.target}">, "<c:out value="${subMenuNode.target}" />"</c:if>);
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:when>
		<c:when test="${node.separator}">
	addNavSeparator();
		</c:when>
		<c:when test="${node.spacer}">
	addNavSpacer(10);
		</c:when>
		<c:when test="${empty node.href}">
	addNavLabel("<c:out value="${node.title}" escapeXml="false" />");
		</c:when>
		<c:otherwise>
	addNavLink("<c:out value="${node.title}" escapeXml="false" />", "<c:out value="${node.href}" />"<c:if test="${!empty node.target}" >, "<c:out value="${node.target}" />"</c:if>);
		</c:otherwise>
	</c:choose>

</c:forEach>

	drawLeftNavigation();

</script>

