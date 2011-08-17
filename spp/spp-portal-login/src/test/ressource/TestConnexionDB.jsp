<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="com.epicentric.jdbc.ConnectionPool"%>
<%@ page import="com.epicentric.jdbc.ConnectionPoolManager"%>
<%@ page import="com.epicentric.jdbc.ConnectionPoolConfiguration"%>
<%@ page import="java.util.Properties"%><%
	try{
		out.println("START : ");

		Properties cpProps = new Properties();
		out.println("1");
		cpProps.setProperty("db", "SPP_VIGNETTE.db");
		out.println("cpProps : "+cpProps);
     		for (Enumeration e = cpProps.propertyNames() ; e.hasMoreElements() ;) {
         		out.println(e.nextElement());
	     }
		ConnectionPoolConfiguration cpConfig = new ConnectionPoolConfiguration(cpProps);
		out.println("3");
		ConnectionPool pool = ConnectionPoolManager.createConnectionPool(cpConfig);
		out.println("4");
		Connection con = pool.getConnection();
		out.println("5");
		Statement stmt = con.createStatement();
		out.println("6");

		stmt.execute("SELECT LANDING_PAGE, LOCAL_IN_URL FROM SITE WHERE NAME = 'sppportal'");

		out.println("7");
		ResultSet rs = stmt.getResultSet();
		out.println("8");
		while (rs.next()) {
			out.println("resultat : " + rs.getString(1));
		}
		out.println("9");
		rs.close();
		stmt.close();
		con.close();
} catch (Throwable t){
	out.println("error  : " +t.toString());
}
%>