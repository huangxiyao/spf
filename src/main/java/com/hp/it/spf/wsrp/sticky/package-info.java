/**
 * The classes in this package implement a stickiness, for the time of the user session, 
 * to one of IP addresses to which the remote portlet server DNS name gets resolved.
 * <p> 
 * Such a stickiness is required for the portlets deployed site-to-site setup because GSLB 
 * staying on top of 2 sites is never sticky. GSLB name resolves to one of the virtual IPs
 * and this module provides the stickiness to those VIPs. Note that if the VIPs represent
 * local load balancers, these local balancers must also be configured as sticky to ensure 
 * that the request will be routed to the server hosting the session.
 * <p>
 * The stickiness implemented by the classes in this package consists of swapping the host
 * name with "manually-resolved" VIP in the web service target URL to force Axis to use the
 * URL with IP instead of DNS name. It's therefore important that the portlets to which
 * the portal connects be able to accept requests coming with IP instead of DNS name.
 * <p>
 * Note that by default JVM will cache forever DNS resolution results. In order for this class 
 * to work the DNS cache must be disabled or its time should be limited using the appropriate
 * properties as described at <a href="http://java.sun.com/j2se/1.5.0/docs/guide/net/properties.html">http://java.sun.com/j2se/1.5.0/docs/guide/net/properties.html</a>.
 * The properties in question are either <tt>networkaddress.cache.ttl</tt> or 
 * <tt>sun.net.inetaddr.ttl</tt>.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
package com.hp.it.spf.wsrp.sticky;