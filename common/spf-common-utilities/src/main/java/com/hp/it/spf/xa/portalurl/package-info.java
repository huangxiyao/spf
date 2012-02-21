/**
 * The classes in this package allow the creation
 * of a URL pointing to specified SPF Vignette portal pages and passing in both portal query parameters and also parameters addressed
 * to the portlets present on those pages. Render parameters can be passed, portlet modes and window states can be set,
 * and the action or resource phases of a portlet can be targeted.
 * <p>
 * The classes in this package implement the following use cases for both <b>local</b> and <b>remote</b>
 * portlet deployment topologies in the SPF Vignette environment:
 * <ul>
 * <li>Generate a URL to a page with 1 portlet passing into the portlet some render parameters.</li>
 * <li>Generate a URL to a page with 2+ portlets passing to each of the portlets portlet-specific
 * parameters where portlet parameter names don't clash</li>
 * <li>Generate a URL to a page with 2+ portlets passing to each of the portlets portlet-specific
 * parameters where portlet parameter names clash but it's still possible to have (for the same name
 * and different portlets) different parameter values.</li>
 * <li>Generate a URL to a page with 2+ portlets passing in public render parameters that are shared
 * by the portlets.</li>
 * <li>Generate an action URL to a page with 1 portlet and be able to submit to this URL form
 * parameters.</li>
 * <li>Generate an action URL to a page with 2+ portlets being able to submit to this URL form
 * parameters and passing in additional render parameters to the other portlets.</li>
 * <li>Generate a URL to a portlet requesting a window state change</li>
 * <li>Generate a URL to a portlet requesting a portlet mode change</li>
 * <li>Generate a direct URL to a JSR-168 portlet resource (servlet or static file like an image).</li>
 * <li>Generate a URL to a JSR-286 resource-serving portlet <i>(supported for remote portlets only at this time)</i></li>
 * </ul>
 * <p>
 * <b>Important:</b> The classes from this package can be used by portlet and portal components.
 * They do not depend on any Vignette Portal classes.
 * <p>
 * <b>Usage</b>
 * <p>
 * For example, for a render URL:
 * <p>
 * <pre>
 * // create PortalURL using one of the factory methods
 * PortalURL url = PortalURLFactory.createPageURL(...);
 * // set URL query string parameters if needed; those are not specific to any portlets
 * url.setParameter(queryStringParamName, value);
 * // set portlet-specific parameters using portlet friendly ID as defined in portal console
 * url.setParameter(portletFriendlyId, paramName, value);
 * // set portlet public parameters using portlet friendly ID as defined in portal console
 * url.setPublicParameter(portletFriendlyId, paramName, value);
 * // use other set methods to configure the URL appropriately
 * ...
 * // get the URL to present to the user (eg in redirect, form action, hyperlink, etc)
 * String href = url.toString();
 * </pre>
 * <p>
 * And another example, for a direct JSR-168 resource URL (for a resource inside this portlet application):
 * <p>
 * <pre>
 * // create PortalURL using one of the factory methods
 * PortalURL url = PortalURLFactory.createPageURL(...);
 * // create your URL for the resource
 * String resource = request.getScheme() + "://";
 * resource += request.getServerName() + ":";
 * resource += request.getServerPort();
 * resource += request.getContextPath();  // for a different portlet app, use a different path
 * resource += "/my_resource";  // an image, servlet, whatever
 * // set the PortalURL as a resource URL for the targeted portlet (by friendly ID)
 * url.setAsResourceURL(portletFriendlyId, resource);
 * ...
 * // get the URL to present to the user (eg in redirect, form action, hyperlink, etc)
 * String redirectLocation = url.toString();
 * </pre>
 */
package com.hp.it.spf.xa.portalurl;