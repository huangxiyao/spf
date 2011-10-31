package com.hp.it.cas.persona.webservice.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.HttpTransportException;
import org.springframework.ws.transport.http.HttpUrlConnection;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;

/**
 * Read-only webservice client implementation of the Personal User Attribute Value Service.
 * @author quintin.may@hp.com
 */
public class UserAttributeValueService implements IUserAttributeValueService {
    private final WebServiceTemplate webServiceTemplate;
    private final String MESSAGE_TEMPLATE =
        "<FindUserAttributeValuesRequest xmlns='urn:X-hp:com.hp.it.cas.persona.schema.uav' userType='%s' userId='%s'/>";
    private final DocumentBuilder documentBuilder;
    
    /**
     * Creates a web service client.
     * @param webServiceUrl the web service endpoint URL.
     * @param userName the name of the user (application) connecting to the web service.
     * @param password the user's web service password.
     * @throws Exception if there is an error creating the client.
     */
    public UserAttributeValueService(URL webServiceUrl, String userName, String password) throws Exception {
        webServiceTemplate = createWebServiceTemplate(webServiceUrl, userName, password);
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    
    public Set<IUserAttributeValue> findUserAttributeValues(IUserIdentifier userIdentifier) {
        String message = String.format(MESSAGE_TEMPLATE, userIdentifier.getUserIdentifierType(), userIdentifier.getUserIdentifier());
        byte[] result = sendAndReceive(message);
        Set<IUserAttributeValue> userAttributeValues = parseUserAttributeValues(userIdentifier, result);
        return userAttributeValues;
    }

    private byte[] sendAndReceive(String message) {
        StreamSource source = new StreamSource(new StringReader(message));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(out);
        webServiceTemplate.sendSourceAndReceiveToResult(source, result);
        return out.toByteArray();
    }
    
    private Set<IUserAttributeValue> parseUserAttributeValues(IUserIdentifier userIdentifier, byte[] result) {
        try {
            Document document = documentBuilder.parse(new ByteArrayInputStream(result));
            Set<IUserAttributeValue> userAttributeValues = new LinkedHashSet<IUserAttributeValue>();
            for (Element element = (Element) document.getDocumentElement().getFirstChild();
                         element != null;
                         element = (Element) element.getNextSibling()) {
                
                String compoundId = element.getAttribute("compoundId");
                String simpleId = element.getAttribute("simpleId");
                String instanceIdentifier = element.getAttribute("instance");
                String value = element.getTextContent();
                
                UserAttribute userAttribute = new UserAttribute(compoundId, simpleId);
                UserAttributeValue userAttributeValue = new UserAttributeValue(userIdentifier, userAttribute,  instanceIdentifier, value);
                
                userAttributeValues.add(userAttributeValue);
            }
            return Collections.unmodifiableSet(userAttributeValues);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public IUserAttributeValue addUserAttributeValue(IUserIdentifier userIdentifier,
            String simpleUserAttributeIdentifier, String value) {
        throw new UnsupportedOperationException("addUserAttributeValue is not supported");
    }

    public IUserAttributeValue addUserAttributeValue(IUserIdentifier userIdentifier,
            String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier, String value) {
        throw new UnsupportedOperationException("addUserAttributeValue is not supported");
    }

    public IUserAttributeValue putUserAttributeValue(IUserIdentifier userIdentifier,
            String compoundUserAttributeIdentifier, String instanceIdentifier, String simpleUserAttributeIdentifier,
            String value) {
        throw new UnsupportedOperationException("putUserAttributeValue is not supported");
    }

    public void remove(IUserAttributeValue userAttributeValue) {
        throw new UnsupportedOperationException("remove is not supported");
    }

    private WebServiceTemplate createWebServiceTemplate(URL webServiceUrl, String userName, String password) throws Exception {
        URL soapUrl = new URL(webServiceUrl.toString() + "/soap");
        
        SaajSoapMessageFactory messageFactory = createMessageFactory();
        
        WebServiceTemplate template = new WebServiceTemplate(messageFactory);
        template.setDefaultUri(soapUrl.toString());
        template.setMessageSender(new HttpBasicAuthenticationMessageSender(userName, password));
        template.afterPropertiesSet();
        return template;
    }
    
    private SaajSoapMessageFactory createMessageFactory() throws Exception {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.afterPropertiesSet();
        return messageFactory;
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
