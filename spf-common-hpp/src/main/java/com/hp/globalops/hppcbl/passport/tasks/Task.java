package com.hp.globalops.hppcbl.passport.tasks;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UID;
//import java.security.Security;
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.globalops.hppcbl.passport.manager.IConstantPassportService;
import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.ExtendedResolver;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * Created by IntelliJ IDEA. User: millerand Date: Aug 23, 2004 Time: 11:07:35
 * AM To change this template use File | Settings | File Templates.
 */
public abstract class Task {

    private Call call;

    private ArrayList faults;

    private String mSystemLangCode = "";
    
    private String version = "0";

    private static PassportParametersManager M_wsManagerInstance = null;

    private static Resolver dnsResolver = null;

    //private static final com.vignette.portal.log.LogWrapper LOG = new com.vignette.portal.log.LogWrapper(
    //        Task.class);

    // private static String JAVA_DNS_TTL_PROPERTY = "networkaddress.cache.ttl";

    static {
        Properties systemProperties = System.getProperties();

        M_wsManagerInstance = PassportParametersManager.getInstance();

        String proxyHost = null;
        String proxyPort = null;
        String nonProxyHosts = null;

        if (M_wsManagerInstance != null) {
            proxyHost = M_wsManagerInstance.getProxyHost();
            proxyPort = M_wsManagerInstance.getProxyPort();
            nonProxyHosts = M_wsManagerInstance.getNonProxyHosts();
        }

        if (proxyHost != null && proxyPort != null && !proxyHost.equals("")
                && !proxyPort.equals("")
                && !proxyHost.equalsIgnoreCase("empty")) {
            systemProperties.setProperty("http.proxyHost", proxyHost);
            systemProperties.setProperty("http.proxyPort", proxyPort);

            if (nonProxyHosts != null && !nonProxyHosts.equals("")
                    && !nonProxyHosts.equalsIgnoreCase("empty")) {
                systemProperties.setProperty("http.nonProxyHosts",
                        nonProxyHosts);
            }
        }

        systemProperties.setProperty("java.protocol.handler.pkgs",
                "com.sun.net.ssl.internal.www.protocol");

        java.security.Security
                .addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }

    /**
     * Lookup a DNS entry bypassing the java DNS mapping cache. It will not
     * cache the IP address.
     * 
     * @param hostName
     * @return
     */
    private synchronized InetAddress resolveHostWithoutCaching(String hostName)
            throws TaskExecutionException {

        InetAddress resolvedAddr = null;
        ExtendedResolver eresolve = null;

        try {

            if (dnsResolver == null) {
                // establish a singleton resolver for the whole 
                // web application lifecycle.
                try {
                    eresolve = new ExtendedResolver();
                    dnsResolver = (Resolver)eresolve;
                } catch (UnknownHostException e) {
                    String error = "Failed to initialize resolver";
                    //LOG.error(error);
                    throw new TaskExecutionException(error);
                }
            }

            // this class will first determine a working DNS server using
            // ipconfig /all
            // Type.A means looking up the IP Address.
            Lookup lu = new Lookup(hostName, Type.A);
            
            //provide the DNS resolver
            lu.setResolver(dnsResolver); 
            
            //do not depend on the (unlimited size) cache
            lu.setCache(null); 
            // contact the selected DNS server to get the selection of
            // addresses.
            Record[] raddrs = lu.run();
            String luMessage = lu.getErrorString();
            if (raddrs == null) {
                //LOG.error("DNS lookup issue with " + hostName
                //        + " reported from DNS server as: " + luMessage);
            } else {
                if (lu.getResult() == Lookup.SUCCESSFUL) {
                    ARecord a = (ARecord)raddrs[0];
                    resolvedAddr = a.getAddress();
                //    LOG.info("Resolve IP Address of " + hostName + " is "
                //            + resolvedAddr.toString());
                }
            }

        } catch (TextParseException uhe) {
            //LOG.error(uhe.getMessage());
            throw new TaskExecutionException(uhe.getMessage());
        }

        return resolvedAddr;
    }

    public void init() throws TaskExecutionException {

        if (M_wsManagerInstance == null)
            throw new TaskExecutionException(
                    "PassportService couldn't be initialize... please check if wsParamaters.properties exist");

        faults = new ArrayList();
        Service service = new Service();
        try {
            call = (Call)service.createCall();
        } catch (ServiceException e) {
            throw new TaskExecutionException(e);
        }

        InetAddress currentIP = null;
        try {
            String endpoint = M_wsManagerInstance.getEndPoint();

            java.net.URL url = new java.net.URL(endpoint);
            String virtualHostName = url.getHost();
            if ((virtualHostName == null)
                    || ("".equals(virtualHostName.trim()))) {
                throw new TaskExecutionException("Host has not been set");
            }
            
            //invoke resolveHostWithoutCaching to get the real ip address
            currentIP = resolveHostWithoutCaching(virtualHostName.trim());
            String hostAddress = currentIP.getHostAddress();
            String protocol = url.getProtocol();
            String file = url.getFile();
            int port = url.getPort();

            url = new java.net.URL(protocol, hostAddress, port, file);

            call.setTargetEndpointAddress(url);
            call.addHeader(createTransactionHeader());
            call.addHeader(createSystemLangHeader());
            /** * the following line is added by ck **** */
            /** * at 2006-12-06 for the system error of */
            /** * admin reset password *** */
            call.addHeader(createVersionHeader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SOAPException e) {
            throw new TaskExecutionException(e);
        } catch (SecurityException e) {
            throw new TaskExecutionException("Security exception: "
                    + e.getMessage());
        }
        call.setUsername(M_wsManagerInstance.getUserName());
        call.setPassword(M_wsManagerInstance.getPassword());
        // System.out.println("Call Initiated...");
    }

    protected abstract Object getRequestElement() throws TaskExecutionException;

    protected abstract Object getResponseElement()
            throws TaskExecutionException;

    protected SOAPHeaderElement createSystemLangHeader() throws SOAPException,
            TaskExecutionException {

        if (M_wsManagerInstance == null)
            throw new TaskExecutionException(
                    "PassportService couldn't be initialize... please check if wsParamaters.properties exist");

        SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
                getTargetNameSpace(), "hppwsHeaderElement");
        SOAPElement soapElement = soapHeaderElement
                .addChildElement("systemLangCode");

        if (getSystemLangCode() != null && !getSystemLangCode().equals(""))
            soapElement.addTextNode(getSystemLangCode());
        else
            soapElement.addTextNode(M_wsManagerInstance.getDefaultLangCode());

        return soapHeaderElement;
    }

    /** the following function is added by ck at 2006-12-06 *** */
    /** for the system error of admin reset password***************** */
    protected SOAPHeaderElement createVersionHeader() throws SOAPException {
        SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
                getTargetNameSpace(), "hppwsHeaderElement");
        SOAPElement soapElement = soapHeaderElement.addChildElement("version");
        
        if (getVersion() != null && !getVersion().equals(""))
            soapElement.addTextNode(getVersion());
        else
            soapElement.addTextNode("0");
        return soapHeaderElement;
    }

    protected SOAPHeaderElement createTransactionHeader() throws SOAPException {
        SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
                getTargetNameSpace(), "hppwsHeaderElement");
        SOAPElement soapElement = soapHeaderElement.addChildElement("tx-id");
        soapElement.addTextNode("tx:" + new UID());
        return soapHeaderElement;
    }

    protected SOAPBodyElement createRequest(Object requestObject)
            throws TaskExecutionException {
        StringWriter sw = new StringWriter();

        try {

            Marshaller marshaller = new Marshaller(sw);
            marshaller.marshal(requestObject);
            String xml = sw.getBuffer().toString();

            Document document = XMLUtils.newDocument(new ByteArrayInputStream(
                    xml.getBytes("UTF-8")));
            return new SOAPBodyElement(document.getDocumentElement());
        } catch (MarshalException me) {
            if (me.getMessage() != null
                    && me.getMessage().indexOf("invalid XML character") > -1) {
                createFaultObjects(me);
                throw new TaskExecutionException("Got Faults");
            } else {
                throw new TaskExecutionException(me);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new TaskExecutionException(e); // To change body of catch
            // statement use File |
            // Settings | File
            // Templates.
        }
    }

    private Object createResult(String resultAsString)
            throws TaskExecutionException {
        StringReader reader = new StringReader(resultAsString);
        Object response = getResponseElement();
        Unmarshaller unmarshaller = new Unmarshaller(response);
        try {
            unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            throw new TaskExecutionException(new AxisFault(
                    "Failed to unmarshal response: " + e.getMessage()));
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
            System.out.println("Sending: " + element);
            _resp = call.invoke(new Object[] {element});
            System.out.println(getClass().getName() + " - time: "
                    + (System.currentTimeMillis() - startTime) + " ms");
        } catch (AxisFault e) {
            System.out.println("AXIS FAULT");
            createFaultObjects(e);
            throw new TaskExecutionException("Got Faults");
        } catch (RemoteException e) {
            throw new TaskExecutionException("Remote exception: "
                    + e.getMessage());
        }

        Vector result = (Vector)_resp;
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
            while ((aFault = faultsList.item(faultsIndex)) != null
                    && faultsIndex < faultsList.getLength()) {
                NodeList faultChildNodes = aFault.getChildNodes();
                int faultChildIndex = 0;
                Node aFaultChild = null;
                String ruleNumber = null;
                String description = null;
                String code = null;
                String fieldName = null;
                String ftype = null;
                while ((aFaultChild = faultChildNodes.item(faultChildIndex)) != null
                        && faultChildIndex < faultChildNodes.getLength()) {
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
                    if (aFaultChild.getNodeName().equals("ftype")) {
                        ftype = aFaultChild.getLastChild().getNodeValue();
                    }
                    faultChildIndex++;
                }
                Fault fault = new Fault();
                fault.setRuleNumber(Integer.parseInt(ruleNumber));
                fault.setDescription(description);
                fault.setCode(code);
                fault.setFieldName(fieldName);
                fault.setFtype(ftype);
                faults.add(fault);
                faultsIndex++;
            }
        }
        if (faults.size() == 0) {
            e.printStackTrace();
            throw new TaskExecutionException(
                    "Failed to execute task: No ruleNumber in genericFault or No "
                            + "genericFaults present although AxisFault was thrown");
        }
    }

    private void createFaultObjects(MarshalException e)
            throws TaskExecutionException {
        faults = new ArrayList();

        Fault fault = new Fault();
        fault
                .setRuleNumber(IConstantPassportService.FAULT_MARSHALEXCEPTION_RULENUMBER);
        fault
                .setDescription(IConstantPassportService.FAULT_MARSHALEXCEPTION_DESCRIPTION);
        fault.setCode(IConstantPassportService.FAULT_MARSHALEXCEPTION_CODE);
        fault
                .setFieldName(IConstantPassportService.FAULT_MARSHALEXCEPTION_FIELDNAME);
        fault.setFtype(IConstantPassportService.FAULT_MARSHALEXCEPTION_FTYPE);
        faults.add(fault);
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
