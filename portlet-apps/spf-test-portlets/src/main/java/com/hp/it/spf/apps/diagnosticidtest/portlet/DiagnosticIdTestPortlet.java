package com.hp.it.spf.apps.diagnosticidtest.portlet;

import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import java.io.IOException;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class DiagnosticIdTestPortlet extends GenericPortlet
{
	@Override
	protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException
	{
		PortletRequestDispatcher dispatcher =
				getPortletContext().getRequestDispatcher("/WEB-INF/jsp/diagnosticidtest/view.jsp");
		dispatcher.include(renderRequest, renderResponse);
	}
}
