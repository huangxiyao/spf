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
		String userId    = (String)request.getParameter("userId");
		String password = (String)request.getParameter("password");
		String newPassword = (String)request.getParameter("newpassword");
			
		try {
			PassportService ws = new PassportService(); 
			
			
			String adminSessionToken = (ws.login("sppgrpadmin","E$kenl8P")).getSessionToken();
			//String adminSessionToken = (ws.login("sppgrp","gR#45X!$")).getSessionToken();

			ProfileIdentity profileIdentity = new ProfileIdentity() ; profileIdentity.setUserId(userId) ;
			if (action.equalsIgnoreCase("changePassword")){
				ws.changePassword(userId,password,newPassword,newPassword);
				out.println("Password changed successfully <br/>");
				
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
 		Change password on HPP :
 		<form action="#" method="POST">
 			<input type="hidden" name="action" value="changePassword"/>
 			username : <input type="text" name="userId"/></BR>
 			old password : <input type="text" name="password"/></BR>
 			new password : <input type="text" name="newpassword"/></BR>
 			<input type="submit"/> 
 		</form>
 		<br/><hr/>