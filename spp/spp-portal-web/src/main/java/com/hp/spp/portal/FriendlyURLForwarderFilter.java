package com.hp.spp.portal;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

import com.epicentric.common.website.RequestUtils;
import com.epicentric.uid.UniquelyIdentifiable;
import com.hp.spp.portal.common.helper.PortalURIHelper;
import com.vignette.portal.util.StringUtils;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.portlet.management.external.PortletManager;
import com.vignette.portal.portlet.management.external.PortletResourceNotFoundException;
import com.vignette.portal.portlet.management.external.PortletPersistenceException;

/**
 * @author Cyril MICOUD
 * @version $Revision: 1.7 $ $Date: 2006/10/04 14:40:24 $
 */
public final class FriendlyURLForwarderFilter implements Filter {

	private static Logger mLog = Logger.getLogger(FriendlyURLForwarderFilter.class);

	// ----------------------------------------------------- Constant Variables

	protected static final String FORWARDING = "spp_force_forwarding";
	protected static final String PORTAL_CONTEXT = PortalURIHelper.PORTAL_CONTEXT;
	protected static final String REQUEST_PAGE = "page";

	// ----------------------------------------------------- Instance Variables

	/**
	 * The filter configuration object we are associated with. If this value is
	 * null, this filter instance is not currently configured.
	 */
	protected FilterConfig mFilterConfig = null;
	protected PortalURIHelper mPortalURIHelper = null;

	/**
	 * Patterns for request attributes that will not be removed by {@link #clearRequestAttributes(ServletRequest)} method.
	 */
	private static final String[] PROTECTED_REQUEST_ATTRIBUTES = new String[]{"javax.servlet.", "com.hp.spp.", "__oscache", FORWARDING};

	// --------------------------------------------------------- Public Methods

	/**
	 * Place this filter into service.
	 * 
	 * @param filterConfig
	 *            The filter configuration object
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.mFilterConfig = filterConfig;
		this.mPortalURIHelper = new PortalURIHelper() ;
	}

	/**
	 * 
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String alreadyForward = (String) httpRequest.getAttribute(FORWARDING);
		// this param contains the displayable name of the navigation item
		String pagename = RequestUtils.getParameter(httpRequest, REQUEST_PAGE);

		// Only perform comparison if there is a value for channel otherwise
		// default to standard display
		if (!StringUtils.isEmpty(pagename) && alreadyForward == null) {
			// If the request parameters contain parameters for portlets we have have to replace
			// portlet friendly IDs with their UID values
			httpRequest = convertPortletIds(httpRequest);
			PortalContext pc = (PortalContext) request.getAttribute(PORTAL_CONTEXT);
			PortalURI portalURI = mPortalURIHelper.getPortalURI(pc, httpRequest, pagename) ;

			if (portalURI != null) {
				httpRequest.setAttribute(FORWARDING, FORWARDING);
				forward(httpRequest, httpResponse, portalURI.toString()) ;
				return ;
			}
		} else if (alreadyForward != null) {
			httpRequest.removeAttribute(FORWARDING);
		}

		chain.doFilter(request, response);
	}

	/**
	 * Remove all the request attributes but those matching patterns specified in {@link #PROTECTED_REQUEST_ATTRIBUTES}.
	 * This method is used prior to invoking "forward" to force Vignette to set any request attribtues
	 * once again.
	 * @param request - request for within which attributes are removed
	 */
	private void clearRequestAttributes(ServletRequest request) {
		java.util.Set attributeNames = new java.util.HashSet();
		for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
			attributeNames.add(en.nextElement());
		}
		for (Iterator it = attributeNames.iterator(); it.hasNext(); ) {
			String attributeName = (String) it.next();
			boolean isAttributeProtected = false;
			for (int i = 0, len = PROTECTED_REQUEST_ATTRIBUTES.length; i < len; ++i) {
				if (attributeName.indexOf(PROTECTED_REQUEST_ATTRIBUTES[i]) != -1) {
					isAttributeProtected = true;
					break;
				}
			}
			if (!isAttributeProtected) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("clearRequestAttributes: removing attribute: " + attributeName);
				}
				request.removeAttribute(attributeName);
			}
		}
	}

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		this.mFilterConfig = null;
		this.mPortalURIHelper = null ;
	}


	/**
	 * Forward the request to the specified URL
	 * 
	 * @param request to get the dispatcher and passed to forward
	 * @param response passed to forward
	 * @param url The URL to forward on
	 * @throws IOException if the target resource throws this exception
	 * @throws ServletException if the target resource throws this exception
	 */
	public void forward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		// Clean request attributes to make sure that anything that may have been setup
		// by Vignette will not interfere with the handling of the URL we are forwarding to.
		// The purpose is to force Vignette to deal with it as if this was original request.
		clearRequestAttributes(request);

		if (mLog.isDebugEnabled()) {
			mLog.debug("[Class]=FriendlyURLForwarderFilter [Method]=forward [Parameter]=url [Value]=" + url + " - good url...");
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

//	/**
//	 * Redirect the response to the specified URI
//	 * 
//	 * @param response Used to redirect on the URI
//	 * @param uri The URI to redirect on (it is possible to redirect on a URL)
//	 * @throws IOException If an input or output exception occurs
//	 */
//	public void redirect(HttpServletResponse response, String uri) throws IOException {
//		mLog.info("[Class]=FriendlyURLForwarderFilter [Method]=redirect [Parameter]=uri [Value]=" + uri + " - good uri...");
//		response.sendRedirect(uri);
//	}

	/**
	 * If the given <tt>request</tt> contains porlet parameters that use portlet friendly IDs, the
	 * method will return a request wrapper that uses portlet UIDs instead of friendly IDs in the
	 * parameter names. If the request does not carry any portlet parameters, the method would return
	 * the parameter <tt>request</tt> without creating any wrapper
	 * @param request request to verify
	 * @return RequestWrapper if the request has portlet parameters that use friendly portlet names;
	 * otherwise <tt>request</tt> parameter of this method
	 */
	private HttpServletRequest convertPortletIds(HttpServletRequest request) {
		Enumeration portletIds = RequestUtils.getParameterValues(request, "javax.portlet.tpst");
		if (portletIds == null || !portletIds.hasMoreElements()) {
			return request;
		}

		Map portletFriendlyIdToUidMap = new HashMap();
		while (portletIds.hasMoreElements()) {
			String portletId = (String) portletIds.nextElement();
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
		if (portletFriendlyIdToUidMap.isEmpty()) {
			return request;
		}

		return new RequestWrapper(request, portletFriendlyIdToUidMap);
	}

	/**
	 * Request wrapper that would mask the portlet parameters that use friendly IDs with portlet
	 * parameters that use UIDs. As Vignette doesn't allow to use friendly Ids in parameters, this
	 * wrapper would hide this.
	 */
	public static class RequestWrapper extends HttpServletRequestWrapper {
		private Map mPortletFriendlyIdToUidMap;
		private String mQueryString;
		private Map mParameterMap;

		public RequestWrapper(HttpServletRequest request, Map portletFriendlyIdToUidMap) {
			super(request);
			mPortletFriendlyIdToUidMap = portletFriendlyIdToUidMap;
		}

		/**
		 * Returned query string value is rewritten if it contains any portlet parameters that use
		 * friendly IDs. Otherwise the semantics of this method are the same as for {@link javax.servlet.http.HttpServletRequest#getQueryString()}.
		 */
		public synchronized String getQueryString() {
			if (mQueryString == null) {
				String queryString = super.getQueryString();
				if (queryString == null) {
					return null;
				}
				mQueryString = replacePortletIds(queryString);
			}
			return mQueryString;
		}

		/**
		 * Returned map will contain all the parameters of the wrapped request. All the portlet
		 * parameters that use friendly portlet IDs will be appropriate rewritten.
		 */
		public synchronized Map getParameterMap() {
			if (mParameterMap == null) {
				mParameterMap = replacePortletIds(super.getParameterMap());
			}
			return mParameterMap;
		}

		public String getParameter(String name) {
			String[] value = (String[]) getParameterMap().get(name);
			return (value == null || value.length == 0 ? null : value[0]);
		}

		public Enumeration getParameterNames() {
			return Collections.enumeration(getParameterMap().keySet());
		}

		public String[] getParameterValues(String name) {
			return (String[]) getParameterMap().get(name);
		}

		/**
		 * Replaces in the incoming query string all the occurences of the portlet parameters
		 * having friendly portlet IDs.
		 * @param queryString query string of the wrapped request; must not be <tt>null</tt>
		 * @return rewritten query string
		 */
		String replacePortletIds(String queryString) {
			String result = queryString;
			for (Iterator it = mPortletFriendlyIdToUidMap.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry entry = (Map.Entry) it.next();
				// the parameter javax.portlet.tpst value contains portlet id
				result = result.replaceAll(
						"javax\\.portlet\\.tpst=" + entry.getKey(),
						"javax\\.portlet\\.tpst=" + entry.getValue());
				// parameters starting with javax.portlet.prp_ contain portlet-specific values
				result = result.replaceAll(
						"javax\\.portlet\\.prp_" + entry.getKey() + "_",
						"javax\\.portlet\\.prp_" + entry.getValue() + "_");
			}
			return result;
		}


		/**
		 * For all the parameters using friendly portlet IDs, this method rewrites them appropriately.
		 * All other parameters are copied as-is from the wrapped request
		 * @param paramMap wrapped request parameter map
		 * @return rewritten parameter map
		 */
		Map replacePortletIds(Map paramMap) {
			Map result = new HashMap();
			for (Iterator it = paramMap.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry param = (Map.Entry) it.next();
				String paramName = (String) param.getKey();
				String[] paramValue = (String[]) param.getValue();
				// for parameters "javax.portlet.tpst" we have to replace the value of the parameter
				if ("javax.portlet.tpst".equals(paramName)) {
					for (int i = 0, len = paramValue.length; i < len; ++i) {
						String uid = (String) mPortletFriendlyIdToUidMap.get(paramValue[i]);
						if (uid != null) {
							// in order to avoid table copy I'll update the original value
							paramValue[i] = uid;
						}
					}
					result.put(paramName, paramValue);
				}
				// for parameters starting with "javax.portlet.prp_" we have to rename parameters
				// but keeping values the same
				else if (paramName.startsWith("javax.portlet.prp_")) {
					String newParamName = paramName;
					for (Iterator it2 = mPortletFriendlyIdToUidMap.entrySet().iterator(); it2.hasNext(); ) {
						Map.Entry entry = (Map.Entry) it2.next();
						String friendlyId = (String) entry.getKey();
						String uid = (String) entry.getValue();
						String namePrefix = "javax.portlet.prp_" + friendlyId + "_";
						if (paramName.startsWith(namePrefix)) {
							newParamName = "javax.portlet.prp_" + uid + "_" + paramName.substring(namePrefix.length());
							break;
						}
					}
					result.put(newParamName, paramValue);
				}
				// for any other parametes we just copy them as-is
				else {
					result.put(paramName, paramValue);
				}
			}
			return result;
		}
	}

}
