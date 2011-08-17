package com.hp.spp.wsrp.url;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Class for the construction of urls invoking remote portlets in Vignette Portal.
 * 
 * @author mathieu.vidal@hp.com
 */
public class VignetteWSRPURL extends VignetteURL {

	public VignetteWSRPURL(BaseUrlComposer composer) {
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

			url.append("&javax.portlet.begCacheTok=com.vignette.cachetoken");
			url.append("&javax.portlet.endCacheTok=com.vignette.cachetoken");
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
	String addParameters(String portletId, HashMap parameters) {
		StringBuffer url = new StringBuffer();
		String parameterEncoded = null;
		convertToArrayMap(parameters);
		try {
			parameterEncoded = Base64.encode(MapSerializer.serialize(parameters));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		url.append("javax.portlet.tpst=");
		url.append(portletId);
		url.append("&javax.portlet.prp_");
		url.append(portletId);
		url.append("_wsrp-navigationalState=");
		url.append(parameterEncoded);
		return url.toString();
	}

	/**
	 * Converts parameter values represented as String objects to parameter values represented
	 * by an array of (a single) String object.
	 * 
	 * @param parameters - The Map contening the paramaters where keys and values must be of
	 *        String type
	 */
	private void convertToArrayMap(Map parameters) {

		if (parameters != null) {

			Iterator keys = parameters.keySet().iterator();
			while (keys != null && keys.hasNext()) {

				String key = (String) keys.next();
				Object value = parameters.get(key);
				if (value instanceof String) {
					String[] values = new String[] {(String) value};
					parameters.put(key, values);
				}
			}
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
