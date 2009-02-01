package com.hp.it.spf.wsrp.rewriter.impl;

import com.hp.it.spf.wsrp.rewriter.IRewriter;
import com.hp.it.spf.wsrp.rewriter.Predicates;
import oasis.names.tc.wsrp.v2.types.GetMarkup;
import oasis.names.tc.wsrp.v2.types.HandleEvents;
import oasis.names.tc.wsrp.v2.types.MarkupParams;
import oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v2.types.UserContext;
import org.apache.axis.MessageContext;

/**
 * Rewrites the <tt>userContextKey</tt> to a generic admin user for portlets invoked in <tt>config</tt>
 * mode. It should be invoked only for OpenPortal WSRP producer.<p>
 * Config mode is used to define the configuration for the given portlet instance for all the users.
 * OpenPortal scopes the portlet prefernces (used often to store portlet configuration) to the users
 * invoking the portlet. Without this rewrting the portlet preferences defined in config mode would
 * be specific to the actual administrator user which invoked the portlet and would be invisible to
 * the end users. Changing the current user to generic admin user allows on the producer side to use
 * this user's preferences and merge them with end user preferences. The merge behavior is not
 * provided by OpenPortal either and requires SPF-specfic implementation of the appropriate peristence
 * class to perform it.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class ConfigModeUserIdRewriter implements IRewriter {

	/**
	 * Name of the generic admin user
	 */
	static final String SPF_CONFIG_ADMIN_NAME = "SPF_CONFIG_ADMIN";


	/**
	 * @param messageContext web service call message context
	 * @return <tt>true</tt> if inoved for OpenPortal producer and WSRP V2 markup methods
	 */
	public boolean shouldApply(MessageContext messageContext) {
		return Predicates.isOpenPortalProducer(messageContext) &&
				Predicates.isAnyOfMethods(messageContext, "v2:getMarkup", "v2:performBlockingInteraction", "v2:handleEvents");
	}

	/**
	 * Rewrites <tt>userContextKey</tt> for portlets invoked in <tt>config</tt> mode.
	 *
	 * @param data WSRP request or reponse object
	 */
	public void rewrite(Object data) {
		MarkupParams markupParams = null;
		UserContext userContext = null;

		if (data instanceof GetMarkup) {
			markupParams = ((GetMarkup) data).getMarkupParams();
			userContext = ((GetMarkup) data).getUserContext();
		}
		else if (data instanceof PerformBlockingInteraction) {
			markupParams = ((PerformBlockingInteraction) data).getMarkupParams();
			userContext = ((PerformBlockingInteraction) data).getUserContext();
		}
		else if (data instanceof HandleEvents) {
			markupParams = ((HandleEvents) data).getMarkupParams();
			userContext = ((HandleEvents) data).getUserContext();
		}
		else {
			throw new IllegalArgumentException("Don't know how to rewrite object of type: " +
					(data == null ? "null" : data.getClass().getName()));
		}

		if (markupParams == null || userContext == null) {
			throw new IllegalStateException("markupParams or userContext is null");
		}

		if (isPortletUsingConfigMode(markupParams)) {
			replaceUserContextKey(userContext, SPF_CONFIG_ADMIN_NAME);
		}
	}

	/**
	 * @param markupParams markup parameters
	 * @return <tt>true</tt> if portlet invoked mode is <tt>vignette:config</tt>
	 */
	private boolean isPortletUsingConfigMode(MarkupParams markupParams) {
		return "vignette:config".equals(markupParams.getMode());
	}

	/**
	 * Changes userContextKey to the given value
	 *
	 * @param userContext	   user context
	 * @param newUserContextKey new user context key which replaces the actual user ID
	 */
	private void replaceUserContextKey(UserContext userContext, String newUserContextKey) {
		userContext.setUserContextKey(newUserContextKey);
	}
}
