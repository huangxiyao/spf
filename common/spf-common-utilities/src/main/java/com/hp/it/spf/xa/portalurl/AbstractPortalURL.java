package com.hp.it.spf.xa.portalurl;

import javax.portlet.WindowState;
import javax.portlet.PortletMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.hp.it.spf.xa.misc.Utils;

/**
 * Abstract class capturing all the {@link PortalURL} characteristics and
 * providing a template method for the URL rendering. This class will create
 * Vignette-specific URLs as the classes inherit from it. It reproduces the URL
 * format as expected by Vignette.
 * <p>
 * <b>Note:</b> As of SPF version 1.1 this class is a JSR-168 implementation,
 * so for resource URLs it builds URLs for servlets and static resources inside
 * portlet applications, rather than URLs for JSR-286 resource-serving portlets.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 * @author Scott Jorgenson (scott.jorgenson@hp.com)
 */
abstract class AbstractPortalURL implements PortalURL {

	protected static final String PARAM_NAME_PREFIX = "spf_p";

	protected String mSiteRootUrl = null;
	protected String mAnotherSiteName = null;
	protected String mPageFriendlyUri = null;
	protected boolean mSecure = false;
	protected int mNonstandardHttpPort = 0;
	protected int mNonstandardHttpsPort = 0;

	protected Map<String, PortletParameters> mPortletParameters = new LinkedHashMap<String, PortletParameters>();

	protected String mActionPortletFriendlyId = null;

	/**
	 * @since SPF 1.1
	 */
	protected String mResourcePortletFriendlyId = null;
	/**
	 * @since SPF 1.1
	 */
	protected String mResourceId = null;

	// added attribute for portal parameter support - DSJ 2009/1/28
	protected Map<String, List<String>> mPortalParameters = new LinkedHashMap<String, List<String>>();

	/**
	 * Constructor which takes a required site root URL, optional other site
	 * name, optional page friendly URI, a secure flag, and optional
	 * non-standard HTTP and HTTPS port numbers.
	 * 
	 * @param siteRootUrl
	 * @param anotherSiteName
	 * @param pageFriendlyUri
	 * @param secure
	 * @param nonStandardHttpPort
	 * @param nonStandardHttpsPort
	 */
	protected AbstractPortalURL(String siteRootUrl, String anotherSiteName,
			String pageFriendlyUri, boolean secure, int nonStandardHttpPort,
			int nonStandardHttpsPort) {
		siteRootUrl = checkSiteRootUrl(siteRootUrl);
		anotherSiteName = checkAnotherSiteName(anotherSiteName);
		pageFriendlyUri = checkPageFriendlyUri(pageFriendlyUri);

		mSiteRootUrl = siteRootUrl;
		mAnotherSiteName = anotherSiteName;
		mPageFriendlyUri = pageFriendlyUri;
		mSecure = secure;
		if (nonStandardHttpPort > 0) {
			mNonstandardHttpPort = nonStandardHttpPort;
		}
		if (nonStandardHttpsPort > 0) {
			mNonstandardHttpsPort = nonStandardHttpsPort;
		}
	}

	/**
	 * Checks validity of page friendly URI (no restrictions currently) and
	 * returns normalized value (trimmed if not null, with any consecutive
	 * slashes removed).
	 * 
	 * @param pageFriendlyUri
	 *            page friendly URI as defined in portal console for navigation
	 *            item
	 * @return normalized page friendly URI
	 * @throws IllegalArgumentException
	 *             (should never happen currently)
	 */
	private String checkPageFriendlyUri(String pageFriendlyUri)
			throws IllegalArgumentException {
		if (pageFriendlyUri != null) {
			pageFriendlyUri = pageFriendlyUri.trim();
		}
		return (Utils.slashify(pageFriendlyUri));
	}

	/**
	 * Checks validity of another site name (cannot contain "/") and returns
	 * normalized value (trimmed if not null).
	 * 
	 * @param anotherSiteName
	 * @return
	 * @throws IllegalArgumentException
	 */
	private String checkAnotherSiteName(String anotherSiteName)
			throws IllegalArgumentException {
		if (anotherSiteName != null) {
			if (anotherSiteName.indexOf('/') != -1) {
				throw new IllegalArgumentException(
						"anotherSiteName parameter cannot contain '/' character: "
								+ anotherSiteName);
			}
			anotherSiteName = anotherSiteName.trim();
		}
		return (anotherSiteName);
	}

	/**
	 * Checks validity of site root URL and returns a normalized value. The
	 * validation checks are: it must be non-null; it must be non-blank
	 * (trimmed); it must be either a relative URL (ie starts with "/") or an
	 * absolute URL (ie starts with "http://" or "https://"); and it must
	 * contain a site DNS name (ie "/site/something"). The normalized value is:
	 * trimmed and anything after the site name is removed; also any consecutive
	 * slashes are removed.
	 * 
	 * @param siteRootUrl
	 *            site root URL
	 * @return normalized site root URL (see above)
	 * @throws IllegalArgumentException
	 *             if parameter violates integrity checks (see above)
	 */
	private String checkSiteRootUrl(String siteRootUrl) {
		if (siteRootUrl != null) {
			siteRootUrl = siteRootUrl.trim();
		}
		// validate URL is not null or blank
		if (siteRootUrl == null || siteRootUrl.equals("")) {
			throw new IllegalArgumentException(
					"siteRootUrl parameter cannot be null or empty: "
							+ siteRootUrl);
		}
		// validate URL is relative or well-formed absolute
		int pathBegin = 0;
		if (!siteRootUrl.startsWith("/")) {
			final String protoEndMarker = "://";
			final String https = "https";
			final String http = "http";
			int protoEnd = siteRootUrl.indexOf(protoEndMarker);
			if ((protoEnd == -1)
					|| (!siteRootUrl.substring(0, protoEnd).equalsIgnoreCase(
							http) && !siteRootUrl.substring(0, protoEnd)
							.equalsIgnoreCase(https))) {
				throw new IllegalArgumentException(
						"siteRootUrl parameter must be a relative or absolute HTTP or HTTPS URL: "
								+ siteRootUrl);
			}
			protoEnd += protoEndMarker.length();
			if (protoEnd >= siteRootUrl.length()) {
				throw new IllegalArgumentException(
						"siteRootUrl parameter is a malformed URL: "
								+ siteRootUrl);
			}
			pathBegin = siteRootUrl.indexOf('/', protoEnd);
			if ((pathBegin == -1) || (pathBegin == protoEnd)) {
				throw new IllegalArgumentException(
						"siteRootUrl parameter is a malformedURL: "
								+ siteRootUrl);
			}
		}
		// validate URL contains portal site path (look for /site/name - don't
		// care what comes before or after in path)
		final String siteMarker = "/site/";
		boolean foundSite = false;
		int siteMarkerEnd = siteRootUrl.indexOf(siteMarker, pathBegin);
		if (siteMarkerEnd != -1) {
			siteMarkerEnd += siteMarker.length();
			if (siteMarkerEnd < siteRootUrl.length()) {
				int i = siteRootUrl.indexOf('/', siteMarkerEnd);
				int j = siteRootUrl.indexOf('?', siteMarkerEnd);
				int k = siteRootUrl.indexOf('#', siteMarkerEnd);
				if ((i == -1) || (j > -1 && j < i))
					i = j;
				if ((i == -1) || (k > -1 && k < i))
					i = k;
				int siteNameEnd = i;
				if (siteNameEnd == -1) {
					foundSite = true;
					siteRootUrl += '/';
				} else if (siteNameEnd > siteMarkerEnd) {
					foundSite = true;
					// truncate anything after the site root
					siteRootUrl = siteRootUrl.substring(0, siteNameEnd);
					siteRootUrl += '/';
				}
			}
		}
		if (!foundSite) {
			throw new IllegalArgumentException(
					"siteRootUrl parameter must contain a portal site name: "
							+ siteRootUrl);
		}
		return (Utils.slashify(siteRootUrl));
	}

	/**
	 * Checks validity of portlet friendly ID and returns a trimmed value.
	 * 
	 * @param portletFriendlyId
	 *            portlet friendly ID as defined in portal console
	 * @throws IllegalArgumentException
	 *             if parameter is null, empty or contains underscores
	 */
	private String checkPortletFriendlyId(String portletFriendlyId) {
		if (portletFriendlyId == null || portletFriendlyId.trim().equals("")) {
			throw new IllegalArgumentException(
					"portletFriendlyId cannot be null or empty: "
							+ portletFriendlyId);
		}
		if (portletFriendlyId.indexOf('_') != -1) {
			throw new IllegalArgumentException(
					"portletFriendlyId cannot contain underscores: "
							+ portletFriendlyId);
		}
		return (portletFriendlyId.trim());
	}

	/**
	 * Checks validity of resource ID and returns a trimmed value. This is a
	 * JSR-168 implementation. The resource ID is considered to be a URL
	 * (absolute or relative) for a servlet or static resource file in the
	 * portlet application.
	 * 
	 * @param resourceId
	 *            resource ID requested for the resource URL
	 * @throws IllegalArgumentException
	 *             if parameter is null, empty, or neither a relative nor
	 *             absolute HTTP/HTTPS URL
	 */
	private String checkResourceId(String resourceId) {
		if (resourceId == null || resourceId.trim().equals("")) {
			throw new IllegalArgumentException(
					"resourceId cannot be null or empty: " + resourceId);
		}
		int pathBegin = 0;
		if (!resourceId.startsWith("/")) {
			final String protoEndMarker = "://";
			final String https = "https";
			final String http = "http";
			int protoEnd = resourceId.indexOf(protoEndMarker);
			if ((protoEnd == -1)
					|| (!resourceId.substring(0, protoEnd).equalsIgnoreCase(
							http) && !resourceId.substring(0, protoEnd)
							.equalsIgnoreCase(https))) {
				throw new IllegalArgumentException(
						"resourceId must be a relative or absolute HTTP or HTTPS URL: "
								+ resourceId);
			}
			protoEnd += protoEndMarker.length();
			if (protoEnd >= resourceId.length()) {
				throw new IllegalArgumentException(
						"resourceId is a malformed URL: " + resourceId);
			}
			pathBegin = resourceId.indexOf('/', protoEnd);
			if ((pathBegin == -1) || (pathBegin == protoEnd)) {
				throw new IllegalArgumentException(
						"resourceId parameter is a malformedURL: " + resourceId);
			}
		}
		return (resourceId.trim());
	}

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
	public void setParameter(String paramName, String paramValue) {
		if (paramName == null || paramValue == null) {
			return;
		}
		List<String> portalParamValueList = getPortalParamValueList(paramName);
		portalParamValueList.clear();
		if (paramValue != null) {
			portalParamValueList.add(paramValue);
		}
	}

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
	public void setParameter(String paramName, String[] paramValues) {
		if (paramName == null || paramValues == null) {
			return;
		}
		for (int i = 0; i < paramValues.length; i++) {
			if (paramValues[i] == null) {
				return;
			}
		}
		List<String> portalParamValueList = getPortalParamValueList(paramName);
		portalParamValueList.clear();
		if (paramValues != null) {
			portalParamValueList.addAll(Arrays.asList(paramValues));
		}
	}

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
	public void setParameters(Map params) {
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			try {
				Map.Entry incomingParam = (Map.Entry) it.next();
				String incomingParamName = (String) incomingParam.getKey();
				if (incomingParam.getValue() == null
						|| incomingParam.getValue() instanceof String) {
					setParameter(incomingParamName, (String) incomingParam
							.getValue());
				} else if (incomingParam.getValue() instanceof String[]) {
					setParameter(incomingParamName, (String[]) incomingParam
							.getValue());
				}
			} catch (ClassCastException e) { // continue to next
			}
		}
	}

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
	 * <p>
	 * <b>Note:</b> This parameter will be set as an action parameter when an
	 * action URL is set (only in the case of remote portlets). If a resource
	 * URL is set, the result of setting parameters is undefined (this is a
	 * JSR-168 implementation of resource URL, not JSR-286).
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValue
	 *            the parameter value
	 */
	public void setParameter(String portletFriendlyId, String paramName,
			String paramValue) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(
				portletFriendlyId, paramName, false);
		portletParamValueList.clear();
		if (paramValue != null) {
			portletParamValueList.add(paramValue);
		}
	}

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
	 * <b>Note:</b> This method is for setting portlet <i>private</i> render
	 * parameters. To pass <i>public</i> render parameters to portlets instead,
	 * use a method like {@link #setPublicParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * <b>Note:</b> These parameters will be set as action parameters when an
	 * action URL is set (only in the case of remote portlets). If a resource
	 * URL is set, the result of setting parameters is undefined (this is a
	 * JSR-168 implementation of resource URL, not JSR-286).
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValues
	 *            an array of the parameter values
	 */
	public void setParameter(String portletFriendlyId, String paramName,
			String[] paramValues) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(
				portletFriendlyId, paramName, false);
		portletParamValueList.clear();
		if (paramValues != null) {
			portletParamValueList.addAll(Arrays.asList(paramValues));
		}
	}

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
	 * <b>Note:</b> This method is for setting portlet <i>private</i> render
	 * parameters. To pass <i>public</i> render parameters to portlets instead,
	 * use a method like {@link #setPublicParameter(String, String, String)}.
	 * To pass <i>portal</i> query parameters, use a method like
	 * {@link #setParameter(String, String)}.
	 * <p>
	 * <b>Note:</b> These parameters will be set as action parameters when an
	 * action URL is set (only in the case of remote portlets). If a resource
	 * URL is set, the result of setting parameters is undefined (this is a
	 * JSR-168 implementation of resource URL, not JSR-286).
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param params
	 *            a map of parameter names (strings) to parameter values
	 *            (strings or arrays of strings)
	 */
	public void setParameters(String portletFriendlyId, Map params) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			try {
				Map.Entry incomingParam = (Map.Entry) it.next();
				String incomingParamName = (String) incomingParam.getKey();
				if (incomingParam.getValue() == null
						|| incomingParam.getValue() instanceof String) {
					setParameter(portletFriendlyId, incomingParamName,
							(String) incomingParam.getValue());
				} else if (incomingParam.getValue() instanceof String[]) {
					setParameter(portletFriendlyId, incomingParamName,
							(String[]) incomingParam.getValue());
				}
			} catch (ClassCastException e) { // continue to next
			}
		}
	}

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
	 * you set your <code>PortalURL</code> to be a resource or action URL.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValue
	 *            the parameter value
	 */
	public void setPublicParameter(String portletFriendlyId, String paramName,
			String paramValue) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(
				portletFriendlyId, paramName, true);
		portletParamValueList.clear();
		if (paramValue != null) {
			portletParamValueList.add(paramValue);
		}
	}

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
	 * you set your <code>PortalURL</code> to be a resource or action URL.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param paramName
	 *            the parameter name
	 * @param paramValues
	 *            an array of the parameter values
	 */
	public void setPublicParameter(String portletFriendlyId, String paramName,
			String[] paramValues) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(
				portletFriendlyId, paramName, true);
		portletParamValueList.clear();
		if (paramValues != null) {
			portletParamValueList.addAll(Arrays.asList(paramValues));
		}
	}

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
	 */
	public void setPublicParameters(String portletFriendlyId, Map params) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			try {
				Map.Entry incomingParam = (Map.Entry) it.next();
				String incomingParamName = (String) incomingParam.getKey();
				if (incomingParam.getValue() == null
						|| incomingParam.getValue() instanceof String) {
					setPublicParameter(portletFriendlyId, incomingParamName,
							(String) incomingParam.getValue());
				} else if (incomingParam.getValue() instanceof String[]) {
					setPublicParameter(portletFriendlyId, incomingParamName,
							(String[]) incomingParam.getValue());
				}
			} catch (ClassCastException e) { // continue to next
			}
		}
	}

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
	 */
	public void setWindowState(String portletFriendlyId, WindowState windowState) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		if (windowState == null) {
			windowState = WindowState.NORMAL;
		}
		getPortletParameters(portletFriendlyId).setWindowState(windowState);
	}

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
	public void setPortletMode(String portletFriendlyId, PortletMode portletMode) {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		if (portletMode == null) {
			portletMode = PortletMode.VIEW;
		}
		getPortletParameters(portletFriendlyId).setPortletMode(portletMode);
	}

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
	 *             if action URL has already been set
	 */
	public void setAsActionURL(String portletFriendlyId)
			throws IllegalStateException {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		if (mActionPortletFriendlyId != null) {
			throw new IllegalStateException(
					"Target portlet for action URL has already been set to "
							+ mActionPortletFriendlyId);
		}
		if (mResourcePortletFriendlyId != null) {
			throw new IllegalStateException(
					"Target portlet for resource URL has already been set to "
							+ mResourcePortletFriendlyId);
		}
		mActionPortletFriendlyId = portletFriendlyId;
	}

	/**
	 * Makes this <code>PortalURL</code> a resource URL for the given resource
	 * ID, targeted at the portlet with the given <tt>portletFriendlyId</tt>.
	 * The current implementation is JSR-168 compliant but does not support
	 * JSR-286 resource serving.
	 * <p>
	 * The given resource ID would typically be a URL for a servlet, image, or
	 * other static file inside the portlet application.
	 * <ul>
	 * <li>A relative URL will be assumed to be relative to the portal server,
	 * so provide an absolute URL if your portlet may be remoted.</li>
	 * <li>The resource ID must already contain whatever parameters and
	 * formatting the resource needs - eg if the resource is a servlet which
	 * expects to find request parameters in its query string, you should add
	 * that query string to the resource ID before passing it to this method.</li>
	 * </ul>
	 * <p>
	 * You can use either this method or {@link #setAsActionURL(String)} at most
	 * once with any one <code>PortalURL</code> object, since setting a
	 * resource URL or action URL changes the state of the object irreversibly.
	 * <p>
	 * Any parameters (private or public), portlet mode or window state you set
	 * (eg using {@link #setParameter(String, String)} or
	 * {@link #setPortletMode(String, PortletMode)}) will have undefined
	 * results.
	 * 
	 * @param portletFriendlyId
	 *            the portlet friendly ID as configured in the portal console
	 * @param resourceID
	 *            the resource ID (JSR-286) or servlet or static resource URL
	 *            path (JSR-168) for the resource to serve at that portlet
	 * @throws IllegalStateException
	 *             if action or resource URL has already been set
	 * @since SPF 1.1
	 */
	public void setAsResourceURL(String portletFriendlyId, String resourceId)
			throws IllegalStateException {
		portletFriendlyId = checkPortletFriendlyId(portletFriendlyId);
		resourceId = checkResourceId(resourceId);
		if (mActionPortletFriendlyId != null) {
			throw new IllegalStateException(
					"Target portlet for action URL has already been set to "
							+ mActionPortletFriendlyId);
		}
		if (mResourcePortletFriendlyId != null) {
			throw new IllegalStateException(
					"Target portlet for resource URL has already been set to "
							+ mResourcePortletFriendlyId);
		}
		mResourcePortletFriendlyId = portletFriendlyId;
		mResourceId = resourceId;
	}

	// //////////////////////////////////////////////////////////////////////////////

	private List<String> getPortletParamValueList(String portletFriendlyId,
			String paramName, boolean isPublic) {
		PortletParameters portletPararameters = getPortletParameters(portletFriendlyId);

		Map<String, List<String>> singleTypeParameters = (isPublic ? portletPararameters
				.getPublicParameters()
				: portletPararameters.getPrivateParameters());

		List<String> portletParameterValues = singleTypeParameters
				.get(paramName);
		if (portletParameterValues == null) {
			portletParameterValues = new ArrayList<String>(1);
			singleTypeParameters.put(paramName, portletParameterValues);
		}

		return portletParameterValues;
	}

	private PortletParameters getPortletParameters(String portletFriendlyId) {
		PortletParameters portletPararameters = mPortletParameters
				.get(portletFriendlyId);
		if (portletPararameters == null) {
			portletPararameters = new PortletParameters();
			mPortletParameters.put(portletFriendlyId, portletPararameters);
		}
		return portletPararameters;
	}

	// added method for portal parameter support - DSJ 2009/1/28
	private List<String> getPortalParamValueList(String paramName) {
		List<String> portalParameterValues = mPortalParameters.get(paramName);
		if (portalParameterValues == null) {
			portalParameterValues = new ArrayList<String>(1);
			mPortalParameters.put(paramName, portalParameterValues);
		}
		return portalParameterValues;
	}

	/**
	 * Creates the site root URL string for a render, action or resource
	 * <code>PortalURL</code>.
	 * 
	 * @return the site root URL, ready for building into a base URL.
	 */
	protected StringBuilder createSiteRootUrl() {

		StringBuilder result = new StringBuilder();

		// if the home URL contains protocol we will rewrite it to reflect the
		// mSecure flag - note this parsing assumes the validations done by the
		// constructor in the checkSiteRootUrl method (see) - note that
		// rewriting the URL includes setting the scheme consistent with the
		// mSecure flag and setting the port to any non-standard port.
		int nonStandardPort = 0;
		int pathBegin = 0;
		int protoEnd = 0;
		boolean switchScheme = false;
		if (!mSiteRootUrl.startsWith("/")) {
			final String protoEndMarker = "://";
			final String https = "https";
			final String http = "http";
			protoEnd = mSiteRootUrl.indexOf(protoEndMarker);
			String proto = mSiteRootUrl.substring(0, protoEnd);
			if (mSecure && !https.equals(proto)) {
				result.append(https);
				switchScheme = true;
				nonStandardPort = mNonstandardHttpsPort;
			} else if (!mSecure && !http.equals(proto)) {
				result.append(http);
				switchScheme = true;
				nonStandardPort = mNonstandardHttpPort;
			} else {
				result.append(proto);
			}
			result.append(protoEndMarker);
			protoEnd += protoEndMarker.length();
			pathBegin = mSiteRootUrl.indexOf('/', protoEnd);
			String hostAndPort = mSiteRootUrl.substring(protoEnd, pathBegin);
			if (switchScheme) {
				int portMarker = hostAndPort.indexOf(':');
				if (portMarker == -1) {
					result.append(hostAndPort);
				} else {
					result.append(hostAndPort.substring(0, portMarker));
				}
				if (nonStandardPort > 0) {
					result.append(':');
					result.append(nonStandardPort);
				}
			} else {
				result.append(hostAndPort);
			}
		}

		// now rework the home URL to refer to the alternate site name if given
		// note this parsing assumes the validations done by the constructor in
		// the checkSiteRootUrl method (see)
		if (mAnotherSiteName == null) {
			result.append(mSiteRootUrl.substring(pathBegin));
		} else {
			final String siteMarker = "/site/";
			int sitePos = mSiteRootUrl.indexOf(siteMarker);
			sitePos += siteMarker.length();
			result.append(mSiteRootUrl.substring(pathBegin, sitePos));
			result.append(mAnotherSiteName);
		}

		// terminate site root URL with "/" character
		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}
		return (result);
	}

	/**
	 * Creates a query string containing any portal parameters, including the
	 * initial '?' character. If no portal parameters were set, then an empty
	 * string is returned.
	 * 
	 * @return the portal query string (including '?'), or empty
	 */
	protected StringBuilder createPortalQueryString() {

		StringBuilder result = new StringBuilder();

		// added following block for portal parameter support - DSJ 2009/1/28
		boolean portalParametersSpecified = !mPortalParameters.isEmpty();
		if (portalParametersSpecified) {
			result.append("?");
			Iterator<Map.Entry<String, List<String>>> portalParameterEntries = mPortalParameters
					.entrySet().iterator();
			while (portalParameterEntries.hasNext()) {
				Map.Entry<String, List<String>> portalParameters = portalParameterEntries
						.next();
				addParameters(result, portalParameters);
			}
			result.deleteCharAt(result.length() - 1); // snip off final '&'
		}
		return (result);
	}

	/**
	 * Creates a query string for portlet action or render. The query string
	 * contains any necessary portlet action or render targets, parameters,
	 * modes or window states, including the initial '?' character. If no
	 * portlet action, parameters, modes or window states were set, or this is a
	 * resource URL, then an empty string is returned.
	 * 
	 * @return the portlet render or action query string (including '?'), or
	 *         empty
	 */
	protected StringBuilder createRenderOrActionQueryString() {

		boolean isActionUrl = mActionPortletFriendlyId != null;
		boolean isResourceUrl = mResourcePortletFriendlyId != null;
		boolean isRenderUrl = !isActionUrl && !isResourceUrl;
		boolean portletParametersSpecified = !mPortletParameters.isEmpty();
		StringBuilder result = new StringBuilder();
		Iterator<Map.Entry<String, PortletParameters>> portletParameterEntries = mPortletParameters
				.entrySet().iterator();

		if (isActionUrl) {
			// if this is an action URL we mark it with javax.portlet.action
			// parameter and we have to specify the target portlet for the
			// action (there is always only one) with ".tpst" parameter
			result.append('?');
			result.append("javax.portlet.action=true");
			result.append('&').append(PARAM_NAME_PREFIX).append(".tpst=")
					.append(mActionPortletFriendlyId);
		} else if (isRenderUrl && portletParametersSpecified) {
			// if this is a render URL containing parameters, we take the first
			// portlet from the list and make it the target for this request
			// using ".tpst" parameter; once we have the target we add the rest
			// for this portlet (state, mode, public and private parameters)
			Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries
					.next();
			String portletFriendlyId = portletParameters.getKey();
			result.append('?');
			result.append(PARAM_NAME_PREFIX).append(".tpst=").append(
					portletFriendlyId);
			addStateAndModeToUrlFragment(result, portletParameters.getValue());
			addPublicParameters(result, portletParameters, portletFriendlyId);
			addPrivateParameters(result, portletParameters, portletFriendlyId,
					false);
		}

		if ((isRenderUrl && portletParametersSpecified) || isActionUrl) {
			result.append("&javax.portlet.begCacheTok=com.vignette.cachetoken");
			// we now have to add the remaining portlet information unless this
			// is a render URL with no remaining portlet information.
			while (portletParameterEntries.hasNext()) {
				Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries
						.next();
				String portletFriendlyId = portletParameters.getKey();
				addWindowStateAndPortletMode(result, portletFriendlyId,
						portletParameters.getValue());
				addPublicParameters(result, portletParameters,
						portletFriendlyId);
				addPrivateParameters(result, portletParameters,
						portletFriendlyId, isActionUrl);
			}
			result.append("&javax.portlet.endCacheTok=com.vignette.cachetoken");
		}
		return (result);
	}

	/**
	 * Creates the URL string for a render or action <code>PortalURL</code>,
	 * including the site root URL plus any needed friendly URL, secondary page,
	 * portal parameters, or portlet parameters. If this <code>PortalURL</code>
	 * is set as a resource URL, this returns empty.
	 * 
	 * @return the render or action URL, or empty if this object is a resource
	 *         URL
	 */
	protected StringBuilder createRenderOrActionUrl() {

		boolean isActionUrl = mActionPortletFriendlyId != null;
		boolean isResourceUrl = mResourcePortletFriendlyId != null;
		boolean isRenderUrl = !isActionUrl && !isResourceUrl;
		boolean portletParametersSpecified = !mPortletParameters.isEmpty();
		StringBuilder result = new StringBuilder();
		if (isResourceUrl) {
			return (result);
		}

		// get the site root URL string and make sure it ends with '/'
		result.append(createSiteRootUrl());
		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}

		// if this is a portlet action URL or there are portlet parameters,
		// specify the necessary Vignette page template
		if (isActionUrl || (isRenderUrl && portletParametersSpecified)) {
			// WindowState.MAXIMIZED uses a special secondary page
			Iterator<PortletParameters> portletParameters = mPortletParameters
					.values().iterator();
			if (portletParameters.hasNext()
					&& WindowState.MAXIMIZED.equals(portletParameters.next()
							.getWindowState())) {
				result.append("template.MAXIMIZE/");
			} else {
				result.append("template.PAGE/");
			}
			if (isActionUrl) {
				result.append("action.process/");
			}
		}

		// if there is a page friendly URI, put that next
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

		// terminate portal URL path with a "/" character
		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}

		// merge and add portal parameters and/or portlet parameters, if any
		result.append(mergeQueryStrings(createPortalQueryString(),
				createRenderOrActionQueryString()));

		// finished creating render or action URL
		return (result);
	}

	/**
	 * Creates the URL string for a portlet resource <code>PortalURL</code>,
	 * including the site root URL plus any needed friendly URL, secondary page,
	 * portal parameters, and portlet resource ID. If this
	 * <code>PortalURL</code> is not set as a resource URL, this returns
	 * empty.
	 * <p>
	 * The implementation of a resource URL is quite different between Vignette
	 * local and remote portlets, so this is an abstract method.
	 * 
	 * @return the resource URL, or empty if this object is not a resource URL
	 */
	protected abstract StringBuilder createResourceUrl();

	// added method for portal parameter support - DSJ 2009/1/28
	private void addParameters(StringBuilder result,
			Map.Entry<String, List<String>> parameters) {
		String paramName = parameters.getKey();
		if (paramName != null) {
			for (String paramValue : parameters.getValue()) {
				if (paramValue != null) {
					try {
						result.append(URLEncoder.encode(paramName, "UTF-8"));
						result.append('=');
						result.append(URLEncoder.encode(paramValue, "UTF-8"));
						result.append('&');
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(
								"UTF-8 encoding not supported? " + e, e);
					}
				}
			}
		}
	}

	public String urlToString() {
		return toString();
	}

	protected abstract void addPrivateParameters(StringBuilder result,
			Map.Entry<String, PortletParameters> portletParameters,
			String portletFriendlyId, boolean isActionURL);

	protected abstract void addPublicParameters(StringBuilder result,
			Map.Entry<String, PortletParameters> portletParameters,
			String portletFriendlyId);

	protected void addWindowStateAndPortletMode(StringBuilder result,
			String portletFriendlyId, PortletParameters portletParameters) {
		if (!WindowState.NORMAL.equals(portletParameters.getWindowState())
				|| !PortletMode.VIEW.equals(portletParameters.getPortletMode())) {
			result.append('&').append(PARAM_NAME_PREFIX).append(".pst=");
			result.append(portletFriendlyId);
			addStateAndModeToUrlFragment(result, portletParameters);
		}
	}

	private void addStateAndModeToUrlFragment(StringBuilder result,
			PortletParameters portletParameters) {
		appendWindowStateToUrlFragment(portletParameters.getWindowState(),
				result);
		appendPortletModeToUrlFragment(portletParameters.getPortletMode(),
				result);
	}

	// FIXME (slawek) - how to implement custom window states ???
	private void appendWindowStateToUrlFragment(WindowState windowState,
			StringBuilder buf) {
		// NORMAL window state is not present in the URL as it's default
		if (WindowState.MAXIMIZED.equals(windowState)) {
			buf.append("_ws_MX");
		} else if (WindowState.MINIMIZED.equals(windowState)) {
			buf.append("_ws_MN");
		}
	}

	// FIXME (slawek) - how to implement custom portlet modes ???
	private void appendPortletModeToUrlFragment(PortletMode portletMode,
			StringBuilder buf) {
		// VIEW portlet mode is not present in the URL as it's default
		if (PortletMode.EDIT.equals(portletMode)) {
			buf.append("_pm_ED");
		} else if (PortletMode.HELP.equals(portletMode)) {
			buf.append("_pm_HP");
		}
	}

	/**
	 * Merges 2 query strings. Each given query string is assumed to either be
	 * null or empty, or a valid query string - beginning with '?' and
	 * containing at least one name-value pair.
	 */
	protected StringBuilder mergeQueryStrings(StringBuilder q1, StringBuilder q2) {
		if ((q1 == null) || (q1.length() == 0)) {
			return (q2);
		}
		if ((q2 == null) || (q2.length() == 0)) {
			return (q1);
		}
		q2.setCharAt(0, '&');
		q1.append(q2);
		return (q1);
	}

	/**
	 * Gets the string representation of this <code>PortalURL</code>, which
	 * is a URL string containing all of the relevant parameters set in the
	 * constructor and subsequent method calls. The string will be
	 * Vignette-compliant and will be ready to use in hyperlinks, form actions,
	 * redirects, etc.
	 */
	@Override
	public String toString() {
		boolean isResourceUrl = mResourcePortletFriendlyId != null;
		if (isResourceUrl) {
			return createResourceUrl().toString();
		} else {
			return createRenderOrActionUrl().toString();
		}
	}

	/**
	 * Captures the information such as private and public parameters, window
	 * state and mode for a single portlet.
	 */
	protected class PortletParameters {
		private Map<String, List<String>> mPrivateParameters = new LinkedHashMap<String, List<String>>();;
		private Map<String, List<String>> mPublicParameters = new LinkedHashMap<String, List<String>>();
		private WindowState mWindowState = WindowState.NORMAL;
		private PortletMode mPortletMode = PortletMode.VIEW;

		public Map<String, List<String>> getPrivateParameters() {
			return mPrivateParameters;
		}

		public Map<String, List<String>> getPublicParameters() {
			return mPublicParameters;
		}

		private void setWindowState(WindowState windowState) {
			mWindowState = windowState;
		}

		public WindowState getWindowState() {
			return mWindowState;
		}

		private void setPortletMode(PortletMode portletMode) {
			mPortletMode = portletMode;
		}

		public PortletMode getPortletMode() {
			return mPortletMode;
		}
	}
}
