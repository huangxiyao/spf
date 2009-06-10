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
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
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
	 * returns normalized value (trimmed if not null).
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
		return (pageFriendlyUri);
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
	 * trimmed and anything after the site name is removed.
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
	 * with the given <tt>portletFriendlyId</tt>. You can only do this once
	 * with any one <code>PortalURL</code> object, since setting an action URL
	 * irreversibly changes the state of the object.
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
		mActionPortletFriendlyId = portletFriendlyId;
	}

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
	 * Creates the base URL for the <code>PortalURL</code> - specifically,
	 * creates the portal URL itself, without any portlet-targeted data set yet.
	 * 
	 * @param isActionUrl
	 *            indicates whether to build a portlet action URL or not
	 * @return the base (portal) URL, ready to add portlet-targeted data
	 */
	protected StringBuilder createBaseUrl(boolean isActionUrl) {

		StringBuilder result = new StringBuilder();

		// if the home URL contains protocol we will rewrite it to reflect the
		// mSecure flag - note this parsing assumes the validations done by the
		// constructor in the checkSiteRootUrl method (see) - note that
		// rewriting
		// the URL includes setting the scheme consistent with the mSecure flag
		// and setting the port to any non-standard port.
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

		// if this is a portlet action URL or there are portlet parameters,
		// specify the necessary Vignette page template
		if (isActionUrl || !mPortletParameters.isEmpty()) {
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

		// if there is a page friendly URI, put that next (note this could
		// conflict with the above; we will assume the caller knows what he is
		// doing)
		if (mPageFriendlyUri != null) {
			if (mPageFriendlyUri.charAt(0) == '/') {
				result.append(mPageFriendlyUri.subSequence(1, mPageFriendlyUri
						.length()));
			} else {
				result.append(mPageFriendlyUri);
			}
		}

		// terminate portal URL path with a "/" character
		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}

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

		return result;
	}

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

	private void addWindowStateAndPortletMode(StringBuilder result,
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
	 * Gets the string representation of this <code>PortalURL</code>, which
	 * is a URL string containing all of the parameters set in the constructor
	 * and subsequent method calls.
	 */
	@Override
	public String toString() {
		boolean isActionUrl = mActionPortletFriendlyId != null;
		boolean portletParametersSpecified = !mPortletParameters.isEmpty();

		// Get the base portal URL for this PortalURL object.
		StringBuilder result = createBaseUrl(isActionUrl);
		boolean queryStarted = result.indexOf("?") >= 0;

		Iterator<Map.Entry<String, PortletParameters>> portletParameterEntries = mPortletParameters
				.entrySet().iterator();

		if (isActionUrl) {
			// if this is an action URL we mark it with javax.portlet.action
			// parameter and we have
			// to specify the target portlet for the action (there is always
			// only one) with ".tpst" parameter

			// added following for portal parameter support - DSJ 2009/1/28
			if (!queryStarted) {
				result.append('?');
			} else {
				result.append('&');
			}
			result.append("javax.portlet.action=true");
			// result.append("?javax.portlet.action=true");
			// end DSJ 2009/1/28
			result.append('&').append(PARAM_NAME_PREFIX).append(".tpst=")
					.append(mActionPortletFriendlyId);
		} else if (portletParameterEntries.hasNext()) {

			// If this is not an action URL we take the first portlet from the
			// list and make it
			// as the target for this request using ".tpst" parameter

			Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries
					.next();
			String portletFriendlyId = portletParameters.getKey();

			// added following line for portal parameter support - DSJ 2009/1/28
			// added following for portal parameter support - DSJ 2009/1/28
			if (!queryStarted) {
				result.append('?');
			} else {
				result.append('&');
			}
			result.append(PARAM_NAME_PREFIX).append(".tpst=").append(
					portletFriendlyId);
			// result.append('?').append(PARAM_NAME_PREFIX).append(".tpst=").append(portletFriendlyId);
			// end DSJ 2009/1/28

			// once we have the target we add the rest for this portlet (state,
			// mode, public and private parameters)

			addStateAndModeToUrlFragment(result, portletParameters.getValue());
			addPublicParameters(result, portletParameters, portletFriendlyId);
			addPrivateParameters(result, portletParameters, portletFriendlyId, false);
		}

		if (portletParametersSpecified || isActionUrl) {
			result.append("&javax.portlet.begCacheTok=com.vignette.cachetoken");

			// If we have more than one portlet in this URL we now have to add
			// its information
			// (state, mode, public and private parameters) between VAP's marker
			// parameters

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

		return (result.toString());
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
