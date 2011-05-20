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
               <style>
                        h1 {
                                font-size: 25px;
                                font-weight: normal;
                                margin-bottom: 2px;
                                margin-top: 2px;
                        }

                                #stretchLogoWithPageTitleBlack  {
                                        width: 100%;
                                        overflow: hidden;
                                        margin: 0 0 5px 0;
                                        color: #FFF;
                                        background-color: #000;
                                        position:relative;
                                }
                        #logo {
                                float: left;
                                margin-top: 0pt;
                                margin-right: 0pt;
                                margin-bottom: 0pt;
                                margin-left: 0pt;
                                padding-top: 1.5em;
                                padding-right: 53px;
                                padding-bottom: 19px;
                                padding-left: 53px;
                        }

                        #stretchLogoWithPageTitleBlack #logo
                        {
                                padding-top: 0pt;
                                padding-right: 26px;
                                padding-bottom: 0pt;
                                padding-left: 26px;
                                background-color: transparent;
                                background-image: url("/img/slp_logo_black_000000.gif");
                                background-repeat: no-repeat;
                                background-attachment: scroll;
                                background-position: 26px 0pt;
                                height: 50px;
                        }
                        #maincontent {
                                padding-left:  20px;
                                paddign-right:  20px;
                        }
                        #footer {
                                position: relative;
                                text-align:  center;
                                width: 100%;
                                min-width: 740px;
                                margin-top: 60px;
                                padding-top: 0pt;
                                padding-right: 0pt;
                                padding-bottom: 0pt;
                                padding-left: 0pt;
                                border-top-width: 4px;
                                border-top-style: solid;
                                border-top-color: #666666;
                                clear: both;
                        }

                        #copyright {
                                position: relative;
                                text-align: center;
                                font-size: 11px;
                        }
                        
                        .screenReading  { position: absolute; width: 0; height: 0; font-size: 0; overflow: hidden; }
                </style>

<style>
	#stretchLogoWithPageTitle {color:#FFFFFF; background-color: #003366;}
	#stretchLogoWithPageTitle #logo {background-image: url(http://welcome.hp-ww.com/country/img/slp_logo_003366.gif);}
	
</style>
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
	<!--                                  BANNER                  -->
	<link href="/portal/templates/template0053/spf-system-error-secondarypage/systemError.css" rel="stylesheet" type="text/css">
	        <div id="stretchLogoWithPageTitleBlack">
	                <div id="logo">
	                        <a href="http://welcome.hp.com/country/us/en/welcome.html" title="HP.com Home">
	                        <img src="http://welcome.hp-ww.com//img/slp_logo_black_000000.gif" alt="" border="0">
	                                <span class="screenReading">HP.com Home</span>
	                        </a>
	                </div>
	        </div>
	<!--                    END OF BANNER                        -->
	<div id="maincontent">
	        <h1>Unable to process request at this time</h1>
	        <p>Unfortunately, we are unable to process your request at this time.  We apologize for the inconvenience.  Please try again later.</p>
	        <p style="{display:  none;}">(Error: portappl-internal_error)</p>
	        <p>If the issue persists, please try the following:</p>
	        <ul>
	                <li>Clear your browser's cache and try again.</li>
	                <li><a href="http://welcome.hp.com/country/us/en/wwcontact_us.html">Contacth HP Customer Service</a></li>
	        </ul>
	</div>
	<div id="footer">
	        <a href="http://welcome.hp.com/country/us/en/privacy.html" class="udrlinesmall">Privacy statement</a>
	                                &nbsp;|&nbsp;<a href="http://welcome.hp.com/country/us/en/termsofuse.html" class="udrlinesmall">Using this site means you accept its terms</a>
	</div>
	
	<div id="copyright" class="small">&#169; 2011 Hewlett-Packard Development Company, L.P.</div>
</body>
</html>
