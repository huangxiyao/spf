package com.hp.it.cas.persona.webservice.test;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.UrlResource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.client.support.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.HttpTransportException;
import org.springframework.ws.transport.http.HttpUrlConnection;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

public class FindExample {

    private static final String WEB_SERVICE_URL = "https://sasuft1cl2.austin.hp.com:21978/cas-persona-webservice";
    private static final String SCHEMA_URL = WEB_SERVICE_URL + "/user-attribute-value.xsd";
    private static final String SOAP_URL = WEB_SERVICE_URL + "/soap";
    //private static final String WSDL_URL = SOAP_URL + "/PersonaUserAttributeValue.wsdl";
    private static final String USER_NAME = "APP-111319";
    private static final String PASSWORD = "123qwe!@#QWE";
    
	private static final String MESSAGE =
			"<FindUserAttributeValuesRequest xmlns='urn:X-hp:com.hp.it.cas.persona.schema.uav' userType='EXTERNAL_USER' userId='1234'/>";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();

		PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
		validatingInterceptor.setValidateRequest(true);
		validatingInterceptor.setValidateResponse(true);
//		validatingInterceptor.setSchema(new FileSystemResource("src/main/webapp/WEB-INF/user-attribute-value.xsd"));
		validatingInterceptor.setSchema(new UrlResource(SCHEMA_URL));
		validatingInterceptor.afterPropertiesSet();

		WebServiceTemplate template = new WebServiceTemplate(messageFactory);
        template.setDefaultUri(SOAP_URL);
		template.setMessageSender(new HttpBasicAuthenticationMessageSender(USER_NAME, PASSWORD));
		template.setInterceptors(new ClientInterceptor[] {validatingInterceptor});
		template.afterPropertiesSet();

/*      // WS-SSecurity (yuck...we don't use this)
		Resource policyConfiguration = new FileSystemResource("src/main/webapp/WEB-INF/securityPolicy.xml");

		SimpleUsernamePasswordCallbackHandler callbackHandler = new SimpleUsernamePasswordCallbackHandler();
		callbackHandler.setUsername(USER_NAME);
		callbackHandler.setPassword(PASSWORD);

		XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
		securityInterceptor.setPolicyConfiguration(policyConfiguration);
		securityInterceptor.setCallbackHandler(callbackHandler);
		securityInterceptor.afterPropertiesSet();

		template.setInterceptors(new XwsSecurityInterceptor[] {securityInterceptor});
*/
		
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
