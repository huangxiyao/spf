package com.hp.it.spf.xa.portalurl;

import javax.portlet.WindowState;
import javax.portlet.PortletMode;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class VignetteLocalPortalURL extends AbstractPortalURL
{
	private static final String PARAM_NAME_PREFIX = "spf_p";

	public VignetteLocalPortalURL(String siteRootUrl, String anotherSiteName, String pageFriendlyUri, boolean secure)
	{
		super(siteRootUrl, anotherSiteName, pageFriendlyUri, secure);
	}

	@Override
	public String toString()
	{
		boolean isActionUrl = mActionPortletFriendlyId != null;
		boolean portletParametersSpecified = !mPortletParameters.isEmpty();
		StringBuilder result = createBaseUrl(isActionUrl);
		// added following line for portal parameter support - DSJ 2009/1/28
		boolean queryStarted = result.indexOf("?") >= 0;

		Iterator<Map.Entry<String, PortletParameters>> portletParameterEntries = mPortletParameters.entrySet().iterator();

		if (isActionUrl) {
			// added following for portal parameter support - DSJ 2009/1/28
			if (!queryStarted)
				result.append('?');
			result.append("javax.portlet.action=true");
			// result.append("?javax.portlet.action=true");
			// end DSJ 2009/1/28
			result.append('&').append(PARAM_NAME_PREFIX).append(".tpst=").append(mActionPortletFriendlyId);
		}
		else if (portletParameterEntries.hasNext()) {
			Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries.next();
			String portletFriendlyId = portletParameters.getKey();

			// added following line for portal parameter support - DSJ 2009/1/28
			// added following for portal parameter support - DSJ 2009/1/28
			if (!queryStarted)
				result.append('?');
			result.append(PARAM_NAME_PREFIX).append(".tpst=").append(portletFriendlyId);
			// result.append('?').append(PARAM_NAME_PREFIX).append(".tpst=").append(portletFriendlyId);
			// end DSJ 2009/1/28
			addStateAndModeToUrlFragment(result, portletParameters.getValue());
			addParameters(result, portletFriendlyId, portletParameters.getValue().getPublicParameters(), true);
			addParameters(result, portletFriendlyId, portletParameters.getValue().getPrivateParameters(), false);
		}

		if (portletParametersSpecified || isActionUrl) {
			result.append("&javax.portlet.begCacheTok=com.vignette.cachetoken");
			while (portletParameterEntries.hasNext()) {
				Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries.next();
				String portletFriendlyId = portletParameters.getKey();
				addWindowStateAndPortletMode(result, portletFriendlyId, portletParameters.getValue());
				addParameters(result, portletFriendlyId, portletParameters.getValue().getPublicParameters(), true);
				addParameters(result, portletFriendlyId, portletParameters.getValue().getPrivateParameters(), false);
			}
			result.append("&javax.portlet.endCacheTok=com.vignette.cachetoken");
		}

		return result.toString();
	}

	private void addWindowStateAndPortletMode(StringBuilder result, String portletFriendlyId, PortletParameters portletParameters) {
		if (!WindowState.NORMAL.equals(portletParameters.getWindowState()) ||
				!PortletMode.VIEW.equals(portletParameters.getPortletMode()))
		{
			result.append('&').append(PARAM_NAME_PREFIX).append(".pst=");
			result.append(portletFriendlyId);
			addStateAndModeToUrlFragment(result, portletParameters);
		}
	}

	private void addParameters(StringBuilder result, String portletFriendlyId, Map<String, List<String>> parameters, boolean isPublic)
	{
		for (String paramName : parameters.keySet()) {
			for (String paramValue : parameters.get(paramName)) {
				if (isPublic) {
					result.append('&').append(PARAM_NAME_PREFIX).append(".pbp_");
				}
				else {
					result.append('&').append(PARAM_NAME_PREFIX).append(".prp_");
				}
				try {
					result.append(portletFriendlyId).append('_').append(URLEncoder.encode(paramName, "UTF-8")).append('=');
					result.append(URLEncoder.encode(paramValue, "UTF-8"));
				}
				catch (UnsupportedEncodingException e) {
					throw new RuntimeException("UTF-8 encoding not supported? " + e, e);
				}
			}
		}
	}

	private void addStateAndModeToUrlFragment(StringBuilder result, PortletParameters portletParameters) {
		appendWindowStateToUrlFragment(portletParameters.getWindowState(), result);
		appendPortletModeToUrlFragment(portletParameters.getPortletMode(), result);
	}

	//FIXME (slawek) - how to implement custom window states ???
	private void appendWindowStateToUrlFragment(WindowState windowState, StringBuilder buf) {
		// NORMAL window state is not present in the URL as it's default
		if (WindowState.MAXIMIZED.equals(windowState)) {
			buf.append("_ws_MX");
		}
		else if (WindowState.MINIMIZED.equals(windowState)) {
			buf.append("_ws_MN");
		}
	}

	//FIXME (slawek) - how to implement custom portlet modes ???
	private void appendPortletModeToUrlFragment(PortletMode portletMode, StringBuilder buf) {
		// VIEW portlet mode is not present in the URL as it's default
		if (PortletMode.EDIT.equals(portletMode)) {
			buf.append("_pm_ED");
		}
		else if (PortletMode.HELP.equals(portletMode)) {
			buf.append("_pm_HP");
		}
	}

	public String urlToString() {
		return toString();
	}
}
