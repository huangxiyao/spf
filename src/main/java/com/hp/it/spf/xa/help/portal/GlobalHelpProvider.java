/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal;

import com.hp.it.spf.xa.help.HelpProvider;

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
	 * Protected to prevent external construction except by subclasses. Use an
	 * appropriate subclass instead.
	 */
	protected GlobalHelpProvider() {

	}

}
