package com.hp.it.spf.xa.portalurl;

import java.util.Map;
import java.util.List;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class VignetteLocalPortalURL extends AbstractPortalURL
{
	protected VignetteLocalPortalURL(String siteRootUrl, String anotherSiteName, String pageFriendlyUri, boolean secure)
	{
		super(siteRootUrl, anotherSiteName, pageFriendlyUri, secure);
	}

	protected void addPrivateParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId) {
		addParameters(result, portletFriendlyId, portletParameters.getValue().getPrivateParameters(), ".prp_");
	}

	protected void addPublicParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId) {
		addParameters(result, portletFriendlyId, portletParameters.getValue().getPublicParameters(), ".pbp_");
	}

	private void addParameters(StringBuilder result, String portletFriendlyId, Map<String, List<String>> parameters, String paramNameSuffix)
	{
		for (String paramName : parameters.keySet()) {
			for (String paramValue : parameters.get(paramName)) {
				result.append('&').append(PARAM_NAME_PREFIX).append(paramNameSuffix);
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

}
