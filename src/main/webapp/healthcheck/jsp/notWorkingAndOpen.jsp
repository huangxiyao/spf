<%-------------------------------------
File: notWorkingAndOpen.jsp
View file for case where site is working.
-------------------------------------%>
<jsp:directive.page import="com.hp.it.spf.ac.healthcheck.web.References" />
<jsp:directive.page import="com.hp.bco.pl.wpa.util.Environment" />
<jsp:directive.page import="com.hp.it.spf.xa.ac.HealthcheckStatus" />
<%
	HealthcheckStatus status = HealthcheckStatus.retrieve(Environment
			.getInstance().getContext());
	String portalPulseSource = status.getPortalPulseSource();
	String openSignSource = status.getOpenSignSource();
%>
<html>
<head>
<title>Site is NOT WORKING or CLOSED</title>
<meta http-equiv="<%= References.HTTP_CACHE_CONTROL_HEADER %>"
	content="<%= References.HTTP_NO_CACHE %>">
<meta http-equiv="<%= References.HTTP_PRAGMA_HEADER %>"
	content="<%= References.HTTP_NO_CACHE %>">
<meta http-equiv="<%= References.HTTP_X_SITE_AVAILABLE_HEADER %>"
	content="<%= References.HTTP_X_SITE_UNAVAILABLE %>">
</head>
<body bgcolor="#FFFFFF">
<h1>Site is NOT WORKING or CLOSED</h1>

<p>The HP Service Portal at this site is either <b>closed</b> or <b>not
working</b>. This means it is inaccessible to end users. If it is not
working, it is inaccessible to staff users, too.</p>

<p>You may also check the <b><a href="/healthcheck/isWorking.do">Working
Healthcheck</a></b> for more current status regarding this site.</p>

<font size="-1"><i>Responder: <%=request.getServerName()%>:<%=request.getServerPort()%></i></font>
<br>
<font size="-1"><i>Based on portal pulse at: <%=portalPulseSource%></i></font>
<br>
<font size="-1"><i>Based on open-sign at: <%=openSignSource%></i></font>
<br>
<font size="-1"><i><%=References.HTTP_X_SITE_AVAILABLE_HEADER%>:
<%=References.HTTP_X_SITE_UNAVAILABLE%>
</body>
</html>
