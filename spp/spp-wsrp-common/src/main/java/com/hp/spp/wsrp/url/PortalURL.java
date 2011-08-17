package com.hp.spp.wsrp.url;

import java.util.Map;

/**
 * URL to invoke a portlet from a portal.
 * 
 * @author mathieu.vidal@hp.com
 */
public interface PortalURL {

	/**
	 * Sets a portlet render parameter in the portal url.
	 * 
	 * @param portletId - friendly ID of the portlet in Vignette
	 * @param paramName - name of the portlet render parameter
	 * @param paramValue -  value for the portlet parameter
	 */
	void setParameter(String portletId, String paramName, String paramValue);

	/**
	 * Sets a portlet multi-value render parameter in the portal url.
	 *
	 * @param portletId - friendly ID of the portlet in Vignette
	 * @param paramName - name of the portlet render parameter
	 * @param paramValues - values for the portlet parameter
	 */
	void setParameter(String portletId, String paramName, String[] paramValues);

	/**
	 * @deprecated Use {@link #setParameters(String, java.util.Map)} instead.
	 */
	void setParameter(String portletId, Map params);

	/**
	 * Sets portlet render parameters in the portal url.
	 *
	 * @param portletId - friendly ID of the portlet in Vignette
	 * @param params - portlet render parameters; Each map entry represnts one parameter. The keys must
	 * be strings and they are parameter names. The values can be either String or String[] and represent
	 * parameter values.
	 */
	void setParameters(String portletId, Map params);
	
	/**
	 * Returns the url in String form.
	 * 
	 * @return the url in String form
	 */
	String urlToString();
}
