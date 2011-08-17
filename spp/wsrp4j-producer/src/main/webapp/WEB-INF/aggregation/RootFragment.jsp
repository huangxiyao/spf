<%--
Copyright 2004 The Apache Software Foundation
Licensed  under the  Apache License,  Version 2.0  (the "License");
you may not use  this file  except in  compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed  under the  License is distributed on an "AS IS" BASIS,
WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
implied.

See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page session="true" buffer="none" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.pluto.portalImpl.aggregation.Fragment" %>
<%@ page import="org.apache.pluto.portalImpl.aggregation.navigation.AbstractNavigationFragment" %>
<jsp:useBean id="fragment" type="org.apache.pluto.portalImpl.aggregation.Fragment" scope="request" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en-us">
<head>
	<title>Portlet Test Page</title>
	<!-- For use with white text: #990000,#CC0066,#336666,#003366,#000000,#666666,#336633,#EB5F01,#0066FF,#4FAF00 -->
	<!-- For use with black text: #FFCC00 -->
	<script type="text/javascript" language="JavaScript"><!--
	var theme = '#336666';
	//--></script>

	<script type="text/javascript" language="JavaScript" src="http://welcome.hp-ww.com/country/us/en/js/hpweb_utilities.js"></script>

</head>
<body class="colorFFFFFFbg" text="#000000" link="#003366" alink="#003366" vlink="#660066" marginheight="0" marginwidth="0" leftmargin="0" topmargin="0">
<!-- Begin Top Navigation Area -->
<table border="0" cellpadding="0" cellspacing="0" width="740">
	<tr class="decoration">
		<td width="10"><a href="#jumptocontent"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt="Jump to content" border="0"></a><noscript><a href="http://welcome.hp.com/country/us/en/noscript.html">summary of site-wide JavaScript functionality</a></noscript></td>
		<td align="left" width="260" class="smallbold"><!-- Welcome, 123456789012345678901234567890... --></td>
		<td><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="24" alt="" border="0"></td>
		<td align="left" width="155" class="color003366"><!-- &raquo;&nbsp;<a href="Insert_Link_Here" class="small">Sign-out</a>&nbsp;<span class="color666666">|</span>&nbsp;&raquo;&nbsp;<a href="Insert_Link_Here" class="small">Edit your profile</a> --></td>
		<td align="right" width="285" class="countryInd">United States-English</td>
		<td><img src="http://welcome.hp-ww.com/img/s.gif" width="20" height="1" alt=""></td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td align="left" valign="top" bgcolor="#666666">
			&nbsp;
		</td>
	</tr>
</table>
<!-- End Top Navigation Area -->
<!-- Begin Search Area -->
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td align="left" valign="top" bgcolor="#E7E7E7">
			<br /><br /><br />
		</td>
	</tr>
</table>
<!-- End Search Area -->
<!-- Begin Page Title Area -->
<table border="0" cellpadding="0" cellspacing="0" width="740">
	<tr>
		<td width="170" align="center" valign="middle"><a href="http://welcome.hp.com/country/us/en/welcome.html"><img src="http://welcome.hp-ww.com/img/hpweb_1-2_topnav_hp_logo.gif" width="64" height="55" alt="HP.com home" border="0"></a><br></td>
		<td width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="93" alt=""></td>
		<td width="560" align="left" valign="top">
			<br />
			<img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="6" alt=""><h1 id="pageTitle">Portlet Test Page</h1>
		</td>
	</tr>
</table>
<!-- End Page Title Area -->
<!-- Begin Left Navigation and Content Area.  To increase width of content area, modify width of table below. -->
<table border="0" cellpadding="0" cellspacing="0" width="740">
	<tr>
<!-- Start Left Navigation -->
		<td width="170" align="left" valign="top" bgcolor="#F0F0F0">
			<table border="0" cellpadding="0" cellspacing="0" width="170" background="http://welcome.hp-ww.com/img/s.gif">
				<tr class="colorDCDCDCbg">
					<td align="left" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt=""></td>
					<td align="left" valign="top" width="10" class="color003366bld">&raquo;&nbsp;</td>
					<td align="left" valign="middle"><h2><a href="<%=request.getContextPath() + request.getServletPath()%>" class="bold">Home</a></h2></td>
				</tr>
			</table>
			<table border="0" cellpadding="0" cellspacing="0" width="170" background="http://welcome.hp-ww.com/img/s.gif">
				<tr>
					<td align="left" valign="top" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt=""></td>
					<td align="left" valign="top" width="150">
						<table border="0" cellpadding="0" cellspacing="0" width="150" background="http://welcome.hp-ww.com/img/s.gif">
							<tr class="decoration">
								<td align="left" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="10" alt=""></td>
								<td align="left" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="10" alt=""></td>
								<td align="left" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="10" alt=""></td>
								<td align="left" width="120"><img src="http://welcome.hp-ww.com/img/s.gif" width="120" height="10" alt=""></td>
							</tr>
							<%
								for (Iterator childIterator = fragment.getChildFragments().iterator(); childIterator.hasNext(); ) {
									Fragment subfragment = (Fragment)childIterator.next();
									if (subfragment instanceof AbstractNavigationFragment)
									{
										subfragment.service(request, response);
										break;
									}
								}
							%>
							<tr class="decoration">
								<td align="left" width="150" colspan="4"><img src="http://welcome.hp-ww.com/img/s.gif" width="150" height="10" alt=""></td>
							</tr>
						</table>
					</td>
					<td align="left" valign="top" width="10"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt=""></td>
				</tr>
				<tr class="decoration">
					<td colspan="3" class="colorCCCCCCbg"><img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="2" alt="" border="0"></td>
				</tr>
				<tr class="decoration">
					<td colspan="3"><img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="60" alt="" border="0"></td>
				</tr>
			</table>
		</td>
<!-- End Left Navigation -->
<!-- Begin Gutter Cell 10px Wide -->
		<td valign="top" width="10"><a name="jumptocontent"><img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt="Content starts here"></a></td>
<!-- End Gutter Cell 10px Wide -->
<!--startindex-->
<!-- Start Content Area.  To increase width of content area, modify width of table cell below. -->
		<td width="560" align="left" valign="top">
			<%
				for (Iterator childIterator = fragment.getChildFragments().iterator(); childIterator.hasNext(); ) {
					Fragment subfragment = (Fragment)childIterator.next();
					if (!(subfragment instanceof AbstractNavigationFragment))
					{
						subfragment.service(request, response);
					}
				}
			%>
		</td>
<!-- End Content Area -->
<!--stopindex-->
	</tr>
</table>
<!-- End Left Navigation and Content Area -->
<!-- Begin Footer Area -->
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="decoration">
		<td class="color666666bg"><img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="4" alt="" border="0"></td>
	</tr>
	<tr>
		<td align="left" valign="top">
			<table border="0" cellpadding="0" cellspacing="0" width="740">
				<tr class="decoration">
					<td colspan="4"><img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="4" alt="" border="0"></td>
				</tr>
				<tr>
					<td width="33%" align="center"><a href="http://welcome.hp.com/country/us/en/privacy.html" class="udrlinesmall">Privacy statement</a></td>
					<td width="33%" align="center"><a href="http://welcome.hp.com/country/us/en/termsofuse.html" class="udrlinesmall">Using this site means you accept its terms</a></td>
					<td width="33%" align="center"><!-- Activate Feedback Link Here --><!--<a href="mailto:spp-code-support@hp.com" class="udrlinesmall">Feedback to SPP</a>--><!-- End Activate Feedback Link Here -->&nbsp;</td>
				</tr>
				<tr class="decoration">
					<td colspan="4"><img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="4" alt=""></td>
				</tr>
				<tr>
					<td align="center" colspan="4" class="small">&#169; 2007 Hewlett-Packard Development Company, L.P.</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<!-- End Footer Area -->

</body>
</html>