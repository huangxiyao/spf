/**
 * The classes in this package are used to share data between portal and portlets.
 * <p>
 * The mechanism to share the data is slightly different depending on the type of the portlet,
 * however the way the portlets retrieve this data is exactly the same, as long as they declare
 * in their <code>portlet.xml</code> descriptor <code>MapAttributeFilter</code> portlet filter
 * (com.hp.it.spf.sso.portlet.filter.MapAttributeFilter present in spf-portlet-filters modules).
 * The data shared between portal and portlets, regardless of the type of the portlet,
 * is collected by {@link com.hp.it.spf.xa.portletdata.portal.PortletDataCollector}.
 * </p> <p>
 * For <em>remote portlets</em> the data is injected by <code>UserContextInjector</code> Axis handler
 * (com.hp.it.spf.wsrp.injector.context.UserContextInjector present in spf-portal-wsrp module).
 * </p><p>
 * For <em>local portlets</em> the data is injected by {@link com.hp.it.spf.xa.portletdata.portal.PortletDataPreDisplayAction} class.
 * It's a secondary page pre-display action which is used for pages displaying portlets. Note
 * that in order for this action to be used the SPF secondary pages must be used by Vignette sites.
 * The secondary pages in question include "Page Display", "Maximize", "My Pages - Page Display" and
 * "My Pages - Maximize View".
 */
package com.hp.it.spf.xa.portletdata.portal;