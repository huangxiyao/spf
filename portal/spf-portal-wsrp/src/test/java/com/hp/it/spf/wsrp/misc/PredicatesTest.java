package com.hp.it.spf.wsrp.misc;

import junit.framework.TestCase;
import org.apache.axis.MessageContext;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PredicatesTest extends TestCase
{
	public void testIsWsrp() throws Exception {
		MessageContext messageContext = new MessageContext(null);

		messageContext.setSOAPActionURI(null);
		assertEquals("No SOAP action specified", false, Predicates.isWsrp(messageContext));

		messageContext.setSOAPActionURI("");
		assertEquals("Empty SOAP action specified", false, Predicates.isWsrp(messageContext));

		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v1:getServiceDescription");
		assertEquals("WSRP V1", true, Predicates.isWsrp(messageContext));

		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:initCookie");
		assertEquals("WSRP V2", true, Predicates.isWsrp(messageContext));
	}

	public void testIsOpenPortalProducer() throws Exception {
		MessageContext messageContext = new MessageContext(null);

		assertEquals("No transport URL specified",
				false, Predicates.isOpenPortalProducer(messageContext));

		messageContext.setProperty(MessageContext.TRANS_URL,
				"https://hppwsstg.passport.hp.com/xfire-webservice-webapp/services/UGSRuntimeServiceXfireImpl");
		assertEquals("Another web service URL is used",
				false, Predicates.isOpenPortalProducer(messageContext));

		messageContext.setProperty(MessageContext.TRANS_URL,
				"http://localhost:9001/spp-services-web/WSRP4JProducer/WSRPServiceDescriptionService");
		assertEquals("Invoking WSRP4J",
				false, Predicates.isOpenPortalProducer(messageContext));

		messageContext.setProperty(MessageContext.TRANS_URL,
				"http://localhost:9001/portletdriver/wsrp/router/v2/markup/ConfigModeProducer");
		assertEquals("Invoking Open Portal producer",
				true, Predicates.isOpenPortalProducer(messageContext));
	}

	public void testIsWsrp4jProducer() throws Exception {
		MessageContext messageContext = new MessageContext(null);

		assertEquals("No transport URL specified",
				false, Predicates.isWsrp4jProducer(messageContext));

		messageContext.setProperty(MessageContext.TRANS_URL,
				"https://hppwsstg.passport.hp.com/xfire-webservice-webapp/services/UGSRuntimeServiceXfireImpl");
		assertEquals("Another web service URL is used",
				false, Predicates.isWsrp4jProducer(messageContext));

		messageContext.setProperty(MessageContext.TRANS_URL,
				"http://localhost:9001/portletdriver/wsrp/router/v2/markup/ConfigModeProducer");
		assertEquals("Invoking Open Portal producer",
				false, Predicates.isWsrp4jProducer(messageContext));

		messageContext.setProperty(MessageContext.TRANS_URL,
				"http://localhost:9001/spp-services-web/WSRP4JProducer/WSRPServiceDescriptionService");
		assertEquals("Invoking WSRP4J",
				true, Predicates.isWsrp4jProducer(messageContext));

	}


	public void testIsAnyOfMethods() throws Exception {
		MessageContext messageContext = new MessageContext(null);

		assertEquals("No matching method found when no SOAPAction present",
				false, Predicates.isAnyOfMethods(messageContext, "initCookie"));

		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:initCookie");
		assertEquals("No matching methods found when none specified",
				false, Predicates.isAnyOfMethods(messageContext));
		assertEquals("No matching methods found",
				false, Predicates.isAnyOfMethods(messageContext, "getMarkup", "releaseSessions"));
		assertEquals("Maching method found",
				true, Predicates.isAnyOfMethods(messageContext, "getMarkup", "initCookie", "releaseSessions"));
	}


	public void testIsWsrpBaseCall() throws Exception {
		MessageContext messageContext = new MessageContext(null);

		assertEquals("No SOAP action specified", false, Predicates.isWsrpBaseCall(messageContext));

		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v1:initCookie");
		assertEquals("initCookie", false, Predicates.isWsrpBaseCall(messageContext));

		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:v2:handleEvents");
		assertEquals("handleEvents", true, Predicates.isWsrpBaseCall(messageContext));
	}
}
