<%-----------------------------------------------------------------------------
	spf_metrics.jsp
-----------------------------------------------------------------------------%>
<%@ taglib uri = "vgn-tags" prefix = "vgn-portal"%>
<vgn-portal:defineObjects/>

<%@ page import = "com.hp.it.spf.xa.i18n.portal.I18nUtility"%>
<%@ page import = "com.hp.it.spf.sso.portal.AuthenticationUtility"%>
<%@ page import = "com.hp.serviceportal.framework.common.utils.CountryRegionMapping"%>

<%@ page import = "com.epicentric.user.User"%>
<%@ page import = "com.epicentric.site.Site"%>
<%@ page import = "com.hp.websat.timber.logging.Log"%>

<jsp:useBean id="HPWebModel" scope="request" 
            class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />

<%
String s_language = I18nUtility.localeToLanguageTag(I18nUtility.getLocale(request));
if (s_language == null) {
    s_language = "en-US";
}
%>

<scriptlet>
      Properties metaInfos = HPWebModel.getMetaInfos();
      metaInfos.setProperty("target_country", "<%=s_language%>");
</scriptlet>


<%
User user = portalContext.getCurrentUser();

String s_country = "unknown";
if (!user.isGuestUser()) { // means that this user is logged in
    s_country = (String)user.getProperty("country");
    if (s_country == null || (s_country.trim().length() == 0)) {
        s_country = "unknown";
    }
}

String s_userType = (String)AuthenticationUtility.getSPUserRole(user);
if (s_userType == null) {
    s_userType = "unknown";
}

String s_region = CountryRegionMapping.getRegionFromCountryCode(s_country);
if (s_region == null) {
    s_region = "unknown";
}
    
String s_site = "unknown";
Site currentSite = portalContext.getCurrentSite();
if (currentSite != null) {
    s_site = currentSite.getDNSName();
}

String s_accessType =(String)request.getHeader("AccessType");
if (s_accessType == null || s_accessType.trim().length() == 0) {
     s_accessType = "Direct";
}

%>

<script language="JavaScript">
var s_prop26= "<%= s_userType %>|<%= s_site %>|<%= s_accessType %>|<%= s_region %>";
var s_pageName = document.title;
</script>


<script language="JavaScript">
function s_hp_sendExitLinkEvent(addlAccount,linkName){
    ns=s_account;
    if(addlAccount!=null && addlAccount.length>0)ns+=","+ addlAccount; 		s_linkType="e"; s_lnk=true; s_linkName=linkName; void(s_gs(ns));
}
</script>

<script language="JavaScript" src="https://secure.hp-ww.com/cma/segment/ww/ces/metrics_ServicePortal.js"></script>

