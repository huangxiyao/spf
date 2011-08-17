<%@page import="com.hp.spp.portal.common.site.*" %>

<%@page import="java.util.Set" %>
<%@page import="com.hp.spp.portal.common.dao.CommonDAO" %>
<%@page import="com.hp.spp.portal.common.dao.CommonDAOCacheImpl" %>
<%@page import="com.hp.spp.portal.common.dao.CommonDAOSQLManagerImpl" %>
<%@page import="com.hp.spp.portal.common.sql.*" %>

<html>
<head>
	<title>Site Down Admin Page</title>
	<script type="text/javascript" language="JavaScript"><!--
	var theme = '#336633';
	//--></script>
	<script type="text/javascript" language="JavaScript" src="http://www.hp.com/country/us/en/js/hpweb_utilities.js"></script>
	<link rel="shortcut icon" href="http://welcome.hp-ww.com/img/favicon.ico">
</head>

<h1>Site Down Admin Page</h1>

<p>
Please select the site for which you would like to provide Site Down Message.
<br>
Select the appropriate radio button if you wish to make the site disable/unable. 
<br>
Please provide a proper accesscode for accessing the disabled site.
</p>

<form action="/portal/jsp/SPP/SiteDown.jsp" name="sitetable">
	<input type="hidden" name="siteDownSubmit" value="True">

	<table border="0" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC" width="740">
		<tr class="theme">
			<th width="30%" class="small" scope="col"><span class="themebody">Feature</span></th>
			<th width="70%" class="small" scope="col"><span class="themebody">Value</span></th>
		</tr>

		<tr bgcolor="#E7E7E7">			
			<td>Select Site</td>
			<td>
			<select name="sites" align="left">
			<%
			try{
			CommonDAO commonDao = CommonDAOCacheImpl.getInstance();
			Set supportedSites = commonDao.getSupportedSites();
			Iterator<String> iter = supportedSites.iterator();
			while (iter.hasNext()) {
				String element = iter.next();
					if ( !element.equals("console") && !element.equals("DUMMY_SITE") ){
			%>
			<option value="<%=element%>"><%=element%></option>
			<%
					}
				}
			}catch(Exception e){
				out.println("Exception in supported site");
			}
			%></select>
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
		<td>Enable/Disable</td>
		<td>
			<input type="radio" name="access" value=1> Enable
			<br>
			<input type="radio" name="access" value=0> Disable
		</td>
		</tr>


		<tr bgcolor="#E7E7E7">
		<td>Site Down Message</td>
		<td>
			<textarea name="sitedownmessage" value="Site Down For maintanance purpose" align="left" rows="3" cols="70%" >
			Write the message for site down here.
			</textarea>
		</td>
		</tr>

		<tr bgcolor="#FFFFFF">
		<td>Passcode</td>
		<td>
			<input type="text" name="passcode" align="left">
		</td>
		</tr>

</table>
</form>

<%
if("True".equals(request.getParameter("siteDownSubmit")) ){

	String sitedownmessage = request.getParameter("sitedownmessage");
	String accessCode = request.getParameter("passcode");
	String siteName = request.getParameter("sites");
	
	SiteManager.getInstance().getSite(siteName).setSiteDownMessage(sitedownmessage);
	SiteManager.getInstance().updateSite(SiteManager.getInstance().getSite(siteName));

	PortalCommonDAO portalCommonDao = PortalCommonDAOCacheImpl.getInstance();
	portalCommonDao.setAccessCode(siteName, accessCode);
	portalCommonDao.setSiteAccessible(siteName,Integer.valueOf(request.getParameter("access") ));

	%>
	<p>
	<font size="4" color="red">
	<%="Site Access details are changed according to user request"%>
	</p>
	<%

}
%>


<br>
<input type="submit" value="Submit" onClick="getSiteName()">
<br>

<SCRIPT language="JavaScript">
		
		function getSiteName()
			{
			document.forms['sitetable'].submit();
			}
		
</SCRIPT>
