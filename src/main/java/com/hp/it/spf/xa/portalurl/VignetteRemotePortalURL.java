package com.hp.it.spf.xa.portalurl;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class VignetteRemotePortalURL extends AbstractPortalURL {
	private static final char NAV_STATE_KEY_DELIMITER = '=';
	private static final char NAV_STATE_ARRAY_DELIMITER = ';';
	private static final char NAV_STATE_PARAM_DELIMITER = '|';

	protected VignetteRemotePortalURL(String siteRootUrl, String anotherSiteName, String pageFriendlyUri, boolean secure) {
		super(siteRootUrl, anotherSiteName, pageFriendlyUri, secure);
	}

	protected void addPrivateParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId) {
		Map<String, List<String>> parameters = portletParameters.getValue().getPrivateParameters();
		if (parameters.isEmpty()) {
			return;
		}
		
		result.append('&').append(PARAM_NAME_PREFIX).append(".prp_").append(portletFriendlyId);
		result.append('=');
		addNavigationalStateValue(result, parameters);
	}

	private void addNavigationalStateValue(StringBuilder result, Map<String, List<String>> parameters) {
		result.append("wsrp-navigationalState");

		//everything following 'wsrp-navigationalState' must be URL-encoded - we create it through
		//a separate builder.
		//note that the '=' (equals) character is encoded too
		try {
			StringBuilder navigationalState = new StringBuilder();
			for (Iterator<Map.Entry<String, List<String>>> paramIter = parameters.entrySet().iterator(); paramIter.hasNext(); ) {
				Map.Entry<String, List<String>> param = paramIter.next();
				String paramName = param.getKey();

				navigationalState.append(URLEncoder.encode(paramName, "UTF-8")).append(NAV_STATE_KEY_DELIMITER);

				for (Iterator<String> paramValueIter = param.getValue().iterator(); paramValueIter.hasNext(); ) {
					String paramValue = paramValueIter.next();
					navigationalState.append(URLEncoder.encode(paramValue, "UTF-8"));
					if (paramValueIter.hasNext()) {
						navigationalState.append(NAV_STATE_ARRAY_DELIMITER);
					}
				}
				if (paramIter.hasNext()) {
					navigationalState.append(NAV_STATE_PARAM_DELIMITER);
				}
			}
			result.append(URLEncoder.encode("=" + URLEncoder.encode(navigationalState.toString(), "UTF-8"), "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not supported? " + e, e);
		}
	}

	protected void addPublicParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId) {
		Map<String, List<String>> parameters = portletParameters.getValue().getPublicParameters();
		for (String paramName : parameters.keySet()) {
			for (String paramValue : parameters.get(paramName)) {
				result.append('&').append(PARAM_NAME_PREFIX).append(".pbp_");
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
