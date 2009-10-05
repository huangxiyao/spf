package com.hp.it.spf.xa.portalurl;

import javax.portlet.WindowState;
import javax.portlet.PortletMode;
import java.util.Map;

/**
 * An interface for a URL which can invoke a portal page and pass parameters to
 * its portlets. The resulting URL can be an action URL for a portlet if
 * {@link PortalURL#setAsActionURL(String)} is called. <code>setParameter</code>
 * methods set portlet private render parameters, and also portal query
 * parameters. <code>setPublicParameter</code> methods should be used to set
 * portlet public render parameters.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 * @author Scott Jorgenson (scott.jorgenson@hp.com)
 */
public interface PortalURL {

	/**
	 * Sets a portal query parameter of the given <tt>paramName</tt> to the
	 * given <tt>paramValue</tt>. The parameters are not edit-checked in any
	 * way (but note that any null parameters or values will be ignored and not
	 * included in the final URL string from {@link #toString()}. Each time you
	 * call this method, any previous value(s) set for the same
	 * <tt>paramName</tt> will be overwritten; so to set multiple values for
	 * the same <tt>paramName</tt>, use the companion
	 * {@link #setParameter(String, String[])} method.
	 * <p>
	 * <b>Note:</b> This method is for setting portal query parameters.
	 * Portlets will not see these parameters. To pass parameters to portlets
	 * instead, use one of the methods which takes portlet friendly ID - for
	 * example, {@link #setParameter(String, String, String)} or
	 * {@link #setPublicParameter(String, String, String)}.
	 * 
	 * @param paramName
	 *            the parameter name
	 * @param paramValue
	 *            the parameter value
	 */
	void setParameter(String paramName, String paramValue);

	/**
	 * Sets a portal query parameter of the given <tt>paramName</tt> to the
	 * given set of <tt>paramValues</tt>. The parameters are not edit-checked
	 * in any way (but note that any null parameters or values will be ignored
	 * and not included in the final URL string from {@link #toString()}. Each
	 * time you call this method, any previous value(s) set for the same
	 * <tt>paramName</tt> will be overwritten.
	 * <p>
	 * <b>Note:</b> This method is for setting portal query parameters.
	 * Portlets will not see these parameters. To pass parameters to portlets
	 * instead, use one of the methods which takes portlet friendly ID - for
	 * example, {@link #setParameter(String, String, String)} or
	 * {@link #setPublicParameter(String, String, String)}.
	 * 
	 * @param paramName
	 *            the parameter name
	 * @param paramValues
	 *            an array of the parameter values
	 */
	void setParameter(String paramName, String[] paramValues);

	/**
	 * Sets portal query parameter(s) from the given map of <tt>params</tt>.
	 * The map key is the string parameter name, and the map value is the string
	 * parameter value. Any non-string elements are ignored (and any null
	 * parameters or values will be ignored too) - these are not included in the
	 * final URL string from {@link #toString()}. Otherwise, the parameters are
	 * not edit-checked in any way. Each time you call this method, any previous
	 * value(s) set for the same parameter names will be overwritten.
	 * <p>
	 * <b>Note:</b> This method is for setting portal query parameters.
	 * Portlets will not see these parameters. To pass parameters to portlets
	 * instead, use one of the methods which takes portlet friendly ID - for
	 * example, {@link #setParameter(String, String, String)} or
	 * {@link #setPublicParameter(String, String, String)}.
	 * 
	 * @param params
	 *            a map of the parameter names to values (either single strings
	 *            or arrays of strings)
	 */
	void setParameters(Map params);

	/**
	 * Sets a private render parameter targeted to the portlet with the given
	 * <tt>portletFriendlyId</tt>, using the given <tt>paramName</tt> and
	 * <tt>paramValue</tt>. The parameters are not edit-checked in any way
	 * (but note that any null parameters or values will be ignored and not
	 * included in the final URL string from {@link #toString()}. Each time you
	 * call this method, any previous value(s) set for the same
	 * <tt>paramName</tt> and same <tt>portletFriendlyId</tt> will be
	 * overwritten; so to set multiple values for the same <tt>paramName</tt>
	 * and same <tt>portletFriendlyId</tt>, use the companion
	 * {@link #setParameter(String, String, String[])} method.
	 * <p>
	 * <b>Note:</b> This method is for setting portlet <i>private</i>
	 * parameters. To pass <i>public</i> render parameters to portlets instead,
	 * use a method like {@link #setPublicParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * <b>Note:</b> This parameter will be set as an action parameter when an
	 * action URL is set (only in the case of remote portlets). If a resource
	 * URL is set, it will be set as a resource URL parameter (in the case of a
	 * JSR-286 implementation - for a JSR-168 implementation, results are
	 * undefined).
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValue
	 *            the parameter value
	 */
	void setParameter(String portletFriendlyId, String paramName,
			String paramValue);

	/**
	 * Sets a private render parameter targeted to the portlet with the given
	 * <tt>portletFriendlyId</tt>, using the given <tt>paramName</tt> and
	 * set of <tt>paramValues</tt>. The parameters are not edit-checked in
	 * any way (but note that any null parameters or values will be ignored and
	 * not included in the final URL string from {@link #toString()}. Each time
	 * you call this method, any previous value(s) set for the same
	 * <tt>paramName</tt> and same <tt>portletFriendlyId</tt> will be
	 * overwritten.
	 * <p>
	 * <b>Note:</b> This method is for setting portlet <i>private</i>
	 * parameters. To pass <i>public</i> render parameters to portlets instead,
	 * use a method like {@link #setPublicParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * <b>Note:</b> These parameters will be set as action parameters when an
	 * action URL is set (only in the case of remote portlets). If a resource
	 * URL is set, they will be set as resource URL parameters (in the case of a
	 * JSR-286 implementation - for a JSR-168 implementation, results are
	 * undefined).
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValues
	 *            an array of the parameter values
	 */
	void setParameter(String portletFriendlyId, String paramName,
			String[] paramValues);

	// /**
	// * @deprecated Use {@link #setParameters(String, java.util.Map)} instead.
	// */
	// void setParameter(String portletFriendlyId, Map params);

	/**
	 * Sets private render parameter(s) targeted to the portlet with the given
	 * <tt>portletFriendlyId</tt>, using the given map of <tt>params</tt>.
	 * The map key is the string parameter name, and the map value is the string
	 * parameter value. Any non-string elements are ignored (and any null
	 * parameters or values will be ignored too) - these are not included in the
	 * final URL string from {@link #toString()}. Each time you call this
	 * method, any previous value(s) set for the same <tt>paramName</tt> and
	 * same <tt>portletFriendlyId</tt> will be overwritten.
	 * <p>
	 * <b>Note:</b> This method is for setting portlet <i>private</i>
	 * parameters. To pass <i>public</i> render parameters to portlets instead,
	 * use a method like {@link #setPublicParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * <b>Note:</b> These parameters will be set as action parameters when an
	 * action URL is set (only in the case of remote portlets). If a resource
	 * URL is set, they will be set as resource URL parameters (in the case of a
	 * JSR-286 implementation - for a JSR-168 implementation, results are
	 * undefined).
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param params
	 *            a map of parameter names (strings) to parameter values
	 *            (strings or arrays of strings)
	 */
	void setParameters(String portletFriendlyId, Map params);

	/**
	 * Sets a public render parameter targeted to the portlet with the given
	 * <tt>portletFriendlyId</tt>, using the given <tt>paramName</tt> and
	 * <tt>paramValue</tt>. The parameters are not edit-checked in any way
	 * (but note that any null parameters or values will be ignored and not
	 * included in the final URL string from {@link #toString()}. Each time you
	 * call this method, any previous value(s) set for the same
	 * <tt>paramName</tt> and same <tt>portletFriendlyId</tt> will be
	 * overwritten; so to set multiple values for the same <tt>paramName</tt>
	 * and same <tt>portletFriendlyId</tt>, use the companion
	 * {@link #setParameter(String, String, String[])} method.
	 * <p>
	 * <b>Note:</b> This method is for setting portlet <i>public</i> render
	 * parameters. To pass <i>private</i> render parameters to portlets
	 * instead, use a method like {@link #setParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * Use of this method is undefined and may produce unexpected results, if
	 * you set your <code>PortalURL</code> to be a resource URL.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValue
	 *            the parameter value
	 * @since SPF 1.0
	 */
	void setPublicParameter(String portletFriendlyId, String paramName,
			String paramValue);

	/**
	 * Sets a public render parameter targeted to the portlet with the given
	 * <tt>portletFriendlyId</tt>, using the given <tt>paramName</tt> and
	 * set of <tt>paramValues</tt>. The parameters are not edit-checked in
	 * any way (but note that any null parameters or values will be ignored and
	 * not included in the final URL string from {@link #toString()}. Each time
	 * you call this method, any previous value(s) set for the same
	 * <tt>paramName</tt> and same <tt>portletFriendlyId</tt> will be
	 * overwritten.
	 * <p>
	 * <b>Note:</b> This method is for setting portlet <i>public</i> render
	 * parameters. To pass <i>private</i> render parameters to portlets
	 * instead, use a method like {@link #setParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * Use of this method is undefined and may produce unexpected results, if
	 * you set your <code>PortalURL</code> to be a resource URL.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValues
	 *            an array of the parameter values
	 * @since SPF 1.0
	 */
	void setPublicParameter(String portletFriendlyId, String paramName,
			String[] paramValues);

	/**
	 * Sets public render parameter(s) targeted to the portlet with the given
	 * <tt>portletFriendlyId</tt>, using the given map of <tt>params</tt>.
	 * The map key is the string parameter name, and the map value is the string
	 * parameter value. Any non-string elements are ignored (and any null
	 * parameters or values will be ignored too) - these are not included in the
	 * final URL string from {@link #toString()}. Each time you call this
	 * method, any previous value(s) set for the same <tt>paramName</tt> and
	 * same <tt>portletFriendlyId</tt> will be overwritten.
	 * <p>
	 * <b>Note:</b> This method is for setting portlet <i>public</i> render
	 * parameters. To pass <i>private</i> render parameters to portlets
	 * instead, use a method like {@link #setParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * Use of this method is undefined and may produce unexpected results, if
	 * you set your <code>PortalURL</code> to be a resource or action URL.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param params
	 *            a map of parameter names (strings) to parameter values
	 *            (strings or arrays of strings)
	 * @since SPF 1.0
	 */
	void setPublicParameters(String portletFriendlyId, Map params);

	/**
	 * Sets a <tt>windowState</tt> targeted at the portlet with the given
	 * <tt>portletFriendlyId</tt>. If the given <tt>windowState</tt> is
	 * null, then {@link javax.portlet.WindowState#NORMAL} is presumed.
	 * <p>
	 * Use of this method is undefined and may produce unexpected results, if
	 * you set your <code>PortalURL</code> to be a resource URL.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param windowState
	 *            the portlet window state
	 * @since SPF 1.0
	 */
	void setWindowState(String portletFriendlyId, WindowState windowState);

	/**
	 * Sets a <tt>portletMode</tt> targeted at the portlet with the given
	 * <tt>portletFriendlyId</tt>. If the given <tt>portletMode</tt> is
	 * null, then {@link javax.portlet.PortletMode#VIEW} is presumed.
	 * <p>
	 * Use of this method is undefined and may produce unexpected results, if
	 * you set your <code>PortalURL</code> to be a resource URL.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param portletMode
	 *            the portlet mode
	 */
	void setPortletMode(String portletFriendlyId, PortletMode portletMode);

	/**
	 * Makes this <code>PortalURL</code> an action URL targeted at the portlet
	 * with the given <tt>portletFriendlyId</tt>. You can use either this or
	 * {@link #setAsResourceURL(String, String)} at most once with any one
	 * <code>PortalURL</code> object, since setting an action URL or resource
	 * URL irreversibly changes the state of the object.
	 * <p>
	 * Note that when you use this method to make this <code>portalURL</code>
	 * be an action URL, any private parameters you set (eg using
	 * {@link #setParameter(String, String, String)}) will be treated as action
	 * parameters (only in the case of remote portlets). Any public parameters
	 * you set (eg using {@link #setPublicParameter(String, String, String)})
	 * will have undefined results.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @throws IllegalStateException
	 *             if action or resource URL has already been set
	 * @since SPF 1.0
	 */
	void setAsActionURL(String portletFriendlyId) throws IllegalStateException;

	/**
	 * Makes this <code>PortalURL</code> a resource URL for the given resource
	 * ID, targeted at the portlet with the given <tt>portletFriendlyId</tt>.
	 * Implementations of this method may support JSR-286 or may only be JSR-168
	 * compliant.
	 * <p>
	 * <ul>
	 * <li>In the case of a JSR-286 implementation, the given resource ID would
	 * be a resource ID string for identifying the resource within the portlet's
	 * <code>serveResource</code> method.</li>
	 * <li>In the case of a JSR-168 implementation, the given resource ID would
	 * typically be a URL for a servlet, image, or other static file inside the
	 * portlet application on the producer server.
	 * <ul>
	 * <li>A relative URL will be
	 * assumed to be relative to the portal server, so provide an absolute URL
	 * if your portlet may be remoted.</li>
	 * <li>The resource ID must already contain
	 * whatever parameters and formatting the resource needs - eg if the
	 * resource is a servlet which expects to find request parameters in its
	 * query string, you should add that query string to the resource ID before
	 * passing it to this method.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * <p>
	 * You can use either this method or {@link #setAsActionURL(String)} at most
	 * once with any one <code>PortalURL</code> object, since setting a
	 * resource URL or action URL changes the state of the object irreversibly.
	 * <p>
	 * Note that when you use this method to make a resource URL, any private
	 * parameters you set (eg using
	 * {@link #setParameter(String, String, String)}) will be treated as
	 * resource parameters in the case of a JSR-286 implementation. In the case
	 * of a JSR-168 implementation, results are undefined.
	 * <p>
	 * Any public parameters, portlet mode or window state you set (eg using
	 * {@link #setPublicParameter(String, String, String)} or
	 * {@link #setPortletMode(String, PortletMode)}) will have undefined
	 * results.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param resourceID
	 *            the resource ID (JSR-286) or servlet or static resource URL
	 *            (JSR-168) for the resource to serve at that portlet
	 * @throws IllegalStateException
	 *             if action or resource URL has already been set
	 * @since SPF 1.1
	 */
	void setAsResourceURL(String portletFriendlyId, String resourceID)
			throws IllegalStateException;

	/**
	 * Returns the <code>PortalURL</code> in String form.
	 * 
	 * @return the url in String form
	 * @deprecated
	 */
	String urlToString();

	/**
	 * Returns the <code>PortalURL</code> in String form.
	 * 
	 * @return the url in String form
	 */
	String toString();

}