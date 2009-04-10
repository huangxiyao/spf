package com.hp.it.spf.xa.portalurl.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.lang.reflect.Field;

import com.hp.it.spf.xa.portalurl.PortalURL;
import com.hp.it.spf.xa.portalurl.PortalURLFactory;

import javax.portlet.WindowState;
import javax.portlet.PortletMode;

/**
 * Generates a page allowing to test {@link PortalURL} API.
 * In order to use this page some setup is required. Here are the steps:
 * <ol>
 * <li>Create a test portlet which is: showing its render parameters, has one public render
 * parameter called "pubRenderParam" and has an action which has an action which takes 2 parameters
 * "paramName" and "paramValue" and as the result of the action sets a render parameter with the
 * provided name (value of paramName) and value (value of paramValue)</li>
 * <li>Deploy this portlet as Vignette local portlet application</li>
 * <li>Deploy this portlet as a remote portlet in OpenPortal producer and register it in Vignette</li>
 * <li>Create a test page and map it to Vignette friendly URL "portal-url-test"</li>
 * <li>On the portal-url-test create 3 instances of local test portlet and name (portlet friendly ID)
 * them respectively: portalurltestportlet1, portalurltestportlet2, portalurltestportlet3</li>
 * <li>Create a test page and map it to Vignette friendly URL "portal-url-test-remote"</li>
 * <li>On the portal-url-test-remote create 3 instances of remote test portlet and name them
 * (portlet friendly ID) respectively: portalurltestportlet1remote, portalurltestportlet2remote,
 * portalurltestportlet3remote</li>
 * <li>Call this class passing in the URL of your test site, e.g. http://myhost:1234/portal/site/mysite/</li>
 * </ol>
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class GenerateTestPage
{
	private static final String[] LOCAL_PORTLET_IDS = {
			"portalurltestportlet1", "portalurltestportlet2", "portalurltestportlet3"
	};
	private static final String[] REMOTE_PORTLET_IDS = {
			"portalurltestportlet1remote", "portalurltestportlet2remote", "portalurltestportlet3remote"
	};
	private static final String LOCAL_PORTLETS_PAGE = "portal-url-test";
	private static final String REMOTE_PORTLETS_PAGE = "portal-url-test-remote";


	public static void main(String[] args) throws IOException
	{
		final String siteRootUrl = (args.length > 0 ? args[0] : "http://localhost:9001/portal/site/spf/");

		File file = new File("test.html");
		PrintWriter out = new PrintWriter(new FileWriter(file));
		try {
			out.println("<html>");
			out.println("<body>");
			out.println("<table border='0'><tr>");
			out.println("<th>Local Portlets' URLs</th>");
			out.println("<th>Remote Portlets' URLs</th>");
			out.println("</tr><tr><td>");
			setupFactoryCreatedUrlsAsRemote(false);
			generateTestHtml(siteRootUrl, LOCAL_PORTLETS_PAGE, LOCAL_PORTLET_IDS, out);
			out.println("</td><td>");
			setupFactoryCreatedUrlsAsRemote(true);
			generateTestHtml(siteRootUrl, REMOTE_PORTLETS_PAGE, REMOTE_PORTLET_IDS, out);
			out.println("</td></tr></table>");
			out.println("</body>");
			out.println("</html>");
		}
		finally {
			out.close();
			System.out.printf("Test file created on %s: %s%n", new Date(), file.getAbsolutePath());
		}
	}

	private static void setupFactoryCreatedUrlsAsRemote(boolean isRemote) {
		try {
			Field f = PortalURLFactory.class.getDeclaredField("mCreatesRemoteUrls");
			f.setAccessible(true);
			f.setBoolean(null, isRemote);
		}
		catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static void generateTestHtml(String siteRootUrl, String pageFriendlyId, String[] portletIds, PrintWriter out) {
		out.println("<ul>");

		out.printf("<li><a href='%s'>Default page url</a></li>%n",
				PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId));
		out.printf("<li><a href='%s'>Pass params to a single portlet</a></li>%n",
				passParamsToSinglePortlet(siteRootUrl, pageFriendlyId, portletIds));
		out.printf("<li><a href='%s'>Pass params to 3 portlets</a></li>%n",
				passParamsTo3Portlets(siteRootUrl, pageFriendlyId, portletIds));
		out.printf("<li><a href='%s'>Pass clashing params to 3 portlets</a></li>%n",
				passClashingParamsTo3Portlets(siteRootUrl, pageFriendlyId, portletIds));
		out.printf("<li><a href='%s'>Pass public params to 3 portlets</a></li>%n",
				passPublicParamsTo3Portlets(siteRootUrl, pageFriendlyId, portletIds));
		out.printf("<li><form method='POST' action='%s'><input type='hidden' name='paramName' value='yyy' /><input name='paramValue' value='zzz' /><input type='submit' /></form></li>%n",
				actionUrlToPortlet(siteRootUrl, pageFriendlyId, portletIds));
		out.printf("<li><form method='POST' action='%s'><input type='hidden' name='paramName' value='yyy' /><input name='paramValue' value='zzz' /><input type='submit' /></form></li>%n",
				actionUrlToPortletPlusRenderParamsPassedToOther(siteRootUrl, pageFriendlyId, portletIds));

		out.printf("<li><a href='%s'>window state for 1 (2nd - min): MAXIMIZED</a></li>%n",
				windowStateUrl(siteRootUrl, pageFriendlyId, portletIds, WindowState.MAXIMIZED));
		out.printf("<li><a href='%s'>window state for 1 (2nd - min): MINIMIZED</a></li>%n",
				windowStateUrl(siteRootUrl, pageFriendlyId, portletIds, WindowState.MINIMIZED));
		out.printf("<li><a href='%s'>window state for 1 (2nd - min): NORMAL</a></li>%n",
				windowStateUrl(siteRootUrl, pageFriendlyId, portletIds, WindowState.NORMAL));

		out.printf("<li><a href='%s'>portlet mode for 1 (2nd - help): HELP</a></li>%n",
				portletModeUrl(siteRootUrl, pageFriendlyId, portletIds, PortletMode.HELP));
		out.printf("<li><a href='%s'>portlet mode for 1 (2nd - help): EDIT</a></li>%n",
				portletModeUrl(siteRootUrl, pageFriendlyId, portletIds, PortletMode.EDIT));
		out.printf("<li><a href='%s'>portlet mode for 1 (2nd - help): VIEW</a></li>%n",
				portletModeUrl(siteRootUrl, pageFriendlyId, portletIds, PortletMode.VIEW));

		out.printf("<li><a href='%s'>total recall test!!!</a></li>%n",
				totalRecallUrl(siteRootUrl, pageFriendlyId, portletIds));

		out.println("</ul>");
	}

	private static PortalURL totalRecallUrl(String siteRootUrl, String pageFriendlyId, String[] portletIds)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);

		url.setPublicParameter(portletIds[0], "pubRenderParam", "aaa");
		url.setPortletMode(portletIds[0], PortletMode.HELP);

		url.setParameter(portletIds[1], "x", "1");
		url.setPortletMode(portletIds[1], PortletMode.EDIT);

		url.setPortletMode(portletIds[2], PortletMode.HELP);
		url.setWindowState(portletIds[2], WindowState.MINIMIZED);
		return url;
	}

	private static PortalURL portletModeUrl(String siteRootUrl, String pageFriendlyId, String[] portletIds, PortletMode portletMode)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setPortletMode(portletIds[0], portletMode);
		url.setPortletMode(portletIds[1], PortletMode.HELP);
		return url;
	}

	private static PortalURL windowStateUrl(String siteRootUrl, String pageFriendlyId, String[] portletIds, WindowState windowState)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setWindowState(portletIds[0], windowState);
		url.setWindowState(portletIds[1], WindowState.MINIMIZED);
		return url;
	}

	private static PortalURL actionUrlToPortletPlusRenderParamsPassedToOther(String siteRootUrl, String pageFriendlyId, String[] portletIds)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(portletIds[0], "a", "1");
		url.setAsActionURL(portletIds[1]);
		url.setParameter(portletIds[1], "b", "2");
		url.setParameter(portletIds[2], "c", "3");
		url.setPublicParameter(portletIds[2], "pubRenderParam", "xxx");
		return url;
	}

	private static PortalURL actionUrlToPortlet(String siteRootUrl, String pageFriendlyId, String[] portletIds)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setAsActionURL(portletIds[0]);
		return url;
	}

	private static PortalURL passPublicParamsTo3Portlets(String siteRootUrl, String pageFriendlyId, String[] portletIds)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(portletIds[0], "a", "1");
		url.setParameter(portletIds[1], "a", "2");
		url.setPublicParameter(portletIds[1], "pubRenderParam", "xxx");
		url.setParameter(portletIds[2], "a", "3");
		return url;
	}

	private static PortalURL passClashingParamsTo3Portlets(String siteRootUrl, String pageFriendlyId, String[] portletIds)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(portletIds[0], "a", "1");
		url.setParameter(portletIds[1], "a", "2");
		url.setParameter(portletIds[2], "a", "3");
		return url;
	}

	private static PortalURL passParamsTo3Portlets(String siteRootUrl, String pageFriendlyId, String[] portletIds)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(portletIds[0], "a", "1");
		url.setParameter(portletIds[1], "b", "2");
		url.setParameter(portletIds[2], "c", "3");
		return url;
	}

	private static PortalURL passParamsToSinglePortlet(String siteRootUrl, String pageFriendlyId, String[] portletIds) {
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(portletIds[0], "a1", "1");
		url.setParameter(portletIds[0], "a2", new String[] {"2", "3"});
		Map<String, String> p1 = new HashMap<String, String>();
		p1.put("a3", "4");
		url.setParameters(portletIds[0], p1);
		Map<String, String[]> p2 = new HashMap<String, String[]>();
		p2.put("a4", new String[] {"5", "6"});
		url.setParameters(portletIds[0], p2);
		return url;
	}
}
