/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

/**
 * <p>
 * The abstract base class for contextual help providers and portal global help
 * providers. Subclass this to provide a base class for each general kind of
 * help.
 * </p>
 * <p>
 * For our purposes, contextual help is a hyperlink surrounding some content
 * (called the "link content"), which when clicked opens a view (eg a popup)
 * into some other content (called the "help content"). Global help is a similar
 * hyperlink surrounding some link context, but when clicked it opens a view
 * into the portal's global help secondary page. This class represents what both
 * kinds of help have in common.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class HelpProvider {

	/**
	 * All help has link content - the text or HTML markup surrounded by the
	 * help hyperlink.
	 */
	protected String linkContent = "";

	/**
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate concrete subclass instead.
	 */
	protected HelpProvider() {

	}

	/**
	 * Setter for the link content string: any string of text or HTML which you
	 * want to surround with a hyperlink for some kind of help. When using this
	 * method, you should pass unescaped content.
	 * 
	 * @param pLinkContent
	 *            The link content.
	 */
	public void setLinkContent(String pLinkContent) {
		if (pLinkContent == null) {
			pLinkContent = "";
		}
		this.linkContent = pLinkContent.trim();
	}

	/**
	 * Returns the HTML string for the help hyperlink, including the link
	 * content surrounded by a hyperlink which, if clicked, will reveal the help
	 * content in an appropriate manner. The boolean parameter indicates whether
	 * to escape the content of the hyperlink -- ie, whether to convert any HTML
	 * special characters like <code>&lt;</code> into the equivalent HTML
	 * character entities. By default this is not done, but if you set the
	 * boolean to true this conversion will be performed.
	 * 
	 * @param escape
	 *            Whether to escape the HTML in the link content (and any other
	 *            content contained in the returned HTML)
	 * @return A string containing all of the HTML for the help hyperlink.
	 */
	public abstract String getHTML(boolean escape);

}
