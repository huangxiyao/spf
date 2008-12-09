/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 * A base class for reading a file of text (eg an HTML file), substituting
 * dynamic values for various tokens, and returning the substituted
 * content to the calling class.
 */
package com.hp.it.spf.xa.interpolate;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * <p>
 * An abstract base class for performing file interpolation (reading and
 * substitution) in portal and portlet frameworks. See the portal and portlet
 * concrete subclasses for more information. This class uses the TokenParser
 * heavily to do its work.
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.cas.spf.portal.common.utils.FileInterpolator
 *      com.hp.it.cas.spf.portlet.common.utils.FileInterpolator
 *      com.hp.it.spf.xa.common.util.TokenParser
 * 
 */
public abstract class FileInterpolator {

	/**
	 * Block size for reading file
	 */
	private int bufferSize = 4096;

	/**
	 * File path without localization
	 */
	protected String baseContentFilePath = null;

	/**
	 * Detailed parser for special token
	 */
	protected TokenParser t = null;

	/**
	 * Construction function
	 */
	public FileInterpolator() {
	}

	/**
	 * Get the file path, already localized to the best-fitting file for the
	 * locale in the current request. Different action by portal and portlet, so
	 * therefore this is an abstract method.
	 * 
	 * @return file path
	 */
	protected abstract String getLocalizedContentFilePath();

	/**
	 * Get the ISO 639-1 language code for the user, such as zh. Different
	 * action by portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return ISO 639-1 language code
	 */
	protected abstract String getLanguageCode();

	/**
	 * Get the RFC 3066 language tag for the user, such as zh-CN. Different
	 * action by portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return RFC 3066 language tag
	 */
	protected abstract String getLanguageTag();

	/**
	 * Get the email address for the user. Different action by portal and
	 * portlet, so therefore this is an abstract method.
	 * 
	 * @return email
	 */
	protected abstract String getEmail();

	/**
	 * Get the portal site name for the current portal site. Different action by
	 * portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return site name
	 */
	/* Added by CK for 1000790073 */
	protected abstract String getSite();

	/**
	 * Log a string to the error log. Different action by portal and portlet, so
	 * therefore this is an abstract method.
	 * 
	 * @param msg
	 *            string to log
	 */
	protected abstract void logError(String msg);

	/**
	 * <p>
	 * Gets the localized text file pathname, reads it into a string, and
	 * substitutes the tokens found in the string with the proper dynamic
	 * values. The final string is returned. Returns null if there was a problem
	 * with the interpolation (eg the file was not found, or the base content
	 * file path provided earlier was null).
	 * </p>
	 * 
	 * @return String the file content (null if file was not found or was empty)
	 * @throws Exception
	 *             exception during parsing
	 */
	public String interpolate() throws Exception {

		if (this.baseContentFilePath == null) {
			return null;
		}
		// Get localized file path
		String filePath = getLocalizedContentFilePath();
		if (filePath == null) {
			logError("Text file is not found: " + filePath);
			return null;
		}

		// Read all file content from HTML file
		String content = getContent(filePath);
		if (content == null) {
			logError("Text file is not found or empty: " + filePath);
			return null;
		}

		// Start parsing and substituting the tokens:
		//
		// Added by CK for 1000790073 for parsing token
		content = t.parseToken(content);

		// Add current ISO language code
		content = t.parseLanguageCode(content, getLanguageCode());

		// Add current RFC language code
		content = t.parseLanguageTag(content, getLanguageTag());

		// Transfer tag to localized URL.
		content = t.parseNoLocalizedContentURL(content);

		// Transfer tag to unlocalized URL
		content = t.parseLocalizedContentURL(content);

		// Add current user email
		content = t.parseEmail(content, getEmail());

		// Add current site name
		content = t.parseSite(content, getSite());
		return content;
	}

	/**
	 * Gets the file content.
	 * 
	 * @param filePath
	 *            the file path
	 * @return String the file content, normally not empty
	 */
	private String getContent(String filePath) {

		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fi = new FileInputStream(filePath);
			InputStreamReader is = new InputStreamReader(fi, "utf-8");
			char[] ch = new char[bufferSize];
			int len = 0;
			while ((len = is.read(ch)) != -1) {
				sb.append(ch, 0, len);
			}
			fi.close();
			is.close();
		} catch (IOException e) {
			return null;
		}
		return sb.toString();
	}
}
