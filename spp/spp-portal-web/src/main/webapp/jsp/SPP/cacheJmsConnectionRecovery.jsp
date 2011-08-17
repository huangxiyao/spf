<%@ page import="com.hp.spp.cache.Cache"%>
<%@ page import="com.hp.spp.cache.CacheMonitor"%>
<%@ page import="com.hp.spp.cache.JMSListenerHelper"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
	//Cache.getInstance().flushEntry("com.hp.spp.cache.jms.TopicConnectionRecovery");
	CacheMonitor.getInstance().forceFlushEntryInAllMonitoredCaches(JMSListenerHelper.JMS_RECOVERY_CACHE_KEY);
%>
Connection to JMS server should now be recovered if the JMS server is available. If the server
is not available the error message is available in the log file.
