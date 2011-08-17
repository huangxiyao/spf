<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>  
    
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<SCRIPT LANGUAGE="JavaScript">

function selectAll(bool)
{
        var inputs = document.getElementsByTagName("input")
        for (var cpt = 0 ; cpt < inputs.length ; cpt++)
        {
                if ((inputs[cpt].type == "checkbox") && (inputs[cpt].name == "groupId"))
                        inputs[cpt].checked = bool
        }
}

</SCRIPT>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tests cache</title>
</head>
<body>

nombre d'éléments dans le cache :
<%= com.hp.spp.groups.dao.SiteDAOCacheImpl.getInstance().getNbElementInCache() %>
<br>
----------------------------------------------
<form action="addKey.jsp">
clef a ajouter: <input type="text" name="key"/>
<br>
<input type="submit">
</form>

<br>
<br>

<form action="removeKey.jsp">
clef a supprimer: <input type="text" name="key"/>
<br>
<input type="submit">
</form>

<br>
<%

// PartnerPhone~PrimaryChannelSegment~PhysCity~PhysAdLine1~PhysAdLine2~PhysAdLine3~PartnerName~PartnerFax~PartnerProId~PhysPostalCode~PhysCountry~Email~FirstName~HpInternalUser~PreferredLanguageCode~LastLoginDate~LastName~CountryCode~IsPartnerAdmin~HPPUserId~Tier~HPOrg~PartnerProIdHQ~UserRole~ReceiveMail~ReceiveEmail~ReceiveSMS~ReceiveCall~ReceiveFax~ReceiveAlert~ReceiveNewsLetter~JobFunction~PartnerLegalName~PartnerNameHQ~title~UserPhone~Department~Seniority~DateOfBirth~UserFax~HierarchyType~Contact_HPRoleId~Contact_JobFunction_Id

java.util.Map userContext = new java.util.HashMap();
userContext.put("CurrentStatus", "1");
userContext.put("CountryCode", "GR;NL");
//userContext.put("userRole", "Partner Portal Administrator");
userContext.put("Programs", "EU-Competitor PC;EU-Competitor Storage;EU-Competitor Supplies");

out.println(userContext);
java.util.List groupList = (java.util.List)com.hp.spp.groups.GroupManager.getAvailableGroups("smartportal", userContext);
out.println("<br>");
out.println(groupList.size());
out.println("<br>");
for(int i=0;i<groupList.size();i++){
	String groupName = groupList.get(i).toString();
	out.println(groupName);
	out.println("<br>");
}
out.println("<br>");
%>
<a href="#" onclick="selectAll(true);">Select all</a> / <a href="#" onclick="selectAll(false);">Unselect all</a>
<%
out.println("<br>");
com.hp.spp.groups.Site site = (com.hp.spp.groups.Site) com.hp.spp.groups.dao.SiteDAOCacheImpl.getInstance().loadByName("smartportal");
java.util.Set groupList2 = site.getGroupList();
java.util.Iterator it = groupList2.iterator();
while (it.hasNext()) {
	com.hp.spp.groups.Group g = (com.hp.spp.groups.Group) it.next();
	out
			.print("<tr><td><input type=\"checkbox\" name=\"groupId\"></td><td>"
					+ g.getName()
					+ "</td><td>"
					+ g.getCreationDate()
					+ "</td><td>"
					+ g.getModificationDate() + "</td></tr>");
}

%>

<!-- Group key : -->
<%
	/*com.hp.spp.group.core.business.impl.GroupImpl group = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(1);
	out.println(group.getName());
	out.println("<br>");
	out.println(group.getRules());
	out.println("<br>");
	com.hp.spp.group.core.business.impl.Conjunction c1 = (com.hp.spp.group.core.business.impl.Conjunction) group.getExpression();
	out.println(c1.getExpressions().size());
	out.println("<br>");
	com.hp.spp.group.core.business.impl.Equality e1 = (com.hp.spp.group.core.business.impl.Equality) c1.getExpressions().get(0);
	out.println(e1.getAttributeName());
	out.println("<br>");
	out.println(e1.getAttributeValue());
	out.println("<br>");
	com.hp.spp.group.core.business.impl.Equality e2 = (com.hp.spp.group.core.business.impl.Equality) c1.getExpressions().get(1);
	out.println(e2.getAttributeName());
	out.println("<br>");
	out.println(e2.getAttributeValue());
	out.println("<br>");*/
%>

<br>

<!-- Group key2 : --> 
<%
	/*com.hp.spp.group.core.business.impl.GroupImpl group2 = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(2);
	out.print(group2.getName());
	out.println("<br>");
	com.hp.spp.group.core.business.impl.Conjunction c2 = (com.hp.spp.group.core.business.impl.Conjunction) group2.getExpression();
	out.print(c2.getExpressions().size());
	out.println("<br>");
	com.hp.spp.group.core.business.impl.Equality e3 = (com.hp.spp.group.core.business.impl.Equality) c2.getExpressions().get(0);
	out.println(e3.getAttributeName());
	out.println("<br>");
	out.println(e3.getAttributeValue());
	out.println("<br>");
	com.hp.spp.group.core.business.impl.Equality e4 = (com.hp.spp.group.core.business.impl.Equality) c2.getExpressions().get(1);
	out.println(e4.getAttributeName());
	out.println("<br>");
	out.println(e4.getAttributeValue());
	out.println("<br>");*/
%>

<%

/*String key = "myKey";
String key2 = "myKey2";

com.hp.spp.group.core.business.impl.GroupImpl group = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(key);

com.hp.spp.group.core.business.impl.GroupImpl group2 = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(key);

com.hp.spp.group.core.business.impl.GroupImpl group3 = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(key2);

com.hp.spp.group.core.business.impl.GroupImpl group4 = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(key2);

com.hp.spp.group.core.business.impl.GroupImpl group5 = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(key2);

out.println(group);
out.println(group2);
out.println(group3);
out.println(group4);
out.println(group5);

com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().removeFromCache(key);

com.hp.spp.group.core.business.impl.GroupImpl group6 = com.hp.spp.group.core.base.dao.GroupDAOCacheImpl.getInstance().load(key);
out.println("-------------");
out.println(group);
out.println(group6);*/
%>

<%

// find all site in database
/*
java.util.List siteList = com.hp.spp.groups.dao.SiteDAOHibernateImpl.getInstance().findAll();
out.print("nb site"+ siteList.size()+"<br>");
for (int i=0; i<siteList.size(); i++)
{
	com.hp.spp.groups.Site s = (com.hp.spp.groups.Site) siteList.get(i);
	com.hp.spp.groups.Site s2 = com.hp.spp.groups.dao.SiteDAOCacheImpl.getInstance().load(s.getId());
	out.print(s2.getName()+"<br>");
	java.util.Set ts = (java.util.Set) s2.getGroupList();
	java.util.Iterator it = ts.iterator();
	while (it.hasNext())
	{
		com.hp.spp.groups.Group g = (com.hp.spp.groups.Group) it.next();
		out.print(g.getName());
	}
}
*/
/*
com.hp.spp.groups.Site s1 = com.hp.spp.groups.dao.SiteDAOCacheImpl.getInstance().load(1);
out.print(s1.getName()+"<br>");
java.util.Set ts = (java.util.Set) s1.getGroupList();
java.util.Iterator it = ts.iterator();
while (it.hasNext())
{
	com.hp.spp.groups.Group g = (com.hp.spp.groups.Group) it.next();
	out.print(g.getName());
	out.print(g.getExpression()+"<br>");
}

com.hp.spp.groups.Site s2 = com.hp.spp.groups.dao.SiteDAOCacheImpl.getInstance().load(2);
out.print(s2.getName()+"<br>");
ts = (java.util.Set) s2.getGroupList();
it = ts.iterator();
while (it.hasNext())
{
	com.hp.spp.groups.Group g = (com.hp.spp.groups.Group) it.next();
	out.print(g.getName()+"<br>");
	out.print(g.getExpression()+"<br>");
}
*/
%>

</body>
</html>