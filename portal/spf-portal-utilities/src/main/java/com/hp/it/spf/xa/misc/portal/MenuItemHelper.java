package com.hp.it.spf.xa.misc.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.navigation.MenuItem;
import com.epicentric.template.Style;
//import com.hp.spp.portal.common.sql.PortalCommonDAOCacheImpl;
//import com.hp.spp.profile.Constants;
//import com.vignette.portal.util.StringUtils;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Helper class for using accessing the vignette menu items.
 * 
 * @author Phil Bretherton
 * 
 */
public class MenuItemHelper {

	/**
	 * Standard logger.
	 */
	private static Logger mLog = Logger.getLogger(MenuItemHelper.class);

	protected static final String SEARCHED_NODE = "searchedNode";

	protected static final String FIRST_NODE = "firstNode";

	protected static final String MENU_ITEM_PARAMS_SEPARATOR = ",";

	protected static final String PROTOCOL_HTTP = "http";

	protected static final String PROTOCOL_HTTPS = "https";

	protected static final PortalHelper mPortalHelper = new PortalHelper();

	/**
	 * Empty constructor.
	 */
	public MenuItemHelper() {
	}

	/**
	 * Checks whether the selected menu item id is the home page.
	 * 
	 * @param context
	 *            Used to get the root node and the selected menu item id
	 * @return true if it is the home page
	 */
//	public boolean isHomePage(PortalContext context) {
//		boolean homePage = false;
//		MenuItemNode rootNode = getSiteRootNode(context);
//		MenuItemNode selectedNode = MenuItemUtils.getSelectedMenuItemNode(context);
//		if (rootNode != null && selectedNode != null) {
//			String rootId = rootNode.getID();
//			homePage = rootId.equals(selectedNode.getID());
//		}
//		return homePage;
//	}

	/**
	 * Returns the root node for the site. This root node is defined per site in
	 * the table SPP_SITECONFIG. TODO is this the correct table name?!
	 * 
	 * @param context
	 *            Portal context that will serve to find the menu items for this
	 *            user as well as the site
	 * @return the root node for the site. Can return null if the root has not
	 *         been configured in the database.
	 */
//	public MenuItemNode getSiteRootNode(PortalContext context) {
//
//		MenuItemNode rootNode = null;
//		Map userProfile = (HashMap) context.getPortalRequest().getSession().getAttribute(Constants.PROFILE_MAP);
//		String site = (String) userProfile.get(Constants.MAP_SITE);
//		if (mLog.isDebugEnabled()) {
//			mLog.debug("Getting root node for site: " + site);
//		}
//		String rootNodeId = getRootNodeId(site);
//		if (mLog.isDebugEnabled()) {
//			mLog.debug("Root node id: " + rootNodeId);
//		}
//		List allNodes = MenuItemUtils.getAllNodes(context);
//		if (allNodes != null && !StringUtils.isEmpty(rootNodeId)) {
//			Iterator it = allNodes.iterator();
//			while (rootNode == null && it.hasNext()) {
//				MenuItemNode currentNode = (MenuItemNode) it.next();
//				String currentNodeId = currentNode.getID();
//				if (mLog.isDebugEnabled()) {
//					mLog.debug("Checking against node: " + currentNodeId);
//				}
//				if (rootNodeId.equals(currentNodeId)) {
//					rootNode = currentNode;
//				}
//			}
//		}
//
//		return rootNode;
//	}

	/**
	 * Returns the children of the given node. Incredibly the vignette API does
	 * not seem to provide this function and the only way of doing this is to
	 * get all the nodes from the portal context and returning those nodes whose
	 * parent is the given node.
	 * 
	 * @param context
	 *            Needed in order to get all the nodes
	 * @param node
	 *            The parent node
	 * @return Children of the given parent node
	 */
	public List<MenuItemNode> getChildNodes(PortalContext context, MenuItemNode node) {
		List<MenuItemNode> childNodes = new ArrayList<MenuItemNode>();
		String parentNodeId = node.getID();
		if (mLog.isDebugEnabled()) {
			mLog.debug("Looking for children of " + parentNodeId);
		}
		List allNodes = MenuItemUtils.getAllNodes(context);
		Iterator iter = allNodes.iterator();
		while (iter.hasNext()) {
			MenuItemNode currentNode = (MenuItemNode) iter.next();
			String currentParentNodeId = currentNode.getParentID();
			if (mLog.isDebugEnabled()) {
				mLog.debug("Checking if node is child: " + currentNode.getID() + " with parent = " + currentParentNodeId);
			}
			if (parentNodeId.equals(currentParentNodeId)) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("Child found");
				}
				childNodes.add(currentNode);
			}
		}
		return childNodes;
	}

	/**
	 * Finds the menu item using the menu item title.
	 * 
	 * @param context
	 *            Needed in order to get all the nodes
	 * @param nodeName
	 *            Title of the node
	 * @return Node whose title was passed
	 */
	public MenuItemNode findMenuItemByName(PortalContext context, String nodeName) {
		return findMenuItemByName(context, nodeName, false);
	}

	/**
	 * Finds the menu item using the menu item title.
	 * 
	 * @param portalContext
	 *            Needed in order to get all the nodes
	 * @param pagename
	 *            Title of the node
	 * @return Node whose title was passed
	 */
	public MenuItemNode findMenuItemByName(PortalContext portalContext, String pagename, boolean withFirstNode) {
		MenuItemNode searchedNode = null;
		MenuItemNode firstNode = null;

		Map<String, MenuItemNode> nodeMap = findMenuItemByNameInternal(portalContext, pagename);

		searchedNode = nodeMap.get(SEARCHED_NODE);
		firstNode = nodeMap.get(FIRST_NODE);

		if (searchedNode != null) {
			return searchedNode;
		} else if (null != firstNode && withFirstNode) {
			return firstNode;
		}

		return null;
	}

	/**
	 * Finds the menu item using the menu item title.
	 * 
	 * @param portalContext
	 *            Needed in order to get all the nodes
	 * @param pagename
	 *            Title of the node
	 * @return map with to attribute SEARCHED_NODE and FIRST_NODE
	 */
	protected Map<String, MenuItemNode> findMenuItemByNameInternal(PortalContext portalContext, String pagename) {
		MenuItemNode searchedNode = null;
		MenuItemNode firstNode = null;
		Map<String, MenuItemNode> nodeMap = new HashMap<String, MenuItemNode>();

		Iterator iter = (null != portalContext && null != MenuItemUtils.getAllNodes(portalContext)) ? MenuItemUtils.getAllNodes(portalContext).iterator() : null;

		if (mLog.isDebugEnabled()) {
			mLog.debug("[Class]=MenuItemHelper [Method]=findMenuItemByName [Parameter]=pagename [Value]=" + pagename);
		}
		boolean isFirstNode = false; // flag to capture first node

		pagename = escapeHtml(pagename).trim().toLowerCase();
		if (mLog.isDebugEnabled()) {
			mLog.debug("[Class]=MenuItemHelper [Method]=findMenuItemByName [Parameter]=pagename [Value]=" + pagename);
		}

		MenuItemNode temporary = null;
		// walk the list of pagenames
		while (null != iter && iter.hasNext() && searchedNode == null) {
			temporary = (MenuItemNode) iter.next();

			MenuItem item = temporary.getMenuItem();

			if (item != null && item.getTitle() != null) {

				// this particular page matches the pagename in the request
				if (representsMenuItem(item.getTitle().trim().toLowerCase(), pagename)) {
					searchedNode = temporary;
				} else if (!isFirstNode && temporary.getLevel() == 0) {
					// assign first vertical node
					firstNode = temporary;
					isFirstNode = true;
				}
			}
		}

		nodeMap.put(SEARCHED_NODE, searchedNode);
		nodeMap.put(FIRST_NODE, firstNode);

		return nodeMap;
	}

	private String escapeHtml(String pagename) {
		// Translate (htmlify) the parameter for " ' > < characters
		StringBuffer sbPageName = new StringBuffer(128);

		for (int i = 0, len = pagename.length(); i < len; i++) {
			char ch = pagename.charAt(i);
			if (ch == '\'') {
				sbPageName.append("&#").append((int) ch).append(";");
			}
			else if (ch == '"') {
				sbPageName.append("&quot;");
			}
			else if (ch == '>') {
				sbPageName.append("&gt;");
			}
			else if (ch == '<') {
				sbPageName.append("&lt;");
			}
			else {
				sbPageName.append(ch);
			}
		}
		// update the parameter value
		return sbPageName.toString();
	}

	/**
	 * Returns <tt>true</tt> if the given <tt>searchedMenuItemName</tt> corresponds to  <tt>currentMenuItemName</tt>.
	 * The check uses string comparison, but it is also handling
	 * the cases where the currentMenuItemName contains SPP-specific information
	 * about the protocol, i.e. when menu item node title is "Landing Page (http)" it will actually compare
	 * only "Landing Page" and will ignore the text starting with "(".
	 * @param currentMenuItemName current menu item title for which the name is verified
	 * @param searchedMenuItemName name to compare
	 * @return <tt>true</tt> if the node has the given name
	 */
	private boolean representsMenuItem(String currentMenuItemName, String searchedMenuItemName) {
		if (currentMenuItemName.equals(searchedMenuItemName)) {
			return true;
		}

		// Menu item name can also contain SPP-specific information such as "xyz (http)".
		// In such a case drop the part in the parenthesis and compare

		int pos = currentMenuItemName.lastIndexOf('(');
		if (pos > 0 && currentMenuItemName.substring(0, pos).trim().equals(searchedMenuItemName)) {
			return true;
		}

		return false;
	}

	/**
	 * Finds the menu item using the menu item ID.
	 * 
	 * @param context
	 *            Needed in order to get all the nodes
	 * @param nodeID
	 *            UID of the node
	 * @return Node whose title was passed
	 */
	public MenuItemNode findMenuItemByID(PortalContext context, String nodeID) {
		return findMenuItemByID(context, nodeID, false);
	}

	/**
	 * Finds the menu item using the menu item ID.
	 * 
	 * @param portalContext
	 *            Needed in order to get all the nodes
	 * @param nodeID
	 *            UID of the node
	 * @return Node whose title was passed
	 */
	public MenuItemNode findMenuItemByID(PortalContext portalContext, String nodeID, boolean withFirstNode) {
		MenuItemNode searchedNode = null;
		MenuItemNode firstNode = null;

		Map<String, MenuItemNode> nodeMap = findMenuItemByIDInternal(portalContext, nodeID);

		searchedNode = nodeMap.get(SEARCHED_NODE);
		firstNode = nodeMap.get(FIRST_NODE);

		if (searchedNode != null) {
			return searchedNode;
		} else if (null != firstNode && withFirstNode) {
			return firstNode;
		}

		return null;
	}

	/**
	 * Finds the menu item using the menu item ID.
	 * 
	 * @param portalContext
	 *            Needed in order to get all the nodes
	 * @param nodeID
	 *            UID of the node
	 * @return a map with to attribute SEARCHED_NODE and FIRST_NODE
	 */
	protected Map<String, MenuItemNode> findMenuItemByIDInternal(PortalContext portalContext, String nodeID) {
		MenuItemNode searchedNode = null;
		MenuItemNode firstNode = null;
		Map<String, MenuItemNode> nodeMap = new HashMap<String, MenuItemNode>();

		Iterator iter = (null != portalContext && null != MenuItemUtils.getAllNodes(portalContext)) ? MenuItemUtils.getAllNodes(portalContext).iterator() : null;

		if (mLog.isDebugEnabled()) {
			mLog.debug("[Class]=MenuItemHelper [Method]=findMenuItemByID [Parameter]=pagename [Value]=" + nodeID);
		}
		boolean isFirstNode = false; // flag to capture first node

		// update the parameter value
		nodeID = nodeID.toString().trim();
		if (mLog.isDebugEnabled()) {
			mLog.debug("[Class]=MenuItemHelper [Method]=findMenuItemByID [Parameter]=pagename [Value]=" + nodeID);
		}

		MenuItemNode temporary = null;
		// walk the list of UIDs
		while (null != iter && iter.hasNext() && searchedNode == null) {
			temporary = (MenuItemNode) iter.next();

			MenuItem item = temporary.getMenuItem();

			if (temporary.getID() != null && item != null && item.getUID() != null) {
				// this particular page matches the pagename in the request
				if (temporary.getID().trim().equals(nodeID) || item.getUID().trim().equals(nodeID)) {
					// this is the menuItemNode to redir to
					searchedNode = temporary;
				} else if (!isFirstNode && temporary.getLevel() == 0) {
					// assign first vertical node
					firstNode = temporary;
					isFirstNode = true;
				}
			}
		}

		nodeMap.put(SEARCHED_NODE, searchedNode);
		nodeMap.put(FIRST_NODE, firstNode);

		return nodeMap;
	}

	/**
	 * Get the root node ID for the site from the database.
	 * 
	 * @param context
	 *            Portal context to define the site
	 * @return node id from the table
	 */
//	private String getRootNodeId(String site) {
//		return PortalCommonDAOCacheImpl.getInstance().getPortalHomePageUrl(site);
//	}

	
	public String getNodeHref(PortalContext context, MenuItemNode node) {
		String href = null;
		if (!node.isSpacer()) {
			href = node.getHref();
			// if it is an absolute link then we return it as it is. This will
			// not be the case for links to portal pages
			if (href != null && !href.startsWith("/")) {
				return href;
			}

			// by default we return the node href
			href = getHrefWithContextPath(node.getHref(), context.getBaseContextPath());

			// parse the name of the menu item to see if we need to use a
			// different protocol
			MenuItem item = node.getMenuItem();

			String menuItemProtocol = null;

			// if the item is null then return the site setting
			if (item == null) {
				menuItemProtocol = mPortalHelper.getSiteProtocol(context);
			} else {
				String title = item.getTitle();
				menuItemProtocol = mPortalHelper.getProtocolFromTitle(title, context);
			}

			String requestProtocol = context.getPortalRequest().getRequest().getScheme();

			// create the full URL if the menu item protocol is not the same as
			// the protocol in the request
			if (!menuItemProtocol.equals(requestProtocol)) {
				href = mPortalHelper.buildFullURI(href, menuItemProtocol, context);
			}
		}
		return href;
	}

	public String getHrefWithContextPath(String href, String baseContextPath) {
		if (href == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Href parameter is NULL!");
			}
			return null;
		}
		if (baseContextPath == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("BaseContextPath parameter is NULL!");
			}
			return null;
		}

		String hrefWithContextPath = null;
		if (href.startsWith(baseContextPath)) {
			hrefWithContextPath = href;
		} else {
			hrefWithContextPath = baseContextPath + href;
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("Href with context path: " + hrefWithContextPath);
		}

		return hrefWithContextPath;
	}

	// Checks if the url is an EService URL or not.
	public boolean isEService(MenuItemNode node) {
		if (node == null || node.getJSPPage() == null) {
			return false;
		}
		
		Style eService = node.getJSPPage() ;
		String eServiceTitle = eService.getTitle();
		String eServiceFriendlyId = eService.getFriendlyID();

		// Check if the EService pattern ever occurs.
		String eServicePattern = "SPP EserviceProxy";

		// Check if the EService pattern ever occurs.
		String eServiceFriendlyIdPattern = "eserviceproxy1";

		if ((eServiceTitle.indexOf(eServicePattern) >= 0) || (eServiceFriendlyId.indexOf(eServiceFriendlyIdPattern) >= 0)) {
			return true;
		} else {
			return false;
		}

	}


	/**
	 * Removes the SPAN tag from the given value.
	 * Vignette puts (sometimes?) the SPAN tag in the text coming from the resource bundles
	 * @param value value to remove the SPAN tag from
	 * @return value without SPAN tag
	 */
	public String getValueNoSpan(String value) {
		if (value == null) {
			return "";
		}

		String tmpValue = value.toLowerCase();
		if (!tmpValue.equals("") && tmpValue.indexOf("<span") != -1
				&& tmpValue.indexOf("</span>") != -1) {
			int pos1 = tmpValue.indexOf(">");
			int pos2 = tmpValue.lastIndexOf("<");
			if (pos1 < pos2) {
				value = value.substring(pos1 + 1, pos2);
			} else {
				return "";
			}
		}

		return value;
	}
}
