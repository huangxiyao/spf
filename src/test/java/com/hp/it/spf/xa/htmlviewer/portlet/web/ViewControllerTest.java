/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.htmlviewer.portlet.web;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import javax.portlet.PortletPreferences;
import junit.framework.TestCase;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.htmlviewer.portlet.web.ViewController;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;

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
	 * @throws Exception
	 *             the exception
	 */
	public void testGetRelativeHtmlName() throws Exception {
		MockRenderRequest renderRequest = new MockRenderRequest();
		PortletPreferences pp = renderRequest.getPreferences();
		pp.setValue(Consts.VIEW_FILENAME, "test.html");
		assertEquals("test.html", viewController
				.getFilename(renderRequest));
	}

	/**
	 * Test execute.
	 * 
	 * @throws Exception
	 *             the exception
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
	 * @throws Exception
	 *             the exception
	 */
	public void testHandleRenderRequestInternal() throws Exception {
		MockRenderRequest renderRequest = new MockRenderRequest();
		MockRenderResponse renderResponse = new MockRenderResponse();
		renderRequest.addPreferredLocale(new Locale("zh", "CN"));
		PortletPreferences pp = renderRequest.getPreferences();
		pp.setValue(Consts.VIEW_FILENAME, "/html/test_basic.html");
		ModelAndView modelAndView = (ModelAndView) viewController
				.handleRenderRequest(renderRequest, renderResponse);
		assertEquals("view", modelAndView.getViewName());
		Map map = modelAndView.getModel();
		String content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.1 got: " + content);
		assertEquals(
				"<html><head><title>Hello world (Chinese)!</title></head><body><h1>Hello world (Chinese)!</h1><p>Language: zh-CN</p><p>A token: Hello world!</p><p>Upper language: ZH<br>Lower country: cn</p></body></html>",
				content);

		pp.setValue(Consts.VIEW_FILENAME, "/html/test_url_1.html");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.2 got: " + content);
		assertEquals(
				"<html><head><title>Hello world (Chinese)!</title></head><body><h1>Hello world (Chinese)!</h1> Here is an image tag: <img src=\"/images/test_zh.gif\"><br>Here it is again, with the URL encoded: <img src=\"%2Fimages%2Ftest_zh.gif\"></body></html>",
				content);

		pp.setValue(Consts.VIEW_FILENAME, "test_url_2.html");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.3 got: " + content);
		assertEquals(
				"<html><head><title>Hello world!</title></head><body><h1>Hello world!</h1> Here is an image tag: <img src=\"/images/nonexistent.gif\"><br>Here is the site URL: <br>Here is a site page URL: test/template.PAGE<br></body></html>",
				content);

		pp.setValue(Consts.VIEW_FILENAME, "test_login_container.html");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.4 got: " + content);
		assertEquals(
				"<html><body>Everybody sees this part.  Only logged-out users see this.</body></html>",
				content);

		pp.setValue(Consts.VIEW_FILENAME, "test_nested_token.html");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.5 got: " + content);
		assertEquals(
				"<html><body>Template ID: FRIENDLY_ID<br>Portal URL: /template.FRIENDLY_ID?lang=zh&cc=CN<br></body></html>",
				content);

		pp.setValue(Consts.VIEW_FILENAME, "test_basic.html");
		pp.setValue(Consts.INCLUDES_FILENAME, "test_includes.properties");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.6 got: " + content);
		assertEquals(
				"<html><head><title>Hello world (Chinese)!</title></head><body><h1>Hello world (Chinese)!</h1><p>Language: zh-CN</p><p>A token: Did we find this (Chinese)?</p><p>Upper language: ZH<br>Lower country: cn</p></body></html>",
				content);

		pp.setValue(Consts.INCLUDES_FILENAME,
				"/properties/test_includes_2.properties");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.7 got: " + content);
		assertEquals(
				"<html><head><title>Hello world (Chinese)!</title></head><body><h1>Hello world (Chinese)!</h1><p>Language: zh-CN</p><p>A token: What about this (Chinese)?</p><p>Upper language: ZH<br>Lower country: cn</p></body></html>",
				content);

		pp.setValue(Consts.VIEW_FILENAME, "test_nested_token_2.html");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.8 got: " + content);
		assertEquals(
				"<html><body>Template ID: CHINESE_FRIENDLY_ID<br>Portal URL: /template.CHINA_FRIENDLY_ID<br></body></html>",
				content);

		pp.setValue(Consts.VIEW_FILENAME, "test_nested_token_3.html");
		modelAndView = (ModelAndView) viewController.handleRenderRequest(
				renderRequest, renderResponse);
		map = modelAndView.getModel();
		content = (String) map.get(Consts.VIEW_CONTENT);
		System.out.println("testHandleRenderRequestInternal.9 got: " + content);
		assertEquals(
				"<html><body>Template ID: CHINESE_FRIENDLY_ID</body></html>",
				content);

		if (fileExists("/opt/sasuapps/sp/global_resources/portlet/i18n/html/test_loc_content_url_ext_zh_CN.html")
				&& fileExists("/opt/sasuapps/sp/global_resources/portlet/i18n/images/test_ext_zh_CN.gif")) {
			pp.setValue(Consts.VIEW_FILENAME, "test_loc_content_url_ext.html");
			modelAndView = (ModelAndView) viewController.handleRenderRequest(
					renderRequest, renderResponse);
			map = modelAndView.getModel();
			content = (String) map.get(Consts.VIEW_CONTENT);
			System.out.println("testHandleRenderRequestInternal.10 got: "
					+ content);
			assertEquals(
					"<html><head><title>Hello world (Chinese)!</title></head><body><h1>Hello world (Chinese)!</h1> Here is an image tag: <img src=\"/relay/images/test_ext_zh_CN.gif\"></body></html>",
					content);
		}
	}

	protected static boolean fileExists(String pPath) {
		boolean result = false;
		if (pPath == null) {
			pPath = "";
		}
		pPath = pPath.trim();
		try {
			File file = new File(pPath);
			if (file.exists()) {
				result = true;
			}
		} catch (Exception e) {
			// Exception (eg file not found) - ignore and return false.
		}
		return result;
	}
}