package com.hp.spp.portal;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.hp.spp.portal.util.NavigationItemDetails;
import com.hp.spp.portal.util.UserGroupAssignmentReportConstants;
import com.hp.spp.portal.util.UserGroupAssignmentReportUtil;

/*
 * Servlet class that generates the downloadable Excel report that reports on a "per site" basis (Secured or Unsecured or Both Sites). 
 * This report is in the zipped upon download and manual unzipping the file is required from the user. 
 * The following information would be included in a report:
 *  - Vignette Navigation Item Name :
 *    This is the name of the navigation item in the Vignette console
 *  - Menuitem ID :
 *    The unique navigation item identifier
 *  - Navigation Item Type
 *    To denote whether the navigation item is a page or portlet
 *  - User Group(s)
 *    Assigned user groups (if there are more than one user group, the names would be separated by semicolon)
 * a zipped CSV file.
 * @author Srikanth Adepu
 * @version SPP 4.7.2
*/
public class UserGroupAssignmentReportServlet extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
	private static final Logger mLog = Logger.getLogger(UserGroupAssignmentReportServlet.class);  	

    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		
		if(!validateSecretKey(req)){
			throw new IllegalStateException("Invalid invocation, secret key mismatch");
		}
				
		List<List> dataList = new ArrayList<List>();
		SimpleDateFormat sdf = new SimpleDateFormat(UserGroupAssignmentReportConstants.DEFAULT_DATE_FORMAT);
		String defaultReportName = "XLSReport_"+sdf.format(new Date())+".csv";		
				       	
		String selectedSite = req.getParameter("site");
		String siteDNSName = req.getParameter("bothSites");
			
		if (mLog.isDebugEnabled())  {
			mLog.debug("UGAR-defaultReportName :: " + defaultReportName);
			mLog.debug("UGAR-Selected Site :: " + selectedSite);
			mLog.debug("UGAR-Site DNS Name :: " + siteDNSName);
		}
		//set response object
		response.setContentType("application/zip" ) ;
		response.setHeader("Content-Disposition","attachment; filename=" + UserGroupAssignmentReportConstants.DEFAULT_ZIP_FILE_NAME);

		if( (selectedSite != null) && (!selectedSite.equals("-1")) ){
			
			String menuitemsQuery = UserGroupAssignmentReportConstants.MENUITEMS_QUERY;
			String portletsQuery = UserGroupAssignmentReportConstants.PORTLETS_QUERY;
				
			if (mLog.isDebugEnabled())  {
				mLog.debug("UGAR-menuItemsQuery :: " + menuitemsQuery);
				mLog.debug("UGAR-portletsQuery :: " + portletsQuery);
				mLog.debug("UGAR-Calling generateResult() to get the site menuitem info");
			}			
			//If user selects the 'Both' from the site drop down.
			if((selectedSite.equals("Both")) && !("".equals(siteDNSName)) && (siteDNSName != null)) {							
				dataList.add( UserGroupAssignmentReportUtil.generateResult(menuitemsQuery, "menulist", "public"+siteDNSName) );
				dataList.add( UserGroupAssignmentReportUtil.generateResult(menuitemsQuery, "menulist", siteDNSName) );
				dataList.add( UserGroupAssignmentReportUtil.generateResult(portletsQuery, "portletlist", "public"+siteDNSName) );
				dataList.add( UserGroupAssignmentReportUtil.generateResult(portletsQuery, "portletlist", siteDNSName) );				
			} else if(!(selectedSite.startsWith("public")) && !("".equals(siteDNSName)) && (siteDNSName != null) ) {//If user selects the 'Secured' from the site drop down.				
				dataList.add( UserGroupAssignmentReportUtil.generateResult(menuitemsQuery, "menulist", siteDNSName) );
				dataList.add( UserGroupAssignmentReportUtil.generateResult(portletsQuery, "portletlist", siteDNSName) );
			} else {//If user selects the 'Unsecured' from the site drop down.
				dataList.add( UserGroupAssignmentReportUtil.generateResult(menuitemsQuery, "menulist", "public"+siteDNSName) );
				dataList.add( UserGroupAssignmentReportUtil.generateResult(portletsQuery, "portletlist", "public"+siteDNSName) );
			}
			if (mLog.isDebugEnabled()) {
				mLog.debug("UGAR-Final Generated Result List size :: " + dataList.size());
				mLog.debug("UGAR-Final Generated Result List :: " + dataList);
				mLog.debug("UGAR-Calling generateCSVFile() to generate the zipped CSV file :: ");
			}
			generateCSVFile(response, dataList, defaultReportName);			
		}
	}	
    
	/**
	 * Generates the zipped CSV file
	 * @param res   HttpServletResponse
	 * @param dataList List of NavigationItemDetails objects
	 * @param defaultReportName Name of the CSV file
	 * @return Generates the zipped CSV file and stream it back to JSP
	 */
	private void generateCSVFile(HttpServletResponse res, List<List> dataList, String defaultReportName){	
		if (mLog.isDebugEnabled())  {
			mLog.debug("UGAR-In generateCSVFile()");
		}
		try {			
			ZipOutputStream zout = new ZipOutputStream(res.getOutputStream());
			zout.putNextEntry(new ZipEntry(defaultReportName));
			OutputStreamWriter out = new OutputStreamWriter(zout, "UTF-8");
			/*
			Added this line to fix the non english chars issue [ A byte order mark (BOM) consists of the character code 
			U+FEFF at the beginning of a data stream, where it can be used as a signature defining the byte order and encoding form, 
			primarily of unmarked plaintext files. it can also serve as a hint indicating that the file is in Unicode ]
			http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6378911
			*/
			out.write(0xFEFF);
						
			List<String> labelList = Collections.unmodifiableList(Arrays.asList("MENUITEM_TITLE", "MENUITEM_ID", "MENUITEM_TYPE", "GROUPS"));
			if ((labelList!=null) && (dataList!=null) ) {
				if (dataList != null && dataList.size()>0) {
					for (int i=0, labelLen = labelList.size(); i < labelLen; i++) {
						if(labelLen == i+1) {//to ignore the last CSV_SEPARATOR to appened
							out.write(labelList.get(i));
							break;
						}
						out.write(labelList.get(i)+""+UserGroupAssignmentReportConstants.CSV_SEPARATOR);				
					}
					//CSV file content
					iterateCSVContent(dataList, out);									
				} else {
					out.write("No results found !");
				}	 		 							 
	      	}
			out.flush();
			zout.closeEntry(); 
			zout.finish();
		} catch(IOException ex ) { 
			mLog.error("IOException occured in generateCSVFile() during zip data transfer" +ex);
		} catch (Exception ioex) {
			mLog.error("Exception occured in generateCSVFile() during flush or close the zout object" +ioex);
		}		
	}//End of function
	
	/**
	 * Iterate the CSV file content and write it to the OutputStreamWriter
	 * @param dataList List of NavigationItemDetails objects
	 * @param out OutputStreamWriter object
	 * @return void
	 */
	private void iterateCSVContent(List<List> dataList, OutputStreamWriter out) {
		if (mLog.isDebugEnabled())  {
			mLog.debug("UGAR-In iterateCSVContent() :: " +dataList.size());
		}
		try {
			for (int j=0, dataLen = dataList.size(); j < dataLen; j++) {
				//add the new line
				out.write("\n");			
				List<NavigationItemDetails> list = dataList.get(j);
				for (int k=0, len = list.size(); k < len; k++) {
					NavigationItemDetails row = list.get(k);
					
					//Below piece of coded added too escape commas (,) and double quoes (") from the menuitem titles.
					String menuitemTitle = row.getTitle();
					mLog.debug("menuitemTitle before modify :: " +menuitemTitle);
					
					//The below IF condition added to ignore the null menuitem titles (If the type of menuitem is SPACER, the menuitem title would be NULL). Fix for IM5686398 [QC-7468]
					if (menuitemTitle != null) {  
						
						if (menuitemTitle.contains("\"")) {
							menuitemTitle = menuitemTitle.replace("\"", "\"\"");
						}
						menuitemTitle = "\""+menuitemTitle+"\"";

					}										
					mLog.debug("menuitemTitle after modify :: " +menuitemTitle);
					
					out.write(menuitemTitle+UserGroupAssignmentReportConstants.CSV_SEPARATOR);
					out.write(row.getId()+UserGroupAssignmentReportConstants.CSV_SEPARATOR);
					out.write(row.getType()+UserGroupAssignmentReportConstants.CSV_SEPARATOR);
					
					Collection<String> allGroups = row.getGroupList();
					for (String group : allGroups) {
						out.write(group);
					}
					if(len != k+1) {// add the new line							
						out.write("\n");
					}
				}
			}
		} catch(IOException ex) { 
			mLog.error("IOException occured in iterateCSVContent()" +ex);
		} catch (Exception ioex) {
			mLog.error("Exception occured in iterateCSVContent()" +ioex);
		}	
	}	
	
	/*
	 * Used to verify the secret key is present in session to prevent users from directly 
	 * accessing this servlet. This secret key is set in the view component before this 
	 * servelt is invoked
	 * @see com.hp.spp.report.util.UserGroupAssignmentReportConstants
	 * @return boolean - true if there is a match false otherwise
	*/	
	private boolean validateSecretKey(HttpServletRequest request){
		String key = (String)request.getSession(true).getAttribute("spp_ugsreport_secret_key");
		if (mLog.isDebugEnabled())  {
			mLog.debug("UGAR-In validateSecretKey() :: " +key);
		}
		return UserGroupAssignmentReportConstants.SPP_UGSREPORT_SECRETKEY.equals(key);
	}
	
}