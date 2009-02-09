/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.misc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;

/**
 * A base class for miscellaneous utility methods for both portal and portlet
 * frameworks.
 * 
 * @author <link href="wei.teng@hp.com">Teng Wei</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.misc.portal.Utils com.hp.it.spf.xa.misc.portlet.Utils
 */
public class Utils {
	/**
	 * Private constructor to prevent instantiation.
	 */
	protected Utils() {

	}

	private static final int BUFFER_LENGTH = 50;

	/**
	 * <p>
	 * Convert any XML special characters in the given string, into their XML
	 * character entities. The resulting string is safe to embed in surrounding
	 * XML/HTML content.
	 * </p>
	 * 
	 * @param value
	 *            A string.
	 * @return The escaped/converted string.
	 */
	public static String escapeXml(final String value) {
		if (value == null) {
			return null;
		}
		char[] content = value.toCharArray();
		StringBuffer result = new StringBuffer(content.length + BUFFER_LENGTH);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&#39;");
				break;
			default:
				result.append(content[i]);
				break;
			}
		}
		return result.toString();
	}

	/**
	 * <p>
	 * Decode a base-64 encoded string of UTF-8 bytecodes, returning the decoded
	 * value. Note that the input string is assumed to base-64 encode a series
	 * of UTF-8 bytes; if this is not the case, the returned string will be
	 * corrupt.
	 * </p>
	 * 
	 * @param in
	 *            A base64-encoded string.
	 * @return The decoded string.
	 */
	public static String decodeBase64(String base64Codes) {
		return decodeBase64(base64Codes, "UTF-8");
	}

	/**
	 * <p>
	 * Decode a base-64 encoded string of bytecodes using the given encoding,
	 * returning the decoded value. Returns the given string if the given
	 * encoding is not supported, or there is a problem decoding the string.
	 * </p>
	 * 
	 * @param in
	 *            A base64-encoded string.
	 * @param enc
	 *            The character encoding that was used for the encoded bytes. If
	 *            null, the system default encoding is assumed.
	 * @return The decoded string.
	 */
	public static String decodeBase64(String base64Codes, String enc) {
		if (base64Codes == null) {
			return null;
		}
		try {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
					ASCIIUtility.getBytes(base64Codes));
			InputStream base64Stream = new BASE64DecoderStream(
					bytearrayinputstream);

			int k = bytearrayinputstream.available();
			byte abyte0[] = new byte[k];
			k = base64Stream.read(abyte0, 0, k);
			if (enc == null) {
				return new String(abyte0, 0, k);
			} else {
				return new String(abyte0, 0, k, enc);
			}
		} catch (UnsupportedEncodingException e) {
			return base64Codes;
		} catch (IOException e) {
			return base64Codes;
		}
	}

	/**
	 * <p>
	 * Get an input stream for a file; this uses the class loader to search for
	 * the given file anywhere in the current classpath. If the file cannot be
	 * found in the classpath, then null is returned. Also returns null if the
	 * given filename is null
	 * </p>
	 * 
	 * @param filename
	 *            The file name.
	 * @return The corresponding input stream (null if not found).
	 */
	public static InputStream getResourceAsStream(String resource) {
		if (resource == null) {
			return null;
		}
		resource = resource.trim();
		String stripped = resource;
		stripped.replaceAll("/+", "/");
		if (stripped.equals("") || stripped.equals("/")) {
			return null;
		}
		if (stripped.startsWith("/")) {
			stripped = stripped.substring(1);
		}
		InputStream stream = null;
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			if (classLoader != null) {
				stream = classLoader.getResourceAsStream(stripped);
			}
			if (stream == null) {
				stream = Utils.class.getResourceAsStream(resource);
			}
			if (stream == null) {
				stream = Utils.class.getClassLoader().getResourceAsStream(
						stripped);
			}
		} catch (Exception e) {
			return null;
		}
		return stream;
	}

	/**
	 * <p>
	 * Returns the given path, with any consecutive file separators (<code>/</code>
	 * for Java) reduced to just one. The given path is also trimmed of
	 * whitespace. URL delimiters (ie first <code>://</code> found in the
	 * string) are exempt from this.
	 * </p>
	 * 
	 * @param pPath
	 *            The file path to clean-up.
	 * @return The cleaned-up file path.
	 */
	public static String slashify(String pPath) {
		if (pPath == null) {
			return null;
		}
		String p = pPath.trim();
		String b = "";
		int i = p.indexOf("://");
		if (i != -1) {
			b = p.substring(0, i + 2);
			p = p.substring(i + 2);
		}
		return (b + p.replaceAll("/+", "/"));
	}

	/**
	 * <p>
	 * This method returns the given portal URL, modified to refer to the portal
	 * site name and path provided in the given URI. The given portal URL is
	 * expected to be of the standard format for the Vignette portal:
	 * <code>/portal/site/<i>&lt;site-name&gt;</i></code> and may be
	 * followed by additional path and query string; this URL may be a relative
	 * URL as shown here, or an absolute URL. In any case, the returned URL is
	 * as follows:
	 * </p>
	 * <ul>
	 * <li> if the given URI starts with <code>/</code> then the returned URL
	 * is for the same portal site as in the given portal URL, but any
	 * additional path/query is replaced with the given URL.</li>
	 * <li> otherwise the first part of the given URI (up to the first
	 * <code>/</code>) is used as the replacement site name (ie the Vignette
	 * "site DNS name") in the returned URL, and the remainder of the given URI
	 * is used as additional path/query for it</li>
	 * </ul>
	 * <p>
	 * For example, say that the given portal URL is
	 * <code>/portal/site/abc/template.PAGE/?something=...</code>. Then:
	 * </p>
	 * <ul>
	 * <li> when the given URI is null, the returned URL is
	 * <code>/portal/site/abc/</code></li>
	 * <li> when the given URI is <code>/template.ABC</code>, the returned
	 * URL is <code>/portal/site/abc/template.ABC</code></li>
	 * <li> when the given URI is <code>xyz</code>, the returned URL is
	 * <code>/portal/site/xyz/</code></li>
	 * <li> when the given URI is <code>xyz/template.ABC</code>, the returned
	 * URL is <code>http://host.hp.com/portal/site/xyz/template.ABC</code></li>
	 * </ul>
	 * <p>
	 * This method just returns the given URI if given a null portal URL.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method does not check if the given URI actually exists /
	 * is valid in the portal; it just makes a URL of the proper format for it.
	 * </p>
	 * 
	 * @param portalURL
	 *            An absolute or server-relative portal URL string.
	 * @param uri
	 *            The site name (ie "site DNS name") and/or additional path (eg
	 *            a friendly URI or template friendly ID). (The part before the
	 *            first <code>/</code> is considered the site name.)
	 * @return The modified portal URL.
	 */
	public static String getPortalSiteURL(String portalURL, String uri) {
		// TODO: Change this class to use PortalURL API's instead, after fixing
		// those API's to deal with null parameters.
		String siteURL = uri;
		if (portalURL != null) {
			int j = portalURL.indexOf("/site/");
			if (j == -1) {
				siteURL = portalURL.trim(); // should never happen
			} else {
				String siteDNS = "";
				String path = "";
				if (uri != null) {
					int i = uri.indexOf('/');
					if (i == -1) {
						siteDNS = uri;
					} else {
						siteDNS = uri.substring(0, i);
						path = uri.substring(i);
					}
				}
				if (siteDNS.equals("")) {
					if ((j + 6) < portalURL.length()) {
						int k = portalURL.indexOf("/", j + 6);
						siteDNS = portalURL.substring(j + 6, k);
					} else {
						siteDNS = ""; // should never happen
					}
				}
				siteURL = portalURL.substring(0, j).trim() + "/site/"
						+ siteDNS.trim() + "/" + path.trim();
			}
		}
		siteURL = slashify(siteURL);
		return (siteURL);
	}

	/**
	 * Use {@link #getPortalSiteURL(String,String)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(String siteURL, String uri) {
		return getPortalSiteURL(siteURL, uri);
	}

	/**
	 * Returns true if the given group matches one in the given array of groups.
	 * Group matching is defined to be case-insensitive and disregards leading
	 * or trailing whitespace, nulls, or empty strings.
	 * 
	 * @param groups
	 *            An array of group names.
	 * @param group
	 *            The group name for which to search.
	 * @return True if the given group matches one in the given array.
	 */
	public static boolean groupMatch(String[] groups, String group) {
		if (groups != null && group != null) {
			group = group.trim();
			if (!group.equals("")) {
				for (int i = 0; i < groups.length; i++) {
					if (groups[i] != null) {
						if (groups[i].trim().equalsIgnoreCase(group)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}