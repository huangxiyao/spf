<!-- Menuitemeport.jsp -->
<%/*--
	@(#)Menuitemeport.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        Srikanth Adepu          02-Sep-2009           Created
	
--*/%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
    
<%@ page import="com.epicentric.common.website.EndUserUtils,
	com.epicentric.site.Site,
	com.vignette.portal.website.enduser.PortalContext,
	org.apache.log4j.Logger,
	com.hp.spp.portal.util.UserGroupAssignmentReportConstants"
%>    

<script language="javascript">

function checkForm()
{
	if (document.form.site.value == '-1')
	{
		alert('Please select the site');
		return false
	}
	else
	{
		document.form.submit();
		return true;
	}
}

</script>

<%
	PortalContext portalContext = EndUserUtils.getPortalContext(pageContext);
	Site siteName= EndUserUtils.getSite(pageContext);
	String siteDNSName = siteName.getDNSName();	
%>

<h1>User Group Assignment Report</h1>
<br/>

<style type="text/css">
p
{
background-color:#F5FBEF;
}
p.padding
{
padding-top:15px;
padding-bottom:15px;
padding-right:0px;
padding-left:5px;
}
</style>

<form method="POST" name= "form" action = <%=request.getContextPath()+"/UserGroupAssignmentReportServlet"%> >
<p class="padding">Site : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        

<select name="site">
	<option value="-1" selected>SelectOne</option>
	<option value=<%=siteDNSName%>>Secured</option>
	<option value=<%="public"+siteDNSName%>>Unsecured</option>
	<option value="Both">Both</option>
</select></p>

<input type = "hidden" name = "bothSites" value = <%=siteDNSName%> />

<input type="button" onClick = "checkForm()" value="Report"/>


<%
if (session.getAttribute("spp_ugsreport_secret_key") == null){
	session.setAttribute("spp_ugsreport_secret_key", UserGroupAssignmentReportConstants.SPP_UGSREPORT_SECRETKEY);
}
%>

</form>	


