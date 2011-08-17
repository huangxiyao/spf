<%@ page contentType="text/html;charset=UTF-8" %><%
	if ("POST".equals(request.getMethod())) {
		java.io.Reader xml = null;
		String contentType = request.getContentType() ;
		if (contentType != null && contentType.startsWith("text/xml")) {
			xml = new java.io.InputStreamReader(request.getInputStream(), "UTF-8");
		}
		else if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
			xml = new java.io.StringReader(request.getParameter("xml"));
		}
		if (xml == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to retrieve XML input; content type: " + request.getContentType());
		}
		else {
			response.setContentType("text/xml");
			com.hp.spp.wsrp.export.Export export = new com.hp.spp.wsrp.export.RemotePortletHandleExport();
			export.export(xml, out);
		}
	}
	else {
%>
<html>
<head>
	<title>Portlet handle export</title>
</head>
<body>
<form method="POST">
	<input type="submit" value="Export" /><br />
	<textarea rows="40" cols="80" name="xml"></textarea><br />
</form>
</body>
</html>
<%
	}
%>
