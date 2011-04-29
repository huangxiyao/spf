package com.hp.it.spf.apps.clonebeforewritetest.portlet;

import javax.portlet.GenericPortlet;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.ProcessEvent;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class CloneBeforeWriteEventReceiverPortlet extends GenericPortlet {
    
    @RenderMode(name = "view")
    public void show(RenderRequest request, RenderResponse response) throws IOException {
        PortletPreferences prefs = request.getPreferences();
        PrintWriter out = response.getWriter();

        out.printf(
                "<div>" +
                        "<h1>handleEvents section (receiver)</h1>" +
                        "value: %s" +
                "</div>%n",
                prefs.getValue("handleEvents.value", "<i>undefined</i>"));

    }

//    @ProcessEvent(name = "updatePreference")
    @ProcessEvent(qname = "{com.hp.it.spf.apps.clonebeforewritetestportlet}updatePreference")
    public void updatePreference(EventRequest request, EventResponse response) throws ReadOnlyException, ValidatorException, IOException {
        PortletPreferences prefs = request.getPreferences();
        prefs.setValue("handleEvents.value", new Date().toString());
        prefs.store();
    }


}
