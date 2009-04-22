<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects />
<%
        if (portletSession.getAttribute("result") != null) {
            Map<String, String> result = (Map<String, String>) portletSession.getAttribute("result");
%>
<fieldset>
    <h2>Last Upload results</h2>
    <%
                for (Map.Entry<String, String> entry : result.entrySet()) {
    %>
    <div>
        <strong><%=entry.getKey()%>:</strong>
        <%=entry.getValue()%>
    </div>
    <%
                }
    %>
</fieldset>
<%
        }
%>
<form action="<portlet:actionURL />" method="POST" enctype="multipart/form-data">
    <label for="file1">File 1:</label> <input type="file" name="file1" id="file1"/><br />
    <label for="file2">File 2:</label> <input type="file" name="file2" id="file2" /><br />
    <label for="textentry">Some text:</label> <input type="text" name="textentry" id="textentry" /><br />
    <label>Checkboxes:</label>
    <input type="checkbox" name="multi" value="checkbox1" />Checkbox 1
    <input type="checkbox" name="multi" value="checkbox2" />Checkbox 2<br />
    <input type="submit" value="Upload" />
</form>