
<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator" %>
<%@ page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@ page import="com.hp.spp.portal.common.helper.PortalHelper" %>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<vgn-portal:defineObjects/><%
	//Style _thisStyleObject = portalContext.getCurrentStyle();
	//String _pathToSecondaryFiles = portalContext.getPortalHttpRoot() + portalContext.getCurrentStyle().getUrlSafeRelativePath();
%>

<% 
	String hpImagesURL = HPUrlLocator.getWelcomeUrl(portalContext,false) + "/";
	String uri = new PortalHelper().getSafeRequestURL(request);
	uri += (uri.indexOf('?') == -1 ? "?printable=x" : "&printable=x");
	
	LocalizationHelper localizationHelper = new LocalizationHelper() ;
	String i18nID = localizationHelper.getCommonI18nID(portalContext);
//	String i18nID 	= portalContext.getCurrentStyle().getUID();

%>

<table border="0" cellpadding="0" cellspacing="0" width="170">
<tr>
    <td align="center" valign="bottom" width="170" bgcolor="#F0F0F0"><img src="<%=hpImagesURL %>img/hpweb_1-2_prnt_icn.gif" width="19" height="13" alt="" border="0"><a href="<%=uri%>" class="udrlinebold"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="print" defaultValue="Printable version" /></a></td>
</tr>
<tr>
    <td align="center" valign="bottom" width="170" bgcolor="#F0F0F0"><img src="<%=hpImagesURL %>img/s.gif" width="1" height="20" alt="" border="0"></td>
</tr>
</table>
