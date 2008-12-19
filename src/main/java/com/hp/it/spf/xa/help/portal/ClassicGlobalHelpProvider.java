/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal;

import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;

/**
 * <p>
 * A concrete global help provider, which produces the "classic"-style global
 * help popup window.
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
 * If you are not happy with the "classic" global-help popup style, you can
 * implement your own. Just extend the abstract base class, GlobalHelpProvider,
 * like this one does. You can even implement a tag for it, similar to the
 * above.
 * </p>
 * <p>
 * TODO: Need to finish implementing this class. The logic for the
 * implementation should largely come from the GlobalHelpBaseTag.todo file
 * (taken from the GlobalHelpBaseTag.java file in the Service Portal (OS) code).
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ClassicGlobalHelpProvider extends GlobalHelpProvider {

	/**
	 * "Classic" global help has an optional page fragment - this is an anchor
	 * identifier within the global help page that you want the global help URL
	 * to point to.
	 */
	private String fragment = "";

	/**
	 * Empty constructor; use the setters to provide the attributes.
	 */
	public ClassicGlobalHelpProvider() {

	}

	/**
	 * Setter for the fragment string, which is an anchor name inside the global
	 * help HTML content to which you want the global help window to auto-scroll
	 * when the global help link is clicked. Having a fragment in your global
	 * help is optional. For example, if you set the fragment to
	 * <code>foo</code>, the global help hyperlink returned by the getHTML()
	 * method (see) will, when clicked, open a global-help window that
	 * auto-scrolls to the <code>&lt;a name="foo"&gt;</code> part of the
	 * document.
	 * 
	 * @param pFragment
	 *            The anchor name (the "#" symbol at the beginning is not
	 *            needed).
	 */
	public void setFragment(String pFragment) {
		if (pFragment == null) {
			pFragment = "";
		}
		pFragment = pFragment.trim();
		while (pFragment.startsWith("#")) {
			if (pFragment.length() > 1) 
				pFragment = pFragment.substring(1);
			else
				pFragment = "";
		}
		this.fragment = pFragment;
	}

	/**
	 * <p>
	 * Returns the HTML string for the "classic"-style global help, including
	 * the link content surrounded by a hyperlink which, if clicked, will reveal
	 * the global help secondary page in an appropriately-formed popup window.
	 * </p>
	 * <p>
	 * The boolean parameter controls whether or not to escape any HTML special
	 * characters like <code>&lt;</code> (ie convert them to corresponding
	 * HTML character entities so that they display literally) found in the link
	 * content.
	 * </p>
	 * <p>
	 * In this method, any HTML <code>&lt;SPAN&gt;</code> markup in the link
	 * content, which Vignette may have automatically added, is retained.
	 * </p>
	 * 
	 * @param escape
	 *            Whether or not to escape HTML in the link content.
	 * @return The HTML string for a global help hyperlink containing the link
	 *         content that was set.
	 */
	public String getHTML(boolean escape) {
		return getHTML(escape, false);
	}

	/**
	 * <p>
	 * Returns the HTML string for the global help, including the link content
	 * surrounded by a hyperlink which, if clicked, will reveal the global help
	 * secondary page in an appropriately-formed popup window.
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
	 * 
	 * TODO: Must implement this method, see notes above.
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
		String html = "";
		return html;
	}

}
