<%-----------------------------------------------------------------------------
	hpweb_horiz_nav.jsp

	STYLE: HPWeb Horizontal navigation
	STYLE TYPE: Horizontal navigation
	USES HEADER FILE: Yes

	Horizontal menu style that displays primary navigation tabs, secondary
	navigation row, and flyout menus.
-----------------------------------------------------------------------------%>

<%-----------------------------------------------------------------------------
	Imports
-----------------------------------------------------------------------------%>


<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="java.util.ArrayList" />
<jsp:directive.page import="java.util.Iterator" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemUtils" />
<jsp:directive.page import="com.epicentric.common.website.MenuItemNode" />
<jsp:directive.page import="com.epicentric.settings.Settings" />
<jsp:directive.page import="com.epicentric.site.SiteSettings" />
<jsp:directive.page import="com.vignette.portal.util.StringUtils" />
<jsp:directive.page import="com.hp.frameworks.wpa.hpweb.MenuItem" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.hpweb.Utils" />

<%-----------------------------------------------------------------------------
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hpweb-content" uri="http://frameworks.hp.com/wpa/hpweb-content-enhanced" %>
<%@ taglib prefix="hpweb-ext" uri="http://frameworks.hp.com/wpa/hpweb-extensions" %>

<%-----------------------------------------------------------------------------
	Variables
-----------------------------------------------------------------------------%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		
<jsp:scriptlet>

Style currentStyle = portalContext.getCurrentStyle();
	
pageContext.setAttribute("stylePath", portalContext.getPortalHttpRoot() + 
		currentStyle.getUrlSafeRelativePath());

String whiteCaretImg = 
	"http://welcome.hp-ww.com/img/hpweb_1-2_tbl_sort_wht.gif";
String blackCaretImg = 
	"http://welcome.hp-ww.com/img/hpweb_1-2_tbl_sort_blk.gif";
String spacerImg =
	"http://welcome.hp-ww.com/img/s.gif";

if (pageContext.getRequest().getScheme().equals("https")) {
	whiteCaretImg = 
		"https://secure.hp-ww.com/img/hpweb_1-2_tbl_sort_wht.gif";
	blackCaretImg = 
		"https://secure.hp-ww.com/img/hpweb_1-2_tbl_sort_blk.gif";
	spacerImg =
		"https://secure.hp-ww.com/img/s.gif";
}

pageContext.setAttribute("whiteCaretImg", whiteCaretImg);
pageContext.setAttribute("blackCaretImg", blackCaretImg);
pageContext.setAttribute("spacerImg", spacerImg);

pageContext.setAttribute("helpTextDef", Utils.getI18nValue(i18nID, "hpweb.helpText",
		portalContext));
pageContext.setAttribute("helpUrlDef",  Utils.getI18nValue(i18nID, "hpweb.helpUrl",
		portalContext));

List menuItemList = HPWebModel.getTopMenuItems();

// if the HPWebModel bean has top menu items, use it for hpweb layout
// top nav, i.e. don't need to generate it from vap configured nav menu.

if (menuItemList == null || menuItemList.size() == 0) {
	
	int navSplit = 0;
	int maxLevel = 3;
	menuItemList = Utils.getConfiguredMenuItems(portalContext, navSplit, maxLevel);
}

// We should expect that only one MenuItem node has its highLighted flag 
// set to true.   Find that selected node and save the tab index it
// belongs under.   Also if it is a 3rd level node, set the highlight flag
// of its parent node (a button item) to true.

int selectedIndex = 0;
boolean done = false;
for (int tabIndex=0; tabIndex<menuItemList.size(); tabIndex++) {
	MenuItem tab = (MenuItem) menuItemList.get(tabIndex);
	if (tab.isVisible() == false) {
		continue;
	}
	if (tab.isHighlighted()) {
		// found selected node in tab item, don't need to do anymore
		selectedIndex = tabIndex;
		break;
	}
	int buttonCount = tab.getSubMenuItems().size();
	if (buttonCount > 0) {
		for (int buttonIndex=0; buttonIndex<buttonCount; buttonIndex++) {
			MenuItem button = 
				(MenuItem) tab.getSubMenuItems().get(buttonIndex);
			if (button.isVisible() == false) {
				continue;
			}
			if (button.isHighlighted()) {
				// found selected node in button item, save tab index
				selectedIndex = tabIndex;
				done = true;
				break;
			}
			int flyoutItemCount = button.getSubMenuItems().size();
			if (flyoutItemCount > 0) {
				for (int flyoutIndex=0; flyoutIndex<flyoutItemCount; flyoutIndex++) {
					MenuItem flyoutItem = 
						(MenuItem) button.getSubMenuItems().get(flyoutIndex);
					if (button.isVisible() == false) {
						continue;
					}
					if (flyoutItem.isHighlighted()) {
						// found selected node in flyout item, save tab
						// index and button (parent) item's  highlight flag 
						// to true.
						selectedIndex = tabIndex;
						button.setHighlighted(true);
						done = true;
						break;
					}
				}
			}
		}
	}
	if (done)
		break;
}

pageContext.setAttribute("menuItemList", menuItemList);
pageContext.setAttribute("selectedIndex", selectedIndex);

</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Template
-----------------------------------------------------------------------------%>

<script type="text/javascript" src="${stylePath}hpweb_horiz_nav.js"></script>

<c:if test="${! empty menuItemList}" >

<%-- Render primary navigation/tab set --%>

<hpweb-ext:tabSet selectedIndex="${selectedIndex}" stretchTabs="false" hairline="false">
<c:forEach var="tab" items="${menuItemList}">
	<c:if test="${tab.visible eq true}">
		<hpweb-ext:tab title="${tab.title}" url="${tab.url}" />
	</c:if>
</c:forEach>
</hpweb-ext:tabSet>

<%-- Render secondary navigation/button bar --%>

<div id="horzNavButtonBar">
<c:forEach var="tab" items="${menuItemList}" varStatus="tabIndex">

	<c:if test="${selectedIndex eq tabIndex.count-1 && tab.visible eq true &&  fn:length(tab.subMenuItems) > 0}" >
		<c:forEach var="button" items="${tab.subMenuItems}">
		
			<c:if test="${button.visible eq true}">
		
				<%-- Check if there is any visible flyout menu items,
						so know how to render parent button. --%>
				
				<c:set var="hasFlyout" value="false" />
				<c:forEach var="flyout" items="${button.subMenuItems}">
					<c:if test="${flyout.visible eq true}">
						<c:set var="hasFlyout" value="true" />
					</c:if>
				</c:forEach>
	
				<c:choose>
				<c:when test="${hasFlyout eq true}" >

					<div class="horzNavButton" onmouseover="expand(this, '${blackCaretImg}');"
						onmouseout="collapse(this, ${button.highlighted}, '${blackCaretImg}', '${whiteCaretImg}');">

						<c:if test="${button.highlighted eq true}">
							<a class="menuNormal topMenuItem active" href="${button.url}">${button.title}<img src="${spacerImg}" width="10" height="1" border="0"><img src="${blackCaretImg}" border="0" /></a>
						</c:if>
						<c:if test="${button.highlighted eq false}">
							<a class="menuNormal topMenuItem" href="${button.url}">${button.title}<img src="${spacerImg}" width="10" height="1" border="0"><img src="${whiteCaretImg}" border="0" /></a>
						</c:if>
				
						<%-- Render flyout menu --%>
						<div class="menuNormal horzNavFlyoutMenu">
							<c:forEach var="flyout" items="${button.subMenuItems}">
					
								<c:if test="${flyout.visible eq true}">
									<a class="dropMenuItem" href="${flyout.url}">&raquo; ${flyout.title}</a>
								</c:if>
							
							</c:forEach>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="horzNavButton">
						<%-- Render a link that has no flyout menu --%>
						<a class="topMenuItem noFlyout <c:if test="${button.highlighted eq true}">active</c:if>" href="${button.url}">${button.title}</a>
					</div>
				</c:otherwise>
				</c:choose>
			
			</c:if>
		</c:forEach>	
	</c:if>

</c:forEach>

	<%-- Render Help button if it is specified  --%>
	
	<c:if test="${(!empty HPWebModel.helpUrl || !empty helpUrlDef) && (! empty HPWebModel.helpText || !empty helpTextDef)}">
		<div id="horzNavHelpButton">
			<a class="topMenuItem" href="<c:out value="${HPWebModel.helpUrl}" default="${helpUrlDef}" escapeXml="false" />"><c:out value="${HPWebModel.helpText}" default="${helpTextDef}" /></a>
		</div>
	</c:if>

	<%-- This div is needed for FF to render the background color between 
			the Help button and the previous button in the button bar.  
	--%>
	<div id="horzNavButtonBarBackground">
		&nbsp;
	</div>

</div>

<div style="clear:both;"/>

</c:if>

