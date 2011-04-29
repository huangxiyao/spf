<%-----------------------------------------------------------------------------
	athp_horiz_nav.jsp

	Displays site navigation elements in a horizontally-oriented menu.  
	Capable of rendering two levels of navigation elements.
-----------------------------------------------------------------------------%>


<%---------------------------------------------------------------- IMPORTS --%>

<jsp:directive.page import="java.util.Map" />
<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="java.util.ArrayList" />
<jsp:directive.page import="com.epicentric.template.Style" />
<jsp:directive.page import="com.epicentric.template.StyleUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemNode" />
<jsp:directive.page import="com.epicentric.common.website.I18nUtils" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.athp.MenuItem" />

<%------------------------------------------------------------- DIRECTIVES --%>


<%---------------------------------------------------------- TAG LIBRARIES --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%----------------------------------------------------------------- SCRIPT --%>

<vgn-portal:defineObjects/>

<jsp:useBean id="AtHPModel" scope="request" 
        class="com.hp.frameworks.wpa.portal.athp.AtHPModel" />

<jsp:scriptlet>

	// For horizontal menu rendering:
	
	// Retrieve menu item nodes from Vignette and copy them to MenuItem
	// objects for markup later.
	
	ArrayList horizontalNodeList = new ArrayList();

	// Retrieve the ID of the current secondary page and determine whether or
	// not it is a "regular" nav page (meaning it is of type PAGE or 
	// JSP_INCLUDE_PAGE.
	String spID = portalContext.getCurrentSecondaryPage().getTemplateFriendlyID();
	boolean regularNavPage = spID.equals("PAGE") || spID.equals("JSP_INCLUDE_PAGE");

	// Retrieve an iterator for all menu nodes
	Iterator allNodes = MenuItemUtils.getAllNodes(portalContext).iterator();

	MenuItem level0Node = null;
	ArrayList level1NodeList = null;
	MenuItemNode node;

	// The while loop will process each menu node that is level 0 or 1
	// from the Vignette navigation tree using the allNodes iterator.   
	// Level 0 menu nodes are added to the horz node list (for markup)
	// that are:
	// - nodes with no level 1 nodes, regardless of authorization
	// - nodes with authorized level 1 nodes 
	// - nodes that are visible
	//
	// All level 1 nodes are added to its level 0 node's sub-menu item 
	// linked list.

	while (true) {

		// Get next node to check

		if (allNodes.hasNext()) {
			node = (MenuItemNode) allNodes.next();
		}
		else {
			node = null;
		}

		// When there is a new level 0 node to check or when there is 
		// no more nodes to check, save previous level 0 node and 
		// any of its level 1 nodes to horz node list.

		if ((node == null || node.getLevel() == 0) &&
			level0Node != null) {


			if (level0Node.isSpacer() == true) {
				// Ignore level 0 spacer nodes
			}
			else if ((level0Node.isLeaf() && level0Node.isEmpty() == false) ||
					(level0Node.isLeaf() == false && level1NodeList != null)) {

				// if level 0 node has no submenus (regardless of permissions),
				// or has permissioned submenus, 
				// then save link node and any sub-menus.

				if (level1NodeList != null) {
					level0Node.setSubMenuItems(level1NodeList);
				}
				
				// Add level 0 node to menu if it is visible.
				if (level0Node.isVisible())
					horizontalNodeList.add(level0Node);
			}
		}

		if (node != null) {

			// Create a MenuItem bean to hold the data we need for
			// new node.

			MenuItem menuItem = new MenuItem();
			menuItem.setId(node.getID());
			menuItem.setParentId(node.getParentID());
			menuItem.setTitle(node.getTitle());
			menuItem.setSpacer(node.isSpacer());
			menuItem.setLeaf(node.isLeaf());
			menuItem.setEmpty(node.isEmpty());
			menuItem.setVisible(node.isVisible());

			// Store the target value without the "target=" and ending 
			// double quote in the node target string.

			if (node.getTarget().length() > 0) {
				menuItem.setTarget(node.getTarget().substring(8, node.getTarget().length()-1));
			}

			// If this is a regular nav item, we can set the values directly;
			// otherwise, we'll need to calculate the secondary page URL and
			// look at the current secondary page ID to determine whether
			// or not this was the link that was clicked.
			if (node.getHref().indexOf("template.") == -1)
			{
				menuItem.setHref(node.getHref());
				menuItem.setSelected(node.wasSelected() && regularNavPage);
			}
			else
			{
				String templateID = node.getHref().substring(node.getHref().lastIndexOf(".") + 1);
				menuItem.setHref(portalContext.createDisplayURI(templateID).toString());
				menuItem.setSelected(templateID.equals(spID));
			}

			if (node.getLevel() == 0) {
				// Set the menu item bean to the level 0 node var, and
				// reset the level 1 node list to null.
				level0Node = menuItem;
				level1NodeList = null;
			}
			else if (node.getLevel() == 1 && node.isVisible()) {

				// Add the menu item bean to the level 1 node list, and
				// create a new list if it hasn't been created yet.

				if (level1NodeList == null) {
					level1NodeList = new ArrayList();
				}

				level1NodeList.add(menuItem);

			}				
		}
		else {
			// No more nodes to process
			break;
		}
	}

	// Add our newly-created list of nodes to page-scope for easy access.
	pageContext.setAttribute("nodes", horizontalNodeList);
	
pageContext.setAttribute("horizontalMenuStyleDef", I18nUtils.getValue(i18nID, "athp.horizontalMenuStyle", "",
			false, request));
			
</jsp:scriptlet>


<%---------------------------------------------------------------- MARKUP --%>

<script type="text/javascript">

<c:if test="${AtHPModel.horizontalMenuStyle || horizontalMenuStyleDef eq 'true'}">
	setHorizontalNav();	
</c:if>
	
<c:forEach var="node" items="${nodes}" varStatus="status">		
	createMenu("<c:out value="${node.id}" />", "<c:out value="${node.title}" escapeXml="false" />", "<c:out value="${node.href}" />" <c:if test="${!empty node.target}">, "<c:out value="${node.target}" />"</c:if>);
	<c:if test="${fn:length(node.subMenuItems) > 0}" >
		<c:forEach var="subMenuNode" items="${node.subMenuItems}" varStatus="status2">
			<c:choose>
				<c:when test="${subMenuNode.spacer}">
	addMenuSeparator("<c:out value="${subMenuNode.parentId}" />");
				</c:when>
				<c:otherwise>
	addMenuItem("<c:out value="${subMenuNode.parentId}" />", "<c:out value="${subMenuNode.title}" escapeXml="false" />", "<c:out value="${subMenuNode.href}" />" <c:if test="${!empty subMenuNode.target}">, "<c:out value="${subMenuNode.target}" />"</c:if>);
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</c:if>
</c:forEach>

</script>
