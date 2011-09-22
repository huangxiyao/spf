package com.hp.it.spf.misc.portal;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class RegisterPortletApplicationsTest
{
    @Test
    public void getLocalPortletApplicationDefsCanUnmarshallObjects() throws Exception {
        InputSource src = new InputSource(new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<portlet-applications>\n" +
                "    <portlet-application>\n" +
                "        <name>HTMLViewer    </name>\n" +
                "        <contextRoot>htmlviewer</contextRoot>\n" +
                "        <description>This is a WPAP/Spring portlet application which contains the Shared Portal Framework HTMLViewer portlet.</description>\n" +
                "        <portlets>\n" +
                "            <portlet-name>\n" +
                "                HTMLViewerPortlet\n" +
                "            </portlet-name>\n" +
                "        </portlets>\n" +
                "    </portlet-application>\n" +
                "</portlet-applications>"
        ));

        Document portletAppsDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);
        RegisterPortletApplications rpa = new RegisterPortletApplications();
        List<RegisterPortletApplications.LocalPortletApplicationBean> portletApplications =
                rpa.getLocalPortletApplicationDefs(portletAppsDoc);

        assertThat(portletApplications, is(notNullValue()));
        assertThat(portletApplications.size(), is(1));

        RegisterPortletApplications.LocalPortletApplicationBean portletApplication = portletApplications.get(0);

        assertThat(portletApplication.getName(), is("HTMLViewer"));
        assertThat(portletApplication.getContextRoot(), is("htmlviewer"));
        assertThat(portletApplication.getDescription(), is("This is a WPAP/Spring portlet application which contains the Shared Portal Framework HTMLViewer portlet."));
        assertThat(portletApplication.getTypeList(), is(notNullValue()));
        assertThat(portletApplication.getTypeList().size(), is(1));
        assertThat(portletApplication.getTypeList().get(0), is("HTMLViewerPortlet"));
    }
}
