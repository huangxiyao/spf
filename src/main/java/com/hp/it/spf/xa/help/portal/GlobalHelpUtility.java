/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help.portal;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.hp.it.spf.xa.misc.Utils;
import com.hp.it.spf.xa.help.HelpUtility;
import com.hp.it.spf.xa.help.ContextualHelpProvider;

/**
 * <p>
 * A utility class for portal global help.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class GlobalHelpUtility extends HelpUtility {

	/**
	 * Empty constructor.
	 */
	public GlobalHelpUtility () {
		
	}
	
	/**
	 * <p>
	 * Parses the given message string for the special tokens representing
	 * global help (<code>&lt;Global_Help&gt;...&lt;/Global_Help&gt;</code>)
	 * and replaces them with global help per the given
	 * GlobalHelpProviders.  See the superclass documentation for more information.
	 * </p>
	 * 
	 * @param msg
	 *            The message string.
	 * @param gParams
	 *            An array of GlobalHelpProviders corresponding to the
	 *            tokens in the message string.
	 * @param escapeHTML
	 *            Whether to convert HTML special characters into character
	 *            entities.
	 * @return The given message string, interpolated with all applicable help
	 *         markup.
	 */
	public String parseGlobalHelp(String msg,
			GlobalHelpProvider[] gParams, boolean escapeHTML) {
		this.token = "GLOBAL_HELP";
		return super.parseHelp(msg, gParams, escapeHTML);
	}

}
