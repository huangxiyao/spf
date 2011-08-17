<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="org.apache.commons.httpclient.methods.GetMethod"%>
<html>
<%
out.println("NonProxyHost starting");
URL targetUrl = new URL("http://localhost/portal/getUserProfile.jsp");

HttpClient httpclient = new HttpClient();

GetMethod httpget = new GetMethod(targetUrl.toString());

// Retrieval of input stream
InputStream inputStream = null;
try {
	httpclient.executeMethod(httpget);
	inputStream = new ByteArrayInputStream(httpget.getResponseBody());
	out.println("http status line : "+httpget.getStatusLine());
} finally {
	httpget.releaseConnection();
}
out.println("NonProxyHost stoping");
%>
</html>
