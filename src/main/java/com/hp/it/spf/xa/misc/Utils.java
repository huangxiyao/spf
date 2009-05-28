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
import com.hp.it.spf.xa.portalurl.PortalURLFactory;

/**
 * A base class for miscellaneous utility methods for both portal and portlet
 * frameworks. Portlets and portal components should import the respective
 * subclasses, not this one.
 * 
 * @author <link href="wei.teng@hp.com">Teng Wei</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.misc.portal.Utils</code><br>
 *      <code>com.hp.it.spf.xa.misc.portlet.Utils</code>
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
	 * This method returns the given portal URL, adjusted with the remaining
	 * parameters: security scheme, hostname, port number, and site-relative
	 * URI. The parameters are all optional; if any are set to null or blank (in
	 * the case of the port number, this is any non-positive number), then that
	 * element in the returned URL remains the same as it was in the given URL.
	 * </p>
	 * <p>
	 * The given portal URL is expected to be of the standard format for the
	 * Vignette portal: either a relative URL like
	 * <code>/portal/site/<i>&lt;site-name&gt;</i></code> or an absolute URL
	 * like
	 * <code><i>&lt;scheme&gt;</i>://<i>&lt;host&gt;</i>[:<i>&lt;port&gt;</i>]/portal/site/<i>&lt;site-name&gt;</i></code>.
	 * In either case, it may be followed by additional path and query string.
	 * </p>
	 * <p>
	 * The path in the returned URL is as follows:
	 * </p>
	 * <ul>
	 * <li> if the given site-relative URI starts with <code>/</code> then the
	 * returned URL is for the same portal site as in the given portal URL, but
	 * any additional path/query is replaced with the given URL.</li>
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
	 * <code>/portal/site/abc/template.PAGE/?something=...</code></li>
	 * <li> when the given URI is <code>/template.ABC</code>, the returned
	 * URL is <code>/portal/site/abc/template.ABC</code></li>
	 * <li> when the given URI is <code>xyz</code>, the returned URL is
	 * <code>/portal/site/xyz/</code></li>
	 * <li> when the given URI is <code>xyz/template.ABC</code>, the returned
	 * URL is <code>http://host.hp.com/portal/site/xyz/template.ABC</code></li>
	 * </ul>
	 * <p>
	 * As another example, say that the given portal URL is
	 * <code>http://host.hp.com/portal/site/abc/template.PAGE/?something=...</code>.
	 * In these examples, the given URI is null (or blank). Then:
	 * </p>
	 * <ul>
	 * <li> when the given security scheme is true, the returned URL is
	 * <code>https://host.hp.com/portal/site/abc/template.PAGE/?something=...</code></li>
	 * <li> when the given security scheme is true and the port is 8002, the
	 * returned URL is
	 * <code>https://host.hp.com:8002/portal/site/abc/template.PAGE/?something=...</code></li>
	 * <li> when the given host is <code>another.com</code>, the returned URL
	 * is
	 * <code>http://another.com/portal/site/abc/template.PAGE/?something=...</code></li>
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
	 * @param secure
	 *            If true, force use of <code>https</code>; if false, force
	 *            use of <code>http</code>. If null, use the scheme already
	 *            in the URL.
	 * @param hostname
	 *            The hostname to use (if null, use the hostname already in the
	 *            URL).
	 * @param port
	 *            The port to use (an integer; if non-positive, use the port
	 *            already in the URL).
	 * @param uri
	 *            A site-relative URI, comprised of the site name (ie "site DNS
	 *            name") and/or additional path (eg a friendly URI or template
	 *            friendly ID). (The part before the first <code>/</code> is
	 *            considered the site name.)
	 * @return The modified portal URL.
	 */
	public static String getPortalSiteURL(String portalURL, Boolean secure,
			String host, int port, String uri) {
		// TODO: Change this class to use PortalURL API's instead, after fixing
		// those API's to deal with null parameters.
		String siteURL = uri;
		String scheme;
		if (portalURL != null) {
			siteURL = portalURL;

			// first substitute scheme with given value
			if (secure != null) {
				int i = siteURL.indexOf("://");
				if (i != -1) {
					String currentScheme = siteURL.substring(0, i).trim();
					if (secure.booleanValue()) {
						scheme = "https";
						// if not given port, and URL not already using HTTPS,
						// default port to 443 (this removes port from URL)
						if ((port <= 0)
								&& !currentScheme.equalsIgnoreCase("https")) {
							port = PortalURLFactory.getNonstandardHttpsPort();
							if (port <= 0)
								port = 443;
						}
					} else {
						scheme = "http";
						// if not given port, and URL not already using HTTP,
						// default to 80 (this removes port from URL)
						if ((port <= 0)
								&& !currentScheme.equalsIgnoreCase("http")) {
							port = PortalURLFactory.getNonstandardHttpPort();
							if (port <= 0)
								port = 80;
						}
					}
					siteURL = scheme + siteURL.substring(i);
				}
			}

			// next substitute hostname with given value
			if (host != null) {
				host = host.trim();
				if (host.length() > 0) {
					int i = siteURL.indexOf("://");
					if ((i != -1) && ((i + 3) < siteURL.length())) {
						int j = siteURL.indexOf('/', i + 3);
						int k = siteURL.indexOf(':', i + 3);
						if (j == -1) {
							if (k == -1)
								// case: scheme://host
								siteURL = siteURL.substring(0, i).trim()
										+ "://" + host;
							else
								// case: scheme://host:port
								siteURL = siteURL.substring(0, i).trim()
										+ "://" + host
										+ siteURL.substring(k).trim();
						} else {
							if ((k == -1) || (k > j))
								// case: scheme://host/...
								// case: scheme://host/...:...
								siteURL = siteURL.substring(0, i).trim()
										+ "://" + host
										+ siteURL.substring(j).trim();
							else
								// case: scheme://host:port/...
								siteURL = siteURL.substring(0, i).trim()
										+ "://" + host
										+ siteURL.substring(k).trim();
						}
					}
				}
			}

			// next substitute port with given value
			if (port > 0) {
				int i = siteURL.indexOf("://");
				if ((i != -1) && ((i + 3) < siteURL.length())) {
					String p = ":" + port;
					scheme = siteURL.substring(0, i);
					if ("http".equalsIgnoreCase(scheme) && (port == 80)) {
						// case: http URL and port 80 - remove port
						p = "";
					} else if ("https".equalsIgnoreCase(scheme)
							&& (port == 443)) {
						// case: https URL and port 443 - remove port
						p = "";
					}
					int j = siteURL.indexOf('/', i + 3);
					int k = siteURL.indexOf(':', i + 3);
					if (j == -1) {
						if (k == -1)
							// case: scheme://host
							siteURL = siteURL.trim() + p;
						else
							// case: scheme://host:port
							siteURL = siteURL.substring(0, k).trim() + p;
					} else {
						if ((k == -1) || (k > j))
							// case: scheme://host/...
							// case: scheme://host/...:...
							siteURL = siteURL.substring(0, j).trim() + p
									+ siteURL.substring(j).trim();
						else
							// case: scheme://host:port/...
							siteURL = siteURL.substring(0, k).trim() + p
									+ siteURL.substring(j).trim();
					}
				}
			}

			// finally substitute site-relative URI with given value
			if (uri != null) {
				uri = uri.trim();
				if (uri.length() > 0) {
					int i = siteURL.indexOf("/site/");
					if (i != -1) {
						String siteDNS = "";
						String path = "";
						int j = uri.indexOf('/');
						int k = uri.indexOf('?');
						int l = uri.indexOf('#');
						if ((j == -1) || (k > -1 && k < j))
							j = k;
						if ((j == -1) || (l > -1 && l < j))
							j = l;						
						if (j == -1) {
							siteDNS = uri;
						} else {
							siteDNS = uri.substring(0, j);
							path = uri.substring(j);
						}
						if (siteDNS.equals("")) {
							if ((i + 6) < siteURL.length()) {
								int m = siteURL.indexOf('/', i + 6);
								int n = siteURL.indexOf('?', i + 6);
								int o = siteURL.indexOf('#', i + 6);
								if ((m == -1) || (n > -1 && n < m))
									m = n;
								if ((m == -1) || (o > -1 && o < m))
									m = o;
								if (m == -1) {
									siteDNS = siteURL.substring(i + 6);
								} else {
									siteDNS = siteURL.substring(i + 6, m);
								}
							} else {
								siteDNS = ""; // should never happen
							}
						}
						if (!siteDNS.equals(""))
							siteURL = siteURL.substring(0, i).trim() + "/site/"
									+ siteDNS.trim() + "/" + path.trim();
					}
				}
			}
		}
		siteURL = slashify(siteURL);
		return (siteURL);
	}

	/**
	 * Use {@link #getPortalSiteURL(String,Boolean,String,int,String)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(String siteURL, Boolean secure,
			String host, int port, String uri) {
		return getPortalSiteURL(siteURL, secure, host, port, uri);
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