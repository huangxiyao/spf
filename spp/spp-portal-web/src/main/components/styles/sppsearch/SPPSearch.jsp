<%@page import="com.epicentric.common.website.I18nUtils" %>
<%@page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@page import="com.hp.spp.portal.common.helper.PortalURIHelper" %>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper" %>
<%@page import="com.hp.spp.portal.common.site.SiteManager"%>
<%@page import="com.hp.spp.portal.common.util.HPUrlLocator"%>
<%@page import="com.hp.spp.profile.Constants"%>
<%@page import="com.hp.spp.config.Config"%>
<%@page import="com.hp.spp.config.ConfigException"%>

<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="vgn-tags" prefix="vgn-portal" %>

<vgn-portal:defineObjects/>
<%
	String i18nID = new LocalizationHelper().getCommonI18nID(portalContext);

	ProfileHelper profileHelper = new ProfileHelper();
	PortalURIHelper portalURIHelper = new PortalURIHelper();

	String serverURL = "http://".concat(request.getServerName()) ;

	String contactHP_URI = null;
	String advancedSearch_URI = null;
	String searchHelp_URI = null;
	String portletId = null;
	boolean authenticatied = profileHelper.isUserAuthenticated(portalContext);
	String searchgroups = profileHelper.getProfileValue(portalContext, Constants.MAP_USERGROUPS);
	if( null != searchgroups ){
			if( searchgroups.contains("NO_SEARCH") ){
				authenticatied = false;
			}
	}

	

	try {
		contactHP_URI = portalURIHelper.getFullPortalURL(portalContext, "Contact HP") ;
		if(contactHP_URI == null) {
			contactHP_URI = portalURIHelper.getFullPortalTemplateURL(portalContext, "sppContactHP") ;
		}

		advancedSearch_URI = portalURIHelper.getFullPortalURL(portalContext, "Advanced Search ") ;
		if (advancedSearch_URI == null) {
			advancedSearch_URI = portalURIHelper.getFullPortalTemplateURL(portalContext, "Advanced Search") ;
		}
		
		searchHelp_URI = portalURIHelper.getFullPortalURL(portalContext, "Search_Help");
		if( searchHelp_URI == null ) {
			searchHelp_URI = portalURIHelper.getFullPortalTemplateURL(portalContext, "Search_Help") ;
		}
	}
	catch(Throwable e)	{
		e.printStackTrace();

	}
	String pageId = null;
	String target = advancedSearch_URI;
	int end = target.indexOf("/menuitem.");
	if (end > -1) {
		String urlRootPage = target.substring(0, end);
		pageId = target.replaceFirst(urlRootPage + "/menuitem.",	"");
		pageId = pageId.replaceAll("/", "");
	}

	// Retrieve the language name of the end-user
	//String langCode = profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE).substring(0,2) ;
	String langCode = profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE);
	if(langCode == null || "".equals(langCode))
		langCode = "en" ;
	else
		//langCode = langCode.substring(0, 2).toLowerCase() ;
		langCode = langCode.toLowerCase() ;
	String language = I18nUtils.getValue(i18nID, "language_".concat(langCode), "English", request) ;

	// Retrieve the country name of the end-user

	//Complete fix for country code passed to search.
	String countryCode = profileHelper.getProfileValue(portalContext, Constants.MAP_COUNTRY) ;

	String countrySearch = countryCode;

	try {
		if(countryCode == null || "".equals(countryCode)){
			countryCode = "us";
			countrySearch = countryCode;
		}else if(Config.getValue("SPP.search.CaribbeanCode").toLowerCase().contains(countryCode.toLowerCase())){
			countryCode = "lamerica_nsc_carib";
			langCode = "en";
		}else if(Config.getValue("SPP.search.CentralAmericaCode").toLowerCase().contains(countryCode.toLowerCase())){
			countryCode = "lamerica_nsc_cnt_amer";
			langCode = "es";
		}else if("GB".equalsIgnoreCase(countryCode)){
			countryCode = "uk";
			countrySearch = "gb";
			langCode = "en";
		}	
	} catch (ConfigException e) {
		if(countryCode == null || "".equals(countryCode)){
			countryCode = "us";
			countrySearch = countryCode;
		}
	}

	countryCode = countryCode.toLowerCase() ;

	String country = I18nUtils.getValue(i18nID, "country_".concat(countrySearch.toLowerCase()), "United States", request) ;

	//Temporary fix for handling traditional and simplified Chinese
	if("zh_cn".equalsIgnoreCase(langCode)){
		langCode="zh-hans";
	}
	else if("zh_tw".equalsIgnoreCase(langCode)){
		langCode="zh-hant";
	}	

	String siteName = profileHelper.getProfileValue(portalContext, Constants.MAP_SITE) ;

	String hpImagesURL = HPUrlLocator.getWelcomeUrl(portalContext, false) + "/";

    portletId = SiteManager.getInstance().getSite(siteName).getSearchPortletID();
    if(portletId == null){
        throw new IllegalArgumentException("PortletId NOT Found for site: "+siteName);
    }

    String sppSearchActionURL = "/portal/site/"+siteName+"/template.PAGE/action.process/menuitem."+pageId+"/?javax.portlet.action=true&javax.portlet.tpst="+portletId+"&javax.portlet.begCacheTok=com.vignette.cachetoken&javax.portlet.endCacheTok=com.vignette.cachetoken";
    
    String searchButtonAltText = I18nUtils.getValue(i18nID, "searchButtonAltText", "Search", request);
    if( searchButtonAltText != null ){
    	if (searchButtonAltText.contains("SPAN")){
    		searchButtonAltText = "Search";
    	}
    }
    String searchInputAltText = I18nUtils.getValue(i18nID, "searchInputAltText", "Criteria", request);
    if( searchInputAltText != null ){
    	if (searchInputAltText.contains("SPAN")){
    		searchInputAltText = "Criteria";
    	}
    }
%>

<script type="text/javascript">

function submitSearch() {
	var searchForm = document.forms['searchForm'] ;
	var siteName = "<%=siteName%>";
	
	searchForm.searchString.value=trim(searchForm.qt.value);
	searchForm.qt.value = searchForm.searchString.value;
	

	if(searchForm.searchString != null && trim((searchForm.searchString.value)).length <= 0){
		searchForm.searchStringId.focus();
		return false;
	}
	<%
		if(authenticatied){
	%>
	setCookie("sppSearchString", encodeUTF8(searchForm.searchString.value), null, "/portal/site/<%=siteName%>", null, null);
	<%
			}
	%>

	
	if((searchForm.searchScope[1] == null) || (searchForm.searchScope[1].value == 1 && searchForm.searchScope[1].checked) ){
		
		searchForm.action = "http://www.hp.com/search/";
		searchForm.method="GET";
		searchForm.target="_blank";
		return true;
	} 
	else 
	{
		searchForm.target="_self";
		searchForm.method="POST";
		if(siteName=="sppqa")
		{
			searchForm.action = "/portal/jsp/SPP/search/testGPPSearch.jsp";
		}
		else
		{
			searchForm.action="<%=sppSearchActionURL%>";		
		}
		return true;
	}
}

function setCookie( name, value, expires, path, domain, secure ) 
{
	document.cookie = name + "=" +( value ) +"; path=" + path;
}

function setActionTarget(){
	var searchForm = document.forms['searchForm'] ;
	searchForm.action = "http://www.hp.com/search/";
	searchForm.method="GET";
	searchForm.target="_blank";
}

function trim(sString) 
{	
	while ((sString.substring(0,1) == ' ')&&(sString.length>0)){
		sString = sString.substring(1, sString.length);
	}	
	return sString;
}


function encodeUTF8(string){
	return encodeURIComponent(string);
}

function decodeUTF8(utftext) {
    return decodeURIComponent(utftext);
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return '';
}

function displayHelp(country,language,contextPath){
    document.feedback.country.value=country;
    document.feedback.language.value=language;            
    var path = contextPath+"/GetHelpPageServlet?country="+country+"&language="+language;
    window.open(path,'Help','top=50,width=600,height=400,toolbar=no,  status=no,menubar=no,scrollbars=yes, resizable=yes, screenX=200,screenY=100');
}            

function disableAdvancedSearch(){
	document.getElementById('advancedSearch').style.display='none';
}
function enableAdvancedSearch(){
	document.getElementById('advancedSearch').style.display='inline';
}

</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td bgcolor="#E7E7E7">
			<table border="0" cellpadding="0" cellspacing="0" width="740">
				<tr>
					<td width="1" valign="top"><img src="<%=hpImagesURL%>img/s.gif" width="1" height="48" alt="" class="decoration"></td>
					<td width="100" style="text-align:left;vertical-align:center;padding-left:20px;" class="color003366bld"><% if(contactHP_URI != null) { %>&raquo;&nbsp;<a href="<%=contactHP_URI%>"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="contactHP" defaultValue="Contact HP" /></a><% } %></td>
					<td width="639" style="text-align:right;vertical-align:center;">
					<form name="searchForm" action="" onsubmit="return submitSearch()" method="POST">
							<%-- For HP.com --%>
							<input type="hidden" name="cc" value="<%=countryCode.toLowerCase() %>">
							<input type="hidden" name="lang" value="<%=langCode.toLowerCase() %>">
							<input type="hidden" name="hps" value="<%=I18nUtils.getValue(i18nID, siteName, "", request)%> Section only">
							<input type="hidden" name="hpn" value="Return to <%=I18nUtils.getValue(i18nID, siteName, "", request)%>">
							<input type="hidden" name="qp" value="">
							<input type="hidden" name="hpr" value="<%=serverURL%><%=request.getRequestURI()%>" >
							<input type="hidden" name="hpa" value="">
							<input type="hidden" name="hpo" value="hphqglobalsite,hphqnasite,hpnetserver">
							<input type="hidden" name="encodedKeyword" value="">
							<input type="hidden" name="charset" value="utf-8">
							<input type="hidden" name="actionFromUI" value="submit">
							<input type="hidden" name="fromMainForm" value="true">
							<input type="hidden" name="searchString" value="">
							<%-- For SPP Search --%>
							<% if (authenticatied){ %>
<%--
							<input type="hidden" name="spp_site" value="<%=siteName%>">--%>
							<input type="hidden" name="hpp_id" value="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_HPPID)%>">
							<input type="hidden" name="Audience" value="yes">
							<% } %>

							<table border="0" cellpadding="0" cellspacing="0" align="right">
								<tr class="decoration">
									<td colspan="4"><img src="<%=hpImagesURL%>img/s.gif" width="1" height="2" alt="" border="0"></td>
								</tr>
								<tr>
									<td align="left" class="bold">
										<label for="textbox1">
											<vgn-portal:i18nValue stringID="<%=i18nID%>" key="searchTitle" defaultValue="Search" />:
										</label>
									</td>
									<td valign="top">
										<img src="<%=hpImagesURL%>img/s.gif" width="4" height="1" alt="" class="decoration">
									</td>
									<td align="left" valign="top">
										<input type="text" id='searchStringId' name="qt" size="26" maxlength="250" id="textbox1" value="" alt='<%=searchInputAltText %>' >
										<img src="<%=hpImagesURL%>img/s.gif" width="4" height="1" alt="">
										<a><input type="Image" name="submitImg" onclick="return submitSearch()"  src="<%=hpImagesURL%>img/hpweb_1-2_arrw_sbmt.gif" border="0" alt='<%=searchButtonAltText %>'></a>
									</td>
									<td align="left">
										<img src="<%=hpImagesURL%>img/s.gif" width="20" height="1" alt="" class="decoration">
									</td>
									<td class="color003366" align="left">
											<%
												if (authenticatied){
												
											%>
												<SPAN  id="advancedSearch"><a href="<%=advancedSearch_URI%>" class="small">
												<vgn-portal:i18nValue stringID="<%=i18nID%>" key="advancedSearchTitle" defaultValue="Advanced Search" />
												</a></SPAN>&nbsp;
												<br>
												<a href="<%=searchHelp_URI %>" class="small" onClick="popup = window.open('<%=searchHelp_URI %>', 'PopupPage', 'height=450,width=500,scrollbars=yes,resizable=yes'); return false" target="_blank">
													<vgn-portal:i18nValue stringID="<%=i18nID %>" key="helpLabel" defaultValue="Help" />
												</a>
											<%	} %>

									</td>
								</tr>
								<tr>
									<td align="left">
										<img src="<%=hpImagesURL%>img/s.gif" width="20" height="1" alt="" class="decoration">
									</td>
									<td align="left">
										<img src="<%=hpImagesURL%>img/s.gif" width="20" height="1" alt="" class="decoration">
									</td>
									<td align="left">
										<% 
											if (authenticatied )
											{ 
										%>
											<input type="radio" name="searchScope" value="0" checked="checked" class="srchradbtn" onclick="enableAdvancedSearch()">
											<font color="#333333" face="Arial" class="smallbold"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="sitename" defaultValue="" /></font>
											<input type="radio" name="searchScope" value="1" onclick="disableAdvancedSearch()" class="srchradbtn">
											<font color="#333333" face="Arial" class="small"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="hpDotCom" defaultValue="HP.com" />&nbsp;<%=country%></font>
										<% 
											} 
											else
											{
										%>
										<input type="hidden" name="searchScope" value="1">
										<%
											}
										%>
										
									</td>
									<td align="left">
										<img src="<%=hpImagesURL%>img/s.gif" width="20" height="1" alt="" class="decoration">
									</td>
								</tr>
								<tr>
									<td></td>
									<td align="left" colspan="3" class="small"></td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<script type="text/javascript">
document.forms['searchForm'].searchStringId.value=decodeUTF8(readCookie('sppSearchString'));
</script>