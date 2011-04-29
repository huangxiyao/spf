package com.hp.it.spf.apps.portalurltest.portlet;


import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.IOException;

/**
 * Portlet which can be used to test PortalURL API.
 * <p>
 * The test class <tt>com.hp.it.spf.xa.portalurl.example.GenerateTestPage</tt> generates a page
 * with a set of URLs which can be targeted to this portlet. The class requires the portlet to have
 * specific friendly IDs - check it for details.
 * <p>
 * This portlet can be deployed either as remote or local portlet. Note that for Vignette local
 * portlet the appropriate web.xml file should be used (i.e. web.xml.vignette_local_portlets).
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalURLTestPortlet extends GenericPortlet
{
	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portalurltest/view.jsp").include(request, response);
	}

	@Override
	protected void doHelp(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portalurltest/help.jsp").include(request, response);
	}

	@Override
	protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portalurltest/edit.jsp").include(request, response);
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException
	{
		String paramName = request.getParameter("paramName");
		String paramValue = request.getParameter("paramValue");
		if (paramValue == null || paramValue.trim().equals("")) {
			paramValue = null;
		}
		String[] paramValues = null;
		if (paramValue != null) {
			paramValues = paramValue.split(",");
		}
		response.setRenderParameter(paramName, paramValues);
	}
}
