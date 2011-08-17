<%@page import="com.hp.globalops.hppcbl.passport.PassportService"%>
<%@page import="com.hp.globalops.hppcbl.webservice.ProfileIdentity"%>
<%@page
	import="com.hp.globalops.hppcbl.passport.PassportServiceException"%>
<%@page
	import="java.util.ArrayList"%>
<%@page
	import="java.util.Iterator"%>
<%@page	import="com.hp.globalops.hppcbl.passport.beans.Fault"%>
<%@page	import="com.hp.globalops.hppcbl.webservice.AdminViewUserResponseElement"%>


<!-- 
<a href="https://ppdevatl.external.hp.com/hpp/test.do"> Register in HPP </a>

-->
<br>
<br>
<%
	String action = (String)request.getParameter("action");
	
 	if (action!=null){ 
		String userId = (String)request.getParameter("userId");
			
		try {
			PassportService ws = new PassportService(); 
			
			
			String adminSessionToken = (ws.login("sppgrpadmin","E$kenl8P")).getSessionToken();
			//String adminSessionToken = (ws.login("sppgrp","gR#45X!$")).getSessionToken();

			ProfileIdentity profileIdentity = new ProfileIdentity() ; profileIdentity.setUserId(userId) ;
			if (action.equalsIgnoreCase("addToGroupPortal")){
				ws.addUserToGroup(adminSessionToken, profileIdentity, "SPPPORTAL", "user",null) ; 
			
				out.println("User added successfully  to group SPPPORTAL <br/>");
				
			}else if (action.equalsIgnoreCase("removeFromGroupPortal")){
				ws.removeUserFromGroup(adminSessionToken, profileIdentity, "SPPPORTAL", "user",null) ; 
				out.println("User removed successfully from group SPPPORTAL <br/>");
			}
			if (action.equalsIgnoreCase("addToGroup")){
				ws.addUserToGroup(adminSessionToken, profileIdentity, "SPPQA", "user",null) ; 
			
				out.println("User added successfully  to group SPPQA <br/>");
				
			}else if (action.equalsIgnoreCase("removeFromGroup")){
				ws.removeUserFromGroup(adminSessionToken, profileIdentity, "SPPQA", "user",null) ; 
				out.println("User removed successfully from group SPPQA <br/>");
			}
			else{
				AdminViewUserResponseElement adminview = ws.adminViewUser(adminSessionToken, userId, "userId") ;
				out.println("<BR/>FirstName: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getFirstName()) ;
				out.println("<BR/>LastName: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getLastName()) ;
				out.println("<BR/>CountryCode: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getResidentCountryCode()) ;
				out.println("<BR/>SecurityQuestion: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityQuestion()) ;
				out.println("<BR/>SecurityAnswer: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityAnswer()) ;
				out.println("<BR/>Email: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getEmail()) ;
				out.println("<BR/>Securitylevel: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getProfilePrivate().getSecurityLevel()) ;
				out.println("<BR/>") ;
				out.println("<BR/>LastSuccessfulLoginDate: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getLastSuccessfulLoginDate()) ;
				out.println("<BR/>LastAttemptedLoginDate: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getLastAttemptedLoginDate()) ;
				out.println("<BR/>PasswordCreationDate: "+adminview.getAdminViewUserResultTypeChoice().getPrivateData().getSystemData().getPasswordCreationDate()) ;
				out.println("<BR/>") ;


			}
			
		}
			catch (PassportServiceException pse) 
			{
				ArrayList faults = pse.getFaults();
			 	if(pse.getFaults() !=null && faults.size() > 0 ) {
					Iterator iter = faults.iterator();
					while (iter.hasNext()) {
						Fault afault = (Fault) iter.next();
						out.println(afault+"<BR/>");
					}
				}
			  else out.println(pse.getMessage()); 
			}
			catch (Exception Exp) {
				out.println(Exp.getMessage()); 
			}
			
		}
 		
 		%>
 		 		<hr/>
 		Add user to group SPPPORTAL on HPP :
 		<form action="#" method="POST">
 			<input type="hidden" name="action" value="addToGroupPortal"/>
 			<input type="text" name="userId"/>
 			<input type="submit"/> 
 		</form>
 		<br/><hr/><br/>
 		Remove user from group SPPPORTAL on HPP :
 		<form action="#" method="POST">
 			<input type="hidden" name="action" value="removeFromGroupPortal"/>
 			<input type="text" name="userId"/>
 			<input type="submit"/> 
 		</form>
 		<br/><hr/><br/>
 		<hr/>
 		Add user to group SPPQA on HPP :
 		<form action="#" method="POST">
 			<input type="hidden" name="action" value="addToGroup"/>
 			<input type="text" name="userId"/>
 			<input type="submit"/> 
 		</form>
 		<br/><hr/><br/>
 		Remove user from group SPPQA on HPP :
 		<form action="#" method="POST">
 			<input type="hidden" name="action" value="removeFromGroup"/>
 			<input type="text" name="userId"/>
 			<input type="submit"/> 
 		</form>
 		<br/><hr/><br/>
 		List HPP Attributes of user:
 		<form action="#" method="POST">
 			<input type="hidden" name="action" value="listInfo"/>
 			<input type="text" name="userId"/>
 			<input type="submit"/> 
 		</form>
 