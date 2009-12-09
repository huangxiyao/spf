<%-----------------------------------------------------------------------------
	hpweb_vert_nav.jsp

	Displays site navigation elements in a vertically-oriented menu.  
	Capable of rendering three levels of navigation elements.
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
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-core-enhanced" %>

<%-----------------------------------------------------------------------------
	Variables
-----------------------------------------------------------------------------%>

<jsp:scriptlet>

String leftMenuTitleDef = Utils.getI18nValue(i18nID, "hpweb.leftMenuTitle",
			portalContext);
String leftMenuTitleUrlDef = Utils.getI18nValue(i18nID, "hpweb.leftMenuTitleUrl",
			portalContext);
String leftPromotionDef = Utils.getI18nValue(i18nID, "hpweb.leftPromotion",
			portalContext);

</jsp:scriptlet>

<%-----------------------------------------------------------------------------
	Template
-----------------------------------------------------------------------------%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />

<jsp:scriptlet>

	List leftMenuItems = null;

	// if the HPWebModel bean has left menu items, use it for hpweb layout
	// left nav, i.e. don't need to generate it from vap configured nav menu.

	if (HPWebModel.getLeftMenuItems().size() == 0) {
		
		int navSplit = -1;
		int maxLevel = 3;
		leftMenuItems = Utils.getConfiguredMenuItems(portalContext, navSplit, maxLevel);

	}

</jsp:scriptlet>

<c:choose>
<c:when test="${! empty HPWebModel.leftMenuItems}" >
	<hpweb:leftMenu menuItems="${HPWebModel.leftMenuItems}">
		<jsp:attribute name="title"><c:out value="${HPWebModel.leftMenuTitle}" default="<%= leftMenuTitleDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="titleUrl"><c:out value="${HPWebModel.leftMenuTitleUrl}" default="<%= leftMenuTitleUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="promotion"><c:out value="${HPWebModel.leftPromotion}" default="<%= leftPromotionDef %>" escapeXml="false" /></jsp:attribute>
	</hpweb:leftMenu>
</c:when>
<c:otherwise>
	<hpweb:leftMenu menuItems="<%= leftMenuItems %>" >
		<jsp:attribute name="title"><c:out value="${HPWebModel.leftMenuTitle}" default="<%= leftMenuTitleDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="titleUrl"><c:out value="${HPWebModel.leftMenuTitleUrl}" default="<%= leftMenuTitleUrlDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="promotion"><c:out value="${HPWebModel.leftPromotion}" default="<%= leftPromotionDef %>" escapeXml="false" /></jsp:attribute>
	</hpweb:leftMenu>
</c:otherwise>
</c:choose>


