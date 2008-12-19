/**
 * Project: Service Portal
 * Copyright (c) 2007 HP. All Rights Reserved
 * <Some comments of this file>
 * 
 * TODO add log
 */
package com.hp.it.spf.htmlviewer.portlet.web;

import java.util.Locale;
import javax.portlet.PortletPreferences;
import junit.framework.TestCase;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.htmlviewer.portlet.web.ViewController;

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
        assertEquals(modelAndView.getViewName(), "view");
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
        pp.setValue(Consts.VIEW_FILENAME, "1.htm");
        ModelAndView modelAndView = (ModelAndView) viewController
                .handleRenderRequest(renderRequest, renderResponse);
        assertEquals(modelAndView.getViewName(), "view");
        System.out.println(renderRequest.getAttribute(Consts.VIEW_CONTENT));
        assertEquals(
                renderRequest.getAttribute(Consts.VIEW_CONTENT),
                "<a href=\"http://www.sina.com.cn?lang=zh\">"
                        + "go to OVSC</a><br><a href=\"http://localhost/mockportlet?urlType=render;"
                        + "portletMode=help\">go to help mode </a><br><img src=\"/relay//images/1_zh_CN.jpg\">"
                        + "<img src=\"/relay//images/2.jpg\">");
    }
}