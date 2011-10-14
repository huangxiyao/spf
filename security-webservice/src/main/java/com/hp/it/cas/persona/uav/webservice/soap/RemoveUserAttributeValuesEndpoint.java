package com.hp.it.cas.persona.uav.webservice.soap;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.webservice.IWebService;
import com.hp.it.cas.xa.logging.StopWatch;

/**
 * SOAP endpoint that deletes UserAttributeValues for the specified user.
 *
 * @author Quintin May
 */
public class RemoveUserAttributeValuesEndpoint extends AbstractPersonaEndpoint {

	private final Logger logger = LoggerFactory.getLogger(RemoveUserAttributeValuesEndpoint.class);
	
	private final IWebService webService;
	
	/**
	 * Creates the endpoint.
	 * @param defaultNamespaceUri the default XML namespace.
	 * @param webService the transactional business service.
	 * @throws XPathExpressionException if there is an error compiling XPaths.
	 */
	public RemoveUserAttributeValuesEndpoint(String defaultNamespaceUri, IWebService webService) throws XPathExpressionException {
		super(defaultNamespaceUri);
		this.webService = webService;
	}
	
	protected Element invokeInternal(Element request, Document response) throws Exception {
		logger.debug("ENTRY");
		StopWatch sw = new StopWatch().start();
		
		Collection<IUserAttributeValue> transactionSet = new ArrayList<IUserAttributeValue>();

		NodeList userAttributeValueElements = getUserAttributeValueElements(request);
		
		for (int i = 0, ii = userAttributeValueElements.getLength(); i < ii; ++i) {
			// deserialize from request
			IUserAttributeValue userAttributeValue = createUserAttributeValue((Element) userAttributeValueElements.item(i));
			transactionSet.add(userAttributeValue);
		}

		webService.removeUserAttributeValues(transactionSet);
		
		logger.debug("RETURN {}", sw);
		return null;
	}
}
