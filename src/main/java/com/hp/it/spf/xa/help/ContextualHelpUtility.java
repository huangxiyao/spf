/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

import com.hp.it.spf.xa.help.HelpUtility;
import com.hp.it.spf.xa.help.ContextualHelpProvider;

/**
 * <p>
 * A utility class for contextual help in both the portal and portlet arenas.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ContextualHelpUtility extends HelpUtility {

	/**
	 * Empty constructor.
	 */
	public ContextualHelpUtility () {
		
	}
	
	/**
	 * <p>
	 * Parses the given message string for the special tokens representing
	 * contextual help (<code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code>)
	 * and replaces them with contextual help per the given
	 * ContextualHelpProviders.  See the superclass documentation for more information.
	 * </p>
	 * 
	 * @param msg
	 *            The message string.
	 * @param cParams
	 *            An array of ContextualHelpProviders corresponding to the
	 *            tokens in the message string.
	 * @param escapeHTML
	 *            Whether to convert HTML special characters into character
	 *            entities.
	 * @return The given message string, interpolated with all applicable help
	 *         markup.
	 */
	public String parseContextualHelpTokens(String msg,
			ContextualHelpProvider[] cParams, boolean escapeHTML) {
		this.token = "CONTEXTUAL_HELP";
		return super.parseHelp(msg, cParams, escapeHTML);
	}

}
