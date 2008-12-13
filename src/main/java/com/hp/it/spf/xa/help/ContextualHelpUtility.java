/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.hp.it.spf.xa.misc.Utils;
import com.hp.it.spf.xa.help.ContextualHelpProvider;

/**
 * <p>
 * A utility class for contextual help in both the portal and portlet arenas.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ContextualHelpUtility {

	/**
	 * Opening token for a <code>&lt;Contextual_Help&gt;</code> tag.
	 */
	private static String BEGIN_CONTEXTUAL_HELP = "<CONTEXTUAL_HELP>";

	/**
	 * Closing token for a <code>&lt;Contextual_Help&gt;</code> tag.
	 */
	private static String END_CONTEXTUAL_HELP = "</CONTEXTUAL_HELP>";

	/**
	 * Regular expression for an opening or closing
	 * <code>&lt;Contextual_Help&gt;</code> tag.
	 */
	private static String CONTEXTUAL_HELP_REGEX = "</?CONTEXTUAL_HELP>";

	private static short BEGIN = 1;
	private static short END = 2;

	/**
	 * <p>
	 * Parses the given message string for the special tokens representing
	 * contextual help (<code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code>)
	 * and replaces them with contextual help per the given
	 * ContextualHelpProviders.
	 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> tokens
	 * may not be nested, but multiple may be contained within the message
	 * string. Each one that is found in the message string is stripped of the
	 * special tokens, and mated-up with the same ordinal ContextualHelpProvider
	 * in the given array. The content enclosed by the
	 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> tokens
	 * is taken as the link content for the contextual help popup; the other
	 * contextual-help parameters are assumed to already be set in the
	 * ContextualHelpProvider.
	 * </p>
	 * <p>
	 * Any contextual-help tokens which are out of proper sequence or which lack
	 * a corresponding ContextualHelpProvider are simply stripped from the
	 * message string and ignored. Similarly, any ContextualHelpProviders in the
	 * array which lack a corresponding token in the string are ignored.
	 * </p>
	 * <p>
	 * If the boolean parameter is true, the content of the message string is
	 * scanned for HTML special characters like <code>&lt;</code> - these are
	 * converted to their corresponding HTML character entities. Otherwise they
	 * are left untouched (this lets you put HTML markup into the message string
	 * and have it be treated as such by the browser).
	 * </p>
	 * 
	 * @param msg
	 *            The message string.
	 * @param cParams
	 *            An array of ContextualHelpProviders corresponding to the
	 *            tokens in the message string.
	 * @param escapeHTML
	 * @return
	 */
	public static String parseContextualHelp(String msg,
			ContextualHelpProvider[] cParams, boolean escapeHTML) {

		if (msg == null) {
			return null;
		}
		Pattern tokenPattern = Pattern.compile(CONTEXTUAL_HELP_REGEX,
				Pattern.CASE_INSENSITIVE);
		Matcher tokenMatcher = tokenPattern.matcher(msg);
		String result = "";
		String token, content = "";
		int matchTo = 0, matchFrom = 0, i = 0;
		short lookingFor = BEGIN;
		while (tokenMatcher.find()) {
			token = tokenMatcher.group();
			matchFrom = tokenMatcher.start();
			content += msg.substring(matchTo, matchFrom);
			if (lookingFor == BEGIN) {
				if (token.equalsIgnoreCase(BEGIN_CONTEXTUAL_HELP)) {
					result += treat(content, escapeHTML);
					content = "";
					lookingFor = END;
				}
			} else {
				if (token.equalsIgnoreCase(END_CONTEXTUAL_HELP)) {
					if (cParams != null && cParams.length > i
							&& cParams[i] != null) {
						ContextualHelpProvider p = cParams[i++];
						p.setLinkContent(content);
						result += p.getHTML(escapeHTML);
					} else {
						result += treat(content, escapeHTML);
					}
					content = "";
					lookingFor = BEGIN;
				}
			}
			matchTo = tokenMatcher.end();
		}
		if (matchTo < msg.length()) {
			content += msg.substring(matchTo);
		}
		result += treat(content, escapeHTML);
		return result;
	}

	private static String treat(String content, boolean escapeHTML) {
		if (escapeHTML)
			return Utils.escapeXml(content);
		else
			return content;
	}
}
