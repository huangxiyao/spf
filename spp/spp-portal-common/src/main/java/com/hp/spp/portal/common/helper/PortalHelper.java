package com.hp.spp.portal.common.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Iterator;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.portal.common.sql.PortalCommonDAOCacheImpl;
import com.hp.spp.profile.Constants;
import com.vignette.portal.util.StringUtils;
import com.vignette.portal.website.enduser.PortalContext;

public class PortalHelper {

	/**
	 * Standard logger.
	 */
	private static Logger mLog = Logger.getLogger(PortalHelper.class);

	protected static final String PARAMS_SEPARATOR = ",";

	protected static final String PROTOCOL_HTTP = "http";
	
	protected static final String PROTOCOL_HTTPS = "https";

	public PortalHelper() {
	}

	public String getProtocolFromTitle(String title, PortalContext context) {

		String protocol = null;

		List parameters = getParametersFromTitle(title);

		if (parameters.contains(PROTOCOL_HTTP)) {
			protocol = PROTOCOL_HTTP;
		} else if (parameters.contains(PROTOCOL_HTTPS)) {
			protocol = PROTOCOL_HTTPS;
		} else {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Protocol not specified in menu item name. Use site default.");
			}
			// get the site default protocol (http/https)
			protocol = getSiteProtocol(context);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("Protocol specified is: " + protocol);
		}

		return protocol;
	}

	public String getSiteProtocol(PortalContext context) {

		// get site name
		String site = new ProfileHelper().getProfileValue(context, Constants.MAP_SITE);

		String protocol = PortalCommonDAOCacheImpl.getInstance().getSiteProtocol(site);
		if (mLog.isDebugEnabled()) {
			mLog.debug("Default protocol for site: " + protocol);
		}
		// default to http
		if (protocol == null) {
			protocol = "http";
		}
		return protocol;
	}

	private List getParametersFromTitle(String title) {

		if (mLog.isDebugEnabled()) {
			mLog.debug("Checking parameters for menu item with title: "
							+ title);
		}

		List params = new ArrayList();

		// if no closing bracket at the end of the title, then return an empty
		// map
		if (!title.endsWith(")")) {
			return params;
		}

		int paramsStartIndex = title.lastIndexOf('(');

		// if no opening bracket, then return an empty map
		if (paramsStartIndex == -1) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("No opening bracket found. No parameters defined for menu item.");
			}
			return params;
		}

		String paramsString = title.substring(paramsStartIndex + 1, title
				.length() - 1);

		if (mLog.isDebugEnabled()) {
			mLog.debug("Parameters string: " + paramsString);
		}
		String[] paramsArray = StringUtils
				.split(paramsString, PARAMS_SEPARATOR);
		for (int i = 0; i < paramsArray.length; i++) {
			params.add(paramsArray[i]);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("Parameters list: " + params);
		}
		return params;
	}
	
	/**
	 * Builds the full href using the given href and the site default protocol
	 * 
	 * @param href
	 * @param context
	 * @return
	 */
	public String buildFullHref(String href, PortalContext context) {

		// get the site default protocol (http/https)
		String protocol = getSiteProtocol(context);

		return buildFullURI(href, protocol, context);
	}

	public String buildFullURI(String uri, String protocol, PortalContext context) {
		if(uri == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("URI parameter is NULL!");
			}
			return null ;
		}
		if(protocol == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Protocol parameter is NULL!");
			}
			return null ;
		}
		if(context == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Context parameter is NULL!");
			}
			return null ;
		}

		HttpServletRequest request = context.getPortalRequest().getRequest();
		StringBuffer fullHref = new StringBuffer();
		fullHref.append(protocol).append("://").append(request.getServerName());

		String port = Config.getValue("SPP.port." + protocol, null);

		// only specify the port if is not the default for the protocol
		// (80 for http, 443 for https)
		if (port != null && ((!"80".equals(port) && "http".equals(protocol)) || (!"443".equals(port) && "https".equals(protocol)))) {
			fullHref.append(":" + port);
		}

		fullHref.append(uri);

		if (mLog.isDebugEnabled()) {
			mLog.debug("Full href: " + fullHref.toString());
		}

		return fullHref.toString();
	}
	
	/**
	 * Returns URI with query string, e.g. /portal/site/mysite/menuitem.8888/?page=toto
	 * @param request
	 * @return
	 */
	public String getRequestedURI(HttpServletRequest request)	{
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		return query == null ? uri : uri + "?" + query;
	}

	/**
	 * Returns the request URL including query string with all parameter names and values URL-encoded.
	 * Note that this method will encode all the request parameters in the resulting URL even
	 * if the request was made with POST and some of the parameters could be included in the POST
	 * body. Esentially all the parameters for this request are included in the resulting URL.
	 *
	 * @param request portal request
	 * @return safe URL with URL-encoded parameter names and values
	 */
	public String getSafeRequestURL(HttpServletRequest request)
	{
		StringBuffer safeUrl = request.getRequestURL();

		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = (Map<String, String[]>) request.getParameterMap();

		if (parameterMap != null && !parameterMap.isEmpty()) {
			try {
				safeUrl.append('?');
				for (Iterator<Map.Entry<String, String[]>> parameterIterator = parameterMap.entrySet().iterator(); parameterIterator.hasNext(); ) {
					Map.Entry<String, String[]> parameter = parameterIterator.next();
					String parameterName = parameter.getKey();
					String[] parameterValue = parameter.getValue();

					if (parameterValue == null || parameterValue.length == 0) {
						safeUrl.append(URLEncoder.encode(parameterName, "UTF-8")).append('=');
					}
					else {
						for (int i = 0, len = parameterValue.length; i < len; ++i) {
							safeUrl.append(URLEncoder.encode(parameterName, "UTF-8"));
							safeUrl.append('=');
							safeUrl.append(URLEncoder.encode(parameterValue[i], "UTF-8"));
							if (i < len - 1) {
								safeUrl.append('&');
							}
						}
					}

					if (parameterIterator.hasNext()) {
						safeUrl.append('&');
					}
				}
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UTF-8 is not supported?", e);
			}
		}

		return safeUrl.toString();
	}

}
