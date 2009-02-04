package com.hp.it.spf.portalurl.portal.filter;

import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.epicentric.common.website.RequestUtils;
import com.epicentric.uid.UniquelyIdentifiable;
import com.vignette.portal.portlet.management.external.PortletManager;
import com.vignette.portal.portlet.management.external.PortletResourceNotFoundException;
import com.vignette.portal.portlet.management.external.PortletPersistenceException;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalURLRewriterFilter implements Filter
{
	private static final Logger mLog = Logger.getLogger(PortalURLRewriterFilter.class);

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		if (servletRequest instanceof HttpServletRequest) {
			filterChain.doFilter(convertPortletIds((HttpServletRequest) servletRequest), servletResponse);
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	/**
	 * If the given <tt>request</tt> contains porlet parameters that use portlet friendly IDs, the
	 * method will return a request wrapper that uses portlet UIDs instead of friendly IDs in the
	 * parameter names. If the request does not carry any portlet parameters, the method would return
	 * the parameter <tt>request</tt> without creating any wrapper
	 * @param request request to verify
	 * @return RequestWrapper if the request has portlet parameters that use friendly portlet names;
	 * otherwise <tt>request</tt> parameter of this method
	 */
	@SuppressWarnings("unchecked")
	private HttpServletRequest convertPortletIds(HttpServletRequest request)
	{
		Enumeration portletIdValues = RequestUtils.getParameterValues(request, "spf_p.tpst");
		if (portletIdValues == null || !portletIdValues.hasMoreElements()) {
			return request;
		}

		Enumeration portletRenderParamNames = RequestUtils.getParameterNamesByPrefix(request, "spf_p.prp_");
		Enumeration portletPublicRenderParamNames = RequestUtils.getParameterNamesByPrefix(request, "spf_p.pbp_");
		Enumeration portletStates = RequestUtils.getParameterValues(request, "spf_p.pst");

		Set<String> portletFriendlyIds = new HashSet<String>();

		while (portletIdValues.hasMoreElements()) {
			portletFriendlyIds.add(extractPorltletIdFromParamValue((String) portletIdValues.nextElement()));
		}

		while (portletStates.hasMoreElements()) {
			portletFriendlyIds.add(extractPorltletIdFromParamValue((String) portletStates.nextElement()));
		}

		if (portletRenderParamNames != null && portletRenderParamNames.hasMoreElements()) {
			addPortletFriendlyIdsFromParamNames(portletFriendlyIds, portletRenderParamNames, "spf_p.prp_");
		}

		if (portletPublicRenderParamNames != null && portletPublicRenderParamNames.hasMoreElements()) {
			addPortletFriendlyIdsFromParamNames(portletFriendlyIds, portletPublicRenderParamNames, "spf_p.pbp_");
		}

		Map<String, String> portletFriendlyIdToUidMap = buildPortletFriendlyIdToUIDMap(portletFriendlyIds);
		if (portletFriendlyIdToUidMap.isEmpty()) {
			return request;
		}

		return new RequestWrapper(request, portletFriendlyIdToUidMap);
	}

	private String extractPorltletIdFromParamValue(String value)
	{
		int pos = value.lastIndexOf("_ws_");
		if (pos == -1) {
			pos = value.lastIndexOf("_pm_");
		}

		if (pos != -1) {
			return value.substring(0, pos);
		}
		else {
			return value;
		}
	}

	/**
	 * Extracts from parameter names the portlet friendly IDs. The assumption is that the elements
	 * of <code>portletPrefixedParamNames</code> have the following format: [paramNamePrefix][friendly id]_...
	 * The extracted portlet friendly id is added to <code>portletFriendlyIds</code> set.
	 * @param portletFriendlyIds set of portlet friendly ids which gets updated with values extracted from parameter names
	 * @param portletPrefixedParamNames parameter names. This method iterates over this enumeration
	 * therefore upon its execution the enumeration pointer is put at the end.
	 * @param paramNamePrefix prefix by which all parameters on <code>portletPrefixedParamNames</code> start.
	 */
	private void addPortletFriendlyIdsFromParamNames(Set<String> portletFriendlyIds, Enumeration<String> portletPrefixedParamNames, String paramNamePrefix)
	{
		while (portletPrefixedParamNames.hasMoreElements()) {
			String paramName = (String) portletPrefixedParamNames.nextElement();
			int posUnderscore = paramName.indexOf('_', paramNamePrefix.length());
			if (posUnderscore == -1) {
				portletFriendlyIds.add(paramName.substring(paramNamePrefix.length()));
			}
			else {
				portletFriendlyIds.add(paramName.substring(paramNamePrefix.length(), posUnderscore));
			}
		}
	}

	/**
	 * Builds a map of incoming portlet friendly IDs (<code>portletIds</code>) to their corresponding
	 * UIDs.
	 * @param portletIds portlet friendly IDs
	 * @return Map of portlet friendly IDs present in the request to their respective UIDs
	 */
	private Map<String, String> buildPortletFriendlyIdToUIDMap(Set<String> portletIds)
	{
		Map<String, String> portletFriendlyIdToUidMap = new HashMap<String, String>();
		for (String portletId : portletIds) {
			try {
				UniquelyIdentifiable portlet = PortletManager.getInstance().getPortletByFriendlyID(portletId);
				portletFriendlyIdToUidMap.put(portletId, portlet.getUID());
			}
			catch (PortletResourceNotFoundException e) {
				// do nothing - if this exception occurs, it means that the URL may contain portlet UID
				// and not friendly ID
				if (mLog.isDebugEnabled()) {
					mLog.debug("Incoming request comes with portlet UID and not friendly ID: " + portletId);
				}
			}
			catch (PortletPersistenceException e) {
				// do nothing - if this exception occurs, it means that the URL may contain portlet UID
				// and not friendly ID
				if (mLog.isDebugEnabled()) {
					mLog.debug("Incoming request comes with portlet UID and not friendly ID: " + portletId);
				}
			}
		}
		return portletFriendlyIdToUidMap;
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	public void destroy()
	{
	}
}
