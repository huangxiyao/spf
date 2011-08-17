<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
boolean recreate = false;
if ("1".equals(request.getParameter("recreate")))
{
	recreate = true;
}

if (recreate)
{
	
	// recreate the cache
	com.hp.spp.groups.dao.SiteDAOCacheImpl.recreateCache();
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List all groups of the smartportal site</title>
</head>
<body>

<h1>All groups of the smartportal site</h1>

<%

com.hp.spp.groups.Site site = com.hp.spp.groups.dao.SiteDAOCacheImpl.getInstance().loadByName("smartportal");
java.util.Set groupList = site.getGroupList();
java.util.Iterator it = groupList.iterator();
while (it.hasNext())
{
	com.hp.spp.groups.Group g = (com.hp.spp.groups.Group) it.next();
	out.println("<br>" + g.getName());
}
%>
<br>
<a href="listAllGroups.jsp?recreate=1">Click here to recreate the cache for this server</a>

<%
if (recreate)
{
	
	// recreate the cache
	out.print("<h2><font color=\"blue\">The cache is recreated</font></h2>");
}
%>

</body>
</html>
