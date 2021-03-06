<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Refresh" content="60">
<title>Portal Pulse</title>
</head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.hp.it.spf.pulse.portal.component.secondarypagetype.util.*"%>
<%@ page import="com.hp.it.spf.xa.misc.portal.Consts"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page session="true"%>

<%
    InetAddress  inetadd = InetAddress.getLocalHost();
    String       localname = inetadd.getHostName();
%>

<body>
<h1>Portal Pulse</h1>
<br />
<h2>Server Name: <%= localname %></h2>
<br />
Current Time: <%= new Date() %>
<br />
<br />
<%
	List taskList = (List) session.getAttribute(Consts.SESSION_ATTR_PORTAL_PULSE_DATA);

	if (taskList != null) {
		out.println("<table cellpadding=0 cellspacing=0 width=635>");
		out.println("<tr>");
		out.println("<th align=\"left\">Service Target</th>");
		out.println("<th align=\"left\">Status</th>");
		out.println("<th align=\"left\">Response Time (ms)</th>");
		out.println("</tr>");
   
		Iterator it = taskList.iterator();
		while(it.hasNext()) {
			IComponentCheckTask task = (IComponentCheckTask)it.next();
			String taskName = task.getName();
			out.println("<tr>");
			out.println("<td align=\"left\">");
			out.println(taskName);
			out.println("</td>");

			out.println("<td align=\"left\">");
			int status = task.getStatus();
			if (status == IComponentCheckTask.STATUS_PASS) {
				out.println(" <!-- " + taskName + ":PASS --> ");
				out.println("PASS");
			} else if (status == IComponentCheckTask.STATUS_FAIL) {
				out.println(" <!-- " + taskName + ":FAIL --> ");
				out.println("FAIL");
			} else {
				out.println(" <!-- " + taskName + ":UNKNOWN --> ");
				out.println("UNKNOWN");
			}
			out.println("</td>");

			out.println("<td align=\"left\">");
			out.println(task.getResponseTime());
			out.println("</td>");
			out.println("</tr>");	                    
		}
		out.println("</table>");
	} else {
		String msg;
	    msg = "<br/>The session object <code>portal-pulse.tasks</code> can not be found.<br/>";
	    out.println(msg);
	}	            
%>
<br />
</body>
</html>
