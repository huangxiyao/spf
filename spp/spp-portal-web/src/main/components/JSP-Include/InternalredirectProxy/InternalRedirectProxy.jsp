<%@page import="com.epicentric.common.website.MenuItemUtils"%>
<%@page import="com.epicentric.common.website.MenuItemNode"%>
<%@page import="com.vignette.portal.website.enduser.PortalContext"%>
<%@taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@page import="com.vignette.portal.log.Log"%>

<vgn-portal:defineObjects/>
<html>
<body>
<%


//GET PARAMETERS FROM REQUEST
String eServiceName = (String)request.getParameter("_EService_");

String paramGet = (String)request.getParameter("ParamGet");
if (paramGet==null || paramGet.equalsIgnoreCase("")){
	paramGet = (String)request.getParameter("paramGet");
}
String urlProdFromRequest = request.getParameter("URLProd");
String urlTestFromRequest = request.getParameter("URLTest");
String autoSubmitMode = request.getParameter("autosubmit");

Log.info(this.getClass(),"[SPP] Redirect JSP - Start redirection to the navigationItem with the name :"+eServiceName);

MenuItemNode eServiceNavItem = null;
java.util.List allNodes = MenuItemUtils.getAllNodes(portalContext);
if (allNodes != null)
{
	//Log.info(this.getClass(),"[SPP] Redirect JSP - nb Nodes "+allNodes.size());

    for (int i = 0; i < allNodes.size(); i++)
    {
    	
        MenuItemNode temp = (MenuItemNode) (allNodes.get(i));
        if (temp.getMenuItem() !=null && temp.getMenuItem().getTitle() != null)
        {
        	if (temp.getMenuItem().getTitle().equals(eServiceName))
            {
        		Log.info(this.getClass(),"[SPP] Redirect JSP - Item is found");
            	eServiceNavItem = temp;
                break;
            }
        }
        
    }
    Log.info(this.getClass(),"[SPP] Redirect JSP - finich AllNodes");
}

String url = null;
boolean isFirstParam = true;
if (eServiceNavItem!=null){
	url = portalContext.createMenuItemURI(eServiceNavItem.getID()) ;
	
	if(paramGet!=null){
		isFirstParam = false;
		url += "?ParamGet="+paramGet;
	}
	if (urlProdFromRequest!=null){
		if (isFirstParam){
			url += "?";
			isFirstParam = false;
		}
		else{
			url += "&";
		}
		url += "URLProd="+urlProdFromRequest;
	}
	if (urlTestFromRequest!=null){
		if (isFirstParam){
			url += "?";
			isFirstParam = false;
		}
		else{
			url += "&";
		}
		url += "URLTest="+urlTestFromRequest;
	}
	if (autoSubmitMode!=null && autoSubmitMode.equalsIgnoreCase("False")){
		if (isFirstParam){
			url += "?";
			isFirstParam = false;
		}
		else{
			url += "&";
		}
		url += "autosubmit=False";
	}
%>
<script type="text/javascript"> 
<!--       
 window.location = "<%=url%>";  
--> 
</script>
<%
}
else{
	Log.error(this.getClass(),"[SPP] Redirect JSP - there is no navigation item with the name :"+eServiceName);
%>
	<b>Redirection Failed</b><br/>
	Sorry, there is no Eservice named with the name : <%= eServiceName%> or you are not authorized to access it.
<% }
%>

</html>


