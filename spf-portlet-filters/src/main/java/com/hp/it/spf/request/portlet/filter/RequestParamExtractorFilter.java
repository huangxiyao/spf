/**
 *
 */
package com.hp.it.spf.request.portlet.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.hp.it.spf.xa.misc.portlet.Utils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

/**
 * <p>
 * The filter extracts the request parameters present in the PortalURL and
 * places them in request attribute.
 * </p>
 * <p/>
 * <p>
 * An additional capability of overriding the existing request attributes is provided ,
 * in case a naming conflict exists.
 * We would have to place <code>PARAM_OVERWRITE</code> in initialization configuration of
 * RequestParamExtractorFilter. Default behavior is "false" , which would not overwrite the
 * existing attribute in-case an attribute already exists with the same name.
 * </P>
 * For an Example: If we have received a url of type <code>/portal/site/example/?param1=value1 & param2=value2</code>
 * <br>PortalURL is fetched using the Utils(in spf-portlet-utilities module).
 * <p>
 * Scenario 1 : let's say there is already an attribute present with name "param1" in request attribute.
 * <br>We wouldn't replace the existing attribute according to the default behavior.
 * <br>Only <code>param2=<i>value2</i></code> would be added to request attribute
 * </p>
 * <p>
 * Scenario 2 : let's say there is already an attribute present with name "param1" in request attribute.
 * <br><code>PARAM_OVERWRITE</code> is set to "true" in porlet.xml.
 * <br><code>param1=<i>value1</i></code> and <code>param2=<i>value2</i></code> would be added to request attribute.
 * </p>
 *
 * @author pranav
 * @version TBD
 */
public class RequestParamExtractorFilter
		implements
		ActionFilter,
		RenderFilter,
		EventFilter,
		ResourceFilter
{

	private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws PortletException
	{
		this.filterConfig = filterConfig;
	}

	public void doFilter(ActionRequest actionRequest, ActionResponse actionResponse,
						 FilterChain filterChain) throws IOException, PortletException
	{
		addParametersToAttribute(actionRequest);
		filterChain.doFilter(actionRequest, actionResponse);
	}

	public void doFilter(RenderRequest renderRequest, RenderResponse renderResponse,
						 FilterChain filterChain) throws IOException, PortletException
	{
		addParametersToAttribute(renderRequest);
		filterChain.doFilter(renderRequest, renderResponse);
	}

	public void doFilter(EventRequest eventRequest, EventResponse eventResponse,
						 FilterChain filterChain)
			throws IOException, PortletException
	{
		addParametersToAttribute(eventRequest);
		filterChain.doFilter(eventRequest, eventResponse);

	}

	public void doFilter(ResourceRequest resourceRequest, ResourceResponse resourceResponse,
						 FilterChain filterChain) throws IOException, PortletException
	{
		addParametersToAttribute(resourceRequest);
		filterChain.doFilter(resourceRequest, resourceResponse);
	}

	protected void addParametersToAttribute(PortletRequest portletRequest)
	{

		if ("true".equalsIgnoreCase(filterConfig.getInitParameter("PARAM_OVERWRITE")) && filterConfig.getInitParameter("PARAM_OVERWRITE") != null) {
			for (Map.Entry<String, String> param : extractParametersFromPortalURL(portletRequest).entrySet()) {
				portletRequest.setAttribute(param.getKey(), param.getValue());
			}
		}
		else {
			Enumeration<String> attributeNames = portletRequest.getAttributeNames();

			for (Map.Entry<String, String> param : extractParametersFromPortalURL(portletRequest).entrySet()) {
				while (attributeNames.hasMoreElements()) {
					String name = attributeNames.nextElement();
					if (!name.equalsIgnoreCase(param.getKey())) {
						portletRequest.setAttribute(param.getKey(), param.getValue());
					}
				}
			}
		}
	}

	private Map<String, String> extractParametersFromPortalURL(PortletRequest portletRequest)
	{

		String portalURL = Utils.getPortalRequestURL(portletRequest);
		int ix = portalURL.indexOf('?');
		portalURL = portalURL.substring(ix + 1);
		Map<String, String> paramMap = new HashMap<String, String>();
		while (portalURL.indexOf('&') != -1 && portalURL.indexOf('=') != -1) {
			ix = portalURL.indexOf('=');
			int ixOfAnd = portalURL.indexOf('&');
			paramMap.put(portalURL.substring(0, ix), portalURL.substring(ix + 1, ixOfAnd));
			portalURL = portalURL.substring(ixOfAnd + 1);
		}
		if (portalURL.indexOf('=') != -1) {
			ix = portalURL.indexOf('=');
			paramMap.put(portalURL.substring(0, ix), portalURL.substring(ix + 1));
		}
		return paramMap;
	}

	public void destroy()
	{

	}


}
