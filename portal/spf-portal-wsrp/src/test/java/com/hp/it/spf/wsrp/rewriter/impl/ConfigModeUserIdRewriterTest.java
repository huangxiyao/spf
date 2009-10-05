package com.hp.it.spf.wsrp.rewriter.impl;

import junit.framework.TestCase;
import oasis.names.tc.wsrp.v2.types.GetMarkup;
import oasis.names.tc.wsrp.v2.types.HandleEvents;
import oasis.names.tc.wsrp.v2.types.MarkupParams;
import oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v2.types.UserContext;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class ConfigModeUserIdRewriterTest extends TestCase {

	/**
	 * Rewriting should only happen for <tt>vignette:config</tt> portlet mode
	 */
	public void testRewriteForConfigMode() {
		ConfigModeUserIdRewriter rewriter = new ConfigModeUserIdRewriter();
		MarkupParams markupParams = new MarkupParams();
		markupParams.setMode("vignette:config");
		UserContext userContext = new UserContext();

		userContext.setUserContextKey("john");
		GetMarkup getMarkup = new GetMarkup(null, null, null, userContext, markupParams);
		rewriter.rewrite(getMarkup);
		assertEquals("User rewritten in GetMarkup", ConfigModeUserIdRewriter.SPF_CONFIG_ADMIN_NAME, userContext.getUserContextKey());

		userContext.setUserContextKey("john");
		PerformBlockingInteraction performBlockingInteraction =
				new PerformBlockingInteraction(null, null, null, userContext, markupParams, null);
		rewriter.rewrite(performBlockingInteraction);
		assertEquals("User rewritten in PerformBlockingInteraction",
				ConfigModeUserIdRewriter.SPF_CONFIG_ADMIN_NAME, userContext.getUserContextKey());

		userContext.setUserContextKey("john");
		HandleEvents handleEvents = new HandleEvents(null, null, null, userContext, markupParams, null);
		rewriter.rewrite(handleEvents);
		assertEquals("User rewritten in HandleEvents",
				ConfigModeUserIdRewriter.SPF_CONFIG_ADMIN_NAME, userContext.getUserContextKey());
	}

	/**
	 * Invoking the portlet in any other mode should not result in any changes in the userContextKey.
	 */
	public void testRewriteForNonConfigMode() {
		ConfigModeUserIdRewriter rewriter = new ConfigModeUserIdRewriter();
		MarkupParams markupParams = new MarkupParams();
		UserContext userContext = new UserContext();
		userContext.setUserContextKey("john");
		GetMarkup getMarkup = new GetMarkup(null, null, null, userContext, markupParams);

		markupParams.setMode("wsrp:view");
		rewriter.rewrite(getMarkup);
		assertEquals("User not rewritten for wsrp:view mode", "john", userContext.getUserContextKey());

		markupParams.setMode("wsrp:edit");
		rewriter.rewrite(getMarkup);
		assertEquals("User not rewritten for wsrp:edit mode", "john", userContext.getUserContextKey());

		markupParams.setMode("wsrp:help");
		rewriter.rewrite(getMarkup);
		assertEquals("User not rewritten for wsrp:help mode", "john", userContext.getUserContextKey());
	}
}
