<%-- This JSP displays an SPF-standard generic error page for
federated users who have encountered a federation error.

This JSP references styles defined in either <site>FedLaunchError.css 
or the default file, fedLaunchError.css.  These are assumed to be
secondary support files in this component. --%>

<%@ page import="com.hp.it.spf.xa.misc.portal.Consts"%>
<%@ page import="com.hp.it.spf.xa.i18n.portal.I18nUtility"%>

<%@ taglib uri="http://www.hp.com/spf/i18n/portal" prefix="spf-i18n-portal"%>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects/>

<%
	// get the try-again URL
	String retryURL = (String)request.getAttribute(Consts.REQUEST_ATTR_FED_ERROR_RETRY_URL);
	// get the current component ID
	String i18nID = portalContext.getCurrentStyle().getUID();
	// get the proper CSS URL
	String cssFile = portalContext.getCurrentSite().getDNSName() + "FedLaunchError.css";
	if (I18nUtility.getLocalizedFileAsStream(portalContext, cssFile, false) == null) 
		cssFile = "fedLaunchError.css";
	String cssURL = I18nUtility.getLocalizedFileURL(portalContext, cssFile, false);	
%>

<link href="<%= cssURL %>" rel="stylesheet" type="text/css">

<p class="spf-fedlauncherror-section-header">
	<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_launch_error.1.text"/>
</p>
<p class="spf-fedlauncherror-section-text">
	<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_launch_error.2.text"/><br/>
	<a href="<%= retryURL %>">
		<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_launch_error.3.text"/>
	</a>
	<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_launch_error.4.text"/>
</p>


	