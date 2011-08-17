package com.hp.spp.wsrp.context;

import com.hp.spp.portlets.filter.context.UserContextFilter;

import javax.portlet.PortletRequest;
import java.util.Map;

import org.apache.log4j.Logger;

public class ContextHelper {
	private static final Logger mLog = Logger.getLogger(ContextHelper.class);

	private ContextHelper() {}

	/**
	 * Looks up the request for user context keys map using the default attribute name defined by
	 * <tt>UserContextExtractor.USER_CONTEXT_KEYS_KEY</tt>. If not found it will attempt to use
	 * user-defined key stored in request attribute <tt>UserContextFilter.USER_CONTEXT_KEYS_REF_ATTRIBUTE_KEY</tt>.
	 * @param request current request
	 * @return user context keys map or <tt>null</tt> if not found
	 */
	public static Map getUserContextKeys(PortletRequest request) {
		String keyName = UserContextExtractor.USER_CONTEXT_KEYS_KEY;
		Map map = (Map) request.getAttribute(keyName);
		if (map == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("User context keys map not found in PortletRequest using '" + keyName + "' attribute name");
			}
			keyName = (String) request.getAttribute(UserContextFilter.USER_CONTEXT_KEYS_REF_ATTRIBUTE_KEY);
			if (keyName != null) {
				map = (Map) request.getAttribute(keyName);
				if (map == null) {
					if (mLog.isDebugEnabled()) {
						mLog.debug("User context keys map not found in PortletRequest using '" + keyName + "' attribute name");
					}
				}
			}
		}

		return map;

	}

	/**
	 * Looks up the request for user profile map using the default attribute name defined by
	 * <tt>UserContextExtractor.USER_PROFILE_KEY</tt>. If not found it will attempt to use
	 * user-defined key stored in request attribute <tt>UserContextFilter.PROFILE_REF_ATTRIBUTE_KEY</tt>.
	 * @param request current request
	 * @return user profile map or <tt>null</tt> if not found
	 */
	public static Map getUserProfile(PortletRequest request) {
		String keyName = UserContextExtractor.USER_PROFILE_KEY;
		Map map = (Map) request.getAttribute(keyName);
		if (map == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("Profile map not found in PortletRequest using '" + keyName + "' attribute name");
			}
			keyName = (String) request.getAttribute(UserContextFilter.PROFILE_REF_ATTRIBUTE_KEY);
			if (keyName != null) {
				map = (Map) request.getAttribute(keyName);
				if (map == null) {
					if (mLog.isDebugEnabled()) {
						mLog.debug("Profile map not found in PortletRequest using '" + keyName + "' attribute name");
					}
				}
			}
		}

		return map;
	}
}
