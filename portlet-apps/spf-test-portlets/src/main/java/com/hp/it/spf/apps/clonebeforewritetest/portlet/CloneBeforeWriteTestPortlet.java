package com.hp.it.spf.apps.clonebeforewritetest.portlet;


import javax.portlet.GenericPortlet;
import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.ActionRequest;
import javax.portlet.ResourceURL;
import javax.portlet.ProcessAction;
import javax.portlet.ActionResponse;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.PortletException;
import javax.xml.namespace.QName;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Portlet used to test if the cloning occurs when the portlet stores the preferences. Only the methods 
 * processAction, handleEvents and getResource are allowed to store preferences. The most reliable way 
 * to check if the portlet was cloned is to analyze the WSRP traces. Upon the cloning (i.e. when portletStateChange
 * value is cloneBeforeWrite in the calls of the methods above) a new portlet handle is created
 * by the producer and returned back to the consumer. From now on the consumer should use
 * this clone portlet handle when referencing this portlet instance.
 * <p>
 * <b>BUG:</b> Vignette does not send "cloneBeforeWrite" with handleEvents calls
 * <p>
 * <b>BUG:</b> OpenPortal producer does not encode resourceID in the resource URLs
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class CloneBeforeWriteTestPortlet extends GenericPortlet {

    @RenderMode(name = "view")
    public void show(RenderRequest request, RenderResponse response) throws IOException {
        PortletPreferences prefs = request.getPreferences();
        PrintWriter out = response.getWriter();

        PortletURL actionUrl = response.createActionURL();
        actionUrl.setParameter(ActionRequest.ACTION_NAME, "performBlockingInteraction");
        out.printf(
                "<div>" +
                        "<h1>performBlockingInteraction section</h1>" +
                        "value: %s" +
                        "<form method='POST' action='%s'>" +
                        "<input type='submit' value='update' />" +
                        "</form>" +
                "</div>%n",
                prefs.getValue("performBlockingInteraction.value", "<i>undefined</i>"),
                actionUrl);

        PortletURL eventUrl = response.createActionURL();
        eventUrl.setParameter(ActionRequest.ACTION_NAME, "handleEvents");
        out.printf(
                "<div>" +
                        "<h1>handleEvents section</h1>" +
                        "<form method='POST' action='%s'>" +
                        "<input type='submit' value='update' />" +
                        "</form>" +
                "</div>%n",
                eventUrl);

        ResourceURL resourceUrl = response.createResourceURL();
        resourceUrl.setResourceID("getResource");
        out.printf(
                "<div>" +
                        "<h1>getResource section</h1>" +
                        "value: %s" +
                        "<br /><input type='button' value='update' onclick='location.href=\"%s\"' />" +
                "</div>%n",
                prefs.getValue("getResource.value", "<i>undefined</i>"),
                resourceUrl);
    }

    @ProcessAction(name = "performBlockingInteraction")
    public void performBlockingInteractionAction(ActionRequest request, ActionResponse response) throws ReadOnlyException, ValidatorException, IOException {
        PortletPreferences prefs = request.getPreferences();
        prefs.setValue("performBlockingInteraction.value", new Date().toString());
        prefs.store();
    }

    @ProcessAction(name = "handleEvents")
    public void handleEventsAction(ActionRequest request, ActionResponse response) {
//        response.setEvent("updatePreference", Boolean.TRUE);
        response.setEvent(new QName("com.hp.it.spf.apps.clonebeforewritetestportlet", "updatePreference"), Boolean.TRUE);
    }

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        if ("getResource".equals(request.getResourceID())) {
            PortletPreferences prefs = request.getPreferences();
            prefs.setValue("getResource.value", new Date().toString());
            prefs.store();

            response.setContentType("text/plain");
            response.getWriter().println("Response from serveResource");
        }
    }
}
