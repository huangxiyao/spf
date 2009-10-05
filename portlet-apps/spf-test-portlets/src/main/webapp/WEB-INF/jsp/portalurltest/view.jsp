<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<jsp:include page="renderParams.jsp" />
<hr />
<form action="<portlet:actionURL />" method="POST">
	Set a render parameter (name = value):
	<input type="text" name="paramName" id="paramName" /> =
	<input type="text" name="paramValue" id="paramValue" />
	<input type="submit" value="Set" />
</form>
<small>
	For multi-value parameters use comma as a separator in parameter value field.
	This portlet supports a public render parameter named <strong>pubRenderParam</strong>.
</small>
