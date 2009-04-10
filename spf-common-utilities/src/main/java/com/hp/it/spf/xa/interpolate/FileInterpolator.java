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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Locale;

/**
 * <p>
 * An abstract base class for performing file interpolation (reading and
 * substitution) in portal and portlet frameworks. See the portal and portlet
 * concrete subclasses for more information. This class uses the base
 * {@link TokenParser} heavily to do its work.
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.interpolate.TokenParser</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.portal.FileInterpolator</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.portlet.FileInterpolator</code>
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
	 * The locale to use instead of the one in the current request
	 */
	protected Locale locale = null;

	/**
	 * Token parser for parsing the content string
	 */
	protected TokenParser parser = null;

	public FileInterpolator() {
	}

	/**
	 * Get the input stream for the file to interpolate, already localized to
	 * the best-fitting file for the locale in the current request. Different
	 * action by portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return input stream for the localized file to interpolate
	 */
	protected abstract InputStream getLocalizedContentFileAsStream();

	/**
	 * Log a warning string to the log file. Different action by portal and
	 * portlet, so therefore this is an abstract method.
	 * 
	 * @param msg
	 *            string to log
	 */
	protected abstract void logWarning(String msg);

	/**
	 * <p>
	 * Gets the localized text file as an input stream (using
	 * {@link #getLocalizedContentFileAsStream()}), reads it into a string, and
	 * substitutes the tokens found in the string with the proper dynamic values
	 * (using {@link TokenParser#parse(String)}. The final string is returned.
	 * Returns null and logs a warning if there was a problem with the
	 * interpolation (eg the file was not found, or the base content file path
	 * provided earlier was null).
	 * </p>
	 * <p>
	 * For a list and description of all the supported tokens, and their order
	 * of evaluation, see the concrete subclass documentation. Also see the
	 * documentation for {@link TokenParser#parse(String)} and the other
	 * {@link TokenParser} methods it uses.
	 * </p>
	 * 
	 * @return String the file content (null if file was not found or was empty)
	 * @throws Exception
	 *             exception during parsing
	 */
	public String interpolate() throws Exception {

		if (this.baseContentFilePath == null) {
			logWarning("Base file path was null.");
			return null;
		}
		
		// Get localized file input stream
		InputStream fileStream = getLocalizedContentFileAsStream();
		if (fileStream == null) {
			logWarning("Localized text file is not found for base file: "
					+ this.baseContentFilePath);
			return null;
		}

		// Read all file content from HTML file
		String content = getContent(fileStream);
		if (content == null) {
			logWarning("Localized text file is empty or unreadable for base file: "
					+ this.baseContentFilePath);
			return null;
		}

		// Parse all the tokens in the file content and return the results.
		content = parser.parse(content);
		return content;
	}

	/**
	 * Gets the file content, which is expected to be UTF-8 encoded.
	 * 
	 * @param fileStream
	 *            the input stream
	 * @return String the file content, normally not empty
	 */
	private String getContent(InputStream fileStream) {

		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader is = new InputStreamReader(fileStream, "utf-8");
			char[] ch = new char[bufferSize];
			int len = 0;
			while ((len = is.read(ch)) != -1) {
				sb.append(ch, 0, len);
			}
			is.close();
		} catch (IOException e) {
			return null;
		}
		return sb.toString();
	}

}
