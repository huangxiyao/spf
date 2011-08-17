<%@page import="com.vignette.portal.log.Log"%><%

	
	String paramGet = (String)request.getParameter("ParamGet");
	String urlProdFromRequest = request.getParameter("URLProd");
	String urlTestFromRequest = request.getParameter("URLTest");
	String eServiceName = (String)request.getParameter("_Eservice_");
	String siteName = (String)request.getParameter("_Site_");
	String autoSubmitMode = request.getParameter("autosubmit");


	Log.debug(this.getClass(),"SPP Redirect Proxy Page => "+siteName+" | " + eServiceName);
	Log.debug(this.getClass(),"SPP Redirect Proxy Page => listOfParam "+paramGet+", urlprod "+urlProdFromRequest+", urltest "+urlTestFromRequest);
	String destination = "site/"+siteName+"/?page="+eServiceName;
	
	
	String url = request.getRequestURL().toString();
	String redirectUrl = url.substring(0,url.indexOf("RedirectProxy.jsp"))+destination;
	
	

	if(paramGet!=null){
		redirectUrl += "&ParamGet="+paramGet;
	}
	if (urlProdFromRequest!=null){	
		redirectUrl += "&URLProd="+urlProdFromRequest;
	}
	if (urlTestFromRequest!=null){
		redirectUrl += "&URLTest="+urlTestFromRequest;
	}
	if (autoSubmitMode!=null && autoSubmitMode.equalsIgnoreCase("False")){
		redirectUrl += "&autosubmit=False";
	}

	
	
	
	response.sendRedirect(redirectUrl);
	
%>