<%@page import="com.hp.sesame.log.Log"%>
<%
Log.fatal(this.getClass(), ">>>>> stopBatchUGS");
application.setAttribute("stopBatchUGS", "OK");
out.println("<BR>>>>>> Stop Batch UGS");
%>