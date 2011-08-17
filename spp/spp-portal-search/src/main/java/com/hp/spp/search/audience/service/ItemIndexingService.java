package com.hp.spp.search.audience.service;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.search.common.NavigationItem;
import com.hp.spp.search.common.portal.PortalEngine;
import com.hp.spp.search.common.portal.PortalEngineException;
import org.apache.log4j.Logger;


/**
 * The ItemIndexingService class provides AgentPage Service for particular navigation item.
 * This class will need to be configured with the class name of the class implementing
 * the PortalEngine interface. This class will instantiate and use the PortalEngine
 * interface for its operations. The main purpose of this class is to  create instance
 * of NavigationItem class and populate all values and forward to the AgentPageView.jsp
 *
 * @author Shivashanker Bhujange
 * @version %I%, %G%
 */

/** Revision History:
*
* Ver.   Modified By           Date           Notes
*--------------------------------------------------------------------------------------*
* v1     Shivashanker B      20-Sep-2006      Created
*
*/
public class ItemIndexingService {
	//Get a reference to a logger.
	private static final Logger mLog = Logger.getLogger(ItemIndexingService.class);

	//The reference to the PortalEngine class.
	private PortalEngine thePortalEngine = null;

	/**
	 * The public constructor. This configures the service by reading and
	 * instantiating the appropriate class which implements the PortalEngine
	 * interface.
	 *
	 * @throws ItemIndexingServiceException If there are errors in instantiating the
	 * ItemIndexingService
	 *
	 */
	public ItemIndexingService() throws ItemIndexingServiceException {
		// Get the config property files from database using spp-config module
		// Get the fully qualified class name of the class which implements
		// PortalEngine.


		String portalEngineClassName = null;
		try {
			portalEngineClassName = Config.getValue("SPP.search.PortalEngineClassName");
		} catch (ConfigException configException) {
			String warn = "error occured while retrieving PortalEngineClassName " +
				"value from persistence store : " + configException.getMessage();
			mLog.warn(warn);
		}
		// Defaulting the class name
		if (portalEngineClassName == null
				|| portalEngineClassName.length() == 0) {
			portalEngineClassName = "com.hp.spp.search.vap.VignettePortalEngine";
		}
		// Configure and instantiate the instance of PortalEngine
		try {
			thePortalEngine = (PortalEngine) Class.forName(
					portalEngineClassName).newInstance();
		} catch (InstantiationException e) {
			String message = "Error instantiating portal engine class = "
					+ portalEngineClassName;
			throw new ItemIndexingServiceException(message, e);
		} catch (IllegalAccessException e) {
			String message = "Not enough access to instantiate portal engine class = "
					+ portalEngineClassName;
			throw new ItemIndexingServiceException(message, e);
		} catch (ClassNotFoundException e) {
			String message = "portal engine class = " + portalEngineClassName
					+ " not found";
			throw new ItemIndexingServiceException(message, e);
		}

	}
	/**
	 * This is the primary service method of this class. This method takes in
	 * the Servlet request and response objects. It does the following:
	 * 1\ It calls the getNavigationItem() method. Which populates & consturcts
	 *    Navigation Item object this object will be put into HttpServletRequest.
	 * 2\ Finally it forwads request to the Agent Page using RequestDispatcher
	 *    object's forward method.
	 *
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param ServletContext
	 *
	 * @throws ServletException,IOException
	 */
	public void getAgentPage(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse,ServletContext servletContext)
				throws ServletException,IOException{
		String aNavItemId=(String)httpservletrequest.getParameter("NavItemId");
		String aSiteDNS=(String)httpservletrequest.getParameter("SiteDNS");
		String aLanguageCode=(String)httpservletrequest.getParameter("LanguageCode");
		String aCountryCode=(String)httpservletrequest.getParameter("CountryCode");
		String path = null;
		try {
			path = Config.getValue("SPP.search.AgentPageView");
		} catch (ConfigException configException) {
			String warn = "error during retrieving AgentPage " +
				"value from persistence store : " + configException.getMessage();
			mLog.warn(warn);
			//log as warning
		}
		if(path == null || path.length()==0 ){
			//path = "AgentPageView.jsp";
			path = "jsp/SPP/search/AgentPageView.jsp";
		}
		path="/"+path;
		NavigationItem aNavigationItem = null;
		try {
			aNavigationItem =
				 thePortalEngine.getNavigationItem(aNavItemId,aSiteDNS,aLanguageCode,aCountryCode);
		} catch (PortalEngineException aPortalEngineException) {
			String error = "error occured in getNavigationItem() method of  PortalEngine : "
					+ aPortalEngineException.getMessage();
			mLog.error(error);
			
			//In this case since the navigation item is not found, we set the response header to 
			//HTTP status code 404
			pageNotFound(aPortalEngineException.getMessage(),httpservletrequest,
													httpservletresponse,servletContext);
			
			/*
			forwardToErrorPage(aPortalEngineException.getMessage(),httpservletrequest,
					httpservletresponse,servletContext);*/
		}
		httpservletrequest.setAttribute("NavigationItem",aNavigationItem);
		RequestDispatcher aRequestDispatcher = servletContext.getRequestDispatcher(path);
		try {
			aRequestDispatcher.forward(httpservletrequest,httpservletresponse);
		} catch (ServletException aServletException) {
			String error = "error occured while forwarding a request to agent page : "
				+ aServletException.getMessage();
			mLog.error(error);
			//Forwards to an error page
			forwardToErrorPage(aServletException.getMessage(),httpservletrequest,
					httpservletresponse,servletContext);
		} catch (IOException aIOException) {
			String error = "error occured while forwarding a request to agent page : "
				+ aIOException.getMessage();
			mLog.error(error);
			//Forwards to an error page
			forwardToErrorPage(aIOException.getMessage(),httpservletrequest,
					httpservletresponse,servletContext);
		}
	 }//getAgentPage

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
		} catch (ConfigException configException) {
			String warn = "error while retrieving ErrorPage " +
				"value from persistence store : " + configException.getMessage();
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
	 
	 
	 private void pageNotFound(String message,
			 HttpServletRequest httpservletrequest,HttpServletResponse httpservletresponse,
			 	ServletContext servletContext) throws ServletException,IOException{
		String errorPagePath = null;
		
		httpservletresponse.sendError(HttpServletResponse.SC_NOT_FOUND);
		
	 }//pageNotFound
	 
	 
}//class
