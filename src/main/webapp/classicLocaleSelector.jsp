<%-----------------------------------------------------------------------------
	classicLocaleSelector.jsp
	
	STYLE: Shared Portal Framework - Classic Locale Selector Style
	STYLE TYPE: Shared Portal Framework - Locale Widget Style Type
	USES HEADER FILE: Yes

Displays the "classic" locale selector when multiple locales are available.
If only one locale is available, displays the "classic" locale indicator.
-----------------------------------------------------------------------------%>

<%---------------------------------------------------------------- IMPORTS --%>

<jsp:directive.page import="com.epicentric.user.User" />
<jsp:directive.page import="com.vignette.portal.website.enduser.utils.ConsoleURIFactory" />
<jsp:directive.page import="com.hp.it.spf.xa.i18n.portal.I18nUtility" />

<%----------------------------------------------------------- TAG LIBRARIES--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spf-i18n-portal" uri="http://www.hp.com/spf/i18n/portal" %>

<vgn-portal:defineObjects/>

<%----------------------------------------------------------------- SCRIPT --%>

<jsp:scriptlet>
	// Record page attribute for whether multiple locales exist.
	boolean enableSelector = I18nUtility.multipleLocalesEnabled(request);
	pageContext.setAttribute("enableSelector", Boolean.valueOf(enableSelector));
	
	// Record page attribute for the proper CSS file URL.
	String cssFile = portalContext.getCurrentSite().getDNSName() + "ClassicLocaleSelector.css";
	if (I18nUtility.getLocalizedFileAsStream(portalContext, cssFile, false) == null) 
		cssFile = "classicLocaleSelector.css";
	String cssURL = I18nUtility.getLocalizedFileURL(portalContext, cssFile, false);
	pageContext.setAttribute("cssURL", cssURL);
</jsp:scriptlet>

<%----------------------------------------------------------------- MARKUP --%>

<link href="<%= cssURL %>" rel="stylesheet" type="text/css">

<c:choose>
	<c:when test="${enableSelector}">
		<div id="spfLocaleSelector">
			<span class="spf-localeselector-classic">
				<spf-i18n-portal:classicLocaleSelector labelKey="label.text" />
			</span>
		</div>
	</c:when>
	<c:otherwise>
		<div id="spfLocaleIndicator">
			<span class="spf-localeindicator-classic">
				<spf-i18n-portal:classicLocaleIndicator />
			</span>
		</div>
	</c:otherwise>
</c:choose>
