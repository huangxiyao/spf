<%@taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@page import="com.hp.spp.portal.common.helper.ProfileHelper" %>
<%@page import="com.epicentric.common.website.*,com.epicentric.page.*"%>

<vgn-portal:defineObjects/>

<%
ProfileHelper profileHelper = new ProfileHelper();


if (profileHelper!=null){
  try {			
		String lbl_OutletId = "Outlet Id: ";
		
		String outletIds 	  	= profileHelper.getOmnitureProfileValue(portalContext,"OutletIds");		
		String companyNumber 	  	= profileHelper.getOmnitureProfileValue(portalContext,"CompanyNumber");
		
		if(outletIds.indexOf(";")>0){
			lbl_OutletId="Outlet Ids: ";
		}
		
		outletIds = outletIds.replaceAll(";", "<BR>");
		
		%>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr><td> Location ID: </td><td><%=companyNumber%></td></tr>
			<tr><td valign='top'> <%=lbl_OutletId%> </td><td><%=outletIds%></td></tr>			
		</table>
        	
<%		
  } catch (Exception e) {	
  	out.print("Exception in CompanyInfo Header " + e);	
  }
}%>