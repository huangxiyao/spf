<%-----------------------------------------------------------------------------
	sample_hpweb_layout_config_head.jsp
	
	This is a sample hpweb_layout_config_head.jsp file that includes
	the cfhook js file.

-----------------------------------------------------------------------------%>

<vgn-portal:defineObjects/>

<%

// Create directory string to current style directory to access the
// sample_cfhook.js file (see below).

String stylePath = portalContext.getPortalHttpRoot() + 
	portalContext.getCurrentStyle().getUrlSafeRelativePath();

%>

<%-- Include sample HPP cfhook JS file to use JS functions for signin, signout,
	 edit profile, register urls. --%>

<script type="text/javascript" src="<%= stylePath %>/sample_cfhook.js"></script>