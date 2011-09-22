package com.hp.it.spf.misc.portal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vignette.portal.portlet.management.external.PortletException;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.WsrpPortletApplicationSpiImpl;

/**
 * Utility class used to register, in Vignette Portal, remote portlet servers described in XML
 * document.
 *
 * <p>The sample XML document containing registration data looks as follows:</p>
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;remote-portlet-servers&gt;
 *     &lt;remote-portlet-server&gt;
 *         &lt;name&gt;AAE_FUT_WSRP&lt;/name&gt;
 *         &lt;import-id&gt;AAE_WSRP&lt;/import-id&gt;
 *         &lt;description&gt;&lt;/description&gt;
 *         &lt;producer-url&gt;http://g2u0460c.austin.hp.com:25577/portletdriver/wsrp/wsdl/AAEFUTA&lt;/producer-url&gt;
 *     &lt;/remote-portlet-server&gt;
 * &lt;/remote-portlet-servers&gt;
 * </pre>
 *
 * @author Xu, Ke-Jun (Daniel,HPIT-GADSC) (ke-jun.xu@hp.com)
 */
public class RegisterRemotePortletServers
{

    public static void main(String[] args)
    {
        if (args.length == 0) {
            System.out.printf("java [options] %s %s%n", RegisterRemotePortletServers.class.getName(),
                    "path/to/spf_remote_portlet_servers.xml");
            System.exit(0);
        }

        File remoteServersFile = new File(args[0]);
        if (!remoteServersFile.isFile()) {
            System.err.printf("Data file not found: %s%n", remoteServersFile.getAbsolutePath());
            System.exit(1);
        }

        Document remoteServersData = null;
        try {
            remoteServersData = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(remoteServersFile);
        }
        catch (Exception e) {
            System.err.printf("Error occurred while parsing data file '%s': %s%n",
                    remoteServersFile.getAbsolutePath(), e);
            System.exit(2);
        }

        try {
            RegisterRemotePortletServers registerTool = new RegisterRemotePortletServers() {
                // override registerRemoteServer method so we can print diagnostic information
                // without tying the actual method implementation to use System.out which is ok
                // for command line usage as here.

                @Override
                void registerRemoteServer(RemotePortletServerBean remoteServer) throws PortletException
                {
                    super.registerRemoteServer(remoteServer);
                    System.out.printf("Successfully registered remote server: name=%s, import ID=%s, producer URL=%s%n",
                            remoteServer.getTitle(), remoteServer.getFriendlyID(), remoteServer.getServiceDescriptionEndpoint());
                }
            };
            registerTool.registerRemoteServers(remoteServersData);
        }
        catch (Exception e) {
            System.err.printf("Error occurred while registering remote portlet servers: %s%n", e);
            e.printStackTrace();
            System.exit(3);
        }
    }

    public void registerRemoteServers(Document remoteServersData) throws Exception {
        List<RemotePortletServerBean> list = getRemotePortletServerDefs(remoteServersData);
        for (RemotePortletServerBean remoteServer : list) {
            registerRemoteServer(remoteServer);
        }
    }

    private List<RemotePortletServerBean> getRemotePortletServerDefs(Document remoteServersData)
    {
        remoteServersData.getDocumentElement().normalize();

        List<RemotePortletServerBean> list = new ArrayList<RemotePortletServerBean>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList listOfServers =
                    (NodeList) xpath.evaluate("/remote-portlet-servers/remote-portlet-server",
                            remoteServersData, XPathConstants.NODESET);

            for (int s = 0, lenS = listOfServers.getLength(); s < lenS; s++) {
                Node serverNode = listOfServers.item(s);

                String title = xpath.evaluate("name", serverNode).trim();
                String friendlyID = xpath.evaluate("import-id", serverNode).trim();
                String description = xpath.evaluate("description", serverNode).trim();
                String serviceDescriptionEndpoint = xpath.evaluate("producer-url", serverNode).trim();

                RemotePortletServerBean bean = new RemotePortletServerBean(
                        title, description, friendlyID, serviceDescriptionEndpoint);
                list.add(bean);
            }
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Error reading remote servers data", e);
        }
        return list;
    }

    void registerRemoteServer(RemotePortletServerBean remoteServer) throws PortletException
    {
        WsrpPortletApplicationSpiImpl wpas = new WsrpPortletApplicationSpiImpl(
                remoteServer.getTitle(),
                remoteServer.getDescription(),
                remoteServer.getServiceDescriptionEndpoint(),
                Collections.emptyList());
        wpas.setFriendlyID(remoteServer.getFriendlyID());
        wpas.saveImpl();
    }

    static class RemotePortletServerBean {

        private String title;
        private String description;
        private String friendlyID;
        private String serviceDescriptionEndpoint;

        /**
         * @param title
         *            the title of remote portlet server
         * @param description
         *            the description of remote portlet server
         * @param friendlyID
         *            the friendlyID of remote portlet server
         * @param serviceDescriptionEndpoint
         *            the url of remote portlet server
         */
        public RemotePortletServerBean(String title, String description, String friendlyID,
                String serviceDescriptionEndpoint)
        {
            if (title == null || title.trim().equals("")) {
                throw new IllegalArgumentException("Remote server title (name) cannot be null or empty");
            }
            if (friendlyID == null || friendlyID.trim().equals("")) {
                throw new IllegalArgumentException("Remote server friendlyID (import ID) cannot be null or empty");
            }
            if (serviceDescriptionEndpoint == null || serviceDescriptionEndpoint.trim().equals("")) {
                throw new IllegalArgumentException("Remote server service description endpoint (producer URL) cannot be null or empty");
            }

            this.title = title;
            this.description = description;
            this.friendlyID = friendlyID;
            this.serviceDescriptionEndpoint = serviceDescriptionEndpoint;
        }

        /**
         * @return title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @return the description of remote portlet server
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the friendlyID of remote portlet server
         */
        public String getFriendlyID() {
            return friendlyID;
        }

        /**
         * @return serviceDescriptionEndpoint the serviceDescriptionEndpoint of remote portlet server
         */
        public String getServiceDescriptionEndpoint() {
            return serviceDescriptionEndpoint;
        }

        @Override
        public String toString() {
            return (" title = [" + title + "] ; friendlyID = [" + friendlyID + "] ; description =[" + description
                    + "] ; serviceDescriptionEndpoint = [" + serviceDescriptionEndpoint + "]");
        }
    }

}
