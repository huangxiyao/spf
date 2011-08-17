package com.hp.spp.wsrp.url;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * Class for the construction of urls invoking locale portlets in Vignette Portal.
 * 
 * @author mathieu.vidal@hp.com
 */
public class VignetteLocalURL extends VignetteURL {

	public VignetteLocalURL(BaseUrlComposer composer) {
		super(composer);
	}

	/**
	 * Returns the url in String form.
	 * 
	 * @return the url in String form
	 */
	public String urlToString() {
		StringBuffer url = new StringBuffer();
		url.append(getBaseUrlComposer().getBaseUrl());
		if (!getPortletsMap().isEmpty()) {
			if (url.indexOf("?") == -1) {
				url.append('?');
			}
			else {
				url.append('&');
			}

			Set portlets = this.getPortletsMap().keySet();
			for (Iterator iter = portlets.iterator(); iter.hasNext();) {
				String portletId = (String) iter.next();
				HashMap parameters = (HashMap) this.getPortletsMap().get(portletId);
				url.append(addParameters(portletId, parameters));
				if (iter.hasNext()) {
					url.append('&');
				}
			}
		}

		return url.toString();
	}

	/**
	 * Constructs the string to add the parameters to the url.
	 * 
	 * @param portletId - the identifier of the portlet
	 * @param parameters - the parameters to add
	 * @return the string to add the parameters to the url
	 */
	private String addParameters(String portletId, HashMap parameters) {
		StringBuffer url = new StringBuffer();

		url.append("javax.portlet.tpst=");
		url.append(portletId);

		Set parametersName = parameters.keySet();
		for (Iterator iter = parametersName.iterator(); iter.hasNext();) {
			String parameterName = (String) iter.next();
			if (parameters.get(parameterName) instanceof String) {
				String parameterValue = (String) parameters.get(parameterName);
				appendParameter(url, portletId, parameterName, parameterValue);
			} else if (parameters.get(parameterName) instanceof String[]) {
				String[] parameterValue = (String[]) parameters.get(parameterName);
				for (int i = 0; i < parameterValue.length; i++) {
					appendParameter(url, portletId, parameterName, parameterValue[i]);
				}
			}
		}

		return url.toString();
	}

	private void appendParameter(StringBuffer url, String portletId, String paramName, String paramValue) {
		url.append("&javax.portlet.prp_");
		url.append(portletId);
		url.append("_").append(paramName).append("=");
		try {
			url.append(URLEncoder.encode(paramValue, "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 encoding not supported?: " + e);
		}
	}

	/**
	 * Same as {@link #urlToString()}
	 * @return String representation of this portal URL
	 */
	public String toString() {
		return urlToString();
	}
}
