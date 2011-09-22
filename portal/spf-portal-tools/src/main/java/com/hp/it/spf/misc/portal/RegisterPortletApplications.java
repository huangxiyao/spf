package com.hp.it.spf.misc.portal;

import com.vignette.portal.portlet.management.external.PortletPersistenceException;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletApplicationManagerSpiImpl;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletTypeManagerSpiImpl;
import com.vignette.portal.portlet.management.internal.implementation.provider.jsr.JsrPortletTypeSpiImpl;
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
import java.util.HashSet;
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
 */
public class RegisterPortletApplications
{

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
                void createPortletApplication(LocalPortletApplicationBean application) throws PortletPersistenceException
                {
                    super.createPortletApplication(application);
                    System.out.printf("Successfully registered portlet application: name=%s, context root=%s, portlets=%s%n",
                            application.getName(), application.getContextRoot(), application.getTypeList());
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
            createPortletApplication(application);
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

    void createPortletApplication(LocalPortletApplicationBean application) throws PortletPersistenceException
    {
        Set<String> portletTypeIDs = createApplicationPortletTypes(application);
        JsrPortletApplicationManagerSpiImpl actionManager = JsrPortletApplicationManagerSpiImpl.getInstance();
        actionManager.createPortletApplication(application.getContextRoot(), application.getName(), application.getDescription(), portletTypeIDs, null);
    }

    private Set<String> createApplicationPortletTypes(LocalPortletApplicationBean application) throws PortletPersistenceException
    {
        List<String> typeNames = application.getTypeList();

        Set<String> locales = new HashSet<String>();
        locales.add("English");

        JsrPortletTypeManagerSpiImpl typeManager = JsrPortletTypeManagerSpiImpl.getInstance();

        Set<String> portletTypeIDs = new HashSet<String>();
        for (String typeName : typeNames) {
            JsrPortletTypeSpiImpl type = typeManager.createJsrPortletType(application.getName(), typeName, typeName, "", null, false, null, locales);
            portletTypeIDs.add(type.getID());
        }

        return portletTypeIDs;
    }


    static class LocalPortletApplicationBean
    {
        private String name;
        private String description;
        private String contextRoot;
        private List<String> typeList;

        public LocalPortletApplicationBean(String name, String description, String contextRoot,
                                           List<String> typeList)
        {
            if (name == null || name.trim().equals("")) {
                throw new IllegalArgumentException("Portlet application name cannot be null or empty");
            }
            if (contextRoot == null || contextRoot.trim().equals("")) {
                throw new IllegalArgumentException("Portlet application contextRoot cannot be null or empty");
            }
            if (typeList == null || typeList.isEmpty()) {
                throw new IllegalArgumentException("Portlet application's portlet list cannot be empty");
            }
            for (String type : typeList) {
                if (type == null || type.trim().equals("")) {
                    throw new IllegalArgumentException("Portlet name cannot be null or empty");
                }
            }

            this.name = name;
            this.description = description;
            this.contextRoot = contextRoot;
            this.typeList = typeList;
        }

        public String getName()
        {
            return name;
        }

        public String getDescription()
        {
            return description;
        }

        public String getContextRoot()
        {
            return contextRoot;
        }

        public List<String> getTypeList()
        {
            return typeList;
        }

        @Override
        public String toString()
        {
            return (" name = [" + name + "] ; contextRoot = [" + contextRoot + "] ; description =[" + description
                    + "] ; typeList = [" + typeList + "]");
        }
    }
}
