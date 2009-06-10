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
	 * <b>Note:</b> This method is for setting portlet <i>private</i> render
	 * parameters. To pass <i>public</i> render parameters to portlets instead,
	 * use a method like {@link #setPublicParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
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
	 * 
	 * The private parameters will be set as action parameters when action URL is created,
	 * and this is only relevant to the remote portlets.
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
	 * 
	 * The private parameters will be set as action parameters when action URL is created,
	 * and this is only relevant to the remote portlets.
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
	 * 
	 * The use of these parameters when creating action URL is "undefined and may lead to unexpected results" 
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
	 * 
	 * The use of these parameters when creating action URL is "undefined and may lead to unexpected results"
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
	 * 
	 * The use of these parameters when creating action URL is "undefined and may lead to unexpected results"
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
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param portletMode
	 *            the portlet mode
	 */
	void setPortletMode(String portletFriendlyId, PortletMode portletMode);

	/**
	 * Makes this <code>PortalURL</code> an action URL targeted at the portlet
	 * with the given <tt>portletFriendlyId</tt>. You can only do this once
	 * with any one <code>PortalURL</code> object, since setting an action URL
	 * irreversibly changes the state of the object.
	 * 
	 * The private parameters will be set as action parameters when action URL is created,
	 * and this is only relevant to the remote portlets.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @throws IllegalStateException
	 *             if action URL has already been set
	 * @since SPF 1.0
	 */
	void setAsActionURL(String portletFriendlyId) throws IllegalStateException;

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