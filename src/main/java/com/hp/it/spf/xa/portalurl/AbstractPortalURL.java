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

/**
 * Abstract class capturing all the {@link PortalURL} characteristics and providing a template
 * method for the URL rendering. This class will create Vignette-specific URLs as the classes
 * inherit from it. It reproduces the URL format as expected by Vignette.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
abstract class AbstractPortalURL implements PortalURL
{
	protected static final String PARAM_NAME_PREFIX = "spf_p";

	protected String mSiteRootUrl = null;
	protected String mAnotherSiteName = null;
	protected String mPageFriendlyUri = null;
	protected boolean mSecure;

	protected Map<String, PortletParameters> mPortletParameters = new LinkedHashMap<String, PortletParameters>();
	protected String mActionPortletFriendlyId = null;

	// added attribute for portal parameter support - DSJ 2009/1/28
	protected Map<String, List<String>> mPortalParameters = new LinkedHashMap<String, List<String>>();

	protected AbstractPortalURL(String siteRootUrl, String anotherSiteName, String pageFriendlyUri, boolean secure)
	{
		checkSiteRootUrl(siteRootUrl);
		checkPageFriendlyUri(pageFriendlyUri);

		mSiteRootUrl = siteRootUrl;
		mAnotherSiteName = anotherSiteName;
		mPageFriendlyUri = pageFriendlyUri;
		mSecure = secure;
	}

	/**
	 * Checks validity of page friendly URI.
	 * @param pageFriendlyUri page friendly URI as defined in portal console for navigation item
	 * @throws IllegalArgumentException if parameter is null or empty
	 */
	private void checkPageFriendlyUri(String pageFriendlyUri) throws IllegalArgumentException {
		if (pageFriendlyUri == null || pageFriendlyUri.trim().equals("")) {
			throw new IllegalArgumentException("pageFriendlyUri parameter cannot be null or empty: " + pageFriendlyUri);
		}
	}

	/**
	 * Checks validity of site root URL.
	 * @param siteRootUrl site root URL
	 * @throws IllegalArgumentException if parameter is null or empty
	 */
	private void checkSiteRootUrl(String siteRootUrl) {
		if (siteRootUrl == null || siteRootUrl.trim().equals("")) {
			throw new IllegalArgumentException("siteRootUrl parameter cannot be null or empty: " + siteRootUrl);
		}
	}

	/**
	 * Checks validity of portlet friendly ID.
	 * @param portletFriendlyId portlet friendly ID as defined in portal console
	 * @throws IllegalArgumentException if parameter is null or empty
	 */
	private void checkPortletFriendlyId(String portletFriendlyId) {
		if (portletFriendlyId == null || portletFriendlyId.trim().equals("")) {
			throw new IllegalArgumentException("portletFriendlyId cannot be null or empty: " + portletFriendlyId);
		}
	}

	// added method for portal parameter support - DSJ 2009/1/28
	public void setParameter(String paramName, String paramValue)
	{
		List<String> portalParamValueList = getPortalParamValueList(paramName);
		portalParamValueList.clear();
		if (paramValue != null) {
			portalParamValueList.add(paramValue);
		}
	}
	
	// added method for portal parameter support - DSJ 2009/1/28
	public void setParameter(String paramName, String[] paramValues)
	{
		List<String> portalParamValueList = getPortalParamValueList(paramName);
		portalParamValueList.clear();
		if (paramValues != null) {
			portalParamValueList.addAll(Arrays.asList(paramValues));
		}
	}
	
	// added method for portal parameter support - DSJ 2009/1/28
	public void setParameters(Map params)
	{
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry incomingParam = (Map.Entry) it.next();
			String incomingParamName = (String) incomingParam.getKey();
			if (incomingParam.getValue() == null || incomingParam.getValue() instanceof String) {
				setParameter(incomingParamName, (String) incomingParam.getValue());
			}
			else if (incomingParam.getValue() instanceof String[]) {
				setParameter(incomingParamName, (String[]) incomingParam.getValue());
			}
		}		
	}
	
	public void setParameter(String portletFriendlyId, String paramName, String paramValue)
	{
		checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, false);
		portletParamValueList.clear();
		if (paramValue != null) {
			portletParamValueList.add(paramValue);
		}
	}

	public void setParameter(String portletFriendlyId, String paramName, String[] paramValues)
	{
		checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, false);
		portletParamValueList.clear();
		if (paramValues != null) {
			portletParamValueList.addAll(Arrays.asList(paramValues));
		}
	}

	public void setParameters(String portletFriendlyId, Map params)
	{
		checkPortletFriendlyId(portletFriendlyId);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry incomingParam = (Map.Entry) it.next();
			String incomingParamName = (String) incomingParam.getKey();
			if (incomingParam.getValue() == null || incomingParam.getValue() instanceof String) {
				setParameter(portletFriendlyId, incomingParamName, (String) incomingParam.getValue());
			}
			else if (incomingParam.getValue() instanceof String[]) {
				setParameter(portletFriendlyId, incomingParamName, (String[]) incomingParam.getValue());
			}
		}
	}

	public void setPublicParameter(String portletFriendlyId, String paramName, String paramValue)
	{
		checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, true);
		portletParamValueList.clear();
		if (paramValue != null) {
			portletParamValueList.add(paramValue);
		}
	}

	public void setPublicParameter(String portletFriendlyId, String paramName, String[] paramValues)
	{
		checkPortletFriendlyId(portletFriendlyId);
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, true);
		portletParamValueList.clear();
		if (paramValues != null) {
			portletParamValueList.addAll(Arrays.asList(paramValues));
		}
	}

	public void setPublicParameters(String portletFriendlyId, Map params)
	{
		checkPortletFriendlyId(portletFriendlyId);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry incomingParam = (Map.Entry) it.next();
			String incomingParamName = (String) incomingParam.getKey();
			if (incomingParam.getValue() == null || incomingParam.getValue() instanceof String) {
				setPublicParameter(portletFriendlyId, incomingParamName, (String) incomingParam.getValue());
			}
			else if (incomingParam.getValue() instanceof String[]) {
				setPublicParameter(portletFriendlyId, incomingParamName, (String[]) incomingParam.getValue());
			}
		}
	}

	public void setWindowState(String portletFriendlyId, WindowState windowState)
	{
		checkPortletFriendlyId(portletFriendlyId);
		if (windowState == null) {
			windowState = WindowState.NORMAL;
		}
		getPortletParameters(portletFriendlyId).setWindowState(windowState);
	}

	public void setPortletMode(String portletFriendlyId, PortletMode portletMode)
	{
		checkPortletFriendlyId(portletFriendlyId);
		if (portletMode == null) {
			portletMode = PortletMode.VIEW;
		}
		getPortletParameters(portletFriendlyId).setPortletMode(portletMode);
	}

	public void setAsActionURL(String portletFriendlyId) throws IllegalStateException
	{
		checkPortletFriendlyId(portletFriendlyId);
		if (mActionPortletFriendlyId != null) {
			throw new IllegalStateException("Target portlet for action URL has already been set to " + mActionPortletFriendlyId);
		}
		mActionPortletFriendlyId = portletFriendlyId;
	}

	private List<String> getPortletParamValueList(String portletFriendlyId, String paramName, boolean isPublic)
	{
		PortletParameters portletPararameters = getPortletParameters(portletFriendlyId);

		Map<String, List<String>> singleTypeParameters =
				(isPublic ? portletPararameters.getPublicParameters() : portletPararameters.getPrivateParameters());

		List<String> portletParameterValues = singleTypeParameters.get(paramName);
		if (portletParameterValues == null) {
			portletParameterValues = new ArrayList<String>(1);
			singleTypeParameters.put(paramName, portletParameterValues);
		}

		return portletParameterValues;
	}

	private PortletParameters getPortletParameters(String portletFriendlyId)
	{
		PortletParameters portletPararameters = mPortletParameters.get(portletFriendlyId);
		if (portletPararameters == null) {
			portletPararameters = new PortletParameters();
			mPortletParameters.put(portletFriendlyId, portletPararameters);
		}
		return portletPararameters;
	}

	// added method for portal parameter support - DSJ 2009/1/28
	private List<String> getPortalParamValueList(String paramName)
	{
		List<String> portalParameterValues = mPortalParameters.get(paramName);
		if (portalParameterValues == null) {
			portalParameterValues = new ArrayList<String>(1);
			mPortalParameters.put(paramName, portalParameterValues);
		}
		return portalParameterValues;
	}
	
	protected StringBuilder createBaseUrl(boolean isActionUrl)
	{
		StringBuilder result = new StringBuilder();

		int protoEnd = mSiteRootUrl.indexOf(':');
		// if the home URL contains protocol we will rework it to reflect the mSecure flag
		if (protoEnd > 0) {
			String proto = mSiteRootUrl.substring(0, protoEnd);
			if (mSecure && !"https".equals(proto)) {
				result.append("https");
			}
			else if (!mSecure && !"http".equals(proto)) {
				result.append("http");
			}
			else {
				result.append(proto);
			}
		}
		else {
			protoEnd = 0;
		}

		if (mAnotherSiteName == null) {
			result.append(mSiteRootUrl.substring(protoEnd));
		}
		else {
			final String siteMarker = "/site/";
			int sitePos = mSiteRootUrl.indexOf(siteMarker);
			if (sitePos == -1) {
				throw new IllegalArgumentException("Unable to find '/site/' in the URL: " + mSiteRootUrl);
			}
			sitePos += siteMarker.length();
			result.append(mSiteRootUrl.substring(protoEnd, sitePos));
			result.append(mAnotherSiteName);
		}

		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}

		if (isActionUrl || !mPortletParameters.isEmpty()) {
			// WindowState.MAXIMIZED uses a special secondary page
			Iterator<PortletParameters> portletParameters = mPortletParameters.values().iterator();
			if (portletParameters.hasNext() && WindowState.MAXIMIZED.equals(portletParameters.next().getWindowState())) {
				result.append("template.MAXIMIZE/");
			}
			else {
				result.append("template.PAGE/");
			}

			if (isActionUrl) {
				result.append("action.process/");
			}
		}

		if (mPageFriendlyUri.charAt(0) == '/') {
			result.append(mPageFriendlyUri.subSequence(1, mPageFriendlyUri.length()));
		}
		else {
			result.append(mPageFriendlyUri);
		}

		if (result.charAt(result.length() - 1) != '/') {
			result.append('/');
		}

		// added following block for portal parameter support - DSJ 2009/1/28
		boolean portalParametersSpecified = !mPortalParameters.isEmpty();
		if (portalParametersSpecified) {
			result.append("?");
			Iterator<Map.Entry<String, List<String>>> portalParameterEntries = mPortalParameters.entrySet().iterator();
			while (portalParameterEntries.hasNext()) {
				Map.Entry<String, List<String>> portalParameters = portalParameterEntries.next();
				addParameters(result, portalParameters);
			}
			result.deleteCharAt(result.length() - 1); // snip off final '&'
		}

		return result;
	}

	// added method for portal parameter support - DSJ 2009/1/28
	private void addParameters(StringBuilder result, Map.Entry<String, List<String>> parameters)
	{
		String paramName = parameters.getKey();
		for (String paramValue : parameters.getValue()) {
			try {
				result.append(URLEncoder.encode(paramName, "UTF-8"));
				result.append('=');
				result.append(URLEncoder.encode(paramValue, "UTF-8"));
				result.append('&');
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UTF-8 encoding not supported? " + e, e);
			}
		}
	}

	public String urlToString() {
		return toString();
	}

	protected abstract void addPrivateParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId);

	protected abstract void addPublicParameters(StringBuilder result, Map.Entry<String, PortletParameters> portletParameters, String portletFriendlyId);

	protected void addWindowStateAndPortletMode(StringBuilder result, String portletFriendlyId, PortletParameters portletParameters) {
		if (!WindowState.NORMAL.equals(portletParameters.getWindowState()) ||
				!PortletMode.VIEW.equals(portletParameters.getPortletMode()))
		{
			result.append('&').append(PARAM_NAME_PREFIX).append(".pst=");
			result.append(portletFriendlyId);
			addStateAndModeToUrlFragment(result, portletParameters);
		}
	}

	protected void addStateAndModeToUrlFragment(StringBuilder result, PortletParameters portletParameters) {
		appendWindowStateToUrlFragment(portletParameters.getWindowState(), result);
		appendPortletModeToUrlFragment(portletParameters.getPortletMode(), result);
	}

	//FIXME (slawek) - how to implement custom window states ???
	private void appendWindowStateToUrlFragment(WindowState windowState, StringBuilder buf) {
		// NORMAL window state is not present in the URL as it's default
		if (WindowState.MAXIMIZED.equals(windowState)) {
			buf.append("_ws_MX");
		}
		else if (WindowState.MINIMIZED.equals(windowState)) {
			buf.append("_ws_MN");
		}
	}

	//FIXME (slawek) - how to implement custom portlet modes ???
	private void appendPortletModeToUrlFragment(PortletMode portletMode, StringBuilder buf) {
		// VIEW portlet mode is not present in the URL as it's default
		if (PortletMode.EDIT.equals(portletMode)) {
			buf.append("_pm_ED");
		}
		else if (PortletMode.HELP.equals(portletMode)) {
			buf.append("_pm_HP");
		}
	}

	@Override
	public String toString()
	{
		boolean isActionUrl = mActionPortletFriendlyId != null;
		boolean portletParametersSpecified = !mPortletParameters.isEmpty();
		StringBuilder result = createBaseUrl(isActionUrl);
		// added following line for portal parameter support - DSJ 2009/1/28
		boolean queryStarted = result.indexOf("?") >= 0;

		Iterator<Map.Entry<String, PortletParameters>> portletParameterEntries = mPortletParameters.entrySet().iterator();

		if (isActionUrl) {
			// if this is an action URL we mark it with javax.portlet.action parameter and we have
			// to specify the target portlet for the action (there is always only one) with ".tpst" parameter

			// added following for portal parameter support - DSJ 2009/1/28
			if (!queryStarted) {
				result.append('?');
			}
			else {
				result.append('&');
			}
			result.append("javax.portlet.action=true");
			// result.append("?javax.portlet.action=true");
			// end DSJ 2009/1/28
			result.append('&').append(PARAM_NAME_PREFIX).append(".tpst=").append(mActionPortletFriendlyId);
		}
		else if (portletParameterEntries.hasNext()) {

			// If this is not an action URL we take the first portlet from the list and make it
			// as the target for this request using ".tpst" parameter

			Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries.next();
			String portletFriendlyId = portletParameters.getKey();

			// added following line for portal parameter support - DSJ 2009/1/28
			// added following for portal parameter support - DSJ 2009/1/28
			if (!queryStarted) {
				result.append('?');
			}
			else {
				result.append('&');
			}
			result.append(PARAM_NAME_PREFIX).append(".tpst=").append(portletFriendlyId);
			// result.append('?').append(PARAM_NAME_PREFIX).append(".tpst=").append(portletFriendlyId);
			// end DSJ 2009/1/28

			// once we have the target we add the rest for this portlet (state, mode, public and private parameters)

			addStateAndModeToUrlFragment(result, portletParameters.getValue());
			addPublicParameters(result, portletParameters, portletFriendlyId);
			addPrivateParameters(result, portletParameters, portletFriendlyId);
		}

		if (portletParametersSpecified || isActionUrl) {
			result.append("&javax.portlet.begCacheTok=com.vignette.cachetoken");

			// If we have more than one portlet in this URL we now have to add its information
			// (state, mode, public and private parameters) between VAP's marker parameters

			while (portletParameterEntries.hasNext()) {
				Map.Entry<String, PortletParameters> portletParameters = portletParameterEntries.next();
				String portletFriendlyId = portletParameters.getKey();
				addWindowStateAndPortletMode(result, portletFriendlyId, portletParameters.getValue());
				addPublicParameters(result, portletParameters, portletFriendlyId);
				addPrivateParameters(result, portletParameters, portletFriendlyId);
			}
			result.append("&javax.portlet.endCacheTok=com.vignette.cachetoken");
		}

		return result.toString();
	}

	/**
	 * Captures the information such as private and public parameters, window state and mode for
	 * a single portlet.
	 */
	class PortletParameters
	{
		private Map<String, List<String>> mPrivateParameters = new LinkedHashMap<String, List<String>>();;
		private Map<String, List<String>> mPublicParameters =  new LinkedHashMap<String, List<String>>();
		private WindowState mWindowState = WindowState.NORMAL;
		private PortletMode mPortletMode = PortletMode.VIEW;

		public Map<String, List<String>> getPrivateParameters()
		{
			return mPrivateParameters;
		}

		public Map<String, List<String>> getPublicParameters()
		{
			return mPublicParameters;
		}

		private void setWindowState(WindowState windowState) {
			mWindowState = windowState;
		}

		public WindowState getWindowState()
		{
			return mWindowState;
		}

		private void setPortletMode(PortletMode portletMode)
		{
			mPortletMode = portletMode;
		}

		public PortletMode getPortletMode()
		{
			return mPortletMode;
		}
	}
}
