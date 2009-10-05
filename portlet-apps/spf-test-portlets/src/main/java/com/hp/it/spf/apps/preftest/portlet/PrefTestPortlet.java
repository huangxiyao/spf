package com.hp.it.spf.apps.preftest.portlet;

import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.RenderMode;
import javax.portlet.PortletException;
import javax.portlet.ActionResponse;
import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ValidatorException;
import javax.portlet.ReadOnlyException;
import java.io.IOException;

/**
 * This portlet allows to test the portlet preference scope.
 * <p>
 * The value of preferences set in CONFIG mode should be shared among all users. The values of
 * preferences set in the VIEW or EDIT mode should be specific to all users. Setting the same
 * preference in both CONFIG and EDIT mode will result in the value set in EDIT mode taking precedence.
 * Finally, upon redployment of the portlet application, the preference values should remain as
 * before the redeployment. Additions to the poreferences defined in portlet.xml file should
 * also be visible after the redeployment.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PrefTestPortlet extends GenericPortlet
{
	@RenderMode(name = "view")
	public void showView(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		getPortletContext().getRequestDispatcher("/WEB-INF/jsp/preftest/view.jsp").include(request, response);
	}

	@RenderMode(name = "vignette:config")
	public void showConfig(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		showView(request, response);
	}
	
	@RenderMode(name = "config")
	public void showConfigLocal(RenderRequest request, RenderResponse response) throws PortletException, IOException
	{
		showConfig(request, response);
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws ValidatorException, IOException, ReadOnlyException
	{
		String prefName = request.getParameter("prefName");
		String prefValue = request.getParameter("prefValue");
		if (prefName != null && prefValue != null) {
			PortletPreferences prefs = request.getPreferences();
			if (prefValue.trim().equals("")) {
				prefs.reset(prefName);
			}
			else {
				prefs.setValues(prefName, prefValue.split("\\s*,\\s*"));
			}
			prefs.store();
		}
	}
}
