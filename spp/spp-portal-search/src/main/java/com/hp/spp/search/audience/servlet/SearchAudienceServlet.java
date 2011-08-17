/**
 *
 */
package com.hp.spp.search.audience.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.search.audience.service.AudienceServiceException;
import com.hp.spp.search.audience.service.ItemAudienceService;
import com.hp.spp.search.audience.service.ItemIndexingService;
import com.hp.spp.search.audience.service.ItemIndexingServiceException;

/**
 * This is the face or the interface of the navigation items audiencing service.
 * This servlet acts as the controller for requests to the audiencing & indexing service.
 * The first step here is to determine where this request should be routed based on comparison
 * of the last part of the uri with control string. The control string is obtained from the
 * persistence store
 *
 * @author Akash
 * @version %I%, %G%
 */


/** Revision History:
 *
 * Ver.   Modified By           Date           Notes
 *--------------------------------------------------------------------------------------*
 * v1     Akash   		      11-Sep-2006      Created
 * v2     Shivashanker B      20-Sep-2006      Added forwardToErrorPage() method &
 * 											   modified doPost() method to include condition
 * 											   for handling ItemAudiencRequest
 */
public class SearchAudienceServlet extends HttpServlet {
    //Get a reference to a logger.
	private static final Logger mLog = Logger.getLogger(SearchAudienceServlet.class);
	private String itemAudiencingRequest = null;
	private String itemIndexingRequest = null;
	public void init()
          throws ServletException {
		//Read the names of the control strings using the spp-config module.

		try {
			itemAudiencingRequest = Config.getValue("SPP.search.ItemAudiencingRequest");
		} catch (ConfigException e) {
			String warn = "error occured while retrieving ItemAudiencingRequest " +
				"value from persistence store : " + e.getMessage();
			mLog.warn(warn);
		}
		if(itemAudiencingRequest==null || itemAudiencingRequest.length()==0 ){
			itemAudiencingRequest = "ItemAudiencingRequest";
		}
		try {
			itemIndexingRequest = Config.getValue("SPP.search.ItemIndexingRequest");
		} catch (ConfigException e) {
			String warn = "error occured while retrieving ItemIndexingRequest " +
				"value from persistence store : " + e.getMessage();
			mLog.warn(warn);
		}
		if(itemIndexingRequest==null || itemIndexingRequest.length()==0 ){
			itemIndexingRequest = "ItemIndexingRequest";
		}

	}

	public void doGet(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,
			IOException {
		doPost(httpservletrequest, httpservletresponse);
	}

	public void doPost(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,IOException {
		ServletConfig aServletConfig = getServletConfig();
		ServletContext aServletContext = aServletConfig.getServletContext();
		ItemAudienceService itemAudiencingService = null;

		//Returns the part of this request's URL from the protocol
		//name up to the query string in the first line of the HTTP request.
		String reqUri = httpservletrequest.getRequestURI();
		try {
			//Find out the 'control' string which appeared after the servlet path.
			//This determines where the request should be routed.
			if (reqUri.endsWith(itemAudiencingRequest)) {
				//Route to item audiencing service.
				try {
					itemAudiencingService = ItemAudienceService.getInstance();
				} catch (AudienceServiceException e) {
					//Need to log this as error...
					String error = "ItemAudienceService is not available : "+ e.getMessage();
					mLog.error(error);
					//Forward to error page.
					forwardToErrorPage(e.getMessage(),httpservletrequest,httpservletresponse,aServletContext);
				}
				itemAudiencingService.audienceItems(httpservletrequest, httpservletresponse);
			}else if (reqUri.endsWith(itemIndexingRequest)) {
				ItemIndexingService aItemIndexingService  = null;
				try {
					aItemIndexingService = new ItemIndexingService();
					aItemIndexingService.getAgentPage(httpservletrequest,httpservletresponse,aServletContext);
				}catch (ItemIndexingServiceException aItemIndexingServiceException) {
					String error = "ItemIndexingService is not available : "+ aItemIndexingServiceException.getMessage();
					mLog.error(error);
					//Forward to error page.
					forwardToErrorPage(aItemIndexingServiceException.getMessage(),httpservletrequest,
							httpservletresponse,aServletContext);
				}
			}else{
				//default case... do default thing
				//Or log as error
				String error = "Control string for the servlet is not proper";
				mLog.error(error);
			}
		}catch(Exception e){
			String error = "error occured in servlet : "+ e.getMessage() ;
			error = error + " Exception Class = "+e.getClass().getName();
			mLog.error(error);
			forwardToErrorPage(e.getMessage(),httpservletrequest,httpservletresponse,aServletContext);
		}//End super excpetion catch block
	}

	/**
	 * This method simply forwards request to an error page.
	 *
	 * @param String
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param ServletContext
	 *
	 * @throws ServletException,IOException
	 *
	 */
	private void forwardToErrorPage(String message,
			 HttpServletRequest httpservletrequest,HttpServletResponse httpservletresponse,
			 	ServletContext servletContext) throws ServletException,IOException{
		String errorPagePath = null;
		try {
			errorPagePath = Config.getValue("SPP.search.ErrorPage");
		} catch (ConfigException e) {
			//log this as error here.
			String warn = "error occured while retrieving ErrorPage " +
				"value from persistence store : " + e.getMessage();
			mLog.warn(warn);
		}
		if(errorPagePath==null || errorPagePath.length()==0){
			//errorPagePath = "ErrorPage.jsp";
			errorPagePath = "jsp/SPP/search/ErrorPage.jsp";
		}//if
		errorPagePath = "/"+errorPagePath;
		httpservletrequest.setAttribute("Message",message);
		RequestDispatcher aRequestDispatcher = servletContext.getRequestDispatcher(errorPagePath);
		aRequestDispatcher.forward(httpservletrequest,httpservletresponse);
	 }//forwardToErrorPage
}
