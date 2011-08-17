<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<script language="javascript">

function showHTMLReport()
{
	if (!checkForm())
	{
		alert('Please choose a correct number of days');
	}
	else
	{
		document.form.reportType.value='HTML';
		document.form.submit();
	}
}

function showXLSReport()
{
	if (!checkForm())
	{
		alert('Please choose a correct number of days');
	}
	else
	{
		document.form.reportType.value='XLS';
		document.form.submit();
	}
}

function checkForm()
{
	if ( (document.form.nbDays.value=='') || isNaN(document.form.nbDays.value) )
	{
		return false
	}
	else
	{
		return true;
	}
}

</script>

<style type="text/css">
.sppSectionHeader
{
	border-style: solid;
	border-width: 0px; 
	border-bottom-width: 5px;
	border-color: #FF6600;
}
</style>


<h1>User aging report</h1>
<br/>
<br/>
<%


// Query to find the Users
String query = "SELECT USER_NAME, FIRST_NAME, LAST_NAME, LAST_LOGIN_DATE FROM USERS WHERE LAST_LOGIN_DATE <= SYSDATE - ";
String queryMaxResults = " AND ROWNUM <= ";
String queryOrder = " ORDER BY LAST_LOGIN_DATE";
String reportName = "UserAgingReport";
String xlsJspReportURL = "/portal/jsp/spp/reports/XLSReport.jsp";

// Labels to display in the report
java.util.ArrayList labelList = new java.util.ArrayList();
labelList.add("USER_NAME");
labelList.add("FIRST_NAME");
labelList.add("LAST_NAME");
labelList.add("LAST_LOGIN_DATE");

%>

<p class="sppSectionHeader">Please enter the report criteria</p>

<form action="#" name="form" method="POST">

Number of days : <input size="4" type="text" name="nbDays">
<br>
Number of results : 
<select name="nbResults">
	<option value="50" selected>50</option>
	<option value="100">100</option>
	<option value="200">200</option>
	<option value="500">500</option>	
</select>
<input type="hidden" name="calculateReport" value="1">
<input type="hidden" name="reportType" value="">
<br/>
<br/>
<input type="button" onClick="showHTMLReport()" value="Show report">
<input type="button" onClick="showXLSReport()" value="Generate XLS report">
</form>

<%
if ("1".equals(request.getParameter("calculateReport")))
{
	
	String finalQuery = query + request.getParameter("nbDays");
	finalQuery += queryMaxResults + request.getParameter("nbResults");
	finalQuery += queryOrder;
	
	com.hp.spp.portal.common.sql.SPPSQLManager sqlm = com.hp.spp.portal.common.sql.SPPSQLManager.getInstance();
	java.util.ArrayList sqlResult = sqlm.executeSelectQuery(finalQuery);
	
	out.print("<p class=\"sppSectionHeader\">Report results</p>");
	
	// check the type of the report to generate
	
	if ("HTML".equals(request.getParameter("reportType")))
	{
		// USE HTML formatter
		
		com.hp.spp.portal.reports.HTMLReportsFormatter formatter = new com.hp.spp.portal.reports.HTMLReportsFormatter();
		out.println(formatter.format(labelList, sqlResult));
	}
	else if ("XLS".equals(request.getParameter("reportType")))
	{
		// USE XLS formatter
		//out.println("<p>Please wait while the XLS file is created, if download does not start automatically, click <a href=\""+xlsJspReportURL+"\">here</a></p>");
		out.println("<p>Please wait while the XLS file is created</p>");
		
		request.getSession().setAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_DATALIST, sqlResult);
		request.getSession().setAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_LABELLIST, labelList);		
		request.getSession().setAttribute(com.hp.spp.portal.reports.ReportsConstants.SESSION_KEY_REPORT_NAME, reportName);		
		
		out.print("<script>setTimeout(\"window.location.href='"+xlsJspReportURL+"'\", 1000)</script>");
	}
	else
	{
		// No formatter : error
		out.print("<h1>ERROR no selected report type</h1>");
	}
}

%>
