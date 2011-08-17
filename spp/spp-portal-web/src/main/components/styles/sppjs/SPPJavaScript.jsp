<%@ page import="com.hp.spp.config.Config"%>
<%
	String resourcePath = Config.getValue("SPP.StaticResourcesBaseUri", "/portal/spp");
%>

<script type="text/javascript" language="JavaScript">
<!--
var isDHTML = 0;
var isID = 0;
var isAll = 0;
var isLayers = 0;
if (document.getElementById) {isID = 1; isDHTML = 1;}
else {
if (document.all) {isAll = 1; isDHTML = 1;}
else {
browserVersion = parseInt(navigator.appVersion);
if ((navigator.appName.indexOf('Netscape') != -1) && (browserVersion == 4)) {isLayers = 1; isDHTML = 1;}
}}
//-->
</script>
<script type="text/javascript" language="JavaScript" src="<%=resourcePath%>/js/sppFunctions.js"></script>
