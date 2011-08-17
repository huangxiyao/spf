<%@taglib uri="vgn-tags" prefix="vgn-portal" %>

<%@page import="com.vignette.portal.log.Log"%>
<%@page import="com.hp.spp.webservice.eservice.manager.SPPEServiceWSManager"%>
<%@page import="com.hp.spp.webservice.eservice.client.EServiceResponse"%>
<%@page import="com.hp.spp.webservice.eservice.client.EserviceRequest"%>
<%@page import="com.hp.spp.profile.Constants"%>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper"%>
<%@ page import="org.apache.log4j.Logger"%>
<%!
	private static final Logger mLog = Logger.getLogger("com.hp.spp.jsp.EServiceInformation.jsp");
%>
<vgn-portal:defineObjects/>

*<%
		//GET PARAMETER FROM REQUEST & SESSION
		String eServiceName = request.getParameter("_EService_");
		ProfileHelper profileHelper = new ProfileHelper();
		String siteName = profileHelper.getProfileValue(portalContext, Constants.MAP_SITE);
		java.util.HashMap profile = (java.util.HashMap) profileHelper.getProfile(portalContext);
           
		//INITIALIZE THE REQUEST BEAN FOR ESERVICE WEBSERVICE
        EserviceRequest eServiceRequest = new EserviceRequest();
 		eServiceRequest.setSiteName(siteName);
 		eServiceRequest.setEServiceName(eServiceName);
		//eServiceRequest.setUserContext(profile);
		//eServiceRequest.setHttpRequestParameters((java.util.HashMap)request.getParameterMap());
		
		profile = (java.util.HashMap)(new com.hp.spp.profile.ProfileHelper()).toLegacySimulatingProfile(profile);
		eServiceRequest.setUserContext(profile);
		
		
		mLog.debug("Request Eservice Manager for Site :"+siteName);
	    mLog.debug("Request Eservice Manager for eService :"+eServiceName);
	    mLog.debug("Request Eservice Manager for mapContent :"+profile); 

	    EServiceResponse eServiceResponse = null;
		try {
			eServiceResponse = SPPEServiceWSManager.getEServiceResponse(eServiceRequest);
			session.setAttribute(eServiceName,eServiceResponse);
			mLog.debug("Eservice Response put in the session : "+eServiceResponse);

		} catch (Exception e) {
			e.printStackTrace(new java.io.PrintWriter(out));
			mLog.error("Web Service Exception while getting Eservice :" + e, e);
		}
		if (eServiceResponse == null)	
			mLog.error("eServiceResponse is null for Site " + siteName + " and eService " +eServiceName);

 		if (eServiceResponse.isOpenInNewWindow()){
 			out.print("Y");
 		}
 		else{
 			out.print("N");
 		}
 		if (eServiceResponse.isSecurityMode()){
 			out.print("Y");
 		}
 		else{
 			out.print("N");
 		}
 		
 		//Get the Simulation mode from the eService definition.
 		//The simulation mode can be one of 1/2/3/4 
 		//At this time we only need to find out if the eService does or does not support
 		//the simulation mode.
 		long eServiceSimulationMode = eServiceResponse.getSimulationMode();
 		
 		if(eServiceSimulationMode == 2) {
 		//eService does NOT support simulation mode.
 			out.print("N");
 		}else {
 			out.print("Y");
 		}
 		out.print("'");
		if (eServiceResponse.isOpenInNewWindow()){
 			if(eServiceResponse.getWindowParameters() != null){
				out.print(eServiceResponse.getWindowParameters());
			}
		} 	
		out.print("'");
		out.print("$");
		if(eServiceResponse.getCharacterEncoding() != null)
			out.print(eServiceResponse.getCharacterEncoding());
		out.print("$");
%>*
