/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.help;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.hp.it.spf.xa.misc.Utils;
import com.hp.it.spf.xa.help.HelpProvider;

/**
 * <p>
 * An abstract base utility class for help in both the portal and portlet
 * arenas.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public abstract class HelpUtility {

	/**
	 * The token to search for - for example, <code>Contextual_Help</code> for
	 * the contextual-help token. This must match the token used in the message
	 * string, and is defined by the subclass.
	 */
	protected String token = null;

	private static short BEGIN = 1;
	private static short END = 2;

	/**
	 * <p>
	 * Parses the given message string for the given special help tokens, and
	 * replaces them with the particular help HTML per the given HelpProviders.
	 * Help tokens (such as
	 * <code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code> in both
	 * portal and portlet frameworks, and
	 * <code>&lt;Global_Help&gt;...&lt;/Global_Help&gt;</code> in the portal
	 * framework) may not be nested, but multiple may be contained within the
	 * message string. Each one that is found in the message string is stripped
	 * of the special tokens, and mated-up with the same ordinal HelpProvider in
	 * the given array. The content enclosed by the tokens is taken as the link
	 * content for the help hyperlink; any other parameters are assumed to
	 * already have been set in the HelpProvider.
	 * </p>
	 * <p>
	 * Any help tokens which are out of proper sequence or which lack a
	 * corresponding HelpProvider are simply stripped from the message string
	 * and ignored. Similarly, any HelpProviders in the array which lack a
	 * corresponding token in the string are ignored.
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
	 * @param hParams
	 *            An array of HelpProviders corresponding to the tokens in the
	 *            message string.
	 * @param escapeHTML
	 *            Whether to convert HTML special characters into character
	 *            entities.
	 * @return The given message string, interpolated with all applicable help
	 *         markup.
	 */
	protected String parseHelp(String msg,
			HelpProvider[] hParams, boolean escapeHTML) {

		if (msg == null || token == null) {
			return null;
		}
		String beginToken = "<" + token + ">";
		String endToken = "</" + token + ">";
		String regex = "</?" + token + ">";
		Pattern tokenPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
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
				if (token.equalsIgnoreCase(beginToken)) {
					result += treat(content, escapeHTML);
					content = "";
					lookingFor = END;
				}
			} else {
				if (token.equalsIgnoreCase(endToken)) {
					if (hParams != null && hParams.length > i
							&& hParams[i] != null) {
						HelpProvider p = hParams[i++];
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
