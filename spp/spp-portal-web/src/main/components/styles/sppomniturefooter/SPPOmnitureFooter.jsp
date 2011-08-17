<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper" %>

<vgn-portal:defineObjects/>

<%
if (!new ProfileHelper().isSimulationMode(session)){%>
  <!-- Begin METRICS Javascript -->
  	<!-- URL to be defined -->
  <!-- End METRICS Javascript -->
<%}%>
