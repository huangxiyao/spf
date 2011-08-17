<%@ page session="false" %>
<%@ page import="org.apache.log4j.Logger,
				java.lang.System,
				com.hp.spp.common.healthcheck.HealthcheckServerInfo,
				com.hp.spp.common.healthcheck.ConfigBackedHealthcheckDAO,
				com.hp.spp.common.healthcheck.HealthcheckDAO,
				com.hp.spp.common.healthcheck.HealthcheckDAOFactory,
				com.hp.spp.common.healthcheck.HealthcheckHelper,
				com.hp.spp.config.ConfigException,
				com.hp.spp.common.util.Environment,
				com.hp.spp.config.Config"
%>
<html>
<body>
<%
String currentServerName = request.getHeader("SPP-Web-Server");
String currentURL = request.getRequestURL().toString();

if (currentServerName == null) {
	currentServerName = "UNKNOWN";
	out.println("Cannot get the current server name from HTTP header: " +  currentServerName);
}

// set HTTP Headers (no caching)
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setHeader("expires", "-1");

java.util.Date today = new java.util.Date();
out.println("<p>Today's date is: " + today);

// If the server name is in the shutdown listing, then return fail message.
HealthcheckDAO dao = new HealthcheckDAOFactory().createHealthcheckDAO();
HealthcheckServerInfo queryExampleInfobyName = new HealthcheckServerInfo(currentServerName, null, null, null);
List<HealthcheckServerInfo> listbyName = dao.findServersByExample(queryExampleInfobyName);
Iterator<HealthcheckServerInfo> iName = listbyName.iterator();
if (iName.hasNext()) {
	HealthcheckServerInfo serverInfo = (HealthcheckServerInfo) iName.next();
	if (serverInfo.getOutOfRotation()) {
		out.println("<p>Failed - the current servername (" + currentServerName + ") is in the LB out of rotation name listing in the SPP_HEALTHCHECK_SERVER_INFO table");
		return;
	}
}

// Access the test page URL and make sure the page is accessible with correct output and returns result. 
String result = HealthcheckHelper.testPageURL(currentServerName, currentURL);
out.println(result);

%>
</body>
</html>
