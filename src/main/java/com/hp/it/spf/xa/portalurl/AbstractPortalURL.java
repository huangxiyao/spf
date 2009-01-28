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
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
abstract class AbstractPortalURL implements PortalURL
{
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
		mSiteRootUrl = siteRootUrl;
		mAnotherSiteName = anotherSiteName;
		mPageFriendlyUri = pageFriendlyUri;
		mSecure = secure;
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
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, false);
		portletParamValueList.clear();
		if (paramValue != null) {
			portletParamValueList.add(paramValue);
		}
	}

	public void setParameter(String portletFriendlyId, String paramName, String[] paramValues)
	{
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, false);
		portletParamValueList.clear();
		if (paramValues != null) {
			portletParamValueList.addAll(Arrays.asList(paramValues));
		}
	}

	public void setParameters(String portletFriendlyId, Map params)
	{
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
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, true);
		portletParamValueList.clear();
		if (paramValue != null) {
			portletParamValueList.add(paramValue);
		}
	}

	public void setPublicParameter(String portletFriendlyId, String paramName, String[] paramValues)
	{
		List<String> portletParamValueList = getPortletParamValueList(portletFriendlyId, paramName, true);
		portletParamValueList.clear();
		if (paramValues != null) {
			portletParamValueList.addAll(Arrays.asList(paramValues));
		}
	}

	public void setPublicParameters(String portletFriendlyId, Map params)
	{
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
		if (windowState == null) {
			windowState = WindowState.NORMAL;
		}
		getPortletParameters(portletFriendlyId).setWindowState(windowState);
	}

	public void setPortletMode(String portletFriendlyId, PortletMode portletMode)
	{
		if (portletMode == null) {
			portletMode = PortletMode.VIEW;
		}
		getPortletParameters(portletFriendlyId).setPortletMode(portletMode);
	}

	public void setAsActionURL(String portletFriendlyId) throws IllegalStateException
	{
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
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UTF-8 encoding not supported? " + e, e);
			}
		}
	}
	
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
