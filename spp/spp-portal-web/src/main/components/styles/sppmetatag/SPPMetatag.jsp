<%@ page import="com.hp.spp.portal.common.helper.ProfileHelper,com.hp.spp.portal.common.helper.SiteURLHelper,com.hp.spp.portal.login.business.preprocess.Localizer,com.hp.spp.hpp.supporttools.HPPHeaderHelper,com.hp.spp.portal.login.business.postprocess.SPPSessionManager" %>
<%@ page import="com.hp.spp.portal.common.site.Site,com.hp.spp.portal.common.site.SiteManager" %>
<%@ page import="com.hp.spp.profile.Constants,com.hp.spp.portal.login.model.SPPGuestUser" %>
<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<vgn-portal:defineObjects/>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta name="segment" content="" >
<meta name="page_content" content="">
<meta name= "hp_design_version" content= "">
    <%
    if(request.getRequestURI().startsWith(new StringBuilder(request.getContextPath()).append("/site/public").toString())){
     Localizer localizer = new Localizer();
	 String siteName=SiteURLHelper.determineMasterSite(request);
     String siteIdentifier = localizer.getSiteIdentifier(siteName);
     //Fetch appropriate guest user from SPP_LOCALE table
     SPPGuestUser sppGuestLocal = localizer.getSSOGuestUser(request);
     String guestUser = sppGuestLocal.getGuestUser();
     String preferredLanguageCode = sppGuestLocal.getPreferredLanguageCode();
     //Fetch language/country ':' separated values.xx-yy:zz
     String[] langCountryComboForSession =localizer.getGuestUserLangCountry(guestUser);
     String sessionLanguage = langCountryComboForSession[0];
     String sessionCountry = langCountryComboForSession[1];
     String webSectionIdLandPage = SiteManager.getInstance().getSite(siteName).getWebSectionID();
    %>
    <meta name="Language " content="<%=sessionLanguage %>" >
    <meta name="Country " content="<%=sessionCountry %>" >
	<%
	 if(webSectionIdLandPage != null && !"".equals(webSectionIdLandPage)){
	%>
    <meta name="web_section_id" content="<%=webSectionIdLandPage %>" >
    <%}}else{
     ProfileHelper profileHelper = new ProfileHelper();
     String siteName=profileHelper.getProfileValue(portalContext, Constants.MAP_SITE);
     String webSectionIdHomePage = SiteManager.getInstance().getSite(siteName).getWebSectionID();
	 if(webSectionIdHomePage != null && !"".equals(webSectionIdHomePage)){
    %>
		<meta name="web_section_id" content="<%=webSectionIdHomePage %>" >
	<%}%>
    <meta name="Country" content="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_COUNTRY)%>" >
    <meta name="language" content="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE)%>" >
    <%}%>
<meta name="product_service_solution_hierarchy " content="">
<meta name="user_type" content="">
<meta name="description" content="">
<meta name="Keywords" content="">
<meta name="content_country_applicability" content="">
