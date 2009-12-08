package com.hp.it.spf.session.portlet.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * <p>
 * This portlet filter cleans up APPLICATION_SCOPE and PORTLET_SCOPE sessions of a portlet. 
 * The filter has one optional init-param cleanupPortletSession, which has a String value of true, false 
 * or any else (which is treated as true), the value is case-insensitive. The default value is true. 
 * </p>
 * <p/>
 * <p>
 * This filter can be used and configured in three possible ways to achieve how a portlet's session attributes 
 * (for both Application Scope and Portlet scope) are cleaned up. For all three cases, the seesion attributes
 * with a key starting with SPF_RETAIN_ will be kept. The only difference is whether to clean up session 
 * attributes with a key starting with SPF_ (but not SPF_RETAIN_) and any other key values.</p>
 * <p/> 
 * <ul>
 * <li> Scenario 1) The filter is not used at all. Then there is no seesion clean up performed for a portlet.</li>
 * <li> Scenario 2) The filter is used and configured without init-param or with an init-param which has 
 * 		a value of anything other than false, all session attributes except those with a key starting 
 * 		with SPF_RETAIN_ will be cleaned up.</li>
 * <li> Scenario 3) The filter is used and configured with init-param which has a value of false
 * 		(case-insensitive), then only those session attributes with a key starting with SPF_ but not SPF_RETAIN_
 * 		will be cleaned up. All other session attributes will be kept.</li>
 * </ul>
 * 
 * @author zhizhong.zhao@hp.com
 * @version TBD
 */
public class SessionCleanupFilter implements ActionFilter, RenderFilter, EventFilter, ResourceFilter {

	private FilterConfig filterConfig;
	private static final String INIT_PARAM = "portletSessionCleanupMode";
	private static final String INIT_PARAM_VALUE_MODE1 = "spf.keepStickySessionAttributesOnly";
	private static final String INIT_PARAM_VALUE_MODE2 = "spf.removeNonStickySessionAttributesOnly";
	private static final Log log = LogFactory.getLog(SessionCleanupFilter.class);

	public void init(FilterConfig filterConfig) throws PortletException
	{
		this.filterConfig = filterConfig;
	}

	public void doFilter(ActionRequest actionRequest, ActionResponse actionResponse,
						 FilterChain filterChain) throws IOException, PortletException
	{
		cleanupSession(actionRequest);
		filterChain.doFilter(actionRequest, actionResponse);
	}

	public void doFilter(RenderRequest renderRequest, RenderResponse renderResponse,
						 FilterChain filterChain) throws IOException, PortletException
	{
		cleanupSession(renderRequest);
		filterChain.doFilter(renderRequest, renderResponse);
	}

	public void doFilter(EventRequest eventRequest, EventResponse eventResponse,
						 FilterChain filterChain)
			throws IOException, PortletException
	{
		cleanupSession(eventRequest);
		filterChain.doFilter(eventRequest, eventResponse);

	}

	public void doFilter(ResourceRequest resourceRequest, ResourceResponse resourceResponse,
						 FilterChain filterChain) throws IOException, PortletException
	{
		cleanupSession(resourceRequest);
		filterChain.doFilter(resourceRequest, resourceResponse);
	}

	
	public void destroy()
	{

	}
	
	/**
	 * Determines whether to remove the session attribute for the given session attribute name. 
	 * The attribute value is not used by default but can be used by an extended class to determine
	 * whether to remove the session attribute. 
	 * @param name <code>String</code> session attribute name
	 * @param value <code>String</code> session attribute value for the given attribute name
	 * @param mode <code>String</code> the value for init-param configured in portlet filter
	 * @param portletSessionScope <code>int</code> PORTLET_SCOPE
	 * @return boolean return value which indicates whether to remove attribute name from portlet session
	 */
	protected boolean canSessionAttributeBeRemoved(String name, Object value, String mode, int portletSessionScope) {
	
		// mode 1, remove all other session attributes except sticky session attributes
		if ( (mode == null || INIT_PARAM_VALUE_MODE1.equalsIgnoreCase(mode))) {
			if (!name.startsWith(Consts.STICKY_SESSION_ATTR_PREFIX)) {
			
				return true;
			}
			
		}
		// mode 2, remove unsticky session attributes only
		else if (INIT_PARAM_VALUE_MODE2.equalsIgnoreCase(mode)) {
			if (name.startsWith(Consts.UNSTICKY_SESSION_ATTR_PREFIX) && 
				!name.startsWith(Consts.STICKY_SESSION_ATTR_PREFIX)) {
			
				return true;
			
			}
		}
		else if (mode != null) {
			throw new IllegalArgumentException("SessionCleanupFilter: init-param " + INIT_PARAM + " - value is invalid: " + mode);
			
		}
		
		return false;
	}

	private void cleanupSession(PortletRequest portletRequest)
	{
		PortletSession portletSession = portletRequest.getPortletSession();
		Long lastPortletSessionCleanupDate = (Long)portletSession
			.getAttribute(Consts.KEY_LAST_PORTLET_SESSION_CLEANUP_DATE, 
							PortletSession.PORTLET_SCOPE);

		long lastPortalSessionCleanupDate = 0;
		lastPortalSessionCleanupDate  = Utils.getLastSessionCleanupDate(portletRequest);
		log.info("From WSRP portlet request, lastPortalSessionCleanupDate = " + lastPortalSessionCleanupDate);
		
		// determine whether to clean
		if (lastPortalSessionCleanupDate > 0 ) { // exists portalSessionCleanupDate from request
			if (lastPortletSessionCleanupDate == null // first time 
					|| lastPortalSessionCleanupDate > lastPortletSessionCleanupDate) { // subsequent times
				
				log.info("Performing portlet session clean up...");
				
				// determine what to clean
				String mode = filterConfig.getInitParameter(INIT_PARAM);
				cleanupSessionAttributes(portletSession, mode, PortletSession.PORTLET_SCOPE);
				cleanupSessionAttributes(portletSession, mode, PortletSession.APPLICATION_SCOPE);

				portletSession.setAttribute(Consts.KEY_LAST_PORTLET_SESSION_CLEANUP_DATE, 
							Long.valueOf(System.currentTimeMillis()), 
							PortletSession.PORTLET_SCOPE);
			}
		}
	}
	
	/**
	 * removes all session attributes except those keys starting with SPF_RETAIN_ for a given session scope
	 * @param portletSession <code>PortletSession</code>
	 * @param mode <code>String</code> which determines which session attributes to remove and retain
	 * @param scope <code>int</code> Session scope, APPLICATION_SCOPE or PORTLET_SCOPE
	 */
	private void cleanupSessionAttributes(PortletSession portletSession, String mode, int scope) {
		Enumeration<String> portletScopeAttributes = portletSession.getAttributeNames(scope);
		String attributeName = null;
		Object attributeValue = null;
		while (portletScopeAttributes.hasMoreElements()) {
			attributeName = portletScopeAttributes.nextElement();
			attributeValue = portletSession.getAttribute(attributeName); 
			if (canSessionAttributeBeRemoved(attributeName, attributeValue, mode, scope)) {
				portletSession.removeAttribute(attributeName, scope);
			}
		}
		
	}
}

