
<!-- report.jsp -->
<%/*--
	@(#)report.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        Girish             29-Oct-2006          Created

	Note :
	
--*/%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="ageingReportURL">
	<portlet:param name="action" value="ageingReport" />
</portlet:actionURL>

<html>
<title></title>
<head>
<script type="text/javascript">

function report()
{
		document.forms["reportForm"].action='<c:out value="${ageingReportURL}" escapeXml="false"/>';
		document.forms["reportForm"].submit();
}
</script>

<style type="text/css">
 .theme {bgcolor:#0066FF;}
</style>
</head>

<body>
<!-- Search table ----->
<p><form name="reportForm" method="post" action="">
<spring:bind path="lastLoginCommand">
<c:forEach items="${status.errorMessage}" var ="errorMessage"> 
<font color="red">
<c:out value ="${errorMessage}"/><br>
</font>
</c:forEach>
</spring:bind>


<table width="95%" bgcolor="f8f8ff" border="0" cellspacing="0" cellpadding="5">
<tr>
<td alignment="right" width="20%">No of days since last login:</td>
<spring:bind path="lastLoginCommand.lastLoginNumDays">
<td width="20%">
<input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
</td>
</spring:bind>
<td width="60%">
</tr>

</table>
<br>
<br>
<input type="submit" alignment="center" value="report" onClick="report()">
</form>
</p>


</body>
</html>
