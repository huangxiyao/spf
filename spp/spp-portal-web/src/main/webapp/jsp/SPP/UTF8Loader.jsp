<%@ page import="java.sql.*"%><%
	try{
		Class.forName("oracle.jdbc.OracleDriver");
		
		// connection source
		String urlSource = "jdbc:oracle:thin:@bbnapppro02.bbn.hp.com:1521:sppdev";
		Connection connectionSource = DriverManager.getConnection(urlSource,
                "sppdev", "sppdev");
		String querySource = "SELECT LOCALE, LABEL, MESSAGE FROM SPP_LOGIN_LABEL";
		
		// connection cible
		String urlCible = "jdbc:oracle:thin:@cceor06.cce.cpqcorp.net:1563:KANAD";
		Connection connectionCible = DriverManager.getConnection(urlCible,
                "SPP_USER", "SPP_USER");
		String queryCible = "INSERT INTO SPP_LOGIN_LABEL (LOCALE, LABEL, MESSAGE) VALUES (?, ?, ?)";
		
		// retrieve data from source
		Statement stmtSource = connectionSource.createStatement();
		ResultSet rs = stmtSource.executeQuery(querySource);

		// put data in cible
		PreparedStatement pstmtCible = connectionCible.prepareStatement(queryCible);
		while(rs.next()){
			pstmtCible.setString(1, rs.getString(1));
			pstmtCible.setString(2, rs.getString(2));
			pstmtCible.setString(3, rs.getString(3));
			pstmtCible.executeUpdate();
		}
		connectionCible.commit();
		
		// close
		rs.close();
		connectionSource.close();
		pstmtCible.close();
		connectionCible.close();
	
} catch (Throwable t){
	out.println("error  : " +t.toString());
}
%>
