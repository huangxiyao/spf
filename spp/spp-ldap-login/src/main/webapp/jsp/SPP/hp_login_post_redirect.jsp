<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
	String postRedirectUrl = (String) pageContext.findAttribute("PostRedirectUrl");
	Map postRedirectParams = (Map) pageContext.findAttribute("PostRedirectParams");
%>
<html>
<body onload="document.forms['PostRedirectForm'].submit();">
<form action="<%=postRedirectUrl%>" method="POST" name="PostRedirectForm">
	<%
		for (Iterator it = postRedirectParams.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry param = (Map.Entry) it.next();
			String name = (String) param.getKey();
			String[] values = (String[]) param.getValue();
			if (values != null) {
				for (int i = 0, len = values.length; i < len; ++i ) {
					String value = values[i];
	%>
	<input type="hidden" name="<%=name%>" value="<%=value%>" />
	<%
				}
			}
		}
	%>
</form>
</body>
</html>
