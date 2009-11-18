/**
 *
 */
package com.hp.it.spf.request.portlet.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.HashSet;

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

	protected FilterConfig filterConfig;

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
	
	/**
	 * This method adds the extracted parameters to request Attribute.
	 * The filterConfig parameter "PARAM_OVERWRITE" is initialized in the "web.xml"
	 * Based on this parameter value method either overwrites/doesn't overwrite the existing attributes.
	 *  
	 * @param portletRequest portlet request which will contain the attribute mapped from portal
	 * request query string parameters
	 */
	protected void addParametersToAttribute(PortletRequest portletRequest)
	{
		Map<String, String[]> parametersFromPortalURL = extractParametersFromPortalURL(portletRequest);
		if (!parametersFromPortalURL.isEmpty()) {
			if ("true".equalsIgnoreCase(filterConfig.getInitParameter("PARAM_OVERWRITE"))) 
			{
				for (Map.Entry<String, String[]> param : parametersFromPortalURL.entrySet()) {
					portletRequest.setAttribute(param.getKey(), param.getValue());
				}
			}
			else {
				Set<String> lowerCaseAttributeNames = new HashSet<String>();
				for (Enumeration<String> attrNameEnum = portletRequest.getAttributeNames(); attrNameEnum.hasMoreElements(); ) {
					lowerCaseAttributeNames.add(attrNameEnum.nextElement().toLowerCase());
				}

				for (Map.Entry<String, String[]> param : parametersFromPortalURL.entrySet()) {
					if (!lowerCaseAttributeNames.contains(param.getKey().toLowerCase())) {
						portletRequest.setAttribute(param.getKey(), param.getValue());
					}
				}
			}
		}
	}

	/**
	 * This method extracts the parameters from PortalURL.
	 * Creates a Map of all the extracted parameters and returns the Map.
	 * 
	 * The query string may contain repeated values for the same name, eg: <i>color=red&color=blue</i>  
	 * According to the HTTP RFC, in this case all the values need to be returned. The method stores the values
	 * in the array and returns the values in the String[]
	 * 
	 * If PortalURL doesn't contain any query string parameter this method returns an empty map.
	 * 
	 * @param portletRequest portlet request
	 * @return Map containing key/value pairs retrieved from PortalURL query-string
	 */
	protected Map<String, String[]> extractParametersFromPortalURL(PortletRequest portletRequest)
	{

		String portalURL = Utils.getPortalRequestURL(portletRequest);
		Map<String, String[]> map = new HashMap<String, String[]>();

		int ix = portalURL.indexOf('?');
//		if (ix >= 0 && ix < portalURL.length() - 1) {
		if (ix != -1) {
			portalURL = portalURL.substring(ix + 1);

			if (portalURL.length() != -1 && portalURL != "") {
				StringTokenizer st = new StringTokenizer(portalURL, "&");
				while (st.hasMoreTokens()) {
					String keyValPairs = st.nextToken();
					int ixOfEqual = keyValPairs.indexOf('=');
					String key = keyValPairs.substring(0, ixOfEqual);
					String value = keyValPairs.substring(ixOfEqual + 1);
					if (map.containsKey(key)) {
						String[] valueArray = map.get(key);
						String[] tempArray = appendValue(valueArray, value);
						map.put(key, tempArray);
					}
					else {
						map.put(key, new String[]{value});
					}
				}
			}
		}
		return map;
	}

	private String[] appendValue(String[] valueArray, String value)
	{
//						List<String> tempList = new ArrayList<String>(Arrays.asList(valueArray));
//						tempList.add(value);
//						map.put(key, tempList.toArray(new String[tempList.size()]));
		String[] tempArray = new String[valueArray.length + 1];
		for (int index = 0; index < valueArray.length; index++) {
			tempArray[index] = valueArray[index];
		}
		tempArray[valueArray.length] = value;
		return tempArray;
	}

	public void destroy()
	{

	}

}
