/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.wsrp.extractor.context;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.Name;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is the test class for UserContextExtractor class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.wsrp.extractor.context.UserContextExtractor
 */
@RunWith(JMock.class)
public class UserContextExtractorTest {
    private static Mockery context;
    
    private static SOAPMessageContext messageContext;
    /**
     * Init mock objects.
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = new Mockery();         
        context.setImposteriser(ClassImposteriser.INSTANCE);
        context.setDefaultResultForType(Object.class, null);
        
        messageContext = context.mock(SOAPMessageContext.class);
        final SOAPMessage soapMessage = context.mock(SOAPMessage.class);
        final SOAPPart soapPart = context.mock(SOAPPart.class);
        final SOAPEnvelope soapEnvelope = context.mock(SOAPEnvelope.class);
        final SOAPHeader soapHeader = context.mock(SOAPHeader.class);
        final SOAPElement soapElement = context.mock(SOAPElement.class);
        final Node node = context.mock(Node.class);
        final SOAPBody soapBody = context.mock(SOAPBody.class);
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        context.checking(new Expectations() {
            {
                allowing(messageContext).getMessage();
                will(returnValue(soapMessage));
                
                allowing(messageContext).get(MessageContext.SERVLET_REQUEST);
                will(returnValue(request));
                
                allowing(messageContext).get("javax.xml.ws.soap.http.soapaction.uri");
                will(returnValue("urn:oasis:names:tc:wsrp:v1:getMarkup"));
                
                allowing(messageContext).get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
                will(returnValue(false));
                
                allowing(soapMessage).getSOAPPart();
                will(returnValue(soapPart));
                
                allowing(soapPart).getEnvelope();
                will(returnValue(soapEnvelope));
                
                allowing(soapEnvelope).createName(with(any(String.class)), with(any(String.class)), with(any(String.class)));
                
                allowing(soapEnvelope).getHeader();
                will(returnValue(soapHeader));
                
                allowing(soapHeader).getChildElements(with(any(Name.class)));
                will(returnIterator(soapElement));
                
                allowing(soapElement).hasChildNodes();
                will(returnValue(Boolean.TRUE));
                
                allowing(soapElement).getFirstChild();
                will(returnValue(node));
                
                String nodevalue = "{ContextMap={NavigationItemName=Html Viewer, PortalSessionId=6RsdJnHDcbXWly9Q5qq5jQqqR6ySCyr9BhzfnvHm69z5lLZhvtgc!-1891947260!1235732227333, PortalSite=acme, SessionToken=, PortalSiteURL=http://yliu92.asiapacific.hpqcorp.net:7001/portal/site/acme/, PortalRequestURL=http://yliu92.asiapacific.hpqcorp.net:7001/portal/site/acme/anon/htmlviewer/}, userProfile={Country=us, UserGroups=[ADMIN-PERSONA, LOCAL_PORTAL_LANG_EN, LOCAL_PORTAL_COUNTRY_US], Timezone=America/Los_Angeles, Language=en}}";
                allowing(node).getNodeValue();
                will(returnValue(nodevalue));
                
                allowing(soapMessage).getSOAPBody();
                will(returnValue(soapBody));
                
                allowing(soapBody).getElementsByTagName("clientAttributes");  
                
                allowing(request).setAttribute(with(any(String.class)), with(any(Object.class)));                
            }
        });
    }
    
    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        messageContext = null;        
    }
    
    /**
     * Test method for {@link com.hp.it.spf.wsrp.extractor.context.UserContextExtractor#getHeaders()}.
     */
    @Test
    public void testGetHeaders() {
        UserContextExtractor extractor = new UserContextExtractor();
        Set headers = extractor.getHeaders();
        assertNull(headers);
    }

    /**
     * Test method for {@link com.hp.it.spf.wsrp.extractor.context.UserContextExtractor#handleMessage(javax.xml.ws.handler.MessageContext)}.
     */
    @Test
    public void testHandleMessage() {
        boolean result = false;
        UserContextExtractor extractor = new UserContextExtractor();
        result = extractor.handleMessage(null);
        assertTrue(result);
        
        result = extractor.handleMessage(messageContext);
        assertTrue(result);
    }

    /**
     * Test method for {@link com.hp.it.spf.wsrp.extractor.context.UserContextExtractor#handleFault(javax.xml.ws.handler.MessageContext)}.
     */
    @Test
    public void testHandleFault() {
        UserContextExtractor extractor = new UserContextExtractor();
        boolean result = extractor.handleFault(null);
        assertTrue(result);
    }
}
