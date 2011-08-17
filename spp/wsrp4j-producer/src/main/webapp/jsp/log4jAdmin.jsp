<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.log4j.LogManager"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.Set"%>
<%@ page import="org.apache.log4j.Level"%>
<% long beginPageLoadTime = System.currentTimeMillis();%>
<%
	String pageURI = request.getContextPath() + request.getServletPath();

	String logNameFilter     = request.getParameter("logNameFilter") != null ? request.getParameter("logNameFilter") : "";
	String logNameFilterType = request.getParameter("logNameFilterType") != null ? request.getParameter("logNameFilterType") : "";

	String containsFilter = "Contains";
	String beginsWithFilter = "Begins With";

	Enumeration loggers = LogManager.getCurrentLoggers();
	HashMap loggersMap = new HashMap(128);

	Logger rootLogger = LogManager.getRootLogger();

	if(!loggersMap.containsKey(rootLogger.getName()))
	{
	  loggersMap.put(rootLogger.getName(), rootLogger);
	}

	while(loggers.hasMoreElements())
	{
	  Logger logger = (Logger)loggers.nextElement();

	  if(logNameFilter == null || logNameFilter.trim().length() == 0)
	  {
		  loggersMap.put(logger.getName(), logger);
	  }
	  else if(containsFilter.equals(logNameFilterType))
	  {
		if(logger.getName().toUpperCase().indexOf(logNameFilter.toUpperCase()) >= 0)
		{
		  loggersMap.put(logger.getName(), logger);
		}
	  }
	  else
	  {
		// Either was no filter in IF, contains filter in ELSE IF, or begins with in ELSE
		if(logger.getName().startsWith(logNameFilter))
		{
		  loggersMap.put(logger.getName(), logger);
		}
	  }
	}

	Set loggerKeys = loggersMap.keySet();
	String[] keys = new String[loggerKeys.size()];
	keys = (String[])loggerKeys.toArray(keys);
	Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);


	if ("changeLogLevel".equals(request.getParameter("operation")) )
	{
		String[] targetLoggers = request.getParameterValues("logger");
		String targetLogLevel = request.getParameter("newLogLevel");
		if (targetLoggers != null) {
			for (int i = 0, len = targetLoggers.length; i < len; ++i) {
//				System.out.println("Changing log level of '" + targetLoggers[i] + "' to " + targetLogLevel);
				Logger selectedLogger = (Logger)loggersMap.get(targetLoggers[i]);
				selectedLogger.setLevel(Level.toLevel(targetLogLevel));
			}
		}
		response.sendRedirect(pageURI + "?logNameFilter=" + logNameFilter + "&logNameFilterType=" + logNameFilterType);
	}
	else
	{
		String[] logLevels = { "debug", "info", "warn", "error", "fatal", "off" };
%>
<html>
<head>
	<title>Log4J Administration</title>
	<script type="text/javascript" language="JavaScript"><!--
	var theme = '#336633';
	//--></script>
	<script type="text/javascript" language="JavaScript" src="http://www.hp.com/country/us/en/js/hpweb_utilities.js"></script>
	<link rel="shortcut icon" href="http://welcome.hp-ww.com/img/favicon.ico">
</head>

<body onLoad="javascript:document.logFilterForm.logNameFilter.focus();">
<h1>Log4J Administration</h1>

<form name="logFilterForm" action="<%=pageURI%>">Filter Loggers:&nbsp;&nbsp;
	<input name="logNameFilter" type="text" size="50" value="<%=logNameFilter%>" class="filterText"/>
	<input name="logNameFilterType" type="submit" value="<%=beginsWithFilter%>" class="primButton"/>&nbsp;
	<input name="logNameFilterType" type="submit" value="<%=containsFilter%>" class="primButton"/>&nbsp;
	<input name="logNameClear" type="button" value="Clear" class="primButton" onmousedown='javascript:document.logFilterForm.logNameFilter.value="";'/>
	<input name="logNameReset" type="reset" value="Reset" class="primButton"/>
</form>

<form action="<%=pageURI%>" method="POST" name="changeLogLevelForm">
	<input type="hidden" name="logNameFilter" value="<%=logNameFilter%>"/>
	<input type="hidden" name="logNameFilterType" value="<%=logNameFilterType%>"/>
	<input type="hidden" name="operation" value="changeLogLevel"/>
	<input type="hidden" name="newLogLevel"/>

	<table border="0" cellpadding="4" cellspacing="1" bgcolor="#CCCCCC">
		<tr class="theme">
			<th>&nbsp;</th>
			<th width="25%" class="small" scope="col"><span class="themebody">Logger</span></th>
			<th width="25%" class="small" scope="col"><span class="themebody">Parent Logger</span></th>
			<th width="15%" class="small" scope="col"><span class="themebody">Effective Level</span></th>
			<th width="35%" class="small" scope="col"><span class="themebody">Change Log Level To</span></th>
		</tr>
		<tr>
			<td align="center">
				<input type="checkbox" onclick="var els=document.changeLogLevelForm.elements; for (i=0;i<els.length;i++) if (els[i].name=='logger') els[i].checked=this.checked;"/>
			</td>
			<td class="small"><b>Selected Loggers</b></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>
				<%
					for (int cnt = 0; cnt < logLevels.length; cnt++) {
				%><a href="javascript:var f=document.changeLogLevelForm; f.newLogLevel.value='<%=logLevels[cnt]%>'; f.submit();">[<%=logLevels[cnt].toUpperCase()%>]</a>&nbsp;<%
					}
				%>
			</td>
		</tr>
		<%
			boolean nonWhiteBgColor = false;
			for (int i = 0; i < keys.length; ++i) {
				nonWhiteBgColor = !nonWhiteBgColor;
				Logger logger = (Logger) loggersMap.get(keys[i]);

				String loggerName = null;
				String loggerEffectiveLevel = null;
				String loggerParent = null;

				if (logger != null) {
					loggerName = logger.getName();
					loggerEffectiveLevel = String.valueOf(logger.getEffectiveLevel());
					loggerParent = (logger.getParent() == null ? null : logger.getParent().getName());
				}
		%>
		<tr bgcolor="<%=(nonWhiteBgColor ? "#E7E7E7" : "#FFFFFF")%>">
			<td>
				<%
					if (!"root".equalsIgnoreCase(loggerName)) {
				%><input type="checkbox" name="logger" value="<%=loggerName%>"/>
				<%
				}
				else { %>
				&nbsp;
				<%
					}
				%>
			</td>
			<td><%=loggerName%></td>
			<td><%=loggerParent%></td>
			<td><%=loggerEffectiveLevel%></td>
			<td>
				<%
					for (int cnt = 0; cnt < logLevels.length; cnt++) {
						String url = pageURI + "?operation=changeLogLevel&logger=" + loggerName + "&newLogLevel=" + logLevels[cnt] + "&logNameFilter=" + logNameFilter + "&logNameFilterType=" + logNameFilterType;

						if (logger.getLevel() == Level.toLevel(logLevels[cnt]) || logger.getEffectiveLevel() == Level.toLevel(logLevels[cnt])) {
				%>[<%=logLevels[cnt].toUpperCase()%>]&nbsp;<%
						}
						else {
				%><a href='<%=url%>'>[<%=logLevels[cnt].toUpperCase()%>]</a>&nbsp;<%
						}
					}
				%>
			</td>
		</tr>
		<%
			}
		%>
	</table>
<!--
	<h2>
	  Mike Amend<br />
	  BEA Systems<br />
	  Email: <a href="mailto:mamend@bea.com">mamend@bea.com</a><br />
	  Revision: 1.0<br />
	  Page Load Time (Millis): <%=(System.currentTimeMillis() - beginPageLoadTime)%>
	</h2>
-->
</form>
</body>
</html>
<%
	}
%>