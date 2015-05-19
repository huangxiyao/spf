package com.hp.it.cas.persona.webservice.test;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.HttpTransportException;
import org.springframework.ws.transport.http.HttpUrlConnection;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

public class PutExample {

	private static final String MESSAGE =
		"<PutUserAttributeValuesRequest xmlns='http://cas.it.hp.com/persona/schema/uav'>"
		+ "<attribute compoundKey='64' instance='b859c55e-d89f-4691-82fb-3cc1748e5c5f' simpleKey='65' userId='1234' userType='EXTERNAL_USER'>Charlie</attribute>"
		+ "<attribute compoundKey='64' instance='b859c55e-d89f-4691-82fb-3cc1748e5c5f' simpleKey='66' userId='1234' userType='EXTERNAL_USER'>Brown</attribute>"
		//+ "<attribute simpleKey='63' userId='1234' userType='EXTERNAL_USER'>Mr.</attribute>"
		//+ "<attribute simpleKey='67' userId='1234' userType='EXTERNAL_USER'>Kal</attribute>"
		+ "</PutUserAttributeValuesRequest>"
		;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();

		WebServiceTemplate template = new WebServiceTemplate(messageFactory);
		template.setDefaultUri("http://localhost:8080/cas-persona-webservice/soap");
		template.setMessageSender(new HttpBasicAuthenticationMessageSender("APP-999999", "123qwe!@#QWE"));

//		Resource policyConfiguration = new FileSystemResource("src/main/webapp/WEB-INF/securityPolicy.xml");
//
//		SimpleUsernamePasswordCallbackHandler callbackHandler = new SimpleUsernamePasswordCallbackHandler();
//		callbackHandler.setUsername("APP-116414");
//		callbackHandler.setPassword("123qwe!@#QWE");
//
//		XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
//		securityInterceptor.setPolicyConfiguration(policyConfiguration);
//		securityInterceptor.setCallbackHandler(callbackHandler);
//		securityInterceptor.afterPropertiesSet();
//
//		template.setInterceptors(new XwsSecurityInterceptor[] {securityInterceptor});

		StreamSource source = new StreamSource(new StringReader(MESSAGE));
		StreamResult result = new StreamResult(System.out);
		template.sendSourceAndReceiveToResult(source, result);
	}

	private static class HttpBasicAuthenticationMessageSender extends HttpUrlConnectionMessageSender {

		private final String userName;
		private final String password;

		HttpBasicAuthenticationMessageSender(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}

		@Override
		public WebServiceConnection createConnection(URI uri) throws IOException {
			WebServiceConnection webServiceConnection = super.createConnection(uri);

			if (webServiceConnection instanceof HttpUrlConnection) {
				String authentication = userName + ":" + password;
				authentication = new String(Base64.encodeBase64(authentication.getBytes()));
				((HttpUrlConnection) webServiceConnection).getConnection().setRequestProperty("Authorization", "Basic " + authentication);
			} else {
				throw new HttpTransportException("Connection is not an HTTP URL connection.");
			}

			return webServiceConnection;
		}

	}
}
