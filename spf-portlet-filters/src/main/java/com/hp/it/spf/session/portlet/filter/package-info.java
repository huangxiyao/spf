/**
  Shared Portal Framework portlet filters.
 <body>
	<h3>Portlet session cleanup filter</h3>
	
	<p>{@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter}, a portlet filter, is the primary class 
	of interest to portlet applications. It reads a time stamp value from request and compares with the value from
	portlet session to determine whether to perform certain portlet session cleanup which is configurable via an optional initialization parameter. 
	This class could be extended to customize what session attributes need to be removed and/or retained.</p>
	
	<p>
	This portlet filter cleans up APPLICATION_SCOPE and PORTLET_SCOPE sessions of a portlet. 
	The filter has one optional init-param cleanupPortletSession, which has a String value of mode1 or mode2,
	the value is case-insensitive. The default value is mode1 if not specified. If the class is extended, the 
	values are not limited to mode1 and mode2. The extender can specify any desired value.  
	</p>
	<p/>
	<p>
	This filter can be used and configured in three possible ways to achieve how a portlet's session attributes 
	(for both Application Scope and Portlet scope) are cleaned up. For all three cases, the session attributes with a 
	key starting with SPF_RETAIN_ will be kept. The only difference is whether to clean up session attributes with a 
	key starting with SPF_ (but not SPF_RETAIN_) and any other key values. This behavior can be overridden by 
	extending this class and overriding predicate method.</p>
	<p/> 
	<ul>
	<li> Scenario 1) The filter is not used at all. Then there is no seesion clean up performed for a portlet.</li>
	<li> Scenario 2) The filter is used and configured without init-param or with an init-param which has 
			a value of mode1, all session attributes except those with a key starting with SPF_RETAIN_ will be cleaned up.</li>
	<li> Scenario 3) The filter is used and configured with init-param which has a value of mode2, then only those session 
		 attributes with a key starting with SPF_ but not SPF_RETAIN_ will be cleaned up. All other session attributes will be kept.</li>
	</ul>
	
	<p>If portlet session cleanup is desired, a portlet application will typically: </p>
	<ol>
		<li>Use and configure the {@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter} in the application's 
			<code>portlet.xml</code> including an initialization parameter, the value of which is default to true if not specified.</li>
	
		<li>Optionally, extend the {@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter} and override predicate method 
		which determine whether to cleanup the specified session attribute and then configure the extended filter 
		in the application's <code>portlet.xml</code> including an optional initialization parameter.</li>
			
	</ol>
	
	<p>The following illustrates the configuration of the {@link com.hp.it.spf.session.portlet.filter.SessionCleanupFilter}.</p>
	
<code><pre>
&lt;filter&gt;
	&lt;filter-name&gt;Session Cleanup Filter&lt;/filter-name&gt;
	&lt;filter-class&gt;com.hp.it.spf.session.portlet.filter.SessionCleanupFilter&lt;/filter-class&gt;
	&lt;lifecycle&gt;ACTION_PHASE&lt;/lifecycle&gt;      
	&lt;lifecycle&gt;RENDER_PHASE&lt;/lifecycle&gt;      
	&lt;lifecycle&gt;RESOURCE_PHASE&lt;/lifecycle&gt;      
	&lt;lifecycle&gt;EVENT_PHASE&lt;/lifecycle&gt;      

	&lt;init-param&gt;
		&lt;param-name&gt;portletSessionCleanupMode&lt;/param-name&gt;
		&lt;param-value&gt;mode1&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/filter&gt;

&lt;filter-mapping&gt;
		&lt;filter-name&gt;Session Cleanup Filter&lt;/filter-name&gt;
		&lt;portlet-name&gt;Your-Specific-Portlet-Name&lt;/portlet-name&gt;
&lt;/filter-mapping&gt;
</body>
 */
package com.hp.it.spf.session.portlet.filter;