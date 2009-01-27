/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.htmlviewer.portlet.web;

import java.util.Locale;
import java.util.Map;
import javax.portlet.PortletPreferences;
import junit.framework.TestCase;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.htmlviewer.portlet.web.ViewController;

/**
 * The Class ViewControllerTest.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @version 1.0 2007-04-06
 * @see others
 */
public class ViewControllerTest extends TestCase {

    private ViewController viewController;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        viewController = new ViewController();
        viewController.setViewName("view");
    }

    /**
     * Test get relative html name.
     * 
     * @throws Exception the exception
     */
    public void testGetRelativeHtmlName() throws Exception {
        MockRenderRequest renderRequest = new MockRenderRequest();
        PortletPreferences pp = renderRequest.getPreferences();
        pp.setValue(Consts.VIEW_FILENAME, "view.htm");
        assertEquals(viewController.getFilename(renderRequest),
                "/html/view.htm");
    }

    /**
     * Test execute.
     * 
     * @throws Exception the exception
     */
    public void testExecute() throws Exception {
        MockRenderRequest renderRequest = new MockRenderRequest();
        MockRenderResponse renderResponse = new MockRenderResponse();
        ModelAndView modelAndView = (ModelAndView) viewController.execute(
                renderRequest, renderResponse, "file content");
        assertEquals("view", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        String content = (String) map.get(Consts.VIEW_CONTENT);
        assertEquals("file content", content);
    }

    /**
     * Test handle render request internal.
     * 
     * @throws Exception the exception
     */
    public void testHandleRenderRequestInternal() throws Exception {
        MockRenderRequest renderRequest = new MockRenderRequest();
        MockRenderResponse renderResponse = new MockRenderResponse();
        renderRequest.addPreferredLocale(new Locale("zh", "CN"));
        PortletPreferences pp = renderRequest.getPreferences();
        pp.setValue(Consts.VIEW_FILENAME, "test.html");
        ModelAndView modelAndView = (ModelAndView) viewController
                .handleRenderRequest(renderRequest, renderResponse);
        assertEquals("view", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        String content = (String) map.get(Consts.VIEW_CONTENT);
        System.out.println(content);
        assertEquals("<html><head><title>Hello world (Chinese)!</title></head><body><h1>Hello world (Chinese)!</h1></body></html>",
                content);
    }
}