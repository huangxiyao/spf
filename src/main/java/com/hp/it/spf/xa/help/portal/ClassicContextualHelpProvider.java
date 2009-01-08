/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal;

import javax.servlet.http.HttpServletRequest;

import com.vignette.portal.website.enduser.PortalContext;

import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.Utils;

/**
 * <p>
 * A concrete contextual help provider, which produces the "classic"-style
 * contextual help popup window in a portal component.
 * </p>
 * <p>
 * This is the style of contextual-help popup which is rendered by the portal
 * framework's <code>&lt;spf-help-portal:classicContextualHelp&gt;</code> tag.
 * It is also the style rendered by the portal framework's
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag.
 * Use that tag inside the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag
 * body to render this kind of contextual-help popup into a message with your
 * chosen attributes. (Using the
 * <code>&lt;spf-i18n-portal:i18nClassicContextualHelpParam&gt;</code> tag
 * instantiates this class.) You can also instantiate this class directly and
 * pass it to the <code>I18nUtility.getValue</code> methods of the portal
 * framework to produce this classic-style contextual-help popup.
 * </p>
 * <p>
 * This class just implements some portal-specific concrete methods which the
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
	 * The portal context for this classic contextual help provider.
	 */
	private PortalContext portalContext = null;

	/**
	 * <p>
	 * Constructor for the "classic"-style contextual help provider for a
	 * particular request. If a null PortalContext is provided, the getHTML
	 * methods of this class will not work (ie will return null).
	 * </p>
	 */
	public ClassicContextualHelpProvider(PortalContext pContext) {
		portalContext = pContext;
	}

	/**
	 * <p>
	 * Returns the HTML string for the "classic"-style contextual help,
	 * including the link content surrounded by a hyperlink which, if clicked,
	 * will reveal the contextual help layer in an appropriately-formed DHTML
	 * popup layer.
	 * </p>
	 * <p>
	 * The first boolean parameter controls whether or not to escape any HTML
	 * special characters like <code>&lt;</code> (ie convert them to
	 * corresponding HTML character entities so that they display literally)
	 * found in the link content.
	 * </p>
	 * <p>
	 * The second boolean parameter controls whether or not to remove any HTML
	 * <code>&lt;SPAN&gt;</code> markup from the link content, which Vignette
	 * may have automatically added.
	 * </p>
	 * <p>
	 * This method requires that a valid PortalContext was given to the
	 * constructor. If not, it returns null.
	 * </p>
	 * 
	 * @param escape
	 *            Whether or not to escape HTML in the link content.
	 * @param filterSpan
	 *            Whether or not to strip <code>&lt;SPAN&gt;</code> from the
	 *            link content.
	 * @return The HTML string for a global help hyperlink containing the link
	 *         content that was set.
	 */
	public String getHTML(boolean escape, boolean filterSpan) {

		String html = null;
		if (filterSpan) {
			// Filter span in the link content.
			String origLinkContent = this.linkContent;
			this.linkContent = I18nUtility.filterSpan(origLinkContent);
			// Filter span in the title content.
			String origTitleContent = this.titleContent;
			this.titleContent = I18nUtility.filterSpan(origTitleContent);
			// Filter span in the help content.
			String origHelpContent = this.helpContent;
			this.helpContent = I18nUtility.filterSpan(origHelpContent);
			// Now the contents have all been filtered, get the HTML and then
			// restore the original values (because they are supposed to be kept
			// in unescaped, unfiltered state).
			html = super.getHTML(escape);
			this.linkContent = origLinkContent;
			this.titleContent = origTitleContent;
			this.helpContent = origHelpContent;
		} else {
			html = super.getHTML(escape);
		}
		return html;
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
		if (portalContext != null) {
			HttpServletRequest request = portalContext.getHttpServletRequest();
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
		if (portalContext != null) {
			HttpServletRequest request = portalContext.getHttpServletRequest();
			if (request != null) {
				count = getClassicContextualHelpCounter();
				count++;
				request.setAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR,
						String.valueOf(count));
			}
		}

	}

	/**
	 * A concrete implementation, to reset the counter of how many times in this
	 * request lifecycle a classic contextual help provider has been generated
	 * (ie its getHTML method has been invoked). Different action for portal and
	 * portlet, so this is an abstract method.
	 */
	public void resetClassicContextualHelpCounter() {
		if (portalContext != null) {
			HttpServletRequest request = portalContext.getHttpServletRequest();
			if (request != null) {
				request.removeAttribute(CLASSIC_CONTEXTUAL_HELP_COUNTER_ATTR);
			}
		}
	}

}
