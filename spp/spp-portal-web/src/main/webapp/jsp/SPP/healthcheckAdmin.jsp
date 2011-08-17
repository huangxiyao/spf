<%@ page import="org.apache.log4j.Logger,
				java.net.URL,
				java.net.URLConnection,
				java.net.HttpURLConnection,
				com.hp.spp.common.healthcheck.HealthcheckServerInfo,
				com.hp.spp.common.healthcheck.ConfigBackedHealthcheckDAO,
				com.hp.spp.common.healthcheck.HealthcheckDAO,
				com.hp.spp.common.healthcheck.HealthcheckDAOFactory,
				com.hp.spp.common.healthcheck.HealthcheckHelper,
				com.hp.spp.config.ConfigException,
				com.hp.spp.config.Config,
				com.hp.spp.config.ConfigEntry"
%>
<%

Iterator<String> fi;
String optionValue = null;
List<HealthcheckServerInfo> list = null;
String filterByServerName = null;
String filterByApplicationName = null;
String filterByServerType = null;
String filterBySiteName = null;
String selectedTag = null;

String action = request.getParameter("action");
if ((action == null) || action.trim().equals("")) {
	action = "view";
}

HealthcheckDAO dao = new HealthcheckDAOFactory().createHealthcheckDAO();

if ("save".equals(action)) {
	String outOfRotationServers = request.getParameter("outOfRotationList");
	list = dao.getAllServers();
	Iterator<HealthcheckServerInfo> i = list.iterator();
    while(i.hasNext()) {
		HealthcheckServerInfo serverInfo = i.next();
		if (outOfRotationServers.indexOf(serverInfo.getServerName()) >= 0) {
			serverInfo.setOutOfRotation(true);
		} else {
			serverInfo.setOutOfRotation(false);
		}
     }
	 // save it to DB
	 dao.updateOutOfRotationFlag(list);
 	 response.sendRedirect(request.getRequestURI());
	 return; 
}

if ("filter".equals(action)) {

	filterByServerName = request.getParameter("selByServerName");
	filterByApplicationName = request.getParameter("selByAppName");
	filterByServerType = request.getParameter("selByServerType");
	filterBySiteName = request.getParameter("selBySiteName");

	if ("All".equals(filterByServerName)) {
			filterByServerName = null;
	}
	if ("All".equals(filterByApplicationName)) {
			filterByApplicationName = null;
	}
	if ("All".equals(filterByServerType)) {
			filterByServerType = null;
	}
	if ("All".equals(filterBySiteName)) {
			filterBySiteName = null;
	}
	
	HealthcheckServerInfo queryExampleInfo = 
		new HealthcheckServerInfo(filterByServerName, filterByApplicationName, filterByServerType, filterBySiteName);

	list = dao.findServersByExample(queryExampleInfo);
}

if ("view".equals(action)) {
	// Get the list of all web server names from DB.
	list = dao.getAllServers();
}
%>
<html>
<head>
	<title>Layer 7 Health check Admin page</title>
	<script type="text/javascript" language="JavaScript"><!--
	var theme = '#336633';
	//--></script>
	<script type="text/javascript" language="JavaScript" src="http://www.hp.com/country/us/en/js/hpweb_utilities.js"></script>
	<link rel="shortcut icon" href="http://welcome.hp-ww.com/img/favicon.ico">
</head>

<body>
<h1>Layer 7 Health check Admin page</h1>
<p>This admin page is to set the flag to Layer 7 health check routine to trigger selected server is down
<br>This function doesn't shutdown the server physically, but just simulating so that the layer 7 health check returns server down message to the local load balancer.</p>

<form action="<%=request.getRequestURL()%>" method="get" name="hcAdmin">
	<input type="hidden" name="outOfRotationList" value="">
	<input type="hidden" name="action" value="view" />
	<input type="hidden" name="selByServerName" value="" />
	<input type="hidden" name="selByAppName" value="" />
	<input type="hidden" name="selByServerType" value="" />
	<input type="hidden" name="selBySiteName" value="" />

	<table border="1" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC">
		<tr class="theme">
			<td class="small" scope="col"><span class="themebody">Filter: <select name="byServerName" onChange="filterForm();">
					<option value="All">All</option>
					<%	List<String> filterAllServerName = HealthcheckHelper.getFilterAllOption("serverName");
					  fi = filterAllServerName.iterator();
						while(fi.hasNext()) { 
							optionValue=fi.next(); 
							if (optionValue.equals(filterByServerName)) {
								selectedTag = "selected";
							} else { 
								selectedTag = "";
							} %>
						<option <%=selectedTag%> value="<%=optionValue%>"><%=optionValue%></option>
					<%  } %>
			     </select></span></td>
			<td class="small" scope="col"><span class="themebody">Filter: <select name="byAppName" onChange="filterForm();">
					<option value="All">All</option>
					<%	List<String> filterAllApplicationName = HealthcheckHelper.getFilterAllOption("applicationName");
					  fi = filterAllApplicationName.iterator();
						while(fi.hasNext()) { 
							optionValue=fi.next();
							if (optionValue.equals(filterByApplicationName)) {
								selectedTag = "selected";
							} else { 
								selectedTag = "";
							} %>
						<option <%=selectedTag%> value="<%=optionValue%>"><%=optionValue%></option>
					<%  } %>
			     </select></span></td>
			<td class="small" scope="col"><span class="themebody">Filter: <select name="byServerType" onChange="filterForm();">
					<option value="All">All</option>
					<%	List<String> filterAllServerType = HealthcheckHelper.getFilterAllOption("serverType");
					  fi = filterAllServerType.iterator();
						while(fi.hasNext()) { 
							optionValue=fi.next();
							if (optionValue.equals(filterByServerType)) {
								selectedTag = "selected";
							} else { 
								selectedTag = "";
							} %>
						<option <%=selectedTag%> value="<%=optionValue%>"><%=optionValue%></option>
					<%  } %>
			     </select></span></td>
			<td class="small" scope="col"><span class="themebody">Filter: <select name="bySiteName" onChange="filterForm();">
					<option value="All">All</option>
					<%	List<String> filterAllSiteName = HealthcheckHelper.getFilterAllOption("siteName");
					  fi = filterAllSiteName.iterator();
						while(fi.hasNext()) { 
							optionValue=fi.next();
							if (optionValue.equals(filterBySiteName)) {
								selectedTag = "selected";
							} else { 
								selectedTag = "";
							} %>
						<option <%=selectedTag%> value="<%=optionValue%>"><%=optionValue%></option>
					<%  } %>
			     </select></span></td>
			<td class="small" align="center" scope="col"><span class="themebody">&nbsp;</td>
		</tr>
		<tr class="theme">
			<th width="25%" class="small" scope="col"><span class="themebody">Web Server</span></th>
			<th width="15%" class="small" scope="col"><span class="themebody">Application</span></th>
			<th width="15%" class="small" scope="col"><span class="themebody">Server Type</span></th>
			<th width="15%" class="small" scope="col"><span class="themebody">Site</span></th>
			<th width="15%" class="small" scope="col"><span class="themebody">LB Out of Rotation?</span></th>
		</tr>

		<%  Iterator<HealthcheckServerInfo> i = list.iterator();
			while(i.hasNext()) {
				HealthcheckServerInfo serverInfo = (HealthcheckServerInfo) i.next();
				String yesCheckedValue = (serverInfo.getOutOfRotation() == true)? "checked" : "";
				String noCheckedValue  = (serverInfo.getOutOfRotation() == false)? "checked" : "";   %>
			<tr>
				<td><%=serverInfo.getServerName()%></td>
				<td><%=serverInfo.getApplicationName()%></td>
				<td><%=serverInfo.getServerType()%></td>
				<td><%=serverInfo.getSiteName()%></td>
				<td align="center">
					<input type="radio" name="server<%=serverInfo.getServerName()%>" value="<%=serverInfo.getServerName()%>" <%=yesCheckedValue%> />Yes 
					<input type="radio" name="server<%=serverInfo.getServerName()%>" value="" <%=noCheckedValue%> />No
				</td>
			</tr>
		<% 		} %>
	</table>
<p><input type="button" value="Submit" onClick="saveForm();"></p>
<br>
</form>

<SCRIPT language="JavaScript">		
function saveForm()
{
	var name_list = ""; 	
    var allElementList = document.hcAdmin.elements; 
    for( var i = 0; i < allElementList.length; i++){
        var element = allElementList[i];//element is taken
        if( element.type == "radio"){
            if(( element.checked == true) && (element.value !== "")) {
					name_list = name_list + element.value + ", ";					
            }
        }
    }
	document.hcAdmin.outOfRotationList.value=name_list;
	document.hcAdmin.action.value='save';
	document.hcAdmin.method = 'post';
	document.hcAdmin.submit();
}		
function filterForm(thisObj)
{
	// collect all filters
	document.hcAdmin.selByServerName.value =
		document.hcAdmin.byServerName.options[document.hcAdmin.byServerName.selectedIndex].value;
	document.hcAdmin.selByAppName.value =
		document.hcAdmin.byAppName.options[document.hcAdmin.byAppName.selectedIndex].value;
	document.hcAdmin.selByServerType.value =
		document.hcAdmin.byServerType.options[document.hcAdmin.byServerType.selectedIndex].value;
	document.hcAdmin.selBySiteName.value =
		document.hcAdmin.bySiteName.options[document.hcAdmin.bySiteName.selectedIndex].value;

	document.hcAdmin.action.value='filter';
	document.hcAdmin.method = 'get';
	document.hcAdmin.submit();
}		
</SCRIPT>

</body>
</html>