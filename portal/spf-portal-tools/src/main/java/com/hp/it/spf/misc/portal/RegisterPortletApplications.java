package com.hp.it.spf.misc.portal;

import com.vignette.portal.portlet.management.external.PortletPersistenceException;
import com.vignette.portal.portlet.management.external.PortletResourceNotFoundException;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletApplicationManagerSpiImpl;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletApplicationSpiImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Utility class used to register, in Vignette Portal, portlet applications described in XML
 * document. This command line utility allows pre-registration in portal database before
 * the actual application has been deployed into the server container.
 *
 * <p>The sample XML document containing registration data looks as follows:</p>
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;portlet-applications&gt;
 *     &lt;portlet-application&gt;
 *         &lt;name&gt;HTMLViewer&lt;/name&gt;
 *         &lt;contextRoot&gt;htmlviewer&lt;/contextRoot&gt;
 *         &lt;description&gt;This is a WPAP/Spring portlet application which contains the Shared Portal Framework HTMLViewer portlet.&lt;/description&gt;
 *         &lt;portlets&gt;
 *             &lt;portlet-name&gt;HTMLViewerPortlet&lt;/portlet-name&gt;
 *         &lt;/portlets&gt;
 *     &lt;/portlet-application&gt;
 * &lt;/portlet-applications&gt;
 * </pre>
 *
 * @author Liu, Ye (HPIT-GADSC) (ye.liu@hp.com)
 * @author Xu, Ke-Jun (Daniel,HPIT-GADSC) (ke-jun.xu@hp.com)
 */
public class RegisterPortletApplications
{
    private PortletTypeHelper portletTypeHelper = new PortletTypeHelper();

    public static void main(String[] args)
    {
        if (args.length == 0) {
            System.out.printf("java [options] %s %s%n",
                    RegisterPortletApplications.class.getName(),
                    "path/to/spf_local_portlet_applications.xml");
            System.exit(0);
        }

        File portletAppFile = new File(args[0]);
        if (!portletAppFile.isFile()) {
            System.err.printf("Data file not found: %s%n", portletAppFile.getAbsolutePath());
            System.exit(1);
        }

        Document portletAppData = null;
        try {
            portletAppData = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(portletAppFile);
        }
        catch (Exception e) {
            System.err.printf("Error occurred while parsing data file '%s': %s%n",
                    portletAppFile.getAbsolutePath(), e);
            System.exit(2);
        }

        try {
            RegisterPortletApplications tool = new RegisterPortletApplications() {
                // override createPortletApplication method so we can print diagnostic information
                // without tying the actual method implementation to use System.out which is ok
                // for command line usage as here.

                @Override
                boolean createOrUpdatePortletApplication(LocalPortletApplicationBean application) throws PortletPersistenceException
                {
                    boolean portletApplicationCreated = super.createOrUpdatePortletApplication(application);
                    System.out.printf("Successfully %s portlet application: name=%s, context root=%s, portlets=%s%n",
                            (portletApplicationCreated ? "registered" : "updated"),
                            application.getName(),
                            application.getContextRoot(),
                            application.getTypeList());
                    return portletApplicationCreated;
                }
            };
            tool.registerPortletApplications(portletAppData);
        }
        catch (Exception e) {
            System.err.printf("Error occurred while registering portlet applications: %s%n", e);
            e.printStackTrace();
            System.exit(3);
        }

    }

    public void registerPortletApplications(Document portletAppData) throws Exception
    {
        List<LocalPortletApplicationBean> portletApplications = getLocalPortletApplicationDefs(portletAppData);
        for (LocalPortletApplicationBean application : portletApplications) {
            createOrUpdatePortletApplication(application);
        }
    }

    List<LocalPortletApplicationBean> getLocalPortletApplicationDefs(Document portletApplicationsData)
    {
        portletApplicationsData.getDocumentElement().normalize();

        List<LocalPortletApplicationBean> list = new ArrayList<LocalPortletApplicationBean>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList applicationList =
                    (NodeList) xpath.evaluate("/portlet-applications/portlet-application",
                            portletApplicationsData, XPathConstants.NODESET);
            for (int s = 0, lenS = applicationList.getLength(); s < lenS; s++) {
                Node applicationNode = applicationList.item(s);

                String name = xpath.evaluate("name", applicationNode).trim();
                String contextRoot = xpath.evaluate("contextRoot", applicationNode).trim();
                String description = xpath.evaluate("description", applicationNode).trim();

                List<String> portletTypeNames = new ArrayList<String>();
                NodeList typeNodes =
                        (NodeList) xpath.evaluate("portlets/portlet-name",
                                applicationNode, XPathConstants.NODESET);

                for (int i = 0, lenI = typeNodes.getLength(); i < lenI; i++) {
                    Node typeNode = typeNodes.item(i);
                    String type = typeNode.getTextContent().trim();
                    portletTypeNames.add(type);
                }

                LocalPortletApplicationBean bean = new LocalPortletApplicationBean(name, description, contextRoot, portletTypeNames);
                list.add(bean);
            }
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Error reading portlet application data", e);
        }

        return list;
    }

    boolean createOrUpdatePortletApplication(LocalPortletApplicationBean application) throws PortletPersistenceException
    {
        try {
            updatePortletApplication(application);
            return false;
        }
        catch (PortletResourceNotFoundException e) {
            createPortletApplication(application);
            return true;
        }
    }

    private void updatePortletApplication(LocalPortletApplicationBean application) throws PortletResourceNotFoundException, PortletPersistenceException
    {
        final int ID_TYPE_CONTEXT_ROOT = 1;

        JsrPortletApplicationSpiImpl existingPortletApp =
                JsrPortletApplicationSpiImpl.getJsrPortletApplicationByID(
                        application.getContextRoot(), ID_TYPE_CONTEXT_ROOT);

        portletTypeHelper.createMissingPortletTypes(existingPortletApp, application);
        portletTypeHelper.deleteObsoletePortletTypes(existingPortletApp, application);

        if (! application.getDescription().equals(existingPortletApp.getDescription())) {
            existingPortletApp.setDescription(application.getDescription());
        }

        existingPortletApp.save();
    }

    private void createPortletApplication(LocalPortletApplicationBean application) throws PortletPersistenceException
    {
        Set<String> portletTypeIDs = portletTypeHelper.createApplicationPortletTypes(application.getName(), application.getTypeList());
        JsrPortletApplicationManagerSpiImpl actionManager = JsrPortletApplicationManagerSpiImpl.getInstance();
        actionManager.createPortletApplication(application.getContextRoot(), application.getName(), application.getDescription(), portletTypeIDs, null);
    }
}
