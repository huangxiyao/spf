<%-----------------------------------------------------------------------------
	classicLocaleIndicator.jsp
	
	STYLE: Shared Portal Framework - Classic Locale Indicator Style
	STYLE TYPE: Shared Portal Framework - Locale Widget Style Type
	USES HEADER FILE: Yes

Displays the "classic" locale indicator.
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
	// Record page attribute for the proper CSS file URL.
	String cssFile = portalContext.getCurrentSite().getDNSName() + "ClassicLocaleIndicator.css";
	if (I18nUtility.getLocalizedFileStream(portalContext, cssFile) == null) 
		cssFile = "classicLocaleIndicator.css";
	String cssURL = I18nUtility.getLocalizedFileURL(portalContext, cssFile);
	pageContext.setAttribute("cssURL", cssURL);
</jsp:scriptlet>

<%----------------------------------------------------------------- MARKUP --%>

<link href="<%= cssURL %>" rel="stylesheet" type="text/css">

<div id="spfLocaleIndicator">
	<span class="spf-localeindicator-classic">
		<spf-i18n-portal:classicLocaleIndicator />
	</span>
</div>
