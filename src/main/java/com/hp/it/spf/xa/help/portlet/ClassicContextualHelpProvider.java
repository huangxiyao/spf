/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portlet;

import javax.portlet.PortletRequest;

/**
 * <p>
 * A concrete contextual help provider, which produces the "classic"-style
 * contextual help popup window in a portlet.
 * </p>
 * <p>
 * This is the style of contextual-help popup which is rendered by the portlet
 * framework's <code>&lt;spf-help-portlet:classicContextualHelp&gt;</code>
 * tag. It is also the style rendered by the portlet framework's
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag. Use
 * that tag inside the <code>&lt;spf-i18n-portlet:message&gt;</code> tag body
 * to render this kind of contextual-help popup into a message with your chosen
 * attributes. (Using the
 * <code>&lt;spf-i18n-portlet:classicContextualHelpParam&gt;</code> tag
 * instantiates this class.) You can also instantiate this class directly and
 * pass it to the <code>I18nUtility.getMessage</code> methods of the portlet
 * framework to produce this classic-style contextual-help popup.
 * </p>
 * <p>
 * This class just implements some portlet-specific concrete methods which the
 * generic superclass cannot. The main logic for generating the contextual-help
 * popup is in the superclass.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicContextualHelpProvider extends
		com.hp.it.spf.xa.help.ClassicContextualHelpProvider {

	/**
	 * The request for this classic contextual help provider.
	 */
	private PortletRequest request = null;

	/**
	 * <p>
	 * Constructor for the "classic"-style contextual help provider for a
	 * particular request. If a null PortletRequest is provided, the getHTML
	 * methods of this class will not work (ie will return null).
	 * </p>
	 */
	public ClassicContextualHelpProvider(PortletRequest pRequest) {
		request = pRequest;
	}

	/**
	 * A concrete implementation, to get a counter of how many times in this
	 * request lifecycle a classic contextual help provider has been generated
	 * (ie its getHTML method has been invoked).
	 * 
	 * @return The counter.
	 */
	public int getClassicContextualHelpCounter() {

		int count = 0;
		if (request != null) {
			Object countString = request
					.getAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR);
			if (countString == null) {
				count = 0;
			} else {
				try {
					count = Integer.parseInt((String) countString);
				} catch (NumberFormatException e) { // should never happen
					count = 0;
				}
			}
		}
		return count;

	}

	/**
	 * A concrete implementation, to increment the counter of how many times in
	 * this request lifecycle a classic contextual help provider has been
	 * generated (ie its getHTML method has been invoked). Different action for
	 * portal and portlet, so this is an abstract method.
	 */
	public void bumpClassicContextualHelpCounter() {

		int count = 0;
		if (request != null) {
			count = getClassicContextualHelpCounter();
			count++;
			request.setAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR, String
					.valueOf(count));
		}
	}

	/**
	 * A concrete implementation, to reset the counter of how many times in this
	 * request lifecycle a classic contextual help provider has been generated
	 * (ie its getHTML method has been invoked). Different action for portal and
	 * portlet, so this is an abstract method.
	 */
	public void resetClassicContextualHelpCounter() {
		if (request != null) {
			request.removeAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR);
		}
	}
}
