package com.hp.spp.wsrp.url;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for the construction of urls invoking portlets in Vignette Portal.
 * 
 * @author mathieu.vidal@hp.com
 */
public abstract class VignetteURL implements PortalURL {

	/**
	 * the map contening the portlets associated with their parameters.
	 */
	private HashMap mPortletsMap = new HashMap();

	private BaseUrlComposer mBaseUrlComposer = null;

	public VignetteURL(BaseUrlComposer composer/*, String site, String pageId*/) {
		mBaseUrlComposer = composer;
	}

	/**
	 * Sets a uni-value parameter in the portal url.
	 *
	 * @param portletId - the identifiant of the portlet in Vignette
	 * @param name - the name of the parameter
	 * @param value - the value for the parameter
	 * @see com.hp.spp.wsrp.url.PortalURL#setParameter(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public void setParameter(String portletId, String name, String value) {
		HashMap portletMap = getPortletMap(portletId);
		portletMap.put(name, value);
	}

	/**
	 * Sets a multi-value parameter in the portal url.
	 *
	 * @param portletId - the identifiant of the portlet in Vignette
	 * @param name - the name of the parameter
	 * @param values - the array of value for the parameter
	 * @see com.hp.spp.wsrp.url.PortalURL#setParameter(java.lang.String, java.lang.String,
	 *      java.lang.String[])
	 */
	public void setParameter(String portletId, String name, String[] values) {
		HashMap portletMap = getPortletMap(portletId);
		portletMap.put(name, values);
	}

	/**
	 * Sets a map of parameters in the portal url.
	 *
	 * @param portletId - the identifiant of the portlet in Vignette
	 * @param params - the parameters to add to the request for the portlet
	 * @see com.hp.spp.wsrp.url.PortalURL#setParameter(java.lang.String, java.util.Map)
	 */
	public void setParameter(String portletId, Map params) {
		setParameters(portletId, params);
	}

	/**
	 * Sets a map of parameters in the portal url.
	 *
	 * @param portletId - the identifiant of the portlet in Vignette
	 * @param params - the parameters to add to the request for the portlet
	 * @see com.hp.spp.wsrp.url.PortalURL#setParameter(java.lang.String, java.util.Map)
	 */
	public void setParameters(String portletId, Map params) {
		HashMap portletMap = getPortletMap(portletId);
		portletMap.putAll(params);
	}

	/**
	 * Getter of mPortletsMap.
	 *
	 * @return mPortletsMap
	 */
	public HashMap getPortletsMap() {
		return mPortletsMap;
	}


	public BaseUrlComposer getBaseUrlComposer() {
		return mBaseUrlComposer;
	}

	/**
	 * Gets the parameters map associated with a portlet.
	 * 
	 * @param portletId - the identifier of the porltet
	 * @return the parameters map associated with a portlet
	 */
	public HashMap getPortletMap(String portletId) {
		if (!mPortletsMap.containsKey(portletId)) {
			mPortletsMap.put(portletId, new HashMap());
		}
		return (HashMap) mPortletsMap.get(portletId);
	}

	/**
	 * Returns the first part of the url contening the site, the pageId and the portletId.
	 * 
	 * @param site - the url of the site (for instance http://mycompany:8087/mysite
	 * @param pageId - the vignette id of the menu item
	 * @return the first part of the url
	 * @deprecated Use {@link BaseUrlComposer} instead
	 */
	static String composeRootUrl(String site, String pageId) {
		StringBuffer url = new StringBuffer();
		url.append(site);
		url.append("/template.PAGE/menuitem.");
		url.append(pageId);

		return url.toString();
	}
}
