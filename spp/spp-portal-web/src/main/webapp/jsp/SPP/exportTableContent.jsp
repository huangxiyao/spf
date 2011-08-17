<%@ page import="java.util.ArrayList, 
				java.util.List, 
				java.util.Iterator, 
				java.io.BufferedWriter,
				java.io.IOException,
				java.io.OutputStream,
				java.io.OutputStreamWriter,
				java.io.PrintWriter,
				java.io.StringWriter,
				java.nio.charset.Charset,
				java.nio.charset.CharsetEncoder,
				java.sql.DriverManager,
				java.sql.ResultSet,
				java.sql.SQLException,
				javax.sql.DataSource,
				oracle.jdbc.pool.OracleDataSource,
				org.apache.log4j.Logger,
				com.hp.spp.db.DataSourceHolder,
				com.hp.spp.db.DB,
				com.hp.spp.db.RowMapper,
				com.hp.spp.db.RowToListMapper"
%><%!
	private String mEnvironment = null ;
	private DataSource mDataSourceBackup = null ;
	
	private static Logger mLog = null ;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator") ;
	private static final String FIELD_SEPARATOR = "|" ;

	private static final String DEV_URL = "jdbc:oracle:thin:SPPDEV_USER/SPPDEV_USER@(DESCRIPTION = (SDU = 32768)(enable = broken)(LOAD_BALANCE = yes)(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0248.houston.hp.com)(PORT = 1525))(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0249.houston.hp.com)(PORT = 1525))(CONNECT_DATA = (SERVICE_NAME = SPPD)))";
	private static final String FT_URL = "jdbc:oracle:thin:SPP_USER/SPP_USER@(DESCRIPTION = (SDU = 32768)(enable = broken)(LOAD_BALANCE = yes)(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0248.houston.hp.com)(PORT = 1525))(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0249.houston.hp.com)(PORT = 1525))(CONNECT_DATA = (SERVICE_NAME = SPPD)))";
	private static final String ITG_URL = "jdbc:oracle:thin:SPP_USER/SPP_USER@(DESCRIPTION = (SDU = 32768)(enable = broken)(LOAD_BALANCE = yes)(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0346.houston.hp.com)(PORT = 1525))(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0347.houston.hp.com)(PORT = 1525))(CONNECT_DATA = (SERVICE_NAME = SPPI)))";
	private static final String PRO_URL = "jdbc:oracle:thin:SPP_USER/SPP_USER@(DESCRIPTION =(SDU = 32768)(enable = broken)(LOAD_BALANCE = yes)(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0134.houston.hp.com)(PORT = 1525))(ADDRESS = (PROTOCOL = TCP)(HOST = gvu0342.houston.hp.com)(PORT = 1525))(CONNECT_DATA =(SERVICE_NAME = SPPP)))";

	public void initDataSource() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		OracleDataSource ods = new OracleDataSource();

		if (mEnvironment.equalsIgnoreCase("PRO")) {
			ods.setURL(PRO_URL) ;
		} else if (mEnvironment.equalsIgnoreCase("ITG")) {
			ods.setURL(ITG_URL) ;
		} else if (mEnvironment.equalsIgnoreCase("FT")) {
			ods.setURL(FT_URL) ;
		} else {
			ods.setURL(DEV_URL) ;
		}

		mDataSourceBackup = DataSourceHolder.getDataSource() ;
		DataSourceHolder.setTestDataSource(ods);
	}

	public void restoreDataSource() throws SQLException {
		DataSourceHolder.setTestDataSource(mDataSourceBackup);
	}

	private List searchListTable() {
		if(mLog.isDebugEnabled()) {
			mLog.debug("[ExportTableContent] searchListTable ") ;
		}
		
		StringBuffer query = new StringBuffer("");
		query.append(" select TABLE_NAME ") ;
		query.append(" from ALL_TABLES ") ;
		query.append(" where OWNER = ? ") ;
		query.append(" order by TABLE_NAME ") ;
		
		String[] queryArgs = new String[1] ;
		if (mEnvironment.equalsIgnoreCase("DEV")) {
			queryArgs[0] = "SPPDEV" ;
		} else {
			queryArgs[0] = "SPP" ;
		}

//		query.append(" select TABLE_NAME ");
//		query.append(" from USER_TABLES ");

		return executeQuery(query, queryArgs);
	}
	
	private List searchTableColumnName(String tableName) {
		if(mLog.isDebugEnabled()) {
			mLog.debug("[ExportTableContent] searchTableColumnName ") ;
		}
		
		StringBuffer query = new StringBuffer("");
		query.append(" select COLUMN_NAME ") ;
		query.append(" from ALL_TAB_COLUMNS ") ;
		query.append(" where OWNER = ? ") ;
		query.append(" and TABLE_NAME = ? ") ;
		
		String[] queryArgs = new String[2] ;
		if (mEnvironment.equalsIgnoreCase("DEV")) {
			queryArgs[0] = "SPPDEV" ;
		} else {
			queryArgs[0] = "SPP" ;
		}
		queryArgs[1] = tableName.toUpperCase() ;

//		query.append(" select COLUMN_NAME ");
//		query.append(" from USER_TAB_COLUMNS ");
//		query.append(" where TABLE_NAME = ? ");
//
//		String[] queryArgs = new String[1] ;
//		queryArgs[0] = tableName.toUpperCase() ;

		return executeQuery(query, queryArgs);
	}	
	
	private List searchTableContent(String tableName) {
		if(mLog.isDebugEnabled()) {
			mLog.debug("[ExportTableContent] searchTableContent ") ;
		}
		
		StringBuffer query = new StringBuffer("");
		query.append(" select * ");
		query.append(" from " + tableName.toUpperCase());
		
		return executeQuery(query, null);
	}
	
	private List executeQuery(StringBuffer query, String[] queryArgs) {
		final List result = new ArrayList();
		try {
			initDataSource() ;
			RowMapper mapper = new RowMapper() {
				private int mColumnCount = -1;
	
				public Object mapRow(ResultSet resultSet, int i) throws SQLException {
					List row = null;
					
					if (resultSet != null) {
						int columnCount = getColumnCount(resultSet);
						
						if(mLog.isDebugEnabled()) {
							mLog.debug("[ExportTableContent] columnCount: "+columnCount) ;
							mLog.debug("[ExportTableContent] isBeforeFirst: "+resultSet.isBeforeFirst()) ;
							mLog.debug("[ExportTableContent] isFirst: "+resultSet.isFirst()) ;
							mLog.debug("[ExportTableContent] isAfterLast: "+resultSet.isAfterLast()) ;
							mLog.debug("[ExportTableContent] isLast: "+resultSet.isLast()) ;
						}
						
						if(resultSet.isBeforeFirst() 
								|| resultSet.isAfterLast()
								|| !resultSet.isFirst()) {
							try {
								resultSet.first() ;
							} catch(Exception e) {
								// Do Nothing...
							}
						}
						
						if(resultSet.isFirst()) {
							do {
								row = new ArrayList(columnCount);
								for (int index = 1; index <= columnCount; ++index)
									row.add((resultSet.getObject(index) != null) ? resultSet.getObject(index) : "");
			
								if(mLog.isDebugEnabled()) {
									mLog.debug("[ExportTableContent] rowCount: "+row.size()) ;
								}
								
								result.add(row);
							} while (resultSet.next()) ;
						}
					}
					
					if(mLog.isDebugEnabled()) {
						mLog.debug("[ExportTableContent] resultCount: "+result.size()) ;
					}
					
					// just return null as we create this result ourselves
					return null;
				}
				
				private int getColumnCount(ResultSet rs) throws SQLException {
					if (mColumnCount == -1) {
						mColumnCount = rs.getMetaData().getColumnCount();
					}
					return mColumnCount;
				}
	
			};	
			
			if(mLog.isDebugEnabled()) {
				mLog.debug("[ExportTableContent] query: "+query.toString()) ;
			}

			// do the query
			DB.query(query.toString(), mapper, queryArgs);
		} catch(Exception e) {
			StringWriter sw = new StringWriter() ;
			e.printStackTrace(new PrintWriter(sw)) ;
			result.add(sw) ;
		}
		
		return result;
	}
%><%

mLog = Logger.getLogger(this.getClass());

String envSelect = request.getParameter("environment");
String tableName = request.getParameter("tableName");

//If submitSearch is true , we create file with table content
if (envSelect != null && !"".equals(envSelect) && tableName != null && !"".equals(tableName)) {
	
	String fileName = envSelect + "_" + tableName ;
	mEnvironment = envSelect ;

	response.setContentType("text/txt; charset=utf-8");
	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".txt\"");
	OutputStream output = null;
	CharsetEncoder encoder = null;
	OutputStreamWriter osw = null;
	BufferedWriter bw = null;
	try {
		output = response.getOutputStream();
		encoder = Charset.forName("UTF-8").newEncoder();
		osw = new OutputStreamWriter(output, encoder);
		bw = new BufferedWriter(osw);

		List resultColumn = searchTableColumnName(tableName);
		if(mLog.isDebugEnabled()) {
			mLog.debug("[ExportTableContent] resultColumn: "+resultColumn.size()) ;
		}
		
		if (resultColumn.size() != 0) {
			Iterator iterResultColumn = resultColumn.iterator();
			while (iterResultColumn.hasNext()) {
				List currentRowColumn = (List) iterResultColumn.next();
				if(mLog.isDebugEnabled()) {
					mLog.debug("[ExportTableContent] currentRowColumn: "+currentRowColumn.size()) ;
				}
				Iterator iterRowColumn = currentRowColumn.iterator();
				while (iterRowColumn.hasNext()) {
					Object currentObjectColumn = iterRowColumn.next();
					if (currentObjectColumn != null) {
						bw.write(currentObjectColumn + "");
					}
				}
				if (iterResultColumn.hasNext()) {
					bw.write(FIELD_SEPARATOR);
				}
			}
			bw.write(LINE_SEPARATOR);

			List result = searchTableContent(tableName);
			Iterator iterResult = result.iterator();
			while (iterResult.hasNext()) {
				List currentRow = (List) iterResult.next();
				Iterator iterRow = currentRow.iterator();
				while (iterRow.hasNext()) {
					Object currentObject = iterRow.next();
					if (currentObject != null) {
						bw.write(currentObject + "");
					}
					if (iterRow.hasNext()) {
						bw.write(FIELD_SEPARATOR);
					}
				}
				if (iterResult.hasNext()) {
					bw.write(LINE_SEPARATOR);
				}
			}
		}

		bw.flush();
		bw.close();
	} catch (IOException e) {
		e.printStackTrace(new PrintWriter(out)) ;
	} finally {
		try {
			if (bw != null)
				bw.close();
			else if (osw != null)
				osw.close();
			else if (out != null)
				out.close();
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(out));
		}
	}

} else {
	
	out.println("<!-- ") ;
	out.println("submitSearch: " + request.getAttribute("submitSearch"));
	out.println("error: " + request.getAttribute("error"));
	out.println("tableNameMapObj: " + request.getAttribute("tableNameMapObj"));
	out.println("environment: " + request.getParameter("environment"));
	out.println("dataBaseWasChange: " + request.getParameter("dataBaseWasChange"));
	out.println("tableName: " + request.getParameter("tableName"));
	out.println(" -->") ;

	request.setAttribute("environment", envSelect);
	boolean change = (request.getParameter("change_submit") != null);
	boolean submitted = (request.getParameter("already_sent") != null);
	if (!submitted) {
		out.println("<!-- Case #0 -->") ;
		// Do nothing... just display the page at the first time!
	} else if (!change && (envSelect == null || (!envSelect.equalsIgnoreCase("DEV") && !envSelect.equalsIgnoreCase("FT") && !envSelect.equalsIgnoreCase("ITG") && !envSelect.equalsIgnoreCase("PRO")))) {
		out.println("<!-- Case #1 -->") ;
		// No environment select.
		request.setAttribute("error", "Select an environment and a table please.");
	} else if (change) {
		out.println("<!-- Case #2 -->") ;
		// environment was changed 1->getEnv | 2->put tableNameMap
		mEnvironment = envSelect;
		request.setAttribute("tableNameMapObj", searchListTable());
	} else if (tableName == null || tableName.equals("")) {
		out.println("<!-- Case #3 -->") ;
		// No table select.
		mEnvironment = envSelect;
		request.setAttribute("tableNameMapObj", searchListTable());
		request.setAttribute("error", "Select a Table please.");
	} else {
		mEnvironment = envSelect;
		List resultTableColumn = searchTableColumnName(tableName);
		// if tableContent !=null we put table content in session
		if (resultTableColumn.size() != 0) {
			out.println("<!-- Case #4 -->") ;
			List resultTableContent = searchTableContent(tableName);
			request.setAttribute("submitSearch", "true");
			request.setAttribute("tableContent", resultTableContent);
			request.setAttribute("tableColumnName", resultTableColumn);
			request.setAttribute("fileName", mEnvironment + "_" + tableName);
		} else {
			out.println("<!-- Case #5 -->") ;
			request.setAttribute("submitSearch", "false");
		}
	}

	String pageURI = request.getContextPath() + request.getServletPath();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<html>
	<head>
		<title>Export table content</title>
		<script type="text/javascript" language="JavaScript">
			<!--
			
			var theme = '#0066FF';

			var isDHTML = 0;
			var isID = 0;
			var isAll = 0;
			var isLayers = 0;
			
			
			if (document.getElementById) 
				{isID = 1; isDHTML = 1;}
			else {
				if (document.all) 
					{isAll = 1; isDHTML = 1;}
				else {
					browserVersion = parseInt(navigator.appVersion);
					if ((navigator.appName.indexOf('Netscape') != -1) && (browserVersion == 4)) 
						{isLayers = 1; isDHTML = 1;}
				}
			}
			
			
			
			//-->
		</script>
		<script type="text/javascript" language="javascript" src="http://welcome.hp-ww.com/js/hpweb_utilities.js" name="externalCSS"></script>
		<script type="text/javascript" language="JavaScript" src="/portal/templates/YaTTRWaRYVaSafUWURaaSdRQYVdaaYfQ/QVdRYQSTZdYRXQVWURaaSdRQYVdaaYfQ/sppPrepareLink.js"></script>
		<script type="text/javascript" language="JavaScript" src="/portal/templates/YaTTRWaRYVaSafUWURaaSdRQYVdaaYfQ/QVdRYQSTZdYRXQVWURaaSdRQYVdaaYfQ/SPPCommonFunction.js"></script>
	</head>
	<body bgcolor="FFFFFF" text="#000000" link="#003366" alink="#003366" vlink="#336699" marginheight="0" marginwidth="0" leftmargin="0" topmargin="0">

		<!-- Begin Top Navigation Area -->
		<table border="0" cellpadding="0" cellspacing="0" width="740">
			<tr class="decoration">
				<td>&nbsp;</td>
			</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td align="left" valign="top" bgcolor="#666666">
					<table border="0" cellpadding="0" cellspacing="0" width="720">
						<tr class="decoration">
							<td>&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<!-- End Top Navigation Area -->

		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td align="left" valign="top" bgcolor="#E7E7E7">
					<table border="0" cellpadding="0" cellspacing="0" width="740">
						<tr>
							<td width="20" valign="top">&nbsp;</td>
							<td width="150" align="left" valign="middle" class="color003366bld">&nbsp;</td>
							<td width="570" align="right">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

		<!-- Begin Page Title Area -->
		<table border="0" cellpadding="0" cellspacing="0" width="740">
			<tr>
				<td width="170" align="center" valign="middle">
					<a href="http://www.hp.com/">
						<img src="http://welcome.hp-ww.com/img/hpweb_1-2_topnav_hp_logo.gif" width="64" height="55" alt="HP.com home" border="0">
					</a>
					<br>
				</td>
				<td width="10">
					<img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="93" alt="">
				</td>
				<td width="560" align="left" valign="top">
					<img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="20" alt="">
					<h1>Export table content</h1>
				</td>
			</tr>
		</table>
		<!-- End Page Title Area -->

		<form action="<%=pageURI%>" method="post" name="export_table_content_form">
		<!-- Begin Left Navigation and Content Area.  To increase width of content area, modify width of table below. -->
		<table border="0" cellpadding="0" cellspacing="0" width="740">
			<tr>
				<td width="170" align="left" valign="top">&nbsp;</td>
				<td valign="top" width="10">
					<img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt="Content starts here">
				</td>
		<!-- Start Content Area.  To increase width of content area, modify width of table cell below. -->
				<td width="560" align="left" valign="top">
					<table border="0" cellpadding="0" cellspacing="0" width="560">
						<tr class="colorFFFFFFbg">
							<td width="100">Environment : </td>
							<td><select class="textBox18" name="environment" onchange="this.form.tableName.selectedIndex=0;this.form.change_submit.click();">
								<option value="-1">Select a Database</option>
								<option value="DEV"<%=("DEV".equals(request.getAttribute("environment")))?" selected":""%>>Development Database</option>
								<option value="FT"<%=("FT".equals(request.getAttribute("environment")))?" selected":""%>>Functional Test Database</option>
								<option value="ITG"<%=("ITG".equals(request.getAttribute("environment")))?" selected":""%>>Integration Database</option>
								<option value="PRO"<%=("PRO".equals(request.getAttribute("environment")))?" selected":""%>>Production Database</option>
								</select>
							</td>
						</tr>
						<tr class="decoration">
							<td valign="top" colspan="2">
								<img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="5" alt="Content starts here">
							</td>
						</tr>
						<tr class="colorFFFFFFbg">
							<td width="100">Table Name : </td>
							<td>
								<select class="textBox18" name="tableName">
									<option value="">Select a Table</option>
									<%
									Object tableNameMapObj = request.getAttribute("tableNameMapObj");
									List tableNameMap = null;
									if (tableNameMapObj!=null){
										tableNameMap = (List) tableNameMapObj;
										Iterator iter = tableNameMap.iterator() ;
										while(iter.hasNext()) { 
											List row = (List) iter.next() ;
											String value = (String) row.get(0) ;
											%><option value="<%=value%>"><%=value%></option><%
										}
									}%>
								</select>
							</td>
						</tr>
						<tr class="decoration">
							<td valign="top" colspan="2">
								<img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="5" alt="Content starts here">
							</td>
						</tr>
						<tr class="colorFFFFFFbg">
							<td width="100">&nbsp;</td>
							<td><input type="submit" name="submit" value="Search Table Content"/></td>
						</tr>
						<% if (request.getAttribute("submitSearch") != null && ((String)request.getAttribute("submitSearch")).equalsIgnoreCase("false")) {
						%><tr>
							<td>
							<script language="javascript">
								<!--
								alert("Table is empty");
								//-->
							</script>
							</td>
						</tr>
						<% } 
						if (request.getAttribute("error") != null && !((String)request.getAttribute("error")).equalsIgnoreCase("")) {
						%><tr>
							<td>
							<script language="javascript">
								<!--
								alert("<%=request.getAttribute("error")%>");
								//-->
							</script>
							</td>
						</tr><%}%>
						<input type="hidden" name="already_sent" value="true" />
						<input type="submit" name="change_submit" value="change_submit" key="change_submit" defaultValue="change_submit" style="visibility:hidden" />
					</table>
				</td>
		<!-- End Content Area -->
			</tr>
		</table>
		<!-- End Left Navigation and Content Area -->
		</form>

	</body>
</html>
<% } %><% if(mDataSourceBackup != null) { restoreDataSource(); } %>