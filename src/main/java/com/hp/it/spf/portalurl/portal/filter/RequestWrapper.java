package com.hp.it.spf.portalurl.portal.filter;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.HashMap;

/**
 * Request wrapper that would mask the portlet parameters that use friendly IDs with portlet
 * parameters that use UIDs. As Vignette doesn't allow to use friendly Ids in parameters, this
 * wrapper would hide this.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class RequestWrapper extends HttpServletRequestWrapper
{
	private Map<String, String> mPortletFriendlyIdToUidMap;
	private String mQueryString;
	private Map mParameterMap;

	public RequestWrapper(HttpServletRequest request, Map<String, String> portletFriendlyIdToUidMap) {
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
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
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
		for (Iterator<Map.Entry<String, String>> it = mPortletFriendlyIdToUidMap.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, String> entry = it.next();
			String portletFriendlyId = entry.getKey();
			String portletUid = entry.getValue();

			// the parameter javax.portlet.tpst value contains portlet id
			result = result.replaceAll(
					"spf_p\\.tpst=" + portletFriendlyId,
					"javax\\.portlet\\.tpst=" + portletUid);
			// the parameter javax.portlet.pst value contains portlet id
			result = result.replaceAll(
					"spf_p\\.pst=" + portletFriendlyId,
					"javax\\.portlet\\.pst=" + portletUid);
			// parameters starting with javax.portlet.prp_ contain portlet-specific values
			result = result.replaceAll(
					"spf_p\\.prp_" + portletFriendlyId + "_",
					"javax\\.portlet\\.prp_" + portletUid + "_");
			// parameters starting with javax.portlet.pbp_ contain portlet-specific values
			result = result.replaceAll(
					"spf_p\\.pbp_" + portletFriendlyId + "_",
					"javax\\.portlet\\.pbp_" + portletUid + "_");
		}
		return result;
	}


	/**
	 * For all the parameters using friendly portlet IDs, this method rewrites them appropriately.
	 * All other parameters are copied as-is from the wrapped request
	 * @param paramMap wrapped request parameter map
	 * @return rewritten parameter map
	 */
	Map<String, String[]> replacePortletIds(Map<String, String[]> paramMap) {
		Map<String, String[]> result = new HashMap<String, String[]>();
		for (Iterator<Map.Entry<String, String[]>> it = paramMap.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, String[]> param = it.next();
			String paramName = param.getKey();
			String[] paramValue = param.getValue();
			// for parameters "spf_p.tpst" and "spf_p.pst" we have to replace the name and the value of the parameter
			if ("spf_p.tpst".equals(paramName) || "spf_p.pst".equals(paramName)) {
				for (int i = 0, len = paramValue.length; i < len; ++i) {
					// paramValue[i] may contain portlet friendly ID but also window state (e.g. [portlet friendly id]_ws_MX)
					String portletFriendlyId = paramValue[i];
					int pos = paramValue[i].lastIndexOf("_ws_");
					if (pos == -1) {
						pos = paramValue[i].lastIndexOf("_pm_");
					}
					if (pos != -1) {
						portletFriendlyId = paramValue[i].substring(0, pos);
					}

					String uid = mPortletFriendlyIdToUidMap.get(portletFriendlyId);
					if (uid != null) {
						// in order to avoid table copy I'll update the original value
						if (pos == -1) {
							paramValue[i] = uid;
						}
						else {
							paramValue[i] = uid + paramValue[i].substring(pos);
						}
					}
				}

				if ("spf_p.tpst".equals(paramName)) {
					result.put("javax.portlet.tpst", paramValue);
				}
				else if ("spf_p.pst".equals(paramName)) {
					result.put("javax.portlet.pst", paramValue);
				}
			}
			// for parameters starting with "spf_p.prp_" and "spf_p.pbp" we have to rename parameters
			// but keeping values the same
			else if (paramName.startsWith("spf_p.prp_") || paramName.startsWith("spf_p.pbp_")) {
				boolean isPublicRenderParameter = paramName.startsWith("spf_p.pbp_");
				String newParamName = paramName;
				for (Iterator<Map.Entry<String, String>> it2 = mPortletFriendlyIdToUidMap.entrySet().iterator(); it2.hasNext(); ) {
					Map.Entry<String, String> entry = it2.next();
					String friendlyId = entry.getKey();
					String uid = entry.getValue();
					String namePrefix = (isPublicRenderParameter ? "spf_p.pbp_" : "spf_p.prp_") + friendlyId + "_";
					if (paramName.startsWith(namePrefix)) {
						newParamName =
								(isPublicRenderParameter ? "javax.portlet.pbp_" : "javax.portlet.prp_") +
								uid + "_" + paramName.substring(namePrefix.length());
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
