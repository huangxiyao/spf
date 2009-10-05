<%-- This JSP displays an SPF-standard logout confirmation page for
federated users.

This JSP references styles defined in either <site>FedLogoutConfirm.css 
or the default file, fedLogoutConfirm.css.  These are assumed to be
secondary support files in this component. --%>

<%@ page import="com.hp.it.spf.xa.misc.portal.Consts"%>
<%@ page import="com.hp.it.spf.xa.i18n.portal.I18nUtility"%>

<%@ taglib uri="/spf-i18n-portal.tld" prefix="spf-i18n-portal"%>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects/>
	
<%
	// get the resume URL
	String resumeURL = (String)request.getAttribute(Consts.REQUEST_ATTR_FED_LOGOUT_RESUME_URL);
	// get the current component ID
	String i18nID = portalContext.getCurrentStyle().getUID();
	// get the proper CSS URL
	String cssFile = portalContext.getCurrentSite().getDNSName() + "FedLogoutConfirm.css";
	if (I18nUtility.getLocalizedFileName(portalContext, cssFile, false) == null) 
		cssFile = "fedLogoutConfirm.css";
	String cssURL = I18nUtility.getLocalizedFileURL(portalContext, cssFile, false);	
%>

<link href="<%= cssURL %>" rel="stylesheet" type="text/css">

<p class="spf-fedlogoutconfirm-section-header">
	<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_logout.section.1.text"/>		
</p>
<p class="spf-fedlogoutconfirm-section-text">
	<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_logout.section.2.text"/><br/>
	<span class="spf-fedlogoutconfirm-section-text-note"><spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_logout.section.3.text"/></span>
	<span class="spf-fedlogoutconfirm-section-text-note-details"><spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_logout.section.4.text"/></span>
</p>
<table>
	<tr>
		<td colspan="2" class="spf-fedlogoutconfirm-section-text">
			<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_logout.section.5.text"/>
		</td>	
	</tr>
	<tr>
		<td rowspan="2" width="20" />					
		<td class="spf-fedlogoutconfirm-section-text-link">					
			<a href="<%= resumeURL %>">
				<blockquote><li><spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_logout.section.6.text"/></li></blockquote>
			</a>						
		</td>
	</tr>
	<tr>			
		<td class="spf-fedlogoutconfirm-section-text-link">					
			<a href="javascript:window.close()">
				<blockquote><li><sp-i18n-portal:i18nValue stringID="<%= i18nID %>" key="fed_logout.section.7.text"/></li></blockquote>
			</a>						
		</td>
	</tr>
</table>
