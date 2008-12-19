/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

import com.hp.it.spf.xa.help.HelpProvider;

/**
 * <p>
 * The abstract base class for contextual help providers. Subclass this to
 * provide a concrete implementation of a contextual help popup for your
 * particular application. For example, the SPF-provided "classic"-style
 * of contextual help is provided by a subclass of this one.
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
public abstract class ContextualHelpProvider extends HelpProvider {

	/**
	 * All contextual help has help content -- the text or HTML markup revealed
	 * when the contextual-help hyperlink is clicked.
	 */
	protected String helpContent = "";

	/**
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate subclass instead.
	 */
	protected ContextualHelpProvider() {

	}

	/**
	 * Setter for the help content string: any string of text or HTML which you
	 * want to display inside the contextual-help popup. When using this method,
	 * you should pass unescaped content.
	 * 
	 * @param pHelpContent
	 *            The help content.
	 */
	public void setHelpContent(String pHelpContent) {
		if (pHelpContent == null) {
			pHelpContent = "";
		}
		pHelpContent = pHelpContent.trim();
		this.helpContent = pHelpContent;
	}
}
