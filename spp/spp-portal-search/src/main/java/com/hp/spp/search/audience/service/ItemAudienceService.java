package com.hp.spp.search.audience.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.search.common.ItemList;
import com.hp.spp.search.common.Site;
import com.hp.spp.search.common.User;
import com.hp.spp.search.common.portal.PortalEngine;
import com.hp.spp.search.common.portal.PortalEngineException;

/**
 * This class is responsible for carrying out the audiencing of the items. This
 * class will need to be configured with the class name of the class
 * implementing the <I> PortalEngine </I> interface. This class will 
 * instantiate and use the PortalEngine interface for its operations. 
 * This takes as input the ServletRequest and ServletResponse. It uses castor 
 * to parse the xml request. This class makes use of a Castor mapping file 
 * which is loaded when the first instance of the class is created.
 * It then makes use of the PortalEngine object to audience the list of items.
 * It then calculated the list of 'allowed' items and builds the XML response.
 *
 * This is a singleton class
 *
 * @author Akash
 * @version %I%, %G%
 */

/** Revision History:
 *
 * Ver.   Modified By           Date           Notes
 *-----------------------------------------------------------------------------*
 * v1     Akash   		      11-Sep-2006      Created
 * 
 */
public class ItemAudienceService {
	
	// Get a reference to a logger.
	private static final Logger mLog = Logger.getLogger(ItemAudienceService.class);
	 
	//The reference to the PortalEngine class.
	private PortalEngine thePortalEngine = null;

	// The single instance of this class.
	private static ItemAudienceService thisService = null;

	//The Castor Mapping object. This will hold a reference to the Mapping file.
	private Mapping mMapping =  null;


	//The default error response to send in case everything fails.
	private static String DEFAULT_ERROR_RESPONSE = 
		"<AudiencingServiceResponse>" +
		"<ErrorResponse type=\"system\">" +
		"<Code>spp-search-100</Code>" +
		"<Description>audience system failure</Description>" +
		"</ErrorResponse>" +
		"</AudiencingServiceResponse>";

	/**
	 * The private constructor. This configures the service by reading and
	 * instantiating the appropriate class which implements the PortalEngine
	 * interface. In addition, this constructor also reads the location of
	 * the Castor mapping file and holds a reference to it.
	 * 
	 * This class is a singleton mainly to avoid having to read and reload 
	 * the PortalEngine class and Castor mapping file every time.
	 *
	 * @throws AudienceServiceException If there are errors in instantiating the
	 * ItemAudienceService
	 *
	 */
	private ItemAudienceService() throws AudienceServiceException {

		// Get the config property files from database using spp-config module
		// Get the fully qualified class name of the class which implements
		// PortalEngine.
		String portalEngineClassName = null;
		String castorMappingFile = null;
		try {
			portalEngineClassName = Config.getValue("SPP.search.PortalEngineClassName");
			castorMappingFile = Config.getValue("SPP.search.castorMappingFile");
		} catch (ConfigException e1) {
			String warn = "error occured while retrieving PortalEngineClassName " +
				"value from persistence store : " + e1.getMessage();
			mLog.warn(warn);
		}
		// Defaulting the class name
		if (portalEngineClassName == null
				|| portalEngineClassName.length() == 0) {
			portalEngineClassName = "com.hp.spp.search.vap.VignettePortalEngine";
		}
		// Defaulting the location of the castor mapping file
		if (castorMappingFile == null
				|| castorMappingFile.length() == 0) {
			castorMappingFile = "/com/hp/spp/search/audience/service/AudiencingCastorMapping.xml";
		}
		mMapping = new Mapping();
		try {
			//URL tempUrl = this.getClass().getResource(castorMappingFile);
			//System.out.println(tempUrl.getPath());
			//System.out.println(tempUrl.toString());
			//Read the file resource as URL and load it once.
			mMapping.loadMapping(this.getClass().getResource(castorMappingFile));			
		} catch (IOException e1) {
			String message = "Error loading the castor mapping file at "+castorMappingFile;
			throw new AudienceServiceException(message, e1);
		} catch (MappingException e1) {						
			String message = "Error reading the castor mapping file at "+castorMappingFile;
			throw new AudienceServiceException(message);
		}
		// Configure and instantiate the instance of PortalEngine
		try {
			thePortalEngine = (PortalEngine) Class.forName(
					portalEngineClassName).newInstance();
		} catch (InstantiationException e) {
			String message = "Error instantiating portal engine class = "
					+ portalEngineClassName;
			throw new AudienceServiceException(message, e);
		} catch (IllegalAccessException e) {
			String message = "Not enough access to instantiate portal engine class = "
					+ portalEngineClassName;
			throw new AudienceServiceException(message, e);
		} catch (ClassNotFoundException e) {
			String message = "portal engine class = " + portalEngineClassName
					+ " not found";
			throw new AudienceServiceException(message, e);
		}
	}

	/**
	 * Returns the single instance of this class.
	 *
	 * @throws AudienceServiceException
	 *
	 */
	public static synchronized ItemAudienceService getInstance()
			throws AudienceServiceException {
		if (thisService == null) {
			thisService = new ItemAudienceService();
		}
		return thisService;
	}
	
	/**
	 * This is the primary service method of this class. This method takes in 
	 * the Servlet request and response objects. It does the following:
	 * 1\ It parses & unmarshals the XML request from the ServletRequest object.
	 * 2\ It calls the configured <I>PortalEngine</I> to filter the navigation items
	 * 3\ It marshals the allowed items list returned by the PortalEngine.
	 * 4\ It sends back the response xml in the ServletResponse object.
	 * 5\ If there are exceptions, it contstructs the appropriate response xml.
	 * 6\ It does <B>not</B> throw an exception back.  
	 *
	 * @param HttpServletRequest 
	 * @param HttpServletResponse
	 *
	 */
	public void audienceItems(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) {

		String xmlRequest = (String) httpservletrequest.getParameter("xmlRequest");
		String xmlResponse = null;
		
		// URL decode the incomming xml request. For compatibility and as a
		// standard, UTF-8 is followed for decoding.
		try {			
			xmlRequest = URLDecoder.decode(xmlRequest, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			String message = "Error decoding the xml in the request. The xml = "
					+ xmlRequest;
			// Log the error here
			String error = message + "  message from exception = " + e.getMessage();   
    		mLog.error(error);
			// Build and return the System Exception
			sendResponseXml(buildErrorResponse("spp-search-101"), httpservletresponse);			
		}		
		try {			
			xmlResponse = audienceItems(xmlRequest);
		} catch (MarshalException e) {
			e.printStackTrace();
			String error = "error while marshalling or unmarshalling the xml : " + e.getMessage();
			error = error + "xmlRequest = " + xmlRequest;
    		mLog.error(error);
			xmlResponse = buildErrorResponse("spp-search-102");			
		} catch (ValidationException e) {
			e.printStackTrace();
			String error = "error while validating  the xml : " + e.getMessage();
			error = error + "xmlRequest = " + xmlRequest;
    		mLog.error(error);
			xmlResponse = buildErrorResponse("spp-search-103");			
		} catch (PortalEngineException e) {
			e.printStackTrace();
			String error = "problem occured in audienceItems(String) method  : " + e.getMessage();   
    		mLog.error(error);
			xmlResponse = buildErrorResponse("spp-search-104");			
		}catch (Exception e) {
			//This is the safety net. This catches all types of exceptions and ensures
			//that the client gets an elegant error response.
			String error = "error occured in audienceItems(String) method : " + e.getMessage();   
    		mLog.error(error);
			xmlResponse = getDefaultErrorResponseXml();			
		}		
		//Finally return the response XML to the client.
		sendResponseXml(xmlResponse, httpservletresponse);		
	}//end getOutputStream

	/**
	 * This is the helper method. This method takes in 
	 * the request XML as a String. It does the following:
	 * 1\ It parses & unmarshals the XML request.
	 * 2\ It calls the configured <I>PortalEngine</I> to filter the navigation items
	 * 3\ It marshals the allowed items list returned by the PortalEngine.
	 * 4\ It sends back the response xml  
	 *
	 * @param xmlRequest: String 
	 * @return Response XML: String
	 *
	 */
	private String audienceItems(String xmlRequest) throws MarshalException,
			ValidationException, PortalEngineException {
		StringReader xmlRequestReader = new StringReader(xmlRequest);
		String outputXmlString = null;		
		AudiencingServiceRequest audienceRequest = null;
		//Unmarshaller unmarshaller = new Unmarshaller(AudiencingServiceRequest.class);
		Unmarshaller unmarshaller = null;
		try{
			unmarshaller = new Unmarshaller(mMapping);
			//unmarshaller.setDebug(true);
			unmarshaller.setMapping(mMapping);
			audienceRequest = (AudiencingServiceRequest)unmarshaller.unmarshal(xmlRequestReader);
		}catch(MappingException e){
			String message = "Mapping error during unmarshalling the request";
			message = message+" mapping file = "+mMapping;
			message = message+" Incomming xml request = "+xmlRequest;
			message = message+" message from exception = "+e.getMessage();			
			//Log the above string here.			 
    		mLog.error(message);			
			return buildErrorResponse("spp-search-105");			
		}		
		List itemList = audienceRequest.getItemList().getNavigationItemList();
		Site site = audienceRequest.getSite();
		User user = audienceRequest.getUser();
		List listOfAllowedItems = null; 			
		try{	
			listOfAllowedItems = thePortalEngine.audienceItems(site, user, itemList);			
		}catch(IllegalArgumentException e) {
			//As per design an Illegal argument such as null or invalid user Id
			//should be logged as warning but result in an empty list of 'allowed items'.			
			//create an empty list of allowed items
			listOfAllowedItems = new ArrayList();
			//Get the message from the IllegalArgumentException and log it as warning.
			String warn = "Illegal arguments are passed to audienceItems() method " + e.getMessage();
			mLog.warn(warn);			
		}
		ItemList allowedList = new ItemList();
		allowedList.setNavigationItemList(listOfAllowedItems);
		AudiencingServiceResponse audienceResponse  = new AudiencingServiceResponse();
		SuccessResponse successResponse = new SuccessResponse();
		successResponse.setItemList(allowedList);
		audienceResponse.setSuccessResponse(successResponse);
		StringWriter outputXml = new StringWriter();
		//audienceResponse.marshal(outputXml);
		Marshaller marshaller = null;
		try {
			marshaller = new Marshaller(outputXml);
			marshaller.setMapping(mMapping);
			//marshaller.setDebug(true);
		} catch (IOException e) {
			String message = "IOException when setting the mapping in the Marshaller";
			message = message+" mapping file = "+mMapping;
			message = message+" Incomming xml request = "+xmlRequest;
			message = message+" message from exception = "+e.getMessage();
			//Log the above string here
			mLog.error(message);			
			outputXmlString = ("spp-search-106");			
		} catch (MappingException e) {
			String message = "Mapping error during marshalling the request";
			message = message+" mapping file = "+mMapping;
			message = message+" Incomming xml request = "+xmlRequest;
			message = message+" message from exception = "+e.getMessage();			
			//Log the above string here.			
			mLog.error(message);
			outputXmlString = buildErrorResponse("spp-search-105");
		}
		marshaller.marshal(audienceResponse);
		outputXmlString = outputXml.toString();		
		return  outputXmlString;
	}

	/**
	 * This method returns the error response xml to the client
	 * based on error code passed to it.
	 * 
	 * @param errorCode String
	 * 
	 */
	private String buildErrorResponse(String errorCode){
		String errorDescription = null;
		try{
			errorDescription = Config.getValue("SPP.search."+errorCode);
		} catch (ConfigException e1) {
			// Log this here
			String warn = "error occured while retrieving the error description " +
				"for error code : " + errorCode;
			mLog.warn(warn);
			// No need to propogate this exception since we default the value
		}
		if (errorDescription == null || errorDescription.length() == 0 ) {
			errorDescription = "System error";
		}
		AudiencingServiceResponse audienceResponse  = new AudiencingServiceResponse();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode(errorCode);
		errorResponse.setDescription(errorDescription);
		audienceResponse.setErrorResponse(errorResponse);

		StringWriter errorResponseXml = new StringWriter();
		//audienceResponse.marshal(outputXml);
		Marshaller marshaller = null;
		try {
			marshaller = new Marshaller(errorResponseXml);
			marshaller.setMapping(mMapping);
		} catch (IOException e) {			
			return getDefaultErrorResponseXml();			
		} catch (MappingException e) {			
			return getDefaultErrorResponseXml();			
		}
		try {
			marshaller.marshal(audienceResponse);			
		} catch (MarshalException e) {
			e.printStackTrace();
			return getDefaultErrorResponseXml();			
		} catch (ValidationException e) {
			e.printStackTrace();
			return getDefaultErrorResponseXml();			
		}
		return errorResponseXml.toString();
	}

	/**
	 * This method returns the response xml to the client.
	 * It first sets the character encoding of the xml response as UTF-8.
	 * It then sets the content type.
	 * This same method is used to write back the success response or the error response.
	 *
	 * @param xmlToBeSent String
	 * @param HttpServletResponse
	 *
	 */
	private void sendResponseXml(String xmlToBeSent, HttpServletResponse httpservletresponse){

		//httpservletresponse.setCharacterEncoding("UTF-8");
		httpservletresponse.setContentType("text/html;charset=UTF-8");
		PrintWriter pWriter = null;
		try {
			pWriter = httpservletresponse.getWriter();
		} catch (IOException e) {			
			//Log it here, nothing more can be done.
			String error = "Problem occured while obtaining a handle to PrintWriter : "+e.getMessage();
			mLog.error(error);
			//send the default error response.
			xmlToBeSent = getDefaultErrorResponseXml();
		}
		try {
			pWriter.print(xmlToBeSent);
			pWriter.flush();			
		}finally{
			//close the PrintWriter here.
			String warn = "PrintWriter is closed.";
			mLog.warn(warn);
			pWriter.close();
		}
	}
    
	/**
	 * This method returns the default response xml to the client.
	 * @return String defaultErrorResponseXml
	 */
	private String getDefaultErrorResponseXml(){
		String defaultErrorResponseXml = null;
		try {
			defaultErrorResponseXml = Config.getValue("SPP.search.defaultSystemErrorResponseXml");
		} catch (ConfigException e1) {
			// Log this here
			String warn = "problem occured while retrieving SPP.search.defaultSystemErrorResponseXml " +
				"value from persistence store : " + e1.getMessage();
			mLog.warn(warn);
			// No need to propogate this exception since we default the value
			defaultErrorResponseXml = DEFAULT_ERROR_RESPONSE;
		}
		if (defaultErrorResponseXml == null || defaultErrorResponseXml.length() == 0) {
			defaultErrorResponseXml = DEFAULT_ERROR_RESPONSE;
		}
		return defaultErrorResponseXml;
	}
}
