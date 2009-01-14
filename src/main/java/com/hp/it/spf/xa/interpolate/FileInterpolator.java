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
import java.util.Locale;

/**
 * <p>
 * An abstract base class for performing file interpolation (reading and
 * substitution) in portal and portlet frameworks. See the portal and portlet
 * concrete subclasses for more information. This class uses the base
 * TokenParser heavily to do its work.
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.portal.FileInterpolator
 *      com.hp.it.spf.xa.interpolate.portlet.FileInterpolator
 *      com.hp.it.spf.xa.interpolate.TokenParser
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
	 * Token parser for parsing the content string
	 */
	protected TokenParser t = null;

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
	 * Get the locale for the user. Different action by portal and portlet, so
	 * therefore this is an abstract method.
	 * 
	 * @return locale
	 */
	protected abstract Locale getLocale();

	/**
	 * Get the email address for the user. Different action by portal and
	 * portlet, so therefore this is an abstract method.
	 * 
	 * @return email
	 */
	protected abstract String getEmail();

	/**
	 * Get the first name for the user. Different action by portal and portlet,
	 * so therefore this is an abstract method.
	 * 
	 * @return first name
	 */
	protected abstract String getFirstName();

	/**
	 * Get the last name for the user. Different action by portal and portlet,
	 * so therefore this is an abstract method.
	 * 
	 * @return last name
	 */
	protected abstract String getLastName();

	/**
	 * Get the portal site name for the current portal site. Different action by
	 * portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return site name
	 */
	/* Added by CK for 1000790073 */
	protected abstract String getSite();

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
	 * Gets the localized text file pathname, reads it into a string, and
	 * substitutes the tokens found in the string with the proper dynamic
	 * values. The final string is returned. Returns null if there was a problem
	 * with the interpolation (eg the file was not found, or the base content
	 * file path provided earlier was null).
	 * </p>
	 * <p>
	 * For a list and description of all the supported tokens, see the concrete
	 * subclass documentation. This interpolate method is responsible for
	 * substituting the following tokens, in the following order:
	 * <dl>
	 * <dt><code>&lt;TOKEN:<i>key</i>&gt;</code></dt>
	 * <dd>This token is parsed first, and the substituted content is added to
	 * the string. So subsequent substitutions operate against the value for the
	 * <i>key</i> - therefore that value may itself contain other tokens.
	 * <dt><code>&lt;LANGUAGE-CODE&gt;</code></dt>
	 * <dt><code>&lt;LANGUAGE-TAG&gt;</code></dt>
	 * <dt><code>&lt;CONTENT-URL:</i>path</i>&gt;</code></dt>
	 * <dt><code>&lt;LOCALIZED-CONTENT-URL:<i>path</i>&gt;</code></dt>
	 * <dt><code>&lt;SITE&gt;</code></dt>
	 * <dt><code>&lt;EMAIL&gt;</code></dt>
	 * <dt><code>&lt;NAME&gt;</code></dt>
	 * <dt><code>&lt;USER-PROPERTY:<i>key</i>&gt;</code></dt>
	 * <dt><code>&lt;SITE:<i>names</i>&gt;...&lt;/SITE&gt;</code></dt>
	 * </dl>
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
			logWarning("Text file is not found: " + filePath);
			return null;
		}

		// Read all file content from HTML file
		String content = getContent(filePath);
		if (content == null) {
			logWarning("Text file is not found or empty: " + filePath);
			return null;
		}

		// Start parsing and substituting the tokens:
		//
		// Added by CK for 1000790073 for parsing token
		content = t.parseToken(content);

		// Add current ISO language code
		content = t.parseLanguageCode(content, getLocale());

		// Add current RFC language code
		content = t.parseLanguageTag(content, getLocale());

		// Transfer tag to localized URL.
		content = t.parseNoLocalizedContentURL(content);

		// Transfer tag to unlocalized URL
		content = t.parseLocalizedContentURL(content);

		// Add current site name
		content = t.parseSite(content, getSite());

		// Add current user email
		content = t.parseEmail(content, getEmail());

		// Add current user display name
		content = t.parseName(content, getFirstName(), getLastName(),
				getLocale());

		// Add other property values for current user
		content = t.parseUserProperty(content);

		// Parse site sections
		content = t.parseSiteContainer(content, getSite());

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

	/**
	 * Get the value of the given user property from the portal User object in
	 * the portal context (portal request) provided to the constructor. Returns
	 * null if this has not been set in the request (eg, when the user is not
	 * logged-in), or the portal context provided to the constructor was null.
	 * 
	 * @return email
	 */
	protected String getUserProperty(String key) {
		if (t == null) {
			return null;
		}
		return t.getUserProperty(key);
	}
}
