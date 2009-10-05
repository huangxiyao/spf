<%--
/*######################################################################################
Copyright 1999-2007 Vignette Corporation.  All rights reserved.  This software
is an unpublished work and trade secret of Vignette, and distributed only
under restriction.  This software (or any part of it) may not be used,
modified, reproduced, stored on a retrieval system, distributed, or
transmitted without the express written consent of Vignette.  Violation of
the provisions contained herein may result in severe civil and criminal
penalties, and any violators will be prosecuted to the maximum extent
possible under the law.  Further, by using this software you acknowledge and
agree that if this software is modified by anyone such as you, a third party
or Vignette, then Vignette will have no obligation to provide support or
maintenance for this software.
#####################################################################################*/ 
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

<%-- ------------------------------------------------------------------- --%>
<%-- SPF changes to standardize error display per SPF standard messaging --%>
<%-- ------------------------------------------------------------------- --%>

<html>
<head>
<title>Could Not Open Page</title>
</head>

<% if (msg != null) {
	if (isErrorMessageCommented)  { // if error message has to be commented out (currently true for exceptions)
%>
<!--
<%=StringUtils.encodeXMLText(msg[0])%>		
-->
<% 	}
   }
%>

<body>
<h1>Could not open page</h1>
The service or information you requested is not available at this time. <br/>
Please try again later. <br/>
<br/>
<span style="font-size:smaller;">(Error: portappl-internal_error)</span>
</body>
</html>
