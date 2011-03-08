<%-----------------------------------------------------------------------------
	cleansheet_horiz_nav.jsp

	STYLE: Cleansheet Horizontal navigation
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
<%@ taglib prefix="csc" uri="http://frameworks.hp.com/spf/cleansheet-content" %>
<%@ taglib prefix="cs-ext" uri="http://frameworks.hp.com/spf/cleansheet-extensions" %>

<%-----------------------------------------------------------------------------
	Variables
-----------------------------------------------------------------------------%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		
<jsp:scriptlet>

	Utils.initHorzNav(portalContext, pageContext);

</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Template
-----------------------------------------------------------------------------%>

<script type="text/javascript" src="${stylePath}cleansheet_horiz_nav.js"></script>

<c:if test="${! empty menuItemList}" >

<%-- Render primary navigation/tab set --%>

<cs-ext:tabSet selectedIndex="${selectedIndex}" stretchTabs="false" hairline="false">
<c:forEach var="tab" items="${menuItemList}">
	<c:if test="${tab.visible eq true}">
		<cs-ext:tab title="${tab.title}" url="${tab.url}" />
	</c:if>
</c:forEach>
</cs-ext:tabSet>

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

