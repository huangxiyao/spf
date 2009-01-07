/**
 * Project: Service Portal
 * Copyright (c) 2007 HP. All Rights Reserved
 * <Some comments of this file>
 * 
 * TODO add log
 */
package com.hp.it.spf.xa.htmlviewer.portlet.web;

import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockActionResponse;
import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.htmlviewer.portlet.web.ConfigController;

import javax.portlet.PortletPreferences;
import junit.framework.TestCase;

/**
 * The Class ConfigControllerTest.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @version 1.0 2007-04-06
 * @see others
 */
public class ConfigControllerTest extends TestCase {

    private ConfigController viewController;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {        
        viewController = new ConfigController();
        viewController.setViewName("config");
    }

    /**
     * Test handle render request internal.
     * 
     * @throws Exception the exception
     */
    public void testHandleRenderRequestInternal() throws Exception {
        MockRenderRequest renderRequest = new MockRenderRequest();
        MockRenderResponse renderResponse = new MockRenderResponse();
        PortletPreferences pp = renderRequest.getPreferences();
        pp.setValue(Consts.VIEW_FILENAME, "view.htm");
        ModelAndView modelAndView = (ModelAndView) viewController
                .handleRenderRequest(renderRequest, renderResponse);
        assertEquals(modelAndView.getViewName(), "config");
        assertEquals(pp.getValue(Consts.VIEW_FILENAME, ""), "view.htm");
     }

    /**
     * Test handle action request internal.
     * 
     * @throws Exception the exception
     */
    public void testHandleActionRequestInternal() throws Exception {
        MockActionRequest actionRequest = new MockActionRequest();
        MockActionResponse actionResponse = new MockActionResponse();
        PortletPreferences pp = actionRequest.getPreferences();
        actionRequest.addParameter(Consts.VIEW_FILENAME, "1.htm");
        viewController.handleActionRequestInternal(actionRequest,
                actionResponse);
        assertEquals(pp.getValue(Consts.VIEW_FILENAME, ""), "1.htm");
    }
}