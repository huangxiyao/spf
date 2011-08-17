<%@ page import="com.hp.spp.portal.login.dao.LoginDAO"%>
<%@ page import="com.hp.spp.portal.login.dao.LoginDAOCacheImpl"%>
<%@ page import="com.hp.spp.profile.Constants" %>
<%@ page import="com.hp.spp.portal.common.helper.ProfileHelper" %>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>
<vgn-portal:defineObjects />

<form action="/portal/jsp/SPP/logout.jsp" method="POST" name="spp_logout">
	<%
	ProfileHelper profileHelper = new ProfileHelper();
	String siteName = profileHelper.getProfileValue(portalContext, Constants.MAP_SITE) ;
	LoginDAO loginDAO = LoginDAOCacheImpl.getInstance() ;
	String menuItemId = loginDAO.getLogoutMenuItemId(siteName) ;
	if(profileHelper.isSimulationMode(portalContext)) {
		// /////////////////////////////////////////////////////////////////// //
		// Look logout.jsp to know the corresponding name of the abbreviation. //
		// /////////////////////////////////////////////////////////////////// //
	%>
	<input type="hidden" name="ps" value="<% if(loginDAO.getPersistSimulation(siteName)) { %>true<% } else { %>false<% } %>" />
	<input type="hidden" name="sd" value="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_HPPID)%>" />
	<input type="hidden" name="sr" value="<%=(String) profileHelper.getSimulatorProfile(portalContext).get(Constants.MAP_HPPID)%>" />
	<%
	}
	if(menuItemId != null && !"".equals(menuItemId)) {
	%>
	<input type="hidden" name="mi" value="<%=menuItemId%>" />
	<%
	}
	%>
	<input type="hidden" name="sn" value="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_SITE)%>" />
	<input type="hidden" name="cc" value="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_COUNTRY)%>"/>
	<input type="hidden" name="lang" value="<%=profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE)%>"/>
</form>
<script type="text/javascript" language="javascript">
	function logout() {
		document.cookie = 'sppSearchString' + "=" +( '' ) +"; path=" + "/portal/site/<%=siteName%>";
		return document.spp_logout.submit();
	}
</script>
