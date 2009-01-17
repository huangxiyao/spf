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

<%----------------------------------------------------------- TAG LIBRARIES--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spf-i18n-portal" uri="http://www.hp.com/spf/i18n/portal" %>

<vgn-portal:defineObjects/>

<%----------------------------------------------------------------- SCRIPT --%>

<%----------------------------------------------------------------- MARKUP --%>

<div id="spfLocaleIndicator">
<spf-i18n-portal:classicLocaleIndicator />
</div>
