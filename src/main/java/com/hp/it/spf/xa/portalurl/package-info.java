/**
 * The classes in this package ({@link PortalURLFactory} and {@link PortalURL}) allow the creation
 * of URL to specified portal pages passing in the query parameters but also parameters addressed
 * to the portlets present on those pages.
 * <p>
 * The classes in this package implement the following use cases:
 * <ul>
 * <li>Generate a URL to a page with 1 portlet passing into the portlet some render parameters.</li>
 * <li>Generate a URL to a page with 2+ portlets passing to each of the portlets portlet-specific
 * parameters where portlet parameter names don't clash</li>
 * <li>Generate a URL to a page with 2+ portlets passing to each of the portlets portlet-specific
 * parameters where portlet parameter names clash but it's still possible to have for the same name
 * and different portlets different parameter values.</li>
 * <li>Generate a URL to a page with 2+ portlets passing in public render parameters that are shared
 * by the portlets.</li>
 * <li>Generate an action URL to a page with 1 portlet and be able to submit to this URL form
 * parameters.</li>
 * <li>Generate an action URL to a page with 2+ portlets being able to submit to this URL form
 * parameters and passing in additional render parameters to the other portlets.</li>
 * <li>Generate a URL to a portlet requesting a window state change</li>
 * <li>Generate a URL to a portlet requesting a portlet mode change</li>
 * </ul>
 */
package com.hp.it.spf.xa.portalurl;