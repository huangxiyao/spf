/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal;

import com.hp.it.spf.xa.help.HelpProvider;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * The abstract base class for global help providers. Subclass this to provide a
 * concrete implementation of a global help popup for your particular
 * application.
 * </p>
 * <p>
 * For our purposes, global help is a hyperlink surrounding some content (called
 * the "link content"), which when clicked opens a view (eg a popup) into the
 * portal's global help secondary page.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class GlobalHelpProvider extends HelpProvider {

	/**
	 * The portal context for this global help provider.
	 */
	protected PortalContext portalContext = null;

	/**
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate subclass instead.
	 */
	protected GlobalHelpProvider(PortalContext pContext) {
		this.portalContext = pContext;
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
	 * @param escape
	 *            Whether or not to escape HTML in the link content.
	 * @param filterSpan
	 *            Whether or not to strip <code>&lt;SPAN&gt;</code> from the
	 *            link content.
	 * @return The HTML string for a global help hyperlink containing the link
	 *         content that was set.
	 */
	public abstract String getHTML(boolean escape, boolean filterSpan);

}
