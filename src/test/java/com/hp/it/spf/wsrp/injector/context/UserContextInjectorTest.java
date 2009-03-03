package com.hp.it.spf.wsrp.injector.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import junit.framework.TestCase;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.NullProvider;
import org.springframework.mock.web.MockHttpServletRequest;

import com.hp.it.spf.wsrp.misc.Utils;

/**
 * This is the test class for UserContextInjector class.
 * 
 * @author <link href="kaijian.ding@hp.com">Ding Kai-Jian</link>
 * @version 1.0
 * @see com.hp.it.spf.wsrp.injector.context.UserContextInjector
 */

public class UserContextInjectorTest extends TestCase {

	private MessageContext messageContext = null;
	private SOAPMessage message = null;

	public void setUp() {
		// read in soap xml
		InputStream input = UserContextInjectorTest.class.getClassLoader()
				.getResourceAsStream("vignette.xml");
		AxisClient tmpEngine = new AxisClient(new NullProvider());
		messageContext = new MessageContext(tmpEngine);
		// prepare SOAPMessage
		Message msg = new Message(input);
		msg.setMessageContext(messageContext);
		message = msg;
		messageContext.setMessage(message);

	}

	/**
	 * test when it is not a wsrp base call
	 */
	public void testInvokeNotWSRPBaseCall() {
		try {
			UserContextInjector injector = new UserContextInjector();
			injector.invoke(messageContext);
			// nothing will happen when it is not a wsrp base call
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * test when it is not a wsrp Request type
	 */
	public void testInvokeNotWSRPRequestType() {
		try {
			messageContext
					.setSOAPActionURI("urn:oasis:names:tc:wsrp:getMarkup");
			messageContext.setPastPivot(true);
			UserContextInjector injector = new UserContextInjector();
			injector.invoke(messageContext);
			// nothing will happen when it is not a wsrp request type soap
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * test when it is wsrp and with request
	 */
	public void testInvokeNormal() {
		// prepare test data
		messageContext.setSOAPActionURI("urn:oasis:names:tc:wsrp:getMarkup");
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpSession session = request.getSession();
		Map userProfile = new HashMap();
		userProfile.put("profileId", "1234");
		session.setAttribute("userProfile", userProfile);
		messageContext.setProperty(Utils.class.getName() + ".Request", request);
		try {
			UserContextInjector injector = new UserContextInjector();
			injector.invoke(messageContext);

			// verify UserContext node has been created
			Name headerName = message.getSOAPPart().getEnvelope().createName(
					"UserContext", "spf", "http://www.hp.com/spf");
			SOAPHeader header = message.getSOAPPart().getEnvelope().getHeader();
			Iterator it = header.getChildElements(headerName);
			if (it.hasNext()) {
				SOAPElement headerElement = (SOAPElement) it.next();
				if (headerElement.hasChildNodes()) {
					String userContextString = headerElement.getFirstChild()
							.getNodeValue();
					try {
						// verify can find userProfile info in SOAP
						assertTrue(userContextString.indexOf("1234") > 0);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
