

<%


javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO : get the JNDI name from the hibernate configuration
javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
//get the connection
java.sql.Connection conn = ds.getConnection();

%>
Driver version is : <%=conn.getMetaData().getDriverVersion()%>