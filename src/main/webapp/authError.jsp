<%-- This JSP displays an SPF-standard authorization error page. 

This JSP assumes that the surrounding grid or styles have defined the 
following CSS classes: errorTitle, errorMessage, and errorCode. --%>

<%@ page import="com.epicentric.common.website.SessionUtils"%>
<%@ page import="com.hp.it.spf.xa.i18n.portal.I18nUtility"%>

<%@ taglib uri="http://www.hp.com/spf/i18n/portal" prefix="spf-i18n-portal"%>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects />

<%-- Get page component ID --%>
<%    
    String i18nID = portalContext.getCurrentStyle().getUID();
%>

<%-- Prepare CSS URL --%>
<%
	String cssFile = portalContext.getCurrentSite().getDNSName() + "AuthError.css";
	if (I18nUtility.getLocalizedFileAsStream(portalContext, cssFile, false) == null) 
		cssFile = "authError.css";
	String cssURL = I18nUtility.getLocalizedFileURL(portalContext, cssFile, false);
%>

<%-- Output page data --%>
<link href="<%= cssURL %>" rel="stylesheet" type="text/css">

<div style="padding-top: 10px; margin-left: 10px">
<h3 class="spf-autherror-title">
	<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="autherror.title.text" />
</h3>
<table border="0" cellspacing="0">
	<tr>
		<td>
			<p class="spf-autherror-message">
				<sp-i18n-portal:i18nValue stringID="<%= i18nID %>" key="autherror.notice.text" />
			</p>
		</td>
	</tr>
	<tr>
		<td>
			<p class="spf-autherror-code">
				<sp-i18n-portal:i18nValue stringID="<%= i18nID %>" key="autherror.errorcode.text" />
			</p>
		</td>
	</tr>
</table>
</div>
