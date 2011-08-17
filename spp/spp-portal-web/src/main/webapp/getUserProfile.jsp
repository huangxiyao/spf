<%@page import="com.hp.spp.webservice.ups.manager.SPPUserManager,com.hp.spp.profile.ProfileHelper,com.vignette.portal.log.Log"%><%

java.util.Map userProfile =null;
java.util.Map legacyProfile =null;
String _HPPUserId  = (String)request.getParameter("HPPUserId");
String _SiteName  = (String)request.getParameter("SiteName");
if (_HPPUserId==null || _SiteName == null){
	userProfile = (java.util.Map)session.getAttribute(com.hp.spp.profile.Constants.PROFILE_MAP);
}
else{
	userProfile = (new SPPUserManager()).getUserProfile(_HPPUserId, _SiteName, true);
}
ProfileHelper profileHelper = new ProfileHelper();
legacyProfile = profileHelper.toLegacyProfile(userProfile);
java.util.Iterator it = legacyProfile.keySet().iterator();
 %><?xml version="1.0" encoding="UTF-8"?>
<user>
<%
while (it.hasNext()) {
	String paramName  = (String)it.next();
%><attribute name="<%=paramName%>">
<% 	
	String paramValue = (String) legacyProfile.get(paramName);
	if (paramValue!=null && paramValue.indexOf(";") != 0) {
		String[] insideParamMap = paramValue.split(";");

		for (int i = 0; i < insideParamMap.length; i++) {
			if (!insideParamMap[i].equals(null)
					&& !insideParamMap[i].trim().equalsIgnoreCase("")) {
%>
	<value><%=insideParamMap[i]%></value>
<%				
			}
		}

	} else {
		%>
	<value><%=paramValue %></value><%
	}
%>
</attribute><%}%>
</user>
