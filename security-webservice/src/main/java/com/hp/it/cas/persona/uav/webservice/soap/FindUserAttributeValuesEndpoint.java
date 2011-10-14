package com.hp.it.cas.persona.uav.webservice.soap;

import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.webservice.IWebService;
import com.hp.it.cas.xa.logging.StopWatch;

/**
 * SOAP endpoint that returns all UserAttributeValues for the specified user.
 *
 * @author Quintin May
 */
public class FindUserAttributeValuesEndpoint extends AbstractPersonaEndpoint {

	private final Logger logger = LoggerFactory.getLogger(FindUserAttributeValuesEndpoint.class);
	
	private static final String RESPONSE = "FindUserAttributeValuesResponse";
	
	private final IWebService webService;
	
	/**
	 * Creates the endpoint.
	 * @param defaultNamespaceUri the default XML namespace.
	 * @param webService the transactional business service.
	 * @throws XPathExpressionException if there is an error compiling XPaths.
	 */
	public FindUserAttributeValuesEndpoint(String defaultNamespaceUri, IWebService webService) throws XPathExpressionException {
		super(defaultNamespaceUri);
		this.webService = webService;
	}
	
	protected Element invokeInternal(Element request, Document response) throws Exception {
		logger.debug("ENTRY");
		StopWatch sw = new StopWatch().start();
		
		IUserIdentifier userIdentifier = getUserIdentifier(request);		
		Set<IUserAttributeValue> userAttributeValues = webService.findUserAttributeValues(userIdentifier);
		
		// serialize into response
		Element responseElement = addElement(response, RESPONSE);

		for (IUserAttributeValue userAttributeValue : userAttributeValues) {
			addUserAttributeValue(responseElement, userAttributeValue);
		}
		
		logger.debug("RETURN {}", sw);
		return responseElement;
	}

}
