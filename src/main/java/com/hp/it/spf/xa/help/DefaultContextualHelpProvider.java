/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

import com.hp.it.spf.xa.help.ContextualHelpProvider;

/**
 * <p>
 * A concrete contextual help provider, which produces a default-style
 * contextual help popup window.
 * </p>
 * <p>
 * <b>In the portlet framework:</b> This is the style of contextual-help popup
 * which is rendered by the <code>&lt;spf-help-portlet:contextualHelp&gt;</code>
 * tag. This is also the style of contextual-help popup which is rendered by the
 * <code>&lt;spf-i18n-portlet:contextualHelpParam&gt;</code> tag. Use that tag
 * inside the <code>&lt;spf-i18n-portlet:message&gt;</code> tag body to render
 * this kind of contextual-help popup into a message with your chosen
 * attributes. (Using the
 * <code>&lt;spf-i18n-portlet:contxtualHelpParam&gt;</code> tag instantiates
 * this class.) You can also instantiate this class directly, and pass it to the
 * portlet <code>I18nUtility.getMessage</code> methods to produce a message
 * containing this style of contextual-help popup.
 * </p>
 * <p>
 * <b>In the portal framework:</b> This is the style of contextual-help popup
 * which is rendered by the <code>&lt;spf-help-portal:contextualHelp&gt;</code>
 * tag. This is also the style of contextual-help popup which is rendered by the
 * <code>&lt;spf-i18n-portal:i18nContextualHelpParam&gt;</code> tag. Use this
 * tag inside the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body to
 * render this kind of contextual-help popup into a message with your chosen
 * attributes. (Using the
 * <code>&lt;spf-i18n-portal:i18nContextualHelpParam&gt;</code> tag
 * instantiates this class.) You can also instantiate this class directly and
 * pass it to the portal <code>I18nUtility.getValue</code> methods to produce
 * a message containing this style of contextual-help popup.
 * </p>
 * <p>
 * If you are not happy with the default contextual-help popup style, you can
 * implement your own. Just extend the abstract base class like this one does.
 * You can even implement a tag for it, similar to the ones mentioned above.
 * </p>
 * <p>
 * TODO: Need to finish implementing this class. The logic for the
 * implementation should largely come from the ContextualHelpBaseTag class in
 * the Service Portal (OS) code.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class DefaultContextualHelpProvider extends ContextualHelpProvider {

	/**
	 * Default contextual help has a title. This is the title content.
	 */
	private String titleContent = "";

	/**
	 * Default contextual help can put an alternate URL in the contextual-help
	 * hyperlink which the browser will open if JavaScript is not supported.
	 * (Default contextual help style requires JavaScript, so this allows for a
	 * noscript alternative.)
	 */
	private String noScriptHref = "";

	/**
	 * Empty constructor; use the setters to provide the attributes.
	 */
	public DefaultContextualHelpProvider() {

	}

	/**
	 * Setter for the title content string: any string of text or HTML which you
	 * want to display as the title of the default-style contextual-help popup.
	 * Depending on how the contextual help is invoked, your title content may
	 * or may not later be escaped (ie conversion of HTML special characters
	 * like <code>&lt;</code> inside the title content to their corresponding
	 * HTML character entities). When using this method, you should pass
	 * unescaped content.
	 * 
	 * @param pTitleContent
	 *            The title content.
	 */
	public void setTitleContent(String pTitleContent) {
		if (pTitleContent == null) {
			pTitleContent = "";
		}
		this.titleContent = pTitleContent;
	}

	/**
	 * Setter for the noscript URL: any URL you want to be opened instead of the
	 * contextual-help popup in a non-JavaScript-enabled browser. Default-style
	 * contextual help requires the use of JavaScript, so this lets you provide
	 * an alternative experience for non-JavaScript-enabled browsers.
	 * 
	 * @param pNoScriptHref
	 *            A URL to fallback upon in the noscript case.
	 */
	public void setNoScriptHref(String pNoScriptHref) {
		if (pNoScriptHref == null) {
			pNoScriptHref = "";
		}
		this.noScriptHref = pNoScriptHref;
	}

	/**
	 * Returns the HTML string for the contextual help, including the link
	 * content surrounded by a hyperlink which, if clicked, will reveal the help
	 * content in an appropriately-formed popup window.
	 * 
	 * TODO: Must implement this method, see notes above.
	 * 
	 * @param escape
	 * @return
	 */
	public String getHTML(boolean escape) {
		String html = "";
		return html;
	}

}
