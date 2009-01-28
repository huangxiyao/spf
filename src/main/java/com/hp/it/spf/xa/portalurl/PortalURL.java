package com.hp.it.spf.xa.portalurl;

/**
 * URL class to invoke a portal page and to pass parameters to its portlets.
 * The resulting URL can be action URL for a portlet if {@link PortalURL#setAsActionURL(String)}
 * is called. <code>setParameter</code> methods set portlet private render parameters.
 * <code>setPublicParameter</code> should be used to set portlet public render parameters.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
import javax.portlet.WindowState;
import javax.portlet.PortletMode;
import java.util.Map;

public interface PortalURL {

	/**
	 * Sets a query parameter targeted to the portal in the portal url.
	 *
	 * @param paramName - name of the portal query parameter
	 * @param paramValue -  value for the portal query parameter
	 */
	void setParameter(String paramName, String paramValue);

	/**
	 * Sets a multi-value query parameter targeted to the portal in the portal url.
	 *
	 * @param paramName - name of the portal query parameter
	 * @param paramValues - values for the portal query parameter
	 */
	void setParameter(String paramName, String[] paramValues);

	/**
	 * Sets query parameters targeted to the portal in the portal url.
	 *
	 * @param params - Each map entry represents one portal query parameter. The keys must
	 * be strings and they are parameter names. The values can be either String or String[] and represent
	 * parameter values.
	 */
	void setParameters(Map params);

	/**
	 * Sets a portlet render parameter in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param paramName - name of the portlet render parameter
	 * @param paramValue -  value for the portlet parameter
	 */
	void setParameter(String portletFriendlyId, String paramName, String paramValue);

	/**
	 * Sets a portlet multi-value render parameter in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param paramName - name of the portlet render parameter
	 * @param paramValues - values for the portlet parameter
	 */
	void setParameter(String portletFriendlyId, String paramName, String[] paramValues);

//	/**
//	 * @deprecated Use {@link #setParameters(String, java.util.Map)} instead.
//	 */
//	void setParameter(String portletFriendlyId, Map params);

	/**
	 * Sets portlet render parameters in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param params - portlet render parameters; Each map entry represnts one parameter. The keys must
	 * be strings and they are parameter names. The values can be either String or String[] and represent
	 * parameter values.
	 */
	void setParameters(String portletFriendlyId, Map params);

	/**
	 * Sets a portlet public render parameter in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param paramName - name of the portlet render parameter
	 * @param paramValue -  value for the portlet parameter
	 *
	 * @since SPF 1.0
	 */
	void setPublicParameter(String portletFriendlyId, String paramName, String paramValue);

	/**
	 * Sets a portlet public multi-value render parameter in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param paramName - name of the portlet render parameter
	 * @param paramValues - values for the portlet parameter
	 *
	 * @since SPF 1.0
	 */
	void setPublicParameter(String portletFriendlyId, String paramName, String[] paramValues);

	/**
	 * Sets portlet public render parameters in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param params - portlet render parameters; Each map entry represnts one parameter. The keys must
	 * be strings and they are parameter names. The values can be either String or String[] and represent
	 * parameter values.
	 *
	 * @since SPF 1.0
	 */
	void setPublicParameters(String portletFriendlyId, Map params);

	/**
	 * Sets a window state in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param windowState - requested window state
	 *
	 * @since SPF 1.0
	 */
	void setWindowState(String portletFriendlyId, WindowState windowState);

	/**
	 * Sets a portlet mode in the portal url.
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @param portletMode - requested portlet mode
	 */
	void setPortletMode(String portletFriendlyId, PortletMode portletMode);

	/**
	 * Sets this URL as action URL for the portlet with the specified friendly ID
	 *
	 * @param portletFriendlyId - friendly ID of the portlet in Vignette
	 * @throws IllegalStateException If this URL has already been set as an action URL
	 *
	 * @since SPF 1.0
	 */
	void setAsActionURL(String portletFriendlyId) throws IllegalStateException;

	/**
	 * Returns the url in String form.
	 *
	 * @return the url in String form
	 * @deprecated
	 */
	String urlToString();
	
	/**
	 * Returns the url in String form.
	 *
	 * @return the url in String form
	 */
	String toString();

}