<%--
This is the jsp page which is responsible for rendering
the grid of the Site index page. This page ensures that the site index
page is rendered without any frame. This is important so that the 
spider does not crawl the links on the header, footer or side bars of the 
index page.

--%><%@ page import="com.epicentric.common.Installation,
                     com.epicentric.common.website.ParameterConstants,
                     com.epicentric.common.website.EndUserUtils,
                     java.util.*,
                     com.epicentric.template.*"
%><%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<vgn-portal:defineObjects/>
<%
        Style _thisStyleObject = portalContext.getCurrentStyle();
        String _pathToImages = portalContext.getPortalHttpRoot() + _thisStyleObject.getUrlSafeRelativePath();
%>



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<vgn-portal:pageContentTitle />
	<vgn-portal:styleBlock />
    <script language="JavaScript" src="<%= portalContext.getPortalHttpRoot() %>jslib/form_state_manager.js"></script>
    <noscript><vgn-portal:i18nValue stringID="<%= _thisStyleObject.getUID() %>" key="noscript" defaultValue="In order to bring you the best possible user experience, this site uses Javascript. If you are seeing this message, it is likely that the Javascript option in your browser is disabled. For optimal viewing of this site, please ensure that Javascript is enabled for your browser."/>
    </noscript>
<base target="_top">
</head>

<body topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0" class="epi-pageBG">


<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
	<tr>
		<td valign="top">

		
       
        <!-- <a name="mainContent" id="mainContent"></a> -->
        <!-- begin page content area -->
        <vgn-portal:includePageContent />
        <!-- end page content area -->

            </td>
	    </tr>
    	<!-- <tr>
		<td valign="bottom">
			<!-- begin footer area -->
			<vgn-portal:includeStyle friendlyID="footer" />
			<!-- end footer area -->
		</td>
	</tr> -->
</table>

</body>
</html>
