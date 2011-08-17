<%@taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper" %>

<%
java.util.Map userProfile = (java.util.Map)session.getAttribute(com.hp.spp.profile.Constants.PROFILE_MAP);

%>

<vgn-portal:defineObjects/>

<%
if (!new ProfileHelper().isSimulationMode(session)){
  try {
  %>
  
  <script type="text/javascript" language="JavaScript">
  
  	var language = '<%=userProfile.get("UserPreferedLanguageCode")!=null?userProfile.get("UserPreferedLanguageCode"):""%>';
  	var phoneNumber = '<%=userProfile.get("PhoneContactPreference")!=null?userProfile.get("PhoneContactPreference"):""%>';
  	var completeName = '<%=userProfile.get("LastName")!=null?userProfile.get("LastName"):""%><%=userProfile.get("FirstName")!=null?(String)userProfile.get("FirstName"):""%>';
  	var status = '<%=userProfile.get("Status")!=null?userProfile.get("Status"):""%>';
  	var hppUserID = '<%=userProfile.get("HPPUserId")!=null?userProfile.get("HPPUserId"):""%>';
  	var country = '<%=userProfile.get("Country")!=null?((String)userProfile.get("Country")).toLowerCase():""%>';
  	var email = '<%=userProfile.get("Email")!=null?((String)userProfile.get("Email")).toUpperCase():""%>';
  	
  	var firstName= '<%=userProfile.get("FirstName")!=null?((String)userProfile.get("FirstName")):""%>';
	var loginID= '<%=userProfile.get("LoginId")!=null?((String)userProfile.get("LoginId")):""%>';
	var emailContact= '<%=userProfile.get("EmailContactPreference")!=null?((String)userProfile.get("EmailContactPreference")):""%>';
	var userGroups= '<%=userProfile.get("UserGroups")!=null?((String)userProfile.get("UserGroups")):""%>';
	var postalContact= '<%=userProfile.get("PostalContactPreference")!=null?((String)userProfile.get("PostalContactPreference")):""%>';
   	
  </script>
  
  <%} catch (Exception e) {	
  	out.print("Exception in SSP Omniture Header " + e);	
  }
}%>
