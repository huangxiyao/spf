package com.hp.it.spf.apps.sessiontest.portlet;

import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.RenderMode;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Portlet used to test the fact that the value stored is consistently available
 * when accessing the portlet several times. This is either achieved through session stickiness
 * (used in SPF) or through session replication.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class SessionTestPortlet extends GenericPortlet
{
	private static final String SESSION_VALUE_KEY = "SESSION_VALUE";

	@RenderMode(name = "view")
	public void showView(RenderRequest request, RenderResponse response) throws IOException
	{
		String sessionValue = (String) request.getPortletSession().getAttribute(SESSION_VALUE_KEY);
		PrintWriter out = response.getWriter();
		out.printf("<p>Running on %s</p>%n", System.getProperty("weblogic.Name"));
		out.printf("<p>Value in session: %s</p>%n",
				(sessionValue == null ? "<i>not defined yet</i>" : sessionValue));
		out.printf(
				"<form method='post' action='%s'>%n" +
				"<label for='sessionValue'>Session Value:</label>%n" +
				"<input type='text' id='sessionValue' name='sessionValue' value='%s' />%n" +
				"<input type='submit' value='Save' />" +
				"</form>",
				response.createActionURL(),
				(sessionValue == null ? "" : sessionValue));
	}

	@RenderMode(name = "vignette:config")
	public void showConfig(RenderRequest request, RenderResponse response) throws IOException
	{
		showView(request, response);
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response) {
		String value = request.getParameter("sessionValue");
		PortletSession session = request.getPortletSession();
		if (value == null || value.trim().equals("")) {
			session.removeAttribute(SESSION_VALUE_KEY);
		}
		else {
			session.setAttribute(SESSION_VALUE_KEY, value.trim());
		}
	}
}
