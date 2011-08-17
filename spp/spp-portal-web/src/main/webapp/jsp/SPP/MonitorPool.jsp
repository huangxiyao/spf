<%--
JSP to test the connection pool performance on individual Weblogic Managed Servers.
Implementation - Get the initialcontext passing the corresponding Weblogic Managed Server name
Get the datasource object from JNDI, make a DB connection and execute the dummy SQL Statement (select 1 from dual).
Record the time taken for each of the activity
--%>

<%@ page errorPage="error.jsp"
			import = "java.sql.Connection,
			java.sql.ResultSet,java.sql.SQLException,
			java.lang.StringBuffer,
			java.sql.Statement,java.util.Hashtable,
			javax.naming.InitialContext,
			javax.naming.NamingException,
			java.util.Iterator,
			javax.sql.DataSource,
				 java.text.*,
                 java.util.Date,
                 com.epicentric.test.*,
                 com.epicentric.common.*,
                 com.epicentric.jdbc.*,
                 com.epicentric.jdbc.internal.*,
                 com.vignette.portal.connectionpool.internal.ConnectionHandle"
			contentType="text/html; charset=UTF-8"
%>

<%@page isThreadSafe="true" %>

<%!

	// DECLARE VARIABLES
	
	//private String wl_server_name = null;
	private final String DATASOURCE = "jdbc/Vignette_PORTALDS";
	private final String Query = "select 1 from dual";
	private final int SLEEP = 1000; //Sleep time for the current running thread
	private StringBuffer htmlString = null;
	private final String HTML_BREAK = "<BR></BR>";
	private InitialContext ctx = null;
	private long start;
	private long end;
	

	//DECLARE METHODS


	/*
	 * Create an InitialContext in the Weblogic server
	 */
	public void createContext(javax.servlet.jsp.JspWriter out) {
		
		try {
		
			//out.print("\n CREATE INITIALCONTEXT ");
			
			start = System.currentTimeMillis();
			
			try {
				
				ctx = new InitialContext();
				
						/*
							Iterator itr  = ctx.getEnvironment().keySet().iterator();
			
							while(itr.hasNext()) {
						
							out.println(itr.next());
						
							}
						*/
						
					
			} catch (NamingException e) {
				
				out.print("NamingException: ERROR creating an INITIALCONTEXT "+HTML_BREAK);
				e.printStackTrace(System.out);
				return;
			} 
			
			
			end = System.currentTimeMillis();
			
			
			//Print the execution time for INITIALCONTEXT
				htmlString.append("<TD>");
				htmlString.append(end-start);
				htmlString.append("</TD>");
			
			
			//out.print("\t ( CALL_CONTEXT =>  milliseconds = "+(end-start)+HTML_BREAK);
			//out.println(HTML_BREAK+"\n INITIALCONTEXT CREATED"+HTML_BREAK);
	
		} catch (java.io.IOException e) {
		
			e.printStackTrace(System.out);
		
		}	
		
	}
	
	
	/*
	 * Entry point to perform all the actions
	 */
	
	public void doStart(javax.servlet.jsp.JspWriter out) {

		DataSource ds  = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		 
		try {

			createContext(out); //Create an initial context
			
			if(ctx != null) {
				
				//out.println("Got an initialcontext");
				
				
				try {
					
					//out.print(HTML_BREAK+"\n CREATE DATASOURCE ");
					
					start = System.currentTimeMillis();
					
					//Get the Datasource
					ds = (DataSource) ctx.lookup(DATASOURCE);
					
					end = System.currentTimeMillis();
					
					
					//PRINT THE EXECUTION TIME FOR DATASOURCE
					htmlString.append("<TD>");
						//htmlString.append("end = "+end);
						//htmlString.append("start = "+start);
					htmlString.append((end-start));
					htmlString.append("</TD>");
					
					//out.print("\t ( CALL_DATASOURCE =>  milliseconds = "+(end-start));
					//out.println(HTML_BREAK+"\n DATASOURCE CREATED ");
					
				} catch (NamingException e) {
					
						out.println("NamingException: ERROR retrieving the DATASOURCE = "+DATASOURCE+HTML_BREAK);
						e.printStackTrace(System.out);	
				}	
					
					try {
						
						//out.print(HTML_BREAK+"\n GET DB CONNECTION ");
						start = System.currentTimeMillis();
					
						//Get the Connection from Datasource
						con = ds.getConnection();
					
						end = System.currentTimeMillis();
					
						//out.print("\t ( CALL_DB =>  milliseconds = "+(end-start));
						
						
						//PRINT THE EXECUTION TIME FOR CONNECTION
						htmlString.append("<TD>");
							//htmlString.append("end = "+end);
							//htmlString.append("start = "+start);
						htmlString.append(end-start);
						htmlString.append("</TD>");
				
					} catch (SQLException e) {
					
						out.println("SQLException: ERROR getting a connection from the DATASOURCE = "+DATASOURCE+HTML_BREAK);
						e.printStackTrace(System.out);	
						
					}	
						
						//out.println(HTML_BREAK+"\n DB CONNECTION CREATED ");
						
						try {
							//out.print("\n CREATE STATEMENT ");
							start = System.currentTimeMillis();
						
							//Create a Statement object
							stmt = con.createStatement();
							
							end = System.currentTimeMillis();
							
							//PRINT THE EXECUTION TIME FOR STATEMENT
							htmlString.append("<TD>");
								//htmlString.append("end = "+end);
								//htmlString.append("start = "+start);
							htmlString.append(end-start);
							htmlString.append("</TD>");
							
							
							//out.print("\t ( CALL_STMT =>  milliseconds = "+(end-start));
							//out.println(HTML_BREAK+"\n STATEMENT CREATED ");
							
						} catch (SQLException e) {
						
							out.println("SQLException: ERROR getting a statement object from the CONNECTION "+HTML_BREAK);
							e.printStackTrace(System.out);	
	
						
						}	
							
							
							try {
								
								if(stmt != null) {
									
									//out.print(HTML_BREAK+"\n EXECUTE QUERY ");
									start = System.currentTimeMillis();
									
									//Execute the query and store it in a resultset object
									rs = stmt.executeQuery(Query);
									
									end = System.currentTimeMillis();
									//out.print("\t\t ( CALL_QUERY =>  milliseconds = "+(end-start));
								
									//PRINT EXECUTION TIME FOR QUERY
									htmlString.append("<TD>");
										//htmlString.append("end = "+end);
										//htmlString.append("start = "+start);
									htmlString.append((end-start));
									htmlString.append("</TD>");
								
								
											//if(rs.next() && rs.isFirst())
											
											//	out.println(HTML_BREAK+"\n QUERY EXECUTED SUCCESSFULLY ");
											//else
											//	out.println(HTML_BREAK+"\n QUERY FAILED TO EXECUTE. PLEASE TRY AGAIN ");
											
								}
							  
							  } catch (SQLException e) {
							  
					  				out.println("SQLException: ERROR executing the Query "+HTML_BREAK);
									e.printStackTrace(System.out);	
			
							  }	
					
		
			} //if(ctx != null)  
		
		} catch (java.io.IOException e) {
	
			e.printStackTrace(System.out);
	
		} finally {
			
			
			if(stmt!= null) {
			
				try {
					//out.println(HTML_BREAK+"\n\t CLOSING STATEMENT....");
					stmt.close();
					//out.print("\t CLOSED\n");
											
				} catch (SQLException e) {
									
					 try {	
					
						e.printStackTrace(System.out);
						out.println("ERROR closing Statement "+HTML_BREAK);
					
					 } catch (java.io.IOException e2) {
	
						e2.printStackTrace(System.out);
					
					 }
											
				}  	
			}
									
			if(con != null) {
										
					try {
						//out.println(HTML_BREAK+"\n\t CLOSING CONNECTION....");
						con.close();
						//out.print("\t CLOSED\n");
					} catch (SQLException e) {
						
						try {					
							e.printStackTrace(System.out);
							out.println("ERROR closing DB Connection "+HTML_BREAK);
						} catch (java.io.IOException e2) {
	
							e2.printStackTrace(System.out);
		
						}
					} 
										
			}			
			
			if(ctx != null) {
				
				try {
					
					//out.println(HTML_BREAK+"\n\t CLOSING INITIALCONTEXT....");
					ctx.close();
					//out.print("\t CLOSED\n");
				} catch (NamingException e) {
				
					try {
					
						e.printStackTrace(System.out);
						out.println("ERROR closing the Initial Context "+HTML_BREAK);
					
					} catch (java.io.IOException e2) {
	
						e2.printStackTrace(System.out);
					}				
				
				} 
				
			}
		}
			
	  }
		
		
	// Construct the Table Header with Title
		
	public void constructTableHeader() {
		
		htmlString.append("<TABLE BORDER=1>");	
		
		htmlString.append("<TR>");	
			
			htmlString.append("<TH><H3> ATTEMPT </H3></TH>");		
			htmlString.append("<TH><H3> INITIALCONTEXT(ms) </H3></TH>");		
			htmlString.append("<TH><H3> DATASOURCE(ms) </H3></TH>");		
			htmlString.append("<TH><H3> CONNECTION(ms) </H3></TH>");		
			htmlString.append("<TH><H3> STATEMENT(ms) </H3></TH>");		
			htmlString.append("<TH><H3> QUERY(ms) </H3></TH>");		
			htmlString.append("<TH><H3> TOTAL PROCESSING TIME(ms) </H3></TH>");		

		htmlString.append("</TR>");	

	}
		
%>	

	<html>
	<head>
	  <title>
	    Connection Pool Diagnostic
	  </title>
	  <meta http-equiv="Content-type" content="text/html; charset=ISO-8859-1">
	  <meta http-equiv="Content-Style-Type" content="text/css">
	  <meta http-equiv="Pragma" content="no-cache">
	  <meta http-equiv="Cache-Control" content="no-cache">
	  <meta http-equiv="Expires" content="-1">
	</head>

	<body>

<%
		
			int loop = 1; //Default to 1

			if(request.getParameter("loop") != null ) loop = Integer.parseInt(request.getParameter("loop"));
			
			long start;
			long end;
		
			htmlString = new StringBuffer();
			
			//Print the Heading
			htmlString.append(HTML_BREAK+"<B>"+DATASOURCE+" connection pool status</B> "+HTML_BREAK+"<i>Note: Add loop=(n) parameter in the URL to check the SPP connection pool status n times</i>");
			htmlString.append(HTML_BREAK);
			
			constructTableHeader();
			
			for(int i = 0; i < loop; i++) {			
				
			  	htmlString.append("<TR>");	//Start a new row
	
				//Print ATTEMPT Column
				htmlString.append("<TD>");	
				htmlString.append((i+1));
				htmlString.append("</TD>");
					
				start = System.currentTimeMillis();
				
				doStart(out); 
				
				end = System.currentTimeMillis();
				
				//out.println("\n TOTAL_CALL_TIME =>  milliseconds = "+(end-start));
				
				//PRINT TOTAL PROCESSING TIME
				htmlString.append("<TD>");
				htmlString.append((end-start));
				htmlString.append("</TD>");
				
			  	htmlString.append("</TR>");	//End of the row
	
				//Sleep for some milliseconds before making the next attempt
				try {
					Thread.sleep(SLEEP);	
				} catch (InterruptedException e) {
					e.printStackTrace(System.out);
				}
			}	
			
			htmlString.append("</TABLE>");	 //Close the table
			
			//htmlString.append(HTML_BREAK);	
			
			out.println(htmlString.toString()); //Print the HTML string
			
			htmlString = null;
%>			
	
	
	
	
	
	
<%

		out.println("<BR></BR><B>Vignette Connection pool status</B><BR></BR>");

        String DEFAULT_POOL_ARG = "default";

        String connectionPoolName =
            WebsiteUtils.getReferrerData(request, "poolname");
        boolean useDefaultPool = false;
        if (connectionPoolName.equalsIgnoreCase(DEFAULT_POOL_ARG) ||
            connectionPoolName.equals(""))
        {
            connectionPoolName = DEFAULT_POOL_ARG;
            useDefaultPool = true;
        }

        /*
        *  Get connection pool.
        */
        ConnectionPool connectionPool = null;
        if (useDefaultPool)
        {
            connectionPool = ConnectionPoolManager.getDefaultPool();
        }
	  else
        {
	      connectionPool = ConnectionPoolManager.getPool(connectionPoolName);
        }
        if (connectionPool == null)
        {
%>
            Could not get pool
<%
        }
        else
        {

            /*
            * Get the pool length
            */
            int poolLength = connectionPool.getPoolSize();
            int [] connectionState = new int[poolLength];
            long [] connectionLastUsed = new long[poolLength];
            long [] connectionLease = new long[poolLength];

            /*
            * Get connection pool statistics
            */
            for (int i = 0; i < poolLength; i++)
            {
                connectionState[i] = connectionPool.getConnectionState(i);
                connectionLastUsed[i] = connectionPool.getConnectionLastUsed(i);
                connectionLease[i] = connectionPool.getConnectionLease(i);
            }
%>
            Connection status for <%=connectionPoolName%> pool
            <table border width=400>
                <tr>
                    <td>Connection</td>
                    <td>State</td>
                    <td>Last Used</td>
                    <td>Lease</td>
                </tr>
<%

                DateFormat timestampFormat =
                    new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS");
                for (int i = 0; i < poolLength; i++)
                {
                    int currentLength = 0;
                    /*
                    ** Print connection number.
                    */
%>
                    <tr>
                        <td>
                            <%=Integer.toString(i+1)%>
                        </td>
<%
                    int state = connectionState[i];
                    String stateDisplay = null;
                    if (state == com.epicentric.jdbc.ConnectionPool.STATE_FREE)
                    {
                        stateDisplay = "Free";
                    }
                    if (state == com.epicentric.jdbc.ConnectionPool.STATE_BUSY)
                    {
                        stateDisplay = "Busy";
                    }
                    if (state == com.epicentric.jdbc.ConnectionPool.STATE_ASLEEP)
                    {
                        stateDisplay = "Asleep";
                    }
%>
                    <td>
                        <%=stateDisplay%>
                    </td>
                    <td>
<%
                    /*
                    ** Print connection "last used"
                    */
                    long lastUsed = connectionLastUsed[i];
                    if (lastUsed == 0)
                    {
%>
                        Not Used
<%                  }
                    else
                    {
%>
                        <%=timestampFormat.format(new Date(lastUsed))%>
<%
                    }
%>
                    </td>
<%
                    /*
                    ** Print connection "lease"
                    */
                    long lease = connectionLease[i];
%>
                    <td>
                        <%=Long.toString(lease)%>
                    </td>
<%
                }
%>
                </table>
<%
            }
%>
	
	
		
	</body>
	</html>
	