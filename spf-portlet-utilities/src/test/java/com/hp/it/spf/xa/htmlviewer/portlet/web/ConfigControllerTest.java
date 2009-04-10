/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
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
import java.util.Map;
import java.util.ArrayList;

/**
 * The Class ConfigControllerTest.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @version 1.0 2007-04-06
 * @see others
 */
public class ConfigControllerTest extends TestCase {

	private ConfigController configController;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		configController = new ConfigController();
		configController.setViewName("config");
	}

	/**
	 * Test handle render request internal.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void testHandleRenderRequestInternal() throws Exception {
		MockRenderRequest renderRequest = new MockRenderRequest();
		MockRenderResponse renderResponse = new MockRenderResponse();
		PortletPreferences pp = renderRequest.getPreferences();
		pp.setValue(Consts.VIEW_FILENAME, "view.htm");
		pp.setValue(Consts.LAUNCH_BUTTONLESS, "true");
		ModelAndView modelAndView = (ModelAndView) configController
				.handleRenderRequest(renderRequest, renderResponse);
		assertEquals("config", modelAndView.getViewName());
		assertEquals("view.htm", pp.getValue(Consts.VIEW_FILENAME, ""));
		assertEquals("true", pp.getValue(Consts.LAUNCH_BUTTONLESS, ""));
	}

	/**
	 * Test handle action request internal.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public void testHandleActionRequestInternal() throws Exception {
		MockActionRequest actionRequest = new MockActionRequest();
		MockActionResponse actionResponse = new MockActionResponse();
		PortletPreferences pp = actionRequest.getPreferences();
		actionRequest.addParameter(Consts.VIEW_FILENAME, "file.htm");
		actionRequest.addParameter(Consts.INCLUDES_FILENAME, "includes.properties");
		actionRequest.addParameter(Consts.LAUNCH_BUTTONLESS,
				Consts.LAUNCH_BUTTONLESS);
		configController.handleActionRequestInternal(actionRequest,
				actionResponse);
		assertEquals("file.htm", pp.getValue(Consts.VIEW_FILENAME, ""));
		assertEquals("includes.properties", pp.getValue(Consts.INCLUDES_FILENAME, ""));
		assertEquals("true", pp.getValue(Consts.LAUNCH_BUTTONLESS, ""));
		assertEquals(Consts.INFO_CODE_PREFS_SAVED, actionResponse.getRenderParameter(Consts.INFO_CODE));
		MockRenderRequest renderRequest = new MockRenderRequest();
		MockRenderResponse renderResponse = new MockRenderResponse();
		renderRequest.setParameter(Consts.INFO_CODE, Consts.INFO_CODE_PREFS_SAVED);
		ModelAndView model = (ModelAndView) configController.handleRenderRequest(
				renderRequest, renderResponse);
		Map map = model.getModel();
		ArrayList<String> infoMsgs = (ArrayList<String>) map.get(Consts.INFO_MESSAGES);
		assertEquals(1, infoMsgs.size());
		assertEquals(Consts.INFO_CODE_PREFS_SAVED, infoMsgs.get(0));
		
		actionRequest = new MockActionRequest();
		actionResponse = new MockActionResponse();
		actionRequest.setParameter(Consts.VIEW_FILENAME, "");
		configController.handleActionRequestInternal(actionRequest,
				actionResponse);
		assertEquals(Consts.ERROR_CODE_VIEW_FILENAME_NULL, actionResponse.getRenderParameter(Consts.ERROR_CODE));
		renderRequest = new MockRenderRequest();
		renderResponse = new MockRenderResponse();
		renderRequest.setParameter(Consts.ERROR_CODE, Consts.ERROR_CODE_VIEW_FILENAME_NULL);
		model = (ModelAndView) configController.handleRenderRequest(
				renderRequest, renderResponse);
		map = model.getModel();
		ArrayList<String> errMsgs = (ArrayList<String>) map.get(Consts.ERROR_MESSAGES);
		assertEquals(1, errMsgs.size());
		assertEquals(Consts.ERROR_CODE_VIEW_FILENAME_NULL, errMsgs.get(0));

		actionRequest = new MockActionRequest();
		actionResponse = new MockActionResponse();
		actionRequest.setParameter(Consts.VIEW_FILENAME, "/some/../invalid/path");
		configController.handleActionRequestInternal(actionRequest,
				actionResponse);
		assertEquals(Consts.ERROR_CODE_VIEW_FILENAME_PATH, actionResponse.getRenderParameter(Consts.ERROR_CODE));
		renderRequest = new MockRenderRequest();
		renderResponse = new MockRenderResponse();
		renderRequest.setParameter(Consts.ERROR_CODE, Consts.ERROR_CODE_VIEW_FILENAME_PATH);
		model = (ModelAndView) configController.handleRenderRequest(
				renderRequest, renderResponse);
		map = model.getModel();
		errMsgs = (ArrayList<String>) map.get(Consts.ERROR_MESSAGES);
		assertEquals(2, errMsgs.size());
		assertEquals(Consts.ERROR_CODE_VIEW_FILENAME_PATH, errMsgs.get(0));
		assertEquals(Consts.ERROR_CODE_VIEW_FILENAME_NULL, errMsgs.get(1));
		
		actionRequest = new MockActionRequest();
		actionResponse = new MockActionResponse();
		actionRequest.setParameter(Consts.VIEW_FILENAME, "/some/non-existent/file");
		actionRequest.setParameter(Consts.INCLUDES_FILENAME, "/some/non-existent/properties");
		configController.handleActionRequestInternal(actionRequest,
				actionResponse);
		assertEquals(null, actionResponse.getRenderParameter(Consts.ERROR_CODE));
		assertEquals(null, actionResponse.getRenderParameter(Consts.WARN_CODE));
		assertEquals(Consts.INFO_CODE_PREFS_SAVED, actionResponse.getRenderParameter(Consts.INFO_CODE));
		renderRequest = new MockRenderRequest();
		renderResponse = new MockRenderResponse();
		renderRequest.setPreferences(actionRequest.getPreferences());
		model = (ModelAndView) configController.handleRenderRequest(renderRequest, renderResponse);
		map = model.getModel();
		errMsgs = (ArrayList<String>) map.get(Consts.ERROR_MESSAGES);
		assertEquals(null, errMsgs);
		ArrayList<String> warnMsgs = (ArrayList<String>) map.get(Consts.WARN_MESSAGES);
		assertEquals(2, warnMsgs.size());
		assertEquals(Consts.WARN_CODE_INCLUDES_FILE_NULL, warnMsgs.get(1));
		assertEquals(Consts.WARN_CODE_VIEW_FILE_NULL, warnMsgs.get(0));
		infoMsgs = (ArrayList<String>) map.get(Consts.INFO_MESSAGES);
		assertEquals(null, infoMsgs);
	}
}