package com.hp.globalops.hppcbl.passport.tasks;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;


/**
 * Created by IntelliJ IDEA.
 * User: millerand
 * Date: Aug 23, 2004
 * Time: 11:07:35 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Task {

    private Call call;
    private ArrayList faults;
	private String mSystemLangCode = "" ;
	
	private static PassportParametersManager M_wsManagerInstance = null ;

    static {
    	Properties systemProperties = System.getProperties();
    	
    	M_wsManagerInstance = PassportParametersManager.getInstance() ;
    	
    	String proxyHost = null ;
    	String proxyPort = null ;
    	String nonProxyHosts = null ;
    	
    	if(M_wsManagerInstance != null)
    	{
        	proxyHost = M_wsManagerInstance.getProxyHost() ;
        	proxyPort = M_wsManagerInstance.getProxyPort() ;
        	nonProxyHosts = M_wsManagerInstance.getNonProxyHosts() ;
    	}
    	
    	if(proxyHost != null && proxyPort != null 
    	&& !proxyHost.equals("") && !proxyPort.equals("")
    	&& !proxyHost.equalsIgnoreCase("empty"))
    	{
        	systemProperties.setProperty("http.proxyHost",proxyHost);
        	systemProperties.setProperty("http.proxyPort",proxyPort);
        	
        	if(nonProxyHosts != null && !nonProxyHosts.equals("")
	    	&& !nonProxyHosts.equalsIgnoreCase("empty"))
	    	{
	        	systemProperties.setProperty("http.nonProxyHosts",nonProxyHosts);
	    	}
    	}
    	    	
    	systemProperties.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");

    	java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }

    public void init() throws TaskExecutionException {
 
        if(M_wsManagerInstance == null)
            throw new TaskExecutionException("PassportService couldn't be initialize... please check if wsParamaters.properties exist");
    	
    	faults = new ArrayList();
        Service service = new Service();
        try {
            call = (Call) service.createCall();
        } catch (ServiceException e) {
            throw new TaskExecutionException(e);
        }
        

        try {
            call.setTargetEndpointAddress(new java.net.URL(M_wsManagerInstance.getEndPoint()));
            call.addHeader(createTransactionHeader());
            call.addHeader(createSystemLangHeader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SOAPException e) {
            throw new TaskExecutionException(e);
        }
        call.setUsername(M_wsManagerInstance.getUserName());
        call.setPassword(M_wsManagerInstance.getPassword());
//		System.out.println("Call Initiated...");
    }

    protected abstract Object getRequestElement() throws TaskExecutionException;

    protected abstract Object getResponseElement() throws TaskExecutionException;

    protected SOAPHeaderElement createSystemLangHeader() throws SOAPException, TaskExecutionException {
    	 
        if(M_wsManagerInstance == null)
            throw new TaskExecutionException("PassportService couldn't be initialize... please check if wsParamaters.properties exist");
    	
        SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(getTargetNameSpace(), "hppwsHeaderElement");
        SOAPElement soapElement = soapHeaderElement.addChildElement("systemLangCode");
        
        if(getSystemLangCode() != null && !getSystemLangCode().equals(""))
        	soapElement.addTextNode(getSystemLangCode());
        else
        	soapElement.addTextNode(M_wsManagerInstance.getDefaultLangCode());
        
        return soapHeaderElement;
    }

    protected SOAPHeaderElement createTransactionHeader() throws SOAPException {
        SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(getTargetNameSpace(), "hppwsHeaderElement");
        SOAPElement soapElement = soapHeaderElement.addChildElement("tx-id");
        soapElement.addTextNode("tx:" + new UID());
        return soapHeaderElement;
    }

    protected SOAPBodyElement createRequest(Object requestObject) throws TaskExecutionException {
        StringWriter sw = new StringWriter();
            
        try {
           
            Marshaller marshaller = new Marshaller(sw);
            marshaller.marshal(requestObject);
            String xml = sw.getBuffer().toString();
            
            Document document = XMLUtils.newDocument(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            return new SOAPBodyElement(document.getDocumentElement());
        } catch (Exception e) {
//			System.out.println(e.getMessage());
            throw new TaskExecutionException(e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Object createResult(String resultAsString) throws TaskExecutionException {
        StringReader reader = new StringReader(resultAsString);
        Object response = getResponseElement();
        Unmarshaller unmarshaller = new Unmarshaller(response);
        try {
            unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            throw new TaskExecutionException(new AxisFault("Failed to unmarshal response: " + e.getMessage()));
        }
        return response;
    }

    public ArrayList getFaults() {
        return faults;
    }

    public Object invoke() throws TaskExecutionException {
       
        Object requestObject = getRequestElement();
        
        Object _resp = null;
        
        SOAPBodyElement element = createRequest(requestObject);
        try {
            long startTime = System.currentTimeMillis();
			//System.out.println("Sending: " + element);
            _resp = call.invoke(new Object[]{element});
			System.out.println(getClass().getName() + " - time: " +
				(System.currentTimeMillis() - startTime) + " ms");


        } catch (AxisFault e) {
//			System.out.println("AXIS FAULT");
            createFaultObjects(e);
            throw new TaskExecutionException("Got Faults");

        } catch (RemoteException e) {
            throw new TaskExecutionException("Remote exception: " + e.getMessage());
        }
        Vector result = (Vector) _resp;
        return createResult(result.get(0).toString());
    }

    private void createFaultObjects(AxisFault e) throws TaskExecutionException {
        faults = new ArrayList();
        Element aFaultDetail = null;
        Element[] faultDetails = e.getFaultDetails();
        for (int i = 0; i < faultDetails.length; i++) {
            aFaultDetail = faultDetails[i];
            NodeList faultsList = aFaultDetail.getElementsByTagName("fault");
            int faultsIndex = 0;
            Node aFault = null;
            while ((aFault = faultsList.item(faultsIndex)) != null && faultsIndex < faultsList.getLength()) {
                NodeList faultChildNodes = aFault.getChildNodes();
                int faultChildIndex = 0;
                Node aFaultChild = null;
                String ruleNumber = null;
                String description = null;
                String code = null;
                String fieldName = null;
                while ((aFaultChild = faultChildNodes.item(faultChildIndex)) != null &&
                        faultChildIndex < faultChildNodes.getLength()) {
                    if (aFaultChild.getNodeName().equals("ruleNumber")) {
                        ruleNumber = aFaultChild.getLastChild().getNodeValue();
                    }
                    if (aFaultChild.getNodeName().equals("desc")) {
                        description = aFaultChild.getLastChild().getNodeValue();
                    }
                    if (aFaultChild.getNodeName().equals("code")) {
                        code = aFaultChild.getLastChild().getNodeValue();
                    }
                    if (aFaultChild.getNodeName().equals("fieldName")) {
                        fieldName = aFaultChild.getLastChild().getNodeValue();
                    }
                    faultChildIndex++;
                }
                Fault fault = new Fault();
                fault.setRuleNumber(Integer.parseInt(ruleNumber));
                fault.setDescription(description);
                fault.setCode(code);
                fault.setFieldName(fieldName);
                faults.add(fault);
                faultsIndex++;
            }
        }
        if (faults.size() == 0) {
            e.printStackTrace();
            throw new TaskExecutionException("Failed to execute task: No ruleNumber in genericFault or No " +
                    "genericFaults present although AxisFault was thrown");
        }
    }

    protected String getTargetNameSpace() {
        return "http://hppws.globalops.hp.com/PassportService";
    }

    public String getSystemLangCode() {
		return mSystemLangCode;
	}

	public void setSystemLangCode(String systemLangCode) {
		mSystemLangCode = systemLangCode;
	}
}
