
<%--
Copyright 1999-2004 Vignette Corporation.
All rights reserved.
 
THIS PROGRAM IS CONFIDENTIAL AND AN UNPUBLISHED WORK AND TRADE
SECRET OF THE COPYRIGHT HOLDER, AND DISTRIBUTED ONLY UNDER RESTRICTION. 

EXCEPT AS EXPLICITLY STATED IN A WRITTEN AGREEMENT BETWEEN THE PARTIES,
THE SOFTWARE IS PROVIDED AS-IS, WITHOUT WARRANTIES OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NONINFRINGEMENT,
PERFORMANCE, AND QUALITY.
--%>



<%@ page isErrorPage="true"
		 import="javax.servlet.http.*,
		 java.io.*,
		 java.util.*,
                 com.epicentric.common.*,
                 com.epicentric.common.website.*,
                 com.epicentric.components.*,
                 com.epicentric.metastore.*,
                 com.epicentric.users.*,
                 com.epicentric.portalbeans.*,
                 com.epicentric.template.*,
                 com.epicentric.permission.*,
                 com.hp.spp.profile.Constants,
				 com.hp.spp.common.util.DiagnosticContext,
		 java.net.URLEncoder"
			contentType="text/html; charset=UTF-8"
%>
<%
	response.setHeader("Cache-Control","no-cache");
	String homeURL = PortalServicesComponent._getPortalHttpRoot();
	String controllerURL = request.getParameter(ParameterConstants.CONTROLLER_URL);

	if (controllerURL != null) {
		// url encode whatever value we get for controllerURL so that malicious
		// javascript can't be interested via the request. at worst, if someone
		// puts a funny value in for controllerURL they'll get a 404 when they
		// try to follow the 'Click Here' link.
		// homeURL = controllerURL;
		homeURL = URLEncoder.encode(controllerURL);
	}

	ErrorMessage em = (ErrorMessage) SessionUtils.getNamespacedAttribute(session, ParameterConstants.ERROR_MESSAGE, null);

	// clean up the session, remove ErrorMessage object from session
	SessionUtils.removeNamespacedAttribute(session, ParameterConstants.ERROR_MESSAGE, null);

	String[] msg = null;
	String errorImg = "icon_status_system_error_sm.gif";
	boolean isSystemStartupError = false;
	boolean isErrorMessageCommented = false;

	if (em != null) {
		switch(em.getType()) {
			case ErrorMessage.SYSTEM_ERROR:
				// commenting out to avoid stack traces showing
				msg = em.getMessages(); 
				msg[0] = "Exception stack trace:\n" + msg[0];
				isErrorMessageCommented = true;
				break;
			case ErrorMessage.SYSTEM_STARTUP_ERROR:
				// commenting out to avoid stack traces showing
				msg = em.getMessages();
				msg[0] = "System Startup Exception stack trace:\n" + msg[0];
				isErrorMessageCommented = true;
				isSystemStartupError = true;
				break;
			default:
				msg = em.getMessages();
				msg[0] = "Error message: " + msg[0];
				break;
		}
	}

	String endUserHomeView = HtmlUtils.getTextResource(HtmlUtils.END_USER_HOME_VIEW);
	if (endUserHomeView == null) {
		endUserHomeView = "";
	}
%>

<html>
<head>
<title>Error Page</title>

<style type="text/css">
body	{margin-top:10px;
			margin-bottom:10px;
			margin-left:25px;
			margin-right:25px;
		background-color:#ffffff;}
		
body,
td, 
p	 {font-family: verdana, geneva, arial, helvetica, sans-serif;
	 			font-size: xx-small; 
	 			font-weight: normal;
				font-style: normal;
				color: #000000;}			
				
h1	 {font-family: arial, helvetica, sans-serif;
				font-size: small;
				color: #000000;
				font-weight: bold;
				margin-bottom:0px;}
				
.epi-error {color: #ff0000 !important;}	
</style>	
</head>

<% if (msg != null) {
	if (isErrorMessageCommented)  { // if error message has to be commented out (currently true for exceptions)
%>
<!-------------------------------------------------------------------------------------
<%=StringUtils.encodeXMLText(msg[0])%>		
--------------------------------------------------------------------------------------->
<% 	} 

   }
%>
	
	
<body>
	
<% if (isSystemStartupError) { %>		
	<h1>System initialization error has occured.</h1>
	<hr size="1" noshade="noshade" />
	<div class="epi-error" style="font-weight:bold; margin: 3px 0px 5px;">A fatal error has occurred during system startup.</div>  
	This error may cause any data generated during usage to be corrupted.<br /><br />
	Please contact your administrator.	
	<hr size="1" />

<% } else { %>		
		
	<h1>System Error</h1>
	<hr size="1" noshade="noshade" />
	<div class="epi-error" style="font-weight:bold; margin: 3px 0px 5px;">An error has occurred. </div>
	
<% 	
	if (msg != null) {
		if (!isErrorMessageCommented) {
%>		
		<div><%=StringUtils.encodeXMLText(msg[0])%></div><br />
<%
		}
	}
%>
	Please return to the home page of the portal.
	<hr size="1" noshade="noshade" />
<% } %>			
<% if (!DiagnosticContext.getThreadInstance().isEmpty())
   {
		out.println("<!-- Diagnostic error message -->");
		out.println("<!--" + DiagnosticContext.getThreadInstance().getErrorMessage() + "-->");
   }
%>
	
</body>
</html>

