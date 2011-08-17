<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.hp.spp.cache.CacheMonitor"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Set"%>
<%@ page import="com.hp.spp.cache.JMSListenerHelper"%>
<%!
	static String label(String label) {
		final int maxLength = 130;
		if (label == null || label.length() < maxLength) {
			return label;
		}
		return label.substring(0, maxLength) + "...";
	}
%>
<%
	String key = request.getParameter("key");
	if (key != null && key.trim().equals("")) {
		key = null;
	}
	String action = request.getParameter("action");
	if (action == null || action.trim().equals("") || key == null) {
		action = "view";
	}

	CacheMonitor cacheMonitor = CacheMonitor.getInstance();

	if ("jmsConnectionRecovery".equals(action)) {
		cacheMonitor.forceFlushEntryInAllMonitoredCaches(JMSListenerHelper.JMS_RECOVERY_CACHE_KEY);
		response.sendRedirect(request.getRequestURI());
		return;
	}
	else if ("flushEntry".equals(action)) {
		if ("ALL".equals(key)) {
			cacheMonitor.flushAll();
		}
		else {
			cacheMonitor.flushEntry(key);
		}
		response.sendRedirect(request.getRequestURI());
		return;
	}
	else if ("flushGroup".equals(action)) {
		cacheMonitor.flushGroup(key);
		response.sendRedirect(request.getRequestURI());
		return;
	}

	List values = null;
	if ("view".equals(action) && key != null) {
		values = cacheMonitor.getFromCache(key);
	}

	Set allCacheKeys = cacheMonitor.getAllKeys();
	Set allCacheGroups = cacheMonitor.getAllGroups();
%>
<html>
<head>
	<title>SPP Cache Administration</title>
	<script type="text/javascript" language="JavaScript"><!--
	var theme = '#336633';
	//--></script>
	<script type="text/javascript" language="JavaScript" src="http://www.hp.com/country/us/en/js/hpweb_utilities.js"></script>
	<link rel="shortcut icon" href="http://welcome.hp-ww.com/img/favicon.ico">
	<script type="text/javascript" language="JavaScript">
		function submitView(key) {
			document.forms.CacheAdminForm.elements.key.value = key;
			document.forms.CacheAdminForm.submit();
		}
		function submitFlush(what, key) {
			var doSubmit = false;
			if (what == 'jmsConnectionRecovery') {
				doSubmit = confirm('This will re-establish the connection to JMS server assumig that JMS is properly configured and the server is available. Continue?');
			}
			else {
				doSubmit = confirm("Are you sure to flush the selected entries?");
			}
			if (doSubmit) {
				document.forms.CacheAdminForm.method = 'post';
				if (what == 'jmsConnectionRecovery') {
					document.forms.CacheAdminForm.elements.action.value = 'jmsConnectionRecovery';
				}
				else if (what == 'group') {
					document.forms.CacheAdminForm.elements.action.value = 'flushGroup';
				}
				else {
					document.forms.CacheAdminForm.elements.action.value = 'flushEntry';
				}
				document.forms.CacheAdminForm.elements.key.value = key;
				document.forms.CacheAdminForm.submit();
			}
		}
	</script>
</head>
<body>
	<form action="cacheAdmin.jsp" method="get" name="CacheAdminForm">
		<input type="hidden" name="action" value="view" />
		<input type="hidden" name="key" value="TBD" />
	</form>
	<h1>Cache Administration Page</h1>

	<%-- LIST GROUPS / ENTRIES SECTION --%>
	<% if (values == null || values.isEmpty()) { %>

	<%-- CACHE GROUPS SECTION --%>
	<p>
		<input type="button" value="Recover JMS Connection &raquo;" onclick="submitFlush('jmsConnectionRecovery', 'dummy')" class="primButton" />
	</p>
	<h2>Cache Groups</h2>
	<table border="0" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC">
		<tr class="theme">
			<th class="small" scope="col"><span class="themebody">Cache group</span></th>
			<th class="small" scope="col"><span class="themebody">Actions</span></th>
		</tr>
		<%
			if (allCacheGroups.isEmpty()) {
		%>
		<tr bgcolor="#FFFFFF">
			<td colspan="2"><i>No cache groups used!</i></td>
		</tr>
		<%
			}
			boolean nonWhiteBgColor = false;
			for (Iterator it = allCacheGroups.iterator(); it.hasNext(); ) {
				nonWhiteBgColor = !nonWhiteBgColor;
				String groupName = (String) it.next();
		%>
		<tr bgcolor="<%=(nonWhiteBgColor ? "#E7E7E7" : "#FFFFFF")%>">
			<td><%=label(groupName)%></td>
			<td>
				<input type="button" value="Flush &raquo;" onclick="submitFlush('group', '<%=groupName%>')" class="primButton" />
			</td>
		</tr>
		<% } %>
	</table>
	<% if (!allCacheGroups.isEmpty()) { %>
	<small><i>Note: Flushing a group may not be reflected in the list of the cache entries below.
	The expired entries will remain on this list and will be automatically flushed upon the next
	read of their values from the cache.</i></small><br />
	<% } %>


	<%-- CACHE ENTRIES SECTION --%>
	<br />
	<h2>Cache Entries</h2>
	<table border="0" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC">
		<tr class="theme">
			<th class="small" scope="col"><span class="themebody">Cache entry key</span></th>
			<th class="small" scope="col"><span class="themebody">Actions</span></th>
		</tr>
		<% if (allCacheKeys.isEmpty()) { %>
		<tr bgcolor="#FFFFFF">
			<td colspan="2"><i>No cache entry found!</i></td>
		</tr>
		<%
			}
			nonWhiteBgColor = false;
			for (Iterator it = cacheMonitor.getAllKeys().iterator(); it.hasNext(); ) {
				nonWhiteBgColor = !nonWhiteBgColor;
				String entryKey = (String) it.next();
		%>
		<tr bgcolor="<%=(nonWhiteBgColor ? "#E7E7E7" : "#FFFFFF")%>">
			<td><%=label(entryKey)%></td>
			<td>
				<input type="button" value="View &raquo;" onclick="submitView('<%=entryKey%>')" class="primButton"/>
				<input type="button" value="Flush &raquo;" onclick="submitFlush('entry', '<%=entryKey%>')" class="primButton" />
			</td>
		</tr>
		<% } %>
	</table>
		<% if (!allCacheKeys.isEmpty()) { %>
	<p>
		<input type="button" value="Flush All &raquo;" onclick="submitFlush('entry', 'ALL')" class="primButton" />
	</p>
		<% } %>

	<%-- VIEW ENTRY SECTION --%>
	<% } else { %>

	<p>
		<input type="button" value="View All &raquo;" onclick="submitView('')" class="primButton" />
		<input type="button" value="Flush &raquo;" onclick="submitFlush('entry', '<%=key%>')" class="primButton" />
	</p>
	<table border="0" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC">
		<% for (Iterator it = values.iterator(); it.hasNext(); ) { %>
		<tr bgcolor="#FFFFFF">
			<td class="smallbold" scope="row" bgcolor="#E7E7E7">Entry key</td>
			<td><%=label(key)%></td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td class="smallbold" scope="row" bgcolor="#E7E7E7">Entry value</td>
			<td><%=it.next()%></td>
		</tr>
		<% } %>
	</table>
	<p>
		<input type="button" value="View All &raquo;" onclick="submitView('')" class="primButton" />
		<input type="button" value="Flush &raquo;" onclick="submitFlush('entry', '<%=key%>')" class="primButton" />
	</p>
	<% } %>
</body>
</html>
