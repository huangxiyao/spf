<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>

<h1>An error has occured</h1>
<br>

<%
/*
java.util.Enumeration att = request.getAttributeNames();
while (att.hasMoreElements())
{
	String s = (String) att.nextElement();
	out.print(s +" : " +request.getAttribute(s) +"<br>" );
}
*/
// get the exception from the request and display it
Exception e = (Exception) request.getAttribute("exception");

if (e !=null)
{
	out.print("<h2><font color=\"red\">Error message :<br>");
	out.print(e.getMessage());
	out.print("</font></h2>");
}
else
{
	out.print("<h2>Unknown error message</h2>");
}
%>

<portlet:renderURL var="actionBackUrl">
	<portlet:param name="action" value="ageingReport"/>
</portlet:renderURL>

<p style="text-align:center;"><a href='<c:out value="${actionBackUrl}" escapeXml="false"/>'>- HOME -</a></p>
