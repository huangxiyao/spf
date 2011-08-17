<%@ page import="com.hp.spp.config.Config,
				com.hp.spp.portal.common.helper.ProfileHelper,
				com.hp.spp.portal.common.site.SiteManager,
				com.hp.spp.profile.Constants" %>
<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<vgn-portal:defineObjects/>
<%
try {
	ProfileHelper profileHelper = new ProfileHelper();
	
	if(profileHelper.isUserAuthenticated(portalContext)) {
		
		// HP Smart Portal: (Hardcoded for this release... Do not modify!)
			// Test URL = http://h18000.www1.hp.com/survey/source/test_emea_partneronline.js
			// PRO URL = http://h18000.www1.hp.com/survey/source/emea_partneronline.js
		//String surveyURL = Config.getValue("SPP." + profileHelper.getProfileValue(portalContext, Constants.MAP_SITE) + ".SatisfactionSurveyUrl", null);
        String surveyURL = SiteManager.getInstance().getSite(profileHelper.getProfileValue(portalContext, Constants.MAP_SITE)).getSatisfactionSurveyUrl();
        // HP Smart Portal:
			// Site = 5
		String siteCode = "5"; // (Hardcoded for this release... Do not modify!)

		// #############################################################################################

		// ###############################
		// # - START - Modifiable Area - #
		// ###############################
		
		String strISOCountryCode = profileHelper.getProfileValue(portalContext, "CountryCode");
		String strISOLanguageCode = profileHelper.getProfileValue(portalContext, "PreferredLanguageCode");
		String tier = profileHelper.getProfileValue(portalContext, "Tier");
		String segment = profileHelper.getProfileValue(portalContext, "ChannelSegment");

		// ###############################
		// #  - END - Modifiable Area -  #
		// ###############################
		
		// #############################################################################################
		
		if(surveyURL == null) 
			surveyURL = "";
		
		out.println("<!-- ") ;
		out.println("surveyURL: " + surveyURL) ;
		out.println("siteCode: " + siteCode) ;
		out.println("strISOCountryCode: " + strISOCountryCode) ;
		out.println("strISOLanguageCode: " + strISOLanguageCode) ;
		out.println("tier: " + tier) ;
		out.println("segment: " + segment) ;
		out.println(" -->") ;
		
		if (!("".equals(surveyURL) || "".equals(siteCode) || strISOCountryCode.equals("") || "".equals(strISOLanguageCode) || "".equals(tier) || "".equals(segment))) {
%><script type="text/javascript">
	function mapLanguage(colCode)
	{
	    if (colCode == "ar" || colCode == "AR") return "1";
	    if (colCode == "bg" || colCode == "BG") return "2";
	    if (colCode == "cs" || colCode == "CS") return "5";
	    if (colCode == "da" || colCode == "DA") return "6";
	    if (colCode == "de" || colCode == "DE") return "7";
	    if (colCode == "el" || colCode == "EL") return "8";
	    if (colCode == "en" || colCode == "EN") return "2057";
	    if (colCode == "es" || colCode == "ES") return "10";
	    if (colCode == "fi" || colCode == "FI") return "11";
	    if (colCode == "fr" || colCode == "FR") return "12";
	    if (colCode == "he" || colCode == "HE") return "13";
	    if (colCode == "hu" || colCode == "HU") return "14";
	    if (colCode == "it" || colCode == "IT") return "16";
	    if (colCode == "nl" || colCode == "NL") return "19";
	    if (colCode == "no" || colCode == "NO") return "20";
	    if (colCode == "pl" || colCode == "PL") return "21";
	    if (colCode == "pt" || colCode == "PT") return "22";
	    if (colCode == "ro" || colCode == "RO") return "24";
	    if (colCode == "ru" || colCode == "RU") return "25";
	    if (colCode == "si" || colCode == "SI") return "36";
	    if (colCode == "sk" || colCode == "SK") return "27";
	    if (colCode == "sv" || colCode == "SV") return "29";
	    if (colCode == "tr" || colCode == "TR") return "31";
	    
	    // fallback language in case the passed code is not know 
	    return "2057"; 
	}
  	var site="<%=siteCode%>";
  	var country_code="<%=strISOCountryCode%>";
  	var l = mapLanguage("<%=strISOLanguageCode%>");
  	var tier_code="<%=tier%>";
  	var segment="<%=segment%>";
</script>
<script type="text/javascript" language="JavaScript" src="<%=surveyURL%>"></script><%
		}
	}
} catch(Exception e) {
	out.println (e);
}
%>
<!-- test -->