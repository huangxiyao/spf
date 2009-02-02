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
 * @see {@link TokenParser}<br>
 *      {@link com.hp.it.spf.xa.interpolate.portal.FileInterpolator}<br>
 *      {@link com.hp.it.spf.xa.interpolate.portlet.FileInterpolator}
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
	 * Get the input stream for the file to interpolate, already localized to
	 * the best-fitting file for the locale in the current request. Different
	 * action by portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return input stream for the localized file to interpolate
	 */
	protected abstract InputStream getLocalizedContentFileAsStream();

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
	protected abstract String getSite();

	/**
	 * Get the portal site root (ie home page) URL for the current portal site.
	 * Different action by portal and portlet, so therefore this is an abstract
	 * method.
	 * 
	 * @return site URL string
	 */
	protected abstract String getSiteURL();

	/**
	 * Get the current portal request URL. Different action by portal and
	 * portlet, so therefore this is an abstract method.
	 * 
	 * @return request URL string
	 */
	protected abstract String getRequestURL();

	/**
	 * Get the authorization groups for the current request. Different action by
	 * portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return array of groups
	 */
	protected abstract String[] getGroups();

	/**
	 * Return true if the user is logged-in and false otherwise. Different
	 * action by portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return array of groups
	 */
	protected abstract boolean getLoginStatus();

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
	 * <dt><code>{TOKEN:<i>key</i>}</code></dt>
	 * <dd>This token is parsed first, and the substituted content is added to
	 * the string. So subsequent substitutions operate against the value for the
	 * <i>key</i> - therefore that value may itself contain other tokens.
	 * <dt><code>{SITE}</code></dt>
	 * <dt><code>{SITE-URL}</code></dt>
	 * <dt><code>{REQUEST-URL}</code></dt>
	 * <dt><code>{LANGUAGE-CODE}</code></dt>
	 * <dt><code>{COUNTRY-CODE}</code></dt>
	 * <dt><code>{LANGUAGE-TAG}</code></dt>
	 * <dt><code>{EMAIL}</code></dt>
	 * <dt><code>{NAME}</code></dt>
	 * <dt><code>{USER-PROPERTY:<i>key</i>}</code></dt>
	 * <dt><code>{SITE:<i>names</i>}...{/SITE}</code></dt>
	 * <dt><code>{LOGGED-IN}...{/LOGGED-IN}</code></dt>
	 * <dt><code>{LOGGED-OUT}...{/LOGGED-OUT}</code></dt>
	 * <dt><code>{GROUP:<i>groups</i>}...{/GROUP}</code></dt>
	 * </dl>
	 * </p>
	 * <p>
	 * <b>Note:</b> The <code>&lt;</code> and <code>&gt;</code> symbols are
	 * also supported for the token boundaries, in place of <code>{</code> and
	 * <code>}</code>.
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

		// Start parsing and substituting the tokens:
		content = t.parseToken(content);

		// Add current site name
		content = t.parseSite(content, getSite());

		// Add current site URL
		content = t.parseSiteURL(content, getSiteURL());

		// Add current request URL
		content = t.parseRequestURL(content, getRequestURL());

		// Add current ISO language code
		content = t.parseLanguageCode(content, getLocale());

		// Add current ISO country code
		content = t.parseCountryCode(content, getLocale());
		
		// Add current RFC language code
		content = t.parseLanguageTag(content, getLocale());

		// Add current user email
		content = t.parseEmail(content, getEmail());

		// Add current user display name
		content = t.parseName(content, getFirstName(), getLastName(),
				getLocale());

		// Add other property values for current user
		content = t.parseUserProperty(content);

		// Transfer tag to localized URL.
		content = t.parseNoLocalizedContentURL(content);

		// Transfer tag to unlocalized URL
		content = t.parseLocalizedContentURL(content);

		// Parse site sections
		content = t.parseSiteContainer(content, getSite());

		// Parse login/logout sections
		content = t.parseLoggedInContainer(content, getLoginStatus());
		content = t.parseLoggedOutContainer(content, getLoginStatus());

		// Parse group sections
		content = t.parseGroupContainer(content, getGroups());

		return content;
	}

	/**
	 * Gets the file content.
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
