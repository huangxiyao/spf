<%@ page
import="com.epicentric.metastore.MetaStoreFolder,
com.epicentric.portalbeans.PortalPageContext,
com.epicentric.portalbeans.PortalBean,
com.epicentric.user.User,
java.util.Map,
com.hp.spp.profile.Constants"
contentType="text/html; charset=UTF-8"
%>
<%@ page import="com.epicentric.common.website.I18nUtils"%>
<%@	taglib uri="module-tags" prefix="mod" %>
<%-- VAP 7.4 comes with JSTL 1.1 but SPP cannot upgrade to this version due to other deps; therefore
we continue to use JSTL 1.0 but have to update taglib URL, otherwise the runtime EL values are not allowed --%>
<%@ taglib uri='http://java.sun.com/jstl/core_rt' prefix='c' %>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects />
<mod:view class="com.epicentric.portalbeans.beans.jspbean.JSPView">

<%
// Use View object to get user and other context info.
PortalPageContext ppc = view.getPortalPageContext();
Map userProfile = (Map)session.getAttribute(Constants.PROFILE_MAP);
pageContext.setAttribute("userProfile", userProfile);

String firstname = (String)userProfile.get(Constants.MAP_FIRSTNAME);
String lastname = (String)userProfile.get(Constants.MAP_LASTNAME);
String country = (String)userProfile.get(Constants.MAP_COUNTRY);
String userName = "";
if ( "jp".equalsIgnoreCase(country) ){
	userName = lastname + " "+ firstname;
}
else{
	userName = firstname + " "+ lastname;
}
%>

<table  bgcolor="WHITE" border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
<td><%=I18nUtils.getValue("560c851827484a5d946029217128efa0","myinfo_id", "User ID", session,request)%> : </td>
<td><c:out value='${userProfile.LoginId}'/></td>
</tr>
<tr>
<td><%=I18nUtils.getValue("560c851827484a5d946029217128efa0","myinfo_name", "User Naam", session,request)%> : </td>
<td><%= userName %></td>
</tr>
<tr>
<td><%=I18nUtils.getValue("560c851827484a5d946029217128efa0","myinfo_email", "Email", session,request)%> : </td>
<td><c:out value='${userProfile.Email}'/></td>
</tr>
<tr>
<td><%=I18nUtils.getValue("560c851827484a5d946029217128efa0","myinfo_phone", "Phone Number", session,request)%> : </td>
<td><c:out value='${userProfile.PartnerPhone}'/></td>
</tr>
<tr>
<td><%=I18nUtils.getValue("560c851827484a5d946029217128efa0","myinfo_address", "Address", session,request)%> : </td>
<td><c:out value='${userProfile.PartnerPhysicalAddressLine1}'/></td>
</tr>
</table>

</mod:view>