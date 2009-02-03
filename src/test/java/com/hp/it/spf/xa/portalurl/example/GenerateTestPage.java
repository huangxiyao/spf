package com.hp.it.spf.xa.portalurl.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

import com.hp.it.spf.xa.portalurl.PortalURL;
import com.hp.it.spf.xa.portalurl.PortalURLFactory;

import javax.portlet.WindowState;
import javax.portlet.PortletMode;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class GenerateTestPage
{
//	private static final String PORTLET_1_ID = "fbe12f83a1daa8ea102b85d34c6f0101";
	private static final String PORTLET_1_ID = "portalurltestportlet1";
//	private static final String PORTLET_2_ID = "be56384a8ea22aea102b85d34c6f0101";
	private static final String PORTLET_2_ID = "portalurltestportlet2";
//	private static final String PORTLET_3_ID = "eb8fdc188409baea102b85d34c6f0101";
	private static final String PORTLET_3_ID = "portalurltestportlet3";


	public static void main(String[] args) throws IOException
	{
		final String siteRootUrl = (args.length > 0 ? args[0] : "http://localhost:9001/portal/site/spf/");
		final String pageFriendlyId = (args.length > 1 ? args[1] : "portal-url-test");

		PrintWriter out = new PrintWriter(new FileWriter("test.html"));
		try {
			out.println("<html>");
			out.println("<body>");
			out.println("<ul>");

			out.printf("<li><a href='%s'>Default page url</a></li>%n",
					PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId));
			out.printf("<li><a href='%s'>Pass params to a single portlet</a></li>%n",
					passParamsToSinglePortlet(siteRootUrl, pageFriendlyId));
			out.printf("<li><a href='%s'>Pass params to 3 portlets</a></li>%n",
					passParamsTo3Portlets(siteRootUrl, pageFriendlyId));
			out.printf("<li><a href='%s'>Pass clashing params to 3 portlets</a></li>%n",
					passClashingParamsTo3Portlets(siteRootUrl, pageFriendlyId));
			out.printf("<li><a href='%s'>Pass public params to 3 portlets</a></li>%n",
					passPublicParamsTo3Portlets(siteRootUrl, pageFriendlyId));
			out.printf("<li><form method='POST' action='%s'><input type='hidden' name='paramName' value='yyy' /><input name='paramValue' value='zzz' /><input type='submit' /></form></li>%n",
					actionUrlToPortlet(siteRootUrl, pageFriendlyId));
			out.printf("<li><form method='POST' action='%s'><input type='hidden' name='paramName' value='yyy' /><input name='paramValue' value='zzz' /><input type='submit' /></form></li>%n",
					actionUrlToPortletPlusRenderParamsPassedToOther(siteRootUrl, pageFriendlyId));

			out.printf("<li><a href='%s'>window state for 1 (2nd - min): MAXIMIZED</a></li>%n", windowStateUrl(siteRootUrl, pageFriendlyId, WindowState.MAXIMIZED));
			out.printf("<li><a href='%s'>window state for 1 (2nd - min): MINIMIZED</a></li>%n", windowStateUrl(siteRootUrl, pageFriendlyId, WindowState.MINIMIZED));
			out.printf("<li><a href='%s'>window state for 1 (2nd - min): NORMAL</a></li>%n", windowStateUrl(siteRootUrl, pageFriendlyId, WindowState.NORMAL));

			out.printf("<li><a href='%s'>portlet mode for 1 (2nd - help): HELP</a></li>%n", portletModeUrl(siteRootUrl, pageFriendlyId, PortletMode.HELP));
			out.printf("<li><a href='%s'>portlet mode for 1 (2nd - help): EDIT</a></li>%n", portletModeUrl(siteRootUrl, pageFriendlyId, PortletMode.EDIT));
			out.printf("<li><a href='%s'>portlet mode for 1 (2nd - help): VIEW</a></li>%n", portletModeUrl(siteRootUrl, pageFriendlyId, PortletMode.VIEW));

			out.printf("<li><a href='%s'>total recall test!!!</a></li>%n", totalRecallUrl(siteRootUrl, pageFriendlyId));
			out.println("</ul>");
			out.println("</body>");
			out.println("</html>");
		}
		finally {
			out.close();
		}
	}

	private static PortalURL totalRecallUrl(String siteRootUrl, String pageFriendlyId)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);

		url.setPublicParameter(PORTLET_1_ID, "pubRenderParam", "aaa");
		url.setPortletMode(PORTLET_1_ID, PortletMode.HELP);

		url.setParameter(PORTLET_2_ID, "x", "1");
		url.setPortletMode(PORTLET_2_ID, PortletMode.EDIT);

		url.setPortletMode(PORTLET_3_ID, PortletMode.HELP);
		url.setWindowState(PORTLET_3_ID, WindowState.MINIMIZED);
		return url;
	}

	private static PortalURL portletModeUrl(String siteRootUrl, String pageFriendlyId, PortletMode portletMode)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setPortletMode(PORTLET_1_ID, portletMode);
		url.setPortletMode(PORTLET_2_ID, PortletMode.HELP);
		return url;
	}

	private static PortalURL windowStateUrl(String siteRootUrl, String pageFriendlyId, WindowState windowState)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setWindowState(PORTLET_1_ID, windowState);
		url.setWindowState(PORTLET_2_ID, WindowState.MINIMIZED);
		return url;
	}

	private static PortalURL actionUrlToPortletPlusRenderParamsPassedToOther(String siteRootUrl, String pageFriendlyId)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(PORTLET_1_ID, "a", "1");
		url.setAsActionURL(PORTLET_2_ID);
		url.setParameter(PORTLET_2_ID, "b", "2");
		url.setParameter(PORTLET_3_ID, "c", "3");
		url.setPublicParameter(PORTLET_3_ID, "pubRenderParam", "xxx");
		return url;
	}

	private static PortalURL actionUrlToPortlet(String siteRootUrl, String pageFriendlyId)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setAsActionURL(PORTLET_1_ID);
		return url;
	}

	private static PortalURL passPublicParamsTo3Portlets(String siteRootUrl, String pageFriendlyId)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(PORTLET_1_ID, "a", "1");
		url.setParameter(PORTLET_2_ID, "a", "2");
		url.setPublicParameter(PORTLET_2_ID, "pubRenderParam", "xxx");
		url.setParameter(PORTLET_3_ID, "a", "3");
		return url;
	}

	private static PortalURL passClashingParamsTo3Portlets(String siteRootUrl, String pageFriendlyId)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(PORTLET_1_ID, "a", "1");
		url.setParameter(PORTLET_2_ID, "a", "2");
		url.setParameter(PORTLET_3_ID, "a", "3");
		return url;
	}

	private static PortalURL passParamsTo3Portlets(String siteRootUrl, String pageFriendlyId)
	{
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(PORTLET_1_ID, "a", "1");
		url.setParameter(PORTLET_2_ID, "b", "2");
		url.setParameter(PORTLET_3_ID, "c", "3");
		return url;
	}

	private static PortalURL passParamsToSinglePortlet(String siteRootUrl, String pageFriendlyId) {
		PortalURL url = PortalURLFactory.createPageURL(siteRootUrl, pageFriendlyId);
		url.setParameter(PORTLET_1_ID, "a1", "1");
		url.setParameter(PORTLET_1_ID, "a2", new String[] {"2", "3"});
		Map<String, String> p1 = new HashMap<String, String>();
		p1.put("a3", "4");
		url.setParameters(PORTLET_1_ID, p1);
		Map<String, String[]> p2 = new HashMap<String, String[]>();
		p2.put("a4", new String[] {"5", "6"});
		url.setParameters(PORTLET_1_ID, p2);
		return url;
	}
}
