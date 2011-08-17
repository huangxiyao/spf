<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>  
    
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Eservice cache</title>
</head>
<body>


<br>



<br>
<%

// PartnerPhone~PrimaryChannelSegment~PhysCity~PhysAdLine1~PhysAdLine2~PhysAdLine3~PartnerName~PartnerFax~PartnerProId~PhysPostalCode~PhysCountry~Email~FirstName~HpInternalUser~PreferredLanguageCode~LastLoginDate~LastName~CountryCode~IsPartnerAdmin~HPPUserId~Tier~HPOrg~PartnerProIdHQ~UserRole~ReceiveMail~ReceiveEmail~ReceiveSMS~ReceiveCall~ReceiveFax~ReceiveAlert~ReceiveNewsLetter~JobFunction~PartnerLegalName~PartnerNameHQ~title~UserPhone~Department~Seniority~DateOfBirth~UserFax~HierarchyType~Contact_HPRoleId~Contact_JobFunction_Id

java.util.Map userContext = new java.util.HashMap();
userContext.put("environmentPlatform", "PROD");
userContext.put("username", "jdoe");
userContext.put("firstname", "Jhon");
userContext.put("lastname", "Doe");
userContext.put("language", "FR");

out.println(userContext);

java.util.List eServicelist = (java.util.List)com.hp.spp.eservice.dao.EServiceDAO.getInstance().find("select eservice from com.hp.spp.eservice.EService as eservice where eservice.site=1 and eservice.name='EasyCatalogue'");
com.hp.spp.eservice.el.ExpressionResolver er = new com.hp.spp.eservice.el.ExpressionResolver(userContext);
//java.util.List groupList = (java.util.List)com.hp.spp.groups.GroupManager.getAvailableGroups("SMART PORTAL", userContext);
out.println("<br>");

out.println("<br>");
for(int i=0;i<eServicelist.size();i++){
	com.hp.spp.eservice.EService eservice = (com.hp.spp.eservice.EService)eServicelist.get(i);
	out.println("Eservice Name => "+eservice.getName());
	out.println("<br>");
	out.println("Eservice Method => "+eservice.getMethod()+"<BR/>");
	out.println("Eservice URL => "+eservice.getEserviceUrl(er)+"<BR/>");
	java.util.Map parameters = eservice.getEserviceParameters(er);
	
	out.println("Parameters => "+parameters+"<br>");
}

%>



</body>
</html>