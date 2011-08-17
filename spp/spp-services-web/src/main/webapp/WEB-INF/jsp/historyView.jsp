<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>

<b>Definitions change history</b>
<br><br>

<script type="text/javascript">
<!--

function exportHistory(formName)
{
	document.forms[formName].submit();
}



//-->
</script>

<table border="1" cellspacing="0">
	<tr style="background-color: darkblue;color: white">
		<td>Date (mm/dd/yyyy)<br>
		Time (EST)</td>
		<td>Who uploaded XML file</td>
		<td>Comment</td>
		<td>Change</td>
		<td>retrieve backup</td>
	</tr>
	
<portlet:actionURL var="exportHistoryUrl">
	<portlet:param name="action" value="exportHistory"/>
</portlet:actionURL>

<% 
	String exportSPPHistoryUrl = String.valueOf(pageContext.findAttribute("exportHistoryUrl"));
	java.util.HashMap model = (java.util.HashMap) pageContext
					.findAttribute("model");

			com.hp.spp.common.util.format.Formatter formatter = new com.hp.spp.common.util.format.Formatter();

			java.util.List historyList = (java.util.List) model.get("history");
			java.util.Iterator it = historyList.iterator();
			
			while (it.hasNext()) {
				com.hp.spp.common.ResourceHistory rs = (com.hp.spp.common.ResourceHistory) it.next();
				long id = rs.getId();
				
				String dataChange = new String(rs.getDataChange());
				
				out.print("<form method=\"POST\" action=\""+exportSPPHistoryUrl+"\" name=\"exportHistory_"+id+"\">");
				out.print("<input type=\"hidden\" name=\"backupId\" value=\""+id+"\"");
				out.print("<tr valign=\"top\"><td>"
								+ formatter.dateToStringWithHours(rs.getCreationDate())
								+ "</td><td>"
								+ StringEscapeUtils.escapeHtml(rs.getOwner())
								+ "</td><td>"
								+ StringEscapeUtils.escapeHtml(rs.getComment())
								+ "</td><td>"
								+ dataChange
								+ "</td><td>"
								+ "<a href=\"javascript:exportHistory('exportHistory_"+id+"')\">backup</a>"
								+ "</td></tr>");
				out.print("</form>");
			}

		%>

</table>
<br>
<portlet:renderURL var="actionBackUrl">
	<c:choose>
		<c:when test="${backView == 'standardParameterSets'}">
			<portlet:param name="action" value="standardParameterSets"/>
		</c:when>
		<c:when test="${backView == 'eservices'}">
			<portlet:param name="action" value="eservices"/>
		</c:when>
		<c:when test="${backView == 'groups'}">
			<portlet:param name="action" value="groups"/>
		</c:when>
	</c:choose>
</portlet:renderURL>

<p style="text-align:center;"><a href='<c:out value="${actionBackUrl}" escapeXml="false"/>'>Back</a></p>

