<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper" %>

<vgn-portal:defineObjects/>

<%
if (!new ProfileHelper().isSimulationMode(session)){%>
  <!-- Begin METRICS Javascript -->
  	<!-- URL to be defined -->
	<% if ("http".equals(request.getScheme())) { %>
	  <script type="text/javascript" language="JavaScript" src="http://welcome.hp-ww.com/cma/exceptions/emeapartnerpro/metrics.js"></script>
	<% } else { %>
	  <script type="text/javascript" language="JavaScript" src="https://secure.hp-ww.com/cma/exceptions/emeapartnerpro/metrics.js"></script>
	<% } %>
  <!-- End METRICS Javascript -->
<%}%>
