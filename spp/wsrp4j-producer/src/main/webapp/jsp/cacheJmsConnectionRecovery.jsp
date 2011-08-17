<%@ page import="com.hp.frameworks.wpa.wsrp4j.services.portletentityregistry.RegistryCache"%>
<%@ page import="com.hp.spp.cache.Cache"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
	RegistryCache.getInstance().flushEntry("com.hp.spp.cache.jms.TopicConnectionRecovery");
	Cache.getInstance().flushEntry("com.hp.spp.cache.jms.TopicConnectionRecovery");
%>
Connection to JMS server should now be recovered if the JMS server is available. If the server
is not available the error message is available in the log file.
