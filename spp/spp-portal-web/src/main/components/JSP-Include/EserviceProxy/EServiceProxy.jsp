<%@page import="com.epicentric.common.website.MenuItemUtils"%>
<%@page import="com.epicentric.common.website.MenuItemNode"%>
<%@page import="com.vignette.portal.website.enduser.PortalContext"%>
<%@taglib uri="vgn-tags" prefix="vgn-portal"%>
<%@page import="com.vignette.portal.log.Log"%>

<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator" %>
<%@page import="com.hp.spp.webservice.eservice.manager.SPPEServiceWSManager"%>
<%@page import="com.hp.spp.webservice.eservice.client.EServiceResponse"%>
<%@page import="com.hp.spp.webservice.eservice.client.EserviceRequest"%>
<%@page import="com.hp.spp.profile.Constants"%>

<vgn-portal:defineObjects />
<html>
<body>


<%
			//*****************************************
			//GET PARAMETERS FROM REQUEST
			//*****************************************
			java.util.Map userProfile = (java.util.Map)session.getAttribute(Constants.PROFILE_MAP);
		
			String siteName = (String)userProfile.get(Constants.MAP_SITE);
			String paramGet = (String) request.getParameter("ParamGet");
			String urlProdFromRequest = request.getParameter("URLProd");
			String urlTestFromRequest = request.getParameter("URLTest");
			String autoSubmit		  = request.getParameter("autosubmit");
			
			//GET Eservice Name FROM MenuItems
			String eServiceName = null;
			MenuItemNode menuItemNode = MenuItemUtils.getSelectedMenuItemNode(portalContext);
			if (menuItemNode != null) {
				eServiceName = menuItemNode.getMenuItem().getTitle();
			} else {
				Log.error(this.getClass(),
						"Problem while getting NavigationItem title (title is null)");
			}
			
			java.util.HashMap legacyProfile = (java.util.HashMap)(new com.hp.spp.profile.ProfileHelper()).toLegacySimulatingProfile(userProfile);

			java.util.HashMap httpRequestMap = new java.util.HashMap();
			// working on paramGet
			if (paramGet != null) {
				String[] paramToAdd = paramGet.split(";");
				String paramName = null;
				String paramValue = null;
				for (int i = 0; i < paramToAdd.length; i++) {
					paramName = paramToAdd[i].substring(0, paramToAdd[i].indexOf("."));
					paramValue = paramToAdd[i].substring(paramToAdd[i].indexOf(".") + 1,
							paramToAdd[i].length());
					Log.debug(this.getClass(), "Add Get parameter named :" + paramName);
					httpRequestMap.put(paramName, paramValue);
				}
			}
			//*****************************************
			//END GET PARAMETERS FROM REQUEST
			//*****************************************
			
			
			//*****************************************
			//EService optimization - Begin
			//*****************************************
			EServiceResponse eServiceResponse = null;

			//Try to get the eServiceResponse from session
			eServiceResponse = (EServiceResponse) session.getAttribute(eServiceName);
			
			//Flag to check if a call to the ESM WS is required.
			boolean callESM = true;
			
			//Find out if it is required to make a WS call to ESM
			if (eServiceResponse != null && urlProdFromRequest == null && urlTestFromRequest == null) {
				//todo Not sure if we need to check 
				//urlProdFromRequest & urlTestFromRequest here. These can be replaced in the
				//EService response retrieved from session as well.
				callESM = false;
			}
			//Call ESM if callESM flag is true
			if (callESM) {
				//*****************************************
				//INITIALIZE THE REQUEST BEAN FOR ESERVICE WEBSERVICE
				//*****************************************
				EserviceRequest eServiceRequest = new EserviceRequest();
				eServiceRequest.setSiteName(siteName);
				eServiceRequest.setEServiceName(eServiceName);
				eServiceRequest.setUserContext(legacyProfile);
				if (urlProdFromRequest != null)
					eServiceRequest.setUrlProdFromRequest(urlProdFromRequest);
				if (urlTestFromRequest != null)
					eServiceRequest.setUrlTestFromRequest(urlTestFromRequest);
				eServiceRequest.setHttpRequestParameters(httpRequestMap);

				Log.debug(this.getClass(), "Request Eservice Manager for Site :" + siteName);
				Log.debug(this.getClass(), "Request Eservice Manager for eService :"
						+ eServiceName);
				Log.debug(this.getClass(), "Request Eservice Manager for userContext :"
						+ legacyProfile);
				Log.debug(this.getClass(), "Request Eservice Manager for httpGetParameter :"
						+ httpRequestMap);
				Log.debug(this.getClass(), "Request Eservice Manager autoSubmit :"
						+ autoSubmit);

				//*****************************************
				//END INITIALIZE THE REQUEST BEAN FOR ESERVICE WEBSERVICE
				//*****************************************

				//*****************************************
				//GET THE RESPONSE OF THE WEBSERVICE
				//*****************************************
				try {
					eServiceResponse = SPPEServiceWSManager.getEServiceResponse(eServiceRequest);
				
				} catch (Exception e) {
					Log.error(this.getClass(),
							"Web Service Exception while getting Eservice :" + e);
				}
		
			}//End if (callESM)
			
			//*****************************************
			//EService optimization - End
			//*****************************************
			
			//******************************************
			//PREPARE THE GET PARAM STRING
			//******************************************
			java.util.Iterator getParamIter = httpRequestMap.keySet().iterator();
			String currentGetParameter = null;
			String currentGetParameterValue = null;
			String urlParameterString = "";
			Log.debug(this.getClass(),"http Map contains "+httpRequestMap);
			while (getParamIter.hasNext()){
				currentGetParameter = (String)getParamIter.next();
				if ( eServiceResponse.getParameters().containsKey(currentGetParameter)){
					currentGetParameterValue = (String)httpRequestMap.get(currentGetParameter);
					if (currentGetParameterValue != null && currentGetParameterValue.equals((String)eServiceResponse.getParameters().get(currentGetParameter))){
						
						Log.debug (this.getClass(),
								"Adding param "+currentGetParameter+" to the get String");
						if (urlParameterString.length()!=0){
							urlParameterString = urlParameterString.concat("&");
						}
						urlParameterString = urlParameterString.concat(currentGetParameter).concat("=").concat((String)httpRequestMap.get(currentGetParameter));
					}
				}
			}
			
			//*****************************************
			//POPULATE THE FORM
			//*****************************************

			
if (eServiceResponse!=null && eServiceResponse.getUrl()!=null){
	Log.debug(this.getClass(), "Response from Web service for EserviceParameter:"
		+ eServiceResponse.getParameters());

	request.setAttribute("isEservicePage","true");
	request.setAttribute("eServiceName",eServiceName);
%>
	<!-- Begin Omniture Header -->
	<vgn-portal:includeStyle friendlyID="SPP_OmnitureHeader_Type"/>
	<!-- End Omniture Header -->

	<form id="eserviceform" name="eserviceform" method="<%=eServiceResponse.getMethod() %>" action="<%=eServiceResponse.getUrl()%><%=urlParameterString.length()>0?"?"+urlParameterString:"" %>">
<%
			java.util.Iterator paramIter = eServiceResponse.getParameters().keySet().iterator();
				String paramNameToPost = null;
				while (paramIter.hasNext()) {
					paramNameToPost = (String) paramIter.next();
						if (autoSubmit != null && autoSubmit.equalsIgnoreCase("False")){%>
					<%=paramNameToPost%> : <input type="text" name="<%=paramNameToPost%>" value="<%=eServiceResponse.getParameters().get(paramNameToPost)%>"><BR>				
						
					<%	}
						else{ %>
					<input type="hidden" name="<%=paramNameToPost%>" value="<%=eServiceResponse.getParameters().get(paramNameToPost)%>"><BR>				
<%					}
				}
%> 
	
	
<%
	if (autoSubmit != null && autoSubmit.equalsIgnoreCase("False")){
		Log.debug(this.getClass(), "Call in test mode (autoSubmit=False)");
%>
			<input type="submit" name="eservicebutton" >
	</form>
	<%}else{
		Log.debug(this.getClass(), "Call in test mode (autoSubmit=TRUE)");
		String hpOmnitureURL = HPUrlLocator.getWelcomeUrl(portalContext,true);%>
	</form>
		
	<!-- Begin Omniture Footer -->
		<vgn-portal:includeStyle friendlyID="SPP_OmnitureFooter_Type" />
	<!-- End Omniture Footer -->
		
				 <script language="javascript" type="text/javascript">
				 document.forms['eserviceform'].submit();
			 </script>
	<%}
}else{
	Log.error(this.getClass(),
			"JSP Exception : NO Eservice named "+eServiceName+" or url is empty");
String i18nID = new com.hp.spp.portal.common.helper.LocalizationHelper().getCommonI18nID(portalContext);		
%>
	<vgn-portal:i18nValue stringID="<%=i18nID%>" key="eservice_error_message" defaultValue="" />
<%} %>
<%
 String _clear_userprofile = (String)session.getAttribute(Constants.CLEAR_USERPROFILE_FLAG);
 //If _clear_userprofile is set to true then remove user profile from session
 if("true".equals(_clear_userprofile)){
         session.removeAttribute(Constants.PROFILE_MAP);
 }
%>

</body>
</html>


