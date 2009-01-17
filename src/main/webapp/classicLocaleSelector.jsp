<%-----------------------------------------------------------------------------
	classicLocaleSelector.jsp
	
	STYLE: Shared Portal Framework - Classic Locale Selector Style
	STYLE TYPE: Shared Portal Framework - Locale Widget Style Type
	USES HEADER FILE: Yes

Displays the "classic" locale selector.
-----------------------------------------------------------------------------%>

<%---------------------------------------------------------------- IMPORTS --%>

<jsp:directive.page import="com.epicentric.user.User" />
<jsp:directive.page import="com.vignette.portal.website.enduser.utils.ConsoleURIFactory" />

<%----------------------------------------------------------- TAG LIBRARIES--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spf-i18n-portal" uri="http://www.hp.com/spf/i18n/portal" %>

<%----------------------------------------------------------------- SCRIPT --%>

<%----------------------------------------------------------------- MARKUP --%>

<div id="spfLocaleSelector">
<spf-i18n-portal:classicLocaleSelector labelKey="label.text" />
</div>
