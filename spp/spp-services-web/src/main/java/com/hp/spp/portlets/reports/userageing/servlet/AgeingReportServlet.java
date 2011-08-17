package com.hp.spp.portlets.reports.userageing.servlet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/*
 * Servlet class that reads the report data from Vignette USERS table and generates
 * a zipped xsl file.
 * @author girishsk
 * @version SPP 2.0 intial
*/



public class AgeingReportServlet extends HttpServlet { 
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//The JNDI name of Vignette database
	private static final String DEF_VIGNETTE_JNDI_NAME="jdbc/wsrp4j";
	//Name of the report file 
	private static String DEFAULT_REPORT_NAME = "XLSReport.csv";
	//Default name of zip file
	private static String DEFAULT_ZIP_FILE_NAME= "report.zip";
	//The CSV seperator value
	private static String  CSV_SEPARATOR = ",";
	//Deafult date format for last_login_date.
    private static String DEFAULT_DATE_FORMAT= "MM/dd/yyyy";
    private static Logger mLog = Logger.getLogger(AgeingReportServlet.class);
	private DataSource dataSource = null;
	
	

     public void doGet (HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException { 
          	String lastLoginDate = req.getParameter("lastLoginDate");
			mLog.debug("Obtained last login date : " + lastLoginDate);
	
			List dataList = findUsersByLoginDate(lastLoginDate);
		
		  if(lastLoginDate != null){
			response.setContentType("application/zip" ) ;
			response.setHeader("Content-Disposition","attachment; filename=" + DEFAULT_ZIP_FILE_NAME);
		      generateXSLFile(lastLoginDate, response, dataList);
		  }
     }
	 
     protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
 		// TODO Auto-generated method stub
 		doGet(arg0, arg1);
 	}
     
/**
 * Generates the zipped CSV file
 * @param lastLoginDate date since user last logged in
 * @param res   HttpServletResponse
 * @param dataList List of user rows (Vignette users table)
 */
	
	private void generateXSLFile(String lastLoginDate, HttpServletResponse res, List dataList){
		List labelList = getLabelList();
		mLog.debug("Data List size : " + dataList.size());
	
		StringBuffer buf = new StringBuffer();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DEFAULT_DATE_FORMAT);
		
		if ((labelList!=null) && (dataList!=null) )
	    {
			
			buf.append("");
			
			if (dataList != null && dataList.size()>0)
			{
				for (int i=0; i<labelList.size(); i++)
				{
					buf.append((String) labelList.get(i)).append(CSV_SEPARATOR);
				}
				// remove the last separator
				int length = buf.length();
				if (length > 0)
				{
					buf.deleteCharAt(length - CSV_SEPARATOR.length());
				}
				// add the new line
				buf.append("\n");
				
				// content
				for (int j=0; j<dataList.size(); j++)
				{
					java.util.ArrayList row = (java.util.ArrayList) dataList.get(j);
					for (int i=0; i<row.size(); i++)
					{
						Object value = row.get(i);
						String formattedValue = new String();
						if(value==null)
							value = new String(" ");
						if (value instanceof java.sql.Date)
						{
							formattedValue = sdf.format((java.util.Date) value);
						}
						else
						{
							formattedValue = value.toString();
						}
						buf.append(formattedValue).append(CSV_SEPARATOR);
					}
					// remove the last separator
					if (buf.length()>0)
					{
						buf.deleteCharAt(buf.length() - CSV_SEPARATOR.length());
					}
					// add the new line
					buf.append("\n");
				}
				// remove the last \n
				if (buf.length()>0)
				{
					buf.deleteCharAt(buf.length()-1);
				}
			}
			else
			{
				buf.append("No results found !");
			}

		   //Generate the zip data file.			
		   ByteArrayOutputStream bout=new ByteArrayOutputStream(); 
		   ZipOutputStream zout=new ZipOutputStream(bout); 
			
			try{
				String csv = buf.toString();		
				byte[] b = csv.getBytes();
				zout.putNextEntry(new ZipEntry(DEFAULT_REPORT_NAME)); 
				zout.write(b);
				zout.closeEntry(); 
				zout.finish();
				bout.flush();
						
				byte[] zipBytes=bout.toByteArray() ;
		
				ServletOutputStream sos= res.getOutputStream();
				sos.write(zipBytes); 
				sos.flush(); 
							 
          	}catch(IOException ex ) 
			{ 
				mLog.debug("IOException occured during zip data transfer" +ex.getMessage());
          		try
				{
					zout.flush();
					zout.close();	
				}
				catch (Exception ioex)
				{
				}
				ex.printStackTrace(); 
			}
			
		}

	}//End of function
	


/*
 *  Get users by last login date querying database through
 *   Vignette.
 *	@param date date since user last logged.
 *  @return list or user row data (list of array lists(rows))
 */
	public List findUsersByLoginDate(String date){
		List sqlResult = new ArrayList();
		mLog.debug("findUSersByLoginDate called =======");
		// Query to find the Users
		String query = "SELECT FIRST_NAME, LAST_NAME, USER_NAME, EMAIL_ADDRESS, " +
				"LAST_LOGIN_DATE FROM USERS WHERE LAST_LOGIN_DATE <= SYSDATE " ;
		
		//If date is not null or empty
		if(! (date == null || "".equals(date)) ){
			query = query + "-" + " " + date;
		}

		String queryOrder = " ORDER BY LAST_LOGIN_DATE";
		query = query + queryOrder;
	
		sqlResult = fetchDataInternal(query);
		mLog.debug("Returnin SQL result set size =" + sqlResult.size());

		return sqlResult;
	}


/*
 *  Get labels to apply in the CSV file
 *  @return list users labels 
 */
	public List getLabelList(){
		ArrayList list = new ArrayList();
		list.add("First Name");
		list.add("Last Name");
		list.add("Login ID");
		list.add("email address");	
		list.add("Last Login Date");
		return list;
	}
/*
 * Fetch data from Vignette database, this is an internal API
 * to fetch user data from database, the JNDI name 
 * is temporarily hardcoded
 * 
 */	 
	private List fetchDataInternal(String statement){
		//Step (1) Get the Vignette datasource
		ArrayList result = new ArrayList();
		
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource) ic.lookup(DEF_VIGNETTE_JNDI_NAME);
		} catch (NamingException e) {
			mLog.error("Error retrieving default datasource jdbc/Vignette_PORTALDS");
			//return since all further calls will fail
			return result;
		}
		//Step (2) Execute SQL query against Vignette
			
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList row = null;

		try {
			con = this.dataSource.getConnection();
			stmt = con.createStatement();
			mLog.debug("Execute query : "+statement);
			stmt.execute(statement);

			rs = stmt.getResultSet();
			int nbAtt = rs.getMetaData().getColumnCount();
			mLog.debug("Coulum count : "+nbAtt);
	
			while (rs.next()) {
				row = new ArrayList(nbAtt);
				for (int i = 0; i < nbAtt; i++)
					row.add((rs.getObject(i + 1) != null) ? rs.getObject(i + 1) : "");

				result.add(row);
			}

		} catch (Exception e) {
			mLog.error(" Error in executeSelectQuery ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				 mLog.error(e, e);
			}
		}
	
		return result;
	}



}
