package com.hp.it.spf.xa.portalurl;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import com.hp.it.spf.xa.portalurl.AbstractPortalURL.PortletParameters;

/**
 * A concrete {@link PortalURL} which implements remote portlet handling for
 * portlet parameters.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class VignetteRemotePortalURL extends AbstractPortalURL {
	private static final char NAV_STATE_KEY_DELIMITER = '=';
	private static final char NAV_STATE_ARRAY_DELIMITER = ';';
	private static final char NAV_STATE_PARAM_DELIMITER = '|';

	/**
	 * Call the superclass constructor to build this object.
	 */
	protected VignetteRemotePortalURL(String siteRootUrl,
			String anotherSiteName, String pageFriendlyUri, boolean secure,
			int nonStandardHttpPort, int nonStandardHttpsPort) {
		super(siteRootUrl, anotherSiteName, pageFriendlyUri, secure,
				nonStandardHttpPort, nonStandardHttpsPort);
	}

	/**
	 * Add private parameters in the format for Vignette local portlets.
	 */
	protected void addPrivateParameters(StringBuilder result,
			Map.Entry<String, PortletParameters> portletParameters,
			String portletFriendlyId, boolean isActionURL) {
		Map<String, List<String>> parameters = portletParameters.getValue()
				.getPrivateParameters();
		if (parameters.isEmpty()) {
			return;
		}

		result.append('&').append(PARAM_NAME_PREFIX).append(".prp_").append(
				portletFriendlyId);
		result.append('=');
		addNavigationalStateValue(result, parameters, isActionURL);
	}

	private void addNavigationalStateValue(StringBuilder result,
			Map<String, List<String>> parameters, boolean isActionURL) {
		if (isActionURL) {
			result.append("wsrp-interactionState");
		} else {
			result.append("wsrp-navigationalState");
		}

		// everything following 'wsrp-navigationalState' must be URL-encoded -
		// we create it through
		// a separate builder.
		// note that the '=' (equals) character is encoded too
		try {
			StringBuilder navigationalState = new StringBuilder();
			for (Iterator<Map.Entry<String, List<String>>> paramIter = parameters
					.entrySet().iterator(); paramIter.hasNext();) {
				Map.Entry<String, List<String>> param = paramIter.next();
				String paramName = param.getKey();
				if (paramName != null) {
					navigationalState.append(
							URLEncoder.encode(paramName, "UTF-8")).append(
							NAV_STATE_KEY_DELIMITER);
					for (Iterator<String> paramValueIter = param.getValue()
							.iterator(); paramValueIter.hasNext();) {
						String paramValue = paramValueIter.next();
						if (paramValue != null) {
							navigationalState.append(URLEncoder.encode(
									paramValue, "UTF-8"));
							if (paramValueIter.hasNext()) {
								navigationalState
										.append(NAV_STATE_ARRAY_DELIMITER);
							}
						}
					}
					if (paramIter.hasNext()) {
						navigationalState.append(NAV_STATE_PARAM_DELIMITER);
					}
				}
			}
			result.append(URLEncoder.encode("="
					+ URLEncoder.encode(navigationalState.toString(), "UTF-8"),
					"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not supported? " + e, e);
		}
	}

	/**
	 * Add public parameters in the format for Vignette local portlets.
	 */
	protected void addPublicParameters(StringBuilder result,
			Map.Entry<String, PortletParameters> portletParameters,
			String portletFriendlyId) {
		Map<String, List<String>> parameters = portletParameters.getValue()
				.getPublicParameters();
		for (String paramName : parameters.keySet()) {
			if (paramName != null) {
				for (String paramValue : parameters.get(paramName)) {
					if (paramValue != null) {
						result.append('&').append(PARAM_NAME_PREFIX).append(
								".pbp_");
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

	/**
	 * Create a resource URL string for a Vignette remote portlet. This is a
	 * JSR-168 implementation, in which a portlet resource URL is a servlet,
	 * image or other static file typically in the portlet application. JSR-286
	 * true portlet resource serving is not supported by this implementation.
	 * <p>
	 * A Vignette remote portlet resource URL contains the resource ID that was
	 * set earlier (using
	 * {@link AbstractPortalURL#setAsResourceURL(String, String)}), which was
	 * either a relative or absolute HTTP or HTTPS URL. This method embeds that
	 * in a Vignette <code>template.BINARYPORTLET</code> URL and returns it.
	 * <p>
	 * If the current <code>PortalURL</code> was not set as a resource URL,
	 * this method returns empty.
	 * 
	 * @return the resource URL string for the remote Vignette portlet
	 */
	protected StringBuilder createResourceUrl() {

		boolean isResourceUrl = mResourcePortletFriendlyId != null;
		StringBuilder result = new StringBuilder();
		if (!isResourceUrl) {
			return (result);
		}

		// get the site root URL string and make sure it ends with '/'
		result.append(createSiteRootUrl());
		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}

		// append the "template.BINARYPORTLET" secondary page friendly ID.
		result.append("template.BINARYPORTLET/");

		// for a resource URL, Vignette puts the friendly URL next (before the
		// "resource.process")
		if ((mPageFriendlyUri != null) && (mPageFriendlyUri.length() > 0)) {
			if (mPageFriendlyUri.charAt(0) == '/') {
				if (mPageFriendlyUri.length() > 1) {
					result.append(mPageFriendlyUri.subSequence(1,
							mPageFriendlyUri.length()));
				}
			} else {
				result.append(mPageFriendlyUri);
			}
		}

		// append the "resource.process" element next and
		// terminate portal URL path with a "/" character
		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}
		result.append("resource.process/");

		// merge and add portal parameters and portlet resource parameters
		result.append(mergeQueryStrings(createPortalQueryString(),
				createResourceQueryString()));

		// finished creating resource URL
		return (result);
	}

	private StringBuilder createResourceQueryString() {

		boolean isResourceUrl = mResourcePortletFriendlyId != null;
		StringBuilder result = new StringBuilder();
		Iterator<Map.Entry<String, PortletParameters>> portletParameterEntries = mPortletParameters
				.entrySet().iterator();

		if (isResourceUrl) {
			// if this is a resource URL we specify the target portlet for the
			// resource (there is always only one) with ".tpst" parameter
			// (I don't know what the "_ws_BI" means but Vignette generates it
			// so
			// we will put it in)
			result.append('?');
			result.append(PARAM_NAME_PREFIX).append(".tpst=").append(
					mResourcePortletFriendlyId).append("_ws_BI");

			// then we specify the resource information with ".rst_" parameter
			result.append('&').append(PARAM_NAME_PREFIX).append(".rst_")
					.append(mResourcePortletFriendlyId).append('=');

			// first resource info is the WSRP URL
			StringBuilder info = new StringBuilder();
			try {
				info.append("wsrp-url=");
				info.append(URLEncoder.encode(mResourceId, "UTF-8"));
				info.append('&');
				info.append("wsrp-requiresRewrite=false");
				result.append(URLEncoder.encode(info.toString(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(
						"UTF-8 encoding not supported? " + e, e);
			}

			result.append("&javax.portlet.begCacheTok=com.vignette.cachetoken");
			// we now will add the remaining portlet information if it is there,
			// though it should not be (this may have undefined results when the
			// URL is opened)
			while (portletParameterEntries.hasNext()) {
				Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries
						.next();
				String portletFriendlyId = portletParameters.getKey();
				addWindowStateAndPortletMode(result, portletFriendlyId,
						portletParameters.getValue());
				addPublicParameters(result, portletParameters,
						portletFriendlyId);
				addPrivateParameters(result, portletParameters,
						portletFriendlyId, false);
			}
			result.append("&javax.portlet.endCacheTok=com.vignette.cachetoken");
		}
		return (result);

	}
}
