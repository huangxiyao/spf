/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

/**
 * <p>
 * The abstract base class for contextual help providers. Subclass this to
 * provide a concrete implementation of a contextual help popup for your
 * particular portlet.
 * </p>
 * <p>
 * For our purposes, contextual help is a hyperlink surrounding some content
 * (called the "link content"), which when clicked opens a view (eg a popup)
 * into some other content (called the "help content").
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class ContextualHelpProvider {

	/**
	 * All contextual help has help content.
	 */
	protected String helpContent = "";

	/**
	 * All contextual help has link content.
	 */
	protected String linkContent = "";

	/**
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate subclass instead.
	 */
	protected ContextualHelpProvider() {

	}

	/**
	 * Setter for the help content string: any string of text or HTML which you
	 * want to display inside the contextual-help popup. Depending on how the
	 * contextual help is invoked, your help content may or may not later be
	 * escaped (ie conversion of HTML special characters like <code>&lt;</code>
	 * inside the help content to their corresponding HTML character entities).
	 * When using this method, you should pass unescaped content.
	 * 
	 * @param pHelpContent
	 *            The help content.
	 */
	public void setHelpContent(String pHelpContent) {
		if (pHelpContent == null) {
			pHelpContent = "";
		}
		this.helpContent = pHelpContent;
	}

	/**
	 * Setter for the link content string: any string of text or HTML which you
	 * want to surround with a hyperlink for the contextual-help popup.
	 * Depending on how the contextual help is invoked, your link content may or
	 * may not later be escaped (ie conversion of HTML special characters like
	 * <code>&lt;</code> inside the link content to their corresponding HTML
	 * character entities). When using this method, you should pass unescaped
	 * content.
	 * 
	 * @param pLinkContent
	 *            The help content.
	 */
	public void setLinkContent(String pLinkContent) {
		if (pLinkContent == null) {
			pLinkContent = "";
		}
		this.linkContent = pLinkContent;
	}

	/**
	 * Returns the HTML string for the contextual help, including the link
	 * content surrounded by a hyperlink which, if clicked, will reveal the help
	 * content in an appropriately-formed popup window.
	 * 
	 * @param escape
	 * @return
	 */
	public abstract String getHTML(boolean escape);

}
