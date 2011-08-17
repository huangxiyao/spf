package com.hp.spp.portlets.filter.context;

import java.io.IOException;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.apache.portals.bridges.portletfilter.PortletFilter;
import org.apache.portals.bridges.portletfilter.PortletFilterChain;
import org.apache.portals.bridges.portletfilter.PortletFilterConfig;
import com.hp.spp.wsrp.context.UserContextExtractor;

public class UserContextFilter implements PortletFilter {

	private static Logger mLog = Logger.getLogger(UserContextFilter.class);

	public static final String PARAM_USER_CONTEXT_KEYS = "UserContextKeysAttributeName";
	public static final String PARAM_USER_CONTEXT = "UserContextAttributeName";
	public static final String PARAM_USER_PROFILE = "UserProfileAttributeName";
	public static final String PARAM_IMPERSONATION = "ImpersonationMode";
	public static final String PARAM_USER_CONTEXT_URL = "UserContextUrl";
	public static final String PARAM_PROXY_HOST = "ProxyHost";
	public static final String PARAM_PROXY_PORT = "ProxyPort";

	public static final String PROFILE_SIMULATOR_KEY = "SimulatingUser";

	/**
	 * Name of the request attribute containing the request key under which the profile map is bound.
	 * If the attribute with this name doesn't exist, the profile map has been bound to the default
	 * attribute name.
	 * @see DEFAULT_PROFILE_ATTRIBUTE_KEY
	 */
	public static final String PROFILE_REF_ATTRIBUTE_KEY = UserContextExtractor.USER_PROFILE_KEY + ".Ref";

	/**
	 * Name of the request attribute containing the request key under which the user context keys map
	 * is bound. if the attribute with this name doesn't exist, the profile map has been bound to the
	 * default attribute name.
	 * @see DEFAULT_CONTEXT_KEYS_ATTRIBUTE_KEY
	 */
	public static final String USER_CONTEXT_KEYS_REF_ATTRIBUTE_KEY = UserContextExtractor.USER_CONTEXT_KEYS_KEY + ".Ref";


	public static final String DEFAULT_PROFILE_ATTRIBUTE_KEY = UserContextExtractor.USER_PROFILE_KEY;
	public static final String DEFAULT_CONTEXT_KEYS_ATTRIBUTE_KEY = UserContextExtractor.USER_CONTEXT_KEYS_KEY;

	public static final String IMPERSONATION_MODE_BOTH = "1";
	public static final String IMPERSONATION_MODE_BLOCKED = "2";
	public static final String IMPERSONATION_MODE_IMPERSONATED_ONLY = "3";
	public static final String IMPERSONATION_MODE_IMPERSONATOR_ONLY = "4";

	public static final String DEFAULT_IMPERSONATION_MODE = IMPERSONATION_MODE_BOTH;

	/**
	 * Name of the request attribute containing a Boolean object indicating whether the impersonation
	 * is occuring for this request.
	 */
	public static final String IMPERSONATING_ATTRIBUTE_KEY = "com.hp.spp.Impersonating";

	/**
	 * Name of the request attribute used to bind user profile Map in the request. The value of
	 * the attribute name can be configured in the filter config using {@link PARAM_USER_PROFILE}
	 * or {@link PARAM_USER_CONTEXT} init parameters.
	 */
	private String mProfileAttributeKey = DEFAULT_PROFILE_ATTRIBUTE_KEY;

	/**
	 * Name of the request attribute used to bind user context keys Map in the request. The value of
	 * the attribute name can be configured in the filter config using {@link PARAM_USER_CONTEXT_KEYS}
	 * init parameters.
	 */
	private String mContextKeysAttributeKey = DEFAULT_CONTEXT_KEYS_ATTRIBUTE_KEY;

	/**
	 * Impersonation mode supported by the portlet. It can be configured using filter config
	 * {@link PARAM_IMPERSONATION} init parameter. Its value can be one of the constants in
	 * this class <tt>IMPERSONATION_MODE_XXX</tt>.
	 */
	private String mImpersonationMode = DEFAULT_IMPERSONATION_MODE;

	public void init(PortletFilterConfig portletFilterConfig) throws PortletException {
		String contextKeysAttributeKey = portletFilterConfig.getInitParameter(PARAM_USER_CONTEXT_KEYS);
		if (contextKeysAttributeKey != null && !contextKeysAttributeKey.trim().equals("")) {
			mContextKeysAttributeKey = contextKeysAttributeKey.trim();
		}
		else {
			contextKeysAttributeKey = null;
		}

		String profileAttributeKey = portletFilterConfig.getInitParameter(PARAM_USER_PROFILE);
		if (profileAttributeKey == null || profileAttributeKey.trim().equals("")) {
			profileAttributeKey = portletFilterConfig.getInitParameter(PARAM_USER_CONTEXT);
		}
		if (profileAttributeKey != null && !profileAttributeKey.trim().equals("")) {
			mProfileAttributeKey = profileAttributeKey.trim();
		}
		else {
			profileAttributeKey = null;
		}

		String impersonationMode = portletFilterConfig.getInitParameter(PARAM_IMPERSONATION);
		if (impersonationMode != null && !impersonationMode.trim().equals("")) {
			mImpersonationMode = impersonationMode;
		}
		else {
			impersonationMode = null;
		}

		if (mLog.isInfoEnabled()) {
			mLog.info("Filter configured with the following data: " +
				PARAM_USER_CONTEXT_KEYS + "=" + mContextKeysAttributeKey + (contextKeysAttributeKey != null ? ", " : " (default), ") +
				PARAM_USER_PROFILE + "=" + mProfileAttributeKey + (profileAttributeKey != null ? ", " : " (default), ") +
				PARAM_IMPERSONATION + "=" + mImpersonationMode + (impersonationMode != null ? "" : " (default)"));
		}

	}

	public void renderFilter(RenderRequest renderRequest, RenderResponse renderResponse, PortletFilterChain portletFilterChain) throws PortletException, IOException {
		boolean impersonation = checkIfImpersonating(renderRequest);
		bindMaps(renderRequest, impersonation);
		if (!impersonation) {
			// if we are not in impersonation mode, then just let the portlet render
			portletFilterChain.renderFilter(renderRequest, renderResponse);
		}
		else {
			//
			if (!blockPortletWhileImpersonating()) {
				portletFilterChain.renderFilter(renderRequest, renderResponse);
			}
			else {
				//FIXME (slawek) - show message; localization
				renderResponse.setContentType("text/html");
				renderResponse.getWriter().println("<p>This portlet does not support impersonation!</p>");
			}
		}
	}

	public void processActionFilter(ActionRequest actionRequest, ActionResponse actionResponse, PortletFilterChain portletFilterChain) throws PortletException, IOException {
		boolean impersonation = checkIfImpersonating(actionRequest);
		bindMaps(actionRequest, impersonation);
		if (!impersonation) {
			portletFilterChain.processActionFilter(actionRequest, actionResponse);
		}
		else {
			// processAction cannot generate markup. If we block du to impersonation, this method does
			// nothing.
			if (!blockPortletWhileImpersonating()) {
				portletFilterChain.processActionFilter(actionRequest, actionResponse);
			}
		}
	}

	private void bindMaps(PortletRequest request, boolean impersonation) {
		// rebind user context keys only if another than default attribute is used
		if (!UserContextExtractor.USER_CONTEXT_KEYS_KEY.equals(mContextKeysAttributeKey)) {
			Map contextKeys = (Map) request.getAttribute(UserContextExtractor.USER_CONTEXT_KEYS_KEY);
			request.removeAttribute(UserContextExtractor.USER_CONTEXT_KEYS_KEY);
			request.setAttribute(mContextKeysAttributeKey, contextKeys);
			request.setAttribute(USER_CONTEXT_KEYS_REF_ATTRIBUTE_KEY, mContextKeysAttributeKey);
		}

		Map userProfile = (Map) request.getAttribute(UserContextExtractor.USER_PROFILE_KEY);
		// remove the complete profile map from request. we will rebind it below depending on
		// impersonation mode used.
		request.removeAttribute(UserContextExtractor.USER_PROFILE_KEY);
		if (!UserContextExtractor.USER_PROFILE_KEY.equals(mProfileAttributeKey)) {
			request.setAttribute(PROFILE_REF_ATTRIBUTE_KEY, mProfileAttributeKey);
		}

		if (!impersonation) {
			request.setAttribute(mProfileAttributeKey, userProfile);
		}
		else {
			// we are in impersonation mode
 			if (IMPERSONATION_MODE_BLOCKED.equals(mImpersonationMode)) {
				// do nothing - the profile was removed already below
			}
			else if (IMPERSONATION_MODE_IMPERSONATED_ONLY.equals(mImpersonationMode)) {
				 userProfile.remove(PROFILE_SIMULATOR_KEY);
				 request.setAttribute(mProfileAttributeKey, userProfile);
			}
			else if (IMPERSONATION_MODE_IMPERSONATOR_ONLY.equals(mImpersonationMode)) {
				 request.setAttribute(mProfileAttributeKey, userProfile.get(PROFILE_SIMULATOR_KEY));
			}
			else {
				 request.setAttribute(mProfileAttributeKey, userProfile);
			 }
		}
	}

	private boolean blockPortletWhileImpersonating() {
		return IMPERSONATION_MODE_BLOCKED.equals(mImpersonationMode);
	}

	private boolean checkIfImpersonating(PortletRequest request) {
		Boolean impersonating = (Boolean) request.getAttribute(IMPERSONATING_ATTRIBUTE_KEY);
		if (impersonating != null) {
			return impersonating.booleanValue();
		}
		//save in the request that there is impersonation going on as bindMaps, depending on
		// the impersonation mode, may remove the impersonated or impersonator user information.
		Map userProfile = (Map) request.getAttribute(UserContextExtractor.USER_PROFILE_KEY);
		impersonating = Boolean.valueOf(userProfile != null && userProfile.containsKey(PROFILE_SIMULATOR_KEY));
		request.setAttribute(IMPERSONATING_ATTRIBUTE_KEY, impersonating);
		return impersonating.booleanValue();
	}

	public void destroy() {
	}

}
