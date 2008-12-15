/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal;

import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;

/**
 * <p>
 * A concrete global help provider, which produces a default-style global help
 * popup window.
 * </p>
 * <p>
 * This is the style of global-help popup which is rendered by the portal
 * framework's <code>&lt;spf-help-portal:globalHelp&gt;</code> tag. It is also
 * the style rendered by the portal framework's
 * <code>&lt;spf-i18n-portal:i18nGlobalHelpParam&gt;</code> tag. Use that tag
 * inside the <code>&lt;spf-i18n-portal:i18nValue&gt;</code> tag body to
 * render this kind of global-help popup into a message with your chosen
 * attributes. (Using the
 * <code>&lt;spf-i18n-portal:i18nGlobalHelpParam&gt;</code> tag instantiates
 * this class.) You can also instantiate this class directly and pass it to the
 * <code>I18nUtility.getValue</code> methods of the portal framework to
 * produce this default-style global-help popup.
 * </p>
 * <p>
 * If you are not happy with the default global-help popup style, you can
 * implement your own. Just extend the abstract base class like this one does.
 * You can even implement a tag for it, similar to the above.
 * </p>
 * <p>
 * TODO: Need to finish implementing this class. The logic for the
 * implementation should largely come from the GlobalHelpBaseTag class in the
 * Service Portal (OS) code.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class DefaultGlobalHelpProvider extends GlobalHelpProvider {

	/**
	 * Default contextual help can put an alternate URL in the global-help
	 * hyperlink which the browser will open if JavaScript is not supported.
	 * (Default global help style requires JavaScript, so this allows for a
	 * noscript alternative.)
	 */
	private String noScriptHref = "";

	/**
	 * Empty constructor; use the setters to provide the attributes.
	 */
	public DefaultGlobalHelpProvider() {

	}

	/**
	 * Setter for the noscript URL: any URL you want to be opened instead of the
	 * global-help popup in a non-JavaScript-enabled browser. Default-style
	 * global help requires the use of JavaScript, so this lets you provide an
	 * alternative experience for non-JavaScript-enabled browsers.
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
	 * Returns the HTML string for the global help, including the link content
	 * surrounded by a hyperlink which, if clicked, will reveal the global help
	 * secondary page in an appropriately-formed popup window. The boolean
	 * parameter controls whether or not to escape any HTML special characters
	 * like <code>&lt;</code> (ie convert them to corresponding HTML character
	 * entities so that they display literally) found in the link content.
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
