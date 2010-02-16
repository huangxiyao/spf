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
 * <h3>Portlet session cleanup filter</h3>
 *	
 * <p>{@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter}, a portlet filter, is the primary class 
 *  of interest to portlet applications. It reads a time stamp value from request and compares with the value from
 *	portlet session to determine whether to perform certain portlet session cleanup, for both APPLICATION_SCOPE and 
 *	PORTLET_SCOPE session attributes.</p>
 *	
 *	<p>The filter takes one	initialization parameter, <em>portletSessionCleanupMode</em>. It could take one of two default values, 
 *	spf.keepStickySessionAttributesOnly	or spf.removeNonStickySessionAttributesOnly. If the former is used, all portlet 
 *	session attributes except sticky session attributes will be removed for both application and portlet scopes. 
 *	This is default behavior if init-param is not specified. If the latter is used, then only unsticky session attributes 
 *	will be removed.</P>  
 *	
 *	<p>After cleanup, it will add two different session attributes in portlet scope and application scope to record when
 *		last session cleanup were done.</p>
 * 
 *	<p>This class could be extended to customize what session attributes need to be removed and/or retained by overriding
 *	<em>canSessionAttributeBeRemoved</em> method to suit different application needs. If the filter does have been extended,
 *	then the value for portletSessioCleanMode is not limited by above two values. Application Developer can choose whatever
 *	value he/she prefers.</p>
 *	<p/>
 *	<p>
 *	By default, this filter can be used and configured in three possible ways to achieve how a portlet's session attributes 
 *	(for both Application Scope and Portlet scope) are cleaned up as outlined below.</p>
 *	<p/> 
 *	<ul>
 *	<li> Scenario 1) The filter is not used at all. Then there is no session cleanup performed for a portlet.</li>
 *	<li> Scenario 2) The filter is used and configured without init-param or with an init-param which has 
 *			a value of spf.keepStickySessionAttributesOnly, all session attributes except those with a key starting with SPF_RETAIN_ will be cleaned up.</li>
 *	<li> Scenario 3) The filter is used and configured with init-param which has a value of spf.removeNonStickySessionAttributesOnly, then only those session 
 *		 attributes with a key starting with SPF_ but not SPF_RETAIN_ will be cleaned up. All other session attributes will be kept as well.</li>
 *	</ul>
 *	<p>If portlet session cleanup is desired, a portlet application will typically: </p>
 *	<ol>
 *		<li>Use and configure the {@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter} in the application's 
 *			<code>portlet.xml</code> including an initialization parameter.</li>
 *		<li>Optionally, extend the {@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter} and override <em>canSessionAttributeBeRemoved</em> method 
 *		which determine whether to cleanup the specified session attribute based on evaluation of session attribute name, session attribute value, 
 *		init-param value and portlet session scope. And then configure the extended filter 
 *		in the application's <code>portlet.xml</code> including an initialization parameter.</li>
 *	</ol>
 *
 *	<p>There is a known caveat about the current implementation of the filter. If there are multiple portlets in one portlet application, cleanup one portlet's application scope 
 *		session attributes would affect another portlet's application scope session attributes even the latter portlet has not applied SessionCleanupFilter. Currently,
 *		there is a good way to avoid this.</p>
 *
 *	<p>The following illustrates the configuration of the {@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter}.</p>
 *  <code><pre>
 *	&lt;filter&gt;
 *		&lt;filter-name&gt;Session Cleanup Filter&lt;/filter-name&gt;
 *		&lt;filter-class&gt;com.hp.it.spf.session.portlet.filter.SessionCleanupFilter&lt;/filter-class&gt;
 *		&lt;lifecycle&gt;ACTION_PHASE&lt;/lifecycle&gt;      
 *		&lt;lifecycle&gt;RENDER_PHASE&lt;/lifecycle&gt;      
 *		&lt;lifecycle&gt;RESOURCE_PHASE&lt;/lifecycle&gt;      
 *		&lt;lifecycle&gt;EVENT_PHASE&lt;/lifecycle&gt;      
 *		&lt;init-param&gt;
 *			&lt;name&gt;portletSessionCleanupMode&lt;/name&gt;
 *			&lt;value&gt;spf.keepStickySessionAttributesOnly&lt;/value&gt;
 *		&lt;/init-param&gt;
 *	&lt;/filter&gt;
 *
 *	&lt;filter-mapping&gt;
 *		&lt;filter-name&gt;Session Cleanup Filter&lt;/filter-name&gt;
 *		&lt;portlet-name&gt;Your-Specific-Portlet-Name&lt;/portlet-name&gt;
 *	&lt;/filter-mapping&gt;
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
	 * @param portletSessionScope <code>int</code> PORTLET SCOPE or APPLICATION SCOPE
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
		Long lastPortletScopeSessionCleanupDate = (Long)portletSession
						.getAttribute(Consts.KEY_LAST_PORTLET_SCOPE_SESSION_CLEANUP_DATE, 
									PortletSession.PORTLET_SCOPE);

		Long lastAppScopeSessionCleanupDate = (Long)portletSession
						.getAttribute(Consts.KEY_LAST_APP_SCOPE_SESSION_CLEANUP_DATE, 
									PortletSession.APPLICATION_SCOPE);
		
		long lastPortalSessionCleanupDate = 0;
		lastPortalSessionCleanupDate  = Utils.getLastSessionCleanupDate(portletRequest);
		log.info("From WSRP portlet request, lastPortalSessionCleanupDate = " + lastPortalSessionCleanupDate);
		
		// determine whether to clean
		if (lastPortalSessionCleanupDate > 0 ) { // exists portalSessionCleanupDate from request
			if (lastPortletScopeSessionCleanupDate == null // first time 
					|| lastPortalSessionCleanupDate > lastPortletScopeSessionCleanupDate) { // subsequent times
				
				log.info("Performing portlet scope session clean up...");
				
				// determine what to clean
				String mode = filterConfig.getInitParameter(INIT_PARAM);
				cleanupSessionAttributes(portletSession, mode, PortletSession.PORTLET_SCOPE);
				portletSession.setAttribute(Consts.KEY_LAST_PORTLET_SCOPE_SESSION_CLEANUP_DATE, 
											Long.valueOf(System.currentTimeMillis()), 
											PortletSession.PORTLET_SCOPE);
			}

		    // try to cleanup application scope session attributes
		    if (lastAppScopeSessionCleanupDate == null // first time
		            || lastPortalSessionCleanupDate > lastAppScopeSessionCleanupDate) { // subsequent times
		        log.info("Performing portlet application scope session clean up...");

		        // determine what to clean
		        String mode = filterConfig.getInitParameter(INIT_PARAM);
		        cleanupSessionAttributes(portletSession, mode, PortletSession.APPLICATION_SCOPE);
		        portletSession.setAttribute(Consts.KEY_LAST_APP_SCOPE_SESSION_CLEANUP_DATE, 
		        							Long.valueOf(System.currentTimeMillis()), 
		        							PortletSession.APPLICATION_SCOPE);

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

