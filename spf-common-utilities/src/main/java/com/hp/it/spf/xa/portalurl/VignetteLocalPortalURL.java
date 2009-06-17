package com.hp.it.spf.xa.portalurl;

import java.util.Map;
import java.util.List;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * A concrete {@link PortalURL} which implements Vignette local portlet handling
 * for portlet parameters and resource URL's.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 * @author Scott Jorgenson (scott.jorgenson@hp.com)
 */
class VignetteLocalPortalURL extends AbstractPortalURL {

	/**
	 * Call the superclass constructor to build this object.
	 */
	protected VignetteLocalPortalURL(String siteRootUrl,
			String anotherSiteName, String pageFriendlyUri, boolean secure,
			int nonStandardHttpPort, int nonStandardHttpsPort) {
		super(siteRootUrl, anotherSiteName, pageFriendlyUri, secure,
				nonStandardHttpPort, nonStandardHttpsPort);
	}

	/**
	 * Create a resource URL string for a Vignette local portlet. This is a
	 * JSR-168 implementation, in which a portlet resource URL is a servlet,
	 * image or other static file typically in the portlet application. JSR-286
	 * true portlet resource serving is not supported by this implementation.
	 * <p>
	 * A Vignette local portlet resource URL is just the resource ID that was
	 * set earlier (using
	 * {@link AbstractPortalURL#setAsResourceURL(String, String)}), which was
	 * either a relative or absolute HTTP or HTTPS URL. So this method just
	 * returns the resource ID that was set.
	 * <p>
	 * If the current <code>PortalURL</code> was not set as a resource URL,
	 * this method returns empty.
	 * 
	 * @return the resource URL string for the local Vignette portlet
	 */
	protected StringBuilder createResourceUrl() {
		boolean isResourceUrl = mResourcePortletFriendlyId != null;
		StringBuilder result = new StringBuilder();
		if (!isResourceUrl) {
			return (result);
		}
		result.append(mResourceId);
		return (result);
	}

	/**
	 * Add private parameters in the format for Vignette local portlets.
	 */
	protected void addPrivateParameters(StringBuilder result,
			Map.Entry<String, PortletParameters> portletParameters,
			String portletFriendlyId, boolean isActionURL) {
		addParameters(result, portletFriendlyId, portletParameters.getValue()
				.getPrivateParameters(), ".prp_");
	}

	/**
	 * Add public parameters in the format for Vignette local portlets.
	 */
	protected void addPublicParameters(StringBuilder result,
			Map.Entry<String, PortletParameters> portletParameters,
			String portletFriendlyId) {
		addParameters(result, portletFriendlyId, portletParameters.getValue()
				.getPublicParameters(), ".pbp_");
	}

	private void addParameters(StringBuilder result, String portletFriendlyId,
			Map<String, List<String>> parameters, String paramNameSuffix) {
		for (String paramName : parameters.keySet()) {
			if (paramName != null) {
				for (String paramValue : parameters.get(paramName)) {
					if (paramValue != null) {
						result.append('&').append(PARAM_NAME_PREFIX).append(
								paramNameSuffix);
						try {
							result.append(portletFriendlyId).append('_')
									.append(
											URLEncoder.encode(paramName,
													"UTF-8")).append('=');
							result.append(URLEncoder
									.encode(paramValue, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(
									"UTF-8 encoding not supported? " + e, e);
						}
					}
				}
			}
		}
	}

}
