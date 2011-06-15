<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-------------------------------------
File: isWorkingAndOpen.jsp
View file for case where site is both working and open.
-------------------------------------%>
<jsp:directive.page import="com.hp.it.spf.ac.healthcheck.web.References" />
<jsp:directive.page import="com.hp.bco.pl.wpa.util.Environment" />
<jsp:directive.page import="com.hp.it.spf.xa.ac.HealthcheckStatus" />
<%
	String portalPulseSource = "(pending)";
	String openSignSource = "(pending)";
	HealthcheckStatus status = HealthcheckStatus.retrieve(Environment
			.getInstance().getContext());
	if (status != null) {
		portalPulseSource = status.getPortalPulseSource();
		openSignSource = status.getOpenSignSource();
	}
%>
<html>
<head>
<title>Site is WORKING and OPEN</title>
<meta http-equiv="<%= References.HTTP_CACHE_CONTROL_HEADER %>"
	content="<%= References.HTTP_NO_CACHE %>">
<meta http-equiv="<%= References.HTTP_PRAGMA_HEADER %>"
	content="<%= References.HTTP_NO_CACHE %>">
<meta http-equiv="<%= References.HTTP_X_SITE_AVAILABLE_HEADER %>"
	content="<%= References.HTTP_X_SITE_AVAILABLE %>">
</head>
<body bgcolor="#FFFFFF">
<h1>Site is WORKING and OPEN</h1>

<p>This site is <b>working and open</b>. This means it should be
accessible, to both end users and staff users.</p>

<p>You may also check the <b><a href="/healthcheck/isWorking.do">Working
Healthcheck</a></b> for more current status regarding this site.</p>

<font size="-1"><i>Responder: <%=request.getServerName()%>:<%=request.getServerPort()%></i></font>
<br>
<font size="-1"><i>Based on portal pulse at: <%=portalPulseSource%></i></font>
<br>
<font size="-1"><i>Based on open-sign at: <%=openSignSource%></i></font>
<br>
<font size="-1"><i><%=References.HTTP_X_SITE_AVAILABLE_HEADER%>:
<%=References.HTTP_X_SITE_AVAILABLE%></i></font>
</body>
</html>
